package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.FunctionAndContext;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.ICallContext;
import dk.brics.tajs.solver.IStatistics;
import dk.brics.tajs.solver.NodeAndContext;

/**
 * Resolves 'unknown' properties for {@link BlockState}.
 * Contains wrappers for certain methods in {@link Obj}.
 * Each method reads the appropriate property. If unknown, it is recovered via the function entry state.
 */
public class UnknownValueResolver {
	
	private UnknownValueResolver() {}
	
	/**
	 * Parameter to 
	 * {@link UnknownValueResolver#recover(dk.brics.tajs.solver.GenericSolver.SolverInterface, ObjectLabel, RecoverParam)}.
	 */
	private static interface RecoverParam<ResultType,
	BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
	CallContextType extends ICallContext,
	StatisticsType extends IStatistics> {

		/**
		 * Checks whether the property is 'unknown'
		 */
		boolean isUnknown(Obj obj);
		
		/**
		 * Reads the non-'unknown' result.
		 * The property must be non-unknown at obj.
		 */
		ResultType getResult(Obj obj);
		
		/**
		 * Joins a property from src into dst.
		 * The property must be non-unknown at src.
		 * @param summarized state containing summarization sets, null if no singleton/summary mangling necessary
		 * @return true if dst is changed
		 */
		boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized);
		
		void reportStart();
		
		void reportResult(ResultType res);
	}
	
	/**
	 * Generic function for recovering 'unknown' properties.
	 */
	private static <ResultType,
			BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
			CallContextType extends ICallContext,
			StatisticsType extends IStatistics> 
		ResultType recover(BlockState<BlockStateType,CallContextType,StatisticsType> s,
				ObjectLabel objlabel, RecoverParam<ResultType,BlockStateType,CallContextType,StatisticsType> p) {
		CallContextType current_context = s.getContext();
		ResultType res;
		Obj obj = s.getObject(objlabel, true);
		if (Options.isLazyDisabled() || !p.isUnknown(obj) || current_context == null) { // already known at the current location? (or initializing)
			if (obj == null)
				throw new RuntimeException("Object " + objlabel + " not found!?");
			res = p.getResult(obj);
		} else { // unknown, need to recover at function entries
			s.getSolverInterface().getStatistics().registerUnknownValueResolve();
			if (Options.isDebugEnabled()) {
				p.reportStart();
			}
			GenericSolver<BlockStateType,CallContextType,StatisticsType>.SolverInterface c = s.getSolverInterface();
			Function current_f = s.getBasicBlock().getFunction();
			BlockStateType entry_state = c.getAnalysisLatticeElement().getState(current_f.getEntry(), current_context);
			Obj entry_obj = entry_state.getObjectRaw(objlabel, false);
			if (p.isUnknown(entry_obj)) { // also unknown at function entry?
				// build graph
				Map<FunctionAndContext<CallContextType>,Map<Node,Set<FunctionAndContext<CallContextType>>>> graph = newMap(); // nodes and forward-edges of the sub-graph of the function call graph
				List<FunctionAndContext<CallContextType>> roots = newList();
				LinkedList<FunctionAndContext<CallContextType>> pending = new LinkedList<FunctionAndContext<CallContextType>>();
				FunctionAndContext<CallContextType> first = new FunctionAndContext<CallContextType>(current_f, current_context);
				Map<Node,Set<FunctionAndContext<CallContextType>>> t = newMap();
				graph.put(first, t);
				pending.add(first);
				while (!pending.isEmpty()) {
					FunctionAndContext<CallContextType> fcp = pending.remove();
					if (fcp.getFunction().isMain())
						roots.add(fcp);
					else
						for (NodeAndContext<CallContextType> ncp : c.getCallSources(fcp.getFunction(), fcp.getContext())) {
							BlockStateType call_state = c.getCallEdgeState(ncp.getNode(), ncp.getContext(), fcp.getFunction());
							BlockStateType call_functionentry_state = c.getAnalysisLatticeElement().getState(ncp.getNode().getBlock().getFunction().getEntry(), ncp.getContext());
							Obj call_obj = call_state.getObjectRaw(objlabel, false);
							Obj call_functionentry_obj = call_functionentry_state.getObjectRaw(objlabel, false);
							if (!p.isUnknown(call_obj) || !p.isUnknown(call_functionentry_obj))
								roots.add(fcp);
							else {
								FunctionAndContext<CallContextType> caller_fcp = new FunctionAndContext<CallContextType>(ncp.getNode().getBlock().getFunction(), ncp.getContext());
								Map<Node,Set<FunctionAndContext<CallContextType>>> t2 = graph.get(caller_fcp);
								if (t2 == null) {
									t2 = newMap();
									graph.put(caller_fcp, t2);
									pending.add(caller_fcp);
								}
								Set<FunctionAndContext<CallContextType>> s2 = t2.get(ncp.getNode());
								if (s2 == null) {
									s2 = newSet();
									t2.put(ncp.getNode(), s2);
								}
								s2.add(fcp);
							}
						}
				}
				// create missing objects and recover at roots
				for (FunctionAndContext<CallContextType> fcp : graph.keySet()) {
					BlockStateType fcp_state = c.getAnalysisLatticeElement().getState(fcp.getFunction().getEntry(), fcp.getContext());
					Obj fcp_obj = fcp_state.getObject(objlabel, true); // creates the object if missing
					if (roots.contains(fcp)) 
						if (fcp.getFunction().isMain())
							p.join(fcp_obj, Obj.makeBottom(), null); // nothing is unknown at program entry
						else
							for (NodeAndContext<CallContextType> ncp : c.getCallSources(fcp.getFunction(), fcp.getContext())) {
								BlockStateType call_state = c.getCallEdgeState(ncp.getNode(), ncp.getContext(), fcp.getFunction());
								BlockStateType call_functionentry_state = c.getAnalysisLatticeElement().getState(ncp.getNode().getBlock().getFunction().getEntry(), ncp.getContext());
								Obj call_obj = call_state.getObjectRaw(objlabel, false);
								Obj call_functionentry_obj = call_functionentry_state.getObjectRaw(objlabel, false);
								if (!p.isUnknown(call_obj)) // recover from call edge
									p.join(fcp_obj, call_obj, null); // no summarization
								else if (call_functionentry_obj != null && !p.isUnknown(call_functionentry_obj)) // recover from entry of caller function
									p.join(fcp_obj, call_functionentry_obj, call_state); // call_state contains the appropriate summarization sets
							}
				}
				// propagate throughout the graph
				Set<FunctionAndContext<CallContextType>> pending2 = newSet(roots);
				LinkedList<FunctionAndContext<CallContextType>> pending_list2 = new LinkedList<FunctionAndContext<CallContextType>>(pending2);
				while (!pending2.isEmpty()) {
					FunctionAndContext<CallContextType> fcp = pending_list2.remove();
					pending2.remove(fcp);
					BlockStateType fcp_state = c.getAnalysisLatticeElement().getState(fcp.getFunction().getEntry(), fcp.getContext());
					Obj fcp_obj = fcp_state.getObjectRaw(objlabel, false);
					for (Map.Entry<Node,Set<FunctionAndContext<CallContextType>>> me : graph.get(fcp).entrySet()) {
						for (FunctionAndContext<CallContextType> callee : me.getValue()) {
							BlockStateType call_state = c.getCallEdgeState(me.getKey(), fcp.getContext(), callee.getFunction());
							BlockStateType callee_state = c.getAnalysisLatticeElement().getState(callee.getFunction().getEntry(), callee.getContext());
							Obj callee_obj = callee_state.getObjectRaw(objlabel, true);
							boolean changed = p.join(callee_obj, fcp_obj, call_state); // call_state contains the appropriate summarization sets
							if (changed && !pending2.contains(callee)) { // propagate further if changed
								pending2.add(callee);
								pending_list2.add(callee);
							}
						}
					}
				}
				if (Options.isDebugEnabled()) {
					for (FunctionAndContext<CallContextType> fcp : graph.keySet()) {
						BlockStateType fcp_state = c.getAnalysisLatticeElement().getState(fcp.getFunction().getEntry(), fcp.getContext());
						Obj fcp_obj = fcp_state.getObject(objlabel, false); 
						if (p.isUnknown(fcp_obj))
							throw new RuntimeException("Unexpected 'unknown' value at " + objlabel + ", should have been recovered!?");
					}
				}
			}
			// now get result at current function entry
			obj = s.getObjectRaw(objlabel, true);
			entry_obj = entry_state.getObjectRaw(objlabel, false);
			p.join(obj, entry_obj, s);
			if (Options.isDebugEnabled() && p.isUnknown(obj))
				throw new RuntimeException("Unexpected 'unknown' value at " + objlabel + ", should have been recovered!?");
			res = p.getResult(obj);
			if (Options.isDebugEnabled())
				p.reportResult(res);
		}
		return res;
	}
	
	/**
	 * Wrapper for {@link Obj#readProperty(String)}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
			CallContextType extends ICallContext,
			StatisticsType extends IStatistics>
			Value readProperty(final ObjectLabel objlabel, final String propertyname, final BlockState<BlockStateType,CallContextType,StatisticsType> s) {
		return recover(s, objlabel, new RecoverParam<Value,BlockStateType,CallContextType,StatisticsType>(){

			@Override
			public boolean isUnknown(Obj obj) {
				return obj == null || obj.readProperty(propertyname).isUnknown();
			}

			@Override
			public Value getResult(Obj obj) {
				return obj.readProperty(propertyname);
			}

			@Override
			public boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized) {
				return dst.joinProperty(src, propertyname, summarized != null ? summarized.getSummarized() : null);
			}

			@Override
			public void reportStart() {
				System.out.println("UnknownValueResolver: recovering property '" + propertyname + "' of " + objlabel  + " at block " + s.getBasicBlock().getIndex() + ", context " + s.getContext());
			}

			@Override
			public void reportResult(Value res) {
				System.out.println("UnknownValueResolver: readProperty(" + objlabel  + "," + propertyname + ") = " + res);
			}
		});
	}
	
	/**
	 * Wrapper for {@link Obj#getDefaultArrayProperty}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
			CallContextType extends ICallContext,
			StatisticsType extends IStatistics> 
			Value getDefaultArrayProperty(final ObjectLabel objlabel, final BlockState<BlockStateType,CallContextType,StatisticsType> s) {
		return recover(s, objlabel, new RecoverParam<Value,BlockStateType,CallContextType,StatisticsType>(){

			@Override
			public boolean isUnknown(Obj obj) {
				return obj == null || obj.isSomeArrayPropertyUnknown();
			}

			@Override
			public Value getResult(Obj obj) {
				return obj.getDefaultArrayProperty();
			}

			@Override
			public boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized) {
				return dst.joinAllArrayProperties(src, summarized != null ? summarized.getSummarized() : null);
			}

			@Override
			public void reportStart() {
				System.out.println("UnknownValueResolver: recovering default array property of " + objlabel  + " at block " + s.getBasicBlock().getIndex() + ", context " + s.getContext());
			}

			@Override
			public void reportResult(Value res) {
				System.out.println("UnknownValueResolver: getDefaultArrayProperty(" + objlabel  + ") = " + res);
			}
		});
	}
	
	/**
	 * Wrapper for {@link Obj#getDefaultNonArrayProperty}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
			CallContextType extends ICallContext,
			StatisticsType extends IStatistics>
			Value getDefaultNonArrayProperty(final ObjectLabel objlabel, final BlockState<BlockStateType,CallContextType,StatisticsType> s) {
		return recover(s, objlabel, new RecoverParam<Value,BlockStateType,CallContextType,StatisticsType>(){

			@Override
			public boolean isUnknown(Obj obj) {
				return obj == null || obj.isSomeNonArrayPropertyUnknown();
			}

			@Override
			public Value getResult(Obj obj) {
				return obj.getDefaultNonArrayProperty();
			}

			@Override
			public boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized) {
				return dst.joinAllNonArrayProperties(src, summarized != null ? summarized.getSummarized() : null);
			}

			@Override
			public void reportStart() {
				System.out.println("UnknownValueResolver: recovering default non-array property of " + objlabel  + " at block " + s.getBasicBlock().getIndex() + ", context " + s.getContext());
			}

			@Override
			public void reportResult(Value res) {
				System.out.println("UnknownValueResolver: getDefaultNonArrayProperty(" + objlabel  + ") = " + res);
			}
		});
	}

	/**
	 * Wrapper for {@link Obj#getInternalValue()}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
		CallContextType extends ICallContext,
		StatisticsType extends IStatistics>
			Value getInternalValue(final ObjectLabel objlabel, final BlockState<BlockStateType,CallContextType,StatisticsType> s) {
		return recover(s, objlabel, new RecoverParam<Value,BlockStateType,CallContextType,StatisticsType>(){

			@Override
			public boolean isUnknown(Obj obj) {
				return obj == null || obj.getInternalValue().isUnknown();
			}

			@Override
			public Value getResult(Obj obj) {
				return obj.getInternalValue();
			}

			@Override
			public boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized) {
				return dst.joinInternalValue(src, summarized != null ? summarized.getSummarized() : null);
			}

			@Override
			public void reportStart() {
				System.out.println("UnknownValueResolver: recovering internal value of " + objlabel  + " at block " + s.getBasicBlock().getIndex() + ", context " + s.getContext());
			}

			@Override
			public void reportResult(Value res) {
				System.out.println("UnknownValueResolver: getInternalValue(" + objlabel  + ") = " + res);
			}
		});
	}
	
	/**
	 * Wrapper for {@link Obj#getInternalPrototype()}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
			CallContextType extends ICallContext,
			StatisticsType extends IStatistics>
			Value getInternalPrototype(final ObjectLabel objlabel, final BlockState<BlockStateType,CallContextType,StatisticsType> s) {
		return recover(s, objlabel, new RecoverParam<Value,BlockStateType,CallContextType,StatisticsType>(){
			
			@Override
			public boolean isUnknown(Obj obj) {
				return obj == null || obj.getInternalPrototype().isUnknown();
			}

			@Override
			public Value getResult(Obj obj) {
				return obj.getInternalPrototype();
			}

			@Override
			public boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized) {
				return dst.joinInternalPrototype(src, summarized != null ? summarized.getSummarized() : null);
			}

			@Override
			public void reportStart() {
				System.out.println("UnknownValueResolver: recovering internal prototype of " + objlabel  + " at block " + s.getBasicBlock().getIndex() + ", context " + s.getContext());
			}

			@Override
			public void reportResult(Value res) {
				System.out.println("UnknownValueResolver: getInternalPrototype(" + objlabel  + ") = " + res);
			}
		});
	}
	
	/**
	 * Wrapper for {@link Obj#getScopeChain()}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
			CallContextType extends ICallContext,
			StatisticsType extends IStatistics>
			Set<ScopeChain> getScopeChain(final ObjectLabel objlabel, final BlockState<BlockStateType,CallContextType,StatisticsType> s) {
		return recover(s, objlabel, new RecoverParam<Set<ScopeChain>,BlockStateType,CallContextType,StatisticsType>(){

			@Override
			public boolean isUnknown(Obj obj) {
				return obj == null || obj.isScopeChainUnknown();
			}

			@Override
			public Set<ScopeChain> getResult(Obj obj) {
				return obj.getScopeChain();
			}

			@Override
			public boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized) {
				return dst.joinInternalScope(src, summarized != null ? summarized.getSummarized() : null);
			}

			@Override
			public void reportStart() {
				System.out.println("UnknownValueResolver: recovering scope chain of " + objlabel  + " at block " + s.getBasicBlock().getIndex() + ", context " + s.getContext());
			}

			@Override
			public void reportResult(Set<ScopeChain> res) {
				System.out.println("UnknownValueResolver: getScopeChain(" + objlabel  + ") = " + res);
			}
		});
	} 
	
	/**
	 * Wrapper for {@link Obj#getDefaultArrayProperty}, {@link Obj#getDefaultNonArrayProperty},
	 * and {@link Obj#getAllProperties()}.
	 * None of the properties are 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,StatisticsType>,
			CallContextType extends ICallContext,
			StatisticsType extends IStatistics>
			Obj getAllProperties(final ObjectLabel objlabel, final BlockState<BlockStateType,CallContextType,StatisticsType> s) {
		return recover(s, objlabel, new RecoverParam<Obj,BlockStateType,CallContextType,StatisticsType>(){

			@Override
			public boolean isUnknown(Obj obj) {
				return obj == null || obj.isSomePropertyUnknown();
			}

			@Override
			public Obj getResult(Obj obj) {
				return obj;
			}

			@Override
			public boolean join(Obj dst, Obj src, BlockState<BlockStateType,CallContextType,StatisticsType> summarized) {
				return dst.joinAllProperties(src, summarized != null ? summarized.getSummarized() : null);
			}

			@Override
			public void reportStart() {
				System.out.println("UnknownValueResolver: recovering all properties of " + objlabel  + " at block " + s.getBasicBlock().getIndex() + ", context " + s.getContext());
			}

			@Override
			public void reportResult(Obj res) {
				System.out.println("UnknownValueResolver: getAllProperties(" + objlabel + ") = " + res);
			}
		});
	} 
}
