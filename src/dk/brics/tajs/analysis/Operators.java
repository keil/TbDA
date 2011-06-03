package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Collection;
import java.util.Set;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyLabel;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.Num;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;

/**
 * Evaluation of operators on abstract values (Chapter 11).
 */
public class Operators {

	private Operators() {
	}

	private enum NumericOp {
		ADD, SUB, MUL, DIV, MOD
	}

	private enum BitwiseOp {
		AND, OR, XOR
	}

	private enum ShiftOp {
		LEFTSHIFT, SIGNEDRIGHTSHIFT, UNSIGNEDRIGHTSHIFT
	}

	/**
	 * 11.4.3 <code>typeof</code>
	 */
	public static Value typeof(Value v, boolean base_maybe_null) {
		boolean maybe_boolean = !v.isNotBool();
		boolean maybe_number = !v.isNotNum();
		boolean maybe_string = !v.isNotStr();
		boolean maybe_undefined = v.isMaybeUndef() || base_maybe_null;
		boolean maybe_object = v.isMaybeNull();
		boolean maybe_function = false;
		for (ObjectLabel objlabel : v.getObjectLabels())
			if (objlabel.getKind() == Kind.FUNCTION)
				maybe_function = true;
			else
				maybe_object = true;
		int count = (maybe_boolean ? 1 : 0) + (maybe_number ? 1 : 0)
				+ (maybe_string ? 1 : 0) + (maybe_undefined ? 1 : 0)
				+ (maybe_object ? 1 : 0) + (maybe_function ? 1 : 0);
		if (count > 1)
			return Value.makeAnyStr(v.getDependency());
		else { // table p. 47
			String s;
			if (maybe_boolean)
				s = "boolean";
			else if (maybe_number)
				s = "number";
			else if (maybe_string)
				s = "string";
			else if (maybe_undefined)
				s = "undefined";
			else if (maybe_object)
				s = "object";
			else if (maybe_function)
				s = "function";
			else
				return Value.makeBottom(v.getDependency());
			return Value.makeStr(s, v.getDependency());
		}
	}

	/**
	 * 11.4.6 <code>+</code> (unary)
	 */
	public static Value uplus(Value v, Solver.SolverInterface c) {
		return Conversion.toNumber(v, c);
	}

	/**
	 * 11.4.7 <code>-<code> (unary)
	 */
	public static Value uminus(Value v, Solver.SolverInterface c) {
		Value nm = Conversion.toNumber(v, c);
		if (nm.isNotNum())
			return Value.makeBottom(v.getDependency());
		else if (nm.isMaybeSingleNum())
			return Value.makeNum(-nm.getNum(), v.getDependency());
		else
			return nm;
	}

	/**
	 * 11.4.8 <code>~</code> (bitwise not)
	 */
	public static Value complement(Value v, Solver.SolverInterface c) {
		Value nm = Conversion.toNumber(v, c);
		if (nm.isNotNum())
			return Value.makeBottom(v.getDependency());
		else if (nm.isMaybeSingleNum())
			return Value.makeNum(~Conversion.toInt32(nm.getNum()),
					v.getDependency());
		else
			return Value.makeAnyNumNotNaNInf(v.getDependency());
	}

	/**
	 * 11.4.9 <code>&#33;</code> (logical not)
	 */
	public static Value not(Value v, Solver.SolverInterface c) {
		Bool bv = Conversion.toBoolean(v);
		if (bv.isNotBool())
			return Value.makeBottom(v.getDependency());
		else if (bv.isMaybeTrueButNotFalse())
			return Value.makeBool(false, v.getDependency());
		else if (bv.isMaybeFalseButNotTrue())
			return Value.makeBool(true, v.getDependency());
		return Value.makeAnyBool(v.getDependency());
	}

	/**
	 * 11.5.1 <code>*</code>
	 */
	public static Value mul(Value v1, Value v2, Solver.SolverInterface c) {
		return numeric(NumericOp.MUL, v1, v2, c);
	}

	/**
	 * 11.5.2 <code>/</code>
	 */
	public static Value div(Value v1, Value v2, Solver.SolverInterface c) {
		return numeric(NumericOp.DIV, v1, v2, c);
	}

	/**
	 * 11.5.3 <code>%</code>
	 */
	public static Value rem(Value v1, Value v2, Solver.SolverInterface c) {
		return numeric(NumericOp.MOD, v1, v2, c);
	}

	/**
	 * 11.5 Multiplicative operators and 11.6.3 Applying the Additive operators
	 * to numbers
	 */
	private static Value numeric(NumericOp op, Value v1, Value v2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		// FIXME
		switch (op) {
		case ADD:
			break;
		case SUB:
			break;
		case MUL:
			break;
		case DIV:
			break;
		case MOD:
			break;
		}

		if (v1.isNoValue() || v2.isNoValue())
			return Value.makeBottom(dependency);
		Value arg1 = Conversion.toNumber(v1, c);
		Value arg2 = Conversion.toNumber(v2, c);
		if (arg1.isMaybeSingleNum() && arg2.isMaybeSingleNum()) {
			Double d1 = arg1.getNum();
			Double d2 = arg2.getNum();
			double r = 0;
			switch (op) {
			case ADD:
				r = d1 + d2;
				break;
			case SUB:
				r = d1 - d2;
				break;
			case MUL:
				r = d1 * d2;
				break;
			case DIV:
				r = d1 / d2;
				break;
			case MOD:
				r = d1 % d2;
				break;
			}
			return Value.makeNum(r, dependency);
		}
		Value r = Value.makeBottom(dependency);
		Value zero = r.joinNum(0.0).joinNum(-0.0); // TODO: bad for precision?
		if (arg1.isMaybeNaN() || arg2.isMaybeNaN()) {
			r = r.joinNumNaN();
			if (arg1.isNaN() || arg2.isNaN())
				return r;
		}
		switch (op) {
		case ADD:
		case SUB:
			if (arg1.isMaybeInf() && arg2.isMaybeInf())
				r = r.joinNumNaN();
			if (arg1.isMaybeInf() || arg2.isMaybeInf())
				r = r.joinNumInf();
			if (arg1.isMaybeNumUInt() && arg2.isMaybeNumUInt())
				r = r.joinAnyNumUInt(); // FIXME: be sound unless
										// Options.isUnsound() is set (Peter)
			else if (!arg1.isNotNum() || !arg2.isNotNum())
				r = r.joinAnyNumUInt().joinAnyNumNotUInt();
			break;
		case MUL: // TODO: it would be useful to know if a value is 0, too
			if (arg1.isMaybeInf() && arg2.isMaybeInf())
				r = r.joinNumInf();
			if (arg1.isMaybeInf() && !arg2.isNotNum())
				r = r.joinNumNaN().joinNumInf();
			if (!arg1.isNotNum() && arg2.isMaybeInf())
				r = r.joinNumNaN().joinNumInf();
			if (!arg1.isNotNum() && !arg2.isNotNum())
				r = r.joinAnyNumUInt().joinAnyNumNotUInt();
			break;
		case DIV:
			if (arg1.isMaybeInf() && arg2.isMaybeInf())
				r = r.joinNumNaN();
			if (arg1.isMaybeInf() && !arg2.isNotNum())
				r = r.joinNumInf();
			if (!arg1.isNotNum() && arg2.isMaybeInf())
				r = r.join(zero);
			if (!arg1.isNotNum() && !arg2.isNotNum())
				r = r.joinAnyNumUInt().joinAnyNumNotUInt().joinNumNaN()
						.joinNumInf(); // TODO: (use Options.isUnsound()) - can
										// avoid NaN here sometimes! requires
										// isZero
			break;
		case MOD:
			if (arg1.isMaybeInf() && arg2.isMaybeInf())
				r = r.joinNumNaN();
			if (arg1.isMaybeInf() && !arg2.isNotNum())
				r = r.joinNumNaN();
			if (!arg1.isNotNum() && arg2.isMaybeInf())
				r = r.join(arg1);
			if (!arg1.isNotNum() && !arg2.isNotNum())
				r = r.joinAnyNumUInt().joinAnyNumNotUInt().joinNumNaN(); // TODO:
																			// (use
																			// Options.isUnsound())
																			// -
																			// can
																			// avoid
																			// NaN
																			// here
																			// sometimes!
																			// requires
																			// isZero
																			// (benchpress2.js)
			break;
		}
		return r;
	}

	/**
	 * Numeric addition
	 */
	private static Value addNumbers(Value v1, Value v2, Solver.SolverInterface c) {
		return numeric(NumericOp.ADD, v1, v2, c);
	}

	/**
	 * 11.6.1 <code>+</code> (binary)
	 */
	public static Value add(Value v1, Value v2, Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		Value p1 = Conversion.toPrimitive(v1, Conversion.Hint.NONE, c);
		Value p2 = Conversion.toPrimitive(v2, Conversion.Hint.NONE, c);
		// is neither p1 nor p2 a string?
		if (p1.isNotStr() && p2.isNotStr()) {
			// now it is definitively not a string operator
			return addNumbers(p1, p2, c);
		}
		// at least one operand *may be* a string
		Value r = Value.makeBottom(dependency);
		if (p1.isMaybeFuzzyStr() || p2.isMaybeFuzzyStr()) {
			// TODO: could improve precision for concatenation of uint /
			// non-uint strings
			/* p1 may be str or p2 may be str */
			r = r.joinAnyStr();
		} else {
			/* p1 is not Tstr and p2 is not Tstr */
			if (!p1.isNotStr()) {
				/* p1 is a defined string */
				Value s2 = Conversion.toString(p2, c);
				if (s2.isMaybeFuzzyStr())
					r = r.joinAnyStr();
				else if (!s2.isNotStr())
					r = r.joinStr(p1.getStr() + s2.getStr());
			} else if (!p2.isNotStr()) {
				/* p2 is a defined string, but p1 is not */
				Value s1 = Conversion.toString(p1, c);
				if (s1.isMaybeFuzzyStr())
					r = r.joinAnyStr();
				else if (!s1.isNotStr())
					r = r.joinStr(s1.getStr() + p2.getStr());
			} else {
				/* neither p1 nor p2 is a string */
			}
		}
		// now we are done, if one argument is definitively a string
		if (p1.isNotNull() && p1.isNotBool() && p1.isNotNum()
				&& p1.isNotUndef() || p2.isNotNull() && p2.isNotBool()
				&& p2.isNotNum() && p2.isNotUndef())
			return r;
		// otherwise + might be interpreted as a numeric add, too
		Value n1 = Value.join(p1.restrictToBool(), p1.restrictToNull(),
				p1.restrictToNum(), p1.restrictToUndef());
		Value n2 = Value.join(p2.restrictToBool(), p2.restrictToNull(),
				p2.restrictToNum(), p2.restrictToUndef());
		return r.join(addNumbers(n1, n2, c));
	}

	/**
	 * 11.6.2 <code>-</code> (binary)
	 */
	public static Value sub(Value v1, Value v2, Solver.SolverInterface c) {
		return numeric(NumericOp.SUB, v1, v2, c);
	}

	/**
	 * 11.7 Bitwise Shift Operators
	 */
	private static Value shiftop(ShiftOp op, Value v1, Value v2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		// FIXME
		switch (op) {
		case LEFTSHIFT:
			break;
		case SIGNEDRIGHTSHIFT:
			break;
		case UNSIGNEDRIGHTSHIFT:
			break;
		}
		
		if (v1.isNoValue() || v2.isNoValue())
			return Value.makeBottom(dependency);
		Value arg1 = Conversion.toNumber(v1, c);
		Value arg2 = Conversion.toNumber(v2, c);
		if (arg1.isMaybeSingleNum() && arg2.isMaybeSingleNum()) {
			Double d1 = arg1.getNum();
			Double d2 = arg2.getNum();
			double r = 0;
			switch (op) {
			case LEFTSHIFT:
				r = Conversion.toInt32(d1) << Conversion.toUInt32(d2);
				break;
			case SIGNEDRIGHTSHIFT:
				r = Conversion.toInt32(d1) >> Conversion.toUInt32(d2);
				break;
			case UNSIGNEDRIGHTSHIFT:
				r = Conversion.toUInt32(d1) >>> Conversion.toUInt32(d2);
				break;
			}
			return Value.makeNum(r, dependency);
		} else
			return Value.makeBottom(dependency).joinAnyNumUInt();
	}

	/**
	 * 11.7.1 <code>&lt;&lt;</code> (left shift)
	 */
	public static Value shl(Value v1, Value v2, Solver.SolverInterface c) {
		return shiftop(ShiftOp.LEFTSHIFT, v1, v2, c);
	}

	/**
	 * 11.7.2 <code>&gt;&gt;</code> (signed right shift)
	 */
	public static Value shr(Value v1, Value v2, Solver.SolverInterface c) {
		return shiftop(ShiftOp.SIGNEDRIGHTSHIFT, v1, v2, c);
	}

	/**
	 * 11.7.3 <code>&gt;&gt;&gt;</code> (unsigned right shift)
	 */
	public static Value ushr(Value v1, Value v2, Solver.SolverInterface c) {
		return shiftop(ShiftOp.UNSIGNEDRIGHTSHIFT, v1, v2, c);
	}

	/**
	 * 11.8.1 <code>&lt;</code>
	 */
	public static Value lt(Value v1, Value v2, Solver.SolverInterface c) {
		return abstractRelationalComparison(v1, v2, c);
	}

	/**
	 * 11.8.2 <code>&gt;</code>
	 */
	public static Value gt(Value v1, Value v2, Solver.SolverInterface c) {
		return abstractRelationalComparison(v2, v1, c);
	}

	/**
	 * 11.8.3 <code>&lt;=</code>
	 */
	public static Value le(Value v1, Value v2, Solver.SolverInterface c) {
		return not(abstractRelationalComparison(v2, v1, c), c);
	}

	/**
	 * 11.8.4 <code>&gt;=</code>
	 */
	public static Value ge(Value v1, Value v2, Solver.SolverInterface c) {
		return not(abstractRelationalComparison(v1, v2, c), c);
	}

	/**
	 * 11.8.5 The Abstract Relational Comparison Algorithm.
	 */
	private static Value abstractRelationalComparison(Value v1, Value v2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		Value p1 = Conversion.toPrimitive(v1, Conversion.Hint.NUM, c);
		Value p2 = Conversion.toPrimitive(v2, Conversion.Hint.NUM, c);
		if (p1.isMaybeFuzzyStr() || p2.isMaybeFuzzyStr() || p1.isMaybeAnyBool()
				|| p2.isMaybeAnyBool() || p1.isMaybeFuzzyNum()
				|| p2.isMaybeFuzzyNum()) {
			Value r = Value.makeAnyBool(dependency);
			if ((p1.isMaybeOtherThanStr() || p2.isMaybeOtherThanStr())
					&& (Conversion.toNumber(p1, c).isMaybeNaN() || Conversion
							.toNumber(p2, c).isMaybeNaN()))
				r = r.joinUndef(); // undefined is the correct outcome. see
									// items 6 and 7!
			return r;
		} else if (p1.isNotStr() || p2.isNotStr()) {
			// at most one argument is a string: perform numeric comparison
			return numericComparison(p1, p2, c);
		} else {
			// (at least) two defined string arguments: perform a string
			// comparison
			Value r;
			String st1 = p1.getStr();
			String st2 = p2.getStr();
			if (st1 != null && st2 != null) {
				if (st1.compareTo(st2) < 0)
					r = Value.makeBool(true, dependency);
				else
					r = Value.makeBool(false, dependency);
			} else
				r = Value.makeBottom(dependency);
			if (p1.isMaybeOtherThanStr() || p2.isMaybeOtherThanStr())
				r = r.join(numericComparison(p1, p2, c));
			return r;
		}
	}

	/**
	 * Numeric comparison, used by abstractRelationalComparison.
	 */
	private static Value numericComparison(Value p1, Value p2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(p1.getDependency());
		dependency.join(p2.getDependency());
		// ##################################################

		if (p1.isNoValue() || p2.isNoValue())
			return Value.makeBottom(dependency);
		Value n1 = Conversion.toNumber(p1, c);
		Value n2 = Conversion.toNumber(p2, c);
		if (n1.isMaybeSingleNum() && n2.isMaybeSingleNum()) {
			Double d1 = n1.getNum();
			Double d2 = n2.getNum();
			if (d1.isNaN() || d2.isNaN())
				return Value.makeUndef(dependency);
			if (d1 < d2)
				return Value.makeBool(true, dependency);
			else
				return Value.makeBool(false, dependency);
		} else {
			Value r = Value.makeAnyBool(dependency);
			if (n1.isMaybeNaN() || n2.isMaybeNaN())
				r = r.joinUndef();
			return r;
		}
	}

	/**
	 * 11.8.6 <code>instanceof</code>
	 */
	public static Value instof(State state, Value v1, Value v2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		// 11.8.6 step 5-6
		boolean maybe_v2_non_function = v2.isMaybePrimitive();
		boolean maybe_v2_function = false;
		Set<ObjectLabel> v2_objlabels = v2.getObjectLabels();
		for (ObjectLabel objlabel : v2_objlabels)
			if (objlabel.getKind() == Kind.FUNCTION)
				maybe_v2_function = true;
			else
				maybe_v2_non_function = true;
		if (maybe_v2_function || maybe_v2_non_function)
			c.addMessage(
					maybe_v2_function ? maybe_v2_non_function ? Status.MAYBE
							: Status.NONE : Status.CERTAIN, Severity.HIGH,
					"TypeError, non-function-object at 'instanceof'");
		// 15.3.5.3 step 1-4
		Value v2_prototype = state.readProperty(v2_objlabels, "prototype");
		boolean maybe_v2_prototype_primitive = false;
		boolean maybe_v2_prototype_nonprimitive = false;
		if (v2_prototype.isMaybeObject())
			maybe_v2_prototype_nonprimitive = true;
		if (v2_prototype.isMaybePrimitive())
			maybe_v2_prototype_primitive = true;
		if (maybe_v2_prototype_nonprimitive || maybe_v2_prototype_primitive)
			c.addMessage(
					maybe_v2_prototype_nonprimitive ? maybe_v2_prototype_primitive ? Status.MAYBE
							: Status.NONE
							: Status.CERTAIN, Severity.HIGH,
					"TypeError, non-object prototype at 'instanceof'");
		if (maybe_v2_non_function || maybe_v2_prototype_primitive) {
			Exceptions.throwTypeError(state, c);
			if ((maybe_v2_non_function && !maybe_v2_function)
					|| (maybe_v2_prototype_nonprimitive && !maybe_v2_prototype_primitive))
				return Value.makeBottom(dependency);
		}
		return state.hasInstance(v2_prototype.getObjectLabels(), v1);
	}

	/**
	 * 11.8.7 <code>in</code>
	 */
	public static Value in(State state, Value v1, Value v2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		// 11.8.7 step 5
		boolean maybe_v2_object = v2.isMaybeObject();
		boolean maybe_v2_nonobject = v2.isMaybePrimitive();
		if (maybe_v2_object || maybe_v2_nonobject)
			c.addMessage(maybe_v2_object ? maybe_v2_nonobject ? Status.MAYBE
					: Status.NONE : Status.CERTAIN, Severity.HIGH,
					"TypeError, non-object at 'in'");
		if (maybe_v2_nonobject) {
			Exceptions.throwTypeError(state, c);
			if (!maybe_v2_object)
				return Value.makeBottom(dependency);
		}
		// 11.8.7 step 6-8
		Value v1_str = Conversion.toString(v1, c);
		if (v1_str.isMaybeSingleStr())
			return state.hasProperty(v2.getObjectLabels(), v1_str.getStr())
					.joinDependency(dependency);
		else
			return Value.makeAnyBool(dependency);
	}

	/**
	 * 11.9.1 <code>==</code>
	 */
	public static Value eq(Value v1, Value v2, Solver.SolverInterface c) {
		return abstractEqualityComparison(v1, v2, c);
	}

	/**
	 * 11.9.2 <code>&#33;=</code>
	 */
	public static Value neq(Value v1, Value v2, Solver.SolverInterface c) {
		return not(abstractEqualityComparison(v1, v2, c), c);
	}

	/**
	 * 11.9.3 The Abstract Equality Comparison Algorithm.
	 */
	private static Value abstractEqualityComparison(Value v1, Value v2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		Value r = Value.makeBottom(dependency);
		if (v1.isMaybeUndef()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(true);
			if (v2.isMaybeNull())
				r = r.joinBool(true);
			if (!v2.isNotBool())
				r = r.joinBool(false);
			if (!v2.isNotNum())
				r = r.joinBool(false);
			if (!v2.isNotStr())
				r = r.joinBool(false);
			if (v2.isMaybeObject())
				r = r.joinBool(false);
		}
		if (v1.isMaybeNull()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(true);
			if (v2.isMaybeNull())
				r = r.joinBool(true);
			if (!v2.isNotBool())
				r = r.joinBool(false);
			if (!v2.isNotNum())
				r = r.joinBool(false);
			if (!v2.isNotStr())
				r = r.joinBool(false);
			if (v2.isMaybeObject())
				r = r.joinBool(false);
		}
		if (!v1.isNotBool()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool()) {
				Num n1 = Conversion.fromBooltoNum(v1);
				Num n2 = Conversion.fromBooltoNum(v2);
				r = abstractNumberEquality(r, n1, n2);
			}
			if (!v2.isNotNum()) {
				Num n1 = Conversion.fromBooltoNum(v1);
				Num n2 = v2;
				r = abstractNumberEquality(r, n1, n2);
			}
			if (!v2.isNotStr()) {
				Num n1 = Conversion.fromBooltoNum(v1);
				Num n2 = Conversion.fromStrtoNum(v2, c);
				r = abstractNumberEquality(r, n1, n2);
			}
			if (v2.isMaybeObject()) {
				Num n1 = Conversion.fromBooltoNum(v1);
				Num n2 = Conversion.toNumber(Conversion.toPrimitive(
						Value.makeObject(v2.getObjectLabels(), dependency),
						Conversion.Hint.NUM, c), c);
				r = abstractNumberEquality(r, n1, n2);
			}
		}
		if (!v1.isNotNum()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool()) {
				Num n1 = v1;
				Num n2 = Conversion.fromBooltoNum(v2);
				r = abstractNumberEquality(r, n1, n2);
			}
			if (!v2.isNotNum()) {
				Num n1 = v1;
				Num n2 = v2;
				r = abstractNumberEquality(r, n1, n2);
			}
			if (!v2.isNotStr()) {
				Num n1 = v1;
				Num n2 = Conversion.fromStrtoNum(v2, c);
				r = abstractNumberEquality(r, n1, n2);
			}
			if (v2.isMaybeObject()) {
				Value arg1 = v1.restrictToNum();
				Value arg2 = Conversion.toPrimitive(
						Value.makeObject(v2.getObjectLabels(), dependency),
						Conversion.Hint.NONE, c);
				r = r.join(abstractEqualityComparison(arg1, arg2, c));
			}
		}
		if (!v1.isNotStr()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool()) {
				Num n1 = Conversion.fromStrtoNum(v1, c);
				Num n2 = Conversion.fromBooltoNum(v2);
				r = abstractNumberEquality(r, n1, n2);
			}
			if (!v2.isNotNum()) {
				Num n1 = Conversion.fromStrtoNum(v1, c);
				Num n2 = v2;
				r = abstractNumberEquality(r, n1, n2);
			}
			if (!v2.isNotStr()) {
				if (v1.isMaybeFuzzyStr() || v2.isMaybeFuzzyStr())
					r = Value.makeAnyBool(dependency);
				else {
					String s1 = v1.getStr();
					String s2 = v2.getStr();
					if (s1 != null && s2 != null) {
						r = r.joinBool(s1.equals(s2));
					}
				}
			}
			if (v2.isMaybeObject()) {
				Value arg1 = v1.restrictToStr();
				Value arg2 = Conversion.toPrimitive(
						Value.makeObject(v2.getObjectLabels(), dependency),
						Conversion.Hint.NONE, c);
				r = r.join(abstractEqualityComparison(arg1, arg2, c));
			}
		}
		if (v1.isMaybeObject()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			Value vv1 = Value.makeObject(v1.getObjectLabels(), dependency);
			if (!v2.isNotBool()) {
				Num n1 = Conversion.toNumber(
						Conversion.toPrimitive(vv1, Conversion.Hint.NUM, c), c);
				Num n2 = Conversion.fromBooltoNum(v2);
				r = abstractNumberEquality(r, n1, n2);
			}
			if (!v2.isNotNum()) {
				Value arg1 = Conversion.toPrimitive(vv1, Conversion.Hint.NONE,
						c);
				Value arg2 = v2.restrictToNum();
				r = r.join(abstractEqualityComparison(arg1, arg2, c));
			}
			if (!v2.isNotStr()) {
				Value arg1 = Conversion.toPrimitive(vv1, Conversion.Hint.NONE,
						c);
				Value arg2 = v2.restrictToStr();
				r = r.join(abstractEqualityComparison(arg1, arg2, c));
			}
			if (v2.isMaybeObject()) {
				r = eqObject(r, v1.getObjectLabels(), v2.getObjectLabels());
			}
		}
		return r.joinDependency(dependency);
	}

	/**
	 * Part of 11.9.3 The Abstract Equality Comparison Algorithm and 11.9.6 The
	 * Strict Equality Comparison Algorithm.
	 */
	private static Value eqObject(Bool r, Collection<ObjectLabel> labels1,
			Collection<ObjectLabel> labels2) {
		Set<ObjectLabel> labelsInBoth = newSet();
		labelsInBoth.addAll(labels1);
		labelsInBoth.retainAll(labels2);
		if (labelsInBoth.isEmpty())
			return r.joinBool(false);
		else if (labels1.size() == 1 && labels2.size() == 1
				&& labelsInBoth.iterator().next().isSingleton())
			return r.joinBool(true);
		else
			return r.joinAnyBool();
	}

	/**
	 * Part of 11.9.3 The Abstract Equality Comparison Algorithm and 11.9.6 The
	 * Strict Equality Comparison Algorithm.
	 */
	private static Value abstractNumberEquality(Value r, Num n1, Num n2) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(r.getDependency());
		// ##################################################

		if (r.isMaybeAnyBool())
			return r;
		if (n1.isMaybeNaN() || n2.isMaybeNaN()) {
			r = r.joinBool(false);
			if (n1.isNaN() || n2.isNaN())
				return r;
		}
		if (n1.isMaybeSingleNum() && n2.isMaybeSingleNum()) {
			double d1 = n1.getNum();
			double d2 = n2.getNum();
			if (d1 == d2 || d1 == 0.0 && d2 == -0.0 || d1 == -0.0 && d2 == 0.0)
				// absolute weirdness, but required by standard 11.9.3 points 8
				// and 9
				return r.joinBool(true);
			else
				return r.joinBool(false);
		}
		return Value.makeAnyBool(dependency);
	}

	/**
	 * 11.9.4 <code>===</code>
	 */
	public static Value stricteq(Value v1, Value v2, Solver.SolverInterface c) {
		return strictEqualityComparison(v1, v2);
	}

	/**
	 * 11.9.5 <code>&#33;==</code>
	 */
	public static Value strictneq(Value v1, Value v2, Solver.SolverInterface c) {
		return not(strictEqualityComparison(v1, v2), c);
	}

	/**
	 * 11.9.6 The Strict Equality Comparison Algorithm.
	 */
	private static Value strictEqualityComparison(Value v1, Value v2) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(v1.getDependency());
		dependency.join(v2.getDependency());
		// ##################################################

		Value r = Value.makeBottom(dependency);
		if (v1.isMaybeUndef()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(true);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool())
				r = r.joinBool(false);
			if (!v2.isNotNum())
				r = r.joinBool(false);
			if (!v2.isNotStr())
				r = r.joinBool(false);
			if (v2.isMaybeObject())
				r = r.joinBool(false);
		}
		if (v1.isMaybeNull()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(true);
			if (!v2.isNotBool())
				r = r.joinBool(false);
			if (!v2.isNotNum())
				r = r.joinBool(false);
			if (!v2.isNotStr())
				r = r.joinBool(false);
			if (v2.isMaybeObject())
				r = r.joinBool(false);
		}
		if (!v1.isNotBool()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool()) {
				if (v1.isMaybeAnyBool() || v2.isMaybeAnyBool())
					return Value.makeAnyBool(dependency);
				else if (v1.isMaybeTrueButNotFalse()
						&& v2.isMaybeTrueButNotFalse())
					r = r.joinBool(true);
				else if (v1.isMaybeFalseButNotTrue()
						&& v2.isMaybeFalseButNotTrue())
					r = r.joinBool(true);
				else
					r = r.joinBool(false);
			}
			if (!v2.isNotNum())
				r = r.joinBool(false);
			if (!v2.isNotStr())
				r = r.joinBool(false);
			if (v2.isMaybeObject())
				r = r.joinBool(false);
		}
		if (!v1.isNotNum()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool())
				r = r.joinBool(false);
			if (!v2.isNotNum())
				r = abstractNumberEquality(r, v1, v2);
			if (!v2.isNotStr())
				r = r.joinBool(false);
			if (v2.isMaybeObject())
				r = r.joinBool(false);
		}
		if (!v1.isNotStr()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool())
				r = r.joinBool(false);
			if (!v2.isNotNum())
				r = r.joinBool(false);
			if (!v2.isNotStr()) {
				if (v1.isMaybeFuzzyStr() || v2.isMaybeFuzzyStr())
					return Value.makeAnyBool(dependency);
				else {
					String s1 = v1.getStr();
					String s2 = v2.getStr();
					if (s1 != null && s2 != null)
						r = r.joinBool(s1.equals(s2));
				}
			}
			if (v2.isMaybeObject())
				r = r.joinBool(false);
		}
		if (v1.isMaybeObject()) {
			if (v2.isMaybeUndef())
				r = r.joinBool(false);
			if (v2.isMaybeNull())
				r = r.joinBool(false);
			if (!v2.isNotBool())
				r = r.joinBool(false);
			if (!v2.isNotNum())
				r = r.joinBool(false);
			if (!v2.isNotStr())
				r = r.joinBool(false);
			if (v2.isMaybeObject())
				r = eqObject(r, v1.getObjectLabels(), v2.getObjectLabels());
		}
		return r.joinDependency(dependency);
	}

	/**
	 * 11.10 Binary Bitwise Operators
	 */
	private static Value bitwise(BitwiseOp op, Value arg1, Value arg2,
			Solver.SolverInterface c) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(arg1.getDependency());
		dependency.join(arg2.getDependency());
		// ##################################################

		// FIXME
		switch (op) {
		case AND:
			break;
		case OR:
			break;
		case XOR:
			break;
		}
		
		arg1 = Conversion.toNumber(arg1, c);
		arg2 = Conversion.toNumber(arg2, c);
		if (arg1.isNoValue() || arg2.isNoValue())
			return Value.makeBottom(dependency);
		else if (arg1.isMaybeSingleNum() && arg2.isMaybeSingleNum()) {
			int i1 = Conversion.toInt32(arg1.getNum());
			int i2 = Conversion.toInt32(arg2.getNum());
			int r = 0;
			switch (op) {
			case AND:
				r = i1 & i2;
				break;
			case OR:
				r = i1 | i2;
				break;
			case XOR:
				r = i1 ^ i2;
				break;
			}
			return Value.makeNum(r, dependency);
		} else
			return Value.makeAnyNumUInt(dependency);
	}

	/**
	 * 11.10<code>&amp;</code>
	 */
	public static Value and(Value arg1, Value arg2, Solver.SolverInterface c) {
		return bitwise(BitwiseOp.AND, arg1, arg2, c);
	}

	/**
	 * 11.10 <code>|</code>
	 */
	public static Value or(Value arg1, Value arg2, Solver.SolverInterface c) {
		return bitwise(BitwiseOp.OR, arg1, arg2, c);
	}

	/**
	 * 11.10 <code>^</code>
	 */
	public static Value xor(Value arg1, Value arg2, Solver.SolverInterface c) {
		return bitwise(BitwiseOp.XOR, arg1, arg2, c);
	}
}
