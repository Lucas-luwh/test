package datastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 用队列模仿堆栈
 * @author julin
 */
public class StackTest {

	private static Logger logger = LoggerFactory.getLogger(StackTest.class);

	public static void main(String[] args) {
		//a队列
		Queue<String> a = new LinkedList<>();
		//b队列
		Queue<String> b = new LinkedList<>();
		//中间参数
		ArrayList<String> arrayList = new ArrayList<>();
		a.offer("a");
		a.offer("b");
		a.offer("c");
		a.offer("d");
		a.offer("e");
		logger.info("进栈操作：---");

		//放入中间参数
		for (String queue : a){
			arrayList.add(queue);
			logger.info(queue);
		}
		//以倒序方法取出
		for (int i = arrayList.size() - 1; i >= 0; i--){
			b.offer(arrayList.get(i));
		}
		logger.info("出栈：---");
		for (String queue : b){
			System.out.println(queue);
		}
	}
}
