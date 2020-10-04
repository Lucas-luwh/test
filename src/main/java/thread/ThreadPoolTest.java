package thread;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 线程并发库之线程池EXECUTORS
 * @author julin
 */
public class ThreadPoolTest {

	private static final Logger log = LoggerFactory.getLogger(ThreadPoolTest.class);
	private static final int SIZE = 10;

	public static void main(String[] args) {
		//无返回值
		ForkJoinPool forkJoinPool = new ForkJoinPool(4);
		MyRecursiveAction myRecursiveAction = new MyRecursiveAction(24);
		//执行任务
		forkJoinPool.invoke(myRecursiveAction);

		//有返回值
		MyRecursiveTask myRecursiveTask = new MyRecursiveTask(128);
		Long mergedResult = forkJoinPool.invoke(myRecursiveTask);
		log.info("mergedResult = {}",mergedResult);

		ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(3,new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

		for (int i= 1; i <= SIZE; i++){
			final int task = i;
			threadPool.execute(() -> {
				for (int j = 1; j <= SIZE; j++){
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						log.error("thread sleep error");
					}
					System.out.println(Thread.currentThread().getName() + " is looping of " + j + " for  task of " + task);
				}
			});
		}
		System.out.println("all of 10 tasks have committed! ");
	}

	/**
	 *
	 * @param corePoolSize 池中所保存的线程数，包括空闲线程。
	 * @param maxPoolSize 池中允许的最大线程数。
	 * @param keepAliveTime 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
	 * @return ThreadPoolExecutor
	 */
	public static ExecutorService creatThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime){
		return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), r -> new BasicThreadFactory.Builder().namingPattern("小卢-%d").daemon(true).priority(Thread.NORM_PRIORITY).build().newThread(r),new ThreadPoolExecutor.AbortPolicy());
	}

	/**
	 * jdk 1.7 任务可以分割成小任务，小任务可以合并为大任务
	 * @return ForkJoinPool
	 */
	public static ForkJoinPool createForkJoinPool(){
		return new ForkJoinPool(4);
	}
}

/**
 * RecursiveAction 将任务分割成小任务，无返回值
 * 例如：写数据到磁盘任务
 */
class MyRecursiveAction extends RecursiveAction{

	private AtomicReference<Logger> logger = new AtomicReference<>(LoggerFactory.getLogger(MyRecursiveAction.class));
	private long workLoad;

	public MyRecursiveAction(long workLoad){
		this.workLoad = workLoad;
	}

	@Override
	protected void compute() {
		//若工作量超过16门槛，分解
		int threshold = 16;
		if (this.workLoad > threshold) {
			logger.get().info("分解任务：{}", workLoad);

			ArrayList<MyRecursiveAction> subTasks = new ArrayList<>(createSubTasks());
			subTasks.forEach(ForkJoinTask::fork);
		}else{
			logger.get().info("doing workload myself : {}", this.workLoad);
		}
	}

	public List<MyRecursiveAction> createSubTasks(){
		ArrayList<MyRecursiveAction> subTasks = new ArrayList<>();
		MyRecursiveAction subtask1 = new MyRecursiveAction(this.workLoad / 2);
		MyRecursiveAction subtask2 = new MyRecursiveAction(this.workLoad / 2);

		subTasks.add(subtask1);
		subTasks.add(subtask2);
		return subTasks;
	}

}

/**
 * 有返回值结果的任务
 * 可拆分，可合并
 */
class MyRecursiveTask extends  RecursiveTask<Long> {
	/**
	 * 将日志序列化
	 */
	private final AtomicReference<Logger> logger = new AtomicReference<>(LoggerFactory.getLogger(MyRecursiveTask.class));

	private long workLoad;

	public MyRecursiveTask(long workLoad){
		this.workLoad = workLoad;
	}

	@Override
	protected Long compute() {
		int threshold = 16;
		if (this.workLoad > threshold){
			logger.get().info("Splitting workLoad:{}", this.workLoad);
			ArrayList<MyRecursiveTask> subtasks = new ArrayList<>(createSubtasks());
			subtasks.forEach(ForkJoinTask::fork);

			long result = 0;
			for (MyRecursiveTask subtask : subtasks){
				result += subtask.join();
			}
			return result;
		}else {
			logger.get().info("Doing workLoad myself:{}", this.workLoad);
			return workLoad * 3;
		}
	}

	private List<MyRecursiveTask> createSubtasks(){
		ArrayList<MyRecursiveTask> subtasks = new ArrayList<>();

		MyRecursiveTask subtask1 = new MyRecursiveTask(this.workLoad / 2);
		MyRecursiveTask subtask2 = new MyRecursiveTask(this.workLoad / 2);

		subtasks.add(subtask1);
		subtasks.add(subtask2);

		return subtasks;
	}
}
