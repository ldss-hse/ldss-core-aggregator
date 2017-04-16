package dss.lingvo.t2hflts;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTJSONUtils;
import dss.lingvo.utils.TTUtils;
import dss.lingvo.utils.models.input.TTJSONInputModel;
import dss.lingvo.utils.models.output.TTJSONOutputModel;

import java.io.IOException;
import java.util.*;

public class TT2HFLTSCoordinator {
    private TTUtils log = TTUtils.getInstance();

    public void go() throws IOException {
        TTJSONUtils ttjsonReader = TTJSONUtils.getInstance();
        TTJSONInputModel ttjsonModel = null;
        ttjsonModel = ttjsonReader.readJSONDescription("description_from_article.json");

        if (ttjsonModel == null) {
            return;
        }

        // now we need to create all instances
        // 1. register all scales
        TTNormalizedTranslator.registerScalesBatch(ttjsonModel.getScales());


        // MHTWOWA example

        // 1. Gather feedback and translate directly to the TT2HFLTS (per each expert)

        // 2. read estimates from file
        int numExperts = ttjsonModel.getExperts().size();
        int numAlternatives = ttjsonModel.getAlternatives().size();

        List<ArrayList<ArrayList<TT2HFLTS>>> expEstimationsAll = TTUtils.getAllEstimationsFromJSONModel(ttjsonModel);

        // 2. We need to aggregate value for each alternative for each expert (so collapse
        // all estimates by criteria)
        float[] criteriaWeights = {0.5f, 0.3f, 0.2f};

        List<ArrayList<TT2HFLTS>> aggEstAll = TTUtils.aggregateIndividualEstimations(expEstimationsAll, criteriaWeights);

        float[] w = {0f, 1f / 3, 2f / 3}; // weighting of alternatives
        float[] p = {0.3f, 0.4f, 0.3f}; // weighting of experts

        // now need to make the calculation for every alternative
        TT2HFLTSMHTWOWAOperator tt2HFLTSMHTWOWAOperator = new TT2HFLTSMHTWOWAOperator();
        List<TT2HFLTS> altOverall = tt2HFLTSMHTWOWAOperator.calculate(numAlternatives,
                numExperts, p, w, aggEstAll, 7);

        TTJSONOutputModel res = TTUtils.prepareAllResultsForJSON(altOverall, ttjsonModel, 7);
        // now output results
        TTJSONUtils.getInstance().writeResultToJSON("build/resources/result.json", res);

        // now how to work with numbers
        float estimation = 0.78f;
        int targetScaleSize = 7;

        List<Float> fSet = TTNormalizedTranslator.getInstance().getFuzzySetForNumericEstimation(0.78f, 5);
        float resTranslation = TTNormalizedTranslator.getInstance().getTranslationFromFuzzySet(fSet);
        TTTuple resTuple = TTNormalizedTranslator.getInstance().getTTupleForNumericTranslation(resTranslation, 5);
        System.out.println(resTuple);

    }
}
