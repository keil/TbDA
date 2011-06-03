package dk.brics.tajs.test;

//import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
//import org.junit.Ignore;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;

public class TestGoogle { // TODO: STUDY THE CAUSES OF FALSE POSITIVES!

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestGoogle");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		// Options.set("-no-lazy");
	}
	
	@Test
	public void testRichards() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/google/richards.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testBenchpress() throws Exception { // FIXME: check output
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/google/benchpress.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testDeltaBlue() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/google/delta-blue.js"};
		Main.main(args);
		//fail("???"); // TODO 
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testCryptobench() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/google/cryptobench.js"};
		Main.main(args);
		//fail("???"); // TODO 
		Misc.checkSystemOutput();
	}
}
