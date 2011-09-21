package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptFunctions;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.NativeObject;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.util.Strings;

/**
 * Dispatch evaluation of native functions and common functionality used by the
 * native functions.
 */
public class NativeFunctions {

	private NativeFunctions() {
	}

	public static void evaluateGetter(NativeObject nativeobject, ObjectLabel objlabel, String propertyname, Solver.SolverInterface c) {
		DOMFunctions.evaluateGetter(nativeobject, objlabel, propertyname, c);
	}

	public static void evaluateSetter(NativeObject nativeobject, ObjectLabel objlabel, String propertyname, Value v, Solver.SolverInterface c) {
		DOMFunctions.evaluateSetter(nativeobject, objlabel, propertyname, v, c);
	}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(NativeObject nativeobject, FunctionCalls.CallInfo<? extends Node> call, State state, Solver.SolverInterface c) {
		switch (nativeobject.getAPI()) {
		case ECMA_SCRIPT_NATIVE:
			return ECMAScriptFunctions.evaluate((ECMAScriptObjects) nativeobject, call, state, c);
		case DOCUMENT_OBJECT_MODEL:
			return DOMFunctions.evaluate((DOMObjects) nativeobject, call, state, c);
		default:
			throw new RuntimeException("Unknown or unimplemented native API");
		}
	}

	/**
	 * Issues a warning if the number of parameters is not in the given
	 * interval. max is ignored if -1.
	 */
	public static void expectParameters(NativeObject nativeobject, FunctionCalls.CallInfo<? extends Node> call, Solver.SolverInterface c, int min, int max) {
		int num_actuals = call.getNumberOfArgs();
		boolean num_actuals_unknown = call.isUnknownNumberOfArgs();
		c.addMessage((num_actuals_unknown && min > 0) ? Status.MAYBE : (!num_actuals_unknown && num_actuals < min) ? Status.CERTAIN : Status.NONE,
				Severity.MEDIUM, "Too few parameters to native function " + nativeobject);
		if (max != -1) {
			c.addMessage(num_actuals_unknown ? Status.MAYBE : num_actuals > max ? Status.CERTAIN : Status.NONE, Severity.HIGH,
					"Too many parameters to native function " + nativeobject);
			// TODO: implementations *may* throw TypeError if too many
			// parameters to native functions (p.76)
		}
	}

	/**
	 * Reads the value of a call parameter. Returns 'undefined' if too few
	 * parameters. The first parameter has number 0.
	 */
	public static Value readParameter(FunctionCalls.CallInfo<? extends Node> call, int param) {
		int num_actuals = call.getNumberOfArgs();
		boolean num_actuals_unknown = call.isUnknownNumberOfArgs();
		if (num_actuals_unknown || param < num_actuals)
			return call.getArg(param);
		else
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
	}

	/**
	 * Reads the value of a call parameter. Only to be called if the number of
	 * arguments is unknown.
	 */
	public static Value readUnknownParameter(FunctionCalls.CallInfo<? extends Node> call) {
		return call.getUnknownArg().joinUndef();
	}

	/**
	 * Updates the length property of any arrays among the given objects in
	 * accordance with 15.4.5.1. Also models truncation of the array if the
	 * 'length' property is being set. Sets the state to bottom if an exception
	 * is definitely thrown.
	 */
	public static void updateArrayLength(Node node, State state, Set<ObjectLabel> objlabels, Value propertyval, Value value, Solver.SolverInterface c) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		dependency.join(propertyval.getDependency());
		dependency.join(value.getDependency());
		// ##################################################

		// ##################################################
		dependency.join(state.readProperty(objlabels, "length").getDependency());
		// ##################################################

		// ==================================================
		DependencyGraphReference reference = new DependencyGraphReference();
		reference.join(state.getDependencyGraphReference());
		reference.join(propertyval.getDependencyGraphReference());
		reference.join(value.getDependencyGraphReference());
		reference.join(state.readProperty(objlabels, "length").getDependencyGraphReference());
		// ==================================================

		Set<ObjectLabel> arrays = newSet();
		boolean any_non_arrays = false;
		for (ObjectLabel ol : objlabels)
			if (ol.getKind() == Kind.ARRAY)
				arrays.add(ol);
			else
				any_non_arrays = true;
		if (arrays.isEmpty())
			return;
		// step 12-14 assignment to 'length'
		Str propertystr = Conversion.toString(propertyval, c);
		boolean definitely_length = propertystr.isMaybeSingleStr() && propertystr.getStr().equals("length");
		boolean maybe_length = propertystr.isMaybeStrNotUInt();
		if (definitely_length || maybe_length) {
			Value numvalue = Conversion.toNumber(value, c);
			Status s;
			if (numvalue.isMaybeSingleNum()) {
				long uintvalue = numvalue.isMaybeSingleNum() ? Conversion.toUInt32(numvalue.getNum()) : 0;
				if (uintvalue != numvalue.getNum())
					s = any_non_arrays ? Status.MAYBE : Status.CERTAIN;
				else
					s = Status.NONE;
			} else if (numvalue.isMaybeNumUInt()) {
				if (numvalue.isMaybeOtherThanNumUInt())
					s = Status.MAYBE;
				else
					s = Status.NONE;
			} else {
				if (numvalue.isMaybeOtherThanNumUInt())
					s = Status.CERTAIN;
				else
					s = Status.NONE;
			}
			if (!definitely_length && s == Status.CERTAIN)
				s = Status.MAYBE;
			c.addMessage(node, s, Severity.HIGH, "RangeError, assigning invalid value to array 'length' property");
			if (s != Status.NONE)
				Exceptions.throwRangeError(state, c);
			if (s == Status.CERTAIN && !Options.isPropagateDeadFlow()) {
				state.setToBottom();
				return;
			}
			// TODO: model array truncation more precisely for constant index
			// values? (like old impl)
			state.deleteUnknownProperty(arrays); // be conservative; could be
													// 'length' being set to 0
			state.writeSpecialProperty(arrays, "length", Value.makeAnyNumUInt(dependency, reference).setAttributes(true, true, false));
			return; // TODO: make sure maybe_length and maybe_index can both be
					// active
		}
		// step 9-10 assignment to array index
		boolean definitely_index = propertystr.isMaybeSingleStr() && Strings.isArrayIndex(propertystr.getStr());
		boolean maybe_index = propertystr.isMaybeStrUInt();
		if (definitely_index || maybe_index) {
			// FIXME: model assignment to array index more precisely for
			// constant index values? (micro/testFunctionApply.js)
			state.writeSpecialProperty(arrays, "length", Value.makeAnyNumUInt(dependency, reference).setAttributes(true, true, false));
		}
	}

	/**
	 * Throws a type error exception and issues a warning if the given call is a
	 * constructor call. Don't forget to set the ordinary state to bottom if the
	 * exception will definitely occur.
	 * 
	 * @return true if the exception is definitely thrown
	 */
	public static boolean throwTypeErrorIfConstructor(FunctionCalls.CallInfo<? extends Node> call, State state, Solver.SolverInterface c) {
		Status s;
		if (call.isConstructorCall()) {
			Exceptions.throwTypeError(state, c);
			s = Status.CERTAIN;
		} else
			s = Status.NONE;
		c.addMessage(call.getSourceNode(), s, Severity.HIGH, "TypeError, constructor call to object that cannot be used as constructor");
		return s == Status.CERTAIN;
	}

	/**
	 * Throws a type error exception and issues a warning if the kind of the
	 * 'this' object is not as expected. Don't forget to set the ordinary state
	 * to bottom if the exception will definitely occur.
	 * 
	 * @return true if the exception is definitely thrown
	 */
	public static boolean throwTypeErrorIfWrongKindOfThis(ECMAScriptObjects nativeobject, FunctionCalls.CallInfo<? extends Node> call, State state,
			Solver.SolverInterface c, ObjectLabel.Kind kind) {
		Set<ObjectLabel> this_obj = state.readThis().getObjectLabels();
		boolean some_bad = false;
		boolean some_good = false;
		for (ObjectLabel objlabel : this_obj)
			if (objlabel.getKind() != kind)
				some_bad = true;
			else
				some_good = true;
		if (some_bad)
			Exceptions.throwTypeError(state, c);
		c.addMessage(call.getSourceNode(), some_bad ? some_good ? Status.MAYBE : Status.CERTAIN : Status.NONE, Severity.HIGH, "TypeError, native function "
				+ nativeobject + " called on invalid object kind, expected " + kind);
		return some_bad && !some_good;
	}
}
