package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;
import no.ntnu.tile.UtilityTile;

/**
 * Action for utility tiles that calculates rent based on dice roll.
 */
public class UtilityAction implements TileAction {
    private final UtilityTile tile;

    /**
     * Creates a utility action for the specified tile.
     * 
     * @param tile the utility tile
     */
    public UtilityAction(UtilityTile tile) {
        this.tile = tile;
    }

    /**
     * Executes utility action: charges rent based on dice roll if owned.
     * 
     * @param player the player landing on the tile
     * @param game the game instance
     */
    @Override
    public void execute(Player player, BoardGame game) {
        if (tile.isOwned() && tile.getOwner() != player) {
            Player owner = tile.getOwner();
            int ownedCount = owner.getOwnedUtilities().size();
            int lastRoll = game.getLastRoll();
            tile.setRentContext(lastRoll, ownedCount);
            int rent = tile.getRent();
            
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