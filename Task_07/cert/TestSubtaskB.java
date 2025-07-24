import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import studiplayer.basic.TagReader;

public class TestSubtaskB {
	@Test
	public void test_readTags() {
		TaggedFile tf = new TaggedFile("audiofiles/Rock 812.mp3");
		Map<String, Object> tagMap = TagReader.readTags(tf.getPathname());
		for (String tag : tagMap.keySet()) {
			Object value = tagMap.get(tag);
			System.out.printf("key: %-25s value: %-30s (type: %s)\n",
					tag, value, value.getClass().getSimpleName());
		}
	}

	@Test
	public void test_storeTags() {
		TaggedFile tf = new TaggedFile("audiofiles/wellenmeister_awakening.ogg");
		assertEquals("Incorrect value for title!", "TANOM Part I: Awakening",
				tf.getTitle());
		assertEquals("Incorrect value for author!", "Wellenmeister",
				tf.getAuthor());
		assertEquals("Incorrect value for album!", "TheAbsoluteNecessityOfMeaning",
				tf.getAlbum());
		System.out.println("hii");
		assertEquals("Incorrect value for duration!", 355154000L,
				tf.getDuration());
	}

}
