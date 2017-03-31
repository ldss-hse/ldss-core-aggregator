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

        String[] scale9 = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        TTHFLTSScale hScale9 = new TTHFLTSScale(scale9);

        String[] scale3 = {"0", "1", "2"};
        TTHFLTSScale hScale3 = new TTHFLTSScale(scale3);

        String[] scale5 = {"0", "1", "2", "3", "4"};
        TTHFLTSScale hScale5 = new TTHFLTSScale(scale5);

        String[] scale7 = {"0", "1", "2", "3", "4", "5", "6"};
        TTHFLTSScale hScale7 = new TTHFLTSScale(scale7);



        myScaleStore.put(scale3.length, hScale3);
        myScaleStore.put(scale5.length, hScale5);
        myScaleStore.put(scale7.length, hScale7);

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
