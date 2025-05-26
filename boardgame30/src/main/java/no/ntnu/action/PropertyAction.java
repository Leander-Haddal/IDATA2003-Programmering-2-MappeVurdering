package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;
import no.ntnu.tile.PropertyTile;

/**
 * Handles landing on a property: offer purchase or pay rent.
 */
public class PropertyAction implements TileAction {
    private final PropertyTile property;

    public PropertyAction(PropertyTile property) {
        this.property = property;
    }

    @Override
    public void execute(Player player, BoardGame game) {
        if (!property.isOwned()) {
            game.notifyPropertyAvailable(player, property);
        } else if (property.getOwner() != player) {
            int rent = property.getRent();
            player.adjustBalance(-rent);
            Player owner = property.getOwner();
            owner.adjustBalance(rent);
            game.notifyRentPaid(player, owner, property, rent);
        }
    }
    
}