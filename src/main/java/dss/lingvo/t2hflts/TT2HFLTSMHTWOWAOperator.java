package dss.lingvo.t2hflts;

import dss.lingvo.utils.TTUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TT2HFLTSMHTWOWAOperator {

    public ArrayList<TT2HFLTS> calculate(int numAlt,
                                         int numExp,
                                         float[] p,
                                         float[] w,
                                         ArrayList<ArrayList<TT2HFLTS>> aggEstAll,
                                         int targetScaleSize) {
        // now need to make the calculation for every alternative
        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperator = new TT2HFLTSMHTWAOperator();
        ArrayList<float[]> altWeights = calculateAlternativeWeights(numAlt, numExp, p, w, aggEstAll);

        ArrayList<TT2HFLTS> altOverall = new ArrayList<>();
        for (int alt = 0; alt < numAlt; ++alt) {
            ArrayList<TT2HFLTS> tmpSet = new ArrayList<>();
            for (int tmpExpIdx = 0; tmpExpIdx < numExp; ++tmpExpIdx) {
                tmpSet.add(aggEstAll.get(tmpExpIdx).get(alt));
            }
            // what if we also sort them??
            List<TT2HFLTS> newSet = TTUtils.sortTT2HFLTS(tmpSet, true);
            altOverall.add(tt2HFLTSMHTWAOperator.calculate(newSet, altWeights.get(alt), targetScaleSize));
        }
        return altOverall;
    }

    private LinkedHashMap<Float[], TTUtils.PiecewiseLinearLambda> prepareAsterixLambdas(float[] w) {
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
        return wAsterixFunc;
    }

    public ArrayList<float[]> calculateAlternativeWeights(int numAlt,
                                                          int numExp,
                                                          float[] p,
                                                          float[] w,
                                                          ArrayList<ArrayList<TT2HFLTS>> aggEstAll) {
        LinkedHashMap<Float[], TTUtils.PiecewiseLinearLambda> wAsterixFunc = prepareAsterixLambdas(w);
        ArrayList<float[]> altWeights = new ArrayList<>();
        // sort

        //          exp1    exp2    exp3
        // alt 1    x           y       z
        // alt 2
        // alt 3
        // alt 4

        for (int alt = 0; alt < numAlt; ++alt) {
            final ArrayList<TT2HFLTS> tmpEstimates = new ArrayList<>();
            for (int tmpExpIdx = 0; tmpExpIdx < numExp; ++tmpExpIdx) {
                tmpEstimates.add(aggEstAll.get(tmpExpIdx).get(alt));
            }

            List<TT2HFLTS> tmpSetsOrdered = TTUtils.sortTT2HFLTS(tmpEstimates, true);

            // now let's make the right weights vector
            float[] tmpP = new float[p.length]; // weighting of experts
            for (int sortedIdx = 0; sortedIdx < p.length; ++sortedIdx) {
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
        return altWeights;
    }
}
