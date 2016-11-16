package dss.lingvo;

import dss.lingvo.SampleClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by demidovs on 16.11.16.
 */
public class SampleClassTest {

    @Test
    public void testCallSample() throws Exception {
        assertEquals("Hey, there", SampleClass.callSample());
    }
}