package dss.lingvo.hflts;

import org.junit.Test;
import static org.junit.Assert.*;

public class TTHFLTSUtilsTest {
    @Test
    public void testCallSample() throws Exception {
        String [] referenceSet = {"bad", "good", "best", "awesome"};
        TTHFLTS referenceHFLTS = new TTHFLTS(referenceSet);
        String [] desiredSet = {"good", "best"};
        TTHFLTS desiredHFLTS = new TTHFLTS(desiredSet);
        assertArrayEquals(desiredHFLTS.getTerms(), TTHFLTSUtils.getSubset(referenceHFLTS, 1,3).getTerms());
    }
}
