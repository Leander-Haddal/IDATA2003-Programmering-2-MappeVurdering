package no.ntnu.action;

import java.util.Random;
import no.ntnu.BoardGame;
import no.ntnu.Player;

/**
 * Draws a Community Chest card: random small gain or loss.
 */
public class ChestAction implements TileAction {
    private final Random rnd = new Random();

    @Override
    public void execute(Player player, BoardGame game) {
        int delta = rnd.nextBoolean() ? +100 : -50;
        player.adjustBalance(delta);
    }
}