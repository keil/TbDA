package dk.brics.tajs.dependency.graph;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.DependencyAnalyzer;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.dependency.graph.visitor.DependencyVisitor;

public abstract class DependencyNode {

	private static Map<Label, Integer> i = new HashMap<Label, Integer>();

	/**
	 * Returns and increments the label counter
	 * 
	 * @return next label number
	 */
	protected static int nextNumber(Label label) {
		if (i.containsKey(label)) {
			i.put(label, i.get(label) + 1);
		} else {
			i.put(label, 0);
			return 0;
		}
		return i.get(label);
	}
	
	/**
	 * child nodes
	 */
	protected Set<DependencyNode> mChildNodes;
	/**
	 * parent nodes
	 */
	protected Set<DependencyNode> mParentNodes;

	/**
	 * 
	 */
	protected DependencyNode() {
		this.mChildNodes = new LinkedHashSet<DependencyNode>();
		this.mParentNodes = new LinkedHashSet<DependencyNode>();

		// add to analyser
		DependencyAnalyzer.dependencyNodes.add(this);
	}

	/**
	 * @param node
	 */
	protected void addChildNode(DependencyNode node) {
		this.mChildNodes.add(node);
	}

	/**
	 * @param node
	 */
	protected void addParentNode(DependencyNode node) {
		this.mParentNodes.add(node);
		node.addChildNode(this);
	}

	/**
	 * @return
	 */
	public Set<DependencyNode> getChildNodes() {
		return new LinkedHashSet<DependencyNode>(this.mChildNodes);
	}

	/**
	 * @return
	 */
	public Set<DependencyNode> getParentNodes() {
		return new LinkedHashSet<DependencyNode>(this.mParentNodes);
	}

	/**
	 * @param node
	 */
	public void addParent(DependencyNode node) {
		if (node != null)
			this.addParentNode(node);
	}

	/**
	 * @param reference
	 */
	public void addParent(DependencyGraphReference reference) {
		for (DependencyNode node : reference) {
			this.addParentNode(node);
		}
	}

	/**
	 * @return
	 */
	public DependencyGraphReference getReference() {
		return new DependencyGraphReference(this);
	}

	public Dependency getDependency() {
		DependencyVisitor visitor = new DependencyVisitor();
		accept(visitor);
		return visitor.getDependency();
	}

	/**
	 * @param visitor
	 */
	public abstract void accept(IDependencyGraphVisitor visitor);

	/**
	 * @return identifier
	 */
	public abstract String getIdentifier();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public abstract int hashCode();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public abstract boolean equals(Object obj);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
	}
}