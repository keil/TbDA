package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.DependencyObject;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphReference;
import dk.brics.tajs.dependency.interfaces.IDependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Strings;

/**
 * Abstract value. Value objects are immutable.
 */
/**
 * @author Matthias
 *
 */

public final class Value implements Undef, Null, Bool, Num, Str, IDependency<Value>, IDependencyGraphReference<Value> {

	// TODO: ? NUM_ZERO, STR_EMPTY
	private final static int BOOL_TRUE = 0x0000001; // if changing these bits,
													// update computePosition
	private final static int BOOL_FALSE = 0x0000002;
	private final static int UNDEF = 0x0000004;
	private final static int NULL = 0x0000008;
	private final static int STR_UINT = 0x0000010;
	private final static int STR_NOTUINT = 0x0000020;
	private final static int MODIFIED = 0x0000040;
	private final static int ABSENT = 0x0000080;
	private final static int NUM_NAN = 0x0000100;
	private final static int NUM_INF = 0x0000200;
	private final static int NUM_UINT = 0x0000400;
	private final static int NUM_NOTUINT = 0x0000800;
	private final static int ATTR_DONTENUM = 0x0010000;
	private final static int ATTR_NOTDONTENUM = 0x0020000;
	private final static int ATTR_READONLY = 0x0040000;
	private final static int ATTR_NOTREADONLY = 0x0080000;
	private final static int ATTR_DONTDELETE = 0x0100000;
	private final static int ATTR_NOTDONTDELETE = 0x0200000;
	private final static int UNKNOWN = 0x1000000; // TODO: make unknown=bottom?

	private final static int BOOL_ANY = BOOL_TRUE | BOOL_FALSE;
	private final static int STR_ANY = STR_UINT | STR_NOTUINT;
	private final static int NUM_ANY = NUM_NAN | NUM_INF | NUM_UINT
			| NUM_NOTUINT;
	private final static int ATTR_DONTENUM_ANY = ATTR_DONTENUM
			| ATTR_NOTDONTENUM;
	private final static int ATTR_READONLY_ANY = ATTR_READONLY
			| ATTR_NOTREADONLY;
	private final static int ATTR_DONTDELETE_ANY = ATTR_DONTDELETE
			| ATTR_NOTDONTDELETE;
	private final static int ATTR_ANY = ATTR_DONTENUM_ANY | ATTR_READONLY_ANY
			| ATTR_DONTDELETE_ANY;
	private final static int PRIMITIVE = UNDEF | NULL | BOOL_ANY | NUM_ANY
			| STR_ANY;

	private static Map<Value, WeakReference<Value>> value_cache = new WeakHashMap<Value, WeakReference<Value>>();
	private static int value_cache_hits;
	private static int value_cache_misses;

	private static Map<Set<ObjectLabel>, WeakReference<Set<ObjectLabel>>> objset_cache = new WeakHashMap<Set<ObjectLabel>, WeakReference<Set<ObjectLabel>>>();
	private static int objset_cache_hits;
	private static int objset_cache_misses;

	private static Value theBottom = reallyMakeBottom(new Dependency());
	private static Value theUndef = reallyMakeUndef(null);
	private static Value theNull = reallyMakeNull(null);
	private static Value theBoolTrue = reallyMakeBool(true, new Dependency());
	private static Value theBoolFalse = reallyMakeBool(false, new Dependency());
	private static Value theBoolAny = reallyMakeBool(null, new Dependency());
	private static Value theStrAny = reallyMakeAnyStr(new Dependency());
	private static Value theNumAny = reallyMakeAnyNum(new Dependency());
	private static Value theNumUInt = reallyMakeAnyUInt(new Dependency());
	private static Value theNumNotUInt = reallyMakeAnyNotUInt(new Dependency());
	private static Value theNumNotNaNInf = reallyMakeAnyNumNotNaNInf(new Dependency());
	private static Value theNumNaN = reallyMakeNumNaN(new Dependency());
	private static Value theNumInf = reallyMakeNumInf(new Dependency());
	private static Value theAbsent = reallyMakeAbsent(new Dependency());
	private static Value theAbsentModified = reallyMakeAbsentModified(new Dependency());
	private static Value theUnknown = reallyMakeUnknown(new Dependency());

	/*
	 * Representation invariant: !((flags & STR_ANY) != 0 && str != null) &&
	 * !((flags & NUM_ANY) != 0 && num != null) && !(object_labels != null &&
	 * object_labels.isEmpty()) && !(num != null && Double.isNaN(num)) &&
	 * !((flags & UNKNOWN) != 0 && ((flags & ~UNKNOWN) != 0 || str != null ||
	 * num != null || !object_labels.isEmpty()))
	 */

	private int flags;
	private Double num;
	private String str;
	
	/**
	 * value dependencies
	 */
	private Dependency mDependency;
	
	/**
	 * dependency graph reference
	 */
	private DependencyGraphReference mDependencyGraphReference = new DependencyGraphReference();

	/**
	 * Possible values regarding object references.
	 */
	private Set<ObjectLabel> object_labels; // TODO: other representation than
											// Set<ObjectLabel>? (e.g. bit
											// vector?)

	/**
	 * A measure of the value's position in the value lattice. Set in
	 * canonicalization.
	 */
	private int position;

	private static boolean canonicalizing; // set during canonicalization

	/**
	 * Constructs a new value with given dependency object.
	 */
	private Value(DependencyObject dependencyObject) {
		this(dependencyObject.getDependency());
	}
	
	/**
	 * Constructs a new bottom value with given dependency.
	 */
	private Value(Dependency dependency) {
		flags = 0;
		num = null;
		str = null;
		object_labels = null;
		mDependency = new Dependency(dependency);
	}

	/**
	 * Constructs a shallow clone of the given value object.
	 */
	private Value(Value v) {
		flags = v.flags;
		num = v.num;
		str = v.str;
		object_labels = v.object_labels;
		mDependencyGraphReference = v.getDependencyGraphReference();
		mDependency = new Dependency(v.getDependency());
	}
	
	/**
	 * Constructs a empty value. 
	 */
	private Value() {
		this(new Dependency());
	}
		
	private static Value canonicalize(Value v) {
		if (Options.isDebugEnabled()) { // checking representation invariants
			if (((v.flags & STR_ANY) != 0 && v.str != null)
					|| ((v.flags & NUM_ANY) != 0 && v.num != null)
					|| (v.num != null && Double.isNaN(v.num))
					|| (v.object_labels != null && v.object_labels.isEmpty())
					|| ((v.flags & UNKNOWN) != 0 && ((v.flags & ~UNKNOWN) != 0
							|| v.str != null || v.num != null || (v.object_labels != null && !v.object_labels
							.isEmpty()))))
				throw new RuntimeException("Invalid value: " + v);
		}
		canonicalizing = true;
		if (v.object_labels != null) {
			WeakReference<Set<ObjectLabel>> ref1 = objset_cache
					.get(v.object_labels);
			Set<ObjectLabel> so = ref1 != null ? ref1.get() : null;
			if (so == null) {
				objset_cache.put(v.object_labels,
						new WeakReference<Set<ObjectLabel>>(v.object_labels));
				objset_cache_misses++;
			} else {
				v.object_labels = so;
				objset_cache_hits++;
			}
		}
		WeakReference<Value> ref2 = value_cache.get(v);
		Value cv = ref2 != null ? ref2.get() : null;
		if (cv == null) {
			cv = v;
			value_cache.put(v, new WeakReference<Value>(v));
			value_cache_misses++;
			v.computePosition();
		} else
			value_cache_hits++;
		canonicalizing = false;
		return cv;
	}

	private void computePosition() {
		position = (num != null ? 1 : 0) + (str != null ? 1 : 0)
				+ (object_labels != null ? object_labels.size() : 0)
				+ ((flags & BOOL_TRUE) >> 0) // 0x1
				+ ((flags & BOOL_FALSE) >> 1) // 0x2
				+ ((flags & UNDEF) >> 2) // 0x4
				+ ((flags & NULL) >> 3) // 0x8
				+ ((flags & STR_UINT) >> 4) // 0x10
				+ ((flags & STR_NOTUINT) >> 5) // 0x20
				+ ((flags & ABSENT) >> 7) // 0x80
				+ ((flags & NUM_NAN) >> 8) // 0x100
				+ ((flags & NUM_INF) >> 9) // 0x200
				+ ((flags & NUM_UINT) >> 10) // 0x400
				+ ((flags & NUM_NOTUINT) >> 11); // 0x800
	}

	/**
	 * Returns a measure of the vertical position of this lattice element within
	 * the lattice.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Clears the canonicalization caches.
	 */
	public static void clearCaches() {
		value_cache.clear();
		objset_cache.clear();
	}

	/**
	 * Returns the value cache size.
	 */
	public static int getValueCacheSize() {
		return value_cache.size();
	}

	/**
	 * Returns the number of value cache misses.
	 */
	public static int getNumberOfValueCacheMisses() {
		return value_cache_misses;
	}

	/**
	 * Returns the number of value cache hits.
	 */
	public static int getNumberOfValueCacheHits() {
		return value_cache_hits;
	}

	/**
	 * Returns the object set cache size.
	 */
	public static int getObjectSetCacheSize() {
		return objset_cache.size();
	}

	/**
	 * Returns the number of object set cache misses.
	 */
	public static int getNumberOfObjectSetCacheMisses() {
		return objset_cache_misses;
	}

	/**
	 * Returns the number of object set cache hits.
	 */
	public static int getNumberOfObjectSetCacheHits() {
		return objset_cache_hits;
	}

	/**
	 * Resets the cache numbers.
	 */
	public static void reset() {
		value_cache_misses = 0;
		value_cache_hits = 0;
		objset_cache_misses = 0;
		objset_cache_hits = 0;
		clearCaches();
	}

	/**
	 * Checks whether this value is marked as maybe modified.
	 */
	public boolean isMaybeModified() {
		if (Options.isModifiedDisabled())
			return true;
		return (flags & MODIFIED) != 0;
	}

	private static Value reallyMakeBottom(Dependency dependency) {
		return canonicalize(new Value(dependency));
	}

	/**
	 * Constructs the bottom value.
	 */
	public static Value makeBottom(Dependency dependency) {
		return theBottom.joinDependency(dependency);
	}

	/**
	 * Returns true if this is the bottom value. Note that this is <i>not</i>
	 * the same as {@link #isNoValue()}.
	 */
	public boolean isBottom() {
		return flags == 0 && num == null && str == null
				&& object_labels == null;
	}

	/**
	 * Constructs a value as a copy of the given value but marked as maybe
	 * modified.
	 */
	public Value joinModified() {
		if (isMaybeModified())
			return this;
		Value r = new Value(this);
		r.flags |= MODIFIED;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of the given value but marked as definitely
	 * non-modified.
	 */
	public Value restrictToNonModified() {
		if (!isMaybeModified())
			return this;
		Value r = new Value(this);
		r.flags &= ~MODIFIED;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of the given value but marked as definitely
	 * present.
	 */
	public Value restrictToNonAbsent() {
		if (!isMaybeModified())
			return this;
		Value r = new Value(this);
		r.flags &= ~ABSENT;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of the given value but marked as definitely
	 * non-modified.
	 */
	public Value clearModified() {
		if (!isMaybeModified())
			return this;
		Value r = new Value(this);
		r.flags &= ~MODIFIED;
		return canonicalize(r);
	}

	/**
	 * Constructs the absent value.
	 */
	public static Value makeAbsent(Dependency dependency) {
		return theAbsent.joinDependency(dependency);
	}

	/**
	 * Constructs the absent modified value.
	 */
	public static Value makeAbsentModified(Dependency dependency) {
		return theAbsentModified.joinDependency(dependency);
	}

	/**
	 * Constructs the unknown value.
	 */
	public static Value makeUnknown(Dependency dependency) {
		return theUnknown.joinDependency(dependency);
	}

	/**
	 * Constructs a value as a copy of this value but definitely present.
	 */
	public Value clearAbsent() {
		if (isNotAbsent())
			return this;
		Value r = new Value(this);
		r.flags &= ~ABSENT;
		return canonicalize(r);
	}

	/**
	 * Returns true if this value belongs to a maybe absent property.
	 */
	public boolean isMaybeAbsent() {
		return (flags & ABSENT) != 0;
	}

	/**
	 * Returns true if this value belongs to a definitely present property.
	 */
	public boolean isNotAbsent() {
		return (flags & ABSENT) == 0
				&& ((flags & PRIMITIVE) != 0 || num != null || str != null || object_labels != null);
	}

	/**
	 * Returns true if this value is 'unknown'.
	 */
	public boolean isUnknown() {
		return (flags & UNKNOWN) != 0;
	}

	/**
	 * Constructs a value as a copy of the given value but marked as maybe
	 * absent.
	 */
	public Value joinAbsent() {
		if (isMaybeAbsent())
			return this;
		else {
			Value r = new Value(this);
			r.flags |= ABSENT;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of the given value but marked as maybe
	 * absent and marks it as maybe modified if the resulting value is different
	 * from this value.
	 */
	public Value joinAbsentWithModified() {
		if (isMaybeAbsent())
			return this;
		else {
			Value r = new Value(this);
			r.flags |= ABSENT | MODIFIED;
			return canonicalize(r);
		}
	}

	private static Value reallyMakeAbsent(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags |= ABSENT;
		return canonicalize(r);
	}

	private static Value reallyMakeAbsentModified(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags |= ABSENT | MODIFIED;
		return canonicalize(r);
	}

	private static Value reallyMakeUnknown(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags |= UNKNOWN;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of the given value but with all attributes
	 * definitely not set.
	 */
	public Value removeAttributes() {
		Value r = new Value(this);
		r.flags &= ~ATTR_ANY;
		r.flags |= ATTR_NOTDONTDELETE | ATTR_NOTDONTENUM | ATTR_NOTREADONLY;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of this value but with attributes set as in
	 * the given value.
	 */
	public Value setAttributes(Value from) {
		Value r = new Value(this);
		r.flags &= ~ATTR_ANY;
		r.flags |= from.flags & ATTR_ANY;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of this value but with no attribute
	 * information.
	 */
	public Value bottomAttributes() {
		Value r = new Value(this);
		r.flags &= ~ATTR_ANY;
		return canonicalize(r);
	}

	/**
	 * Returns true is this value belongs to a property which definitely has
	 * DontEnum set.
	 */
	public boolean isDontEnum() {
		return (flags & ATTR_DONTENUM_ANY) == ATTR_DONTENUM;
	}

	/**
	 * Returns true is this value belongs to a property which maybe has DontEnum
	 * set.
	 */
	public boolean isMaybeDontEnum() {
		return (flags & ATTR_DONTENUM) != 0;
	}

	/**
	 * Returns true is this value belongs to a property which definitely does
	 * not have DontEnum set.
	 */
	public boolean isNotDontEnum() {
		return (flags & ATTR_DONTENUM_ANY) == ATTR_NOTDONTENUM;
	}

	/**
	 * Returns true is this value belongs to a property which maybe does not
	 * have DontEnum set.
	 */
	public boolean isMaybeNotDontEnum() {
		return (flags & ATTR_NOTDONTENUM) != 0;
	}

	/**
	 * Returns true if this value has DontEnum information.
	 */
	public boolean hasDontEnum() {
		return (flags & ATTR_DONTENUM_ANY) != 0;
	}

	/**
	 * Constructs a value as a copy of this value but with DoneEnum definitely
	 * set.
	 */
	public Value setDontEnum() {
		if (isDontEnum())
			return this;
		else {
			Value r = new Value(this);
			r.flags &= ~ATTR_DONTENUM_ANY;
			r.flags |= ATTR_DONTENUM;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but with DoneEnum definitely
	 * not set.
	 */
	public Value setNotDontEnum() {
		if (isNotDontEnum())
			return this;
		else {
			Value r = new Value(this);
			r.flags &= ~ATTR_DONTENUM_ANY;
			r.flags |= ATTR_NOTDONTENUM;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but with DoneEnum maybe not
	 * set.
	 */
	public Value joinNotDontEnum() {
		if (isMaybeNotDontEnum())
			return this;
		else {
			Value r = new Value(this);
			r.flags |= ATTR_NOTDONTENUM;
			return canonicalize(r);
		}
	}

	/**
	 * Returns true is this value belongs to a property which definitely has
	 * DontDelete set.
	 */
	public boolean isDontDelete() {
		return (flags & ATTR_DONTDELETE_ANY) == ATTR_DONTDELETE;
	}

	/**
	 * Returns true is this value belongs to a property which maybe has
	 * DontDelete set.
	 */
	public boolean isMaybeDontDelete() {
		return (flags & ATTR_DONTDELETE) != 0;
	}

	/**
	 * Returns true is this value belongs to a property which definitely does
	 * not have DontDelete set.
	 */
	public boolean isNotDontDelete() {
		return (flags & ATTR_DONTDELETE_ANY) == ATTR_NOTDONTDELETE;
	}

	/**
	 * Returns true is this value belongs to a property which maybe does not
	 * have DontDelete set.
	 */
	public boolean isMaybeNotDontDelete() {
		return (flags & ATTR_NOTDONTDELETE) != 0;
	}

	/**
	 * Returns true if this value has DontDelete information.
	 */
	public boolean hasDontDelete() {
		return (flags & ATTR_DONTDELETE_ANY) != 0;
	}

	/**
	 * Constructs a value as a copy of this value but with DoneDelete definitely
	 * set.
	 */
	public Value setDontDelete() {
		if (isDontDelete())
			return this;
		else {
			Value r = new Value(this);
			r.flags &= ~ATTR_DONTDELETE_ANY;
			r.flags |= ATTR_DONTDELETE;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but with DoneDelete definitely
	 * not set.
	 */
	public Value setNotDontDelete() {
		if (isNotDontDelete())
			return this;
		else {
			Value r = new Value(this);
			r.flags &= ~ATTR_DONTDELETE_ANY;
			r.flags |= ATTR_NOTDONTDELETE;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but with DontDelete maybe not
	 * set.
	 */
	public Value joinNotDontDelete() {
		if (isMaybeNotDontDelete())
			return this;
		else {
			Value r = new Value(this);
			r.flags |= ATTR_NOTDONTDELETE;
			return canonicalize(r);
		}
	}

	/**
	 * Returns true is this value belongs to a property which definitely has
	 * ReadOnly set.
	 */
	public boolean isReadOnly() {
		return (flags & ATTR_READONLY_ANY) == ATTR_READONLY;
	}

	/**
	 * Returns true is this value belongs to a property which maybe has ReadOnly
	 * set.
	 */
	public boolean isMaybeReadOnly() {
		return (flags & ATTR_READONLY) != 0;
	}

	/**
	 * Returns true is this value belongs to a property which definitely does
	 * not have ReadOnly set.
	 */
	public boolean isNotReadOnly() {
		return (flags & ATTR_READONLY_ANY) == ATTR_NOTREADONLY;
	}

	/**
	 * Returns true is this value belongs to a property which maybe does not
	 * have ReadOnly set.
	 */
	public boolean isMaybeNotReadOnly() {
		return (flags & ATTR_NOTREADONLY) != 0;
	}

	/**
	 * Returns true if this value has ReadOnly information.
	 */
	public boolean hasReadOnly() {
		return (flags & ATTR_READONLY_ANY) != 0;
	}

	/**
	 * Constructs a value as a copy of this value but with ReadOnly definitely
	 * set.
	 */
	public Value setReadOnly() {
		if (isReadOnly())
			return this;
		else {
			Value r = new Value(this);
			r.flags &= ~ATTR_READONLY_ANY;
			r.flags |= ATTR_READONLY;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but with ReadOnly definitely
	 * not set.
	 */
	public Value setNotReadOnly() {
		if (isNotReadOnly())
			return this;
		else {
			Value r = new Value(this);
			r.flags &= ~ATTR_READONLY_ANY;
			r.flags |= ATTR_NOTREADONLY;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but with ReadOnly maybe not
	 * set.
	 */
	public Value joinNotReadOnly() {
		if (isMaybeNotReadOnly())
			return this;
		else {
			Value r = new Value(this);
			r.flags |= ATTR_NOTREADONLY;
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but with the given attributes.
	 */
	public Value setAttributes(boolean dontenum, boolean dontdelete,
			boolean readonly) {
		Value r = new Value(this);
		r.flags &= ~ATTR_ANY;
		if (dontdelete)
			r.flags |= ATTR_DONTDELETE;
		else
			r.flags |= ATTR_NOTDONTDELETE;
		if (readonly)
			r.flags |= ATTR_READONLY;
		else
			r.flags |= ATTR_NOTREADONLY;
		if (dontenum)
			r.flags |= ATTR_DONTENUM;
		else
			r.flags |= ATTR_NOTDONTENUM;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as the join of this value and the given value.
	 */
	public Value join(Value v) {
		return join(v, false);
	}

	/**
	 * Constructs a value as the join of this value and the given value and
	 * marks it as maybe modified if the resulting value is different from this
	 * value.
	 */
	public Value joinWithModified(Value v) {
		return join(v, true);
	}

	/**
	 * Constructs a value as the join of this value and the given value.
	 * 
	 * @param set_modified
	 *            if true, the resulting value is marked as maybe modified if
	 *            different from this value.
	 */
	public Value join(Value v, boolean set_modified) {
		if (v == this)
			return this;
		Value r = new Value(this);
		if (r.joinMutable(v)) {
			if (set_modified)
				r.flags |= MODIFIED;
			return canonicalize(r);
		} else
			return this;
	}

	/**
	 * Constructs a value as the join of the given collection of values.
	 */
	public static Value join(Iterable<Value> values) {
		Value r = new Value(new Dependency());
		for (Value v : values)
			r.joinMutable(v);
		return canonicalize(r);
	}

	/**
	 * Constructs a value as the join of the given collection of values.
	 */
	public static Value join(Value... values) {
		Value r = new Value(new Dependency());
		for (Value v : values)
			r.joinMutable(v);
		return canonicalize(r);
	}

	/**
	 * Joins the given value into this one.
	 * 
	 * @return true if this value is modified
	 */
	private boolean joinMutable(Value v) {
		// if (isUnknown() != v.isUnknown())
		// throw new
		// IllegalArgumentException("Attempt to join unknown with non-unknown");
		if (isUnknown())
			if (v.isUnknown())
				return false;
			else {
				flags = v.flags;
				num = v.num;
				str = v.str;
				object_labels = v.object_labels;
				mDependency.join(v.getDependency());
				mDependencyGraphReference.join(v.getDependencyGraphReference());
				return true;
			}
		else if (v.isUnknown())
			return false;

		boolean modified = false;

		// dependencies
		if(!mDependency.equals(v.getDependency())) {
			mDependency.join(v.getDependency());
			modified = true;
		}

		// dependency graph references
		if(!mDependencyGraphReference.equals(v.getDependencyGraphReference())) {
			mDependencyGraphReference.join(v.getDependencyGraphReference());
			modified = true;
		}

		// flags
		if ((flags | v.flags) != flags) {
			modified = true;
			flags |= v.flags;
		}
		// numbers
		if (isMaybeSingleNum() && v.isMaybeSingleNum()) {
			if (!num.equals(v.num)) {
				modified = true;
				updateNumFlags(num);
				updateNumFlags(v.num);
			}
		} else if (isMaybeSingleNum()) {
			modified = true;
			if (!v.isNotNum())
				updateNumFlags(num);
		} else if (v.isMaybeSingleNum()) {
			modified = true;
			if (isNotNum())
				num = v.num;
			else
				updateNumFlags(v.num);
		}
		// strings
		if (isMaybeSingleStr() && v.isMaybeSingleStr()) {
			if (!str.equals(v.str)) {
				modified = true;
				updateStrFlags(str);
				updateStrFlags(v.str);
			}
		} else if (isMaybeSingleStr()) {
			modified = true;
			if (!v.isNotStr())
				updateStrFlags(str);
		} else if (v.isMaybeSingleStr()) {
			modified = true;
			if (isNotStr())
				str = v.str;
			else
				updateStrFlags(v.str);
		}
		// objects
		if (v.object_labels != null) {
			if (object_labels == null) {
				modified = true;
				object_labels = v.object_labels;
			} else if (!object_labels.containsAll(v.object_labels)) {
				modified = true;
				object_labels = newSet(object_labels);
				object_labels.addAll(v.object_labels);
			}
		}
		return modified;
	}

	/**
	 * Checks whether the given value is equal to this one.
	 */
	@Override
	public boolean equals(Object obj) {
//		if (!canonicalizing) // use object identity as equality, except during
//								// canonicalization
//			return obj == this;
		if (obj == this)
			return true;
		if (!(obj instanceof Value))
			return false;
		Value v = (Value) obj;

		
//		if(mDependencyGraphReference == null) {
//			if(v.mDependencyGraphReference == null) {
//				// ok
//			} else {
//				return false;
//			}
//		} else {
//			if(v.mDependencyGraphReference == null) {
//				return false;
//			} else {
//				if(!mDependencyGraphReference.equals(v.mDependencyGraphReference))
//					return false;
//			}
//		}
		
		
		
//		// FIXME
//		if(mDependencyNode != null && v.mDependencyNode != null)
//			if(!mDependencyNode.equals(v.mDependencyNode))
//				return false;
//		else if(!(mDependencyNode == null && v.mDependencyNode == null))
//				return false;
//		}
//		else {
		
			
//		} 
		// TODO
		return flags == v.flags // && false
				&& mDependency.equals(v.getDependency())
				&& mDependencyGraphReference.equals(v.mDependencyGraphReference)
				&& (num == v.num || (num != null && v.num != null && num
						.equals(v.num)))
				&& (str == v.str || (str != null && v.str != null && str
						.equals(v.str)))
				&& (object_labels == v.object_labels || (object_labels != null
						&& v.object_labels != null && object_labels
						.equals(v.object_labels)));
	}

	/**
	 * Returns a description of the changes from the old value to this value. It
	 * is assumed that the old value is less than this value.
	 */
	public void diff(Value old, StringBuilder b) {
		Value v = new Value(this);
		v.flags &= ~old.flags;
		if (v.object_labels != null && old.object_labels != null) {
			v.object_labels = newSet(v.object_labels);
			v.object_labels.removeAll(old.object_labels);
		}
		b.append(v);
	}

	/**
	 * Computes the hash code for this value.
	 */
	@Override
	public int hashCode() {
		return flags
				* 17
				+ (mDependency != null ? mDependency.hashCode() : 0)
				+ (mDependencyGraphReference != null ? mDependencyGraphReference.hashCode() : 0)
				+ (num != null ? num.hashCode() : 0)
				+ (str != null ? str.hashCode() : 0)
				+ (object_labels != null ? object_labels.hashCode() : 0);
	}

	/**
	 * Returns the source locations of the objects in this value.
	 */
	public List<SourceLocation> getObjectSourceLocations() {
		List<SourceLocation> res = newList();
		if (object_labels != null)
			for (ObjectLabel objlabel : object_labels)
				res.add(objlabel.getSourceLocation());
		return res;
	}

	/**
	 * Produces a string description of this value. Ignores attributes and
	 * modified flag.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		boolean any = false;
		if (isMaybeUndef()) {
			b.append("Undef");
			any = true;
		}
		if (isMaybeNull()) {
			if (any)
				b.append('|');
			b.append("Null");
			any = true;
		}
		if (isMaybeAnyBool()) {
			if (any)
				b.append('|');
			b.append("Bool");
			any = true;
		} else if (isMaybeTrueButNotFalse()) {
			if (any)
				b.append('|');
			b.append("true");
			any = true;
		} else if (isMaybeFalseButNotTrue()) {
			if (any)
				b.append('|');
			b.append("false");
			any = true;
		}
		if (isMaybeAnyNum()) {
			if (any)
				b.append('|');
			b.append("Num");
			any = true;
		} else {
			if (isMaybeNumUInt()) {
				if (any)
					b.append('|');
				b.append("UInt");
				any = true;
			}
			if (isMaybeNumNotUInt()) {
				if (any)
					b.append('|');
				b.append("NotUInt");
				any = true;
			}
			if (isMaybeNaN()) {
				if (any)
					b.append('|');
				b.append("NaN");
				any = true;
			}
			if (isMaybeInf()) {
				if (any)
					b.append('|');
				b.append("Inf");
				any = true;
			}
			if (num != null) {
				if (any)
					b.append('|');
				b.append(num);
				any = true;
			}
		}
		if (isMaybeAnyStr()) {
			if (any)
				b.append('|');
			b.append("Str");
			any = true;
		} else {
			if (isMaybeStrUInt()) {
				if (any)
					b.append('|');
				b.append("UIntStr");
				any = true;
			}
			if (isMaybeStrNotUInt()) {
				if (any)
					b.append('|');
				b.append("NotUIntStr");
				any = true;
			}
			if (str != null) {
				if (any)
					b.append('|');
				b.append('"').append(Strings.escape(str)).append('"');
				any = true;
			}
		}
		if (object_labels != null) {
			if (any)
				b.append('|');
			b.append(object_labels);
			any = true;
		}
		if (isMaybeAbsent()) {
			if (any)
				b.append('|');
			b.append("absent");
			any = true;
		}
		if (isUnknown()) {
			b.append("?");
			any = true;
		}
		if (!any)
			b.append("<no value>");
		return b.toString();
	}

	/**
	 * Produces a string description of the attributes of this value.
	 */
	public String printAttributes() {
		StringBuilder b = new StringBuilder();
		if (hasDontDelete()) {
			b.append("(DontDelete");
			if (isMaybeDontDelete())
				b.append("+");
			if (isMaybeNotDontDelete())
				b.append("-");
			b.append(")");
		}
		if (hasDontEnum()) {
			b.append("(DontEnum");
			if (isMaybeDontEnum())
				b.append("+");
			if (isMaybeNotDontEnum())
				b.append("-");
			b.append(")");
		}
		if (hasReadOnly()) {
			b.append("(ReadOnly");
			if (isMaybeReadOnly())
				b.append("+");
			if (isMaybeNotReadOnly())
				b.append("-");
			b.append(")");
		}
		return b.toString();
	}

	/* the Undef facet */

	@Override
	public boolean isMaybeUndef() {
		return (flags & UNDEF) != 0;
	}

	@Override
	public boolean isNotUndef() {
		return (flags & UNDEF) == 0;
	}

	@Override
	public boolean isMaybeOtherThanUndef() {
		return (flags & (NULL | BOOL_ANY | NUM_ANY | STR_ANY)) != 0
				|| num != null || str != null || object_labels != null;
	}

	@Override
	public Value joinUndef() {
		if (isMaybeUndef())
			return this;
		else
			return reallyMakeUndef(this);
	}

	@Override
	public Value restrictToNotUndef() {
		if (isNotUndef())
			return this;
		Value r = new Value(this);
		r.flags &= ~UNDEF;
		return canonicalize(r);
	}

	@Override
	public Value restrictToUndef() {
		if (isNotUndef())
			return theBottom.joinDependency(mDependency);
		return theUndef.joinDependency(mDependency);
	}

	private static Value reallyMakeUndef(Value prototype) {
		Value r = (prototype == null) ? new Value(new Dependency()) : new Value(prototype);
		r.flags |= UNDEF;
		return canonicalize(r);
	}

	/**
	 * Constructs the value describing definitely undefined.
	 */
	public static Value makeUndef(Dependency dependency) {
		return theUndef.joinDependency(dependency);
	}

	/* the Null facet */

	@Override
	public boolean isMaybeNull() {
		return (flags & NULL) != 0;
	}

	@Override
	public boolean isNotNull() {
		return (flags & NULL) == 0;
	}

	@Override
	public boolean isMaybeOtherThanNull() {
		return (flags & (UNDEF | BOOL_ANY | NUM_ANY | STR_ANY)) != 0
				|| num != null || str != null || object_labels != null;
	}

	@Override
	public Value joinNull() {
		if (isMaybeNull())
			return this;
		else
			return reallyMakeNull(this);
	}

	@Override
	public Value restrictToNotNull() {
		if (isNotNull())
			return this;
		Value r = new Value(this);
		r.flags &= ~NULL;
		return canonicalize(r);
	}

	@Override
	public Value restrictToNull() {
		if (isNotNull())
			return theBottom.joinDependency(mDependency);
		return theNull.joinDependency(mDependency);
	}

	/**
	 * Returns true if this value is definitely null or undefined.
	 */
	public boolean isNullOrUndef() {
		return (flags & (NULL | UNDEF)) != 0
				&& (flags & (NUM_ANY | STR_ANY | BOOL_ANY)) == 0 && num == null
				&& str == null && object_labels == null;
	}

	/**
	 * Constructs a value as a copy of this value but definitely not null nor
	 * undefined.
	 */
	public Value restrictToNotNullNotUndef() {
		if (!isMaybeNull() && !isMaybeUndef())
			return this;
		Value r = new Value(this);
		r.flags &= ~(NULL | UNDEF);
		return canonicalize(r);
	}

	private static Value reallyMakeNull(Value prototype) {
		Value r = (prototype == null) ? new Value(new Dependency()) : new Value(prototype);
		r.flags |= NULL;
		return canonicalize(r);
	}

	/**
	 * Constructs the value describing definitely null.
	 */
	public static Value makeNull(Dependency dependency) {
		return theNull.joinDependency(dependency);
	}

	/* the Bool facet */

	@Override
	public boolean isMaybeAnyBool() {
		return (flags & BOOL_ANY) == BOOL_ANY;
	}

	@Override
	public boolean isMaybeTrueButNotFalse() {
		return (flags & BOOL_ANY) == BOOL_TRUE;
	}

	@Override
	public boolean isMaybeFalseButNotTrue() {
		return (flags & BOOL_ANY) == BOOL_FALSE;
	}

	@Override
	public boolean isMaybeTrue() {
		return (flags & BOOL_TRUE) == BOOL_TRUE;
	}

	@Override
	public boolean isMaybeFalse() {
		return (flags & BOOL_FALSE) == BOOL_FALSE;
	}

	@Override
	public boolean isNotBool() {
		return (flags & BOOL_ANY) == 0;
	}

	@Override
	public boolean isMaybeOtherThanBool() {
		return (flags & (UNDEF | NULL | NUM_ANY | STR_ANY)) != 0 || num != null
				|| str != null || object_labels != null;
	}

	@Override
	public Value joinAnyBool() {
		if (isMaybeAnyBool())
			return this;
		else {
			Value r = new Value(this);
			r.flags |= BOOL_ANY;
			return canonicalize(r);
		}
	}

	@Override
	public Value joinBool(boolean x) {
		if (isMaybeAnyBool()
				|| (x ? isMaybeTrueButNotFalse() : isMaybeFalseButNotTrue()))
			return this;
		else {
			Value r = new Value(this);
			r.flags |= x ? BOOL_TRUE : BOOL_FALSE;
			return canonicalize(r);
		}
	}

	private static Value reallyMakeBool(Boolean b, Dependency dependency) {
		Value r = new Value(dependency);
		if (b == null)
			r.flags |= BOOL_ANY;
		else if (b)
			r.flags |= BOOL_TRUE;
		else
			r.flags |= BOOL_FALSE;
		return canonicalize(r);
	}

	/**
	 * Constructs the value representing any boolean.
	 */
	public static Value makeAnyBool(Dependency dependency) {
		return theBoolAny.joinDependency(dependency);
	}

	/**
	 * Constructs the value describing the given boolean.
	 */
	public static Value makeBool(boolean b, Dependency dependenc) {
		if (b)
			return reallyMakeBool(true, dependenc); //return theBoolTrue;
		else
			return reallyMakeBool(false, dependenc);// return theBoolFalse;
	}

	/**
	 * Constructs a value from this value where only the boolean facet is
	 * considered.
	 */
	public Value restrictToBool() {
		if (isMaybeAnyBool())
			return theBoolAny.joinDependency(mDependency);
		else if (isMaybeTrueButNotFalse())
			return theBoolTrue.joinDependency(mDependency);
		else if (isMaybeFalseButNotTrue())
			return theBoolFalse.joinDependency(mDependency);
		else
			return theBottom.joinDependency(mDependency);
	}

	/**
	 * Constructs a value from this value where only the string/boolean/number
	 * facets are considered.
	 */
	public Value restrictToStrBoolNum() {
		Value r = new Value(this);
		r.object_labels = null;
		r.flags &= STR_ANY | BOOL_ANY | NUM_ANY;
		return canonicalize(r);
	}

	/* the Num facet */

	@Override
	public boolean isMaybeAnyNum() {
		return (flags & NUM_ANY) == NUM_ANY;
	}

	@Override
	public boolean isMaybeSingleNum() {
		return num != null;
	}

	@Override
	public boolean isMaybeFuzzyNum() {
		return (flags & NUM_ANY) != 0;
	}

	@Override
	public boolean isMaybeNaN() {
		return (flags & NUM_NAN) != 0;
	}

	@Override
	public boolean isNaN() {
		return (flags & NUM_ANY) == NUM_NAN && !isMaybeOtherThanNum();
	}

	@Override
	public boolean isMaybeInf() {
		return (flags & NUM_INF) != 0;
	}

	@Override
	public boolean isMaybeNumUInt() {
		return (flags & NUM_UINT) != 0;
	}

	@Override
	public boolean isMaybeNumNotUInt() {
		return (flags & NUM_NOTUINT) != 0;
	}

	@Override
	public boolean isMaybeOtherThanNum() {
		return ((flags & (UNDEF | NULL | BOOL_ANY | STR_ANY)) != 0)
				|| str != null || object_labels != null;
	}

	@Override
	public boolean isMaybeOtherThanNumUInt() {
		return ((flags & (UNDEF | NULL | BOOL_ANY | STR_ANY | NUM_INF | NUM_NAN | NUM_NOTUINT)) != 0)
				|| str != null || object_labels != null;
	}

	@Override
	public Double getNum() {
		return num != null ? num : (flags & NUM_NAN) != 0 ? Double.NaN : null;
	}

	@Override
	public boolean isNotNum() {
		return (flags & NUM_ANY) == 0 && num == null;
	}

	@Override
	public Value joinAnyNum() {
		if (isMaybeAnyNum())
			return this;
		else {
			Value r = new Value(this);
			r.num = null;
			r.flags |= NUM_ANY;
			return canonicalize(r);
		}
	}

	@Override
	public Value joinAnyNumUInt() {
		if (isMaybeNumUInt())
			return this;
		Value r = new Value(this);
		r.flags |= NUM_UINT;
		if (isMaybeSingleNum())
			r.updateNumFlags(num);
		return canonicalize(r);
	}

	@Override
	public Value joinAnyNumNotUInt() {
		if (isMaybeNumNotUInt())
			return this;
		Value r = new Value(this);
		r.flags |= NUM_NOTUINT;
		if (isMaybeSingleNum())
			r.updateNumFlags(num);
		return canonicalize(r);
	}

	@Override
	public Value restrictToNotNaN() {
		if (!isMaybeNaN())
			return this;
		Value r = new Value(this);
		r.flags &= ~NUM_NAN;
		return canonicalize(r);
	}

	private static boolean checkUInt32(double v) {
		return v >= 0 && v < Integer.MAX_VALUE * 2.0 + 1 && (v % 1) == 0;
	}

	private static boolean isUInt32(double v) {
		return !Double.isNaN(v) && !Double.isInfinite(v) && checkUInt32(v);
	}

	/**
	 * Updates the flags according to the given concrete number.
	 */
	private void updateNumFlags(double v) {
		if (Double.isNaN(v))
			flags |= NUM_NAN;
		else if (Double.isInfinite(v))
			flags |= NUM_INF;
		else if (isUInt32(v))
			flags |= NUM_UINT;
		else
			flags |= NUM_NOTUINT;
		num = null;
	}

	@Override
	public Value joinNum(double v) {
		if (Double.isNaN(v))
			return joinNumNaN();
		if (isMaybeSingleNum() && num == v)
			return this;
		Value r = new Value(this);
		if (isNotNum())
			r.num = v;
		else {
			r.updateNumFlags(v);
			if (isMaybeSingleNum())
				r.updateNumFlags(num);
		}
		return canonicalize(r);
	}

	@Override
	public Value joinNumNaN() {
		if (isMaybeNaN())
			return this;
		Value r = new Value(this);
		r.flags |= NUM_NAN;
		if (isMaybeSingleNum())
			r.updateNumFlags(num);
		return canonicalize(r);
	}

	@Override
	public Value joinNumInf() {
		if (isMaybeInf())
			return this;
		Value r = new Value(this);
		r.flags |= NUM_INF;
		if (isMaybeSingleNum())
			r.updateNumFlags(num);
		return canonicalize(r);
	}

	private static Value reallyMakeAnyUInt(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags = NUM_UINT;
		return canonicalize(r);
	}

	private static Value reallyMakeAnyNotUInt(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags = NUM_NOTUINT;
		return canonicalize(r);
	}

	private static Value reallyMakeAnyNumNotNaNInf(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags = NUM_UINT | NUM_NOTUINT;
		return canonicalize(r);
	}

	private static Value reallyMakeNumNaN(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags = NUM_NAN;
		return canonicalize(r);
	}

	private static Value reallyMakeNumInf(Dependency dependency) {	
		Value r = new Value(dependency);
		r.flags = NUM_INF;
		return canonicalize(r);
	}

	private static Value reallyMakeAnyNum(Dependency dependency) {	
		Value r = new Value(dependency);
		r.flags = NUM_ANY;
		return canonicalize(r);
	}

	/**
	 * Constructs the value describing the given number.
	 */
	public static Value makeNum(double d, Dependency dependency) {
		Value r = new Value(dependency);
		if (Double.isNaN(d))
			r.flags |= NUM_NAN;
		else
			r.num = d;
		return canonicalize(r);
	}

	/**
	 * Constructs the value describing NaN.
	 */
	public static Value makeNumNaN(Dependency dependency) {
		return theNumNaN.joinDependency(dependency);
	}

	/**
	 * Constructs the value describing +/-Inf.
	 */
	public static Value makeNumInf(Dependency dependency) {
		return theNumInf.joinDependency(dependency);
	}

	/**
	 * Constructs the value describing any number.
	 */
	public static Value makeAnyNum(Dependency dependency) {
		return theNumAny.joinDependency(dependency);
	}

	/**
	 * Constructs the value describing any UInt number.
	 */
	public static Value makeAnyNumUInt(Dependency dependency) {
		return theNumUInt.joinDependency(dependency);
	}

	/**
	 * Constructs the value describing any non-UInt number.
	 */
	public static Value makeAnyNumNotUInt(Dependency dependency) {
		return theNumNotUInt.joinDependency(dependency);
	}

	/**
	 * Constructs the value describing number except NaN and infinity.
	 */
	public static Value makeAnyNumNotNaNInf(Dependency dependency) {
		return theNumNotNaNInf.joinDependency(dependency);
	}

	@Override
	public Value restrictToNum() {	
		Value r = new Value(this);
		r.flags = flags & NUM_ANY;
		r.num = num;
		return canonicalize(r);
	}

	@Override
	public Value restrictToNotNum() {
		Value r = new Value(this);
		r.flags &= ~NUM_ANY;
		r.num = null;
		return canonicalize(r);
	}

	/* the Str facet */

	@Override
	public boolean isMaybeAnyStr() {
		return (flags & STR_ANY) == STR_ANY;
	}

	@Override
	public boolean isMaybeStrUInt() {
		return (flags & STR_UINT) != 0;
	}

	@Override
	public boolean isMaybeStrNotUInt() {
		return (flags & STR_NOTUINT) != 0;
	}

	@Override
	public boolean isMaybeStrOnlyUInt() {
		return (flags & STR_ANY) == STR_UINT;
	}

	@Override
	public boolean isMaybeSingleStr() {
		return str != null;
	}

	@Override
	public boolean isMaybeFuzzyStr() {
		return (flags & STR_ANY) != 0;
	}

	@Override
	public String getStr() {
		return str;
	}

	@Override
	public boolean isNotStr() {
		return (flags & STR_ANY) == 0 && str == null;
	}

	@Override
	public boolean isMaybeOtherThanStr() {
		return (flags & (UNDEF | NULL | BOOL_ANY | NUM_ANY)) != 0
				|| num != null || object_labels != null;
	}

	@Override
	public Value joinAnyStr() {
		if (isMaybeAnyStr())
			return this;
		else {
			Value r = new Value(this);
			r.flags |= STR_ANY;
			r.str = null;
			return canonicalize(r);
		}
	}

	@Override
	public Value joinAnyStrUInt() {
		if (isMaybeStrUInt())
			return this;
		Value r = new Value(this);
		r.flags |= STR_UINT;
		r.str = null;
		if (isMaybeSingleStr())
			r.updateStrFlags(str);
		return canonicalize(r);
	}

	@Override
	public Value joinStr(String s) {
		if (isMaybeSingleStr() && str.equals(s))
			return this;
		Value r = new Value(this);
		if (isNotStr())
			r.str = s;
		else {
			r.updateStrFlags(s);
			if (isMaybeSingleStr())
				r.updateStrFlags(str);
		}
		return canonicalize(r);
	}

	/**
	 * Updates the flags according to the given concrete string.
	 */
	private void updateStrFlags(String s) {
		if (Strings.isArrayIndex(s))
			flags |= STR_UINT;
		else
			flags |= STR_NOTUINT;
		str = null;
	}

	private static Value reallyMakeAnyStr(Dependency dependency) {
		Value r = new Value(dependency);
		r.flags |= STR_ANY;
		return canonicalize(r);
	}

	/**
	 * Constructs the value describing any string.
	 */
	public static Value makeAnyStr(Dependency dependency) {
		return theStrAny.joinDependency(dependency);
	}

	/**
	 * Constructs the value describing the given string.
	 */	
	public static Value makeStr(String s , Dependency dependency) {	
		Value r = new Value(dependency);
		r.str = s;
		return canonicalize(r);
	}

	@Override
	public Value restrictToStr() {
		if (isNotStr())
			return theBottom.joinDependency(mDependency);
		else if (isMaybeSingleStr())
			return makeStr(getStr(), mDependency);
		else {
			Value r = new Value(mDependency);
			if (isMaybeStrUInt())
				r.flags |= Value.STR_UINT;
			if (isMaybeStrNotUInt())
				r.flags |= Value.STR_NOTUINT;
			return canonicalize(r);
		}
	}

	/* object labels */

	/**
	 * Constructs the value describing the given object label.
	 */
	public static Value makeObject(ObjectLabel v, Dependency dependency) {
		Value r = new Value(dependency);
		r.object_labels = newSet();
		r.object_labels.add(v);
		return canonicalize(r);
	}

	/**
	 * Constructs the value describing the given object labels.
	 */
	public static Value makeObject(Set<ObjectLabel> v ,Dependency dependency) {
		Value r = new Value(dependency);
		if (!v.isEmpty()) {
			r.object_labels = newSet();
			r.object_labels.addAll(v);
		}
		return canonicalize(r);
	}

	/**
	 * Constructs a value as the join of this value and maybe the given object
	 * label.
	 */
	public Value joinObject(ObjectLabel objlabel) {
		if (object_labels != null && object_labels.contains(objlabel))
			return this;
		else {
			Value r = new Value(this);
			if (r.object_labels == null)
				r.object_labels = newSet();
			r.object_labels.add(objlabel);
			return canonicalize(r);
		}
	}

	/**
	 * Constructs a value as a copy of this value but only with object values.
	 */
	public Value restrictToObject() {
		if (!isMaybePrimitive())
			return this;
		Value r = new Value(this);
		r.flags &= ~PRIMITIVE;
		r.num = null;
		r.str = null;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of this value but only with non-object
	 * values.
	 */
	public Value restrictToNonObject() {
		if (object_labels == null)
			return this;
		Value r = new Value(this);
		r.object_labels = null;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of this value but with the given object
	 * labels.
	 */
	private Value replaceObjectLabels(Set<ObjectLabel> object_labels) {
		Value r = new Value(this);
		if (object_labels.isEmpty())
			object_labels = null;
		r.object_labels = object_labels;
		return canonicalize(r);
	}

	/**
	 * Constructs a value as a copy of this value but with object labels
	 * summarized. If s is null, this value is returned instead.
	 */
	public Value summarize(Summarized s) {
		if (s == null || isUnknown())
			return this;
		Set<ObjectLabel> ss = ObjectLabel.summarize(getObjectLabels(), s);
		if (ss == getObjectLabels())
			return this;
		else
			return replaceObjectLabels(ss);
	}

	/**
	 * Returns true if this value maybe represents an object or a primitive
	 * value.
	 */
	public boolean isMaybeValue() {
		return (flags & PRIMITIVE) != 0 || num != null || str != null
				|| object_labels != null;
	}

	/**
	 * Returns true if this value maybe represents an object or a primitive
	 * value or us 'unknown'.
	 */
	public boolean isMaybeValueOrUnknown() {
		return isMaybeValue() || isUnknown();
	}

	/**
	 * Returns true if this value definitely represents no object nor a
	 * primitive value. Note that this is <i>not</i> the same as
	 * {@link #isBottom()}.
	 */
	public boolean isNoValue() {
		return !isMaybeValue();
	}

	/**
	 * Returns true if this value maybe represents an object.
	 */
	public boolean isMaybeObject() {
		return object_labels != null;
	}

	/**
	 * Returns true if this value may be a non-object, including undefined and
	 * null.
	 */
	public boolean isMaybePrimitive() {
		return (flags & PRIMITIVE) != 0 || num != null || str != null;
	}

	/**
	 * Returns the (immutable) set of object labels.
	 */
	public Set<ObjectLabel> getObjectLabels() {
		if (object_labels == null)
			return Collections.emptySet();
		return Collections.unmodifiableSet(object_labels);
	}

	/**
	 * If this value is definitely non-modified, then return the given value,
	 * otherwise return this value.
	 */
	public Value replaceIfNonModified(Value v) {
		if (isMaybeModified() || v.isUnknown())
			return this;
		else {
			if (Options.isDebugEnabled()) {
				if (!equals(v.restrictToNonModified()))
					System.out.println("Value: replacing nonmodified value "
							+ this + " by " + v);
			}
			return v;
		}
	}

	/**
	 * Returns a copy of this value where the given object label has been
	 * replaced, if present. Does not change modified flags.
	 */
	public Value replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel) {
		if (object_labels == null || oldlabel.equals(newlabel)
				|| !object_labels.contains(oldlabel))
			return this;
		Set<ObjectLabel> newobjlabels = newSet(object_labels);
		newobjlabels.remove(oldlabel);
		newobjlabels.add(newlabel);
		Value r = new Value(this);
		r.object_labels = newobjlabels;
		return canonicalize(r);
	}

	/**
	 * Checks that this value is non-empty.
	 * 
	 * @throws RuntimeException
	 *             if empty
	 */
	public void assertNonEmpty() {
		if (isNoValue() && !Options.isPropagateDeadFlow())
			throw new RuntimeException("Empty value");
	}

	/* dependency functions */
	
	@Override
	public Dependency getDependency() {
		return mDependency;
	}

	@Override
	public boolean hasDependency() {
		if (mDependency.isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public Value joinDependency(Dependency dependency) {
		Value r = new Value(this);
		r.mDependency.join(dependency);
		return canonicalize(r);
	}

	@Override
	public Value setDependency(Dependency dependency) {
		Value r = new Value(this);
		r.mDependency = new Dependency(dependency);
		return canonicalize(r);
	}

	@Override
	public void removeDependency() {
		mDependency = new Dependency();
	}

	@Override
	public String printDependency() {
		return mDependency.toString();
	}

	@Override
	public DependencyGraphReference getDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? mDependencyGraphReference : null;
	}

	@Override
	public Value setDependencyGraphReference(DependencyGraphReference reference) {
		Value r = new Value(this);
		r.mDependencyGraphReference = new DependencyGraphReference(reference);
		return canonicalize(r);
	}

	@Override
	public boolean hasDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? true : false;
	}

	@Override
	public String printDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? mDependencyGraphReference.toString() : "";
	}

	@Override
	public Value joinDependencyGraphReference(DependencyGraphReference reference) {
		Value r = new Value(this);
		r.mDependencyGraphReference.join(reference);
		return canonicalize(r);
	}

	@Override
	public Value joinDependencyGraphReference(DependencyNode node) {
		return joinDependencyGraphReference(node.getReference());
	}
}