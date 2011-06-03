package dk.brics.tajs.analysis;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphReference;
import dk.brics.tajs.dependency.interfaces.IDependency;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.solver.GenericSolver;

/**
 * Abstract state.
 */
public final class State extends BlockState<State, CallContext, Statistics>
		implements IDependency<State>, IDependencyGraphReference<State> {

	/**
	 * state dependencies
	 */
	private Dependency mDependency = new Dependency();

	/**
	 * dependency graph reference
	 */
	private DependencyGraphReference mDependencyGraphReference = new DependencyGraphReference();

	/**
	 * Constructs a new abstract state.
	 */
	public State(
			GenericSolver<State, CallContext, Statistics>.SolverInterface c,
			BasicBlock block) {
		super(c, block);
	}

	private State(State state) {
		super(state);
		mDependency = state.getDependency();
		mDependencyGraphReference = state.getDependencyGraphReference();
	}

	/**
	 * Constructs a new abstract state without solver interface and basic block
	 * references.
	 */
	public State() {
		super();
	}

	@Override
	public State clone() {
		return new State(this);
	}

	@Override
	public String diff(State old) {
		return super.diff((BlockState<State, CallContext, Statistics>) old);
	}

	@Override
	public boolean join(State s) {
		return super.join((BlockState<State, CallContext, Statistics>) s);
	}

	@Override
	public boolean hasDependency() {
		if (mDependency.isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public Dependency getDependency() {
		return mDependency;
	}

	@Override
	public State joinDependency(Dependency dependency) {
		Dependency d = new Dependency();
		d.join(mDependency);
		d.join(dependency);

		mDependency = d;
		return this;
	}

	@Override
	public State setDependency(Dependency dependency) {
		mDependency = dependency;
		return this;
	}

	@Override
	public void removeDependency() {
		mDependency = new Dependency();
	}

	@Override
	public String printDependency() {
		return mDependency.toString();
	}

	@Override
	public DependencyGraphReference getDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? mDependencyGraphReference
				: null;
	}

	@Override
	public State setDependencyGraphReference(DependencyGraphReference reference) {
		mDependencyGraphReference = new DependencyGraphReference(reference);
		return this;
	}

	@Override
	public boolean hasDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? true : false;
	}

	@Override
	public String printDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? mDependencyGraphReference
				.toString() : "";
	}

	@Override
	public State joinDependencyGraphReference(DependencyGraphReference reference) {
		DependencyGraphReference newReference = new DependencyGraphReference();
		newReference.join(mDependencyGraphReference);
		newReference.join(reference);
		mDependencyGraphReference  = newReference;
		return this;
	}
}