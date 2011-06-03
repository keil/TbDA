package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.nodes.EventEntryNode;
import dk.brics.tajs.flowgraph.nodes.IfNode;
import dk.brics.tajs.flowgraph.nodes.NopNode;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IEdgeTransfer;

/**
 * Transfer for flow graph edges.
 */
public class EdgeTransfer implements IEdgeTransfer<State, CallContext> {

    /**
     * Constructs a new EdgeTransfer object.
     */
    public EdgeTransfer() {
    }

    @Override
    public boolean transfer(BasicBlock src, BasicBlock dst, State state) {
        boolean res = true;

        // Kill infeasible if-flow
        if (!Options.isLocalPathSensitivityDisabled()) {
            Node n = src.getLastNode();
            if (n instanceof IfNode) { // kill flow at if-nodes if a branch is infeasible
                IfNode ifnode = (IfNode) n;
                if (ifnode.getSuccTrue() != ifnode.getSuccFalse()) {
                    Value cond = state.readTemporary(ifnode.getConditionVar());
                    Value boolcond = Conversion.toBoolean(cond);
                    boolean cond_definitely_true = boolcond.isMaybeTrueButNotFalse();
                    boolean cond_definitely_false = boolcond.isMaybeFalseButNotTrue();
                    if ((dst == ifnode.getSuccTrue() && cond_definitely_false) ||
                            (dst == ifnode.getSuccFalse() && cond_definitely_true))
                        res = false;
                }
            }
        }


        // If there is atleast one load event handler remove the forward edge
        if (Options.isDOMEnabled()) {
            Node srcNode = src.getLastNode();
            Node dstNode = dst.getFirstNode();
            if (srcNode instanceof EventEntryNode && dstNode instanceof NopNode) {
                int definiteEventHandlers = state.getDefiniteLoadEventHandlers().size();
                if (definiteEventHandlers >= 1) {
                    res = false;
                }
            }
        }

        // If there is exactly one event handler remove the backward edge
        if (Options.isDOMEnabled()) {
            Node srcNode = src.getLastNode();
            Node dstNode = dst.getFirstNode();
            if (srcNode instanceof NopNode && dstNode instanceof EventDispatcherNode) {
                EventDispatcherNode handlerNode = (EventDispatcherNode) dstNode;
                if (handlerNode.getType() == EventDispatcherNode.Type.LOAD) {
                    int maybeEventHandlers = state.getMaybeLoadEventHandlers().size();
                    int definiteEventHandlers = state.getDefiniteLoadEventHandlers().size();
                    if (maybeEventHandlers == 1 && definiteEventHandlers == 1) {
                        res = false;
                    }
                }
            }
        }

        if (Options.isDebugEnabled())
            if (!res)
                System.out.println("Killing flow to block " + dst.getIndex());
        return res;
    }
}
