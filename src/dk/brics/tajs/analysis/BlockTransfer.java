package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.solver.IBlockTransfer;

/**
 * Transfer for flow graph blocks.
 */
public class BlockTransfer implements IBlockTransfer<State,CallContext> {

	@Override
	public void transfer(BasicBlock b, State s) {
		s.clearDeadTemporaries(b.getLiveVariables());
	}
}
