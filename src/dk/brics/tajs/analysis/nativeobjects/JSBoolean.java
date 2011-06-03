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
 * 15.6 native Boolean functions.
 */
public class JSBoolean {

	private JSBoolean() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.BOOLEAN)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeBottom(new Dependency());

		switch (nativeobject) {

		case BOOLEAN:  {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			Value b = Conversion.toBoolean(NativeFunctions.readParameter(call, 0));
			
			// ##################################################
			dependency.join(b.getDependency());
			// ##################################################
		
			if (call.isConstructorCall()) { // 15.6.2
				ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.BOOLEAN);
				state.newObject(objlabel);
				state.writeInternalValue(objlabel, b);
				state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.BOOLEAN_PROTOTYPE, dependency));
				return Value.makeObject(objlabel, dependency);
			} else // 15.6.1
				return b;
		}

		case BOOLEAN_TOSTRING: { // 15.6.4.2
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.BOOLEAN))
				return Value.makeBottom(dependency);
			Value val = state.readInternalValue(state.readThisObjects());
			
			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			if (val.isMaybeTrueButNotFalse())
				return Value.makeStr("true", dependency);
			else if (val.isMaybeFalseButNotTrue())
				return Value.makeStr("false", dependency);
			else if (val.isMaybeAnyBool()) 
				return Value.makeAnyStr(dependency); // TODO: treat {"true","false"} specially in Value?
			else
				return Value.makeBottom(dependency);
		}
		
		case BOOLEAN_VALUEOF: { // 15.6.4.3
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.BOOLEAN))
				return Value.makeBottom(dependency);
			return state.readInternalValue(state.readThisObjects());
		}
			
		default:
			return null;
		}
	}
}
