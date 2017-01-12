package dss.lingvo.hflts;

import java.util.ArrayList;
import java.util.Collections;

public class TTHFLTSCalcPrefOperator {
    public float[][] calculate(ArrayList<String[]> bounds, TTHFLTSScale scale) {
        float[][] prefMatrix = new float[bounds.size()][bounds.size()];
        for (int i = 0; i < bounds.size(); i++) {
            for (int j = 0; j < bounds.size(); j++) {
                if (i == j) {
                    prefMatrix[i][j] = 0;
                    continue;
                }
                prefMatrix[i][j] = findPreference(bounds.get(i), bounds.get(j), scale);
            }
        }
        return prefMatrix;
    }

    private float findPreference(String[] a, String[] b, TTHFLTSScale scale) {
        int a1 = scale.termPosition(a[0]) + 1;
        int a2 = scale.termPosition(a[1]) + 1;
        int b1 = scale.termPosition(b[0]) + 1;
        int b2 = scale.termPosition(b[1]) + 1;
        return ((float) (Math.max(0, a2 - b1) - Math.max(0, a1 - b2))) / ((a2 - a1) + (b2 - b1));
    }

    public float[][] getRelationsMatrix(float[][] prefMatrix) {
        float[][] prefRelMatrix = new float[prefMatrix.length][prefMatrix.length];
        for (int i = 0; i < prefMatrix.length; ++i) {
            for (int j = 0; j < prefMatrix.length; ++j) {
                prefRelMatrix[j][i] = (float) Math.max(prefMatrix[j][i] - prefMatrix[i][j], 0.);
            }
        }
        return prefRelMatrix;
    }

    public float[] getNonDominanceVector(float[][] prefRelMatrix) {
        float[] nonDominanceVector = new float[prefRelMatrix.length];
        for (int i = 0; i < prefRelMatrix.length; ++i) {
            ArrayList<Float> options = new ArrayList<Float>();

            for (int j = 0; j < prefRelMatrix.length; ++j){
                if (i == j) {
                    continue;
                }
                options.add(1-prefRelMatrix[j][i]);
            }
            nonDominanceVector[i] = Collections.min(options);
        }
        return nonDominanceVector;
    }

    public int[] getNonDominatedAlternatives(float[] nonDominanceVector) {
        ArrayList<Float> options = new ArrayList<Float>();
        ArrayList<Integer> resList = new ArrayList<Integer>();
        for (int i=0; i < nonDominanceVector.length; ++i){
            options.add(nonDominanceVector[i]);
        }
        int minIndex = 0;
        while ((minIndex = options.indexOf(Collections.min(options))) > 0 && !resList.contains(minIndex)){
            resList.add(minIndex);
        }
        int[] res = new int[resList.size()];
        for (int i = 0; i < resList.size(); ++i){
            res[i] = resList.get(i);
        }
        return res;
    }
}
