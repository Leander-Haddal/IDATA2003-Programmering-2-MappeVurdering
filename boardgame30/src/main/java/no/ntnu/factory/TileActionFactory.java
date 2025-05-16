package no.ntnu.factory;

import no.ntnu.TileAction;
import no.ntnu.LadderAction;
import no.ntnu.Board;
import no.ntnu.Tile;
import no.ntnu.exception.InvalidDataException;

/**
 * Create TileAction instances based on type.
 */
public class TileActionFactory {

    /**
     * Build a TileAction of given type for the board.
     * @param type action class name (e.g. "LadderAction")
     * @param destinationId target tile ID for the action
     * @param board the board containing the tiles
     * @return the new TileAction
     * @throws InvalidDataException if type unknown or destination missing
     */
    public static TileAction create(String type, int destinationId, Board board)
            throws InvalidDataException {
        if ("LadderAction".equals(type)) {
            Tile dest = board.getTileById(destinationId);
            if (dest == null) {
                throw new InvalidDataException("No tile with ID " + destinationId);
            }
            return new LadderAction(dest);
        }
        throw new InvalidDataException("Unknown action type: " + type);
    }
}
