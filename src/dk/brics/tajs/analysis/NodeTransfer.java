package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.DependencyObject;
import dk.brics.tajs.dependency.graph.DependencyGraph;
import dk.brics.tajs.dependency.graph.DependencyLabel;
import dk.brics.tajs.dependency.graph.Label;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphReference;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.flowgraph.nodes.AssumeNode;
import dk.brics.tajs.flowgraph.nodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.nodes.CallNode;
import dk.brics.tajs.flowgraph.nodes.CatchNode;
import dk.brics.tajs.flowgraph.nodes.ConstantNode;
import dk.brics.tajs.flowgraph.nodes.ContextDependencyPopNode;
import dk.brics.tajs.flowgraph.nodes.ContextDependencyPushNode;
import dk.brics.tajs.flowgraph.nodes.DeclareEventHandlerNode;
import dk.brics.tajs.flowgraph.nodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.nodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.nodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.nodes.EnterWithNode;
import dk.brics.tajs.flowgraph.nodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.nodes.EventEntryNode;
import dk.brics.tajs.flowgraph.nodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.nodes.GetPropertiesNode;
import dk.brics.tajs.flowgraph.nodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.nodes.IfNode;
import dk.brics.tajs.flowgraph.nodes.LeaveWithNode;
import dk.brics.tajs.flowgraph.nodes.NewObjectNode;
import dk.brics.tajs.flowgraph.nodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.nodes.NopNode;
import dk.brics.tajs.flowgraph.nodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.nodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.nodes.ReturnNode;
import dk.brics.tajs.flowgraph.nodes.ThrowNode;
import dk.brics.tajs.flowgraph.nodes.TypeofNode;
import dk.brics.tajs.flowgraph.nodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.nodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.nodes.WriteVariableNode;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.INodeTransfer;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.solver.NodeAndContext;

/**
 * Transfer for flow graph nodes.
 */
public class NodeTransfer implements INodeTransfer<State, CallContext> {

	private Solver.SolverInterface c;

	/**
	 * dependency graph
	 */
	private DependencyGraph mDependencyGraph;

	/**
	 * scope for handling context specific DependencyExpressionNode
	 */
	Map<Node, DependencyExpressionNode> mScope;

	/**
	 * Constructs a new TransferFunctions object.
	 */
	public NodeTransfer() {
		mScope = new HashMap<Node, DependencyExpressionNode>();
	}

	/**
	 * Initializes the connection to the solver.
	 */
	public void setSolverInterface(Solver.SolverInterface c) {
		this.c = c;
		this.mDependencyGraph = c.getDependencyGraph();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.solver.INodeTransfer#transfer(dk.brics.tajs.flowgraph.Node,
	 * dk.brics.tajs.solver.IBlockState)
	 */
	@Override
	public void transfer(Node n, State state) {
		n.visitBy(this, state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.solver.INodeTransfer#transferReturn(dk.brics.tajs.flowgraph
	 * .Node, dk.brics.tajs.flowgraph.Function,
	 * dk.brics.tajs.solver.ICallContext, dk.brics.tajs.solver.ICallContext)
	 */
	@Override
	public void transferReturn(Node call_node, Function callee, CallContext caller_context, CallContext callee_context) {
		BasicBlock ordinary_exit = callee.getOrdinaryExit();
		BasicBlock exceptional_exit = callee.getExceptionalExit();
		State ordinary_exit_state = c.getAnalysisLatticeElement().getState(ordinary_exit, callee_context);
		State exceptional_exit_state = c.getAnalysisLatticeElement().getState(exceptional_exit, callee_context);
		NodeAndContext<CallContext> caller = new NodeAndContext<CallContext>(call_node, caller_context);
		if (ordinary_exit_state != null)
			transferReturn((ReturnNode) ordinary_exit.getFirstNode(), ordinary_exit_state.clone(), caller);
		if (exceptional_exit_state != null)
			transferExceptionalReturn((ExceptionalReturnNode) exceptional_exit.getFirstNode(), exceptional_exit_state.clone(), caller);
	}

	/**
	 * returns an DependencyExpressionNode
	 * 
	 * @param label
	 *            expression label
	 * @param n
	 *            evaluated node
	 * @return corresponding DependencyExpressionNode
	 */
	private DependencyExpressionNode link(Label label, Node node) {
		if (!mScope.containsKey(node)) {
			DependencyExpressionNode dependencyExpressionNode = new DependencyExpressionNode(new DependencyLabel(label, node));
			mScope.put(node, dependencyExpressionNode);
		}
		return mScope.get(node);
	}

	/**
	 * returns an DependencyExpressionNode
	 * 
	 * @param label
	 *            expression label
	 * @param n
	 *            evaluated node
	 * @param references
	 *            dependency references
	 * @return corresponding DependencyExpressionNode
	 */
	private DependencyExpressionNode link(Label label, Node n, IDependencyGraphReference<?>... references) {
		DependencyExpressionNode node = link(label, n);
		for (IDependencyGraphReference<?> reference : references) {
			node.addParent(reference.getDependencyGraphReference());
		}
		return node;
	}

	/**
	 * 12.3 empty statement.
	 */
	@Override
	public void visit(NopNode n, State state) {
		// do nothing
	}

	/**
	 * 12.2 variable declaration.
	 */
	@Override
	public void visit(DeclareVariableNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		state.declareAndWriteVariable(n.getVarName(), Value.makeUndef(dependency));
	}

	/**
	 * 11.13 and 7.8 assignment with literal. Example: node 4: constant[7.0,v6]
	 */
	@Override
	public void visit(ConstantNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v;

		switch (n.getType()) {
		case NULL:
			v = Value.makeNull(dependency);
			break;
		case UNDEFINED:
			v = Value.makeUndef(dependency);
			break;
		case BOOLEAN:
			v = Value.makeBool(n.getBoolean(), dependency);
			break;
		case NUMBER:
			v = Value.makeNum(n.getNumber(), dependency);
			break;
		case STRING:
			v = Value.makeStr(n.getString(), dependency);
			break;
		default:
			throw new RuntimeException();
		}

		// ##################################################
		if (Options.isTraceAll()) {
			DependencyObject dependencyObject = DependencyObject.getDependencyObject(n.getSourceLocation());
			dependency.join(dependencyObject);

			// ==================================================
			DependencyObjectNode node = new DependencyObjectNode(dependencyObject, mDependencyGraph.getRoot());
			v = v.setDependencyGraphReference(node.getReference());
			// ==================================================
		}
		// ##################################################

		state.writeTemporary(n.getResultVar(), v.joinDependency(dependency));
	}

	/**
	 * 11.1.5 object initializer.
	 */
	@Override
	public void visit(NewObjectNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT);
		Value prototype = Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, dependency);
		Value v = Value.makeObject(objlabel, dependency);

		// ##################################################
		dependency.join(prototype.getDependency());
		dependency.join(v.getDependency());
		// ##################################################

		// ##################################################
		if (Options.isTraceAll()) {
			DependencyObject dependencyObject = DependencyObject.getDependencyObject(n.getSourceLocation());
			dependency.join(dependencyObject);

			// ==================================================
			DependencyObjectNode node = new DependencyObjectNode(dependencyObject, mDependencyGraph.getRoot());
			prototype = prototype.setDependencyGraphReference(node.getReference());
			v = v.setDependencyGraphReference(node.getReference());
			// ==================================================
		}
		// ##################################################

		state.newObject(objlabel);
		state.writeInternalPrototype(objlabel, prototype.joinDependency(dependency));
		state.writeTemporary(n.getResultVar(), v.joinDependency(dependency));
	}

	/**
	 * 11.13 and 11.4 assignment with unary operator.
	 */
	@Override
	public void visit(UnaryOperatorNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value arg = state.readTemporary(n.getArgVar());

		// ##################################################
		dependency.join(arg.getDependency());
		// ##################################################

		Value v;
		Label label = Label.OPERATION;
		switch (n.getOperator()) {
		case COMPLEMENT:
			v = Operators.complement(arg, c);
			label = Label.COMPLEMENT;
			break;
		case MINUS:
			v = Operators.uminus(arg, c);
			label = Label.MINUS;
			break;
		case NOT:
			v = Operators.not(arg, c);
			label = Label.NOT;
			break;
		case PLUS:
			v = Operators.uplus(arg, c);
			label = Label.PLUS;
			break;
		default:
			throw new RuntimeException();
		}
		if (v.isNoValue() && !Options.isPropagateDeadFlow()) {
			state.setToBottom();
			return;
		}

		// ==================================================
		v = v.setDependencyGraphReference(link(label, n, arg, state).getReference());
		// ==================================================

		state.writeTemporary(n.getResultVar(), v.joinDependency(dependency));
	}

	/**
	 * 11.13 and 11.5/6/7/8 assignment with binary operator.
	 */
	@Override
	public void visit(BinaryOperatorNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value arg1 = state.readTemporary(n.getArg1Var());
		Value arg2 = state.readTemporary(n.getArg2Var());

		// ##################################################
		dependency.join(arg1.getDependency());
		dependency.join(arg2.getDependency());
		// ##################################################

		Value v;
		Label label = Label.OPERATION;
		switch (n.getOperator()) {
		case ADD:
			v = Operators.add(arg1, arg2, c); // FIXME: test07.js could improve
												// messages if keeping
												// conversions of the two args
												// separate
			label = Label.ADD;
			break;
		case AND:
			v = Operators.and(arg1, arg2, c);

			label = Label.AND;
			break;
		case DIV:
			v = Operators.div(arg1, arg2, c);
			label = Label.DIV;
			break;
		case EQ:
			v = Operators.eq(arg1, arg2, c);
			label = Label.EQ;
			break;
		case GE:
			v = Operators.ge(arg1, arg2, c);
			label = Label.GE;
			break;
		case GT:
			v = Operators.gt(arg1, arg2, c);
			label = Label.GT;
			break;
		case IN:
			v = Operators.in(state, arg1, arg2, c);
			label = Label.IN;
			break;
		case INSTANCEOF:
			v = Operators.instof(state, arg1, arg2, c);
			label = Label.INSTANCEOF;
			break;
		case LE:
			v = Operators.le(arg1, arg2, c);
			label = Label.LE;
			break;
		case LT:
			v = Operators.lt(arg1, arg2, c);
			label = Label.LT;
			break;
		case MUL:
			v = Operators.mul(arg1, arg2, c);
			label = Label.MUL;
			break;
		case NE:
			v = Operators.neq(arg1, arg2, c);
			label = Label.NE;
			break;
		case OR:
			v = Operators.or(arg1, arg2, c);
			label = Label.OR;
			break;
		case REM:
			v = Operators.rem(arg1, arg2, c);
			label = Label.REM;
			break;
		case SEQ:
			v = Operators.stricteq(arg1, arg2, c);
			label = Label.SEQ;
			break;
		case SHL:
			v = Operators.shl(arg1, arg2, c);
			label = Label.SHL;
			break;
		case SHR:
			v = Operators.shr(arg1, arg2, c);
			label = Label.SHR;
			break;
		case SNE:
			v = Operators.strictneq(arg1, arg2, c);
			label = Label.SNE;
			break;
		case SUB:
			v = Operators.sub(arg1, arg2, c);
			label = Label.SUB;
			break;
		case USHR:
			v = Operators.ushr(arg1, arg2, c);
			label = Label.USHR;
			break;
		case XOR:
			v = Operators.xor(arg1, arg2, c);
			label = Label.XOR;
			break;
		default:
			throw new RuntimeException();
		}
		if (v.isNoValue() && !Options.isPropagateDeadFlow()) {
			state.setToBottom();
			return;
		}

		// ==================================================
		v = v.setDependencyGraphReference(link(label, n, arg1, arg2, state).getReference());
		// ==================================================

		state.writeTemporary(n.getResultVar(), v.joinDependency(dependency));
	}

	/**
	 * 11.13 and 11.1.2 assignment with right-hand-side identifier reference.
	 */
	@Override
	public void visit(ReadVariableNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		String varname = n.getVarName();
		Value v;
		Value baseval = null;
		if (varname.equals("this")) {
			v = state.readThis();

			// ##################################################
			dependency.join(v.getDependency());
			// ##################################################

			Status s;
			if (v.getObjectLabels().contains(InitialStateBuilder.GLOBAL)) {
				if (v.getObjectLabels().size() == 1 && !v.isMaybePrimitive())
					s = Status.CERTAIN;
				else
					s = Status.MAYBE;
			} else
				s = Status.NONE;
			c.addMessage(s, Severity.MEDIUM, "Reading 'this' yields the global object");
			if (n.getResultBaseVar() != Node.NO_VALUE)
				throw new RuntimeException("ReadVariableNode for 'this' should not have a result base var");
		} else {
			v = state.readVariable(varname);

			// ##################################################
			dependency.join(v.getDependency());
			// ##################################################

			if (Options.isCollectVariableInfoEnabled()) {
				c.record(varname, n.getSourceLocation(), v);
			}

			Status s;
			if (v.isNoValue())
				s = Status.CERTAIN;
			else if (v.isMaybeAbsent())
				s = Status.MAYBE;
			else
				s = Status.NONE;
			c.addMessage(s, Severity.HIGH, "ReferenceError, reading absent variable: " + varname);
			if (s != Status.NONE) {
				Exceptions.throwReferenceError(state, c);
				if (c.isScanning())
					c.getStatistics().registerAbsentVariableRead(n);
				if (s == Status.CERTAIN && !Options.isPropagateDeadFlow()) {
					state.setToBottom();
					return;
				}
			}
			int result_base_var = n.getResultBaseVar();
			if (result_base_var != Node.NO_VALUE) {
				baseval = state.readVariableBase(varname);

				state.writeTemporary(result_base_var, baseval);

				// ##################################################
				dependency.join(baseval.getDependency());
				// ##################################################
			}
		}
		if (!varname.equals("undefined")) {
			Status s;
			if (v.isNullOrUndef())
				s = Status.CERTAIN;
			else if (v.isMaybeNull() || v.isMaybeUndef())
				s = Status.MAYBE;
			else
				s = Status.NONE;
			c.addMessage(s, Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "Variable is null/undefined: " + varname);
		}

		// ==================================================
		if (baseval != null)
			v = v.setDependencyGraphReference(link(Label.READ, n, v, baseval, state).getReference());
		else
			v = v.setDependencyGraphReference(link(Label.READ, n, v, state).getReference());
		// ==================================================

		state.writeTemporary(n.getResultVar(), v.restrictToNonAbsent().joinDependency(dependency));
	}

	/**
	 * 11.13 and 11.1.2 assignment with left-hand-side identifier reference.
	 */
	@Override
	public void visit(WriteVariableNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v = state.readTemporary(n.getValueVar());
		// ##################################################
		dependency.join(v.getDependency());
		// ##################################################

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.WRITE, n, v, state).getReference());
		// ==================================================

		state.writeVariable(n.getVarName(), v.joinDependency(dependency));
	}

	/**
	 * 11.13 and 11.2.1 assignment with right-hand-side property accessor.
	 */
	@Override
	public void visit(ReadPropertyNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value baseval = state.readTemporary(n.getBaseVar());

		Status s;
		if (baseval.isNullOrUndef())
			s = Status.CERTAIN;
		else if (baseval.isMaybeNull() || baseval.isMaybeUndef())
			s = Status.MAYBE;
		else
			s = Status.NONE;
		c.addMessage(s, Severity.HIGH, "TypeError, reading property of null/undefined");
		if (s != Status.NONE) {
			Exceptions.throwTypeError(state, c);
			if (c.isScanning())
				c.getStatistics().registerNullUndefPropertyAccess(n);
			if (s == Status.CERTAIN && !Options.isPropagateDeadFlow()) {
				state.setToBottom();
				return;
			}
		}
		baseval = baseval.restrictToNotNullNotUndef();

		// needed ??
		// state.writeTemporary(n.getBaseVar(),
		// baseval.joinDependency(dependency)); // if
		// null/undefined,
		// an
		// exception would have
		// been thrown
		Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
		Value propertyval;
		if (n.isPropertyFixed())
			propertyval = Value.makeStr(n.getPropertyStr(), baseval.getDependency());
		else
			propertyval = state.readTemporary(n.getPropertyVar());
		boolean maybe_undef = propertyval.isMaybeUndef();
		boolean maybe_null = propertyval.isMaybeNull();
		boolean maybe_nan = propertyval.isMaybeNaN();
		propertyval = propertyval.restrictToNonObject(); // FIXME: unsound,
															// assumes that
															// object->string
															// conversion isn't
															// relevant (see
															// preceding
															// statement)
															// (Peter)
		propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
		Str propertystr = Conversion.toString(propertyval, c);
		Value v;
		boolean read_all = false;
		if (propertystr.isMaybeSingleStr()) {
			String propertyname = propertystr.getStr();
			checkPropertyPresent(n, objlabels, propertyname, n.isPropertyFixed(), false, false, maybe_undef || maybe_null || maybe_nan, state);
			v = state.readProperty(objlabels, propertyname);
		} else if (propertystr.isMaybeStrUInt() || propertystr.isMaybeStrNotUInt()) {
			if (!propertystr.isMaybeStrNotUInt()) {
				checkPropertyPresent(n, objlabels, null, false, true, false, true, state);
				v = state.readUnknownArrayIndex(objlabels);
			} else {
				checkPropertyPresent(n, objlabels, null, false, false, true, true, state);
				v = state.readUnknownProperty(objlabels);
				read_all = true;
			}
		} else
			v = Value.makeBottom(new Dependency());
		if (!read_all) {
			if (maybe_undef) {
				checkPropertyPresent(n, objlabels, "undefined", false, false, false, true, state);
				v = v.join(state.readProperty(objlabels, "undefined"));
			}
			if (maybe_null) {
				checkPropertyPresent(n, objlabels, "null", false, false, false, true, state);
				v = v.join(state.readProperty(objlabels, "null"));
			}
			if (maybe_nan) {
				checkPropertyPresent(n, objlabels, "NaN", false, false, false, true, state);
				v = v.join(state.readProperty(objlabels, "NaN"));
			}
		}

		if (Options.isCollectVariableInfoEnabled()) {
			c.record(n.getPropertyStr(), n.getSourceLocation(), v);
		}

		/*
		 * ############################################################
		 * Dependency: set parent dependency to child attribute d(objects) is
		 * subset from d(objects.attribute)
		 * ############################################################
		 */

		// ##################################################
		dependency.join(propertyval.getDependency());
		dependency.join(baseval.getDependency());
		// ##################################################

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.ATTRIBUTE, n, v, propertyval, baseval, state).getReference());
		// ==================================================

		state.writeTemporary(n.getResultVar(), v.joinDependency(dependency));
	}

	private void checkPropertyPresent(Node n, Set<ObjectLabel> objlabels, String propertyname, boolean fixed_property, boolean array_index, boolean any,
			boolean maybe, State state) {
		Value v;
		if (propertyname != null)
			v = state.readPropertyPreserveAttributes(objlabels, propertyname);
		else if (array_index)
			v = state.readUnknownArrayIndexPreserveAttributes(objlabels);
		else
			v = state.readUnknownPropertyPreserveAttributes(objlabels);
		Status s;
		if (!maybe && v.isNoValue())
			s = Status.CERTAIN;
		else if (v.isMaybeAbsent())
			s = Status.MAYBE;
		else
			s = Status.NONE;
		if (fixed_property)
			c.addMessage(s, Severity.MEDIUM, "Reading absent property: " + propertyname);
		else
			c.addMessage(s, Severity.LOW, "Reading absent property (computed name)");
		if (s != Status.NONE && fixed_property && c.isScanning())
			c.getStatistics().registerAbsentFixedPropertyRead(n);
		Status s2;
		if (v.isNullOrUndef() && s != Status.CERTAIN)
			s2 = Status.CERTAIN;
		else if (v.isMaybeNull() || v.isMaybeUndef())
			s2 = Status.MAYBE;
		else
			s2 = Status.NONE;
		c.addMessage(s2, Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "Property is null/undefined");
	}

	/**
	 * 11.13 and 11.2.1 assignment with left-hand-side property accessor.
	 */
	@Override
	public void visit(WritePropertyNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value baseval = state.readTemporary(n.getBaseVar());
		Value v = state.readTemporary(n.getValueVar());

		Status s;
		if (baseval.isNullOrUndef())
			s = Status.CERTAIN;
		else if (baseval.isMaybeNull() || baseval.isMaybeUndef())
			s = Status.MAYBE;
		else
			s = Status.NONE;
		c.addMessage(s, Severity.HIGH, "TypeError, writing property of null/undefined");
		if (s != Status.NONE) {
			Exceptions.throwTypeError(state, c);
			if (c.isScanning())
				c.getStatistics().registerNullUndefPropertyAccess(n);
			if (s == Status.CERTAIN && !Options.isPropagateDeadFlow()) {
				state.setToBottom();
				return;
			}
		}

		Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);

		Value propertyval;
		if (n.isPropertyFixed())
			propertyval = Value.makeStr(n.getPropertyStr(), dependency);
		else
			propertyval = state.readTemporary(n.getPropertyVar());
		Str propertystr = Conversion.toString(propertyval, c);

		// ##################################################
		dependency.join(propertyval.getDependency());
		// ##################################################

		if (propertystr.isMaybeStrNotUInt())
			state.writeUnknownProperty(objlabels, v.joinDependency(dependency));
		else if (propertystr.isMaybeStrUInt())
			state.writeUnknownArrayProperty(objlabels, v.joinDependency(dependency));
		else if (propertystr.isMaybeSingleStr())
			state.writeProperty(objlabels, propertystr.getStr(), v.joinDependency(dependency));

		// ##################################################
		dependency.join(v.getDependency());
		// ##################################################

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.ATTRIBUTE, n, v, propertyval, state).getReference());
		// ==================================================

		NativeFunctions.updateArrayLength(n, state, objlabels, propertyval, v.joinDependency(dependency), c);
	}

	/**
	 * 11.13 and 11.4.1 assignment with 'delete' operator.
	 */
	@Override
	public void visit(DeletePropertyNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v;
		if (n.isVariable()) {
			v = state.deleteVariable(n.getVarName());

			// ##################################################
			dependency.join(v.getDependency());
			// ##################################################

			// ==================================================
			v = v.setDependencyGraphReference(link(Label.ATTRIBUTE, n, v, state).getReference());
			// ==================================================
		} else {
			Value baseval = state.readTemporary(n.getBaseVar());
			Value propertyval = state.readTemporary(n.getPropertyVar());

			// ##################################################
			dependency.join(baseval.getDependency());
			dependency.join(propertyval.getDependency());
			// ##################################################

			Status s;
			if (baseval.isNullOrUndef())
				s = Status.CERTAIN;
			else if (baseval.isMaybeNull() || baseval.isMaybeUndef())
				s = Status.MAYBE;
			else
				s = Status.NONE;
			c.addMessage(s, Severity.HIGH, "TypeError, deleting property of null/undefined");
			if (s != Status.NONE) {
				Exceptions.throwTypeError(state, c);
				if (c.isScanning())
					c.getStatistics().registerNullUndefPropertyAccess(n);
				if (s == Status.CERTAIN && !Options.isPropagateDeadFlow()) {
					state.setToBottom();
					return;
				}
			}
			baseval = baseval.restrictToNotNullNotUndef();
			state.writeTemporary(n.getBaseVar(), baseval);
			Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
			Str propertystr = Conversion.toString(propertyval, c);
			if (propertystr.isMaybeSingleStr())
				v = state.deleteProperty(objlabels, propertystr.getStr());
			else {
				state.deleteUnknownProperty(objlabels);
				v = Value.makeAnyBool(dependency);
			}

			// ##################################################
			dependency.join(v.getDependency());
			// ##################################################

			// ==================================================
			v = v.setDependencyGraphReference(link(Label.ATTRIBUTE, n, v, propertyval, baseval, state).getReference());
			// ==================================================
		}

		state.writeTemporary(n.getResultVar(), v.joinDependency(dependency));
	}

	/**
	 * 11.13 and 11.4.3 assignment with 'typeof' operator.
	 */
	@Override
	public void visit(TypeofNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v;
		if (n.isVariable()) {
			Value val = state.readVariable(n.getVarName());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################

			v = Operators.typeof(val, val.isMaybeAbsent());

			if (Options.isCollectVariableInfoEnabled()) {
				c.record(n.getVarName(), n.getSourceLocation(), v);
			}
		} else {
			Value val = state.readTemporary(n.getArgVar());

			// ##################################################
			dependency.join(val.getDependency());
			// ##################################################

			v = Operators.typeof(val, false);
		}

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.TYPEOF, n, v, state).getReference());
		// ==================================================

		state.writeTemporary(n.getResultVar(), v.joinDependency(dependency));
	}

	/**
	 * 12.5 and 12.6 'if'/iteration statement.
	 */
	@Override
	public void visit(IfNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v = state.readTemporary(n.getConditionVar());

		// ##################################################
		dependency.join(v.getDependency());
		// ##################################################

		/*
		 * ############################################################
		 * Dependency: write expression dependency to abstract state
		 * ############################################################
		 */

		// ==================================================
		state.setDependencyGraphReference(link(Label.IF, n, v, state).getReference());
		// ==================================================

		// ##################################################
		state.joinDependency(dependency);
		// ##################################################

		// do nothing (but see EdgeTransfer)
	}

	/**
	 * 13 function definition.
	 */
	@Override
	public void visit(DeclareFunctionNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		// TODO: join function objects (p.72)? (if same n and same scope)
		ObjectLabel fn = new ObjectLabel(n.getFunction());
		// 13.2 step 2 and 3
		state.newObject(fn);
		Value f = Value.makeObject(fn, dependency);

		// ##################################################
		if (Options.isTraceAll()) {
			DependencyObject dependencyObject = DependencyObject.getDependencyObject(n.getSourceLocation());
			dependency.join(dependencyObject);
			f = f.joinDependency(dependency);

			// ==================================================
			DependencyObjectNode node = new DependencyObjectNode(dependencyObject, mDependencyGraph.getRoot());
			f = f.setDependencyGraphReference(node.getReference());
			// ==================================================
		}
		// ##################################################

		// 13.2 step 4
		state.writeInternalPrototype(fn, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE, dependency));
		// 13.2 step 7
		Set<ScopeChain> scope = state.getScopeChain();
		if (n.isExpression() && n.getFunction().getName() != null) {
			// p.71 (function expression with identifier)
			ObjectLabel front = new ObjectLabel(n, Kind.OBJECT);
			state.newObject(front);
			scope = ScopeChain.make(front, scope);
			state.writeSpecialProperty(front, n.getFunction().getName(), f.setAttributes(false, true, true));
		} else if (!n.isExpression() && n.getFunction().getName() != null) {
			// p.71 (function declaration)
			state.declareAndWriteVariable(n.getFunction().getName(), f);
		}
		state.writeObjectScope(fn, scope);
		// 13.2 step 8
		state.writeSpecialProperty(fn, "length", Value.makeNum(n.getFunction().getParameterNames().size(), dependency).setAttributes(true, true, true));
		// 13.2 step 9
		ObjectLabel prototype = new ObjectLabel(n, Kind.OBJECT);
		state.newObject(prototype);
		state.writeInternalPrototype(prototype, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, dependency));
		// 13.2 step 10
		state.writeSpecialProperty(prototype, "constructor", Value.makeObject(fn, dependency).setAttributes(true, false, false));
		// 13.2 step 11
		state.writeSpecialProperty(fn, "prototype", Value.makeObject(prototype, dependency).setAttributes(false, true, false));
		state.writeInternalValue(prototype, Value.makeNum(Double.NaN, dependency)); // TODO:
																							// as
																							// in
																							// Rhino
																							// (?)
		
		// ==================================================
		f = f.setDependencyGraphReference(link(Label.FUNCTION, n, f, state).getReference());
		// ==================================================

		state.writeTemporary(n.getResultVar(), f.joinDependency(dependency));
	}

	/**
	 * 11.2.2, 11.2.3, 13.2.1, and 13.2.2 'new' / function call.
	 */
	@Override
	public void visit(final CallNode n, final State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		if (n.getBlock().getNodes().size() != 1)
			throw new RuntimeException("CallNode should have its own basic block");

		Value function = state.readTemporary(n.getFunctionVar());

		// ##################################################
		dependency.join(function.getDependency());
		// ##################################################

		/*
		 * ############################################################
		 * Dependency: write expression dependency to abstract state
		 * ############################################################
		 */

		// ==================================================
		state.setDependencyGraphReference(link(Label.CALL, n, function, state).getReference());
		// ==================================================

		// ##################################################
		state.joinDependency(dependency);
		// ##################################################

		FunctionCalls.callFunction(new FunctionCalls.CallInfo<CallNode>() {

			@Override
			public CallNode getSourceNode() {
				return n;
			}

			@Override
			public boolean isConstructorCall() {
				return n.isConstructorCall();
			}

			@Override
			public Value getFunction() {
				return state.readTemporary(n.getFunctionVar());
			}

			@Override
			public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
				return FunctionCalls.determineThis(n, caller_state, callee_state, c);
			}

			@Override
			public Value getArg(int i) {
				if (i < n.getNumberOfArgs()) {
					return state.readTemporary(n.getArgVar(i));
				} else
					return Value.makeUndef(new Dependency());
			}

			@Override
			public int getNumberOfArgs() {
				return n.getNumberOfArgs();
			}

			@Override
			public Value getUnknownArg() {
				throw new RuntimeException("Calling getUnknownArg but number of arguments is not unknown");
			}

			@Override
			public boolean isUnknownNumberOfArgs() {
				return false;
			}

			@Override
			public int getResultVar() {
				return n.getResultVar();
			}

			@Override
			public int getBaseVar() {
				return n.getBaseVar();
			}
		}, state, c);
	}

	/**
	 * 12.9 and 13.2.1 'return' statement.
	 */
	@Override
	public void visit(ReturnNode n, State state) {
		transferReturn(n, state, null);
	}

	/**
	 * Transfer for a return statement.
	 * 
	 * @param caller
	 *            if non-null, only consider this caller
	 */
	public void transferReturn(ReturnNode n, State state, NodeAndContext<CallContext> caller) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v;
		if (n.getValueVar() != Node.NO_VALUE)
			v = state.readTemporary(n.getValueVar());
		else
			v = Value.makeUndef(dependency);

		// ##################################################
		// dependency.join(v.getDependency());
		// ##################################################

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.RETURN, n, v, state).getReference());
		// ==================================================

		FunctionCalls.leaveUserFunction(v.joinDependency(dependency), false, n.getBlock().getFunction(), state, c, caller);
	}

	/**
	 * 13.2.1 exceptional return.
	 */
	@Override
	public void visit(ExceptionalReturnNode n, State state) {
		transferExceptionalReturn(n, state, null);
	}

	/**
	 * Transfer for an exceptional-return statement.
	 * 
	 * @param caller
	 *            if non-null, only consider this caller
	 */
	public void transferExceptionalReturn(ExceptionalReturnNode n, State state, NodeAndContext<CallContext> caller) {

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v = state.readTemporary(Node.EXCEPTION_VAR);

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.EXCEPTION, n, v, state).getReference());
		// ==================================================

		state.removeTemporary(Node.EXCEPTION_VAR);
		FunctionCalls.leaveUserFunction(v.joinDependency(dependency), true, n.getBlock().getFunction(), state, c, caller);
	}

	/**
	 * 12.13 'throw' statement.
	 */
	@Override
	public void visit(ThrowNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v = state.readTemporary(n.getValueVar());

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.THROW, n, v, state).getReference());
		// ==================================================

		Exceptions.throwException(state, v.joinDependency(dependency), c);
		Exceptions.throwException(state, v.joinDependency(dependency), c);
		state.setToBottom();
	}

	/**
	 * 12.14 'catch' block.
	 */
	@Override
	public void visit(CatchNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v = state.readTemporary(Node.EXCEPTION_VAR);

		// ##################################################
		dependency.join(v.getDependency());
		// ##################################################

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.CATCH, n, v, state).getReference());
		// ==================================================

		state.removeTemporary(Node.EXCEPTION_VAR);
		if (n.getTempVar() != Node.NO_VALUE) {
			state.writeTemporary(n.getTempVar(), v.joinDependency(dependency));
		} else {
			ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT);
			state.newObject(objlabel);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, v.getDependency()));
			state.writeSpecialProperty(objlabel, n.getVarName(), v.setAttributes(false, true, false).joinDependency(dependency));
			state.writeTemporary(n.getScopeObjVar(), Value.makeObject(objlabel, dependency));
		}
	}

	/**
	 * 12.10 enter 'with' statement.
	 */
	@Override
	public void visit(EnterWithNode n, State state) { // FIXME: test
														// EnterWithNode
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v = state.readTemporary(n.getObjectVar());

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.WITH, n, v, state).getReference());
		// ==================================================

		Set<ObjectLabel> objs = Conversion.toObjectLabels(state, n, v.joinDependency(dependency), c);
		Set<ExecutionContext> new_execution_context = newSet();
		for (ObjectLabel obj : objs)
			for (ExecutionContext ec : state.getExecutionContext())
				new_execution_context.add(new ExecutionContext(ScopeChain.make(obj, ec.getScopeChain()), ec.getVariableObject(), ec.getThisObject()));
		state.setExecutionContext(new_execution_context);
	}

	/**
	 * 12.10 leave 'with' statement.
	 */
	@Override
	public void visit(LeaveWithNode n, State state) { // FIXME: test
														// LeaveWithNode and
														// exceptions from with
														// blocks
		Set<ExecutionContext> new_execution_context = newSet();
		for (ExecutionContext ec : state.getExecutionContext())
			new_execution_context.add(new ExecutionContext(ec.getScopeChain().next(), ec.getVariableObject(), ec.getThisObject()));
		state.setExecutionContext(new_execution_context);
	}

	/**
	 * 12.6.4 'for-in' statement.
	 */
	@Override
	public void visit(GetPropertiesNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value v = state.readTemporary(n.getObjectVar());

		// ##################################################
		dependency.join(v.getDependency());
		// ##################################################

		/* Set<ObjectLabel> objs = */Conversion.toObjectLabels(state, n, v.joinDependency(dependency), c);

		// ==================================================
		v = v.setDependencyGraphReference(link(Label.IN, n, v, state).getReference());
		// ==================================================

		state.writeTemporary(n.getPropertyQueueVar(), v.joinDependency(dependency));

		// TODO: improve transfer for GetPropertiesNode? - need path sensitivity
		// or a stronger Str lattice?
		// currently just using AnyString for the property names
		// store some kind of property name iterator in n.getPropertyQueueVar()
		// note that deleted properties not yet visited should not be visited
	}

	/**
	 * 12.6.4 'for-in' statement.
	 */
	@Override
	public void visit(NextPropertyNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value queue = state.readTemporary(n.getPropertyQueueVar());

		// ==================================================
		queue = queue.setDependencyGraphReference(link(Label.IN, n, queue, state).getReference());
		// ==================================================

		// ##################################################
		dependency.join(queue.getDependency());
		// ##################################################

		// TODO: improve transfer for NextPropertyNode?
		// currently just using AnyString for the property names
		// if n.getPropertyQueueVar() has a next property, store its name in
		// n.getPropertyVar()

		state.writeTemporary(n.getPropertyVar(), Value.makeAnyStr(dependency));
	}

	/**
	 * 12.6.4 'for-in' statement.
	 */
	@Override
	public void visit(HasNextPropertyNode n, State state) {
		// TODO: improve transfer for HasNextPropertyNode? (use EdgeTransfer?)

		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		Value queue = state.readTemporary(n.getPropertyQueueVar());

		// ==================================================
		queue = queue.setDependencyGraphReference(link(Label.IN, n, queue, state).getReference());
		// ==================================================

		// ##################################################
		dependency.join(queue.getDependency());
		// ##################################################

		state.writeTemporary(n.getResultVar(), Value.makeAnyBool(dependency));
	}

	/**
	 * Assumption.
	 */
	@Override
	public void visit(AssumeNode n, State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		if (Options.isLocalPathSensitivityDisabled())
			return;
		switch (n.getKind()) {

		case VARIABLE_NON_NULL_UNDEF: {
			if (Options.isDOMEnabled() && n.getVarName().equals("window"))
				return;
			Value v = state.readVariable(n.getVarName());
			v = v.restrictToNotNullNotUndef().clearAbsent();

			if (Options.isCollectVariableInfoEnabled()) {
				c.record(n.getVarName(), n.getSourceLocation(), v.joinDependency(dependency));
			}

			// ==================================================
			v = v.setDependencyGraphReference(link(Label.OPERATION, n, v, state).getReference());
			// ==================================================

			if (v.isNoValue() && !Options.isPropagateDeadFlow())
				state.setToBottom();
			else
				state.writeVariable(n.getVarName(), v.joinDependency(dependency));
			break;
		}

		case PROPERTY_NON_NULL_UNDEF: {
			String propname;
			if (n.isPropertyFixed())
				propname = n.getPropertyStr();
			else {
				Value propval = state.readTemporary(n.getPropertyVar());

				// ##################################################
				dependency.join(propval.getDependency());
				// ##################################################

				if (!propval.isMaybeSingleStr() || propval.isMaybeOtherThanStr())
					break; // safe to do nothing here if it gets complicated
				propname = propval.getStr();
			}
			Set<ObjectLabel> baseobjs = Conversion.toObjectLabels(state, n.getAccessNode(), state.readTemporary(n.getBaseVar()), null);
			Value v = state.readPropertyPreserveAttributes(baseobjs, propname);
			v = v.restrictToNotNullNotUndef().clearAbsent();

			// ==================================================
			v = v.setDependencyGraphReference(link(Label.OPERATION, n, v, state).getReference());
			// ==================================================

			if (v.isNoValue() && !Options.isPropagateDeadFlow())
				state.setToBottom();
			else
				state.writeSpecialProperty(baseobjs, propname, v.joinDependency(dependency));
			break;
		}
		}
	}

	@Override
	public void visit(EventEntryNode n, State state) {
		// do nothing
	}

	@Override
	public void visit(final EventDispatcherNode n, final State state) {
		// ##################################################
		Dependency dependency = new Dependency();
		dependency.join(state.getDependency());
		// ##################################################

		if (n.getBlock().getNodes().size() != 1)
			throw new RuntimeException("EventDispatcherNode should have its own basic block");
		if (!Options.isDOMEnabled()) {
			throw new RuntimeException();
		}

		// Load Event Handlers
		if (n.getType() == EventDispatcherNode.Type.LOAD) {
			// Debugging
			if (Options.isDebugEnabled()) {
				for (ObjectLabel l : state.getAllLoadEventHandlers()) {
					System.out.println("NodeTransfer: LoadEventHandler: " + l);
				}
			}

			Value v = Value.makeObject(state.getAllLoadEventHandlers(), dependency);

			// ==================================================
			v = v.setDependencyGraphReference(link(Label.EVENT, n, v, state).getReference());
			// ==================================================

			FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, v, null, c), state, c);
		}

		// Unload Event Handlers
		if (n.getType() == EventDispatcherNode.Type.UNLOAD) {
			// Debugging
			if (Options.isDebugEnabled()) {
				for (ObjectLabel l : state.getUnloadEventHandlers()) {
					System.out.println("NodeTransfer: UnloadEventHandler: " + l);
				}
			}

			Value v = Value.makeObject(state.getUnloadEventHandlers(), dependency);

			// ==================================================
			v = v.setDependencyGraphReference(link(Label.EVENT, n, v, state).getReference());
			// ==================================================

			FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, v, null, c), state, c);
		}

		// Other Event Handlers
		if (n.getType() == EventDispatcherNode.Type.OTHER) {
			// Debugging
			if (Options.isDebugEnabled()) {
				for (ObjectLabel l : state.getKeyboardEventHandlers()) {
					System.out.println("NodeTransfer: KeyboardEventHandler: " + l);
				}
				for (ObjectLabel l : state.getMouseEventHandlers()) {
					System.out.println("NodeTransfer: MouseEventHandler: " + l);
				}
				for (ObjectLabel l : state.getUnknownEventHandlers()) {
					System.out.println("NodeTransfer: UnknownEventHandler: " + l);
				}
				for (ObjectLabel l : state.getTimeoutEventHandlers()) {
					System.out.println("NodeTransfer: TimeoutEventHandler: " + l);
				}
			}

			// States
			final State keyboardState = state;
			final State mouseState = state.clone();
			final State unknownState = state.clone();
			final State timeoutState = state.clone();

			// Keyboard Events
			final Value keyboardEvent = DOMEvents.createAnyKeyboardEvent(keyboardState);
			keyboardState.writeProperty(DOMWindow.WINDOW, "event", keyboardEvent);
			c.setCurrentState(keyboardState);

			Value vkeyboard = Value.makeObject(keyboardState.getKeyboardEventHandlers(), dependency);

			// ==================================================
			vkeyboard = vkeyboard.setDependencyGraphReference(link(Label.EVENT, n, vkeyboard, state).getReference());
			// ==================================================

			FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, vkeyboard, keyboardEvent, c), keyboardState, c);

			// Mouse Events
			final Value mouseEvent = DOMEvents.createAnyMouseEvent(mouseState);
			mouseState.writeProperty(DOMWindow.WINDOW, "event", mouseEvent);

			c.setCurrentState(mouseState);

			Value vmouse = Value.makeObject(mouseState.getMouseEventHandlers(), dependency);

			// ==================================================
			vmouse = vmouse.setDependencyGraphReference(link(Label.EVENT, n, vmouse, state).getReference());
			// ==================================================

			FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, vmouse, mouseEvent, c), mouseState, c);

			// Unknown Event Handlers
			final Value anyEvent = DOMEvents.createAnyEvent(unknownState);
			unknownState.writeProperty(DOMWindow.WINDOW, "event", anyEvent);

			c.setCurrentState(unknownState);

			Value vunknown = Value.makeObject(unknownState.getUnknownEventHandlers(), dependency);

			// ==================================================
			vunknown = vunknown.setDependencyGraphReference(link(Label.EVENT, n, vunknown, state).getReference());
			// ==================================================

			FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, vunknown, anyEvent, c), unknownState, c);

			// Timeout
			c.setCurrentState(timeoutState);

			Value vtimeout = Value.makeObject(timeoutState.getTimeoutEventHandlers(), dependency);

			// ==================================================
			vtimeout = vtimeout.setDependencyGraphReference(link(Label.EVENT, n, vtimeout, state).getReference());
			// ==================================================

			FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, vtimeout, null, c), timeoutState, c);

			// Return state to its original
			c.setCurrentState(state);

			// Join states
			state.join(mouseState);
			state.join(unknownState);
			state.join(timeoutState);
		}
	}

	@Override
	public void visit(DeclareEventHandlerNode n, State state) {
		if (!Options.isDOMEnabled()) {
			throw new RuntimeException("DOM not enabled!");
		}

		Value eventHandler = state.readTemporary(n.getFunction());
		switch (n.getType()) {
		case LOAD: {
			state.addLoadEventHandler(eventHandler.getObjectLabels());
			break;
		}
		case UNLOAD: {
			state.addUnloadEventHandler(eventHandler.getObjectLabels());
			break;
		}
		case KEYBOARD: {
			state.addKeyboardEventHandler(eventHandler.getObjectLabels());
			break;
		}
		case MOUSE: {
			state.addMouseEventHandler(eventHandler.getObjectLabels());
			break;
		}
		case UNKNOWN: {
			state.addUnknownEventHandler(eventHandler.getObjectLabels());
			break;
		}
		default: {
			throw new RuntimeException("Unknown Event Handler Type");
		}
		}
	}

	@Override
	public void visit(ContextDependencyPushNode n, State state) {
		/*
		 * ############################################################
		 * Dependency: read and push the dependencies of current abstract state
		 * ############################################################
		 */

		// ##################################################
		n.setContextDependency(state.getDependency());
		n.setContextDependencyGraphReference(state.getDependencyGraphReference());
		// ##################################################
	}

	@Override
	public void visit(ContextDependencyPopNode n, State state) {
		/*
		 * ############################################################
		 * Dependency: write pushed dependencies to current abstract state
		 * ############################################################
		 */

		// ##################################################
		state.setDependency(n.getContextDependency());
		state.setDependencyGraphReference(n.getContextDependencyGraphReference());
		// ##################################################
	}
}