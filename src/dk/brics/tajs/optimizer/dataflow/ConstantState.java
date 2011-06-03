package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.nodes.ConstantNode;
import dk.brics.tajs.util.Collections;

import java.util.Iterator;
import java.util.Set;

/**
 * Abstract state representing which constants have DEFINITELY been declared at particular program point.
 */
public class ConstantState implements Cloneable {

    private Set<ConstantNode> constantNodes = Collections.newSet();

    /**
     * Construct a new empty state.
     */
    public ConstantState() {
    }

    /**
     * Constructs a new state with the given set of constant nodes.
     */
    public ConstantState(Set<ConstantNode> constantNodes) {
        this.constantNodes = Collections.newSet(constantNodes);
    }

    /**
     * Add a ConstantNode to the set of DEFINITELY declared constants.
     */
    public void add(ConstantNode node) {
        constantNodes.add(node);
    }

    /**
     * Returns a set of constants with the same type and value as the given constant.
     */
    public Set<ConstantNode> get(ConstantNode target) {
        Set<ConstantNode> result = Collections.newSet();
        for (ConstantNode node : constantNodes) {
            // Target and node have the same type
            if (node.getType() == target.getType()) {
                // Boolean type
                if (node.getType() == ConstantNode.Type.BOOLEAN) {
                    if (node.getBoolean() == target.getBoolean()) {
                        result.add(node);
                    }
                } else if (node.getType() == ConstantNode.Type.FUNCTION) {
                    if (node.getFunction() == node.getFunction()) {
                        result.add(node);
                    }
                } else if (node.getType() == ConstantNode.Type.NULL) {
                    result.add(node);
                } else if (node.getType() == ConstantNode.Type.NUMBER) {
                    if (node.getNumber() == target.getNumber()) {
                        result.add(node);
                    }
                } else if (node.getType() == ConstantNode.Type.STRING) {
                    if (node.getString().equals(target.getString())) {
                        result.add(node);
                    }
                } else if (node.getType() == ConstantNode.Type.UNDEFINED) {
                    result.add(node);
                } else {
                    throw new RuntimeException("Unknown ConstantNode.Type");
                }
            }
        }
        return result;
    }

    /**
     * Returns the intersection of the given states.
     */
    public static ConstantState intersection(Set<ConstantState> states) {
        Set<ConstantNode> result = null;
        for (ConstantState state : states) {
            if (state == null) {
                return new ConstantState();
            } else if (result == null) {
                result = Collections.newSet(state.constantNodes);
            } else {
                result.retainAll(state.constantNodes);
            }
        }
        if (result == null) {
            return new ConstantState();
        }
        return new ConstantState(result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<ConstantNode> iterator = constantNodes.iterator();
        while (iterator.hasNext()) {
            ConstantNode node = iterator.next();
            sb.append(node.toString());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public ConstantState clone() {
        return new ConstantState(Collections.<ConstantNode>newSet(constantNodes));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstantState that = (ConstantState) o;

        if (constantNodes != null ? !constantNodes.equals(that.constantNodes) : that.constantNodes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return constantNodes != null ? constantNodes.hashCode() : 0;
    }

}
