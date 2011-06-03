package dk.brics.tajs.flowgraph;

import static dk.brics.tajs.util.Collections.newList;

import java.io.PrintWriter;
import java.util.*;

import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Strings;

/**
 * Function sub-graph.
 */
public class Function {
	
	private FlowGraph flow_graph;
	
	private int index = -1;

	private Collection<BasicBlock> blocks;
	
	private SourceLocation location;
	
	private BasicBlock entry;
	
	private BasicBlock ordinary_exit;
	
	private BasicBlock exceptional_exit;
	
	private String name; // null for anonymous functions
	
	private List<String> parameter_names;

    private Function outer_function;

	/**
	 * Constructs a new function.
	 * The node set is initially empty, and the entry/exit nodes are not set.
	 * The function name is null for anonymous functions.
	 */
	public Function(String name, List<String> parameter_names, FlowGraph flow_graph, SourceLocation location) {
		this.name = name;
		this.parameter_names = parameter_names;
		this.flow_graph = flow_graph;
		this.location = location;
		blocks = newList();
	}
	
	/**
	 * Returns the flow graph containing this function.
	 */
	public FlowGraph getFlowGraph() {
		return flow_graph;
	}
	
	/**
	 * Returns true if this is the main function.
	 */
	public boolean isMain() {
		return flow_graph.getMain() == this;
	}

	/**
	 * Sets the function index.
	 * Called when the function is added to a flow graph.
	 */
	void setIndex(int index) {
		if (this.index != -1)
			throw new IllegalArgumentException("Function already belongs to a flow graph: " + getSourceLocation());
		this.index = index;
	}

	/**
	 * Returns the function index.
	 * The function index uniquely identifies the function within the flow graph.
	 */
	public int getIndex() {
		if (index == -1)
			throw new RuntimeException("Function has not been added to flow graph: " + getSourceLocation());
		return index;
	}

	/**
	 * Sets the entry block.
	 */
	public void setEntry(BasicBlock entry) {
		this.entry = entry;
	}
	
	/**
	 * Sets the ordinary exit block.
	 * This block must consist of a single ReturnNode.
	 */
	public void setOrdinaryExit(BasicBlock ordinary_exit) {
		this.ordinary_exit = ordinary_exit;
	}
	
	/**
	 * Sets the exceptional exit block.
	 * This block must consist of a single ExceptionalReturnNode.
	 */
	public void setExceptionalExit(BasicBlock exceptional_exit) {
		this.exceptional_exit = exceptional_exit;
	}
	
	/**
	 * Returns the function name, or null if anonymous.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the list of parameter names.
	 */
	public List<String> getParameterNames() {
		return parameter_names;
	}

	/**
	 * Returns the collection of blocks.
	 */
	public Collection<BasicBlock> getBlocks() {
		return blocks;
	}
	
	/**
	 * Returns the entry block.
	 */
	public BasicBlock getEntry() {
		return entry;
	}

	/**
	 * Returns the ordinary exit block.
	 */
	public BasicBlock getOrdinaryExit() {
		return ordinary_exit;
	}

	/**
	 * Returns the exceptional exit block.
	 */
	public BasicBlock getExceptionalExit() {
		return exceptional_exit;
	}

	/**
	 * Returns a source location for this function.
	 */
	public SourceLocation getSourceLocation() {
		return location;
	}

    /**
     * Returns true iff the function has an outer function.
     */
    public boolean hasOuterFunction() {
        return outer_function != null;
    }

    /**
     * Returns the outer function or null if there is no outer function.
     */
    public Function getOuterFunction() {
        return outer_function;
    }

    /**
     * Sets the outer function.
     */
    public void setOuterFunction(Function outerFunction) {
        this.outer_function = outerFunction;
    }

    /**
	 * Returns a string description of this function.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("function");
		if (name != null)
			b.append(' ').append(Strings.escape(name));
		b.append('(');
		boolean first = true;
		for (String s : parameter_names) {
			if (first)
				first = false;
			else
				b.append(',');
			b.append(Strings.escape(s));
		}
		b.append(')');
		return b.toString();
	}
	
	/**
	 * Produces a Graphviz dot representation of this function.
	 */
	public void toDot(PrintWriter pw, boolean standalone, boolean main) {
        List<String> colors = Collections.newList();
        colors.add("blue1");
        colors.add("green1");
        colors.add("red1");
        int colorIndex = 0;

		if (standalone) {
			pw.println("digraph {");
		}
		else {
			pw.println("subgraph " + "cluster" + index + " {");
			pw.println("label=\"" + (main ? "<main> " : "") + toString() + "\\n" + location
                    + (outer_function != null ? "\\nouter: " + (outer_function.getName() == null ? "<main>" : outer_function.getName()) : "")  +  "\";");
			pw.println("labelloc=\"t\";");
			pw.println("fontsize=18;");
		}
		pw.println("rankdir=\"TD\"");
		Set<BasicBlock> labels = new HashSet<BasicBlock>();
		pw.println("BB_entry" + index + "[shape=none,label=\"\"];");
		pw.println("BB_entry" + index + " -> BB" + entry.getIndex() 
				+ " [tailport=s, headport=n, headlabel=\"    " + entry.getIndex() + "\"]");
		labels.add(entry);
		for (BasicBlock b : blocks) {
			b.toDot(pw, false);
			for (BasicBlock bs : b.getSuccessors()) {
                String color = colors.get(colorIndex);
                colorIndex = (colorIndex + 1) % colors.size();
				pw.print("BB" + b.getIndex() + " -> BB" + bs.getIndex() 
						+ " [tailport=s, headport=n, color=" + color);
				if (!labels.contains(bs)) {
					labels.add(bs);
					pw.print(", headlabel=\"      " + bs.getIndex() /*+ (bs.hasOrder() ? "[" +bs.getOrder() + "]" : "")*/ +"\"");
				}
				pw.println("]");
			}
			BasicBlock ex = b.getExceptionHandler();
			if (ex != b && b.canThrowExceptions()) {
				pw.print("BB" + b.getIndex() + " -> BB" + ex.getIndex() 
						+ " [tailport=s, headport=n, color=gray");
				if (!labels.contains(ex)) {
					labels.add(ex);
					pw.print(", headlabel=\"      " + ex.getIndex() +"\"");
				}
				pw.println("]");
			}
		}
		pw.println("}");
		if (standalone) {
			pw.flush();
		}
	}
	
	/**
	 * Sets the block orders.
	 */
	public void setBlockOrders() {
		int max = 0;
		for (BasicBlock b : blocks) {
			if (!b.getNodes().isEmpty()) {
				int x = b.getSourceLocation().getLineNumber();
				b.setOrder(x);
				if (x > max)
					max = x;
			}
		}
		if (entry != null)
			entry.setOrder(0);
		if (ordinary_exit != null)
			ordinary_exit.setOrder(max+1);
		if (exceptional_exit != null)
			exceptional_exit.setOrder(max+2);
	}
}
