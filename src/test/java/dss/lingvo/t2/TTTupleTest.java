package dss.lingvo.t2;

import dss.lingvo.utils.TTConstants;
import org.junit.Test;

import static org.junit.Assert.*;

public class TTTupleTest {
    @Test
    public void testHashCode() throws Exception {
        TTTuple tmp = new TTTuple("5", 9, -0.5f, 5);

        int hash = tmp.hashCode();

        assertEquals(17989, hash);
    }

    @Test
    public void testEquals1() throws Exception {
        TTTuple tmp = new TTTuple("5", 9, -0.5f, 5);
        TTTuple tmp2 = new TTTuple("5", 9, -0.5f, 5);

        boolean res = tmp.equals(tmp2);

        assertEquals(true, res);
    }

    @Test
    public void testEquals2() throws Exception {
        TTTuple tmp = new TTTuple("5", 9, -0.5f, 5);
        TTTuple tmp2 = new TTTuple("5", 9, 0f, 6);

        boolean res = tmp.equals(tmp2);

        assertEquals(false, res);
    }

    @Test
    public void testEquals3() throws Exception {
        TTTuple tmp = new TTTuple("5", 9, -0.5f, 5);

        boolean res = tmp.equals(null);

        assertEquals(false, res);
    }

    @Test
    public void testEquals4() throws Exception {
        TTTuple tmp = new TTTuple("5", 9, -0.5f, 5);

        boolean res = tmp.equals("fwe");

        assertEquals(false, res);
    }

    @Test
    public void testToString() throws Exception {
        TTTuple tmp = new TTTuple("5", 9, -0.5f, 5);

        String res = tmp.toString();

        assertEquals("<2Tuple> { label: 5; index: 5; translation: -0.5; }", res);
    }

    @Test
    public void testConstructor() throws Exception {
        TTTuple tmp = new TTTuple("5", 9, -0.5f, 5);

        assertEquals("5", tmp.getLabel());
        assertEquals(5, tmp.getIndex());
        assertEquals(9, tmp.getScaleSize());
        assertEquals(-0.5f, tmp.getTranslation(), TTConstants.FLOAT_PRECISION_DELTA);
    }
}