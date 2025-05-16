package no.ntnu.ui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import no.ntnu.Board;
import no.ntnu.Tile;

/**
 * Draws the board grid
 */
public class BoardView {
    private final Canvas canvas;
    private final Board board;
    private final int cols = 10;
    private final double tileSize;

    /**
     * Create view for given canvas and board
     */
    public BoardView(Canvas canvas, Board board) {
        this.canvas = canvas;
        this.board = board;
        this.tileSize = canvas.getWidth() / cols;
    }

    /**
     * Return the canvas this view draws on
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Return size (in pixels) of one tile
     */
    public double getTileSize() {
        return tileSize;
    }

    /**
     * Draw the empty board with numbered tiles
     */
    public void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Tile t = board.getFirstTile();
        while (t != null) {
            Point2D p = getTilePosition(t.getId());
            gc.setStroke(Color.BLACK);
            gc.strokeRect(p.getX(), p.getY(), tileSize, tileSize);
            gc.fillText(String.valueOf(t.getId()), p.getX() + 5, p.getY() + 15);
            t = t.getNextTile();
        }
    }

    /**
     * Convert a tile ID to pixel coordinates
     */
    public Point2D getTilePosition(int id) {
        int idx = id - 1;
        int row = idx / cols;
        int col = idx % cols;
        double x = col * tileSize;
        double y = canvas.getHeight() - (row + 1) * tileSize;
        return new Point2D(x, y);
    }
}
