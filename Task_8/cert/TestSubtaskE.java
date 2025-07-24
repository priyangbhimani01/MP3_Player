import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestSubtaskE {

	@Test
	public void testLoadFromM3U_01() throws IOException {
		PlayList pl = new PlayList();
		AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
		AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

		pl.getList().add(tf1);
		pl.getList().add(tf2);
		pl.getList().add(tf3);
		List<String> expected = Arrays.asList(
				pl.getList().get(0).toString(),
				pl.getList().get(1).toString(),
				pl.getList().get(2).toString());
		
		

		pl.saveAsM3U("test.m3u");
		pl.loadFromM3U("test.m3u");
		assertEquals("wrong current index!", 0, pl.getCurrent());
		assertEquals("wrong number of files in list!", 3, pl.getList().size());
		List<String> actual = Arrays.asList(
				pl.getList().get(0).toString(),
				pl.getList().get(1).toString(),
				pl.getList().get(2).toString());
		assertEquals("saveAsM3U doesn't save the audiofiles correctly!",
				expected, actual);
	}

	@Test
	public void testLoadFromM3U_02() throws IOException {
		PlayList pl1 = new PlayList();
		AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
		AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

		pl1.getList().add(tf1);
		pl1.getList().add(tf2);
		pl1.getList().add(tf3);
		pl1.saveAsM3U("test.m3u");
		List<String> expected = Arrays.asList(
				pl1.getList().get(0).toString(),
				pl1.getList().get(1).toString(),
				pl1.getList().get(2).toString());

		PlayList pl2 = new PlayList("test.m3u");
		assertEquals("wrong current index!", 0, pl2.getCurrent());
		assertEquals("wrong number of files in list!", 3, pl2.getList().size());
		List<String> actual = Arrays.asList(
				pl2.getList().get(0).toString(),
				pl2.getList().get(1).toString(),
				pl2.getList().get(2).toString());
		assertEquals("saveAsM3U doesn't save the audiofiles correctly!",
				expected, actual);
	}
}
