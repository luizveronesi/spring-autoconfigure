package dev.luizveronesi.autoconfigure.collections;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class extends HashMap to facilitate workings with lists inside maps.
 *
 * @author Luiz Veronesi
 * @version 1.0 10/06/2013
 */

public class HashMapList<K, V> extends AbstractHashMapList<K, V> {
	
	private static final long serialVersionUID = 1939654229311347832L;

	public HashMapList() {
		Map<K, List<V>> map = new LinkedHashMap<>();
		internalMap(map);
	}
	
	public void reverse() {
		Map<K, List<V>> newMap = new TreeMap<>(Collections.reverseOrder());
		newMap.putAll(this.getMap());
		internalMap(newMap);
	}
	
	public String toString() {
		Map<K, List<V>> map = super.getMap();
		if (map == null) return null;
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<K, List<V>> entry : map.entrySet()) {
			for (V v : entry.getValue()) {
				sb.append("key: " + entry.getKey());
				sb.append(" ");
				sb.append("value: " + v.toString());
				sb.append(System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}
}