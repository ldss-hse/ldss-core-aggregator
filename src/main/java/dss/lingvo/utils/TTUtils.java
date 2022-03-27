package dss.lingvo.utils;

import dss.lingvo.hflts.TTHFLTS;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.t2hflts.TT2HFLTSMHTWAOperator;
import dss.lingvo.utils.models.input.*;
import dss.lingvo.utils.models.input.common.TTCommonInputModel;
import dss.lingvo.utils.models.input.multilevel.TTJSONMultiLevelInputModel;
import dss.lingvo.utils.models.input.singlelevel.TTJSONInputModel;
import dss.lingvo.utils.models.output.TTAlternativeEstimationModel;
import dss.lingvo.utils.models.output.TTJSONOutputModel;

import java.util.*;

public class TTUtils {
    private static TTUtils myInstance = new TTUtils();

    private TTUtils() {
    }

    public static TTUtils getInstance() {
        return myInstance;
    }

    public void info(Object obj) {
        System.out.println(obj.toString());
    }

    public static List<TTTuple> sortTuples(List<TTTuple> ttTupleList, boolean isReversed) {
        ArrayList<TTTuple> res = new ArrayList<>(ttTupleList);
        if (isReversed) {
            Collections.sort(res, Collections.reverseOrder(TTUtils::compareTTTuples));
        } else {
            Collections.sort(res, TTUtils::compareTTTuples);
        }
        return res;
    }

    private static int compareTTTuples(TTTuple tt1, TTTuple tt2) {
        float tt1Translation = TTNormalizedTranslator.getInstance().getTranslationFrom2Tuple(tt1);
        float tt2Translation = TTNormalizedTranslator.getInstance().getTranslationFrom2Tuple(tt2);
        if (tt1Translation < tt2Translation) {
            return -1;
        } else if (Math.abs(tt2Translation - tt1Translation) < TTConstants.FLOAT_PRECISION_DELTA) {
            return 0;
        }
        return 1;
    }

    public static int compareTT2HFLTS(TT2HFLTS tt1, TT2HFLTS tt2) {
        float tt1Score = tt1.getScore();
        float tt2Score = tt2.getScore();
        if (tt1Score < tt2Score) {
            return -1;
        } else if (Math.abs(tt1Score - tt2Score) < TTConstants.FLOAT_PRECISION_DELTA) {
            float tt1Variance = tt1.getVariance();
            float tt2Variance = tt2.getVariance();
            if (tt1Variance < tt2Variance) {
                return 1;
            } else if (Math.abs(tt1Variance - tt2Variance) < TTConstants.FLOAT_PRECISION_DELTA) {
                return 0;
            }
            return -1;
        }
        return 1;
    }

    public static List<TT2HFLTS> sortTT2HFLTS(List<TT2HFLTS> ttTupleList, boolean isReversed) {
        ArrayList<TT2HFLTS> res = new ArrayList<>(ttTupleList);
        if (isReversed) {
            Collections.sort(res, Collections.reverseOrder(TTUtils::compareTT2HFLTS));
        } else {
            Collections.sort(res, TTUtils::compareTT2HFLTS);
        }
        return res;
    }

    @FunctionalInterface
    public interface PiecewiseLinearLambda {
        public float compute(int i, float[] wVec, float x);
    }

    public static List<ArrayList<ArrayList<TT2HFLTS>>> getAllEstimationsFromJSONModel(TTJSONInputModel ttjsonModel) {
        Map<String, List<TTExpertEstimationsModel>> fileEstimations = ttjsonModel.getEstimations();

        String integerRegex = "[\\D]";
        Map<String, List<TTExpertEstimationsModel>> treeMap = new TreeMap<>(
                (Comparator<String>) (o1, o2) -> {
                    int num1 = Integer.parseInt(o1.replaceAll(integerRegex, ""));
                    int num2 = Integer.parseInt(o2.replaceAll(integerRegex, ""));
                    return num1 - num2;
                }
        );

        treeMap.putAll(fileEstimations);

        ArrayList<ArrayList<ArrayList<TT2HFLTS>>> expEstimationsAll = new ArrayList<>();

        for (Map.Entry<String, List<TTExpertEstimationsModel>> el : treeMap.entrySet()) {
            ArrayList<ArrayList<TT2HFLTS>> expAll = new ArrayList<>();
            el.getValue().sort((Comparator<TTExpertEstimationsModel>) (o1, o2) -> {
                int num1 = Integer.parseInt(o1.getAlternativeID().replaceAll(integerRegex, ""));
                int num2 = Integer.parseInt(o2.getAlternativeID().replaceAll(integerRegex, ""));
                return num1 - num2;
            });
            // loop though sorted by alternative
            for (TTExpertEstimationsModel model : el.getValue()) {
                model.getCriteria2Estimation().sort((Comparator<TTCriteriaEstimationsModel>) (o1, o2) -> {
                    int num1 = Integer.parseInt(o1.getCriteriaID().replaceAll(integerRegex, ""));
                    int num2 = Integer.parseInt(o2.getCriteriaID().replaceAll(integerRegex, ""));
                    return num1 - num2;
                });
                ArrayList<TT2HFLTS> expToAltToCrit = new ArrayList<>();
                // loop though sorted by criteria
                for (TTCriteriaEstimationsModel criterion : model.getCriteria2Estimation()) {
                    expToAltToCrit.add(transformToTTHFLTS(ttjsonModel.getScales(),
                            criterion.getScaleID(),
                            criterion.getEstimation()));
                }
                expAll.add(expToAltToCrit);
            }
            expEstimationsAll.add(expAll);
        }
        return expEstimationsAll;
    }

    private static TT2HFLTS transformToTTHFLTS(List<TTScaleModel> scales, String scaleID, List<String> estimation) {
        ArrayList<TTTuple> expToAltToCritTTuple = new ArrayList<>();
        TTScaleModel scale = scales
                .stream()
                .filter(x -> x.getScaleID().equals(scaleID))
                .findFirst()
                .orElse(null);
        int scaleSize = scale.getLabels().size();
        for (String label : estimation) {
            expToAltToCritTTuple.add(new TTTuple(label, scaleSize, 0, scale.getLabels().indexOf(label)));
        }
        return new TT2HFLTS(expToAltToCritTTuple);
    }

    public static List<ArrayList<TT2HFLTS>> aggregateIndividualEstimations(List<ArrayList<ArrayList<TT2HFLTS>>> expEstimationsAll, float[] criteriaWeights) {
        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperatorFinal = new TT2HFLTSMHTWAOperator();
        ArrayList<ArrayList<TT2HFLTS>> aggEstAll = new ArrayList<>();

        for (List<ArrayList<TT2HFLTS>> singleExpertMatrix : expEstimationsAll) {
            ArrayList<TT2HFLTS> aggregatedForSingle = new ArrayList<>();
            for (List<TT2HFLTS> alternativePerCriteriaList : singleExpertMatrix) {
                TT2HFLTS aggRes = tt2HFLTSMHTWAOperatorFinal.calculate(alternativePerCriteriaList, criteriaWeights, 7);
                aggregatedForSingle.add(aggRes);
            }
            aggEstAll.add(aggregatedForSingle);
        }
        return aggEstAll;
    }

    private static List<TTAlternativeEstimationModel> prepareJSONForAggregationEstimates(List<TT2HFLTS> altOverall, TTCommonInputModel ttjsonModel, int targetScaleSize) {
        // now just to sort
        List<TT2HFLTS> sortedAltOverall = TTUtils.sortTT2HFLTS(altOverall, true);

        List<TTAlternativeEstimationModel> tmpModel = new ArrayList<>();

        // 4 1 2 3 5
        for (TT2HFLTS sortedAlt : sortedAltOverall) {
            List<Map<String, Float>> estimates = new ArrayList<>();
            for (TTTuple res : sortedAlt.getTerms()) {
                Map<String, Float> tmp = new HashMap<>();
                tmp.put(res.getLabel(), res.getTranslation());
                estimates.add(tmp);
            }

            TTAlternativeEstimationModel tmp = new TTAlternativeEstimationModel();

            tmp.setEstimation(estimates);

            TTScaleModel resScale = ttjsonModel.getScales()
                    .stream()
                    .filter(x -> x.getLabels().size() == targetScaleSize)
                    .findFirst()
                    .orElse(null);

            if (resScale == null) {
                return tmpModel;
            }

            tmp.setScaleID(resScale.getScaleID());

            int originalIndex = altOverall.indexOf(sortedAlt);
            TTAlternativeModel resAlt = ttjsonModel.getAlternatives().get(originalIndex);

            tmp.setAlternativeID(resAlt.getAlternativeID());
            tmpModel.add(tmp);
        }
        return tmpModel;
    }

    public static TTJSONOutputModel prepareAllResultsForJSON(List<TT2HFLTS> altOverall, TTJSONInputModel ttjsonModel, int targetScaleSize) {
        TTJSONOutputModel ttjsonOutputModel = new TTJSONOutputModel();
        ttjsonOutputModel.setAbstractionLevels(ttjsonModel.getAbstractionLevels());
        ttjsonOutputModel.setAlternatives(ttjsonModel.getAlternatives());
        ttjsonOutputModel.setScales(ttjsonModel.getScales());
        ttjsonOutputModel.setAbstractionLevels(ttjsonModel.getAbstractionLevels());
        ttjsonOutputModel.setAbstractionLevelWeights(ttjsonModel.getAbstractionLevelWeights());
        ttjsonOutputModel.setExpertWeightsRule(ttjsonModel.getExpertWeightsRule());
        ttjsonOutputModel.setExperts(ttjsonModel.getExperts());
        ttjsonOutputModel.setAlternativesOrdered(TTUtils.prepareJSONForAggregationEstimates(altOverall, ttjsonModel, targetScaleSize));
        return ttjsonOutputModel;
    }

    public static TTJSONOutputModel prepareAllResultsForJSON(List<TT2HFLTS> altOverall, TTJSONMultiLevelInputModel ttjsonModel, int targetScaleSize) {
        TTJSONOutputModel ttjsonOutputModel = new TTJSONOutputModel();
        ttjsonOutputModel.setAbstractionLevels(ttjsonModel.getAbstractionLevels());
        ttjsonOutputModel.setAlternatives(ttjsonModel.getAlternatives());
        ttjsonOutputModel.setScales(ttjsonModel.getScales());
        ttjsonOutputModel.setAbstractionLevels(ttjsonModel.getAbstractionLevels());
        ttjsonOutputModel.setAbstractionLevelWeights(ttjsonModel.getAbstractionLevelWeights());
        ttjsonOutputModel.setExpertWeightsRule(ttjsonModel.getExpertWeightsRule());
        ttjsonOutputModel.setExperts(ttjsonModel.getExperts());
        ttjsonOutputModel.setAlternativesOrdered(TTUtils.prepareJSONForAggregationEstimates(altOverall, ttjsonModel, targetScaleSize));
        return ttjsonOutputModel;
    }

    /**
     * When calculating weights for the experts, we introduce a new formula
     * E.g. distribution is [0.8, 0.2] and we have 3 experts.
     * That means that the first expert takes the 0.8 weight
     * The second expert - 0.8 * remaining part - 0.8*(1-0.8)
     * And so fourth until the last, that gets all remaining: 1 - weights_of_other_experts
     *
     * @param distribution - array of weights distribution
     * @param numExperts   - number of experts
     * @return vector of weights
     */
    public static float[] calculateWeightsVector(float[] distribution, int numExperts) {
        float[] res = new float[numExperts];
        for (int i = 0; i < numExperts; i++) {
            res[i] = calculateWeight(distribution[0], i, numExperts);
        }
        return res;
    }

    private static float calculateWeight(float firstWeight, int currentExpertNumber, int totalExpertsNumber) {
        if (currentExpertNumber == 0) {
            return firstWeight;
        } else if (currentExpertNumber == totalExpertsNumber - 1) {
            float acc = calculatePredessorsWeightsSum(firstWeight, currentExpertNumber, totalExpertsNumber);
            return 1 - acc;
        } else {
            float acc = calculatePredessorsWeightsSum(firstWeight, currentExpertNumber, totalExpertsNumber);
            return (1 - acc) * firstWeight;
        }
    }

    private static float calculatePredessorsWeightsSum(float firstWeight, int currentExpertNumber, int totalExpertsNumber) {
        float acc = 0f;
        for (int i = 0; i < currentExpertNumber; i++) {
            acc += calculateWeight(firstWeight, i, totalExpertsNumber);
        }
        return acc;
    }


    public static List<ArrayList<ArrayList<TT2HFLTS>>> getAllEstimationsFromMultiLevelJSONModel(TTJSONMultiLevelInputModel ttjsonModel, int targetScaleSize) {
        Map<String, List<TTExpertEstimationsModel>> estimations = ttjsonModel.getEstimations();
        Map<String, Integer> averages = getAverageForEachNumericCriterion(estimations);
        Map<String, List<TTCriteriaModel>> criteria = ttjsonModel.getCriteria();
        List<TTAbstractionLevelModel> levels = ttjsonModel.getAbstractionLevels();
        List<ArrayList<ArrayList<TT2HFLTS>>> expertsEstimationsList = new ArrayList<>();
        for (TTExpertModel expert : ttjsonModel.getExperts()) {
            List<TTExpertEstimationsModel> expertEstimations = estimations.get(expert.getExpertID());
            ArrayList<ArrayList<TT2HFLTS>> alternativesEstimationsList = new ArrayList<>();
            for (TTAlternativeModel alternativeModel : ttjsonModel.getAlternatives()) {
                ArrayList<TT2HFLTS> singleAltEstList = new ArrayList<>();
                TTExpertEstimationsModel expEst = expertEstimations
                        .stream()
                        .filter(estModel -> estModel.getAlternativeID().equals(alternativeModel.getAlternativeID()))
                        .findFirst()
                        .orElse(null);

                for (TTAbstractionLevelModel level : levels) {
                    for (TTCriteriaModel criteriaModel : criteria.get(level.getAbstractionLevelID())) {
                        TTCriteriaEstimationsModel critEst = expEst.getCriteria2Estimation()
                                .stream()
                                .filter(e -> e.getCriteriaID().equals(criteriaModel.getCriteriaID()))
                                .findFirst()
                                .orElse(null);

                        TT2HFLTS res;
                        if (critEst.getQualitative()) {
                            // transform it to 2tuple as usual linguistic info
                            res = transformToTTHFLTS(ttjsonModel.getScales(), critEst.getScaleID(), critEst.getEstimation());
                        } else {
                            // transform numeric to tuple and then to TTHFLTS
                            // however, first of all we need to normalize the values
                            float numericEstimation = Integer.parseInt(critEst.getEstimation().get(0)) / ((float) averages.get(critEst.getCriteriaID()));
                            List<Float> fSet = TTNormalizedTranslator.getInstance().getFuzzySetForNumericEstimation(numericEstimation, targetScaleSize);
                            float resTranslation = TTNormalizedTranslator.getInstance().getTranslationFromFuzzySet(fSet);
                            TTTuple resTuple = TTNormalizedTranslator.getInstance().getTTupleForNumericTranslation(resTranslation, targetScaleSize);
                            List<TTTuple> tmpL = new ArrayList<>();
                            tmpL.add(resTuple);
                            res = new TT2HFLTS(tmpL);
                        }
                        singleAltEstList.add(res);
                    }
                }
                alternativesEstimationsList.add(singleAltEstList);
            }
            expertsEstimationsList.add(alternativesEstimationsList);
        }
        return expertsEstimationsList;
    }

    private static Map<String, Integer> getAverageForEachNumericCriterion(Map<String, List<TTExpertEstimationsModel>> estimationsMap) {
        Map<String, List<Integer>> averages = new TreeMap<>();
        Map<String, Integer> sumFinal = new TreeMap<>();
        for (Map.Entry<String, List<TTExpertEstimationsModel>> entry : estimationsMap.entrySet()) {
            for (TTExpertEstimationsModel ttExpertEstimationsModel : entry.getValue()) {
                for (TTCriteriaEstimationsModel ttCriteriaEstimationsModel : ttExpertEstimationsModel.getCriteria2Estimation()) {
                    if (!ttCriteriaEstimationsModel.getQualitative()) {
                        List<Integer> averList = averages.get(ttCriteriaEstimationsModel.getCriteriaID());
                        if (averList == null) {
                            averList = new ArrayList<>();
                        }
                        averList.add(Integer.parseInt(ttCriteriaEstimationsModel.getEstimation().get(0)));
                        averages.put(ttCriteriaEstimationsModel.getCriteriaID(), averList);
                    }
                }

            }
        }
        for (Map.Entry<String, List<Integer>> entry : averages.entrySet()) {
            sumFinal.put(entry.getKey(), entry.getValue().stream().mapToInt(Integer::intValue).sum());
        }
        return sumFinal;

    }

    public static List<TTCriteriaModel> getOrderedCriteriaList(Map<String, List<TTCriteriaModel>> criteria,
                                                               List<TTAbstractionLevelModel> levels) {
        List<TTCriteriaModel> res = new ArrayList<>();
        for (TTAbstractionLevelModel level : levels) {
            for (TTCriteriaModel criteriaModel : criteria.get(level.getAbstractionLevelID())) {
                res.add(criteriaModel);
            }
        }
        return res;
    }
}
