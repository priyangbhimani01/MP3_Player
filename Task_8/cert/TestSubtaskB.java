import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

public class TestSubtaskB {

	@Test
	public void testCurrentAttribute() {
		// check if there is an attribute of type int named current
		// check
		PlayList playList = new PlayList();
		Field[] attributes = playList.getClass().getDeclaredFields();

		Field currentAttribute = null;
		for (Field attribute : attributes) {
			if (attribute.getName().equals("current")) {
				currentAttribute = attribute;
				break;
			}
		}
		if (currentAttribute == null) {
			fail("PlayList should declare an attribute named 'current'!");
		}
		if (!currentAttribute.getType().getSimpleName().equals("int")) {
			fail("current attribute should be of type int!");
		}

		// check if getter and setter work correctly
		AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
		playList.getList().add(tf1);
		playList.getList().add(tf2);
		assertEquals("Initial value for current not correct!",
				0, playList.getCurrent());
		playList.setCurrent(1);
		assertEquals("Value for current not correct!", 1, playList.getCurrent());
	}

	@Test
	public void testNextSong() {
		PlayList pl = new PlayList();
		AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
		AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

		pl.getList().add(tf1);
		pl.getList().add(tf2);
		pl.getList().add(tf3);

		assertEquals("wrong current index!", 0, pl.getCurrent());
		pl.nextSong();
		assertEquals("wrong current index after change to next song!",
				1, pl.getCurrent());
		pl.nextSong();
		assertEquals("wrong current index after change to next song!",
				2, pl.getCurrent());
		pl.nextSong();
		assertEquals("wrong current index after change to next song!",
				0, pl.getCurrent());
	}

	@Test
	public void testNextSongInvalidIndex() {
		PlayList pl = new PlayList();
		AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
		AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

		pl.getList().add(tf1);
		pl.getList().add(tf2);
		pl.getList().add(tf3);

		pl.setCurrent(4);
		assertEquals("wrong current index!", 4, pl.getCurrent());
		pl.nextSong();
		assertEquals("wrong current index after change to next song!",
				0, pl.getCurrent());
		pl.nextSong();
		assertEquals("wrong current index after change to next song!",
				1, pl.getCurrent());
	}
}
