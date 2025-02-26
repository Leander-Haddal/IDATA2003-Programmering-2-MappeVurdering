import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import no.ntnu.Player;
import no.ntnu.Tile;

/**
 * Tests for the Player class.
 */
public class PlayerTest {

    @Test
    void testPlayerNameAndInitialTile() {
        Player player = new Player("Alice");
        Assertions.assertEquals("Alice", player.getName(),
            "Player name should be 'Alice'.");
        Assertions.assertEquals(-1, player.getCurrentTileId(),
            "Player should start with no tile assigned (ID = -1).");
    }

    @Test
    void testSetCurrentTile() {
        Player player = new Player("Bob");
        Tile tile = new Tile(5);
        player.setCurrentTile(tile);
        Assertions.assertEquals(tile, player.getCurrentTile(),
            "Player should be on tile #5");
        Assertions.assertEquals(5, player.getCurrentTileId(),
            "Player's current tile ID should be 5.");
    }
}
