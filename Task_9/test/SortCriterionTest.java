package studiplayer.cert;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import studiplayer.audio.SortCriterion;

public class SortCriterionTest {

    @SuppressWarnings("rawtypes")
    private Class clazz = SortCriterion.class;

    @Test
    public void testEntries() {
        Object[] consts = clazz.getEnumConstants();
        assertNotNull("Kein Enum", consts);
        assertTrue("Falsche Anzahl Enum-Entries", consts.length == 5);
    }
}
