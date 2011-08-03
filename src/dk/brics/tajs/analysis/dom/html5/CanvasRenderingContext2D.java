package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

public class CanvasRenderingContext2D {

	public static ObjectLabel CONTEXT2D;
	public static ObjectLabel CONTEXT2D_PROTOTYPE;

	public static ObjectLabel GRADIENT;
	public static ObjectLabel PATTERN;
	public static ObjectLabel PIXEL_ARRAY;
	public static ObjectLabel IMAGE_DATA;
	public static ObjectLabel TEXT_METRICS;

	public static void build(State s) {
		CONTEXT2D = new ObjectLabel(DOMObjects.CANVASRENDERINGCONTEXT2D, ObjectLabel.Kind.OBJECT);
		CONTEXT2D_PROTOTYPE = new ObjectLabel(DOMObjects.CANVASRENDERINGCONTEXT2D_PROTOTYPE, ObjectLabel.Kind.OBJECT);

		GRADIENT = new ObjectLabel(DOMObjects.CANVASGRADIENT, ObjectLabel.Kind.OBJECT);
		PATTERN = new ObjectLabel(DOMObjects.CANVASPATTERN, ObjectLabel.Kind.OBJECT);
		PIXEL_ARRAY = new ObjectLabel(DOMObjects.CANVASPIXELARRAY, ObjectLabel.Kind.OBJECT);
		IMAGE_DATA = new ObjectLabel(DOMObjects.IMAGEDATA, ObjectLabel.Kind.OBJECT);
		TEXT_METRICS = new ObjectLabel(DOMObjects.TEXTMETRICS, ObjectLabel.Kind.OBJECT);

		// Prototype Object
		s.newObject(CONTEXT2D_PROTOTYPE);
		createDOMInternalPrototype(s, CONTEXT2D_PROTOTYPE,
				Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "CanvasRenderingContext2D", Value.makeObject(CONTEXT2D, new Dependency(), new DependencyGraphReference()));

		// Canvas Context Object
		s.newObject(CONTEXT2D);
		createDOMInternalPrototype(s, CONTEXT2D, Value.makeObject(CONTEXT2D_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, CONTEXT2D, "prototype", Value.makeObject(CONTEXT2D_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Misc Objects
		s.newObject(PATTERN);
		s.newObject(PIXEL_ARRAY);
		s.newObject(IMAGE_DATA);
		s.newObject(TEXT_METRICS);
		s.newObject(GRADIENT);
		createDOMInternalPrototype(s, PATTERN, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMInternalPrototype(s, PIXEL_ARRAY, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMInternalPrototype(s, IMAGE_DATA, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMInternalPrototype(s, TEXT_METRICS, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMInternalPrototype(s, GRADIENT, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/**
		 * Properties.
		 */

		// compositing
		createDOMProperty(s, CONTEXT2D_PROTOTYPE, "globalAlpha", Value.makeNum(1.0, new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D_PROTOTYPE, "globalCompositeOperation", Value.makeStr("source-over", new Dependency(), new DependencyGraphReference()),
				DOMSpec.HTML5);

		// colours and styles
		createDOMProperty(s, CONTEXT2D, "strokeStyle",
				Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).join(Value.makeAnyNum(new Dependency(), new DependencyGraphReference()))
						.join(Value.makeAnyStr(new Dependency(), new DependencyGraphReference())), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "fillStyle",
				Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).join(Value.makeAnyNum(new Dependency(), new DependencyGraphReference()))
						.join(Value.makeAnyStr(new Dependency(), new DependencyGraphReference())), DOMSpec.HTML5);

		// line caps/joins
		createDOMProperty(s, CONTEXT2D, "lineWidth", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "lineCap", Value.makeStr("butt", new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "lineJoin", Value.makeStr("miter", new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "miterLimit", Value.makeNum(10, new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);

		// shadows
		createDOMProperty(s, CONTEXT2D, "shadowOffsetX", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "shadowOffsetY", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "shadowBlur", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "shadowColor", Value.makeStr("transparent black", new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);

		// text
		createDOMProperty(s, CONTEXT2D, "font", Value.makeStr("10px sans-serif", new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "textAlign", Value.makeStr("start", new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CONTEXT2D, "textBaseline", Value.makeStr("alphabetic", new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);

		// ImageData
		createDOMProperty(s, IMAGE_DATA, "data", Value.makeObject(PIXEL_ARRAY, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.HTML5);
		createDOMProperty(s, IMAGE_DATA, "height", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.HTML5);
		createDOMProperty(s, IMAGE_DATA, "width", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.HTML5);

		// CanvasPixelArray
		createDOMProperty(s, PIXEL_ARRAY, "length", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.HTML5);

		/**
		 * Canvas Functions.
		 */

		// State
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SAVE, "save", 0, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_RESTORE, "restore", 0, DOMSpec.HTML5);

		// Transformations
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SCALE, "scale", 2, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ROTATE, "rotate", 1, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_TRANSLATE, "translate", 2, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_TRANSFORM, "transform", 6, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SETTRANSFORM, "setTransform", 6, DOMSpec.HTML5);

		// Colors & Style
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT, "createLinearGradient", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT, "createRadialGradient", 6, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_PATTERN, "createPattern", 2, DOMSpec.HTML5);

		// Rects
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLEAR_RECT, "clearRect", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL_RECT, "fillRect", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE_RECT, "strokeRect", 4, DOMSpec.HTML5);

		// Paths
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_BEGIN_PATH, "beginPath", 0, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLOSE_PATH, "closePath", 0, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_MOVE_TO, "moveTo", 2, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_LINE_TO, "lineTo", 2, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO, "quadraticCurveTo", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO, "bezierCurveTo", 6, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ARC_TO, "arcTo", 5, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_RECT, "rect", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ARC, "arc", 5, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL, "fill", 0, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE, "stroke", 0, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLIP, "clip", 0, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH, "isPointInPath", 2, DOMSpec.HTML5);

		// Focus Management
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING, "drawFocusRing", 4, DOMSpec.HTML5);

		// Text
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL_TEXT, "fillText", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE_TEXT, "strokeText", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_MEASURE_TEXT, "measureText", 1, DOMSpec.HTML5);

		// Drawing Images
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_DRAW_IMAGE, "drawImage", 9, DOMSpec.HTML5);

		// Pixel Manipulation
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA, "createImageData", 2, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA, "getImageData", 4, DOMSpec.HTML5);
		createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA, "putImageData", 7, DOMSpec.HTML5);

		/*
		 * Gradient Functions
		 */
		createDOMFunction(s, GRADIENT, DOMObjects.CANVASGRADIENT_ADD_COLOR_STOP, "addColorStop", 2, DOMSpec.HTML5);

		/*
		 * Multiply objects
		 */
		// Canvas
		s.multiplyObject(CONTEXT2D);
		CONTEXT2D = CONTEXT2D.makeSingleton().makeSummary();

		// Image Data
		s.multiplyObject(IMAGE_DATA);
		IMAGE_DATA = IMAGE_DATA.makeSingleton().makeSummary();

		// Pattern
		s.multiplyObject(PATTERN);
		PATTERN = PATTERN.makeSingleton().makeSummary();

		// Gradient
		s.multiplyObject(GRADIENT);
		GRADIENT = GRADIENT.makeSingleton().makeSummary();

		// Pixel Array
		s.multiplyObject(PIXEL_ARRAY);
		PIXEL_ARRAY = PIXEL_ARRAY.makeSingleton().makeSummary();

		// Text Metrics
		s.multiplyObject(TEXT_METRICS);
		TEXT_METRICS = TEXT_METRICS.makeSingleton().makeSummary();
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		// State
		case CANVASRENDERINGCONTEXT2D_SAVE:
		case CANVASRENDERINGCONTEXT2D_RESTORE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

			// Transformations
		case CANVASRENDERINGCONTEXT2D_SCALE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_ROTATE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value angle = */
			Conversion.toNumber(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_TRANSLATE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_TRANSFORM:
		case CANVASRENDERINGCONTEXT2D_SETTRANSFORM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
			/* Value m11 = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value m12 = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value m21 = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value m22 = */
			Conversion.toNumber(call.getArg(3), c);
			/* Value dx = */
			Conversion.toNumber(call.getArg(4), c);
			/* Value dy = */
			Conversion.toNumber(call.getArg(5), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

			// Colors & Styles
		case CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
			/* Value x0 = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y0 = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value x1 = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value y1 = */
			Conversion.toNumber(call.getArg(3), c);
			return Value.makeObject(GRADIENT, new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
			/* Value x0 = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y0 = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value r0 = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value x1 = */
			Conversion.toNumber(call.getArg(3), c);
			/* Value y1 = */
			Conversion.toNumber(call.getArg(4), c);
			/* Value r1 = */
			Conversion.toNumber(call.getArg(5), c);
			return Value.makeObject(GRADIENT, new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_CREATE_PATTERN: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			return Value.makeObject(PATTERN, new Dependency(), new DependencyGraphReference());
		}

			// Rects
		case CANVASRENDERINGCONTEXT2D_CLEAR_RECT:
		case CANVASRENDERINGCONTEXT2D_FILL_RECT:
		case CANVASRENDERINGCONTEXT2D_STROKE_RECT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value width = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value height = */
			Conversion.toNumber(call.getArg(3), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

			// Paths
		case CANVASRENDERINGCONTEXT2D_BEGIN_PATH: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_CLOSE_PATH: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_MOVE_TO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_LINE_TO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
			/* Value cpx = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value cpy = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value x = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(3), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
			/* Value cp1x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value cp1y = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value cp2x = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value cp2y = */
			Conversion.toNumber(call.getArg(3), c);
			/* Value x = */
			Conversion.toNumber(call.getArg(4), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(5), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_RECT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value w = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value h = */
			Conversion.toNumber(call.getArg(3), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_ARC: {
			NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value radius = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value startAngle = */
			Conversion.toNumber(call.getArg(3), c);
			/* Value endAngle = */
			Conversion.toNumber(call.getArg(4), c);
			/* Value anticlockwise = */
			Conversion.toBoolean(call.getArg(5));
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_ARC_TO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 5, 5);
			/* Value x1 = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y1 = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value x2 = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value y2 = */
			Conversion.toNumber(call.getArg(3), c);
			/* Value radius = */
			Conversion.toNumber(call.getArg(4), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_CLIP: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value x = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(1), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_FILL:
		case CANVASRENDERINGCONTEXT2D_STROKE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_DRAW_IMAGE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 3, -1);

			boolean good = false;
			boolean bad = call.getArg(0).isMaybePrimitive();
			for (ObjectLabel l : call.getArg(0).getObjectLabels()) {
				if (l.isNative()
						&& (l.getNativeObjectID() == DOMObjects.HTMLIMAGEELEMENT_INSTANCES || l.getNativeObjectID() == DOMObjects.HTMLCANVASELEMENT_INSTANCES)) {
					good = true;
				} else {
					bad = true;
				}
			}
			String msg = "TypeError, non-HTMLImageElement or non-HTMLCanvasElement as argument to drawImage";
			Message.Status status = Message.Status.NONE;
			if (!good && bad) {
				status = Message.Status.CERTAIN;
			} else if (good && bad) {
				status = Message.Status.MAYBE;
			}
			c.addMessage(status, Message.Severity.HIGH, msg);

			Conversion.toNumber(call.getArg(1), c);
			Conversion.toNumber(call.getArg(2), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA: {
			NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
			/* Value sx = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value sy = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value sw = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value sh = */
			Conversion.toNumber(call.getArg(3), c);
			return Value.makeObject(IMAGE_DATA, new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA: {
			NativeFunctions.expectParameters(nativeObject, call, c, 3, -1);
			/* Value imageData = */
			DOMConversion.toNativeObject(DOMObjects.IMAGEDATA, call.getArg(0), false, c);
			Value dx = Conversion.toNumber(call.getArg(1), c);
			Value dy = Conversion.toNumber(call.getArg(2), c);

			final String message = "TypeError, inf or NaN as arguments to CanvasRenderingContext2D.putImageData";
			Message.Status status = Message.Status.NONE;
			if (dx.isNaN() || dy.isNaN()) {
				status = Message.Status.CERTAIN;
			} else if (dx.isMaybeInf() || dy.isMaybeInf()) {
				status = Message.Status.MAYBE;
			}
			c.addMessage(status, Message.Severity.HIGH, message);

			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASGRADIENT_ADD_COLOR_STOP: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value offset = */
			Conversion.toNumber(call.getArg(0), c);
			/* Value color = */
			Conversion.toString(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_STROKE_TEXT:
		case CANVASRENDERINGCONTEXT2D_FILL_TEXT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 3, 4);
			/* Value text = */
			Conversion.toString(call.getArg(0), c);
			/* Value x = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value y = */
			Conversion.toNumber(call.getArg(2), c);
			if (call.getArg(3) != Value.makeUndef(new Dependency(), new DependencyGraphReference())) {
				/* Value maxWidth = */
				Conversion.toNumber(call.getArg(3), c);
			}
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_MEASURE_TEXT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value text = */
			Conversion.toString(call.getArg(0), c);
			return Value.makeObject(TEXT_METRICS, new Dependency(), new DependencyGraphReference());
		}
		case CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 2);
			if (call.getArg(1) == Value.makeUndef(new Dependency(), new DependencyGraphReference())) {
				// Version with only one argument
				/* Value imageData = */
				DOMConversion.toNativeObject(DOMObjects.IMAGEDATA, call.getArg(0), false, c);
			} else {
				// Version with two arguments
				/* Value sw = */
				Conversion.toNumber(call.getArg(0), c);
				/* Value sh = */
				Conversion.toNumber(call.getArg(1), c);
			}
			return Value.makeObject(IMAGE_DATA, new Dependency(), new DependencyGraphReference());
		}
			// Focus Management
		case CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING: {
			NativeFunctions.expectParameters(nativeObject, call, c, 3, 4);
			/* Value element = */
			DOMConversion.toHTMLElement(call.getArg(0), c);
			/* Value xCaret = */
			Conversion.toNumber(call.getArg(1), c);
			/* Value yCaret = */
			Conversion.toNumber(call.getArg(2), c);
			/* Value canDrawCustom (optional) = */
			Conversion.toBoolean(call.getArg(3));
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
