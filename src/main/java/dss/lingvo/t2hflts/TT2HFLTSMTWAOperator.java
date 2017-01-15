package dss.lingvo.t2hflts;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;

import java.util.ArrayList;

public class TT2HFLTSMTWAOperator {

    public TTTuple calculate(ArrayList<TTTuple> ttTuples, float[] weights, int targetScaleSize) {
        TTNormalizedTranslator myTranslator = TTNormalizedTranslator.getInstance();
        float res = 0;
        for (int i = 0; i < ttTuples.size(); ++i) {
            res += myTranslator.getTranslationFrom2Tuple(ttTuples.get(i)) * weights[i];
        }
        return myTranslator.translateTo2Tuple(res, targetScaleSize);
    }
}
