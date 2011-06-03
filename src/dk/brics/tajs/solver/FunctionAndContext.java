package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.Function;

/**
 * Pair of a function and a call context.
 */
public final class FunctionAndContext<CallContextType extends ICallContext> { // TODO: use FunctionAndContext elsewhere?

	private Function f;
	
	private CallContextType c;
	
	public FunctionAndContext(Function f, CallContextType c) {
		this.f = f;
		this.c = c;
	}
	
	public Function getFunction() {
		return f;
	}
	
	public CallContextType getContext() {
		return c;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FunctionAndContext))
			return false;
		FunctionAndContext<CallContextType> fcp = (FunctionAndContext<CallContextType>) obj;
		return fcp.f == f && fcp.c.equals(c);
	}

	@Override
	public int hashCode() {
		return f.getIndex() * 13 + c.hashCode() * 3;
	}
}
