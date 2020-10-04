package datastructure;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.ThreadPoolTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;

/**
 * 同步队列
 * @author julin
 */
public class SynchronousQueueTest {
	private static Logger logger = LoggerFactory.getLogger(SynchronousQueueTest.class);

	public static void main(String[] args) {
		final SynchronousQueue<Integer> queue = new SynchronousQueue<>();
		ExecutorService exec = ThreadPoolTest.creatThreadPool(3, 5, 10);
		exec.execute(() -> {
			logger.info("put thread start");
			try {
				queue.put(1);
			} catch (InterruptedException e) {
				logger.error("put thread error: {}",e.getMessage());
			}
			logger.info("put thread end");
		});

		exec.execute(() -> {
			logger.info("take thread start");
			try {
				logger.info("take form putThread : {}",queue.take());
			} catch (InterruptedException e) {
				logger.error("queue take error: {}",e.getMessage());
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error("thread sleep error : {}",e.getMessage());
		}
	}
}
