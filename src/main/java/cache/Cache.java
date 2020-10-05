package cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.ThreadPoolTest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author julin
 */
public class Cache<K, V> {
	private Logger logger = LoggerFactory.getLogger(Cache.class);

	private ConcurrentMap<K, V> cacheObjMap = new ConcurrentHashMap<>();
	private DelayQueue<DelayItem<Pair<K, V>>> queue = new DelayQueue<>();
	private Thread daemonThread;


	public Cache() {
		ExecutorService executorService = ThreadPoolTest.creatThreadPool(1, 2, 100);
		executorService.execute(this::daemonCheck);
	}

	private void daemonCheck() {
		if (logger.isInfoEnabled()) {
			logger.info("cache service started");
		}
		for (; ; ) {

		}
	}
}
