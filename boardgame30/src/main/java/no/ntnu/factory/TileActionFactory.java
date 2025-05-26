package no.ntnu.factory;

import no.ntnu.Board;
import no.ntnu.BoardGame;
import no.ntnu.exception.InvalidDataException;
import no.ntnu.action.TileAction;
import no.ntnu.action.PropertyAction;
import no.ntnu.action.RailroadAction;
import no.ntnu.action.UtilityAction;
import no.ntnu.action.GoAction;
import no.ntnu.action.ChestAction;
import no.ntnu.action.ChanceAction;
import no.ntnu.action.TaxAction;
import no.ntnu.action.GoToJailAction;
import no.ntnu.action.SkipTurnAction;
import no.ntnu.tile.Tile;
import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.RailroadTile;
import no.ntnu.tile.UtilityTile;

/**
 * Factory to create TileAction instances based on a string key and tile type.
 */
public class TileActionFactory {

    /**
     * Create a TileAction for the given actionKey and tile.
     * @param type action key (e.g. "PropertyAction")
     * @param tileId ID of the tile
     * @param board board for lookup (e.g. jail)
     * @param game game instance (unused by most actions)
     * @return new TileAction or null if no action
     * @throws InvalidDataException if type unknown or tile mismatch
     */
    public static TileAction create(String type, int tileId, Board board, BoardGame game) throws InvalidDataException {
        Tile t = board.getTileById(tileId);
        switch (type) {
            case "GoAction":
                return new GoAction(200);
            case "PropertyAction":
                if (!(t instanceof PropertyTile pt)) {
                    throw new InvalidDataException("Tile #" + tileId + " is not a PropertyTile");
                }
                return new PropertyAction(pt);
            case "RailroadAction":
                if (!(t instanceof RailroadTile rt)) {
                    throw new InvalidDataException("Tile #" + tileId + " is not a RailroadTile");
                }
                return new RailroadAction(rt);
            case "UtilityAction":
                if (!(t instanceof UtilityTile ut)) {
                    throw new InvalidDataException("Tile #" + tileId + " is not a UtilityTile");
                }
                return new UtilityAction(ut);
            case "TaxAction":
                if (tileId == 5) {
                    return new TaxAction(200);
                } else if (tileId == 39) {
                    return new TaxAction(100);
                } else {
                    throw new InvalidDataException("Unknown tax tile #" + tileId);
                }
            case "ChestAction":
                return new ChestAction();
            case "ChanceAction":
                return new ChanceAction();
            case "GoToJailAction":
                Tile jailTile = board.getTileById(11);
                if (jailTile == null) {
                    throw new InvalidDataException("Jail tile not found id=11");
                }
                return new GoToJailAction(jailTile);
            case "SkipTurnAction":
                return new SkipTurnAction();
            default:
                // no action for null
                return null;
        }
    }
}
