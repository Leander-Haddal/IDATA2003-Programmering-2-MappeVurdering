package no.ntnu.observer;

import no.ntnu.Player;
import no.ntnu.tile.Tile;

/**
 * Logs all game events to the console
 */
public class ConsoleGameLogger implements GameObserver {
    /**
     * Log dice roll results
     * @param player the player who rolled
     * @param die1 result of first die
     * @param die2 result of second die
     */
    @Override
    public void onDiceRoll(Player player, int die1, int die2) {
        int total = die1 + die2;
        System.out.println(player.getName() + " rolled " + die1 + " and " + die2 + " for " + total);
    }

    /**
     * Log player movement
     * @param player the player who moved
     * @param from starting tile
     * @param to destination tile
     * @param steps number of steps moved
     */
    @Override
    public void onPlayerMoved(Player player, Tile from, Tile to, int steps) {
        System.out.println(player.getName() + " moves from " + from.getId() + " to " + to.getId() + " in " + steps + " steps");
    }

    /**
     * Log a tile action execution
     * @param player the player undergoing action
     * @param actionName name of action executed
     * @param tile the tile where action occurred
     */
    @Override
    public void onTileAction(Player player, String actionName, Tile tile) {
        System.out.println(player.getName() + " executes " + actionName + " on tile " + tile.getId());
    }

    /**
     * Log property available event
     * @param player the player who can buy
     * @param property the property tile available
     */
    @Override
    public void onPropertyAvailable(Player player, Tile property) {
        System.out.println(player.getName() + " may buy " + property.getName() + " for " + property.getPrice());
    }

    /**
     * Log rent payment
     * @param payer the player who paid
     * @param owner the owner who received payment
     * @param property the property on which rent was paid
     * @param amount the rent amount
     */
    @Override
    public void onRentPaid(Player payer, Player owner, Tile property, int amount) {
        System.out.println(payer.getName() + " pays " + amount + " to " + owner.getName() + " for " + property.getName());
    }

    /**
     * Log a skipped turn<
     * @param player the player who skips
     */
    @Override
    public void onPlayerSkipped(Player player) {
        System.out.println(player.getName() + " skips turn");
    }

    /**
     * Log game end and winner
     * @param winner the winning player
     */
    @Override
    public void onGameEnd(Player winner) {
        System.out.println("Game over winner " + winner.getName());
    }
}
