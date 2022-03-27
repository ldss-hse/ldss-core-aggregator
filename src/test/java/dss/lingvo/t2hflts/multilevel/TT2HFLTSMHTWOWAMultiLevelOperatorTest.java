package dss.lingvo.t2hflts.multilevel;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.utils.TTJSONUtils;
import dss.lingvo.utils.TTUtils;
import dss.lingvo.utils.models.input.multilevel.TTJSONMultiLevelInputModel;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TT2HFLTSMHTWOWAMultiLevelOperatorTest {
    private static TTJSONMultiLevelInputModel model;
    private static List<ArrayList<ArrayList<TT2HFLTS>>> all;
    private static int targetScaleSize;
    private static TT2HFLTSMHTWOWAMultiLevelOperator tt2HFLTSMHTWOWAMultiLevelOperator;
    private static List<ArrayList<ArrayList<TT2HFLTS>>> allByLevel;
    private static List<ArrayList<TT2HFLTS>> altToLevel;
    private static List<ArrayList<ArrayList<TT2HFLTS>>> allByExpert;
    private static List<TT2HFLTS> altVec;

    @BeforeClass
    public static void runOnceBeforeClass() throws IOException {
        TTJSONUtils ttjsonReader = TTJSONUtils.getInstance();
        File multilevelDescriptionFile = new File("description_multilevel.json");

        model = ttjsonReader.readJSONMultiLevelDescription(multilevelDescriptionFile, true);
        TTNormalizedTranslator.registerScalesBatch(model.getScales());
        all = TTUtils.getAllEstimationsFromMultiLevelJSONModel(model, 7);
        targetScaleSize = 7;
        tt2HFLTSMHTWOWAMultiLevelOperator = new TT2HFLTSMHTWOWAMultiLevelOperator();

        allByLevel = tt2HFLTSMHTWOWAMultiLevelOperator
                .aggregateByAbstractionLevel(model.getCriteria(),
                        model.getAbstractionLevels(),
                        all,
                        targetScaleSize);

        allByExpert = tt2HFLTSMHTWOWAMultiLevelOperator
                .transposeByAbstractionLevel(model.getAbstractionLevels().size(),
                        model.getAlternatives().size(),
                        model.getExperts().size(),
                        allByLevel);

        float []a = {0.8f, 0.2f};
        altToLevel = tt2HFLTSMHTWOWAMultiLevelOperator
                .aggregateByExpert(model.getAbstractionLevels().size(),
                        model.getAlternatives().size(),
                        targetScaleSize,
                        allByExpert,
                        a);
        altVec = tt2HFLTSMHTWOWAMultiLevelOperator
                .aggregateFinalAltEst(targetScaleSize, altToLevel);
    }

    @Test
    public void testAggregateByAbstractionLevel(){
        assertEquals(7, allByLevel.size());
        assertEquals(26, allByLevel.get(0).size());
        assertEquals(8, allByLevel.get(0).get(0).size());
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("p",7,0.17411518f,1));

        TT2HFLTS myHFLTS1 = new TT2HFLTS(tmp);
        assertEquals(myHFLTS1, allByLevel.get(0).get(0).get(0));
    }

    @Test
    public void testAggregateByExpert(){
        // Step 1. Aggregate by abstraction level

        assertEquals(26, altToLevel.size());
        assertEquals(8, altToLevel.get(0).size());
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("p",7,0.17411518f,1));

        TT2HFLTS myHFLTS1 = new TT2HFLTS(tmp);
        assertEquals(myHFLTS1, allByLevel.get(0).get(0).get(0));
    }

    @Test
    public void testTransposeByAbstractionLevel(){
        // Step 1. Aggregate by abstraction level

        assertEquals(8, allByExpert.size());
        assertEquals(26, allByExpert.get(0).size());
        assertEquals(7, allByExpert.get(0).get(0).size());
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("p",7,0.17411518f,1));

        TT2HFLTS myHFLTS1 = new TT2HFLTS(tmp);
        assertEquals(myHFLTS1, allByExpert.get(0).get(0).get(0));
    }

    @Test
    public void testAggregateFinalAltEst(){
        // Step 1. Aggregate by abstraction level

        assertEquals(26, altVec.size());
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("p",7,0.17411518f,1));

        TT2HFLTS myHFLTS1 = new TT2HFLTS(tmp);
        assertEquals(myHFLTS1, allByExpert.get(0).get(0).get(0));
    }
}