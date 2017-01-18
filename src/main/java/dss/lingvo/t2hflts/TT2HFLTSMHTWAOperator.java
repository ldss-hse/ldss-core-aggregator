package dss.lingvo.t2hflts;

import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTUtils;

import java.util.*;

public class TT2HFLTSMHTWAOperator {

    public TT2HFLTS calculate(List<TT2HFLTS> sets, float[] weights, int targetScaleSize) {
        TT2HFLTSMTWAOperator mtwaOperator = new TT2HFLTSMTWAOperator();

        int expectedQuantity = (int) Arrays
                .stream(sets.toArray())
                .reduce(1, (el, partial) -> (int) el * ((TT2HFLTS) partial).getSize());

        ArrayList<ArrayList<TTTuple>> store = new ArrayList<>();
        for (int i = 0; i < expectedQuantity; i++) {
            store.add(new ArrayList<>());
        }

        for (TT2HFLTS set : sets) {
            for (int i = 0; i < expectedQuantity; i++) {
                int actualIndex = i % set.getSize();
                store.get(i).add(set.getTerms().get(actualIndex));
            }
        }

        ArrayList<TT2HFLTS> t2HFLTSstore = new ArrayList<>();
        for (ArrayList<TTTuple> est : store) {
            t2HFLTSstore.add(new TT2HFLTS(est));
        }

        ArrayList<TTTuple> res = new ArrayList<>();
        for (TT2HFLTS el : t2HFLTSstore) {
            res.add(mtwaOperator.calculate(el.getTerms(), weights, targetScaleSize));
        }
        List<TTTuple> sortedRes = TTUtils.sortTuples(res, false);
        return new TT2HFLTS(sortedRes);
    }
}
