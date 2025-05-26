package no.ntnu;

import java.util.*;
import no.ntnu.observer.GameObserver;
import no.ntnu.tile.Tile;
import no.ntnu.tile.UtilityTile;
import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.RailroadTile;
import no.ntnu.action.TileAction;

/**
 * Central class controlling Monopoly gameplay.
 */
public class BoardGame {
    private final Dice dice;
    private Board board;
    private final List<Player> players = new ArrayList<>();
    private final List<GameObserver> observers = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private boolean finished = false;
    private Player winner = null;
    private int lastRoll;

    public BoardGame(Dice dice) {
        this.dice = dice;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int getLastRoll() {
        return lastRoll;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void addPlayer(Player player) {
        player.setCurrentTile(board.getFirstTile());
        players.add(player);
    }

    /**
     * Ends current turn and advances to next, handling skips.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player next = players.get(currentPlayerIndex);
        if (next.isSkipNextTurn()) {
            next.setSkipNextTurn(false);
            notifyPlayerSkipped(next);
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /**
     * Attempt to purchase a tile for current player.
     */
    public void buyProperty(Player player, Tile tile) {
        if (player != getCurrentPlayer()) {
            return;
        }
        int price = tile.getPrice();
        if (player.getBalance() < price) {
            return;
        }
        player.adjustBalance(-price);
        if (tile instanceof PropertyTile pt) {
            pt.setOwner(player);
        } else if (tile instanceof RailroadTile rt) {
            rt.setOwner(player);
        } else if (tile instanceof UtilityTile ut) {
            ut.setOwner(player);
        } else {
            return;
        }
        player.addOwned(tile);
        notifyTileAction(player, "PurchaseAction", tile);
    }

    public void playTurn() {
        if (finished) return;
        Player player = getCurrentPlayer();
        
        if (player.isSkipNextTurn()) {
            player.setSkipNextTurn(false);
            notifyPlayerSkipped(player);
            nextTurn();
            return;
        }
        
        int die1 = dice.getDice().get(0).roll();
        int die2 = dice.getDice().get(1).roll();
        lastRoll = die1 + die2;
        notifyDiceRoll(player, die1, die2);
        
        if (player.isInJail()) {
            if (!handleJailTurn(player, die1, die2)) {
                return;
            }
        }
        
        Tile from = player.getCurrentTile();
        Tile to = movePlayer(player, lastRoll);
        notifyPlayerMoved(player, from, to, lastRoll);
        processActions(player);
        
        if (die1 == die2 && !player.isInJail()) {
        }
    }

    private void processActions(Player player) {
        Tile current = player.getCurrentTile();
        while (current.getAction() != null) {
            TileAction action = current.getAction();
            String name = action.getClass().getSimpleName();
            action.execute(player, this);
            notifyTileAction(player, name, current);
            Tile newTile = player.getCurrentTile();
            if (newTile == current) break;
            notifyPlayerMoved(player, current, newTile, 0);
            current = newTile;
        }
    }

    /**
     * Checks if a player is bankrupt and handles elimination.
     * 
     * @param player the player to check
     * @return true if player is bankrupt
     */
    public boolean checkBankruptcy(Player player) {
        if (player.getBalance() < 0) {
            handleBankruptcy(player);
            return true;
        }
        return false;
    }

    /**
     * Handles player bankruptcy by transferring assets and removing from game.
     * 
     * @param bankruptPlayer the player who is bankrupt
     */
    private void handleBankruptcy(Player bankruptPlayer) {
        // Transfer all properties to the bank (make them available again)
        for (PropertyTile property : bankruptPlayer.getOwnedProperties()) {
            property.setOwner(null);
        }
        for (RailroadTile railroad : bankruptPlayer.getOwnedRailroads()) {
            railroad.setOwner(null);
        }
        for (UtilityTile utility : bankruptPlayer.getOwnedUtilities()) {
            utility.setOwner(null);
        }
        
        players.remove(bankruptPlayer);
        
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        
        checkGameEnd();
    }

    /**
     * Checks if the game should end (only one player remaining).
     */
    private void checkGameEnd() {
        if (players.size() == 1) {
            finished = true;
            winner = players.get(0);
            notifyGameEnd(winner);
        }
    }

    /**
     * Processes a rent payment with bankruptcy check.
     * 
     * @param payer the player paying rent
     * @param owner the property owner
     * @param property the property being rented
     * @param rentAmount the rent amount
     */
    public void processRentPayment(Player payer, Player owner, Tile property, int rentAmount) {
        int actualPayment = Math.min(rentAmount, payer.getBalance());
        payer.adjustBalance(-actualPayment);
        owner.adjustBalance(actualPayment);
        notifyRentPaid(payer, owner, property, actualPayment);
        
        if (payer.getBalance() <= 0) {
            checkBankruptcy(payer);
        }
    }

   /**
     * Attempts to release a player from jail by paying fine.
     * 
     * @param player the player attempting to pay
     * @return true if successful
     */
    public boolean payJailFine(Player player) {
        if (!player.isInJail()) {
            return false;
        }
        
        if (player.getBalance() >= 50) {
            player.adjustBalance(-50);
            player.releaseFromJail();
            return true;
        }
        return false;
    }

    /**
     * Handles a player's turn when in jail.
     * 
     * @param player the jailed player
     * @param die1 first die result
     * @param die2 second die result
     * @return true if player rolled doubles and is released
     */
    private boolean handleJailTurn(Player player, int die1, int die2) {
        player.incrementJailTurns();
        
        if (die1 == die2) {
            player.releaseFromJail();
            return true;
        }
        
        if (player.getJailTurns() >= 3) {
            if (player.getBalance() >= 50) {
                player.adjustBalance(-50);
            }
            player.releaseFromJail();
            return true;
        }
        
        return false;
    }

    private Tile movePlayer(Player player, int steps) {
        Tile current = player.getCurrentTile();
        for (int i = 0; i < steps; i++) {
            Tile next = current.getNextTile();
            if (next == null) break;
            current = next;
            if (current.getId() == board.getFirstTile().getId()) {
                player.adjustBalance(200);
                notifyTileAction(player, "GoAction", current);
            }
        }
        player.setCurrentTile(current);
        return current;
    }

    // Notifications
    private void notifyDiceRoll(Player p, int d1, int d2) {
        observers.forEach(o -> o.onDiceRoll(p, d1, d2));
    }
    private void notifyPlayerMoved(Player p, Tile f, Tile t, int s) {
        observers.forEach(o -> o.onPlayerMoved(p, f, t, s));
    }
    private void notifyTileAction(Player p, String n, Tile t) {
        observers.forEach(o -> o.onTileAction(p, n, t));
    }
    public void notifyPropertyAvailable(Player p, Tile t) {
        observers.forEach(o -> o.onPropertyAvailable(p, t));
    }
    public void notifyRentPaid(Player payer, Player owner, Tile t, int amt) {
        observers.forEach(o -> o.onRentPaid(payer, owner, t, amt));
    }
    private void notifyPlayerSkipped(Player p) {
        observers.forEach(o -> o.onPlayerSkipped(p));
    }
    private void notifyGameEnd(Player w) {
        observers.forEach(o -> o.onGameEnd(w));
    }
}
