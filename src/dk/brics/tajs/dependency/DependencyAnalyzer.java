package dk.brics.tajs.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.dependency.graph.DependencyGraph;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.visitor.GraphVisitor;
import dk.brics.tajs.dependency.interfaces.IDependency;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Triple;

/**
 * creates the dependency output
 * 
 * @author Matthias Keil
 * 
 */
public class DependencyAnalyzer {

	/**
	 * store the initial state
	 */
	public static HashSet<String> initialstate = new HashSet<String>();

	/**
	 * store the state analysis
	 */
	public static List<List<HashMap<String, Dependency>>> values = new ArrayList<List<HashMap<String, Dependency>>>();

	/**
	 * store the state analysis
	 */
	public static List<List<HashMap<String, DependencyGraphReference>>> references = new ArrayList<List<HashMap<String, DependencyGraphReference>>>();

	/**
	 * store the dump analysis
	 */
	// public static Map<String, List<Pair<Dependency,
	// DependencyGraphReference>>> dumps = new HashMap<String,
	// List<Pair<Dependency, DependencyGraphReference>>>();

	public static Map<Triple<String, IDependency<?>, SourceLocation>, List<Pair<Dependency, DependencyGraphReference>>> dumps = new HashMap<Triple<String, IDependency<?>, SourceLocation>, List<Pair<Dependency, DependencyGraphReference>>>();

	/**
	 * store all dependency nodes
	 */
	public static HashSet<DependencyNode> dependencyNodes = new HashSet<DependencyNode>();

	/**
	 * print the initial state
	 */
	public static String printInitialstate() {
		StringBuilder b = new StringBuilder();
		b.append(makeSpace());
		b.append(makeHeader("InitialState"));

		ArrayList<String> list = new ArrayList<String>(initialstate);
		Collections.sort(list);

		for (String string : list) {
			String space_string = " ";
			String initialstate_string = string;

			b.append(space_string + initialstate_string);
			b.append("\n");
		}

		b.append(makeSeperator());
		return b.toString();
	}

	/**
	 * print dependencies
	 */
	public static String printDependencies() {
		StringBuilder b = new StringBuilder();
		b.append(makeSpace());
		b.append(makeHeader("Dependencies"));

		HashMap<SourceLocation, DependencyObject> cache = DependencyObject.getCache();
		ArrayList<DependencyObject> list = new ArrayList<DependencyObject>(cache.values());
		Collections.sort(list);

		for (DependencyObject dependencyObject : list) {

			int length = calculateMaxLengthOfDepenencyObject(list);
			int width = (length > 10) ? length : 10;

			String space_string = " ";
			String key_string = leftPadding(dependencyObject.getTrace(), width);
			String equals_string = " : ";
			String dependecy_string = dependencyObject.getSourceLocation().toString();

			b.append(space_string + key_string + equals_string + dependecy_string);
			b.append("\n");
		}

		b.append(makeSeperator());
		return b.toString();
	}

	/**
	 * print dependencies
	 */
	public static String printValues() {
		List<List<HashMap<String, Dependency>>> output = new ArrayList<List<HashMap<String, Dependency>>>(values);

		StringBuilder b = new StringBuilder();
		b.append(makeSpace());
		b.append(makeHeader("Value Dependencies"));

		if (output.isEmpty())
			return b.toString();

		if (!Options.isShowAllStates()) {
			List<List<HashMap<String, Dependency>>> tmp = new ArrayList<List<HashMap<String, Dependency>>>();

			tmp.add(output.get(0));
			tmp.add(output.get(output.size() - 1));

			output = tmp;
		}

		if (!Options.isIncludeInitialState()) {
			output.remove(0);
		}

		for (List<HashMap<String, Dependency>> list : output) {
			for (HashMap<String, Dependency> hashMap : list) {

				ArrayList<String> keySet = new ArrayList<String>(hashMap.keySet());
				Collections.sort(keySet);

				for (String key : keySet) {
					Dependency dependecy = hashMap.get(key);

					int length = calculateMaxLengthOfString(output) + 3;
					int width = (length > 15) ? length : 15;

					String space_string = " ";
					String key_string = leftPadding("d(" + key + ")", width);
					String equals_string = " = ";

					String dependecy_string = dependecy.toString();

					b.append(space_string + key_string + equals_string + dependecy_string);
					b.append("\n");
				}

				if (!list.get(list.size() - 1).equals(hashMap)) {
					b.append("-\n");
				}
			}
			b.append(makeSeperator());
		}
		return b.toString();
	}

	/**
	 * print dependency graph references
	 */
	public static String printReferences() {
		List<List<HashMap<String, DependencyGraphReference>>> output = new ArrayList<List<HashMap<String, DependencyGraphReference>>>(references);

		StringBuilder b = new StringBuilder();
		b.append(makeSpace());
		b.append(makeHeader("Dependency Graph References"));

		if (output.isEmpty())
			return b.toString();

		if (!Options.isShowAllStates()) {
			List<List<HashMap<String, DependencyGraphReference>>> tmp = new ArrayList<List<HashMap<String, DependencyGraphReference>>>();

			tmp.add(output.get(0));
			tmp.add(output.get(output.size() - 1));

			output = tmp;
		}

		if (!Options.isIncludeInitialState()) {
			output.remove(0);
		}

		for (List<HashMap<String, DependencyGraphReference>> list : output) {
			for (HashMap<String, DependencyGraphReference> hashMap : list) {

				ArrayList<String> keySet = new ArrayList<String>(hashMap.keySet());
				Collections.sort(keySet);

				for (String key : keySet) {
					DependencyGraphReference reference = hashMap.get(key);

					int length = calculateMaxLengthOfString(output) + 3;
					int width = (length > 15) ? length : 15;

					String space_string = " ";
					String key_string = leftPadding("d(" + key + ")", width);
					String equals_string = " = ";

					String dependecy_string = reference.toString();

					b.append(space_string + key_string + equals_string + dependecy_string);
					b.append("\n");
				}

				if (!list.get(list.size() - 1).equals(hashMap)) {
					b.append("-\n");
				}
			}
			b.append(makeSeperator());
		}
		return b.toString();
	}

	/**
	 * print dependency graph
	 */
	public static String printGraph(DependencyGraph d) {
		StringBuilder b = new StringBuilder();
		b.append(makeSpace());
		b.append(makeHeader("Dependency Graph"));

		GraphVisitor visitor = new GraphVisitor();
		d.getRoot().accept(visitor);
		b.append(visitor.toString());

		b.append(makeSeperator());
		return b.toString();
	}

	/**
	 * print dependency nodes
	 */
	public static String printDependencyNodes() {
		StringBuilder b = new StringBuilder();
		b.append(makeSpace());
		b.append(makeHeader("Dependency Nodes"));

		Map<String, DependencyNode> nodes = new HashMap<String, DependencyNode>();
		for (DependencyNode node : dependencyNodes) {
			nodes.put(node.getIdentifier(), node);
		}

		List<String> list = new ArrayList<String>(nodes.keySet());
		Collections.sort(list);

		int length = calculateMaxLengthOfDepenencyNode(list);
		int width = (length > 10) ? length : 10;

		for (String key : list) {

			DependencyNode node = nodes.get(key);

			String space_string = " ";
			String key_string = leftPadding(node.getIdentifier(), width);
			String equals_string = " = ";
			String dependecy_string = node.toString();

			b.append(space_string + key_string + equals_string + dependecy_string);
			b.append("\n");
		}

		b.append(makeSeperator());
		return b.toString();
	}

	/**
	 * print dumped dependencies
	 */
	public static String printDumpedValues() {
		Map<Triple<String, IDependency<?>, SourceLocation>, List<Pair<Dependency, DependencyGraphReference>>> output = new HashMap<Triple<String, IDependency<?>, SourceLocation>, List<Pair<Dependency, DependencyGraphReference>>>(
				dumps);

		StringBuilder b = new StringBuilder();
		b.append(makeSpace());
		b.append(makeHeader("Dumped Value Dependencies"));

		if (output.isEmpty())
			return b.toString();

		List<String> dependencyList = new ArrayList<String>();
		List<String> referenceList = new ArrayList<String>();

		for (Triple<String, IDependency<?>, SourceLocation> triple : output.keySet()) {
			for (Pair<Dependency, DependencyGraphReference> pair : output.get(triple)) {
				dependencyList.add(pair.getElement1().toString());
			}
		}

		for (Triple<String, IDependency<?>, SourceLocation> triple : output.keySet()) {
			for (Pair<Dependency, DependencyGraphReference> pair : output.get(triple)) {
				referenceList.add(pair.getElement2().toString());
			}
		}

		int dependencyLength = calculateMaxLengthOfKey(dependencyList);
		int referenceLength = calculateMaxLengthOfKey(referenceList);

		int dependencyWidth = (dependencyLength > 15) ? dependencyLength : 15;
		int referenceWidth = (referenceLength > 15) ? referenceLength : 15;

		for (Triple<String, IDependency<?>, SourceLocation> triple : output.keySet()) {
			String spaceString = " ";
			String newlineString = "\n";
			String equalsString = " = ";
			String separatorString = " | ";

			String identifier = triple.getElement1();
			String object = triple.getElement2().toString();
			String location = "(" + triple.getElement3().toString() + ")";

			String keyString = spaceString + identifier + location;

			if (triple.getElement2() instanceof State) {
				keyString = spaceString + object + "\n(" + location + ")";
			} else if (triple.getElement2() instanceof Value) {
				keyString = spaceString + identifier + "=" + object + " (" + location + ")";
			}

			b.append(keyString);
			b.append(newlineString);

			for (Pair<Dependency, DependencyGraphReference> pair : output.get(triple)) {
				b.append(equalsString);
				b.append(leftPadding(pair.getElement1().toString(), dependencyWidth));
				b.append(separatorString);
				b.append(leftPadding(pair.getElement2().toString(), referenceWidth));
				b.append(newlineString);
			}
			b.append(newlineString);
		}
		return b.toString();
	}

	/*
	 * ##################################################
	 * HELPERS
	 * ##################################################
	 */

	/**
	 * @return
	 */
	public static String makeSpace() {
		StringBuilder b = new StringBuilder();
		b.append("\n\n\n\n\n");
		return b.toString();
	}

	/**
	 * @return
	 */
	public static String makeHeader(String name) {
		StringBuilder b = new StringBuilder();
		b.append(makeSeperator());
		b.append("* " + name + ": \n");
		b.append(makeSeperator());
		return b.toString();
	}

	/**
	 * @return
	 */
	public static String makeSeperator() {
		StringBuilder b = new StringBuilder();
		b.append("**************************************************\n");
		return b.toString();
	}

	/**
	 * add spaces right to the string
	 * 
	 * @param string
	 * @param width
	 * @return string
	 */
	public static String rightPadding(String string, int width) {
		return String.format("%" + width + "s", string);
	}

	/**
	 * ad spaces left to the string
	 * 
	 * @param string
	 * @param width
	 * @return string
	 */
	public static String leftPadding(String string, int width) {
		return String.format("%-" + width + "s", string);
	}

	/**
	 * calculates the max - length of the given strings
	 * 
	 * @param list
	 * @return int
	 */
	public static <T> int calculateMaxLengthOfKey(List<String> values) {
		int max = 0;
		for (String string : values) {
			max = Math.max(max, string.length());
		}
		return max;
	}

	/**
	 * calculates the max - length of the given strings
	 * 
	 * @param list
	 * @return int
	 */
	public static <T> int calculateMaxLengthOfString(List<List<HashMap<String, T>>> values) {
		int max = 0;
		for (List<HashMap<String, T>> list : values) {
			for (HashMap<String, T> hashMap : list) {
				for (String string : hashMap.keySet()) {
					max = Math.max(max, string.length());
				}
			}
		}
		return max;
	}

	/**
	 * calculates the max - length of the given DependencyObjects
	 * 
	 * @param list
	 * @return int
	 */
	public static int calculateMaxLengthOfDepenencyObject(List<DependencyObject> list) {
		int max = 0;
		for (DependencyObject dependencyObject : list) {
			max = Math.max(max, dependencyObject.getTrace().length());
		}
		return max;
	}

	/**
	 * calculates the max - length of the given DependencyNodes
	 * 
	 * @param list
	 * @return int
	 */
	public static int calculateMaxLengthOfDepenencyNode(List<String> list) {
		int max = 0;
		for (String identifier : list) {
			max = Math.max(max, identifier.length());
		}
		return max;
	}
}