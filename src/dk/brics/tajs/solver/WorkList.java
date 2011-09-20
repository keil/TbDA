package dk.brics.tajs.solver;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.options.Options;

/**
 * Work list used by solver.
 */
public class WorkList<CallContextType extends ICallContext> {
	
	// invariant: pending_set is a subset of pending_queue (not necessarily equal)
	
	private static int next_serial;
	
	private Map<Entry,Entry> pending_set;
	
	private PriorityQueue<Entry> pending_queue;
	
	private IWorkListStrategy<CallContextType> worklist_strategy;
	
	// TODO: replace pending_set+pending_queue by a combined data structure?

	/**
	 * Constructs a new empty work list.
	 */
	public WorkList(IWorkListStrategy<CallContextType> w) {
		worklist_strategy = w;
		pending_set = new HashMap<Entry,Entry>();
		pending_queue = new PriorityQueue<Entry>();
	}
	
	/**
	 * Adds or replaces an entry.
	 */
	public void add(Entry e) {
		Entry old = pending_set.remove(e);
		if (old != null) {
			e.setImpact(e.getImpact() + old.getImpact()); 
			if (Options.isDebugEnabled())
				System.out.println("Replacing worklist entry for block " + e.b.getIndex());
		} else {
			pending_queue.add(e);
			if (Options.isDebugEnabled())
				System.out.println("Adding worklist entry for block " + e.b.getIndex());
		}
		pending_set.put(e, e);
	}

	/**
	 * Checks whether the work list is empty.
	 */
	public boolean isEmpty() {
		return pending_queue.isEmpty();
	}

	/**
	 * Picks and removes the next entry.
	 * Returns null if the entry has been removed.
	 */
	public Entry removeNext() {
		Entry p = pending_queue.remove();
		if (pending_set.remove(p) == null) {
			if (Options.isDebugEnabled())
				System.out.println("Skipping removed entry " + p);
			return null;
		}
		return p;
	}

	/**
	 * Returns the number of entries in the work list.
	 */
	public int size() {
		return pending_set.size();
	}
	
	/**
	 * Removes the given entry.
	 */
	public void remove(Entry e) {
		if (pending_set.remove(e) != null)
			if (Options.isDebugEnabled())
				System.out.println("Removing entry " + e);
	}

	/**
	 * Returns a string description of this work list.
	 */
	@Override
	public String toString() {
		return pending_set.keySet().toString();
	}
	
	/**
	 * Work list entry.
	 * Consists of a block and a call context.
	 */
	public class Entry implements IWorkListStrategy.IEntry<CallContextType>, Comparable<Entry> {
		
		private BasicBlock b;
		
		private CallContextType c;
		
		private int impact;
		
		private int serial;
		
		private int hash;
		
		/**
		 * Constructs a new entry.
		 * The state becomes immutable.
		 */
		public Entry(BasicBlock b, CallContextType c, int impact) {
			this.b = b;
			this.c = c;
			if (impact < 0)
				throw new RuntimeException("Negative impact!?");
			this.impact = impact;
			serial = next_serial++;
			hash = b.getIndex() + c.hashCode();
		}
		
		@Override
		public BasicBlock getBlock() {
			return b;
		}
		
		@Override
		public CallContextType getContext() {
			return c;
		}
		
		@Override
		public int getImpact() {
			return impact;
		}
		
		public void setImpact(int impact) {
			this.impact = impact;
		}
		
		@Override
		public int getSerial() {
			return serial;
		}
		
		/**
		 * Checks whether this entry is equal to the given one.
		 * Ignores the impact number.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !getClass().equals(obj.getClass()))
				return false;
			WorkList.Entry p = (WorkList.Entry) obj;
			return p.b == b && p.c.equals(c);
		}

		/**
		 * Computes a hash code for this entry.
		 */
		@Override
		public int hashCode() {
			return hash;
		}
		
		/**
		 * Compares this and the given entry.
		 * This method defines the work list priority using the work list strategy.
		 */
		@Override
		public int compareTo(Entry p) {
			return worklist_strategy.compare(this, p);
		}

		/**
		 * Returns a string description of this entry.
		 */
		@Override
		public String toString() {
			return Integer.toString(b.getIndex());
		}
	}
}
