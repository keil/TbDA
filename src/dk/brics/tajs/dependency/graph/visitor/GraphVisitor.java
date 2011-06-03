package dk.brics.tajs.dependency.graph.visitor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyRootNode;
import dk.brics.tajs.flowgraph.SourceLocation;

public class GraphVisitor implements IDependencyGraphVisitor {

	/**
	 * output
	 */
	private TreeMap<Integer, List<String>> mMapping;

	/**
	 * visited nodes
	 */
	private Set<DependencyNode> mVisited;

	/**
	 * 
	 */
	public GraphVisitor() {
		mMapping = new TreeMap<Integer, List<String>>();
		mVisited = new HashSet<DependencyNode>();
	}

	/**
	 * @param location
	 * @param value
	 */
	private void append(SourceLocation location, String value) {
		if (!mMapping.containsKey(location.getLineNumber()))
			mMapping.put(location.getLineNumber(), new LinkedList<String>());
		mMapping.get(location.getLineNumber()).add(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		Iterator<Integer> iterator = mMapping.keySet().iterator();
		while (iterator.hasNext()) {
			int key = iterator.next();

			buffer.append(String.format("%-" + 10 + "s", "line" + key));
			for (String item : mMapping.get(key)) {
				buffer.append(" #" + item + " ");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visited
	 * (dk.brics.tajs.dependency.graph.DependencyNode)
	 */
	public boolean visited(DependencyNode node) {
		return mVisited.contains(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visit
	 * (dk.brics.tajs.dependency.graph.nodes.DependencyRootNode)
	 */
	@Override
	public void visit(DependencyRootNode node) {
		append(new SourceLocation(0, ""), node.toString());

		mVisited.add(node);
		for (DependencyNode n : node.getChildNodes()) {
			if (!visited(n)) {
				n.accept(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visit
	 * (dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode)
	 */
	@Override
	public void visit(DependencyObjectNode node) {
		append(node.getSourceLocation(), node.toString());

		mVisited.add(node);
		for (DependencyNode n : node.getChildNodes()) {
			if (!visited(n)) {
				n.accept(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visit
	 * (dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode)
	 */
	@Override
	public void visit(DependencyExpressionNode node) {
		append(node.getSourceLocation(), node.toString());

		mVisited.add(node);
		for (DependencyNode n : node.getChildNodes()) {
			if (!visited(n)) {
				n.accept(this);
			}
		}
	}
}