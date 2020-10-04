package thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.GenerateRandomUtil;

import java.util.concurrent.ExecutorService;

/**
 * ThreadLocal练习
 * @author julin
 */
public class ThreadLocalTest {

	/**
	 * 序列号
	 */
	private static int nextSerialNum = 0;
	/**
	 * 隔离对象
	 */
	static ThreadLocal<Studen> studenThreadLocal = new ThreadLocal<>();
	private static Logger logger = LoggerFactory.getLogger(ThreadLocalTest.class);

	/**
	 *为每个线程提供公共变量
	 */
	public static final ThreadLocal<Integer> SERIAL_NUM = ThreadLocal.withInitial(() -> nextSerialNum++);

	/**
	 * 线程结束时使用，释放内存
	 */
	public static void remove(){
		studenThreadLocal.remove();
	}

	public static void main(String[] args) {
		Integer a = ThreadLocalTest.SERIAL_NUM.get();
		logger.info("当前线程序列号是：{}", a);
		ExecutorService executorService = ThreadPoolTest.creatThreadPool(1, 2, 100);
		executorService.execute(() -> {
			String currentThreadName = Thread.currentThread().getName();
			logger.info( "{} is running...",currentThreadName);
			int age = GenerateRandomUtil.generateRandomNextInt(0, 100);
			logger.info("{} is set age: {}",currentThreadName,age);
			//每个线程对应一个studen对象
			Studen studen = getStudent();
			studen.setAge(age);
			logger.info("{} is first get age : {}",currentThreadName,age);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.error("线程异常：{}",e.getMessage());
			}
			logger.info("{} is second get age :{}",currentThreadName,studen.getAge());
		});

		//线程执行结束，释放内存
		ThreadLocalTest.remove();
	}


	private static Studen getStudent(){
		Studen studen = studenThreadLocal.get();
		if (null == studen){
			studen = new Studen();
			studenThreadLocal.set(studen);
		}
		return studen;
	}
}

class Studen{

	int age;
	public int getAge() {
		return age;
	}

	public void setAge(int age){
		this.age = age;
	}
}

