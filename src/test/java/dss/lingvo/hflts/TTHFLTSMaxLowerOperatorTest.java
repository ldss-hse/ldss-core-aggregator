package dss.lingvo.hflts;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TTHFLTSMaxLowerOperatorTest {
    private static TTHFLTSScale singleScale;
    private static TTHFLTSMaxLowerOperator maxLower;

    @BeforeClass
    public static void runOnceBeforeClass() {
        String[] scaleString = {"n", "vl", "l", "m", "h", "vh", "p"};
        singleScale = new TTHFLTSScale(scaleString);
        maxLower = new TTHFLTSMaxLowerOperator();
        ;
    }

    @Test
    public void testCalculate1() throws Exception {
        String[] x1c1 = {"vl", "l", "m"};
        TTHFLTS x1c1H = new TTHFLTS(x1c1);

        String[] x1c2 = {"h", "vh"};
        TTHFLTS x1c2H = new TTHFLTS(x1c2);

        String[] x1c3 = {"h"};
        TTHFLTS x1c3H = new TTHFLTS(x1c3);

        TTHFLTS[] x1H = {x1c1H, x1c2H, x1c3H};

        String res = maxLower.calculate(new TTHFLTSAlternativeEstimation(x1H), singleScale);
        assertEquals(res, "h");
    }

    @Test
    public void testCalculate2() throws Exception {
        String[] x2c1 = {"l", "m"};
        TTHFLTS x2c1H = new TTHFLTS(x2c1);

        String[] x2c2 = {"m"};
        TTHFLTS x2c2H = new TTHFLTS(x2c2);

        String[] x2c3 = {"n", "vl", "l"};
        TTHFLTS x2c3H = new TTHFLTS(x2c3);

        TTHFLTS[] x2H = {x2c1H, x2c2H, x2c3H};

        String res = maxLower.calculate(new TTHFLTSAlternativeEstimation(x2H), singleScale);
        assertEquals(res, "m");
    }

    @Test
    public void testCalculate3() throws Exception {
        String[] x3c1 = {"h", "vh", "p"};
        TTHFLTS x3c1H = new TTHFLTS(x3c1);

        String[] x3c2 = {"vl", "l"};
        TTHFLTS x3c2H = new TTHFLTS(x3c2);

        String[] x3c3 = {"h", "vh", "p"};
        TTHFLTS x3c3H = new TTHFLTS(x3c3);

        TTHFLTS[] x3H = {x3c1H, x3c2H, x3c3H};

        String res = maxLower.calculate(new TTHFLTSAlternativeEstimation(x3H), singleScale);
        assertEquals(res, "h");
    }
}