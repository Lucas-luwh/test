package datastructure;

import thread.ThreadPoolTest;
import util.GenerateRandomUtil;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * 非阻塞无界链表队列
 * 1.线程安全的队列
 * 2.是先进先出（FIFO）入队规则
 *
 * @author julin
 */
public class ConcurrentLinkedQueueTest {

	public static int threadCount = 10;
	public static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

	static class Offer implements Runnable {

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			//队列判空的情况下，size()会遍历整个链表
			if (queue.isEmpty()) {
				String ele = GenerateRandomUtil.generateRandomNextInt(0, Integer.MAX_VALUE) + "";
				queue.offer(ele);
				System.out.println("入队元素为:" + ele);
				long end = System.currentTimeMillis();
				long time = end - start;
				System.out.println("入队时间为: " + time);
			}
		}
	}

	static class Poll implements Runnable {

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			if (!queue.isEmpty()) {
				String ele = queue.poll();
				System.out.println("出队元素为:" + ele);
				long end = System.currentTimeMillis();
				long time = end - start;
				System.out.println("出队时间为：" + time);
			}
		}
	}

	public static void main(String[] args) {
		ExecutorService executorService = ThreadPoolTest.creatThreadPool(10, 50, 100);
		for (int x = 0; x < threadCount; x++) {
			executorService.submit(new Offer());
			executorService.submit(new Poll());
		}
		executorService.shutdown();
	}
}
