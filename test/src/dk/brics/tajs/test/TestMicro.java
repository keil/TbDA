package dk.brics.tajs.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;

public class TestMicro {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestMicro");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		//Options.set("-no-flowgraph-optimization");
	}

	@Test
	public void testMicro00() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test00.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro01() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test01.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro02() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test02.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro03() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test03.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro04() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test04.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro05() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test05.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro06() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test06.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro07() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test07.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro08() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test08.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro09() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test09.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro10() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test10.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro11() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test11.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro12() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test12.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro13() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test13.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro14() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test14.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro15() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test15.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro16() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test16.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro17() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test17.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro18() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test18.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro19() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test19.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro20() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test20.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro21() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test21.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro22() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test22.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro23() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test23.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro24() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test24.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro25() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test25.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro26() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test26.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro27() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test27.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro28() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test28.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro29() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test29.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro30() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test30.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro31() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test31.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro32() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test32.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro33() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test33.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro34() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test34.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro35() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test35.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro36() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test36.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro37() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test37.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro38() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test38.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro39() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test39.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro40() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test40.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro41() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test41.js", "test/micro/test41b.js"};
		Main.main(args);
		 Misc.checkSystemOutput();
	}

	@Test
	public void testMicro42() throws Exception {  
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test42.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro43() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test43.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro44() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test44.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro45() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test45.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro46() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test46.js"};
		Main.main(args);
		Misc.checkSystemOutput();// TODO: summary function object (related to function object joining?)		
	}

	@Test
	public void testMicro47() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test47.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro48() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test48.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro49() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test49.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: for-in
	@Test
	public void testMicro50() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test50.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: for-in
	@Test
	public void testMicro51() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test51.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro52() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test52.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro53() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test53.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro54() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test54.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro55() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test55.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro56() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test56.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro57() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test57.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro58() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test58.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro59() throws Exception { // TODO: precision could be improved at concatenation of uint / non-uint strings 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test59.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro60() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test60.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro61() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test61.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro62() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test62.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: for-in
	@Test
	public void testMicro63() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test63.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro64() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test64.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro65() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test65.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro66() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test66.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro67() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test67.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro68() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test68.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro69() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test69.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro70() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test70.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro71() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test71.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro72() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test72.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro73() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test73.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: for-in
	@Test
	public void testMicro74() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test74.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	

	@Test
	public void testMicro75() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test75.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro76() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test76.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro77() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test77.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro78() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test78.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro79() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test79.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro80() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test80.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro81() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test81.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro82() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test82.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro83() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test83.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro84() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test84.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro85() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test85.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro86() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test86.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro87() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test87.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro88() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test88.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro89() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test89.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro90() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test90.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro91() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test91.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro92() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test92.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro93() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test93.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro94() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test94.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro95() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test95.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro96() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test96.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro97() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test97.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro98() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test98.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro99() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test99.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro100() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test100.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: assumeNonNullUndef
	@Test
	public void testMicro101() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test101.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro102() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test102.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro103() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test103.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro104() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test104.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro105() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test105.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro106() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test106.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro107() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test107.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro108() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test108.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro109() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test109.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro110() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test110.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro111() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test111.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro112() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test112.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro113() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test113.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: 'Unknown node type: SET_REF', should be runtime error (but see Sec 16 p.149)
	@Test
	public void testMicro114() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test114.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro115() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test115.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro116() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test116.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro117() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test117.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro118() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test118.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro119() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test119.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro123() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test123.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro126() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test126.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro127() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test127.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro128() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test128.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro129() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test129.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro130() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test130.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro131() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test131.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro132() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test132.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro133() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test133.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro134() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test134.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro135() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test135.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro136() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test136.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro137() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test137.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro138() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test138.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testMicro139() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test139.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

    @Ignore // TODO
	@Test
	public void testMicro140() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test140.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro141() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test141.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro142() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test142.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

    @Ignore // TODO
	@Test
	public void testArray() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testArray.js"};
		Main.main(args);
		fail("check output"); // FIXME: testArray.js
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testBoolean() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testBoolean.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: testEval
	@Test
	public void testEval() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testEval.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	
	@Ignore // TODO: Function(..)
	@Test
	public void testFunction() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testFunction.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
	
	@Test
	public void testFunctionApply() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testFunctionApply.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testFunctionCall() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testFunctionCall.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testNumber() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testNumber.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testObject() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testObject.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testOO() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testOO.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testRegExp() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testRegExp.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testString() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testString.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

    @Ignore // TODO
	@Test
	public void testToPrimitive() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testToPrimitive.js"};
		Main.main(args);
		fail("check output"); // FIXME: testToPrimitive.js 
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testPaper() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testPaper.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}
}
