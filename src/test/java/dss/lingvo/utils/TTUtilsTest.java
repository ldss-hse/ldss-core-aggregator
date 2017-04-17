package dss.lingvo.utils;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TTUtilsTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

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
    }

    @Test
    public void testCalculate1() throws Exception {
        ArrayList<TTTuple> alt1 = new ArrayList<>();
        alt1.add(new TTTuple("5", 9, -0.5f, 5));
        alt1.add(new TTTuple("7", 9, -0.25f, 7));
        alt1.add(new TTTuple("5", 9, 0f, 5));
        alt1.add(new TTTuple("5", 9, 0f, 5));

        List<TTTuple> res = TTUtils.sortTuples(alt1, true);

        ArrayList<TTTuple> expectedRes = new ArrayList<>();
        expectedRes.add(new TTTuple("7", 9, -0.25f, 7));
        expectedRes.add(new TTTuple("5", 9, 0f, 5));
        expectedRes.add(new TTTuple("5", 9, 0f, 5));
        expectedRes.add(new TTTuple("5", 9, -0.5f, 5));

        assertEquals(expectedRes, res);
    }

    @Test
    public void testGetInstance() throws Exception {
        TTUtils v = TTUtils.getInstance();

        assertNotNull(v);
    }

    @Test
    public void testInfo() throws Exception {
        TTUtils v = TTUtils.getInstance();
        v.info("hello");

        assertEquals("hello\n", outContent.toString());
    }

    @Test
    public void testSortTT2HFLTS1() throws Exception {
        ArrayList<TTTuple> est1 = new ArrayList<>();
        est1.add(new TTTuple("2", 7, 0, 2));
        est1.add(new TTTuple("3", 7, 0, 3));
        est1.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS tt2HFLTS1 = new TT2HFLTS(est1);

        ArrayList<TTTuple> est2 = new ArrayList<>();
        est2.add(new TTTuple("2", 5, 0, 2));
        est2.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS tt2HFLTS2 = new TT2HFLTS(est2);

        ArrayList<TTTuple> est3 = new ArrayList<>();
        est3.add(new TTTuple("1", 3, 0, 1));

        TT2HFLTS tt2HFLTS3 = new TT2HFLTS(est3);

        ArrayList<TT2HFLTS> sets = new ArrayList<>();

        sets.add(tt2HFLTS1);
        sets.add(tt2HFLTS2);
        sets.add(tt2HFLTS3);

        List<TT2HFLTS> res = TTUtils.sortTT2HFLTS(sets, true);

        ArrayList<TT2HFLTS> expectedSet = new ArrayList<>();
        expectedSet.add(tt2HFLTS2);
        expectedSet.add(tt2HFLTS3);
        expectedSet.add(tt2HFLTS1);

        assertEquals(expectedSet, res);
    }

    @Test
    public void testSortTT2HFLTS2() throws Exception {
        ArrayList<TTTuple> est1 = new ArrayList<>();
        est1.add(new TTTuple("2", 7, 0, 2));
        est1.add(new TTTuple("3", 7, 0, 3));
        est1.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS tt2HFLTS1 = new TT2HFLTS(est1);

        ArrayList<TTTuple> est2 = new ArrayList<>();
        est2.add(new TTTuple("2", 5, 0, 2));
        est2.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS tt2HFLTS2 = new TT2HFLTS(est2);

        ArrayList<TTTuple> est3 = new ArrayList<>();
        est3.add(new TTTuple("1", 3, 0, 1));

        TT2HFLTS tt2HFLTS3 = new TT2HFLTS(est3);

        ArrayList<TT2HFLTS> sets = new ArrayList<>();

        sets.add(tt2HFLTS1);
        sets.add(tt2HFLTS2);
        sets.add(tt2HFLTS3);

        List<TT2HFLTS> res = TTUtils.sortTT2HFLTS(sets, false);

        ArrayList<TT2HFLTS> expectedSet = new ArrayList<>();
        expectedSet.add(tt2HFLTS1);
        expectedSet.add(tt2HFLTS3);
        expectedSet.add(tt2HFLTS2);

        assertEquals(expectedSet, res);
    }

    @Test
    public void testSortTT2HFLTS3() throws Exception {
        ArrayList<TTTuple> est1 = new ArrayList<>();
        est1.add(new TTTuple("2", 7, 0, 2));
        est1.add(new TTTuple("3", 7, 0, 3));
        est1.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS tt2HFLTS1 = new TT2HFLTS(est1);
        TT2HFLTS spy1 = spy(tt2HFLTS1);

        ArrayList<TTTuple> est2 = new ArrayList<>();
        est2.add(new TTTuple("2", 5, 0, 2));
        est2.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS tt2HFLTS2 = new TT2HFLTS(est2);
        TT2HFLTS spy2 = spy(tt2HFLTS2);

        when(spy1.getScore()).thenReturn(0.5f);
        when(spy1.getVariance()).thenReturn(0.5f);

        when(spy2.getScore()).thenReturn(0.5f);
        when(spy2.getVariance()).thenReturn(0.5f);

        ArrayList<TT2HFLTS> sets = new ArrayList<>();

        sets.add(spy1);
        sets.add(spy2);

        List<TT2HFLTS> res = TTUtils.sortTT2HFLTS(sets, false);

        ArrayList<TT2HFLTS> expectedSet = new ArrayList<>();
        expectedSet.add(spy1);
        expectedSet.add(spy2);

        assertEquals(expectedSet, res);
    }

    @Test
    public void testWeightsDistribution() throws Exception {
        float[] expDistr = {0.8f, 0.2f};
        float[] resVector = TTUtils.calculateWeightsVector(expDistr, 4);
        float[] expV = {0.8f, 0.16f, 0.03199997f, 0.008000016f};
        float sum = 0f;
        for (int i = 0; i < resVector.length; i++) {
            sum += resVector[i];
        }

        assertEquals(resVector.length, 4);
        assertTrue(Arrays.equals(expV, resVector));
        assertEquals(1, sum, TTConstants.FLOAT_PRECISION_DELTA);
    }
}