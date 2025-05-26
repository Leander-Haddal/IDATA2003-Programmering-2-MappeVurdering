package ui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.PropertyColor;
import no.ntnu.ui.GameView;
import no.ntnu.ui.MonopolyBoardView;

/**
 * TODO
 * Tests for GameView UI component logic.
 */
public class GameViewTest {
    
    @Test
    void testGameViewPlayerCount() {
        GameView view2 = new GameView(2);
        GameView view3 = new GameView(3);
        GameView view4 = new GameView(4);
        
        assertEquals(2, view2.getPlayerCount());
        assertEquals(3, view3.getPlayerCount());
        assertEquals(4, view4.getPlayerCount());
    }
    
    @Test
    void testGetPlayerPane() {
        GameView view = new GameView(4);
        
        assertNotNull(view.getPlayerPane(0));
        assertNotNull(view.getPlayerPane(1));
        assertNotNull(view.getPlayerPane(2));
        assertNotNull(view.getPlayerPane(3));
        
        assertThrows(IndexOutOfBoundsException.class, () -> view.getPlayerPane(4));
    }
    
    @Test
    void testPropertyOfferManagement() {
        GameView view = new GameView(2);
        PropertyTile property = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        
        assertNull(view.getOfferedProperty());
        
        view.setOfferedProperty(property);
        assertEquals(property, view.getOfferedProperty());
        
        view.clearPropertyOffer();
        assertNull(view.getOfferedProperty());
    }
    
    @Test
    void testBoardViewExists() {
        GameView view = new GameView(2);
        
        assertNotNull(view.getBoardView());
        assertTrue(view.getBoardView() instanceof MonopolyBoardView);
    }
    
    @Test
    void testCenterLeftRightComponents() {
        GameView view = new GameView(4);
        
        assertNotNull(view.getCenter());
        assertNotNull(view.getLeft());
        assertNotNull(view.getRight());
        
        assertEquals(view.getBoardView(), view.getCenter());
    }
}