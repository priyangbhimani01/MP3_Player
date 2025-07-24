package studiplayer.cert;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;
import studiplayer.ui.Player;

public class PlayerTest {

	public static class PlayerToTest extends Player {
		public PlayerToTest() {
			super();
			playerRef = this;
		}
		
		@Override
		public void start(Stage primaryStage) throws Exception {
		    primaryStageRef = primaryStage;
		    super.setUseCertPlayList(true);
		    super.start(primaryStage);
		}
	}

	private boolean debug = false;
	private static Thread playerThread = null;
    private static Player playerRef = null;
    private static Stage primaryStageRef = null;
	private Class<Player> clazz = Player.class;

	@Test
	public void testDefaultPlaylist() {
		String attribut = "DEFAULT_PLAYLIST";
		try {
			Field f;
			f = clazz.getDeclaredField(attribut);
			f.setAccessible(true);
			assertEquals("Typ des Attributs " + attribut, "java.lang.String", f.getType().getName());
			int mod = f.getModifiers();
			assertTrue("Attribut " + attribut + " sollte 'public static final' sein",
					isPublic(mod) && isStatic(mod) && isFinal(mod));
			assertEquals("Konstanter Wert falsch", "playlists/DefaultPlayList.m3u",
					studiplayer.ui.Player.DEFAULT_PLAYLIST);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			fail("Attribut " + attribut + " existiert nicht.");
		}
	}

	@Test
	public void testAttributes() {
		Hashtable<String, String> hm = new Hashtable<String, String>();
		hm.put("playButton", "javafx.scene.control.Button");
		hm.put("pauseButton", "javafx.scene.control.Button");
		hm.put("stopButton", "javafx.scene.control.Button");
		hm.put("nextButton", "javafx.scene.control.Button");
		hm.put("filterButton", "javafx.scene.control.Button");
		hm.put("playListLabel", "javafx.scene.control.Label");
		hm.put("playTimeLabel", "javafx.scene.control.Label");
		hm.put("currentSongLabel", "javafx.scene.control.Label");
		hm.put("searchTextField", "javafx.scene.control.TextField");
		hm.put("sortChoiceBox", "javafx.scene.control.ChoiceBox");
		hm.put("playList", "studiplayer.audio.PlayList");
		hm.put("DEFAULT_PLAYLIST", "java.lang.String");
		hm.put("PLAYLIST_DIRECTORY", "java.lang.String");
		hm.put("INITIAL_PLAY_TIME_LABEL", "java.lang.String");
		hm.put("NO_CURRENT_SONG", "java.lang.String");

		String attr = null;
		try {
			for (String attribut : hm.keySet()) {
				attr = attribut; // For catch clause;
				Field f = clazz.getDeclaredField(attribut);
				f.setAccessible(true);
				assertEquals("Typ des Attributs " + attribut, hm.get(attribut), f.getType().getName());
				if (!"DEFAULT_PLAYLIST".equals(attribut)) {
					int mod = f.getModifiers();
					assertTrue("Zugriff auf " + attribut + " einschraenken", isPrivate(mod));
				}
			}
		} catch (SecurityException e) {
			fail("whatever");
		} catch (NoSuchFieldException e) {
			fail("Attribut " + attr + " existiert nicht.");
		}
	}

	@Test
	public void testConstructor() {
		// Test initialization of default play list
		try {
			playerRef.loadPlayList(null); // use default play list
			Field fieldPlayList = clazz.getDeclaredField("playList");
			fieldPlayList.setAccessible(true);
			PlayList pl1 = (PlayList) fieldPlayList.get(playerRef);
			assertEquals("PlayList muss initial leer sein", 0, pl1.size());
			assertEquals("PlayList abspielposition muss 0 sein", null, pl1.currentAudioFile());
		} catch (NoSuchFieldException e) {
			fail("Kein Attribut playList definiert in Klasse Player");
		} catch (IllegalAccessException e) {
			fail(e.toString());
		}

		// Test with a special play list
		try {
			// Constants for the test depending on the test list used.
			final int correctSize = 8; // NOTE: must be adapted to size of
										// special play list
			final String firstSongToString = "Wellenmeister - " + "TANOM Part I: Awakening - "
					+ "TheAbsoluteNecessityOfMeaning - 05:55";

			// Test parser for play lists
			// and initialization of play list
			Player player = playerRef;
			player.loadPlayList("playlists/playList.cert.m3u");
			wait(1);
			Field fieldPlayList = clazz.getDeclaredField("playList");
			fieldPlayList.setAccessible(true);
			PlayList pl2 = (PlayList) fieldPlayList.get(player);
			assertEquals("Anzahl der in PlayList eingefuegten Lieder falsch", correctSize, pl2.size());

			// Test initialization of play list
			AudioFile currentSong = pl2.currentAudioFile();
			assertNotNull("currentSong nicht gesetzt", currentSong);
			assertEquals("Attribut currentSong falsch", firstSongToString, currentSong.toString());
			// Test setting of the window title
			// Must contain (not equals) the firstSongToString
			assertTrue("Fenster-Titel falsch", getTitle().equals("APA Player"));
		} catch (NoSuchFieldException e) {
			fail("Kein Attribut playList definiert in Klasse Player");
		} catch (IllegalAccessException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testButtonLayout() {
		// Test with a special play list
		playerRef.loadPlayList("playlists/playList.cert.m3u");

		// setup button list
		List<Button> buttonList = fillButtonList();

		// Test setup of buttons
		for (Button button : buttonList) {
			EventHandler<ActionEvent> handler = button.getOnAction();
			assertFalse("Keine Action Command fuer Button " + button.getText() + " gesetzt", handler == null);
		}
	}

	@Test
	public void testButtons() {
		// Activate debug printing
		debug = true;

		// Test with a special play list
		playerRef.loadPlayList("playlists/playList.cert.m3u");
		PlayList pl = new PlayList("playlists/playList.cert.m3u");
		// Constants for the test depending on the test list used.
		// Extract toString() of the first and third song
		Iterator<AudioFile> iter = pl.iterator();
		final String firstSongToString = iter.next().toString();
		iter.next(); // 2nd song, ignore
		final String thirdSongToString = iter.next().toString();

		// A string that documents the events already occurred
		// Used for error messages
		// Initialized here
		String eventSequence = "Aktionen: <start>";

		// Collect the buttons we are using
		Button play = getButton("playButton");
		Button pause = getButton("pauseButton");
		Button stop = getButton("stopButton");
		Button next = getButton("nextButton");

		// Check for correct enabling state of buttons
		wait(1);
		assertFalse(eventSequence + " Play muss aktiviert sein", play.isDisabled());
		assertTrue(eventSequence + " Pause darf nicht aktiviert sein", pause.isDisabled());
		assertTrue(eventSequence + " Stop darf nicht aktiviert sein", stop.isDisabled());
		assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());

		// get player's play list
		Field fieldPlayList;
		try {
			fieldPlayList = clazz.getDeclaredField("playList");
			fieldPlayList.setAccessible(true);
			pl = (PlayList) fieldPlayList.get(playerRef);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

		// Press Start
		eventSequence += "<play1>";
		pressButton(play);
		assertTrue(eventSequence + " Play darf nicht aktiviert sein", play.isDisabled());
		assertFalse(eventSequence + " Pause muss aktiviert sein", pause.isDisabled());
		assertFalse(eventSequence + " Stop muss aktiviert sein", stop.isDisabled());
		assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());
		printDebug(String.format("after play1: %s", pl.currentAudioFile()));
		wait(1);

		// Press Pause (activate pause)
		eventSequence += "<pause1>";
		pressButton(pause);
		// State of buttons should not have changed
		assertTrue(eventSequence + " Play darf nicht aktiviert sein", play.isDisabled());
		assertFalse(eventSequence + " Pause muss aktiviert sein", pause.isDisabled());
		assertFalse(eventSequence + " Stop muss aktiviert sein", stop.isDisabled());
		assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());
		printDebug(String.format("after pause1: %s", pl.currentAudioFile()));
		wait(2);

		// Press Pause (resume playing)
		eventSequence += "<pause2>";
		pressButton(pause);
		// State of buttons should not have changed
		assertTrue(eventSequence + " Play darf nicht aktiviert sein", play.isDisabled());
		assertFalse(eventSequence + " Pause muss aktiviert sein", pause.isDisabled());
		assertFalse(eventSequence + " Stop muss aktiviert sein", stop.isDisabled());
		assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());
		printDebug(String.format("after pause2: %s", pl.currentAudioFile()));
		wait(1);

		// Press Stop
		eventSequence += "<stop1>";
		pressButton(stop);
		// State of buttons should have changed now!
		assertFalse(eventSequence + " Play muss aktiviert sein", play.isDisabled());
		assertTrue(eventSequence + " Pause darf nicht aktiviert sein", pause.isDisabled());
		assertTrue(eventSequence + " Stop darf nicht aktiviert sein", stop.isDisabled());
		assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());
		printDebug(String.format("after stop1: %s", pl.currentAudioFile()));

		// Here, we are stopped, give threads a chance to react
		wait(1);
		try {
			Field fieldPlayTime = clazz.getDeclaredField("playTimeLabel");
			fieldPlayTime.setAccessible(true);
			Label ptime = (Label) fieldPlayTime.get(playerRef);
			// Stop must reset playTime
			assertEquals(eventSequence + " Stop setzt playTime nicht zurueck", "00:00", ptime.getText());

			// Current song must still be first song in list
			Field fieldPlaylist = clazz.getDeclaredField("playList");
			fieldPlaylist.setAccessible(true);
			assertNotNull(eventSequence + " Attribut currentSong nicht gesetzt", 
					pl.currentAudioFile());
			assertEquals(eventSequence + " currentSong falsch", firstSongToString, 
					pl.currentAudioFile().toString());
		} catch (NoSuchFieldException e) {
			fail("Attribut existiert nicht " + e);
		} catch (IllegalAccessException e) {
			fail(e.toString());
		}
		wait(1);

		// Press Next (and start playing the second song in list)
		eventSequence += "<next1>";
		pressButton(next);
		// State of buttons should have changed
		assertTrue(eventSequence + " Play darf nicht aktiviert sein", play.isDisabled());
		assertFalse(eventSequence + " Pause muss aktiviert sein", pause.isDisabled());
		assertFalse(eventSequence + " Stop muss aktiviert sein", stop.isDisabled());
		assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());
		printDebug(String.format("after next1: %s", pl.currentAudioFile()));
		wait(1);

		// Next (this changes to the third song)
		eventSequence += "<next2>";
		pressButton(next);
		// State of buttons should not have changed
		assertTrue(eventSequence + " Play darf nicht aktiviert sein", play.isDisabled());
		assertFalse(eventSequence + " Pause muss aktiviert sein", pause.isDisabled());
		assertFalse(eventSequence + " Stop muss aktiviert sein", stop.isDisabled());
		assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());

		try {
			// Give threads a chance to react to the 'next' command
			wait(1);
			Field fieldPlaylist = clazz.getDeclaredField("playList");
			fieldPlaylist.setAccessible(true);

			// Current song must be the third song in list
			printDebug(String.format("after next2: %s", pl.currentAudioFile()));
			assertNotNull(eventSequence + " Attribut currentSong nicht gesetzt", 
					pl.currentAudioFile());
			assertEquals(eventSequence + " currentSong falsch", thirdSongToString, 
					pl.currentAudioFile().toString());

			// Check advancement of playTime
			Field fieldPlayTime = clazz.getDeclaredField("playTimeLabel");
			fieldPlayTime.setAccessible(true);
			Label playTime = (Label) fieldPlayTime.get(playerRef);
			// Take first time probe
			String pos1 = playTime.getText();
			// Give player thread a chance to play the song a bit further
			wait(2);
			// Take second time probe
			String pos2 = playTime.getText();

			// Compare: probe 2 should be ahead of probe 1
			// Note: we compare the formatted string representations here
			assertTrue(eventSequence + " Abspielzeit nicht aktualisiert", pos1.compareTo(pos2) < 0);
			printDebug(String.format("playtime pos1: %s", pos1));
			printDebug(String.format("playtime pos2: %s", pos2));

			// Finally, press stop
			eventSequence += "<stop2>";
			pressButton(stop);
			// State of buttons should have changed now!
			assertFalse(eventSequence + " Play muss aktiviert sein", play.isDisabled());
			assertTrue(eventSequence + " Pause darf nicht aktiviert sein", pause.isDisabled());
			assertTrue(eventSequence + " Stop darf nicht aktiviert sein", stop.isDisabled());
			assertFalse(eventSequence + " Next muss aktiviert sein", next.isDisabled());
			printDebug(String.format("after stop2: %s", pl.currentAudioFile()));
		} catch (NoSuchFieldException e) {
			fail("Attribut existiert nicht " + e);
		} catch (IllegalAccessException e) {
			fail(e.toString());
		}

		// Cleanup, play it safe
		studiplayer.basic.BasicPlayer.stop();
	}
	
	@SuppressWarnings("unchecked")
    @Test
	public void testSortAndSearch() throws InterruptedException {
	    playerRef.loadPlayList("playlists/playList.cert.m3u");
	    
	    Button filter = getButton("filterButton");
	    TextField search = getTextField("searchTextField");
	    @SuppressWarnings("rawtypes")
        ChoiceBox sort = getChoiceBox("sortChoiceBox");
	    
	    Platform.runLater(() -> {
	        search.setText("The Sea");
	        sort.getSelectionModel().select(SortCriterion.DURATION);
	    });
	    wait(1);
	    pressButton(filter);
	    PlayList list = getElement("playList", PlayList.class);
	    Iterator<AudioFile> iterator = list.iterator();
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Henrik Klagges - Road Movie - The Sea, the Sky", "Henrik Klagges - Road Movie - The Sea, the Sky - 03:01", iterator.next().toString());
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Henrik Klagges - Road Movie - The Sea, the Sky", "Henrik Klagges - Road Movie - The Sea, the Sky - 03:01", iterator.next().toString());
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Eisbach - Rock 812 - The Sea, the Sky", "Eisbach - Rock 812 - The Sea, the Sky - 05:31", iterator.next().toString());
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Eisbach - Rock 812 - The Sea, the Sky", "Eisbach - Rock 812 - The Sea, the Sky - 05:31", iterator.next().toString());
        assertFalse("Iterator should not have more elements", iterator.hasNext());
	    
        Platform.runLater(() -> {
            sort.getSelectionModel().select(SortCriterion.AUTHOR);
        });
        wait(1);
        pressButton(filter);
        iterator = list.iterator();
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Eisbach - Rock 812 - The Sea, the Sky", "Eisbach - Rock 812 - The Sea, the Sky - 05:31", iterator.next().toString());
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Eisbach - Rock 812 - The Sea, the Sky", "Eisbach - Rock 812 - The Sea, the Sky - 05:31", iterator.next().toString());
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Henrik Klagges - Road Movie - The Sea, the Sky", "Henrik Klagges - Road Movie - The Sea, the Sky - 03:01", iterator.next().toString());
        assertTrue("Iterator should have more elements", iterator.hasNext());
        assertEquals("Element should be Henrik Klagges - Road Movie - The Sea, the Sky", "Henrik Klagges - Road Movie - The Sea, the Sky - 03:01", iterator.next().toString());
        assertFalse("Iterator should not have more elements", iterator.hasNext());
        

        // Important: Unset all filter configs (has effects on following tests! might be better in tear down?)
        Platform.runLater(() -> {
            search.setText("");
            sort.getSelectionModel().select(SortCriterion.DEFAULT);
        });
        wait(1);
        pressButton(filter);
	}

	@Before
	public void setUp() throws Exception {
		if (playerThread == null)
			startApp();
	}

	@SuppressWarnings("deprecation")
	@After
	public void tearDown() throws Exception {
		if (playerThread == null)
			playerThread.stop();
	}

	private static void startApp() {
		playerThread = new Thread(() -> {
			Application.launch(PlayerToTest.class, new String[] {});
		});
		playerThread.start();

		while (playerRef == null) {
			wait(1);
		}
	}

	private static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
		}
	}

	private void pressButton(Button b) {
		Platform.runLater(() -> b.fire());
		wait(1);
	}

	// Helper methods

	private List<Button> fillButtonList() {
		List<Button> buttonList = new ArrayList<>();

		Button play = getButton("playButton");
		Button pause = getButton("pauseButton");
		Button stop = getButton("stopButton");
		Button next = getButton("nextButton");

		// check if all buttons are created
		assertNotNull("Kein Button fuer PLAY", play);
		assertNotNull("Kein Button fuer PAUSE", pause);
		assertNotNull("Kein Button fuer STOP", stop);
		assertNotNull("Kein Button fuer NEXT", next);

		// fill list
		buttonList.add(play);
		buttonList.add(pause);
		buttonList.add(stop);
		buttonList.add(next);

		return buttonList;
	}

	private String getTitle() {
		if(primaryStageRef == null) {
		    fail("Missing primary stage for test");
		}
		return primaryStageRef.getTitle();
	}

    private Button getButton(String name) {
        return getElement(name, Button.class);
    }

    private TextField getTextField(String name) {
        return getElement(name, TextField.class);
    }

    private ChoiceBox<?> getChoiceBox(String name) {
        return getElement(name, ChoiceBox.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T getElement(String name, Class<T> clazz) {
        T element = null;

        try {
            Field field = this.clazz.getDeclaredField(name);
            field.setAccessible(true);
            element = (T) field.get(playerRef);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return element;
    }

	// Printing of debug messages
	// Depends on attribute 'debug' of this class
	private void printDebug(String msg) {
		if (this.debug) {
			System.out.printf("DEBUG:%s\n", msg);
		}
	}

}
