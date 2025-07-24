package studiplayer.cert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Test;

import studiplayer.audio.AudioFile;
import studiplayer.audio.AudioFileFactory;
import studiplayer.audio.NotPlayableException;

public class NotPlayableExceptionTest {
    @Test
    public void testConstructors() {
        try {
            throw new NotPlayableException("abc-path", "def-msg");
        } catch (NotPlayableException e) {
            String ts = e.toString();
            assertTrue("toString von NotPlayableException ungenuegend", ts.contains("abc-path")
                    && ts.contains("studiplayer.audio.NotPlayableException")
                    && ts.contains("def-msg"));
        }
        try {
            throw new NotPlayableException("abc-path", new RuntimeException("def-msg"));
        } catch (NotPlayableException e) {
            String ts = e.toString();
            assertTrue("toString von NotPlayableException ungenuegend", ts.contains("abc-path")
                    && ts.contains("studiplayer.audio.NotPlayableException")
                    && ts.contains("def-msg"));
        }
        try {
            throw new NotPlayableException("abc-path", "def-msg", new RuntimeException(
                    "ghi"));
        } catch (NotPlayableException e) {
            String ts = e.toString();
            assertTrue("toString von NotPlayableException ungenuegend", ts.contains("abc-path")
                    && ts.contains("studiplayer.audio.NotPlayableException")
                    && ts.contains("def-msg"));

            assertEquals("Root-Cause falsch", "java.lang.RuntimeException", e
                    .getCause().getClass().getName());
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean checkException(Method meth) {
        Class[] ex = meth.getExceptionTypes();
        if (ex.length == 0)
            return false;
        if (studiplayer.audio.NotPlayableException.class.equals(ex[0])) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    private boolean checkException(Constructor meth) {
        Class[] ex = meth.getExceptionTypes();
        if (ex.length == 0)
            return false;
        if (studiplayer.audio.NotPlayableException.class.equals(ex[0])) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testDeclarations() {
        Method meth;
        Constructor ctor;
        try {
            meth = AudioFile.class.getMethod("play", new Class[] {});
            assertTrue(meth.getName()
                    + " deklariert keine NotPlayableException",
                    checkException(meth));
            ctor = AudioFile.class
                    .getDeclaredConstructor(new Class[] { java.lang.String.class });
            assertTrue(ctor.getName()
                    + " deklariert keine NotPlayableException",
                    checkException(ctor));
            meth = AudioFileFactory.class.getMethod("createAudioFile",
                    new Class[] { java.lang.String.class });
            assertTrue(meth.getName()
                    + " deklariert keine NotPlayableException",
                    checkException(meth));
        } catch (SecurityException e) {
            fail(e.toString());
        } catch (NoSuchMethodException e) {
            fail("Geforderte Methode existiert nicht " + e);
        }
    }
}
