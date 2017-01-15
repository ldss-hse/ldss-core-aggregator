package dss.lingvo.t2;

import dss.lingvo.hflts.TTHFLTSScale;

import java.util.ArrayList;
import java.util.HashMap;

public class TTNormalizedTranslator {
    public static TTNormalizedTranslator myInstance = null;
    private static HashMap<Integer, TTHFLTSScale> scaleStore;

    public static TTNormalizedTranslator getInstance(){
        if (myInstance == null){
           myInstance = new TTNormalizedTranslator();
        }
        return myInstance;
    }

    public static void setScaleStore(HashMap<Integer, TTHFLTSScale> scaleStore){
        TTNormalizedTranslator.scaleStore = scaleStore;
    }

    public TTTuple translateTo2Tuple(float translation, int targetScaleSize){
        float beta = translation * (targetScaleSize-1);
        int position = Math.round(beta);
        float newTranslation = beta - position;
        TTHFLTSScale scale = scaleStore.get(targetScaleSize);
        String label = scale.getLabelByPosition(position);
        return new TTTuple(label, targetScaleSize, newTranslation, position);
    }

    public float getTranslationFrom2Tuple(TTTuple tt){
        return (tt.getIndex()+tt.getTranslation())/(tt.getScaleSize()-1);
    }
}
