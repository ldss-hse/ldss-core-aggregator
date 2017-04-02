package dss.lingvo.t2;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.utils.models.TTScaleModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TTNormalizedTranslator {
    private static TTNormalizedTranslator myInstance = new TTNormalizedTranslator();
    private static Map<Integer, TTHFLTSScale> scaleStore;

    public static TTNormalizedTranslator getInstance() {
        return myInstance;
    }

    public static void registerScalesBatch(List<TTScaleModel> newScales) {
        HashMap<Integer, TTHFLTSScale> myScaleStore = new HashMap<>();

        for (TTScaleModel scale: newScales){
            String[] tmp = scale.getLabels().stream().toArray(String[]::new);
            myScaleStore.put(tmp.length, new TTHFLTSScale(tmp));
        }

        TTNormalizedTranslator.setScaleStore(myScaleStore);
    }

    public static void setScaleStore(Map<Integer, TTHFLTSScale> scaleStore) {
        TTNormalizedTranslator.scaleStore = scaleStore;
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
}
