package util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成随机数工具类
 * @author julin
 */
public class GenerateRandomUtil {

	public static void main(String[] args) {

		/*第一种方法*/
		long start = System.currentTimeMillis();
		int i = generateRandomWithMath(1, 1000000000);
		System.out.println(i);
		long end = System.currentTimeMillis();
		System.out.println("N1耗时："+ (end - start));

		/*第二种方法*/
		long start1 = System.currentTimeMillis();
		int j = generateRandomNextInt(1, 1000000000);
		System.out.println(j);
		long end1 = System.currentTimeMillis();
		System.out.println("N2耗时："+ (end1 - start1));

		/*第三种方法*/
		long start2 = System.currentTimeMillis();
		generateRandomIntStream(1, 1000000000,1);
		long end2 = System.currentTimeMillis();
		System.out.println("N3.1耗时："+ (end2 - start2));

		long start4 = System.currentTimeMillis();
		generateRandomIntStream(1, 1000000000,2);
		long end4 = System.currentTimeMillis();
		System.out.println("N3.2耗时："+ (end4- start4));

		/*第四种方法*/
		long start3 = System.currentTimeMillis();
		int k = generateRandomWithThreadLocal(1, 1000000000);
		System.out.println(k);
		long end3 = System.currentTimeMillis();
		System.out.println("N4耗时："+ (end3 - start3));

		/*第五种方法*/
		long start5 = System.currentTimeMillis();
		int l = generateRandomWithSecureRandom(1, 1000000000);
		System.out.println(l);
		long end5 = System.currentTimeMillis();
		System.out.println("N5耗时："+ (end5 - start5));

		/*第六种方法*/
		long start6 = System.currentTimeMillis();
		int m = generateRandomWithSplittableRandom(1, 1000000000);
		System.out.println(m);
		long end6 = System.currentTimeMillis();
		System.out.println("N6耗时："+ (end6 - start6));
	}

	/**
	 * java.lang.Math
	 * @param min 最小值
	 * @param max 最大值
	 * @return 返回最小值和最大值之间的一个数
	 */
	public static int generateRandomWithMath(int min, int max){
		return (int) ((Math.random() * (max - min)) + min);
	}

	public static int generateRandomNextInt(int min, int max){
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}

	public static void generateRandomIntStream(int min, int max, int type){
		Random random = new Random();
		if(type == 1) {
			random.ints(1, min, max).forEach(System.out::println);
		}else{
			ThreadLocalRandom.current().ints(1, min, max).forEach(System.out::println);
		}
	}

	/**
	 * 在多线程方面：ThreadLocalRandom比random表现要好
	 * @param min 最小值
	 * @param max 最大值
	 * @return 随机数
	 */
	public static int generateRandomWithThreadLocal(int min, int max){
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	/**
	 *  此方法会强加密，对安全有保障
	 * @param min 最小值
	 * @param max 最大值
	 * @return 指定范围生成随机数
	 */
	public static int generateRandomWithSecureRandom(int min, int max){
		SecureRandom secureRandom = new SecureRandom();
		return secureRandom.nextInt(max - min) + min;
	}

	public static int generateRandomWithSplittableRandom(int min, int max){
		SplittableRandom splittableRandom = new SplittableRandom();
		return splittableRandom.nextInt(min, max);
	}
}
