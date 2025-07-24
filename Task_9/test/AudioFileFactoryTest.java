package studiplayer.cert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import studiplayer.audio.AudioFile;
import studiplayer.audio.AudioFileFactory;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.TaggedFile;
import studiplayer.audio.WavFile;

public class AudioFileFactoryTest {
    @Test
    public void testFactory() {
        try {
            AudioFile f1 = AudioFileFactory
                    .createAudioFile("audiofiles/Rock 812.mp3");
            assertEquals("MP3 type not recognized",
                    "studiplayer.audio.TaggedFile", f1.getClass().getName());
            AudioFile f2 = AudioFileFactory
                    .createAudioFile("audiofiles/wellenmeister - tranquility.wav");
            assertEquals("WAV type not recognized",
                    "studiplayer.audio.WavFile", f2.getClass().getName());
            AudioFile f3 = AudioFileFactory
                    .createAudioFile("audiofiles/wellenmeister_awakening.ogg");
            assertEquals("OGG type not recognized",
                    "studiplayer.audio.TaggedFile", f3.getClass().getName());
            AudioFile f4 = AudioFileFactory
                    .createAudioFile("audiofiles/special.oGg");
            assertEquals("OGG type not recognized",
                    "studiplayer.audio.TaggedFile", f4.getClass().getName());
            AudioFile f5 = AudioFileFactory
                    .createAudioFile("audiofiles/kein.wav.sondern.ogg");
            assertTrue("Check for filename suffix not correct",
                    f5 instanceof TaggedFile);
            AudioFile f6 = AudioFileFactory
                    .createAudioFile("audiofiles/kein.ogg.sondern.wav");
            assertTrue("Check for filename suffix not correct",
                    f6 instanceof WavFile);

        } catch (NotPlayableException e) {
            fail("AudioFileFactory is not able to create AudioFile: "
                    + e.getMessage());
        }
    }

    @Test
    public void testException() {
        try {
            AudioFileFactory.createAudioFile("does not exist.mp3");
            fail("Expected exception NotPlayableException");
        } catch (NotPlayableException e) {
            // Expected
        }
    }
}
