package dk.brics.tajs.analysis.nativeobjects;

import java.util.Set;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.Label;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;

/**
 * 15.4 native Array functions.
 */
public class JSArray {

	private JSArray() {
	}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.ARRAY)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeBottom(new Dependency());

		switch (nativeobject) {

		case ARRAY: { // 15.4, no difference between function and constructor
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);

			if (call.isUnknownNumberOfArgs()) { // TODO: warn about this case?
				state.writeUnknownArrayProperty(objlabel, call.getUnknownArg());
				state.writeSpecialProperty(objlabel, "length", Value.makeAnyNumUInt(call.getUnknownArg().getDependency()).setAttributes(true, true, false));
			} else if (call.getNumberOfArgs() == 1) { // 15.4.2.2
				Value lenarg = call.getArg(0);

				// ##################################################
				dependency.join(lenarg.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(lenarg);
				// ==================================================

				Status s;
				Value length = null;
				if (lenarg.isMaybeSingleNum()) {
					double d = lenarg.getNum();
					if (d >= 0 && d < 2147483647d && Math.floor(d) == d) {
						s = Status.NONE;
						length = Value.makeNum(d, dependency).joinDependencyGraphReference(node);
					} else
						s = Status.CERTAIN;
				} else if (lenarg.isMaybeNumUInt() && !lenarg.isMaybeNumNotUInt() && !lenarg.isMaybeInf() && !lenarg.isMaybeNaN()) {
					s = Status.NONE;
					length = Value.makeAnyNumUInt(lenarg.getDependency());
				} else if (!lenarg.isMaybeNumUInt() && (lenarg.isMaybeNumNotUInt() || lenarg.isMaybeInf() || lenarg.isMaybeNaN()))
					s = Status.CERTAIN;
				else if (lenarg.isMaybeFuzzyNum()) {
					s = Status.MAYBE;
					length = Value.makeAnyNumUInt(dependency).joinDependencyGraphReference(node);
				} else {
					s = Status.NONE;
					length = Value.makeBottom(dependency).joinDependencyGraphReference(node);
				}
				if (s == Status.CERTAIN && lenarg.isMaybeOtherThanNum())
					s = Status.MAYBE;
				c.addMessage(call.getSourceNode(), s, Severity.HIGH, "RangeError, invalid value of array length");
				if (s != Status.NONE)
					Exceptions.throwRangeError(state, c);
				if (s == Status.CERTAIN)
					return Value.makeBottom(dependency).joinDependencyGraphReference(node);
				if (lenarg.isMaybeOtherThanNum()) {
					length = length.joinNum(1);
					Value zeroprop = lenarg.restrictToNotNum();
					if (!lenarg.isNotNum())
						zeroprop = zeroprop.joinAbsent();
					state.writeProperty(objlabel, "0", zeroprop);
				}
				state.writeSpecialProperty(objlabel, "length", length.setAttributes(true, true, false).joinDependency(dependency));
			} else { // 15.4.2.1
				// ##########
				state.writeSpecialProperty(objlabel, "length", Value.makeNum(call.getNumberOfArgs(), dependency).setAttributes(true, true, false));
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					state.writeProperty(objlabel, Integer.toString(i), call.getArg(i));
					// ##################################################
					dependency.join(call.getArg(i).getDependency());
					// ##################################################

					// ==================================================
					node.addParent(call.getArg(i));
					// ==================================================
				}
			}
			Value res = Value.makeObject(objlabel, dependency);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE, dependency));

			return res.joinDependencyGraphReference(node);
		}

		case ARRAY_TOSTRING: // 15.4.4.2
		case ARRAY_TOLOCALESTRING: // 15.4.4.3
		case ARRAY_JOIN: { // 15.4.4.5
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

			if (nativeobject == ECMAScriptObjects.ARRAY_JOIN) {
				NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
				Value arg = Conversion.toInteger(call.getArg(0), c);

				// ##################################################
				dependency.join(arg.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(arg);
				// ==================================================

			} else {
				if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.ARRAY))
					return Value.makeBottom(dependency);
				NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			}
			return Value.makeAnyStr(dependency).joinDependencyGraphReference(node); // TODO:
																					// improve
																					// precision?
			// String sep;
			// if (params == 2)
			// sep = ",";
			// else {
			// sep =
			// Conversion.toString(call.getArg(first_param)).getStrValue();
			// if (sep == null)
			// return Value.makeMaybeStr();
			// }
			// Value thisArray = call.getArg(first_param-1);
			// Value lengthV =
			// Conversion.toNumber(before.readProperty(thisArray.getObjectLabels(),
			// "length"));
			// if (lengthV.getNumValue() == null)
			// return Value.makeMaybeStr();
			// int length = Conversion.toInt32(lengthV.getNumValue());
			// String resString = "";
			// if (length == 0) return Value.makeStr("");
			// for (int i = 0; i < length; i++) {
			// Set<ObjectLabel> objlabels = thisArray.getObjectLabels();
			// String s = Integer.toString(i);
			// solver.checkPropertyPresent(objlabels, s, false, false);
			// Value v = Conversion.toString(before.readProperty(objlabels, s));
			// if (v.isMaybeStr())
			// return Value.makeMaybeStr();
			// String vs = v.getStrValue();
			// if (vs == null || vs.equals("undefined"))
			// resString = resString + sep;
			// else
			// resString = resString + vs + sep;
			// }
			// return Value.makeStr(resString.substring(0,
			// resString.length()-sep.length()));
		}

		case ARRAY_CONCAT: { // 15.4.4.4
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);

			Value v = state.readUnknownArrayIndex(state.readThisObjects());
			if (call.isUnknownNumberOfArgs()) {
				v = v.join(call.getUnknownArg());

				// ##################################################
				dependency.join(v.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(v);
				// ==================================================

			} else {
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					v = v.join(call.getArg(i));
					// ##################################################
					dependency.join(v.getDependency());
					// ##################################################

					// ==================================================
					node.addParent(v);
					// ==================================================
				}
			}
			Value res = Value.makeObject(objlabel, dependency).joinDependencyGraphReference(node);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE, dependency));

			state.writeUnknownArrayProperty(objlabel, v);
			state.writeSpecialProperty(objlabel, "length", Value.makeAnyNumUInt(dependency).setAttributes(true, true, false));
			return res; // TODO: improve precision?
			// Value res = Value.makeBottom();
			// ObjectLabel conc = new ObjectLabel(node.getFunction().getIndex(),
			// call.getProgramCounter(), Kind.ARRAY, node.getSourceLocation());
			// after.newObject(conc);
			// res = res.add(conc);
			// Value finalLength = Value.makeNum(0);
			// int n = 0,k = 0;
			// boolean exactIndexKnown = true;
			// while (n < params-1) {
			// Value arr = call.getArg(first_param+n-1);
			// Value length =
			// Conversion.toNumber(before.readProperty(arr.getObjectLabels(),
			// "length"));
			// boolean isMaybeNotArray = false;
			// for (ObjectLabel ol : arr.getObjectLabels())
			// if (ol.getKind() != Kind.ARRAY)
			// isMaybeNotArray = true;
			// if (!arr.isNotBool() || !arr.isNotNull() || !arr.isNotNum() ||
			// !arr.isNotStr() || !arr.isNotUndef()) isMaybeNotArray = true;
			// if (isMaybeNotArray && arr.getObjectLabels().size() > 1)
			// exactIndexKnown = false;
			// if (length.isFuzzyNum() || !exactIndexKnown) {
			// exactIndexKnown = false;
			// finalLength = Value.makeMaybeNumNotNaN();
			// solver.checkUnknownArrayIndexPresent(arr.getObjectLabels());
			// after.writeUnknownArrayIndex(conc,
			// after.readUnknownArrayIndex(arr.getObjectLabels()));
			// }
			// else {
			//
			// double len = length.getNumValue() == null ? 0 :
			// length.getNumValue();
			// finalLength = Value.makeNum(finalLength.getNumValue() + len);
			// Value kth = isMaybeNotArray ? arr : Value.makeBottom();
			// if (len == 0 && isMaybeNotArray) {
			// after.writeProperty(conc, Integer.toString(k++), kth);
			// finalLength = Value.makeNum(finalLength.getNumValue() + 1);
			// }
			// else
			// for (int i = 0; i < len; i++) {
			// String s = Integer.toString(i);
			// solver.checkPropertyPresent(arr.getObjectLabels(), s, false,
			// false);
			// Value ith = before.readProperty(arr.getObjectLabels(), s);
			// if (isMaybeNotArray && i > 0) ith = ith.joinMaybeAbsent();
			// after.writeProperty(conc, Integer.toString(k++), i == 0 ?
			// ith.join(kth) : ith);
			// }
			// }
			// n++;
			// }
			// after.writeSpecialProperty(conc, "length", finalLength, true,
			// true, false);
			// return res;
		}

		case ARRAY_POP: { // 15.4.4.6
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Set<ObjectLabel> thisobj = state.readThisObjects();
			Value res = state.readUnknownArrayIndex(thisobj);
			state.deleteUnknownArrayProperty(thisobj);
			state.writeSpecialProperty(thisobj, "length", Value.makeAnyNumUInt(res.getDependency()).setAttributes(true, true, false));
			return res.joinDependencyGraphReference(node); // TODO: improve
															// precision?
			// NativeFunctions.expectZeroParameters(solver, node, params,
			// first_param);
			// Value arr = call.getArg(first_param-1);
			// Value len =
			// Conversion.toNumber(before.readProperty(arr.getObjectLabels(),
			// "length"));
			// if (len.isFuzzyNum()) {
			// after.deleteUnknownProperty(arr.getObjectLabels());
			// solver.checkUnknownArrayIndexPresent(arr.getObjectLabels());
			// return
			// after.readUnknownArrayIndex(arr.getObjectLabels()).joinMaybeUndef();
			// }
			//
			// long leni = Conversion.toUInt32(len.getNumValue());
			// if (leni == 0) { // step 4-5
			// after.writeSpecialProperty(arr.getObjectLabels(), "length",
			// Value.makeNum(0), true, true, false);
			// NativeFunctions.updateArrayLength(solver, node, before, after,
			// arr.getObjectLabels(), Value.makeStr("length"),
			// Value.makeNum(0));
			// return Value.makeUndef();
			// } else {
			// String propertyname = Long.toString(leni - 1);
			// solver.checkPropertyPresent(arr.getObjectLabels(), propertyname,
			// false, false);
			// Value elm = after.readProperty(arr.getObjectLabels(),
			// propertyname);
			// if (!elm.isDontEnum()) {
			// after.deleteProperty(arr.getObjectLabels(), propertyname);
			// after.writeSpecialProperty(arr.getObjectLabels(), "length",
			// Value.makeMaybeNumNotNaN(), true, true, false);
			// }
			// return elm;
			// }
		}

		case ARRAY_PUSH: { // 15.4.4.7
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			Set<ObjectLabel> arr = state.readThisObjects();
			if (call.isUnknownNumberOfArgs()) {
				Value v = call.getUnknownArg();

				// ##################################################
				dependency.join(v.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(v);
				// ==================================================

				state.writeUnknownArrayProperty(arr, v);
			} else { // TODO: improve precision?
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					Value v = call.getArg(i);

					// ##################################################
					dependency.join(v.getDependency());
					// ##################################################

					// ==================================================
					node.addParent(v);
					// ==================================================

					state.writeUnknownArrayProperty(arr, v);
				}
			}
			state.writeSpecialProperty(arr, "length", Value.makeAnyNumUInt(dependency).setAttributes(true, true, false));
			return Value.makeAnyNumUInt(dependency).joinDependencyGraphReference(node);
		}

		case ARRAY_REVERSE: { // 15.4.4.8
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Set<ObjectLabel> thisobj = state.readThisObjects();
			state.writeUnknownArrayProperty(thisobj, state.readUnknownArrayIndex(thisobj));
			return Value.makeObject(thisobj, state.readUnknownArrayIndex(thisobj).getDependency()); // TODO:
																									// improve
																									// precision?
			// NativeFunctions.expectZeroParameters(solver, node, params,
			// first_param);
			// Value arr = call.getArg(first_param-1);
			// Value len =
			// Conversion.toNumber(before.readProperty(arr.getObjectLabels(),
			// "length"));
			// if (len.isFuzzyNum()) {
			// solver.checkUnknownArrayIndexPresent(arr.getObjectLabels());
			// after.writeUnknownArrayIndex(arr.getObjectLabels(),
			// after.readUnknownArrayIndex(arr.getObjectLabels()));
			// } else {
			// long leni = Conversion.toUInt32(len.getNumValue());
			// if (leni == 0)
			// return arr;
			// ArrayList<Value> vals = new ArrayList<Value>();
			// for (int i = 0; i < leni; i++) {
			// String s = Integer.toString(i);
			// solver.checkPropertyPresent(arr.getObjectLabels(), s, false,
			// false);
			// vals.add(before.readProperty(arr.getObjectLabels(), s));
			// }
			// Collections.reverse(vals);
			// for (int i = 0; i < leni; i++)
			// after.writeProperty(arr.getObjectLabels(), Integer.toString(i),
			// vals.get(i));
			// }
			// return arr;
		}

		case ARRAY_SHIFT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Set<ObjectLabel> thisobj = state.readThisObjects();
			Value res = state.readUnknownArrayIndex(thisobj);

			// ##################################################
			dependency.join(res.getDependency());
			// ##################################################

			// ==================================================
			node.addParent(res);
			// ==================================================

			state.deleteUnknownArrayProperty(thisobj);
			state.writeSpecialProperty(thisobj, "length", Value.makeAnyNumUInt(dependency).setAttributes(true, true, false));
			return res.joinDependencyGraphReference(node); // TODO: improve
															// precision?
			// NativeFunctions.expectZeroParameters(solver, node, params,
			// first_param);
			// Value arr = call.getArg(first_param-1);
			// Value len =
			// Conversion.toNumber(before.readProperty(arr.getObjectLabels(),
			// "length"));
			// if (len.isFuzzyNum()) {
			// solver.checkUnknownArrayIndexPresent(arr.getObjectLabels());
			// after.writeUnknownArrayIndex(arr.getObjectLabels(),
			// after.readUnknownArrayIndex(arr.getObjectLabels()));
			// return after.readUnknownArrayIndex(arr.getObjectLabels());
			// }
			// else {
			// long leni = Conversion.toUInt32(len.getNumValue());
			// if (leni == 0)
			// return Value.makeUndef();
			// String s = "0";
			// solver.checkPropertyPresent(arr.getObjectLabels(), s, false,
			// false);
			// Value zelm = before.readProperty(arr.getObjectLabels(), s);
			// after.writeSpecialProperty(arr.getObjectLabels(), "length",
			// zelm.joinMaybeAbsent(), true, true, false);
			// for (int i = 1; i < leni; i++) {
			// Value isDef = before.hasProperty(arr.getObjectLabels(),
			// Integer.toString(i));
			// String s1 = Integer.toString(i);
			// solver.checkPropertyPresent(arr.getObjectLabels(), s1, false,
			// false);
			// Value oldVal = before.readProperty(arr.getObjectLabels(), s1);
			// if (isDef.isMaybeTrueButNotFalse()) {
			// after.writeProperty(arr.getObjectLabels(), Integer.toString(i-1),
			// oldVal);
			// } else if (isDef.isMaybeFalseButNotTrue()) {
			// after.deleteProperty(arr.getObjectLabels(), Integer.toString(i));
			// } else {
			// after.deleteProperty(arr.getObjectLabels(), Integer.toString(i));
			// String s2 = Integer.toString(i-1);
			// solver.checkPropertyPresent(arr.getObjectLabels(), s2, false,
			// false);
			// after.writeProperty(arr.getObjectLabels(), Integer.toString(i-1),
			// oldVal.join(before.readProperty(arr.getObjectLabels(), s2)));
			// }
			// }
			// after.writeSpecialProperty(arr.getObjectLabels(), "length",
			// Value.makeNum(leni-1), true, true, false);
			// return zelm;
			// }
		}

		case ARRAY_SLICE: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);

			Value v = state.readUnknownArrayIndex(state.readThisObjects());
			// ##################################################
			dependency.join(v.getDependency());
			// ##################################################

			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################

				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}

			Value res = Value.makeObject(objlabel, dependency);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE, dependency));

			state.writeUnknownArrayProperty(objlabel, v);
			state.writeSpecialProperty(objlabel, "length", Value.makeAnyNumUInt(dependency).setAttributes(true, true, false));
			return res.joinDependencyGraphReference(node); // TODO: improve
															// precision?
			// NativeFunctions.expectTwoParameters(solver, node, params,
			// first_param);
			// Value arr = call.getArg(first_param-1);
			// Value res = Value.makeBottom();
			// ObjectLabel slice = new
			// ObjectLabel(node.getFunction().getIndex(),
			// call.getProgramCounter(), Kind.ARRAY, node.getSourceLocation());
			// res.add(slice);
			// after.newObject(slice); // TODO: set internal prototype?
			// after.writeUnknownArrayIndex(slice,
			// before.readUnknownArrayIndex(arr.getObjectLabels()));
			// after.writeSpecialProperty(slice, "length", Value.makeMaybeNum(),
			// true, true, false);
			// return arr;
		}

		case ARRAY_SORT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			// FIXME: make sure function argument may get evaluated some number
			// of times (it may have side effects!)
			Set<ObjectLabel> thisobj = state.readThisObjects();
			state.writeUnknownArrayProperty(thisobj, state.readUnknownArrayIndex(thisobj));
			Value v = state.readUnknownArrayIndex(thisobj);
			// ##################################################
			dependency.join(v.getDependency());
			// ##################################################

			// ==================================================
			node.addParent(v);
			// ==================================================

			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################

				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}

			return Value.makeObject(thisobj, dependency).joinDependencyGraphReference(node); // TODO:
																								// improve
			// precision?
		}

		case ARRAY_SPLICE: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 2, -1);
			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);
			Value v = state.readUnknownArrayIndex(state.readThisObjects());
			if (call.isUnknownNumberOfArgs()) {
				v = v.join(call.getUnknownArg());
				// ##################################################
				dependency.join(v.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(v);
				// ==================================================
			} else {
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					v = v.join(call.getArg(i));
					// ##################################################
					dependency.join(v.getDependency());
					// ##################################################

					// ==================================================
					node.addParent(v);
					// ==================================================
				}
			}
			Value res = Value.makeObject(objlabel, dependency);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE, dependency));

			state.writeUnknownArrayProperty(objlabel, v);
			state.writeSpecialProperty(objlabel, "length", Value.makeAnyNumUInt(dependency).setAttributes(true, true, false));
			return res.joinDependencyGraphReference(node); // TODO: improve
															// precision?
		}

		case ARRAY_UNSHIFT: { // 15.4.4.13
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			Set<ObjectLabel> arr = state.readThisObjects();
			if (call.isUnknownNumberOfArgs()) {
				Value v = call.getUnknownArg();

				// ##################################################
				dependency.join(v.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(v);
				// ==================================================

				state.writeUnknownArrayProperty(arr, v);
			} else { // TODO: improve precision?
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					Value v = call.getArg(i);

					// ##################################################
					dependency.join(v.getDependency());
					// ##################################################

					// ==================================================
					node.addParent(v);
					// ==================================================

					state.writeUnknownArrayProperty(arr, v);
				}
			}
			state.writeSpecialProperty(arr, "length", Value.makeAnyNumUInt(dependency).setAttributes(true, true, false));
			return Value.makeAnyNumUInt(dependency).joinDependencyGraphReference(node);
		}

		default:
			return null;
		}
	}
}
