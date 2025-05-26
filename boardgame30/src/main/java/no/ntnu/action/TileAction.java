package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;

/**
 * Represents any special action triggered when a player lands on a tile.
 */
public interface TileAction {
    /**
     * Execute this action for the given player within the game context.
     * @param player the player landing on the tile
     * @param game the game instance for callbacks
     */
    void execute(Player player, BoardGame game);
}