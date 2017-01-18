package dss.lingvo.t2hflts;

import dss.lingvo.hflts.TTHFLTS;
import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by demidovs on 18.01.17.
 */
public class TT2HFLTSMHTWAOperatorTest {

    private static TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperator;

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

        tt2HFLTSMHTWAOperator = new TT2HFLTSMHTWAOperator();
    }

    @Test
    public void testCalculate() throws Exception {
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

        List<TT2HFLTS> setsOrdered = TTUtils.sortTT2HFLTS(sets,true);

        float [] hWeights = {0.25f, 0.5f, 0.25f};

        TT2HFLTS aggregationRes = tt2HFLTSMHTWAOperator.calculate(setsOrdered, hWeights, 7);

        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("3",7,-0.25f,3));
        tmp.add(new TTTuple("3",7,0f,3));
        tmp.add(new TTTuple("3",7,0.125f,3));
        tmp.add(new TTTuple("3",7,0.25f,3));
        tmp.add(new TTTuple("3",7,0.375f,3));
        tmp.add(new TTTuple("4",7,-0.375f,4));

        TT2HFLTS expectedTT2HFLTS = new TT2HFLTS(tmp);

        assertEquals(expectedTT2HFLTS,aggregationRes);
    }
}