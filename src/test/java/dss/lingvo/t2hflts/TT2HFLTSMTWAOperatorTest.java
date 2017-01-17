package dss.lingvo.t2hflts;

import dss.lingvo.hflts.TTHFLTSConcatBoundsOperator;
import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TT2HFLTSMTWAOperatorTest {
    private static TT2HFLTSMTWAOperator mtwaOperator;
    private static float[] weights;

    @BeforeClass
    public static void runOnceBeforeClass() {
        String[] scale9 = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        TTHFLTSScale hScale9 = new TTHFLTSScale(scale9);

        String[] scale3 = {"0", "1", "2"};
        TTHFLTSScale hScale3 = new TTHFLTSScale(scale3);

        String[] scale5 = {"0", "1", "2", "3", "4"};
        TTHFLTSScale hScale5 = new TTHFLTSScale(scale5);

        HashMap<Integer, TTHFLTSScale> scaleStore = new HashMap<>();
        scaleStore.put(scale9.length, hScale9);
        scaleStore.put(scale3.length, hScale3);
        scaleStore.put(scale5.length, hScale5);

        TTNormalizedTranslator.setScaleStore(scaleStore);

        mtwaOperator = new TT2HFLTSMTWAOperator();

        int numberExp = 4;
        weights = new float[numberExp];
        for (int i = 0; i < numberExp; i++) {
            weights[i] = 1f / numberExp; // they are of equal importance currently
        }
    }

    @Test
    public void testCalculate1() throws Exception {
        ArrayList<TTTuple> alt1 = new ArrayList<>();
        alt1.add(new TTTuple("4", 9, 0, 4));
        alt1.add(new TTTuple("3", 5, 0, 3));
        alt1.add(new TTTuple("1", 3, 0, 1));
        alt1.add(new TTTuple("4", 9, 0, 4));

        TTTuple res = mtwaOperator.calculate(alt1, weights, 9);
        TTTuple expectedRes = new TTTuple("5", 9, -0.5f, 5);
        assertEquals(expectedRes, res);
    }

    @Test
    public void testCalculate2() throws Exception {
        ArrayList<TTTuple> alt2 = new ArrayList<>();
        alt2.add(new TTTuple("6", 9, 0, 6));
        alt2.add(new TTTuple("4", 5, 0, 4));
        alt2.add(new TTTuple("2", 3, 0, 2));
        alt2.add(new TTTuple("5", 9, 0, 5));

        TTTuple res = mtwaOperator.calculate(alt2, weights, 9);
        TTTuple expectedRes = new TTTuple("7", 9, -0.25f, 7);
        assertEquals(expectedRes, res);
    }

    @Test
    public void testCalculate3() throws Exception {
        ArrayList<TTTuple> alt3 = new ArrayList<>();
        alt3.add(new TTTuple("3", 9, 0, 3));
        alt3.add(new TTTuple("3", 5, 0, 3));
        alt3.add(new TTTuple("2", 3, 0, 2));
        alt3.add(new TTTuple("3", 9, 0, 3));

        TTTuple res = mtwaOperator.calculate(alt3, weights, 9);
        TTTuple expectedRes = new TTTuple("5", 9, 0f, 5);
        assertEquals(expectedRes, res);
    }

    @Test
    public void testCalculate4() throws Exception {
        ArrayList<TTTuple> alt4 = new ArrayList<>();
        alt4.add(new TTTuple("5", 9, 0, 5));
        alt4.add(new TTTuple("3", 5, 0, 3));
        alt4.add(new TTTuple("1", 3, 0, 1));
        alt4.add(new TTTuple("5", 9, 0, 5));

        TTTuple res = mtwaOperator.calculate(alt4, weights, 9);
        TTTuple expectedRes = new TTTuple("5", 9, 0f, 5);
        assertEquals(expectedRes, res);
    }
}