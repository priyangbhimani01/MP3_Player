package studiplayer.cert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import studiplayer.audio.AudioFile;
import studiplayer.audio.ControllablePlayListIterator;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.SortCriterion;
import studiplayer.audio.TaggedFile;

public class ControllablePlayListIteratorTest {
    @Test
    public void testIterator() throws NotPlayableException {
        List<AudioFile> list = Arrays.asList(
                new TaggedFile("audiofiles/Rock 812.mp3"),
                new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg"),
                new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
        
        ControllablePlayListIterator iterator = new ControllablePlayListIterator(list);

        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Rock 812.mp3 as first iteration result", list.get(0), iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Motiv 5. Symphonie von Beethoven.ogg as first iteration result", list.get(1), iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as second iteration result", list.get(2), iterator.next());
        assertFalse("Should only provide two elements", iterator.hasNext());
    }
    
    @Test
    public void testJumpToAudioFile() throws NotPlayableException {
        List<AudioFile> list = Arrays.asList(
                new TaggedFile("audiofiles/Rock 812.mp3"),
                new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg"),
                new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
        
        ControllablePlayListIterator iterator = new ControllablePlayListIterator(list);

        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Rock 812.mp3 as first iteration result", list.get(0), iterator.next());
        
        assertEquals("Should provide Rock 812.mp3 with jump to call", list.get(0), iterator.jumpToAudioFile(list.get(0)));
        
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Motiv 5. Symphonie von Beethoven.ogg as first iteration result", list.get(1), iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as second iteration result", list.get(2), iterator.next());
        assertFalse("Should only provide two elements", iterator.hasNext());
        
        assertEquals("Should provide Eisbach Deep Snow.ogg as second iteration result", list.get(2), iterator.jumpToAudioFile(list.get(2)));
        assertFalse("Should not has next element after jumping to last one", iterator.hasNext());
    }
    
    @Test
    public void testSearch() throws NotPlayableException {
        List<AudioFile> list = new ArrayList<AudioFile>();

        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
        AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

        list.add(tf1);
        list.add(tf2);
        list.add(tf3);
        
        Iterator<AudioFile> iterator = new ControllablePlayListIterator(list, "Eisbach", SortCriterion.DEFAULT);
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Rock 812.mp3 as first iteration result", tf1, iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as second iteration result", tf3, iterator.next());
        assertFalse("Should only provide two elements", iterator.hasNext());
    }
    
    @Test
    public void testSortDuration() throws NotPlayableException {
        List<AudioFile> list = new ArrayList<AudioFile>();

        AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
        AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

        list.add(tf1);
        list.add(tf2);
        list.add(tf3);
        
        Iterator<AudioFile> iterator = new ControllablePlayListIterator(list, "", SortCriterion.DURATION);
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Motiv 5. Symphonie von Beethoven.ogg as first iteration result", tf2, iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as second iteration result", tf3, iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Rock 812.mp3 as third iteration result", tf1, iterator.next());
        assertFalse("Should only provide two elements", iterator.hasNext());
    }
    
    @Test
    public void testSortAndSearch() throws NotPlayableException {
        List<AudioFile> list = Arrays.asList(
                new TaggedFile("audiofiles/Rock 812.mp3"),
                new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg"),
                new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
        
        Iterator<AudioFile> iterator = new ControllablePlayListIterator(list, "Eisbach", SortCriterion.TITLE);
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Eisbach Deep Snow.ogg as first iteration result", list.get(2), iterator.next());
        assertTrue("Iterator should provide one more result", iterator.hasNext());
        assertEquals("Should provide Rock 812.mp3 as second iteration result", list.get(0), iterator.next());
        assertFalse("Should only provide two elements", iterator.hasNext());
    }
    
}
