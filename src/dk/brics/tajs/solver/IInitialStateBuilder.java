package dk.brics.tajs.solver;

import org.jdom.Document;

/**
 * Interface for initial state builder classes.
 */
public interface IInitialStateBuilder<BlockStateType extends IBlockState<BlockStateType>, CallContextType extends ICallContext, StatisticsType extends IStatistics> {

	/**
	 * Builds the initial state.
	 */
	public void addInitialState(GenericSolver<BlockStateType, CallContextType, StatisticsType>.SolverInterface c);

	/**
	 * Builds DOM specific state.
	 */
	public void addDOMSpecificState(Document document);

}
