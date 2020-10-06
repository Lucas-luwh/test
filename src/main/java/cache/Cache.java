package cache;

import thread.ThreadPoolTest;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 缓存
 *
 * @author julin
 */
public class Cache<K, V> {
	private static Logger logger = Logger.getLogger(Cache.class.getName());

	private ConcurrentMap<K, V> cacheObjMap = new ConcurrentHashMap<>();
	private DelayQueue<DelayItem<Pair<K, V>>> queue = new DelayQueue<>();

	public Cache() {
		ExecutorService executorService = ThreadPoolTest.creatThreadPool(1, 2, 60000);
		executorService.execute(this::daemonCheck);
	}

	private void daemonCheck() {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("cache service started");
		}
		do {
			try {
				DelayItem<Pair<K, V>> delayItem = queue.take();
				//超时处理
				Pair<K, V> pair = delayItem.getItem();
				cacheObjMap.remove(pair.getFirst(), pair.getSecond());
			} catch (InterruptedException e) {
				if (logger.isLoggable(Level.SEVERE)) {
					logger.log(Level.SEVERE, e.getMessage(), e);
					break;
				}
			}
		} while (true);
		if (logger.isLoggable(Level.INFO)) {
			logger.info("cache service stopped");
		}
	}

	/**
	 * 添加缓存对象
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  缓存时间
	 * @param unit  并发工具类
	 */
	public void put(K key, V value, long time, TimeUnit unit) {
		V oldValue = cacheObjMap.put(key, value);
		if (null != oldValue) {
			boolean remove = queue.remove(key);
			logger.info("是否正常删除：" + remove);
		}
		long nanoTime = TimeUnit.NANOSECONDS.convert(time, unit);
		queue.put(new DelayItem<>(new Pair<>(key, value), nanoTime));
	}

	public V get(K key) {
		return cacheObjMap.get(key);
	}

	public static void main(String[] args) throws Exception {
		Cache<Integer, String> cache = new Cache<>();
		Integer key = 1;
		cache.put(key, "aaaa", 8000, TimeUnit.SECONDS);

		Thread.sleep(2000);
		String str = cache.get(1);
		logger.info("key:" + key + "取出来的值是：" + str);

		Thread.sleep(2000);
		String s = cache.get(1);
		logger.info("key:" + key + "取出来的值是：" + s);
	}
}
