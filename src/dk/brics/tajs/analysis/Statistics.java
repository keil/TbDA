package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.nodes.*;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.ICallContext;
import dk.brics.tajs.solver.IStatistics;

// TODO: improve statistics?

/**
 * Records various information during fixpoint solving.
 */
public class Statistics implements IStatistics { 
	
	private int node_transfers;
	
	private int block_transfers;
	
	private Set<Node> call_to_non_function;
	
	private Set<Node> call_nodes;
	
	private Set<Node> absent_variable_read;
	
	private Set<Node> read_variable_nodes;
	
	private Set<Node> null_undef_base;
	
	private Set<Node> property_access_nodes;
	
	private Set<Node> absent_fixed_property_read;
	
	private Set<Node> read_fixed_property_nodes;
	
	private Map<BasicBlock,Map<ICallContext,List<String>>> newflows;
	
	private int unknown_value_resolve;
	
	private int joins;
	
	private long max_memory = 0;
	
	private float average_memory = 0;
	
	private static final long MEGABYTE = 1024L * 1024L;
	
	private NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);

	private long initial_mem = 0;

    private int domProperties = 0;

    private int domFunctions = 0;
	
	/**
	 * Constructs a new statistics collector.
	 */
	public Statistics() {
		call_to_non_function = newSet();
		call_nodes = newSet();
		absent_variable_read = newSet();
		read_variable_nodes = newSet();
		null_undef_base = newSet();
		property_access_nodes = newSet();
		absent_fixed_property_read = newSet();
		read_fixed_property_nodes = newSet();
		newflows = newMap();
	}
	
	@Override
	public void registerNodeTransfer(Node n) {
		node_transfers++;
		// TODO transfers[n.getNodeIndex()]++;
	}

	@Override
	public void registerBlockTransfer() {
		block_transfers++;
	}

	@Override
	public void registerNewFlow(BasicBlock b, ICallContext c, String diff) {
		if (Options.isNewFlowEnabled() && b.isEntry()) {
			Map<ICallContext,List<String>> m = newflows.get(b);
			if (m == null) {
				m = newMap();
				newflows.put(b, m);
			}
			List<String> diffs = m.get(c);
			if (diffs == null) {
				diffs = newList();
				m.put(c, diffs);
			}
			diffs.add(diff);
		}		
	}
	
	@Override
	public int getTotalNumberOfNodeTransfers() {
		return node_transfers;
	}

	/**
	 * Registers a potential call/construct to a non-function value.
	 */
	public void registerCallToNonFunction(Node n) {
		call_to_non_function.add(n);
	}
	
	/**
	 * Registers a call/construct node.
	 */
	public void registerCallNode(Node n) {
		call_nodes.add(n);
	}
	
	/**
	 * Registers a potential read of absent variable.
	 */
	public void registerAbsentVariableRead(Node n) {
		absent_variable_read.add(n);
	}
	
	/**
	 * Registers a read variable node.
	 */
	public void registerReadVariableNode(Node n) {
		read_variable_nodes.add(n);
	}
	
	/**
	 * Registers a potential property access of null/undef.
	 */
	public void registerNullUndefPropertyAccess(Node n) {
		null_undef_base.add(n);
	}
	
	/**
	 * Registers a property access node.
	 */
	public void registerPropertyAccessNode(Node n) {
		property_access_nodes.add(n);
	}
	
	/**
	 * Registers a potential absent fixed-property read.
	 */
	public void registerAbsentFixedPropertyRead(Node n) {
		absent_fixed_property_read.add(n);
	}
	
	/**
	 * Registers a fixed-property read node.
	 */
	public void registerFixedPropertyReadNode(Node n) {
		read_fixed_property_nodes.add(n);
	}
	
	/**
	 * Returns a string description of the results.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		if (Options.isNewFlowEnabled()) {
			b.append("\n\nNew flow at each function for each context:");
			for (Map.Entry<BasicBlock,Map<ICallContext,List<String>>> me1 : newflows.entrySet()) {
				Function f = me1.getKey().getFunction();
				b.append("\n").append(f).append(" at ").append(f.getSourceLocation()).append(":");
				for (Map.Entry<ICallContext,List<String>> me2 : me1.getValue().entrySet()) {
					b.append("\n  ").append(me2.getKey()).append(" state diffs: ").append(me2.getValue().size());
					for (String diff : me2.getValue())
						if (diff != null)
							b.append("\n    state diff:").append(diff);
				}
			}
		}
				
		b.append("\n\nCall/construct nodes with potential call to non-function:                     " + call_to_non_function.size());
		b.append("\nTotal number of call/construct nodes:                                         " + call_nodes.size());
		b.append("\n==> Call/construct nodes that are certain to never call non-functions:        " + 
				(call_nodes.size() > 0 ?
						((call_nodes.size() - call_to_non_function.size()) * 1000 / call_nodes.size()) / 10f + "%" :
				"-"));

		b.append("\n\nRead variable nodes with potential absent variable:                           " + absent_variable_read.size());
		b.append("\nTotal number of (non-this) read variable nodes:                               " + read_variable_nodes.size());
		b.append("\n==> Read variable nodes that are certain to never read absent variables:      " +
				(read_variable_nodes.size() > 0 ?
						((read_variable_nodes.size() - absent_variable_read.size()) * 1000 / read_variable_nodes.size()) / 10f + "%" :
				"-"));

		b.append("\n\nProperty access nodes with potential null/undef base:                         " + null_undef_base.size());
		b.append("\nTotal number of property access nodes:                                        " + property_access_nodes.size());
		b.append("\n==> Property access nodes that are certain to never have null/undef base:     " +
				(property_access_nodes.size() > 0 ?
						((property_access_nodes.size() - null_undef_base.size()) * 1000 / property_access_nodes.size()) / 10f + "%" :
				"-"));

		b.append("\n\nFixed-property read nodes with potential absent property:                     " + absent_fixed_property_read.size());
		b.append("\nTotal number of fixed-property read nodes:                                    " + read_fixed_property_nodes.size());
		b.append("\n==> Fixed-property read nodes that are certain to never have absent property: " +
				(read_fixed_property_nodes.size() > 0 ?
						((read_fixed_property_nodes.size() - absent_fixed_property_read.size()) * 1000 / read_fixed_property_nodes.size()) / 10f + "%" :
				"-"));
		b.append("\n\nNode transfers: " + node_transfers);
		b.append("\nBlock transfers: " + block_transfers);
		b.append("\nUnknown-value recoveries: " + unknown_value_resolve);
		b.append("\nState joins: " + joins + "\n");

        if (Options.isDOMEnabled()) {
            b.append("\nDOM Properties: " + domProperties + ", DOM Functions: " + domFunctions + "\n");
        }

		if (Options.isMemoryMeasurementEnabled()) {
			formatter.setMaximumFractionDigits(2);
			b.append("\nMemory profiling:\n");
			b.append(" Initial memory: " +  formatter.format((initial_mem / MEGABYTE))).append("M\n");
			b.append(" Max memory used: " + formatter.format((max_memory / MEGABYTE))).append("M\n");
			b.append(" Approximate average memory used: " + formatter.format(average_memory / MEGABYTE)).append("M\n");
		}
		return b.toString();
	}

	@Override
	public void count(Node n) {
		n.visitBy(new NodeVisitor<Void>() {
			
			@Override
			public void visit(NopNode n, Void a) {
			}

			@Override
			public void visit(DeclareVariableNode n, Void a) {
			}

			@Override
			public void visit(ConstantNode n, Void a) {
			}

			@Override
			public void visit(NewObjectNode n, Void a) {
			}

			@Override
			public void visit(UnaryOperatorNode n, Void a) {
			}

			@Override
			public void visit(BinaryOperatorNode n, Void a) {
			}

			@Override
			public void visit(ReadVariableNode n, Void a) {
				String varname = n.getVarName();
				if (!varname.equals("this")) 
					registerReadVariableNode(n);
			}

			@Override
			public void visit(WriteVariableNode n, Void a) {
			}

			@Override
			public void visit(ReadPropertyNode n, Void a) {
				registerPropertyAccessNode(n);
				if (n.isPropertyFixed())
					registerFixedPropertyReadNode(n);
			}

			@Override
			public void visit(WritePropertyNode n, Void a) {
				registerPropertyAccessNode(n);
			}

			@Override
			public void visit(DeletePropertyNode n, Void a) {
				if (!n.isVariable())
					registerPropertyAccessNode(n);
			}

			@Override
			public void visit(TypeofNode n, Void a) {
			}

			@Override
			public void visit(IfNode n, Void a) {
			}

			@Override
			public void visit(DeclareFunctionNode n, Void a) {
			}

			@Override
			public void visit(CallNode n, Void a) {
				registerCallNode(n);
			}

			@Override
			public void visit(ReturnNode n, Void a) {
			}

			@Override
			public void visit(ExceptionalReturnNode n, Void a) {
			}

			@Override
			public void visit(ThrowNode n, Void a) {
			}

			@Override
			public void visit(CatchNode n, Void a) {
			}

			@Override
			public void visit(EnterWithNode n, Void a) {
			}

			@Override
			public void visit(LeaveWithNode n, Void a) {
			}

			@Override
			public void visit(GetPropertiesNode n, Void a) {
			}

			@Override
			public void visit(NextPropertyNode n, Void a) {
			}
			
			@Override 
			public void visit(HasNextPropertyNode n, Void a) {
			}
			
			@Override
			public void visit(AssumeNode n, Void a) {
			}

            @Override
            public void visit(EventEntryNode n, Void a) {
            }

            @Override
			public void visit(EventDispatcherNode n, Void a) {
			}

			@Override
			public void visit(DeclareEventHandlerNode n, Void a) {
			}

			@Override
			public void visit(ContextDependencyPushNode n, Void a) {	
			}
			
			@Override
			public void visit(ContextDependencyPopNode n, Void a) {	
			}
			
		}, null);
	}

	@Override
	public void registerUnknownValueResolve() {
		unknown_value_resolve++;
	}

	@Override
	public void registerJoin() {
		joins++;
	}
	

	@Override
	public void registerFinishedIteration() {
		if (Options.isMemoryMeasurementEnabled()) {
			System.gc();
			long currUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
			if (currUsedMem > max_memory)
				max_memory = currUsedMem;
			if (average_memory == 0)
				average_memory = currUsedMem;
			else 
				average_memory = ((block_transfers - 1) * average_memory + currUsedMem) / block_transfers;
		}	
	}

	@Override
	public void registerBegin() {
		if (Options.isMemoryMeasurementEnabled()) {
			initial_mem  =  Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		}
	}

    /**
     * Register a DOM property.
     */
    @Override
    public void registerDOMProperty(DOMSpec spec) {
        domProperties++;
    }

    /**
     * Register a DOM function.
     */
    @Override
    public void registerDOMFunction(DOMSpec spec) {
        domFunctions++;
    }

}
