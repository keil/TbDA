package dk.brics.tajs.analysis.nativeobjects;

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

/**
 * 15.9 and B.2 native Date functions.
 */
public class JSDate {

	private JSDate() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.DATE)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeBottom(new Dependency(), new DependencyGraphReference());
		
		// TODO: warn about year 2000 problem for getYear/setYear?

		switch (nativeobject) {

		case DATE: { // 15.9.3
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 7);
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################
				
				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}
			
			if (call.isConstructorCall()) {
				return createDateObject(call.getSourceNode(), state, dependency, node.getReference());
			} else // 15.9.2
				return Value.makeAnyStr(dependency, node.getReference());
		}
		
		case DATE_GETFULLYEAR: // 15.9.5.10
		case DATE_GETUTCFULLYEAR: // 15.9.5.11
		case DATE_GETMONTH: // 15.9.5.12
		case DATE_GETUTCMONTH: // 15.9.5.13
		case DATE_GETDATE: // 15.9.5.14
		case DATE_GETUTCDATE: // 15.9.5.15
		case DATE_GETDAY: // 15.9.5.16
		case DATE_GETUTCDAY: // 15.9.5.17
		case DATE_GETHOURS: // 15.9.5.18
		case DATE_GETUTCHOURS: // 15.9.5.19
		case DATE_GETMINUTES: // 15.9.5.20
		case DATE_GETUTCMINUTES: // 15.9.5.21
		case DATE_GETSECONDS: // 15.9.5.22
		case DATE_GETUTCSECONDS: // 15.9.5.23
		case DATE_GETMILLISECONDS: // 15.9.5.24
		case DATE_GETUTCMILLISECONDS: // 15.9.5.25
		case DATE_GETTIMEZONEOFFSET: // 15.9.5.26
		case DATE_GETYEAR: { // B.2.4
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_GETTIME: { // 15.9.5.9
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.DATE))
				return Value.makeBottom(dependency, node.getReference());
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_PARSE: // 15.9.4.2
		case DATE_SETDATE: // 15.9.5.36
		case DATE_SETUTCDATE: // 15.9.5.37
		case DATE_SETYEAR: { // B.2.5
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################
				
				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}
			
			Value thisValue = state.readThis().joinDependency(dependency).setDependencyGraphReference(node);
			for (ObjectLabel objectLabel : state.readThisObjects()) {
				state.writeInternalValue(objectLabel, thisValue);
			}
			
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_SETHOURS: // 15.9.5.35
		case DATE_SETUTCHOURS: { // 15.9.5.36
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 4);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################
				
				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}
			
			Value thisValue = state.readThis().joinDependency(dependency).joinDependencyGraphReference(node);
			for (ObjectLabel objectLabel : state.readThisObjects()) {
				state.writeInternalValue(objectLabel, thisValue);
			}
			
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_SETMILLISECONDS: // 15.9.5.28
		case DATE_SETUTCMILLISECONDS: { // 15.9.5.29
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################
				
				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}
			
			Value thisValue = state.readThis().joinDependency(dependency).joinDependencyGraphReference(node);
			for (ObjectLabel objectLabel : state.readThisObjects()) {
				state.writeInternalValue(objectLabel, thisValue);
			}
			
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_SETMINUTES: // 15.9.5.33
		case DATE_SETUTCMINUTES: // 15.9.5.34
		case DATE_SETFULLYEAR: // 15.9.5.40
		case DATE_SETUTCFULLYEAR: { // 15.9.5.41
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 3);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################
				
				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}
			
			Value thisValue = state.readThis().joinDependency(dependency).joinDependencyGraphReference(node);
			for (ObjectLabel objectLabel : state.readThisObjects()) {
				state.writeInternalValue(objectLabel, thisValue);
			}
			
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_SETSECONDS: // 15.9.5.30
		case DATE_SETUTCSECONDS: // 15.9.5.31
		case DATE_SETMONTH: // 15.9.5.38
		case DATE_SETUTCMONTH: { // 15.9.5.39
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################
				
				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}
			
			Value thisValue = state.readThis().joinDependency(dependency).joinDependencyGraphReference(node);
			for (ObjectLabel objectLabel : state.readThisObjects()) {
				state.writeInternalValue(objectLabel, thisValue);
			}
			
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_SETTIME: { // 15.9.5.27
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value val = state.readInternalValue(state.readThisObjects());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(val);
			// ==================================================
			
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				// ##################################################
				dependency.join(call.getArg(i).getDependency());
				// ##################################################
				
				// ==================================================
				node.addParent(call.getArg(i));
				// ==================================================
			}
			
			Value thisValue = state.readThis().joinDependency(dependency).joinDependencyGraphReference(node);
			for (ObjectLabel objectLabel : state.readThisObjects()) {
				state.writeInternalValue(objectLabel, thisValue);
			}
			
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.DATE))
				return Value.makeBottom(dependency, node.getReference());
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		case DATE_TOSTRING: // 15.9.5.2
		case DATE_TODATESTRING: // 15.9.5.3
		case DATE_TOTIMESTRING: // 15.9.5.4
		case DATE_TOLOCALESTRING: // 15.9.5.5
		case DATE_TOLOCALEDATESTRING: // 15.9.5.6
		case DATE_TOLOCALETIMESTRING: { // 15.9.5.7
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
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeAnyStr(dependency, node.getReference());
		}
		
		case DATE_TOUTCSTRING: // 15.9.5.42
		case DATE_TOGMTSTRING: { // B.2.6
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
			
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeAnyStr(dependency, node.getReference());
		}
		
		case DATE_UTC: { // 15.9.4.3
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################
			
			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 7);
			return createDateObject(call.getSourceNode(), state, dependency, node.getReference());
		}
		
		case DATE_VALUEOF: { // 15.9.5.8
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

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeAnyNum(dependency, node.getReference());
		}
		
		default:
			return null;
		}
	}
	
	/**
	 * Creates a new Date object.
	 */
	private static Value createDateObject(Node n, State state, Dependency dependency, DependencyGraphReference reference) {	
		ObjectLabel objlabel = new ObjectLabel(n, Kind.DATE);
		state.newObject(objlabel);
		state.writeInternalValue(objlabel, Value.makeAnyNum(dependency, reference));
		state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.DATE_PROTOTYPE, dependency, reference));
		return Value.makeObject(objlabel, dependency, reference);
	}
}
