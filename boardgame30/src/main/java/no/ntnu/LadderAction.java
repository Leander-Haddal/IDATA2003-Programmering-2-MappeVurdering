package no.ntnu;

/**
 * Simple example of a TileAction that moves the player to a specific destination tile.
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