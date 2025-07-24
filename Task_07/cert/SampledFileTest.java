import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;

import studiplayer.basic.BasicPlayer;

public class SampledFileTest {
    @SuppressWarnings("rawtypes")
    private Class clazz = SampledFile.class;
    private SampledFile f1;
    private SampledFile f2;
    private SampledFile f3;

    @Before
    public void setup() {
        // Initializer block
        // This checks the proper connection of constructors already

        try {
            f1 = new TaggedFile("audiofiles/Rock 812.mp3");
            f2 = new WavFile("audiofiles/wellenmeister - tranquility.wav");
            f3 = new TaggedFile("audiofiles/wellenmeister_awakening.ogg");
        } catch (Exception e) {
            fail("Problem creating AudioFile objects: " + e.getMessage());
        }
    }

    @Test
    public void testSuperClass() {
        assertEquals("SampledFile is not derived from AudioFile",
                "AudioFile", clazz.getSuperclass().getName());
    }

    @Test
    public void testAbstract() {
        int mod = clazz.getModifiers();
        assertTrue("SampledFile is not declared abstract",
                Modifier.isAbstract(mod));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConstructor() {
        try {
            clazz.getDeclaredConstructor(new Class[] { String.class });
        } catch (SecurityException e) {
            fail(e.toString());
        } catch (NoSuchMethodException e) {
            fail("Constructor SampledFile(String) does not exist");
        }
    }

    @Test
    public void test_timeFormatter_01() {
        assertEquals("Wrong time format", "00:00",
                SampledFile.timeFormatter(999999L));
        assertEquals("Wrong time format", "00:01",
                SampledFile.timeFormatter(1000000L));
        assertEquals("Wrong time format", "01:01",
                SampledFile.timeFormatter(61000000L));
        assertEquals("Wrong time format", "99:00",
                SampledFile.timeFormatter(5940000000L));
        assertEquals("Wrong time format", "99:59",
                SampledFile.timeFormatter(5999000000L));
        assertEquals("Wrong time format", "99:59",
                SampledFile.timeFormatter(5999999999L));
        assertEquals("Wrong time format", "05:05",
                SampledFile.timeFormatter(305862000L));
        assertEquals("Wrong time format", "05:06",
                SampledFile.timeFormatter(306862000L));
        assertEquals("Wrong time format", "02:21",
                SampledFile.timeFormatter(141400816L));
    }

    @Test
    public void test_timeFormatter_02() {
        try {
            // Call method with time value that underflows our format
            SampledFile.timeFormatter(-1L);
            // We should never get here
            fail("Time value underflows format; expecting exception");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void test_timeFormatter_03() {
        try {
            // Call method with time value that will overflow our format
            SampledFile.timeFormatter(6000000000L);
            // We should never get here
            fail("Time value overflows format; expecting exception");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void test_getFormattedDuration_01() {
        assertEquals("Wrong formatted duration", "05:31",
                f1.formatDuration());
        assertEquals("Wrong formatted duration", "02:21",
                f2.formatDuration());
        assertEquals("Wrong formatted duration", "05:55",
                f3.formatDuration());
    }

    // A helper to sleep for some milliseconds
    private void takeANap(long millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    @Test
    public void testPlayPauseStop() {
        // Make sure that other tests that play songs are stopped
        BasicPlayer.stop();
        long posInitial = BasicPlayer.getPosition();
        int counter = 0;
        while (counter < 10 && posInitial != 0) {
            System.out.printf("InitialPos = %d\n", posInitial);
            BasicPlayer.stop();
            takeANap(1000L);
            posInitial = BasicPlayer.getPosition();
            counter++;
        }
        assertEquals("Wrong initial position" + posInitial, 0L, posInitial);

        // Now, start our test
        String formattedPositionInitial = f1.formatPosition();
        // Start playing f1
        new Thread() {
            public void run() {
                try {
                    f1.play();
                } catch (RuntimeException e) {
                    fail("Cannot play " + f1 + " " + e);
                }
            }
        }.start();

        // Let f1 be played for 1 second
        takeANap(1000L);
        // Now, take the first probe for our subsequent test.
        // Obviously, we expect posPlay > 0L
        long posPlay = BasicPlayer.getPosition();

        // Let f1 be played for 1 second
        takeANap(1000L);

        // Pause f1 and sleep 1 second to let the command get active
        f1.togglePause();
        takeANap(1000L);
        // Get position after pause command
        long posPause1 = BasicPlayer.getPosition();
        // Position in paused state should be after posPlay
        assertTrue("Position does not change during playback: " + posPlay
                + " !< " + posPause1, posPlay < posPause1);

        // We assume that playing the song is stopped now
        // Sleep again a second in paused state
        takeANap(1000L);
        // Take another probe in paused state
        // If playing the song is really paused this should yield the
        // same position as posPause1
        long posPause2 = BasicPlayer.getPosition();
        assertEquals("Position in paused state is not stable:" + posPause1
                + " != " + posPause2, posPause1, posPause2);

        // Resume playing and sleep 1 second to let the command get active
        f1.togglePause();
        takeANap(1000L);
        // We assume that the song is now played again

        // Take another probe during playing
        // If the song is played again the position should be larger than any
        // position probed during the paused state
        long posResume1 = BasicPlayer.getPosition();
        assertTrue("Position does not change after resuming playback: "
                + posPause2 + " !< " + posResume1, posPause2 < posResume1);

        // Take a probe short before stopping
        // Note: stop now resets position to 0!
        String formattedPositionEnd = f1.formatPosition();
        // Check formatted output and compare strings.
        // The comparison of strings of our special format should yield
        // formattedPositionInitial < formattedPositionEnd
        assertTrue("Method getFormattedPosition does not work properly",
                formattedPositionInitial.compareTo(formattedPositionEnd) < 0);

        // Stop playing and sleep 1 second to let the command get active
        f1.stop();
        takeANap(1000L);
        // We assume that playing the song is stopped by now.
        // Take a probe and sleep for 1 second
        long posStop1 = BasicPlayer.getPosition();
        takeANap(1000L);

        // Take again a probe.
        // If playing the song was really stopped the two probes taken during
        // state stopped should be equal
        long posStop2 = BasicPlayer.getPosition();
        assertEquals("Position is not stable in state stop: " + posStop1
                + " != " + posStop2, posStop1, posStop2);

    }

}
