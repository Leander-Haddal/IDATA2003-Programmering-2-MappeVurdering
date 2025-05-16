package no.ntnu;

import no.ntnu.observer.GameObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Central class controlling the gameplay.
 * Supports observers, exposing board, players, and turn logic.
 */
public class BoardGame {
    private final Board board;
    private final Dice dice;
    private final List<Player> players = new ArrayList<>();
    private final List<GameObserver> observers = new ArrayList<>();

    private int roundNumber;
    private boolean finished;
    private int lastRoll;
    private int currentPlayerIndex = 0;

    /**
     * Create a game on the given board with the given dice.
     */
    public BoardGame(Board board, Dice dice) {
        this.board = board;
        this.dice = dice;
        this.roundNumber = 0;
        this.finished = false;
    }

    /**
     * Return the game’s board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Return the most recent dice roll.
     */
    public int getLastRoll() {
        return lastRoll;
    }

    /**
     * Return the player whose turn it currently is.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Return the list of players in playing order.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Return how many rounds have been played.
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Return true if someone has reached the last tile.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Add a player to the game (starts on first tile).
     */
    public void addPlayer(Player player) {
        player.setCurrentTile(board.getFirstTile());
        players.add(player);
    }

    /**
     * Register an observer to receive game events.
     */
    public void addObserver(GameObserver obs) {
        observers.add(obs);
    }

    /**
     * Unregister a previously registered observer.
     */
    public void removeObserver(GameObserver obs) {
        observers.remove(obs);
    }

    /**
     * Execute one turn: roll for the current player, move them, notify,
     * then advance to the next player.
     */
    public void playTurn() {
        if (finished) return;

        Player player = getCurrentPlayer();
        int roll = dice.rollAll();
        this.lastRoll = roll;
        movePlayer(player, roll);

        // Check for win
        if (player.getCurrentTile() == board.getLastTile()) {
            finished = true;
            notifyGameFinished(player);
            return;
        }

        // Advance to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyTurnChanged(getCurrentPlayer());
    }

    // NEW: Let observers know whose turn is next
    private void notifyTurnChanged(Player next) {
        for (GameObserver obs : observers) {
            obs.onTurnChanged(next);
        }
    }

    /**
     * Play one full round: each player rolls, moves, and triggers actions.
     */
    public void playRound() {
        roundNumber++;
        for (Player player : players) {
            if (finished) break;

            int roll = dice.rollAll();
            this.lastRoll = roll;
            movePlayer(player, roll);

            if (player.getCurrentTile() == board.getLastTile()) {
                finished = true;
                notifyGameFinished(player);
                break;
            }
        }
    }

    /**
     * Return the first player on the last tile, or null if none.
     */
    public Player getWinner() {
        Tile last = board.getLastTile();
        for (Player p : players) {
            if (p.getCurrentTile() == last) {
                return p;
            }
        }
        return null;
    }

    /**
     * Move the player along the board and notify observers.
     */
    private void movePlayer(Player player, int steps) {
        Tile current = player.getCurrentTile();
        for (int i = 0; i < steps && current.getNextTile() != null; i++) {
            current = current.getNextTile();
        }
        player.setCurrentTile(current);

        if (current.getAction() != null) {
            current.getAction().execute(player);
        }

        notifyPlayerMoved(player, current);
    }

    /**
     * Notify observers that a player has moved.
     */
    private void notifyPlayerMoved(Player player, Tile toTile) {
        for (GameObserver obs : observers) {
            obs.onPlayerMoved(player, toTile);
        }
    }

    /**
     * Notify observers that the game has finished.
     */
    private void notifyGameFinished(Player winner) {
        for (GameObserver obs : observers) {
            obs.onGameFinished(winner);
        }
    }
}
