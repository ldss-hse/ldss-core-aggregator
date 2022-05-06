package dss.lingvo.samples;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.t2hflts.TT2HFLTSMHTWAOperator;
import dss.lingvo.t2hflts.TT2HFLTSMHTWOWAOperator;
import dss.lingvo.t2hflts.multilevel.TT2HFLTSMHTWOWAMultiLevelOperator;
import dss.lingvo.utils.TTJSONUtils;
import dss.lingvo.utils.TTUtils;
import dss.lingvo.utils.models.input.TTAlternativeModel;
import dss.lingvo.utils.models.input.multilevel.TTJSONMultiLevelInputModel;
import dss.lingvo.utils.models.input.singlelevel.TTJSONInputModel;
import dss.lingvo.utils.models.output.TTJSONOutputModel;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TT2HFLTSCoordinator {
    private final TTJSONUtils ttjsonReader = TTJSONUtils.getInstance();

    private void processSimpleCase(File outputDirectory) throws IOException {
        TTJSONInputModel ttjsonModel = ttjsonReader.readJSONDescription("description_from_article.json");

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
        List<TT2HFLTS> altOverall = tt2HFLTSMHTWOWAOperator.calculate(numAlternatives, numExperts, p, w, aggEstAll, 7);

        TTJSONOutputModel res = TTUtils.prepareAllResultsForJSON(altOverall, ttjsonModel, 7);
        // now output results
        Path outputJSONFilePath = Paths.get(outputDirectory.toString(), "result_simple.json");
        TTJSONUtils.getInstance().writeResultToJSON(outputJSONFilePath, res);
    }

    private void processNumericSample() {
        // now how to work with numbers
        final float numeric_assessment = 0.78f;
        List<Float> fSet = TTNormalizedTranslator.getInstance().getFuzzySetForNumericEstimation(numeric_assessment, 5);
        float resTranslation = TTNormalizedTranslator.getInstance().getTranslationFromFuzzySet(fSet);
        TTTuple resTuple = TTNormalizedTranslator.getInstance().getTTupleForNumericTranslation(resTranslation, 5);
        System.out.printf("Translating %f to 2-tuple: %s\n", numeric_assessment, resTuple.toString());
    }

    private void processWeightsGenerationSample() {
        float[] expDistr = {0.8f, 0.2f};
        float[] resVector = TTUtils.calculateWeightsVector(expDistr, 4);
        System.out.println(Arrays.toString(resVector));

        float sum = 0f;
        for (float v : resVector) {
            sum += v;
        }
        System.out.printf("Sum of weights should be 1. Actual sum is: %f", sum);
    }

    private void processMHTWASample() {
        float[] ww = {0.33f, 0.33f, 0.33f};
        List<TTTuple> l = new ArrayList<>();
        l.add(new TTTuple("p", 9, 0f, 3));
        TT2HFLTS el1 = new TT2HFLTS(l);
        List<TTTuple> l2 = new ArrayList<>();
        l2.add(new TTTuple("mp", 9, 0f, 4));
        TT2HFLTS el2 = new TT2HFLTS(l2);
        List<TTTuple> l3 = new ArrayList<>();
        l3.add(new TTTuple("mg", 9, 0f, 6));
        TT2HFLTS el3 = new TT2HFLTS(l3);
        List<TT2HFLTS> ll = new ArrayList<>();
        ll.add(el1);
        ll.add(el2);
        ll.add(el3);
        TT2HFLTSMHTWAOperator op = new TT2HFLTSMHTWAOperator();
        TT2HFLTS rr = op.calculate(ll, ww, 9);
    }

    private void processMultiLevelAdvancedSample(File inputFile, File outputDirectory) throws IOException {
        int targetScaleSize = 7;

        TTJSONMultiLevelInputModel model = ttjsonReader.readJSONMultiLevelDescription(inputFile, false);

        TTNormalizedTranslator.registerScalesBatch(model.getScales());

        List<ArrayList<ArrayList<TT2HFLTS>>> all = TTUtils.getAllEstimationsFromMultiLevelJSONModel(model,
                targetScaleSize);

        // Step 1. Aggregate by abstraction level
        TT2HFLTSMHTWOWAMultiLevelOperator tt2HFLTSMHTWOWAMultiLevelOperator = new TT2HFLTSMHTWOWAMultiLevelOperator();

        List<ArrayList<ArrayList<TT2HFLTS>>> allByLevel = tt2HFLTSMHTWOWAMultiLevelOperator.aggregateByAbstractionLevel(
                model.getCriteria(), model.getAbstractionLevels(), all, targetScaleSize,
                model.getCriteriaWeightsPerGroup());

        List<ArrayList<ArrayList<TT2HFLTS>>> allByExpert =
                tt2HFLTSMHTWOWAMultiLevelOperator.transposeByAbstractionLevel(model.getAbstractionLevels().size(),
                        model.getAlternatives().size(), model.getExperts().size(), allByLevel);

        int numExperts = model.getExpertWeightsRule().values().size();
        float[] a = new float[numExperts];
        float curMax = 0f;
        for (Map.Entry<String, Float> e : model.getExpertWeightsRule().entrySet()) {
            if (e.getKey().equals("1")) {
                curMax = e.getValue();
                break;
            }
        }
        a[0] = curMax;
        if (numExperts > 1) {
            a[1] = 1 - curMax;
        }
        List<ArrayList<TT2HFLTS>> altToLevel = tt2HFLTSMHTWOWAMultiLevelOperator.aggregateByExpert(
                model.getAbstractionLevels().size(), model.getAlternatives().size(), targetScaleSize, allByExpert, a);

        List<TT2HFLTS> altVec = tt2HFLTSMHTWOWAMultiLevelOperator.aggregateFinalAltEst(targetScaleSize, altToLevel);

        // all below is just processing

        // saving to file
        TTJSONOutputModel res = TTUtils.prepareAllResultsForJSON(altVec, model, targetScaleSize);
        Path outputJSONFilePath = Paths.get(outputDirectory.toString(), "result.json");
        TTJSONUtils.getInstance().writeResultToJSON(outputJSONFilePath, res);


        // printing to console
        List<Pair<String, TT2HFLTS>> resZippedVec = IntStream.range(0, altVec.size()).mapToObj(i -> new Pair<>(model.getAlternatives().get(i).getAlternativeID(), altVec.get(i))).collect(Collectors.toList());

        resZippedVec.sort(Collections.reverseOrder((o1, o2) -> TTUtils.compareTT2HFLTS(o1.getValue(), o2.getValue())));

        System.out.println("\n\n\n[MULTILEVEL] [REPORT] Aggregation results");
        for (Pair<String, TT2HFLTS> stringTT2HFLTSPair : resZippedVec) {
            TTAlternativeModel altInstance = model.getAlternatives().stream().filter((TTAlternativeModel ttAlternativeModel) -> ttAlternativeModel.getAlternativeID().equals(stringTT2HFLTSPair.getKey())).findFirst().orElse(null);
            System.out.println(stringTT2HFLTSPair.getKey() + ' ' + altInstance.getAlternativeName());
        }
    }

    public void go(File inputFile, File outputDirectory) throws IOException {
//        processSimpleCase(outputDirectory);
//        processNumericSample();
        processMultiLevelAdvancedSample(inputFile, outputDirectory);
    }
}
