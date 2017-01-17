package dss.lingvo.hflts;

import org.junit.Test;

import static org.junit.Assert.*;

public class TTHFLTSScaleTest {
    @Test
    public void testGetGranularity() throws Exception {
        String[] scaleString = {"n", "vl", "l", "m", "h", "vh", "p"};
        TTHFLTSScale singleScale = new TTHFLTSScale(scaleString);

        assertEquals(7, singleScale.getGranularity());
    }
}