package studiplayer.cert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;
import studiplayer.audio.TaggedFile;
import studiplayer.audio.WavFile;

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
    //  - advancing in sequential mode with changeCurrent() should yield an
    //    incremented value by getCurrent() and
    //  - after advancing up to and beyond the end of the list
    //    getCurrent() should yield 0 again (wrap around)   
    //

    // Here we test, that currentAudioFiles yields an AudioFile while
    // looping through them. At the end of this iteration, AudioFile should
    // start over.
    @Test
    public void testLoopAudioFiles() {
        PlayList pl = new PlayList();
        List<AudioFile> files;
        
        // A newly created PlayList is empty
        // Thus, getCurrentAudioFile() should yield null
        assertNull("currentAudioFile() should yield null at beginning", pl.currentAudioFile());
        
        try {
            files = Arrays.asList(
                    new TaggedFile("audiofiles/Rock 812.mp3"),
                    new WavFile("audiofiles/wellenmeister - tranquility.wav"),
                    new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            for(AudioFile file : files) {
                pl.add(file);
            }
        } catch (NotPlayableException e) {
            fail("Unable to create AudioFile:" + e.getMessage());
            return;
        }
        
        // Check that list contains expected amount of AudioFiles
        assertEquals("Wrong size of PlayList", 3, pl.size());
        
        for(int i = 0; i < 5 * files.size(); i ++) {
            AudioFile cur = pl.currentAudioFile();
            assertEquals("currentAudioFile() yields wrong AudioFile",
                    files.get(i % files.size()).toString(), cur.toString());
            pl.nextSong();
        }
    }

    // Here we test, that currentAudioFiles yields an AudioFile while
    // looping through them. At the end of this iteration, AudioFile should
    // start over.
    @Test
    public void testSingleFileLoop() {
        PlayList pl = new PlayList();
        AudioFile file;
        
        // A newly created PlayList is empty
        // Thus, getCurrentAudioFile() should yield null
        assertNull("currentAudioFile() should yield null at beginning", pl.currentAudioFile());
        
        try {
            file = new TaggedFile("audiofiles/Rock 812.mp3");
            pl.add(file);
        } catch (NotPlayableException e) {
            fail("Unable to create AudioFile:" + e.getMessage());
            return;
        }
        
        // Check that list contains expected amount of AudioFiles
        assertEquals("Wrong size of PlayList", 1, pl.size());
        
        for(int i = 0; i < 5; i ++) {
            AudioFile cur = pl.currentAudioFile();
            assertEquals("currentAudioFile() yields wrong AudioFile",
                    file.toString(), cur.toString());
            pl.nextSong();
        }
    }

    @Test
    public void testPlayListIsEmptyAtBeginning() {
        PlayList pl = new PlayList();
        try {
            assertNull(pl.currentAudioFile());
        } catch (IllegalArgumentException e) {
            fail("getCurrentAudioFile() yields exception for empty PlayList");
        }
    }

    @Test
    public void testNextSongOnEmptyPlayList() {
        PlayList pl = new PlayList();
        pl.nextSong();
        try {
            assertNull(pl.currentAudioFile());
        } catch (IllegalArgumentException e) {
            fail("currentAudioFile() yields exception for empty PlayList");
        }
    }

    // When creating a new PlayList and directly calling nextSong,
    // the next call of currentAudioFile has to be the second AudioFile.
    @Test
    public void testCallingNextSongFirst() {
        PlayList pl = new PlayList();
        List<AudioFile> files;
        // A newly created PlayList is empty
        // Thus, getCurrentAudioFile() should yield null
        assertNull("currentAudioFile() should yield null at beginning", pl.currentAudioFile());
        try {
            files = Arrays.asList(
                    new TaggedFile("audiofiles/Rock 812.mp3"),
                    new WavFile("audiofiles/wellenmeister - tranquility.wav"),
                    new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
            for(AudioFile file : files) {
                pl.add(file);
            }
        } catch (NotPlayableException e) {
            fail("Unable to create AudioFile:" + e.getMessage());
            return;
        }
        
        assertEquals("Wrong size of PlayList", 3, pl.size());
        
        pl.nextSong();
        AudioFile cur = pl.currentAudioFile();
        assertEquals("currentAudioFile() yields wrong AudioFile",
                files.get(1).toString(), cur.toString());
    }

    @Test
    public void testSaveAndLoadM3U() {
        // Create a play list
        PlayList pl1 = new PlayList();
        try {
            pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        } catch (NotPlayableException e) {
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
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55]",
                pl2.toString());
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
    public void testLenientLoadM3U() {
        final String m3uName = "pl.m3u";
        File m3u = new File(m3uName);

        // Create a play list file with non-existent names for audio files
        try {
            FileWriter fw = new FileWriter(m3u);
            fw.write("gibt es nicht.wav\n");
            fw.write("und das auch nicht.ogg\n");
            fw.write("und das - sowieso nicht.mp3\n");
            fw.close();
        } catch (IOException e) {
            fail(e.toString());
        }
        
        // Now try to load this M3U file into a PlayList
        // We expect the loader to be lenient in the sense
        // that it either skips non-existing files
        // or stops after reading the first non-existing file
        PlayList pl = null;
        pl = new PlayList(m3uName);
        assertEquals("PlayList generates entries for non-existent AudioFiles",
                0, pl.size());
        // Cleanup
        m3u.delete();
    }

    @Test
    public void testLenientLoadM3UHarder() {
        final String m3uName = "pl.m3u";
        File m3u = new File(m3uName);

        // Create a play list file with non-existent names for audio files
        // and an existing file at the end of the list
        try {
            FileWriter fw = new FileWriter(m3u);
            fw.write("gibt es nicht.wav\n");
            fw.write("und das auch nicht.ogg\n");
            fw.write("und das - sowieso nicht.mp3\n");
            fw.write("audiofiles/Rock 812.mp3");
            fw.close();
        } catch (IOException e) {
            fail(e.toString());
        }
        
        // Now try to load this M3U file into a PlayList
        // We expect the loader to be lenient in the sense
        // that it skips non-existing files
        // and loads the existing file.
        //
        // In order to pass this test the loader must contain
        // the catch clauses in the right places
        PlayList pl = null;
        pl = new PlayList(m3uName);
        assertEquals(
                "Load PlayList from M3U file yields wrong result",
                "[Eisbach - Rock 812 - The Sea, the Sky - 05:31]",
                pl.getList().toString());
        
        // Cleanup
        m3u.delete();
    }
    
    private List<AudioFile> loop(PlayList list) {
        List<AudioFile> files = new ArrayList<>();
        AudioFile first = list.currentAudioFile();
        files.add(first);
        AudioFile cur;
        do {
            list.nextSong();
            cur = list.currentAudioFile();
            if(cur != first) {
                files.add(cur);
            }
        } while (cur != first);
        return files;
    }

    /**
     * Here we check sorting according to album criteria.
     * Note that sorting of strings with compareTo() depends on
     * the lexicographic order of letters in the ASCII code.
     * Therefore, "TANOM..." < "kein.wav..."
     */
    @Test
    public void testSortByAlbum() {
        // create a PlayList
        PlayList pl1 = new PlayList();
        try {
            pl1.add(new TaggedFile("audiofiles/kein.wav.sondern.ogg"));
            pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        } catch (NotPlayableException e) {
            fail("Cannot create AudioFile:" + e.getMessage());
        }
        pl1.setSortCriterion(SortCriterion.ALBUM);
        assertEquals(
                "Sorting according to criterion album is not correct",
                "[wellenmeister - tranquility - 02:21, "
                        + "kein.wav.sondern - 00:00, "
                        + "Eisbach - Rock 812 - The Sea, the Sky - 05:31, "
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55]",
                loop(pl1).toString());
    }
    /**
     * Here we check sorting according to author criteria.
     * Note that sorting of strings with compareTo() depends on
     * the lexicographic order of letters in the ASCII code.
     * Therefore, "TANOM..." < "kein.wav..."
     */
    @Test
    public void testSortByAuthor() {
        // create a PlayList
        PlayList pl1 = new PlayList();
        try {
            pl1.add(new TaggedFile("audiofiles/kein.wav.sondern.ogg"));
            pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        } catch (NotPlayableException e) {
            fail("Cannot create AudioFile:" + e.getMessage());
        }
        pl1.setSortCriterion(SortCriterion.AUTHOR);
        assertEquals(
                "Sorting according to criterion author is not correct",
                "[kein.wav.sondern - 00:00, "
                        + "Eisbach - Rock 812 - The Sea, the Sky - 05:31, "
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55, "
                        + "wellenmeister - tranquility - 02:21]",
                        loop(pl1).toString());
    }
    /**
     * Here we check sorting according to title criteria.
     * Note that sorting of strings with compareTo() depends on
     * the lexicographic order of letters in the ASCII code.
     * Therefore, "TANOM..." < "kein.wav..."
     */
    @Test
    public void testSortByTitle() {
        // create a PlayList
        PlayList pl1 = new PlayList();
        try {
            pl1.add(new TaggedFile("audiofiles/kein.wav.sondern.ogg"));
            pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        } catch (NotPlayableException e) {
            fail("Cannot create AudioFile:" + e.getMessage());
        }
        pl1.setSortCriterion(SortCriterion.TITLE);
        assertEquals(
                "Sorting according to criterion title is not correct",
                "[Eisbach - Rock 812 - The Sea, the Sky - 05:31, "
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55, "
                        + "kein.wav.sondern - 00:00, "
                        + "wellenmeister - tranquility - 02:21]",
                        loop(pl1).toString());
    }
    /**
     * Here we check sorting according to duration criteria.
     * Note that sorting of strings with compareTo() depends on
     * the lexicographic order of letters in the ASCII code.
     * Therefore, "TANOM..." < "kein.wav..."
     */
    @Test
    public void testSortByDuration() {
        // create a PlayList
        PlayList pl1 = new PlayList();
        try {
            pl1.add(new TaggedFile("audiofiles/kein.wav.sondern.ogg"));
            pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        } catch (NotPlayableException e) {
            fail("Cannot create AudioFile:" + e.getMessage());
        }
        pl1.setSortCriterion(SortCriterion.DURATION);
        assertEquals(
                "Sorting according to criterion duration is not correct",
                "[kein.wav.sondern - 00:00, "
                        + "wellenmeister - tranquility - 02:21, "
                        + "Eisbach - Rock 812 - The Sea, the Sky - 05:31, "
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55]",
                        loop(pl1).toString());
    }
    
    /**
     * Here we check search filter.
     */
    @Test
    public void testSearch() {
        // create a PlayList
        PlayList pl1 = new PlayList();
        try {
            pl1.add(new TaggedFile("audiofiles/kein.wav.sondern.ogg"));
            pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
            pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
            pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
        } catch (NotPlayableException e) {
            fail("Cannot create AudioFile:" + e.getMessage());
        }
        pl1.setSearch("meister");
        assertEquals(
                "Sorting according to criterion duration is not correct",
                "[wellenmeister - tranquility - 02:21, "
                        + "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55]",
                        loop(pl1).toString());
    }
    
    /**
     * Here we set search and sort, iterate and unset these again to check
     * if original order is not changed.
     */
    @Test
    public void testSearchAndSortDoesNotChangeOrder() {
        PlayList list = new PlayList();

        List<AudioFile> check;
        try {
            check = Arrays.asList(
                    new TaggedFile("audiofiles/Rock 812.mp3"),
                    new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg"),
                    new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
        } catch (NotPlayableException e) {
            fail("Cannot create AudioFile:" + e.getMessage());
            return;
        }

        for(AudioFile file : check) {
            list.add(file);
        }
        
        list.setSortCriterion(SortCriterion.TITLE);
        list.setSearch("Eisbach");
        Iterator<AudioFile> tmp = list.iterator();
        assertTrue("Iterator should provide one more result", tmp.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as first iteration result", check.get(2), tmp.next());

        list.setSortCriterion(SortCriterion.DEFAULT);
        list.setSearch("");
        
        Iterator<AudioFile> iterator = list.iterator();
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as first iteration result", check.get(0), iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Motiv 5. Symphonie von Beethoven.ogg as second iteration result", check.get(1), iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as third iteration result", check.get(2), iterator.next());
        assertFalse("Should only provide two elements", iterator.hasNext());
    }

    /**
     * Test if jump to is working as expected. Jump to should change iteration
     * and return value of currentAudioFile.
     */
    @Test
    public void testJumpTo() throws Exception {
        System.out.println("---- Test JumpTo ----");
        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
        AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

        PlayList list = new PlayList();
        list.add(tf1);
        list.add(tf2);
        list.add(tf3);
        
        list.jumpToAudioFile(tf2);
        System.out.println(list.currentAudioFile());
        assertEquals(tf2, list.currentAudioFile());
        
        list.nextSong();
        System.out.println(list.currentAudioFile());
        assertEquals(tf3, list.currentAudioFile());
        
        list.nextSong();
        System.out.println(list.currentAudioFile());
        assertEquals(tf1, list.currentAudioFile());
    }

    /**
     * Test nextSong with PlayList which contains just one AudioFile.
     */
    @Test
    public void testNextSongWithOneAudioFile() throws Exception {
        System.out.println("---- Test Snippet 01 ----");
        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");

        PlayList pl1 = new PlayList();
        pl1.add(tf1);
        System.out.println(pl1.currentAudioFile());
        assertEquals(tf1, pl1.currentAudioFile());
        pl1.nextSong();
        System.out.println(pl1.currentAudioFile());
        assertEquals(tf1, pl1.currentAudioFile());
    }

    /**
     * Test nextSong with PlayList which contains two AudioFiles and initial
     * position is every time the first AudioFile.
     */
    @Test
    public void testNextSongWithTwoAudioFiles() throws Exception {
        System.out.println("---- Test Snippet 02 ----");
        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");

        PlayList pl2 = new PlayList();
        pl2.add(tf1);
        pl2.add(tf2);
        pl2.nextSong();
        System.out.println(pl2.currentAudioFile());
        assertEquals(tf2, pl2.currentAudioFile());
    }

    /**
     * Test looping with AudioFiles in PlayList.
     */
    @Test
    public void testNextSongLoopingCorrectly() throws Exception {
        System.out.println("---- Test Snippet 03 ----");
        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");

        PlayList pl3 = new PlayList();
        pl3.add(tf1);
        pl3.add(tf2);
        for(int i = 0; i < 5; i ++) {
            System.out.println(pl3.currentAudioFile());
            assertEquals(tf1, pl3.currentAudioFile());
            pl3.nextSong();
            System.out.println(pl3.currentAudioFile());
            assertEquals(tf2, pl3.currentAudioFile());
            pl3.nextSong();
        }
    }

    /**
     * Test looping / nextSong with sort criterion.
     */
    @Test
    public void testNextSongWithSort() throws Exception {
        System.out.println("---- Test Snippet 04 ----");
        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
        AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

        PlayList pl4 = new PlayList();
        pl4.add(tf1);
        pl4.add(tf2);
        pl4.add(tf3);
        pl4.setSortCriterion(SortCriterion.DURATION);
        System.out.println(pl4.currentAudioFile());
        assertEquals(tf2, pl4.currentAudioFile());
        pl4.nextSong();
        System.out.println(pl4.currentAudioFile());
        assertEquals(tf3, pl4.currentAudioFile());
        pl4.nextSong();
        System.out.println(pl4.currentAudioFile());
        assertEquals(tf1, pl4.currentAudioFile());
    }

    /**
     * Test looping / nextSong with search string.
     */
    @Test
    public void testNextSongWithSearch() throws Exception {
        System.out.println("---- Test Snippet 05 ----");
        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
        AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

        PlayList pl5 = new PlayList();
        pl5.add(tf1);
        pl5.add(tf2);
        pl5.add(tf3);
        System.out.println(pl5.currentAudioFile());
        assertEquals(tf1, pl5.currentAudioFile());
        pl5.nextSong();
        pl5.setSearch("Eisbach");
        System.out.println(pl5.currentAudioFile());
        assertEquals(tf1, pl5.currentAudioFile());
        pl5.nextSong();
        System.out.println(pl5.currentAudioFile());
        assertEquals(tf3, pl5.currentAudioFile());
    }

}
