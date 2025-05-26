package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;
import no.ntnu.tile.Tile;

/**
 * Action that sends a player directly to jail.
 */
public class GoToJailAction implements TileAction {
    private final Tile jailTile;

    /**
     * Creates a go to jail action.
     * 
     * @param jailTile the jail tile to send players to
     */
    public GoToJailAction(Tile jailTile) {
        this.jailTile = jailTile;
    }

    /**
     * Sends the player to jail without collecting GO salary.
     * 
     * @param player the player to send to jail
     * @param game the game instance
     */
    @Override
    public void execute(Player player, BoardGame game) {
        player.setCurrentTile(jailTile);
        player.goToJail();
    }
}