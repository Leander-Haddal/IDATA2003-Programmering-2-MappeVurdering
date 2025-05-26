import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import no.ntnu.Player;
import no.ntnu.tile.Tile;
import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.PropertyColor;
import no.ntnu.observer.ConsoleGameLogger;

/**
 * Tests for ConsoleGameLogger output.
 */
public class ConsoleGameLoggerTest {
    
    private ConsoleGameLogger logger;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        logger = new ConsoleGameLogger();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    void testOnDiceRoll() {
        Player player = new Player("Alice");
        logger.onDiceRoll(player, 3, 4);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Alice rolled 3 and 4 for 7"));
    }
    
    @Test
    void testOnPlayerMoved() {
        Player player = new Player("Bob");
        Tile from = new Tile(1);
        Tile to = new Tile(5);
        
        logger.onPlayerMoved(player, from, to, 4);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Bob moves from 1 to 5 in 4 steps"));
    }
    
    @Test
    void testOnTileAction() {
        Player player = new Player("Charlie");
        Tile tile = new Tile(10);
        
        logger.onTileAction(player, "TaxAction", tile);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Charlie executes TaxAction on tile 10"));
    }
    
    @Test
    void testOnPropertyAvailable() {
        Player player = new Player("Dave");
        PropertyTile property = new PropertyTile(3, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        
        logger.onPropertyAvailable(player, property);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Dave may buy Baltic for 60"));
    }
    
    @Test
    void testOnRentPaid() {
        Player payer = new Player("Eve");
        Player owner = new Player("Frank");
        PropertyTile property = new PropertyTile(5, "Park Place", PropertyColor.DARK_BLUE, 350, 35, 175);
        
        logger.onRentPaid(payer, owner, property, 100);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Eve pays 100 to Frank for Park Place"));
    }
    
    @Test
    void testOnPlayerSkipped() {
        Player player = new Player("Grace");
        
        logger.onPlayerSkipped(player);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Grace skips turn"));
    }
    
    @Test
    void testOnGameEnd() {
        Player winner = new Player("Henry");
        
        logger.onGameEnd(winner);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Game over winner Henry"));
    }
    
    @Test
    void testMultipleEvents() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        
        logger.onDiceRoll(player1, 2, 3);
        logger.onPlayerMoved(player1, new Tile(1), new Tile(6), 5);
        logger.onPlayerSkipped(player2);
        
        String output = outputStream.toString();
        String[] lines = output.split(System.lineSeparator());
        
        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("Alice rolled"));
        assertTrue(lines[1].contains("Alice moves"));
        assertTrue(lines[2].contains("Bob skips"));
    }
}