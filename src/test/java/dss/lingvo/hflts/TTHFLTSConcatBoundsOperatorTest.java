package dss.lingvo.hflts;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class TTHFLTSConcatBoundsOperatorTest {
    private static TTHFLTSConcatBoundsOperator concatOperator;
    private static TTHFLTSScale singleScale;

    @BeforeClass
    public static void runOnceBeforeClass(){
        String[] scaleString = {"n", "vl", "l", "m", "h", "vh", "p"};
        singleScale = new TTHFLTSScale(scaleString);
        concatOperator = new TTHFLTSConcatBoundsOperator();
    }

    @Test
    public void testCalculate1() throws Exception {
        final String top = "m";
        final String bottom = "h";
        String[] res = concatOperator.calculate(top, bottom, singleScale);
        String [] expectedRes = {"m","h"};
        assertArrayEquals(res, expectedRes);
    }

    @Test
    public void testCalculate2() throws Exception {
        final String top = "l";
        final String bottom = "m";
        String[] res = concatOperator.calculate(top, bottom, singleScale);
        String [] expectedRes = {"l","m"};
        assertArrayEquals(res, expectedRes);
    }

    @Test
    public void testCalculate3() throws Exception {
        final String top = "l";
        final String bottom = "h";
        String[] res = concatOperator.calculate(top, bottom, singleScale);
        String [] expectedRes = {"l","h"};
        assertArrayEquals(res, expectedRes);
    }

}