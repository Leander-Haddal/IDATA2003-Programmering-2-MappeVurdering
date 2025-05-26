package no.ntnu.action;

import no.ntnu.BoardGame;
import no.ntnu.Player;

/**
 * Causes the player to skip their next turn.
 */
public class SkipTurnAction implements TileAction {
    @Override
    public void execute(Player player, BoardGame game) {
        player.setSkipNextTurn(true);
    }
}