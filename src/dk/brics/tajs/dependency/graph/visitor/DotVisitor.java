package dk.brics.tajs.dependency.graph.visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyRootNode;

public class DotVisitor implements IDependencyGraphVisitor {

	/**
	 * node counter
	 */
	private int counter = 0;

	/**
	 * buffer for main output
	 */
	private StringBuffer mBuffer;
	/**
	 * buffer for node output
	 */
	private StringBuffer mBufferNodes;
	/**
	 * buffer for edge output
	 */
	private StringBuffer mBufferEdges;

	/**
	 * maps node->label
	 */
	private Map<DependencyNode, String> mMapping;

	/**
	 * visited nodes
	 */
	private Set<DependencyNode> mVisited;

	/**
	 * 
	 */
	public DotVisitor() {
		mVisited = new HashSet<DependencyNode>();
		mMapping = new HashMap<DependencyNode, String>();

		mBuffer = new StringBuffer();
		mBufferNodes = new StringBuffer();
		mBufferEdges = new StringBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		generateHeader();
		generateNodes();
		generateEdges();
		generateFooter();

		return mBuffer.toString();
	}

	public void flush(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(this.toString());
		writer.flush();
		writer.close();
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
		addRoot(getLabel(node), "ROOT");
		mVisited.add(node);

		for (DependencyNode n : node.getChildNodes()) {
			addRootEdge(getLabel(node), getLabel(n));

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
		addRoot(getLabel(node), node.toString());
		mVisited.add(node);

		for (DependencyNode n : node.getChildNodes()) {
			addNodeEdge(getLabel(node), getLabel(n));

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
		addNode(getLabel(node), node.toString());
		mVisited.add(node);

		for (DependencyNode n : node.getChildNodes()) {
			addNodeEdge(getLabel(node), getLabel(n));

			if (!visited(n)) {
				n.accept(this);
			}
		}
	}

	/**
	 * @param node
	 * @return label of current node
	 */
	private String getLabel(DependencyNode node) {
		if (!mMapping.containsKey(node)) {
			mMapping.put(node, "node_" + counter++);
		}
		return mMapping.get(node);
	}

	/**
	 * @param id
	 * @param label
	 */
	private void addRoot(String id, String label) {
		mBufferNodes.append("   " + id + " " + "[label=\"" + label
				+ "\"  shape=box];\n");
	}

	/**
	 * @param id
	 * @param label
	 */
	private void addNode(String id, String label) {
		mBufferNodes.append("   " + id + " [label=\"" + label + "\"];\n");
	}

	/**
	 * @param id0
	 * @param id1
	 */
	private void addRootEdge(String id0, String id1) {
		mBufferEdges.append("   " + id0 + " -> " + id1 + " [style=dotted];\n");
	}

	/**
	 * @param id0
	 * @param id1
	 */
	private void addNodeEdge(String id0, String id1) {
		mBufferEdges.append("   " + id0 + " -> " + id1 + "[dir=back];\n");
	}

	/**
	 * generate header
	 */
	private void generateHeader() {
		mBuffer.append("digraph dependencygraph {\n");
	}

	/**
	 * generate nodes
	 */
	private void generateNodes() {
		mBuffer.append(mBufferNodes.toString());
	}

	/**
	 * generate edges
	 */
	private void generateEdges() {
		mBuffer.append(mBufferEdges.toString());
	}

	/**
	 * generate footer
	 */
	private void generateFooter() {
		mBuffer.append("}");
	}
}