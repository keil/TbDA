package dk.brics.tajs.analysis.nativeobjects;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.Label;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;

/**
 * 15.3 native Function functions.
 */
public class JSFunction {

	private JSFunction() {
	}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo<? extends Node> call, final State state, final Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.FUNCTION && nativeobject != ECMAScriptObjects.FUNCTION_PROTOTYPE)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeBottom(new Dependency(), new DependencyGraphReference());

		switch (nativeobject) {

		case FUNCTION: { // 15.3.1 / 15.3.2 (no difference between function and
							// constructor)
			return Value.makeUndef(new Dependency(), new DependencyGraphReference()); // XXX throw new
														// RuntimeException("Don't know how to handle call to 'Function' :-(");
														// // TODO: call to
														// 'Function' (just
														// issue an error
														// message and return
														// undefined?)
		}

		case FUNCTION_PROTOTYPE: { // 15.3.4
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

		case FUNCTION_TOSTRING: { // 15.3.4.2
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Value thisval = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(thisval.getDependency());
			// ##################################################

			// ==================================================
			node.addParent(thisval);
			// ==================================================

			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.FUNCTION))
				return Value.makeBottom(dependency, node.getReference());
			return Value.makeAnyStr(dependency, node.getReference());
		}

		case FUNCTION_APPLY: { // 15.3.4.3
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
			Value argarray = call.getArg(1);

			// ##################################################
			dependency.join(argarray.getDependency());
			// ##################################################

			// ==================================================
			node.addParent(argarray);
			// ==================================================

			final boolean maybe_empty = argarray.isMaybeNull() || argarray.isMaybeUndef();
			boolean maybe_typeerror = !argarray.isNotBool() || !argarray.isNotNum() || !argarray.isNotStr();
			boolean maybe_ok = false;
			boolean unknown_length = false;
			int fixed_length = -1;
			if (maybe_empty) {
				fixed_length = 0;
				maybe_ok = true;
			}
			final Set<ObjectLabel> argarrays = newSet();
			for (ObjectLabel objlabel : argarray.getObjectLabels())
				if (objlabel.getKind() == Kind.ARRAY || objlabel.getKind() == Kind.ARGUMENTS) {
					argarrays.add(objlabel);
					Value lengthval = state.readProperty(objlabel, "length");
					if (lengthval.isMaybeSingleNum()) {
						int len = lengthval.getNum().intValue();
						if (fixed_length == -1)
							fixed_length = len;
						else if (len != fixed_length)
							unknown_length = true;
					} else
						unknown_length = true;
					maybe_ok = true;
				} else
					maybe_typeerror = true;
			if (maybe_typeerror)
				Exceptions.throwTypeError(state, c);
			if (maybe_ok || maybe_typeerror)
				c.addMessage(maybe_typeerror ? maybe_ok ? Status.MAYBE : Status.CERTAIN : Status.NONE, Severity.HIGH, "TypeError, invalid arguments to 'apply'");
			if (!maybe_ok)
				return Value.makeBottom(dependency, node.getReference());
			final boolean unknown_length__final = unknown_length;
			final int fixed_length__final = fixed_length;
			FunctionCalls.callFunction(new FunctionCalls.CallInfo() { // TODO:
																		// possible
																		// infinite
																		// recursion
																		// of
																		// callFunction
																		// with
																		// apply/call?
																		// (see
																		// test109.js)

						@Override
						public Node getSourceNode() {
							return call.getSourceNode();
						}

						@Override
						public boolean isConstructorCall() {
							return false;
						}

						@Override
						public Value getFunction() {
							return state.readThis();
						}

						@Override
						public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
							return JSFunction.prepareThis(call, callee_state, c);
						}

						@Override
						public Value getArg(int i) {
							if (unknown_length__final)
								return getUnknownArg();
							else if (i < fixed_length__final) {
								Value v = state.readProperty(argarrays, Integer.toString(i));
								if (maybe_empty)
									v = v.joinUndef();
								return v;
							} else
								return Value.makeUndef(new Dependency(), new DependencyGraphReference());
						}

						@Override
						public int getNumberOfArgs() {
							if (unknown_length__final)
								return -1;
							else
								return fixed_length__final;
						}

						@Override
						public Value getUnknownArg() {
							return state.readUnknownArrayIndex(argarrays);
						}

						@Override
						public boolean isUnknownNumberOfArgs() {
							return unknown_length__final;
						}

						@Override
						public int getResultVar() {
							return call.getResultVar();
						}

						@Override
						public int getBaseVar() {
							return call.getResultVar();
						}
					}, state, c);
			return Value.makeBottom(dependency, node.getReference());
		}

		case FUNCTION_CALL: { // 15.3.4.4
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################

			// ==================================================
			node.addParent(val);
			// ==================================================

			FunctionCalls.callFunction(new FunctionCalls.CallInfo() {

//				@Override
				public Node getSourceNode() {
					return call.getSourceNode();
				}

				@Override
				public boolean isConstructorCall() {
					return false;
				}

				@Override
				public Value getFunction() {
					return state.readThis();
				}

				@Override
				public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
					return JSFunction.prepareThis(call, callee_state, c);
				}

				@Override
				public Value getArg(int i) {
					return call.getArg(i + 1);
				}

				@Override
				public int getNumberOfArgs() {
					return call.getNumberOfArgs() - 1;
				}

				@Override
				public Value getUnknownArg() {
					return call.getUnknownArg();
				}

				@Override
				public boolean isUnknownNumberOfArgs() {
					return call.isUnknownNumberOfArgs();
				}

				@Override
				public int getResultVar() {
					return call.getResultVar();
				}

				@Override
				public int getBaseVar() {
					return call.getBaseVar();
				}
			}, state, c);
			return Value.makeBottom(dependency, node.getReference()); // no
																					// direct
																					// flow
																					// to
																					// the
																					// successor
		}

		default:
			return null;
		}
	}

	private static Set<ObjectLabel> prepareThis(CallInfo<? extends Node> call, State callee_state, Solver.SolverInterface c) {
		Value thisval = call.getArg(0);
		// 15.3.4.3/4
		boolean maybe_null_or_undef = thisval.isMaybeNull() || thisval.isMaybeUndef();
		thisval = thisval.restrictToNotNullNotUndef();
		Set<ObjectLabel> this_objs = newSet(Conversion.toObjectLabels(callee_state, call.getSourceNode(), thisval, c)); // TODO:
																														// disable
																														// messages?
																														// (but
																														// not
																														// side-effects!)
		if (maybe_null_or_undef)
			this_objs.add(InitialStateBuilder.GLOBAL);
		return this_objs;
	}
}
