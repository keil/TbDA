package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.nodes.WriteVariableNode;
import dk.brics.tajs.util.Collections;

import java.util.Iterator;
import java.util.Set;

/**
 * Abstract state representing which variables have DEFINITELY been written at a particular program point.
 */
public class WriteVariableState implements DataflowState, Cloneable {

    private Set<WriteVariableNode> writeVariables = Collections.newSet();

    /**
     * Construct a new empty state.
     */
    public WriteVariableState() {

    }

    /**
     * Construct a new state with the given set of write variables.
     */
    public WriteVariableState(Set<WriteVariableNode> writeVariables) {
        this.writeVariables = Collections.newSet(writeVariables);
    }

    /**
     * Add a node to the set of write variables.
     */
    public void add(WriteVariableNode node) {
        writeVariables.add(node);
    }

    /**
     * Remove all write variable nodes with the given name.
     */
    public void remove(String varName) {
        Iterator<WriteVariableNode> iterator = writeVariables.iterator();
        while (iterator.hasNext()) {
            WriteVariableNode node = iterator.next();
            if (node.getVarName().equals(varName)) {
                iterator.remove();
            }
        }
    }

    /**
     * Returns a write variable node with the given name or null if none exists.
     */
    public WriteVariableNode get(String varName) {
        Set<WriteVariableNode> candidates = Collections.newSet();
        for (WriteVariableNode candidate : writeVariables) {
            if (candidate.getVarName().equals(varName)) {
                candidates.add(candidate);
            }
        }
        if (candidates.size() == 1) {
            return candidates.iterator().next();
        }
        return null;
    }

    public static WriteVariableState intersection(Set<WriteVariableState> states) {
        Set<WriteVariableNode> result = null;
        for (WriteVariableState state : states) {
            if (state == null) {
                return new WriteVariableState();
            } else if (result == null) {
                result = Collections.newSet(state.writeVariables);
            } else {
                result.retainAll(state.writeVariables);
            }
        }
        if (result == null) {
            return new WriteVariableState();
        }
        return new WriteVariableState(result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<WriteVariableNode> iterator = writeVariables.iterator();
        while (iterator.hasNext()) {
            WriteVariableNode node = iterator.next();
            sb.append(node.getVarName());
            sb.append(" (");
            sb.append(node.getValueVar());
            sb.append(")");
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public WriteVariableState clone() {
        return new WriteVariableState(Collections.<WriteVariableNode>newSet(writeVariables));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WriteVariableState that = (WriteVariableState) o;

        if (writeVariables != null ? !writeVariables.equals(that.writeVariables) : that.writeVariables != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return writeVariables != null ? writeVariables.hashCode() : 0;
    }

}
