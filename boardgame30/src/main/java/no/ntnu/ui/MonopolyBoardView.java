package no.ntnu.ui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import no.ntnu.Player;

import java.util.List;

/**
 * View of a Monopoly board with debug grid and colored tokens.
 */
public class MonopolyBoardView extends StackPane {
    private static final int NUM_SIDE_TILES = 9;
    private static final double CORNER_RATIO = 1.75;
    private static final double TILE_RATIO = 1.0;
    private static final Color[] TOKEN_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE, Color.BROWN
    };

    private final ImageView background;
    private final Canvas tokenLayer;

    /**
     * @param imagePath   resource path for board image
     * @param initialSize board width & height in px
     */
    public MonopolyBoardView(String imagePath, double initialSize) {
        Image boardImage = new Image(getClass().getResourceAsStream(imagePath));
        background = new ImageView(boardImage);
        background.setPreserveRatio(true);
        background.setFitWidth(initialSize);
        background.setFitHeight(initialSize);

        tokenLayer = new Canvas(initialSize, initialSize);

        background.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            double size = newVal.doubleValue();
            tokenLayer.setWidth(size);
            tokenLayer.setHeight(size);
        });
        background.fitHeightProperty().addListener((obs, oldVal, newVal) -> {
            double size = newVal.doubleValue();
            tokenLayer.setWidth(size);
            tokenLayer.setHeight(size);
        });

        getChildren().addAll(background, tokenLayer);
    }

    /**
     * @param players list of players to display on board
     */
    public void drawTokens(List<Player> players) {
        GraphicsContext gc = tokenLayer.getGraphicsContext2D();
        double boardSize = tokenLayer.getWidth();
        gc.clearRect(0, 0, boardSize, boardSize);

        drawDebugGrid(gc, boardSize);

        double totalUnits = NUM_SIDE_TILES * TILE_RATIO + 2 * CORNER_RATIO;
        double unitSize = boardSize / totalUnits;
        double cornerSize = unitSize * CORNER_RATIO;
        double tileSize = unitSize * TILE_RATIO;
        double tokenDiam = tileSize * 0.4;
        double tokenRad = tokenDiam / 2;

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Point2D center = computeTileCenter(
                    player.getCurrentTileId(),
                    boardSize, cornerSize, tileSize);
            double offset = (i - players.size() / 2.0) * (tokenDiam * 0.6);
            gc.setFill(TOKEN_COLORS[i % TOKEN_COLORS.length]);
            gc.fillOval(
                    center.getX() - tokenRad + offset,
                    center.getY() - tokenRad,
                    tokenDiam, tokenDiam);
        }
    }

    /**
     * @param gc        graphics context for drawing debug grid
     * @param boardSize board width & height in px
     */
    private void drawDebugGrid(GraphicsContext gc, double boardSize) {
        double totalUnits = NUM_SIDE_TILES * TILE_RATIO + 2 * CORNER_RATIO;
        double unitSize = boardSize / totalUnits;
        double cornerSize = unitSize * CORNER_RATIO;
        double tileSize = unitSize * TILE_RATIO;

        double[] xs = new double[NUM_SIDE_TILES + 3];
        xs[0] = 0;
        xs[1] = cornerSize;
        for (int j = 0; j < NUM_SIDE_TILES; j++) {
            xs[j + 2] = xs[j + 1] + tileSize;
        }
        xs[NUM_SIDE_TILES + 2] = boardSize;

        double[] ys = new double[NUM_SIDE_TILES + 3];
        ys[0] = 0;
        ys[1] = cornerSize;
        for (int j = 0; j < NUM_SIDE_TILES; j++) {
            ys[j + 2] = ys[j + 1] + tileSize;
        }
        ys[NUM_SIDE_TILES + 2] = boardSize;

        gc.setStroke(Color.web("#ff0f0008"));
        for (double x : xs) {
            gc.strokeLine(x, 0, x, boardSize);
        }
        for (double y : ys) {
            gc.strokeLine(0, y, boardSize, y);
        }

        gc.setFill(Color.web("#ff00000c"));
        for (int id = 1; id <= 40; id++) {
            Point2D center = computeTileCenter(id, boardSize, cornerSize, tileSize);
            gc.fillOval(center.getX() - 3, center.getY() - 3, 6, 6);
        }
    }

    /**
     * @param id         tile id 1–40
     * @param boardSize  board width & height in px
     * @param cornerSize corner square size in px
     * @param tileSize   side tile size in px
     * @return center point of tile
     * This method was created with the help of ChatGPT
     */
    private Point2D computeTileCenter(int id,
            double boardSize,
            double cornerSize,
            double tileSize) {
        double x = 0, y = 0;
        int edgeLen = NUM_SIDE_TILES + 2;

        if (id <= edgeLen) {
            if (id == 1)
                x = boardSize - cornerSize / 2;
            else if (id == edgeLen)
                x = cornerSize / 2;
            else
                x = boardSize - cornerSize - tileSize * (id - 2) - tileSize / 2;
            y = boardSize - cornerSize / 2;

        } else if (id <= 2 * edgeLen) {
            x = cornerSize / 2;
            if (id == edgeLen * 2)
                y = cornerSize / 2;
            else
                y = boardSize - cornerSize - tileSize * (id - edgeLen - 1) - tileSize / 2;

        } else if (id <= 3 * edgeLen) {
            y = cornerSize / 2;
            if (id == edgeLen * 3)
                x = boardSize - cornerSize / 2;
            else
                x = cornerSize + tileSize * (id - 2 * edgeLen - 1) + tileSize / 2;

        } else {
            x = boardSize - cornerSize / 2;
            y = cornerSize + tileSize * (id - 3 * edgeLen - 1) + tileSize / 2;
        }

        return new Point2D(x, y);
    }
}
