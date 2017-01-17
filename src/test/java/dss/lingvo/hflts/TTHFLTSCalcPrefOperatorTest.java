package dss.lingvo.hflts;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TTHFLTSCalcPrefOperatorTest {
    private static TTHFLTSCalcPrefOperator prefOperator;
    private static TTHFLTSScale singleScale;

    @BeforeClass
    public static void runOnceBeforeClass() {
        String[] scaleString = {"n", "vl", "l", "m", "h", "vh", "p"};
        singleScale = new TTHFLTSScale(scaleString);
        prefOperator = new TTHFLTSCalcPrefOperator();
    }

    @Test
    public void testCalculate() throws Exception {
        ArrayList<String[]> bounds = new ArrayList<>();
        String[] r1 = {"m", "h"};
        String[] r2 = {"l", "m"};
        String[] r3 = {"l", "h"};
        bounds.add(r1);
        bounds.add(r2);
        bounds.add(r3);
        float[][] matrix = prefOperator.calculate(bounds, singleScale);
        float[][] expMatrix = {{0f, 1f, 0.6666667f},
                {0f, 0f, 0.33333334f},
                {0.33333334f, 0.6666667f, 0f}};

        assertArrayEquals(expMatrix, matrix);
    }

    @Test
    public void testGetRelationsMatrix() throws Exception {
        float[][] relMatrix = {{0f, 1f, 0.6666667f},
                {0f, 0f, 0.33333334f},
                {0.33333334f, 0.6666667f, 0f}};
        float[][] matrix = prefOperator.getRelationsMatrix(relMatrix);
        float [][] expMatrix = {{0f, 1f, 0.33333334f},
                {0f, 0f, 0f},
                {0f, 0.33333334f, 0f}};

        assertArrayEquals(expMatrix, matrix);
    }

    @Test
    public void testGetNonDominanceVector() throws Exception {
        float [][] prefRelMatrix = {{0f, 1f, 0.33333334f},
                {0f, 0f, 0f},
                {0f, 0.33333334f, 0f}};
        float[] res = prefOperator.getNonDominanceVector(prefRelMatrix);
        float [] expRes = {1f, 0f, 0.6666666f};

        assertArrayEquals(expRes, res, 0.0001f);
    }

    @Test
    public void testGetNonDominatedAlternatives1() throws Exception {
        float [] nonDominanceVector = {1f, 0f, 0.6666666f};
        int[] res = prefOperator.getNonDominatedAlternatives(nonDominanceVector);
        int [] expRes = {1};

        assertArrayEquals(expRes, res);
    }

    @Test
    public void testGetNonDominatedAlternatives2() throws Exception {
        float [] nonDominanceVector = new float[1];
        int[] res = prefOperator.getNonDominatedAlternatives(nonDominanceVector);
        int [] expRes = {};

        assertArrayEquals(expRes, res);
    }

}