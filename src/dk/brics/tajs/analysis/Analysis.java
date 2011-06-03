package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.NativeObject;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.AnalysisLatticeElement;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.IAnalysis;
import dk.brics.tajs.solver.IEdgeTransfer;
import dk.brics.tajs.solver.IWorkListStrategy;
import dk.brics.tajs.solver.SolverSynchronizer;

/**
 * Encapsulation of the analysis using {@link State}, {@link CallContext},
 * {@link InitialStateBuilder}, {@link NodeTransfer}, {@link EdgeTransfer},
 * {@link WorkListStrategy}, {@link Statistics}, and {@link GenericSolver}.
 */
public final class Analysis implements
		IAnalysis<State, CallContext, Statistics> {

	private FlowGraph flowgraph;

	private Solver solver;

	private InitialStateBuilder b;

	private NodeTransfer t;

	private BlockTransfer bt;

	private EdgeTransfer e;

	private WorkListStrategy w;

	private Statistics s;

	/**
	 * Constructs a new analysis object.
	 */
	public Analysis() {
		b = new InitialStateBuilder();
		t = new NodeTransfer();
		e = new EdgeTransfer();
		bt = new BlockTransfer();
		w = new WorkListStrategy();
		s = new Statistics();
		solver = new Solver(this);
	}

	@Override
	public AnalysisLatticeElement<State, CallContext, Statistics> makeAnalysisLattice() {
		return new AnalysisLatticeElement<State, CallContext, Statistics>(
				flowgraph);
	}

	@Override
	public InitialStateBuilder getInitialStateBuilder() {
		return b;
	}

	@Override
	public NodeTransfer getNodeTransferFunctions() {
		return t;
	}

	@Override
	public BlockTransfer getBlockTransferFunction() {
		return bt;
	}

	@Override
	public IEdgeTransfer<State, CallContext> getEdgeTransferFunctions() {
		return e;
	}

	@Override
	public IWorkListStrategy<CallContext> getWorklistStrategy() {
		return w;
	}

	@Override
	public Statistics getStatistics() {
		return s;
	}

	@Override
	public void setSolverInterface(Solver.SolverInterface c) {
		t.setSolverInterface(c);
		w.setCallGraph(c.getAnalysisLatticeElement().getCallGraph());
	}

	@Override
	public void setFlowGraph(FlowGraph g) {
		flowgraph = g;
	}

	/**
	 * Returns the solver.
	 */
	public Solver getSolver() {
		return solver;
	}

	/**
	 * Returns the flow graph.
	 */
	public FlowGraph getFlowGraph() {
		return flowgraph;
	}

	/**
	 * Analyze dependencies.
	 */
	public void analyzeDependencies() {
		solver.analyzeDependencies();
	}

	/**
	 * Sets the solver synchronizer.
	 */
	public void setSynchronizer(SolverSynchronizer sync) {
		solver.setSynchronizer(sync);
	}

	@Override
	public void evaluateGetter(NativeObject nativeobject, ObjectLabel objlabel,
			String propertyname, Solver.SolverInterface c) {
		NativeFunctions.evaluateGetter(nativeobject, objlabel, propertyname, c);
	}

	@Override
	public void evaluateSetter(NativeObject nativeobject, ObjectLabel objlabel,
			String propertyname, Value v, Solver.SolverInterface c) {
		NativeFunctions.evaluateSetter(nativeobject, objlabel, propertyname, v,
				c);
	}

}
