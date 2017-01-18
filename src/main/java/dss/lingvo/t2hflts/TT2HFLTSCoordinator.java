package dss.lingvo.t2hflts;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TT2HFLTSCoordinator {
    private TTUtils log = TTUtils.getInstance();

    public void go() {
        String[] scale9 = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        TTHFLTSScale hScale9 = new TTHFLTSScale(scale9);

        String[] scale3 = {"0", "1", "2"};
        TTHFLTSScale hScale3 = new TTHFLTSScale(scale3);

        String[] scale5 = {"0", "1", "2", "3", "4"};
        TTHFLTSScale hScale5 = new TTHFLTSScale(scale5);

        String[] scale7 = {"0", "1", "2", "3", "4", "5", "6"};
        TTHFLTSScale hScale7 = new TTHFLTSScale(scale7);

        HashMap<Integer, TTHFLTSScale> scaleStore = new HashMap<>();
        scaleStore.put(scale9.length, hScale9);
        scaleStore.put(scale3.length, hScale3);
        scaleStore.put(scale5.length, hScale5);
        scaleStore.put(scale7.length, hScale7);

        TTNormalizedTranslator.setScaleStore(scaleStore);

        int numberExp = 4;
        float[] weights = new float[numberExp];
        for (int i = 0; i < numberExp; i++) {
            weights[i] = 1f / numberExp; // they are of equal importance currently
        }

        log.info("Step 1. Gather feedback and parse to T2HFLTS...");
        ArrayList<ArrayList<TTTuple>> estimates = new ArrayList<>();

        ArrayList<TTTuple> alt1 = new ArrayList<>();
        alt1.add(new TTTuple("4", 9, 0, 4));
        alt1.add(new TTTuple("3", 5, 0, 3));
        alt1.add(new TTTuple("1", 3, 0, 1));
        alt1.add(new TTTuple("4", 9, 0, 4));

        ArrayList<TTTuple> alt2 = new ArrayList<>();
        alt2.add(new TTTuple("6", 9, 0, 6));
        alt2.add(new TTTuple("4", 5, 0, 4));
        alt2.add(new TTTuple("2", 3, 0, 2));
        alt2.add(new TTTuple("5", 9, 0, 5));

        ArrayList<TTTuple> alt3 = new ArrayList<>();
        alt3.add(new TTTuple("3", 9, 0, 3));
        alt3.add(new TTTuple("3", 5, 0, 3));
        alt3.add(new TTTuple("2", 3, 0, 2));
        alt3.add(new TTTuple("3", 9, 0, 3));

        ArrayList<TTTuple> alt4 = new ArrayList<>();
        alt4.add(new TTTuple("5", 9, 0, 5));
        alt4.add(new TTTuple("3", 5, 0, 3));
        alt4.add(new TTTuple("1", 3, 0, 1));
        alt4.add(new TTTuple("5", 9, 0, 5));

        estimates.add(alt1);
        estimates.add(alt2);
        estimates.add(alt3);
        estimates.add(alt4);

        log.info("Step 2. Aggregate alternative values...");
        TT2HFLTSMTWAOperator mtwaOperator = new TT2HFLTSMTWAOperator();
        ArrayList<TTTuple> res = new ArrayList<>();
        for (int i = 0; i < estimates.size(); i++) {
            res.add(mtwaOperator.calculate(estimates.get(i), weights, 9));
        }

        log.info(res);

        log.info("Step 3. Sort the alternatives and find the best one...");
        List<TTTuple> sortedRes = TTUtils.sortTuples(res, true);
        log.info(sortedRes);

        //MHTWA example

        ArrayList<TTTuple> est1 = new ArrayList<>();
        est1.add(new TTTuple("2", 7, 0, 2));
        est1.add(new TTTuple("3", 7, 0, 3));
        est1.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS tt2HFLTS1 = new TT2HFLTS(est1);

        ArrayList<TTTuple> est2 = new ArrayList<>();
        est2.add(new TTTuple("2", 5, 0, 2));
        est2.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS tt2HFLTS2 = new TT2HFLTS(est2);

        ArrayList<TTTuple> est3 = new ArrayList<>();
        est3.add(new TTTuple("1", 3, 0, 1));

        TT2HFLTS tt2HFLTS3 = new TT2HFLTS(est3);

        ArrayList<TT2HFLTS> sets = new ArrayList<>();

        sets.add(tt2HFLTS1);
        sets.add(tt2HFLTS2);
        sets.add(tt2HFLTS3);

        List<TT2HFLTS> setsOrdered = TTUtils.sortTT2HFLTS(sets,true);

        float [] hWeights = {0.25f, 0.5f, 0.25f};

        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperator = new TT2HFLTSMHTWAOperator();
        TT2HFLTS aggregationRes = tt2HFLTSMHTWAOperator.calculate(setsOrdered, hWeights, 7);

    }
}
