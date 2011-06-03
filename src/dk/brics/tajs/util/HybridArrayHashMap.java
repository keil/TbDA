package dk.brics.tajs.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Hybrid of array and hash map.
 * Small maps are represented as arrays; above a certain threshold a HashMap is used instead. 
 */
public class HybridArrayHashMap<K,V> implements Map<K,V> { // TODO: javadoc
	
	// invariant: exactly one of array and hashmap is null
	
	private static final int DEFAULT_THRESHOLD = 8;
	
	private Entry<K,V>[] array;
	
	private int number_of_used_array_entries; // = number of non-null entries in array, if non-null 
	
	private HashMap<K,V> hashmap;
	
	@SuppressWarnings("unchecked")
	public HybridArrayHashMap(int threshold) {
		array = new Entry[threshold];
	}
	
	public HybridArrayHashMap() {
		this(DEFAULT_THRESHOLD);
	}
	
	@SuppressWarnings("unchecked")
	public HybridArrayHashMap(Map<K,V> m) {
		if (m.size() <= DEFAULT_THRESHOLD) { // note: this constructor uses the default threshold
			array = new Entry[DEFAULT_THRESHOLD];
			number_of_used_array_entries = 0;
			for (Entry<K,V> e : m.entrySet())
				array[number_of_used_array_entries++] = new ArrayEntry<K,V>(e.getKey(), e.getValue());
		} else {
			hashmap = new HashMap<K,V>();
			hashmap.putAll(m);
		}
	}
	
	@Override
	public void clear() {
		if (array != null) {
			Arrays.fill(array, null);
			number_of_used_array_entries = 0;
		} else
			hashmap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		if (array != null) {
			for (Entry<K,V> e : array) 
				if (e != null && e.getKey().equals(key))
					return true;
			return false;
		}
		return hashmap.containsKey(key); 
	}

	@Override
	public boolean containsValue(Object value) {
		if (array != null) {
			for (Entry<K,V> e : array) 
				if (e != null && e.getValue().equals(value))
					return true;
			return false;
		}
		return hashmap.containsValue(value);
	}

	@Override
	public V get(Object key) {
		if (array != null) {
			for (Entry<K,V> e : array) 
				if (e != null && e.getKey().equals(key))
					return e.getValue();
			return null;
		}
		return hashmap.get(key);
	}

	@Override
	public V put(K key, V value) {
		if (array != null) {
			int free_entry = -1;
			for (int i = 0; i < array.length; i++) {
				Entry<K,V> e = array[i];
				if (e == null)
					free_entry = i;
				else if (e.getKey().equals(key)) {
					V old = e.getValue();
					e.setValue(value);
					return old;
				}
			}
			if (free_entry != -1) {
				array[free_entry] = new ArrayEntry<K,V>(key, value);
				number_of_used_array_entries++;
				return null;
			} else
				convertArrayToHashMap();
		}
		return hashmap.put(key, value);
	}

	private void convertArrayToHashMap() {
		hashmap = new HashMap<K,V>(array.length * 2);
		for (Entry<K,V> e : array) 
			if (e != null)
				hashmap.put(e.getKey(), e.getValue());
		array = null;
		number_of_used_array_entries = 0;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		if (array != null) {
			if (number_of_used_array_entries + m.size() <= array.length) {
				loop: for (Entry<? extends K, ? extends V> f : m.entrySet()) {
					K key = f.getKey();
					V value = f.getValue();
					int free_entry = -1;
					for (int i = 0; i < array.length; i++) {
						Entry<K,V> e = array[i];
						if (e == null)
							free_entry = i;
						else if (e.getKey().equals(key)) {
							e.setValue(value);
							continue loop;
						}
					}
					array[free_entry] = new ArrayEntry<K,V>(key, value);
					number_of_used_array_entries++;
				}
				return;
			} else
				convertArrayToHashMap();
		}
		hashmap.putAll(m);
	}

	@Override
	public V remove(Object key) {
		if (array != null) { 
			for (int i = 0; i < array.length; i++) {
				Entry<K,V> e = array[i];
				if (e != null && e.getKey().equals(key)) {
					V v = e.getValue();
					array[i] = null;
					number_of_used_array_entries--;
					return v;
				}
			}
			return null;
		} 
		return hashmap.remove(key);
	}

	@Override
	public int size() {
		return array != null ? number_of_used_array_entries : hashmap.size();
	}

	@Override
	public boolean isEmpty() {
		return array != null ? number_of_used_array_entries == 0 : hashmap.isEmpty();
	}

	@Override
	public Collection<V> values() {
		if (array != null) {
			return new AbstractCollection<V>() {

				@Override
				public Iterator<V> iterator() {
					return new ArrayEntryIterator<V>() {
						@Override
						public V next() {
							return nextEntry().getValue();
						}
					};
				}

				@Override
				public int size() {
					return number_of_used_array_entries;
				}

				@Override
				public void clear() {
					HybridArrayHashMap.this.clear();
				}

				@Override
				public boolean contains(Object o) {
					for (Entry<K,V> e : array)
						if (e != null && e.getValue().equals(o))
							return true;
					return false;
				}
			};
		}
		return hashmap.values();
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		if (array != null) {
			return new AbstractSet<Entry<K, V>>() {
				
				@Override
				public Iterator<Entry<K, V>> iterator() {
					return new ArrayEntryIterator<Entry<K, V>>() {
						@Override
						public Entry<K, V> next() {
							return nextEntry();
						}
					};
				}

				@Override
				public int size() {
					return number_of_used_array_entries;
				}

				@Override
				public void clear() {
					HybridArrayHashMap.this.clear();
				}

				@Override
				public boolean contains(Object o) {
					for (Entry<K,V> e : array)
						if (e != null && e.equals(o))
							return true;
					return false;
				}

				@Override
				public boolean remove(Object o) {
					for (int i = 0; i < array.length; i++) {
						Entry<K,V> e = array[i];
						if (e != null && e.equals(o)) {
							array[i] = null;
							number_of_used_array_entries--;
							return true;
						}
					}
					return false;
				}
			};
		} 
		return hashmap.entrySet();
	}

	@Override
	public Set<K> keySet() {
		if (array != null) {
			return new AbstractSet<K>() {

				@Override
				public Iterator<K> iterator() {
					return new ArrayEntryIterator<K>() {
						@Override
						public K next() {
							return nextEntry().getKey();
						}
					};
				}

				@Override
				public int size() {
					return number_of_used_array_entries;
				}

				@Override
				public void clear() {
					HybridArrayHashMap.this.clear();
				}

				@Override
				public boolean contains(Object o) {
					for (Entry<K,V> e : array)
						if (e != null && e.getKey().equals(o))
							return true;
					return false;
				}

				@Override
				public boolean remove(Object o) {
					for (int i = 0; i < array.length; i++) {
						Entry<K,V> e = array[i];
						if (e != null && e.getKey().equals(o)) {
							array[i] = null;
							number_of_used_array_entries--;
							return true;
						}
					}
					return false;
				}
			};
		} 
		return hashmap.keySet();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Map<?,?>))
			return false;
		Map<?,?> m = (Map<?,?>)obj;
		return entrySet().equals(m.entrySet());
	}

	@Override
	public int hashCode() {
		if (array != null) {
			int h = 0;
			for (int i = 0; i < array.length; i++) {
				Entry<K,V> e = array[i];
				if (e != null)
					h += e.hashCode();
			}
			return h;
		}
		return hashmap.hashCode();
	}

	@Override
	public String toString() {
		if (array != null) {
			StringBuilder b = new StringBuilder();
			b.append('{');
			boolean first = true;
			for (int i = 0; i < array.length; i++) {
				Entry<K,V> e = array[i];
				if (e != null) {
					if (first)
						first = false;
					else
						b.append(", ");
					b.append(e.getKey()).append('=').append(e.getValue());
				}
			}
			b.append('}');
			return b.toString();
		} 
		return hashmap.toString();
	}

	private static final class ArrayEntry<K,V> implements Entry<K,V> {

		private K key;
		
		private V value;
		
		public ArrayEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (!(obj instanceof Entry<?,?>))
				return false;
			Entry<?,?> e = (Entry<?,?>)obj;
			return (key==null ? e.getKey()==null : key.equals(e.getKey())) 
			&& (value==null ? e.getValue()==null : value.equals(e.getValue()));
		}

		@Override
		public int hashCode() {
			return (key==null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
		}
	}
	
	private abstract class ArrayEntryIterator<E> implements Iterator<E> {
		
		int next, last;
		
		ArrayEntryIterator() {
			findNext();
			last = -1;
		}
		
		private void findNext() {
			while (next < array.length && array[next] == null)
				next++;
		}

		@Override
		public boolean hasNext() {
			return next < array.length;
		}
		
		public Entry<K,V> nextEntry() {
			last = next;
			Entry<K,V> e = array[next++];
			findNext();
			return e;
		}

		@Override
		public void remove() {
			array[last] = null;
			number_of_used_array_entries--;
		}
	}
}
