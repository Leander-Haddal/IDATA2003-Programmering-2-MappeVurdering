package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;

/**
 * Pays a fixed tax amount when landing on a Tax tile.
 */
public class TaxAction implements TileAction {
    private final int amount;

    public TaxAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player player, BoardGame game) {
        player.adjustBalance(-amount);
    }
}