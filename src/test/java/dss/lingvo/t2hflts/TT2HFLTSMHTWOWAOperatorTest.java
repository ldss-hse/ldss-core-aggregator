package dss.lingvo.t2hflts;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTJSONReader;
import dss.lingvo.utils.TTUtils;
import dss.lingvo.utils.models.TTJSONModel;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by demidovs on 28.03.17.
 */
public class TT2HFLTSMHTWOWAOperatorTest {

    private static TT2HFLTSMHTWOWAOperator tt2HFLTSMHTWOWAOperator;
    private static List<ArrayList<TT2HFLTS>> aggEstAll;

    @BeforeClass
    public static void runOnceBeforeClass() {
        TTJSONReader ttjsonReader = TTJSONReader.getInstance();
        TTJSONModel ttjsonModel = null;
        try {
            ttjsonModel = ttjsonReader.readJSONDescription("description.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        tt2HFLTSMHTWOWAOperator = new TT2HFLTSMHTWOWAOperator();

        List<ArrayList<ArrayList<TT2HFLTS>>> expEstimationsAll = TTUtils.getAllEstimationsFromJSONModel(ttjsonModel);

        float[] criteriaWeights = {0.5f, 0.3f, 0.2f};

        aggEstAll = TTUtils.aggregateIndividualEstimations(expEstimationsAll, criteriaWeights);

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