import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import studiplayer.basic.TagReader;
import studiplayer.basic.WavParamReader;

public class TestSubtaskC {
	@Test
	public void test_readWavFileParams() {
		WavParamReader.readParams("audiofiles/wellenmeister - tranquility.wav");
		System.out.printf("framerate: %f, #frames: %d\n", WavParamReader.getFrameRate(),
				WavParamReader.getNumberOfFrames());
	}

	@Test
	public void test_computeDuration() {
		assertEquals("wrong value for duration!", 141400816L,
				WavFile.computeDuration(6235776L, 44100.0f));
	}

	@Test
	public void test_readDuration() {
		WavFile wf = new WavFile("audiofiles/wellenmeister - tranquility.wav");
		assertEquals("Wrong value for duration!", 141400816L, wf.getDuration());
	}
}
