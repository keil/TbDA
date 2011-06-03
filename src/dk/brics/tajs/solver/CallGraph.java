package dk.brics.tajs.solver;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.CallNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Strings;

/**
 * Call graph.
 */
public class CallGraph<BlockStateType extends IBlockState<BlockStateType>, CallContextType extends ICallContext> {

	/**
	 * The flow graph owning this call graph.
	 */
	private FlowGraph flow_graph;

	/**
	 * Map from call node and caller context to target functions.
	 */
	private Map<Node, Map<CallContextType, Set<Function>>> call_targets; // default
																			// is
																			// empty
																			// map
																			// and
																			// empty
																			// set

	/**
	 * Map from function and callee context to callers.
	 */
	private Map<Function, Map<CallContextType, Set<NodeAndContext<CallContextType>>>> call_sources; // default
																									// is
																									// empty
																									// maps

	/**
	 * Map from call node, caller context, and function to call edge state.
	 */
	private Map<NodeAndContext<CallContextType>, Map<Function, BlockStateType>> call_states; // default
																								// is
																								// empty
																								// maps

	/**
	 * Map from function and context to occurrence order.
	 */
	private Map<FunctionAndContext<CallContextType>, Integer> function_context_order;

	private int next_function_context_order;

	/**
	 * Constructs a new initially empty call graph.
	 */
	public CallGraph(FlowGraph flow_graph) {
		this.flow_graph = flow_graph;
		call_targets = newMap();
		call_sources = newMap();
		call_states = newMap();
		function_context_order = newMap();
	}

	/**
	 * Adds an edge from the given call node to the given function. The node is
	 * typically a {@link CallNode} but it may be any node due to implicit
	 * valueOf/toString calls. The call_state is the state after parameter
	 * passing etc. but before introduction of 'unknowns'. The call_state may
	 * not be modified after this operation.
	 * 
	 * @return true if the call graph was changed
	 */
	public boolean addTarget(Node call_node, Function target,
			CallContextType caller_context, CallContextType callee_context,
			BlockStateType call_state) {
		registerFunctionContext(target, callee_context);
		// update call target map
		Map<CallContextType, Set<Function>> mt = call_targets.get(call_node);
		if (mt == null) {
			mt = newMap();
			call_targets.put(call_node, mt);
		}
		Set<Function> fs = mt.get(caller_context);
		if (fs == null) {
			fs = newSet();
			mt.put(caller_context, fs);
		}
		fs.add(target);
		// update call source map
		Map<CallContextType, Set<NodeAndContext<CallContextType>>> ms = call_sources
				.get(target);
		if (ms == null) {
			ms = newMap();
			call_sources.put(target, ms);
		}
		Set<NodeAndContext<CallContextType>> cs = ms.get(callee_context);
		if (cs == null) {
			cs = newSet();
			ms.put(callee_context, cs);
		}
		NodeAndContext<CallContextType> nc = new NodeAndContext<CallContextType>(
				call_node, caller_context);
		boolean changed = cs.add(nc);
		// update call state map
		Map<Function, BlockStateType> mb = call_states.get(nc);
		if (mb == null) {
			mb = newMap();
			call_states.put(nc, mb);
		}
		BlockStateType old_call_state = mb.get(target);
		if (old_call_state == null || !old_call_state.equals(call_state)) {
			mb.put(target, call_state);
			changed = true;
			if (Options.isDebugEnabled())
				System.out.println((old_call_state == null ? "Adding"
						: "Updating")
						+ " call edge from node "
						+ call_node.getIndex() + " to function " + target);
		}
		return changed;
	}

	/**
	 * Assigns an order to the given function-context pair.
	 */
	public void registerFunctionContext(Function f, CallContextType context) {
		FunctionAndContext<CallContextType> fc = new FunctionAndContext<CallContextType>(
				f, context);
		if (!function_context_order.containsKey(fc))
			function_context_order.put(fc, next_function_context_order++);
	}

	/**
	 * Returns the occurrence order of the given (function,context).
	 */
	public int getFunctionContextOrder(Function f, CallContextType context) {
		Integer order = function_context_order
				.get(new FunctionAndContext<CallContextType>(f, context));
		if (order == null)
			throw new RuntimeException("Unexpected function and context: " + f
					+ " at " + f.getSourceLocation() + ", " + context);
		return order;
	}

	/**
	 * Returns the call nodes that have the given function as target (for any
	 * context).
	 */
	private Set<Node> getSources(Function f) {
		Map<CallContextType, Set<NodeAndContext<CallContextType>>> ms = call_sources
				.get(f);
		if (ms == null)
			return Collections.emptySet();
		Set<Node> res = newSet();
		for (Set<NodeAndContext<CallContextType>> s : ms.values())
			for (NodeAndContext<CallContextType> p : s)
				res.add(p.getNode());
		return res;
	}

	/**
	 * Returns the call nodes and caller contexts that have the given function
	 * as target for a given callee context.
	 */
	public Set<NodeAndContext<CallContextType>> getSources(Function f,
			CallContextType callee_context) {
		Map<CallContextType, Set<NodeAndContext<CallContextType>>> ms = call_sources
				.get(f);
		if (ms == null)
			return Collections.emptySet();
		Set<NodeAndContext<CallContextType>> s = ms.get(callee_context);
		if (s == null)
			return Collections.emptySet();
		return s;
	}

	/**
	 * Returns the specified call edge state.
	 */
	public BlockStateType getCallEdgeState(Node caller,
			CallContextType caller_context, Function callee) {
		NodeAndContext<CallContextType> nc = new NodeAndContext<CallContextType>(
				caller, caller_context);
		Map<Function, BlockStateType> mb = call_states.get(nc);
		if (mb == null)
			throw new RuntimeException("No such edge!?");
		BlockStateType b = mb.get(callee);
		if (b == null)
			throw new RuntimeException("No such edge!?");
		return b;
	}

	/**
	 * Returns the specified call sources..
	 */
	public Set<NodeAndContext<CallContextType>> getCallSources(Function callee,
			CallContextType callee_context) {
		Map<CallContextType, Set<NodeAndContext<CallContextType>>> ms = call_sources
				.get(callee);
		if (ms == null)
			return Collections.emptySet();
		Set<NodeAndContext<CallContextType>> cs = ms.get(callee_context);
		if (cs == null)
			return Collections.emptySet();
		return cs;
	}

	/**
	 * Returns the functions that are targets of the given call node (for any
	 * context).
	 */
	private Set<Function> getTargets(Node call_node) {
		Map<CallContextType, Set<Function>> mt = call_targets.get(call_node);
		if (mt == null)
			return Collections.emptySet();
		Set<Function> res = newSet();
		for (Set<Function> s : mt.values())
			res.addAll(s);
		return res;
	}

	/**
	 * Returns a textual description of this call graph. Contexts are
	 * disregarded in the output.
	 */
	@Override
	public String toString() { // TODO: sort the output
		StringBuilder b = new StringBuilder();
		for (Function f : call_sources.keySet()) {
			b.append(f).append(" at ").append(f.getSourceLocation())
					.append(" may be called from:\n");
			for (Node n : getSources(f))
				b.append("  ").append(n.getSourceLocation()).append("\n");
		}
		return b.toString();
	}

	/**
	 * Produces a Graphviz dot representation of this call graph. Contexts are
	 * disregarded in the output.
	 */
	public void toDot(PrintWriter out) {
		out.println("digraph {");
		for (Function f : flow_graph.getFunctions()) {
			if (f.isMain() || !getSources(f).isEmpty()) {
				out.println("  f"
						+ f.getIndex()
						+ " [shape=box label=\""
						+ (f.isMain() ? "<main>"
								: (Strings.escape(f.toString()) + "\\n" + f
										.getSourceLocation())) + "\"]");
				for (BasicBlock b : f.getBlocks())
					for (Node n : b.getNodes())
						for (Function target : getTargets(n))
							out.println("  f" + f.getIndex() + " -> f"
									+ target.getIndex());
			}
		}
		out.println("}");
	}

	// TODO: CallGraph: make variants of toString and toDot that make a node for
	// each (function,context)
}
