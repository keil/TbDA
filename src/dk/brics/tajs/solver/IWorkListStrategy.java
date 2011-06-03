package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

/**
 * Interface for work list strategies.
 */
public interface IWorkListStrategy<CallContextType extends ICallContext> {

	/**
	 * Compares two work list entries.
	 * A negative value means that the first has higher priority than the second,
	 * a positive value means that the second has higher priority than the first.
	 * This must be a stable comparison.
	 */
	public int compare(IEntry<CallContextType> e1, IEntry<CallContextType> e2);
	
	/**
	 * Interface for work list entries.
	 */
	public interface IEntry<CallContextType extends ICallContext> {
		
		/**
		 * Returns the block.
		 */
		public BasicBlock getBlock();
		
		/**
		 * Returns the context.
		 */
		public CallContextType getContext();
		
		/**
		 * Returns the state position impact.
		 */
		public int getImpact();
		
		/**
		 * Returns the entry serial number.
		 */
		public int getSerial();
	}
}
