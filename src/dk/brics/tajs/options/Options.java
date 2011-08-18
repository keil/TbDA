package dk.brics.tajs.options;

import dk.brics.tajs.util.Collections;

import java.util.Set;

/**
 * Global analysis options.
 */
public class Options {

	private static boolean debug;
	private static boolean collect_variable_info;
	private static boolean bug_patterns;
	private static boolean states;
	private static boolean test;
	private static boolean timing;
	private static boolean flowgraph;
	private static boolean callgraph;
	private static boolean no_recency;
	private static boolean quiet;
	private static boolean no_hybrid_collections;
	private static boolean no_modified;
	private static boolean no_lazy;
	private static boolean no_context_sensitivity;
	private static boolean no_local_path_sensitivity;
	private static boolean no_copy_on_write;
	private static boolean no_gc;
	private static boolean unsound;
	private static boolean include_dom;
	private static boolean no_exceptions;
	private static boolean newflow;
	private static boolean memory_usage;
	private static boolean no_flowgraph_optimization;
	private static boolean no_unreachable;
	private static boolean propagate_dead_flow;
	private static boolean low_severity;
	private static boolean statistics;
	private static boolean dependency;
	private static boolean extended_dependency;
	private static boolean dependencygraph;
	private static boolean todot;
	private static boolean traceall;
	private static boolean exit_on_error;
	private static boolean include_initialstate;
	private static boolean show_all_states;
	private static boolean isReturnJSON;
	private static boolean eval_statistics;
	private static boolean coverage;
	private static boolean error_batch_mode;
	private static boolean ignore_libraries;
	private static Set<String> ignored_libraries = Collections.newSet();
	private static boolean single_event_handler_loop;
	private static boolean single_event_handler_type;
	private static boolean isIgnoreHTMLContent;

	private Options() {
	}

	/**
	 * Returns a description of the available options.
	 */
	public static String describe() {
		return "Options:\n" + "  -no-local-path-sensitivity  Disable local path sensitivity\n" + "  -no-context-sensitivity     Disable context sensitivity\n"
				+ "  -no-recency                 Disable recency abstraction\n" + "  -no-modified                Disable modified flags\n"
				+ "  -no-exceptions              Disable implicit exception flow\n" + "  -no-gc                      Disable abstract garbage collection\n"
				+ "  -no-lazy                    Disable lazy propagation\n" + "  -no-flowgraph-optimization  Disable flowgraph optimization\n"
				+ "  -low-severity               Enable low severity messages\n" + "  -unsound                    Enable unsound assumptions\n"
				+ "  -flowgraph                  Output flowgraph.dot\n" + "  -callgraph                  Output callgraph.dot\n"
				+ "  -debug                      Output debug information\n"
				+ "  -collect-variable-info      Output type and line information on reachable variables\n"
				+ "  -newflow                    Report summary of new flow at function entries\n"
				+ "  -states                     Output intermediate abstract states\n"
				+ "  -test                       Test mode (implies quiet), ensures predictable iteration orders\n"
				+ "  -timing                     Report analysis time\n" + "  -statistics                 Report statistics\n"
				+ "  -memory-usage               Report the memory usage of the analysis\n"
				+ "  -quiet                      Only output results, not progress\n" + "  -no-copy-on-write           Disable copy-on-write\n"
				+ "  -no-hybrid-collections      Disable hybrid collections\n" + "  -dom                        Enable Mozilla DOM browser model\n"
				+ "  -no-unreachable             Disable warnings about unreachable code\n" + "  -propagate-dead-flow        Propagate empty values\n"
				+ "  -exit-on-error              Exit, if an error occurs\n" + "  -dependency                 Show value dependencies\n"
				+ "  -extended_dependency        Show extended value dependencies\n" + "  -dependencygraph            Show dependency graph\n"
				+ "  -todot                      Write dependency graph to dot\n" + "  -traceall                   Trace all Values\n"
				+ "  -include_initialstate       Include InitialState dependencies\n" + "  -show_all_states            Show all states\n"
				+ "  -isIgnoreHTMLContent        Ignore the content of the html page, i.e. id, names, event handleer, etc.\n"
				+ "  -isReturnJSON               Assume that AJAX calls Return JSON\n" + "  -bug-patterns               Detect bug patterns\n"
				+ "  -eval-statistics            Don't fail on use of eval and innerHTML, but record their use\n"
				+ "  -coverage                   Output a view of the source with unreachble lines highlighted\n"
				+ "  -single-event-handler-loop  Use a single non-deterministic event loop for events\n"
				+ "  -single-event-handler-type  Do not distinguish between different types of event handlers\n"
				+ "  -introduce-error            Measure precision by randomly introducing syntax errors\n"
				+ "  -ignore-libraries           Ignore unreachable code messages from libraries (may not be the last option before files!)\n";

	}

	/**
	 * Sets the given option.
	 * 
	 * @return true if understood
	 */
	public static boolean set(String option) {
		if (option.equals("-no-local-path-sensitivity"))
			no_local_path_sensitivity = true;
		else if (option.equals("-no-context-sensitivity"))
			no_context_sensitivity = true;
		else if (option.equals("-no-recency"))
			no_recency = true;
		else if (option.equals("-no-modified"))
			no_modified = true;
		else if (option.equals("-no-lazy"))
			no_lazy = true;
		else if (option.equals("-no-gc"))
			no_gc = true;
		else if (option.equals("-unsound"))
			unsound = true;
		else if (option.equals("-no-exceptions"))
			no_exceptions = true;
		else if (option.equals("-debug"))
			debug = true;
		else if (option.equals("-collect-variable-info"))
			collect_variable_info = true;
		else if (option.equals("-newflow"))
			newflow = true;
		else if (option.equals("-flowgraph"))
			flowgraph = true;
		else if (option.equals("-callgraph"))
			callgraph = true;
		else if (option.equals("-states"))
			states = true;
		else if (option.equals("-test"))
			quiet = test = true;
		else if (option.equals("-timing"))
			timing = true;
		else if (option.equals("-statistics"))
			statistics = true;
		else if (option.equals("-memory-usage"))
			memory_usage = true;
		else if (option.equals("-quiet"))
			quiet = true;
		else if (option.equals("-no-copy-on-write"))
			no_copy_on_write = true;
		else if (option.equals("-no-hybrid-collections"))
			no_hybrid_collections = true;
		else if (option.equals("-dom"))
			include_dom = true;
		else if (option.equals("-no-flowgraph-optimization"))
			no_flowgraph_optimization = true;
		else if (option.equals("-no-unreachable"))
			no_unreachable = true;
		else if (option.equals("-propagate-dead-flow"))
			propagate_dead_flow = true;
		else if (option.equals("-low-severity"))
			low_severity = true;
		else if (option.equals("-exit_on_error"))
			exit_on_error = true;
		else if (option.equals("-dependency"))
			dependency = true;
		else if (option.equals("-extended_dependency"))
			extended_dependency = true;
		else if (option.equals("-dependencygraph"))
			dependencygraph = true;
		else if (option.equals("-todot"))
			todot = true;
		else if (option.equals("-traceall"))
			traceall = true;
		else if (option.equals("-include_initialstate"))
			include_initialstate = true;
		else if (option.equals("-show_all_states"))
			show_all_states = true;
		else if (option.equals("-isIgnoreHTMLContent"))
			isIgnoreHTMLContent = true;
		else if (option.equals("-isReturnJSON"))
			isReturnJSON = true;
		else if (option.equals("-bug-patterns"))
			bug_patterns = true;
		else if (option.equals("-eval-statistics"))
			eval_statistics = true;
		else if (option.equals("-coverage"))
			coverage = true;
		else if (option.equals("-single-event-handler-loop"))
			single_event_handler_loop = true;
		else if (option.equals("-single-event-handler-type"))
			single_event_handler_type = true;
		else if (option.equals("-introduce-error "))
			error_batch_mode = true;
		else if (option.equals("-bug-patterns"))
			bug_patterns = true;
		else if (option.equals("-ignore-libraries"))
			ignore_libraries = true;
		else
			return false;
		return true;
	}

	/**
	 * Resets all options.
	 */
	public static void reset() {
		no_local_path_sensitivity = false;
		no_context_sensitivity = false;
		no_recency = false;
		no_modified = false;
		no_lazy = false;
		no_gc = false;
		unsound = false;
		no_exceptions = false;
		debug = false;
		collect_variable_info = false;
		newflow = false;
		flowgraph = false;
		callgraph = false;
		states = false;
		quiet = test = false;
		timing = false;
		statistics = false;
		memory_usage = false;
		quiet = false;
		no_copy_on_write = false;
		no_hybrid_collections = false;
		include_dom = false;
		no_flowgraph_optimization = false;
		no_unreachable = false;
		propagate_dead_flow = false;
		low_severity = false;
		exit_on_error = false;
		dependency = false;
		extended_dependency = false;
		dependencygraph = false;
		todot = false;
		traceall = false;
		include_initialstate = false;
		show_all_states = false;
		isIgnoreHTMLContent = false;
		isReturnJSON = false;
		single_event_handler_loop = false;
		single_event_handler_type = false;
		bug_patterns = false;
		eval_statistics = false;
		ignore_libraries = false;
		ignored_libraries = Collections.newSet();

	}

	/**
	 * Prints the settings (if in debug mode).
	 */
	public static void dump() {
		if (debug) {
			System.out.println("Options affecting analysis precision:");
			System.out.println("  no-local-path-sensitivity: " + no_local_path_sensitivity);
			System.out.println("  no-context-sensitivity:    " + no_context_sensitivity);
			System.out.println("  no-recency:                " + no_recency);
			System.out.println("  no-modified:               " + no_modified);
			System.out.println("  no-exceptions:             " + no_exceptions);
			System.out.println("  no-lazy                    " + no_lazy);
			System.out.println("  no-gc:                     " + no_gc);
			System.out.println("  unsound:                   " + unsound);
			System.out.println("  propagate-dead-flow:       " + propagate_dead_flow);
			System.out.println("  low-severity:              " + low_severity);
			System.out.println("Other options:");
			System.out.println("  flowgraph:                 " + flowgraph);
			System.out.println("  callgraph:                 " + callgraph);
			System.out.println("  debug:                     " + debug);
			System.out.println("  collect-variable-info:     " + collect_variable_info);
			System.out.println("  newflow:                   " + newflow);
			System.out.println("  states:                    " + states);
			System.out.println("  test:                      " + test);
			System.out.println("  statistics:                " + statistics);
			System.out.println("  timing:                    " + timing);
			System.out.println("  memory-usage:              " + memory_usage);
			System.out.println("  no-copy-on-write:          " + no_copy_on_write);
			System.out.println("  no-hybrid-collections:     " + no_hybrid_collections);
			System.out.println("  dom:                       " + include_dom);
			System.out.println("  no-flowgraph-optimization: " + no_flowgraph_optimization);
			System.out.println("  no-unreachable:            " + no_unreachable);
			System.out.println("  exit_on_error:             " + exit_on_error);
			System.out.println("  dependency:                " + dependency);
			System.out.println("  extended_dependency:       " + extended_dependency);
			System.out.println("  dependencygraph:           " + dependencygraph);
			System.out.println("  todot:                     " + todot);
			System.out.println("  tarceall:                  " + traceall);
			System.out.println("  include_initialstate:      " + include_initialstate);
			System.out.println("  show_all_states:           " + show_all_states);
			System.out.println("  isIgnoreHTMLContent:       " + isIgnoreHTMLContent);
			System.out.println("  isReturnJSON:		         " + isReturnJSON);
			System.out.println("  single-event-handler-loop: " + single_event_handler_loop);
			System.out.println("  single-event-handler-type: " + single_event_handler_type);
			System.out.println("  bug-patterns:              " + bug_patterns);
			System.out.println("  eval-statistics:           " + eval_statistics);
			System.out.println("  ignore-libraries:          " + ignore_libraries);
		}
	}

	/**
	 * return json
	 */
	public static boolean isReturnJSON() {
		return isReturnJSON;
	}

	/**
	 * is html ignored
	 */
	public static boolean isIgnoreHTMLContent() {
		return isIgnoreHTMLContent;
	}

	/**
	 * If set, ignore unreachable code warnings from libraries.
	 */
	public static boolean isIgnoreLibraries() {
		return ignore_libraries;
	}

	/**
	 * IS eval statistics
	 */
	public static boolean isEvalStatistics() {
		return eval_statistics;
	}

	/**
	 * Add a file to the set of ignored library files.
	 */
	public static void addLibrary(String fileName) {
		ignored_libraries.add(fileName);
	}

	/**
	 * Get the set of ignored libraries.
	 */
	public static Set<String> getLibraries() {
		return ignored_libraries;
	}

	/**
	 * If set, detect bug patterns.
	 */
	public static boolean isBugPatternsEnabled() {
		return bug_patterns;
	}


	public static boolean isCoverageEnabled() {
		return coverage;
	}

	/**
	 * If set, produce verbose output.
	 */
	public static boolean isDebugEnabled() {
		return debug;
	}

	/**
	 * If set, summarize information on reachable variables, e.g. their typ and
	 * location.
	 */
	public static boolean isCollectVariableInfoEnabled() {
		return collect_variable_info;
	}

	/**
	 * If set, output flowgraph.dot.
	 */
	public static boolean isFlowGraphEnabled() {
		return flowgraph;
	}

	/**
	 * If set, output callgraph.dot.
	 */
	public static boolean isCallGraphEnabled() {
		return callgraph;
	}

	/**
	 * If set, output intermediate abstract states.
	 */
	public static boolean isIntermediateStatesEnabled() {
		return states;
	}

	/**
	 * If set, avoid nondeterministic output.
	 */
	public static boolean isTestEnabled() {
		return test;
	}

	/**
	 * If set, report statistics.
	 */
	public static boolean isStatisticsEnabled() {
		return statistics;
	}

	/**
	 * If set, report timings.
	 */
	public static boolean isTimingEnabled() {
		return timing;
	}

	/**
	 * If set, do not use recency abstraction.
	 */
	public static boolean isRecencyDisabled() {
		return no_recency;
	}

	/**
	 * If set, only report results.
	 */
	public static boolean isQuietEnabled() {
		return quiet;
	}

	/**
	 * If set, do not use {@link dk.brics.tajs.util.HybridArrayHashMap} and
	 * {@link dk.brics.tajs.util.HybridArrayHashSet}.
	 */
	public static boolean isHybridCollectionsDisabled() {
		return no_hybrid_collections;
	}

	/**
	 * If set, do not use modified flags.
	 */
	public static boolean isModifiedDisabled() {
		return no_modified;
	}

	/**
	 * If set, do not use lazy propagation.
	 */
	public static boolean isLazyDisabled() {
		return no_lazy;
	}

	/**
	 * If set, do not use context sensitivity.
	 */
	public static boolean isContextSensitivityDisabled() {
		return no_context_sensitivity;
	}

	/**
	 * If set, do not use local path sensitivity, including assume node effects.
	 */
	public static boolean isLocalPathSensitivityDisabled() {
		return no_local_path_sensitivity;
	}

	/**
	 * If set, do not use copy-on-write.
	 */
	public static boolean isCopyOnWriteDisabled() {
		return no_copy_on_write;
	}

	/**
	 * If set, do not use abstract garbage collection.
	 */
	public static boolean isGCDisabled() {
		return no_gc;
	}

	/**
	 * If set, allow certain unsound tricks.
	 */
	public static boolean isUnsoundEnabled() {
		return unsound;
	}

	/**
	 * If set, print low severity messages.
	 */
	public static boolean isLowSeverityEnabled() {
		return low_severity;
	}

	/**
	 * If set, report summary of new flow at function entries.
	 */
	public static boolean isNewFlowEnabled() {
		return newflow;
	}

	/**
	 * If set, exclude implicit exception flow.
	 */
	public static boolean isExceptionsDisabled() {
		return no_exceptions;
	}

	/**
	 * If set, the DOM objects and functions are part of the initial state
	 */
	public static boolean isDOMEnabled() {
		return include_dom;
	}

	/**
	 * Sets whether or not DOM is enabled.
	 */
	public static void setDOM(boolean dom) {
		Options.include_dom = dom;
	}

	/**
	 * If set, measure memory usage.
	 */
	public static boolean isMemoryMeasurementEnabled() {
		return memory_usage;
	}

	/**
	 * If set, do not perform flowgraph optimization.
	 */
	public static boolean isFlowGraphOptimizationDisabled() {
		return no_flowgraph_optimization;
	}

	/**
	 * If set, do not report unreachable code.
	 */
	public static boolean isUnreachableDisabled() {
		return no_unreachable;
	}

	/**
	 * vasu
	 */
	public static boolean isErrorBatchMode() {
		return error_batch_mode;
	}

	/**
	 * If set, dead flow is propagated.
	 */
	public static boolean isPropagateDeadFlow() {
		return propagate_dead_flow;
	}

	/**
	 * If set, exit if an error occurs
	 */
	public static boolean isExitOnError() {
		return exit_on_error;
	}

	/**
	 * If set, shows value dependencies
	 */
	public static boolean isDependency() {
		return dependency;
	}

	/**
	 * If set, shows extended value dependencies
	 */
	public static boolean isExtendedDependency() {
		return extended_dependency;
	}

	/**
	 * If set, shows dependency graph
	 */
	public static boolean isDependencyGraph() {
		return dependencygraph;
	}

	/**
	 * If set, prints dependency graph to dot
	 */
	public static boolean isToDot() {
		return todot;
	}

	/**
	 * If set, trace all values
	 */
	public static boolean isTraceAll() {
		return traceall;
	}

	/**
	 * If set, shows value dependencies
	 */
	public static boolean isIncludeInitialState() {
		return include_initialstate;
	}

	/**
	 * If set, shows dependencies in all state
	 */
	public static boolean isShowAllStates() {
		return show_all_states;
	}

	/**
	 * If set, produce verbose output.
	 */
	public static void setDebug(boolean debug) {
		Options.debug = debug;
	}

	/**
	 * If set, avoid nondeterministic output. Also sets quiet mode and
	 * low_severity.
	 */
	public static void setTest(boolean test) {
		Options.test = test;
		if (test) {
			quiet = true;
			low_severity = true;
		}
	}
}