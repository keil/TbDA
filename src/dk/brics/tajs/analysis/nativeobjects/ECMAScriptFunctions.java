package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

/**
 * Encapsulation of transfer functions for ECMAScript native functions.
 */
public class ECMAScriptFunctions {

    /**
     * Evaluates the given native ECMAScript function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo<? extends Node> call, State state, Solver.SolverInterface c) {
        if (Options.isDebugEnabled())
            System.out.println("native function: " + nativeobject);
        Value res = null;
        switch (nativeobject) {

            case OBJECT:
            case OBJECT_TOSTRING:
            case OBJECT_TOLOCALESTRING:
            case OBJECT_VALUEOF:
            case OBJECT_HASOWNPROPERTY:
            case OBJECT_ISPROTOTYPEOF:
            case OBJECT_PROPERTYISENUMERABLE:
                res = JSObject.evaluate(nativeobject, call, state, c);
                break;

            case FUNCTION:
            case FUNCTION_TOSTRING:
            case FUNCTION_APPLY:
            case FUNCTION_CALL:
            case FUNCTION_PROTOTYPE:
                res = JSFunction.evaluate(nativeobject, call, state, c);
                break;

            case ARRAY:
            case ARRAY_JOIN:
            case ARRAY_TOSTRING:
            case ARRAY_TOLOCALESTRING:
            case ARRAY_CONCAT:
            case ARRAY_FOREACH:
            case ARRAY_PUSH:
            case ARRAY_POP:
            case ARRAY_REVERSE:
            case ARRAY_SHIFT:
            case ARRAY_SLICE:
            case ARRAY_SOME:	
            case ARRAY_SORT:
            case ARRAY_SPLICE:
            case ARRAY_UNSHIFT:
            case ARRAY_INDEXOF:
                res = JSArray.evaluate(nativeobject, call, state, c);
                break;

            case NUMBER:
            case NUMBER_TOEXPONENTIAL:
            case NUMBER_TOFIXED:
            case NUMBER_TOPRECISION:
            case NUMBER_TOLOCALESTRING:
            case NUMBER_TOSTRING:
            case NUMBER_VALUEOF:
                res = JSNumber.evaluate(nativeobject, call, state, c);
                break;

            case EVAL_ERROR:
            case RANGE_ERROR:
            case REFERENCE_ERROR:
            case SYNTAX_ERROR:
            case TYPE_ERROR:
            case URI_ERROR:
            case ERROR:
            case ERROR_TOSTRING:
                res = JSError.evaluate(nativeobject, call, state, c);
                break;

            case REGEXP:
            case REGEXP_EXEC:
            case REGEXP_TEST:
            case REGEXP_TOSTRING:
                res = JSRegExp.evaluate(nativeobject, call, state, c);
                break;

            case DATE:
            case DATE_GETDATE:
            case DATE_GETDAY:
            case DATE_GETFULLYEAR:
            case DATE_GETHOURS:
            case DATE_GETMILLISECONDS:
            case DATE_GETMINUTES:
            case DATE_GETMONTH:
            case DATE_GETSECONDS:
            case DATE_GETTIME:
            case DATE_GETTIMEZONEOFFSET:
            case DATE_GETUTCDATE:
            case DATE_GETUTCDAY:
            case DATE_GETUTCFULLYEAR:
            case DATE_GETUTCHOURS:
            case DATE_GETUTCMILLISECONDS:
            case DATE_GETUTCMINUTES:
            case DATE_GETUTCMONTH:
            case DATE_GETUTCSECONDS:
            case DATE_PARSE:
            case DATE_SETDATE:
            case DATE_SETFULLYEAR:
            case DATE_SETHOURS:
            case DATE_SETMILLISECONDS:
            case DATE_SETMINUTES:
            case DATE_SETMONTH:
            case DATE_SETSECONDS:
            case DATE_SETTIME:
            case DATE_SETUTCDATE:
            case DATE_SETUTCFULLYEAR:
            case DATE_SETUTCHOURS:
            case DATE_SETUTCMILLISECONDS:
            case DATE_SETUTCMINUTES:
            case DATE_SETUTCMONTH:
            case DATE_SETUTCSECONDS:
            case DATE_TODATESTRING:
            case DATE_TOLOCALEDATESTRING:
            case DATE_TOLOCALESTRING:
            case DATE_TOLOCALETIMESTRING:
            case DATE_TOSTRING:
            case DATE_TOTIMESTRING:
            case DATE_TOUTCSTRING:
            case DATE_UTC:
            case DATE_VALUEOF:
            case DATE_GETYEAR:
            case DATE_SETYEAR:
            case DATE_TOGMTSTRING:
                res = JSDate.evaluate(nativeobject, call, state, c);
                break;

            case STRING:
            case STRING_VALUEOF:
            case STRING_TOSTRING:
            case STRING_REPLACE:
            case STRING_TOUPPERCASE:
            case STRING_TOLOCALEUPPERCASE:
            case STRING_TOLOWERCASE:
            case STRING_TOLOCALELOWERCASE:
            case STRING_SUBSTRING:
            case STRING_SUBSTR:
            case STRING_SPLIT:
            case STRING_SLICE:
            case STRING_SEARCH:
            case STRING_MATCH:
            case STRING_LOCALECOMPARE:
            case STRING_LASTINDEXOF:
            case STRING_INDEXOF:
            case STRING_CONCAT:
            case STRING_FROMCHARCODE:
            case STRING_CHARCODEAT:
            case STRING_CHARAT:
                res = JSString.evaluate(nativeobject, call, state, c);
                break;

            case BOOLEAN:
            case BOOLEAN_TOSTRING:
            case BOOLEAN_VALUEOF:
                res = JSBoolean.evaluate(nativeobject, call, state, c);
                break;

            case MATH_ABS:
            case MATH_SIN:
            case MATH_COS:
            case MATH_TAN:
            case MATH_ATAN:
            case MATH_SQRT:
            case MATH_ROUND:
            case MATH_CEIL:
            case MATH_ASIN:
            case MATH_ACOS:
            case MATH_EXP:
            case MATH_FLOOR:
            case MATH_LOG:
            case MATH_ATAN2:
            case MATH_POW:
            case MATH_MAX:
            case MATH_MIN:
            case MATH_RANDOM:
                res = JSMath.evaluate(nativeobject, call, state, c);
                break;

            //case EVAL:
            case PARSEINT:
            case PARSEFLOAT:
            case ISNAN:
            case ISFINITE:
            case DECODEURI:
            case DECODEURICOMPONENT:
            case ENCODEURI:
            case ENCODEURICOMPONENT:
            case PRINT:
            case ALERT:
            case ESCAPE:
            case UNESCAPE:
            case TRACE:
            case DUMPDEPENDENCY:
            case ASSERT:
            case DUMPVALUE:
            case DUMPPROTOTYPE:
            case DUMPOBJECT:
            case DUMPSTATE:
            case DUMPMODIFIEDSTATE:
            case DUMPATTRIBUTES:
            case DUMPOBJECTORIGIN:
            case CONVERSION_TO_PRIMITIVE:
            case ASSUME_NON_NULLUNDEF:
            case TAJS_GET_DOCUMENT_EVENT:
            case TAJS_GET_KEYBOARD_EVENT:
            case TAJS_GET_MOUSE_EVENT:
            case TAJS_GET_UI_EVENT:
            case TAJS_GET_EVENT_LISTENER:
            case TAJS_GET_WHEEL_EVENT:
                res = JSGlobal.evaluate(nativeobject, call, state, c);
                break;

            case ARRAY_PROTOTYPE:
            case BOOLEAN_PROTOTYPE:
            case DATE_PROTOTYPE:
            case ERROR_PROTOTYPE:
            case EVAL_ERROR_PROTOTYPE:
            case GLOBAL:
            case MATH:
            case NUMBER_PROTOTYPE:
            case OBJECT_PROTOTYPE:
            case RANGE_ERROR_PROTOTYPE:
            case REFERENCE_ERROR_PROTOTYPE:
            case REGEXP_PROTOTYPE:
            case STRING_PROTOTYPE:
            case SYNTAX_ERROR_PROTOTYPE:
            case TYPE_ERROR_PROTOTYPE:
            case URI_ERROR_PROTOTYPE:
                throw new RuntimeException("Native object is not a function: " + nativeobject);
        }

        if (res == null)
            throw new RuntimeException("No transfer function for native function " + nativeobject);
        return res;
    }

}
