package no.ntnu.Archive;

import no.ntnu.Board;
import no.ntnu.factory.TileActionFactory;
import no.ntnu.tile.Tile;
import no.ntnu.exception.InvalidDataException;
import com.google.gson.JsonSyntaxException;
import java.nio.file.Path;
import java.io.IOException;

/**
 * Central factory for creating Board instances.
 */
public final class BoardFactory {
    private BoardFactory() {
    }

    /**
     * Create a default 10-tile board
     */
    public static Board createDefaultBoard() {
        Board board = new Board();
        Tile first = new Tile(1);
        board.setFirstTile(first);
        Tile current = first;
        for (int i = 2; i <= 10; i++) {
            Tile next = new Tile(i);
            current.setNextTile(next);
            board.registerTile(next);
            current = next;
        }
        try {
            current = board.getTileById(3);
            current.setAction(TileActionFactory.create("LadderAction", 7, board, /*pass game*/ null));
        } catch (InvalidDataException e) {
        }
        return board;
    }

    /**
     * Load a board configuration from a JSON file.
     * @param jsonPath path to JSON
     * @return the loaded Board
     */
    public static Board fromJson(Path jsonPath)
            throws IOException, InvalidDataException {
        try {
            return new BoardJsonDao().loadBoard(jsonPath);
        } catch (JsonSyntaxException e) {
            throw new InvalidDataException("Malformed JSON for board", e);
        }
    }
}
