package dk.brics.tajs.analysis;

import dk.brics.tajs.solver.GenericSolver;

/**
 * Fixpoint solver.
 */
public final class Solver extends GenericSolver<State,CallContext,Statistics> {

	/**
	 * Constructs a new solver.
	 */
	public Solver(Analysis analysis) {
		super(analysis);
	}
}
