package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.nodes.*;
import dk.brics.tajs.flowgraph.nodes.BinaryOperatorNode.Op;
import dk.brics.tajs.options.Options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.FunctionNode;
import org.mozilla.javascript.Node.Symbol;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ScriptOrFnNode;
import org.mozilla.javascript.Token;

/**
 * Translates ECMAScript files into dk.brics.flowgraph.FlowGraph instances,
 * using the Mozilla Rhino parser.
 * 
 * @author Simon Holm Jensen
 */
public class RhinoAST2Flowgraph {

	/**
	 * Environment for Rhino. Only used for callback in case of Syntax errors.
	 */
	private final CompilerEnvirons rhinoEnv;

	/**
	 * The graph being constructed.
	 */
	private final FlowGraph graph;

	/**
	 * The AST walker used to parse translate rhino parse trees.
	 */
	RhinoASTWalker walker = null;

	/**
	 * Translation contexts.
	 */
	private enum rContext {
		EXPR, STMT
	}

	/**
	 * New FlowGraph builder.
	 */
	public RhinoAST2Flowgraph() {
		this(new CompilerEnvirons());
	}

	/**
	 * New FlowGraph builder, with user defined environment.
	 * 
	 * @param rhinoEnv
	 */
	public RhinoAST2Flowgraph(CompilerEnvirons rhinoEnv) {
		this.rhinoEnv = rhinoEnv;
		rhinoEnv.setGeneratingSource(true);
		graph = new FlowGraph();
	}

	/**
	 * Parses the files and builds the flowgraph.
	 */
	public void build(List<String> files) throws FileNotFoundException, IOException {
		Parser p = new Parser(rhinoEnv, rhinoEnv.getErrorReporter());
		org.mozilla.javascript.Node root = null;
		for (String file : files) {
			try {
				if (file.equals("-")) {
					if (!Options.isQuietEnabled())
						System.out.println("Parsing from standard input");
					file = "<input>";
					root = p.parse(new InputStreamReader(System.in), file, 0);
				} else {
					if (!Options.isQuietEnabled())
						System.out.println("Parsing " + file);
					root = p.parse(new FileReader(file), new File(file).getCanonicalPath(), 0);
				}
				if (Options.isDebugEnabled()) { // This step requires the
												// patched js.jar otherwise just
												// "null" is printed
					System.out.println(root.toStringTree((ScriptOrFnNode) root));
				}
			} catch (org.mozilla.javascript.EvaluatorException e) {
				throw new IOException(e.getMessage());
			}
			if (walker == null)
				walker = new RhinoASTWalker(file, (ScriptOrFnNode) root, null, null, 0);
			walker.newBasicBlock();
			walker.buildTopLevel((ScriptOrFnNode) root, file);
		}
	}

	public Function build(String js, String url) {
		return build(js, url, 0);
	}

	public Function build(String js, String url, int lineno_offset) {
		org.mozilla.javascript.Node root = parse(js, url);
		if (walker == null)
			walker = new RhinoASTWalker(url, (ScriptOrFnNode) root, null, null, lineno_offset);
		return walker.buildTopLevel((ScriptOrFnNode) root, url);
	}

	/**
	 * Returns the graph currently being constructed. Call {@link #close()} to
	 * access the finished graph.
	 */
	public FlowGraph getGraph() {
		return graph;
	}

	private ScriptOrFnNode parse(String js, String url) {
		Parser p = new Parser(rhinoEnv, rhinoEnv.getErrorReporter());
		org.mozilla.javascript.Node root = null;
		if (Options.isDebugEnabled())
			System.out.println("Parsing: " + js);
		try {
			root = p.parse(new StringReader(js), url, 0);
		} catch (IOException e) {
			throw new RuntimeException("Error during parsing: " + e);
		}
		if (Options.isDebugEnabled()) { // This step requires the patched js.jar
										// otherwise just "null" is printed
			System.out.println(root.toStringTree((ScriptOrFnNode) root));
		}
		return (ScriptOrFnNode) root;
	}

	/**
	 * Wrap the code in an event handler.
	 */
	public Function addEventHandler(String js, String attribute, String url, int line_offset) {
		ScriptOrFnNode root = parse(js, url);
		RhinoASTWalker wwalker = new RhinoASTWalker(url, root, Collections.singletonList("event"), null, line_offset);
		Function function = wwalker.buildTopLevel(root, url);
		wwalker.close();
		if (DOMEvents.isLoadEventAttribute(attribute)) {
			graph.addLoadEventHandler(function);
		} else if (DOMEvents.isUnloadEventAttribute(attribute)) {
			graph.addUnloadEventHandler(function);
		} else if (DOMEvents.isKeyboardEventAttribute(attribute)) {
			graph.addKeyboardEventHandler(function);
		} else if (DOMEvents.isMouseEventAttribute(attribute)) {
			graph.addMouseEventHandler(function);
		} else if (DOMEvents.isOtherEventAttribute(attribute)) {
			graph.addUnknownEventHandler(function);
		} else {
			throw new RuntimeException("Unknown Event Attribute: " + attribute);
		}
		return function;
	}

	/**
	 * Finishes the flowgraph being constructed by this builder and returns the
	 * flowgraph. Call this method when all JS code has been parsed.
	 */
	public FlowGraph close() {
		if (Options.isDOMEnabled())
			walker.addEventLoop();
		walker.close();
		graph.complete();
		return graph;
	}

	/**
	 * Translates a Rhino AST by recursive decent. Multiple files/functions are
	 * parsed into the same graph, with separate functions.
	 */
	private class RhinoASTWalker {

		BasicBlock entryBB, exceptional_exitBB, normal_exitBB, declsBB;
		String fileName;
		Function res; // Resulting function
		BasicBlock currBB; // Always to current basic block, new nodes are
							// appended here
		Function currFun; // Always to the current function
		private ScriptOrFnNode top;
		Map<org.mozilla.javascript.Node, BasicBlock> locations = new HashMap<org.mozilla.javascript.Node, BasicBlock>();
		LinkedList<BasicBlock> catchBlocks = new LinkedList<BasicBlock>(); // Catchblocks
		// Rhino inserts some extra Leavewith blocks in connection exceptions,
		// its not quite clear what the intention of these
		// are and they are ignored using this stack.
		LinkedList<Boolean> ignoreLeaveWith = new LinkedList<Boolean>();
		LinkedList<Integer> valueStack = new LinkedList<Integer>(); // Models
																	// the Rhino
																	// stack.
		LinkedList<Integer> linenoStack = new LinkedList<Integer>(); // Linenumbers
		LinkedList<Boolean> dontRegisterStack = new LinkedList<Boolean>(); // If
																			// true
																			// is
																			// popped
																			// the
																			// translations
																			// are
																			// temporary
		LinkedList<BasicBlock> breakStack = new LinkedList<BasicBlock>(); // break
																			// statement
																			// locations.
		LinkedList<Integer> switchVarStack = new LinkedList<Integer>(); // Switch
																		// variables.
		LinkedList<AssumeNode> assumeStack = new LinkedList<AssumeNode>(); // Assume
																			// nodes.
		LinkedList<Integer> enumStack = new LinkedList<Integer>(); // GetProperties
																	// variables
																	// used in
																	// connection
																	// with
																	// 'for-in'
		LinkedList<Integer> localLoadStack = new LinkedList<Integer>(); // Used
																		// to
																		// model
																		// the
																		// LOCAL_LOAD
																		// instructions
																		// of
																		// Rhino.
		LinkedList<Integer> baseVarStack = new LinkedList<Integer>(); // Base
																		// vars
																		// for
																		// simple
																		// variable
																		// reads.
		LinkedList<BasicBlock> finallyStack = new LinkedList<BasicBlock>(); // Duplicated
																			// finally
																			// blocks
		boolean ignorable = false;

		private SourceLocation topSloc; // Source location of the start of the
										// function, used for the standard
										// nodes.

		private int nextTmp = Node.FIRST_ORDINARY_VAR;
		int return_var = nextTemporary();
		private int offset;

		/**
		 * store ContextDependencyNodes, associated with an tagrget node
		 */
		private Map<org.mozilla.javascript.Node, ContextDependencyPopNode> m_ContextDependencyNodes = new HashMap<org.mozilla.javascript.Node, ContextDependencyPopNode>();

		/**
		 * Constructs an AST walker.
		 * 
		 * @param filename
		 * @param root
		 *            Root of the Rhino AST.
		 * @param arguments
		 *            Arguments of the generated function. If null the arguments
		 *            will be taken from AST
		 */
		public RhinoASTWalker(String filename, ScriptOrFnNode root, List<String> arguments, Function currFun, int lineno_offset) {
			setTopSloc(new SourceLocation(root.getLineno() + 1, filename));
			this.offset = lineno_offset;
			setTopSloc(new SourceLocation(root.getLineno() + 1 + this.offset, filename));
			this.fileName = filename;
			this.currFun = currFun;
			if (root instanceof FunctionNode) {
				FunctionNode fnNode = (FunctionNode) root;
				String funname = fnNode.getFunctionName();
				if (funname.length() == 0)
					funname = null;
				res = new Function(funname, (arguments == null ? fnNode.getParamCount() == 0 ? new LinkedList<String>() : getParams(fnNode) : arguments),
						graph, getTopSloc());
			} else {
				res = new Function(null, arguments == null ? new ArrayList<String>() : arguments, graph, getTopSloc());
				graph.setMain(res);
			}
			graph.addFunction(res);
			res.setOuterFunction(currFun);
			this.currFun = res;

			entryBB = new BasicBlock(res);

			Node entry = ConstantNode.makeUndefined(return_var, getTopSloc());
			entryBB.addNode(entry);

			// Declarations basic block
			declsBB = new BasicBlock(res);
			entryBB.addSuccessor(declsBB);
			graph.addBlock(entryBB, res);

			// Start of the graph (after declarations)
			BasicBlock nodeStart = new BasicBlock(res);
			declsBB.addSuccessor(nodeStart);

			// Normal exit node (in a singleton BB)
			Node normal_exit = new ReturnNode(return_var, getTopSloc());
			normal_exitBB = new BasicBlock(res);
			normal_exitBB.addNode(normal_exit);

			// Exceptional exit (singleton BB)
			Node exceptional_exit = new ExceptionalReturnNode(getTopSloc());
			exceptional_exitBB = new BasicBlock(res);
			exceptional_exitBB.addNode(exceptional_exit);
			catchBlocks.push(exceptional_exitBB);

			dontRegisterStack.push(false);
			finallyStack.push(null);

			// Set the exception handlers for the standard parts of the graph.
			entryBB.setExceptionHandler(exceptional_exitBB);
			normal_exitBB.setExceptionHandler(exceptional_exitBB);
			exceptional_exitBB.setExceptionHandler(exceptional_exitBB);
			declsBB.setExceptionHandler(exceptional_exitBB);

			this.currBB = nodeStart;
			res.setEntry(entryBB);
			res.setExceptionalExit(exceptional_exitBB);
			res.setOrdinaryExit(normal_exitBB);
			graph.addBlock(exceptional_exitBB, res);
			graph.addBlock(normal_exitBB, res);
			setTop(root);
		}

		/**
		 * Adds and event loop to the flowgraph. Does not make sense if DOM is
		 * not enabled.
		 */
		public void addEventLoop() {
			newBasicBlock();

			// Load Event Handlers
			for (Function evh : graph.getLoadEventHandlers()) {
				DeclareFunctionNode df = new DeclareFunctionNode(evh, false, nextTmp, getTopSloc());
				addNode(df, null);
				DeclareEventHandlerNode evDecl = new DeclareEventHandlerNode(nextTmp, DeclareEventHandlerNode.Type.LOAD, getTopSloc());
				addNode(evDecl, null);
			}

			// Unload Event Handlers
			for (Function evh : graph.getUnloadEventHandlers()) {
				DeclareFunctionNode df = new DeclareFunctionNode(evh, false, nextTmp, getTopSloc());
				addNode(df, null);
				DeclareEventHandlerNode evDecl = new DeclareEventHandlerNode(nextTmp, DeclareEventHandlerNode.Type.UNLOAD, getTopSloc());
				addNode(evDecl, null);
			}

			// Keyboard Event Handlers
			for (Function evh : graph.getKeyboardEventHandlers()) {
				DeclareFunctionNode decl = new DeclareFunctionNode(evh, false, nextTmp, getTopSloc());
				addNode(decl, null);
				DeclareEventHandlerNode evDecl = new DeclareEventHandlerNode(nextTmp, DeclareEventHandlerNode.Type.KEYBOARD, getTopSloc());
				addNode(evDecl, null);
			}

			// Mouse Event Handlers
			for (Function evh : graph.getMouseEventHandlers()) {
				DeclareFunctionNode decl = new DeclareFunctionNode(evh, false, nextTmp, getTopSloc());
				addNode(decl, null);
				DeclareEventHandlerNode evDecl = new DeclareEventHandlerNode(nextTmp, DeclareEventHandlerNode.Type.MOUSE, getTopSloc());
				addNode(evDecl, null);
			}

			// Unknown Event Handlers
			for (Function evh : graph.getUnknownEventHandlers()) {
				DeclareFunctionNode decl = new DeclareFunctionNode(evh, false, nextTmp, getTopSloc());
				addNode(decl, null);
				DeclareEventHandlerNode evDecl = new DeclareEventHandlerNode(nextTmp, DeclareEventHandlerNode.Type.UNKNOWN, getTopSloc());
				addNode(evDecl, null);
			}

			// Event Entry Block
			newBasicBlock();
			BasicBlock entryBB = currBB;
			addNode(new EventEntryNode(getTopSloc()), null);

			// Load Event Handlers
			newBasicBlock();
			BasicBlock loadBB = currBB;
			EventDispatcherNode loadDispatcher = new EventDispatcherNode(EventDispatcherNode.Type.LOAD, new SourceLocation(0, getTopSloc().getFileName()));
			addNode(loadDispatcher, null);
			newBasicBlock();
			BasicBlock nopPostLoad = currBB;
			addNode(new NopNode(getTopSloc()), null);
			nopPostLoad.addSuccessor(loadBB);

			// Other Event Handlers
			newBasicBlock();
			BasicBlock otherBB = currBB;
			EventDispatcherNode otherDispatcher = new EventDispatcherNode(EventDispatcherNode.Type.OTHER, new SourceLocation(0, getTopSloc().getFileName()));
			addNode(otherDispatcher, null);
			newBasicBlock();
			BasicBlock nopPostOther = currBB;
			addNode(new NopNode(getTopSloc()), null);
			nopPostOther.addSuccessor(otherBB);

			// Unload Event Handlers
			newBasicBlock();
			BasicBlock unloadBB = currBB;
			EventDispatcherNode unloadDispatcher = new EventDispatcherNode(EventDispatcherNode.Type.UNLOAD, new SourceLocation(0, getTopSloc().getFileName()));
			addNode(unloadDispatcher, null);
			newBasicBlock();
			BasicBlock nopPostUnload = currBB;
			addNode(new NopNode(getTopSloc()), null);
			nopPostUnload.addSuccessor(unloadBB);

			entryBB.addSuccessor(nopPostLoad);
			nopPostLoad.addSuccessor(nopPostOther);
			nopPostOther.addSuccessor(nopPostUnload);
			nopPostUnload.addSuccessor(normal_exitBB);

			newBasicBlockNoSucc(false);
		}

		/**
		 * Get parameter names in function
		 * 
		 * @param n
		 *            AST function node
		 * @return A list of the parameter names.
		 */
		private List<String> getParams(FunctionNode n) {
			LinkedList<String> l = new LinkedList<String>();
			// The Symbol class is not public by default in Rhino so the patched
			// version
			// is necessary.
			for (Entry<String, Symbol> x : n.getSymbolTable().entrySet()) {
				if (x.getValue().declType == Token.LP) // If decltype is
														// Token.LP the Symbol
														// is a parameter.
					l.add(x.getKey());
			}
			return l;
		}

		/**
		 * Build a flowgraph representing all files.
		 * 
		 * @return The toplevel function.
		 */
		public Function buildFunction(FunctionNode n) {
			linenoStack.push(n.getLineno() + 1);
			translateAllChildren(n);
			addCurrentBasicBlock();
			graph.addBlock(declsBB, res);
			return res;
		}

		/**
		 * Build a function representing the toplevel of a JS file. ie. the part
		 * not in function.
		 * 
		 * @param n
		 * @return
		 */
		public Function buildTopLevel(ScriptOrFnNode n, String filename) {
			if (res == null) {
				List<String> args = Collections.emptyList();
				res = new Function(null, args, graph, getTopSloc());
			}
			setTop(n);
			linenoStack.push(n.getLineno() + 1);

			translateAllChildren(n);
			this.fileName = filename;
			return res;
		}

		/**
		 * Close the flowgraph. Its not possible to add new functions or files
		 * after.
		 */
		public void close() {
			addCurrentBasicBlock();
			currBB.addSuccessor(normal_exitBB);
			// Add the declarations block
			graph.addBlock(declsBB, res);
		}

		/**
		 * Translate all children of n.
		 * 
		 * @param n
		 */
		private void translateAllChildren(org.mozilla.javascript.Node n) {
			org.mozilla.javascript.Node nm = n.getFirstChild();
			while (nm != null) {
				translate(nm, rContext.STMT);
				assumeStack.clear();
				nm = nm.getNext();
			}
		}

		/**
		 * Translate a node.
		 * 
		 * @param rn
		 *            Rhino node
		 * @param ctx
		 *            Context, either expression or statement.
		 * @return The temporary containing the result if expression or -1 if
		 *         statement.
		 */
		public int translate(org.mozilla.javascript.Node rn, rContext ctx) {
			return translate(rn, nextTemporary(), ctx);
		}

		/**
		 * Translate a Rhino AST tree rooted in rn.
		 * 
		 * @param result_var
		 *            The variable to used to store the result.
		 * @return result_var if expression else -1.
		 */
		public int translate(org.mozilla.javascript.Node rn, int result_var, rContext ctx) {
			if (rn.getType() == Token.TARGET) {
				newBasicBlock();
			}

			if (locations.containsKey(rn) && !dontRegisterStack.peek()) { // only
																			// Token.TARGET
																			// nodes
																			// are
																			// "seen before"
				currBB.addEdgeTo(locations.get(rn));
				addCurrentBasicBlock();
				currBB = locations.get(rn);
			}

			if (!dontRegisterStack.peek())
				locations.put(rn, currBB);

			/*
			 * ############################################################
			 * Dependency: write context dependency node
			 * ############################################################
			 */
			if (m_ContextDependencyNodes.containsKey(rn)) {
				addNode(m_ContextDependencyNodes.get(rn), rn);
				m_ContextDependencyNodes.remove(rn);
			}

			if (rn.getType() == Token.TARGET)
				return -1;
			if (rn.getLineno() > 0)
				linenoStack.push(rn.getLineno() + 1);

			// A hack, but rhino does something similar. Since catch blocks does
			// not get any linenumber assigned
			SourceLocation sloc = new SourceLocation(rn.getType() == Token.CATCH_SCOPE ? rn.getNext().getLineno() : linenoStack.peek(), fileName);
			switch (rn.getType()) {
			case Token.FUNCTION: { // function [id]([args]...) {}
				FunctionNode fn = getTop().getFunctionNode(rn.getExistingIntProp(org.mozilla.javascript.Node.FUNCTION_PROP));

				// fixes, tha all function will be created in line 1
				if (sloc.getLineNumber() != fn.getBaseLineno() + 1) {
					sloc = new SourceLocation(fn.getBaseLineno() + 1, fileName);
				}
				Function f = new RhinoASTWalker(fileName, fn, null, currFun, this.offset).buildFunction(fn);
				DeclareFunctionNode fnN = new DeclareFunctionNode(f, ctx == rContext.EXPR, result_var, sloc);
				if (ctx == rContext.EXPR)
					addNode(fnN, rn);
				else
					addDecl(fnN, rn);
				break;
			}
			case Token.SETNAME: {
				if (rn.getLastChild().getType() == Token.THISFN) {
					result_var = -1;
					break;
				}
				String varName = rn.getFirstChild().getString();
				int val = translate(rn.getLastChild(), result_var, rContext.STMT);
				WriteVariableNode wn = new WriteVariableNode(val, varName, sloc);
				addNode(wn, rn);
				break;
			}
			case Token.CONST: // FIXME: NOT SOUND, but const is not part of
								// ECMAScript anyway.
			case Token.VAR: { // var ([id] = [exp]?)+
				org.mozilla.javascript.Node child = rn.getFirstChild();
				while (child != null) {
					String id = child.getString();
					DeclareVariableNode vn = new DeclareVariableNode(id, sloc);
					addDecl(vn, rn);
					if (child.getFirstChild() != null) {
						int expTmp = translate(child.getFirstChild(), rContext.EXPR);
						WriteVariableNode wn = new WriteVariableNode(expTmp, id, sloc);
						addNode(wn, rn);
						// return expTmp;
					}
					child = child.getNext();
				}
				result_var = -1;
				break;
			}
			case Token.LOCAL_BLOCK:
			case Token.BLOCK: {
				translateAllChildren(rn);
				if (rn.getFirstChild() != null && rn.getFirstChild().getType() == Token.CATCH_SCOPE && finallyStack.peek() != null)
					catchBlocks.pop();
				result_var = -1;
				break;
			}
			case Token.EXPR_VOID:
			case Token.EXPR_RESULT: { // [expr]; Expression statements.
				translateAllChildren(rn);
				result_var = -1;
				break;
			}
			case Token.NEW:
			case Token.CALL: { // [new] [fn-name]([arg1],...,[argn])
				// First child is the function name, remaining are arguments.
				org.mozilla.javascript.Node fnNameNode = rn.getFirstChild();
				int objVar = Node.NO_VALUE;
				int fnVal;
				if (fnNameNode.getType() == Token.GETPROP || fnNameNode.getType() == Token.GETELEM) {
					ReadPropertyNode redN;
					objVar = translate(fnNameNode.getFirstChild(), rContext.EXPR);
					fnVal = nextTemporary();
					if (fnNameNode.getLastChild().getType() == Token.STRING)
						redN = new ReadPropertyNode(objVar, fnNameNode.getLastChild().getString(), fnVal, sloc);
					else
						redN = new ReadPropertyNode(objVar, translate(fnNameNode.getLastChild(), rContext.EXPR), fnVal, sloc);
					addNode(redN, rn);
				} else if (fnNameNode.getType() == Token.NAME) {
					fnVal = translate(fnNameNode, rContext.EXPR);
					objVar = baseVarStack.pop();
					// TODO: FIXME: baseVar with function call?
				} else {
					fnVal = translate(fnNameNode, rContext.EXPR);
				}
				org.mozilla.javascript.Node arg = fnNameNode.getNext();
				List<Integer> args = new LinkedList<Integer>();
				while (arg != null) {
					args.add(translate(arg, rContext.EXPR));
					arg = arg.getNext();
				}
				int[] argArray = new int[args.size()];
				for (int i = 0; i < argArray.length; i++) {
					argArray[i] = args.get(i);
				}

				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				newBasicBlock();
				CallNode call = new CallNode(rn.getType() == Token.NEW, result_var, objVar, fnVal, argArray, sloc);
				addNode(call, rn);
				newBasicBlock();

				// ##################################################
				addNode(contextDependencyPopNode, rn);
				// ##################################################

				break;
			}
			case Token.NAME: { // var reference
				int result_base_var = nextTemporary();
				baseVarStack.push(result_base_var);
				ReadVariableNode r = new ReadVariableNode(rn.getString(), result_var, result_base_var, sloc);
				addNode(r, rn);
				break;
			}
			case Token.STRING: { // [string]
				addNode(ConstantNode.makeString(rn.getString(), result_var, sloc), rn);
				break;
			}
			case Token.NUMBER: { // [number]
				addNode(ConstantNode.makeNumber(rn.getDouble(), result_var, sloc), rn);
				break;
			}
			case Token.TYPEOF:
			case Token.TYPEOFNAME: {
				TypeofNode ton;
				if (rn.getType() == Token.TYPEOF) {
					int op = translate(rn.getFirstChild(), rContext.EXPR);
					ton = new TypeofNode(op, result_var, sloc);
				} else {
					ton = new TypeofNode(rn.getString(), result_var, sloc);
				}
				addNode(ton, rn);
				break;
			}
			case Token.BITNOT:
			case Token.NOT:
			case Token.POS:
			case Token.NEG: { // [op] [operand]
				int operand = translate(rn.getFirstChild(), rContext.EXPR);
				UnaryOperatorNode un = new UnaryOperatorNode(getFlowGraphUnaryOp(rn.getType()), operand, result_var, sloc);
				addNode(un, rn);
				break;
			}
			case Token.IN:
			case Token.DIV:
			case Token.LSH:
			case Token.MOD:
			case Token.MUL:
			case Token.RSH:
			case Token.SUB:
			case Token.URSH:
			case Token.BITAND:
			case Token.BITOR:
			case Token.BITXOR:
			case Token.GE:
			case Token.GT:
			case Token.LE:
			case Token.LT:
			case Token.NE:
			case Token.SHNE:
			case Token.ADD:
			case Token.SHEQ:
			case Token.EQ:
			case Token.INSTANCEOF: // [left-operand] [op] [right-operand]
			{
				int vLop = translate(rn.getFirstChild(), rContext.EXPR);
				int vRop = translate(rn.getLastChild(), rContext.EXPR);

				BinaryOperatorNode op = new BinaryOperatorNode(getFlowGraphBinaryOp(rn.getType()), vLop, vRop, result_var, sloc);
				addNode(op, rn);
				break;
			}
			case Token.INC:
			case Token.DEC: {
				Op o = (rn.getType() == Token.INC ? Op.ADD : Op.SUB);
				boolean isPost = (rn.getIntProp(org.mozilla.javascript.Node.INCRDECR_PROP, -1) & org.mozilla.javascript.Node.POST_FLAG) != 0;
				int one = nextTemporary();
				ConstantNode cn = ConstantNode.makeNumber(1, one, sloc);
				addNode(cn, rn);
				dontRegisterStack.push(true);// dontRegister = true;
				int res = translate(rn.getFirstChild(), nextTemporary(), rContext.EXPR);
				int conv = isPost ? result_var : nextTemporary();
				UnaryOperatorNode uno = new UnaryOperatorNode(UnaryOperatorNode.Op.PLUS, res, conv, sloc);
				addNode(uno, rn);
				int after = isPost ? nextTemporary() : result_var;
				BinaryOperatorNode bm = new BinaryOperatorNode(o, conv, one, after, sloc);
				addNode(bm, rn);
				// Write back result
				if (rn.getFirstChild().getType() == Token.GETELEM || rn.getFirstChild().getType() == Token.GETPROP) {
					org.mozilla.javascript.Node getProp = rn.getFirstChild();
					int obj = translate(getProp.getFirstChild(), rContext.EXPR);
					WritePropertyNode wn;
					if (getProp.getLastChild().getType() == Token.STRING)
						wn = new WritePropertyNode(obj, getProp.getLastChild().getString(), after, sloc);
					else
						wn = new WritePropertyNode(obj, translate(getProp.getLastChild(), rContext.EXPR), after, sloc);
					addNode(wn, rn);
				} else {
					String varName = rn.getFirstChild().getString();
					WriteVariableNode wn = new WriteVariableNode(after, varName, sloc);
					addNode(wn, rn);
				}
				dontRegisterStack.pop();
				break;
			}
			case Token.IFNE:
			case Token.IFEQ: {
				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				m_ContextDependencyNodes.put(rn.getLastSibling(), contextDependencyPopNode);
				// ##################################################

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				int condVar = translate(rn.getFirstChild(), rContext.EXPR);

				IfNode branch = new IfNode(condVar, sloc);
				addNode(branch, rn);
				org.mozilla.javascript.Node target = ((org.mozilla.javascript.Node.Jump) rn).target;

				if (locations.containsKey(target))
					currBB.addSuccessor(locations.get(target));
				else {
					BasicBlock tbb = new BasicBlock(res);
					locations.put(target, tbb);
					currBB.addSuccessor(tbb);
				}

				newBasicBlock();

				if (rn.getType() == Token.IFEQ)
					branch.setSuccessors(locations.get(target), currBB);
				else
					branch.setSuccessors(currBB, locations.get(target));

				// Insert assume nodes for comparisons with null
				if (rn.getFirstChild().getType() == Token.NE) {
					org.mozilla.javascript.Node nnEntity = null;
					if (rn.getFirstChild().getFirstChild().getType() == Token.NULL)
						nnEntity = rn.getFirstChild().getLastChild();
					else if ((rn.getFirstChild().getLastChild().getType() == Token.NULL))
						nnEntity = rn.getFirstChild().getFirstChild();
					if (nnEntity != null) {
						AssumeNode assN = null;
						if (nnEntity.getType() == Token.NAME)
							assN = AssumeNode.makeVariableNonNullUndef(nnEntity.getString(), sloc);
						else if (nnEntity.getType() == Token.GETPROP || nnEntity.getType() == Token.GETELEM) {
							assN = assumeStack.peek();
						}
						if (assN != null) {
							if (rn.getType() == Token.IFNE)
								addNode(assN, rn);
						}
					}
				}

				result_var = -1;
				break;
			}
			case Token.TARGET: {
				newBasicBlock();
				if (locations.containsKey(rn)) {
					addCurrentBasicBlock();
					currBB = locations.get(rn);
				} else {
					locations.put(rn, currBB);
				}
				result_var = -1;
				break;
			}
			case Token.JSR:
			case Token.GOTO: {
				org.mozilla.javascript.Node tar = ((org.mozilla.javascript.Node.Jump) rn).target;

				if (locations.containsKey(tar)) {
					currBB.addSuccessor(locations.get(tar));
				} else {
					BasicBlock tbb = new BasicBlock(res);
					locations.put(tar, tbb);
					currBB.addSuccessor(tbb);
				}
				newBasicBlockNoSucc(false);
				result_var = -1;
				break;
			}
			case Token.TRUE:
			case Token.FALSE: {
				addNode(ConstantNode.makeBoolean(rn.getType() == Token.TRUE, result_var, sloc), rn);
				break;
			}
			case Token.NULL: {
				addNode(ConstantNode.makeNull(result_var, sloc), rn);
				break;
			}
			case Token.GETELEM:
			case Token.GETPROP:
				// FIXME: ============ correct to handle GET_REF and SET_REF
				// like this?
			case Token.GET_REF: { // [obj].[prop]
				int objVar = translate(rn.getFirstChild(), rContext.EXPR);
				ReadPropertyNode read;
				AssumeNode assume;

				if (rn.getLastChild().getType() == Token.STRING) {
					read = new ReadPropertyNode(objVar, rn.getLastChild().getString(), result_var, sloc);
					assume = AssumeNode.makePropertyNonNullUndef(objVar, rn.getLastChild().getString(), read, sloc);
				} else {
					int strVar = translate(rn.getFirstChild().getNext(), rContext.EXPR);
					read = new ReadPropertyNode(objVar, strVar, result_var, sloc);
					assume = AssumeNode.makePropertyNonNullUndef(objVar, strVar, read, sloc);
				}
				addNode(read, rn);
				if (rn.getFirstChild().getType() == Token.NAME)
					addNode(AssumeNode.makeVariableNonNullUndef(rn.getFirstChild().getString(), sloc), rn);
				if (rn.getFirstChild().getType() == Token.SETPROP || rn.getFirstChild().getType() == Token.GETPROP) {
					addNode(assumeStack.pop(), rn);
				}
				assumeStack.push(assume);
				break;
			}
			case Token.SETPROP:
			case Token.SETELEM:
			case Token.SET_REF: { // [obj].[prop] = [val]
				org.mozilla.javascript.Node objNode = rn.getFirstChild();
				org.mozilla.javascript.Node propNode = objNode.getNext();
				org.mozilla.javascript.Node valNode = rn.getLastChild();
				AssumeNode ass;

				Node wn;
				int objVar = translate(objNode, rContext.EXPR);
				if (propNode.getType() != Token.STRING) {
					int strVar = translate(propNode, rContext.EXPR);
					wn = new WritePropertyNode(objVar, strVar, translate(valNode, result_var, rContext.EXPR), sloc);
					ass = AssumeNode.makePropertyNonNullUndef(objVar, strVar, wn, sloc);
				} else {
					wn = new WritePropertyNode(objVar, propNode.getString(), translate(valNode, result_var, rContext.EXPR), sloc);
					ass = AssumeNode.makePropertyNonNullUndef(objVar, propNode.getString(), wn, sloc);
				}
				addNode(wn, rn);
				if (rn.getFirstChild().getType() == Token.NAME)
					addNode(AssumeNode.makeVariableNonNullUndef(rn.getFirstChild().getString(), sloc), rn);
				if (rn.getFirstChild().getType() == Token.SETPROP || rn.getFirstChild().getType() == Token.GETPROP) {
					addNode(assumeStack.pop(), rn);
				}
				assumeStack.push(ass);
				break;
			}
			case Token.SETPROP_OP:
			case Token.SETELEM_OP: {
				org.mozilla.javascript.Node baseN = rn.getFirstChild();
				org.mozilla.javascript.Node propN = baseN.getNext();

				int base = translate(baseN, rContext.EXPR);
				int prop = translate(propN, rContext.EXPR);
				int op = nextTemporary();
				ReadPropertyNode rpn = new ReadPropertyNode(base, prop, op, sloc);
				addNode(rpn, rn);
				valueStack.push(op);
				translate(propN.getNext(), result_var, rContext.EXPR);

				WritePropertyNode wn;
				if (propN.getType() == Token.STRING)
					wn = new WritePropertyNode(base, propN.getString(), result_var, sloc);
				else
					wn = new WritePropertyNode(base, prop, result_var, sloc);
				addNode(wn, rn);
				break;
			}
			case Token.THISFN:
				throw new RuntimeException("Encountered THISFN"); // handled in
																	// function
																	// calls.
			case Token.THIS: {
				ReadVariableNode read = new ReadVariableNode("this", result_var, Node.NO_VALUE, sloc);
				addNode(read, rn);
				break;
			}
			case Token.LOOP: { // Generic loop node used by all looping
								// constructs, loop semantics are encoded by
								// gotos.
				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				translateAllChildren(rn);

				// ##################################################
				addNode(contextDependencyPopNode, rn);
				// ##################################################

				if (rn.getFirstChild().getType() == Token.ENUM_INIT_KEYS || rn.getFirstChild().getNext().getType() == Token.ENUM_INIT_KEYS) // for-in
																																			// loop.
					enumStack.pop();
				result_var = -1;
				break;
			}
			case Token.BREAK:
			case Token.CONTINUE: {
				org.mozilla.javascript.Node tar = (rn.getType() == Token.BREAK ? ((org.mozilla.javascript.Node.Jump) rn).getJumpStatement().target
						: ((org.mozilla.javascript.Node.Jump) rn).getJumpStatement().getContinue());
				if (locations.containsKey(tar))
					currBB.addSuccessor(locations.get(tar));
				else {
					BasicBlock tbb = new BasicBlock(res);
					locations.put(tar, tbb);
					currBB.addSuccessor(tbb);
				}
				newBasicBlockNoSucc(false);
				result_var = -1;
				break;
			}

			case Token.EMPTY: {
				result_var = -1;
				break;
			}
			case Token.RETURN: {
				if (rn.getFirstChild() != null) {
					translate(rn.getFirstChild(), return_var, rContext.EXPR);
				}
				currBB.addSuccessor(normal_exitBB);
				newBasicBlockNoSucc(false);
				result_var = -1;
				break;
			}
			case Token.TRY: {
				org.mozilla.javascript.Node catchB = ((org.mozilla.javascript.Node.Jump) rn).target;
				newBasicBlock();
				// Find the finally block, if any.
				org.mozilla.javascript.Node finallyN = rn.getFirstChild();
				while (finallyN != null) {
					if (finallyN.getType() == Token.FINALLY)
						break;
					finallyN = finallyN.getNext();
				}
				BasicBlock oldBB = currBB;
				if (finallyN != null && catchB != null) {
					finallyStack.push(newBasicBlockNoSucc(true));
					Map<org.mozilla.javascript.Node, BasicBlock> oldLocs = new HashMap<org.mozilla.javascript.Node, BasicBlock>(locations);
					boolean oldIg = ignorable;
					ignorable = true;
					int ex = nextTemporary();
					CatchNode cn = new CatchNode(ex, sloc); // FIXME: always the
															// first node in its
															// block?
					graph.addIgnorableNode(cn);
					addNode(cn, rn);
					translate(finallyN, rContext.STMT);
					ThrowNode tn = new ThrowNode(ex, sloc);
					graph.addIgnorableNode(tn);
					addNode(tn, rn);
					ignorable = oldIg;
					addCurrentBasicBlock();
					currBB.addSuccessor(catchBlocks.peek());
					currBB = oldBB;
					newBasicBlock();
					locations = oldLocs;
				} else
					finallyStack.push(null);
				if (catchB != null) {
					BasicBlock catchBB = getTarget(catchB);
					catchBlocks.push(catchBB);
				}

				translateAllChildren(rn);
				finallyStack.pop();
				result_var = -1;
				break;
			}
			case Token.CATCH_SCOPE: {
				String varName = rn.getFirstChild().getString();
				catchBlocks.pop();
				if (finallyStack.peek() != null)
					catchBlocks.push(finallyStack.peek());
				int obj = nextTemporary();
				CatchNode cn = new CatchNode(varName, obj, sloc);
				localLoadStack.push(obj);
				addNode(cn, rn);
				result_var = -1;
				break;
			}
			case Token.THROW: {
				int throwVal = translate(rn.getFirstChild(), rContext.EXPR);
				ThrowNode tn = new ThrowNode(throwVal, sloc);
				addNode(tn, rn);
				currBB.addSuccessor(catchBlocks.peek());
				newBasicBlockNoSucc(false);
				result_var = -1;
				break;
			}
			case Token.FINALLY: {
				translateAllChildren(rn);
				result_var = -1;
				break;
			}
			case Token.ENTERWITH: {
				ignoreLeaveWith.push(false);
				int withVal = translate(rn.getFirstChild(), rContext.EXPR);
				EnterWithNode en = new EnterWithNode(withVal, sloc);
				addNode(en, rn);
				newBasicBlock();
				result_var = -1;
				break;
			}
			case Token.LEAVEWITH: {
				// A LEAVEWITH *without* matching ENTERWITH is inserted when
				// translating a catch block.
				if (ignoreLeaveWith.pop()) {
					result_var = -1;
					break;
				}
				LeaveWithNode lw = new LeaveWithNode(sloc);
				graph.addIgnorableNode(lw);
				addNode(lw, rn);
				result_var = -1;
				break;
			}
			case Token.WITH: {
				ignoreLeaveWith.add(true);
				BasicBlock lwb = new BasicBlock(res);
				lwb.addSuccessor(catchBlocks.peek());
				Node lwn = new LeaveWithNode(sloc);
				lwb.addNode(lwn);
				graph.addIgnorableNode(lwn);
				catchBlocks.push(lwb);
				translateAllChildren(rn);
				catchBlocks.pop();
				graph.addBlock(lwb, res);
				result_var = -1;
				break;
			}
			case Token.ARRAYLIT: { // [[exp]*]
				org.mozilla.javascript.Node nn = rn.getFirstChild();
				int i = 0;
				int arr = nextTemporary();
				int[] skips = (int[]) rn.getProp(org.mozilla.javascript.Node.SKIP_INDEXES_PROP);
				int skip = 0;
				int result_base_var = nextTemporary();
				ReadVariableNode readArray = new ReadVariableNode("Array", arr, result_base_var, sloc); // FIXME:
																										// need
																										// result_base_var
																										// here?
				baseVarStack.push(result_base_var);
				addNode(readArray, rn);
				List<Integer> args = new LinkedList<Integer>();

				int undef = 0;
				if (skips != null) {
					undef = nextTemporary();
					addNode(ConstantNode.makeUndefined(undef, sloc), rn);
				}

				while (nn != null) {
					if (skips != null && skip < skips.length && skips[skip] == i) {
						skip++;
						i++;
						args.add(undef);
						continue;
					}
					int val = translate(nn, rContext.EXPR);
					args.add(val);

					nn = nn.getNext();
					i++;
				}

				int[] arg_vars = new int[args.size()];
				for (int j = 0; j < args.size(); j++) {
					arg_vars[j] = args.get(j);
				}

				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				newBasicBlock();
				CallNode cn = new CallNode(true, result_var, Node.NO_VALUE, arr, arg_vars, sloc);
				addNode(cn, rn);
				newBasicBlock();

				// ##################################################
				addNode(contextDependencyPopNode, rn);
				// ##################################################

				break;
			}
			case Token.OBJECTLIT: { // {(prop: [exp])*}
				Object[] propertyList = (Object[]) rn.getProp(org.mozilla.javascript.Node.OBJECT_IDS_PROP);
				org.mozilla.javascript.Node n = rn.getFirstChild();
				int nameIdx = 0;
				NewObjectNode newObj = new NewObjectNode(res, result_var, sloc);
				addNode(newObj, rn);

				while (n != null) {
					int val = translate(n, rContext.EXPR);
					Object propName = propertyList[nameIdx++];
					// SPEC: 11.1.5 A property name can be either a string or
					// number literal.
					String name = propName instanceof Integer ? ((Integer) propName).toString() : (String) propName;
					int propVar = nextTemporary();
					ConstantNode str = ConstantNode.makeString(name, propVar, sloc);
					addNode(str, rn);
					WritePropertyNode wn = new WritePropertyNode(result_var, propVar, val, sloc);
					addNode(wn, rn);

					n = n.getNext();
				}
				break;
			}
			case Token.DELPROP: { // delete [exp] or delete [identifier]
				DeletePropertyNode dpn;
				if (rn.getFirstChild().getType() != Token.BINDNAME) {
					int obj = translate(rn.getFirstChild(), rContext.EXPR);
					int prop = translate(rn.getFirstChild().getNext(), rContext.EXPR);
					dpn = new DeletePropertyNode(obj, prop, result_var, sloc);
				} else {
					String varName = rn.getFirstChild().getString();
					dpn = new DeletePropertyNode(varName, result_var, sloc);
				}
				addNode(dpn, rn);
				if (rn.getFirstChild().getType() == Token.NAME)
					addNode(AssumeNode.makeVariableNonNullUndef(rn.getFirstChild().getString(), sloc), rn);
				if (rn.getFirstChild().getType() == Token.SETPROP || rn.getFirstChild().getType() == Token.GETPROP) {
					addNode(assumeStack.pop(), rn);
				}
				break;
			}
			case Token.USE_STACK: { // Used in connection with [op]=
									// construction.
				result_var = valueStack.pop();
				break;
			}
			case Token.OR:
			case Token.AND: { // [exp] AND/OR [exp]
				int valL = translate(rn.getFirstChild(), result_var, rContext.EXPR);

				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				IfNode ifn = new IfNode(valL, sloc);
				addNode(ifn, rn);
				BasicBlock bPoint = currBB;
				BasicBlock trueBranch = new BasicBlock(res);
				BasicBlock falseBranch = newBasicBlock();
				bPoint.addSuccessor(falseBranch);
				bPoint.addSuccessor(trueBranch);
				translate(rn.getFirstChild().getNext(), result_var, rContext.EXPR);
				currBB.addSuccessor(trueBranch);
				addCurrentBasicBlock();
				currBB = trueBranch;
				if (rn.getType() == Token.AND)
					ifn.setSuccessors(falseBranch, trueBranch);
				else
					ifn.setSuccessors(trueBranch, falseBranch);

				// ##################################################
				addNode(contextDependencyPopNode, rn);
				// ##################################################
				break;
			}
			case Token.COMMA: { // [exp]+ -- evaluates all exp, result is the
								// last exp.
				org.mozilla.javascript.Node n = rn.getFirstChild();
				while (n != null) {
					if (n.getNext() != null) {
						translate(n, rContext.EXPR);
					} else
						translate(n, result_var, rContext.EXPR);
					n = n.getNext();
				}
				break;
			}
			case Token.VOID: { // void [exp] -- Evaluates expression and returns
								// undefined.
				translate(rn.getFirstChild(), rContext.EXPR);
				addNode(ConstantNode.makeUndefined(result_var, sloc), rn);
				break;
			}
			case Token.HOOK: { // [exp] ? [exp] : [exp]
				org.mozilla.javascript.Node condN = rn.getFirstChild();
				org.mozilla.javascript.Node trueBN = condN.getNext();
				org.mozilla.javascript.Node falseBN = trueBN.getNext();

				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				int cond = translate(condN, rContext.EXPR);
				IfNode ifn = new IfNode(cond, sloc);
				addNode(ifn, rn);
				BasicBlock bPoint = currBB;
				BasicBlock trueBranch = new BasicBlock(res);
				BasicBlock meet = new BasicBlock(res);
				BasicBlock falseBranch = newBasicBlock();
				bPoint.addSuccessor(falseBranch);
				bPoint.addSuccessor(trueBranch);
				translate(falseBN, result_var, rContext.EXPR);
				currBB.addSuccessor(meet);
				addCurrentBasicBlock();
				currBB = trueBranch;
				translate(trueBN, result_var, rContext.EXPR);
				currBB.addSuccessor(meet);
				addCurrentBasicBlock();
				currBB = meet;
				ifn.setSuccessors(trueBranch, falseBranch);

				// ##################################################
				addNode(contextDependencyPopNode, rn);
				// ##################################################
				break;
			}
			case Token.SWITCH: {
				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				BasicBlock breakTo = getTarget(((org.mozilla.javascript.Node.Jump) rn).target);
				breakStack.push(breakTo);
				int switchVar = translate(rn.getFirstChild(), rContext.EXPR);
				switchVarStack.push(switchVar);

				org.mozilla.javascript.Node n = rn.getFirstChild().getNext();
				while (n != null) {
					translate(n, rContext.STMT);
					n = n.getNext();
				}
				switchVarStack.pop();
				breakStack.pop();

				// ##################################################
				m_ContextDependencyNodes.put(rn.getLastSibling(), contextDependencyPopNode);
				// ##################################################

				break;
			}
			case Token.CASE: {
				BasicBlock target = getTarget(((org.mozilla.javascript.Node.Jump) rn).target);
				int compTo = translate(rn.getFirstChild(), rContext.EXPR);
				int resV = nextTemporary();
				BinaryOperatorNode bn = new BinaryOperatorNode(Op.EQ, switchVarStack.peek(), compTo, resV, sloc);
				addNode(bn, rn);
				IfNode ifn = new IfNode(resV, sloc);
				addNode(ifn, rn);

				BasicBlock branchP = currBB;
				BasicBlock nextC = newBasicBlock();
				ifn.setSuccessors(target, nextC);
				branchP.addSuccessor(target);

				result_var = -1;
				break;
			}
			case Token.LABEL: { // Safe to ignore;
				result_var = -1;
				break;
			}
			case Token.REGEXP: { // /[regexp]/[flags]
				// translate into -> new RegExp(exp,flags).
				int regexIdx = rn.getIntProp(org.mozilla.javascript.Node.REGEXP_PROP, -1);
				String flags = getTop().getRegexpFlags(regexIdx);
				String str = getTop().getRegexpString(regexIdx);

				int strVar = nextTemporary();
				int flagsVar = nextTemporary();
				ConstantNode strNode = ConstantNode.makeString(str, strVar, sloc);
				ConstantNode flagsNode = ConstantNode.makeString(flags, flagsVar, sloc);
				addNode(strNode, rn);
				addNode(flagsNode, rn);
				int cons = nextTemporary();
				ReadVariableNode readRegExpConstructor = new ReadVariableNode("RegExp", cons, nextTemporary(), sloc);// FIXME:
																														// need
																														// result_base_var
																														// here?
				addNode(readRegExpConstructor, rn);

				/*
				 * ############################################################
				 * Dependency: ContextDependencyNode
				 * ############################################################
				 */
				ContextDependencyPopNode contextDependencyPopNode = new ContextDependencyPopNode(sloc);
				ContextDependencyPushNode contextDependencyPushNode = new ContextDependencyPushNode(contextDependencyPopNode, sloc);

				// ##################################################
				addNode(contextDependencyPushNode, rn);
				// ##################################################

				newBasicBlock();
				CallNode cn = new CallNode(true, result_var, Node.NO_VALUE, cons, new int[] { strVar, flagsVar }, sloc);
				addNode(cn, rn);
				newBasicBlock();

				// ##################################################
				addNode(contextDependencyPopNode, rn);
				// ##################################################

				break;
			}
			case Token.ENUM_INIT_KEYS: {
				GetPropertiesNode gp = new GetPropertiesNode(translate(rn.getFirstChild(), rContext.EXPR), result_var, sloc);
				addNode(gp, rn);
				enumStack.push(result_var);
				break;
			}
			case Token.ENUM_ID: {
				NextPropertyNode gn = new NextPropertyNode(enumStack.peek(), result_var, sloc);
				addNode(gn, rn);
				break;
			}
			case Token.ENUM_NEXT: {
				HasNextPropertyNode hn = new HasNextPropertyNode(enumStack.peek(), result_var, sloc);
				addNode(hn, rn);
				break;
			}
			case Token.LOCAL_LOAD: {
				result_var = localLoadStack.pop();
				break;
			}
			default:
				throw new RuntimeException("Unknown node type: " + Token.name(rn.getType()) + " at " + sloc);
			}
			if (rn.getLineno() > 0)
				linenoStack.pop();
			return result_var;
		}

		/**
		 * Token.TARGET nodes are the target of GOTOs in the Rhino AST. This
		 * function returns either the BasicBlock which represent the control
		 * flow location equivalent to the TARGET node or creates a new basic
		 * block and making it the representation of the TARGET node.
		 * 
		 * @param target
		 * @return
		 */
		private BasicBlock getTarget(org.mozilla.javascript.Node target) {
			if (locations.containsKey(target))
				return locations.get(target);
			else {
				BasicBlock bb = new BasicBlock(res);
				locations.put(target, bb);
				return bb;
			}
		}

		/**
		 * Add the current basic block to the graph and set the exception
		 * handler.
		 */
		private void addCurrentBasicBlock() {
			currBB.setExceptionHandler(catchBlocks.peek());
			if (!currBB.isAdded())
				graph.addBlock(currBB, res);
			return;
		}

		private int nextTemporary() {
			return nextTmp++;
		}

		/**
		 * Rhino binary operator -> TAJS flowgraph operator.
		 */
		private Op getFlowGraphBinaryOp(int rhinoType) {
			switch (rhinoType) {
			case Token.DIV:
				return Op.DIV;
			case Token.LSH:
				return Op.SHL;
			case Token.MOD:
				return Op.REM;
			case Token.MUL:
				return Op.MUL;
			case Token.RSH:
				return Op.SHR;
			case Token.SUB:
				return Op.SUB;
			case Token.URSH:
				return Op.USHR;
			case Token.GE:
				return Op.GE;
			case Token.GT:
				return Op.GT;
			case Token.LE:
				return Op.LE;
			case Token.LT:
				return Op.LT;
			case Token.NE:
				return Op.NE;
			case Token.SHNE:
				return Op.SNE;
			case Token.ADD:
				return Op.ADD;
			case Token.SHEQ:
				return Op.SEQ;
			case Token.EQ:
				return Op.EQ;
			case Token.BITOR:
				return Op.OR;
			case Token.BITAND:
				return Op.AND;
			case Token.BITXOR:
				return Op.XOR;
			case Token.INSTANCEOF:
				return Op.INSTANCEOF;
			case Token.IN:
				return Op.IN;
			default:
				throw new RuntimeException("Unknown binary operator: " + rhinoType);
			}
		}

		/**
		 * Rhino unary operator -> TAJS flowgraph operator.
		 */
		private UnaryOperatorNode.Op getFlowGraphUnaryOp(int rhinoType) {
			switch (rhinoType) {
			case Token.BITNOT:
				return UnaryOperatorNode.Op.COMPLEMENT;
			case Token.NOT:
				return UnaryOperatorNode.Op.NOT;
			case Token.POS:
				return UnaryOperatorNode.Op.PLUS;
			case Token.NEG:
				return UnaryOperatorNode.Op.MINUS;
			default:
				throw new RuntimeException("Unknown unary operator: " + rhinoType);
			}
		}

		/**
		 * Start a new basic block as a successor, the current basic block is
		 * added to the flowgraph. If the current block is empty, no new block
		 * is started.
		 */
		private BasicBlock newBasicBlock() {
			if (currBB.getNodes().isEmpty())
				return currBB;
			BasicBlock bb = new BasicBlock(res);
			addCurrentBasicBlock();
			currBB.addSuccessor(bb);
			currBB = bb;
			return bb;
		}

		/**
		 * Add the current block to the graph and start a new, making it the
		 * current basic block after returning.
		 * 
		 * @param dontAdd
		 *            Don't add the current block to the graph. The block MUST
		 *            be added at some other time.
		 * @return The new BasicBlock
		 */
		private BasicBlock newBasicBlockNoSucc(boolean dontAdd) {
			BasicBlock bb = new BasicBlock(res);
			if (!dontAdd)
				addCurrentBasicBlock();
			currBB = bb;
			return bb;
		}

		/**
		 * Add a node to the graph.
		 * 
		 * @param n
		 * @param rhinoNode
		 */
		private void addNode(Node n, org.mozilla.javascript.Node rhinoNode) {
			if (currBB.isAdded())
				throw new RuntimeException("Adding node to already added BasicBlock!");
			if (ignorable)
				graph.addIgnorableNode(n);
			currBB.addNode(n);
			if (rhinoNode != null)
				locations.put(rhinoNode, currBB);
		}

		/**
		 * Add a declaration to the front of the graph.
		 * 
		 * @param n
		 * @param rhinoNode
		 */
		private void addDecl(Node n, org.mozilla.javascript.Node rhinoNode) {
			declsBB.addNode(n);
			locations.put(rhinoNode, declsBB);
		}

		public SourceLocation getTopSloc() {
			return topSloc;
		}

		public void setTopSloc(SourceLocation s) {
			topSloc = s;
		}

		public void setTop(ScriptOrFnNode top) {
			this.top = top;
		}

		public ScriptOrFnNode getTop() {
			return top;
		}
	}
}