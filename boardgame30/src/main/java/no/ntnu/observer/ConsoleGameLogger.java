package no.ntnu.observer;

import no.ntnu.Player;
import no.ntnu.Tile;

/**
 * Simple observer that logs moves and game finish to the console
 */
public class ConsoleGameLogger implements GameObserver {

    @Override
    public void onPlayerMoved(Player player, Tile toTile) {
        System.out.println(player.getName() + " moved to tile " + toTile.getId());
    }

    /**
     * Log when the turn changes
     */
    @Override
    public void onTurnChanged(Player nextPlayer) {
        System.out.println("Next turn: " + nextPlayer.getName());
    }

    @Override
    public void onGameFinished(Player winner) {
        System.out.println("Game over! Winner: " + winner.getName());
    }
}
