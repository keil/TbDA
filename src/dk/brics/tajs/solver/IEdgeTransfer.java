package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

/**
 * Interface for edge transfer function classes.
 */
public interface IEdgeTransfer<BlockStateType extends IBlockState<BlockStateType>,
                               CallContextType extends ICallContext> {
	
	/**
	 * Returns true if flow should occur on the given edge and state.
	 */
	public boolean transfer(BasicBlock src, BasicBlock dst, BlockStateType state);
}
