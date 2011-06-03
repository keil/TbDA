package dk.brics.tajs.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;

public class TestWala {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestWala");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		// Options.set("-no-lazy");
	}

	@Test
	public void testWalaControlFlow() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/control-flow.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: 'for-in'
	@Test
	public void testWalaForIn() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/forin.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void testWalaFunctions() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/functions.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaInherit() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/inherit.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaMoreControlFlow() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/more-control-flow.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: uses 'new Function("...")'
	@Test
	public void testWalaNewFn() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/newfn.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void testWalaObjects() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/objects.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testWalaPortalExampleSimple() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/portal-example-simple.html"};
		Main.main(args);
		throw new RuntimeException(".html");//fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Test
	public void testWalaSimpleLexical() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simple-lexical.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaSimple() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simple.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaSimpler() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simpler.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaStringOp() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/string-op.js"}; //note: bug in assumption
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaStringPrims() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/string-prims.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}


	@Test
	public void testWalaTry() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/try.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaUpward() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/upward.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
}
