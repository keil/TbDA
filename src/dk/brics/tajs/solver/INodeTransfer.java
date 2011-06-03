package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;

/**
 * Interface for node transfer function classes.
 */
public interface INodeTransfer<BlockStateType extends IBlockState<BlockStateType>,
                               CallContextType extends ICallContext> extends NodeVisitor<BlockStateType> {
	
	/**
	 * Applies the transfer function on the given node and input state.
	 */
	public void transfer(Node n, BlockStateType in);
	
	/**
	 * Processes ordinary/exceptional return flow when a new call edge has been added.
	 */
	public void transferReturn(Node call_node, Function callee, CallContextType caller_context, CallContextType callee_context);
}
