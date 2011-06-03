package dk.brics.tajs.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;

public class TestResig {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestResig");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		// Options.set("-no-lazy");
	}

	@Test
	public void testEjohn12() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn12.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn13() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn13.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn14() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn14.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn15() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn15.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn17() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn17.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn18() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn18.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn19() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn19.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn20() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn20.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn21() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn21.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn23() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn23.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn24() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn24.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn25() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn25.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn26() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn26.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn28() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn28.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn30() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn30.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn31() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn31.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn33() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn33.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn34() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn34.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn35() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn35.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn36() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn36.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn38() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn38.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in
	@Test
	public void testEjohn40() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn40.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn41() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn41.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn42() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn42.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn43() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn43.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

    @Ignore // TODO
	@Test
	public void testEjohn45() throws Exception { // TODO: shouldn't report unreachable code (from Array sort)
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn45.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn47() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn47.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn49() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn49.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn50() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn50.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: jQuery
	@Test
	public void testEjohn51() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn51.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn52() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn52.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn53() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn53.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn54() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn54.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn56() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn56.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn58() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn58.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn59() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn59.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn63() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn63.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn65() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn65.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn66() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn66.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn67() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn67.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn69() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn69.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn71() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn71.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn72() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn72.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn74() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn74.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn76() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn76.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn78() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn78.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn80() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn80.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in
	@Test
	public void testEjohn81() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn81.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn83() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn83.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn84() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn84.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn85() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn85.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void testEjohn86() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn86.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testEjohn88() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn88.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

    @Ignore // TODO
	@Test
	public void testEjohn90() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn90.js"};
		Main.main(args);
		fail("check output"); // FIXME: ejohn90.js 
		Misc.checkSystemOutput();
	}
}
