package dk.brics.tajs.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.flowgraph.nodes.NopNode;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

public class DependencyValueTest {

	Value vBottom;
	Value vAbsent;
	Value vUndef;
	Value vNull;
	Value vAnyBool;
	Value vTrue;
	Value vFalse;
	Value vAnyNum;
	Value v65536;
	Value v314159;
	Value v127e35;
	Value vNaN;
	Value vNaN2;
	Value vPosInf;
	Value vInf;
	Value vNumUInt;
	Value vNumNotUInt;
	Value vAnyStr;
	Value vEmptyStr;
	Value vSomeStr;
	Value vObject1;
	Value vObject2;
	
	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.DependencyXValueTest");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		Options.setDebug(true);
		
		clear();
	}
	
	public void clear() {
		FlowGraph fg = new FlowGraph();
		SourceLocation loc = new SourceLocation(117, "foo.js");
		List<String> args = Collections.emptyList();
		Function f = new Function("foo", args, fg, loc);
		BasicBlock b = new BasicBlock(f);
		Node n = new NopNode(loc); 
		b.addNode(n);
		fg.addBlock(b, f);
		fg.addFunction(f);
		fg.setMain(f);
		fg.complete();
		
		Value.clearCaches();
		
		vBottom = Value.makeBottom(new Dependency(), new DependencyGraphReference());
		vAbsent = Value.makeAbsent(new Dependency(), new DependencyGraphReference());
		vUndef = Value.makeUndef(new Dependency(), new DependencyGraphReference());
		vNull = Value.makeNull(new Dependency(), new DependencyGraphReference());
		vAnyBool = Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		vTrue = Value.makeBool(true, new Dependency(), new DependencyGraphReference());
		vFalse = Value.makeBool(false, new Dependency(), new DependencyGraphReference());
		vAnyNum = Value.makeAnyNum(new Dependency(), new DependencyGraphReference());
		v65536 = Value.makeNum(65536, new Dependency(), new DependencyGraphReference());
		v314159 = Value.makeNum(3.14159, new Dependency(), new DependencyGraphReference());
		v127e35 = Value.makeNum(-.127e35, new Dependency(), new DependencyGraphReference());
		vNaN = Value.makeNumNaN(new Dependency(), new DependencyGraphReference());
		vNaN2 = Value.makeNum(Double.NaN, new Dependency(), new DependencyGraphReference());
		vPosInf = Value.makeNum(Double.POSITIVE_INFINITY, new Dependency(), new DependencyGraphReference());
		vInf = Value.makeNumInf(new Dependency(), new DependencyGraphReference());
		vNumUInt = Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference());
		vNumNotUInt = Value.makeAnyNumNotUInt(new Dependency(), new DependencyGraphReference());
		vAnyStr = Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		vEmptyStr = Value.makeStr("", new Dependency(), new DependencyGraphReference());
		vSomeStr = Value.makeStr("bar", new Dependency(), new DependencyGraphReference());
		vObject1 = Value.makeObject(new ObjectLabel(n, Kind.OBJECT), new Dependency(), new DependencyGraphReference());
		vObject2 = Value.makeObject(new ObjectLabel(n, Kind.BOOLEAN), new Dependency(), new DependencyGraphReference());
	}
	
	private static void printInfo(Value v) {
		System.out.println("****" + v);
		System.out.println("Dependency: " + v.printDependency());
	}

	@Test
	public void testXValue() {
		Misc.start();
		Misc.captureSystemOutput();		
		
		System.out.println(vBottom);
		System.out.println(vAbsent);
		System.out.println(vUndef);
		System.out.println(vNull);
		System.out.println(vAnyBool);
		System.out.println(vTrue);
		System.out.println(vFalse);
		System.out.println(vAnyNum);
		System.out.println(v65536);
		System.out.println(v314159);
		System.out.println(v127e35);
		System.out.println(vNaN);
		System.out.println(vNaN2);
		System.out.println(vPosInf);
		System.out.println(vInf);
		System.out.println(vNumUInt);
		System.out.println(vNumNotUInt);
		System.out.println(vAnyStr);
		System.out.println(vEmptyStr);
		System.out.println(vSomeStr);
		System.out.println(vObject1);
		System.out.println(vObject2);
		
		printInfo(vBottom);
		printInfo(vAbsent);
		printInfo(vUndef);
		printInfo(vNull);
		printInfo(vAnyBool);
		printInfo(vTrue);
		printInfo(vFalse);
		printInfo(vAnyNum);
		printInfo(v65536);
		printInfo(v314159);
		printInfo(v127e35);
		printInfo(vNaN);
		printInfo(vNaN2);
		printInfo(vPosInf);
		printInfo(vAnyStr);
		printInfo(vEmptyStr);
		printInfo(vSomeStr);
		printInfo(vObject1);
		printInfo(vObject2);
		
		/* # */ clear();
		
		Value v0 = v65536.joinModified();
		printInfo(v0);
		v0 = v0.clearModified();
		printInfo(v0);
		v0 = v0.clearModified();
		printInfo(v0);
		v0 = v0.joinModified();
		printInfo(v0);
		v0 = v0.joinModified();
		printInfo(v0);
		
		/* # */ clear();
		
		Value v1 = vUndef.joinAbsent();
		printInfo(v1);
		v1 = v1.joinAbsent();
		printInfo(v1);
		v1 = vUndef.joinAbsentWithModified();
		printInfo(v1);
		v1 = v1.clearAbsent();
		printInfo(v1);
		v1 = v1.clearAbsent();
		printInfo(v1);

		/* # */ clear();
		
		Value v2 = vUndef.joinAbsentWithModified();
		printInfo(v2);
		v2 = v2.joinAbsentWithModified();
		printInfo(v2);
		
		/* # */ clear();
		
		Value v3 = vUndef.setDontDelete().setDontEnum().setReadOnly();
		printInfo(v3);
		v3 = v3.setDontDelete().setDontEnum().setReadOnly();
		printInfo(v3);
		Value v4 = vNull.setAttributes(v3);
		printInfo(v4);
		v4 = v4.removeAttributes();
		printInfo(v4);
		v4 = v4.bottomAttributes();
		printInfo(v4);
		v4 = v4.setNotDontDelete().setNotDontEnum().setNotReadOnly();
		printInfo(v4);
		v4 = v4.setNotDontDelete().setNotDontEnum().setNotReadOnly();
		printInfo(v4);
		v4 = v3.joinNotDontDelete().joinNotDontEnum().joinNotReadOnly();
		printInfo(v4);
		v4 = v4.joinNotDontDelete().joinNotDontEnum().joinNotReadOnly();
		
		/* # */ clear();
		
		printInfo(vTrue.joinWithModified(vFalse));
		printInfo(vTrue.joinWithModified(vTrue));
		printInfo(Value.join(vTrue,vFalse,vUndef,v314159,vSomeStr));
		printInfo(Value.join());
		printInfo(Value.join(vTrue,vTrue));
		
		/* # */ clear();
		
		printInfo(Value.join(Value.makeNum(1, new Dependency(), new DependencyGraphReference()), Value.makeNum(2, new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeNum(1, new Dependency(), new DependencyGraphReference()), Value.makeNum(2.5, new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeNum(1.5, new Dependency(), new DependencyGraphReference()), Value.makeNum(2.5, new Dependency(), new DependencyGraphReference())));

		/* # */ clear();
		
		System.out.println(vTrue.equals(vFalse));
		System.out.println(vFalse.equals(vFalse));
		
		/* # */ clear();
		
		printInfo(vTrue.join(vFalse));
		printInfo(v65536.join(v314159));
		printInfo(vEmptyStr.join(vSomeStr));
		printInfo(vUndef.join(vNull));
		printInfo(vNull.join(vFalse).join(v127e35).join(vSomeStr).join(vObject1));
		printInfo(vObject1.join(vObject2));

		/* # */ clear();
		
		printInfo(vUndef.joinUndef());
		printInfo(vNull.joinUndef());
		printInfo(vNull.joinUndef().restrictToNotUndef());
		printInfo(vNull.joinUndef().restrictToUndef());
		printInfo(vNull.joinUndef().restrictToNotNullNotUndef());
		
		/* # */ clear();
		
		printInfo(vUndef.joinNull());
		printInfo(vNull.joinNull());
		printInfo(vTrue.joinNull().restrictToNotNull());
		printInfo(vTrue.joinNull().restrictToNull());
		printInfo(vTrue.restrictToNull());

		/* # */ clear();
		
		printInfo(vAnyNum.joinAnyBool());
		printInfo(vAnyNum.joinBool(false));
		printInfo(vAnyNum.joinBool(true).restrictToBool());

		/* # */ clear();
		
		printInfo(vAnyStr.joinAnyNum());
		printInfo(vAnyStr.joinAnyNumUInt());
		printInfo(vAnyStr.joinAnyNumNotUInt());
		printInfo(vAnyNum.restrictToNotNaN());

		/* # */ clear();
		
		printInfo(Value.makeNum(0, new Dependency(), new DependencyGraphReference()).joinNum(1));
		printInfo(Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).joinNum(Double.NaN));
		printInfo(Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).joinNumNaN());
		printInfo(Value.makeBottom(new Dependency(), new DependencyGraphReference()).joinNum(Double.POSITIVE_INFINITY));
		printInfo(Value.makeBottom(new Dependency(), new DependencyGraphReference()).joinNumInf());
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).joinNum(42).restrictToNum());

		/* # */ clear();
		
		printInfo(Value.makeStr("foo", new Dependency(), new DependencyGraphReference()).joinAnyStr());
		printInfo(Value.makeStr("foo", new Dependency(), new DependencyGraphReference()).joinAnyStrUInt());
		printInfo(Value.makeStr("4", new Dependency(), new DependencyGraphReference()).joinAnyStrUInt());
		printInfo(Value.makeStr("4", new Dependency(), new DependencyGraphReference()).joinStr("5"));
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).join(vSomeStr).restrictToStr());
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).join(vSomeStr).restrictToStrBoolNum());
		
		/* # */ clear();
		
		Set<ObjectLabel> objs = new HashSet<ObjectLabel>();
		objs.add(new ObjectLabel(ECMAScriptObjects.ARRAY_SORT, Kind.FUNCTION));
		Value v7 = Value.makeObject(objs, new Dependency(), new DependencyGraphReference());
		printInfo(v7);
		v7 = v7.joinAnyStr();
		printInfo(v7);
		
		/* # */ clear();
		
		List<Value> vs = new ArrayList<Value>();
		vs.add(vTrue);
		vs.add(vFalse);
		vs.add(Value.makeNum(1.2, new Dependency(), new DependencyGraphReference()));
		vs.add(Value.makeNum(3.4, new Dependency(), new DependencyGraphReference()));
		printInfo(Value.join(vs));

		/* # */ clear();
		
		printInfo(Value.join(Value.makeNull(new Dependency(), new DependencyGraphReference()), Value.makeBool(true, new Dependency(), new DependencyGraphReference()), Value.makeNumNaN(new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeNull(new Dependency(), new DependencyGraphReference()), Value.makeBool(false, new Dependency(), new DependencyGraphReference()), Value.makeNumInf(new Dependency(), new DependencyGraphReference())));

		/* # */ clear();
		
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).joinAnyBool());
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).joinBool(true));
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).joinAnyNum().restrictToBool());
		printInfo(Value.makeBool(true, new Dependency(), new DependencyGraphReference()).joinAnyNum().restrictToBool());
		printInfo(Value.makeBool(false, new Dependency(), new DependencyGraphReference()).joinAnyNum().joinAnyNum().joinAnyNumUInt().restrictToBool());
		printInfo(Value.makeNum(87, new Dependency(), new DependencyGraphReference()).joinAnyNumUInt());
		printInfo(Value.makeNum(87, new Dependency(), new DependencyGraphReference()).joinNum(42).joinAnyNumUInt());
		printInfo(Value.makeNum(87, new Dependency(), new DependencyGraphReference()).joinAnyNumNotUInt());
		printInfo(Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).restrictToNotNaN());
		printInfo(Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).restrictToNotNaN().joinNumNaN());
		printInfo(Value.makeStr("foo", new Dependency(), new DependencyGraphReference()).joinNumInf());
		printInfo(Value.join(Value.makeNum(87, new Dependency(), new DependencyGraphReference()), Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeNum(87, new Dependency(), new DependencyGraphReference()), Value.makeAnyNumNotUInt(new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeAnyNumNotUInt(new Dependency(), new DependencyGraphReference()), Value.makeNum(87, new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeStr("x", new Dependency(), new DependencyGraphReference()), Value.makeAnyStr(new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), Value.makeStr("x", new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), Value.makeStr("1", new Dependency(), new DependencyGraphReference()), Value.makeStr("2", new Dependency(), new DependencyGraphReference())));
		printInfo(Value.join(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), Value.makeStr("x", new Dependency(), new DependencyGraphReference()), Value.makeStr("y", new Dependency(), new DependencyGraphReference())));
		printInfo(vNull.restrictToNotUndef());
		printInfo(vNull.restrictToUndef());
		printInfo(vUndef.restrictToUndef());
		printInfo(vUndef.restrictToNotNull());
		printInfo(vSomeStr.restrictToNotNullNotUndef());
		printInfo(vTrue.joinBool(true));
		printInfo(vNull.restrictToBool());
		printInfo(vAnyNum.joinAnyNumNotUInt());
		printInfo(vAnyBool.restrictToNotNaN()); 
		printInfo(Value.makeNum(1, new Dependency(), new DependencyGraphReference()).joinNum(1));
		printInfo(Value.makeNum(1, new Dependency(), new DependencyGraphReference()).joinNum(Double.NaN));
		printInfo(Value.makeNum(1, new Dependency(), new DependencyGraphReference()).joinNum(Double.POSITIVE_INFINITY));
		printInfo(Value.makeNum(1, new Dependency(), new DependencyGraphReference()).joinNumNaN());
		printInfo(Value.makeNum(1, new Dependency(), new DependencyGraphReference()).joinNumInf());
		printInfo(Value.makeNumInf(new Dependency(), new DependencyGraphReference()).joinNumInf());
		printInfo(Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinAnyStr());
		printInfo(Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinAnyStrUInt());
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).joinAnyNumUInt());
		printInfo(Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).joinAnyStrUInt());
		printInfo(Value.makeStr("x", new Dependency(), new DependencyGraphReference()).joinStr("x"));
		printInfo(vNull.joinStr("x"));
		printInfo(Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).restrictToStr());
		printInfo(vNull.restrictToStr());

		/* # */ clear();
		
		printInfo(v7.restrictToObject());
		printInfo(v7.restrictToNonObject());
		printInfo(v7.restrictToNonObject().restrictToNonObject());
		printInfo(v7.joinNumInf().restrictToObject());
		printInfo(v7.joinNumInf().restrictToNonObject());
		printInfo(v7.restrictToObject().restrictToObject());

		/* # */ clear();
		
		printInfo(Value.makeAbsentModified(new Dependency(), new DependencyGraphReference()));
		printInfo(Value.makeAnyNumNotNaNInf(new Dependency(), new DependencyGraphReference()));

		
		// TODO: coverage test for joinObject, isNonModifiedAndInconsistentWith, replaceIfNonModified, replaceObjectLabel, mixObjectLabels, shuffleObjectLabels, replaceSingletonBySummary
	}
}