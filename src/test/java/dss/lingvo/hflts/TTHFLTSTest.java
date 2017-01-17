package dss.lingvo.hflts;

import org.junit.Test;

import static org.junit.Assert.*;

public class TTHFLTSTest {

    @Test
    public void testGetTerms() throws Exception {
        String[] x1 = {"vl", "l", "m"};
        TTHFLTS x1c1H = new TTHFLTS(x1);
        assertArrayEquals(x1, x1c1H.getTerms());
    }

    @Test
    public void testEquals1() throws Exception {
        String[] x1 = {"vl", "l", "m"};
        TTHFLTS x1c1H = new TTHFLTS(x1);

        String[] x2 = {"vl", "l", "m"};
        TTHFLTS x2c1H = new TTHFLTS(x2);

        boolean res = x1c1H.equals(x2c1H);
        assertEquals(true, res);
    }

    @Test
    public void testEquals2() throws Exception {
        String[] x1 = {"vl", "l"};
        TTHFLTS x1c1H = new TTHFLTS(x1);

        String[] x2 = {"vl", "l", "m"};
        TTHFLTS x2c1H = new TTHFLTS(x2);

        boolean res = x1c1H.equals(x2c1H);
        assertEquals(false, res);
    }

    @Test
    public void testToString() throws Exception {
        String[] x1 = {"vl", "l"};
        TTHFLTS x1c1H = new TTHFLTS(x1);

        String exp = "[vl, l]";

        assertEquals(exp, x1c1H.toString());
    }

    @Test
    public void testHashCode() throws Exception {
        String[] x1 = {"vl", "l"};
        TTHFLTS x1c1H = new TTHFLTS(x1);

        int res = x1c1H.hashCode();

        assertEquals(118342, res);

    }
}