package dss.lingvo.utils;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.t2hflts.TT2HFLTSMHTWAOperator;
import dss.lingvo.utils.models.TTCriteriaEstimationsModel;
import dss.lingvo.utils.models.TTExpertEstimationsModel;
import dss.lingvo.utils.models.TTJSONModel;
import dss.lingvo.utils.models.TTScaleModel;

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
        return tt1Translation < tt2Translation ?
                -1 :
                Math.abs(tt2Translation - tt1Translation) < TTConstants.FLOAT_PRECISION_DELTA ? 0 : 1;
    }

    private static int compareTT2HFLTS(TT2HFLTS tt1, TT2HFLTS tt2) {
        float tt1Score = tt1.getScore();
        float tt2Score = tt2.getScore();
        if (tt1Score < tt2Score) {
            return -1;
        } else if (Math.abs(tt1Score - tt2Score) < TTConstants.FLOAT_PRECISION_DELTA) {
            float tt1Variance = tt1.getVariance();
            float tt2Variance = tt2.getVariance();
            if (tt1Variance < tt2Variance) {
                return 1;
            } else if (Math.abs(tt1Variance- tt2Variance) < TTConstants.FLOAT_PRECISION_DELTA) {
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
    public interface PiecewiseLinearLambda{
        public float compute(int i, float[] wVec, float x);
    }

    public static ArrayList<ArrayList<ArrayList<TT2HFLTS>>> getAllEstimationsFromJSONModel(TTJSONModel ttjsonModel){
        Map<String, List<TTExpertEstimationsModel>> fileEstimations = ttjsonModel.getEstimations();

        Map<String, List<TTExpertEstimationsModel>> treeMap = new TreeMap<String, List<TTExpertEstimationsModel>>(
                (Comparator<String>) (o1, o2) -> {
                    int num1 = Integer.parseInt(o1.replaceAll("[\\D]", ""));
                    int num2 = Integer.parseInt(o2.replaceAll("[\\D]", ""));
                    return num1 - num2;
                }
        );

        treeMap.putAll(fileEstimations);

        ArrayList<ArrayList<ArrayList<TT2HFLTS>>> expEstimationsAll = new ArrayList<>();

        for(Map.Entry<String, List<TTExpertEstimationsModel>> el:fileEstimations.entrySet()){
            ArrayList<ArrayList<TT2HFLTS>> expAll = new ArrayList<>();
            el.getValue().sort((Comparator<TTExpertEstimationsModel>) (o1, o2) -> {
                int num1 = Integer.parseInt(o1.getAlternativeID().replaceAll("[\\D]", ""));
                int num2 = Integer.parseInt(o2.getAlternativeID().replaceAll("[\\D]", ""));
                return num1 - num2;
            });
            // loop though sorted by alternative
            for (TTExpertEstimationsModel model: el.getValue()){
                model.getCriteria2Estimation().sort((Comparator<TTCriteriaEstimationsModel>) (o1, o2) -> {
                    int num1 = Integer.parseInt(o1.getCriteriaID().replaceAll("[\\D]", ""));
                    int num2 = Integer.parseInt(o2.getCriteriaID().replaceAll("[\\D]", ""));
                    return num1 - num2;
                });
                ArrayList<TT2HFLTS> expToAltToCrit = new ArrayList<>();
                // loop though sorted by criteria
                for (TTCriteriaEstimationsModel criterion: model.getCriteria2Estimation()){
                    List<String> val = criterion.getEstimation();
                    ArrayList<TTTuple> expToAltToCritTTuple = new ArrayList<>();
                    TTScaleModel scale= ttjsonModel.getScales()
                            .stream()
                            .filter(x -> x.getScaleID().equals(criterion.getScaleID()))
                            .findFirst()
                            .orElse(null);
                    int scaleSize = scale.getLabels().size();
                    for (String label: val){
                        expToAltToCritTTuple.add(new TTTuple(label, scaleSize, 0, scale.getLabels().indexOf(label)));
                    }
                    expToAltToCrit.add(new TT2HFLTS(expToAltToCritTTuple));
                }
                expAll.add(expToAltToCrit);
            }
            expEstimationsAll.add(expAll);
        }
        return expEstimationsAll;
    }

    public static ArrayList<ArrayList<TT2HFLTS>> aggregateIndividualEstimations(ArrayList<ArrayList<ArrayList<TT2HFLTS>>> expEstimationsAll, float[] criteriaWeights){
        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperatorFinal = new TT2HFLTSMHTWAOperator();
        ArrayList<ArrayList<TT2HFLTS>> aggEstAll = new ArrayList<>();

        for (ArrayList<ArrayList<TT2HFLTS>> singleExpertMatrix: expEstimationsAll){
            ArrayList<TT2HFLTS> aggregatedForSingle = new ArrayList<>();
            for (ArrayList<TT2HFLTS> alternativePerCriteriaList: singleExpertMatrix){
                TT2HFLTS aggRes = tt2HFLTSMHTWAOperatorFinal.calculate(alternativePerCriteriaList, criteriaWeights, 7);
                aggregatedForSingle.add(aggRes);
            }
            aggEstAll.add(aggregatedForSingle);
        }
        return aggEstAll;
    }
}
