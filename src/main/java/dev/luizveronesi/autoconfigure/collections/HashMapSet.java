package dev.luizveronesi.autoconfigure.collections;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class extends HashMap to facilitate workings with sets inside maps.
 *
 * @author Luiz Veronesi
 */
public class HashMapSet<K, V> implements Serializable {

	private static final long serialVersionUID = -2300543371499063512L;
	
	private Map<K, Set<V>> map;
	
	public HashMapSet() {
		map = new TreeMap<>();
	}
	
	public V put(K key, V value) {
		if (map.containsKey(key)) {
			map.get(key).add(value);
		} else {
			Set<V> values = new HashSet<>();
			values.add(value);
			map.put(key, values);
		}
		
		return value;
	}
	
	public Set<V> put(K key, Set<V> values) {
		map.put(key, values);
		return values;
	}
	
	public Set<V> get(K key) {
		return this.map.get(key);
	}

	public Map<K, Set<V>> getMap() {
		return map;
	}

	public void setMap(Map<K, Set<V>> map) {
		this.map = map;
	}
}
