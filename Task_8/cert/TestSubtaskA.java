import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

public class TestSubtaskA {

    @Test
    public void testListAttribute() {
        // check if there is an attribute of type LinkedList
        // and whether the getter is there and yields a result of type LinkedList
        PlayList playList = new PlayList();
        Field[] attributes = playList.getClass().getDeclaredFields();

        boolean foundLinkedListAttribute = false;
        for (Field attribute : attributes) {
            if (List.class.isAssignableFrom(attribute.getType())) {
                foundLinkedListAttribute = true;
                break;
            }
        }
        if (!foundLinkedListAttribute) {
            fail("No attribute of type List or LinkedList found!");
        }

        Object getterResult = playList.getList();
        if (!(getterResult instanceof List)) {
            fail("getList() should return an object of type List or LinkedList!");
        }
    }

    @Test
    public void testAdd() {
        TaggedFile file1 = new TaggedFile("audiofiles/Rock 812.mp3");
        TaggedFile file2 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
        PlayList playList = new PlayList();
        List<AudioFile> list = playList.getList();
        assertEquals("List should be empty after creation", 0, list.size());

        playList.add(file1);
        assertEquals("We've added one AudioFile, this the list should contain one element", 1, list.size());
        assertTrue("The added reference should be the same as the element in your list", list.get(0) == file1);

        playList.add(file2);
        assertEquals("We've added two AudioFiles, this the list should contain two elements", 2, list.size());
        assertTrue("The list should contain the first file", list.contains(file1));
        assertTrue("The list should contain the second file", list.contains(file2));

        playList.add(file1);
        assertEquals("We've added one more file, this the list should contain three elements", 3, list.size());
    }

    @Test
    public void testSize() {
        TaggedFile file1 = new TaggedFile("audiofiles/Rock 812.mp3");
        TaggedFile file2 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
        PlayList playList = new PlayList();
        assertEquals("List should be empty after creation", 0, playList.size());

        playList.add(file1);
        assertEquals("One file is added, thus the size has to be 1", 1, playList.size());

        playList.add(file2);
        assertEquals("Added one more file, size is 2 now", 2, playList.size());
    }

    @Test
    public void testRemove() {
        TaggedFile file1 = new TaggedFile("audiofiles/Rock 812.mp3");
        TaggedFile file2 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
        PlayList playList = new PlayList();
        playList.add(file1);
        playList.add(file2);
        List<AudioFile> list = playList.getList();
        assertEquals("List contain two elements", 2, playList.size());

        playList.remove(file1);
        assertEquals("One file is removed, thus the size has to be 1", 1, playList.size());
        assertFalse("List should not contain Rock 812", list.contains(file1));

        playList.remove(file2);
        assertEquals("Second file is removed, thus the size has to be 0", 0, playList.size());
        assertFalse("List should not contain Eisbach Deep Snow", list.contains(file2));
    }

}
