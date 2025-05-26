package ui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.util.List;
import no.ntnu.ui.PlayerSetupView;


/**
 * TODO
 * Tests for PlayerSetupView UI component logic.
 */
public class PlayerSetupViewTest {

    @Test
    void testStartButtonExists() {
        PlayerSetupView view = new PlayerSetupView();
        
        assertNotNull(view.getStartButton());
        assertEquals("Start Game", view.getStartButton().getText());
    }
    
    @Test
    void testInitialPlayerFieldCount() {
        PlayerSetupView view = new PlayerSetupView();
        
        VBox fieldsBox = findFieldsBox(view);
        assertNotNull(fieldsBox);
        assertEquals(4, fieldsBox.getChildren().size());
    }
    
    @Test
    void testGetPlayerNamesWithEmptyFields() {
        PlayerSetupView view = new PlayerSetupView();
        
        List<String> names = view.getPlayerNames();
        
        assertEquals(4, names.size());
        assertEquals("Player 1", names.get(0));
        assertEquals("Player 2", names.get(1));
        assertEquals("Player 3", names.get(2));
        assertEquals("Player 4", names.get(3));
    }
    
    @Test
    void testGetPlayerNamesWithFilledFields() {
        PlayerSetupView view = new PlayerSetupView();
        VBox fieldsBox = findFieldsBox(view);
        
        if (fieldsBox != null && fieldsBox.getChildren().size() >= 2) {
            ((TextField)fieldsBox.getChildren().get(0)).setText("Alice");
            ((TextField)fieldsBox.getChildren().get(1)).setText("Bob");
            
            List<String> names = view.getPlayerNames();
            
            assertEquals("Alice", names.get(0));
            assertEquals("Bob", names.get(1));
        }
    }
    
    @Test
    void testButtonsExist() {
        PlayerSetupView view = new PlayerSetupView();
        
        long buttonCount = view.getChildren().stream()
            .filter(node -> node instanceof HBox)
            .flatMap(hbox -> ((HBox)hbox).getChildren().stream())
            .filter(node -> node instanceof Button)
            .count();
        
        assertTrue(buttonCount >= 2);
    }
    
    /**
     * Helper method to find the fields VBox.
     * 
     * @param view the PlayerSetupView
     * @return the VBox containing player fields
     */
    private VBox findFieldsBox(PlayerSetupView view) {
        return view.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .map(node -> (VBox)node)
            .filter(vbox -> vbox.getChildren().size() > 0 && 
                          vbox.getChildren().get(0) instanceof TextField)
            .findFirst()
            .orElse(null);
    }
}