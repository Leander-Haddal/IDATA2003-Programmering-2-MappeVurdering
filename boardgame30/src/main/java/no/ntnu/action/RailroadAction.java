package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;
import no.ntnu.tile.RailroadTile;

/**
 * Action for railroad tiles that calculates rent based on number owned.
 */
public class RailroadAction implements TileAction {
    private final RailroadTile tile;

    /**
     * Creates a railroad action for the specified tile.
     * 
     * @param tile the railroad tile
     */
    public RailroadAction(RailroadTile tile) {
        this.tile = tile;
    }

    /**
     * Executes railroad action: charges rent if owned by another player.
     * 
     * @param player the player landing on the tile
     * @param game the game instance
     */
    @Override
    public void execute(Player player, BoardGame game) {
        if (tile.isOwned() && tile.getOwner() != player) {
            Player owner = tile.getOwner();
            int ownedCount = owner.getOwnedRailroads().size();
            int rent = tile.calculateRent(ownedCount);
            
            if (player.getBalance() >= rent) {
                player.adjustBalance(-rent);
                owner.adjustBalance(rent);
                game.notifyRentPaid(player, owner, tile, rent);
            } else {
                player.adjustBalance(-player.getBalance());
                owner.adjustBalance(player.getBalance());
                game.notifyRentPaid(player, owner, tile, player.getBalance());
            }
        } else if (!tile.isOwned()) {
            game.notifyPropertyAvailable(player, tile);
        }
    }
}