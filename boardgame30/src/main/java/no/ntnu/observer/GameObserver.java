package no.ntnu.observer;

import no.ntnu.Player;
import no.ntnu.tile.Tile;

/**
 * Observer for Monopoly game events.
 */
public interface GameObserver {
    /**
     * Called when the current player rolls the dice.
     * @param player the player who rolled
     * @param die1 result of first die
     * @param die2 result of second die
     */
    void onDiceRoll(Player player, int die1, int die2);

    /**
     * Called after a player moves to a tile.
     * @param player the player who moved
     * @param from starting tile before move
     * @param to destination tile after move
     * @param steps total steps moved
     */
    void onPlayerMoved(Player player, Tile from, Tile to, int steps);

    /**
     * Called when a tile action is executed (e.g., tax, chance, jail).
     * @param player the player undergoing the action
     * @param actionName descriptive action name
     * @param tile the tile where action occurred
     */
    void onTileAction(Player player, String actionName, Tile tile);

    /**
     * Called when a property is available for purchase.
     * @param player the player who landed on unowned property
     * @param property the property tile available
     */
    void onPropertyAvailable(Player player, Tile property);

    /**
     * Called when rent is paid from one player to another.
     * @param payer the player who paid
     * @param owner the owner receiving rent
     * @param property the property on which rent was paid
     * @param amount the rent amount
     */
    void onRentPaid(Player payer, Player owner, Tile property, int amount);

    /**
     * Called when the current player's turn is skipped.
     * @param player the player whose turn is skipped
     */
    void onPlayerSkipped(Player player);

    /**
     * Called when the game ends with a winner.
     * @param winner the winning player
     */
    void onGameEnd(Player winner);
}