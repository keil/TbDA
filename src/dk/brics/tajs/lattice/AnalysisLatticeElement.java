package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dk.brics.tajs.dependency.DependencyAnalyzer;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.IAnalysisLatticeElement;
import dk.brics.tajs.solver.ICallContext;
import dk.brics.tajs.solver.IStatistics;

/**
 * Global analysis lattice element.
 */
public class AnalysisLatticeElement<BlockStateType extends BlockState<BlockStateType, CallContextType, StatisticsType>, CallContextType extends ICallContext, StatisticsType extends IStatistics>
		implements IAnalysisLatticeElement<BlockStateType, CallContextType> {

	/**
	 * Abstract block states. Stores an abstract state for each basic block
	 * entry and call context. Default is bottom.
	 */
	public List<Map<CallContextType, BlockStateType>> block_entry_states;

	/**
	 * Call graph.
	 */
	private CallGraph<BlockStateType, CallContextType> call_graph;

	/**
	 * A measure of the size of the current element.
	 */
	private int size;

	/**
	 * A measure of the vertical position of this element in the lattice.
	 */
	private int position;

	/**
	 * Constructs a new bottom global analysis lattice element.
	 */
	public AnalysisLatticeElement(FlowGraph flowgraph) {
		block_entry_states = newList();
		for (int i = 0; i < flowgraph.getNumberOfBlocks(); i++) {
			Map<CallContextType, BlockStateType> m = newMap();
			block_entry_states.add(m);
		}
		call_graph = new CallGraph<BlockStateType, CallContextType>(flowgraph);
	}

	@Override
	public CallGraph<BlockStateType, CallContextType> getCallGraph() {
		return call_graph;
	}

	@Override
	public BlockStateType getState(BasicBlock block, CallContextType context) {
		BlockStateType b = block_entry_states.get(block.getIndex())
				.get(context);
		if (b != null)
			b.checkOwner(block, context);
		return b;
	}

	@Override
	public Map<CallContextType, BlockStateType> getStates(BasicBlock block) {
		return block_entry_states.get(block.getIndex());
	}

	@Override
	public Collection<Map.Entry<CallContextType, BlockStateType>> getStateMapEntries(
			BasicBlock block) {
		return block_entry_states.get(block.getIndex()).entrySet();
	}

	@Override
	public int getSize(BasicBlock block) {
		return block_entry_states.get(block.getIndex()).size();
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public MergeResult joinBlockEntry(BlockStateType s, BasicBlock b,
			CallContextType c, boolean reduce) {
		if (Options.isDebugEnabled()) {
			if (s.getExecutionContext().isEmpty()
					&& !Options.isPropagateDeadFlow())
				throw new RuntimeException("Empty execution context!?");
			s.check();
		}
		if (Options.isIntermediateStatesEnabled() && reduce)
			System.out.println("AnalysisLatticeElement: before reduce: "
					+ s.toStringBrief());
		boolean add = false;
		int oldposition;
		int delta;
		String diff = null;
		Map<CallContextType, BlockStateType> m = getStates(b);
		BlockStateType state_current = m.get(c);
		if (state_current == null) {
			add = true;
			if (reduce)
				s.reduce(null);
			s.setBasicBlock(b);
			s.setContext(c);
			m.put(c, s);
			state_current = s;
			size++;
			oldposition = 0;
		} else {
			BlockStateType state_old = null;
			if (Options.isNewFlowEnabled())
				state_old = state_current.clone();
			state_current.checkOwner(b, c);
			oldposition = state_current.getPosition(); // TODO: cache the
														// position
														// computations? or
														// remove them if not
														// used...
			if (reduce)
				s.reduce(state_current);
			if (Options.isIntermediateStatesEnabled() && reduce)
				System.out
						.println("AnalysisLatticeElement: after reduce, before join: "
								+ s.toStringBrief());
			add = state_current.join(s);
			s.getSolverInterface().getStatistics().registerJoin();
			if (Options.isNewFlowEnabled())
				diff = state_current.diff(state_old);
		}
		if (add) {
			delta = state_current.getPosition() - oldposition; // diff stays
																// null if first
																// time
			if (delta < 0)
				throw new RuntimeException("Negative delta!?");
			position += delta;
			if (Options.isDebugEnabled()) {
				if (state_current.getExecutionContext().isEmpty()
						&& !Options.isPropagateDeadFlow())
					throw new RuntimeException("Empty execution context!?");
				state_current.check();
			}
			if (Options.isIntermediateStatesEnabled())
				System.out.println("Added block entry state at block "
						+ b.getIndex() + ": " + state_current);
			return new MergeResult(delta, diff);
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brics.tajs.solver.IAnalysisLatticeElement#printDependencies()
	 */
	public void analyzeDependencies() {
		List<Map<CallContextType, BlockStateType>> states = new ArrayList<Map<CallContextType, BlockStateType>>(
				block_entry_states);
		Collections.reverse(states);

		for (Map<CallContextType, BlockStateType> map : states) {
			for (BlockStateType blockStateType : map.values()) {
				DependencyAnalyzer.values.add(blockStateType
						.analyzeDependencies());
				DependencyAnalyzer.references.add(blockStateType
						.analyzeDependencyReferences());
			}
		}
	}
}