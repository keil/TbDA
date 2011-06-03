package dk.brics.tajs.lattice;

import static dk.brics.tajs.analysis.InitialStateBuilder.FUNCTION_PROTOTYPE;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.sortedEntries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeAPIs;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.flowgraph.nodes.ConstantNode;
import dk.brics.tajs.flowgraph.nodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.nodes.WriteVariableNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.IBlockState;
import dk.brics.tajs.solver.ICallContext;
import dk.brics.tajs.solver.IStatistics;
import dk.brics.tajs.util.Strings;

/**
 * Abstract state for block entries. Mutable.
 */
public abstract class BlockState<BlockStateType extends BlockState<BlockStateType, CallContextType, StatisticsType>, CallContextType extends ICallContext, StatisticsType extends IStatistics>
		implements IBlockState<BlockStateType> {

	private GenericSolver<BlockStateType, CallContextType, StatisticsType>.SolverInterface c;

	private BasicBlock block; // the basic block owning this state

	private CallContextType context; // the call context for this state

	/**
	 * Map from ObjectLabel to Object.
	 */
	private Map<ObjectLabel, Obj> store; // default maps to bottom (i.e. the Obj
											// where everything is bottom)
	private boolean writable_store; // for copy-on-write

	/**
	 * Reusable immutable part of the store. Entries may be overridden by
	 * 'store'. Not used if lazy propagation is enabled.
	 */
	private Map<ObjectLabel, Obj> basis_store;

	/**
	 * Current execution context.
	 */
	private Set<ExecutionContext> execution_context;
	private boolean writable_execution_context; // for copy-on-write

	/**
	 * Maybe/definitely summarized objects since function entry. (Contains the
	 * singleton object labels.)
	 */
	private Summarized summarized;

	/**
	 * Temporary variables.
	 */
	private List<Value> temporaries;
	private boolean writable_temporaries; // for copy-on-write

	/**
	 * Object labels that appear on the stack.
	 */
	private Set<ObjectLabel> stacked_objlabels; // not used if lazy propagation
												// is enabled
	private boolean writable_stacked_objlabels; // for copy-on-write

	/**
	 * (On)Load Event Handlers
	 */
	private Set<ObjectLabel> load_event_handlers_definite;
	private Set<ObjectLabel> load_event_handlers_maybe;
	private boolean writable_load_event_handlers;

	/**
	 * (On)Unload Event Handlers
	 */
	private Set<ObjectLabel> unload_event_handlers;
	private boolean writable_unload_event_handlers;

	/**
	 * Keyboard Event Handlers
	 */
	private Set<ObjectLabel> keyboard_event_handlers;
	private boolean writable_keyboard_event_handlers;

	/**
	 * Mouse Event Handlers
	 */
	private Set<ObjectLabel> mouse_event_handlers;
	private boolean writable_mouse_event_handlers;

	/**
	 * Timeout Event Handlers
	 */
	private Set<ObjectLabel> timeout_event_handlers;
	private boolean writable_timeout_event_handlers;

	/**
	 * Misc. Event Handlers
	 */
	private Set<ObjectLabel> unknown_event_handlers;
	private boolean writable_unknown_event_handlers;

	/**
	 * Elements (by id, name, and tagname)
	 */
	private Map<String, Set<ObjectLabel>> element_by_id;
	private Set<ObjectLabel> default_element_by_id;
	private Map<String, Set<ObjectLabel>> elements_by_name;
	private Set<ObjectLabel> default_elements_by_name;
	private Map<String, Set<ObjectLabel>> elements_by_tagname;
	private Set<ObjectLabel> default_elements_by_tagname;
	private boolean writable_elements;

	private static int number_of_states_created;
	private static int number_of_makewritable_store;
	private static int number_of_makewritable_temporaries;

	/**
	 * Constructs a new bottom state.
	 */
	public BlockState(
			GenericSolver<BlockStateType, CallContextType, StatisticsType>.SolverInterface c,
			BasicBlock block) {
		this();
		this.c = c;
		this.block = block;
	}

	public BlockState() {
		summarized = new Summarized();
		setToBottom();
		number_of_states_created++;
	}

	/**
	 * Constructs a new state as a copy of the given state. (The resulting state
	 * is never immutable, though.)
	 */
	protected BlockState(
			BlockState<BlockStateType, CallContextType, StatisticsType> x) {
		c = x.c;
		block = x.block;
		context = x.context;
		summarized = new Summarized(x.summarized);
		if (Options.isCopyOnWriteDisabled()) {
			store = newMap();
			for (Map.Entry<ObjectLabel, Obj> xs : x.store.entrySet())
				store.put(xs.getKey(), new Obj(xs.getValue()));
			basis_store = x.basis_store;
			execution_context = newSet(x.execution_context);
			temporaries = newList(x.temporaries);
			if (Options.isDOMEnabled()) {
				stacked_objlabels = newSet(x.stacked_objlabels);

				load_event_handlers_definite = newSet(x.load_event_handlers_definite);
				load_event_handlers_maybe = newSet(x.load_event_handlers_maybe);
				unload_event_handlers = newSet(x.unload_event_handlers);
				keyboard_event_handlers = newSet(x.keyboard_event_handlers);
				mouse_event_handlers = newSet(x.mouse_event_handlers);
				unknown_event_handlers = newSet(x.unknown_event_handlers);
				timeout_event_handlers = newSet(x.timeout_event_handlers);

				element_by_id = newMap(x.element_by_id);
				elements_by_name = newMap(x.elements_by_name);
				elements_by_tagname = newMap(x.elements_by_tagname);
			}
		} else {
			store = x.store;
			basis_store = x.basis_store;
			execution_context = x.execution_context;
			temporaries = x.temporaries;
			stacked_objlabels = x.stacked_objlabels;
			x.writable_execution_context = writable_execution_context = false;
			x.writable_store = writable_store = false;
			x.writable_temporaries = writable_temporaries = false;
			x.writable_stacked_objlabels = writable_stacked_objlabels = false;
			if (Options.isDOMEnabled()) {
				load_event_handlers_definite = x.load_event_handlers_definite;
				load_event_handlers_maybe = x.load_event_handlers_maybe;
				x.writable_load_event_handlers = writable_load_event_handlers = false;

				unload_event_handlers = x.unload_event_handlers;
				x.writable_unload_event_handlers = writable_unload_event_handlers = false;

				keyboard_event_handlers = x.keyboard_event_handlers;
				x.writable_keyboard_event_handlers = writable_keyboard_event_handlers = false;

				mouse_event_handlers = x.mouse_event_handlers;
				x.writable_mouse_event_handlers = writable_mouse_event_handlers = false;

				unknown_event_handlers = x.unknown_event_handlers;
				x.writable_unknown_event_handlers = writable_unknown_event_handlers = false;

				timeout_event_handlers = x.timeout_event_handlers;
				x.writable_timeout_event_handlers = writable_timeout_event_handlers = false;

				element_by_id = x.element_by_id;
				elements_by_name = x.elements_by_name;
				elements_by_tagname = x.elements_by_tagname;
				x.writable_elements = writable_elements = false;
			}
		}
		number_of_states_created++;
	}

	/**
	 * Constructs a new state as a copy of this state.
	 */
	@Override
	abstract public BlockStateType clone();

	/**
	 * Returns the solver interface.
	 */
	public GenericSolver<BlockStateType, CallContextType, StatisticsType>.SolverInterface getSolverInterface() {
		return c;
	}

	/**
	 * Set the solver interface
	 */
	public void setSolverInterface(
			GenericSolver<BlockStateType, CallContextType, StatisticsType>.SolverInterface c) {
		this.c = c;
	}

	/**
	 * Checks whether this state belongs to the program entry.
	 */
	public boolean atProgramEntry() {
		return block != null
				&& block == block.getFunction().getFlowGraph().getMain()
						.getEntry();
	}

	/**
	 * Returns the basic block owning this state.
	 */
	public BasicBlock getBasicBlock() {
		return block;
	}

	/**
	 * Returns the call context for this state.
	 */
	public CallContextType getContext() {
		return context;
	}

	/**
	 * Sets the basic block owning this state.
	 */
	public void setBasicBlock(BasicBlock block) {
		this.block = block;
	}

	/**
	 * Sets the call context.
	 */
	public void setContext(CallContextType context) {
		this.context = context;
	}

	/**
	 * Checks that the owner block and context are the given.
	 * 
	 * @throws RuntimeException
	 *             if mismatch
	 */
	public void checkOwner(BasicBlock b, CallContextType c) {
		if (!block.equals(b) || !context.equals(c))
			throw new RuntimeException(
					"BlockState owner block/context mismatch!");
	}

	/**
	 * Returns the number of objects in the store, excluding the basis store.
	 */
	public int getStoreSize() {
		return store.size();
	}

	/**
	 * Returns a measure of the vertical position of this lattice element within
	 * the lattice.
	 */
	@Override
	public int getPosition() {
		return 0; // TODO: lattice position impact heuristic not in use - remove
					// it completely?
		// int s = 0;
		// for (Obj obj : store.values())
		// s += obj.getPosition();
		// if (basis_store != null)
		// for (Obj obj : basis_store.values())
		// s += obj.getPosition();
		// for (Value v : temporaries)
		// if (v != null)
		// s += v.getPosition();
		// return s; // ignoring the execution context, summarized sets, and
		// stacked temporaries
	}

	/**
	 * Sets an object in the store.
	 */
	void putObject(ObjectLabel objlabel, Obj obj) {
		makeWritableStore();
		store.put(objlabel, obj);
	}

	/**
	 * Looks up an object in the store. Returns null if absent or 'unknown'.
	 * 
	 * @param writable
	 *            if set, the object will be copied from basis store if
	 *            necessary
	 */
	@Override
	public Obj getObjectRaw(ObjectLabel objlabel, boolean writable) {
		if (writable)
			makeWritableStore();
		Obj obj = store.get(objlabel);
		if (obj == null && basis_store != null) {
			obj = basis_store.get(objlabel);
			if (obj != null && writable) {
				obj = new Obj(obj);
				putObject(objlabel, obj);
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: getting writable object from basis: "
									+ objlabel);
			}
		}
		return obj;
	}

	/**
	 * Looks up an object in the store. Returns null if bottom_obj (never
	 * 'unknown') - but that is only possible if lazy propagation is disabled.
	 * 
	 * @param writable
	 *            if set, the object will be copied from basis store if
	 *            necessary
	 */
	public Obj getObject(ObjectLabel objlabel, boolean writable) {
		Obj obj = getObjectRaw(objlabel, writable);
		if (obj == null && !Options.isLazyDisabled()) {
			obj = Obj.makeUnknown();
			if (writable)
				putObject(objlabel, obj);
		}
		return obj;
	}

	/**
	 * Looks up an object in the store. The object must be present but may be
	 * 'unknown'. If 'unknown', it is first resolved.
	 * 
	 * @param writable
	 *            if set, the object will be copied from basis store if
	 *            necessary
	 */
	Obj getPresentObject(ObjectLabel objlabel, boolean writable) {
		Obj obj = getObject(objlabel, writable);
		if (obj == null) // only relevant without lazy prop
			throw new RuntimeException("Dangling reference: " + objlabel
					+ " from " + objlabel.getSourceLocation());
		return obj;
	}

	/**
	 * Returns the summarized sets.
	 */
	public Summarized getSummarized() {
		return summarized;
	}

	/**
	 * Sets the current store contents as the basis store. After this, objects
	 * in the basis store should never be summarized. Ignored if lazy
	 * propagation is enabled.
	 */
	public void freezeBasisStore() {
		if (Options.isLazyDisabled()) {
			basis_store = store;
			store = newMap();
			writable_store = true;
			if (Options.isDebugEnabled())
				System.out.println("BlockState: freezeBasisStore()");
		}
	}

	/**
	 * Makes store writable.
	 */
	private void makeWritableStore() {
		if (writable_store)
			return;
		Map<ObjectLabel, Obj> new_store = newMap();
		for (Map.Entry<ObjectLabel, Obj> xs : store.entrySet())
			new_store.put(xs.getKey(), new Obj(xs.getValue()));
		store = new_store;
		writable_store = true;
		number_of_makewritable_store++;
	}

	/**
	 * Makes execution_context writable.
	 */
	private void makeWritableExecutionContext() {
		if (writable_execution_context)
			return;
		execution_context = newSet(execution_context);
		writable_execution_context = true;
	}

	/**
	 * Makes temporaries writable.
	 */
	private void makeWritableTemporaries() {
		if (writable_temporaries)
			return;
		temporaries = newList(temporaries);
		writable_temporaries = true;
		number_of_makewritable_temporaries++;
	}

	/**
	 * Makes stacked object set writable.
	 */
	private void makeWritableStackedObjects() {
		if (!Options.isLazyDisabled())
			return;
		if (writable_stacked_objlabels)
			return;
		stacked_objlabels = newSet(stacked_objlabels);
		writable_stacked_objlabels = true;
	}

	/**
	 * Makes the set of load handlers writable
	 */
	private void makeWritableLoadEventHandlers() {
		if (!Options.isDOMEnabled() || writable_load_event_handlers)
			return;
		load_event_handlers_definite = newSet(load_event_handlers_definite);
		load_event_handlers_maybe = newSet(load_event_handlers_maybe);
		writable_load_event_handlers = true;
	}

	/**
	 * Makes the set of unload handlers writable
	 */
	private void makeWritableUnloadEventHandlers() {
		if (!Options.isDOMEnabled() || writable_unload_event_handlers)
			return;
		unload_event_handlers = newSet(unload_event_handlers);
		writable_unload_event_handlers = true;
	}

	/**
	 * Makes the set of keyboard event handlers writable
	 */
	private void makeWritableKeyboardEventHandlers() {
		if (!Options.isDOMEnabled() || writable_keyboard_event_handlers)
			return;
		keyboard_event_handlers = newSet(keyboard_event_handlers);
		writable_keyboard_event_handlers = true;
	}

	/**
	 * Makes the set of mouse event handlers writable
	 */
	private void makeWritableMouseEventHandlers() {
		if (!Options.isDOMEnabled() || writable_mouse_event_handlers)
			return;
		mouse_event_handlers = newSet(mouse_event_handlers);
		writable_mouse_event_handlers = true;
	}

	/**
	 * Makes the set of unknown event handlers writable
	 */
	private void makeWritableUnknownEventHandlers() {
		if (!Options.isDOMEnabled() || writable_unknown_event_handlers)
			return;
		unknown_event_handlers = newSet(unknown_event_handlers);
		writable_unknown_event_handlers = true;
	}

	/**
	 * Makes the set of timeout event handlers writable
	 */
	private void makeWritableTimeoutHandlers() {
		if (!Options.isDOMEnabled() || writable_timeout_event_handlers)
			return;
		timeout_event_handlers = newSet(timeout_event_handlers);
		writable_timeout_event_handlers = true;
	}

	/**
	 * Makes the set of elements writable
	 */
	private void makeWritableElements() {
		if (!Options.isDOMEnabled() || writable_elements) {
			return;
		}
		element_by_id = newMap(element_by_id);
		elements_by_name = newMap(elements_by_name);
		elements_by_tagname = newMap(elements_by_tagname);
		writable_elements = true;
	}

	/**
	 * Returns the total number of BlockState objects created.
	 */
	public static int getNumberOfStatesCreated() {
		return number_of_states_created;
	}

	/**
	 * Resets the global counters.
	 */
	public static void reset() {
		number_of_states_created = 0;
		number_of_makewritable_store = 0;
	}

	/**
	 * Returns the total number of makeWritableStore operations.
	 */
	public static int getNumberOfMakeWritableStoreCalls() {
		return number_of_makewritable_store;
	}

	/**
	 * Returns the total number of makeWritableTemporaries operations.
	 */
	public static int getNumberOfMakeWritableTemporariesCalls() {
		return number_of_makewritable_temporaries;
	}

	/**
	 * Returns the size of the temporaries map.
	 */
	public int getNumberOfTemporaries() {
		return temporaries.size();
	}

	/**
	 * Clears modified flags for all values. Ignores the basis store.
	 */
	private void clearModified() {
		makeWritableStore();
		for (Obj obj : store.values())
			// ignores basis store
			obj.clearModified();
		if (Options.isDebugEnabled())
			System.out.println("BlockState: clearModified()");
	}

	/**
	 * Checks that this state is consistent.
	 * 
	 * @see #checkNoDanglingRefs()
	 */
	@Override
	public void check() {
		checkNoDanglingRefs();
	}

	/**
	 * Checks for dangling references. Ignored if lazy propagation is enabled.
	 * 
	 * @throws RuntimeException
	 *             if dangling references are found
	 */
	public void checkNoDanglingRefs() {
		if (!Options.isLazyDisabled())
			return;
		Set<ObjectLabel> used = newSet();
		for (ExecutionContext e : execution_context) {
			for (ObjectLabel l : e.getScopeChain())
				used.add(l);
			ObjectLabel varobj = e.getVariableObject();
			if (varobj != null)
				used.add(varobj);
		}
		for (ObjectLabel objlabel : store.keySet())
			used.addAll(getAllObjectLabels(objlabel));
		if (basis_store != null)
			for (ObjectLabel objlabel : basis_store.keySet())
				used.addAll(getAllObjectLabels(objlabel));
		for (Value v : temporaries)
			if (v != null)
				used.addAll(v.getObjectLabels());
		used.addAll(stacked_objlabels);
		used.removeAll(store.keySet());
		if (basis_store != null)
			used.removeAll(basis_store.keySet());
		if (!used.isEmpty()) {
			if (Options.isDebugEnabled())
				System.out.println(this);
			throw new RuntimeException("Dangling references: " + used);
		}
	}

	/**
	 * Sets this state to the bottom state. Used for representing 'no flow'.
	 */
	public void setToBottom() {
		basis_store = null;
		summarized.clear();
		if (Options.isCopyOnWriteDisabled()) {
			store = newMap();
			writable_store = true;
			execution_context = newSet();
			writable_execution_context = true;
			temporaries = new ArrayList<Value>();
			writable_temporaries = true;
			stacked_objlabels = newSet();
			writable_stacked_objlabels = true;
			if (Options.isDOMEnabled()) {
				load_event_handlers_definite = newSet();
				load_event_handlers_maybe = newSet();
				writable_load_event_handlers = true;

				unload_event_handlers = newSet();
				writable_unload_event_handlers = true;

				keyboard_event_handlers = newSet();
				writable_keyboard_event_handlers = true;

				mouse_event_handlers = newSet();
				writable_keyboard_event_handlers = true;

				unknown_event_handlers = newSet();
				writable_unknown_event_handlers = true;

				timeout_event_handlers = newSet();
				writable_timeout_event_handlers = true;

				element_by_id = newMap();
				elements_by_name = newMap();
				elements_by_tagname = newMap();
				writable_elements = true;
			}
		} else {
			store = Collections.emptyMap();
			writable_store = false;
			execution_context = Collections.emptySet();
			writable_execution_context = false;
			temporaries = Collections.emptyList();
			writable_temporaries = false;
			stacked_objlabels = Collections.emptySet();
			writable_stacked_objlabels = false;
			if (Options.isDOMEnabled()) {
				load_event_handlers_definite = Collections.emptySet();
				load_event_handlers_maybe = Collections.emptySet();
				writable_load_event_handlers = false;

				unload_event_handlers = Collections.emptySet();
				writable_unload_event_handlers = false;

				keyboard_event_handlers = Collections.emptySet();
				writable_keyboard_event_handlers = false;

				mouse_event_handlers = Collections.emptySet();
				writable_mouse_event_handlers = false;

				unknown_event_handlers = Collections.emptySet();
				writable_unknown_event_handlers = false;

				timeout_event_handlers = Collections.emptySet();
				writable_timeout_event_handlers = false;

				element_by_id = newMap();
				elements_by_name = newMap();
				elements_by_tagname = newMap();
				writable_elements = false;
			}
		}
	}

	/**
	 * Checks whether the execution context is empty.
	 */
	@Override
	public boolean isEmpty() {
		return execution_context.isEmpty();
	}

	/**
	 * Replaces all definitely non-modified parts of this state by the
	 * corresponding parts of the given states. The store is restored from the
	 * call edge state; the stack is restored from the caller state.
	 */
	public void mergeFunctionReturn(
			BlockState<BlockStateType, CallContextType, StatisticsType> caller_state,
			BlockState<BlockStateType, CallContextType, StatisticsType> calledge_state,
			Summarized callee_summarized) {
		makeWritableStore();
		if (Options.isDebugEnabled()) {
			check();
			caller_state.check();
			calledge_state.check();
		}
		if (basis_store != caller_state.basis_store
				|| basis_store != calledge_state.basis_store)
			throw new RuntimeException("Not identical basis stores");
		// strengthen each object
		Map<ObjectLabel, Obj> summarized_calledge_store = summarizeStore(
				calledge_state, summarized);
		for (ObjectLabel objlabel : store.keySet()) {
			Obj calledge_obj = summarized_calledge_store.get(objlabel);
			if (calledge_obj == null && calledge_state.basis_store != null)
				calledge_obj = calledge_state.basis_store.get(objlabel); // basis
																			// store
																			// objects
																			// are
																			// never
																			// summarized
																			// after
																			// initialization
			if (calledge_obj != null) {
				Obj obj = getObjectRaw(objlabel, true);
				obj.strengthenNonModifiedParts(calledge_obj);
			}
		}
		// restore objects that were not used by the callee (i.e. either
		// 'unknown' or never retrieved from basis_store to store)
		for (Map.Entry<ObjectLabel, Obj> me : summarized_calledge_store
				.entrySet())
			if (!store.containsKey(me.getKey()))
				putObject(me.getKey(), me.getValue());
		// remove fully unknown objects
		List<ObjectLabel> fully_unknown = newList();
		for (Map.Entry<ObjectLabel, Obj> me : store.entrySet()) {
			Obj obj = me.getValue();
			if (obj.getDefaultArrayProperty().isUnknown()
					&& obj.getDefaultNonArrayProperty().isUnknown()
					&& obj.getAllProperties().isEmpty()
					&& obj.getInternalPrototype().isUnknown()
					&& obj.getInternalValue().isUnknown()
					&& obj.isScopeChainUnknown())
				fully_unknown.add(me.getKey());
		}
		if (!fully_unknown.isEmpty()) {
			if (Options.isDebugEnabled())
				System.out
						.println("BlockState: removing fully unknown objects: "
								+ fully_unknown);
			for (ObjectLabel objlabel : fully_unknown)
				store.remove(objlabel);
		}
		// restore execution_context and stacked_objlabels from caller
		execution_context = ExecutionContext.summarize(
				caller_state.execution_context, callee_summarized);
		writable_execution_context = true;
		temporaries = summarize(caller_state.temporaries, callee_summarized);
		writable_temporaries = true;
		if (Options.isLazyDisabled())
			stacked_objlabels = ObjectLabel.summarize(
					caller_state.stacked_objlabels, callee_summarized);
		writable_stacked_objlabels = false;
		// merge summarized sets
		summarized = new Summarized();
		summarized.getMaybeSummarized().addAll(
				caller_state.summarized.getMaybeSummarized());
		summarized.getMaybeSummarized().addAll(
				callee_summarized.getMaybeSummarized());
		summarized.getDefinitelySummarized().addAll(
				caller_state.summarized.getDefinitelySummarized());
		summarized.getDefinitelySummarized().addAll(
				callee_summarized.getDefinitelySummarized());
		if (Options.isDebugEnabled()) {
			check();
			System.out.println("BlockState: mergeFunctionReturn(...) done");
		}
	}

	/**
	 * Summarizes the store of the given state.
	 */
	private Map<ObjectLabel, Obj> summarizeStore(
			BlockState<BlockStateType, CallContextType, StatisticsType> s,
			Summarized summarized) {
		Map<ObjectLabel, Obj> res = newMap();
		for (Map.Entry<ObjectLabel, Obj> me : s.store.entrySet()) {
			ObjectLabel objlabel = me.getKey();
			Obj obj = me.getValue().summarize(summarized);
			if (objlabel.isSingleton()) {
				if (summarized.isDefinitelySummarized(objlabel))
					join(res, objlabel.makeSummary(), obj, s);
				else if (summarized.isMaybeSummarized(objlabel)) {
					join(res, objlabel, new Obj(obj), s);
					join(res, objlabel.makeSummary(), obj, s);
				} else
					join(res, objlabel, obj, s);
			} else
				join(res, objlabel, obj, s);
		}
		return res;
	}

	/**
	 * Joins the object into the store. May modify the given object.
	 */
	private void join(Map<ObjectLabel, Obj> store, ObjectLabel objlabel,
			Obj obj,
			BlockState<BlockStateType, CallContextType, StatisticsType> s) {
		Obj old = store.get(objlabel);
		if (old != null)
			join(objlabel, old, s, objlabel, obj, s);
		else
			store.put(objlabel, obj);
	}

	/**
	 * Joins the given state into this state. Replaces 'unknown' objects and
	 * values when necessary.
	 * 
	 * @return true if changed
	 */
	protected boolean join(
			BlockState<BlockStateType, CallContextType, StatisticsType> s) {
		BlockState<BlockStateType, CallContextType, StatisticsType> old = null;
		if (Options.isDebugEnabled())
			old = clone();
		if (Options.isDebugEnabled() && Options.isIntermediateStatesEnabled()) {
			System.out.println("BlockState: join this state: " + this);
			System.out.println("BlockState: join other state: " + s);
		}
		makeWritableStore();
		makeWritableExecutionContext();
		makeWritableTemporaries();
		makeWritableStackedObjects();
		if (Options.isDOMEnabled()) {
			makeWritableLoadEventHandlers();
			makeWritableUnloadEventHandlers();
			makeWritableKeyboardEventHandlers();
			makeWritableMouseEventHandlers();
			makeWritableUnknownEventHandlers();
			makeWritableTimeoutHandlers();
			makeWritableElements();
		}
		boolean changed = execution_context.addAll(s.execution_context);
		Set<ObjectLabel> labs = newSet();
		labs.addAll(store.keySet());
		labs.addAll(s.store.keySet());
		for (ObjectLabel lab : labs)
			changed |= join(lab, s, lab);
		changed |= summarized.join(s.summarized);
		for (int i = 0; i < temporaries.size() || i < s.temporaries.size(); i++) {
			Value v1 = i < temporaries.size() ? temporaries.get(i) : null;
			Value v2 = i < s.temporaries.size() ? s.temporaries.get(i) : null;
			Value v = v1 != null ? (v2 != null ? v1.join(v2) : v1) : v2; // TODO:
																			// should
																			// be
																			// safe
																			// to
																			// set
																			// to
																			// null
																			// if
																			// either
																			// is
																			// null?
																			// or
																			// maybe
																			// it
																			// makes
																			// no
																			// difference
			if ((v1 == null && v2 != null) || (v1 != null && !v.equals(v1))) {
				if (i < temporaries.size())
					temporaries.set(i, v);
				else
					temporaries.add(v);
				changed = true;
			}
		}
		if (Options.isLazyDisabled())
			changed |= stacked_objlabels.addAll(s.stacked_objlabels);
		if (Options.isDOMEnabled()) {
			changed |= load_event_handlers_definite
					.retainAll(s.load_event_handlers_definite);
			changed |= load_event_handlers_maybe
					.addAll(s.load_event_handlers_maybe);
			changed |= unload_event_handlers.addAll(s.unload_event_handlers);
			changed |= keyboard_event_handlers
					.addAll(s.keyboard_event_handlers);
			changed |= mouse_event_handlers.addAll(s.mouse_event_handlers);
			changed |= unknown_event_handlers.addAll(s.unknown_event_handlers);
			changed |= timeout_event_handlers.addAll(s.timeout_event_handlers);
			changed |= !element_by_id.keySet().equals(s.element_by_id.keySet());
			changed |= !elements_by_name.keySet().equals(
					s.elements_by_name.keySet());
			changed |= !elements_by_tagname.keySet().equals(
					s.elements_by_tagname.keySet());
			for (String id : s.element_by_id.keySet()) {
				Set<ObjectLabel> labels = element_by_id.get(id);
				if (labels == null) {
					labels = newSet();
					element_by_id.put(id, labels);
				}
				changed |= labels.addAll(s.element_by_id.get(id));
			}
			for (String name : s.elements_by_name.keySet()) {
				Set<ObjectLabel> labels = elements_by_name.get(name);
				if (labels == null) {
					labels = newSet();
					elements_by_name.put(name, labels);
				}
				changed |= labels.addAll(s.elements_by_name.get(name));
			}
			for (String tagname : s.elements_by_tagname.keySet()) {
				Set<ObjectLabel> labels = elements_by_tagname.get(tagname);
				if (labels == null) {
					labels = newSet();
					elements_by_tagname.put(tagname, labels);
				}
				changed |= labels.addAll(s.elements_by_tagname.get(tagname));
			}
		}
		if (Options.isDebugEnabled())
			if (Options.isIntermediateStatesEnabled())
				System.out.println("BlockState: join result state: " + this);
			else
				System.out.println("BlockState: join(...)");
		if (Options.isDebugEnabled()) {
			boolean really_changed = !equals(old);
			if (changed != really_changed && diff(old).length() > 0
					|| !really_changed && changed)
				throw new RuntimeException("Spurious state change?!");
		}
		return changed;
	}

	/**
	 * Joins objlabel2 from state2 into objlabel1 in this state. Replaces
	 * 'unknown' when necessary.
	 * 
	 * @return true if changed
	 */
	private boolean join(ObjectLabel objlabel1,
			BlockState<BlockStateType, CallContextType, StatisticsType> state2,
			ObjectLabel objlabel2) {
		makeWritableStore();
		Obj obj1 = getObjectRaw(objlabel1, true);
		Obj obj2 = state2.getObjectRaw(objlabel2, false);
		if (obj1 == null && obj2 == null) // both are bottom
			return false;
		// resolve 'unknown' objects
		if (obj1 == null)
			obj1 = getObject(objlabel1, true);
		if (obj2 == null)
			obj2 = state2.getObject(objlabel2, false);
		if (obj1 == null) { // only relevant without lazy prop
			// object is absent in this state, just copy from input state
			store.put(objlabel1, new Obj(obj2));
			return true;
		} else if (obj2 != null) {
			// object is present both in this state and in the input state, join
			// all properties from obj2 into obj1
			return join(objlabel1, obj1, this, objlabel2, obj2, state2);
		}
		return false;
	}

	/**
	 * Joins obj2 into obj1. Replaces 'unknown' when necessary.
	 * 
	 * @return true if changed
	 */
	private boolean join(ObjectLabel objlabel1, Obj obj1,
			BlockState<BlockStateType, CallContextType, StatisticsType> state1,
			ObjectLabel objlabel2, Obj obj2,
			BlockState<BlockStateType, CallContextType, StatisticsType> state2) { // obj1
																					// and
																					// obj2
																					// may
																					// not
																					// appear
																					// in
																					// state1
																					// and
																					// state2,
																					// so
																					// only
																					// call
																					// UnknownValueResolver
																					// if
																					// really
																					// unknown
		boolean changed = false;
		Set<String> propertynames = newSet();
		propertynames.addAll(obj1.getPropertyNames());
		propertynames.addAll(obj2.getPropertyNames());
		for (String propertyname : propertynames) {
			Value v1 = obj1.readProperty(propertyname);
			Value v1_original = v1;
			Value v2 = obj2.readProperty(propertyname);
			if (v1 != v2) {
				if (v1.isUnknown())
					v1 = UnknownValueResolver.readProperty(objlabel1,
							propertyname, state1);
				if (v2.isUnknown())
					v2 = UnknownValueResolver.readProperty(objlabel2,
							propertyname, state2);
			}
			Value v = v1.join(v2);
			if (!v.equals(v1_original)) {
				obj1.setProperty(propertyname, v);
				changed = true;
			}
		}
		Value default_array_property1 = obj1.getDefaultArrayProperty();
		Value default_array_property2 = obj2.getDefaultArrayProperty();
		if (default_array_property1 != default_array_property2) {
			Value default_array_property1_original = default_array_property1;
			if (default_array_property1.isUnknown())
				default_array_property1 = UnknownValueResolver
						.getDefaultArrayProperty(objlabel1, state1);
			if (default_array_property2.isUnknown())
				default_array_property2 = UnknownValueResolver
						.getDefaultArrayProperty(objlabel2, state2);
			Value v = default_array_property1.join(default_array_property2);
			if (!v.equals(default_array_property1_original)) {
				obj1.setDefaultArrayProperty(v);
				changed = true;
			}
		}
		Value default_nonarray_property1 = obj1.getDefaultNonArrayProperty();
		Value default_nonarray_property2 = obj2.getDefaultNonArrayProperty();
		if (default_nonarray_property1 != default_nonarray_property2) {
			Value default_nonarray_property1_original = default_nonarray_property1;
			if (default_nonarray_property1.isUnknown())
				default_nonarray_property1 = UnknownValueResolver
						.getDefaultNonArrayProperty(objlabel1, state1);
			if (default_nonarray_property2.isUnknown())
				default_nonarray_property2 = UnknownValueResolver
						.getDefaultNonArrayProperty(objlabel2, state2);
			Value v = default_nonarray_property1
					.join(default_nonarray_property2);
			if (!v.equals(default_nonarray_property1_original)) {
				obj1.setDefaultNonArrayProperty(v);
				changed = true;
			}
		}
		Value internal_prototype1 = obj1.getInternalPrototype();
		Value internal_prototype2 = obj2.getInternalPrototype();
		if (internal_prototype1 != internal_prototype2) {
			Value internal_prototype1_original = internal_prototype1;
			if (internal_prototype1.isUnknown())
				internal_prototype1 = UnknownValueResolver
						.getInternalPrototype(objlabel1, state1);
			if (internal_prototype2.isUnknown())
				internal_prototype2 = UnknownValueResolver
						.getInternalPrototype(objlabel2, state2);
			Value v = internal_prototype1.join(internal_prototype2);
			if (!v.equals(internal_prototype1_original)) {
				obj1.setInternalPrototype(v);
				changed = true;
			}
		}
		Value internal_value1 = obj1.getInternalValue();
		Value internal_value2 = obj2.getInternalValue();
		if (internal_value1 != internal_value2) {
			Value internal_value1_original = internal_value1;
			if (internal_value1.isUnknown())
				internal_value1 = UnknownValueResolver.getInternalValue(
						objlabel1, state1);
			if (internal_value2.isUnknown())
				internal_value2 = UnknownValueResolver.getInternalValue(
						objlabel2, state2);
			Value v = internal_value1.join(internal_value2);
			if (!v.equals(internal_value1_original)) {
				obj1.setInternalValue(v);
				changed = true;
			}
		}
		if ((!obj1.isScopeChainUnknown() || !obj2.isScopeChainUnknown())
				&& (obj1.isScopeChainUnknown() || obj2.isScopeChainUnknown() || !obj1
						.getScopeChain().equals(obj2.getScopeChain()))) {
			if (obj1.isScopeChainUnknown())
				changed = true;
			Set<ScopeChain> scope_chain1 = obj1.isScopeChainUnknown() ? UnknownValueResolver
					.getScopeChain(objlabel1, state1) : obj1.getScopeChain();
			Set<ScopeChain> scope_chain2 = obj2.isScopeChainUnknown() ? UnknownValueResolver
					.getScopeChain(objlabel2, state2) : obj2.getScopeChain();
			Set<ScopeChain> new_scope_chain = newSet();
			new_scope_chain.addAll(scope_chain1);
			if (new_scope_chain.addAll(scope_chain2)) {
				obj1.setScopeChain(new_scope_chain);
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Joins the empty object into the given object in this state.
	 */
	private void joinEmpty(ObjectLabel objlabel) {
		makeWritableStore();
		Obj obj = getObject(objlabel, true);
		if (obj == null) { // only relevant without lazy prop
			store.put(objlabel, Obj.makeAbsent());
		} else {
			for (Map.Entry<String, Value> me : newSet(obj.getAllProperties()
					.entrySet())) {
				String propertyname = me.getKey();
				Value v = me.getValue();
				if (v.isUnknown())
					v = UnknownValueResolver.readProperty(objlabel,
							propertyname, this);
				obj.setProperty(propertyname, v.joinAbsent());
			}
			obj.setInternalPrototype(UnknownValueResolver.getInternalPrototype(
					objlabel, this).joinAbsent());
			obj.setInternalValue(UnknownValueResolver.getInternalValue(
					objlabel, this).joinAbsent());
		}
	}

	/**
	 * Returns the value of the given property in the given object. The internal
	 * prototype chains are used. Absent is converted to undefined. Attributes
	 * are set to bottom.
	 */
	public Value readProperty(ObjectLabel objlabel, String propertyname) {
		Value v = readPropertyRaw(objlabel, propertyname, true);
		if (v.isMaybeAbsent())
			v = v.clearAbsent().joinUndef();
		v = v.bottomAttributes();
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readProperty(" + objlabel + ","
					+ propertyname + ") = " + v);
		return v;
	}

	/**
	 * Returns the value of the given property in the given objects. The
	 * internal prototype chains are used. Absent is converted to undefined.
	 * Attributes are cleared.
	 */
	public Value readProperty(Set<ObjectLabel> objlabels, String propertyname) {
		Collection<Value> values = new ArrayList<Value>();
		for (ObjectLabel obj : objlabels)
			values.add(readProperty(obj, propertyname));
		Value v = Value.join(values);
		if (v.isBottom())
			v = Value.makeUndef(v.getDependency());
		return v;
	}

	/**
	 * Returns the value of the given property in the given objects. The
	 * internal prototype chains are used.
	 */
	public Value readPropertyPreserveAttributes(Set<ObjectLabel> objlabels,
			String propertyname) {
		Collection<Value> values = new ArrayList<Value>();
		for (ObjectLabel obj : objlabels)
			values.add(readPropertyRaw(obj, propertyname, false));
		Value v = Value.join(values);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readPropertyPreserveAttributes("
					+ objlabels + "," + propertyname + ") = " + v);
		return v;
	}

	/**
	 * Returns the value of the given property in the given object. The internal
	 * prototype chains are used.
	 */
	private Value readPropertyRaw(ObjectLabel objlabel, String propertyname,
			boolean really_reading) {
		Collection<Value> values = new ArrayList<Value>();
		Set<ObjectLabel> ol = Collections.singleton(objlabel);
		Set<ObjectLabel> visited = newSet();
		while (!ol.isEmpty()) {
			Set<ObjectLabel> ol2 = newSet();
			for (ObjectLabel l : ol)
				if (!visited.contains(l)) {
					visited.add(l);
					Value v = UnknownValueResolver.readProperty(l,
							propertyname, this);
					// FIXME
					/*
					 * if (l.isNative() &&
					 * l.getNativeObjectID().isPropertyMagic(propertyname)) { if
					 * (really_reading) v =
					 * c.evaluateGetter(l.getNativeObjectID(), l, propertyname);
					 * else v = Value.makeUndef(); // present, writable, value
					 * irrelevant in this case } else { v =
					 * UnknownValueResolver.readProperty(l, propertyname, this);
					 * }
					 */
					Value v2 = v.clearAbsent();
					values.add(v2);
					if (really_reading) {
						// TODO: PropertyMonitor.reading(l, propertyname, v2);
					}
					if (v.isMaybeAbsent() || v.isNoValue()) {
						Value proto = UnknownValueResolver
								.getInternalPrototype(l, this);
						ol2.addAll(proto.getObjectLabels());
						if (proto.isMaybeAbsent() || proto.isMaybeNull()) {
							values.add(Value.makeAbsent(proto.getDependency()));
							if (really_reading) {
								// TODO: PropertyMonitor.reading(l,
								// propertyname, Value.makeAbsent());
							}
						}
					}
				}
			ol = ol2;
		}
		return Value.join(values);
	}

	/**
	 * Returns the value of the given property in the given objects. The
	 * internal prototype chains are <i>not</i> used.
	 */
	public Value readPropertyDirect(Set<ObjectLabel> objlabels,
			String propertyname) {
		Collection<Value> values = new ArrayList<Value>();
		for (ObjectLabel obj : objlabels) {
			Value v = UnknownValueResolver
					.readProperty(obj, propertyname, this);
			values.add(v);
			// TODO: PropertyMonitor.reading(obj, propertyname, v);
		}
		Value v = Value.join(values);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readPropertyDirect(" + objlabels
					+ "," + propertyname + ") = " + v);
		return v;
	}

	/**
	 * Checks whether the given property is present in the given object. The
	 * internal prototype chains are used.
	 */
	public Value hasProperty(ObjectLabel objlabel, String propertyname) {
		Value v = readPropertyRaw(objlabel, propertyname, false);
		Value b;
		if (v.isBottom())
			b = Value.makeBottom(v.getDependency());
		else if (v.isNotAbsent())
			b = Value.makeBool(true, v.getDependency());
		else if (v.isNoValue())
			b = Value.makeBool(false, v.getDependency());
		else
			b = Value.makeAnyBool(v.getDependency());
		if (Options.isDebugEnabled())
			System.out.println("BlockState: hasProperty(" + objlabel + ","
					+ propertyname + ") = " + b);
		return b;
	}

	/**
	 * Checks whether the given property is present in the given objects. The
	 * internal prototype chains are used.
	 */
	public Value hasProperty(Set<ObjectLabel> objlabels, String propertyname) {
		Collection<Value> values = new ArrayList<Value>();
		for (ObjectLabel obj : objlabels)
			values.add(hasProperty(obj, propertyname));
		return Value.join(values);
	}

	/**
	 * Checks whether the given property can be assigned in the given object.
	 * The internal prototype chains are used.
	 */
	private Value canPut(ObjectLabel objlabel, String propertyname) { // 8.6.2.3
		Value v = readPropertyRaw(objlabel, propertyname, false);
		Value b;
		if (v.isNoValue() || v.isNotReadOnly())
			b = Value.makeBool(true, v.getDependency());
		else if (v.isReadOnly() && !v.isMaybeAbsent())
			b = Value.makeBool(false, v.getDependency());
		else
			b = Value.makeAnyBool(v.getDependency());
		if (Options.isDebugEnabled())
			System.out.println("BlockState: canPut(" + objlabel + ","
					+ propertyname + ") = " + b);
		return b;
	}

	/**
	 * Returns the join of all values of properties in the given object. The
	 * internal prototype chains are used. Unless preserve_attributes is set,
	 * absent is converted to undefined and attributes are set to bottom.
	 */
	private Value readUnknownProperty(Set<ObjectLabel> objlabels,
			boolean only_array_indices, boolean preserve_attributes) {
		Collection<Value> values = new ArrayList<Value>();
		for (ObjectLabel obj : objlabels)
			values.add(readUnknownProperty(obj, only_array_indices,
					preserve_attributes));
		return Value.join(values);
	}

	/**
	 * Returns the join of all values of properties in the given object. The
	 * internal prototype chains are used. Unless preserve_attributes is set,
	 * absent is converted to undefined and attributes are set to bottom.
	 */
	private Value readUnknownProperty(ObjectLabel objlabel,
			boolean only_array_indices, boolean preserve_attributes) {
		Collection<Value> values = new ArrayList<Value>();
		Set<ObjectLabel> ol = Collections.singleton(objlabel);
		Set<ObjectLabel> visited = newSet();
		while (!ol.isEmpty()) {
			Set<ObjectLabel> ol2 = newSet();
			for (ObjectLabel l : ol)
				if (!visited.contains(l)) {
					visited.add(l);
					Value v = only_array_indices ? readUnknownArrayProperty(l)
							: readUnknownProperty(l);
					// TODO: PropertyMonitor.readingDefault(l, v);
					values.add(v.clearAbsent());
					if (v.isMaybeAbsent()) {
						Value proto = UnknownValueResolver
								.getInternalPrototype(l, this);
						ol2.addAll(proto.getObjectLabels());
						if (proto.isMaybeAbsent() || proto.isMaybeNull())
							values.add(Value.makeAbsent(proto.getDependency()));
					}
				}
			ol = ol2;
		}
		Value res = Value.join(values);
		if (!preserve_attributes) {
			if (res.isMaybeAbsent())
				res = res.clearAbsent().joinUndef();
			res = res.bottomAttributes();
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readUnknownProperty(" + objlabel
					+ "," + only_array_indices + "," + preserve_attributes
					+ ") = " + res);
		return res;
	}

	/**
	 * Returns the join of the values of all properties of an object. The
	 * internal prototype chains and scope chains are <emph>not</emph> used.
	 */
	private Value readUnknownProperty(ObjectLabel objlabel) {
		Collection<Value> values = new ArrayList<Value>();
		Obj fo = UnknownValueResolver.getAllProperties(objlabel, this);
		values.addAll(fo.getAllProperties().values());
		values.add(fo.getDefaultArrayProperty());
		values.add(fo.getDefaultNonArrayProperty());
		return Value.join(values);
	}

	/**
	 * Returns the join of the values of all array indices of an object. The
	 * internal prototype chains and scope chains are <emph>not</emph> used.
	 */
	private Value readUnknownArrayProperty(ObjectLabel objlabel) {
		Collection<Value> values = new ArrayList<Value>();
		Obj fo = UnknownValueResolver.getAllProperties(objlabel, this);
		for (Map.Entry<String, Value> me : fo.getAllProperties().entrySet())
			if (Strings.isArrayIndex(me.getKey()))
				values.add(me.getValue());
		values.add(fo.getDefaultArrayProperty());
		return Value.join(values);
	}

	/**
	 * Returns the join of all values of properties in the given objects. The
	 * internal prototype chains are used. Absent is converted to undefined.
	 * Attributes are set to bottom.
	 */
	public Value readUnknownProperty(Set<ObjectLabel> objlabels) {
		return readUnknownProperty(objlabels, false, false);
	}

	/**
	 * Returns the join of all values of properties in the given objects. The
	 * internal prototype chains are used.
	 */
	public Value readUnknownPropertyPreserveAttributes(
			Set<ObjectLabel> objlabels) {
		return readUnknownProperty(objlabels, false, true);
	}

	/**
	 * Returns the join of all values of array indices in the given objects. The
	 * internal prototype chains are used. Absent is converted to undefined.
	 * Attributes are set to bottom.
	 */
	public Value readUnknownArrayIndex(Set<ObjectLabel> objlabels) {
		return readUnknownProperty(objlabels, true, false);
	}

	/**
	 * Returns the join of all values of array indices in the given objects. The
	 * internal prototype chains are used.
	 */
	public Value readUnknownArrayIndexPreserveAttributes(
			Set<ObjectLabel> objlabels) {
		return readUnknownProperty(objlabels, true, true);
	}

	/**
	 * Removes the given object from the store if there are no references to it
	 * from the active store. (Assumes that the object was created within the
	 * current function!) If lazy propagation is enabled, the object is set to
	 * 'unknown' (the default) instead of absent.
	 */
	public void removeObject(ObjectLabel objlabel) {
		boolean removed = false;
		if (store.containsKey(objlabel)) {
			boolean remove = true;
			// if noone in the active store refers to the object, it has not
			// escaped
			for (Obj obj : store.values())
				if (obj.getAllObjectLabels().contains(objlabel)) {
					remove = false;
					break;
				}
			if (remove) {
				makeWritableStore();
				store.remove(objlabel);
				removed = true;
			}
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: removeObject(" + objlabel
					+ "), removed: " + removed);
	}

	/**
	 * Adds an object label, representing a new empty object, to the store.
	 * Takes recency abstraction into account. Updates sets of summarized
	 * objects.
	 */
	public void newObject(ObjectLabel objlabel) { // FIXME: update
													// id/name/tagname/classname
		if (basis_store != null && basis_store.containsKey(objlabel))
			throw new RuntimeException(
					"Attempt to summarize object from basis store");
		Obj oldobj = getObject(objlabel, false);
		if (objlabel.isSingleton()) {
			if (oldobj != null) {
				// join singleton object into its summary object
				ObjectLabel summarylabel = objlabel.makeSummary();
				join(summarylabel, this, objlabel);
				// update references
				Map<ScopeChain, ScopeChain> cache = new HashMap<ScopeChain, ScopeChain>();
				for (Obj obj : store.values())
					obj.replaceObjectLabel(objlabel, summarylabel, cache);
				execution_context = ExecutionContext.replaceObjectLabel(
						execution_context, objlabel, summarylabel, cache);
				writable_execution_context = true;
				makeWritableTemporaries();
				for (int i = 0; i < temporaries.size(); i++) {
					Value v = temporaries.get(i);
					if (v != null)
						temporaries.set(i,
								v.replaceObjectLabel(objlabel, summarylabel));
				}
				if (Options.isLazyDisabled())
					if (stacked_objlabels.contains(objlabel)) {
						makeWritableStackedObjects();
						stacked_objlabels.remove(objlabel);
						stacked_objlabels.add(summarylabel);
					}
			}
			// now the old object is gone
			summarized.addDefinitelySummarized(objlabel);
			makeWritableStore();
			store.put(objlabel, Obj.makeAbsent());
		} else
			// only relevant if recency abstraction is disabled
			joinEmpty(objlabel);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: newObject(" + objlabel + ")");
	}

	/**
	 * Moves the given object from singleton to summary, such that it represents
	 * an unknown number of concrete objects.
	 */
	public void multiplyObject(ObjectLabel objlabel) {
		if (!store.containsKey(objlabel))
			throw new RuntimeException("Object " + objlabel + " not found!?");
		if (objlabel.isSingleton()) {
			// move the object
			ObjectLabel summarylabel = objlabel.makeSummary();
			join(summarylabel, this, objlabel);
			store.remove(objlabel);
			// update references
			Map<ScopeChain, ScopeChain> cache = new HashMap<ScopeChain, ScopeChain>();
			for (Obj obj : store.values())
				obj.replaceObjectLabel(objlabel, summarylabel, cache);
			execution_context = ExecutionContext.replaceObjectLabel(
					execution_context, objlabel, summarylabel, cache);
			writable_execution_context = true;
			makeWritableTemporaries();
			for (int i = 0; i < temporaries.size(); i++) {
				Value v = temporaries.get(i);
				if (v != null)
					temporaries.set(i,
							v.replaceObjectLabel(objlabel, summarylabel));
			}
			if (Options.isLazyDisabled())
				if (stacked_objlabels.contains(objlabel)) {
					makeWritableStackedObjects();
					stacked_objlabels.remove(objlabel);
					stacked_objlabels.add(summarylabel);
				}
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: multiplyObject(" + objlabel + ")");
	}

	/**
	 * Assigns the given value to all properties of the given objects (weak
	 * update). Attributes are cleared or copied from the old value according to
	 * 8.6.2.2.
	 */
	public void writeUnknownProperty(Collection<ObjectLabel> objlabels,
			Value value) {
		value.assertNonEmpty();
		for (ObjectLabel objlabel : objlabels) {
			Obj obj = getPresentObject(objlabel, true);
			for (String propertyname : newSet(obj.getPropertyNames()))
				weakWritePropertyIfCanPut(objlabel, propertyname, value, true);
			writeDefaultArrayIndexProperty(objlabel, value, true);
			writeDefaultNonArrayIndexProperty(objlabel, value, true);
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeUnknownProperty(" + objlabels
					+ "," + value + ")");
	}

	/**
	 * Deletes an unknown array index property (weak update).
	 */
	public void deleteUnknownArrayProperty(Collection<ObjectLabel> objlabels) {
		for (ObjectLabel objlabel : objlabels) {
			Obj obj = getPresentObject(objlabel, true);
			for (String propertyname : newSet(obj.getPropertyNames()))
				if (Strings.isArrayIndex(propertyname))
					weakWritePropertyIfCanPut(objlabel, propertyname,
							Value.makeAbsent(new Dependency()), true);
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: deleteUnknownArrayProperty("
					+ objlabels + ")");
	}

	/**
	 * Assigns the given value to all array index properties of the given
	 * objects (weak update). Attributes are cleared or copied from the old
	 * value according to 8.6.2.2.
	 */
	public void writeUnknownArrayProperty(Collection<ObjectLabel> objlabels,
			Value value) {
		value.assertNonEmpty();
		for (ObjectLabel objlabel : objlabels) {
			Obj obj = getPresentObject(objlabel, true);
			for (String propertyname : newSet(obj.getPropertyNames()))
				if (Strings.isArrayIndex(propertyname))
					weakWritePropertyIfCanPut(objlabel, propertyname, value,
							true);
			writeDefaultArrayIndexProperty(objlabel, value, true);
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeUnknownArrayProperty("
					+ objlabels + "," + value + ")");
	}

	/**
	 * Assigns the given value to all array index properties of the given object
	 * (weak update). Attributes are cleared or copied from the old value
	 * according to 8.6.2.2.
	 */
	public void writeUnknownArrayProperty(ObjectLabel objlabel, Value value) {
		writeUnknownArrayProperty(Collections.singleton(objlabel), value);
	}

	/**
	 * Assigns the given value to all array index properties of the given object
	 * (weak update). Attributes are taken from the given value.
	 */
	public void writeSpecialUnknownArrayProperty(ObjectLabel objlabel,
			Value value) {
		Obj obj = getPresentObject(objlabel, true);
		for (String propertyname : newSet(obj.getPropertyNames()))
			if (Strings.isArrayIndex(propertyname))
				weakWritePropertyIfCanPut(objlabel, propertyname, value, false);
		writeDefaultArrayIndexProperty(objlabel, value, false);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeSpecialUnknownArrayProperty("
					+ objlabel + "," + value + "," + value.printAttributes()
					+ ")");
	}

	/**
	 * Assigns the given value to the default non-array index property of an
	 * object (weak update).
	 * 
	 * @param put
	 *            if set, attributes are cleared or copied from the old value
	 *            according to 8.6.2.2.
	 */
	public void writeDefaultNonArrayIndexProperty(ObjectLabel objlabel,
			Value value, boolean put) {
		if (put)
			value = value.removeAttributes(); // 8.6.2.2 item 6
		Obj obj = getPresentObject(objlabel, true);
		obj.setDefaultNonArrayProperty(obj.getDefaultNonArrayProperty()
				.joinWithModified(value));
	}

	/**
	 * Assigns the given value to the default array index property of an object
	 * (weak update).
	 * 
	 * @param put
	 *            if set, attributes are cleared or copied from the old value
	 *            according to 8.6.2.2.
	 */
	public void writeDefaultArrayIndexProperty(ObjectLabel objlabel,
			Value value, boolean put) {
		if (put)
			value = value.removeAttributes(); // 8.6.2.2 item 6
		Obj obj = getPresentObject(objlabel, true);
		obj.setDefaultArrayProperty(obj.getDefaultArrayProperty()
				.joinWithModified(value));
	}

	/**
	 * Assigns the given value to the given property of the given objects.
	 * Attributes are cleared or copied from the old value according to 8.6.2.2.
	 */
	public void writeProperty(Collection<ObjectLabel> objlabels,
			String propertyname, Value value) {
		writePropertyInternal(objlabels, propertyname, value, true);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeProperty(" + objlabels + ","
					+ propertyname + "," + value + ")");
	}

	/**
	 * Assigns the given value to the given property of the given object.
	 * Attributes are cleared or copied from the old value according to 8.6.2.2.
	 */
	public void writeProperty(ObjectLabel objlabel, String propertyname,
			Value value) {
		writeProperty(Collections.singleton(objlabel), propertyname, value);
	}

	/**
	 * Assigns the given value to the given property of the given objects, with
	 * attributes. Attributes are taken from the given value.
	 */
	public void writeSpecialProperty(Collection<ObjectLabel> objlabels,
			String propertyname, Value value) {
		writePropertyInternal(objlabels, propertyname, value, false);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeSpecialProperty(" + objlabels
					+ "," + propertyname + "," + value + ","
					+ value.printAttributes() + ")");
	}

	/**
	 * Assigns the given value to the given property of the given object, with
	 * attributes. Attributes are taken from the given value.
	 */
	public void writeSpecialProperty(ObjectLabel objlabel, String propertyname,
			Value value) {
		writeSpecialProperty(Collections.singleton(objlabel), propertyname,
				value);
	}

	/**
	 * Assigns the given value to the given property of the given objects, with
	 * attributes.
	 * 
	 * @param put
	 *            if set, attributes are cleared or copied from the old value
	 *            according to 8.6.2.2.
	 */
	private void writePropertyInternal(Collection<ObjectLabel> objlabels,
			String p, Value value, boolean put) {
		if (p == null)
			throw new NullPointerException();
		// TODO: report mixed types?
		// if (value.getObjectLabels().size() > 0 && !value.isNotNum()) {
		// System.out.println("MIXING OBJ/NUM: " + value);
		// }
		if (objlabels.size() == 1) {
			ObjectLabel objlabel = objlabels.iterator().next();
			if (objlabel.isSingleton())
				strongWritePropertyIfCanPut(objlabel, p, value, put);
			else
				weakWritePropertyIfCanPut(objlabel, p, value, put);
		} else
			for (ObjectLabel objlabel : objlabels)
				weakWritePropertyIfCanPut(objlabel, p, value, put);
	}

	/**
	 * Assigns the given value to the given object property if [[CanPut]] is
	 * true (weak update). Takes ReadOnly into account.
	 * 
	 * @param put
	 *            if set, attributes are cleared or copied from the old value
	 *            according to 8.6.2.2.
	 */
	private void weakWritePropertyIfCanPut(ObjectLabel objlabel,
			String propertyname, Value value, boolean put) {
		if (!put || canPut(objlabel, propertyname).isMaybeTrue())
			weakWriteProperty(objlabel, propertyname, value, put);
		// TODO: generate warning/error if [[CanPut]] gives false
	}

	/**
	 * Assigns the given value to the given property of an object (weak update).
	 * 
	 * @param put
	 *            if set, attributes are cleared or copied from the old value
	 *            according to 8.6.2.2.
	 */
	private void weakWriteProperty(ObjectLabel objlabel, String propertyname,
			Value value, boolean put) {
		Value oldvalue = UnknownValueResolver.readProperty(objlabel,
				propertyname, this);
		if (put) {
			value = value.bottomAttributes(); // 8.6.2.2 item 4
			if (oldvalue.isMaybeAbsent() || oldvalue.isNoValue())
				value = value.removeAttributes(); // 8.6.2.2 item 6
		}
		Value newvalue = oldvalue.joinWithModified(value);
		if (!oldvalue.equals(newvalue)) {
			checkProperty(newvalue);
			if (objlabel.isNative()
					&& objlabel.getNativeObjectID().hasSetter(propertyname)) {
				c.evaluateSetter(objlabel.getNativeObjectID(), objlabel,
						propertyname, newvalue);
			}

			getPresentObject(objlabel, true)
					.setProperty(propertyname, newvalue);
		}
	}

	/**
	 * Checks for the given value that attributes are non-bottom if the value is
	 * nonempty.
	 */
	private void checkProperty(Value v) {
		if (v.isMaybeValue()
				&& (!v.hasDontDelete() || !v.hasDontEnum() || !v.hasReadOnly()))
			throw new RuntimeException(
					"Missing attribute information at property value " + v);
	}

	/**
	 * Assigns the given value to the given object property if [[CanPut]] is
	 * true (strong update). Takes ReadOnly into account.
	 * 
	 * @param put
	 *            if set, attributes are cleared or copied from the old value
	 *            according to 8.6.2.2.
	 */
	private void strongWritePropertyIfCanPut(ObjectLabel objlabel,
			String propertyname, Value value, boolean put) {
		value.assertNonEmpty();
		Value canput = put ? canPut(objlabel, propertyname) : Value.makeBool(
				true, value.getDependency()); // #########
		if (canput.isMaybeTrueButNotFalse())
			strongWriteProperty(objlabel, propertyname, value, put);
		else if (canput.isMaybeTrue())
			weakWriteProperty(objlabel, propertyname, value, put);
		// TODO: generate warning/error if [[CanPut]] gives false
	}

	/**
	 * Assigns the given value to the given property of an object (strong
	 * update).
	 * 
	 * @param put
	 *            if set, attributes are cleared or copied from the old value
	 *            according to 8.6.2.2.
	 */
	private void strongWriteProperty(ObjectLabel objlabel, String propertyname,
			Value value, boolean put) {
		Value oldvalue = UnknownValueResolver.readProperty(objlabel,
				propertyname, this);
		if (put)
			if (oldvalue.isNoValue())
				value = value.removeAttributes(); // 8.6.2.2 item 6
			else
				value = value.setAttributes(oldvalue); // 8.6.2.2 item 4
		if (value.equals(oldvalue))
			return;

		if (objlabel.isNative()
				&& objlabel.getNativeObjectID().hasSetter(propertyname)) {
			c.evaluateSetter(objlabel.getNativeObjectID(), objlabel,
					propertyname, value);
		}

		getPresentObject(objlabel, true).setProperty(propertyname,
				value.joinModified());

		// TODO: collect statistics, e.g. largest number of properties
		// (strongWriteProperty, weakWriteProperty, join)
	}

	/**
	 * Deletes the given property. Ignored if the property has attribute
	 * DontDelete. Returns false if attempting to delete a property with
	 * attribute DontDelete, true otherwise.
	 */
	public Value deleteProperty(Set<ObjectLabel> objlabels, String propertyname) { // 8.6.2.5
		if (objlabels.size() == 1) {
			ObjectLabel objlabel = objlabels.iterator().next();
			if (objlabel.isSingleton())
				return strongDeleteProperty(objlabel, propertyname);
		}
		Value res = Value.makeBottom(new Dependency());
		for (ObjectLabel objlabel : objlabels) {
			Value v = UnknownValueResolver.readProperty(objlabel, propertyname,
					this);
			res = res.joinDependency(v.getDependency());

			if (v.isMaybeAbsent() || v.isMaybeNotDontDelete() || v.isNoValue())
				res = res.joinBool(true);
			if (v.isMaybeDontDelete())
				res = res.joinBool(false);
			weakDeleteProperty(objlabel, propertyname);
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: deleteProperty(" + objlabels + ","
					+ propertyname + ") = " + res);
		return res;
	}

	/**
	 * Deletes the given property of an object, unless DontDelete is set (weak
	 * update).
	 */
	private void weakDeleteProperty(ObjectLabel objlabel, String propertyname) {
		Value v = UnknownValueResolver.readProperty(objlabel, propertyname,
				this);
		if (v.isMaybeValue() && !v.isDontDelete())
			getPresentObject(objlabel, true).setProperty(propertyname,
					v.joinAbsentWithModified());
	}

	/**
	 * Deletes the given property of an object unless DontDelete is set (strong
	 * update).
	 * 
	 * @return success of the deletion
	 */
	private Value strongDeleteProperty(ObjectLabel objlabel, String propertyname) {
		Value v = UnknownValueResolver.readProperty(objlabel, propertyname,
				this);
		if (!v.isMaybeValue())
			return Value.makeBool(true, v.getDependency());
		Obj obj = getPresentObject(objlabel, true);
		if (v.isNotDontDelete()) { // can delete
			obj.setProperty(propertyname,
					Value.makeAbsentModified(v.getDependency())); // FIXME:
			// deleting
			// magic
			// properties??
			// (also
			// other
			// places...)
			return Value.makeBool(true, v.getDependency());
		} else if (v.isDontDelete() && !v.isMaybeAbsent()) // present but can't
															// delete
			return Value.makeBool(false, v.getDependency());
		else { // don't know, maybe delete
			obj.setProperty(propertyname, v.joinAbsentWithModified());
			return Value.makeAnyBool(v.getDependency());
		}
	}

	/**
	 * Deletes an unknown property. Ignored for properties that have attribute
	 * DontDelete. Takes recency abstraction into account.
	 */
	public Value deleteUnknownProperty(Set<ObjectLabel> objlabels) {
		Value res = Value.makeBottom(new Dependency());
		for (ObjectLabel objlabel : objlabels) {
			res = res.joinDependency(readUnknownProperty(objlabel)
					.getDependency());
			if (!readUnknownProperty(objlabel).isMaybeDontDelete())
				res = res.joinBool(true);
			else
				res = Value.makeAnyBool(readUnknownProperty(objlabel)
						.getDependency());
			weakDeleteUnknownProperty(objlabel);
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: deleteUnknownProperty(" + objlabels
					+ ") = " + res);
		return res;
	}

	/**
	 * Deletes an unknown property of an object, excluding those with DontDelete
	 * (weak update).
	 */
	private void weakDeleteUnknownProperty(ObjectLabel objlabel) {
		Obj obj = getPresentObject(objlabel, true);
		Obj fo = UnknownValueResolver.getAllProperties(objlabel, this);
		for (Map.Entry<String, Value> me : fo.getAllProperties().entrySet()) {
			String propertyname = me.getKey();
			Value v = me.getValue();
			if (!v.isDontDelete())
				obj.setProperty(propertyname, v.joinAbsentWithModified());
		}
		if (!fo.getDefaultArrayProperty().isNoValue())
			obj.setDefaultArrayProperty(fo.getDefaultArrayProperty()
					.joinModified());
		if (!fo.getDefaultNonArrayProperty().isNoValue())
			obj.setDefaultNonArrayProperty(fo.getDefaultNonArrayProperty()
					.joinModified());
	}

	/**
	 * Assigns the given value to the internal prototype links of the given
	 * objects. Takes recency abstraction into account.
	 */
	public void writeInternalPrototype(Collection<ObjectLabel> objlabels,
			Value value) {
		value.assertNonEmpty();
		if (objlabels.size() == 1) {
			ObjectLabel objlabel = objlabels.iterator().next();
			if (objlabel.isSingleton())
				strongWriteInternalPrototype(objlabel, value);
			else
				weakWriteInternalPrototype(objlabel, value);
		} else
			for (ObjectLabel objlabel : objlabels)
				weakWriteInternalPrototype(objlabel, value);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeInternalPrototype("
					+ objlabels + "," + value + ")");
	}

	/**
	 * Assigns the given value to the internal prototype link of an object
	 * (strong update).
	 */
	private void strongWriteInternalPrototype(ObjectLabel objlabel, Value v) {
		Value oldval = UnknownValueResolver
				.getInternalPrototype(objlabel, this);
		if (oldval.equals(v))
			return;
		getPresentObject(objlabel, true).setInternalPrototype(v.joinModified());
	}

	/**
	 * Assigns the given value to the internal prototype link of an object (weak
	 * update).
	 */
	private void weakWriteInternalPrototype(ObjectLabel objlabel, Value v) {
		Value oldval = UnknownValueResolver
				.getInternalPrototype(objlabel, this);
		if (oldval.equals(v))
			return;
		getPresentObject(objlabel, true).setInternalPrototype(
				oldval.joinWithModified(v));
	}

	/**
	 * Assigns the given value to the internal prototype link of the given
	 * object. Takes recency abstraction into account.
	 */
	public void writeInternalPrototype(ObjectLabel objlabel, Value value) {
		writeInternalPrototype(Collections.singleton(objlabel), value);
	}

	/**
	 * Assign the given value to the internal value property of the given
	 * object.
	 */
	public void writeInternalValue(ObjectLabel objlabel, Value value) {
		value.assertNonEmpty();
		if (objlabel.isSingleton())
			strongWriteInternalValue(objlabel, value);
		else
			weakWriteInternalValue(objlabel, value);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeInternalValue(" + objlabel
					+ "," + value + ")");
	}

	/**
	 * Sets the internal [[Value]] property of an object.
	 */
	private void strongWriteInternalValue(ObjectLabel objlabel, Value v) {
		Value oldval = UnknownValueResolver.getInternalValue(objlabel, this);
		if (oldval.equals(v))
			return;
		getPresentObject(objlabel, true).setInternalValue(v.joinModified());
	}

	/**
	 * Weakly updates the internal [[Value]] property of an object.
	 */
	private void weakWriteInternalValue(ObjectLabel objlabel, Value v) {
		Value oldval = UnknownValueResolver.getInternalValue(objlabel, this);
		if (oldval.equals(v))
			return;
		getPresentObject(objlabel, true).setInternalValue(
				oldval.joinWithModified(v));
	}

	/**
	 * Returns the value of the internal value property of the given objects.
	 */
	public Value readInternalValue(Set<ObjectLabel> objlabels) {
		Collection<Value> values = new ArrayList<Value>();
		for (ObjectLabel obj : objlabels)
			values.add(UnknownValueResolver.getInternalValue(obj, this));
		Value v = Value.join(values);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readInternalValue(" + objlabels
					+ ") = " + v);
		return v;
	}

	/**
	 * Returns the value of the internal prototype of the given object.
	 */
	public Value readInternalPrototype(ObjectLabel objlabel) {
		return readInternalPrototype(Collections.singleton(objlabel));
	}

	/**
	 * Returns the value of the internal prototype of the given objects.
	 */
	public Value readInternalPrototype(Set<ObjectLabel> objlabels) {
		Collection<Value> values = new ArrayList<Value>();
		for (ObjectLabel obj : objlabels)
			values.add(UnknownValueResolver.getInternalPrototype(obj, this));
		Value v = Value.join(values);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readInternalPrototype(" + objlabels
					+ ") = " + v);
		return v;
	}

	/**
	 * Returns the value of the internal scope property of the given objects.
	 */
	public Set<ScopeChain> readObjectScope(ObjectLabel objlabel) {
		Set<ScopeChain> scope = UnknownValueResolver.getScopeChain(objlabel,
				this);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readObjectScope(" + objlabel
					+ ") = " + scope);
		return Collections.unmodifiableSet(scope);
	}

	/**
	 * Assigns a copy of the given scope chain to the internal scope property of
	 * the given object.
	 */
	public void writeObjectScope(ObjectLabel objlabel, Set<ScopeChain> scope) {
		if (objlabel.getKind().equals(Kind.FUNCTION) && !objlabel.isNative()
				&& scope.isEmpty())
			throw new RuntimeException("Empty scope chain for function!?");
		getPresentObject(objlabel, true).setScopeChain(newSet(scope));
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeObjectScope(" + objlabel + ","
					+ scope + ")");
	}

	/**
	 * Returns the scope chain.
	 */
	public Set<ScopeChain> getScopeChain() {
		Set<ScopeChain> scope = newSet();
		for (ExecutionContext e : execution_context)
			scope.add(e.getScopeChain());
		if (Options.isDebugEnabled())
			System.out.println("BlockState: getScopeChain() = " + scope);
		return scope;
	}

	/**
	 * Returns the execution context.
	 */
	public Set<ExecutionContext> getExecutionContext() {
		if (Options.isDebugEnabled())
			System.out.println("BlockState: getExecutionContext() = "
					+ execution_context);
		return Collections.unmodifiableSet(execution_context);
	}

	/**
	 * Sets the execution context.
	 */
	public void setExecutionContext(ExecutionContext e) {
		setExecutionContext(Collections.singleton(e));
	}

	/**
	 * Sets the execution context.
	 */
	public void setExecutionContext(Set<ExecutionContext> es) {
		execution_context = es;
		writable_execution_context = true;
		if (Options.isDebugEnabled())
			System.out.println("BlockState: setExecutionContext(" + es + ")");
	}

	/**
	 * Checks whether the given state is equal to this one.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof BlockState))
			return false;
		BlockState<BlockStateType, CallContextType, StatisticsType> x = (BlockState<BlockStateType, CallContextType, StatisticsType>) obj;
		if (basis_store != x.basis_store) {
			if (Options.isDebugEnabled())
				System.out
						.println("BlockState: equals(...)=false, not identical basis stores");
			return false;
		}
		for (Map.Entry<ObjectLabel, Obj> me : store.entrySet()) {
			Obj xo = x.getObjectRaw(me.getKey(), false);
			if (xo == null || !xo.equals(me.getValue())) {
				if (Options.isDebugEnabled()) {
					System.out
							.println("BlockState: equals(...)=false, stores differ on "
									+ me.getKey());
				}
				return false;
			}
		}
		for (Map.Entry<ObjectLabel, Obj> me : x.store.entrySet()) {
			Obj to = getObjectRaw(me.getKey(), false);
			if (to == null || !to.equals(me.getValue())) {
				if (Options.isDebugEnabled()) {
					System.out
							.println("BlockState: equals(...)=false, stores differ on "
									+ me.getKey());
				}
				return false;
			}
		}
		if (!execution_context.equals(x.execution_context)) {
			if (Options.isDebugEnabled())
				System.out
						.println("BlockState: equals(...)=false, execution contexts differ");
			return false;
		}
		if (!summarized.equals(x.summarized)) {
			if (Options.isDebugEnabled())
				System.out
						.println("BlockState: equals(...)=false, summarized sets differ");
			return false;
		}
		if (!temporaries.equals(x.temporaries)) {
			if (Options.isDebugEnabled())
				System.out
						.println("BlockState: equals(...)=false, temporaries differ");
			return false;
		}
		if (!stacked_objlabels.equals(x.stacked_objlabels)) {
			if (Options.isDebugEnabled())
				System.out
						.println("BlockState: equals(...)=false, stacked temporaries object labels differ");
			return false;
		}
		if (Options.isDOMEnabled()) {
			if (!load_event_handlers_definite
					.equals(x.load_event_handlers_definite)) {
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: equals(...)=false, load event handlers differ");
				return false;
			}
			if (!load_event_handlers_maybe.equals(x.load_event_handlers_maybe)) {
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: equals(...)=false, load event handlers differ");
				return false;
			}
			if (!unload_event_handlers.equals(x.unload_event_handlers)) {
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: equals(...)=false, unload event handlers differ");
				return false;
			}
			if (!keyboard_event_handlers.equals(x.keyboard_event_handlers)) {
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: equals(...)=false, keyboard event handlers differ");
				return false;
			}
			if (!mouse_event_handlers.equals(x.mouse_event_handlers)) {
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: equals(...)=false, mouse event handlers differ");
				return false;
			}
			if (!unknown_event_handlers.equals(x.unknown_event_handlers)) {
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: equals(...)=false, unknown event handlers differ");
				return false;
			}
			if (!timeout_event_handlers.equals(x.timeout_event_handlers)) {
				if (Options.isDebugEnabled())
					System.out
							.println("BlockState: equals(...)=false, timeout event handlers differ");
				return false;
			}
			if (!element_by_id.equals((x.element_by_id))) {
				if (Options.isDebugEnabled()) {
					System.out
							.println("BlockState: equals(...)=false, elements_by_id differ");
				}
				return false;
			}
			for (String id : element_by_id.keySet()) {
				if (!element_by_id.get(id).equals(x.element_by_id.get(id))) {
					if (Options.isDebugEnabled()) {
						System.out
								.println("BlockState: equals(...)=false, elements_by_id: "
										+ id + " differ");
					}
					return false;
				}
			}
			if (!elements_by_name.equals(x.elements_by_name)) {
				if (Options.isDebugEnabled()) {
					System.out
							.println("BlockState: equals(...)=false, elements_by_name differ");
				}
				return false;
			}
			for (String name : elements_by_name.keySet()) {
				if (!elements_by_name.get(name).equals(
						x.elements_by_name.get(name))) {
					if (Options.isDebugEnabled()) {
						System.out
								.println("BlockState: equals(...)=false, elements_by_name: "
										+ name + " differ");
					}
					return false;
				}
			}
			if (!elements_by_tagname.equals(x.elements_by_tagname)) {
				if (Options.isDebugEnabled()) {
					System.out
							.println("BlockState: equals(...)=false, elements_by_tagname differ");
				}
				return false;
			}
			for (String tagname : elements_by_tagname.keySet()) {
				if (!elements_by_tagname.get(tagname).equals(
						x.elements_by_tagname.get(tagname))) {
					if (Options.isDebugEnabled()) {
						System.out
								.println("BlockState: equals(...)=false, elements_by_name: "
										+ tagname + " differ");
					}
					return false;
				}
			}
		}
		if (Options.isDebugEnabled())
			System.out.println("BlockState: equals(...)=true");
		return true;
	}

	protected String diff(
			BlockState<BlockStateType, CallContextType, StatisticsType> old) {
		StringBuilder b = new StringBuilder();
		for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store)) {
			Obj xo = old.getObject(me.getKey(), false);
			if (xo == null) // only relevant without lazy prop
				b.append("\n      new object: ").append(me.getKey())
						.append(" at ").append(me.getKey().getSourceLocation());
			else if (!me.getValue().equals(xo)) {
				b.append("\n      changed object ").append(me.getKey())
						.append(" at ").append(me.getKey().getSourceLocation())
						.append(": ");
				me.getValue().diff(xo, b);
			}
		}
		for (ExecutionContext ec : execution_context)
			if (!old.execution_context.contains(ec))
				b.append("\n      new execution context: ").append(ec);
		Set<ObjectLabel> temp = newSet(summarized.getMaybeSummarized());
		temp.removeAll(old.summarized.getMaybeSummarized());
		if (!temp.isEmpty())
			b.append("\n      new maybe-summarized: ").append(temp);
		temp = newSet(summarized.getDefinitelySummarized());
		temp.removeAll(old.summarized.getDefinitelySummarized());
		if (!temp.isEmpty())
			b.append("\n      new definitely-summarized: ").append(temp);
		temp = newSet(stacked_objlabels);
		temp.removeAll(old.stacked_objlabels);
		if (!temp.isEmpty())
			b.append("\n      new stacked object labels: ").append(temp);
		if (!temporaries.equals(old.temporaries))
			b.append("\n      temporaries changed");
		if (Options.isDOMEnabled()) {
			// temp = newSet(load_event_handlers);
			// temp.removeAll(old.load_event_handlers);
			temp = newSet(load_event_handlers_definite);
			temp.removeAll(old.load_event_handlers_definite);
			temp = newSet(load_event_handlers_maybe);
			temp.removeAll(old.load_event_handlers_maybe);
			if (!temp.isEmpty()) {
				b.append("\n      new load event handlers: ").append(temp);
			}

			temp = newSet(unload_event_handlers);
			temp.removeAll(old.unload_event_handlers);
			if (!temp.isEmpty()) {
				b.append("\n      new unload event handlers: ").append(temp);
			}

			temp = newSet(keyboard_event_handlers);
			temp.removeAll(old.keyboard_event_handlers);
			if (!temp.isEmpty()) {
				b.append("\n      new keyboard event handlers: ").append(temp);
			}

			temp = newSet(mouse_event_handlers);
			temp.removeAll(old.mouse_event_handlers);
			if (!temp.isEmpty()) {
				b.append("\n      new mouse event handlers: ").append(temp);
			}

			temp = newSet(unknown_event_handlers);
			temp.removeAll(old.unknown_event_handlers);
			if (!temp.isEmpty()) {
				b.append("\n      new unknown event handlers: ").append(temp);
			}

			temp = newSet(timeout_event_handlers);
			temp.removeAll(old.timeout_event_handlers);
			if (!temp.isEmpty()) {
				b.append("\n      new timeout event handlers: ").append(temp);
			}

			Set<String> elms = newSet(element_by_id.keySet());
			if (!element_by_id.equals(old.element_by_id)) {
				b.append("\n      new elements_by_id: ").append(
						elms.removeAll(old.element_by_id.keySet()));
			}
			elms = newSet(elements_by_name.keySet());
			if (!elements_by_name.equals((old.elements_by_name))) {
				b.append("\n      new elements_by_name: ").append(
						elms.removeAll(old.elements_by_name.keySet()));
			}
			elms = newSet(elements_by_tagname.keySet());
			if (!elements_by_tagname.equals(old.elements_by_tagname)) {
				b.append("\n      new elements_by_tagname: ").append(
						elms.removeAll(old.elements_by_tagname.keySet()));
			}
		}
		return b.toString();
	}

	/**
	 * Computed the hash code for this state.
	 */
	@Override
	public int hashCode() {
		return execution_context.hashCode()
				* 7
				+ store.hashCode()
				* 2
				+ summarized.hashCode()
				* 37
				+ +temporaries.hashCode()
				* 5
				+ stacked_objlabels.hashCode()
				* 17
				// + (Options.isDOMEnabled() ? load_event_handlers.hashCode() :
				// 0)
				+ (Options.isDOMEnabled() ? load_event_handlers_definite
						.hashCode() : 0)
				* 57
				+ (Options.isDOMEnabled() ? load_event_handlers_maybe
						.hashCode() : 0)
				* 89
				+ (Options.isDOMEnabled() ? unload_event_handlers.hashCode()
						: 0)
				* 19
				+ (Options.isDOMEnabled() ? keyboard_event_handlers.hashCode()
						: 0)
				* 59
				+ (Options.isDOMEnabled() ? mouse_event_handlers.hashCode() : 0)
				* 61
				+ (Options.isDOMEnabled() ? unknown_event_handlers.hashCode()
						: 0)
				* 43
				+ (Options.isDOMEnabled() ? timeout_event_handlers.hashCode()
						: 0) * 29
				+ (Options.isDOMEnabled() ? element_by_id.hashCode() : 0) * 47
				+ (Options.isDOMEnabled() ? elements_by_name.hashCode() : 0)
				* 53
				+ (Options.isDOMEnabled() ? elements_by_tagname.hashCode() : 0)
				* 57;
	}

	/**
	 * Returns a description of this abstract state.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("Abstract state:");
		b.append("\n  Execution context: ").append(execution_context);
		b.append("\n  Summarized: ").append(summarized);
		b.append("\n  Store (excluding basis and unknown objects): ");
		for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store))
			b.append("\n    ").append(me.getKey()).append(" (")
					.append(me.getKey().getSourceLocation()).append("): ")
					.append(me.getValue()).append("");
		b.append("\n  Temporaries: ");
		for (int i = 0; i < temporaries.size(); i++)
			if (temporaries.get(i) != null)
				b.append("\n    v").append(i).append("=")
						.append(temporaries.get(i));
		if (Options.isDOMEnabled()) {
			// b.append("\n  Load Event Handlers: ").append(load_event_handlers);
			b.append("\n  Load Event Handlers: ").append(
					load_event_handlers_definite);
			b.append("\n  Load Event Handlers: ").append(
					load_event_handlers_maybe);
			b.append("\n  Unload Event Handlers: ").append(
					unload_event_handlers);
			b.append("\n  Keyboard Event Handlers: ").append(
					keyboard_event_handlers);
			b.append("\n  Mouse Event Handlers: ").append(mouse_event_handlers);
			b.append("\n  Unknown Event Handlers: ").append(
					unknown_event_handlers);
			b.append("\n  Timeout Event Handlers: ").append(
					timeout_event_handlers);
			b.append("\n  Elements by id: ").append(element_by_id);
			b.append("\n  Elements by name: ").append(elements_by_name);
			b.append("\n  Elements by tagname: ").append(elements_by_tagname);
		}
		if (Options.isLazyDisabled())
			b.append("\n  Objects used by outer scopes: ").append(
					stacked_objlabels);
		return b.toString();
	}

	/**
	 * Prints the objects of the given value.
	 */
	public String printObject(Value v) {
		StringBuilder b = new StringBuilder();
		for (ObjectLabel obj : new TreeSet<ObjectLabel>(v.getObjectLabels())) {
			if (b.length() > 0)
				b.append(", ");
			b.append(getPresentObject(obj, false))/*
												 * TODO .append(" at ").append(
												 * obj.getSourceLocation())
												 */;
		}
		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brics.tajs.solver.IBlockState#analyzeDependencies()
	 */
	public ArrayList<HashMap<String, Dependency>> analyzeDependencies() {
		ArrayList<HashMap<String, Dependency>> analysisResults = new ArrayList<HashMap<String, Dependency>>();

		for (ObjectLabel label : store.keySet()) {
			Obj obj = store.get(label);
			HashMap<String, Dependency> values = obj.analyzeDependencies();
			analysisResults.add(values);
		}

		return analysisResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brics.tajs.solver.IBlockState#analyzeDependencyReferences()
	 */
	public ArrayList<HashMap<String, DependencyGraphReference>> analyzeDependencyReferences() {
		ArrayList<HashMap<String, DependencyGraphReference>> analysisResults = new ArrayList<HashMap<String, DependencyGraphReference>>();

		for (ObjectLabel label : store.keySet()) {
			Obj obj = store.get(label);
			HashMap<String, DependencyGraphReference> values = obj
					.analyzeDependencyReferences();

			analysisResults.add(values);
		}

		return analysisResults;
	}

	/**
	 * Prints the source locations of the origins of the objects of the given
	 * value.
	 */
	public String printObjectOrigins(Value v) {
		StringBuilder b = new StringBuilder();
		for (ObjectLabel obj : v.getObjectLabels()) {
			if (b.length() > 0)
				b.append(", ");
			b.append(obj.getSourceLocation());
		}
		return b.toString();
	}

	/**
	 * As {link {@link #toString()} but excludes temporaries and non-modified
	 * objects and properties.
	 */
	@Override
	public String toStringBrief() {
		StringBuilder b = new StringBuilder("Abstract state:");
		b.append("\n  Execution context: ").append(execution_context);
		b.append("\n  Summarized: ").append(summarized);
		b.append("\n  Store (excluding non-modified): ");
		printModifiedStore(b);
		return b.toString();
	}

	/**
	 * Prints the modified parts of the store.
	 */
	public String toStringModified() {
		StringBuilder b = new StringBuilder();
		printModifiedStore(b);
		return b.toString();
	}

	/**
	 * Prints the modified parts of the store.
	 */
	private void printModifiedStore(StringBuilder b) {
		for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store))
			b.append("\n  ").append(me.getKey()).append(" (")
					.append(me.getKey().getSourceLocation()).append("):")
					.append(me.getValue().printModified());
	}

	/**
	 * Produces a graphviz dot representation of this state.
	 * 
	 * @param dom
	 *            if true, only output DOM objects
	 */
	public String toDot(boolean dom) { // FIXME: make toDot work with dom=false,
										// used by dumpState
		StringBuilder ns = new StringBuilder("\n\t/* Nodes */\n");
		StringBuilder es = new StringBuilder("\n\t/* Edges */\n");

		for (Map.Entry<ObjectLabel, Obj> e : sortedEntries(store)) {
			ObjectLabel label = e.getKey();
			Obj object = e.getValue();
			if (dom && !label.isNative()) {
				// Ignore non-native objects
				continue;
			}
			if (dom
					&& object.getInternalPrototype().getObjectLabels()
							.contains(FUNCTION_PROTOTYPE)) {
				// Ignore functions objects
				continue;
			}
			if (Options.isDOMEnabled()) {
				if (dom
						&& label.getNativeObjectID().getAPI() != NativeAPIs.DOCUMENT_OBJECT_MODEL) {
					// Ignore non-DOM objects (if DOM is enabled)
					continue;
				}
			}

			// Build label (i.e. the box)
			// The intended format is: FOO [shape=record
			// label="{NAME\lEntry_1\lEntry2\l}"]
			String prefix = label.isSingleton() ? "@" : "*";
			String name = label.getNativeObjectID().toString();
			StringBuilder lbl = new StringBuilder();
			lbl.append(prefix + name);
			Map<String, Value> properties = new TreeMap<String, Value>(
					object.getAllProperties());
			Iterator<String> iter = properties.keySet().iterator();
			int i = 0;
			int limit = 10;
			while (i < limit && iter.hasNext()) {
				if (i == 0) {
					lbl.append("|");
				} else {
					lbl.append("\\l");
				}
				String property = iter.next();
				lbl.append(property);
				// Is the property a function?
				Value v = properties.get(property);
				Value pv = readInternalPrototype(v.getObjectLabels());
				boolean isMaybeFunction = pv.getObjectLabels().contains(
						InitialStateBuilder.FUNCTION_PROTOTYPE);
				if (isMaybeFunction) {
					lbl.append("()");
				}
				i++;
			}
			lbl.append("\\l");
			if (iter.hasNext()) {
				lbl.append("...\\l");
			}
			ns.append("\t").append("\"").append(name).append("\"")
					.append(" [shape=record label=\"{").append(lbl)
					.append("}\"]").append("\n");

			// Build arrows
			for (ObjectLabel prototype : object.getInternalPrototype()
					.getObjectLabels()) {
				es.append("\t").append("\"").append(name).append("\"")
						.append(" -> ").append("\"")
						.append(prototype.getNativeObjectID()).append("\"")
						.append("\n");
			}
		}

		// Put everything together
		StringBuilder sb = new StringBuilder();
		sb.append("digraph {\n");
		sb.append("\tcompound=true\n");
		sb.append("\trankdir=\"BT\"\n");
		sb.append("\tnode [fontname=\"Arial\"]\n");
		sb.append(ns);
		sb.append(es);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Sharpens this state.
	 * 
	 * @see #gc()
	 */
	@Override
	public void sharpen() {
		gc();
	}

	/**
	 * Runs garbage collection on the contents of this state. Ignored if
	 * {@link Options#isGCDisabled()} is set.
	 */
	public void gc() {
		if (Options.isGCDisabled())
			return;
		if (Options.isDebugEnabled())
			check();
		// if (Options.isDebug())
		// System.out.print("BlockState: gc(): Before: " + this);
		Set<ObjectLabel> live = findLiveObjectLabels();
		if (Options.isDebugEnabled()) {
			Set<ObjectLabel> dead = newSet(store.keySet());
			dead.removeAll(live);
			System.out
					.println("BlockState: gc(): Unreachable objects: " + dead);
		}
		makeWritableStore();
		Map<ObjectLabel, Obj> newstore = newMap();
		for (ObjectLabel objlabel : live) {
			Obj obj = store.get(objlabel);
			if (obj != null)
				newstore.put(objlabel, obj);
		}
		store = newstore;
		writable_store = true;
		summarized.retainAll(live);
		if (Options.isDebugEnabled())
			check();
		// if (Options.isDebug())
		// System.out.print("BlockState: gc(): After: " + this);
	}

	/**
	 * Finds live object labels (i.e. those reachable from the execution
	 * context, temporaries, or stacked object labels). If lazy propagation is
	 * enabled, all potentially summarized objects are also considered roots.
	 * Note that the summarized sets may contain dead object labels. Unknown
	 * objects and properties are ignored.
	 */
	private Set<ObjectLabel> findLiveObjectLabels() {
		Set<ObjectLabel> live = ExecutionContext
				.getObjectLabels(execution_context);
		for (Value v : temporaries)
			if (v != null)
				live.addAll(v.getObjectLabels());
		live.addAll(stacked_objlabels);
		if (Options.isDOMEnabled()) {
			// live.addAll(load_event_handlers);
			live.addAll(load_event_handlers_definite);
			live.addAll(load_event_handlers_maybe);
			live.addAll(unload_event_handlers);
			live.addAll(keyboard_event_handlers);
			live.addAll(mouse_event_handlers);
			live.addAll(unknown_event_handlers);
			live.addAll(timeout_event_handlers);
			for (String id : element_by_id.keySet()) {
				live.addAll(element_by_id.get(id));
			}
			for (String name : elements_by_name.keySet()) {
				live.addAll(elements_by_name.get(name));
			}
			for (String tagname : elements_by_tagname.keySet()) {
				live.addAll(elements_by_tagname.get(tagname));
			}
		}
		if (!Options.isLazyDisabled())
			for (ObjectLabel objlabel : store.keySet())
				if (!objlabel.isSingleton()
						|| summarized.isMaybeSummarized(objlabel))
					live.add(objlabel);
		LinkedHashSet<ObjectLabel> pending = new LinkedHashSet<ObjectLabel>(
				live);
		while (!pending.isEmpty()) {
			Iterator<ObjectLabel> it = pending.iterator();
			ObjectLabel objlabel = it.next();
			it.remove();
			live.add(objlabel);
			for (ObjectLabel obj2 : getAllObjectLabels(objlabel))
				if (!live.contains(obj2))
					pending.add(obj2);
		}
		return live;
	}

	/**
	 * Returns the set of all object labels used in the given abstract object.
	 * Unknown objects and properties are ignored.
	 */
	private Set<ObjectLabel> getAllObjectLabels(ObjectLabel objlabel) {
		Set<ObjectLabel> objlabels = newSet();
		Obj fo = getObjectRaw(objlabel, false);
		if (fo != null) {
			for (Value v : fo.getAllProperties().values())
				objlabels.addAll(v.getObjectLabels());
			objlabels.addAll(fo.getDefaultArrayProperty().getObjectLabels());
			objlabels.addAll(fo.getDefaultNonArrayProperty().getObjectLabels());
			objlabels.addAll(fo.getInternalPrototype().getObjectLabels());
			objlabels.addAll(fo.getInternalValue().getObjectLabels());
			if (!fo.isScopeChainUnknown())
				for (ScopeChain e : fo.getScopeChain())
					for (ObjectLabel l : e)
						objlabels.add(l);
		}
		return objlabels;
	}

	/**
	 * Models [[HasInstance]].
	 */
	public Value hasInstance(Set<ObjectLabel> f_prototype, Value v) {
		boolean maybe_true = false;
		boolean maybe_false = false;
		if (v.isMaybePrimitive())
			maybe_false = true;
		List<ObjectLabel> pending = new ArrayList<ObjectLabel>(
				v.getObjectLabels());
		Set<ObjectLabel> visited = newSet(v.getObjectLabels());
		while (!pending.isEmpty()) {
			ObjectLabel obj = pending.remove(pending.size() - 1);
			Value proto = UnknownValueResolver.getInternalPrototype(obj, this);
			if (proto.isMaybeNull())
				maybe_false = true;
			for (ObjectLabel p : proto.getObjectLabels()) {
				if (f_prototype.contains(p))
					maybe_true = true;
				else if (!visited.contains(p)) {
					pending.add(p);
					visited.add(p);
				}
			}
			if (maybe_true && maybe_false)
				return Value.makeAnyBool(v.getDependency());
		}
		return maybe_true ? (maybe_false ? Value.makeAnyBool(v.getDependency())
				: Value.makeBool(true, v.getDependency()))
				: (maybe_false ? Value.makeBool(false, v.getDependency())
						: Value.makeBottom(v.getDependency()));
		// ##########
	}

	/**
	 * Assigns the given value to the given temporary variable (strong update).
	 */
	public void writeTemporary(int var, Value value) {
		value.assertNonEmpty();
		makeWritableTemporaries();
		while (var >= temporaries.size())
			temporaries.add(null);
		temporaries.set(var, value);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeTemporary(v" + var + ","
					+ value + ")");
	}

	/**
	 * Removes the given temporary variable (strong update).
	 */
	public void removeTemporary(int var) {
		makeWritableTemporaries();
		while (var >= temporaries.size())
			temporaries.add(null);
		temporaries.set(var, null);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: removeTemporary(v" + var + ")");
	}

	/**
	 * Adds an load event handler.
	 */
	public void addLoadEventHandler(ObjectLabel e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableLoadEventHandlers();
		load_event_handlers_definite.add(e);
		load_event_handlers_maybe.add(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addLoadEventHandler(" + e + ")");
	}

	/**
	 * Adds an load event handler.
	 */
	public void addLoadEventHandler(Set<ObjectLabel> e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableLoadEventHandlers();
		load_event_handlers_definite.addAll(e);
		load_event_handlers_maybe.addAll(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addLoadEventHandler(" + e + ")");
	}

	/**
	 * Removes a load event handler.
	 */
	public void removeLoadEventHandler(Set<ObjectLabel> e) {
		if (!Options.isDOMEnabled()) {
			throw new RuntimeException("Event Handler Removed, but no DOM!");
		}
		makeWritableLoadEventHandlers();
		load_event_handlers_definite.removeAll(e);
		if (Options.isDebugEnabled()) {
			System.out.println("BlockState: removeLoadEventHandler(" + e + ")");
		}
	}

	/**
	 * Adds an unload event handler.
	 */
	public void addUnloadEventHandler(ObjectLabel e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableUnloadEventHandlers();
		unload_event_handlers.add(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addUnloadEventHandler(" + e + ")");
	}

	/**
	 * Adds an unload event handler.
	 */
	public void addUnloadEventHandler(Set<ObjectLabel> e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableUnloadEventHandlers();
		unload_event_handlers.addAll(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addUnloadEventHandler(" + e + ")");
	}

	/**
	 * Adds a keyboard event handler.
	 */
	public void addKeyboardEventHandler(ObjectLabel e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableKeyboardEventHandlers();
		keyboard_event_handlers.add(e);
		if (Options.isDebugEnabled())
			System.out
					.println("BlockState: addKeyboardEventHandler(" + e + ")");
	}

	/**
	 * Adds a keyboard event handler.
	 */
	public void addKeyboardEventHandler(Set<ObjectLabel> e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableKeyboardEventHandlers();
		keyboard_event_handlers.addAll(e);
		if (Options.isDebugEnabled())
			System.out
					.println("BlockState: addKeyboardEventHandler(" + e + ")");
	}

	/**
	 * Adds a mouse-event handler.
	 */
	public void addMouseEventHandler(ObjectLabel e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableMouseEventHandlers();
		mouse_event_handlers.add(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addMouseEventHandler(" + e + ")");
	}

	/**
	 * Adds a mouse-event handler.
	 */
	public void addMouseEventHandler(Set<ObjectLabel> e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableMouseEventHandlers();
		mouse_event_handlers.addAll(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addMouseEventHandler(" + e + ")");
	}

	/**
	 * Adds an unknown event handler.
	 */
	public void addUnknownEventHandler(ObjectLabel e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableUnknownEventHandlers();
		unknown_event_handlers.add(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addUnknownEventHandler(" + e + ")");
	}

	/**
	 * Adds a collection of event handlers.
	 */
	public void addUnknownEventHandler(Collection<ObjectLabel> e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableUnknownEventHandlers();
		unknown_event_handlers.addAll(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addUnknownEventHandler(" + e + ")");
	}

	/**
	 * Adds a timeout-event handler.
	 */
	public void addTimeoutHandler(ObjectLabel e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableTimeoutHandlers();
		timeout_event_handlers.add(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addTimeoutEventHandler(" + e + ")");
	}

	/**
	 * Adds a collection of timeout-event handlers.
	 */
	public void addTimeoutEventHandler(Collection<ObjectLabel> e) {
		if (!Options.isDOMEnabled())
			throw new RuntimeException("Event Handler Added, but no DOM!");
		makeWritableTimeoutHandlers();
		timeout_event_handlers.addAll(e);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: addTimeoutEventHandler(" + e + ")");
	}

	/**
	 * Returns the set of load event handlers.
	 */
	public Set<ObjectLabel> getAllLoadEventHandlers() {
		Set<ObjectLabel> load_event_handlers = newSet(load_event_handlers_definite);
		load_event_handlers.addAll(load_event_handlers_maybe);
		return load_event_handlers;
	}

	public Set<ObjectLabel> getDefiniteLoadEventHandlers() {
		return newSet(load_event_handlers_definite);
	}

	public Set<ObjectLabel> getMaybeLoadEventHandlers() {
		return newSet(load_event_handlers_maybe);
	}

	/**
	 * Returns the set of unload event handlers.
	 */
	public Set<ObjectLabel> getUnloadEventHandlers() {
		return newSet(unload_event_handlers);
	}

	/**
	 * Returns the set of keyboard event handlers.
	 */
	public Set<ObjectLabel> getKeyboardEventHandlers() {
		return newSet(keyboard_event_handlers);
	}

	/**
	 * Returns the set of mouse event handlers.
	 */
	public Set<ObjectLabel> getMouseEventHandlers() {
		return newSet(mouse_event_handlers);
	}

	/**
	 * Returns the set of timeout event handlers.
	 */
	public Set<ObjectLabel> getTimeoutEventHandlers() {
		return newSet(timeout_event_handlers);
	}

	/**
	 * Returns the set of unknown event handlers.
	 */
	public Set<ObjectLabel> getUnknownEventHandlers() {
		return newSet(unknown_event_handlers);
	}

	/**
	 * Returns a set of object labels for the given id.
	 */
	public Set<ObjectLabel> getElementById(String id) {
		Set<ObjectLabel> result = newSet();
		if (element_by_id.get(id) != null) {
			result.addAll(element_by_id.get(id));
		}
		if (default_element_by_id != null) {
			result.addAll(default_element_by_id);
		}
		return result;
	}

	/**
	 * Returns a set of object labels for all element ids.
	 */
	public Set<ObjectLabel> getAllElementById() {
		Set<ObjectLabel> result = newSet();
		for (Set<ObjectLabel> s : element_by_id.values()) {
			result.addAll(s);
		}
		if (default_element_by_id != null) {
			result.addAll(default_element_by_id);
		}
		return result;
	}

	/**
	 * Sets a collection of labels for the given element id.
	 */
	public void addElementById(String id, Collection<ObjectLabel> e) {
		makeWritableElements();
		Set<ObjectLabel> labels = element_by_id.get(id);
		if (labels == null) {
			labels = newSet();
			element_by_id.put(id, labels);
		}
		labels.addAll(e);
	}

	/**
	 * Returns a set of object labels for the given name.
	 */
	public Set<ObjectLabel> getElementsByName(String name) {
		Set<ObjectLabel> result = newSet();
		if (elements_by_name.get(name) != null) {
			result.addAll(elements_by_name.get(name));
		}
		if (default_elements_by_name != null) {
			result.addAll(default_elements_by_name);
		}
		return result;
	}

	/**
	 * Returns a set of all object labels for all element names.
	 */
	public Set<ObjectLabel> getAllElementsByName() {
		Set<ObjectLabel> result = newSet();
		for (Set<ObjectLabel> s : elements_by_name.values()) {
			result.addAll(s);
		}
		if (default_elements_by_name != null) {
			result.addAll(default_elements_by_name);
		}
		return result;
	}

	/**
	 * Adds a collection of labels for the given element name.
	 */
	public void addElementByName(String name, Collection<ObjectLabel> e) {
		makeWritableElements();
		Set<ObjectLabel> labels = elements_by_name.get(name);
		if (labels == null) {
			labels = newSet();
			elements_by_name.put(name, labels);
		}
		labels.addAll(e);
	}

	/**
	 * Returns a set of object labels for the given tagname.
	 */
	public Set<ObjectLabel> getElementsByTagName(String tagName) {
		Set<ObjectLabel> result = newSet();
		if (elements_by_tagname.get(tagName) != null) {
			result.addAll(elements_by_tagname.get(tagName));
		}
		if (default_elements_by_tagname != null) {
			result.addAll(default_elements_by_tagname);
		}
		return result;
	}

	/**
	 * Returns a set of all object labels for all tagnames.
	 */
	public Set<ObjectLabel> getAllElementsByTagName() {
		Set<ObjectLabel> result = newSet();
		for (Set<ObjectLabel> s : elements_by_tagname.values()) {
			result.addAll(s);
		}
		if (default_elements_by_tagname != null) {
			result.addAll(default_elements_by_tagname);
		}
		return result;
	}

	/**
	 * Adds a collection of labels for the given element tagname.
	 */
	public void addElementByTagName(String tagName, Collection<ObjectLabel> e) {
		makeWritableElements();
		Set<ObjectLabel> labels = elements_by_tagname.get(tagName);
		if (labels == null) {
			labels = newSet();
			elements_by_tagname.put(tagName, labels);
		}
		labels.addAll(e);
	}

	/**
	 * Reads the value of the given temporary variable.
	 */
	public Value readTemporary(int var) {
		Value res;
		if (var >= temporaries.size())
			res = null;
		else
			res = temporaries.get(var);
		if (res == null)
			throw new RuntimeException("Reading undefined temporary v" + var);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readTemporary(v" + var + ") = "
					+ res);
		return res;
	}

	/**
	 * Adds object labels used in current temporary variables and execution
	 * context to stacked object labels.
	 */
	public void stackObjectLabels() {
		if (!Options.isLazyDisabled())
			return;
		makeWritableStackedObjects();
		for (Value v : temporaries)
			if (v != null)
				stacked_objlabels.addAll(v.getObjectLabels());
		stacked_objlabels.addAll(ExecutionContext
				.getObjectLabels(execution_context));
	}

	/**
	 * Clears the temporaries.
	 */
	public void clearTemporaries() {
		if (writable_temporaries)
			temporaries.clear();
		else {
			temporaries = Collections.emptyList();
			writable_temporaries = false;
		}
	}

	/**
	 * Clears the non-live temporary variables.
	 */
	public void clearDeadTemporaries(int[] live_vars) {
		// System.out.println("****** live: " +
		// Arrays.toString(live_vars));//XXX
		for (int var = 0, i = 0; var < temporaries.size(); var++) {
			if (i < live_vars.length && live_vars[i] == var)
				i++;
			else if (temporaries.get(var) != null
					&& var >= Node.FIRST_ORDINARY_VAR) {
				// System.out.println("*** clearing v" + var + " at block " +
				// block.getIndex());//XXX
				makeWritableTemporaries();
				temporaries.set(var, null);
			}
		}
	}

	/**
	 * Summarizes the specified list of values. Always returns a new list.
	 */
	private static List<Value> summarize(List<Value> vs, Summarized s) {
		List<Value> res = newList();
		for (int i = 0; i < vs.size(); i++) {
			Value v = vs.get(i);
			res.add(i, v != null ? v.summarize(s) : null);
		}
		return res;
	}

	/**
	 * Assigns the given value to the given variable.
	 */
	public void writeVariable(String varname, Value value) {
		value.assertNonEmpty();
		Set<ObjectLabel> objs = newSet();
		for (ExecutionContext e : execution_context) {
			for (Iterator<ObjectLabel> it = e.getScopeChain().iterator(); it
					.hasNext();) {
				ObjectLabel objlabel = it.next();
				Value v = UnknownValueResolver.readProperty(objlabel, varname,
						this);
				if (v.isMaybeValue() || !it.hasNext()) {
					objs.add(objlabel);
					if (v.isNotAbsent())
						break;
				}
			}
		}
		boolean unique = objs.size() == 1;
		for (ObjectLabel objlabel : objs)
			if (unique && objlabel.isSingleton())
				strongWritePropertyIfCanPut(objlabel, varname, value, true);
			else
				weakWritePropertyIfCanPut(objlabel, varname, value, true);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: writeVariable(" + varname + ","
					+ value + ")");
	}

	/**
	 * Declares the given variable and assigns the given value to it.
	 */
	public void declareAndWriteVariable(String varname, Value value) {
		value.assertNonEmpty();
		Set<ObjectLabel> objlabels = newSet();
		for (ExecutionContext e : execution_context)
			objlabels.add(e.getVariableObject());
		writePropertyInternal(objlabels, varname, value.clearAbsent()
				.setAttributes(false, true, false), false);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: declareAndWriteVariable(" + varname
					+ "," + value + ")");
	}

	/**
	 * Returns the value of the given variable.
	 */
	public Value readVariable(String varname) {
		Collection<Value> values = new ArrayList<Value>();
		c: for (ExecutionContext e : execution_context) {
			for (ObjectLabel objlabel : e.getScopeChain()) {
				Value v = UnknownValueResolver.readProperty(objlabel, varname,
						this);
				if (v.isMaybeValue()) {
					Value v2 = v.clearAbsent();
					values.add(v2);
				}
				if (v.isNotAbsent())
					continue c;
			}
			values.add(Value.makeAbsent(new Dependency()));
		}
		Value res = Value.join(values);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readVariable(" + varname + ") = "
					+ res);
		return res;
	}

	/**
	 * Returns the reference base value of the given variable.
	 */
	public Value readVariableBase(String varname) {
		Collection<Value> values = new ArrayList<Value>();
		c: for (ExecutionContext e : execution_context) {
			for (ObjectLabel objlabel : e.getScopeChain()) {
				Value v = UnknownValueResolver.readProperty(objlabel, varname,
						this);
				if (v.isMaybeValue())
					values.add(Value.makeObject(objlabel, v.getDependency()));
				if (v.isNotAbsent())
					continue c;
			}
			values.add(Value.makeNull(new Dependency()));
		}
		Value res = Value.join(values);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readVariableBase(" + varname
					+ ") = " + res);
		return res;
	}

	/**
	 * Deletes the given variable.
	 * 
	 * @see #deleteProperty(Set, String)
	 */
	public Value deleteVariable(String varname) {
		Set<ObjectLabel> objlabels = newSet();
		boolean maybe_absent = false;
		c: for (ExecutionContext e : execution_context) {
			for (ObjectLabel objlabel : e.getScopeChain()) {
				Value v = UnknownValueResolver.readProperty(objlabel, varname,
						this);
				if (v.isMaybeValue())
					objlabels.add(objlabel);
				if (v.isNotAbsent())
					continue c;
			}
			maybe_absent = true;
		}
		Value res = deleteProperty(objlabels, varname);
		if (maybe_absent)
			res = res.joinBool(true);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: deleteVariable(" + varname + ") = "
					+ res);
		return res;
	}

	/**
	 * Returns the value of 'this'.
	 */
	public Value readThis() {
		Collection<Value> values = new ArrayList<Value>();
		for (ExecutionContext e : execution_context)
			values.add(Value.makeObject(e.getThisObject(), new Dependency()));
		Value res = Value.join(values);
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readThis() = " + res);
		return res;
	}

	/**
	 * Returns the object value of 'this'.
	 */
	public Set<ObjectLabel> readThisObjects() {
		Set<ObjectLabel> this_objs = newSet();
		for (ExecutionContext e : execution_context)
			this_objs.add(e.getThisObject());
		if (Options.isDebugEnabled())
			System.out.println("BlockState: readThisObjects() = " + this_objs);
		return this_objs;
	}

	@Override
	public void reduce(BlockStateType s) {
		if (!Options.isLazyDisabled()) {
			if (s == null) {
				// set everything to bottom, i.e. unknown
				store = newMap();
				writable_store = true;
			} else {
				// reduce each object
				makeWritableStore();
				for (Map.Entry<ObjectLabel, Obj> me : newList(store.entrySet())) {
					ObjectLabel objlabel = me.getKey();
					Obj other = s.getObjectRaw(objlabel, false);
					// FIXME if (other != null) {
					Obj obj = me.getValue();
					obj.reduce(other);
					// } else
					// store.remove(objlabel);
				}
			}
		}
	}

	@Override
	public void clearEffects() {
		clearModified();
		summarized.clear();
	}

	/**
	 * @param resultVar
	 * @return further object label of given temporarie
	 */
	public String getObjectLabel(int resultVar) {
		List<Node> nodelist = block.getNodes();

		for (Node node : nodelist) {

			if (node instanceof WriteVariableNode) {
				WriteVariableNode wvnode = (WriteVariableNode) node;
				if (wvnode.getValueVar() == resultVar) {
					return wvnode.getVarName();
				}
			}

			if (node instanceof WritePropertyNode) {
				WritePropertyNode wpnode = (WritePropertyNode) node;
				if (wpnode.getValueVar() == resultVar) {

					int propertyVar = wpnode.getPropertyVar();

					for (Node probertyNode : nodelist) {
						if (probertyNode instanceof ConstantNode) {
							ConstantNode cnode = (ConstantNode) probertyNode;
							if (cnode.getResultVar() == propertyVar) {
								return cnode.getString();
							}
						}
					}
				}
			}
		}

		// else, if node not found
		return "v" + resultVar;
	}

	/**
	 * @param resultVar
	 * @return further object label of given temporarie
	 */
	public String getAttributeLabel(int propertyVar) {
		List<Node> nodelist = block.getNodes();

		for (Node node : nodelist) {

			if (node instanceof ConstantNode) {
				ConstantNode cnode = (ConstantNode) node;
				if (cnode.getResultVar() == propertyVar) {
					return cnode.getString();
				}
			}
		}

		// else, if node not found
		return "v" + propertyVar;
	}

}
