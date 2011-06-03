package dk.brics.tajs.util;

import java.util.*;

import dk.brics.tajs.options.Options;

/**
 * Miscellaneous collection construction methods.
 * If {@link Options#isTestEnabled()} is enabled, the methods return collections with predictable iteration order.
 */
public class Collections {
	
	private Collections() {}
	
	/**
	 * Constructs a new empty map.
	 * @param <T1> key type
	 * @param <T2> value type
	 */
	public static <T1,T2> Map<T1,T2> newMap() {
		if (Options.isTestEnabled()) 
			return new LinkedHashMap<T1,T2>();
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashMap<T1,T2>();
		else
			return new HashMap<T1,T2>(8);
	}

	/**
	 * Constructs a new map as a copy of the given map.
	 * @param <T1> key type
	 * @param <T2> value type
	 */
	public static <T1,T2> Map<T1,T2> newMap(Map<T1,T2> m) {
		if (Options.isTestEnabled()) 
			return new LinkedHashMap<T1,T2>(m);
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashMap<T1,T2>(m);
		else 
			return new HashMap<T1,T2>(m);
	}

	/**
	 * Constructs a new empty set.
	 * @param <T> value type
	 */
	public static <T> Set<T> newSet() {
		if (Options.isTestEnabled()) 
			return new LinkedHashSet<T>();
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashSet<T>();
		else
			return new HashSet<T>(8);
	}
	
	/**
	 * Constructs a new set from the given collection.
	 * @param <T> value type
	 */
	public static <T> Set<T> newSet(Collection<T> s) {
		if (Options.isTestEnabled()) 
			return new LinkedHashSet<T>(s);
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashSet<T>(s);
		else
			return new HashSet<T>(s);
	}
	
	/**
	 * Constructs a new empty list.
	 * @param <T> value type
	 */
	public static <T> List<T> newList() {
		return new ArrayList<T>();
	}
	
	/**
	 * Constructs a new list from the given collection.
	 * @param <T> value type
	 */
	public static <T> List<T> newList(Collection<T> s) {
		return new ArrayList<T>(s);
	}

    /**
     * Constructs a new empty queue.
     * @param <T> value type
     */
    public static <T> Queue<T> newQueue() {
        return new LinkedList<T>();
    }

    /**
     * Returns a singleton collection.
     */
    public static <T> Collection<T> newSingleton(T obj) {
        Collection<T> result = new ArrayList<T>();
        result.add(obj);
        return result;
    } 
	
	/**
	 * Returns a string description of the differences between the two maps.
	 * Only the first 10 differences are reported.
	 */
	public static <K,V> String diff(Map<K,V> m1, Map<K,V> m2) {
		int count = 0;
		final int MAX = 10;
		StringBuilder b = new StringBuilder();
		for (Map.Entry<K,V> me : m1.entrySet()) {
			V v = m2.get(me.getKey());
			if (v == null) {
				b.append("Only in first map: ").append(me.getKey()).append("\n");
				if (count++ >= MAX)
					break;
			} else if (!v.equals(me.getValue())) {
				b.append("Values differ for ").append(me.getKey()).append(": ").append(me.getValue()).append(" vs. ").append(v).append("\n");
				if (count++ >= MAX)
					break;
			}
		}
		if (count < MAX)
			for (Map.Entry<K,V> me : m2.entrySet()) {
				V v = m1.get(me.getKey());
				if (v == null) {
					b.append("Only in second map: ").append(me.getKey()).append("\n");
					if (count++ >= MAX)
						break;
				}
			}
		return b.toString();
	}
	
	/**
	 * Returns an ordered set of map entries, sorted by the natural order of the entry keys.
	 */
	public static <K extends Comparable<K>,V>TreeSet<Map.Entry<K,V>> sortedEntries(Map<K,V> m) {
		TreeSet<Map.Entry<K,V>> s = new TreeSet<Map.Entry<K,V>>(new MapEntryComparator<K,V>());
		s.addAll(m.entrySet());
		return s;
	}
}
