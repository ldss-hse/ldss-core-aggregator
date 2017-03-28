package dss.lingvo.t2hflts;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by demidovs on 28.03.17.
 */
public class TT2HFLTSMHTWOWAOperatorTest {

    private static TT2HFLTSMHTWOWAOperator tt2HFLTSMHTWOWAOperator;
    private static ArrayList<ArrayList<TT2HFLTS>> aggEstAll;

    @BeforeClass
    public static void runOnceBeforeClass() {
        String[] scale9 = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        TTHFLTSScale hScale9 = new TTHFLTSScale(scale9);

        String[] scale3 = {"0", "1", "2"};
        TTHFLTSScale hScale3 = new TTHFLTSScale(scale3);

        String[] scale5 = {"0", "1", "2", "3", "4"};
        TTHFLTSScale hScale5 = new TTHFLTSScale(scale5);

        String[] scale7 = {"0", "1", "2", "3", "4", "5", "6"};
        TTHFLTSScale hScale7 = new TTHFLTSScale(scale7);

        HashMap<Integer, TTHFLTSScale> scaleStore = new HashMap<>();
        scaleStore.put(scale9.length, hScale9);
        scaleStore.put(scale3.length, hScale3);
        scaleStore.put(scale5.length, hScale5);
        scaleStore.put(scale7.length, hScale7);

        TTNormalizedTranslator.setScaleStore(scaleStore);

        tt2HFLTSMHTWOWAOperator = new TT2HFLTSMHTWOWAOperator();

        aggEstAll = new ArrayList<>();

        ArrayList<TT2HFLTS> exp1 = new ArrayList<>();
        ArrayList<TTTuple> exp1alt1 = new ArrayList<>();
        exp1alt1.add(new TTTuple("4", 7, -0.3f, 4));
        exp1alt1.add(new TTTuple("4", 7, 0.2f, 4));

        ArrayList<TTTuple> exp1alt2 = new ArrayList<>();
        exp1alt2.add(new TTTuple("3", 7, 0.1f, 3));
        exp1alt2.add(new TTTuple("3", 7, 0.4f, 3));

        ArrayList<TTTuple> exp1alt3 = new ArrayList<>();
        exp1alt3.add(new TTTuple("3", 7, -0.4f, 3));
        exp1alt3.add(new TTTuple("3", 7, -0.2f, 3));

        ArrayList<TTTuple> exp1alt4 = new ArrayList<>();
        exp1alt4.add(new TTTuple("4", 7, 0.3f, 4));
        exp1alt4.add(new TTTuple("5", 7, -0.2f, 5));

        ArrayList<TTTuple> exp1alt5 = new ArrayList<>();
        exp1alt5.add(new TTTuple("3", 7, -0.2f, 3));
        exp1alt5.add(new TTTuple("3", 7, 0f, 3));

        exp1.add(new TT2HFLTS(exp1alt1));
        exp1.add(new TT2HFLTS(exp1alt2));
        exp1.add(new TT2HFLTS(exp1alt3));
        exp1.add(new TT2HFLTS(exp1alt4));
        exp1.add(new TT2HFLTS(exp1alt5));

        ArrayList<TT2HFLTS> exp2 = new ArrayList<>();
        ArrayList<TTTuple> exp2alt1 = new ArrayList<>();
        exp2alt1.add(new TTTuple("4", 7, -0.3f, 4));
        exp2alt1.add(new TTTuple("4", 7, -0.1f, 4));

        ArrayList<TTTuple> exp2alt2 = new ArrayList<>();
        exp2alt2.add(new TTTuple("3", 7, 0.1f, 3));
        exp2alt2.add(new TTTuple("3", 7, 0.4f, 3));
        exp2alt2.add(new TTTuple("4", 7, -0.4f, 4));
        exp2alt2.add(new TTTuple("4", 7, 0.1f, 4));

        ArrayList<TTTuple> exp2alt3 = new ArrayList<>();
        exp2alt3.add(new TTTuple("3", 7, -0.5f, 3));
        exp2alt3.add(new TTTuple("3", 7, -0.3f, 3));
        exp2alt3.add(new TTTuple("3", 7, -0.2f, 3));
        exp2alt3.add(new TTTuple("3", 7, 0f, 3));

        ArrayList<TTTuple> exp2alt4 = new ArrayList<>();
        exp2alt4.add(new TTTuple("4", 7, 0.4f, 4));
        exp2alt4.add(new TTTuple("5", 7, -0.3f, 5));

        ArrayList<TTTuple> exp2alt5 = new ArrayList<>();
        exp2alt5.add(new TTTuple("3", 7, 0.1f, 3));

        exp2.add(new TT2HFLTS(exp2alt1));
        exp2.add(new TT2HFLTS(exp2alt2));
        exp2.add(new TT2HFLTS(exp2alt3));
        exp2.add(new TT2HFLTS(exp2alt4));
        exp2.add(new TT2HFLTS(exp2alt5));

        ArrayList<TT2HFLTS> exp3 = new ArrayList<>();
        ArrayList<TTTuple> exp3alt1 = new ArrayList<>();
        exp3alt1.add(new TTTuple("3", 5, 0f, 3));
        exp3alt1.add(new TTTuple("3", 5, 0.2f, 3));

        ArrayList<TTTuple> exp3alt2 = new ArrayList<>();
        exp3alt2.add(new TTTuple("4", 5, -0.4f, 4));

        ArrayList<TTTuple> exp3alt3 = new ArrayList<>();
        exp3alt3.add(new TTTuple("2", 5, 0.3f, 2));
        exp3alt3.add(new TTTuple("3", 5, -0.5f, 3));

        ArrayList<TTTuple> exp3alt4 = new ArrayList<>();
        exp3alt4.add(new TTTuple("3", 5, -0.2f, 3));
        exp3alt4.add(new TTTuple("3", 5, 0.3f, 3));

        ArrayList<TTTuple> exp3alt5 = new ArrayList<>();
        exp3alt4.add(new TTTuple("2", 5, -0.2f, 2));
        exp3alt4.add(new TTTuple("2", 5, 0.1f, 2));

        exp3.add(new TT2HFLTS(exp3alt1));
        exp3.add(new TT2HFLTS(exp3alt2));
        exp3.add(new TT2HFLTS(exp3alt3));
        exp3.add(new TT2HFLTS(exp3alt4));
        exp3.add(new TT2HFLTS(exp3alt5));

        aggEstAll.add(exp1);
        aggEstAll.add(exp2);
        aggEstAll.add(exp3);

    }

    @Test
    public void testCalculate() throws Exception {

    }

    @Test
    public void testCalculateAlternativeWeights() throws Exception {
        int numAlt = 5;
        int numExp = 3;
        float[] w = {0f, 1f / 3, 2f / 3}; // weighting of alternatives
        float[] p = {0.3f, 0.4f, 0.3f}; // weighting of experts
        ArrayList<float[]> altOverall = tt2HFLTSMHTWOWAOperator.calculateAlternativeWeights(numAlt, numExp, p, w, aggEstAll);
        ArrayList<float[]> expResList = new ArrayList<>();
        float[] alt1 = {0f, 4/15f, 11/15f};
        float[] alt2 = {0f, 2/5f, 3/5f};
        float[] alt3 = {0f, 2/5f, 3/5f};
        float[] alt4 = {0f, 2/5f, 3/5f};
        float[] alt5 = {1/15f, 1/3f, 3/5f};
        expResList.add(alt1);
        expResList.add(alt2);
        expResList.add(alt3);
        expResList.add(alt4);
        expResList.add(alt5);
        for (int i = 0; i < expResList.size(); i++){
            assertArrayEquals(expResList.get(i), altOverall.get(i), 0.0001f);
        }
    }
}