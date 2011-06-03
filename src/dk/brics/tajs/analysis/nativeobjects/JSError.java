package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

/**
 * 15.11 native Error functions.
 */
public class JSError {

	private JSError() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject == ECMAScriptObjects.ERROR_TOSTRING)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeBottom(new Dependency());

		switch (nativeobject) {

		case ERROR: { // 15.11.1 / 15.11.2 (function calls act like constructors here, also below)
			return createErrorObject(InitialStateBuilder.ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case EVAL_ERROR: { // 15.11.6.1
			return createErrorObject(InitialStateBuilder.EVAL_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case RANGE_ERROR: { // 15.11.6.2
			return createErrorObject(InitialStateBuilder.RANGE_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case REFERENCE_ERROR: { // 15.11.6.3
			return createErrorObject(InitialStateBuilder.REFERENCE_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case SYNTAX_ERROR: { // 15.11.6.4
			return createErrorObject(InitialStateBuilder.SYNTAX_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case TYPE_ERROR: { // 15.11.6.5
			return createErrorObject(InitialStateBuilder.TYPE_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case URI_ERROR: { // 15.11.6.6
			return createErrorObject(InitialStateBuilder.URI_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case ERROR_TOSTRING: { // 15.11.4.4
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Value val = state.readInternalValue(state.readThisObjects());
			
			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			return Value.makeAnyStr(dependency);
		}
			
		default:
			return null;
		}
	}
	
	/**
	 * Creates a new error object according to 15.11.1.1 / 15.11.2.1 / 15.11.7.2 / 15.11.7.4.
	 */
	private static Value createErrorObject(ObjectLabel proto, ECMAScriptObjects nativeobject, CallInfo call, 
			State state, Solver.SolverInterface c) {
		// ##################################################
		Dependency dependency = new Dependency();
		// ##################################################
		
		NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
		Value message = NativeFunctions.readParameter(call, 0);

		// ##################################################
		dependency.join(message.getDependency());
		// ##################################################
		
		ObjectLabel obj = new ObjectLabel(call.getSourceNode(), Kind.ERROR);
		state.newObject(obj);
		state.writeInternalPrototype(obj, Value.makeObject(proto, dependency));
		
		if (message.isMaybeOtherThanUndef())
			state.writeProperty(obj, "message", Conversion.toString(message.restrictToNotUndef(), c).removeAttributes());
		return Value.makeObject(obj, dependency);
	}
}
