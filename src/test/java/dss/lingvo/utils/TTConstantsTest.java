package dss.lingvo.utils;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class TTConstantsTest {
    @Test
    public void testEquals1() throws Exception {
        assertEquals(0.0001, TTConstants.FLOAT_PRECISION_DELTA, 0.0001);
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor constructor = TTConstants.class.getDeclaredConstructor();
        assertTrue("Constructor is not private", Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }
}