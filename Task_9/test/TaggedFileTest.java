package studiplayer.cert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import studiplayer.audio.NotPlayableException;
import studiplayer.audio.TaggedFile;

public class TaggedFileTest {
    @SuppressWarnings("rawtypes")
    private Class clazz = TaggedFile.class;
    private TaggedFile f1;
    private TaggedFile f2;

    @Before
    public void setup() { 
    	// Initializer block
    	// This checks the proper connection of constructors already

        try {
            f1 = new TaggedFile("audiofiles/Rock 812.mp3");
            f2 = new TaggedFile("audiofiles/wellenmeister_awakening.ogg");
        } catch (NotPlayableException e) {
        	fail("Problem beim Erzeugen der AudioFile-Objekte: " + e.getMessage());
        }
    }

    @Test
    public void testSuperClass() {
        assertEquals("TaggedFile ist not derived from SampledFile",
                "studiplayer.audio.SampledFile", clazz.getSuperclass()
                        .getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConstructor() {
        try {
            clazz.getDeclaredConstructor(new Class[] { String.class });
        } catch (SecurityException e) {
            fail(e.toString());
        } catch (NoSuchMethodException e) {
            fail("Constructor TaggedFile(String) does not exist");
        }
    }

    // Test the toString implementation in class TaggedFile
    @Test
    public void testToString() {
        assertEquals("toString not correct",
                "Eisbach - Rock 812 - The Sea, the Sky - 05:31", f1.toString());
        assertEquals(
                "toString not correct",
                "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55",
                f2.toString());
    }

    @Test
    public void test_readAndStoreTags_01() {
        assertEquals("Wrong author", "Eisbach", f1.getAuthor());
        assertEquals("Wrong title", "Rock 812", f1.getTitle());
        assertEquals("Wrong album", "The Sea, the Sky", f1.getAlbum());
        assertEquals("Wrong duration", "05:31", f1.formatDuration());

        assertEquals("Wrong author", "Wellenmeister", f2.getAuthor());
        assertEquals("Wrong title", "TANOM Part I: Awakening", f2.getTitle());
        assertEquals("Wrong album", "TheAbsoluteNecessityOfMeaning",
                f2.getAlbum());
        assertEquals("Wrong duration", "05:55", f2.formatDuration());
    }

    // Class TaggedFile does only need one attribute (one for the album)
    // The others should have been moved to some super class
    @Test
    public void testNrAttributes() {
        assertTrue(
                "Do not define any local variables of methods as attributes?",
                clazz.getDeclaredFields().length == 1);
    }

    @Test
    public void testInvalid() {
        try {
            new TaggedFile("audiofiles/Rock 812.cut.mp3");
            fail("NotPlayableException expected for erroneous MP3 file Rock 812.cut.mp3");
        } catch (NotPlayableException e) {
            // Expected
        } catch (RuntimeException e) {
            e.printStackTrace();
            // RuntimeException of studiplayer.basic.TagReader.readTags
            // was not mapped to NotPlayableException
            fail("NotPlayableException expected for erroneous MP3 file Rock 812.cut.mp3");
        }
    }

}
