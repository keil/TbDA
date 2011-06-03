package dk.brics.tajs.solver;

import java.util.Collection;
import java.util.Map;

import dk.brics.tajs.flowgraph.BasicBlock;

/**
 * Interface for global analysis lattice elements.
 */
public interface IAnalysisLatticeElement<BlockStateType extends IBlockState<BlockStateType>,
                                         CallContextType extends ICallContext> {

	/**
	 * Result from {@link IAnalysisLatticeElement#joinBlockEntry(IBlockState, BasicBlock, ICallContext, boolean)}.
	 */
	public static class MergeResult {
		
		private int delta;
		
		private String diff;
		
		/**
		 * Constructs a new merge result.
		 */
		public MergeResult(int delta, String diff) {
			this.delta = delta;
			this.diff = diff;
		}
		
		/**
		 * Returns the position delta.
		 */
		public int getDelta() {
			return delta;
		}
		
		/**
		 * Returns a description of the abstract state difference, 
		 * or null if not available.
		 */
		public String getDiff() {
			return diff;
		}
	}
	
	/**
	 * Returns the abstract state for entry of the given basic block and call context,
	 * where null represents bottom.
	 */
	public BlockStateType getState(BasicBlock block, CallContextType context);

	/**
	 * Returns the contexts and abstract states for the entry of the given basic block.
	 */
	public Map<CallContextType,BlockStateType> getStates(BasicBlock block);

	/**
	 * Returns the state entries for the given basic block.
	 */
	public Collection<Map.Entry<CallContextType,BlockStateType>> getStateMapEntries(BasicBlock block);

	/**
	 * Returns the call graph.
	 */
	public CallGraph<BlockStateType,CallContextType> getCallGraph();

	/**
	 * Returns the number of states stored for the given basic block.
	 */
	public int getSize(BasicBlock block);

	/**
	 * Returns the total position.
	 */
	public int getPosition();

	/**
	 * Merges s into the entry state of b in call context c.
	 * The given state may be modified by the operation.
	 * @param reduce if set, reduce the state while joining
	 * @return a merge result, or null if no new flow added.
	 */
	public MergeResult joinBlockEntry(BlockStateType s, BasicBlock b, CallContextType c, boolean reduce);

	/**
	 * Analyze the dependency of the current lattice element
	 */
	public void analyzeDependencies();
}