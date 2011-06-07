package dk.brics.tajs.flowgraph;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.util.Collections;

/**
 * Flow graph. A flow graph is divided into functions, one of them representing
 * the main program. Each function contains blocks of nodes. Nodes represent
 * primitive instructions, edges represent control flow.
 */
public class FlowGraph {
	private Collection<Function> functions;

	private Collection<Function> load_event_handlers; // always a subset of
														// functions
	private Collection<Function> unload_event_handlers; // always a subset of
														// functions
	private Collection<Function> keyboard_event_handlers; // always a subset of
															// functions
	private Collection<Function> mouse_event_handlers; // always a subset of
														// functions
	private Collection<Function> unknown_event_handlers; // always a subset of
															// functions

	private Function main;

	private Map<ObjectLabel, Function> objlabel2function;

	private int number_of_blocks;

	private int number_of_nodes;

	private int number_of_functions;

	private Set<Node> ignorable_nodes;

	/**
	 * Constructs a new uninitialized flow graph.
	 */
	public FlowGraph() {
		functions = newList();
		load_event_handlers = newList();
		unload_event_handlers = newList();
		keyboard_event_handlers = newList();
		mouse_event_handlers = newList();
		unknown_event_handlers = newList();
		ignorable_nodes = newSet();
	}

	/**
	 * Completes this flow graph after all functions and blocks have been added.
	 */
	public void complete() {
		objlabel2function = newMap();
		for (Function f : functions)
			objlabel2function.put(new ObjectLabel(f), f);
		setBlockOrders();
	}

	/**
	 * Adds a block to this flow graph. Also sets the block index and the node
	 * index for each node in the block.
	 */
	public void addBlock(BasicBlock b, Function f) {
		if (b == null || f == null)
			throw new RuntimeException("Trying to add null block of function");
		f.getBlocks().add(b);
		b.setIndex(number_of_blocks++);
		for (Node n : b.getNodes())
			n.setIndex(number_of_nodes++);
	}

	/**
	 * Returns the total number of nodes in this flow graph.
	 */
	public int getNumberOfNodes() {
		return number_of_nodes;
	}

	/**
	 * Returns the total number of basic blocks in this flow graph.
	 */
	public int getNumberOfBlocks() {
		return number_of_blocks;
	}

	/**
	 * Returns the number of functions in this flow graph, including the main
	 * function.
	 */
	public int getNumberOfFunctions() {
		return functions.size();
	}

	/**
	 * Returns the functions, including the main function.
	 */
	public Collection<Function> getFunctions() {
		return functions;
	}

	/**
	 * Adds the given function.
	 */
	public void addFunction(Function f) {
		if (f == null)
			throw new NullPointerException();
		functions.add(f);
		f.setIndex(number_of_functions++);
	}

	/**
	 * Adds the given event handler. Must also be added as a function.
	 */
	public void addLoadEventHandler(Function f) {
		if (f == null)
			throw new NullPointerException();
		load_event_handlers.add(f);
	}

	/**
	 * Adds the given event handler. Must also be added as a function.
	 */
	public void addUnloadEventHandler(Function f) {
		if (f == null)
			throw new NullPointerException();
		unload_event_handlers.add(f);
	}

	/**
	 * Adds the given event handler. Must also be added as a function.
	 */
	public void addKeyboardEventHandler(Function f) {
		if (f == null)
			throw new NullPointerException();
		keyboard_event_handlers.add(f);
	}

	/**
	 * Adds the given event handler. Must also be added as a function.
	 */
	public void addMouseEventHandler(Function f) {
		if (f == null)
			throw new NullPointerException();
		mouse_event_handlers.add(f);
	}

	/**
	 * Adds the given event handler. Must also be added as a function.
	 */
	public void addUnknownEventHandler(Function f) {
		if (f == null)
			throw new NullPointerException();
		unknown_event_handlers.add(f);
	}

	/**
	 * Returns the function with the given name.
	 */
	public Function getFunction(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException();
		}
		Set<Function> result = Collections.newSet();
		for (Function function : functions) {
			if (name.equals(function.getName())) {
				result.add(function);
			}
		}
		if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return result.iterator().next();
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * Returns the function for the given function object label.
	 */
	public Function getFunction(ObjectLabel objlabel) {
		if (objlabel.getKind() != Kind.FUNCTION)
			throw new IllegalArgumentException("Non-Function object label: "
					+ objlabel);
		Function f = objlabel2function.get(objlabel.makeSingleton());
		if (f == null)
			throw new IllegalArgumentException("No such function: " + objlabel);
		return f;
	}

	/**
	 * Returns the main code.
	 */
	public Function getMain() {
		return main;
	}

	/**
	 * Returns the nodes marked as ignorable.
	 */
	public Set<Node> getIgnorableNodes() {
		return ignorable_nodes;
	}

	/**
	 * Marks the given node as ignorable, meaning that it does not correspond
	 * directly to the give source code.
	 */
	public void addIgnorableNode(Node n) {
		ignorable_nodes.add(n);
	}

	/**
	 * Sets the main function. The main function must be included in the
	 * collection of all functions.
	 */
	public void setMain(Function main) {
		if (main == null)
			throw new NullPointerException();
		this.main = main;
	}

	/**
	 * Returns a string description of this flow graph.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Function f : functions) {
			if (f == main)
				b.append("<main> ");
			b.append(f).append('\n');
			for (BasicBlock k : f.getBlocks()) {
				b.append("  block ").append(k.getIndex()).append(':');
				if (f.getEntry() == k)
					b.append(" [entry]");
				if (f.getOrdinaryExit() == k)
					b.append(" [exit-ordinary]");
				if (f.getExceptionalExit() == k)
					b.append(" [exit-exceptional]");
				b.append('\n');
				for (Node n : k.getNodes())
					b.append("    node ").append(n.getIndex()).append(": ")
							.append(n).append(" (")
							.append(n.getSourceLocation()).append(")\n");
				b.append("    ->[");
				boolean first = true;
				for (BasicBlock s : k.getSuccessors()) {
					if (first)
						first = false;
					else
						b.append(',');
					b.append("block ").append(s.getIndex());
				}
				b.append("]\n");
			}
		}
		return b.toString();
	}

	/**
	 * Produces a Graphviz dot representation of this flow graph.
	 */
	public void toDot(PrintWriter pw) {
		pw.println("digraph {");
		pw.println("compound=true");
		for (Function f : functions)
			f.toDot(pw, false, f == main);
		pw.println("}");
		pw.close(); // TODO: nice to close here?
	}

	/**
	 * Sets the block orders in every function.
	 */
	public void setBlockOrders() {
		for (Function f : functions)
			f.setBlockOrders();
	}

	/**
	 * Returns the collection of load event handlers.
	 */
	public Collection<Function> getLoadEventHandlers() {
		return load_event_handlers;
	}

	/**
	 * Returns the collection of unload event handlers.
	 */
	public Collection<Function> getUnloadEventHandlers() {
		return unload_event_handlers;
	}

	/**
	 * Returns the collection of keyboard event handlers.
	 */
	public Collection<Function> getKeyboardEventHandlers() {
		return keyboard_event_handlers;
	}

	/**
	 * Returns the collection of mouse event handlers.
	 */
	public Collection<Function> getMouseEventHandlers() {
		return mouse_event_handlers;
	}

	/**
	 * Returns the collection of unknown event handlers.
	 */
	public Collection<Function> getUnknownEventHandlers() {
		return unknown_event_handlers;
	}
}