package dss.lingvo.utils;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;

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
}
