package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.sortedEntries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Strings;

/**
 * Abstract object. Mutable.
 */
public final class Obj {

	private Map<String, Value> properties;
	private boolean writable_properties; // for copy-on-write

	private Value default_array_property; // represents all other possible
											// properties that are valid array
											// indices
	private Value default_nonarray_property; // represents all other possible
												// properties

	private Value internal_prototype; // the [[Prototype]] property
	private Value internal_value; // the [[Value]] property

	private Set<ScopeChain> scope; // the [[Scope]] property, null if empty
	private boolean writable_scope; // for copy-on-write

	private boolean scope_unknown; // if set, the value of scope is unknown (and
									// the 'scope' field is then not used)

	private static int number_of_objs_created;
	private static int number_of_makewritable_properties;

	private Obj() {
		number_of_objs_created++;
	}

	/**
	 * Creates a new abstract object as a copy of the given.
	 */
	public Obj(Obj x) {
		setTo(x);
		number_of_objs_created++;
	}

	/**
	 * Constructs an abstract object where all properties have unknown values.
	 */
	public static Obj makeUnknown() {
		Obj obj = new Obj();
		obj.properties = Collections.emptyMap();
		obj.writable_properties = false;
		obj.default_array_property = obj.default_nonarray_property = obj.internal_prototype = obj.internal_value = Value.makeUnknown(new Dependency(),
				new DependencyGraphReference());
		obj.scope_unknown = true;
		return obj;
	}

	/**
	 * Constructs an abstract object as a copy of this one but with object
	 * labels summarized.
	 */
	public Obj summarize(Summarized s) {
		Obj obj = new Obj();
		obj.properties = newMap();
		for (Map.Entry<String, Value> me : properties.entrySet())
			obj.properties.put(me.getKey(), me.getValue().summarize(s));
		obj.writable_properties = true;
		obj.default_array_property = default_array_property.summarize(s);
		obj.default_nonarray_property = default_nonarray_property.summarize(s);
		obj.internal_prototype = internal_prototype.summarize(s);
		obj.internal_value = internal_value.summarize(s);
		obj.scope_unknown = scope_unknown;
		if (scope != null)
			obj.scope = ScopeChain.summarize(scope, s);
		obj.writable_scope = true;
		return obj;
	}

	/**
	 * Replaces all definitely non-modified properties in this object by the
	 * corresponding properties of other.
	 */
	public void strengthenNonModifiedParts(Obj other) {
		Map<String, Value> newproperties = newMap();
		for (Map.Entry<String, Value> me : properties.entrySet()) {
			Value v = me.getValue();
			Value other_v = other.getProperty(me.getKey());
			if (other_v != null) {
				v = v.replaceIfNonModified(other_v);
			}
			newproperties.put(me.getKey(), v);
		}
		for (Map.Entry<String, Value> me : other.properties.entrySet())
			if (!newproperties.containsKey(me.getKey()))
				newproperties.put(me.getKey(), me.getValue());
		properties = newproperties;
		writable_properties = true;
		default_array_property = default_array_property.replaceIfNonModified(other.default_array_property);
		default_nonarray_property = default_nonarray_property.replaceIfNonModified(other.default_nonarray_property);
		internal_prototype = internal_prototype.replaceIfNonModified(other.internal_prototype);
		internal_value = internal_value.replaceIfNonModified(other.internal_value);
		if (scope_unknown && !other.scope_unknown) {
			scope = other.scope;
			scope_unknown = other.scope_unknown;
			writable_scope = other.writable_scope = false;
		}
	}

	/**
	 * Constructs an abstract object where all properties are absent and scope
	 * is set to empty.
	 */
	public static Obj makeAbsent() {
		Obj obj = new Obj();
		obj.properties = Collections.emptyMap();
		obj.default_nonarray_property = obj.default_array_property = obj.internal_prototype = obj.internal_value = Value.makeAbsent(new Dependency(),
				new DependencyGraphReference());
		return obj;
	}

	/**
	 * Constructs an abstract object where all properties are absent and scope
	 * is set to empty.
	 */
	public static Obj makeBottom() {
		Obj obj = new Obj();
		obj.properties = Collections.emptyMap();
		obj.default_nonarray_property = obj.default_array_property = obj.internal_prototype = obj.internal_value = Value.makeBottom(new Dependency(),
				new DependencyGraphReference());
		return obj;
	}

	/**
	 * Makes properties writable.
	 */
	private void makeWritableProperties() {
		if (writable_properties)
			return;
		properties = newMap(properties);
		writable_properties = true;
		number_of_makewritable_properties++;
	}

	/**
	 * Makes scope writable.
	 */
	private void makeWritableScope() {
		if (writable_scope)
			return;
		if (scope != null)
			scope = newSet(scope);
		else
			scope = null;
		writable_scope = true;
	}

	/**
	 * Returns the total number of Obj objects created.
	 */
	public static int getNumberOfObjsCreated() {
		return number_of_objs_created;
	}

	/**
	 * Resets the global counters.
	 */
	public static void reset() {
		number_of_objs_created = 0;
		number_of_makewritable_properties = 0;
	}

	/**
	 * Returns the total number of makeWritableProperties operations.
	 */
	public static int getNumberOfMakeWritablePropertiesCalls() {
		return number_of_makewritable_properties;
	}

	/**
	 * Returns the size of the properties map.
	 */
	public int getNumberOfProperties() {
		return properties.size();
	}

	/**
	 * Returns a measure of the vertical position of this lattice element within
	 * the lattice.
	 */
	public int getPosition() {
		int s = default_nonarray_property.getPosition() + default_array_property.getPosition() + internal_prototype.getPosition()
				+ internal_value.getPosition();
		for (Value v : properties.values())
			s += v.getPosition();
		return s; // ignoring the scope chain
	}

	/**
	 * Sets this object to be a copy of the given.
	 */
	public void setTo(Obj x) {
		default_nonarray_property = x.default_nonarray_property;
		default_array_property = x.default_array_property;
		internal_prototype = x.internal_prototype;
		internal_value = x.internal_value;
		if (Options.isCopyOnWriteDisabled()) {
			properties = newMap(x.properties);
			if (x.scope != null)
				scope = newSet(x.scope);
			else
				scope = null;
		} else {
			properties = x.properties;
			scope = x.scope;
			scope_unknown = x.scope_unknown;
			x.writable_properties = writable_properties = false;
			x.writable_scope = writable_scope = false;
		}
	}

	/**
	 * Clears modified flags for all values.
	 */
	public void clearModified() {
		Map<String, Value> new_properties = newMap();
		for (Map.Entry<String, Value> me : properties.entrySet())
			new_properties.put(me.getKey(), me.getValue().clearModified());
		properties = new_properties;
		writable_properties = true;
		default_nonarray_property = default_nonarray_property.clearModified();
		default_array_property = default_array_property.clearModified();
		internal_prototype = internal_prototype.clearModified();
		internal_value = internal_value.clearModified();
	}

	/**
	 * Returns the value of the given property, or null if not available. Does
	 * not consider the defaults.
	 */
	public Value getProperty(String propertyname) {
		return properties.get(propertyname);
	}

	/**
	 * Returns the value of the given property, considering defaults if
	 * necessary. Never returns null. May return 'unknown'.
	 */
	public Value readProperty(String propertyname) {
		Value v = getProperty(propertyname);
		if (v == null)
			if (Strings.isArrayIndex(propertyname))
				v = getDefaultArrayProperty();
			else
				v = getDefaultNonArrayProperty();
		return v;
	}

	/**
	 * Sets the given property.
	 */
	public void setProperty(String propertyname, Value v) {
		makeWritableProperties();
		properties.put(propertyname, v);
	}

	/**
	 * Returns all property names, excluding the defaults and internal
	 * properties.
	 */
	public Set<String> getPropertyNames() {
		return properties.keySet();
	}

	/**
	 * Returns all properties, excluding the defaults and internal properties.
	 * Should not be modified by the caller.
	 */
	public Map<String, Value> getAllProperties() {
		return properties;
	}

	/**
	 * Returns the value of the default array property.
	 */
	public Value getDefaultArrayProperty() {
		return default_array_property;
	}

	/**
	 * Sets the value of the default array property.
	 */
	public void setDefaultArrayProperty(Value v) {
		default_array_property = v;
	}

	/**
	 * Returns the value of the default non-array property.
	 */
	public Value getDefaultNonArrayProperty() {
		return default_nonarray_property;
	}

	/**
	 * Sets the value of the default non-array property.
	 */
	public void setDefaultNonArrayProperty(Value v) {
		default_nonarray_property = v;
	}

	/**
	 * Checks whether some array property is unknown, including the default.
	 */
	public boolean isSomeArrayPropertyUnknown() {
		if (default_array_property.isUnknown())
			return true;
		for (Map.Entry<String, Value> me : properties.entrySet())
			if (me.getValue().isUnknown() && Strings.isArrayIndex(me.getKey()))
				return true;
		return false;
	}

	/**
	 * Checks whether some non-array property is unknown, including the default.
	 */
	public boolean isSomeNonArrayPropertyUnknown() {
		if (default_nonarray_property.isUnknown())
			return true;
		for (Map.Entry<String, Value> me : properties.entrySet())
			if (me.getValue().isUnknown() && !Strings.isArrayIndex(me.getKey()))
				return true;
		return false;
	}

	/**
	 * Returns the value of the internal [[Value]] property.
	 */
	public Value getInternalValue() {
		return internal_value;
	}

	/**
	 * Sets the internal [[Value]] property.
	 */
	public void setInternalValue(Value v) {
		internal_value = v;
	}

	/**
	 * Returns the value of the internal [[Prototype]] property.
	 */
	public Value getInternalPrototype() {
		return internal_prototype;
	}

	/**
	 * Sets the internal [[Prototype]] property.
	 */
	public void setInternalPrototype(Value v) {
		internal_prototype = v;
	}

	/**
	 * Returns the value of the internal [[Scope]] property. Assumed to be
	 * non-'unknown'. Should not be modified by the caller.
	 */
	public Set<ScopeChain> getScopeChain() {
		// if (scope_unknown)
		// throw new
		// RuntimeException("Calling getScopeChain when scope is 'unknown'");
		if (scope != null)
			return scope;
		else
			return Collections.emptySet();
	}

	/**
	 * Sets the internal [[Scope]] property.
	 */
	public void setScopeChain(Set<ScopeChain> scope) {
		if (scope.isEmpty())
			scope = null;
		this.scope = scope;
		writable_scope = true;
		scope_unknown = false;
	}

	/**
	 * Adds to the internal [[Scope]] property.
	 * 
	 * @return true if changed
	 */
	public boolean addToScopeChain(Set<ScopeChain> newscope) {
		if (scope_unknown)
			throw new RuntimeException("Calling addToScopeChain when scope is 'unknown'");
		makeWritableScope();
		if (scope == null)
			scope = newSet();
		return scope.addAll(newscope);
	}

	/**
	 * Returns true if internal [[Scope]] property is 'unknown'.
	 */
	public boolean isScopeChainUnknown() {
		return scope_unknown;
	}

	/**
	 * Returns true if some property is 'unknown'. Internal properties are
	 * ignored.
	 */
	public boolean isSomePropertyUnknown() {
		if (default_array_property.isUnknown() || default_nonarray_property.isUnknown())
			return true;
		for (Map.Entry<String, Value> me : properties.entrySet())
			if (me.getValue().isUnknown())
				return true;
		return false;
	}

	/**
	 * Replaces all occurrences of oldlabel by newlabel. Does not change
	 * modified flags. Ignores 'unknown' values.
	 */
	public void replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel, Map<ScopeChain, ScopeChain> cache) {
		Map<String, Value> newproperties = newMap();
		for (Map.Entry<String, Value> me : properties.entrySet())
			newproperties.put(me.getKey(), me.getValue().replaceObjectLabel(oldlabel, newlabel));
		properties = newproperties;
		if (scope != null) {
			Set<ScopeChain> newscope = newSet();
			for (ScopeChain e : scope)
				newscope.add(e.replaceObjectLabel(oldlabel, newlabel, cache));
			scope = newscope;
		}
		default_nonarray_property = default_nonarray_property.replaceObjectLabel(oldlabel, newlabel);
		default_array_property = default_array_property.replaceObjectLabel(oldlabel, newlabel);
		internal_prototype = internal_prototype.replaceObjectLabel(oldlabel, newlabel);
		internal_value = internal_value.replaceObjectLabel(oldlabel, newlabel);
		writable_properties = writable_scope = true;
	}

	/**
	 * Checks whether the given abstract object is equal to this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Obj))
			return false;
		Obj x = (Obj) obj;
		if ((scope == null) != (x.scope == null))
			return false;
		return properties.equals(x.properties) && (scope == null || scope.equals(x.scope)) && (scope_unknown == x.scope_unknown)
				&& default_nonarray_property.equals(x.default_nonarray_property) && default_array_property.equals(x.default_array_property)
				&& internal_prototype.equals(x.internal_prototype) && internal_value.equals(x.internal_value);
	}

	/**
	 * Returns a description of the changes from the old object to this object.
	 * It is assumed that the old object is less than this object and that no
	 * explicit property has been moved to default_other_property or
	 * default_array_property.
	 */
	public void diff(Obj old, StringBuilder b) {
		for (Map.Entry<String, Value> me : sortedEntries(properties)) {
			Value v = old.properties.get(me.getKey());
			if (v == null)
				b.append("\n        new property: ").append(me.getKey());
			else if (!me.getValue().equals(v)) {
				b.append("\n        changed property: ").append(me.getKey()).append(": ");
				me.getValue().diff(v, b);
			}
		}
		if (!default_array_property.equals(old.default_array_property)) {
			b.append("\n      changed default_array_property: ");
			default_array_property.diff(old.default_array_property, b);
		}
		if (!default_nonarray_property.equals(old.default_nonarray_property)) {
			b.append("\n      changed default_nonarray_property: ");
			default_nonarray_property.diff(old.default_nonarray_property, b);
		}
		if (!internal_prototype.equals(old.internal_prototype)) {
			b.append("\n      changed internal_prototype: ");
			internal_prototype.diff(old.internal_prototype, b);
		}
		if (!internal_value.equals(old.internal_value)) {
			b.append("\n      changed internal_value: ");
			internal_value.diff(old.internal_value, b);
		}
	}

	/**
	 * Computes the hash code for this abstract object.
	 */
	@Override
	public int hashCode() {
		return properties.hashCode() * 3 + (scope != null ? scope.hashCode() * 7 : 0) + (scope_unknown ? 13 : 0) + internal_prototype.hashCode() * 11
				+ internal_value.hashCode() * 113 + default_nonarray_property.hashCode() * 23 + default_array_property.hashCode() * 31;
	}

	/**
	 * Produces a string description of this abstract object.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		boolean any = false;
		b.append("{");
		for (Map.Entry<String, Value> me : sortedEntries(properties)) {
			Value v = me.getValue();
			if (v.isMaybeValueOrUnknown()) {
				if (any)
					b.append(",");
				else
					any = true;
				b.append(Strings.escape(me.getKey())).append(":").append(v);
			}
		}
		if (default_array_property.isMaybeValueOrUnknown()) {
			if (any)
				b.append(",");
			else
				any = true;
			b.append("[[DefaultArray]]=").append(default_array_property);
		}
		if (default_nonarray_property.isMaybeValueOrUnknown()) {
			if (any)
				b.append(",");
			else
				any = true;
			b.append("[[DefaultNonArray]]=").append(default_nonarray_property);
		}
		if (internal_prototype.isMaybeValueOrUnknown()) {
			if (any)
				b.append(",");
			else
				any = true;
			b.append("[[Prototype]]=").append(internal_prototype);
		}
		if (internal_value.isMaybeValueOrUnknown()) {
			if (any)
				b.append(",");
			else
				any = true;
			b.append("[[Value]]=").append(internal_value);
		}
		if (scope != null || scope_unknown) {
			if (any)
				b.append(",");
			else
				any = true;
			b.append("[[Scope]]=");
			if (scope != null)
				b.append(scope);
			else
				b.append("?");
		}
		b.append("}");
		return b.toString();
	}

	/**
	 * prints dependencies construct of current object
	 * 
	 * @return string
	 */
	public HashMap<String, Dependency> analyzeDependencies() {
		HashMap<String, Dependency> analysisResults = new HashMap<String, Dependency>();

		for (Map.Entry<String, Value> me : sortedEntries(properties)) {
			Value v = me.getValue();
			analysisResults.put(me.getKey(), v.getDependency());
		}
		return analysisResults;
	}

	/**
	 * prints dependency graph references of current object
	 * 
	 * @return string
	 */
	public HashMap<String, DependencyGraphReference> analyzeDependencyReferences() {
		HashMap<String, DependencyGraphReference> analysisResults = new HashMap<String, DependencyGraphReference>();

		for (Map.Entry<String, Value> me : sortedEntries(properties)) {
			Value v = me.getValue();
			analysisResults.put(me.getKey(), v.getDependencyGraphReference());
		}
		return analysisResults;
	}

	/**
	 * Prints the maybe modified properties. Internal properties are ignored.
	 */
	public String printModified() {
		StringBuilder b = new StringBuilder();
		for (Map.Entry<String, Value> me : sortedEntries(properties)) {
			Value v = me.getValue();
			if (v.isMaybeModified() && v.isMaybeValueOrUnknown())
				b.append("\n    ").append(Strings.escape(me.getKey())).append(": ").append(v);
		}
		if ((default_array_property.isMaybeModified()) && default_array_property.isMaybeValueOrUnknown())
			b.append("\n    ").append("[[DefaultArray]] = ").append(default_array_property);
		if ((default_nonarray_property.isMaybeModified()) && default_nonarray_property.isMaybeValueOrUnknown())
			b.append("\n    ").append("[[DefaultNonArray]] = ").append(default_nonarray_property);
		if (internal_prototype.isMaybeModified() && internal_prototype.isMaybeValueOrUnknown())
			b.append("\n    [[Prototype]] = ").append(internal_prototype);
		if (internal_value.isMaybeModified() && internal_value.isMaybeValueOrUnknown())
			b.append("\n    [[Value]] = ").append(internal_value);
		return b.toString();
	}

	/**
	 * Returns the set of all object labels used in this abstract object
	 * 'unknown' values are ignored.
	 */
	public Set<ObjectLabel> getAllObjectLabels() {
		Set<ObjectLabel> objlabels = newSet();
		for (Value v : properties.values())
			objlabels.addAll(v.getObjectLabels());
		objlabels.addAll(default_array_property.getObjectLabels());
		objlabels.addAll(default_nonarray_property.getObjectLabels());
		objlabels.addAll(internal_prototype.getObjectLabels());
		objlabels.addAll(internal_value.getObjectLabels());
		if (scope != null)
			for (ScopeChain e : scope)
				for (ObjectLabel l : e)
					objlabels.add(l);
		return objlabels;
	}

	/**
	 * Joins all properties into this object during recovery. Internal
	 * properties are ignored. The modified flags are reset.
	 * 
	 * @return true if changed
	 */
	public boolean joinAllProperties(Obj src, Summarized s) {
		boolean changed = false;
		for (String propertyname : src.properties.keySet()) {
			Value dst_v = properties.get(propertyname);
			if (dst_v == null || !dst_v.isMaybeModified())
				changed |= joinProperty(src, propertyname, s);
		}
		Value src_v_nonarray = src.getDefaultNonArrayProperty().summarize(s).clearModified();
		if (src_v_nonarray.isUnknown())
			throw new RuntimeException("Unexpected 'unknown' value?!");
		Value dst_v_nonarray = getDefaultNonArrayProperty();
		if (!dst_v_nonarray.isMaybeModified()) {
			Value new_dst_v_nonarray;
			if (dst_v_nonarray.isUnknown())
				new_dst_v_nonarray = src_v_nonarray;
			else
				new_dst_v_nonarray = dst_v_nonarray.join(src_v_nonarray);
			if (!new_dst_v_nonarray.equals(dst_v_nonarray)) {
				setDefaultNonArrayProperty(new_dst_v_nonarray);
				changed = true;
			}
		}
		Value src_v_array = src.getDefaultArrayProperty().summarize(s).clearModified();
		if (src_v_array.isUnknown())
			throw new RuntimeException("Unexpected 'unknown' value?!");
		Value dst_v_array = getDefaultArrayProperty();
		if (!dst_v_array.isMaybeModified()) {
			Value new_dst_v_array;
			if (dst_v_array.isUnknown())
				new_dst_v_array = src_v_array;
			else
				new_dst_v_array = dst_v_array.join(src_v_array);
			if (!new_dst_v_array.equals(dst_v_array)) {
				setDefaultArrayProperty(new_dst_v_array);
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Joins all non-array properties including the default into this object
	 * during recovery. The modified flag is reset.
	 * 
	 * @return true if changed
	 */
	public boolean joinAllNonArrayProperties(Obj src, Summarized s) {
		boolean changed = false;
		for (String propertyname : src.properties.keySet())
			if (!Strings.isArrayIndex(propertyname)) {
				Value dst_v = properties.get(propertyname);
				if (dst_v == null || !dst_v.isMaybeModified())
					changed |= joinProperty(src, propertyname, s);
			}
		Value src_v = src.getDefaultNonArrayProperty().summarize(s).clearModified();
		if (src_v.isUnknown())
			throw new RuntimeException("Unexpected 'unknown' value?!");
		Value dst_v = getDefaultNonArrayProperty();
		if (dst_v.isMaybeModified())
			throw new RuntimeException("Attempt to join into modified property?!");
		Value new_dst_v;
		if (dst_v.isUnknown())
			new_dst_v = src_v;
		else
			new_dst_v = dst_v.join(src_v);
		if (!new_dst_v.equals(dst_v)) {
			setDefaultNonArrayProperty(new_dst_v);
			changed = true;
		}
		return changed;
	}

	/**
	 * Joins all array properties including the default into this object during
	 * recovery. The modified flag is reset.
	 * 
	 * @return true if changed
	 */
	public boolean joinAllArrayProperties(Obj src, Summarized s) {
		boolean changed = false;
		for (String propertyname : src.properties.keySet())
			if (Strings.isArrayIndex(propertyname)) {
				Value dst_v = properties.get(propertyname);
				if (dst_v == null || !dst_v.isMaybeModified())
					changed |= joinProperty(src, propertyname, s);
			}
		Value src_v = src.getDefaultArrayProperty().summarize(s).clearModified();
		if (src_v.isUnknown())
			throw new RuntimeException("Unexpected 'unknown' value?!");
		Value dst_v = getDefaultArrayProperty();
		if (dst_v.isMaybeModified())
			throw new RuntimeException("Attempt to join into modified property?!");
		Value new_dst_v;
		if (dst_v.isUnknown())
			new_dst_v = src_v;
		else
			new_dst_v = dst_v.join(src_v);
		if (!new_dst_v.equals(dst_v)) {
			setDefaultArrayProperty(new_dst_v);
			changed = true;
		}
		return changed;
	}

	/**
	 * Joins the internal [[Value]] property into this object during recovery.
	 * The modified flag is reset.
	 * 
	 * @return true if changed
	 */
	public boolean joinInternalValue(Obj src, Summarized s) {
		Value src_v = src.getInternalValue().summarize(s).clearModified();
		if (src_v.isUnknown())
			throw new RuntimeException("Unexpected 'unknown' value?!");
		Value dst_v = getInternalValue();
		if (dst_v.isMaybeModified())
			throw new RuntimeException("Attempt to join into modified property?!");
		Value new_dst_v;
		if (dst_v.isUnknown())
			new_dst_v = src_v;
		else
			new_dst_v = dst_v.join(src_v);
		boolean changed = !new_dst_v.equals(dst_v);
		if (changed)
			setInternalValue(new_dst_v);
		return changed;
	}

	/**
	 * Joins the internal [[Prototype]] property into this object during
	 * recovery. The modified flag is reset.
	 * 
	 * @return true if changed
	 */
	public boolean joinInternalPrototype(Obj src, Summarized s) {
		Value src_v = src.getInternalPrototype().summarize(s).clearModified();
		if (src_v.isUnknown())
			throw new RuntimeException("Unexpected 'unknown' value?!");
		Value dst_v = getInternalPrototype();
		if (dst_v.isMaybeModified())
			throw new RuntimeException("Attempt to join into modified property?!");
		Value new_dst_v;
		if (dst_v.isUnknown())
			new_dst_v = src_v;
		else
			new_dst_v = dst_v.join(src_v);
		boolean changed = !new_dst_v.equals(dst_v);
		if (changed)
			setInternalPrototype(new_dst_v);
		return changed;
	}

	/**
	 * Joins the internal [[Scope]] property into this object.
	 * 
	 * @return true if changed
	 */
	public boolean joinInternalScope(Obj src, Summarized s) {
		makeWritableScope();
		boolean changed;
		Set<ScopeChain> src_v = src.getScopeChain();
		if (s != null)
			src_v = ScopeChain.summarize(src_v, s);
		if (isScopeChainUnknown()) {
			setScopeChain(newSet(src_v));
			changed = true;
		} else
			changed = addToScopeChain(src_v);
		return changed;
	}

	/**
	 * Joins the given property into this object during recovery. The modified
	 * flag is reset.
	 * 
	 * @return true if changed
	 */
	public boolean joinProperty(Obj src, String propertyname, Summarized s) {
		Value src_v = src.readProperty(propertyname).summarize(s).clearModified();
		if (src_v.isUnknown())
			throw new RuntimeException("Unexpected 'unknown' value?!");
		Value dst_v = readProperty(propertyname);
		if (dst_v.isMaybeModified())
			throw new RuntimeException("Attempt to join into modified property?!");
		Value new_dst_v;
		if (dst_v.isUnknown())
			new_dst_v = src_v;
		else
			new_dst_v = dst_v.join(src_v);
		setProperty(propertyname, new_dst_v);
		return !new_dst_v.equals(dst_v);
	}

	/**
	 * Reduces this object according to the given existing object. If the given
	 * object is null, the entire object is reduced.
	 */
	public void reduce(Obj obj) {
		// ##################################################
		Dependency dependency = new Dependency();
		DependencyGraphReference reference = new DependencyGraphReference();
		// ##################################################

		if (obj != null) {
			// reduce those properties that are unknown in obj
			Map<String, Value> new_properties = newMap();
			for (Map.Entry<String, Value> me : properties.entrySet()) {
				String propertyname = me.getKey();
				Value v = me.getValue();
				dependency.join(v.getDependency());
				reference.join(v.getDependencyGraphReference());

				if (!obj.readProperty(propertyname).isUnknown())
					new_properties.put(propertyname, v);
			}
			properties = new_properties;
			writable_properties = true;
			if (obj.default_array_property.isUnknown())
				default_array_property = Value.makeUnknown(dependency, reference);
			if (obj.default_nonarray_property.isUnknown())
				default_nonarray_property = Value.makeUnknown(dependency, reference);
			if (obj.internal_value.isUnknown())
				internal_value = Value.makeUnknown(dependency, reference);
			if (obj.internal_prototype.isUnknown())
				internal_prototype = Value.makeUnknown(dependency, reference);
			if (obj.scope_unknown) {
				scope = null;
				scope_unknown = true;
				writable_scope = false;
			}
		} else {
			// reduce all properties
			properties = Collections.emptyMap();
			writable_properties = false;
			default_array_property = default_nonarray_property = internal_value = internal_prototype = Value.makeUnknown(dependency, reference);
			scope = null;
			scope_unknown = true;
			writable_scope = false;
		}
	}
}
