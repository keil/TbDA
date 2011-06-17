package dk.brics.tajs.analysis.nativeobjects;

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
 * 15.7 native Number functions.
 */
public class JSNumber {

	private JSNumber() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.NUMBER)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeBottom(new Dependency());

		switch (nativeobject) {

		case NUMBER: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			Value v;
			if (call.isUnknownNumberOfArgs())
				v =  Conversion.toNumber(NativeFunctions.readParameter(call, 0), c).joinNum(+0.0d);
			else if (call.getNumberOfArgs() >= 1)
				v = Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			else
				v = Value.makeNum(+0.0d, dependency);
			
			// ##################################################
			dependency.join(v.getDependency());
			// ##################################################		

			// ==================================================
			node.addParent(v);
			// ==================================================
			
			if (call.isConstructorCall()) { // 15.7.2
				ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.NUMBER);
				state.newObject(objlabel);
				state.writeInternalValue(objlabel, v);
				state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.NUMBER_PROTOTYPE, dependency).joinDependencyGraphReference(node));
				return Value.makeObject(objlabel, dependency).joinDependencyGraphReference(node);
			} else // 15.7.1
				return v.joinDependencyGraphReference(node);
		}

		case NUMBER_TOFIXED: // 15.7.4.5
		case NUMBER_TOEXPONENTIAL: // 15.7.4.6
		case NUMBER_TOPRECISION: { // 15.7.4.7
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
				return Value.makeBottom(dependency).joinDependencyGraphReference(node);
			
			Value f;
			if (call.isUnknownNumberOfArgs())
				f = Conversion.toInteger(NativeFunctions.readParameter(call, 0), c).joinNum(0);
			else if (call.getNumberOfArgs() >= 1)
				f = Conversion.toInteger(NativeFunctions.readParameter(call, 0), c);
			else
				f = Value.makeNum(0, dependency).joinDependencyGraphReference(node);
			
			Value x = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(f.getDependency());
			dependency.join(x.getDependency());
			// ##################################################

			// ==================================================
			node.addParent(f);
			node.addParent(x);
			// ==================================================
			
			boolean definitely_rangeerror = false;
			boolean maybe_rangeerror = false;
			int f_int = 0;
			if (f.isMaybeSingleNum()) {
				f_int = f.getNum().intValue();
				if (f_int < 0 || f_int > 20)
					definitely_rangeerror = true;
			} else
				maybe_rangeerror = true;
			if (maybe_rangeerror || definitely_rangeerror)
				Exceptions.throwRangeError(state, c);
			c.addMessage(call.getSourceNode(), definitely_rangeerror ? Status.CERTAIN : maybe_rangeerror ? Status.MAYBE : Status.NONE, 
					Severity.HIGH, "RangeError in Number function");
			if (definitely_rangeerror)
				return Value.makeBottom(dependency).joinDependencyGraphReference(node);
			if (x.isMaybeNaN())
				return Value.makeStr("NaN", dependency).joinDependencyGraphReference(node);
			if (x.isMaybeFuzzyNum() || f.isMaybeFuzzyNum())
				return Value.makeAnyStr(dependency).joinDependencyGraphReference(node);
			double x_num = x.getNum();
			if (Double.isInfinite(x_num)) {
				StringBuilder sb = new StringBuilder();
				if (x_num < 0) {
					x_num = -x_num; // FIXME: unused!? (Peter)
					sb.append('-');
				}
				sb.append("Infinity");
				return Value.makeStr(sb.toString(), dependency).joinDependencyGraphReference(node);
			}
			// rough approximation, best effort with Java formatting
			// TODO: improve NUMBER_TOEXPONENTIAL, TO_FIXED, TO_PRECISION?
			// probably an implementation of the specification is required
			// alternative: steal from org.mozilla.javascript
			// however, the needed classes are not public! Eg. DToA.java
			String format;
			switch (nativeobject) {
			case NUMBER_TOFIXED:
				format = "%%1.%df";
				break;
			case NUMBER_TOEXPONENTIAL:
				format = "%%1.%de";
				break;
			case NUMBER_TOPRECISION:
				format = "%%1.%dg";
				break;
			default:
				throw new RuntimeException();
			}
			String flag = String.format(format, f_int);
			String result = String.format(flag, x_num);
			return Value.makeStr(result, dependency).joinDependencyGraphReference(node);
		}		
		
		case NUMBER_TOLOCALESTRING: // 15.7.4.3 
		case NUMBER_TOSTRING: { // 15.7.4.2
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
				return Value.makeBottom(dependency);
			
			Value val = state.readInternalValue(state.readThisObjects());
			
			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			if (call.getNumberOfArgs() == 1) {
				Value radix = Conversion.toInteger(NativeFunctions.readParameter(call, 0), c);
				// ##################################################
				dependency.join(radix.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(radix);
				// ==================================================
			}
			
			if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() == 0)
				return Conversion.toString(val, c).joinDependencyGraphReference(node);
			else
				return Value.makeAnyStr(dependency).joinDependencyGraphReference(node);
			// TODO: assuming that toLocaleString methods behave as toString (also other objects) - OK?
		}
		
		case NUMBER_VALUEOF: { // 15.7.4.4
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################	

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
				return Value.makeBottom(dependency).joinDependencyGraphReference(node);
			return state.readInternalValue(state.readThisObjects()).joinDependencyGraphReference(node);
		}
			
		default:
			return null;
		}
	}
}
