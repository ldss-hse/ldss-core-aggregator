package dss.lingvo.utils;

import dss.lingvo.hflts.TTHFLTSScale;
import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.utils.models.input.TTAlternativeModel;
import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class TTReportUtilsTest {
    private static TT2HFLTS myHFLTS1;
    private static TT2HFLTS myHFLTS2;
    private static ArrayList<TTAlternativeModel> levels;

    @BeforeClass
    public static void runOnceBeforeClass() throws IOException {
        levels = new ArrayList<>();
        TTAlternativeModel model1 = new TTAlternativeModel();
        model1.setAlternativeID("id1");
        model1.setAlternativeName("name1");
        TTAlternativeModel model2 = new TTAlternativeModel();
        model2.setAlternativeID("id2");
        model2.setAlternativeName("name2");
        levels.add(model1);
        levels.add(model2);
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("2", 7, 0f, 2));
        tmp.add(new TTTuple("3", 7, 0f, 3));
        tmp.add(new TTTuple("4", 7, 0f, 4));

        myHFLTS1 = new TT2HFLTS(tmp);

        ArrayList<TTTuple> tmp2 = new ArrayList<>();
        tmp2.add(new TTTuple("2", 5, 0f, 2));
        tmp2.add(new TTTuple("3", 5, 0f, 3));

        myHFLTS2 = new TT2HFLTS(tmp2);
    }

    @Test
    public void testDumpFinalVector() {
        List<TT2HFLTS> altOverall = new ArrayList<>();
        altOverall.add(myHFLTS1);
        altOverall.add(myHFLTS2);

        String res = TTReportUtils.dumpFinalVector(altOverall, levels, 7);
        assertEquals("id1 &  {\\(s_{2}^{7}\\)\\(s_{3}^{7}\\)\\(s_{4}^{7}\\)} \\\\ \\hline \n" +
                "id2 &  {\\(s_{2}^{7}\\)\\(s_{3}^{7}\\)} \\\\ \\hline \n", res);
    }

    @Test
    public void testDumpFinalSortedZippedVector() {
        Pair<String, TT2HFLTS> pair1 = new Pair<>("id1", myHFLTS1);
        Pair<String, TT2HFLTS> pair2 = new Pair<>("id2", myHFLTS2);

        List<Pair<String, TT2HFLTS>> altOverall = new ArrayList<>();
        altOverall.add(pair1);
        altOverall.add(pair2);

        String res = TTReportUtils.dumpFinalSortedZippedVector(altOverall, levels);
        assertEquals("id1 & name1\\\\ \\hline \n" +
                "id2 & name2\\\\ \\hline \n", res);
    }
}