package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.*;
import dk.brics.tajs.optimizer.FlowGraphDecorator;

import java.util.Map;

/**
 * Interface for transfer functions.
 */
public interface TransferFunction<T> {

    public T join(FlowGraphDecorator flowGraph, Map<Node, T> states, Node node);

    public T transfer(FlowGraphDecorator flowGraph, Map<Node, T> states, Node node);

    public T transferAssignmentNode(AssignmentNode node, T state);

    public T transferAssumeNode(AssumeNode node, T state);

    public T transferBinaryOperatorNode(BinaryOperatorNode binaryOperatorNode, T state);

    public T transferCallNode(CallNode node, T state);

    public T transferCatchNode(CatchNode node, T state);

    public T transferConstantNode(ConstantNode node, T state);

    public T transferDeclareEventHandlerNode(DeclareEventHandlerNode node, T state);

    public T transferDeclareFunctionNode(DeclareFunctionNode node, T state);

    public T transferDeclareVariableNode(DeclareVariableNode node, T state);

    public T transferDeletePropertyNode(DeletePropertyNode node, T state);

    public T transferEnterWithNode(EnterWithNode node, T state);

    public T transferEventEntryNode(EventEntryNode node, T state);

    public T transferEventDispatcherNode(EventDispatcherNode node, T state);

    public T transferExceptionalReturnNode(ExceptionalReturnNode node, T state);

    public T transferGetPropertiesNode(GetPropertiesNode node, T state);

    public T transferHasNextPropertyNode(HasNextPropertyNode node, T state);

    public T transferIfNode(IfNode node, T state);

    public T transferLeaveWithNode(LeaveWithNode node, T state);

    public T transferNewObjectNode(NewObjectNode node, T state);

    public T transferNextPropertyNode(NextPropertyNode node, T state);

    public T transferNopNode(NopNode node, T state);

    public T transferReadPropertyNode(ReadPropertyNode node, T state);

    public T transferReadVariableNode(ReadVariableNode node, T state);

    public T transferReturnNode(ReturnNode node, T state);

    public T transferThrowNode(ThrowNode node, T state);

    public T transferTypeofNode(TypeofNode node, T state);

    public T transferUnaryOperatorNode(UnaryOperatorNode node, T state);

    public T transferWritePropertyNode(WritePropertyNode node, T state);

    public T transferWriteVariableNode(WriteVariableNode node, T state);

    public T transferContextDependencySetNode(ContextDependencyPushNode node, T state);

    public T transferContextDependencyResetNode(ContextDependencyPopNode node, T state);
}
