package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.*;
import dk.brics.tajs.optimizer.FlowGraphDecorator;

import java.util.Map;

/**
 * Template class for transfer functions.
 */
// TODO: Use visitor
public abstract class AbstractTransferFunction<T> implements TransferFunction<T> {

    @Override
    public T transfer(FlowGraphDecorator flowGraph, Map<Node, T> states, Node node) {
        T state = join(flowGraph, states, node);

        if (node.getClass() == AssignmentNode.class) {
            return transferAssignmentNode((AssignmentNode) node, state);
        } else if (node.getClass() == AssumeNode.class) {
            return transferAssumeNode((AssumeNode) node, state);
        } else if (node.getClass() == BinaryOperatorNode.class) {
            return transferBinaryOperatorNode((BinaryOperatorNode) node, state);
        } else if (node.getClass() == CallNode.class) {
            return transferCallNode((CallNode) node, state);
        } else if (node.getClass() == CatchNode.class) {
            return transferCatchNode((CatchNode) node, state);
        } else if (node.getClass() == ConstantNode.class) {
            return transferConstantNode((ConstantNode) node, state);
        } else if (node.getClass() == DeclareEventHandlerNode.class) {
            return transferDeclareEventHandlerNode((DeclareEventHandlerNode) node, state);
        } else if (node.getClass() == DeclareFunctionNode.class) {
            return transferDeclareFunctionNode((DeclareFunctionNode) node, state);
        } else if (node.getClass() == DeclareVariableNode.class) {
            return transferDeclareVariableNode((DeclareVariableNode) node, state);
        } else if (node.getClass() == DeletePropertyNode.class) {
            return transferDeletePropertyNode((DeletePropertyNode) node, state);
        } else if (node.getClass() == EnterWithNode.class) {
            return transferEnterWithNode((EnterWithNode) node, state);
        } else if (node.getClass() == EventEntryNode.class) {
            return transferEventEntryNode((EventEntryNode) node, state);
        } else if (node.getClass() == EventDispatcherNode.class) {
            return transferEventDispatcherNode((EventDispatcherNode) node, state);
        } else if (node.getClass() == ExceptionalReturnNode.class) {
            return transferExceptionalReturnNode((ExceptionalReturnNode) node, state);
        } else if (node.getClass() == GetPropertiesNode.class) {
            return transferGetPropertiesNode((GetPropertiesNode) node, state);
        } else if (node.getClass() == HasNextPropertyNode.class) {
            return transferHasNextPropertyNode((HasNextPropertyNode) node, state);
        } else if (node.getClass() == IfNode.class) {
            return transferIfNode((IfNode) node, state);
        } else if (node.getClass() == LeaveWithNode.class) {
            return transferLeaveWithNode((LeaveWithNode) node, state);
        } else if (node.getClass() == NewObjectNode.class) {
            return transferNewObjectNode((NewObjectNode) node, state);
        } else if (node.getClass() == NextPropertyNode.class) {
            return transferNextPropertyNode((NextPropertyNode) node, state);
        } else if (node.getClass() == NopNode.class) {
            return transferNopNode((NopNode) node, state);
        } else if (node.getClass() == ReadPropertyNode.class) {
            return transferReadPropertyNode((ReadPropertyNode) node, state);
        } else if (node.getClass() == ReadVariableNode.class) {
            return transferReadVariableNode((ReadVariableNode) node, state);
        } else if (node.getClass() == ReturnNode.class) {
            return transferReturnNode((ReturnNode) node, state);
        } else if (node.getClass() == ThrowNode.class) {
            return transferThrowNode((ThrowNode) node, state);
        } else if (node.getClass() == TypeofNode.class) {
            return transferTypeofNode((TypeofNode) node, state);
        } else if (node.getClass() == UnaryOperatorNode.class) {
            return transferUnaryOperatorNode((UnaryOperatorNode) node, state);
        } else if (node.getClass() == WritePropertyNode.class) {
            return transferWritePropertyNode((WritePropertyNode) node, state);
        } else if (node.getClass() == WriteVariableNode.class) {
            return transferWriteVariableNode((WriteVariableNode) node, state);
        } else if (node.getClass() == ContextDependencyPushNode.class) {
            return transferContextDependencySetNode((ContextDependencyPushNode) node, state);
        } else if (node.getClass() == ContextDependencyPopNode.class) {
            return transferContextDependencyResetNode((ContextDependencyPopNode) node, state);
        } else {
            throw new RuntimeException("Unknown Node Type: " + node.getClass());
        }
    }

    @Override
    public T transferAssignmentNode(AssignmentNode node, T state) {
        return state;
    }

    @Override
    public T transferAssumeNode(AssumeNode node, T state) {
        return state;
    }

    @Override
    public T transferBinaryOperatorNode(BinaryOperatorNode node, T state) {
        return state;
    }

    @Override
    public T transferCallNode(CallNode node, T state) {
        return state;
    }

    @Override
    public T transferCatchNode(CatchNode node, T state) {
        return state;
    }

    @Override
    public T transferConstantNode(ConstantNode node, T state) {
        return state;
    }

    @Override
    public T transferEventEntryNode(EventEntryNode node, T state) {
        return state;
    }

    @Override
    public T transferDeclareEventHandlerNode(DeclareEventHandlerNode node, T state) {
        return state;
    }

    @Override
    public T transferDeclareFunctionNode(DeclareFunctionNode node, T state) {
        return state;
    }

    @Override
    public T transferDeclareVariableNode(DeclareVariableNode node, T state) {
        return state;
    }

    @Override
    public T transferDeletePropertyNode(DeletePropertyNode node, T state) {
        return state;
    }

    @Override
    public T transferEnterWithNode(EnterWithNode node, T state) {
        return state;
    }

    @Override
    public T transferEventDispatcherNode(EventDispatcherNode node, T state) {
        return state;
    }

    @Override
    public T transferExceptionalReturnNode(ExceptionalReturnNode node, T state) {
        return state;
    }

    @Override
    public T transferGetPropertiesNode(GetPropertiesNode node, T state) {
        return state;
    }

    @Override
    public T transferHasNextPropertyNode(HasNextPropertyNode node, T state) {
        return state;
    }

    @Override
    public T transferIfNode(IfNode node, T state) {
        return state;
    }

    @Override
    public T transferLeaveWithNode(LeaveWithNode node, T state) {
        return state;
    }

    @Override
    public T transferNewObjectNode(NewObjectNode node, T state) {
        return state;
    }

    @Override
    public T transferNextPropertyNode(NextPropertyNode node, T state) {
        return state;
    }

    @Override
    public T transferNopNode(NopNode node, T state) {
        return state;
    }

    @Override
    public T transferReadPropertyNode(ReadPropertyNode node, T state) {
        return state;
    }

    @Override
    public T transferReadVariableNode(ReadVariableNode node, T state) {
        return state;
    }

    @Override
    public T transferReturnNode(ReturnNode node, T state) {
        return state;
    }

    @Override
    public T transferThrowNode(ThrowNode node, T state) {
        return state;
    }

    @Override
    public T transferTypeofNode(TypeofNode node, T state) {
        return state;
    }

    @Override
    public T transferUnaryOperatorNode(UnaryOperatorNode node, T state) {
        return state;
    }

    @Override
    public T transferWritePropertyNode(WritePropertyNode node, T state) {
        return state;
    }

    @Override
    public T transferWriteVariableNode(WriteVariableNode node, T state) {
        return state;
    }

    @Override
    public T transferContextDependencySetNode(ContextDependencyPushNode node, T state) {
        return state;
    }
    
    @Override
    public T transferContextDependencyResetNode(ContextDependencyPopNode node, T state) {
        return state;
    }
}
