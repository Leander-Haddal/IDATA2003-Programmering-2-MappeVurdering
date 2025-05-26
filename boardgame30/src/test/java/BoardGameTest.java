
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import no.ntnu.tile.*;
import no.ntnu.action.*;
import no.ntnu.Player;
import no.ntnu.BoardGame;
import no.ntnu.Board;
import no.ntnu.Dice;
import no.ntnu.tile.PropertyTile;


/**
 * Tests for the BoardGame class including turns, purchasing, bankruptcy, and jail.
 */
public class BoardGameTest {
    
    private Board board;
    private BoardGame game;
    private Player player1;
    private Player player2;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        Tile start = new Tile(1);
        board.setFirstTile(start);
        
        Dice dice = new Dice(2);
        game = new BoardGame(dice);
        game.setBoard(board);
        
        player1 = new Player("Alice", "car");
        player2 = new Player("Bob", "hat");
    }
    
    @Test
    void testAddPlayersAndInitialPosition() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        List<Player> players = game.getPlayers();
        assertEquals(2, players.size());
        assertTrue(players.contains(player1));
        assertTrue(players.contains(player2));
        
        assertEquals(board.getFirstTile(), player1.getCurrentTile());
        assertEquals(board.getFirstTile(), player2.getCurrentTile());
    }
    
    @Test
    void testGetCurrentPlayer() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(0, game.getCurrentPlayerIndex());
        
        game.nextTurn();
        assertEquals(player2, game.getCurrentPlayer());
        assertEquals(1, game.getCurrentPlayerIndex());
    }
    
    @Test
    void testNextTurnCycles() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        assertEquals(0, game.getCurrentPlayerIndex());
        game.nextTurn();
        assertEquals(1, game.getCurrentPlayerIndex());
        game.nextTurn();
        assertEquals(0, game.getCurrentPlayerIndex());
    }
    
    @Test
    void testNextTurnSkipsPlayer() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        player2.setSkipNextTurn(true);
        
        assertEquals(player1, game.getCurrentPlayer());
        game.nextTurn();
        assertEquals(player1, game.getCurrentPlayer());
        assertFalse(player2.isSkipNextTurn());
    }
    
    @Test
    void testBuyProperty() {
        PropertyTile property = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        board.getFirstTile().setNextTile(property);
        board.registerTile(property);
        
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        game.buyProperty(player1, property);
        
        assertEquals(player1, property.getOwner());
        assertEquals(1440, player1.getBalance());
        assertTrue(player1.getOwnedProperties().contains(property));
        
        game.buyProperty(player2, property);
        assertNotEquals(player2, property.getOwner());
        assertEquals(1500, player2.getBalance());
    }
    
    @Test
    void testBuyPropertyInsufficientFunds() {
        PropertyTile expensive = new PropertyTile(38, "Park Place", PropertyColor.DARK_BLUE, 350, 35, 175);
        
        game.addPlayer(player1);
        player1.adjustBalance(-1200);
        
        game.buyProperty(player1, expensive);
        
        assertNull(expensive.getOwner());
        assertEquals(300, player1.getBalance());
        assertFalse(player1.getOwnedProperties().contains(expensive));
    }
    
    @Test
    void testBuyRailroadAndUtility() {
        RailroadTile railroad = new RailroadTile(6, "Reading Railroad", 200);
        UtilityTile utility = new UtilityTile(13, "Electric Company", 150);
        
        game.addPlayer(player1);
        
        game.buyProperty(player1, railroad);
        game.buyProperty(player1, utility);
        
        assertEquals(player1, railroad.getOwner());
        assertEquals(player1, utility.getOwner());
        assertEquals(1, player1.getOwnedRailroads().size());
        assertEquals(1, player1.getOwnedUtilities().size());
    }
    
    @Test
    void testPlayTurnWithDiceRoll() {
        Tile t1 = board.getFirstTile();
        Tile t2 = new Tile(2);
        Tile t3 = new Tile(3);
        t1.setNextTile(t2);
        t2.setNextTile(t3);
        board.registerTile(t2);
        board.registerTile(t3);
        
        game.addPlayer(player1);
        
        game.playTurn();
        
        assertNotEquals(t1, player1.getCurrentTile());
        assertTrue(game.getLastRoll() >= 2 && game.getLastRoll() <= 12);
    }
    
    @Test
    void testPlayTurnWithSkip() {
        game.addPlayer(player1);
        player1.setSkipNextTurn(true);
        
        Tile startTile = player1.getCurrentTile();
        game.playTurn();
        
        assertEquals(startTile, player1.getCurrentTile());
        assertFalse(player1.isSkipNextTurn());
    }
    
    @Test
    void testCheckBankruptcy() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        player1.adjustBalance(-1600);
        
        assertTrue(game.checkBankruptcy(player1));
        assertEquals(1, game.getPlayers().size());
        assertFalse(game.getPlayers().contains(player1));
    }
    
    @Test
    void testBankruptcyClearsProperties() {
        PropertyTile prop = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        RailroadTile rail = new RailroadTile(6, "Reading Railroad", 200);
        
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        prop.setOwner(player1);
        rail.setOwner(player1);
        player1.addOwned(prop);
        player1.addOwned(rail);
        
        player1.adjustBalance(-1600);
        game.checkBankruptcy(player1);
        
        assertNull(prop.getOwner());
        assertNull(rail.getOwner());
    }
    
    @Test
    void testProcessRentPayment() {
        PropertyTile prop = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        game.processRentPayment(player1, player2, prop, 50);
        
        assertEquals(1450, player1.getBalance());
        assertEquals(1550, player2.getBalance());
    }
 
    
    @Test
    void testPayJailFine() {
        game.addPlayer(player1);
        
        assertFalse(game.payJailFine(player1));
        
        player1.goToJail();
        assertTrue(game.payJailFine(player1));
        assertEquals(1450, player1.getBalance());
        assertFalse(player1.isInJail());
        
        player1.goToJail();
        player1.adjustBalance(-1410);
        assertFalse(game.payJailFine(player1));
        assertTrue(player1.isInJail());
    }
    
    
}
