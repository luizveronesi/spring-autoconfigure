package dev.luizveronesi.autoconfigure.collections;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class extends LinkedHashMap to facilitate workings with lists inside maps.
 */
public class LinkedHashMapList<K, V> extends AbstractHashMapList<K, V> {

	private static final long serialVersionUID = -2372900988417379580L;

	public LinkedHashMapList() {
		Map<K, List<V>> map = new LinkedHashMap<>();
		internalMap(map);
	}
}
