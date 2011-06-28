package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.DependencyLabel;
import dk.brics.tajs.dependency.graph.Label;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.nodes.CallNode;
import dk.brics.tajs.flowgraph.nodes.EventDispatcherNode;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Summarized;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.solver.NodeAndContext;

/**
 * Models function calls.
 */
public class FunctionCalls {

	private FunctionCalls() {
	}

	/**
	 * Information about a function call.
	 */
	public static interface CallInfo<T extends Node> {

		/**
		 * Returns the node where the call originates from. Note that this may
		 * be an indirect call via call/apply.
		 */
		public T getSourceNode();

		/**
		 * Checks whether this is a construct or an ordinary call.
		 */
		public boolean isConstructorCall();

		/**
		 * Returns the abstract value describing which function to call.
		 */
		public Value getFunction();

		/**
		 * Creates the object value of 'this'. Note that this may have
		 * side-effects on the callee_state.
		 */
		public Set<ObjectLabel> prepareThis(State caller_state, State callee_state);

		/**
		 * Returns the value of the i'th argument. The first argument is number
		 * 0. Returns Undef if the argument is not provided. Can be used even if
		 * the number of arguments is unknown.
		 * 
		 * @see #getUnknownArg()
		 */
		public Value getArg(int i);

		/**
		 * Returns the number of arguments.
		 * 
		 * @see #isUnknownNumberOfArgs()
		 */
		public int getNumberOfArgs();

		/**
		 * Returns the value of an unknown argument. Only to be called if the
		 * number of arguments is unknown.
		 */
		public Value getUnknownArg(); // TODO: would simplify things if this
										// could also be used with fixed number
										// of args

		/**
		 * Returns true if the number of arguments is unknown.
		 */
		public boolean isUnknownNumberOfArgs();

		public int getResultVar();

		public int getBaseVar();
	}

	public static class EventHandlerCall implements CallInfo<EventDispatcherNode> {

		private EventDispatcherNode sourceNode;
		private Value function;
		private Value arg1;

		private Solver.SolverInterface solver;

		public EventHandlerCall(EventDispatcherNode sourceNode, Value function, Value arg1, GenericSolver.SolverInterface solver) {
			this.sourceNode = sourceNode;
			this.function = function;
			this.arg1 = arg1;
			this.solver = solver;
		}

		@Override
		public EventDispatcherNode getSourceNode() {
			return sourceNode;
		}

		@Override
		public boolean isConstructorCall() {
			return false;
		}

		@Override
		public Value getFunction() {
			return function;
		}

		@Override
		public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
			return HTMLBuilder.HTML_OBJECT_LABELS;
		}

		@Override
		public Value getArg(int i) {
			return arg1 != null ? arg1 : Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

		@Override
		public int getNumberOfArgs() {
			return arg1 != null ? 1 : 0;
		}

		@Override
		public Value getUnknownArg() {
			throw new RuntimeException("Calling getUnknownArg but number of arguments is not unknown");
		}

		@Override
		public boolean isUnknownNumberOfArgs() {
			return false;
		}

		@Override
		public int getResultVar() {
			return Node.NO_VALUE;
		}

		@Override
		public int getBaseVar() {
			return Node.NO_VALUE;
		}
	}

	/**
	 * Enters a function described by a CallInfo.
	 */

	public static void callFunction(CallInfo<? extends Node> call, State caller_state, Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(caller_state.getDependency());
		// ##################################################
		
		// ==================================================
		DependencyExpressionNode node = new DependencyExpressionNode(new DependencyLabel(Label.CALL, c.getCurrentNode()));
		node.addParent(caller_state);
		// ==================================================

		Value funval = call.getFunction();

		// ##################################################
		dependency.join(funval.getDependency());
		// ##################################################

		// ==================================================
		node.addParent(funval);
		// ==================================================
		
		boolean maybe_non_function = funval.isMaybePrimitive();
		boolean maybe_function = false;
		for (ObjectLabel objlabel : funval.getObjectLabels()) {
			if (objlabel.getKind() == Kind.FUNCTION) {
				maybe_function = true;
				if (objlabel.isNative()) { // native function
					State newstate = caller_state.clone();
					Set<ExecutionContext> old_ec;
					if (!call.isConstructorCall()) {
						old_ec = newstate.getExecutionContext();
						newstate.setExecutionContext(ExecutionContext.make(old_ec, call.prepareThis(caller_state, newstate)));
					} else
						old_ec = null;
					State ts = c.getCurrentState();
					c.setCurrentState(newstate);
					Value res = NativeFunctions.evaluate(objlabel.getNativeObjectID(), call, newstate, c);

					// ##################################################
					res = res.joinDependency(dependency);
					// ##################################################

					// ==================================================
					res = res.setDependencyGraphReference(node.getReference());
					// ==================================================
					
					c.setCurrentState(ts);
					if ((!res.isBottom() && !newstate.isEmpty()) || Options.isPropagateDeadFlow()) {
						if (old_ec != null)
							newstate.setExecutionContext(newSet(old_ec));
						Node n = call.getSourceNode();
						if (call.getResultVar() != CallNode.NO_VALUE)
							newstate.writeTemporary(call.getResultVar(), res);
						c.joinBlockEntry(newstate, n.getBlock().getSingleSuccessor(), c.getCurrentContext());
					}
				} else
					// user-defined function
					FunctionCalls.enterUserFunction(objlabel, call, caller_state, c);
			} else
				maybe_non_function = true;
		}
		if (funval.getObjectLabels().isEmpty() && Options.isPropagateDeadFlow()) {
			State newstate = caller_state.clone();
			if (call.getResultVar() != CallNode.NO_VALUE)
				newstate.writeTemporary(call.getResultVar(), Value.makeBottom(dependency, node.getReference()));
			c.joinBlockEntry(newstate, call.getSourceNode().getBlock().getSingleSuccessor(), c.getCurrentContext());
		}
		Status s = maybe_non_function ? (maybe_function ? Status.MAYBE : Status.CERTAIN) : Status.NONE;
		c.addMessage(s, Severity.HIGH, "TypeError, call to non-function");
		if (s != Status.NONE) {
			Exceptions.throwTypeError(caller_state, c);
			if (c.isScanning())
				c.getStatistics().registerCallToNonFunction(c.getCurrentNode());
		}
	}

	/**
	 * Enters a user-defined function.
	 */
	public static void enterUserFunction(ObjectLabel obj_f, CallInfo<? extends Node> call, State caller_state, Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(caller_state.getDependency());
		// ##################################################

		// ==================================================
		DependencyGraphReference reference = new DependencyGraphReference();
		reference.join(caller_state.getDependencyGraphReference());
		// ==================================================

		Function f = c.getFlowGraph().getFunction(obj_f);
		Node n = call.getSourceNode();

		if (Options.isDebugEnabled())
			System.out.println("enterUserFunction from call node " + n.getIndex() + " at " + n.getSourceLocation() + " to " + f + " at "
					+ f.getSourceLocation());

		State callee_state = caller_state.clone();

		// new stack frame
		callee_state.stackObjectLabels();
		callee_state.clearTemporaries();

		Summarized s = new Summarized();
		Set<ObjectLabel> this_objs = call.prepareThis(caller_state, callee_state);
		if (call.isConstructorCall()) {
			// 13.2.2.1-2 create new object
			ObjectLabel new_obj = new ObjectLabel(n, Kind.OBJECT); // same as
																	// new
																	// object in
																	// determineThis
			s.addDefinitelySummarized(new_obj);
			// 13.2.2.3-5 provide [[Prototype]]
			Value prototype = caller_state.readPropertyDirect(Collections.singleton(obj_f), "prototype");

			// ##################################################
			dependency.join(prototype.getDependency());
			// ##################################################

			// ==================================================
			reference.join(prototype.getDependencyGraphReference());
			// ==================================================
			
			if (prototype.isMaybePrimitive())
				prototype = prototype.restrictToObject().joinObject(InitialStateBuilder.OBJECT_PROTOTYPE);
			callee_state.writeInternalPrototype(new_obj, prototype);
		}
		// 10.2.3 enter new execution context, 13.2.1 transfer parameters,
		// 10.1.6/8 provide 'arguments' object
		ObjectLabel varobj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ACTIVATION); // better
																							// to
																							// use
																							// entry
																							// than
																							// invoke
																							// here
		callee_state.newObject(varobj);
		s.addDefinitelySummarized(varobj);
		ObjectLabel argobj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
		callee_state.newObject(argobj);
		s.addDefinitelySummarized(argobj);
		Set<ScopeChain> sc = ScopeChain.make(varobj, caller_state.readObjectScope(obj_f));
		if (sc.isEmpty()) {
			// throw new RuntimeException("Empty scope chain set!?");
			return; // test/wala/upward.js
		}
		callee_state.setExecutionContext(ExecutionContext.make(sc, varobj, this_objs));
		callee_state.declareAndWriteVariable("arguments", Value.makeObject(argobj, dependency, reference));
		callee_state.writeInternalPrototype(argobj, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, dependency, reference));
		callee_state.writeSpecialProperty(argobj, "callee", Value.makeObject(obj_f, dependency, reference).setAttributes(true, false, false));
		int num_formals = f.getParameterNames().size();
		int num_actuals = call.getNumberOfArgs();
		boolean num_actuals_unknown = call.isUnknownNumberOfArgs();
		callee_state.writeSpecialProperty(argobj, "length",
				(num_actuals_unknown ? Value.makeAnyNumUInt(dependency, reference) : Value.makeNum(num_actuals, new Dependency(), new DependencyGraphReference())).setAttributes(true, false, false));
		if (num_actuals_unknown)
			callee_state.writeSpecialUnknownArrayProperty(argobj, call.getUnknownArg().setAttributes(true, false, false));
		for (int i = 0; i < num_formals || (!num_actuals_unknown && i < num_actuals); i++) {
			Value v;
			if (num_actuals_unknown || i < num_actuals) {
				v = call.getArg(i).summarize(s);
				callee_state.writeSpecialProperty(argobj, Integer.toString(i), v.setAttributes(true, false, false));
			} else
				v = Value.makeUndef(dependency, reference);
			if (i < num_formals)
				callee_state.declareAndWriteVariable(f.getParameterNames().get(i), v);
		}
		// FIXME: properties of 'arguments' should be shared with the formal
		// parameters (see 10.1.8 item 4) - easy solution that does not require
		// Reference types?
		// (see comment at NodeTransfer/WriteVariable... - also needs the other
		// way around...)

		CallContext caller_context = c.getCurrentContext();
		CallContext callee_context = new CallContext(callee_state, f, caller_context, n);
		c.joinFunctionEntry(callee_state, n, caller_context, f, callee_context);
	}

	/**
	 * Determines the value of 'this' when entering a function (other than
	 * call/apply and other than constructor call to native function). May have
	 * side-effects on callee_state.
	 */
	public static Set<ObjectLabel> determineThis(Node n, State caller_state, State callee_state, Solver.SolverInterface c, boolean isConstructorCall,
			int baseVar) {
		Set<ObjectLabel> this_obj;
		if (isConstructorCall) {
			// 13.2.2.1-2 create new object
			ObjectLabel t = new ObjectLabel(n, Kind.OBJECT); // same as new
																// object in
																// enterUserFunction
			callee_state.newObject(t);
			this_obj = newSet();
			this_obj.add(t);
		} else {
			// 10.2.3.3 and 11.2.3.6
			if (baseVar == Node.NO_VALUE) { // 11.2.1.5
				this_obj = newSet();
				this_obj.add(InitialStateBuilder.GLOBAL);
			} else {
				Set<ObjectLabel> t = Conversion.toObjectLabels(callee_state, n, caller_state.readTemporary(baseVar), c); // TODO:
																															// likely
																															// loss
																															// of
																															// precision
																															// if
																															// multiple
																															// object
																															// labels
																															// (or
																															// a
																															// summary
																															// object)
																															// as
																															// 'this'
																															// value
				this_obj = newSet();
				// 10.1.6: replace activation objects by the global object
				for (ObjectLabel objlabel : t)
					if (objlabel.getKind() == Kind.ACTIVATION)
						this_obj.add(InitialStateBuilder.GLOBAL);
					else
						this_obj.add(objlabel);
			}
		}
		return this_obj;
	}

	/**
	 * Determines the value of 'this' when entering a function (other than
	 * call/apply and other than constructor call to native function). May have
	 * side-effects on callee_state.
	 */
	public static Set<ObjectLabel> determineThis(CallNode n, State caller_state, State callee_state, Solver.SolverInterface c) {
		return determineThis(n, caller_state, callee_state, c, n.isConstructorCall(), n.getBaseVar());
	}

	/**
	 * Determines the value of 'this' when entering a function (other than
	 * call/apply and other than constructor call to native function). May have
	 * side-effects on callee_state.
	 */
	public static Set<ObjectLabel> determineThis(CallInfo<? extends Node> i, Node n, State caller_state, State callee_state, Solver.SolverInterface c) {
		return determineThis(n, caller_state, callee_state, c, i.isConstructorCall(), i.getBaseVar());
	}

	/**
	 * Leaves a user-defined function.
	 */
	public static void leaveUserFunction(Value returnval, boolean exceptional, Function f, State state, Solver.SolverInterface c,
			NodeAndContext<CallContext> specific_caller) {

		if (f.isMain()) {
			final String msgkey = "Uncaught exception";
			Node n = f.getOrdinaryExit().getLastNode();
			if (exceptional) {
				Value v = returnval.restrictToNonObject();
				if (v.isMaybeValue())
					c.addMessage(n, Status.CERTAIN, Severity.LOW, msgkey, "Uncaught exception: " + v);
				TreeSet<SourceLocation> objs = new TreeSet<SourceLocation>(returnval.getObjectSourceLocations());
				if (!objs.isEmpty())
					c.addMessage(n, Status.CERTAIN, Severity.LOW, msgkey, "Uncaught exception, constructed at " + objs);
			} else {
				if (returnval.isMaybeValue())
					c.addMessage(n, Status.NONE, Severity.LOW, msgkey, "");
			}
			return; // do nothing when leaving the main function
		}

		if (Options.isDebugEnabled())
			System.out.println("leaveUserFunction from " + f + " at " + f.getSourceLocation());

		// collect garbage
		if (!c.isScanning()) {
			state.clearTemporaries();
			state.writeTemporary(0, returnval); // ensures that returnval is
												// treated as live

			state.sharpen();
			state.clearTemporaries();
		}

		if (specific_caller != null)
			leaveUserFunction(specific_caller, returnval, exceptional, f, state, state.readThis(), c);
		else {
			// try each call node that calls f with the current callee context
			Set<NodeAndContext<CallContext>> es = c.getCallSources(f, c.getCurrentContext());
			for (Iterator<NodeAndContext<CallContext>> i = es.iterator(); i.hasNext();) {
				NodeAndContext<CallContext> p = i.next();
				leaveUserFunction(p, returnval, exceptional, f, i.hasNext() ? state.clone() : state, state.readThis(), c);
			}
		}
	}

	private static void leaveUserFunction(NodeAndContext<CallContext> p, Value returnval, boolean exceptional, Function f, State state, Value thisval,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Node node = p.getNode();
		CallContext caller_context = p.getContext();
		boolean isConstructor;
		int resultVar;
		if (node instanceof CallNode) {
			CallNode call_node = (CallNode) node;
			isConstructor = call_node.isConstructorCall();
			resultVar = call_node.getResultVar();
		} else if (node instanceof EventDispatcherNode) {
			isConstructor = false;
			resultVar = Node.NO_VALUE;
		} else
			return; // FIXME: implicit function call to valueOf/toString (node
					// may then not be first in its block!), how do we handle
					// such return flow? by additional call nodes?

		if (Options.isDebugEnabled()) {
			System.out.println("trying call node " + node.getIndex() + ": " + node + " at " + node.getSourceLocation());
			System.out.println("caller context: " + caller_context + ", callee context: " + c.getCurrentContext());
		}

		// merge newstate with caller state and call edge state
		Summarized callee_summarized = new Summarized(state.getSummarized());
		ObjectLabel activation_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ACTIVATION);
		callee_summarized.addDefinitelySummarized(activation_obj);
		ObjectLabel arguments_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
		callee_summarized.addDefinitelySummarized(arguments_obj);
		if (isConstructor) {
			ObjectLabel this_obj = new ObjectLabel(node, Kind.OBJECT);
			callee_summarized.addDefinitelySummarized(this_obj);
		} // FIXME: =======> prepareThis may create additional objects!!!
		state.mergeFunctionReturn(c.getCallState(node, caller_context), c.getCallEdgeState(node, caller_context, f), callee_summarized);

		if (exceptional) {
			// transfer exception value to caller
			Exceptions.throwException(state, returnval, c, node, caller_context);

		} else {
			if (isConstructor && returnval.isMaybePrimitive()) {
				// 13.2.2.7-8 replace non-object by the new object (which is
				// kept in 'this')
				returnval = returnval.restrictToObject().join(thisval);
			}

			/*
			 * ############################################################
			 * Dependency: join function call dependency to return value
			 * ############################################################
			 */

			// ##################################################
			if (node instanceof CallNode) {
				CallNode call_node = (CallNode) node;
				dependency.join(state.readTemporary(call_node.getFunctionVar()).getDependency());
				returnval.joinDependency(dependency);
			}
			// ##################################################

			// transfer ordinary return value to caller
			if (resultVar != Node.NO_VALUE)
				state.writeTemporary(resultVar, returnval);

			// flow to next basic block after call_node
			c.joinBlockEntry(state, node.getBlock().getSingleSuccessor(), caller_context);
		}
	}
}
