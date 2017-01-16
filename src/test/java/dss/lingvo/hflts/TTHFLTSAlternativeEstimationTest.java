package dss.lingvo.hflts;

import dss.lingvo.t2.TTTuple;
import org.junit.Test;

import static org.junit.Assert.*;

public class TTHFLTSAlternativeEstimationTest {

    @Test
    public void testToString() throws Exception {
        String[] x1c1 = {"vl", "l", "m"};
        TTHFLTS x1c1H = new TTHFLTS(x1c1);

        String[] x1c2 = {"h", "vh"};
        TTHFLTS x1c2H = new TTHFLTS(x1c2);

        String[] x1c3 = {"h"};
        TTHFLTS x1c3H = new TTHFLTS(x1c3);

        TTHFLTS[] x1H = {x1c1H, x1c2H, x1c3H};
        TTHFLTSAlternativeEstimation altEst = new TTHFLTSAlternativeEstimation(x1H);

        assertEquals("[vl, l, m][h, vh][h]", altEst.toString());
    }
}