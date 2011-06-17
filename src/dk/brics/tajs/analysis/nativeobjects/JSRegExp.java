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
 * 15.10 native RegExp functions.
 */
public class JSRegExp { // TODO: see http://dev.opera.com/articles/view/opera-javascript-for-hackers-1/

	private JSRegExp() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.REGEXP)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeBottom(new Dependency());

		switch (nativeobject) {

		case REGEXP: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			boolean ctor = call.isConstructorCall();
			Value pattern = call.getNumberOfArgs() > 0 ? call.getArg(0) : Value.makeStr("", dependency);
			Value flags = call.getNumberOfArgs() > 1 ? call.getArg(1) : Value.makeUndef(dependency);
			Value arg = pattern.joinAnyBool();

			// ##################################################
			dependency.join(pattern.getDependency());
			dependency.join(flags.getDependency());
			// ##################################################			
			
			// ==================================================
			node.addParent(pattern);
			node.addParent(flags);
			// ==================================================
			
			Value result = Value.makeBottom(dependency).joinDependencyGraphReference(node);
			
//			// 15.10.3.1 function call
			if (flags.isMaybeUndef()) {
				for (ObjectLabel ol : pattern.getObjectLabels())
					if (ol.getKind().equals(Kind.REGEXP))
						result = result.joinObject(ol);
					else
						arg = arg.joinObject(ol);
			}
			if (flags.isMaybeOtherThanUndef()) 
				for (ObjectLabel ol : pattern.getObjectLabels())
					arg = arg.joinObject(ol);
			Status s;
			if (ctor && !flags.isMaybeUndef() && !result.getObjectLabels().isEmpty())
				s = Status.MAYBE;
			else
				s = Status.NONE;
			c.addMessage(s, Severity.HIGH, "Typerror, ");
			if (!arg.isNoValue()) {
				Value pGlobal = Value.makeAnyBool(dependency);
				Value pIgnoreCase = Value.makeAnyBool(dependency);
				Value pMultiline = Value.makeAnyBool(dependency);
				if (!flags.isMaybeUndef()) {
					Value sflags = Conversion.toString(flags,c);

					// ##################################################	
					dependency.join(sflags.getDependency());
					// ##################################################	

					// ==================================================
					node.addParent(sflags);
					// ==================================================
					
					if (!sflags.isMaybeAnyStr() && sflags.getStr() != null) {
						String strflags = sflags.getStr();
						pGlobal = Value.makeBool(strflags.indexOf("g") != -1, dependency).joinDependencyGraphReference(node);
						pIgnoreCase = Value.makeBool(strflags.indexOf("i") != -1, dependency).joinDependencyGraphReference(node);
						pMultiline = Value.makeBool(strflags.indexOf("m") != -1, dependency).joinDependencyGraphReference(node);
						strflags = strflags.replaceFirst("g", "").replaceFirst("i", "").replaceFirst("m", "");
						boolean bad = strflags.length() != 0;
						c.addMessage(bad ? Status.MAYBE : Status.NONE,Severity.HIGH, "SyntaxError in flags of RegExp constructor.");
						if (bad) { 
							Exceptions.throwSyntaxError(state, c);
							return Value.makeBottom(dependency).joinDependencyGraphReference(node);
						}
					}
				}
				// 15.10.4.1 constructor
				ObjectLabel no = new ObjectLabel(ECMAScriptObjects.REGEXP, Kind.REGEXP);
				state.newObject(no);
				state.writeInternalPrototype(no, Value.makeObject(InitialStateBuilder.REGEXP_PROTOTYPE, dependency)); 
				Value p = Conversion.toString(arg,c).join(state.readInternalValue(result.getObjectLabels()));
				state.writeInternalValue(no, p);
				state.writeSpecialProperty(no, "source", p.setAttributes(true, true, true));
				state.writeSpecialProperty(no, "lastIndex", Value.makeNum(0, dependency).setAttributes(true, true, false).joinDependencyGraphReference(node));
				state.writeSpecialProperty(no, "global", pGlobal.setAttributes(true, true, true));
				state.writeSpecialProperty(no, "ignoreCase", pIgnoreCase.setAttributes(true, true, true));
				state.writeSpecialProperty(no, "multiline", pMultiline.setAttributes(true, true, true));
				result = result.joinObject(no);
			}
			return result.joinDependencyGraphReference(node);
		}
		
		case REGEXP_EXEC: { // 15.10.6.2 (see STRING_MATCH)
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value thisval = state.readInternalValue(state.readThisObjects());
			Value arg = call.getNumberOfArgs() > 0 ? call.getArg(0) : Value.makeStr("", dependency);
			
			// ##################################################
			dependency.join(thisval.getDependency());
			dependency.join(arg.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(thisval);
			node.addParent(arg);
			// ==================================================
			
			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);
			
			Value res = Value.makeObject(objlabel, dependency);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE, dependency));
			state.writeUnknownArrayProperty(objlabel, Value.makeAnyStr(dependency).joinAbsent());
			state.writeSpecialProperty(objlabel, "length", Value.makeAnyNumUInt(dependency).setAttributes(true, true, false));
			state.writeProperty(objlabel, "index", Value.makeAnyNumUInt(dependency));
			state.writeProperty(objlabel, "input", Value.makeAnyStr(dependency)); // TODO: improve precision?
			return res.joinNull();
		}
		
		case REGEXP_TEST: { // 15.10.6.3
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value thisval = state.readInternalValue(state.readThisObjects());
			Value arg = call.getNumberOfArgs() > 0 ? call.getArg(0) : Value.makeStr("", dependency);
			
			// ##################################################
			dependency.join(thisval.getDependency());
			dependency.join(arg.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(thisval);
			node.addParent(arg);
			// ==================================================
			
			return Value.makeAnyBool(dependency); //TODO: More precision.
			//			NativeFunctions.expectOneParameter(solver, node, params, first_param);
//			Value receiver = call.getArg(first_param-1);
//			Set<ObjectLabel> objlabels = 
//				NativeFunctions.filterByKind(solver, node, receiver.getObjectLabels(), Kind.REGEXP, "RegExp.prototype.test must be applied to RegExp objects only");
//			return objlabels.isEmpty() ? Value.makeBottom() : Value.makeMaybeBool();
		}
		
		case REGEXP_TOSTRING: { // 15.10.6.4
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================
			
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value thisval = state.readInternalValue(state.readThisObjects());
			
			// ##################################################
			dependency.join(thisval.getDependency());
			// ##################################################
			
			// ==================================================
			node.addParent(thisval);
			// ==================================================
			
			return Value.makeAnyStr(dependency); 
		}
			
		default:
			return null;
		}
	}
}
