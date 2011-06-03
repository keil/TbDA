package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.nodes.IfNode;

import java.util.HashSet;
import java.util.Set;

/**
 * Remove all empty blocks from the graph, changing successors appropriately.
 * Should be the first phase to be run.
 */
public class RemoveEmptyBlocks {

    public void optimize(FlowGraph flowGraph) {
        for (Function f : flowGraph.getFunctions()) {
            removeEmptyBlocksInFunction(f);
        }
    }

    private void removeEmptyBlocksInFunction(Function f) {
        boolean changed = false;
        Set<BasicBlock> emptyBlocks = new HashSet<BasicBlock>();
        do {
            changed = false;
            Set<BasicBlock> toKill = new HashSet<BasicBlock>();
            for (BasicBlock b : f.getBlocks()) {
                if (b.getNodes().isEmpty())
                    emptyBlocks.add(b);
                for (BasicBlock bs : new HashSet<BasicBlock>(b.getSuccessors())) {
                    if (bs.getNodes().isEmpty()) {
                        b.getSuccessors().remove(bs);
                        toKill.add(bs);
                        if (b.getSuccessors().addAll(bs.getSuccessors()))
                            changed = true;
                    }
                }
                //Fix branch nodes
                if (!b.getNodes().isEmpty() && b.getLastNode() instanceof IfNode) {
                    IfNode ifn = (IfNode) b.getLastNode();
                    BasicBlock succTrue = ifn.getSuccTrue();
                    BasicBlock succFalse = ifn.getSuccFalse();
                    if (succTrue.getNodes().isEmpty()) {
                        toKill.add(succTrue);
                        succTrue = succTrue.getSingleSuccessor();
                        changed = true;
                    }
                    if (succFalse.getNodes().isEmpty()) {
                        toKill.add(succFalse);
                        succFalse = succFalse.getSingleSuccessor();
                        changed = true;
                    }
                    ifn.setSuccessors(succTrue, succFalse);
                }
                /*	if (!b.getNodes().isEmpty() && b.getLastNode() instanceof NextPropertyNode) {
                        NextPropertyNode npn = (NextPropertyNode) b.getLastNode();
                        BasicBlock succHasNext = npn.getSuccHasNext();
                        BasicBlock succNotHasNext = npn.getSuccNotHasNext();
                        if (succHasNext.getNodes().isEmpty()){
                            toKill.add(succHasNext);
                            succHasNext = succHasNext.getSingleSuccessor();
                            changed = true;
                        }
                        if (succNotHasNext.getNodes().isEmpty()) {
                            toKill.add(succNotHasNext);
                            succNotHasNext = succNotHasNext.getSingleSuccessor();
                            changed = true;
                        }
                        npn.setSuccessors(succHasNext, succNotHasNext);
                    }*/
            }
            f.getBlocks().removeAll(toKill);
        } while (changed); // TODO: improve performance?
        f.getBlocks().removeAll(emptyBlocks); // Quick way to kill all empty blocks with no preds.
    }

}
