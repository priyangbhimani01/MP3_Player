package studiplayer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import studiplayer.ui.Player;

public class TestSubtaskC {
    @Test
    public void testPlayButtonAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute playButton: Button",
                Player.class.getDeclaredField("playButton") != null);
        assertEquals("Class Player must have an attribut eplayButton: Button", Button.class,
                Player.class.getDeclaredField("playButton").getType());
    }

    @Test
    public void testPauseButtonAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute pauseButton: Button",
                Player.class.getDeclaredField("pauseButton") != null);
        assertEquals("Class Player must have an attribute pauseButton: Button", Button.class,
                Player.class.getDeclaredField("pauseButton").getType());
    }

    @Test
    public void testStopButtonAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute stopButton: Button",
                Player.class.getDeclaredField("stopButton") != null);
        assertEquals("Class Player must have an attribute stopButton: Button", Button.class,
                Player.class.getDeclaredField("stopButton").getType());
    }

    @Test
    public void testNextButtonAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute nextButton: Button",
                Player.class.getDeclaredField("nextButton") != null);
        assertEquals("Class Player must have an attribute nextButton: Button", Button.class,
                Player.class.getDeclaredField("nextButton").getType());
    }

    @Test
    public void testFilterButtonAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute filterButton: Button",
                Player.class.getDeclaredField("filterButton") != null);
        assertEquals("Class Player must have an attribute filterButton: Button", Button.class,
                Player.class.getDeclaredField("filterButton").getType());
    }

    @Test
    public void testSearchTextFieldAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute searchTextField: TextField",
                Player.class.getDeclaredField("searchTextField") != null);
        assertEquals("Class Player must have an attribute searchTextField: TextField", TextField.class,
                Player.class.getDeclaredField("searchTextField").getType());
    }

    @Test
    public void testSortChoiceBoxAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute sortChoiceBox: ChoiceBox",
                Player.class.getDeclaredField("sortChoiceBox") != null);
        assertEquals("Class Player must have an attribute sortChoiceBox: ChoiceBox", ChoiceBox.class,
                Player.class.getDeclaredField("sortChoiceBox").getType());
    }

    @Test
    public void testPlayListLabelAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute playListLabel: Label",
                Player.class.getDeclaredField("playListLabel") != null);
        assertEquals("Class Player must have an attribute playListLabel: Label", Label.class,
                Player.class.getDeclaredField("playListLabel").getType());
    }

    @Test
    public void testPlayTimeLabelAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute playTimeLabel: Label",
                Player.class.getDeclaredField("playTimeLabel") != null);
        assertEquals("Class Player must have an attribute playTimeLabel: Label", Label.class,
                Player.class.getDeclaredField("playTimeLabel").getType());
    }

    @Test
    public void testCurrentSongLabelAttribute() throws NoSuchFieldException, SecurityException {
        assertTrue("Class Player must have an attribute currentSongLabel: Label",
                Player.class.getDeclaredField("currentSongLabel") != null);
        assertEquals("Class Player must have an attribute currentSongLabel: Label", Label.class,
                Player.class.getDeclaredField("currentSongLabel").getType());
    }
    
    @Test
    public void testPlayListDirectoryConstant() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertTrue("Class Player must have a constant PLAYLIST_DIRECTORY", Player.class.getDeclaredField("PLAYLIST_DIRECTORY") != null);
        assertTrue("PLAYLIST_DIRECTORY should be private", Modifier.isPrivate(Player.class.getDeclaredField("PLAYLIST_DIRECTORY").getModifiers()));
        assertTrue("PLAYLIST_DIRECTORY should be static", Modifier.isStatic(Player.class.getDeclaredField("PLAYLIST_DIRECTORY").getModifiers()));
        assertEquals("PLAYLIST_DIRECTORY should be of type String", String.class, Player.class.getDeclaredField("PLAYLIST_DIRECTORY").getType());
    }
    
    @Test
    public void testInitialPlayTimeConstant() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertTrue("Class Player should hava a constant INITIAL_PLAY_TIME_LABEL", Player.class.getDeclaredField("INITIAL_PLAY_TIME_LABEL") != null);
        assertTrue("INITIAL_PLAY_TIME_LABEL should be private", Modifier.isPrivate(Player.class.getDeclaredField("INITIAL_PLAY_TIME_LABEL").getModifiers()));
        assertTrue("INITIAL_PLAY_TIME_LABEL should be static", Modifier.isStatic(Player.class.getDeclaredField("INITIAL_PLAY_TIME_LABEL").getModifiers()));
        assertEquals("INITIAL_PLAY_TIME_LABEL should be of type String", String.class, Player.class.getDeclaredField("INITIAL_PLAY_TIME_LABEL").getType());
    }
    
    @Test
    public void testNoCurrentSongConstant() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertTrue("Class Player should have a constant NO_CURRENT_SONG", Player.class.getDeclaredField("NO_CURRENT_SONG") != null);
        assertTrue("NO_CURRENT_SONG should be private", Modifier.isPrivate(Player.class.getDeclaredField("NO_CURRENT_SONG").getModifiers()));
        assertTrue("NO_CURRENT_SONG should be static", Modifier.isStatic(Player.class.getDeclaredField("NO_CURRENT_SONG").getModifiers()));
        assertEquals("NO_CURRENT_SONG should be of type String", String.class, Player.class.getDeclaredField("NO_CURRENT_SONG").getType());
    }

}
