package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.*;

import java.util.Set;

public class LiveVariableTransferFunction extends BackwardTransferFunction<LiveVariableState> implements TransferFunction<LiveVariableState> {

    public LiveVariableTransferFunction() {
        super(new LiveVariableStateFactory());
    }

    @Override
    public LiveVariableState union(Set<LiveVariableState> states) {
        return LiveVariableState.union(states);
    }

    @Override
    public LiveVariableState transferAssumeNode(AssumeNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        if (node.getBaseVar() != Node.NO_VALUE) {
        	result.markAlive(node.getBaseVar());
        }
        if (node.getPropertyVar() != Node.NO_VALUE) {
        	result.markAlive(node.getPropertyVar());
        }
        return result;
    }

    @Override
    public LiveVariableState transferBinaryOperatorNode(BinaryOperatorNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getArg1Var());
        result.markAlive(node.getArg2Var());
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferCallNode(CallNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        if (node.getBaseVar() != Node.NO_VALUE) {
            result.markAlive(node.getBaseVar());
        }
        result.markAlive(node.getFunctionVar());
        for (int i = 0; i < node.getNumberOfArgs(); i++) {
            result.markAlive(node.getArgVar(i));
        }
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferConstantNode(ConstantNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferDeletePropertyNode(DeletePropertyNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        if (node.getBaseVar() != Node.NO_VALUE) {
            result.markAlive(node.getBaseVar());
        }
        if (node.getPropertyVar() != Node.NO_VALUE) {
            result.markAlive(node.getPropertyVar());
        }
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferEnterWithNode(EnterWithNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getObjectVar());
        return result;
    }

    @Override
    public LiveVariableState transferExceptionalReturnNode(ExceptionalReturnNode node, LiveVariableState state) {
        return new LiveVariableState();
    }

    @Override
    public LiveVariableState transferGetPropertiesNode(GetPropertiesNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getObjectVar());
        return result;
    }

    @Override
    public LiveVariableState transferHasNextPropertyNode(HasNextPropertyNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getPropertyQueueVar());
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferIfNode(IfNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getConditionVar());
        return result;
    }

    @Override
    public LiveVariableState transferNewObjectNode(NewObjectNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferNextPropertyNode(NextPropertyNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getPropertyQueueVar());
        result.markDead(node.getPropertyVar());
        return result;
    }

    @Override
    public LiveVariableState transferReadPropertyNode(ReadPropertyNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getBaseVar());
        if (node.getPropertyVar() != Node.NO_VALUE) {
            result.markAlive(node.getPropertyVar());
        }
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferReadVariableNode(ReadVariableNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markDead(node.getResultVar());
        if (node.getResultBaseVar() != Node.NO_VALUE) {
            result.markDead(node.getResultBaseVar());
        }
        return result;
    }

    @Override
    public LiveVariableState transferReturnNode(ReturnNode node, LiveVariableState state) {
        LiveVariableState result = new LiveVariableState();
        if (node.getValueVar() != Node.NO_VALUE) {
            result.markAlive(node.getValueVar());
        }
        return result;
    }

    @Override
    public LiveVariableState transferThrowNode(ThrowNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getValueVar());
        return result;
    }

    @Override
    public LiveVariableState transferTypeofNode(TypeofNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        if (node.getArgVar() != Node.NO_VALUE) {
        	result.markAlive(node.getArgVar());
        }
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferUnaryOperatorNode(UnaryOperatorNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getArgVar());
        result.markDead(node.getResultVar());
        return result;
    }

    @Override
    public LiveVariableState transferWritePropertyNode(WritePropertyNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getBaseVar());
        if (node.getPropertyVar() != Node.NO_VALUE) {
            result.markAlive(node.getPropertyVar());
        }
        result.markAlive(node.getValueVar());
        return result;
    }

    @Override
    public LiveVariableState transferWriteVariableNode(WriteVariableNode node, LiveVariableState state) {
        LiveVariableState result = state.clone();
        result.markAlive(node.getValueVar());
        return result;
    }
}
