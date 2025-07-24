package studiplayer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

import studiplayer.audio.PlayList;
import studiplayer.ui.Player;

public class TestSubtaskB {
    @Test
    public void testUseCertPlayList() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertTrue("Class Player must have an attribute useCertPlayList", Player.class.getDeclaredField("useCertPlayList") != null);
        assertTrue("Attribute useCertPlayList must be of type boolean", Player.class.getDeclaredField("useCertPlayList").getType().equals(boolean.class));
        assertTrue("Class Player must have a method setUseCertPlayList(value: boolean)", Player.class.getMethod("setUseCertPlayList", boolean.class) != null);
    }
    
    @Test
    public void testLoadPlayList() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertTrue("Class Player must have a method loadPlayList(pathname: String)", Player.class.getMethod("loadPlayList", String.class) != null);
    }
    
    @Test
    public void testDefaultPlaylistConstant() throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException {
        assertTrue("Class Player must have a constant named DEFAULT_PLAYLIST", Player.class.getDeclaredField("DEFAULT_PLAYLIST") != null);
        assertTrue("DEFAULT_PLAYLIST should be public", Modifier.isPublic(Player.class.getDeclaredField("DEFAULT_PLAYLIST").getModifiers()));
        assertTrue("DEFAULT_PLAYLIST should be static", Modifier.isStatic(Player.class.getDeclaredField("DEFAULT_PLAYLIST").getModifiers()));
        assertEquals("DEFAULT_PLAYLIST should be a String", String.class, Player.class.getDeclaredField("DEFAULT_PLAYLIST").getType());
        assertEquals("DEFAULT_PLAYLIST should contain playlists/DefaultPlayList.m3u", "playlists/DefaultPlayList.m3u", Player.class.getDeclaredField("DEFAULT_PLAYLIST").get(null));
    }
    
    @Test
    public void testPlayListAttribut() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertTrue("Class Player must have an attribute playList", Player.class.getDeclaredField("playList") != null);
        assertTrue("Attribute playList must be of type PlayList", Player.class.getDeclaredField("playList").getType().equals(PlayList.class));
    }
}
