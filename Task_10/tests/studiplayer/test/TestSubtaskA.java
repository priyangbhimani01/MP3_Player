package studiplayer.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.application.Application;
import studiplayer.ui.Player;

public class TestSubtaskA {
    @Test
    public void testInheritanceFromApplication() {
        assertTrue("Class Player must be a subclass of Application", Application.class.isAssignableFrom(Player.class));
    }
}
