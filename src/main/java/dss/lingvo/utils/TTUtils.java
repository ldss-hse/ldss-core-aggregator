package dss.lingvo.utils;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<TTTuple> sortTuples(List<TTTuple> ttTupleList) {
        ArrayList<TTTuple> res = new ArrayList<>(ttTupleList);
        Collections.sort(res, Collections.reverseOrder((tt1, tt2) -> {
            float tt1Translation = TTNormalizedTranslator.getInstance().getTranslationFrom2Tuple(tt1);
            float tt2Translation = TTNormalizedTranslator.getInstance().getTranslationFrom2Tuple(tt2);
            return tt1Translation < tt2Translation ?
                    -1 :
                    Math.abs(tt2Translation - tt1Translation) < TTConstants.FLOAT_PRECISION_DELTA ? 0 : 1;
        }));
        return res;
    }
}
