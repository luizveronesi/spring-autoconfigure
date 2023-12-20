package dev.luizveronesi.autoconfigure.collections;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class is a counter for the hashmap.
 *
 * @author Luiz Veronesi
 */
public class HashMapCount<K> implements Serializable {

	private static final long serialVersionUID = 2293652620228326329L;
	
	private Map<K, Integer> map;
	
	public HashMapCount() {
		map = new TreeMap<>();
	}
	
	public Integer add(K key, Integer value) {
		if (map.containsKey(key)) {
			Integer n = map.get(key);
			n = n.intValue() + value.intValue();
			map.put(key, n);
		} else {
			map.put(key, value);
		}
		
		return value;
	}

	public Integer increment(K key) {
		if (map.containsKey(key)) {
			Integer n = map.get(key);
			n = n.intValue() + 1;
			map.put(key, n);
		} else {
			map.put(key, 1);
		}
		
		return map.get(key);
	}

	public Integer get(K key) {
		return this.map.get(key);
	}
	
	public Set<K> keySet() {
		return this.getMap().keySet();
	}

	public Map<K, Integer> getMap() {
		return map;
	}

	public void setMap(Map<K, Integer> map) {
		this.map = map;
	}
	
	public Boolean containsKey(K key) {
		return this.getMap().containsKey(key);
	}
	
	public Boolean remove(K key, Integer element) {
		if (!this.getMap().containsKey(key)) return Boolean.FALSE;

		this.getMap().remove(key);
		
		return Boolean.TRUE;
	}
}
