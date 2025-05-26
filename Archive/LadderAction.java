package no.ntnu.Archive;

import no.ntnu.Player;
import no.ntnu.action.TileAction;
import no.ntnu.tile.Tile;

/**
 * TileAction for moving a player to a specific destination tile,
 * simulating a ladder in the game.
 */
public class LadderAction implements TileAction {
    private final Tile destination;

    public LadderAction(Tile destination) {
        this.destination = destination;
    }

    @Override
    public void execute(Player player) {
        System.out.println("  [LadderAction] Moving " + player.getName() 
                           + " from Tile " + player.getCurrentTileId() 
                           + " to Tile " + destination.getId());
        player.setCurrentTile(destination);
    }
}