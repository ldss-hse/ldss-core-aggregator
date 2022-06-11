package dss.lingvo.t2hflts;

import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTConstants;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TT2HFLTSTest {
    private static TT2HFLTS myHFLTS1;
    private static TT2HFLTS myHFLTS2;

    @BeforeClass
    public static void runOnceBeforeClass() {
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("2",7,0f,2));
        tmp.add(new TTTuple("3",7,0f,3));
        tmp.add(new TTTuple("4",7,0f,4));

        myHFLTS1 = new TT2HFLTS(tmp);

        ArrayList<TTTuple> tmp2 = new ArrayList<>();
        tmp2.add(new TTTuple("2",5,0f,2));
        tmp2.add(new TTTuple("3",5,0f,3));

        myHFLTS2 = new TT2HFLTS(tmp2);
    }

    @Test
    public void testGetTerms() throws Exception {
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(new TTTuple("2",7,0f,2));
        tmp.add(new TTTuple("3",7,0f,3));
        tmp.add(new TTTuple("4",7,0f,4));

        assertEquals(tmp, myHFLTS1.getTerms());
    }

    @Test
    public void testGetSize() throws Exception {
        assertEquals(3, myHFLTS1.getSize());
        assertEquals(2, myHFLTS2.getSize());
    }

    @Test
    public void testGetScore() throws Exception {
        assertEquals(0.5f, myHFLTS1.getScore(), TTConstants.FLOAT_PRECISION_DELTA);
        assertEquals(0.625f, myHFLTS2.getScore(), TTConstants.FLOAT_PRECISION_DELTA);
    }

    @Test
    public void testGetVariance() throws Exception {
        assertEquals(0.1361f, myHFLTS1.getVariance(), TTConstants.FLOAT_PRECISION_DELTA);
    }

    @Test
    public void testEquals() throws Exception {
        assertNotEquals(myHFLTS1,myHFLTS2);
    }

    @Test
    public void testEquals2() throws Exception {
        assertNotEquals(myHFLTS1,null);
    }

    @Test
    public void testEquals3() throws Exception {
        assertNotEquals(myHFLTS1,"");
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("" +
                "<2Tuple> { label: 2; index: 2; translation: 0.0; }" +
                "<2Tuple> { label: 3; index: 3; translation: 0.0; }" +
                "<2Tuple> { label: 4; index: 4; translation: 0.0; }", myHFLTS1.toString());
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(18276212, myHFLTS1.hashCode());
    }

    @Test
    public void testHashCode2() throws Exception {
        ArrayList<TTTuple> tmp = new ArrayList<>();
        tmp.add(null);
        tmp.add(null);
        TT2HFLTS tmpHFLTS = new TT2HFLTS(tmp);
        assertEquals(16337, tmpHFLTS.hashCode());
    }
}
