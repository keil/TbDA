package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.*;
import dk.brics.tajs.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// TODO: Rename

public class FlowGraphDecorator {

    private Map<BasicBlock, Set<BasicBlock>> predecessorBlocks = Collections.newMap();

    private Map<Node, Set<Node>> predecessorNodes = Collections.newMap();
    private Map<Node, Set<Node>> successorNodes = Collections.newMap();

    private Map<Function, Set<Function>> innerFunctions = Collections.newMap();
    private Map<Function, Set<String>> innerVariablesWritten = Collections.newMap();

    public FlowGraphDecorator(FlowGraph flowGraph) {

        // Build a map of predecessor basic blocks
        for (Function function : flowGraph.getFunctions()) {
            for (BasicBlock basicBlock : function.getBlocks()) {
                predecessorBlocks.put(basicBlock, Collections.<BasicBlock>newSet());
            }
            for (BasicBlock predecessor : function.getBlocks()) {
                for (BasicBlock successor : predecessor.getSuccessors()) {
                    predecessorBlocks.get(successor).add(predecessor);
                }
            }
        }

        // Build map of predecessor nodes
        for (Function function : flowGraph.getFunctions()) {
            for (BasicBlock basicBlock : function.getBlocks()) {
                Node previous = null;
                for (Node node : basicBlock.getNodes()) {
                    Set<Node> result = Collections.newSet();
                    if (node == basicBlock.getFirstNode()) {
                        for (BasicBlock predecessorBlock : predecessorBlocks.get(basicBlock)) {
                            result.add(predecessorBlock.getLastNode());
                        }
                    } else {
                        result.add(previous);
                    }
                    predecessorNodes.put(node, result);
                    previous = node;
                }
            }
        }

        // Build map of successor nodes
        for (Function function : flowGraph.getFunctions()) {
            for (BasicBlock basicBlock : function.getBlocks()) {
                List<Node> nodes = basicBlock.getNodes();
                // Case 1: The node has an immediate successor
                // Note: The loops goes to size() - 1,
                // because the last nodes must be handled seperately.
                for (int i = 0; i < nodes.size() - 1; i++) {
                    Set<Node> result = Collections.newSet();
                    result.add(nodes.get(i + 1));
                    successorNodes.put(nodes.get(i), result);
                }
                // Case 2: The node has successors in the next basic blocks
                Set<Node> result = Collections.newSet();
                for (BasicBlock successorBlock : basicBlock.getSuccessors()) {
                    result.add(successorBlock.getFirstNode());
                }
                if (basicBlock.canThrowExceptions() && basicBlock.getExceptionHandler() != null) {
                    result.add(basicBlock.getExceptionHandler().getFirstNode());
                }
                successorNodes.put(basicBlock.getLastNode(), result);
            }
        }

        // Build map of inner functions.
        for (Function function : flowGraph.getFunctions()) {
            innerFunctions.put(function, Collections.<Function>newSet());
        }
        for (Function function : flowGraph.getFunctions()) {
            if (function.hasOuterFunction()) {
                innerFunctions.get(function.getOuterFunction()).add(function);
            }
        }

        // Build map of inner variables (potentially) written
        for (Function function : flowGraph.getFunctions()) {
            Set<String> variables = Collections.<String>newSet();
            if (function.hasOuterFunction()) {
                variables.addAll(getVariablesWritten(function));
            }
            innerVariablesWritten.put(function, variables);
        }
    }

    /**
     * Recursively visit each function.
     */
    private Set<String> getVariablesWritten(Function function) {
        Set<String> result = Collections.newSet();
        for (Function innerFunction : innerFunctions.get(function)) {
            for (BasicBlock block : innerFunction.getBlocks()) {
                for (Node node : block.getNodes()) {
                    if (node instanceof WriteVariableNode) {
                        result.add(((WriteVariableNode) node).getVarName());
                    }
                }
            }
            result.addAll(getVariablesWritten(innerFunction));
        }
        return result;
    }

    /**
     * Returns the set of variables declared in a given function.
     */
    public Set<String> getDeclaredVariables(Function function) {
        Set<String> result = Collections.newSet();
        for (BasicBlock basicBlock : function.getBlocks()) {
            for (Node node : basicBlock.getNodes()) {
                if (node instanceof DeclareVariableNode) {
                    DeclareVariableNode declareVariableNode = (DeclareVariableNode) node;
                    result.add(declareVariableNode.getVarName());
                }
            }
        }
        result.addAll(function.getParameterNames());
        return result;
    }

    /**
     * Returns the set of basic blocks occuring immediately before the given basic block.
     */
    public Set<BasicBlock> getPredecessorBlocks(BasicBlock basicBlock) {
        return predecessorBlocks.get(basicBlock);
    }

    /**
     * Returns the node occuring immediately before the given node.
     */
    public Node getPredecessorNode(Node node) {
        Set<Node> nodes = predecessorNodes.get(node);
        if (nodes.size() != 1) {
            throw new RuntimeException("Node does not have a single predecessor node");
        }
        return nodes.iterator().next();
    }

    /**
     * Returns the set of predecessor nodes, either the node occuring immediately before the given node,
     * or the last node occuring in each predecessor basic block
     */
    public Set<Node> getAllPredecessorNodes(Node node) {
        return predecessorNodes.get(node);
    }

    /**
     * Returns the node occuring immediate after the given node.
     */
    public Node getSuccessorNode(Node node) {
        Set<Node> nodes = successorNodes.get(node);
        if (nodes.size() != 1) {
            throw new RuntimeException("Node does not have a single successor node");
        }
        return nodes.iterator().next();
    }

    /**
     * Returns the set of successor nodes, either the node occuring immediately after the given node,
     * or the set of nodes occuring in the following basic blocks
     */
    public Set<Node> getAllSuccessorNodes(Node node) {
        return successorNodes.get(node);
    }

    /**
     * Returns the set of variables potentially (over)written by inner functions for the given function.
     */
    public Set<String> getInnerVariablesWritten(Function function) {
        return innerVariablesWritten.get(function);
    }

    /**
     * Replace targetVar by replacementVar in the BasicBlock and all successors.
     */
    public void replaceVariable(BasicBlock basicBlock, int targetVar, int replacementVar) {
        Set<BasicBlock> visited = Collections.newSet();
        Queue<BasicBlock> queue = Collections.newQueue();
        queue.add(basicBlock);
        while (!queue.isEmpty()) {
            BasicBlock bs = queue.poll();
            // Updates Nodes
            for (Node node : bs.getNodes()) {
                replaceInNode(node, targetVar, replacementVar);
            }
            // Add successors to the queue?
            for (BasicBlock successor : bs.getSuccessors()) {
                if (!visited.contains(successor)) {
                    queue.add(successor);
                }
            }
            visited.add(bs);
        }
    }

    /**
     * Replace all occurences of targetVar with replacementVar.
     */
    private void replaceInNode(Node node, int targetVar, int replacementVar) {

        if (node instanceof AssumeNode) {
            // TODO: Implement new type of assume node
            AssumeNode assumeNode = (AssumeNode) node;
            if (assumeNode.getBaseVar() == targetVar) {
                assumeNode.setBaseVar(replacementVar);
            }
            if (assumeNode.getPropertyVar() == targetVar) {
                assumeNode.setPropertyVar(replacementVar);
            }
        } else if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode binaryOperatorNode = (BinaryOperatorNode) node;
            if (binaryOperatorNode.getArg1Var() == targetVar) {
                binaryOperatorNode.setArg1Var(replacementVar);
            }
            if (binaryOperatorNode.getArg2Var() == targetVar) {
                binaryOperatorNode.setArg2Var(replacementVar);
            }
        } else if (node instanceof CallNode) {
            CallNode callNode = (CallNode) node;
            if (callNode.getBaseVar() == targetVar) {
                callNode.setBaseVar(replacementVar);
            }
            if (callNode.getFunctionVar() == targetVar) {
                callNode.setFunctionVar(replacementVar);
            }
            for (int i = 0; i < callNode.getNumberOfArgs(); i++) {
                if (callNode.getArgVar(i) == targetVar) {
                    callNode.setArgVar(i, replacementVar);
                }
            }
        } else if (node instanceof CatchNode) {
            // do nothing
        } else if (node instanceof ConstantNode) {
            // do nothing
        } else if (node instanceof DeclareEventHandlerNode) {
            // do nothing
        } else if (node instanceof DeclareFunctionNode) {
            // do nothing
        } else if (node instanceof DeclareVariableNode) {
            // do nothing
        } else if (node instanceof DeletePropertyNode) {
            DeletePropertyNode deletePropertyNode = (DeletePropertyNode) node;
            if (deletePropertyNode.getBaseVar() == targetVar) {
                deletePropertyNode.setBaseVar(replacementVar);
            }
            if (deletePropertyNode.getPropertyVar() == targetVar) {
                deletePropertyNode.setPropertyVar(replacementVar);
            }
        } else if (node instanceof EnterWithNode) {
            EnterWithNode enterWithNode = (EnterWithNode) node;
            if (enterWithNode.getObjectVar() == targetVar) {
                enterWithNode.setObjectVar(replacementVar);
            }
        } else if (node instanceof EventDispatcherNode) {
            // do nothing
        } else if (node instanceof ExceptionalReturnNode) {
            // do nothing
        } else if (node instanceof GetPropertiesNode) {
            GetPropertiesNode getPropertiesNode = (GetPropertiesNode) node;
            if (getPropertiesNode.getObjectVar() == targetVar) {
                getPropertiesNode.setObjectVar(replacementVar);
            }
        } else if (node instanceof HasNextPropertyNode) {
            // do nothing
        } else if (node instanceof IfNode) {
            IfNode ifNode = (IfNode) node;
            if (ifNode.getConditionVar() == targetVar) {
                ifNode.setConditionVar(replacementVar);
            }
        } else if (node instanceof LeaveWithNode) {
            // do nothing
        } else if (node instanceof NewObjectNode) {
            // do nothing
        } else if (node instanceof NextPropertyNode) {
            NextPropertyNode nextPropertyNode = (NextPropertyNode) node;
            if (nextPropertyNode.getPropertyVar() == targetVar) {
                nextPropertyNode.setPropertyVar(replacementVar);
            }
        } else if (node instanceof NopNode) {
            // do nothing
        } else if (node instanceof ReadPropertyNode) {
            ReadPropertyNode readPropertyNode = (ReadPropertyNode) node;
            if (readPropertyNode.getBaseVar() == targetVar) {
                readPropertyNode.setBaseVar(replacementVar);
            }
            if (readPropertyNode.getPropertyVar() == targetVar) {
                readPropertyNode.setPropertyVar(replacementVar);
            }
        } else if (node instanceof ReadVariableNode) {
            // do nothing
        } else if (node instanceof ReturnNode) {
            ReturnNode returnNode = (ReturnNode) node;
            if (returnNode.getValueVar() == targetVar) {
                returnNode.setValueVar(replacementVar);
            }
        } else if (node instanceof ThrowNode) {
            ThrowNode throwNode = (ThrowNode) node;
            if (throwNode.getValueVar() == targetVar) {
                throwNode.setValueVar(replacementVar);
            }
        } else if (node instanceof TypeofNode) {
            TypeofNode typeofNode = (TypeofNode) node;
            if (typeofNode.getArgVar() == targetVar) {
                typeofNode.setArgVar(replacementVar);
            }
        } else if (node instanceof UnaryOperatorNode) {
            UnaryOperatorNode unaryOperatorNode = (UnaryOperatorNode) node;
            if (unaryOperatorNode.getArgVar() == targetVar) {
                unaryOperatorNode.setArgVar(replacementVar);
            }
        } else if (node instanceof WritePropertyNode) {
            WritePropertyNode writePropertyNode = (WritePropertyNode) node;
            if (writePropertyNode.getBaseVar() == targetVar) {
                writePropertyNode.setBaseVar(replacementVar);
            }
            if (writePropertyNode.getPropertyVar() == targetVar) {
                writePropertyNode.setPropertyVar(replacementVar);
            }
            if (writePropertyNode.getValueVar() == targetVar) {
                writePropertyNode.setValueVar(replacementVar);
            }
        } else if (node instanceof WriteVariableNode) {
            WriteVariableNode writeVariableNode = (WriteVariableNode) node;
            if (writeVariableNode.getValueVar() == targetVar) {
                writeVariableNode.setValueVar(replacementVar);
            }
        } else if (node instanceof ContextDependencyPushNode) {
        	// do nothing
        } else if (node instanceof ContextDependencyPopNode) {
        	// do nothing
        } else {
            throw new RuntimeException("Unknown Node Type: " + node.getClass());
        }
    }

}
