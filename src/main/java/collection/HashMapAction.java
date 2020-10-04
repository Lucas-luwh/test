package collection;

import java.util.HashMap;

/**
 * @author julin
 */
public class HashMapAction {
	public static void main(String[] args) {
		HashMap<Object,Object> parmMap = new HashMap<>(16);
		parmMap.put(null,null);
		parmMap.put(null,1);
		Object o = parmMap.get(null);
		System.out.println(o);
	}
}
