package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;

/**
 * Collects a fixed amount when passing or landing on Go.
 */
public class GoAction implements TileAction {
    private final int amount;

    public GoAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player player, BoardGame game) {
        player.adjustBalance(amount);
    }
}
