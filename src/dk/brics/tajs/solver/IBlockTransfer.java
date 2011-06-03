package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

/**
 * Interface for end-of-block transfer function classes.
 */
public interface IBlockTransfer<BlockStateType extends IBlockState<BlockStateType>,
                                CallContextType extends ICallContext> {
	
	/**
	 * Applies the transfer function on the given block and input state.
	 */
	public void transfer(BasicBlock b, BlockStateType in);
}
