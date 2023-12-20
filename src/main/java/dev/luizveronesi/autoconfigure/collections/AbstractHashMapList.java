package dev.luizveronesi.autoconfigure.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHashMapList<K, V> implements MapList<K, V>, Serializable {
	
	private static final long serialVersionUID = -2372900988417379580L;
	
	private Map<K, List<V>> map;
	
	protected void internalMap(Map<K, List<V>> map) {
		this.map = map;
	}
	
	protected Map<K, List<V>> internalMap() {
		return map;
	}

	public V put(K key, V value) {
		if (map.containsKey(key)) {
			map.get(key).add(value);
		} else {
			List<V> values = new ArrayList<>();
			values.add(value);
			map.put(key, values);
		}

		return value;
	}

	public List<V> put(K key, List<V> values) {
		map.put(key, values);
		return values;
	}

	public List<V> get(K key) {
		return this.map.get(key);
	}

	public Set<K> keySet() {
		return this.getMap().keySet();
	}

	public Map<K, List<V>> getMap() {
		return map;
	}

	public void setMap(Map<K, List<V>> map) {
		this.map = map;
	}

	public boolean containsKey(K key) {
		return this.getMap().containsKey(key);
	}

	public boolean remove(K key, V element) {
		if (!this.getMap().containsKey(key)) {
			return false;
		}

		List<V> list = this.getMap().get(key);
		list.remove(element);

		if (list.isEmpty()) {
			this.getMap().remove(key);
		}

		return true;
	}

	public V first(K key) {
		if (!this.getMap().containsKey(key)) {
			return null;
		}
		return this.getMap().get(key).get(0);
	}
}
