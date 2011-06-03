package dk.brics.tajs.flowgraph;

import dk.brics.tajs.flowgraph.nodes.*;

/**
 * Visitor for nodes.
 */
public interface NodeVisitor<Arg> {

	/**
	 * Visits a AssumeNode.
	 */
	public void visit(AssumeNode n, Arg a);

	/**
	 * Visits a BinaryOperatorNode.
	 */
	public void visit(BinaryOperatorNode n, Arg a);

	/**
	 * Visits a CallNode.
	 */
	public void visit(CallNode n, Arg a);

	/**
	 * Visits a CatchNode.
	 */
	public void visit(CatchNode n, Arg a);

	/**
	 * Visits a ConstantNode.
	 */
	public void visit(ConstantNode n, Arg a);

	/**
	 * Visits a DeletePropertyNode.
	 */
	public void visit(DeletePropertyNode n, Arg a);

	/**
	 * Visits an EnterWithNode.
	 */
	public void visit(EnterWithNode n, Arg a);

	/**
	 * Visits an ExceptionalReturnNode.
	 */
	public void visit(ExceptionalReturnNode n, Arg a);

	/**
	 * Visits a DeclareFunctionNode.
	 */
	public void visit(DeclareFunctionNode n, Arg a);

	/**
	 * Visits a GetPropertiesNode.
	 */
	public void visit(GetPropertiesNode n, Arg a);

	/**
	 * Visits an IfNode.
	 */
	public void visit(IfNode n, Arg a);

	/**
	 * Visits a LeaveWithNode.
	 */
	public void visit(LeaveWithNode n, Arg a);

	/**
	 * Visits a NewObjectNode.
	 */
	public void visit(NewObjectNode n, Arg a);

	/**
	 * Visits a NextPropertyNode.
	 */
	public void visit(NextPropertyNode n, Arg a);
	
	/**
	 * Visits a HasNextPropertyNode.
	 */
	public void visit(HasNextPropertyNode n, Arg a);


	/**
	 * Visits a NopNode.
	 */
	public void visit(NopNode n, Arg a);

	/**
	 * Visits a ReadPropertyNode.
	 */
	public void visit(ReadPropertyNode n, Arg a);

	/**
	 * Visits a ReadVariableNode.
	 */
	public void visit(ReadVariableNode n, Arg a);

	/**
	 * Visits a ReturnNode.
	 */
	public void visit(ReturnNode n, Arg a);

	/**
	 * Visits a ThrowNode.
	 */
	public void visit(ThrowNode n, Arg a);

	/**
	 * Visits a TypeofNode.
	 */
	public void visit(TypeofNode n, Arg a);

	/**
	 * Visits a UnaryOperatorNode.
	 */
	public void visit(UnaryOperatorNode n, Arg a);

	/**
	 * Visits a DeclareVariableNode.
	 */
	public void visit(DeclareVariableNode n, Arg a);

	/**
	 * Visits a WritePropertyNode.
	 */
	public void visit(WritePropertyNode n, Arg a);

	/**
	 * Visits a WriteVariableNode.
	 */
	public void visit(WriteVariableNode n, Arg a);

	/**
	 * Visits an EventEntryNode
	 */
	public void visit(EventEntryNode n, Arg a);

	/**
	 * Visits an EventDispatcherNode
	 */
	public void visit(EventDispatcherNode n, Arg a);
	
    /**
     * Visits an DeclareEventHandlerNode
     */
	public void visit(DeclareEventHandlerNode n, Arg a);
	
	/**
     * Visits an ContextDependencySetNode
     */
	public void visit(ContextDependencyPushNode n, Arg a);

	/**
     * Visits an ContextDependencyResetNode
     */
	public void visit(ContextDependencyPopNode n, Arg a);
	
}