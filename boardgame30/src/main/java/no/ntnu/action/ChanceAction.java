package no.ntnu.action;

import java.util.Random;
import no.ntnu.BoardGame;
import no.ntnu.Player;

/**
 * Draws a Chance card: random small gain or loss.
 */
public class ChanceAction implements TileAction {
    private final Random rnd = new Random();

    @Override
    public void execute(Player player, BoardGame game) {
        int delta = rnd.nextBoolean() ? +150 : -75;
        player.adjustBalance(delta);
    }
}