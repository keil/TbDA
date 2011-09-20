package dk.brics.tajs.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;

import dk.brics.tajs.analysis.dom.DOMBuilder;
import dk.brics.tajs.analysis.dom.DOMVisitor;
import dk.brics.tajs.analysis.dom.HTMLParser;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.DependencyAnalyzer;
import dk.brics.tajs.dependency.DependencyObject;
import dk.brics.tajs.dependency.DependencyProperties;
import dk.brics.tajs.dependency.graph.DependencyGraph;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.NativeObject;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IInitialStateBuilder;

/**
 * Sets up the initial state (Chapter 15).
 */
public class InitialStateBuilder implements IInitialStateBuilder<State, CallContext, Statistics> {

	private static DependencyGraph mDependencyGraph;

	/**
	 * Object label for the global object.
	 */
	public static final ObjectLabel GLOBAL = new ObjectLabel(ECMAScriptObjects.GLOBAL, Kind.OBJECT);

	/**
	 * Object label for Object.prototype.
	 */
	public static final ObjectLabel OBJECT_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.OBJECT_PROTOTYPE, Kind.OBJECT);

	/**
	 * Object label for Function.prototype.
	 */
	public static final ObjectLabel FUNCTION_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.FUNCTION_PROTOTYPE, Kind.FUNCTION);

	/**
	 * Object label for Array.prototype.
	 */
	public static final ObjectLabel ARRAY_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.ARRAY_PROTOTYPE, Kind.ARRAY);

	/**
	 * Object label for String.prototype.
	 */
	public static final ObjectLabel STRING_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.STRING_PROTOTYPE, Kind.STRING);

	/**
	 * Object label for Boolean.prototype.
	 */
	public static final ObjectLabel BOOLEAN_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.BOOLEAN_PROTOTYPE, Kind.BOOLEAN);

	/**
	 * Object label for Number.prototype.
	 */
	public static final ObjectLabel NUMBER_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.NUMBER_PROTOTYPE, Kind.NUMBER);

	/**
	 * Object label for Date.prototype.
	 */
	public static final ObjectLabel DATE_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.DATE_PROTOTYPE, Kind.DATE);

	/**
	 * Object label for RegExp.prototype.
	 */
	public static final ObjectLabel REGEXP_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.REGEXP_PROTOTYPE, Kind.OBJECT);

	/**
	 * Object label for Error.prototype.
	 */
	public static final ObjectLabel ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.ERROR_PROTOTYPE, Kind.ERROR);

	/**
	 * Object label for EvalError.prototype.
	 */
	public static final ObjectLabel EVAL_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.EVAL_ERROR_PROTOTYPE, Kind.ERROR);

	/**
	 * Object label for RangeError.prototype.
	 */
	public static final ObjectLabel RANGE_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.RANGE_ERROR_PROTOTYPE, Kind.ERROR);

	/**
	 * Object label for ReferenceError.prototype.
	 */
	public static final ObjectLabel REFERENCE_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.REFERENCE_ERROR_PROTOTYPE, Kind.ERROR);

	/**
	 * Object label for SyntaxError.prototype.
	 */
	public static final ObjectLabel SYNTAX_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.SYNTAX_ERROR_PROTOTYPE, Kind.ERROR);

	/**
	 * Object label for TypeError.prototype.
	 */
	public static final ObjectLabel TYPE_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.TYPE_ERROR_PROTOTYPE, Kind.ERROR);

	/**
	 * Object label for URIError.prototype.
	 */
	public static final ObjectLabel URI_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.URI_ERROR_PROTOTYPE, Kind.ERROR);

	/**
	 * The initial state.
	 */
	private State s;

	/**
	 * The HTMLParser us to generate the DOM model, if any.
	 */
	private HTMLParser htmlParser = null;

	/**
	 * Constructs a new InitialStateBuilder object.
	 */
	public InitialStateBuilder() {
	}

	/**
	 * Sets up the initial state.
	 */
	@Override
	public void addInitialState(Solver.SolverInterface c) {
		s = new State(c, c.getFlowGraph().getMain().getEntry());
		mDependencyGraph = c.getDependencyGraph();

		ObjectLabel global = GLOBAL; // same as DOMBuilder.WINDOW
		createObject(s, global);

		ObjectLabel lObject = new ObjectLabel(ECMAScriptObjects.OBJECT, Kind.FUNCTION);
		createObject(s, lObject);
		ObjectLabel lFunction = new ObjectLabel(ECMAScriptObjects.FUNCTION, Kind.FUNCTION);
		createObject(s, lFunction);
		ObjectLabel lArray = new ObjectLabel(ECMAScriptObjects.ARRAY, Kind.FUNCTION);
		createObject(s, lArray);
		ObjectLabel lString = new ObjectLabel(ECMAScriptObjects.STRING, Kind.FUNCTION);
		createObject(s, lString);
		ObjectLabel lBoolean = new ObjectLabel(ECMAScriptObjects.BOOLEAN, Kind.FUNCTION);
		createObject(s, lBoolean);
		ObjectLabel lNumber = new ObjectLabel(ECMAScriptObjects.NUMBER, Kind.FUNCTION);
		createObject(s, lNumber);
		ObjectLabel lDate = new ObjectLabel(ECMAScriptObjects.DATE, Kind.FUNCTION);
		createObject(s, lDate);
		ObjectLabel lRegExp = new ObjectLabel(ECMAScriptObjects.REGEXP, Kind.FUNCTION);
		createObject(s, lRegExp);
		ObjectLabel lError = new ObjectLabel(ECMAScriptObjects.ERROR, Kind.FUNCTION);
		createObject(s, lError);
		ObjectLabel lEvalError = new ObjectLabel(ECMAScriptObjects.EVAL_ERROR, Kind.FUNCTION);
		createObject(s, lEvalError);
		ObjectLabel lRangeError = new ObjectLabel(ECMAScriptObjects.RANGE_ERROR, Kind.FUNCTION);
		createObject(s, lRangeError);
		ObjectLabel lReferenceError = new ObjectLabel(ECMAScriptObjects.REFERENCE_ERROR, Kind.FUNCTION);
		createObject(s, lReferenceError);
		ObjectLabel lSyntaxError = new ObjectLabel(ECMAScriptObjects.SYNTAX_ERROR, Kind.FUNCTION);
		createObject(s, lSyntaxError);
		ObjectLabel lTypeError = new ObjectLabel(ECMAScriptObjects.TYPE_ERROR, Kind.FUNCTION);
		createObject(s, lTypeError);
		ObjectLabel lURIError = new ObjectLabel(ECMAScriptObjects.URI_ERROR, Kind.FUNCTION);
		createObject(s, lURIError);

		ObjectLabel lMath = new ObjectLabel(ECMAScriptObjects.MATH, Kind.MATH);
		createObject(s, lMath);

		ObjectLabel lObjectPrototype = OBJECT_PROTOTYPE;
		createObject(s, lObjectPrototype);
		ObjectLabel lFunProto = FUNCTION_PROTOTYPE;
		createObject(s, lFunProto);
		ObjectLabel lArrayProto = ARRAY_PROTOTYPE;
		createObject(s, lArrayProto);
		ObjectLabel lStringProto = STRING_PROTOTYPE;
		createObject(s, lStringProto);
		ObjectLabel lBooleanProto = BOOLEAN_PROTOTYPE;
		createObject(s, lBooleanProto);
		ObjectLabel lNumberProto = NUMBER_PROTOTYPE;
		createObject(s, lNumberProto);
		ObjectLabel lDateProto = DATE_PROTOTYPE;
		createObject(s, lDateProto);
		ObjectLabel lRegExpProto = REGEXP_PROTOTYPE;
		createObject(s, lRegExpProto);
		ObjectLabel lErrorProto = ERROR_PROTOTYPE;
		createObject(s, lErrorProto);
		ObjectLabel lEvalErrorProto = EVAL_ERROR_PROTOTYPE;
		createObject(s, lEvalErrorProto);
		ObjectLabel lRangeErrorProto = RANGE_ERROR_PROTOTYPE;
		createObject(s, lRangeErrorProto);
		ObjectLabel lReferenceErrorProto = REFERENCE_ERROR_PROTOTYPE;
		createObject(s, lReferenceErrorProto);
		ObjectLabel lSyntaxErrorProto = SYNTAX_ERROR_PROTOTYPE;
		createObject(s, lSyntaxErrorProto);
		ObjectLabel lTypeErrorProto = TYPE_ERROR_PROTOTYPE;
		createObject(s, lTypeErrorProto);
		ObjectLabel lURIErrorProto = URI_ERROR_PROTOTYPE;
		createObject(s, lURIErrorProto);

		// 15.1.1 value properties of the global object
		createSpecialProperty(s, global, "NaN", Value.makeNum(Double.NaN, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, false));
		createSpecialProperty(s, global, "Infinity",
				Value.makeNum(Double.POSITIVE_INFINITY, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, false));
		createSpecialProperty(s, global, "undefined", Value.makeUndef(new Dependency(), new DependencyGraphReference()).setAttributes(true, true, false));
		// TODO: 15.1 the values of the [[Prototype]] and [[Class]] properties
		// of the global object are implementation-dependent
		createInternalPrototype(s, global, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference())); // Rhino's
		// implementation
		// choice

		// 15.1.2 function properties of the global object
		//createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.EVAL, "eval", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.PARSEINT, "parseInt", 2);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.PARSEFLOAT, "parseFloat", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ISNAN, "isNaN", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ISFINITE, "isFinite", 1);

		// 15.1.3 URI handling function properties
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DECODEURI, "decodeURI", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DECODEURICOMPONENT, "decodeURIComponent", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ENCODEURI, "encodeURI", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ENCODEURICOMPONENT, "encodeURIComponent", 1);

		// Rhino's print and alert functions
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.PRINT, "print", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ALERT, "alert", 1);

		// 15.1.4 constructor properties of the global object
		createPrimitiveConstructor(s, global, lFunProto, lObjectPrototype, lObject, "Object", 1);
		createPrimitiveConstructor(s, global, lFunProto, lFunProto, lFunction, "Function", 1);
		createPrimitiveConstructor(s, global, lFunProto, lArrayProto, lArray, "Array", 1);
		createPrimitiveConstructor(s, global, lFunProto, lStringProto, lString, "String", 1);
		createPrimitiveConstructor(s, global, lFunProto, lBooleanProto, lBoolean, "Boolean", 1);
		createPrimitiveConstructor(s, global, lFunProto, lNumberProto, lNumber, "Number", 1);
		createPrimitiveConstructor(s, global, lFunProto, lDateProto, lDate, "Date", 7);
		createPrimitiveConstructor(s, global, lFunProto, lRegExpProto, lRegExp, "RegExp", 2);
		createPrimitiveConstructor(s, global, lFunProto, lErrorProto, lError, "Error", 1);
		createPrimitiveConstructor(s, global, lFunProto, lEvalErrorProto, lEvalError, "EvalError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lRangeErrorProto, lRangeError, "RangeError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lReferenceErrorProto, lReferenceError, "ReferenceError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lSyntaxErrorProto, lSyntaxError, "SyntaxError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lTypeErrorProto, lTypeError, "TypeError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lURIErrorProto, lURIError, "URIError", 1);

		// 15.2.4 properties of the Object prototype object
		createInternalPrototype(s, lObjectPrototype, Value.makeNull(new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lObjectPrototype, "constructor",
				Value.makeObject(lObject, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_HASOWNPROPERTY, "hasOwnProperty", 1);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_ISPROTOTYPEOF, "isPrototypeOf", 1);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_PROPERTYISENUMERABLE, "propertyIsEnumerable", 1);

		// 15.3.4 properties of the Function prototype object
		createInternalPrototype(s, lFunProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lFunProto, "valueOf",
				Value.makeObject(new ObjectLabel(ECMAScriptObjects.OBJECT_VALUEOF, Kind.FUNCTION), new Dependency(), new DependencyGraphReference())
						.setAttributes(true, false, false));
		createSpecialProperty(s, lFunProto, "constructor",
				Value.makeObject(lFunction, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createPrimitiveFunction(s, lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_APPLY, "apply", 2);
		createPrimitiveFunction(s, lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_CALL, "call", 1);

		// 15.4.4 properties of the Array prototype object
		createInternalPrototype(s, lArrayProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lArrayProto, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lArrayProto, "valueOf",
				Value.makeObject(new ObjectLabel(ECMAScriptObjects.OBJECT_VALUEOF, Kind.FUNCTION), new Dependency(), new DependencyGraphReference())
						.setAttributes(true, false, false));
		createSpecialProperty(s, lArrayProto, "constructor",
				Value.makeObject(lArray, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_CONCAT, "concat", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_JOIN, "join", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_POP, "pop", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_PUSH, "push", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_REVERSE, "reverse", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SHIFT, "shift", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SLICE, "slice", 2);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SORT, "sort", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SPLICE, "splice", 2);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_UNSHIFT, "unshift", 1);
/**
 * vasu
 */
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_FOREACH, "forEach", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SOME, "some", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_INDEXOF, "indexOf", 1);
		
		// 15.5.3 properties of the String constructor
		createPrimitiveFunction(s, lString, lFunProto, ECMAScriptObjects.STRING_FROMCHARCODE, "fromCharCode", 1);

		// 15.5.4 properties of the String prototype object
		createInternalPrototype(s, lStringProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lStringProto, "constructor",
				Value.makeObject(lString, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalValue(s, lStringProto, Value.makeStr("", new Dependency(), new DependencyGraphReference()));
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARAT, "charAt", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARCODEAT, "charCodeAt", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_CONCAT, "concat", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_INDEXOF, "indexOf", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_LASTINDEXOF, "lastIndexOf", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_LOCALECOMPARE, "localeCompare", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_MATCH, "match", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_REPLACE, "replace", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SEARCH, "search", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SLICE, "slice", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SPLIT, "split", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SUBSTRING, "substring", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOWERCASE, "toLowerCase", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALELOWERCASE, "toLocaleLowerCase", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOUPPERCASE, "toUpperCase", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALEUPPERCASE, "toLocaleUpperCase", 0);

		// 15.6.4 properties of the Boolean prototype object
		createInternalPrototype(s, lBooleanProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createInternalValue(s, lBooleanProto, Value.makeBool(false, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lBooleanProto, "constructor",
				Value.makeObject(lBoolean, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createPrimitiveFunction(s, lBooleanProto, lFunProto, ECMAScriptObjects.BOOLEAN_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lBooleanProto, lFunProto, ECMAScriptObjects.BOOLEAN_VALUEOF, "valueOf", 0);

		// 15.7.3 properties of the Number constructor
		createSpecialProperty(s, lNumber, "MAX_VALUE",
				Value.makeNum(Double.MAX_VALUE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lNumber, "MIN_VALUE",
				Value.makeNum(Double.MIN_VALUE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lNumber, "NaN", Value.makeNum(Double.NaN, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lNumber, "POSITIVE_INFINITY", Value.makeNum(Double.POSITIVE_INFINITY, new Dependency(), new DependencyGraphReference())
				.setAttributes(true, true, true));
		createSpecialProperty(s, lNumber, "NEGATIVE_INFINITY", Value.makeNum(Double.NEGATIVE_INFINITY, new Dependency(), new DependencyGraphReference())
				.setAttributes(true, true, true));

		// 15.7.4 properties of the Number prototype object
		createInternalPrototype(s, lNumberProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createInternalValue(s, lNumberProto, Value.makeNum(0, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lNumberProto, "constructor",
				Value.makeObject(lNumber, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOSTRING, "toString", 1);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOFIXED, "toFixed", 1);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOEXPONENTIAL, "toExponential", 1);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOPRECISION, "toPrecision", 1);

		// 15.8 the Math object
		createSpecialProperty(s, global, "Math", Value.makeObject(lMath, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalPrototype(s, lMath, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lMath, "E", Value.makeNum(Math.E, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lMath, "LN10", Value.makeNum(Math.log(10), new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lMath, "LN2", Value.makeNum(Math.log(2), new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lMath, "LOG2E",
				Value.makeNum(1 / Math.log(2), new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lMath, "LOG10E",
				Value.makeNum(1 / Math.log(10), new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lMath, "PI", Value.makeNum(Math.PI, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lMath, "SQRT1_2",
				Value.makeNum(Math.sqrt(0.5), new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createSpecialProperty(s, lMath, "SQRT2", Value.makeNum(Math.sqrt(2), new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ABS, "abs", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ACOS, "acos", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ASIN, "asin", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ATAN, "atan", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ATAN2, "atan2", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_CEIL, "ceil", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_COS, "cos", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_EXP, "exp", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_FLOOR, "floor", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_LOG, "log", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_MAX, "max", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_MIN, "min", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_POW, "pow", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_RANDOM, "random", 0);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ROUND, "round", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_SIN, "sin", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_SQRT, "sqrt", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_TAN, "tan", 1);

		// 15.9.4 properties of Date constructor
		createPrimitiveFunction(s, lDate, lFunProto, ECMAScriptObjects.DATE_PARSE, "parse", 1);
		createPrimitiveFunction(s, lDate, lFunProto, ECMAScriptObjects.DATE_UTC, "UTC", 7);

		// 15.9.5 properties of the Date prototype object
		createInternalPrototype(s, lDateProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lDateProto, "constructor",
				Value.makeObject(lDate, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalValue(s, lDateProto, Value.makeNum(Double.NaN, new Dependency(), new DependencyGraphReference()));
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TODATESTRING, "toDateString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOTIMESTRING, "toTimeString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALEDATESTRING, "toLocaleDateString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALETIMESTRING, "toLocaleTimeString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETTIME, "getTime", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETFULLYEAR, "getFullYear", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCFULLYEAR, "getUTCFullYear", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMONTH, "getMonth", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMONTH, "getUTCMonth", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETDATE, "getDate", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCDATE, "getUTCDate", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETDAY, "getDay", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCDAY, "getUTCDay", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETHOURS, "getHours", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCHOURS, "getUTCHours", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMINUTES, "getMinutes", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMINUTES, "getUTCMinutes", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETSECONDS, "getSeconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCSECONDS, "getUTCSeconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMILLISECONDS, "getMilliseconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMILLISECONDS, "getUTCMilliseconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETTIMEZONEOFFSET, "getTimezoneOffset", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETTIME, "setTime", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMILLISECONDS, "setMilliseconds", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMILLISECONDS, "setUTCMilliseconds", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETSECONDS, "setSeconds", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCSECONDS, "setUTCSeconds", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMINUTES, "setMinutes", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMINUTES, "setUTCMinutes", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETHOURS, "setHours", 4);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCHOURS, "setUTCHours", 4);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETDATE, "setDate", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCDATE, "setUTCDate", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMONTH, "setMonth", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMONTH, "setUTCMonth", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETFULLYEAR, "setFullYear", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCFULLYEAR, "setUTCFullYear", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOUTCSTRING, "toUTCFullString", 0);

		// 15.10.6 properties of the RegExp prototype object
		createInternalPrototype(s, lDateProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lRegExpProto, "valueOf",
				Value.makeObject(new ObjectLabel(ECMAScriptObjects.OBJECT_VALUEOF, Kind.FUNCTION), new Dependency(), new DependencyGraphReference())
						.setAttributes(true, false, false));
		createSpecialProperty(s, lRegExpProto, "constructor",
				Value.makeObject(lRegExp, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createPrimitiveFunction(s, lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_EXEC, "exec", 1);
		createPrimitiveFunction(s, lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_TEST, "test", 1);
		createPrimitiveFunction(s, lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_TOSTRING, "toString", 0);

		// 15.11.4 properties of the Error prototype object
		createInternalPrototype(s, lErrorProto, Value.makeObject(lObjectPrototype, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lErrorProto, "constructor",
				Value.makeObject(lError, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lErrorProto, "name", Value.makeStr("Error", new Dependency(), new DependencyGraphReference())
				.setAttributes(true, false, false));
		createSpecialProperty(s, lErrorProto, "message", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false)); // implementation
		// dependent
		// string
		createPrimitiveFunction(s, lErrorProto, lFunProto, ECMAScriptObjects.ERROR_TOSTRING, "toString", 0);

		// 15.11.7 native error objects
		createInternalPrototype(s, lEvalErrorProto, Value.makeObject(lErrorProto, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lEvalErrorProto, "constructor",
				Value.makeObject(lEvalError, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lEvalErrorProto, "name",
				Value.makeStr("EvalError", new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lEvalErrorProto, "message",
				Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalPrototype(s, lRangeErrorProto, Value.makeObject(lErrorProto, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lRangeErrorProto, "constructor", Value.makeObject(lRangeError, new Dependency(), new DependencyGraphReference())
				.setAttributes(true, false, false));
		createSpecialProperty(s, lRangeErrorProto, "name",
				Value.makeStr("RangeError", new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lRangeErrorProto, "message",
				Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalPrototype(s, lReferenceErrorProto, Value.makeObject(lErrorProto, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lReferenceErrorProto, "constructor", Value.makeObject(lReferenceError, new Dependency(), new DependencyGraphReference())
				.setAttributes(true, false, false));
		createSpecialProperty(s, lReferenceErrorProto, "name",
				Value.makeStr("ReferenceError", new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lReferenceErrorProto, "message",
				Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalPrototype(s, lSyntaxErrorProto, Value.makeObject(lErrorProto, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lSyntaxErrorProto, "constructor", Value.makeObject(lSyntaxError, new Dependency(), new DependencyGraphReference())
				.setAttributes(true, false, false));
		createSpecialProperty(s, lSyntaxErrorProto, "name",
				Value.makeStr("SyntaxError", new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lSyntaxErrorProto, "message",
				Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalPrototype(s, lTypeErrorProto, Value.makeObject(lErrorProto, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lTypeErrorProto, "constructor",
				Value.makeObject(lTypeError, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lTypeErrorProto, "name",
				Value.makeStr("TypeError", new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lTypeErrorProto, "message",
				Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createInternalPrototype(s, lURIErrorProto, Value.makeObject(lErrorProto, new Dependency(), new DependencyGraphReference()));
		createSpecialProperty(s, lURIErrorProto, "constructor",
				Value.makeObject(lURIError, new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lURIErrorProto, "name",
				Value.makeStr("URIError", new Dependency(), new DependencyGraphReference()).setAttributes(true, false, false));
		createSpecialProperty(s, lURIErrorProto, "message", Value.makeAnyStr(new Dependency(), new DependencyGraphReference())
				.setAttributes(true, false, false));

		// Annex B functions
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ESCAPE, "escape", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.UNESCAPE, "unescape", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SUBSTR, "substr", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETYEAR, "getYear", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETYEAR, "setYear", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOGMTSTRING, "toGMTString", 0);

		/*
		 * ############################################################
		 * Dependency function, to mark values with source location
		 * ############################################################
		 */
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TRACE, "trace", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPDEPENDENCY, "dumpDependency", 1);

		// our own host defined properties
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSERT, "assert", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPVALUE, "dumpValue", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPPROTOTYPE, "dumpPrototype", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPOBJECT, "dumpObject", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPSTATE, "dumpState", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPMODIFIEDSTATE, "dumpModifiedState", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPATTRIBUTES, "dumpAttributes", 2);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPOBJECTORIGIN, "dumpObjectOrigin", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.CONVERSION_TO_PRIMITIVE, "conversionToPrimitive", 2);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSUME_NON_NULLUNDEF, "assumeNonNullUndef", 2);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_UI_EVENT, "_TAJS_getUIEvent", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_DOCUMENT_EVENT, "_TAJS_getDocumentEvent", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_MOUSE_EVENT, "_TAJS_getMouseEvent", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_KEYBOARD_EVENT, "_TAJS_getKeyboardEvent", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_EVENT_LISTENER, "_TAJS_getEventListener", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_WHEEL_EVENT, "_TAJS_getWheelEvent", 0);

		// build initial DOM state
		if (Options.isDOMEnabled()) {
			DOMBuilder.addInitialDOM(s);
		}

		ExecutionContext e = new ExecutionContext(ScopeChain.make(global), global, global);
		s.setExecutionContext(e);
		s.clearEffects();
		s.freezeBasisStore();

		Function main = c.getFlowGraph().getMain();
		CallContext call_context = new CallContext(s, main, null, null);
		// s.setContext(call_context);
		c.joinBlockEntry(s, main.getEntry(), call_context);
		c.getAnalysisLatticeElement().getCallGraph().registerFunctionContext(main, call_context);
	}

	/**
	 * Builds DOM specific state.
	 */
	@Override
	public void addDOMSpecificState(Document document) {
		if (Options.isDOMEnabled()) {
			DOMVisitor visitor = new DOMVisitor(document, s);
			visitor.visitDocument();
		}
	}

	/**
	 * InitialState dependency trace list
	 */
	static List<String> trace = DependencyProperties.getInstance().getStringArray(DependencyProperties.TRACE);

	private static Map<DependencyObject, DependencyObjectNode> mReferences = new HashMap<DependencyObject, DependencyObjectNode>();

	/**
	 * @param dependency
	 * @return DependencyGraphReference
	 */
	private static DependencyGraphReference createDependencyGraphReference(Dependency dependency) {
		DependencyGraphReference reference = new DependencyGraphReference();
		for (DependencyObject dependencyObject : dependency) {

			if (!mReferences.containsKey(dependencyObject)) {
				mReferences.put(dependencyObject, new DependencyObjectNode(dependencyObject, mDependencyGraph.getRoot()));
			}
			reference.join(mReferences.get(dependencyObject));
		}
		return reference;
	}

	/**
	 * Creates a new built-in object.
	 */
	public static void createObject(State s, ObjectLabel objlabel) {
		s.newObject(objlabel);
	}

	/**
	 * Creates a new built-in internal prototype.
	 */
	public static void createInternalPrototype(State s, ObjectLabel objlabel, Value value) {
		Dependency dependency = new Dependency();

		String string_objlabel = objlabel.getNativeObjectID().toString();

		// string_target
		dependency = trace.contains(string_objlabel) ? DependencyObject.getInitialStateDependencyObject(string_objlabel).getDependency() : new Dependency();
		value = value.setDependencyGraphReference(createDependencyGraphReference(dependency));
		s.writeInternalPrototype(objlabel, value.joinDependency(dependency));
		DependencyAnalyzer.initialstate.add(string_objlabel);
	}

	/**
	 * Creates a new built-in internal value.
	 */
	public static void createInternalValue(State s, ObjectLabel objlabel, Value value) {
		Dependency dependency = new Dependency();

		String string_objlabel = objlabel.getNativeObjectID().toString();

		// string_target
		dependency = trace.contains(string_objlabel) ? DependencyObject.getInitialStateDependencyObject(string_objlabel).getDependency() : new Dependency();
		value = value.setDependencyGraphReference(createDependencyGraphReference(dependency));
		s.writeInternalValue(objlabel, value.joinDependency(dependency));
		DependencyAnalyzer.initialstate.add(string_objlabel);
	}

	/**
	 * Creates a new built-in unknown array.
	 */
	public static void createUnknownArrayProperty(State s, ObjectLabel objlabel, Value value) {
		Dependency dependency = new Dependency();

		String string_objlabel = objlabel.getNativeObjectID().toString();

		// string_objlabel
		dependency = trace.contains(string_objlabel) ? DependencyObject.getInitialStateDependencyObject(string_objlabel).getDependency() : new Dependency();
		value = value.setDependencyGraphReference(createDependencyGraphReference(dependency));
		s.writeUnknownArrayProperty(objlabel, value.joinDependency(dependency));
		DependencyAnalyzer.initialstate.add(string_objlabel);
	}

	/**
	 * Creates a new built-in property.
	 */
	public static void createProperty(State s, ObjectLabel target, String propertyname, Value value) {
		Dependency dependency = new Dependency();

		String string_target = target.equals(GLOBAL) ? "" : target.getNativeObjectID().toString() + ".";
		String string_name = propertyname;

		// string_target + string_name
		dependency = trace.contains(string_target + string_name) ? DependencyObject.getInitialStateDependencyObject(string_target + string_name)
				.getDependency() : new Dependency();
		value = value.setDependencyGraphReference(createDependencyGraphReference(dependency));
		s.writeProperty(target, propertyname, value.joinDependency(dependency));
		DependencyAnalyzer.initialstate.add(string_target + string_name);
	}

	/**
	 * Creates a new built-in special property.
	 */
	public static void createSpecialProperty(State s, ObjectLabel target, String propertyname, Value value) {
		Dependency dependency = new Dependency();

		String string_target = target.equals(GLOBAL) ? "" : target.getNativeObjectID().toString() + ".";
		String string_name = propertyname;

		// string_target + string_name
		dependency = trace.contains(string_target + string_name) ? DependencyObject.getInitialStateDependencyObject(string_target + string_name)
				.getDependency() : new Dependency();
		value = value.setDependencyGraphReference(createDependencyGraphReference(dependency));
		s.writeSpecialProperty(target, propertyname, value.joinDependency(dependency));
		DependencyAnalyzer.initialstate.add(string_target + string_name);
	}

	/**
	 * Creates a new built-in function.
	 */
	public static void createPrimitiveFunction(State s, ObjectLabel target, ObjectLabel internal_proto, NativeObject primitive, String name, int arity) {
		ObjectLabel objlabel = new ObjectLabel(primitive, Kind.FUNCTION);
		Dependency dependency = new Dependency();
		DependencyGraphReference reference = new DependencyGraphReference();

		String string_target = target.equals(GLOBAL) ? "" : target.getNativeObjectID().toString() + ".";
		String string_objlabel = objlabel.getNativeObjectID().toString() + ".";
		String string_objlabel2 = objlabel.getNativeObjectID().toString();
		String string_name = name;
		String string_length = "length";

		// string_objlabel2
		dependency = trace.contains(string_objlabel2) ? DependencyObject.getInitialStateDependencyObject(string_objlabel2).getDependency() : new Dependency();
		s.newObject(objlabel);
		reference = createDependencyGraphReference(dependency);
		s.writeInternalPrototype(objlabel, Value.makeObject(internal_proto, new Dependency(), new DependencyGraphReference()));
		DependencyAnalyzer.initialstate.add(string_objlabel2);

		// string_target + string_name
		dependency = trace.contains(string_target + string_name) ? DependencyObject.getInitialStateDependencyObject(string_target + string_name)
				.getDependency() : new Dependency();
		reference = createDependencyGraphReference(dependency);
		s.writeSpecialProperty(target, name, Value.makeObject(objlabel, dependency, reference).setAttributes(true, false, false));
		DependencyAnalyzer.initialstate.add(string_target + string_name);

		// string_objlabel + string_length
		dependency = trace.contains(string_objlabel + string_length) ? DependencyObject.getInitialStateDependencyObject(string_objlabel + string_length)
				.getDependency() : new Dependency();
		reference = createDependencyGraphReference(dependency);
		s.writeSpecialProperty(objlabel, "length", Value.makeNum(arity, dependency, reference).setAttributes(true, true, true));
		DependencyAnalyzer.initialstate.add(string_objlabel + string_length);
	}

	/**
	 * Creates a new built-in constructor.
	 */
	public static void createPrimitiveConstructor(State s, ObjectLabel target, ObjectLabel internal_proto, ObjectLabel prototype, ObjectLabel objlabel,
			String name, int arity) {
		Dependency dependency = new Dependency();
		DependencyGraphReference reference = new DependencyGraphReference();

		String string_target = target.equals(GLOBAL) ? "" : target.getNativeObjectID().toString() + ".";
		String string_objlabel = objlabel.getNativeObjectID().toString() + ".";
		String string_name = name;
		String string_length = "length";
		String string_prototype = "prototype";

		// string_target + string_name
		dependency = trace.contains(string_target + string_name) ? DependencyObject.getInitialStateDependencyObject(string_target + string_name)
				.getDependency() : new Dependency();
		reference = createDependencyGraphReference(dependency);
		s.writeSpecialProperty(target, name, Value.makeObject(objlabel, dependency, reference).setAttributes(true, false, false));
		DependencyAnalyzer.initialstate.add(string_target + string_name);

		// string_objlabel + string_length
		dependency = trace.contains(string_objlabel + string_length) ? DependencyObject.getInitialStateDependencyObject(string_objlabel + string_length)
				.getDependency() : new Dependency();
		reference = createDependencyGraphReference(dependency);
		s.writeSpecialProperty(objlabel, "length", Value.makeNum(arity, dependency, reference).setAttributes(true, true, true));
		DependencyAnalyzer.initialstate.add(string_objlabel + string_length);

		// string_objlabel + string_prototype
		dependency = trace.contains(string_objlabel + string_prototype) ? DependencyObject.getInitialStateDependencyObject(string_objlabel + string_prototype)
				.getDependency() : new Dependency();
		reference = createDependencyGraphReference(dependency);
		s.writeSpecialProperty(objlabel, "prototype", Value.makeObject(prototype, dependency, reference).setAttributes(true, true, true));
		s.writeInternalPrototype(objlabel, Value.makeObject(internal_proto, dependency, reference));
		DependencyAnalyzer.initialstate.add(string_objlabel + string_prototype);
	}

	/**
	 * Sets the HTML parser.
	 */
	public void setHTMLParser(HTMLParser hTMLParser) {
		htmlParser = hTMLParser;
	}

	/**
	 * Gets the HTML parser.
	 */
	public HTMLParser getHTMLParser() {
		return htmlParser;
	}
}
