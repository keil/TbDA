package dk.brics.tajs;

import static dk.brics.tajs.util.Collections.newList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.w3c.dom.Document;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.TypeCollector;
import dk.brics.tajs.analysis.dom.HTMLParser;
import dk.brics.tajs.analysis.dom.HTMLParserImpl;
import dk.brics.tajs.dependency.DependencyAnalyzer;
import dk.brics.tajs.dependency.graph.DependencyGraph;
import dk.brics.tajs.dependency.graph.visitor.DotVisitor;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.js2flowgraph.RhinoAST2Flowgraph;
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.optimizer.FlowGraphOptimizer;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.SolverSynchronizer;

/**
 * Main class for the TAJS program analysis.
 */
public class Main {

	private static SolverSynchronizer sync;

	private Main() {
	}

	/**
	 * Runs the analysis on the given source files. Run without arguments to see
	 * the usage.
	 */
	public static void main(String[] args) {
		long time = System.currentTimeMillis();

		boolean show_usage = false;
		List<String> files = newList();
		for (String arg : args)
			if (arg.startsWith("-") && arg.length() > 1) {
				if (!Options.set(arg)) {
					System.out.println("Option not recognized: " + arg);
					show_usage = true;
				}
			} else
				files.add(arg);

		if (files.size() == 0) {
			System.out.println("No source files");
			show_usage = true;
		}
		if (show_usage || !Options.isQuietEnabled()) {
			if (show_usage)
				System.out.println();
			System.out.println("TAJS - Type Analyzer for JavaScript");
			System.out
					.println("(C) 2008-2010 Anders M\u00F8ller, Simon Holm Jensen, Peter Thiemann, Magnus Madsen, Matthias Diehn Ingesman, Roman Matthias Keil\n");
		}
		if (show_usage) {
			System.out.println("Usage: java -jar tajs-all.jar [OPTION]... [FILE]...\n");
			System.out.print(Options.describe());
			return;
		}
		Options.dump();

		FlowGraph g = null;
		DependencyGraph d = new DependencyGraph();
		HTMLParser p;
		Document document = null;
		Analysis a = new Analysis();
		a.setSynchronizer(sync);
		FlowGraphOptimizer optimizer = new FlowGraphOptimizer();

		enterPhase("Loading files");
		try {
			List<String> html_files = newList();
			List<String> js_files = newList();
			for (String fn : files) {
				String l = fn.toLowerCase();
				if (l.endsWith(".html") || l.endsWith(".xhtml") || l.endsWith(".htm"))
					html_files.add(fn);
				else
					js_files.add(fn);
			}
			RhinoAST2Flowgraph builder = new RhinoAST2Flowgraph();
			if (!js_files.isEmpty())
				builder.build(js_files);
			if (!html_files.isEmpty()) {
				if (html_files.size() > 1) {
					throw new RuntimeException("Only one html-file can be analyzed at a time.");
				}
				Options.setDOM(true);
				p = new HTMLParserImpl(builder);
				a.getInitialStateBuilder().setHTMLParser(p);
				document = p.build(html_files.get(0));
			}
			g = builder.close();
			optimizer.optimize(g);
		} catch (IOException e) {
			System.out.println("Unable to parse " + e.getMessage());
			return;
		}

		if (Options.isFlowGraphEnabled())
			try {
				PrintWriter pw = new PrintWriter(new FileWriter("flowgraph.dot"));
				g.toDot(pw);
				pw.close();
				System.out.println(g.toString());

				File dir = new File("flowgraphs");
				if (!dir.exists()) {
					dir.mkdir();
				} // TODO: empty dir if already existing
				for (Function function : g.getFunctions()) {
					String n = function.getName();
					if (n == null)
						n = "-";
					String name = (function.getSourceLocation().getFileName() + "." + n + ".line" + function.getSourceLocation().getLineNumber())
							.replace('/', '.').replace('\\', '.').replace(':', '.');
					File file = new File("flowgraphs" + File.separator + name + ".dot");
					PrintWriter writer = new PrintWriter(file);
					function.toDot(writer, true, function == g.getMain());
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		BlockState.reset();
		Obj.reset();
		Value.reset();
		ScopeChain.reset();

		a.getSolver().init(g, d);

		a.getInitialStateBuilder().addDOMSpecificState(document);

		enterPhase("Data flow analysis");
		a.getSolver().solve();

		if (!Options.isQuietEnabled() && Options.isStatisticsEnabled()) {
			enterPhase("Statistics");
			System.out.println(a.getStatistics());
			System.out.println("BlockState: created=" + BlockState.getNumberOfStatesCreated() + ", makeWritableStore="
					+ BlockState.getNumberOfMakeWritableStoreCalls());
			System.out.println("Obj: created=" + Obj.getNumberOfObjsCreated() + ", makeWritableProperties=" + Obj.getNumberOfMakeWritablePropertiesCalls());
			System.out.println("Value cache: hits=" + Value.getNumberOfValueCacheHits() + ", misses=" + Value.getNumberOfValueCacheMisses() + ", finalSize="
					+ Value.getValueCacheSize());
			System.out.println("Value object set cache: hits=" + Value.getNumberOfObjectSetCacheHits() + ", misses=" + Value.getNumberOfObjectSetCacheMisses()
					+ ", finalSize=" + Value.getObjectSetCacheSize());
			System.out.println("ScopeChain cache: hits=" + ScopeChain.getNumberOfCacheHits() + ", misses=" + ScopeChain.getNumberOfCacheMisses()
					+ ", finalSize=" + ScopeChain.getCacheSize());
			System.out.println("Basic blocks: " + g.getNumberOfBlocks());
			if (Options.isDebugEnabled()) {
				System.out.println(optimizer);
			}

			// TODO: dump more statistics?
		}

		if (Options.isTimingEnabled())
			System.out.println("Analysis finished in " + (System.currentTimeMillis() - time) + "ms");

		// FIXME scanning messages
		enterPhase("Scanning for messages");
		a.getSolver().scan();
		if (Options.isDebugEnabled())
			enterPhase("Messages");
		for (Message m : a.getSolver().getMessages()) {
			if (m.getSeverity() != Message.Severity.LOW || Options.isLowSeverityEnabled())
				System.out.println(m);
		}

		if (Options.isExitOnError() & !a.getSolver().getMessages().isEmpty())
			System.exit(0);

		if (Options.isCallGraphEnabled()) {
			enterPhase("Call graph");
			System.out.println(a.getSolver().getAnalysisLatticeElement().getCallGraph());
			FileWriter f = null;
			String filename = "callgraph.dot";
			try {
				f = new FileWriter(filename);
				System.out.println("Writing call graph to " + filename);
				a.getSolver().getAnalysisLatticeElement().getCallGraph().toDot(new PrintWriter(f));
			} catch (IOException e) {
				System.out.println("Unable to write " + filename + ": " + e.getMessage());
			} finally {
				if (f != null)
					try {
						f.close();
					} catch (IOException e) {
						System.out.println("Unable to close " + filename + ": " + e.getMessage());
					}
			}
		}

		if (Options.isCollectVariableInfoEnabled()) {
			enterPhase("Reachable variables summary");
			TypeCollector.getTypeInformation();
		}

		if (Options.isDependency() || Options.isExtendedDependency()) {
			enterPhase("Dependencies");
			a.analyzeDependencies();

			// print initial state objects
			if (Options.isIncludeInitialState())
				System.out.println(DependencyAnalyzer.printInitialstate());

			// print all dependencies
			System.out.println(DependencyAnalyzer.printDependencies());

			// print all value dependencies
			System.out.println(DependencyAnalyzer.printValues());

			// print extended dependencies
			if (Options.isExtendedDependency()) {
				System.out.println(DependencyAnalyzer.printDependencyNodes());
				System.out.println(DependencyAnalyzer.printReferences());
			}

			// print dependency graph
			if (Options.isDependencyGraph()) {
				System.out.println(DependencyAnalyzer.printGraph(d));
			}

			// print dependency graph
			if (Options.isToDot()) {
				DotVisitor visitor = new DotVisitor();
				d.accept(visitor);

				for (String string : files) {
					try {
						File f = new File(string);

						String filename = f.getName().substring(0, f.getName().indexOf(".")) + ".dot";
						String path = "output/" + f.getPath().substring(0, f.getPath().indexOf(f.getName()));

						File dotFile = new File(path + filename);
						File dotDir = new File(path);
						dotDir.mkdirs();

						visitor.flush(dotFile);
					} catch (IOException e) {
						System.out.println("Unable to print dependency graph " + e.getMessage());
					}
				}
			}
		}
	}

	private static void enterPhase(String name) {
		echo("===========  " + name + " ===========");
	}

	private static void echo(String msg) {
		if (!Options.isQuietEnabled())
			System.out.println(msg);
	}

	/**
	 * Sets the solver synchronizer.
	 */
	public static void setSynchronizer(SolverSynchronizer sync) {
		Main.sync = sync;
	}

}
