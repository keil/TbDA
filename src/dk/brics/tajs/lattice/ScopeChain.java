package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.newMap;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import dk.brics.tajs.flowgraph.ObjectLabel;

/**
 * Abstract scope chain.
 * Immutable.
 */
public final class ScopeChain implements Iterable<ObjectLabel> {
	
	private ObjectLabel obj;
	
	private ScopeChain next;
	
	private int hashcode;
	
	private static Map<ScopeChain,WeakReference<ScopeChain>> cache = new WeakHashMap<ScopeChain,WeakReference<ScopeChain>>();
	
	private static int cache_hits;

	private static int cache_misses;

	/**
	 * Clears the canonicalization cache.
	 */
	public static void clearCache() {
		cache.clear();
	}

	/**
	 * Returns the canonicalization cache size.
	 */
	public static int getCacheSize() {
		return cache.size();
	}

	/**
	 * Returns the number of cache misses.
	 */
	public static int getNumberOfCacheMisses() {
		return cache_misses;
	}

	/**
	 * Returns the number of cache hits.
	 */
	public static int getNumberOfCacheHits() {
		return cache_hits;
	}

	/**
	 * Resets the cache numbers.
	 */
	public static void reset() {
		cache_hits = 0;
		cache_misses = 0;
		clearCache();
	}

	/**
	 * Creates a new scope chain.
	 */
	private ScopeChain(ObjectLabel obj, ScopeChain next) {
		this.obj = obj;
		this.next = next;
	}
	
	/**
	 * Creates a scope chain.
	 */
	public static ScopeChain make(ObjectLabel obj, ScopeChain next) {
		return canonicalize(new ScopeChain(obj, next));
	}
	
	/**
	 * Creates a scope chain.
	 */
	public static ScopeChain make(ObjectLabel obj) {
		return make(obj, (ScopeChain)null);
	}
	
	/**
	 * Creates a scope chain.
	 */
	public static Set<ScopeChain> make(ObjectLabel obj, Set<ScopeChain> next) {
		Set<ScopeChain> sc = newSet();
		if (next != null)
			for (ScopeChain n : next)
				sc.add(make(obj, n));
		else
			sc.add(make(obj));
		return sc;
	}
	
	private static ScopeChain canonicalize(ScopeChain e) {
		WeakReference<ScopeChain> ref = cache.get(e);
		ScopeChain c = ref != null ? ref.get() : null;
		if (c == null) {
			cache.put(e, new WeakReference<ScopeChain>(e));
			cache_misses++;
		} else {
			e = c;
			cache_hits++;
		}
		return e;
	}
	
	/**
	 * Returns the tail of this scope chain, or null if none.
	 */
	public ScopeChain next() {
		return next;
	}
	
	/**
	 * Returns the top-most object in this scope chain.
	 */
	public ObjectLabel getObject() {
		return obj;
	}
	
	/**
	 * Replaces all occurrences of oldlabel by newlabel.
	 */
	public ScopeChain replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel, Map<ScopeChain,ScopeChain> cache) {
		ScopeChain c = cache.get(this);
		if (c == null) {
			ScopeChain n = next != null ? next.replaceObjectLabel(oldlabel, newlabel, cache) : null;
			ObjectLabel newobj;
			if (obj.equals(oldlabel))
				newobj = newlabel;
			else
				newobj = obj;
			if (newobj != obj || n != next)
				c = make(newobj, n);
			else
				c = this;
			cache.put(this, c);
		}
		return c;
	}
	
	/**
	 * Constructs a scope chain as a copy of the given one but with object labels summarized.
	 */
	public static Set<ScopeChain> summarize(Set<ScopeChain> scs, Summarized s) {
		Map<ScopeChain,Set<ScopeChain>> cache = newMap();
		Set<ScopeChain> cs = newSet();
		for (ScopeChain sc : scs)
			cs.addAll(sc.summarize(s, cache));
		return cs;
	}
	
	/**
	 * Summarizes this scope chain.
	 */
	public Set<ScopeChain> summarize(Summarized s, Map<ScopeChain,Set<ScopeChain>> cache) {
		Set<ScopeChain> cs = cache.get(this);
		if (cs == null) {
			cs = newSet();
			Set<ScopeChain> n = next != null ? next.summarize(s, cache) : null;
			if (obj.isSingleton()) {
				if (s.isMaybeSummarized(obj)) {
					cs.addAll(make(obj.makeSummary(), n));
					if (!s.isDefinitelySummarized(obj))
						cs.addAll(make(obj, n));
				} else
					cs.addAll(make(obj, n));
			} else
				cs.addAll(make(obj, n));
			cache.put(this, cs);
		}
		return cs;
	}
	
	/**
	 * Checks whether the given scope chain is equal to this one.
	 */
	@Override
	public boolean equals(Object x) {
		if (x == this)
			return true;
		if (!(x instanceof ScopeChain))
			return false;
		ScopeChain e = (ScopeChain)x;
		if ((next == null) != (e.next == null) || (next != null && !next.equals(e.next)))
			return false;
		return obj.equals(e.obj);
	}

	/**
	 * Computes the hash code for this scope chain.
	 */
	@Override
	public int hashCode() {
		if (hashcode == 0) {
			hashcode = obj.hashCode() * 17 + (next != null ? next.hashCode() : 0) * 3;
			if (hashcode == 0)
				hashcode = 1;
		}
		return hashcode;
	}

	/**
	 * Returns a string representation of this scope chain.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[");
		ScopeChain e = this;
		do {
			b.append(e.obj);
			if (e.next != null) 
				b.append(",");
			e = e.next;
			
		} while (e != null);
		b.append("]");
		return b.toString();
	}

	/**
	 * Returns a object label iterator for this scope chain.
	 */
	@Override
	public Iterator<ObjectLabel> iterator() {
		return new Iterator<ObjectLabel>() {
			
			private ScopeChain c = ScopeChain.this;

			@Override
			public boolean hasNext() {
				return c != null;
			}

			@Override
			public ObjectLabel next() {
				ObjectLabel objlabel = c.obj;
				c = c.next;
				return objlabel;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
