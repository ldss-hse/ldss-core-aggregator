package dss.lingvo.utils;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTSMTWAOperator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class TTUtilsTest {
    @BeforeClass
    public static void runOnceBeforeClass(){
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
        alt1.add(new TTTuple("5",9,-0.5f,5));
        alt1.add(new TTTuple("7",9,-0.25f,7));
        alt1.add(new TTTuple("5",9,0f,5));
        alt1.add(new TTTuple("5",9,0f,5));

        List<TTTuple> res = TTUtils.sortTuples(alt1);

        ArrayList<TTTuple> expectedRes = new ArrayList<>();
        expectedRes.add(new TTTuple("7",9,-0.25f,7));
        expectedRes.add(new TTTuple("5",9,0f,5));
        expectedRes.add(new TTTuple("5",9,0f,5));
        expectedRes.add(new TTTuple("5",9,-0.5f,5));

        assertEquals(expectedRes, res);
    }
}