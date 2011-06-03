package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.NativeObject;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

/**
 * Interface for analyses on flow graphs.
 */
public interface IAnalysis<BlockStateType extends IBlockState<BlockStateType>,
                           CallContextType extends ICallContext,
					       StatisticsType extends IStatistics> {

	/**
	 * Returns a new bottom global analysis lattice element.
	 * {@link #setFlowGraph(FlowGraph)} must be called before.
	 */
	public IAnalysisLatticeElement<BlockStateType,CallContextType> makeAnalysisLattice();

	/**
	 * Returns the initial state builder.
	 */
	public IInitialStateBuilder<BlockStateType,CallContextType,StatisticsType> getInitialStateBuilder();

	/**
	 * Returns the node transfer functions.
	 */
	public INodeTransfer<BlockStateType,CallContextType> getNodeTransferFunctions();

	/**
	 * Returns the end-of-block transfer function.
	 */
	public IBlockTransfer<BlockStateType,CallContextType> getBlockTransferFunction();

	/**
	 * Returns the edge transfer functions.
	 */
	public IEdgeTransfer<BlockStateType,CallContextType> getEdgeTransferFunctions();
	
	/**
	 * Returns the work list strategy.
	 */
	public IWorkListStrategy<CallContextType> getWorklistStrategy();
	
	/**
	 * Returns the statistics collector.
	 */
	public StatisticsType getStatistics();
	
	/**
	 * Sets the current solver interface.
	 */
	public void setSolverInterface(GenericSolver<BlockStateType,CallContextType,StatisticsType>.SolverInterface c);
	
	/**
	 * Sets the current flow graph.
	 */
	public void setFlowGraph(FlowGraph g);
	
	public void evaluateGetter(NativeObject nativeobject, ObjectLabel objlabel, String propertyname, 
			GenericSolver<BlockStateType,CallContextType,StatisticsType>.SolverInterface c);

    public void evaluateSetter(NativeObject nativeobject, ObjectLabel objlabel, String propertyname, Value v,
			GenericSolver<BlockStateType,CallContextType,StatisticsType>.SolverInterface c);
}
