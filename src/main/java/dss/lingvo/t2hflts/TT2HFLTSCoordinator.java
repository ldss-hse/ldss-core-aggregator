package dss.lingvo.t2hflts;

import dss.lingvo.hflts.TTHFLTS;
import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTUtils;

import java.util.*;

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

        List<TT2HFLTS> setsOrdered = TTUtils.sortTT2HFLTS(sets, true);

        float[] hWeights = {0.25f, 0.5f, 0.25f};

        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperator = new TT2HFLTSMHTWAOperator();
        tt2HFLTSMHTWAOperator.calculate(setsOrdered, hWeights, 7);

        // MHTWOWA example

        // 1. Gather feedback and translate directly to the TT2HFLTS (per each expert)

        /**
         * Expert #1
         */
        // expert 1, alternative 1
        ArrayList<TTTuple> exp1alt1crit1List = new ArrayList<>();
        exp1alt1crit1List.add(new TTTuple("4", 7, 0, 4));
        exp1alt1crit1List.add(new TTTuple("5", 7, 0, 5));

        TT2HFLTS exp1alt1crit1 = new TT2HFLTS(exp1alt1crit1List);

        ArrayList<TTTuple> exp1alt1crit2List = new ArrayList<>();
        exp1alt1crit2List.add(new TTTuple("3", 7, 0, 3));

        TT2HFLTS exp1alt1crit2 = new TT2HFLTS(exp1alt1crit2List);

        ArrayList<TTTuple> exp1alt1crit3List = new ArrayList<>();
        exp1alt1crit3List.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS exp1alt1crit3 = new TT2HFLTS(exp1alt1crit3List);

        // expert 1, alternative 2
        ArrayList<TTTuple> exp1alt2crit1List = new ArrayList<>();
        exp1alt2crit1List.add(new TTTuple("2", 7, 0, 2));

        TT2HFLTS exp1alt2crit1 = new TT2HFLTS(exp1alt2crit1List);

        ArrayList<TTTuple> exp1alt2crit2List = new ArrayList<>();
        exp1alt2crit2List.add(new TTTuple("5", 7, 0, 5));
        exp1alt2crit2List.add(new TTTuple("6", 7, 0, 6));

        TT2HFLTS exp1alt2crit2 = new TT2HFLTS(exp1alt2crit2List);

        ArrayList<TTTuple> exp1alt2crit3List = new ArrayList<>();
        exp1alt2crit3List.add(new TTTuple("3", 7, 0, 3));

        TT2HFLTS exp1alt2crit3 = new TT2HFLTS(exp1alt2crit3List);

        // expert 1, alternative 3
        ArrayList<TTTuple> exp1alt3crit1List = new ArrayList<>();
        exp1alt3crit1List.add(new TTTuple("1", 7, 0, 1));

        TT2HFLTS exp1alt3crit1 = new TT2HFLTS(exp1alt3crit1List);

        ArrayList<TTTuple> exp1alt3crit2List = new ArrayList<>();
        exp1alt3crit2List.add(new TTTuple("5", 7, 0, 5));

        TT2HFLTS exp1alt3crit2 = new TT2HFLTS(exp1alt3crit2List);

        ArrayList<TTTuple> exp1alt3crit3List = new ArrayList<>();
        exp1alt3crit3List.add(new TTTuple("3", 7, 0, 3));
        exp1alt3crit3List.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS exp1alt3crit3 = new TT2HFLTS(exp1alt3crit3List);

        // expert 1, alternative 4
        ArrayList<TTTuple> exp1alt4crit1List = new ArrayList<>();
        exp1alt4crit1List.add(new TTTuple("5", 7, 0, 5));
        exp1alt4crit1List.add(new TTTuple("6", 7, 0, 6));

        TT2HFLTS exp1alt4crit1 = new TT2HFLTS(exp1alt4crit1List);

        ArrayList<TTTuple> exp1alt4crit2List = new ArrayList<>();
        exp1alt4crit2List.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS exp1alt4crit2 = new TT2HFLTS(exp1alt4crit2List);

        ArrayList<TTTuple> exp1alt4crit3List = new ArrayList<>();
        exp1alt4crit3List.add(new TTTuple("3", 7, 0, 3));

        TT2HFLTS exp1alt4crit3 = new TT2HFLTS(exp1alt4crit3List);

        // expert 1, alternative 5
        ArrayList<TTTuple> exp1alt5crit1List = new ArrayList<>();
        exp1alt5crit1List.add(new TTTuple("3", 7, 0, 3));

        TT2HFLTS exp1alt5crit1 = new TT2HFLTS(exp1alt5crit1List);

        ArrayList<TTTuple> exp1alt5crit2List = new ArrayList<>();
        exp1alt5crit2List.add(new TTTuple("1", 7, 0, 1));

        TT2HFLTS exp1alt5crit2 = new TT2HFLTS(exp1alt5crit2List);

        ArrayList<TTTuple> exp1alt5crit3List = new ArrayList<>();
        exp1alt5crit3List.add(new TTTuple("5", 7, 0, 5));
        exp1alt5crit3List.add(new TTTuple("6", 7, 0, 6));

        TT2HFLTS exp1alt5crit3 = new TT2HFLTS(exp1alt5crit3List);

        ArrayList<TT2HFLTS> exp1alt1 = new ArrayList<>();
        exp1alt1.add(exp1alt1crit1);
        exp1alt1.add(exp1alt1crit2);
        exp1alt1.add(exp1alt1crit3);

        ArrayList<TT2HFLTS> exp1alt2 = new ArrayList<>();
        exp1alt2.add(exp1alt2crit1);
        exp1alt2.add(exp1alt2crit2);
        exp1alt2.add(exp1alt2crit3);

        ArrayList<TT2HFLTS> exp1alt3 = new ArrayList<>();
        exp1alt3.add(exp1alt3crit1);
        exp1alt3.add(exp1alt3crit2);
        exp1alt3.add(exp1alt3crit3);

        ArrayList<TT2HFLTS> exp1alt4 = new ArrayList<>();
        exp1alt4.add(exp1alt4crit1);
        exp1alt4.add(exp1alt4crit2);
        exp1alt4.add(exp1alt4crit3);

        ArrayList<TT2HFLTS> exp1alt5 = new ArrayList<>();
        exp1alt5.add(exp1alt5crit1);
        exp1alt5.add(exp1alt5crit2);
        exp1alt5.add(exp1alt5crit3);

        ArrayList<ArrayList<TT2HFLTS>> exp1 = new ArrayList<>();
        exp1.add(exp1alt1);
        exp1.add(exp1alt2);
        exp1.add(exp1alt3);
        exp1.add(exp1alt4);
        exp1.add(exp1alt5);

        /**
         * Expert #2
         */
        // expert 2, alternative 1
        ArrayList<TTTuple> exp2alt1crit1List = new ArrayList<>();
        exp2alt1crit1List.add(new TTTuple("5", 7, 0, 5));

        TT2HFLTS exp2alt1crit1 = new TT2HFLTS(exp2alt1crit1List);

        ArrayList<TTTuple> exp2alt1crit2List = new ArrayList<>();
        exp2alt1crit2List.add(new TTTuple("2", 7, 0, 2));

        TT2HFLTS exp2alt1crit2 = new TT2HFLTS(exp2alt1crit2List);

        ArrayList<TTTuple> exp2alt1crit3List = new ArrayList<>();
        exp2alt1crit3List.add(new TTTuple("3", 7, 0, 3));
        exp2alt1crit3List.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS exp2alt1crit3 = new TT2HFLTS(exp2alt1crit3List);

        // expert 2, alternative 2
        ArrayList<TTTuple> exp2alt2crit1List = new ArrayList<>();
        exp2alt2crit1List.add(new TTTuple("3", 7, 0, 3));
        exp2alt2crit1List.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS exp2alt2crit1 = new TT2HFLTS(exp2alt2crit1List);

        ArrayList<TTTuple> exp2alt2crit2List = new ArrayList<>();
        exp2alt2crit2List.add(new TTTuple("4", 7, 0, 4));
        exp2alt2crit2List.add(new TTTuple("5", 7, 0, 5));

        TT2HFLTS exp2alt2crit2 = new TT2HFLTS(exp2alt2crit2List);

        ArrayList<TTTuple> exp2alt2crit3List = new ArrayList<>();
        exp2alt2crit3List.add(new TTTuple("2", 7, 0, 2));

        TT2HFLTS exp2alt2crit3 = new TT2HFLTS(exp2alt2crit3List);

        // expert 2, alternative 3
        ArrayList<TTTuple> exp2alt3crit1List = new ArrayList<>();
        exp2alt3crit1List.add(new TTTuple("2", 7, 0, 2));

        TT2HFLTS exp2alt3crit1 = new TT2HFLTS(exp2alt3crit1List);

        ArrayList<TTTuple> exp2alt3crit2List = new ArrayList<>();
        exp2alt3crit2List.add(new TTTuple("3", 7, 0, 3));
        exp2alt3crit2List.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS exp2alt3crit2 = new TT2HFLTS(exp2alt3crit2List);

        ArrayList<TTTuple> exp2alt3crit3List = new ArrayList<>();
        exp2alt3crit3List.add(new TTTuple("3", 7, 0, 3));
        exp2alt3crit3List.add(new TTTuple("4", 7, 0, 4));

        TT2HFLTS exp2alt3crit3 = new TT2HFLTS(exp2alt3crit3List);

        // expert 2, alternative 4
        ArrayList<TTTuple> exp2alt4crit1List = new ArrayList<>();
        exp2alt4crit1List.add(new TTTuple("5", 7, 0, 5));

        TT2HFLTS exp2alt4crit1 = new TT2HFLTS(exp2alt4crit1List);

        ArrayList<TTTuple> exp2alt4crit2List = new ArrayList<>();
        exp2alt4crit2List.add(new TTTuple("5", 7, 0, 5));
        exp2alt4crit2List.add(new TTTuple("6", 7, 0, 6));

        TT2HFLTS exp2alt4crit2 = new TT2HFLTS(exp2alt4crit2List);

        ArrayList<TTTuple> exp2alt4crit3List = new ArrayList<>();
        exp2alt4crit3List.add(new TTTuple("2", 7, 0, 2));

        TT2HFLTS exp2alt4crit3 = new TT2HFLTS(exp2alt4crit3List);

        // expert 2, alternative 5
        ArrayList<TTTuple> exp2alt5crit1List = new ArrayList<>();
        exp2alt5crit1List.add(new TTTuple("3", 7, 0, 3));

        TT2HFLTS exp2alt5crit1 = new TT2HFLTS(exp2alt5crit1List);

        ArrayList<TTTuple> exp2alt5crit2List = new ArrayList<>();
        exp2alt5crit2List.add(new TTTuple("2", 7, 0, 2));

        TT2HFLTS exp2alt5crit2 = new TT2HFLTS(exp2alt5crit2List);

        ArrayList<TTTuple> exp2alt5crit3List = new ArrayList<>();
        exp2alt5crit3List.add(new TTTuple("5", 7, 0, 5));

        TT2HFLTS exp2alt5crit3 = new TT2HFLTS(exp2alt5crit3List);

        ArrayList<TT2HFLTS> exp2alt1 = new ArrayList<>();
        exp2alt1.add(exp2alt1crit1);
        exp2alt1.add(exp2alt1crit2);
        exp2alt1.add(exp2alt1crit3);

        ArrayList<TT2HFLTS> exp2alt2 = new ArrayList<>();
        exp2alt2.add(exp2alt2crit1);
        exp2alt2.add(exp2alt2crit2);
        exp2alt2.add(exp2alt2crit3);

        ArrayList<TT2HFLTS> exp2alt3 = new ArrayList<>();
        exp2alt3.add(exp2alt3crit1);
        exp2alt3.add(exp2alt3crit2);
        exp2alt3.add(exp2alt3crit3);

        ArrayList<TT2HFLTS> exp2alt4 = new ArrayList<>();
        exp2alt4.add(exp2alt4crit1);
        exp2alt4.add(exp2alt4crit2);
        exp2alt4.add(exp2alt4crit3);

        ArrayList<TT2HFLTS> exp2alt5 = new ArrayList<>();
        exp2alt5.add(exp2alt5crit1);
        exp2alt5.add(exp2alt5crit2);
        exp2alt5.add(exp2alt5crit3);

        ArrayList<ArrayList<TT2HFLTS>> exp2 = new ArrayList<>();
        exp2.add(exp2alt1);
        exp2.add(exp2alt2);
        exp2.add(exp2alt3);
        exp2.add(exp2alt4);
        exp2.add(exp2alt5);

        /**
         * Expert #3
         */
        // expert 3, alternative 1
        ArrayList<TTTuple> exp3alt1crit1List = new ArrayList<>();
        exp3alt1crit1List.add(new TTTuple("4", 5, 0, 4));

        TT2HFLTS exp3alt1crit1 = new TT2HFLTS(exp3alt1crit1List);

        ArrayList<TTTuple> exp3alt1crit2List = new ArrayList<>();
        exp3alt1crit2List.add(new TTTuple("2", 5, 0, 2));

        TT2HFLTS exp3alt1crit2 = new TT2HFLTS(exp3alt1crit2List);

        ArrayList<TTTuple> exp3alt1crit3List = new ArrayList<>();
        exp3alt1crit3List.add(new TTTuple("2", 5, 0, 2));
        exp3alt1crit3List.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS exp3alt1crit3 = new TT2HFLTS(exp3alt1crit3List);

        // expert 3, alternative 2
        ArrayList<TTTuple> exp3alt2crit1List = new ArrayList<>();
        exp3alt2crit1List.add(new TTTuple("2", 5, 0, 2));

        TT2HFLTS exp3alt2crit1 = new TT2HFLTS(exp3alt2crit1List);

        ArrayList<TTTuple> exp3alt2crit2List = new ArrayList<>();
        exp3alt2crit2List.add(new TTTuple("4", 5, 0, 4));

        TT2HFLTS exp3alt2crit2 = new TT2HFLTS(exp3alt2crit2List);

        ArrayList<TTTuple> exp3alt2crit3List = new ArrayList<>();
        exp3alt2crit3List.add(new TTTuple("2", 5, 0, 2));

        TT2HFLTS exp3alt2crit3 = new TT2HFLTS(exp3alt2crit3List);

        // expert 3, alternative 3
        ArrayList<TTTuple> exp3alt3crit1List = new ArrayList<>();
        exp3alt3crit1List.add(new TTTuple("2", 5, 0, 2));

        TT2HFLTS exp3alt3crit1 = new TT2HFLTS(exp3alt3crit1List);

        ArrayList<TTTuple> exp3alt3crit2List = new ArrayList<>();
        exp3alt3crit2List.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS exp3alt3crit2 = new TT2HFLTS(exp3alt3crit2List);

        ArrayList<TTTuple> exp3alt3crit3List = new ArrayList<>();
        exp3alt3crit3List.add(new TTTuple("2", 5, 0, 2));
        exp3alt3crit3List.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS exp3alt3crit3 = new TT2HFLTS(exp3alt3crit3List);

        // expert 3, alternative 4
        ArrayList<TTTuple> exp3alt4crit1List = new ArrayList<>();
        exp3alt4crit1List.add(new TTTuple("3", 5, 0, 3));
        exp3alt4crit1List.add(new TTTuple("4", 5, 0, 4));

        TT2HFLTS exp3alt4crit1 = new TT2HFLTS(exp3alt4crit1List);

        ArrayList<TTTuple> exp3alt4crit2List = new ArrayList<>();
        exp3alt4crit2List.add(new TTTuple("3", 5, 0, 3));

        TT2HFLTS exp3alt4crit2 = new TT2HFLTS(exp3alt4crit2List);

        ArrayList<TTTuple> exp3alt4crit3List = new ArrayList<>();
        exp3alt4crit3List.add(new TTTuple("2", 5, 0, 2));

        TT2HFLTS exp3alt4crit3 = new TT2HFLTS(exp3alt4crit3List);

        // expert 3, alternative 5
        ArrayList<TTTuple> exp3alt5crit1List = new ArrayList<>();
        exp3alt5crit1List.add(new TTTuple("2", 5, 0, 2));

        TT2HFLTS exp3alt5crit1 = new TT2HFLTS(exp3alt5crit1List);

        ArrayList<TTTuple> exp3alt5crit2List = new ArrayList<>();
        exp3alt5crit2List.add(new TTTuple("0", 5, 0, 0));
        exp3alt5crit2List.add(new TTTuple("1", 5, 0, 1));

        TT2HFLTS exp3alt5crit2 = new TT2HFLTS(exp3alt5crit2List);

        ArrayList<TTTuple> exp3alt5crit3List = new ArrayList<>();
        exp3alt5crit3List.add(new TTTuple("0", 5, 0, 0));

        TT2HFLTS exp3alt5crit3 = new TT2HFLTS(exp3alt5crit3List);

        ArrayList<TT2HFLTS> exp3alt1 = new ArrayList<>();
        exp3alt1.add(exp3alt1crit1);
        exp3alt1.add(exp3alt1crit2);
        exp3alt1.add(exp3alt1crit3);

        ArrayList<TT2HFLTS> exp3alt2 = new ArrayList<>();
        exp3alt2.add(exp3alt2crit1);
        exp3alt2.add(exp3alt2crit2);
        exp3alt2.add(exp3alt2crit3);

        ArrayList<TT2HFLTS> exp3alt3 = new ArrayList<>();
        exp3alt3.add(exp3alt3crit1);
        exp3alt3.add(exp3alt3crit2);
        exp3alt3.add(exp3alt3crit3);

        ArrayList<TT2HFLTS> exp3alt4 = new ArrayList<>();
        exp3alt4.add(exp3alt4crit1);
        exp3alt4.add(exp3alt4crit2);
        exp3alt4.add(exp3alt4crit3);

        ArrayList<TT2HFLTS> exp3alt5 = new ArrayList<>();
        exp3alt5.add(exp3alt5crit1);
        exp3alt5.add(exp3alt5crit2);
        exp3alt5.add(exp3alt5crit3);

        ArrayList<ArrayList<TT2HFLTS>> exp3 = new ArrayList<>();
        exp3.add(exp3alt1);
        exp3.add(exp3alt2);
        exp3.add(exp3alt3);
        exp3.add(exp3alt4);
        exp3.add(exp3alt5);

        ArrayList<ArrayList<ArrayList<TT2HFLTS>>> expEstAll = new ArrayList<>();
        expEstAll.add(exp1);
        expEstAll.add(exp2);
        expEstAll.add(exp3);

        // 2. We need to aggregate value for each alternative for each expert (so collapse
        // all estimates by criteria)
        float[] criteriaWeights = {0.5f, 0.3f, 0.2f};

        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperatorFinal = new TT2HFLTSMHTWAOperator();

        ArrayList<ArrayList<TT2HFLTS>> aggEstAll = new ArrayList<>();

        for (ArrayList<ArrayList<TT2HFLTS>> singleExpertMatrix: expEstAll){
            ArrayList<TT2HFLTS> aggregatedForSingle = new ArrayList<>();
            for (ArrayList<TT2HFLTS> alternativePerCriteriaList: singleExpertMatrix){
                TT2HFLTS aggRes = tt2HFLTSMHTWAOperatorFinal.calculate(alternativePerCriteriaList, criteriaWeights, 7);
                aggregatedForSingle.add(aggRes);
            }
            aggEstAll.add(aggregatedForSingle);
        }

        float[] w = {0f, 1f / 3, 2f / 3}; // weighting of alternatives
        float[] p = {0.3f, 0.4f, 0.3f}; // weighting of experts
        float[] v; // final weight for the aggregated estimate

        LinkedHashMap<Float[], TTUtils.PiecewiseLinearLambda> wAsterixFunc = new LinkedHashMap<>();
        // get monotone piecewise function
        for (int k = 0; k < w.length; k += 1) {
            float curW = w[k];
            float nextW;
            if (k - w.length + 1 == 0) {
                nextW = 1f;
            } else {
                nextW = w[k + 1];
            }
            Float[] tmpRange = {curW, nextW};
            TTUtils.PiecewiseLinearLambda l = (int i, float[] wVec, float x) -> {
                float sum = 0f;
                for (int j = 0; j <= i - 1; ++j) {
                    sum += wVec[j];
                }
                // actually here in the article we get i -1 assuming that i is starting from 1
                // that is why we don't need to subtract 1 here, so instead of -(i-1) we just
                // write -i
                sum += wVec[i] * (wVec.length * x - i);
                return sum;
            };
            wAsterixFunc.put(tmpRange, l);
        }

        int numAlt = 5;
        int numExp = 3;
        ArrayList<float[]> altWeights = new ArrayList<>();
        // sort

        //          exp1    exp2    exp3
        // alt 1    x           y       z
        // alt 2
        // alt 3
        // alt 4

        for (int alt = 0; alt < numAlt; ++alt) {
            final ArrayList<TT2HFLTS> tmpEstimates = new ArrayList<>();
            for (int tmpExpIdx = 0; tmpExpIdx < numExp; ++tmpExpIdx){
                tmpEstimates.add(aggEstAll.get(tmpExpIdx).get(alt));
            }

            List<TT2HFLTS> tmpSetsOrdered = TTUtils.sortTT2HFLTS(tmpEstimates, true);

            // now let's make the right weights vector
            float[] tmpP = new float[p.length]; // weighting of experts
            for (int sortedIdx = 0; sortedIdx < p.length; ++sortedIdx){
                int oldIndex = tmpEstimates.indexOf(tmpSetsOrdered.get(sortedIdx));
                tmpP[sortedIdx] = p[oldIndex];
            }

            float[] tmpVec = new float[p.length];
            for (int i = 0; i < p.length; ++i) {
                float sum = 0;
                for (int j = 0; j <= i; j++) {
                    sum += tmpP[j];
                }
                float sumRight = sum - tmpP[i];
                TTUtils.PiecewiseLinearLambda leftLambda = null;
                TTUtils.PiecewiseLinearLambda rightLambda = null;
                ArrayList<Float[]> a = new ArrayList<Float[]>(wAsterixFunc.keySet());

                int positionLeft = 0;
                int positionRight = 0;

                // now we need to choose correct lambda
                for (int kk = 0; kk < a.size(); ++kk) {
                    Float[] key = a.get(kk);
                    if ((sum >= key[0] && sum < key[1] && kk < a.size() - 1) ||
                            (sum >= key[0] && sum <= key[1] && kk == a.size() - 1)) {
                        leftLambda = wAsterixFunc.get(key);
                        positionLeft = kk;
                    }
                    if ((sumRight >= key[0] && sumRight < key[1] && kk < a.size() - 1) ||
                            (sumRight >= key[0] && sumRight <= key[1] && kk == a.size() - 1)) {
                        rightLambda = wAsterixFunc.get(key);
                        positionRight = kk;
                    }
                }

                // compute the corresponding value in v(i) weight vector
                tmpVec[i] = leftLambda.compute(positionLeft, w, sum) - rightLambda.compute(positionRight, w, sumRight);
            }
            altWeights.add(tmpVec);
        }

        // now need to make the calculation for every alternative
        ArrayList<TT2HFLTS> altOverall = new ArrayList<>();
        for (int alt = 0; alt < numAlt; ++alt) {
            ArrayList<TT2HFLTS> tmpSet = new ArrayList<>();
            for (int tmpExpIdx = 0; tmpExpIdx < numExp; ++tmpExpIdx){
                tmpSet.add(aggEstAll.get(tmpExpIdx).get(alt));
            }
//            List<TT2HFLTS> sortedTmpSet = TTUtils.sortTT2HFLTS(tmpSet, true);
            altOverall.add(tt2HFLTSMHTWAOperator.calculate(tmpSet, altWeights.get(alt), 7));
        }

        // now just to sort
        List<TT2HFLTS> sortedAltOverall = TTUtils.sortTT2HFLTS(altOverall, true);
        System.out.println("The best alternative index: " + (altOverall.indexOf(sortedAltOverall.get(0))+1));

        // 4 1 2 3 5
        for (int sortIdx = 0; sortIdx < sortedAltOverall.size(); sortIdx++){
            System.out.println("The original index: " + (altOverall.indexOf(sortedAltOverall.get(sortIdx))+1));
        }

        // just to check
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("3", 7, 0.1f, 3));
        tmp.add(new TTTuple("3", 7, 0.4f, 3));

        TT2HFLTS tmp2HFLTS2 = new TT2HFLTS(tmp);

        ArrayList<TTTuple> tmp2 = new ArrayList<>();
        tmp2.add(new TTTuple("3", 7, 0.1f, 3));
        tmp2.add(new TTTuple("3", 7, 0.4f, 3));
        tmp2.add(new TTTuple("4", 7, -0.4f, 4));
        tmp2.add(new TTTuple("4", 7, 0.1f, 4));

        TT2HFLTS tmp22HFLTS2 = new TT2HFLTS(tmp2);

        ArrayList<TTTuple> tmp3 = new ArrayList<>();
        tmp3.add(new TTTuple("3", 5, -0.4f, 3));

        TT2HFLTS tmp22HFLTS3 = new TT2HFLTS(tmp3);

        ArrayList<TT2HFLTS> tmpSet = new ArrayList<>();

        tmpSet.add(tmp2HFLTS2);
        tmpSet.add(tmp22HFLTS2);
        tmpSet.add(tmp22HFLTS3);

        List<TT2HFLTS> tmpsetsOrdered = TTUtils.sortTT2HFLTS(tmpSet, true);


        TT2HFLTSMHTWOWAOperator tt2HFLTSMHTWOWAOperator = new TT2HFLTSMHTWOWAOperator();
        tt2HFLTSMHTWOWAOperator.calculate(null, null, 7);



    }
}
