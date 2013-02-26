package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.NativeAPIs;
import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.flowgraph.NativeObject;
import dk.brics.tajs.options.Options;

/**
 * Native ECMAScript object descriptors.
 */
public enum ECMAScriptObjects implements NativeObject {

	GLOBAL(NativeAPIs.ECMA_SCRIPT_NATIVE, "<the global object>"),

	OBJECT(NativeAPIs.ECMA_SCRIPT_NATIVE, "Object"), OBJECT_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Object.prototype"), OBJECT_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Object.prototype.toString"), OBJECT_TOLOCALESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Object.prototype.toLocaleString"), OBJECT_VALUEOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Object.prototype.valueOf"), OBJECT_HASOWNPROPERTY(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Object.prototype.hasOwnProperty"), OBJECT_ISPROTOTYPEOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Object.prototype.isPrototypeOf"), OBJECT_PROPERTYISENUMERABLE(
			NativeAPIs.ECMA_SCRIPT_NATIVE,
			"Object.prototype.propertyIsEnumerable"),

	FUNCTION(NativeAPIs.ECMA_SCRIPT_NATIVE, "Function"), FUNCTION_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Function.prototype"), FUNCTION_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Function.prototype.toString"), FUNCTION_APPLY(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Function.prototype.apply"), FUNCTION_CALL(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Function.prototype.call"),

	ARRAY(NativeAPIs.ECMA_SCRIPT_NATIVE, "Array"), ARRAY_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype"), ARRAY_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.toString"), ARRAY_TOLOCALESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.toLocaleString"), ARRAY_CONCAT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.concat"), ARRAY_FOREACH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.forEach"), ARRAY_JOIN(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.join"), ARRAY_POP(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.pop"), ARRAY_PUSH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.push"), ARRAY_REVERSE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.reverse"), ARRAY_SHIFT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.shift"), ARRAY_SLICE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.slice"), ARRAY_SOME(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.some"), ARRAY_SORT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.sort"), ARRAY_SPLICE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.splice"), ARRAY_UNSHIFT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.unshift"), ARRAY_INDEXOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Array.prototype.indexOf"),

	STRING(NativeAPIs.ECMA_SCRIPT_NATIVE, "String"), STRING_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype"), STRING_FROMCHARCODE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.fromCharCode"), STRING_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.toString"), STRING_VALUEOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.valueOf"), STRING_CHARAT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.charAt"), STRING_CHARCODEAT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.charCodeAt"), STRING_CONCAT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.concat"), STRING_INDEXOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.indexOf"), STRING_LASTINDEXOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.lastIndexOf"), STRING_LOCALECOMPARE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.localeCompare"), STRING_MATCH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.match"), STRING_REPLACE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.replace"), STRING_SEARCH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.search"), STRING_SLICE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.slice"), STRING_SPLIT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.split"), STRING_SUBSTR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.substr"), STRING_SUBSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.substring"), STRING_TOLOWERCASE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.toLowerCase"), STRING_TOLOCALELOWERCASE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.toLocaleLowerCase"), STRING_TOUPPERCASE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.toUpperCase"), STRING_TOLOCALEUPPERCASE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "String.prototype.toLocaleUpperCase"),

	BOOLEAN(NativeAPIs.ECMA_SCRIPT_NATIVE, "Boolean"), BOOLEAN_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Boolean.prototype"), BOOLEAN_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Boolean.prototype.toString"), BOOLEAN_VALUEOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Boolean.prototype.valueOf"),

	NUMBER(NativeAPIs.ECMA_SCRIPT_NATIVE, "Number"), NUMBER_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Number.prototype"), NUMBER_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Number.prototype.toString"), NUMBER_TOLOCALESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Number.prototype.toLocaleString"), NUMBER_VALUEOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Number.prototype.valueOf"), NUMBER_TOFIXED(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Number.prototype.toFixed"), NUMBER_TOEXPONENTIAL(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Number.prototype.toExponential"), NUMBER_TOPRECISION(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Number.prototype.toPrecision"),

	MATH(NativeAPIs.ECMA_SCRIPT_NATIVE, "Math"), MATH_MAX(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.max"), MATH_MIN(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.min"), MATH_POW(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.pow"), MATH_RANDOM(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.random"), MATH_ROUND(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.round"), MATH_SIN(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.sin"), MATH_SQRT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.sqrt"), MATH_TAN(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.tan"), MATH_ABS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.abs"), MATH_ACOS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.acos"), MATH_ASIN(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.asin"), MATH_ATAN(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.atan"), MATH_ATAN2(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.atanz"), MATH_CEIL(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.ceil"), MATH_COS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.cos"), MATH_EXP(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.exp"), MATH_FLOOR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.floor"), MATH_LOG(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Math.log"),

	DATE(NativeAPIs.ECMA_SCRIPT_NATIVE, "Date"), DATE_PARSE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.parse"), DATE_UTC(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.UTC"), DATE_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype"), DATE_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toString"), DATE_TODATESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toDateString"), DATE_TOTIMESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toTimeString"), DATE_TOLOCALESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toLocaleString"), DATE_TOLOCALEDATESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toLocaleDateString"), DATE_TOLOCALETIMESTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toLocaleTimeString"), DATE_VALUEOF(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.valueOf"), DATE_GETTIME(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getTime"), DATE_GETFULLYEAR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getFullYear"), DATE_GETUTCFULLYEAR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCFullYear"), DATE_GETMONTH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getMonth"), DATE_GETUTCMONTH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCMonth"), DATE_GETDATE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getDate"), DATE_GETUTCDATE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCDate"), DATE_GETDAY(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getDay"), DATE_GETUTCDAY(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCDay"), DATE_GETHOURS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getHours"), DATE_GETUTCHOURS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCHours"), DATE_GETMINUTES(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getMinutes"), DATE_GETUTCMINUTES(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCMinutes"), DATE_GETSECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getSeconds"), DATE_GETUTCSECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCSeconds"), DATE_GETMILLISECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getMilliseconds"), DATE_GETUTCMILLISECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getUTCMilliseconds"), DATE_GETTIMEZONEOFFSET(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getTimezoneOffset"), DATE_SETTIME(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setTime"), DATE_SETMILLISECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setMilliseconds"), DATE_SETUTCMILLISECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setUTCMilliseconds"), DATE_SETSECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setSeconds"), DATE_SETUTCSECONDS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setUTCSeconds"), DATE_SETMINUTES(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setMinutes"), DATE_SETUTCMINUTES(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setUTCMinutes"), DATE_SETHOURS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setHours"), DATE_SETUTCHOURS(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setUTCHours"), DATE_SETDATE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setDate"), DATE_SETUTCDATE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setUTCDate"), DATE_SETMONTH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setMonth"), DATE_SETUTCMONTH(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setUTCMonth"), DATE_SETFULLYEAR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setFullYear"), DATE_SETUTCFULLYEAR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setUTCFullYear"), DATE_TOUTCSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toUTCString"), DATE_GETYEAR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.getYear"), DATE_SETYEAR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.setYear"), DATE_TOGMTSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Date.prototype.toGMTString"),

	REGEXP(NativeAPIs.ECMA_SCRIPT_NATIVE, "RegExp"), REGEXP_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "RegExp.prototype"), REGEXP_EXEC(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "RegExp.prototype.exec"), REGEXP_TEST(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "RegExp.prototype.test"), REGEXP_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "RegExp.prototype.toString"),

	ERROR(NativeAPIs.ECMA_SCRIPT_NATIVE, "Error"), ERROR_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Error.prototype"), ERROR_TOSTRING(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "Error.toString"), EVAL_ERROR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "EvalError"), EVAL_ERROR_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "EvalError.prototype"), RANGE_ERROR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "RangeError"), RANGE_ERROR_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "RangeError.prototype"), REFERENCE_ERROR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "ReferenceError"), REFERENCE_ERROR_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "ReferenceError.prototype"), SYNTAX_ERROR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "SyntaxError"), SYNTAX_ERROR_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "SyntaxError.prototype"), TYPE_ERROR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "TypeError"), TYPE_ERROR_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "TypeError.prototype"), URI_ERROR(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "URIError"), URI_ERROR_PROTOTYPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "URIError.prototype"),

//	EVAL(NativeAPIs.ECMA_SCRIPT_NATIVE, "eval"),
	PARSEINT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "parseInt"), PARSEFLOAT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "parseFloat"), ISNAN(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "isNaN"), ISFINITE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "isFinite"), DECODEURI(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "decodeURI"), DECODEURICOMPONENT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "decodeURIComponent"), ENCODEURI(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "encodeURI"), ENCODEURICOMPONENT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "encodeURIComponent"), PRINT(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "print"), // nonstandard
	ALERT(NativeAPIs.ECMA_SCRIPT_NATIVE, "alert"), // nonstandard
	ESCAPE(NativeAPIs.ECMA_SCRIPT_NATIVE, "escape"), UNESCAPE(
			NativeAPIs.ECMA_SCRIPT_NATIVE, "unescape"),

	/*
	 * ############################################################ Dependency
	 * function, to mark values with source location
	 * ############################################################
	 */
	TRACE(NativeAPIs.ECMA_SCRIPT_NATIVE, "trace"), // nonstandard
	UNTRACE(NativeAPIs.ECMA_SCRIPT_NATIVE, "untrace"), // nonstandard
	DUMPDEPENDENCY(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpDependency"), // nonstandard

	ASSERT(NativeAPIs.ECMA_SCRIPT_NATIVE, "assert"), // nonstandard
	DUMPVALUE(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpValue"), // nonstandard
	DUMPPROTOTYPE(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpPrototype"), // nonstandard
	DUMPOBJECT(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpObject"), // nonstandard
	DUMPSTATE(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpState"), // nonstandard
	DUMPMODIFIEDSTATE(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpModifiedState"), // nonstandard
	DUMPATTRIBUTES(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpAttributes"), // nonstandard
	DUMPOBJECTORIGIN(NativeAPIs.ECMA_SCRIPT_NATIVE, "dumpObjectOrigin"), // nonstandard
	CONVERSION_TO_PRIMITIVE(NativeAPIs.ECMA_SCRIPT_NATIVE,
			"conversionToPrimitive"), // nonstandard
	ASSUME_NON_NULLUNDEF(NativeAPIs.ECMA_SCRIPT_NATIVE, "assumeNonNullUndef"), // nonstandard
	TAJS_GET_UI_EVENT(NativeAPIs.ECMA_SCRIPT_NATIVE, "_TAJS_getUIEvent"), // nonstandard
	TAJS_GET_DOCUMENT_EVENT(NativeAPIs.ECMA_SCRIPT_NATIVE,
			"_TAJS_getDocumentEvent"), // nonstandard
	TAJS_GET_MOUSE_EVENT(NativeAPIs.ECMA_SCRIPT_NATIVE, "_TAJS_getMouseEvent"), // nonstandard
	TAJS_GET_KEYBOARD_EVENT(NativeAPIs.ECMA_SCRIPT_NATIVE,
			"_TAJS_getKeyboardEvent"), // nonstandard
	TAJS_GET_EVENT_LISTENER(NativeAPIs.ECMA_SCRIPT_NATIVE,
			"_TAJS_getEventListener"), // nonstandard
	TAJS_GET_WHEEL_EVENT(NativeAPIs.ECMA_SCRIPT_NATIVE, "_TAJS_getWheelEvent"), // nonstandard
	TAJS_GET_AJAX_EVENT(NativeAPIs.ECMA_SCRIPT_NATIVE, "_TAJS_getAjaxEvent"); // nonstandard
	
	private NativeAPIs api;
	private String string;

	private ECMAScriptObjects(NativeAPIs api, String str) {
		this.api = api;
		this.string = str;
	}

	@Override
	public String toString() {
		return string;
	}

	@Override
	public NativeAPIs getAPI() {
		return api;
	}

	@Override
	public boolean hasGetter(String p) {
		return false;
	}

	@Override
	public boolean hasSetter(String p) {
		if (Options.isDOMEnabled()) {
			// If DOM is enabled, the GLOBAL object is the WINDOW object.
			if (this == GLOBAL) {
				if (DOMEvents.isLoadEventAttribute(p)
						|| DOMEvents.isUnloadEventAttribute(p)) {
					return true;
				}
			}
		}
		return false;
	}
}
