package thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author julin
 */
public class ThreadInteraction {

	public static void main(String[] args) {
		final Bussiness bussiness = new Bussiness();

		//子线程
		new Thread(() -> {
			for(int i = 0; i < 3; i++){
				bussiness.subMethod();
			}
		}).start();
		//主线程
		for (int i = 0; i < 3; i++){
			bussiness.mainMethod();
		}
	}
}

class Bussiness{

	private Logger logger = LoggerFactory.getLogger(Bussiness.class);
	/**
	 * 标识
	 */
	private boolean subFlag = true;

	public synchronized void mainMethod(){
		while (subFlag){
			try {
				wait();
			} catch (InterruptedException e) {
				logger.error("报错信息为: {}", e.getMessage());
			}
		}
		//主线程循环次数
		int mainThreadCount = 5;
		for (int i = 0; i < mainThreadCount; i++){
			logger.info("{}: 主线程循环运行次数--{}", Thread.currentThread().getName(), i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("报错信息：{}", e.getMessage());
			}
		}
		subFlag = true;
		notify();
	}

	public synchronized void subMethod(){
		while (!subFlag){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < 10; i++){
			System.err.println(Thread.currentThread().getName() + "子线程循环次数--" + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		subFlag = false;
		notify();
	}
}
