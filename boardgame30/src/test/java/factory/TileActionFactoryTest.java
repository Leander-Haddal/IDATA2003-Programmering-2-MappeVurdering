package factory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.*;
import no.ntnu.action.*;
import no.ntnu.tile.*;
import no.ntnu.exception.InvalidDataException;
import no.ntnu.factory.TileActionFactory;

/**
 * Tests for TileActionFactory.
 */
public class TileActionFactoryTest {
    
    private Board board;
    private BoardGame game;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        Tile go = new NamedTile(1, "GO");
        board.setFirstTile(go);
        
        Dice dice = new Dice(2);
        game = new BoardGame(dice);
        game.setBoard(board);
    }
    
    @Test
    void testCreateGoAction() throws InvalidDataException {
        TileAction action = TileActionFactory.create("GoAction", 1, board, game);
        assertNotNull(action);
        assertTrue(action instanceof GoAction);
    }
    
    @Test
    void testCreatePropertyAction() throws InvalidDataException {
        PropertyTile prop = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        board.registerTile(prop);
        
        TileAction action = TileActionFactory.create("PropertyAction", 2, board, game);
        assertNotNull(action);
        assertTrue(action instanceof PropertyAction);
    }
    
    @Test
    void testCreatePropertyActionWrongTileType() {
        Tile regular = new Tile(2);
        board.registerTile(regular);
        
        assertThrows(InvalidDataException.class, () -> {
            TileActionFactory.create("PropertyAction", 2, board, game);
        });
    }
    
    @Test
    void testCreateRailroadAction() throws InvalidDataException {
        RailroadTile rail = new RailroadTile(6, "Reading Railroad", 200);
        board.registerTile(rail);
        
        TileAction action = TileActionFactory.create("RailroadAction", 6, board, game);
        assertNotNull(action);
        assertTrue(action instanceof RailroadAction);
    }
    
    @Test
    void testCreateRailroadActionWrongTileType() {
        Tile regular = new Tile(6);
        board.registerTile(regular);
        
        assertThrows(InvalidDataException.class, () -> {
            TileActionFactory.create("RailroadAction", 6, board, game);
        });
    }
    
    @Test
    void testCreateUtilityAction() throws InvalidDataException {
        UtilityTile util = new UtilityTile(13, "Electric Company", 150);
        board.registerTile(util);
        
        TileAction action = TileActionFactory.create("UtilityAction", 13, board, game);
        assertNotNull(action);
        assertTrue(action instanceof UtilityAction);
    }
    
    @Test
    void testCreateUtilityActionWrongTileType() {
        Tile regular = new Tile(13);
        board.registerTile(regular);
        
        assertThrows(InvalidDataException.class, () -> {
            TileActionFactory.create("UtilityAction", 13, board, game);
        });
    }
    
    @Test
    void testCreateTaxAction() throws InvalidDataException {
        Tile incomeTax = new NamedTile(5, "Income Tax");
        Tile luxuryTax = new NamedTile(39, "Luxury Tax");
        board.registerTile(incomeTax);
        board.registerTile(luxuryTax);
        
        TileAction action1 = TileActionFactory.create("TaxAction", 5, board, game);
        TileAction action2 = TileActionFactory.create("TaxAction", 39, board, game);
        
        assertNotNull(action1);
        assertNotNull(action2);
        assertTrue(action1 instanceof TaxAction);
        assertTrue(action2 instanceof TaxAction);
    }
    
    @Test
    void testCreateTaxActionInvalidTileId() {
        assertThrows(InvalidDataException.class, () -> {
            TileActionFactory.create("TaxAction", 10, board, game);
        });
    }
    
    @Test
    void testCreateChestAction() throws InvalidDataException {
        TileAction action = TileActionFactory.create("ChestAction", 3, board, game);
        assertNotNull(action);
        assertTrue(action instanceof ChestAction);
    }
    
    @Test
    void testCreateChanceAction() throws InvalidDataException {
        TileAction action = TileActionFactory.create("ChanceAction", 8, board, game);
        assertNotNull(action);
        assertTrue(action instanceof ChanceAction);
    }
    
    @Test
    void testCreateGoToJailAction() throws InvalidDataException {
        Tile jail = new NamedTile(11, "Jail");
        board.registerTile(jail);
        
        TileAction action = TileActionFactory.create("GoToJailAction", 31, board, game);
        assertNotNull(action);
        assertTrue(action instanceof GoToJailAction);
    }
    
    @Test
    void testCreateGoToJailActionNoJailTile() {
        assertThrows(InvalidDataException.class, () -> {
            TileActionFactory.create("GoToJailAction", 31, board, game);
        });
    }
    
    @Test
    void testCreateSkipTurnAction() throws InvalidDataException {
        TileAction action = TileActionFactory.create("SkipTurnAction", 21, board, game);
        assertNotNull(action);
        assertTrue(action instanceof SkipTurnAction);
    }
    
    
    @Test
    void testCreateUnknownAction() throws InvalidDataException {
        TileAction action = TileActionFactory.create("UnknownAction", 1, board, game);
        assertNull(action);
    }
}