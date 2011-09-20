package dk.brics.tajs.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Comparator for map entries using the natural order of the entry keys.
 */
public class MapEntryComparator<K extends Comparable<K>,V> implements Comparator<Map.Entry<K,V>>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Entry<K, V> e1, Entry<K, V> e2) {
		return e1.getKey().compareTo(e2.getKey());
	}
}

