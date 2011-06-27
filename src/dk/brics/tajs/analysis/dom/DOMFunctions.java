package dk.brics.tajs.analysis.dom;

import static dk.brics.tajs.analysis.InitialStateBuilder.FUNCTION_PROTOTYPE;
import static dk.brics.tajs.analysis.InitialStateBuilder.createInternalPrototype;
import static dk.brics.tajs.analysis.InitialStateBuilder.createPrimitiveFunction;
import static dk.brics.tajs.analysis.InitialStateBuilder.createProperty;
import static dk.brics.tajs.analysis.InitialStateBuilder.createUnknownArrayProperty;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.core.DOMCharacterData;
import dk.brics.tajs.analysis.dom.core.DOMConfiguration;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.analysis.dom.core.DOMElement;
import dk.brics.tajs.analysis.dom.core.DOMImplementation;
import dk.brics.tajs.analysis.dom.core.DOMNamedNodeMap;
import dk.brics.tajs.analysis.dom.core.DOMNode;
import dk.brics.tajs.analysis.dom.core.DOMNodeList;
import dk.brics.tajs.analysis.dom.core.DOMStringList;
import dk.brics.tajs.analysis.dom.core.DOMText;
import dk.brics.tajs.analysis.dom.event.DocumentEvent;
import dk.brics.tajs.analysis.dom.event.Event;
import dk.brics.tajs.analysis.dom.event.EventListener;
import dk.brics.tajs.analysis.dom.event.EventTarget;
import dk.brics.tajs.analysis.dom.event.KeyboardEvent;
import dk.brics.tajs.analysis.dom.event.MouseEvent;
import dk.brics.tajs.analysis.dom.event.MutationEvent;
import dk.brics.tajs.analysis.dom.event.UIEvent;
import dk.brics.tajs.analysis.dom.event.WheelEvent;
import dk.brics.tajs.analysis.dom.html.HTMLAnchorElement;
import dk.brics.tajs.analysis.dom.html.HTMLAppletElement;
import dk.brics.tajs.analysis.dom.html.HTMLAreaElement;
import dk.brics.tajs.analysis.dom.html.HTMLBRElement;
import dk.brics.tajs.analysis.dom.html.HTMLBaseElement;
import dk.brics.tajs.analysis.dom.html.HTMLBaseFontElement;
import dk.brics.tajs.analysis.dom.html.HTMLBodyElement;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.analysis.dom.html.HTMLButtonElement;
import dk.brics.tajs.analysis.dom.html.HTMLCollection;
import dk.brics.tajs.analysis.dom.html.HTMLDListElement;
import dk.brics.tajs.analysis.dom.html.HTMLDirectoryElement;
import dk.brics.tajs.analysis.dom.html.HTMLDivElement;
import dk.brics.tajs.analysis.dom.html.HTMLDocument;
import dk.brics.tajs.analysis.dom.html.HTMLElement;
import dk.brics.tajs.analysis.dom.html.HTMLFieldSetElement;
import dk.brics.tajs.analysis.dom.html.HTMLFontElement;
import dk.brics.tajs.analysis.dom.html.HTMLFormElement;
import dk.brics.tajs.analysis.dom.html.HTMLFrameElement;
import dk.brics.tajs.analysis.dom.html.HTMLFrameSetElement;
import dk.brics.tajs.analysis.dom.html.HTMLHRElement;
import dk.brics.tajs.analysis.dom.html.HTMLHeadElement;
import dk.brics.tajs.analysis.dom.html.HTMLHeadingElement;
import dk.brics.tajs.analysis.dom.html.HTMLHtmlElement;
import dk.brics.tajs.analysis.dom.html.HTMLIFrameElement;
import dk.brics.tajs.analysis.dom.html.HTMLImageElement;
import dk.brics.tajs.analysis.dom.html.HTMLInputElement;
import dk.brics.tajs.analysis.dom.html.HTMLIsIndexElement;
import dk.brics.tajs.analysis.dom.html.HTMLLIElement;
import dk.brics.tajs.analysis.dom.html.HTMLLabelElement;
import dk.brics.tajs.analysis.dom.html.HTMLLegendElement;
import dk.brics.tajs.analysis.dom.html.HTMLLinkElement;
import dk.brics.tajs.analysis.dom.html.HTMLMapElement;
import dk.brics.tajs.analysis.dom.html.HTMLMenuElement;
import dk.brics.tajs.analysis.dom.html.HTMLMetaElement;
import dk.brics.tajs.analysis.dom.html.HTMLModElement;
import dk.brics.tajs.analysis.dom.html.HTMLOListElement;
import dk.brics.tajs.analysis.dom.html.HTMLObjectElement;
import dk.brics.tajs.analysis.dom.html.HTMLOptGroupElement;
import dk.brics.tajs.analysis.dom.html.HTMLOptionElement;
import dk.brics.tajs.analysis.dom.html.HTMLOptionsCollection;
import dk.brics.tajs.analysis.dom.html.HTMLParagraphElement;
import dk.brics.tajs.analysis.dom.html.HTMLParamElement;
import dk.brics.tajs.analysis.dom.html.HTMLPreElement;
import dk.brics.tajs.analysis.dom.html.HTMLQuoteElement;
import dk.brics.tajs.analysis.dom.html.HTMLScriptElement;
import dk.brics.tajs.analysis.dom.html.HTMLSelectElement;
import dk.brics.tajs.analysis.dom.html.HTMLStyleElement;
import dk.brics.tajs.analysis.dom.html.HTMLTableCaptionElement;
import dk.brics.tajs.analysis.dom.html.HTMLTableCellElement;
import dk.brics.tajs.analysis.dom.html.HTMLTableColElement;
import dk.brics.tajs.analysis.dom.html.HTMLTableElement;
import dk.brics.tajs.analysis.dom.html.HTMLTableRowElement;
import dk.brics.tajs.analysis.dom.html.HTMLTableSectionElement;
import dk.brics.tajs.analysis.dom.html.HTMLTextAreaElement;
import dk.brics.tajs.analysis.dom.html.HTMLTitleElement;
import dk.brics.tajs.analysis.dom.html.HTMLUListElement;
import dk.brics.tajs.analysis.dom.html5.CanvasRenderingContext2D;
import dk.brics.tajs.analysis.dom.html5.HTMLCanvasElement;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.NativeObject;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;

/**
 * Dispatcher and utility functions for the DOM support
 */
public class DOMFunctions {

	/*
	 * The following properties are magic:
	 * 
	 * DOM Level 0: Window.location
	 * 
	 * DOM Level 2 Core:
	 * Attr.value -> raises DOMEx on setting
	 * CharacterData.data -> raises DOMEx on setting and retrieval
	 * Node.nodeValue -> raises DOMEx on setting and retrieval
	 * Node.prefix -> raises DOMEx on setting
	 * ProcessingInstruction.data -> raises DOMEx on setting
	 * 
	 * DOm Level 3:
	 * Document.xmlStandalone -> raises DOMEx on setting
	 * Document.xmlVersion -> raises DOMEx on setting
	 * 
	 * DOM HTML:
	 * HTMLDocument.cookie -> raises DOMEx on setting
	 * Element.id, Element.name, Element.className, Element.innerHTML,
	 * Element.onX -> raises DOMEx on setting
	 * SelectElement.length -> raises DOMEx on setting
	 * TableElement.caption -> raises DOMEx on setting
	 * TableElement.tHead -> raises DOMEx on setting
	 * TableElement.tFoot -> raises DOMEx on setting
	 * HTMLOptionsCollection.length -> raises DOMEx on setting
	 */

	/**
	 * Read Magic Property
	 */
	public static void evaluateGetter(NativeObject nativeObject, ObjectLabel label, String property, Solver.SolverInterface c) {
		throw new RuntimeException("Not Implemented");
	}

	/**
	 * Write Magic Property
	 */
	public static void evaluateSetter(NativeObject nativeObject, ObjectLabel label, String property, Value v, Solver.SolverInterface c) {
		// State
		State s = c.getCurrentState();

		// The window.onload / window.onunload properties
		if (nativeObject == DOMWindow.WINDOW.getNativeObjectID()) {
			// Load Event Handlers
			if (DOMEvents.isLoadEventAttribute(property)) {
				if (Options.isDebugEnabled()) {
					System.out.println("Adding Load Event Handler: " + v);
				}
				s.addLoadEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
			}

			// Unload Event Handlers
			if (DOMEvents.isUnloadEventAttribute(property)) {
				if (Options.isDebugEnabled()) {
					System.out.println("Adding Unload Event Handler: " + v);
				}
				s.addUnloadEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
			}
		}

		// The image.onload properties
		if (nativeObject == HTMLImageElement.IMAGE.getNativeObjectID()) {
			// Load Event Handler

			// TODO: Hack: We currently treat image onload event handlers similarly to timeout event handlers
			// might make a difference in practice.
			s.addTimeoutEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
		}

		// Keyboard Event Handlers
		if (DOMEvents.isKeyboardEventAttribute(property)) {
			if (Options.isDebugEnabled()) {
				System.out.println("Adding Keyboard Event Handler: " + v);
			}
			DOMEvents.addKeyboardEventHandler(s, v, c);
		}

		// Mouse Event Handlers
		if (DOMEvents.isMouseEventAttribute(property)) {
			if (Options.isDebugEnabled()) {
				System.out.println("Adding Mouse Event Handler: " + v);
			}
			DOMEvents.addMouseEventHandler(s, v, c);
		}

		// Unknown Event Handlers
		if (DOMEvents.isOtherEventAttribute(property)) {
			if (Options.isDebugEnabled()) {
				System.out.println("Adding Unknown Event Handler: " + v);
			}
			DOMEvents.addUnknownEventHandler(s, v, c);
		}

		// id attribute
		if ("id".equalsIgnoreCase(property)) {
			s.addElementById(property, Collections.newSingleton(label));
		}

		// name attribute
		if ("name".equalsIgnoreCase(property)) {
			s.addElementByName(property, Collections.newSingleton(label));
		}
	}

	/**
	 * Create a new DOM internam prototype with the given name and value on the specified
	 * objectlabel.
	 */
	public static void createDOMInternalPrototype(State s, ObjectLabel objlabel, Value value) {
		createInternalPrototype(s, objlabel, value);
	}

	/**
	 * Create a new DOM unknown array with the given name and value on the specified
	 * objectlabel.
	 */
	public static void createDOMUnknownArrayProperty(State s, ObjectLabel objlabel, Value value) {
		createUnknownArrayProperty(s, objlabel, value);
	}

	/**
	 * Create a new DOM property with the given name and value on the specified
	 * objectlabel.
	 */
	public static void createDOMProperty(State s, ObjectLabel objlabel, String property, Value v) {
		createProperty(s, objlabel, property, v);
	}

	/**
	 * Create a new DOM property with the given name and value on the specified
	 * objectlabel.
	 */
	public static void createDOMProperty(State s, ObjectLabel objlabel, String property, Value v, DOMSpec spec) {
		s.getSolverInterface().getStatistics().registerDOMProperty(spec);
		createProperty(s, objlabel, property, v);
	}

	/**
	 * Create a new DOM function with the given name and number of arguments on
	 * the specified objectlabel.
	 */
	public static void createDOMFunction(State s, ObjectLabel label, NativeObject nativeObject, String name, int args, DOMSpec spec) {
		s.getSolverInterface().getStatistics().registerDOMFunction(spec);
		createPrimitiveFunction(s, label, FUNCTION_PROTOTYPE, nativeObject, name, args);
	}

	/**
	 * Returns a Value representing all possible HTML elements.
	 */
	public static Value makeAnyHTMLElement() {
		return Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS, new Dependency(), new DependencyGraphReference());
	}

	/**
	 * Returns a Value representing a NodeList containg all possible HTML
	 * elements.
	 */
	public static Value makeAnyHTMLNodeList(State s) {
		ObjectLabel nodeList = makeEmptyNodeList(s);
		s.writeUnknownArrayProperty(nodeList, makeAnyHTMLElement());
		return Value.makeObject(nodeList, new Dependency(), new DependencyGraphReference());
	}

	/**
	 * Returns a new empty NodeList object.
	 */
	public static ObjectLabel makeEmptyNodeList(State s) {
		ObjectLabel nodeList = new ObjectLabel(DOMObjects.NODELIST, ObjectLabel.Kind.OBJECT);
		s.newObject(nodeList);
		s.writeInternalPrototype(nodeList, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		return nodeList;
	}

	/**
	 * Returns the object label belonging to the given tagname.
	 */
	public static ObjectLabel getHTMLObjectLabel(String tagname) {
		if ("a".equalsIgnoreCase(tagname)) {
			return HTMLAnchorElement.ANCHOR;
		} else if ("applet".equalsIgnoreCase(tagname)) {
			return HTMLAppletElement.APPLET;
		} else if ("area".equalsIgnoreCase(tagname)) {
			return HTMLAreaElement.AREA;
		} else if ("base".equalsIgnoreCase(tagname)) {
			return HTMLBaseElement.BASE;
		} else if ("basefont".equalsIgnoreCase(tagname)) {
			return HTMLBaseFontElement.BASEFONT;
		} else if ("body".equalsIgnoreCase(tagname)) {
			return HTMLBodyElement.BODY;
		} else if ("br".equalsIgnoreCase(tagname)) {
			return HTMLBRElement.BR;
		} else if ("button".equalsIgnoreCase(tagname)) {
			return HTMLButtonElement.BUTTON;
		} else if ("dir".equalsIgnoreCase(tagname)) {
			return HTMLDirectoryElement.DIRECTORY;
		} else if ("div".equalsIgnoreCase(tagname)) {
			return HTMLDivElement.DIV;
		} else if ("dl".equalsIgnoreCase(tagname)) {
			return HTMLDListElement.DLIST;
		} else if ("fieldset".equalsIgnoreCase(tagname)) {
			return HTMLFieldSetElement.FIELDSET;
		} else if ("font".equalsIgnoreCase(tagname)) {
			return HTMLFontElement.FONT;
		} else if ("form".equalsIgnoreCase(tagname)) {
			return HTMLFormElement.FORM;
		} else if ("frame".equalsIgnoreCase(tagname)) {
			return HTMLFrameElement.FRAME;
		} else if ("frameset".equalsIgnoreCase(tagname)) {
			return HTMLFrameSetElement.FRAMESET;
		} else if ("h1".equalsIgnoreCase(tagname)
				|| "h2".equalsIgnoreCase(tagname)
				|| "h3".equalsIgnoreCase(tagname)
				|| "h4".equalsIgnoreCase(tagname)
				|| "h5".equalsIgnoreCase(tagname)
				|| "h6".equalsIgnoreCase(tagname)) {
			return HTMLHeadingElement.HEADING;
		} else if ("head".equalsIgnoreCase(tagname)) {
			return HTMLHeadElement.HEAD_PROTOTPE;
		} else if ("hr".equalsIgnoreCase(tagname)) {
			return HTMLHRElement.HR;
		} else if ("html".equalsIgnoreCase(tagname)) {
			return HTMLHtmlElement.HTML;
		} else if ("iframe".equalsIgnoreCase(tagname)) {
			return HTMLIFrameElement.IFRAME;
		} else if ("image".equalsIgnoreCase(tagname)) {
			return HTMLImageElement.IMAGE;
		} else if ("input".equalsIgnoreCase(tagname)) {
			return HTMLInputElement.INPUT;
		} else if ("isindex".equalsIgnoreCase(tagname)) {
			return HTMLIsIndexElement.ISINDEX;
		} else if ("label".equalsIgnoreCase(tagname)) {
			return HTMLLabelElement.LABEL;
		} else if ("legend".equalsIgnoreCase(tagname)) {
			return HTMLLegendElement.LEGEND;
		} else if ("li".equalsIgnoreCase(tagname)) {
			return HTMLLIElement.LI;
		} else if ("link".equalsIgnoreCase(tagname)) {
			return HTMLLinkElement.LINK;
		} else if ("map".equalsIgnoreCase(tagname)) {
			return HTMLMapElement.MAP;
		} else if ("menu".equalsIgnoreCase(tagname)) {
			return HTMLMenuElement.MENU;
		} else if ("meta".equalsIgnoreCase(tagname)) {
			return HTMLMetaElement.META;
		} else if ("ins".equalsIgnoreCase(tagname) || "del".equalsIgnoreCase(tagname)) {
			return HTMLModElement.MOD;
		} else if ("object".equalsIgnoreCase(tagname)) {
			return HTMLObjectElement.OBJECT;
		} else if ("ol".equalsIgnoreCase(tagname)) {
			return HTMLOListElement.OLIST;
		} else if ("optgroup".equalsIgnoreCase(tagname)) {
			return HTMLOptGroupElement.OPTGROUP;
		} else if ("option".equalsIgnoreCase(tagname)) {
			return HTMLOptionElement.OPTION;
		} else if ("p".equalsIgnoreCase(tagname)) {
			return HTMLParagraphElement.PARAGRAPH;
		} else if ("param".equalsIgnoreCase(tagname)) {
			return HTMLParamElement.PARAM;
		} else if ("pre".equalsIgnoreCase(tagname)) {
			return HTMLPreElement.PRE;
		} else if ("q".equalsIgnoreCase(tagname) || "blockquote".equalsIgnoreCase(tagname)) {
			return HTMLQuoteElement.QUOTE;
		} else if ("script".equalsIgnoreCase(tagname)) {
			return HTMLScriptElement.SCRIPT;
		} else if ("select".equalsIgnoreCase(tagname)) {
			return HTMLSelectElement.SELECT;
		} else if ("style".equalsIgnoreCase(tagname)) {
			return HTMLStyleElement.STYLE;
		} else if ("caption".equalsIgnoreCase(tagname)) {
			return HTMLTableCaptionElement.TABLECAPTION;
		} else if ("th".equalsIgnoreCase(tagname) || "td".equalsIgnoreCase(tagname)) {
			return HTMLTableCellElement.TABLECELL;
		} else if ("col".equalsIgnoreCase(tagname) || "colgroup".equalsIgnoreCase(tagname)) {
			return HTMLTableColElement.TABLECOL;
		} else if ("table".equalsIgnoreCase(tagname)) {
			return HTMLTableElement.TABLE;
		} else if ("tr".equalsIgnoreCase(tagname)) {
			return HTMLTableRowElement.TABLEROW;
		} else if ("thead".equalsIgnoreCase(tagname)
				|| "tfoot".equalsIgnoreCase(tagname)
				|| "tbody".equalsIgnoreCase(tagname)) {
			return HTMLTableSectionElement.TABLESECTION;
		} else if ("textarea".equalsIgnoreCase(tagname)) {
			return HTMLTextAreaElement.TEXTAREA;
		} else if ("title".equalsIgnoreCase(tagname)) {
			return HTMLTitleElement.TITLE;
		} else if ("ul".equalsIgnoreCase(tagname)) {
			return HTMLUListElement.ULIST;
		}
		// HTML 5
		else if ("canvas".equalsIgnoreCase(tagname)) {
			return HTMLCanvasElement.CANVAS;
		}
		if (Options.isDebugEnabled()) {
			System.out.println("Warning: unknown tagname: "
					+ tagname
					+ ". Using default HTMLElement.");
		}
		return HTMLElement.ELEMENT;
	}

	/**
	 * Evaluate the native function
	 */
	public static Value evaluate(DOMObjects nativeObject, CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case WINDOW_ALERT:
		case WINDOW_ATOB:
		case WINDOW_BACK:
		case WINDOW_BLUR:
		case WINDOW_BTOA:
		case WINDOW_CLOSE:
		case WINDOW_CLEAR_INTERVAL:
		case WINDOW_CLEAR_TIMEOUT:
		case WINDOW_CONFIRM:
		case WINDOW_ESCAPE:
		case WINDOW_FOCUS:
		case WINDOW_FORWARD:
		case WINDOW_HISTORY_BACK:
		case WINDOW_HISTORY_FORWARD:
		case WINDOW_HISTORY_GO:
		case WINDOW_HOME:
		case WINDOW_LOCATION_ASSIGN:
		case WINDOW_LOCATION_RELOAD:
		case WINDOW_LOCATION_REPLACE:
		case WINDOW_LOCATION_TOSTRING:
		case WINDOW_MAXIMIZE:
		case WINDOW_MINIMIZE:
		case WINDOW_MOVEBY:
		case WINDOW_MOVETO:
		case WINDOW_OPEN:
		case WINDOW_PRINT:
		case WINDOW_PROMPT:
		case WINDOW_RESIZEBY:
		case WINDOW_RESIZETO:
		case WINDOW_SCROLL:
		case WINDOW_SCROLLBY:
		case WINDOW_SCROLLBYLINES:
		case WINDOW_SCROLLBYPAGES:
		case WINDOW_SCROLLTO:
		case WINDOW_SET_INTERVAL:
		case WINDOW_SET_TIMEOUT:
		case WINDOW_STOP:
		case WINDOW_UNESCAPE:
			return DOMWindow.evaluate(nativeObject, call, s, c);
		case DOCUMENT_ADOPT_NODE:
		case DOCUMENT_CREATE_ATTRIBUTE:
		case DOCUMENT_CREATE_ATTRIBUTE_NS:
		case DOCUMENT_CREATE_CDATASECTION:
		case DOCUMENT_CREATE_COMMENT:
		case DOCUMENT_CREATE_DOCUMENTFRAGMENT:
		case DOCUMENT_CREATE_ELEMENT:
		case DOCUMENT_CREATE_ELEMENT_NS:
		case DOCUMENT_CREATE_ENTITYREFERENCE:
		case DOCUMENT_CREATE_TEXTNODE:
		case DOCUMENT_CREATEPROCESSINGINSTRUCTION:
		case DOCUMENT_GET_ELEMENT_BY_ID:
		case DOCUMENT_GET_ELEMENTS_BY_TAGNAME:
		case DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS:
		case DOCUMENT_IMPORT_NODE:
		case DOCUMENT_NORMALIZEDOCUMENT:
		case DOCUMENT_RENAME_NODE:
			return DOMDocument.evaluate(nativeObject, call, s, c);
		case DOMIMPLEMENTATION_HASFEATURE:
		case DOMIMPLEMENTATION_CREATEDOCUMENTTYPE:
		case DOMIMPLEMENTATION_CREATEDOCUMENT:
			return DOMImplementation.evaluate(nativeObject, call, s, c);
		case NODELIST_ITEM:
			return DOMNodeList.evaluate(nativeObject, call, s, c);
		case ELEMENT_GET_ATTRIBUTE:
		case ELEMENT_GET_ATTRIBUTE_NS:
		case ELEMENT_GET_ATTRIBUTE_NODE:
		case ELEMENT_GET_ATTRIBUTE_NODE_NS:
		case ELEMENT_GET_ELEMENTS_BY_TAGNAME:
		case ELEMENT_GET_ELEMENTS_BY_TAGNAME_NS:
		case ELEMENT_HAS_ATTRIBUTE:
		case ELEMENT_HAS_ATTRIBUTE_NS:
		case ELEMENT_REMOVE_ATTRIBUTE:
		case ELEMENT_REMOVE_ATTRIBUTE_NS:
		case ELEMENT_REMOVE_ATTRIBUTE_NODE:
		case ELEMENT_SET_ATTRIBUTE:
		case ELEMENT_SET_ATTRIBUTE_NS:
		case ELEMENT_SET_ATTRIBUTE_NODE:
		case ELEMENT_SET_ATTRIBUTE_NODE_NS:
		case ELEMENT_SET_ID_ATTRIBUTE:
		case ELEMENT_SET_ID_ATTRIBUTE_NS:
		case ELEMENT_SET_ID_ATTRIBUTE_NODE:
			return DOMElement.evaluate(nativeObject, call, s, c);
		case CHARACTERDATA_SUBSTRINGDATA:
		case CHARACTERDATA_APPENDDATA:
		case CHARACTERDATA_INSERTDATA:
		case CHARACTERDATA_DELETEDATA:
		case CHARACTERDATA_REPLACEDATA:
			return DOMCharacterData.evaluate(nativeObject, call, s, c);
		case NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEM:
		case NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEM:
		case NAMEDNODEMAP_PROTOTYPE_REMOVENAMEDITEM:
		case NAMEDNODEMAP_PROTOTYPE_ITEM:
		case NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEMNS:
		case NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEMNS:
		case NAMEDNODEMAP_PROTOTYPE_REMOVEDNAMEDITEMNS:
			return DOMNamedNodeMap.evaluate(nativeObject, call, s, c);
		case NODE_APPEND_CHILD:
		case NODE_CLONE_NODE:
		case NODE_HAS_CHILD_NODES:
		case NODE_INSERT_BEFORE:
		case NODE_REMOVE_CHILD:
		case NODE_REPLACE_CHILD:
		case NODE_IS_SUPPORTED:
		case NODE_HAS_ATTRIBUTES:
		case NODE_NORMALIZE:
			return DOMNode.evaluate(nativeObject, call, s, c);
		case TEXT_SPLIT_TEXT:
		case TEXT_REPLACE_WHOLE_TEXT:
			return DOMText.evaluate(nativeObject, call, s, c);
			// HTML
		case HTMLANCHORELEMENT_BLUR:
		case HTMLANCHORELEMENT_FOCUS:
			return HTMLAnchorElement.evaluate(nativeObject, call, s, c);
		case HTMLCOLLECTION_ITEM:
		case HTMLCOLLECTION_NAMEDITEM:
			return HTMLCollection.evaluate(nativeObject, call, s, c);
		case HTMLDOCUMENT_OPEN:
		case HTMLDOCUMENT_CLOSE:
		case HTMLDOCUMENT_WRITE:
		case HTMLDOCUMENT_WRITELN:
		case HTMLDOCUMENT_GET_ELEMENTS_BY_NAME:
		case HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME:
			return HTMLDocument.evaluate(nativeObject, call, s, c);
		case HTMLELEMENT_GET_ELEMENTS_BY_CLASS_NAME:
			return HTMLElement.evaluate(nativeObject, call, s, c);
		case HTMLIMAGEELEMENT_CONSTRUCTOR:
			return HTMLImageElement.evaluate(nativeObject, call, s, c);
		case HTMLOPTIONSCOLLECTION_ITEM:
		case HTMLOPTIONSCOLLECTION_NAMEDITEM:
			return HTMLOptionsCollection.evaluate(nativeObject, call, s, c);
		case HTMLFORMELEMENT_SUBMIT:
		case HTMLFORMELEMENT_RESET:
			return HTMLFormElement.evaluate(nativeObject, call, s, c);
		case HTMLINPUTELEMENT_BLUR:
		case HTMLINPUTELEMENT_CLICK:
		case HTMLINPUTELEMENT_FOCUS:
		case HTMLINPUTELEMENT_SELECT:
			return HTMLInputElement.evaluate(nativeObject, call, s, c);
		case HTMLSELECTELEMENT_ADD:
		case HTMLSELECTELEMENT_BLUR:
		case HTMLSELECTELEMENT_FOCUS:
		case HTMLSELECTELEMENT_REMOVE:
			return HTMLSelectElement.evaluate(nativeObject, call, s, c);
		case HTMLTABLESECTIONELEMENT_DELETEROW:
		case HTMLTABLESECTIONELEMENT_INSERTROW:
			return HTMLTableSectionElement.evaluate(nativeObject, call, s, c);
		case HTMLTABLEELEMENT_CREATECAPTION:
		case HTMLTABLEELEMENT_CREATETFOOT:
		case HTMLTABLEELEMENT_CREATETHEAD:
		case HTMLTABLEELEMENT_DELETECAPTION:
		case HTMLTABLEELEMENT_DELETETFOOT:
		case HTMLTABLEELEMENT_DELETETHEAD:
		case HTMLTABLEELEMENT_INSERTROW:
		case HTMLTABLEELEMENT_DELETEROW:
			return HTMLTableElement.evaluate(nativeObject, call, s, c);
		case HTMLTABLEROWELEMENT_INSERTCELL:
		case HTMLTABLEROWELEMENT_DELETECELL:
			return HTMLTableRowElement.evaluate(nativeObject, call, s, c);
		case HTMLCANVASELEMENT_GET_CONTEXT:
		case HTMLCANVASELEMENT_TO_DATA_URL:
			return HTMLCanvasElement.evaluate(nativeObject, call, s, c);
		case CANVASRENDERINGCONTEXT2D_BEGIN_PATH:
		case CANVASRENDERINGCONTEXT2D_CLOSE_PATH:
		case CANVASRENDERINGCONTEXT2D_MOVE_TO:
		case CANVASRENDERINGCONTEXT2D_LINE_TO:
		case CANVASRENDERINGCONTEXT2D_ARC:
		case CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO:
		case CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO:
		case CANVASRENDERINGCONTEXT2D_FILL:
		case CANVASRENDERINGCONTEXT2D_STROKE:
		case CANVASRENDERINGCONTEXT2D_CLEAR_RECT:
		case CANVASRENDERINGCONTEXT2D_FILL_RECT:
		case CANVASRENDERINGCONTEXT2D_STROKE_RECT:
		case CANVASRENDERINGCONTEXT2D_SAVE:
		case CANVASRENDERINGCONTEXT2D_RESTORE:
		case CANVASRENDERINGCONTEXT2D_SCALE:
		case CANVASRENDERINGCONTEXT2D_ROTATE:
		case CANVASRENDERINGCONTEXT2D_TRANSLATE:
		case CANVASRENDERINGCONTEXT2D_TRANSFORM:
		case CANVASRENDERINGCONTEXT2D_SETTRANSFORM:
		case CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT:
		case CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT:
		case CANVASRENDERINGCONTEXT2D_CREATE_PATTERN:
		case CANVASRENDERINGCONTEXT2D_ARC_TO:
		case CANVASRENDERINGCONTEXT2D_RECT:
		case CANVASRENDERINGCONTEXT2D_CLIP:
		case CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH:
		case CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING:
		case CANVASRENDERINGCONTEXT2D_FILL_TEXT:
		case CANVASRENDERINGCONTEXT2D_STROKE_TEXT:
		case CANVASRENDERINGCONTEXT2D_MEASURE_TEXT:
		case CANVASRENDERINGCONTEXT2D_DRAW_IMAGE:
		case CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA:
		case CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA:
		case CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA:
		case CANVASGRADIENT_ADD_COLOR_STOP:
			return CanvasRenderingContext2D.evaluate(nativeObject, call, s, c);
		case STRINGLIST_CONTAINS:
		case STRINGLIST_ITEM:
			return DOMStringList.evaluate(nativeObject, call, s, c);
		case CONFIGURATION_CAN_SET_PARAMETER:
		case CONFIGURATION_SET_PARAMETER:
		case CONFIGURATION_GET_PARAMETER:
			return DOMConfiguration.evaluate(nativeObject, call, s, c);
		case EVENT_INIT_EVENT:
		case EVENT_PREVENT_DEFAULT:
		case EVENT_STOP_PROPAGATION:
			return Event.evaluate(nativeObject, call, s, c);
		case EVENT_TARGET_ADD_EVENT_LISTENER:
		case WINDOW_ADD_EVENT_LISTENER:
		case EVENT_TARGET_REMOVE_EVENT_LISTENER:
		case WINDOW_REMOVE_EVENT_LISTENER:
		case EVENT_TARGET_DISPATCH_EVENT:
			return EventTarget.evaluate(nativeObject, call, s, c);
		case EVENT_LISTENER_HANDLE_EVENT:
			return EventListener.evaluate(nativeObject, call, s, c);
		case DOCUMENT_EVENT_CREATE_EVENT:
			return DocumentEvent.evaluate(nativeObject, call, s, c);
		case MUTATION_EVENT_INIT_MUTATION_EVENT:
			return MutationEvent.evaluate(nativeObject, call, s, c);
		case UI_EVENT_INIT_UI_EVENT:
			return UIEvent.evaluate(nativeObject, call, s, c);
		case MOUSE_EVENT_INIT_MOUSE_EVENT:
			return MouseEvent.evaluate(nativeObject, call, s, c);
		case KEYBOARD_EVENT_GET_MODIFIER_STATE:
		case KEYBOARD_EVENT_INIT_KEYBOARD_EVENT:
		case KEYBOARD_EVENT_INIT_KEYBOARD_EVENT_NS:
			return KeyboardEvent.evaluate(nativeObject, call, s, c);
		case WHEEL_EVENT_INIT_WHEEL_EVENT:
		case WHEEL_EVENT_INIT_WHEEL_EVENT_NS:
			return WheelEvent.evaluate(nativeObject, call, s, c);
		default: {
			throw new RuntimeException("Native DOM function " + nativeObject + " not supported");
		}
		}
	}

}
