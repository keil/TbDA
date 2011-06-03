package dk.brics.tajs.analysis.nativeobjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.dom.event.*;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.DependencyObject;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;

/**
 * 15.1 and B.2 native global functions.
 */
public class JSGlobal {

	private JSGlobal() {
	}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call,
			State state, Solver.SolverInterface c) {
		if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
			return Value.makeBottom(new Dependency());

		switch (nativeobject) {

		// case EVAL: { // 15.1.2.1
		// NativeFunctions.expectParameters(n, c, 0, 1);
		// TODO: 'eval'
		// }

		case PARSEINT: { // 15.1.2.2
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			Value str = Conversion.toString(
					NativeFunctions.readParameter(call, 0), c);
			Value basis;
			if (call.isUnknownNumberOfArgs())
				basis = NativeFunctions.readParameter(call, 1).joinNum(0);
			else
				basis = call.getNumberOfArgs() >= 2 ? Conversion.toNumber(
						NativeFunctions.readParameter(call, 1), c) : Value
						.makeNum(0, new Dependency());

			// ##################################################
			dependency.join(str.getDependency());
			dependency.join(basis.getDependency());
			// ##################################################

			if (str.isMaybeSingleStr() && basis.isMaybeSingleNum()) {
				String s = str.getStr().trim();
				double sign = 1;
				if (s.length() > 0 && s.charAt(0) == '-')
					sign = -1;
				if (s.length() > 0
						&& (s.charAt(0) == '-' || s.charAt(0) == '+'))
					s = s.substring(1);
				int radix = Conversion.toInt32(basis.getNum());
				if (radix == 0) {
					radix = 10;
					if (s.length() > 1 && s.charAt(0) == '0') {
						radix = 8;
						if (s.length() > 2
								&& Character.toLowerCase(s.charAt(1)) == 'x') {
							radix = 16;
							s = s.substring(2);
						}
					}
				}
				if (radix < 2 || radix > 36)
					return Value.makeNum(Double.NaN, dependency);
				else {
					int i;
					String z = s;
					for (i = 0; i < s.length(); i++)
						if (Character.digit(s.charAt(i), radix) < 0) {
							z = s.substring(0, i);
							break;
						}
					if (z.equals(""))
						return Value.makeNum(Double.NaN, dependency);
					else
						return Value.makeNum(sign * Integer.parseInt(z, radix),
								dependency);
				}
			} else
				return Value.makeAnyNum(dependency);
		}

		case PARSEFLOAT: { // 15.1.2.3
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value str = Conversion.toString(
					NativeFunctions.readParameter(call, 0), c);

			// ##################################################
			dependency.join(str.getDependency());
			// ##################################################

			if (str.isMaybeSingleStr()) {
				String s = str.getStr().trim();
				Pattern p = Pattern
						.compile("[+-]?(Infinity|([0-9]+\\.[0-9]*|\\.[0-9]+|[0-9]+)([eE][+-]?[0-9]+)?)");
				Matcher m = p.matcher(s);
				if (m.lookingAt())
					return Value.makeNum(Double.parseDouble(m.group(0)),
							dependency);
				else
					return Value.makeNum(Double.NaN, dependency);
			} else
				return Value.makeAnyNum(dependency);
		}

		case ISNAN: { // 15.1.2.4
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value num = Conversion.toNumber(
					NativeFunctions.readParameter(call, 0), c);

			// ##################################################
			dependency.join(num.getDependency());
			// ##################################################

			Value res = Value.makeBottom(dependency);
			if (num.isMaybeNaN())
				res = res.joinBool(true);
			if (num.isMaybeSingleNum() || num.isMaybeInf()
					|| num.isMaybeNumUInt() || num.isMaybeNumNotUInt())
				res = res.joinBool(false);
			return res;
		}

		case ISFINITE: { // 15.1.2.5
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value num = Conversion.toNumber(
					NativeFunctions.readParameter(call, 0), c);

			// ##################################################
			dependency.join(num.getDependency());
			// ##################################################

			if (num.isMaybeSingleNum())
				return Value.makeBool(!num.getNum().isInfinite(), dependency);
			Value res = Value.makeBottom(dependency);
			if (num.isMaybeNaN() || num.isMaybeInf())
				res = res.joinBool(false);
			if (num.isMaybeNumUInt() || num.isMaybeNumNotUInt())
				res = res.joinBool(true);
			return res;
		}

		case PRINT: // in Rhino, expects any number of parameters; returns
					// undefined
		case ALERT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			return Value.makeUndef(dependency);
		}

		case DECODEURI: // 15.1.3.1
		case DECODEURICOMPONENT: // 15.1.3.2
		case ENCODEURI: // 15.1.3.3
		case ENCODEURICOMPONENT: // 15.1.3.4
		case ESCAPE: // B.2.1
		case UNESCAPE: { // B.2.2
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value str = Conversion.toNumber(
					NativeFunctions.readParameter(call, 0), c);

			// ##################################################
			dependency.join(str.getDependency());
			// ##################################################

			return Value.makeAnyStr(dependency); // TODO: could improve
													// precision for constant
													// strings
		}

			/*
			 * ############################################################
			 * Dependency function, to mark values with source location
			 * ############################################################
			 */
		case TRACE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value value = NativeFunctions.readParameter(call, 0);

			SourceLocation sourceLocation = call.getSourceNode()
					.getSourceLocation();
			DependencyObject dependencyObject = DependencyObject
					.getDependencyObject(sourceLocation);
			Dependency dependency = new Dependency(dependencyObject);

			// ==================================================
			DependencyObjectNode node = new DependencyObjectNode(
					dependencyObject, c.getFlowGraph().getDependencyGraph()
							.getRoot());
			value = value.setDependencyGraphReference(node.getReference());
			// ==================================================

			value = value.joinDependency(dependency);
			return value;
		}

		case ASSERT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = Conversion.toBoolean(NativeFunctions.readParameter(call,
					0));

			// ##################################################
			dependency.join(x.getDependency());
			// ##################################################

			c.addMessage(
					x.isMaybeFalseButNotTrue() ? Status.CERTAIN : x
							.isMaybeFalse() ? Status.MAYBE : Status.NONE,
					Severity.HIGH, "Assertion fails");
			return Value.makeUndef(dependency);
		}

		case DUMPVALUE: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, 0);

			// ##################################################
			dependency.join(x.getDependency());
			// ##################################################

			c.addMessage(Status.INFO, Severity.HIGH, "Abstract value: " + x /*
																			 * +
																			 * " (context: "
																			 * +
																			 * c
																			 * .
																			 * getCurrentContext
																			 * (
																			 * )
																			 * +
																			 * ")"
																			 */);
			return Value.makeUndef(dependency);
		}

		case DUMPPROTOTYPE: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, 0);

			// ##################################################
			dependency.join(x.getDependency());
			// ##################################################

			StringBuilder sb = new StringBuilder();
			Value p = state.readInternalPrototype(x.getObjectLabels());
			while (p.isMaybeObject()) {
				sb.append(p.toString());
				p = state.readInternalPrototype(p.getObjectLabels());
				if (!p.isNullOrUndef()) {
					sb.append(" -> ");
				}
			}

			c.addMessage(Status.INFO, Severity.HIGH, "Prototype: " + sb);
			return Value.makeUndef(dependency);
		}

		case DUMPOBJECT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, 0);

			// ##################################################
			dependency.join(x.getDependency());
			// ##################################################

			c.addMessage(Status.INFO, Severity.HIGH, "Abstract object: "
					+ state.printObject(x) /*
											 * + " (context: " +
											 * c.getCurrentContext() + ")"
											 */);
			return Value.makeUndef(dependency);
		}

		case DUMPSTATE: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			c.addMessage(Status.INFO, Severity.HIGH, "Abstract state:\n"
					+ state /* + " (context: " + c.getCurrentContext() + ")" */);
			/*
			 * try { FileWriter fw = new FileWriter("state.dot");
			 * fw.write(state.toDot(false)); fw.close(); } catch (IOException e)
			 * { throw new RuntimeException(e); }
			 */
			return Value.makeUndef(dependency);
		}

		case DUMPMODIFIEDSTATE: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			c.addMessage(Status.INFO, Severity.HIGH,
					"Abstract state (modified parts):" /*
														 * + " (context: " +
														 * c.getCurrentContext()
														 * + ")"
														 */
							+ state.toStringModified());
			return Value.makeUndef(dependency);
		}

		case DUMPATTRIBUTES: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			Value x = NativeFunctions.readParameter(call, 0);
			Value p = Conversion.toString(
					NativeFunctions.readParameter(call, 1), c);

			// ##################################################
			dependency.join(x.getDependency());
			dependency.join(p.getDependency());
			// ##################################################

			if (!p.isMaybeSingleStr())
				c.addMessage(Status.INFO, Severity.HIGH,
						"Calling dumpAttributes with non-constant property name");
			else {
				String propertyname = p.getStr();
				Value v = state.readPropertyDirect(x.getObjectLabels(),
						propertyname);
				c.addMessage(Status.INFO, Severity.HIGH,
						"Property attributes: " + v.printAttributes() /*
																	 * +
																	 * " (context: "
																	 * + c.
																	 * getCurrentContext
																	 * () + ")"
																	 */);
			}
			return Value.makeUndef(dependency);
		}

		case DUMPOBJECTORIGIN: { // TODO: remove dumpObjectOrigin? (use
									// dumpObject instead)
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, 0);

			// ##################################################
			dependency.join(x.getDependency());
			// ##################################################

			c.addMessage(Status.INFO, Severity.HIGH, "Origin of objects: "
					+ state.printObjectOrigins(x) /*
												 * + " (context: " +
												 * c.getCurrentContext() + ")"
												 */);
			return Value.makeUndef(dependency);
		}

		case CONVERSION_TO_PRIMITIVE: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			Value varg = NativeFunctions.readParameter(call, 0);

			// ##################################################
			dependency.join(varg.getDependency());
			// ##################################################

			Value vhint;
			if (call.isUnknownNumberOfArgs())
				vhint = NativeFunctions.readParameter(call, 1).joinStr("NONE");
			else
				vhint = call.getNumberOfArgs() >= 2 ? NativeFunctions
						.readParameter(call, 1) : Value.makeStr("NONE",
						dependency);
			if (!vhint.isMaybeSingleStr()) {
				c.addMessage(Status.INFO, Severity.HIGH,
						"Calling conversionToPrimitive with non-constant hint string");
				return Value.makeUndef(dependency);
			} else {
				String shint = vhint.getStr();
				return Conversion.toPrimitive(
						varg,
						shint.equals("NONE") ? Conversion.Hint.NONE : shint
								.equals("NUM") ? Conversion.Hint.NUM
								: Conversion.Hint.STR, c);
			}
		}

		case ASSUME_NON_NULLUNDEF: { // TODO: remove assumeNonNullUndef? (see
										// AssumeNode)
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			if (call.isUnknownNumberOfArgs()) {
				c.addMessage(Status.INFO, Severity.HIGH,
						"Calling assumeNonNullUndef with unknown number of arguments");
			} else {
				if (call.getNumberOfArgs() == 1) {
					Value varg = NativeFunctions.readParameter(call, 0);

					// ##################################################
					dependency.join(varg.getDependency());
					// ##################################################

					if (!varg.isMaybeSingleStr()) {
						c.addMessage(Status.INFO, Severity.HIGH,
								"Calling assumeNonNullUndef with non-constant variable string");
					} else {
						String varname = varg.getStr();
						Value v = state.readVariable(varname);

						// ##################################################
						dependency.join(v.getDependency());
						// ##################################################

						v = v.restrictToNotNullNotUndef().clearAbsent();
						if (v.isNoValue())
							return Value.makeBottom(dependency);
						state.writeVariable(varname, v);
					}
				} else if (call.getNumberOfArgs() == 2) {
					throw new RuntimeException(
							"2 arg variant of assumeNonNullUndef not yet implemented");// TODO:
																						// assumeNonNullUndef
				}
			}
			return Value.makeUndef(dependency);
		}

		case TAJS_GET_UI_EVENT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			return Value.makeObject(UIEvent.UI_EVENT, dependency);
		}

		case TAJS_GET_DOCUMENT_EVENT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			return Value.makeObject(DocumentEvent.DOCUMENT_EVENT, dependency);
		}

		case TAJS_GET_MOUSE_EVENT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			return Value.makeObject(MouseEvent.MOUSE_EVENT, dependency);
		}

		case TAJS_GET_KEYBOARD_EVENT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			return Value.makeObject(KeyboardEvent.KEYBOARD_EVENT, dependency);
		}

		case TAJS_GET_EVENT_LISTENER: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			return Value.makeObject(EventListener.EVENT_LISTENER, dependency);
		}

		case TAJS_GET_WHEEL_EVENT: {
			// ##################################################
			Dependency dependency = new Dependency();
			// ##################################################

			return Value.makeObject(WheelEvent.WHEEL_EVENT, dependency);
		}

		default:
			return null;
		}
	}
}
