import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import no.ntnu.Board;
import no.ntnu.tile.Tile;

/**
 * Tests for the Board class.
 */
public class BoardTest {

    @Test
    void testBoardRegistration() {
        Board board = new Board();
        Tile tile1 = new Tile(1);
        Tile tile2 = new Tile(2);

        board.setFirstTile(tile1); 
        tile1.setNextTile(tile2);
        board.registerTile(tile2);

        Assertions.assertEquals(tile1, board.getFirstTile(),
            "First tile should be tile1.");
        Assertions.assertEquals(tile1, board.getTileById(1),
            "Tile with ID 1 should be tile1.");
        Assertions.assertEquals(tile2, board.getTileById(2),
            "Tile with ID 2 should be tile2.");
    }

    @Test
    void testGetLastTile() {
        Board board = new Board();
        Tile first = new Tile(1);
        board.setFirstTile(first);

        Tile second = new Tile(2);
        Tile third = new Tile(3);
        first.setNextTile(second);
        second.setNextTile(third);
        board.registerTile(second);
        board.registerTile(third);

        Assertions.assertEquals(third, board.getLastTile(),
            "The last tile should be tile with ID 3.");
    }
}
