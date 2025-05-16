package no.ntnu.observer;

import no.ntnu.Player;
import no.ntnu.Tile;

/**
 * Observer for game events in BoardGame
 */
public interface GameObserver {

    /**
     * Called after a player has moved to a tile
     * @param player the player who moved
     * @param toTile the tile the player landed on
     */
    void onPlayerMoved(Player player, Tile toTile);

    /**
     * Called when the game is finished and a winner has been determined
     * @param winner the winning player
     */
    void onGameFinished(Player winner);

    /**
     * Called whenever it becomes the given players turn.
     */
    void onTurnChanged(Player nextPlayer);
}
