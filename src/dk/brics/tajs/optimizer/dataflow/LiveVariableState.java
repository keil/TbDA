package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.util.Collections;

import java.util.Iterator;
import java.util.Set;

public class LiveVariableState implements DataflowState, Cloneable {

    private Set<Integer> liveVariables = Collections.newSet();

    public LiveVariableState() {
    }

    public LiveVariableState(Set<Integer> liveVariables) {
        this.liveVariables = Collections.newSet(liveVariables);
    }

    public void markAlive(int var) {
        liveVariables.add(var);
    }

    public void markDead(int var) {
        liveVariables.remove(var);
    }

    public boolean isAlive(int var) {
        return liveVariables.contains(var);
    }

    public Set<Integer> getLiveVariables() {
        return liveVariables;
    }

    public static LiveVariableState union(Set<LiveVariableState> states) {
        Set<Integer> result = Collections.newSet();
        for (LiveVariableState state : states) {
            if (state != null) {
                result.addAll(state.liveVariables);
            }
        }
        return new LiveVariableState(result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = liveVariables.iterator();
        while (iterator.hasNext()) {
            sb.append("v");
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public LiveVariableState clone() {
        return new LiveVariableState(liveVariables);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LiveVariableState that = (LiveVariableState) o;

        if (liveVariables != null ? !liveVariables.equals(that.liveVariables) : that.liveVariables != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return liveVariables != null ? liveVariables.hashCode() : 0;
    }

}
