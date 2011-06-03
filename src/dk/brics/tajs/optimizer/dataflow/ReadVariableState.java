package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.nodes.ReadVariableNode;
import dk.brics.tajs.util.Collections;

import java.util.Iterator;
import java.util.Set;

/**
 * Abstract state representing which variables have DEFINITELY been read at a particular program point.
 */
public class ReadVariableState implements DataflowState, Cloneable {

    private Set<ReadVariableNode> readVariables = Collections.newSet();

    /**
     * Construct a new empty state.
     */
    public ReadVariableState() {
    }

    /**
     * Construct a new state with the given set of read variables.
     */
    public ReadVariableState(Set<ReadVariableNode> readVariables) {
        this.readVariables = Collections.newSet(readVariables);
    }

    /**
     * Add node to the set of read variables.
     */
    public void add(ReadVariableNode node) {
        readVariables.add(node);
    }

    /**
     * Remove all variables with varName from the set of read variables.
     */
    public void remove(String varName) {
        Set<ReadVariableNode> toKill = Collections.newSet();
        for (ReadVariableNode node : readVariables) {
            if (node.getVarName().equals(varName)) {
                toKill.add(node);
            }
        }
        readVariables.removeAll(toKill);
    }

    /**
     * Returns the set of read variables with name varName at this program point. (Possibly empty).
     */
    public Set<ReadVariableNode> get(String varName) {
        Set<ReadVariableNode> result = Collections.newSet();
        for (ReadVariableNode node : readVariables) {
            if (node.getVarName().equals(varName)) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * Returns the intersection between a set of ReadVariableStates.
     */
    public static ReadVariableState intersection(Set<ReadVariableState> states) {
        Set<ReadVariableNode> result = null;
        for (ReadVariableState state : states) {
            if (state == null) {
                // The intersection is empty if some state is null
                return new ReadVariableState();
            } else if (result == null) {
                result = Collections.newSet(state.readVariables);
            } else {
                result.retainAll(state.readVariables);
            }
        }
        if (result == null) {
            return new ReadVariableState();
        }
        return new ReadVariableState(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadVariableState that = (ReadVariableState) o;

        if (readVariables != null ? !readVariables.equals(that.readVariables) : that.readVariables != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return readVariables != null ? readVariables.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<ReadVariableNode> iterator = readVariables.iterator();
        while (iterator.hasNext()) {
            ReadVariableNode node = iterator.next();
            sb.append(node.getVarName());
            sb.append("(v");
            sb.append(node.getResultVar());
            sb.append(")");
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public ReadVariableState clone() {
        return new ReadVariableState(Collections.<ReadVariableNode>newSet(readVariables));
    }

}
