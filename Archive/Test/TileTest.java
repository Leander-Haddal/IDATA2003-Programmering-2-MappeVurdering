import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import no.ntnu.Player;
import no.ntnu.Archive.LadderAction;
import no.ntnu.tile.Tile;

/**
 * Tests for the Tile class.
 */
public class TileTest {

    @Test
    void testTileCreationAndLink() {
        Tile tile1 = new Tile(1);
        Tile tile2 = new Tile(2);
        tile1.setNextTile(tile2);

        Assertions.assertEquals(1, tile1.getId(), 
            "Tile1 should have ID 1.");
        Assertions.assertEquals(2, tile2.getId(), 
            "Tile2 should have ID 2.");
        Assertions.assertEquals(tile2, tile1.getNextTile(),
            "tile1's nextTile should be tile2.");
    }

    @Test
    void testTileAction() {
        Tile tile1 = new Tile(1);
        Tile tile2 = new Tile(2);
        tile1.setAction(new LadderAction(tile2));
        Player testPlayer = new Player("Test");

        tile1.getAction().execute(testPlayer);
        
        Assertions.assertEquals(tile2, testPlayer.getCurrentTile(),
            "Player should be moved to tile2 by LadderAction.");
    }
}
