
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.tile.*;
import no.ntnu.action.*;
import no.ntnu.Board;
import no.ntnu.Player;

/**
 * Tests for Board and Tile classes including registration, validation, and actions.
 */
public class BoardAndTileTest {
    
    private Board board;
    
    @BeforeEach
    void setUp() {
        board = new Board();
    }
    
    @Test
    void testBoardRegistration() {
        Tile tile1 = new Tile(1);
        Tile tile2 = new Tile(2);
        Tile tile3 = new Tile(3);
        
        board.setFirstTile(tile1);
        board.registerTile(tile2);
        board.registerTile(tile3);
        
        assertEquals(tile1, board.getFirstTile());
        assertEquals(tile1, board.getTileById(1));
        assertEquals(tile2, board.getTileById(2));
        assertEquals(tile3, board.getTileById(3));
        assertEquals(3, board.getTileCount());
    }
    
    @Test
    void testBoardRegisterNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            board.registerTile(null);
        });
    }
    
    @Test
    void testBoardRegisterDuplicateId() {
        Tile tile1 = new Tile(1);
        Tile tile1Dup = new Tile(1);
        
        board.registerTile(tile1);
        board.registerTile(tile1Dup);
        
        assertEquals(tile1, board.getTileById(1));
        assertEquals(1, board.getTileCount());
    }
    
    @Test
    void testBoardValidationNoFirstTile() {
        assertThrows(IllegalStateException.class, () -> {
            board.validate();
        });
    }
    
    @Test
    void testBoardValidationWrongCount() {
        Tile tile1 = new Tile(1);
        board.setFirstTile(tile1);
        
        assertThrows(IllegalStateException.class, () -> {
            board.validate();
        });
    }
    
    @Test
    void testBoardValidationNotCircular() {
        Tile first = new Tile(1);
        board.setFirstTile(first);
        
        for (int i = 2; i <= 40; i++) {
            Tile tile = new Tile(i);
            board.registerTile(tile);
        }
        
        Tile current = first;
        for (int i = 2; i <= 40; i++) {
            Tile next = board.getTileById(i);
            current.setNextTile(next);
            current = next;
        }
        
        assertThrows(IllegalStateException.class, () -> {
            board.validate();
        });
    }
    
    @Test
    void testBoardValidationComplete() {
        Tile first = new Tile(1);
        board.setFirstTile(first);
        
        Tile current = first;
        for (int i = 2; i <= 40; i++) {
            Tile tile = new Tile(i);
            current.setNextTile(tile);
            board.registerTile(tile);
            current = tile;
        }
        current.setNextTile(first);
        
        assertDoesNotThrow(() -> board.validate());
    }
    
    @Test
    void testTileBasics() {
        Tile tile = new Tile(5);
        
        assertEquals(5, tile.getId());
        assertEquals("Tile 5", tile.getName());
        assertEquals(0, tile.getRent());
        assertEquals(0, tile.getPrice());
        assertNull(tile.getNextTile());
        assertNull(tile.getAction());
    }
    
    @Test
    void testTileLinks() {
        Tile tile1 = new Tile(1);
        Tile tile2 = new Tile(2);
        Tile tile3 = new Tile(3);
        
        tile1.setNextTile(tile2);
        tile2.setNextTile(tile3);
        
        assertEquals(tile2, tile1.getNextTile());
        assertEquals(tile3, tile2.getNextTile());
        assertNull(tile3.getNextTile());
    }
    
    @Test
    void testTileAction() {
        Tile tile = new Tile(1);
        TileAction action = new SkipTurnAction();
        
        tile.setAction(action);
        assertEquals(action, tile.getAction());
    }
    
    @Test
    void testNamedTile() {
        NamedTile go = new NamedTile(1, "GO");
        NamedTile jail = new NamedTile(11, "Jail");
        
        assertEquals(1, go.getId());
        assertEquals("GO", go.getName());
        assertEquals(11, jail.getId());
        assertEquals("Jail", jail.getName());
    }
    
    @Test
    void testPropertyTile() {
        PropertyTile prop = new PropertyTile(2, "Baltic Avenue", PropertyColor.BROWN, 60, 4, 50);
        
        assertEquals(2, prop.getId());
        assertEquals("Baltic Avenue", prop.getName());
        assertEquals(60, prop.getPrice());
        assertEquals(PropertyColor.BROWN, prop.getColor());
        assertEquals(50, prop.getHousePrice());
        assertEquals(0, prop.getHouseCount());
        assertFalse(prop.hasHotel());
        assertNull(prop.getOwner());
        assertFalse(prop.isOwned());
        
        Player owner = new Player("Owner");
        prop.setOwner(owner);
        
        assertTrue(prop.isOwned());
        assertEquals(owner, prop.getOwner());
        assertEquals(4, prop.getRent());
    }
    
    @Test
    void testUtilityTile() {
        UtilityTile util = new UtilityTile(13, "Electric Company", 150);
        
        assertEquals(13, util.getId());
        assertEquals("Electric Company", util.getName());
        assertEquals(150, util.getPrice());
        assertNull(util.getOwner());
        assertFalse(util.isOwned());
        
        util.setRentContext(7, 1);
        assertEquals(28, util.getRent());
        
        util.setRentContext(10, 2);
        assertEquals(100, util.getRent());
    }
    
    @Test
    void testUtilityTileBackwardCompatibility() {
        UtilityTile oldUtil = new UtilityTile(28, 150);
        
        assertEquals(28, oldUtil.getId());
        assertEquals("Utility 28", oldUtil.getName());
        assertEquals(150, oldUtil.getPrice());
    }
    
    @Test
    void testPropertyMonopolyRent() {
        PropertyTile prop = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        Player owner = new Player("Monopolist");
        
        prop.setOwner(owner);
        assertEquals(4, prop.getRent());
        
        PropertyTile prop2 = new PropertyTile(4, "Mediterranean", PropertyColor.BROWN, 60, 2, 50);
        owner.addOwned(prop);
        owner.addOwned(prop2);
        
        assertEquals(8, prop.getRent());
    }
    
    @Test
    void testPropertyBuildingMechanics() {
        PropertyTile prop = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        Player owner = new Player("Builder");
        owner.adjustBalance(500);
        
        assertFalse(prop.canBuildHouse());
        
        prop.setOwner(owner);
        PropertyTile prop2 = new PropertyTile(4, "Mediterranean", PropertyColor.BROWN, 60, 2, 50);
        owner.addOwned(prop);
        owner.addOwned(prop2);
        
        assertTrue(prop.canBuildHouse());
        
        prop.addHouse();
        assertEquals(1, prop.getHouseCount());
        assertEquals(24, prop.getRent());
        
        prop.addHouse();
        prop.addHouse();
        prop.addHouse();
        assertEquals(4, prop.getHouseCount());
        assertFalse(prop.canBuildHouse());
        assertTrue(prop.canBuildHotel());
        
        prop.buildHotel();
        assertTrue(prop.hasHotel());
        assertEquals(0, prop.getHouseCount());
        assertEquals(200, prop.getRent());
    }
}