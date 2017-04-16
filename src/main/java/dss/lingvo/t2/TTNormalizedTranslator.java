package dss.lingvo.t2;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.utils.TTConstants;
import dss.lingvo.utils.models.input.TTScaleModel;
import javafx.util.converter.FloatStringConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TTNormalizedTranslator {
    private static TTNormalizedTranslator myInstance = new TTNormalizedTranslator();
    private static Map<Integer, TTHFLTSScale> scaleStore;

    public static TTNormalizedTranslator getInstance() {
        return myInstance;
    }

    public static void registerScalesBatch(List<TTScaleModel> newScales) {
        HashMap<Integer, TTHFLTSScale> myScaleStore = new HashMap<>();

        for (TTScaleModel scale : newScales) {
            String[] tmp = scale.getLabels().stream().toArray(String[]::new);
            myScaleStore.put(tmp.length, new TTHFLTSScale(tmp));
        }

        TTNormalizedTranslator.setScaleStore(myScaleStore);
    }

    public static void setScaleStore(Map<Integer, TTHFLTSScale> scaleStore) {
        TTNormalizedTranslator.scaleStore = scaleStore;
    }

    public static Map<Integer, TTHFLTSScale> getScaleStore() {
        return scaleStore;
    }

    public TTTuple translateTo2Tuple(float translation, int targetScaleSize) {
        float beta = translation * (targetScaleSize - 1);
        int position = Math.round(beta);
        float newTranslation = beta - position;
        TTHFLTSScale scale = scaleStore.get(targetScaleSize);
        String label = scale.getLabelByPosition(position);
        return new TTTuple(label, targetScaleSize, newTranslation, position);
    }

    public float getTranslationFrom2Tuple(TTTuple tt) {
        return (tt.getIndex() + tt.getTranslation()) / (tt.getScaleSize() - 1);
    }

    /**
     * To get tuple from the numeric estimation:
     * 1. translate it to [0,1]
     * 2. get fuzzy set
     * 3. translate fuzzy set to 2tuple
     * <p>
     * This function is the step 2
     *
     * @param estimation - [0,1]
     * @return Fuzzy Set of 2Tuples
     */
    public List<Float> getFuzzySetForNumericEstimation(float estimation, int targetScaleSize) {
        List<float[]> ranges = getRangesForScale(targetScaleSize);
        List<Float> resFuzzySet = new ArrayList<>();
        for (float[] range : ranges) {
            float a = range[0];
            float b = range[1];
            float d = range[2];
            float c = range[3];
            Float tmp = null;
            if (a > estimation || c < estimation) {
                tmp = 0f;
            } else if (estimation >= a && estimation <= b) {
                tmp = (estimation - a) / (b - a);
            } else if (estimation >= b && estimation <= d) {
                tmp = 1f;
            } else if (estimation >= d && estimation <= c) {
                tmp = (c - estimation) / (c - d);
            }
            resFuzzySet.add(tmp);
        }
        return resFuzzySet;
    }

    public List<float[]> getRangesForScale(int targetScaleSize) {
        float delta = 1f / (targetScaleSize-1);
        List<float[]> res = new ArrayList<>();
        for (int i = 0; i < targetScaleSize; i++) {
            final int numIntervals = 4;
            float[] intervals = new float[numIntervals];
            if (i == 0) {
                intervals[0] = intervals[1] = intervals[2] = 0;
                intervals[3] = intervals[0] + delta;
            } else if (i == targetScaleSize-1){
                intervals[0] = (i-1) * delta;
                intervals[1] = intervals[2] = intervals[3] = i * delta;
            } else {
                intervals[0] = (i-1) * delta;
                intervals[1] = intervals[2] = i * delta;
                intervals[3] = (i+1) * delta;
            }
            res.add(intervals);
        }
        return res;
    }

    public float getTranslationFromFuzzySet(List<Float> fuzzySet) {
        float acc = 0f;
        for (int i = 0; i < fuzzySet.size(); i++){
            acc += i * fuzzySet.get(i);
        }
        return acc;
    }

    public TTTuple getTTupleForNumericTranslation(float translation, int targetScaleSize){
        int position = Math.round(translation);
        float alpha = translation - position;
        TTHFLTSScale scale = scaleStore.get(targetScaleSize);
        String label = scale.getLabelByPosition(position);
        return new TTTuple(label, targetScaleSize, alpha, position);

    }
}
