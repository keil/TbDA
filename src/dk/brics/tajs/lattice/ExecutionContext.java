package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.Map;
import java.util.Set;

import dk.brics.tajs.flowgraph.ObjectLabel;

/**
 * Abstract execution context.
 * Immutable.
 */
public final class ExecutionContext { // TODO: canonicalize?
	
	// TODO: change Set<ExecutionContext> to single ExecutionContext that contains Set<ScopeChain, Set<ObjectLabel>, Set<ObjectLabel> - perhaps inline into BlockState
	
	private ScopeChain scope_chain; // null if empty
	
	private ObjectLabel var_obj; // null if removed
	// TODO: do we really need the variable object explicitly in ExecutionContext? or can we always just take the first object from the scope chain?
	
	private ObjectLabel this_obj;

	/**
	 * Constructs a new execution context.
	 */
	public ExecutionContext(ScopeChain scope_chain, ObjectLabel var_obj, ObjectLabel this_obj) {
		if (scope_chain == null)
			throw new NullPointerException("Scope chain is empty");
		this.scope_chain = scope_chain;
		this.var_obj = var_obj;
		this.this_obj = this_obj;
	}
	
	/**
	 * Constructs new execution contexts.
	 */
	public static Set<ExecutionContext> make(Set<ScopeChain> scope_chains, ObjectLabel var_obj, Set<ObjectLabel> this_obj) {
		Set<ExecutionContext> ec = newSet();
		if (scope_chains != null)
			for (ScopeChain s : scope_chains)
				for (ObjectLabel t : this_obj)
					ec.add(new ExecutionContext(s, var_obj, t));
		return ec;
	}
	
	/**
	 * Constructs new execution contexts.
	 */
	public static Set<ExecutionContext> make(Set<ScopeChain> scope_chains, ObjectLabel var_obj, ObjectLabel this_obj) {
		Set<ExecutionContext> ec = newSet();
		if (scope_chains != null)
			for (ScopeChain s : scope_chains)
				ec.add(new ExecutionContext(s, var_obj, this_obj));
		return ec;
	}
	
	/**
	 * Constructs new execution contexts.
	 */
	public static Set<ExecutionContext> make(Set<ExecutionContext> execution_context, Set<ObjectLabel> this_obj) {
		Set<ExecutionContext> ec = newSet();
		for (ExecutionContext e : execution_context)
			for (ObjectLabel t : this_obj)
				ec.add(new ExecutionContext(e.getScopeChain(), e.getVariableObject(), t));
		return ec;
	}
	
	/**
	 * Returns the scope chain of this execution context.
	 */
	public ScopeChain getScopeChain() {
		return scope_chain;
	}

	/**
	 * Returns the variable object of this execution context, or null if it has been removed.
	 */
	public ObjectLabel getVariableObject() {
		return var_obj;
	}
	
	/**
	 * Returns the 'this' object of this execution context.
	 */
	public ObjectLabel getThisObject() {
		return this_obj;
	}

	/**
	 * Summarizes the given execution context set.
	 * Always returns a new set.
	 */
	public static Set<ExecutionContext> summarize(Set<ExecutionContext> se, Summarized summarized) {
		Set<ExecutionContext> res = newSet();
		Map<ScopeChain,Set<ScopeChain>> cache = newMap();
		for (ExecutionContext ec : se)
			ec.summarize(res, summarized, cache);
		return res;
	}

	/**
	 * Summarizes this execution context.
	 */
	private void summarize(Set<ExecutionContext> res, Summarized summarized, Map<ScopeChain,Set<ScopeChain>> cache) {
		Set<ScopeChain> scs = scope_chain.summarize(summarized, cache);
		ObjectLabel this_obj_summarized, var_obj_summarized;
		if (!this_obj.isSingleton() || !var_obj.isSingleton()) {
			res.addAll(make(scs, var_obj, this_obj));
			return;
		}
		this_obj_summarized = this_obj.makeSummary();
		var_obj_summarized = var_obj.makeSummary();
		if (summarized.isDefinitelySummarized(this_obj)) {
			if (summarized.isDefinitelySummarized(var_obj)) {
				// this_obj  definitely summarized, var_obj definitely summarized
				res.addAll(make(scs, var_obj_summarized, this_obj_summarized));
			} else if (summarized.isMaybeSummarized(var_obj)) {
				// this_obj definitely summarized, var_obj maybe summarized
				res.addAll(make(scs, var_obj, this_obj_summarized));
				res.addAll(make(scs, var_obj_summarized, this_obj_summarized));
			} else {
				// this_obj definitely summarized, var_obj not summarized
				res.addAll(make(scs, var_obj, this_obj_summarized));
			}
		} else if (summarized.isMaybeSummarized(this_obj)) {
			if (summarized.isDefinitelySummarized(var_obj)) {
				// this_obj maybe summarized, var_obj definitely summarized
				res.addAll(make(scs, var_obj_summarized, this_obj_summarized));
				res.addAll(make(scs, var_obj_summarized, this_obj));
			} else if (summarized.isMaybeSummarized(var_obj)) {
				// this_obj maybe summarized, var_obj maybe summarized
				res.addAll(make(scs, var_obj_summarized, this_obj_summarized));
				res.addAll(make(scs, var_obj_summarized, this_obj));
				res.addAll(make(scs, var_obj, this_obj_summarized));
				res.addAll(make(scs, var_obj, this_obj));
			} else {
				// this_obj maybe summarized, var_obj not summarized
				res.addAll(make(scs, var_obj, this_obj_summarized));
				res.addAll(make(scs, var_obj, this_obj));
			}
		} else {
			if (summarized.isDefinitelySummarized(var_obj)) {
				// this_obj not summarized, var_obj definitely summarized
				res.addAll(make(scs, var_obj_summarized, this_obj));
			} else if (summarized.isMaybeSummarized(var_obj)) {
				// this_obj not summarized, var_obj maybe summarized
				res.addAll(make(scs, var_obj_summarized, this_obj));
				res.addAll(make(scs, var_obj, this_obj));
			} else {
				// this_obj not summarized, var_obj not summarized
				res.addAll(make(scs, var_obj, this_obj));
			}
		}
	}	
	
	/**
	 * Replaces all occurrences of oldlabel by newlabel.
	 */
	public ExecutionContext replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel, Map<ScopeChain,ScopeChain> cache) {
		return new ExecutionContext(scope_chain != null ? scope_chain.replaceObjectLabel(oldlabel, newlabel, cache) : null, 
				var_obj != null ? var_obj.equals(oldlabel) ? newlabel : var_obj : null, 
				this_obj.equals(oldlabel) ? newlabel : this_obj);
	}
		
	/**
	 * Replaces all occurrences of label1 or label2 by both labels.
	 */
	public static Set<ExecutionContext> replaceObjectLabel(Set<ExecutionContext> se, ObjectLabel oldlabel, ObjectLabel newlabel, 
			Map<ScopeChain,ScopeChain> cache) {
		Set<ExecutionContext> ecs = newSet();
		for (ExecutionContext ec : se)
			ecs.add(ec.replaceObjectLabel(oldlabel, newlabel, cache));
		return ecs;
	}
	
	/**
	 * Returns the set of object labels within the given execution contexts.
	 */
	public static Set<ObjectLabel> getObjectLabels(Set<ExecutionContext> es) {
		Set<ObjectLabel> objs = newSet();
		for (ExecutionContext e : es) {
			if (e.scope_chain != null)
				for (ObjectLabel l : e.scope_chain)
					objs.add(l);
			objs.add(e.this_obj);
			if (e.var_obj != null)
				objs.add(e.var_obj);
		}
		return objs;
	}
	
	/**
	 * Checks whether the given execution context is equal to this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ExecutionContext))
			return false;
		ExecutionContext e = (ExecutionContext)obj;
		if ((var_obj == null) != (e.var_obj == null)
				|| (scope_chain == null) != (e.scope_chain == null))
			return false;
		return (scope_chain != null ? scope_chain.equals(e.scope_chain) : true) 
		&& (var_obj != null ? var_obj.equals(e.var_obj) : true) 
		&& this_obj.equals(e.this_obj);
	}

	/**
	 * Computes the hash code for this execution context.
	 */
	@Override
	public int hashCode() {
		return (scope_chain != null ? scope_chain.hashCode() * 7 : 0) 
		+ (var_obj != null ? var_obj.hashCode() * 3 : 0) 
		+ this_obj.hashCode() * 11;
	}

	/**
	 * Returns a string representation of this execution context.
	 */
	@Override
	public String toString() {
		return "[ScopeChain=" + (scope_chain != null ? scope_chain : "[]") 
		+ ", VariableObject=" + (var_obj != null ? var_obj : "<removed>") 
		+ ", this=" + this_obj + "]";
	}
}
