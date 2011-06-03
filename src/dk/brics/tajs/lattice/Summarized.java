package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import dk.brics.tajs.flowgraph.ObjectLabel;

/**
 * Maybe/definitely summarized object label sets.
 */
public final class Summarized {

	/**
	 * Maybe summarized objects since function entry. (Contains the singleton object labels.)
	 */
	private Set<ObjectLabel> maybe_summarized; 

	/**
	 * Definitely summarized objects since function entry. (Contains the singleton object labels.)
	 */
	private Set<ObjectLabel> definitely_summarized; // note: lattice order reversed for this component! but always a subset of maybe_summarized

	/**
	 * Constructs a new pair of empty summarized sets.
	 */
	public Summarized() {
		maybe_summarized = newSet();
		definitely_summarized = newSet();
	}
	
	/**
	 * Constructs a new pair of summarized sets.
	 * The sets are copied.
	 */
	public Summarized(Summarized s) {
		this.maybe_summarized = newSet(s.maybe_summarized);
		this.definitely_summarized = newSet(s.definitely_summarized);
	}
	
	/**
	 * Returns the maybe summarized object labels.
	 */
	public Set<ObjectLabel> getMaybeSummarized() {
		return maybe_summarized;
	}
	
	/**
	 * Returns the definitely summarized object labels.
	 */
	public Set<ObjectLabel> getDefinitelySummarized() {
		return definitely_summarized;
	}
	
	/**
	 * Adds the specified object label as definitely summarized.
	 */
	public void addDefinitelySummarized(ObjectLabel objlabel) {
		definitely_summarized.add(objlabel);
		maybe_summarized.add(objlabel);
	}
	
	/**
	 * Checks whether the given object label is marked as maybe summarized.
	 */
	public boolean isMaybeSummarized(ObjectLabel objlabel) {
		return maybe_summarized.contains(objlabel);
	}

	/**
	 * Checks whether the given object label is marked as definitely summarized.
	 */
	public boolean isDefinitelySummarized(ObjectLabel objlabel) {
		return definitely_summarized.contains(objlabel);
	}

	/**
	 * Clears the sets.
	 */
	public void clear() {
		maybe_summarized.clear();
		definitely_summarized.clear();
	}
	
	/**
	 * Retains the given object labels.
	 */
	public void retainAll(Set<ObjectLabel> objs) {
		maybe_summarized.retainAll(objs);
		definitely_summarized.retainAll(objs);
	}
	
	/**
	 * Joins the given summarized sets into this pair.
	 * @return true if changed
	 */
	public boolean join(Summarized s) {
		return maybe_summarized.addAll(s.maybe_summarized)
		|| definitely_summarized.retainAll(s.definitely_summarized);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Summarized))
			return false;
		Summarized x = (Summarized)obj;
		return maybe_summarized.equals(x.maybe_summarized) && definitely_summarized.equals(x.definitely_summarized);
	}

	@Override
	public int hashCode() {
		return maybe_summarized.hashCode() * 3 + definitely_summarized.hashCode() * 17;
	}

	@Override
	public String toString() {
		return "maybe=" + maybe_summarized + ", definitely=" + definitely_summarized;
	}
}
