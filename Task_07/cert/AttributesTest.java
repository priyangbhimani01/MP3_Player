import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class AttributesTest {
    // List of all classes
    private Class[] clazzA = {
            AudioFile.class,
            SampledFile.class,
            TaggedFile.class,
            WavFile.class,
    };

    /**
     * Test all classes in array clazzA
     */
    @Test
    public void testAttributes() {
        for (Class theClass : clazzA) {
            try {
                // Test all attributes of theClass
                for (Field field : theClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    String attShort = field.getName();

                    /**
                     * Attribute names should start with a lowercase letter
                     * Exceptions
                     * - synthetic attributes
                     * - constants (final modifier)
                     */
                    assertTrue("Attribute " + attShort
                            + "; name should begin with lowercase letter!",
                            Character.isLowerCase(attShort.charAt(0))
                                    || field.isSynthetic()
                                    || isFinal(field.getModifiers()));

                    /**
                     * Attributes should not be public
                     * Exceptions:
                     * - static attributes defined with static modifier
                     * - synthetic attributes
                     */
                    int mod = field.getModifiers();
                    assertTrue("attribute '" + attShort + "' should not be public!",
                            !isPublic(mod) || isStatic(field.getModifiers()));
                }
            } catch (SecurityException e) {
                fail(e.toString());
            }
        }
    }
}
