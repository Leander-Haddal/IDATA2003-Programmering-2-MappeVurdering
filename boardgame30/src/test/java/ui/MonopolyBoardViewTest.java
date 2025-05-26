package ui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;

import no.ntnu.Player;
import no.ntnu.tile.Tile;

import java.util.Arrays;
import java.util.List;
import no.ntnu.ui.MonopolyBoardView;

/**
 * Tests for MonopolyBoardView UI component logic.
 */
public class MonopolyBoardViewTest {
    
    @Test
    void testBoardViewConstruction() {
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", 600);
        
        assertNotNull(boardView);
        assertEquals(2, boardView.getChildren().size());
    }
    
    @Test
    void testBoardViewComponents() {
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", 500);
        
        assertTrue(boardView.getChildren().get(0) instanceof ImageView);
        assertTrue(boardView.getChildren().get(1) instanceof Canvas);
    }
    
    @Test
    void testCanvasInitialSize() {
        double initialSize = 400;
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", initialSize);
        
        Canvas canvas = (Canvas) boardView.getChildren().get(1);
        assertEquals(initialSize, canvas.getWidth());
        assertEquals(initialSize, canvas.getHeight());
    }
    
    @Test
    void testDrawTokensWithEmptyList() {
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", 500);
        List<Player> emptyList = Arrays.asList();
        
        assertDoesNotThrow(() -> boardView.drawTokens(emptyList));
    }
    
    @Test
    void testDrawTokensWithNullTiles() {
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", 500);
        
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        
        List<Player> players = Arrays.asList(p1, p2);
        
        assertDoesNotThrow(() -> boardView.drawTokens(players));
    }
    
    @Test
    void testDrawTokensWithValidPlayers() {
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", 500);
        
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        Player p3 = new Player("Charlie");
        
        p1.setCurrentTile(new Tile(1));
        p2.setCurrentTile(new Tile(10));
        p3.setCurrentTile(new Tile(20));
        
        List<Player> players = Arrays.asList(p1, p2, p3);
        
        assertDoesNotThrow(() -> boardView.drawTokens(players));
    }
    
    @Test
    void testDrawTokensAllCorners() {
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", 600);
        
        Player p1 = new Player("Corner1");
        Player p2 = new Player("Corner2");
        Player p3 = new Player("Corner3");
        Player p4 = new Player("Corner4");
        
        p1.setCurrentTile(new Tile(1));
        p2.setCurrentTile(new Tile(11));
        p3.setCurrentTile(new Tile(21));
        p4.setCurrentTile(new Tile(31));
        
        List<Player> players = Arrays.asList(p1, p2, p3, p4);
        
        assertDoesNotThrow(() -> boardView.drawTokens(players));
    }
    
    @Test
    void testDrawTokensMaxPlayers() {
        MonopolyBoardView boardView = new MonopolyBoardView("/images/Monopol.jpg", 400);
        
        List<Player> players = Arrays.asList(
            new Player("P1"), new Player("P2"), new Player("P3"),
            new Player("P4"), new Player("P5"), new Player("P6"),
            new Player("P7"), new Player("P8")
        );
        
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setCurrentTile(new Tile((i * 5) + 1));
        }
        
        assertDoesNotThrow(() -> boardView.drawTokens(players));
    }
}