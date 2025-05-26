package ui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import no.ntnu.Player;
import java.util.Arrays;
import java.util.List;
import no.ntnu.ui.PlayerPane;


/**
 * TODO
 * Tests for PlayerPane UI component logic.
 */
public class PlayerPaneTest {
    
    @Test
    void testPlayerPaneCreation() {
        PlayerPane pane = new PlayerPane();
        
        assertNotNull(pane);
        assertNotNull(pane.getRollButton());
        assertNotNull(pane.getBuyButton());
        assertNotNull(pane.getNextButton());
        
        assertEquals("Roll", pane.getRollButton().getText());
        assertEquals("Buy", pane.getBuyButton().getText());
        assertEquals("Next", pane.getNextButton().getText());
    }
    
    @Test
    void testInitialButtonStates() {
        PlayerPane pane = new PlayerPane();
        
        assertTrue(pane.getRollButton().isDisabled());
        assertTrue(pane.getBuyButton().isDisabled());
        assertTrue(pane.getNextButton().isDisabled());
    }
    
    @Test
    void testButtonEnabling() {
        PlayerPane pane = new PlayerPane();
        
        pane.getRollButton().setDisable(false);
        assertFalse(pane.getRollButton().isDisabled());
        
        pane.getBuyButton().setDisable(false);
        assertFalse(pane.getBuyButton().isDisabled());
        
        pane.getNextButton().setDisable(false);
        assertFalse(pane.getNextButton().isDisabled());
    }
    
    @Test
    void testSetStatus() {
        PlayerPane pane = new PlayerPane();
        String testStatus = "Test Status Message";
        
        pane.setStatus(testStatus);
        
        assertTrue(pane.getChildren().stream()
            .filter(node -> node instanceof javafx.scene.control.Label)
            .map(node -> ((javafx.scene.control.Label)node).getText())
            .anyMatch(text -> testStatus.equals(text)));
    }
    
    @Test
    void testUpdatePlayerInfo() {
        PlayerPane pane = new PlayerPane();
        Player player = new Player("TestPlayer", "car");
        player.adjustBalance(500);
        
        List<String> properties = Arrays.asList("Park Place", "Baltic Ave", "Boardwalk");
        
        pane.update(player, properties);
        
        long labelCount = pane.getChildren().stream()
            .filter(node -> node instanceof javafx.scene.control.Label)
            .count();
        
        assertTrue(labelCount >= 2);
    }
}