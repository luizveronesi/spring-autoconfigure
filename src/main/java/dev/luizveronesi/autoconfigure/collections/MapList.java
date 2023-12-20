package dev.luizveronesi.autoconfigure.collections;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MapList<K, V> {
	
	V put(K key, V value);

	List<V> put(K key, List<V> values);

	List<V> get(K key);

	Set<K> keySet();

	Map<K, List<V>> getMap();

	void setMap(Map<K, List<V>> map);

	boolean containsKey(K key);
	
	boolean remove(K key, V element);

	V first(K key);
}
