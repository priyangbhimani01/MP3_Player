import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestSubtaskD {

	@Test
	public void testAudioFileFactory1() {
		try {
			AudioFileFactory.createAudioFile("media/unknown.xyz");
			fail("Invalid suffix should result in RuntimeException!");
			
			System.out.println("code start");
		} catch (Exception e) {
			// expected
			
			assertEquals("Falsche Fehlermeldung!",
					"Unknown suffix for AudioFile \"media/unknown.xyz\"",
					e.getMessage());
			System.out.println("code end");
		}
	}

	@Test
	public void testAudioFileFactory2() {
		try {
			AudioFileFactory.createAudioFile("media/unknown");
			fail("Missing suffix should result in RuntimeException!");
		} catch (Exception e) {
			// expected
		}
	}

	@Test
	public void testAudioFileFactory3() {
		try {
			AudioFileFactory.createAudioFile("media/unknown.mp3");
			fail("Non-readable file should result in RuntimeException!");
		} catch (Exception e) {
			// expected
		}
	}

	@Test
	public void testAudioFileFactory4() {
		AudioFile af = AudioFileFactory.createAudioFile("audiofiles/Rock 812.mp3");
		assertTrue("Expecting TaggedFile", af instanceof TaggedFile);
	}

	@Test
	public void testAudioFileFactory5() {
		AudioFile af = AudioFileFactory.createAudioFile("audiofiles/wellenmeister - tranquility.wav");
		assertTrue("Expecting WavFile", af instanceof WavFile);
	}

	@Test
	public void testAudioFileFactory6() {
		AudioFile af = AudioFileFactory.createAudioFile("audiofiles/special.oGg");
		assertTrue("Expecting TaggedFile", af instanceof TaggedFile);
	}
}
