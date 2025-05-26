
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.RailroadTile;
import no.ntnu.tile.UtilityTile;
import no.ntnu.tile.PropertyColor;
import no.ntnu.tile.Tile;
import no.ntnu.Player;

/**
 * Tests for the Player class including balance, ownership, and jail mechanics.
 */
public class PlayerTest {
    
    private Player player;
    
    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", "car");
    }
    
    @Test
    void testConstructorAndBasicGetters() {
        assertEquals("TestPlayer", player.getName());
        assertEquals("car", player.getToken());
        assertEquals(1500, player.getBalance());
        assertNull(player.getCurrentTile());
        assertEquals(-1, player.getCurrentTileId());
        assertFalse(player.isSkipNextTurn());
        assertFalse(player.isInJail());
    }
    
    @Test
    void testBackwardsCompatibilityConstructor() {
        Player oldPlayer = new Player("OldStyle");
        assertEquals("OldStyle", oldPlayer.getName());
        assertEquals("", oldPlayer.getToken());
    }
    
    @Test
    void testSetCurrentTile() {
        Tile tile = new Tile(5);
        player.setCurrentTile(tile);
        assertEquals(tile, player.getCurrentTile());
        assertEquals(5, player.getCurrentTileId());
    }
    
    @Test
    void testBalanceAdjustments() {
        player.adjustBalance(200);
        assertEquals(1700, player.getBalance());
        
        player.adjustBalance(-500);
        assertEquals(1200, player.getBalance());
        
        player.adjustBalance(-1300);
        assertEquals(-100, player.getBalance());
    }
    
    @Test
    void testSkipNextTurn() {
        assertFalse(player.isSkipNextTurn());
        player.setSkipNextTurn(true);
        assertTrue(player.isSkipNextTurn());
        player.setSkipNextTurn(false);
        assertFalse(player.isSkipNextTurn());
    }
    
    @Test
    void testJailMechanics() {
        assertFalse(player.isInJail());
        assertEquals(0, player.getJailTurns());
        
        player.goToJail();
        assertTrue(player.isInJail());
        assertEquals(0, player.getJailTurns());
        
        player.incrementJailTurns();
        player.incrementJailTurns();
        assertEquals(2, player.getJailTurns());
        
        player.releaseFromJail();
        assertFalse(player.isInJail());
        assertEquals(0, player.getJailTurns());
    }
    
    @Test
    void testPropertyOwnership() {
        PropertyTile prop1 = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        PropertyTile prop2 = new PropertyTile(4, "Mediterranean", PropertyColor.BROWN, 60, 2, 50);
        RailroadTile rail = new RailroadTile(6, "Reading Railroad", 200);
        UtilityTile util = new UtilityTile(13, "Electric Company", 150);
        
        assertTrue(player.getOwnedProperties().isEmpty());
        assertTrue(player.getOwnedRailroads().isEmpty());
        assertTrue(player.getOwnedUtilities().isEmpty());
        
        player.addOwned(prop1);
        player.addOwned(prop2);
        player.addOwned(rail);
        player.addOwned(util);
        
        assertEquals(2, player.getOwnedProperties().size());
        assertEquals(1, player.getOwnedRailroads().size());
        assertEquals(1, player.getOwnedUtilities().size());
        
        assertTrue(player.getOwnedProperties().contains(prop1));
        assertTrue(player.getOwnedProperties().contains(prop2));
        assertTrue(player.getOwnedRailroads().contains(rail));
        assertTrue(player.getOwnedUtilities().contains(util));
    }
    
    @Test
    void testClearOwnedProperties() {
        PropertyTile prop = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        RailroadTile rail = new RailroadTile(6, "Reading Railroad", 200);
        
        player.addOwned(prop);
        player.addOwned(rail);
        
        assertFalse(player.getOwnedProperties().isEmpty());
        assertFalse(player.getOwnedRailroads().isEmpty());
        
        player.clearOwnedProperties();
        
        assertTrue(player.getOwnedProperties().isEmpty());
        assertTrue(player.getOwnedRailroads().isEmpty());
    }
    
    @Test
    void testHasMonopoly() {
        PropertyTile brown1 = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        PropertyTile brown2 = new PropertyTile(4, "Mediterranean", PropertyColor.BROWN, 60, 2, 50);
        PropertyTile blue1 = new PropertyTile(38, "Park Place", PropertyColor.DARK_BLUE, 350, 35, 175);
        
        assertFalse(player.hasMonopoly(PropertyColor.BROWN));
        assertFalse(player.hasMonopoly(PropertyColor.DARK_BLUE));
        assertFalse(player.hasMonopoly(null));
        
        player.addOwned(brown1);
        assertFalse(player.hasMonopoly(PropertyColor.BROWN));
        
        player.addOwned(brown2);
        assertTrue(player.hasMonopoly(PropertyColor.BROWN));
        
        player.addOwned(blue1);
        assertFalse(player.hasMonopoly(PropertyColor.DARK_BLUE));
    }
    
    @Test
    void testOwnedListsAreImmutable() {
        PropertyTile prop = new PropertyTile(2, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        player.addOwned(prop);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            player.getOwnedProperties().add(prop);
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            player.getOwnedRailroads().clear();
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            player.getOwnedUtilities().remove(0);
        });
    }
}