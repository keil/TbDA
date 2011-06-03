package dk.brics.tajs.dependency.graph.nodes;

import java.util.HashMap;
import java.util.Map;

import dk.brics.tajs.dependency.graph.DependencyLabel;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.Label;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.dependency.graph.visitor.ToStringVisitor;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.SourceLocation;

public class DependencyExpressionNode extends DependencyNode {

	
	private static Map<Label, Integer> i = new HashMap<Label, Integer>();
	
	
	
	
	/**
	 * Counter, to number trace labels
	 */
	//private static int i = 0;

	/**
	 * Returns and increments the counter
	 * 
	 * @return next trace number
	 */
	private static int nextNumber(Label label) {
		if(i.containsKey(label)) {
			i.put(label, i.get(label) + 1);
		} else {
			i.put(label, 0);
			return 0;
		}
		return i.get(label);
		
		
		
		//return DependencyExpressionNode.i++;
	}
		
	//  D_{exp1}
	
	////////////////////////////////////

	private DependencyLabel mLabel;
	
	// TODO
	private int id;

	public DependencyExpressionNode(DependencyLabel label) {
		this.mLabel = label;
		
		// TODO
		this.id = DependencyExpressionNode.nextNumber(label.getLabel());
	}

	// TODO
	public String getID() {
		//return "D" + id + mLabel +"";
		//return "D" + mLabel +"";
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("D");
		buffer.append("[");
		buffer.append(mLabel);
		buffer.append("@");
		buffer.append(this.id);
		buffer.append("]");
		
		return buffer.toString();
	}
	
	
	public Label getLabel() {
		return mLabel.getLabel();
	}

	public SourceLocation getSourceLocation() {
		return mLabel.getSourceLocation();
	}

	public Node getNode() {
		return mLabel.getNode();
	}

	public DependencyLabel getDependencyLabel() {
		return mLabel;
	}

	@Override
	public void accept(IDependencyGraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		
		// D[if#45]={[if#45] D[:=#12],D[=#7]}

		// D[if'45]={[if#45] D[:=#12],D[=#7]}
		
		buffer.append("D");
		buffer.append("[");
		buffer.append(mLabel);
		buffer.append("@");
		buffer.append(this.id);
		buffer.append("]");
		
		
		
		
		
//		buffer.append(getID() + "&sum;s");
//		
		buffer.append("=");
		buffer.append("{");
		buffer.append(mLabel);
		buffer.append(" ");

		for (DependencyNode n : this.getParentNodes()) {
			if(n instanceof DependencyExpressionNode) {
				buffer.append(((DependencyExpressionNode) n).getID());
			} else {
				buffer.append(n);	
			}
			buffer.append(" ");
		}
		buffer.append("}");
		
		return buffer.toString();
//
//		ToStringVisitor visitor = new ToStringVisitor();
//		accept(visitor);
//		return buffer.toString() + visitor.toString();
	}

	@Override
	public int hashCode() {
//		HashCodeVisitor visitor = new HashCodeVisitor();
//		accept(visitor);
//		int parent = visitor.getHashCode();

		final int prime = 31;
		int result = 1;
		result = prime * result + ((mLabel == null) ? 0 : mLabel.hashCode());
		
		result = prime * result + id;
		
//		result = prime * result + parent;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependencyExpressionNode other = (DependencyExpressionNode) obj;
		if (mLabel == null) {
			if (other.mLabel != null)
				return false;
		} else if (!mLabel.equals(other.mLabel))
			return false;
		return true;
	}
}