package dk.brics.tajs.flowgraph;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Strings;

/**
 * Sequence of nodes.
 * Must be non-empty.
 * Has a unique entry node and proceeds through the sequence unless exceptions are thrown. 
 */
public class BasicBlock {

	private int index = -1;
	
	private int order = -1;

	private List<Node> nodes;
	
	private Collection<BasicBlock> successors;
	
	private BasicBlock exception_handler;
	
	private Function function;

    private int[] live_variables;
	
	/**
	 * Constructs a new initially empty block of nodes.
	 */
	public BasicBlock(Function function) {
		if (function == null)
			throw new NullPointerException();
		this.function = function;
		successors = newSet();
		nodes = newList();
	}
	
	/**
	 * Adds a successor.
	 */
	public void addSuccessor(BasicBlock succ) {
		if (succ == null)
			throw new NullPointerException();
		successors.add(succ);
	}

	/**
	 * Returns the successors of this block.
	 */
	public Collection<BasicBlock> getSuccessors() {
		return successors;
	}
	
	/**
	 * Returns the single successor block.
	 * @throws RuntimeException if not exactly one successor
	 */
	public BasicBlock getSingleSuccessor() {
		if (successors.size() != 1) {
            for (BasicBlock bb : successors) {
                System.out.println("--> " + bb.getIndex());
            }
			throw new RuntimeException("Expected exactly one successor of basic block."
                    + " Size: " + successors.size()
                    + " Index: " + index);
        }
		return successors.iterator().next();
	}

	/**
	 * Sets the block order.
	 */
	void setOrder(int order) {
		this.order = order;
	}
	
	/**
	 * Returns the block order.
	 */
	public int getOrder() {
		if (order == -1)
			throw new RuntimeException("Block order has not been set");
		return order;
	}
	
	/**
	 * Checks whether the block order has been set.
	 */
	public boolean hasOrder() {
		return order != -1;
	}

	/**
	 * Sets the block index.
	 * Called when the block is added to a flow graph.
	 */
	void setIndex(int index) {
		if (this.index != -1)
			throw new IllegalArgumentException("Block already belongs to a flow graph: " + getSourceLocation());
		this.index = index;
	}

	/**
	 * Returns the block index.
	 * The block index uniquely identifies the block within the flow graph.
	 */
	public int getIndex() {
		if (index == -1)
			throw new RuntimeException("Block has not been added to flow graph: " + getSourceLocation());
		return index;
	}
	
	/**
	 * Checks whether this block has been added to a flow graph.
	 */
	public boolean isAdded() {
		return index != -1;
	}

	/**
	 * Adds a node to this block.
	 */
	public void addNode(Node n) {
		if (n == null)
			throw new NullPointerException();
		nodes.add(n);
		n.setBlock(this);
	}
	
	/**
	 * Returns the sequence of nodes.
	 */
	public List<Node> getNodes() {
		return nodes;
	}
	
    /**
     * Returns the single node in the basic block.
     */
    public Node getSingleNode() {
        if (nodes.size() != 1)
            throw new RuntimeException("Basic block does not contain exactly one node");
        return nodes.get(0);
    }

	/**
	 * Returns the first node.
	 */
	public Node getFirstNode() {
		if (nodes.isEmpty())
			throw new RuntimeException("Basic block is empty");
		return nodes.get(0);
	}
	
	/**
	 * Returns the last node.
	 */
	public Node getLastNode() {
		if (nodes.isEmpty())
			throw new RuntimeException("Basic block is empty");
		return nodes.get(nodes.size() - 1);
	}
	
	/**
	 * Checks whether this is a function entry block.
	 */
	public boolean isEntry() {
		return function.getEntry() == this;
	}

	/**
	 * Checks whether this is a function ordinary exit block.
	 */
	public boolean isOrdinaryExit() {
		return function.getOrdinaryExit() == this;
	}

	/**
	 * Checks whether this is a function exceptional exit block.
	 */
	public boolean isExceptionalExit() {
		return function.getExceptionalExit() == this;
	}

	/**
	 * Returns the function containing this block.
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * Returns the (sorted) live variables for this block.
	 */
    public int[] getLiveVariables() {
        if (live_variables == null) {
            throw new RuntimeException("ERROR: live_variables not initialized for basic block " + getIndex() + " Forgot to compute live variables?");
        }
        return live_variables;
    }

    /**
     * Sets (and sorts) the live variables for this block.
     */
    public void setLiveVariables(int[] variables) {
    	Arrays.sort(variables);
        this.live_variables = variables;
    }

	/**
	 * Returns the source location.
	 */
	public SourceLocation getSourceLocation() {
		return getFirstNode().getSourceLocation();
	}
	
	/**
	 * Returns the exception handler block, or null if not set.
	 */
	public BasicBlock getExceptionHandler() {
		return exception_handler;
	}
	
	/**
	 * Sets the exception handler node.
	 */
	public void setExceptionHandler(BasicBlock exception_handler) {
		this.exception_handler = exception_handler;
	}

	/**
	 * Adds an edge from this block to the given block.
	 */
	public void addEdgeTo(BasicBlock dst) {
		if (dst == null)
			throw new NullPointerException();
		successors.add(dst);
	}
	
	/**
	 * Returns true if this block contains a node that may throw exceptions.
	 */
	public boolean canThrowExceptions() {
		for (Node n : nodes)
			if (n.canThrowExceptions())
				return true;
		return false;
	}
	
	/**
	 * Returns a string description of this block.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("block ").append(index).append(":");
		for (Node n : getNodes())
			s.append("\n  node ").append(n.getIndex()).append(": ").append(n);
		return s.toString();
	}
	
	/**
	 * Produces a Graphviz dot representation of this block.
	 */
	public void toDot(PrintWriter pw, boolean standalone) {
		if (standalone) {
			pw.println("digraph {");
			pw.println("rankdir=\"TD\"");
		}
		pw.print("BB" + index + " [shape=record label=\"{"); // "{" is needed to get the record to align vertically
		boolean first = true;
		for (Node n : getNodes()) {
			if (first)
				first = false;
			else
				pw.print('|');
			pw.print(n.getIndex() + ": " + Strings.escape(n.toString()));
		}
        if (live_variables != null && Options.isDebugEnabled()) {
            pw.print("| [[live: ");
            if (live_variables.length == 0) {
                pw.print("-");    
            } else {
                for (int i = 0; i < live_variables.length; i++) {
                    pw.print("v" + live_variables[i]);
                    if (i != live_variables.length - 1) {
                        pw.print(", ");
                    }
                }
            }
            pw.print("]]");
        }
		pw.print("}\" ] " + "\n");
		if (standalone) {
			pw.println("}");
			pw.close();
		}
	}
}
