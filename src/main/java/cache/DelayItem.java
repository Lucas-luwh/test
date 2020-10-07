package cache;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author julin
 */
public class DelayItem<T> implements Delayed {

	/**
	 * 虚拟机当前高精度时间值，以纳秒为单位
	 */
	private static final long NANO_ORIGIN = System.nanoTime();

	private static final AtomicLong SEQUENCER = new AtomicLong(0);
	/**
	 * 中断队列的序列号
	 */
	private final long sequenceNumber;
	/**
	 * 当前任务执行时间
	 */
	private final long time;
	private final T item;

	public DelayItem(T submit, long timeout) {
		this.time = now() + timeout;
		this.item = submit;
		this.sequenceNumber = SEQUENCER.getAndIncrement();
	}

	public T getItem() {
		return this.item;
	}

	static long now() {
		return System.nanoTime() - NANO_ORIGIN;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(time - now(), TimeUnit.NANOSECONDS);
	}

	@Override
	public int compareTo(Delayed other) {
		if (other == this) {
			return 0;
		}
		if (other instanceof DelayItem) {
			DelayItem x = (DelayItem) other;
			long diff = time - x.time;
			if (diff < 0) {
				return -1;
			} else if (diff > 0) {
				return 1;
			} else if (sequenceNumber < x.sequenceNumber) {
				return -1;
			} else {
				return 1;
			}
		}
		long l = getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS);
		if (l == 0) {
			return 0;
		}
		return (l < 0) ? -1 : 1;
	}
}
