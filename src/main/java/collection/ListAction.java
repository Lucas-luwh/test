package collection;

import java.util.TreeMap;

import static java.lang.System.out;

/**
 * @author julin
 */
public class ListAction {
	public static void main(String[] args) {
		out.println(test());
		TreeMap<String,Object> treeMap = new TreeMap<>();
		treeMap.put("1","hello");
		treeMap.put("2","world");

	}
	static int test(){
		int x = 1;
		 try{
			 out.println("x="+x);
		 	return x;
		 }finally{
			 ++x;
			 out.println("x="+x);
		 }
	}

}
