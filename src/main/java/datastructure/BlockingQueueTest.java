package datastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.ThreadPoolTest;
import util.GenerateRandomUtil;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 阻塞队列
 * @author julin
 */
public class BlockingQueueTest {

	private static Logger logger = LoggerFactory.getLogger(BlockingQueueTest.class);

	static long randomTime(){
		return GenerateRandomUtil.generateRandomWithMath(0, 1000);
	}

	public static void main(String[] args) {

		//线程池
		final ExecutorService threadPoolExecutor = ThreadPoolTest.creatThreadPool(1, 3, 0);
		String pathname = "C:\\Users\\julin\\Desktop";
		File root = new File(pathname);

		//读个数
		AtomicInteger rc = new AtomicInteger();

		//写个数
		AtomicInteger wc = new AtomicInteger();
		//能容纳100个文件
		LinkedBlockingQueue<File> queue = new LinkedBlockingQueue<>(100);

		//完成标志
		File exitFile = new File("");

		Runnable read = new Runnable(){

			@Override
			public void run() {
				scanFile(root);
				scanFile(exitFile);
			}
			public void scanFile(File file){
				if (file.isDirectory()){
					File[] files = file.listFiles(pathname -> pathname.isDirectory() || pathname.getPath().endsWith(".java"));
					logger.info(Arrays.toString(files));
					assert files != null;
					for (File one : files){
						scanFile(one);
					}
				}else {
					int index = rc.incrementAndGet();
					logger.info("Read0: {} {}",index,file.getPath());
					try {
						queue.put(file);
					} catch (InterruptedException e) {
						logger.error("报错异常为:{}",e.getMessage());
					}
				}
			}
		};
		//执行写线程
		threadPoolExecutor.submit(read);
		int threadSize = 4;
		for (int index = 0; index < threadSize; index++){
			final int no = index;
			Runnable write = new Runnable(){
				String threadName = "Write" + no;
				@Override
				public void run() {
					while (true){
						try {
							Thread.sleep(randomTime());
							int index = wc.incrementAndGet();
							File file = queue.take();
							//队列无对象
							if (file == exitFile){
								//添加标志，以让其他线程正常退出
								queue.put(exitFile);
								break;
							}
							logger.info("{} : {} {}",threadName, index, file.getPath());
						} catch (InterruptedException e) {
							logger.error("报错信息为：{}", e.getMessage());
						}
					}
				}
			};
			threadPoolExecutor.submit(write);
		}
		threadPoolExecutor.shutdown();
	}
}
