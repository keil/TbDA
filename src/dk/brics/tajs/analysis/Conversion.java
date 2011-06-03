package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.Collection;
import java.util.Set;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;

/**
 * Type conversions for abstract values (Chapter 9).
 */
public class Conversion {

	/**
	 * Preferred type for conversion.
	 */
	public enum Hint {
		NONE, NUM, STR;
	}

	private Conversion() {}

	/**
	 * 8.6.2.6 [[DefaultValue]].
	 */
	public static Value defaultValue(ObjectLabel obj, Hint hint, Solver.SolverInterface c) { // FIXME: may call valueOf/toString ! (Peter)
		final String MSG = "TypeError when computing default value for object";
		if (hint.equals(Hint.NONE))
			hint = obj.getKind().equals(Kind.DATE) ? Hint.STR : Hint.NUM;
		State s = c.getCurrentState();
		Value hasValueOf = s.hasProperty(obj, "valueOf");
		Value hasToString = s.hasProperty(obj, "toString");
		if (hasValueOf.isBottom() || hasToString.isBottom())
			return Value.makeBottom(new Dependency());
		if (hasValueOf.isMaybeAnyBool() || hasToString.isMaybeAnyBool()) {
			c.addMessage(Status.MAYBE, Severity.HIGH, MSG); 
			Exceptions.throwTypeError(s, c);
			return Value.makeAnyNum(new Dependency()).joinAnyStr();
		} else
			c.addMessage(Status.NONE, Severity.HIGH, MSG);
		if (hasValueOf.isMaybeFalseButNotTrue() && hasToString.isMaybeFalseButNotTrue()) {
			c.addMessage(Status.CERTAIN, Severity.HIGH, MSG); 
			Exceptions.throwTypeError(s, c);
			return Value.makeBottom(new Dependency()); // no ordinary flow (may be called in a loop, so don't set s to bottom)
		} 
		switch (hint) {
		case NUM:
			if (hasValueOf.isMaybeTrueButNotFalse())
				return internalValueOf(obj, c);
			else // hasToString.isMaybeTrueButNotFalse()
				return internalToString(obj, c);				
		case STR:
			if (hasToString.isMaybeTrueButNotFalse())
				return internalToString(obj, c);
			else // hasValueOf.isMaybeTrueButNotFalse()
				return internalValueOf(obj, c);
		default:
			throw new RuntimeException();
		}
	}

	private static Value internalToString(ObjectLabel obj, Solver.SolverInterface c) {
		switch(obj.getKind()) {
		case BOOLEAN:
		case NUMBER:
		case STRING:
		case REGEXP:
			Set<ObjectLabel> ols = newSet();
			ols.add(obj);
			return toString(c.getCurrentState().readInternalValue(ols), c);
		case OBJECT:
		case MATH:
			return Value.makeStr("[object "+ obj.getKind() + "]", new Dependency());
		case ERROR:
		case FUNCTION:
		case DATE:
		case ARRAY:
		case ACTIVATION:
		case ARGUMENTS:
			return Value.makeAnyStr(new Dependency());
		}
		throw new RuntimeException();
	}

	private static Value internalValueOf(ObjectLabel obj, Solver.SolverInterface c) {
		switch(obj.getKind()) {
		case BOOLEAN:
		case NUMBER:
		case STRING:
			Set<ObjectLabel> ols = newSet();
			ols.add(obj);
			return c.getCurrentState().readInternalValue(ols);
		case OBJECT:
		case MATH:
		case FUNCTION:
		case ARRAY:
		case REGEXP:
		case ERROR:
			return Value.makeNum(Double.NaN, new Dependency());
		case DATE:
			return Value.makeAnyNumUInt(new Dependency());
		case ACTIVATION:
		case ARGUMENTS:
			return Value.makeAnyNum(new Dependency());				
		}
		throw new RuntimeException();
	}

	/**
	 * 9.2 ToBoolean.
	 */
	public static Value toBoolean(Value v) {
		Value result = v.restrictToBool();
		if (v.isMaybeUndef())
			result = result.joinBool(false);
		if (v.isMaybeNull())
			result = result.joinBool(false);
		if (!v.isNotNum()) {
			if (v.isNaN())
				result = result.joinBool(false);
			else if (v.isMaybeSingleNum()) {
				if (Math.abs(v.getNum()) == 0.0)
					result = result.joinBool(false);
				else
					result = result.joinBool(true);
			} else // TODO: could still be join(NaN,0.0) or neither  - needs zero in the lattice
				result = result.joinAnyBool();
		}
		if (!v.isNotStr()) {
			if (v.isMaybeSingleStr()) {
				if (v.getStr().equals(""))
					result = result.joinBool(false);
				else
					result = result.joinBool(true);
			} else if (v.isMaybeStrOnlyUInt()) // always non-empty
				result = result.joinBool(true);
			else // TODO: could still always be non-empty - would need to distinguish empty-string in the lattice
				result = result.joinAnyBool();
		}
		if (v.isMaybeObject())
			result = result.joinBool(true);
		return result;
	}

	/**
	 * 9.3 Boolean to Number.
	 */
	public static Value fromBooltoNum(Value b) {
		if (b.isNotBool())
			return Value.makeBottom(b.getDependency());
		else if (b.isMaybeAnyBool())
			return Value.makeAnyNumUInt(b.getDependency());
		else if (b.isMaybeFalseButNotTrue())
			return Value.makeNum(0.0, b.getDependency());
		else
			return Value.makeNum(1.0, b.getDependency());
	}

	/**
	 * 9.3.1 String to Number.
	 */
	public static Value fromStrtoNum(Value str, Solver.SolverInterface c) {
		final String MSG = "Conversion from string to number yields NaN";
		if (str.isNotStr())
			return Value.makeBottom(str.getDependency());
		if (str.isMaybeSingleStr()) {
			String s = str.getStr();
			if (s.equals(""))
				return Value.makeNum(0.0, str.getDependency());
			else {
				s = s.trim();
				if (s.matches("[-+]?(Infinity|([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([eE][-+]?[0-9]+)?)")) {
					c.addMessage(Status.NONE, Severity.LOW, MSG);
					return Value.makeNum(new Double(s), str.getDependency());
				} if (s.matches("0[xX][0-9a-fA-F]+")) {
					c.addMessage(Status.NONE, Severity.LOW, MSG);
					return Value.makeNum(Long.parseLong(s.substring(2), 16), str.getDependency());
				} else  {
					c.addMessage(Status.MAYBE, Severity.LOW, MSG);
					return Value.makeNumNaN(str.getDependency());
				}
			}
		} else if (!str.isMaybeStrNotUInt())
			return Value.makeAnyNumUInt(str.getDependency());
		else if (!str.isMaybeStrUInt())
			return Value.makeAnyNumNotUInt(str.getDependency());
		else
			return Value.makeAnyNum(str.getDependency());
	}

	/**
	 * 9.3 ToNumber.
	 */
	public static Value toNumber(Value v, Solver.SolverInterface c) {
		if (v.isMaybeObject()) 
			v = toPrimitive(v, Hint.NUM, c);
		Value result = v.restrictToNum();
		Status s;
		if (v.isMaybeUndef()) {
			if (!v.isMaybeOtherThanUndef())
				s = Status.CERTAIN;
			else
				s = Status.MAYBE;
			result = result.joinNumNaN();
		} else 
			s = Status.NONE;
		c.addMessage(s, Severity.LOW, "Conversion to number yields NaN");
		if (v.isMaybeNull())
			result = result.joinNum(0.0);
		result = Value.join(result, fromBooltoNum(v), fromStrtoNum(v, c));
		return result;
	}
	
	/**
	 * 9.1 ToPrimitive.
	 * Converts a value to a primitive value according to the hint.
	 * Has no effect on primitive types but transforms wrapper objects to their wrapped values.
	 */
	public static Value toPrimitive(Value v, Hint hint, Solver.SolverInterface c) {
		Collection<Value> vs = newList();
		Value nonobj = v.restrictToNonObject();
		if (!nonobj.isNoValue())
			vs.add(nonobj);
		for (ObjectLabel ol : v.getObjectLabels()) 
			vs.add(defaultValue(ol, hint, c));
		return Value.join(vs);
	}
	
	/**
	 * 9.4 ToInteger.
	 */
	public static Value toInteger(Value v, Solver.SolverInterface c) {
		Value num = toNumber(v, c);
		if (num.isNotNum())
			return num;
		if (num.isNaN())
			return Value.makeNum(0, v.getDependency());
		if (num.isMaybeSingleNum()) {
			Double d = num.getNum();
			if (d == 0.0 || d == -0.0 || Double.isInfinite(d))
				return num;
			else
				return Value.makeNum(Math.signum(d) * Math.floor(Math.abs(d)), v.getDependency());			
		} else {
			Value r = Value.makeBottom(v.getDependency());
			if (num.isMaybeNaN())
				r = r.joinNum(0);
			if (num.isMaybeInf())
				r = r.joinNumInf();
			if (num.isMaybeNumUInt()) 
				r = r.joinAnyNumUInt();
			if (num.isMaybeNumNotUInt()) 
				r = r.joinAnyNumNotUInt();
			return r;
		}
	}

	/**
	 * 9.5 ToInt32.
	 */
	public static int toInt32(double nm) {
		if (Double.isNaN(nm) || Double.isInfinite(nm))
			return 0;
		double w = Math.signum(nm) * Math.floor(Math.abs(nm));
		Double v = w % 4294967296L;
		if (v < 0)
			v += 4294967296L;
		if (v > 2147483648L) 
			v -= 4294967296L;
		return v.intValue();
	}

	/**
	 * 9.6 ToUInt32.
	 */
	public static long toUInt32(double nm) {
		if (Double.isNaN(nm) || Double.isInfinite(nm)) 
			return 0L;
		double w = Math.signum(nm) * Math.floor(Math.abs(nm));
		Double v = w % 4294967296L;
		if (v < 0)
			v += 4294967296L;
		return v.longValue();
	}
	
	/**
	 * 9.7 ToUInt16.
	 */
	public static long toUInt16(double nm) {
		if (Double.isNaN(nm) || Double.isInfinite(nm) || nm == -0.0)
			return 0L;
		double w = Math.signum(nm) * Math.floor(Math.abs(nm));
		Double v = w % 65536L;
		if (v < 0)
			v += 65536L;
		return v.longValue();
	}

	/**
	 * 9.8 ToString.
	 */
	public static Value toString(Value v, Solver.SolverInterface c) {
		final String MSG_BOOLEAN = "Converting boolean to string";
		final String MSG_NULL = "Converting null to string";
		final String MSG_UNDEFINED = "Converting undefined to string";
		final String MSG_NAN = "Converting NaN to string";
		final String MSG_INF = "Converting Infinity to string";
		if (v.isMaybeObject())
			v = toPrimitive(v, Hint.STR, c);
		Value result = v.restrictToStr();
		if (!v.isNotBool()) {
			c.addMessage(Status.MAYBE, Severity.LOW, MSG_BOOLEAN);
			if (v.isMaybeAnyBool())
				result = result.joinAnyStr();
			else if (v.isMaybeTrueButNotFalse())
				result = result.joinStr("true");
			else
				result = result.joinStr("false");
		} else 
			c.addMessage(Status.NONE, Severity.LOW, MSG_BOOLEAN);		
		if (!v.isNotNum()) {
			if (v.isNaN()) {
				result = result.joinStr(Double.toString(Double.NaN));
				c.addMessage(Status.CERTAIN, Severity.LOW, MSG_NAN);
			} else if (v.isMaybeSingleNum()) {
				String s;
				double dbl = v.getNum();
				if (Double.isInfinite(dbl)) {
					s = Double.toString(dbl);
					c.addMessage(Status.CERTAIN, Severity.LOW, MSG_INF);
				} else if (Math.floor(dbl) == dbl)
					s = Long.toString((long)dbl);
				else
					s = Double.toString(dbl);
				result = result.joinStr(s);
			} else if (v.isMaybeNumUInt())
				result = result.joinAnyStrUInt();		
			else
				result = result.joinAnyStr();				
		}
		if (v.isMaybeNull()) {
			c.addMessage(v.isMaybeOtherThanNull() ? Status.MAYBE : Status.CERTAIN, Severity.LOW, MSG_NULL);
			result = result.joinStr("null");
		} else 	
			c.addMessage(Status.NONE, Severity.LOW, MSG_NULL);
		if (v.isMaybeUndef()) {
			c.addMessage(v.isMaybeOtherThanNull() ? Status.MAYBE : Status.CERTAIN, Severity.LOW, MSG_UNDEFINED);
			result = result.joinStr("undefined");
		} else 
			c.addMessage(Status.NONE, Severity.LOW, MSG_UNDEFINED);
		return result;
	}

	/**
	 * 9.9 ToObject, returning a Value.
	 * Note that this may have side-effects on the state!
	 */
	public static Value toObject(State state, Node node, Value v, Solver.SolverInterface c) {
		return Value.makeObject(toObjectLabels(state, node, v, c), v.getDependency());
	}
	
	/**
	 * 9.9 ToObject, returning a set of object labels.
	 * Note that this may have side-effects on the state!
	 * In particular, the state may be set to bottom if no non-exceptional flow is possible.
	 * However, if the solver interface is null, no side-effects or messages are produced.
	 */
	public static Set<ObjectLabel> toObjectLabels(State state, Node node, Value v, 
			Solver.SolverInterface c) {
		final String MSG_NUMBER = "Converting primitive number to object";
		final String MSG_BOOLEAN = "Converting primitive boolean to object";
		final String MSG_STRING = "Converting primitive string to object";
		final String MSG_NULL = "TypeError, converting null to object";
		final String MSG_UNDEFINED = "TypeError, converting undefined to object";
		final String MSG_OBJECT = "No object present";
		Set<ObjectLabel> result = newSet();
		result.addAll(v.getObjectLabels());
		if (!v.isNotNum()) {
			if (c != null)
				c.addMessage(v.isMaybeOtherThanNum() ? Status.MAYBE : Status.CERTAIN, Severity.LOW, MSG_NUMBER);
			ObjectLabel lNum = new ObjectLabel(node, Kind.NUMBER);
			if (c != null) {
				state.newObject(lNum);
				state.writeInternalPrototype(lNum, Value.makeObject(InitialStateBuilder.NUMBER_PROTOTYPE, v.getDependency()));
				state.writeInternalValue(lNum, v.restrictToNum());
			}
			result.add(lNum);
		}
		else if (c != null)
			c.addMessage(Status.NONE, Severity.LOW, MSG_NUMBER);
		if (!v.isNotBool()) {
			if (c != null)
				c.addMessage(v.isMaybeOtherThanBool() ? Status.MAYBE : Status.CERTAIN, Severity.LOW, MSG_BOOLEAN);
			ObjectLabel lBool = new ObjectLabel(node, Kind.BOOLEAN);
			if (c != null) {
				state.newObject(lBool);
				state.writeInternalPrototype(lBool, Value.makeObject(InitialStateBuilder.BOOLEAN_PROTOTYPE, v.getDependency()));
				state.writeInternalValue(lBool, v.restrictToBool());
			}
			result.add(lBool);
		} else if (c != null)
			c.addMessage(Status.NONE, Severity.LOW, MSG_BOOLEAN);
		if (!v.isNotStr()) {
			if (c != null)
				c.addMessage(v.isMaybeOtherThanStr() ? Status.MAYBE : Status.CERTAIN, Severity.LOW, MSG_STRING);
			Value vstring = v.restrictToStr();
			Value vlength = vstring.isMaybeSingleStr() ? Value.makeNum(vstring.getStr().length(), v.getDependency()) : Value.makeAnyNumUInt(v.getDependency());
			ObjectLabel lString = new ObjectLabel(node, Kind.STRING);
			if (c != null) {
				state.newObject(lString);
				state.writeInternalPrototype(lString, Value.makeObject(InitialStateBuilder.STRING_PROTOTYPE, v.getDependency()));
				state.writeInternalValue(lString, vstring);
				state.writeSpecialProperty(lString, "length", vlength.setAttributes(true, true, true));
			}
			result.add(lString);
		} else if (c != null)
			c.addMessage(Status.NONE, Severity.LOW, MSG_STRING);
		if (c != null)
			if (result.isEmpty()) {
				if (!v.isNotNull()) {
					c.addMessage(Status.CERTAIN, Severity.HIGH, MSG_NULL);
					Exceptions.throwTypeError(state, c); // no ordinary flow (may be called in a loop, so don't set s to bottom)
				} else 
					c.addMessage(Status.NONE, Severity.HIGH, MSG_NULL);
				if (!v.isNotUndef()) {
					c.addMessage(Status.CERTAIN, Severity.HIGH, MSG_UNDEFINED);
					Exceptions.throwTypeError(state, c); // no ordinary flow (may be called in a loop, so don't set s to bottom)
				} else 
					c.addMessage(Status.NONE, Severity.HIGH, MSG_UNDEFINED);
				c.addMessage(Status.CERTAIN, Severity.LOW, MSG_OBJECT);
			} else {
				if (!v.isNotNull()) {
					c.addMessage(Status.MAYBE, Severity.HIGH, MSG_NULL);
					Exceptions.throwTypeError(state, c);
				} else 
					c.addMessage(Status.NONE, Severity.HIGH, MSG_NULL);
				if (!v.isNotUndef()) {
					c.addMessage(Status.MAYBE, Severity.HIGH, MSG_UNDEFINED);
					Exceptions.throwTypeError(state, c);
				} else 
					c.addMessage(Status.NONE, Severity.HIGH, MSG_UNDEFINED);
				c.addMessage(Status.NONE, Severity.LOW, MSG_OBJECT);
			}
		return result;
	}
}
