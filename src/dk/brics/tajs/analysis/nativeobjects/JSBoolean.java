package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.Conversion;
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
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), b, state);
			// ==================================================
		
			if (call.isConstructorCall()) { // 15.6.2
				ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.BOOLEAN);
				state.newObject(objlabel);
				state.writeInternalValue(objlabel, b);
				state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.BOOLEAN_PROTOTYPE, dependency).joinDependencyGraphReference(node));
				return Value.makeObject(objlabel, dependency).joinDependencyGraphReference(node);
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
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), val, state);
			// ==================================================
			
			if (val.isMaybeTrueButNotFalse())
				return Value.makeStr("true", dependency).joinDependencyGraphReference(node);
			else if (val.isMaybeFalseButNotTrue())
				return Value.makeStr("false", dependency).joinDependencyGraphReference(node);
			else if (val.isMaybeAnyBool()) 
				return Value.makeAnyStr(dependency).joinDependencyGraphReference(node); // TODO: treat {"true","false"} specially in Value?
			else
				return Value.makeBottom(dependency).joinDependencyGraphReference(node);
		}
		
		case BOOLEAN_VALUEOF: { // 15.6.4.3
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.BOOLEAN))
				return Value.makeBottom(dependency).joinDependencyGraphReference(node);
			return state.readInternalValue(state.readThisObjects()).joinDependencyGraphReference(node);
		}
			
		default:
			return null;
		}
	}
}
