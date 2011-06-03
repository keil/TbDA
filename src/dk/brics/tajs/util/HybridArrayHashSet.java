package dk.brics.tajs.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Hybrid of array and hash set.
 * Small maps are represented as arrays; above a certain threshold a HashSet is used instead. 
 */
public class HybridArrayHashSet<V> implements Set<V> { // TODO: javadoc
	
	// invariant: exactly one of array and hashset is null
	
	private static final int DEFAULT_THRESHOLD = 8;
	
	private V[] array;
	
	private int number_of_used_array_entries; // = number of non-null entries in array, if non-null
	
	private HashSet<V> hashset;
	
	@SuppressWarnings("unchecked")
	public HybridArrayHashSet(int threshold) {
		array = (V[])new Object[threshold];
	}
	
	public HybridArrayHashSet() {
		this(DEFAULT_THRESHOLD);
	}
	
	@SuppressWarnings("unchecked")
	public HybridArrayHashSet(Collection<V> m) {
		if (m.size() <= DEFAULT_THRESHOLD) { // note: this constructor uses the default threshold
			array = (V[])new Object[DEFAULT_THRESHOLD];
			number_of_used_array_entries = 0;
			for (V v : m)
				array[number_of_used_array_entries++] = v;
		} else {
			hashset = new HashSet<V>();
			hashset.addAll(m);
		}
	}

	@Override
	public boolean add(V e) {
		if (array != null) {
			int free_entry = -1;
			for (int i = 0; i < array.length; i++) {
				V v = array[i];
				if (v == null)
					free_entry = i;
				else if (v.equals(e))
					return false;
			}
			if (free_entry != -1) {
				array[free_entry] = e;
				number_of_used_array_entries++;
				return true;
			} else
				convertArrayToHashSet();
		}
		return hashset.add(e);
	}
	
	private void convertArrayToHashSet() {
		hashset = new HashSet<V>();
		for (V v : array)
			if (v != null)
				hashset.add(v);
		array = null;
		number_of_used_array_entries = 0;
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		if (array != null)
			if (number_of_used_array_entries + c.size() <= array.length) {
				boolean changed = false;
				loop: for (V f : c) {
					int free_entry = -1;
					for (int i = 0; i < array.length; i++) {
						V v = array[i];
						if (v == null)
							free_entry = i;
						else if (v.equals(f)) 
							continue loop;
					}
					array[free_entry] = f;
					number_of_used_array_entries++;
					changed = true;
				}
				return changed;
			} else
				convertArrayToHashSet();
		return hashset.addAll(c);
	}

	@Override
	public void clear() {
		if (array != null) {
			Arrays.fill(array, null);
			number_of_used_array_entries = 0;
		} else
			hashset.clear();
	}

	@Override
	public boolean contains(Object o) {
		if (array != null) {
			for (V v : array)
				if (v != null && v.equals(o))
					return true;
			return false;
		}
		return hashset.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (array != null) {
			loop: for (Object o : c) {
				for (V v : array) 
					if (v != null && v.equals(o))
						continue loop;
				return false;
			}
			return true;
		}
		return hashset.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return array != null ? number_of_used_array_entries == 0 : hashset.isEmpty();
	}

	@Override
	public int size() {
		return array != null ? number_of_used_array_entries : hashset.size();
	}

	@Override
	public Iterator<V> iterator() {
		if (array != null)
			return new HybridArrayHashSetIterator();
		return hashset.iterator();
	}

	@Override
	public boolean remove(Object o) {
		if (array != null) { 
			for (int i = 0; i < array.length; i++) {
				V v = array[i];
				if (v != null && v.equals(o)) {
					array[i] = null;
					number_of_used_array_entries--;
					return true;
				}
			}
			return false;
		} 
		return hashset.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (array != null) { 
			boolean changed = false;
			loop: for (Object o : c)
				for (int i = 0; i < array.length; i++) {
					V v = array[i];
					if (v != null && v.equals(o)) {
						array[i] = null;
						number_of_used_array_entries--;
						changed = true;
						continue loop;
					}
				}
			return changed;
		} 
		return hashset.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (array != null) { 
			boolean changed = false;
			for (int i = 0; i < array.length; i++) {
				V v = array[i];
				if (v != null && !c.contains(v)) {
					array[i] = null;
					number_of_used_array_entries--;
					changed = true;
				}
			}
			return changed;
		} 
		return hashset.retainAll(c);
	}

	@Override
	public Object[] toArray() {
		if (array != null) { 
			Object[] a = new Object[number_of_used_array_entries];
			int i = 0;
			for (V v : array)
				if (v != null)
					a[i++] = v;
			return a;
		}
		return hashset.toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if (array != null) { 
			if (a.length < number_of_used_array_entries) 
				a = (T[])Array.newInstance(a.getClass().getComponentType(), number_of_used_array_entries);
			int i = 0;
			for (V v : array)
				if (v != null)
					a[i++] = (T)v; // TODO: throw ArrayStoreException if not T :> V
			while (i < a.length)
				a[i++] = null;
			return a;
		}
		return hashset.toArray(a);
	}

	@Override
	public int hashCode() {
		if (array != null) {
			int h = 0;
			for (V v : array) 
				if (v != null)
					h += v.hashCode();
			return h;
		}
		return hashset.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Set<?>))
			return false;
		Set<?> s = (Set<?>) obj;
		if (size() != s.size() || hashCode() != s.hashCode())
			return false;
		return containsAll(s);
	}

	@Override
	public String toString() {
		if (array != null) {
			StringBuilder b = new StringBuilder();
			b.append('{');
			boolean first = true;
			for (V v : array) 
				if (v != null) {
					if (first)
						first = false;
					else
						b.append(", ");
					b.append(v);
				}
			b.append('}');
			return b.toString();
		} 
		return hashset.toString();
	}
	
	private class HybridArrayHashSetIterator implements Iterator<V> {
		
		int next, last;
		
		HybridArrayHashSetIterator() {
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

		@Override
		public V next() {
			last = next;
			V v = array[next++];
			findNext();
			return v;
		}

		@Override
		public void remove() {
			array[last] = null;
			number_of_used_array_entries--;
		}
	}
}
