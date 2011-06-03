package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestChrome {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestDOM");
    }

    @Before
    public void init() {
        Options.reset();
        Options.setTest(true);
        Options.setDOM(true);
    }

    @Test
    public void testChrome015() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/015.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome018() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/018.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome022() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/022.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome026() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/026.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome029() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/029.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome032() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/032.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome033() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/033.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome034() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/034.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome046() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/046.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome069() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/069.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome076() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/076.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome077() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/077.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome090() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/090.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome091() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/091.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome092() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/092.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome094() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/094.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testChrome099() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/chrome/099.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

}
