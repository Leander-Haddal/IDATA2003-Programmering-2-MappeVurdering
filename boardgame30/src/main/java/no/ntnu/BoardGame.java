package no.ntnu;

import java.util.ArrayList;
import java.util.List;

/**
 * Central class controlling the gameplay. 
 * Holds the Board, Dice, Players, and handles the main logic for playing rounds.
 */
public class BoardGame {
    private final Board board;
    private final Dice dice;
    private final List<Player> players = new ArrayList<>();

    private int roundNumber;
    private boolean finished;

    public BoardGame(Board board, Dice dice) {
        this.board = board;
        this.dice = dice;
        this.roundNumber = 0;
        this.finished = false;
    }

    public void addPlayer(Player player) {
        // Place each new Player on the first tile, or "before" the board if you prefer
        player.setCurrentTile(board.getFirstTile());
        players.add(player);
    }

    /**
     * Play a single round: each player rolls dice, moves, and triggers any tile action.
     */
    public void playRound() {
        roundNumber++;
        for (Player player : players) {
            if (finished) break;  // If game ended mid-round, skip

            int rollValue = dice.rollAll();
            System.out.println("\n[Round " + roundNumber + "] " 
                               + player.getName() + " rolls " + rollValue);

            movePlayer(player, rollValue);

            // Check if player reached last tile => game is finished
            if (player.getCurrentTile() == board.getLastTile()) {
                finished = true;
                System.out.println(">> " + player.getName() + " reached the final tile!");
                break;
            }
        }
    }

    /**
     * Moves a player 'steps' forward along the board.
     */
    private void movePlayer(Player player, int steps) {
        Tile current = player.getCurrentTile();
        for (int i = 0; i < steps && current.getNextTile() != null; i++) {
            current = current.getNextTile();
        }
        // Arrive at the new tile
        player.setCurrentTile(current);

        // If there's a TileAction (e.g. ladder), trigger it
        if (current.getAction() != null) {
            current.getAction().execute(player);
        }
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * Returns the first player who is on the last tile, if any.
     * If no one is on the last tile, returns null.
     */
    public Player getWinner() {
        Tile lastTile = board.getLastTile();
        for (Player player : players) {
            if (player.getCurrentTile() == lastTile) {
                return player;
            }
        }
        return null;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}
