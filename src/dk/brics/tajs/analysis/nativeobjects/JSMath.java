package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.Label;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.lattice.Value;

/**
 * 15.8 native Math functions.
 */
public class JSMath {

	private JSMath() {
	}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
			return Value.makeBottom(new Dependency());

		switch (nativeobject) {

		case MATH_ABS: // 15.8.2.1
		case MATH_ASIN: // 15.8.2.3
		case MATH_ACOS: // 15.8.2.2
		case MATH_ATAN: // 15.8.2.4
		case MATH_CEIL: // 15.8.2.6
		case MATH_COS: // 15.8.2.7
		case MATH_EXP: // 15.8.2.8
		case MATH_FLOOR: // 15.8.2.9
		case MATH_LOG: // 15.8.2.10
		case MATH_ROUND: // 15.8.2.15
		case MATH_SIN: // 15.8.2.16
		case MATH_SQRT: // 15.8.2.17
		case MATH_TAN: { // 15.8.2.18
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value num = Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);

			// ##################################################
			dependency.join(num.getDependency());
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), num, state);
			// ==================================================

			if (num.isMaybeSingleNum()) {
				double d = num.getNum();
				double res;
				switch (nativeobject) {
				case MATH_ABS:
					res = Math.abs(d);
					break;
				case MATH_ASIN:
					res = Math.asin(d);
					break;
				case MATH_ACOS:
					res = Math.acos(d);
					break;
				case MATH_ATAN:
					res = Math.atan(d);
					break;
				case MATH_CEIL:
					res = Math.ceil(d);
					break;
				case MATH_COS:
					res = Math.cos(d);
					break;
				case MATH_EXP:
					res = Math.exp(d);
					break;
				case MATH_FLOOR:
					res = Math.floor(d);
					break;
				case MATH_LOG:
					res = Math.log(d);
					break;
				case MATH_ROUND:
					res = Math.round(d);
					break;
				case MATH_SIN:
					res = Math.sin(d);
					break;
				case MATH_SQRT:
					res = Math.sqrt(d);
					break;
				case MATH_TAN:
					res = Math.tan(d);
					break;
				default:
					throw new RuntimeException();
				}
				return Value.makeNum(res, dependency).joinDependencyGraphReference(node.getReference());
			} else if (!num.isNotNum())
				return Value.makeAnyNum(dependency).joinDependencyGraphReference(node.getReference());
			else
				return Value.makeBottom(dependency).joinDependencyGraphReference(node.getReference());
		}

		case MATH_ATAN2: // 15.8.2.5
		case MATH_POW: { // 15.8.2.13
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			Value num1 = Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Value num2 = Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);

			// ##################################################
			dependency.join(num1.getDependency());
			dependency.join(num2.getDependency());
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), num1, num2, state);
			// ==================================================

			if (num1.isMaybeSingleNum() && num2.isMaybeSingleNum()) {
				double d1 = num1.getNum();
				double d2 = num2.getNum();
				double res;
				switch (nativeobject) {
				case MATH_ATAN2:
					res = Math.atan2(d1, d2);
					break;
				case MATH_POW:
					res = Math.pow(d1, d2);
					break;
				default:
					throw new RuntimeException();
				}
				return Value.makeNum(res, dependency).joinDependencyGraphReference(node.getReference());
			} else if (!num1.isNotNum() && !num2.isNotNum())
				return Value.makeAnyNum(dependency).joinDependencyGraphReference(node.getReference());
			else
				return Value.makeBottom(dependency).joinDependencyGraphReference(node.getReference());
		}

		case MATH_MAX: { // 15.8.2.11
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			double res = Double.NEGATIVE_INFINITY;

			if (call.isUnknownNumberOfArgs()) {
				Value num = Conversion.toNumber(NativeFunctions.readUnknownParameter(call), c);

				// ##################################################
				dependency.join(num.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(num.getDependencyGraphReference());
				// ==================================================

				if (num.isMaybeSingleNum())
					res = num.getNum();
				else if (!num.isNotNum())
					return Value.makeAnyNum(dependency).joinDependencyGraphReference(node.getReference());
				else
					return Value.makeBottom(dependency).joinDependencyGraphReference(node.getReference());
			} else
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					Value num = Conversion.toNumber(NativeFunctions.readParameter(call, i), c);

					// ##################################################
					dependency.join(num.getDependency());
					// ##################################################

					// ==================================================
					node.addParent(num.getDependencyGraphReference());
					// ==================================================

					if (num.isMaybeSingleNum())
						res = Math.max(res, num.getNum());
					else if (!num.isNotNum())
						return Value.makeAnyNum(dependency).joinDependencyGraphReference(node.getReference());
					else
						return Value.makeBottom(dependency).joinDependencyGraphReference(node.getReference());
				}
			return Value.makeNum(res, dependency).joinDependencyGraphReference(node.getReference());
		}

		case MATH_MIN: { // 15.8.2.12
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			double res = Double.POSITIVE_INFINITY;

			if (call.isUnknownNumberOfArgs()) {
				Value num = Conversion.toNumber(NativeFunctions.readUnknownParameter(call), c);

				// ##################################################
				dependency.join(num.getDependency());
				// ##################################################

				// ==================================================
				node.addParent(num.getDependencyGraphReference());
				// ==================================================

				if (num.isMaybeSingleNum())
					res = num.getNum();
				else if (!num.isNotNum())
					return Value.makeAnyNum(dependency).joinDependencyGraphReference(node.getReference());
				else
					return Value.makeBottom(dependency).joinDependencyGraphReference(node.getReference());
			} else
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					Value num = Conversion.toNumber(NativeFunctions.readParameter(call, i), c);

					// ##################################################
					dependency.join(num.getDependency());
					// ##################################################

					// ==================================================
					node.addParent(num.getDependencyGraphReference());
					// ==================================================

					if (num.isMaybeSingleNum())
						res = Math.min(res, num.getNum());
					else if (!num.isNotNum())
						return Value.makeAnyNum(dependency).joinDependencyGraphReference(node.getReference());
					else
						return Value.makeBottom(dependency).joinDependencyGraphReference(node.getReference());
				}
			return Value.makeNum(res, dependency).joinDependencyGraphReference(node.getReference());
		}

		case MATH_RANDOM: { // 15.8.2.14
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			// ==================================================
			DependencyExpressionNode node = DependencyNode.link(Label.CALL, call.getSourceNode(), state);
			// ==================================================

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeAnyNumNotNaNInf(dependency).joinDependencyGraphReference(node.getReference());
		}

		default:
			return null;
		}
	}
}
