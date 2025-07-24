import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class PlayListTest {
    // Note: we do not check the behavior of setCurrent()/getCurrent() with respect
    // to underflow/overflow of the PlayList. Also maintenance of the index
    // as a result of deletions of files from the PlayList is not specified.
    // You may or may not invalidate the current index.
    // Whether arguments of setCurrent() are to be checked for validity is not specified. 
    // The specific behavior for these cases is designed by the implementor.
    //
    // However, for a PlayList pl just created and filled with some files
    //  - getCurrent() should yield 0
    //  - advancing in sequential mode with nextSong() should yield an
    //    incremented value by getCurrent() and
    //  - after advancing up to and beyond the end of the list
    //    getCurrent() should yield 0 again (wrap around)   
    //
    @Test
    public void testGetCurrent() {
        PlayList pl = new PlayList();
        try {
            pl.getList().add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl.getList().add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl.getList().add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            pl.getList().add(new TaggedFile("audiofiles/Haydn - Symphonie # 96 Motiv.ogg"));
        } catch (Exception e) {
            fail("Unable to create AudioFile:" + e.getMessage());
        }
        
        assertEquals("Wrong initialization of current index", 0, pl.getCurrent());
        for (int i=0; i < pl.getList().size(); i++) {
            assertEquals("Wrong current index", i, pl.getCurrent());
            pl.nextSong();
        }
        assertEquals("Wrong current index; expected wrap around", 0 , pl.getCurrent());
    }
    
    // - currentAudioFile() on empty list should return null
    // -                       non null otherwise
    // - Create a play list with n files
    // - Do a sequential test
    
    @Test
    public void testGetSetChangeCurrent() {
        PlayList pl = new PlayList();
        try {
            assertNull(pl.currentAudioFile());
        } catch (IllegalArgumentException e) {
            fail("getCurrentAudioFile() yields exception for empty PlayList");
        }
        try {
            pl.getList().add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl.getList().add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl.getList().add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            pl.getList().add(new TaggedFile("audiofiles/Haydn - Symphonie # 96 Motiv.ogg"));
        } catch (Exception e) {
            fail("Unable to create AudioFile:" + e.getMessage());
        }

        // The next test depends on the fact that we added 4 files to the PlayList
        // Since we added 4 AudioFiles we expect a length of 4
        assertEquals("Wrong size of PlayList", 4, pl.getList().size());
        
        // From here on we do no longer depend on the exact number of files
        // we add to the PlayList in this test
        
        // Test changeCurrent in sequential mode (no random order)
        pl.setCurrent(0);
        for (int i = 0; i < 5 * pl.getList().size(); i++) {
            assertEquals(
                    "Wrong value for getCurrent() in sequential mode",
                    i % pl.getList().size(), pl.getCurrent());
            pl.nextSong();
        }
    }

    
    @Test
    /**
     * Checks if no shuffling is being done in normal, i.e. sequential 
     * mode (randomOrder == false).
     * 
     * - create two playlists with identical Audiofiles
     * - run over first list multiple times using changeCurrent() 
     *   and compare with second list, entries should be identical
     * 
     */
    public void testSequentialMode() {
        PlayList pl = new PlayList();
        try {
            assertNull(pl.currentAudioFile());
        } catch (IllegalArgumentException e) {
            fail("getCurrentAudioFile() yields exception for empty PlayList");
        }
        try {
            pl.getList().add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl.getList().add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl.getList().add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            pl.getList().add(new TaggedFile("audiofiles/Haydn - Symphonie # 96 Motiv.ogg"));
        } catch (Exception e) {
            fail("Unable to create AudioFile:" + e.getMessage());
        }

        // create second list with identical entries 
    	PlayList plCompare = new PlayList();
    	for (int i = 0; i < pl.getList().size(); i++) {
    		plCompare.getList().add(pl.getList().get(i));
    	}

        // Test changeCurrent in sequential mode (no random order)
    	pl.setCurrent(0);
    	for (int i = 0; i < 5 * pl.getList().size(); i++) {
    	    assertEquals(
    	        "Wrong value for currentAudioFile() in sequential mode",
    	         pl.currentAudioFile(), plCompare.currentAudioFile());
    	     pl.nextSong();
    	     plCompare.nextSong();
    	}
    }

    // Here we implicitly check that the elements in the PlayList pl
    // are addressed via index values 0 <= n < pl.size()
    // 
    @Test
    public void testCurrentAudioFile() {
        PlayList pl = new PlayList();
        // A newly created PlayList is empty
        // Thus, getCurrentAudioFile() should yield null
        assertNull(pl.currentAudioFile());
        try {
            pl.getList().add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl.getList().add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl.getList().add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            pl.getList().add(new TaggedFile("audiofiles/Haydn - Symphonie # 96 Motiv.ogg"));
        } catch (Exception e) {
            fail("Unable to create AudioFile:" + e.getMessage());
        }
        
        pl.setCurrent(1);
        AudioFile f1 = pl.currentAudioFile();
        assertEquals("currentAudioFile() yields wrong AudioFile",
                "wellenmeister - tranquility - 02:21", f1.toString());
        pl.setCurrent(0);
        AudioFile f0 = pl.currentAudioFile();
        assertEquals("currentAudioFile() yields wrong AudioFile",
                "Eisbach - Rock 812 - The Sea, the Sky - 05:31", f0.toString());
    }

    @Test
    public void testSaveAndLoadM3U() {
        // Create a play list
        PlayList pl1 = new PlayList();
        try {
            pl1.getList().add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.getList().add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.getList().add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            pl1.getList().add(new TaggedFile("audiofiles/Haydn - Symphonie # 96 Motiv.ogg"));
        } catch (Exception e) {
            fail("Unable to create AudioFile:" + e.getMessage());
        }
        
        // Save PlayList to M3U file
        final String m3uName = "pl.m3u";
        pl1.saveAsM3U(m3uName);
        
        // Check whether we managed to write the file
        File m3u = new File(m3uName);
        assertTrue("Unable to create M3U file", m3u.exists());

        // Append some comments to the M3U file
        try {
            FileWriter fw = new FileWriter(m3u, true);
            String sep = System.getProperty("line.separator");
            fw.write("# comment" + sep);
            fw.write("     " + sep);
            fw.write("# fake.ogg" + sep);
            fw.close();
        } catch (IOException e) {
            fail("Unable to append to M3U file:" + e.toString());
        }
        pl1 = null;
        
        // Try to load the PlayList again
        PlayList pl2 = null;
        
  

        pl2 = new PlayList(m3uName);
        assertEquals(
                "Load PlayList from M3U file yields wrong result",
                "[Eisbach - Rock 812 - The Sea, the Sky - 05:31, "
                        + "wellenmeister - tranquility - 02:21, "
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55, "
                        + "Haydn - Symphonie # 96 Motiv - Musikschnipsel - 00:03]",
                pl2.getList().toString());
        // Cleanup
        m3u.delete();
    }

    @Test
    public void testExceptionDueToNonExistentM3UFile() {
        try {
            new PlayList("does not exist.m3u");
            fail("Expected exception not thrown for non-existing PlayList file!");
        } catch (Exception e) {
            // Expected
        }
    }
    
    @Test
    public void testLoadNewM3UInExistingList() {
        // Create a play list
        PlayList pl1 = new PlayList();
        try {
            pl1.getList().add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.getList().add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.getList().add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            pl1.getList().add(new TaggedFile("audiofiles/Haydn - Symphonie # 96 Motiv.ogg"));
        } catch (Exception e) {
            fail("Unable to create AudioFile:" + e.getMessage());
        }
        
        // Jump one song
        int originalSize = pl1.size();
        pl1.nextSong();
        assertEquals("Current should be at 1", 1, pl1.getCurrent());
        
        // Test m3u-file
        final String m3uName = "pl-load-test.m3u";
        File m3u = new File(m3uName);
        
        // Save file
        pl1.saveAsM3U(m3uName);
        assertEquals("Current should be at 1", 1, pl1.getCurrent());
        assertTrue("Unable to find M3U file", m3u.exists());
        System.out.println(pl1);
        // Load file
        pl1.loadFromM3U(m3uName);
        
        
        assertEquals("Size should be the same as before", originalSize, pl1.size());
        assertEquals("After loading a new m3u file, reset current to zero", 0, pl1.getCurrent());
        assertEquals(
                "Load PlayList from M3U file yields wrong result",
                "[Eisbach - Rock 812 - The Sea, the Sky - 05:31, "
                        + "wellenmeister - tranquility - 02:21, "
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55, "
                        + "Haydn - Symphonie # 96 Motiv - Musikschnipsel - 00:03]",
                pl1.getList().toString());
        
        // Remove file
        m3u.delete();
    }
}
