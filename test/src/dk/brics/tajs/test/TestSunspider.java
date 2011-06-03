package dk.brics.tajs.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;

public class TestSunspider {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestSunspider");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.setTest(true);
		// Options.set("-no-lazy");
	}

	@Test
	public void testSunspider3DCube() throws Exception {  // FIXME: check output
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/3d-cube.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspider3DMorph() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/3d-morph.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspider3DRaytrace() throws Exception {  // FIXME: check output
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/3d-raytrace.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessBinaryTrees() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-binary-trees.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessFannkuch() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-fannkuch.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessNBody() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-nbody.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessNSieve() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-nsieve.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderBitops3BitBitsInByte() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/bitops-3bit-bits-in-byte.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderBitopsBitwiseAnd() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/bitops-bitwise-and.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderBitopsNSieveBits() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/bitops-nsieve-bits.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderControlflowRecursive() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/controlflow-recursive.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void testSunspiderCryptoAES() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/crypto-aes.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderCryptoMD5() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/crypto-md5.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderCryptoSHA1() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/crypto-sha1.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: eval
	@Test
	public void testSunspiderDateFormatTofte() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/date-format-tofte.js"};
		Main.main(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void testSunspiderDateFormatXParb() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/date-format-xparb.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderMathCordic() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/math-cordic.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderMathPartialSums() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/math-partial-sums.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderMathSpectralNorm() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/math-spectral-norm.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderRegexpDNA() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/regexp-dna.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderStringBase64() throws Exception {
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-base64.js"}; // TODO: known to be buggy (according to Google Aarhus)
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderStringFasta() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-fasta.js"};
		Main.main(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderStringTagcloud() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-tagcloud.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderStringUnpackCode() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-unpack-code.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderStringValidateInput() throws Exception { 
		Misc.start();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-validate-input.js"};
		Main.main(args);
		fail(); // Misc.checkSystemOutput();
	}
}
