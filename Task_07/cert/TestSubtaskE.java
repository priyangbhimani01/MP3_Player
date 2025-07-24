import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class TestSubtaskE {
	@Test
	public void test_getAlbum() {
		TaggedFile tf = new TaggedFile("audiofiles/Rock 812.mp3");
		assertEquals("Wrong value for album!",
				"The Sea, the Sky", tf.getAlbum());
	}

	@Test
	public void test_toString() {
		TaggedFile tf = new TaggedFile("audiofiles/Rock 812.mp3");
		assertEquals("toString wrong for TaggedFile!",
				"Eisbach - Rock 812 - The Sea, the Sky - 05:31",
				tf.toString());
		WavFile wf = new WavFile("audiofiles/wellenmeister - tranquility.wav");
		assertEquals("toString wrong for WavFile!",
				"wellenmeister - tranquility - 02:21",
				wf.toString());
	}
}
