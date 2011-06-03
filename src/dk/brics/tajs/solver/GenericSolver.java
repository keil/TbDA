package dk.brics.tajs.solver;

import dk.brics.tajs.analysis.TypeCollector;
import dk.brics.tajs.flowgraph.*;
import dk.brics.tajs.flowgraph.nodes.CallNode;
import dk.brics.tajs.flowgraph.nodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.nodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.nodes.ReturnNode;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Generic fixpoint solver for flow graphs.
 */
public class GenericSolver<BlockStateType extends IBlockState<BlockStateType>, CallContextType extends ICallContext, StatisticsType extends IStatistics> {

    private final IAnalysis<BlockStateType, CallContextType, StatisticsType> analysis;

    private FlowGraph flowgraph;

    private IAnalysisLatticeElement<BlockStateType, CallContextType> the_analysis_lattice_element;

    private WorkList<CallContextType> worklist;

    private Map<Message, Message> messages;

    private Node current_node;

    private BlockStateType current_state;

    private CallContextType current_context;

    private SolverSynchronizer sync;

    private SolverInterface c;

    /**
     * Interface to solver used while evaluating transfer functions. Provides
     * callbacks from transfer functions to solver state.
     */
    public class SolverInterface {

        /**
         * Messages are disabled during fixpoint iteration and enabled in the
         * subsequent scan phase.
         */
        private boolean messages_enabled;

        private SolverInterface() {
        }

        /**
         * Returns the node currently being visited.
         */
        public Node getCurrentNode() {
            if (current_node == null)
                throw new RuntimeException("Unexpected call to getCurrentNode");
            return current_node;
        }

        /**
         * Returns the current calling context.
         */
        public CallContextType getCurrentContext() {
            if (current_context == null)
                throw new RuntimeException("Unexpected call to getCurrentContext");
            return current_context;
        }

        /**
         * Returns the current abstract state.
         */
        public BlockStateType getCurrentState() {
            if (current_state == null)
                throw new RuntimeException("Unexpected call to getCurrentState");
            return current_state;
        }

        /**
         * Sets the current abstract state.
         */
        public void setCurrentState(BlockStateType state) {
            current_state = state;
        }

        /**
         * Returns the flow graph.
         */
        public FlowGraph getFlowGraph() {
            return flowgraph;
        }

        /**
         * Returns the analysis lattice element.
         */
        public IAnalysisLatticeElement<BlockStateType, CallContextType> getAnalysisLatticeElement() {
            return the_analysis_lattice_element;
        }

        /**
         * Adds a message for the current node. If not in scan phase, nothing is
         * done. If the message already exists, the status is joined.
         * Uses the message as key.
         */
        public void addMessage(Status s, Severity severity, String msg) {
            addMessage(current_node, s, severity, msg, msg);
        }

        /**
         * Adds a message for the current node. If not in scan phase, nothing is
         * done. If the message already exists, the status is joined.
         */
        public void addMessage(Status s, Severity severity, String key, String msg) {
            addMessage(current_node, s, severity, key, msg);
        }

        /**
         * Adds a message for the given node. If not in scan phase, nothing is
         * done. If the message already exists, the status is joined.
         * Uses the message as key.
         */
        public void addMessage(Node n, Status s, Severity severity, String msg) {
        	addMessage(n, s, severity, msg, msg);
        }
        
        /**
         * Adds a message for the given node. If not in scan phase, nothing is
         * done. If the message already exists, the status is joined.
         */
        public void addMessage(Node n, Status s, Severity severity, String key, String msg) {
        	if (messages_enabled) {
                boolean log;
                Message m = new Message(n, s, key, msg, severity);
                Message mo = messages.get(m);
                if (mo != null) {
                    Status old = mo.getStatus();
                    mo.join(s);
                    log = !old.equals(mo.getStatus());
                } else {
                    messages.put(m, m);
                    log = !s.equals(Status.NONE);
                }
                if (log && Options.isDebugEnabled())
                    System.out.println("addMessage: " + m.getStatus() + " "
                            + m.getNode().getSourceLocation() + ": " + m.getMessage());
            }
        }

        /**
         * Records the name, type, and line number for a variable. If not in scan
         * phase, nothing is done.
         */
        public void record(String variableName, SourceLocation sourceLocation, Value value) {
            if (messages_enabled) {
                // We are in scan phase
                TypeCollector.record(variableName, sourceLocation, value);
            }
        }

        /**
         * Returns the nodes that have the given function as target for the
         * given context.
         */
        public Set<NodeAndContext<CallContextType>> getCallSources(Function f, CallContextType callee_context) {
            return the_analysis_lattice_element.getCallGraph().getSources(f, callee_context);
        }

        /**
         * Returns the specified call edge state.
         */
        public BlockStateType getCallEdgeState(Node caller, CallContextType caller_context, Function callee) {
            return the_analysis_lattice_element.getCallGraph().getCallEdgeState(caller, caller_context, callee);
        }

        /**
         * Merges s into the entry state of b in call context c and updates the
         * work list accordingly. The given state may be modified by this
         * operation. Ignored if in scan phase.
         */
        public void joinBlockEntry(BlockStateType s, BasicBlock b, CallContextType c) {
            joinBlockEntry(s, b, c, false);
        }

        private void joinBlockEntry(BlockStateType s, BasicBlock b, CallContextType c, boolean reduce) {
            if (messages_enabled)
                return;
            IAnalysisLatticeElement.MergeResult res = the_analysis_lattice_element.joinBlockEntry(s, b, c, reduce);
            if (res != null) {
                worklist.add(worklist.new Entry(b, c, res.getDelta()));
                analysis.getStatistics().registerNewFlow(b, c, res.getDiff());
                if (Options.isDebugEnabled())
                    System.out.println("New flow at block " + b.getIndex() + " node "
                            + b.getFirstNode().getIndex() + ", context " + c
                            + (res.getDiff() != null ? ", diff:" + res.getDiff() : ""));
            }
        }

        /**
         * Merges s into the entry state of f in call context c and updates the
         * work list accordingly. The given state may be modified by this
         * operation. Ignored if in scan phase.
         */
        public void joinFunctionEntry(BlockStateType call_state, Node call_node, CallContextType caller_context, Function callee, CallContextType callee_context) {
            if (messages_enabled)
                return;
            if (setCallEdge(call_node, callee, caller_context, callee_context, call_state)) {
                // new flow at call edge, propagate reduced state into function
                // entry
                BlockStateType t = call_state.clone();
                t.clearEffects();
                joinBlockEntry(t, callee.getEntry(), callee_context, true);
            }
        }

        /**
         * Adds a call target for the given call node and a given call context
         * at entry of the function. Also triggers reevaluation of
         * ordinary/exceptional return flow. The given call state may not be
         * modified after this operation.
         * 
         * @return true if the call edge was modified
         */
        private boolean setCallEdge(Node call_node, Function callee, CallContextType caller_context, CallContextType callee_context, BlockStateType call_state) {
            if (the_analysis_lattice_element.getCallGraph().addTarget(call_node, callee, caller_context, callee_context, call_state)) {
                // process existing ordinary/exceptional return flow
                StoredCurrentContextStateNode current = new StoredCurrentContextStateNode(callee_context, null, null);
                analysis.getNodeTransferFunctions().transferReturn(call_node, callee, caller_context, callee_context);
                current.restore();
                // // trigger reevaluation of ordinary/exceptional return flow
                // [now obsolete due to transferReturn]
                // BasicBlock ordinary_exit = callee.getOrdinaryExit();
                // BasicBlock exceptional_exit = callee.getExceptionalExit();
                // BlockStateType ordinary_exit_state =
                // the_analysis_lattice_element.getState(ordinary_exit,
                // callee_context);
                // BlockStateType exceptional_exit_state =
                // the_analysis_lattice_element.getState(exceptional_exit,
                // callee_context);
                // if (ordinary_exit_state != null)
                // worklist.add(worklist.new Entry(ordinary_exit,
                // callee_context, 0));
                // if (exceptional_exit_state != null)
                // worklist.add(worklist.new Entry(exceptional_exit,
                // callee_context, 0));
                return true;
            } else
                return false;
        }

        private class StoredCurrentContextStateNode {

            CallContextType cct;

            BlockStateType bst;

            Node n;

            StoredCurrentContextStateNode(CallContextType c, BlockStateType s, Node n) {
                cct = current_context;
                bst = current_state;
                this.n = current_node;
                current_context = c;
                current_state = s;
                current_node = n;
            }

            void restore() {
                current_context = cct;
                current_state = bst;
                current_node = n;
            }
        }

        /**
         * Returns the state at entry of the given call node and context.
         */
        public BlockStateType getCallState(Node n, CallContextType caller_context) {
            return the_analysis_lattice_element.getStates(n.getBlock()).get(caller_context);
        }

        /**
         * Returns the statistics collector.
         */
        public StatisticsType getStatistics() {
            return analysis.getStatistics();
        }

        /**
         * Returns true if in message scanning phase.
         */
        public boolean isScanning() {
            return messages_enabled;
        }

        public void evaluateGetter(NativeObject nativeobject, ObjectLabel objlabel, String propertyname) {
            analysis.evaluateGetter(nativeobject, objlabel, propertyname, this);
        }

        public void evaluateSetter(NativeObject nativeobject, ObjectLabel objlabel, String propertyname, Value v) {
            analysis.evaluateSetter(nativeobject, objlabel, propertyname, v, this);
        }
    }

    /**
     * Constructs a new solver.
     */
    public GenericSolver(IAnalysis<BlockStateType, CallContextType, StatisticsType> analysis) {
        this.analysis = analysis;
    }

    /**
     * Sets the synchronizer.
     */
    public void setSynchronizer(SolverSynchronizer sync) {
        this.sync = sync;
    }

    /**
     * Initializes the solver for the given flow graph.
     */
    public void init(FlowGraph flowgraph) {
        if (the_analysis_lattice_element != null)
            throw new IllegalStateException("solve() called repeatedly");

        this.flowgraph = flowgraph;
        analysis.setFlowGraph(flowgraph);
        this.the_analysis_lattice_element = analysis.makeAnalysisLattice();
        c = new SolverInterface();
        analysis.setSolverInterface(c);

        // initialize worklist
        worklist = new WorkList<CallContextType>(analysis.getWorklistStrategy());
        current_node = flowgraph.getMain().getEntry().getFirstNode();
        analysis.getInitialStateBuilder().addInitialState(c);
    }

    /**
     * Runs the solver.
     */
    public void solve() {
        // iterate until fixpoint
        block_loop: while (!worklist.isEmpty()) {
            if (sync != null) {
                if (sync.isSingleStep())
                    System.out.println("Worklist: " + worklist);
                sync.waitIfSingleStep();
            }

            // pick a pending entry
            WorkList<CallContextType>.Entry p = worklist.removeNext();
            if (p == null)
                continue; // entry may have been removed
            BasicBlock block = p.getBlock();
            current_context = p.getContext();
            BlockStateType state = the_analysis_lattice_element.getState(block, p.getContext());
            if (Options.isDebugEnabled()) {
                System.out.println("\nSelecting worklist entry for block " + block.getIndex()
                        + " at " + block.getSourceLocation());
                System.out.println("Worklist: " + worklist);
                System.out.println("Visiting " + block);
                System.out.println("Number of abstract states at this block: "
                        + the_analysis_lattice_element.getSize(block));
                System.out.println("Context: " + current_context);
            } else if (!Options.isQuietEnabled() && !Options.isTestEnabled()) {
                // if (block.isEntry())
                // System.out.println("Entering " + block.getFunction() + " at "
                // + block.getFunction().getSourceLocation());
                // if (block.isOrdinaryExit())
                // System.out.println("Returning from " + block.getFunction() +
                // " at " + block.getFunction().getSourceLocation());
                // if (block.isExceptionalExit())
                // System.out.println("Exception from " + block.getFunction() +
                // " at " + block.getFunction().getSourceLocation());
                System.out.println("block "
                        + String.format("%4d", block.getIndex())
                        + " at "
                        + block.getSourceLocation()
                        + ", transfers: "
                        + String.format("%4d", (analysis.getStatistics().getTotalNumberOfNodeTransfers() + 1))
                        + " (avg/node: "
                        + ((float) ((analysis.getStatistics().getTotalNumberOfNodeTransfers() + 1) * 1000 / flowgraph.getNumberOfNodes()))
                        / 1000 + ")"
                        + ", pending: "
                        + (worklist.size() + 1)
                        + ", contexts: "
                        + the_analysis_lattice_element.getSize(block)
                        +
                        // ", the_analysis_lattice_element: " + states_size +
                        (the_analysis_lattice_element.getPosition() != 0 ? ", position: "
                                + the_analysis_lattice_element.getPosition() : ""));
            }
            // basic block transfer
            c.getStatistics().registerBlockTransfer();
            current_state = state.clone();
            if (block == flowgraph.getMain().getEntry())
                current_state.reduce(null); // use *reduced* initial state
            if (Options.isIntermediateStatesEnabled())
                System.out.println("Before block transfer: " + current_state.toString());
            for (Node n : block.getNodes()) {
                current_node = n;
                if (Options.isDebugEnabled())
                    System.out.println("Visiting node " + current_node.getIndex() + ": "
                            + current_node + " at " + current_node.getSourceLocation());
                analysis.getNodeTransferFunctions().transfer(current_node, current_state);
                analysis.getStatistics().registerNodeTransfer(current_node);
                if (current_state.isEmpty()) {
                    if (Options.isDebugEnabled())
                        System.out.println("No non-exceptional flow");
                    continue block_loop;
                }
                if (Options.isDebugEnabled())
                    current_state.check();
                if (Options.isIntermediateStatesEnabled() && !(n instanceof CallNode))
                    System.out.println("After node transfer: " + current_state.toStringBrief());
            }
            Node first = block.getFirstNode();
            if (!(first instanceof CallNode) && !(first instanceof ReturnNode)
                    && !(first instanceof EventDispatcherNode)
                    && !(first instanceof ExceptionalReturnNode)) {
                analysis.getBlockTransferFunction().transfer(block, current_state);
                // edge transfer
                for (Iterator<BasicBlock> i = block.getSuccessors().iterator(); i.hasNext();) {
                    BasicBlock succ = i.next();
                    if (analysis.getEdgeTransferFunctions().transfer(block, succ, current_state))
                        c.joinBlockEntry(i.hasNext() ? current_state.clone() : current_state, succ, current_context);
                }
            }
        }
        c.messages_enabled = true;
    }

    /**
     * Scans for messages. Takes one round through all nodes and all contexts
     * without modifying the states. {@link #solve()} must be called first.
     */
    public void scan() {
        if (the_analysis_lattice_element == null)
            throw new IllegalStateException("scan() called before solve()");

        messages = new HashMap<Message, Message>();

        for (Function function : flowgraph.getFunctions())
            for (BasicBlock block : function.getBlocks())
                for (Node node : block.getNodes())
                    analysis.getStatistics().count(node);

        for (Function function : flowgraph.getFunctions()) {
            boolean function_is_alive = false;
            for (BasicBlock block : function.getBlocks()) {
                if (Options.isDebugEnabled())
                    System.out.println("\nScanning " + block + " at " + block.getSourceLocation());
                boolean block_is_alive = false;
                block_loop: for (Map.Entry<CallContextType, BlockStateType> me : the_analysis_lattice_element.getStateMapEntries(block)) {
                    current_state = me.getValue().clone();
                    current_context = me.getKey();
                    if (block == flowgraph.getMain().getEntry())
                        current_state.reduce(null); // use *reduced* initial
                                                    // state
                    if (current_state.isEmpty())
                        continue block_loop;
                    else
                        block_is_alive = true;
                    if (Options.isDebugEnabled())
                        System.out.println("Call context: " + current_context);
                    if (Options.isIntermediateStatesEnabled())
                        System.out.println("Before block transfer: " + current_state);
                    for (Node node : block.getNodes()) {
                        current_node = node;
                        if (Options.isDebugEnabled())
                            System.out.println("node " + current_node.getIndex() + ": "
                                    + current_node);
                        if (current_state.isEmpty()
                                && !flowgraph.getIgnorableNodes().contains(node))
                            addUnreachableCodeMessage(node, "code");
                        else
                            analysis.getNodeTransferFunctions().transfer(node, current_state);
                        if (current_state.isEmpty())
                            continue block_loop;
                    }
                }
                if (block_is_alive)
                    function_is_alive = true;
                else if (block != function.getExceptionalExit()
                        && block != function.getOrdinaryExit() && block != function.getEntry()
                        && !(block.getFirstNode() instanceof CallNode)
                        && !flowgraph.getIgnorableNodes().contains(block.getFirstNode()))
                    addUnreachableCodeMessage(block.getFirstNode(), "code");
            }
            if (!function_is_alive)
                addUnreachableCodeMessage(function.getEntry().getFirstNode(), "function"
                        + (function.getName() != null ? ": " + function.getName() : ""));
        }
    }

    private void addUnreachableCodeMessage(Node n, String kind) {
        if (Options.isDebugEnabled())
            System.out.println("Unreachable code at node " + n.getIndex() + ": " + n);
        if (!Options.isUnreachableDisabled()) {
            Message m = new Message(n, Status.CERTAIN, "Unreachable " + kind, Severity.MEDIUM, true);
            messages.put(m, m);
        }
    }

    /**
     * Returns the abstract states for the given block. {@link #solve()} must be
     * called first. The map holds the abstract state for the entry of b
     * assuming that the function containing b is executed in the given call
     * context. The call context contains information from when the function
     * containing b is entered (after parameter passing etc). Default maps to
     * the bottom abstract state.
     */
    public Map<CallContextType, BlockStateType> getStates(BasicBlock b) {
        if (the_analysis_lattice_element == null)
            throw new IllegalStateException("getStates() called before solve()");
        return the_analysis_lattice_element.getStates(b);
    }

    /**
     * Produces a string representation of the flow graph and the analysis
     * result. {@link #solve()} must be called first.
     */
    public void print(PrintWriter pw) {
        if (the_analysis_lattice_element == null)
            throw new IllegalStateException("print() called before solve()");
        for (Function f : flowgraph.getFunctions())
            for (BasicBlock b : f.getBlocks()) {
                pw.print(b);
                pw.println(the_analysis_lattice_element.getStates(b));
                pw.println();
            }
    }

    /**
     * Returns the list of messages produced during scanning. {@link #scan()}
     * must be called first.
     */
    public List<Message> getMessages() {
        if (messages == null)
            throw new IllegalStateException("getMessages() called before scan()");
        List<Message> es = newList();
        for (Message m : messages.values())
            if (m.getStatus() != Status.NONE
                    && !(m.getSeverity() == Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE && m.getStatus() != Status.CERTAIN))
                es.add(m);
        Collections.sort(es);
        return es;
    }

    /**
     * Returns the analysis lattice element.
     */
    public IAnalysisLatticeElement<BlockStateType, CallContextType> getAnalysisLatticeElement() {
        return the_analysis_lattice_element;
    }
    
    /**
     * 
     * Analyze lattice dependencies
     * @return
     */
    public void analyzeDependencies() {
    	the_analysis_lattice_element.analyzeDependencies();
    }
}
