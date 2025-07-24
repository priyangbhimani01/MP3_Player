package studiplayer.cert;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

import studiplayer.audio.AlbumComparator;
import studiplayer.audio.AudioFile;
import studiplayer.audio.AudioFileFactory;
import studiplayer.audio.AuthorComparator;
import studiplayer.audio.DurationComparator;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SampledFile;
import studiplayer.audio.SortCriterion;
import studiplayer.audio.TaggedFile;
import studiplayer.audio.TitleComparator;
import studiplayer.audio.WavFile;
import studiplayer.ui.Player;

@SuppressWarnings("rawtypes")
public class AttributesTest {
    // Liste aller Klassen
    private Class[] clazzA = {
            AlbumComparator.class,
            AudioFile.class,
            AudioFileFactory.class,
            AuthorComparator.class,
            DurationComparator.class,
            NotPlayableException.class,
            PlayList.class,
            SampledFile.class,
            SortCriterion.class,
            TaggedFile.class,
            TitleComparator.class,
            WavFile.class,
            Player.class
            };

    @Test
    public void testAttributes() {
        // Teste alle Klassen im Array clazzA
        for (Class theClass : clazzA) {
            try {
                // Teste alle Attribute
                for (Field field : theClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    String attShort = field.getName();

                    // Attributnamen beginnen mit kleinen Buchstaben
                    //
                    // Ausnahmen:
                    // - synthetische Attribute (etwa Expansionen von ENUMS)
                    // - Konstanten: also Modifier final
                    assertTrue(
                            "Attribut "
                                    + attShort
                                    + "; Name des Attributs soll mit Kleinbuchstaben anfangen",
                            Character.isLowerCase(attShort.charAt(0))
                                    || field.isSynthetic()
                                    || isFinal(field.getModifiers()) );

                    // Attribute sind nicht public
                    //
                    // Ausnahmen:
                    // - statische Attribute: also Modifier static
                    // - synthetische Attribute (diese werden aber auch immer static generiert)
                    int mod = field.getModifiers();
                    // Kodierung der Implikation: a -> b == ~a || b        
                    assertTrue("Zugriff auf Attribut'" + attShort
                            + "' darf nicht public sein!", 
                            !isPublic(mod) || isStatic(field.getModifiers())                            
                    );
                }
            } catch (SecurityException e) {
                fail(e.toString());
            }
        }
    }
}
