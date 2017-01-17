package dss.lingvo.hflts;

import dss.lingvo.utils.TTConstants;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

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

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor constructor = TTHFLTSUtils.class.getDeclaredConstructor();
        assertTrue("Constructor is not private", Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
