package no.ntnu.ui;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import no.ntnu.BoardGame;
import no.ntnu.Player;
import no.ntnu.Tile;
import no.ntnu.observer.GameObserver;
import java.util.List;

/**
 * High-level UI combining board, tokens, and controls
 */
public class GameView extends BorderPane implements GameObserver {
    private final BoardGame model;
    private final BoardView boardView;
    private final Button rollButton = new Button("Roll Dice");
    private final Label statusLabel = new Label();

    /**
     * Create view bound to the given game model
     */
    public GameView(BoardGame model) {
        this.model = model;
        Canvas canvas = new Canvas(600, 600);
        this.boardView = new BoardView(canvas, model.getBoard());
        setTop(statusLabel);
        setCenter(boardView.getCanvas());
        setBottom(rollButton);
        boardView.drawBoard();

        // initial turn display
        Player first = model.getPlayers().isEmpty() ? null : model.getPlayers().get(0);
        if (first != null) {
            statusLabel.setText(first.getName() + "’s turn – roll the dice");
        }

        // disable until playTurn triggers onTurnChanged
        rollButton.setDisable(false);
    }

    /**
     * Return the roll-dice button for controller
     */
    public Button getRollButton() {
        return rollButton;
    }

    /**
     * Called when a player has moved
     */
    @Override
    public void onPlayerMoved(Player player, Tile toTile) {
        Platform.runLater(() -> {
            boardView.drawBoard();
            drawTokens();

            int roll = model.getLastRoll();
            List<Player> players = model.getPlayers();
            int idx = players.indexOf(player);
            int nextIdx = (idx + 1) % players.size();
            String nextName = players.get(nextIdx).getName();

            statusLabel.setText(
                player.getName() + " rolled " + roll + ". Next: " + nextName + "’s turn"
            );
            rollButton.setDisable(true);
        });
    }

    /**
     * Called when it becomes the next players turn
     */
    @Override
    public void onTurnChanged(Player next) {
        Platform.runLater(() -> {
            statusLabel.setText(next.getName() + "’s turn – roll the dice");
            rollButton.setDisable(false);
        });
    }

    /**
     * Called when the game finishes
     */
    @Override
    public void onGameFinished(Player winner) {
        Platform.runLater(() -> {
            statusLabel.setText("Winner: " + winner.getName());
            rollButton.setDisable(true);
        });
    }

    /**
     * Draw all player tokens at their current tiles
     */
    private void drawTokens() {
        GraphicsContext gc = boardView.getCanvas().getGraphicsContext2D();
        double size = boardView.getTileSize();
        for (Player p : model.getPlayers()) {
            Point2D pos = boardView.getTilePosition(p.getCurrentTileId());
            gc.setFill(Color.RED);
            gc.fillOval(
                pos.getX() + size * 0.25,
                pos.getY() + size * 0.25,
                size * 0.5,
                size * 0.5
            );
        }
    }
}
