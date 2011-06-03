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

public class TestValue {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestXValue");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		Options.setDebug(true);
	}
	
	private static void printInfo(Value v) {
		System.out.println("****" + v);
		System.out.println("Attributes: " + v.printAttributes());

		System.out.println("isBottom = " + v.isBottom());
		System.out.println("isNoXValue = " + v.isNoValue());
		System.out.println("isMaybeXValue = " + v.isMaybeValue());
		System.out.println("isMaybePrimitive = " + v.isMaybePrimitive());

		System.out.println("isMaybeAbsent = " + v.isMaybeAbsent());
		System.out.println("isNotAbsent = " + v.isNotAbsent());
		
		System.out.println("isMaybeUndef = " + v.isMaybeUndef());
		System.out.println("isMaybeOtherThanUndef = " + v.isMaybeOtherThanUndef());
		System.out.println("isNotUndef = " + v.isNotUndef());

		System.out.println("isMaybeNull = " + v.isMaybeNull());
		System.out.println("isNotNull = " + v.isNotNull());
		System.out.println("isMaybeOtherThanNull = " + v.isMaybeOtherThanNull());
		System.out.println("isNullOrUndef = " + v.isNullOrUndef());

		System.out.println("isMaybeAnyBool = " + v.isMaybeAnyBool());
		System.out.println("isTrue = " + v.isMaybeTrueButNotFalse());
		System.out.println("isFalse = " + v.isMaybeFalseButNotTrue());
		System.out.println("isNotBool = " + v.isNotBool());
		System.out.println("isMaybeOtherThanBool = " + v.isMaybeOtherThanBool());
		System.out.println("isMaybeTrue = " + v.isMaybeTrue());
		System.out.println("isMaybeFalse = " + v.isMaybeFalse());
		
		System.out.println("isNotNum = " + v.isNotNum());
		System.out.println("isNaN = " + v.isNaN());
		System.out.println("isMaybeInf = " + v.isMaybeInf());
		System.out.println("isMaybeNumUInt = " + v.isMaybeNumUInt());
		System.out.println("isMaybeNumNotUInt = " + v.isMaybeNumNotUInt());
		System.out.println("getNum = " + v.getNum());
		System.out.println("isMaybeFuzzyNum = " + v.isMaybeFuzzyNum());
		System.out.println("isMaybeOtherThanNum = " + v.isMaybeOtherThanNum());
		
		System.out.println("isMaybeOtherThanStr = " + v.isMaybeOtherThanStr());
		System.out.println("isNotStr = " + v.isNotStr());
		System.out.println("getStr = " + v.getStr());
		System.out.println("isMaybeStrUInt = " + v.isMaybeStrUInt());
		
		System.out.println("isObject = " + v.isMaybeObject());
		System.out.println("getObjectLabels = " + v.getObjectLabels());
		
		System.out.println("isMaybeModified = " + v.isMaybeModified());
		
		System.out.println("isDontEnum = " + v.isDontEnum());
		System.out.println("isMaybeDontEnum = " + v.isMaybeDontEnum());
		System.out.println("isMaybeNotDontEnum = " + v.isMaybeNotDontEnum());
		System.out.println("isNotDontEnum = " + v.isNotDontEnum());
		
		System.out.println("isDontDelete = " + v.isDontDelete());
		System.out.println("isMaybeDontDelete = " + v.isMaybeDontDelete());
		System.out.println("isMaybeNotDontDelete = " + v.isMaybeNotDontDelete());
		System.out.println("isNotDontDelete = " + v.isNotDontDelete());
		
		System.out.println("isReadOnly = " + v.isReadOnly());
		System.out.println("isMaybeReadOnly = " + v.isMaybeReadOnly());
		System.out.println("isMaybeNotReadOnly = " + v.isMaybeNotReadOnly());
		System.out.println("isNotReadOnly = " + v.isNotReadOnly());
	}

	@Test
	public void testXValue() {
		Misc.start();
		Misc.captureSystemOutput();
		
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
		
		Value vBottom = Value.makeBottom(new Dependency());
		Value vAbsent = Value.makeAbsent(new Dependency());
		Value vUndef = Value.makeUndef(new Dependency());
		Value vNull = Value.makeNull(new Dependency());
		Value vAnyBool = Value.makeAnyBool(new Dependency());
		Value vTrue = Value.makeBool(true, new Dependency());
		Value vFalse = Value.makeBool(false, new Dependency());
		Value vAnyNum = Value.makeAnyNum(new Dependency());
		Value v65536 = Value.makeNum(65536, new Dependency());
		Value v314159 = Value.makeNum(3.14159, new Dependency());
		Value v127e35 = Value.makeNum(-.127e35, new Dependency());
		Value vNaN = Value.makeNumNaN(new Dependency());
		Value vNaN2 = Value.makeNum(Double.NaN, new Dependency());
		Value vPosInf = Value.makeNum(Double.POSITIVE_INFINITY, new Dependency());
		Value vInf = Value.makeNumInf(new Dependency());
		Value vNumUInt = Value.makeAnyNumUInt(new Dependency());
		Value vNumNotUInt = Value.makeAnyNumNotUInt(new Dependency());
		Value vAnyStr = Value.makeAnyStr(new Dependency());
		Value vEmptyStr = Value.makeStr("", new Dependency());
		Value vSomeStr = Value.makeStr("bar", new Dependency());
		Value vObject1 = Value.makeObject(new ObjectLabel(n, Kind.OBJECT), new Dependency());
		Value vObject2 = Value.makeObject(new ObjectLabel(n, Kind.BOOLEAN), new Dependency());
		
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

		Value v2 = vUndef.joinAbsentWithModified();
		printInfo(v2);
		v2 = v2.joinAbsentWithModified();
		printInfo(v2);
		
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
		
		printInfo(vTrue.joinWithModified(vFalse));
		printInfo(vTrue.joinWithModified(vTrue));
		printInfo(Value.join(vTrue,vFalse,vUndef,v314159,vSomeStr));
		printInfo(Value.join());
		printInfo(Value.join(vTrue,vTrue));
		
		printInfo(Value.join(Value.makeNum(1, new Dependency()), Value.makeNum(2, new Dependency())));
		printInfo(Value.join(Value.makeNum(1, new Dependency()), Value.makeNum(2.5, new Dependency())));
		printInfo(Value.join(Value.makeNum(1.5, new Dependency()), Value.makeNum(2.5, new Dependency())));

		System.out.println(vTrue.equals(vFalse));
		System.out.println(vFalse.equals(vFalse));
		
		printInfo(vTrue.join(vFalse));
		printInfo(v65536.join(v314159));
		printInfo(vEmptyStr.join(vSomeStr));
		printInfo(vUndef.join(vNull));
		printInfo(vNull.join(vFalse).join(v127e35).join(vSomeStr).join(vObject1));
		printInfo(vObject1.join(vObject2));

		printInfo(vUndef.joinUndef());
		printInfo(vNull.joinUndef());
		printInfo(vNull.joinUndef().restrictToNotUndef());
		printInfo(vNull.joinUndef().restrictToUndef());
		printInfo(vNull.joinUndef().restrictToNotNullNotUndef());
		
		printInfo(vUndef.joinNull());
		printInfo(vNull.joinNull());
		printInfo(vTrue.joinNull().restrictToNotNull());
		printInfo(vTrue.joinNull().restrictToNull());
		printInfo(vTrue.restrictToNull());

		printInfo(vAnyNum.joinAnyBool());
		printInfo(vAnyNum.joinBool(false));
		printInfo(vAnyNum.joinBool(true).restrictToBool());

		printInfo(vAnyStr.joinAnyNum());
		printInfo(vAnyStr.joinAnyNumUInt());
		printInfo(vAnyStr.joinAnyNumNotUInt());
		printInfo(vAnyNum.restrictToNotNaN());

		printInfo(Value.makeNum(0, new Dependency()).joinNum(1));
		printInfo(Value.makeAnyNum(new Dependency()).joinNum(Double.NaN));
		printInfo(Value.makeAnyNum(new Dependency()).joinNumNaN());
		printInfo(Value.makeBottom(new Dependency()).joinNum(Double.POSITIVE_INFINITY));
		printInfo(Value.makeBottom(new Dependency()).joinNumInf());
		printInfo(Value.makeAnyBool(new Dependency()).joinNum(42).restrictToNum());

		printInfo(Value.makeStr("foo", new Dependency()).joinAnyStr());
		printInfo(Value.makeStr("foo", new Dependency()).joinAnyStrUInt());
		printInfo(Value.makeStr("4", new Dependency()).joinAnyStrUInt());
		printInfo(Value.makeStr("4", new Dependency()).joinStr("5"));
		printInfo(Value.makeAnyBool(new Dependency()).join(vSomeStr).restrictToStr());
		printInfo(Value.makeAnyBool(new Dependency()).join(vSomeStr).restrictToStrBoolNum());
		
		Set<ObjectLabel> objs = new HashSet<ObjectLabel>();
		objs.add(new ObjectLabel(ECMAScriptObjects.ARRAY_SORT, Kind.FUNCTION));
		Value v7 = Value.makeObject(objs, new Dependency());
		printInfo(v7);
		v7 = v7.joinAnyStr();
		printInfo(v7);
		
		List<Value> vs = new ArrayList<Value>();
		vs.add(vTrue);
		vs.add(vFalse);
		vs.add(Value.makeNum(1.2, new Dependency()));
		vs.add(Value.makeNum(3.4, new Dependency()));
		printInfo(Value.join(vs));

		printInfo(Value.join(Value.makeNull(new Dependency()), Value.makeBool(true, new Dependency()), Value.makeNumNaN(new Dependency())));
		printInfo(Value.join(Value.makeNull(new Dependency()), Value.makeBool(false, new Dependency()), Value.makeNumInf(new Dependency())));

		printInfo(Value.makeAnyBool(new Dependency()).joinAnyBool());
		printInfo(Value.makeAnyBool(new Dependency()).joinBool(true));
		printInfo(Value.makeAnyBool(new Dependency()).joinAnyNum().restrictToBool());
		printInfo(Value.makeBool(true, new Dependency()).joinAnyNum().restrictToBool());
		printInfo(Value.makeBool(false, new Dependency()).joinAnyNum().joinAnyNum().joinAnyNumUInt().restrictToBool());
		printInfo(Value.makeNum(87, new Dependency()).joinAnyNumUInt());
		printInfo(Value.makeNum(87, new Dependency()).joinNum(42).joinAnyNumUInt());
		printInfo(Value.makeNum(87, new Dependency()).joinAnyNumNotUInt());
		printInfo(Value.makeAnyNum(new Dependency()).restrictToNotNaN());
		printInfo(Value.makeAnyNum(new Dependency()).restrictToNotNaN().joinNumNaN());
		printInfo(Value.makeStr("foo", new Dependency()).joinNumInf());
		printInfo(Value.join(Value.makeNum(87, new Dependency()), Value.makeAnyNumUInt(new Dependency())));
		printInfo(Value.join(Value.makeNum(87, new Dependency()), Value.makeAnyNumNotUInt(new Dependency())));
		printInfo(Value.join(Value.makeAnyNumNotUInt(new Dependency()), Value.makeNum(87, new Dependency())));
		printInfo(Value.join(Value.makeStr("x", new Dependency()), Value.makeAnyStr(new Dependency())));
		printInfo(Value.join(Value.makeAnyStr(new Dependency()), Value.makeStr("x", new Dependency())));
		printInfo(Value.join(Value.makeAnyBool(new Dependency()), Value.makeStr("1", new Dependency()), Value.makeStr("2", new Dependency())));
		printInfo(Value.join(Value.makeAnyBool(new Dependency()), Value.makeStr("x", new Dependency()), Value.makeStr("y", new Dependency())));
		printInfo(vNull.restrictToNotUndef());
		printInfo(vNull.restrictToUndef());
		printInfo(vUndef.restrictToUndef());
		printInfo(vUndef.restrictToNotNull());
		printInfo(vSomeStr.restrictToNotNullNotUndef());
		printInfo(vTrue.joinBool(true));
		printInfo(vNull.restrictToBool());
		printInfo(vAnyNum.joinAnyNumNotUInt());
		printInfo(vAnyBool.restrictToNotNaN()); 
		printInfo(Value.makeNum(1, new Dependency()).joinNum(1));
		printInfo(Value.makeNum(1, new Dependency()).joinNum(Double.NaN));
		printInfo(Value.makeNum(1, new Dependency()).joinNum(Double.POSITIVE_INFINITY));
		printInfo(Value.makeNum(1, new Dependency()).joinNumNaN());
		printInfo(Value.makeNum(1, new Dependency()).joinNumInf());
		printInfo(Value.makeNumInf(new Dependency()).joinNumInf());
		printInfo(Value.makeAnyStr(new Dependency()).joinAnyStr());
		printInfo(Value.makeAnyStr(new Dependency()).joinAnyStrUInt());
		printInfo(Value.makeAnyBool(new Dependency()).joinAnyNumUInt());
		printInfo(Value.makeAnyBool(new Dependency()).joinAnyStrUInt());
		printInfo(Value.makeStr("x", new Dependency()).joinStr("x"));
		printInfo(vNull.joinStr("x"));
		printInfo(Value.makeAnyStr(new Dependency()).restrictToStr());
		printInfo(vNull.restrictToStr());

		printInfo(v7.restrictToObject());
		printInfo(v7.restrictToNonObject());
		printInfo(v7.restrictToNonObject().restrictToNonObject());
		printInfo(v7.joinNumInf().restrictToObject());
		printInfo(v7.joinNumInf().restrictToNonObject());
		printInfo(v7.restrictToObject().restrictToObject());

		printInfo(Value.makeAbsentModified(new Dependency()));
		printInfo(Value.makeAnyNumNotNaNInf(new Dependency()));

		Misc.checkSystemOutput();
		
		// TODO: coverage test for joinObject, isNonModifiedAndInconsistentWith, replaceIfNonModified, replaceObjectLabel, mixObjectLabels, shuffleObjectLabels, replaceSingletonBySummary
	}
}