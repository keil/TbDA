package dk.brics.tajs.flowgraph;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import dk.brics.tajs.lattice.Summarized;
import dk.brics.tajs.options.Options;

/**
 * Abstract object.
 */
public final class ObjectLabel implements Comparable<ObjectLabel> { // TODO: canonicalize?

	/**
	 * Source location used for native functions.
	 */
	public static final SourceLocation initial_source = new SourceLocation(0, "<initial state>");
	
	/**
	 * Object kinds.
	 */
	public static enum Kind {
		OBJECT {@Override public String toString() { return "Object"; }}, 
		FUNCTION {@Override public String toString() { return "Function"; }}, 
		ARRAY {@Override public String toString() { return "Array"; }}, 
		REGEXP {@Override public String toString() { return "RegExp"; }}, 
		DATE {@Override public String toString() { return "Date"; }}, 
		STRING {@Override public String toString() { return "String"; }}, 
		NUMBER {@Override public String toString() { return "Number"; }}, 
		BOOLEAN {@Override public String toString() { return "Boolean"; }}, 
		ERROR {@Override public String toString() { return "Error"; }}, 
		MATH {@Override public String toString() { return "Math"; }}, 
		ACTIVATION {@Override public String toString() { return "activation"; }},
		ARGUMENTS {@Override public String toString() { return "arguments"; }},
	}
	
	/**
	 * If set, this abstract object represents a single concrete object.
	 * (If not set, it can represent any number of concrete objects.)
	 */
	private boolean singleton;
	
	private Kind kind; // [[Class]]
	
	private Node node; // non-null for user defined non-Function objects
	
	private NativeObject nativeobject; // non-null for native objects
	
	private Function function; // non-null for user defined functions
	
	private ObjectLabel(NativeObject nativeobject, Node node, Function function, Kind kind) {
		this.nativeobject = nativeobject;
		this.node = node;
		this.function = function;
		this.kind = kind;
		if (!Options.isRecencyDisabled())
			this.singleton = true;
	}
	
	/**
	 * Constructs a new object label for a user defined non-function object.
	 * If {@link Options#isRecencyDisabled()} is disabled, the object label 
	 * represents a single concrete object (otherwise, it may represent any 
	 * number of concrete objects).
	 */
	public ObjectLabel(Node n, Kind kind) {
		this(null, n, null, kind);
	}
	
	/**
	 * Constructs a new object label for a user defined function object.
	 * If {@link Options#isRecencyDisabled()} is disabled, the object label 
	 * represents a single concrete object (otherwise, it may represent any 
	 * number of concrete objects).
	 */
	public ObjectLabel(Function f) {
		this(null, null, f, Kind.FUNCTION);
	}
	
	/**
	 * Constructs a new object label for a native object. 
	 * If {@link Options#isRecencyDisabled()} is disabled, the object label 
	 * represents a single concrete object (otherwise, it may represent any 
	 * number of concrete objects).
	 */
	public ObjectLabel(NativeObject nativeobject, Kind kind) {
		this(nativeobject, null, null, kind);
	}
	
	/**
	 * Returns the object label kind.
	 */
	public Kind getKind() {
		return kind;
	}
	
	/**
	 * Returns the source location.
	 */
	public SourceLocation getSourceLocation() {
		if (node != null)
			return node.getSourceLocation();
		else if (function != null)
			return function.getSourceLocation();
		else
			return initial_source;
	}	

	/**
	 * Returns true if this object label represents a native function.
	 */
	public boolean isNative() {
		return nativeobject != null;
	}

	/**
	 * Returns the descriptor for this object label.
	 * It is assumed that this object label represents a native function.
	 */
	public NativeObject getNativeObjectID() {
		return nativeobject;
	}
	
	public Function getFunction() {
		return function;
	}
	
	/**
	 * Returns true if this object label definitely represents a single concrete object.
	 */
	public boolean isSingleton() {
		return singleton;
	}
	
	/**
	 * Returns the summary object label associated with this singleton object label.
	 */
	public ObjectLabel makeSummary() {
		if (!singleton)
			throw new IllegalStateException("attempt to obtain summary of non-singleton");
		ObjectLabel obj =  new ObjectLabel(nativeobject, node, function, kind);
		obj.singleton = false;
		return obj;
	}

	/**
	 * Returns the singleton object label associated with this object label, or this object if that is singleton.
	 */
	public ObjectLabel makeSingleton() {
		if (singleton)
			return this;
		return new ObjectLabel(nativeobject, node, function, kind);
	}

	/**
	 * Produces a string representation of this object label.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		if (singleton)
			b.append('@');
		else
			b.append('*');
		if (function != null) {
			String f = function.getName();
			if (f == null)
				f = "<anonymous>";
			b.append("function ").append(f).append("#fun").append(function.getIndex());
		} else if (nativeobject != null) 
			b.append("native ").append(nativeobject);
		else
			b.append(describe()).append("#node").append(node.getIndex());
		return b.toString();
	}
	
	/**
	 * Produces a human readable description of this object label.
	 */
	public String describe() {
		if (nativeobject != null)
			return "Primitive";
		else if (function != null)
			return "Function";
		else
			return kind.toString();
	}

	/**
	 * Checks whether the given object label is equal to this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ObjectLabel))
			return false;
		ObjectLabel x = (ObjectLabel) obj;
		if ((nativeobject == null) != (x.nativeobject == null))
			return false;
		return (nativeobject == null || nativeobject.equals(x.nativeobject)) && 
		function == x.function && node == x.node &&
		singleton == x.singleton && kind == x.kind; 
	}

	/**
	 * Computes the hash code for this object label.
	 */
	@Override
	public int hashCode() {
		return (nativeobject != null ? nativeobject.toString().hashCode() : 0) + 
		(function != null ? function.hashCode() : 0) +
		(node != null ? node.getIndex() : 0) +
		(singleton ? 123 : 0) +
		kind.ordinal() * 117; // avoids using enum hashcodes
	}

	/**
	 * Compares this and the given object label.
	 * The source location is used as primary key, and toString is used as secondary key.
	 */
	@Override
	public int compareTo(ObjectLabel objlabel) {
		int c = getSourceLocation().compareTo(objlabel.getSourceLocation());
		if (c != 0)
			return c;
		if (equals(objlabel))
			return 0;
		return toString().compareTo(objlabel.toString());
	}
	
	/**
	 * Summarizes the given set of object labels.
	 * May return the given set unchanged rather than a new set.
	 */
	public static Set<ObjectLabel> summarize(Set<ObjectLabel> objlabels, Summarized s) {
		boolean changed = false;
		for (ObjectLabel ol : objlabels)
			if (ol.isSingleton() && s.isMaybeSummarized(ol)) {
				changed = true;
				break;
			}
		if (!changed)
			return objlabels;
		Set<ObjectLabel> new_objs = newSet();
		for (ObjectLabel ol : objlabels) {
			if (ol.isSingleton()) {
				if (s.isMaybeSummarized(ol)) {
					new_objs.add(ol.makeSummary());
					changed = true;
					if (!s.isDefinitelySummarized(ol))
						new_objs.add(ol);
				} else
					new_objs.add(ol);
			} else
				new_objs.add(ol);
		}
		return new_objs;
	}
}
