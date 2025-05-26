package no.ntnu.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import no.ntnu.Player;
import no.ntnu.tile.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout root pane with board view and dynamic player panes
 */
public class GameView extends BorderPane {
    private final VBox leftPlayerColumn = new VBox(10);
    private final VBox rightPlayerColumn = new VBox(10);
    private final MonopolyBoardView boardView;
    private final List<PlayerPane> playerPanes = new ArrayList<>();
    private Tile offeredProperty;

    /**
     * Constructs the game view for given number of players and applies stylesheet
     * @param playerCount number of player panes to display (2-4)
     */
    public GameView(int playerCount) {
        getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        boardView = new MonopolyBoardView("/images/Monopol.jpg", 600);
        setCenter(boardView);
        setLeft(leftPlayerColumn);
        setRight(rightPlayerColumn);

        for (int i = 0; i < playerCount; i++) {
            PlayerPane pane = new PlayerPane();
            playerPanes.add(pane);
            if (i % 2 == 0) {
                leftPlayerColumn.getChildren().add(pane);
            } else {
                rightPlayerColumn.getChildren().add(pane);
            }
        }
    }

    /** 
     * @return the board view for drawing tokens 
     */
    public MonopolyBoardView getBoardView() {
        return boardView;
    }

    /** 
     * @param index zero-based index of player pane 
     * @return corresponding PlayerPane 
     */
    public PlayerPane getPlayerPane(int index) {
        return playerPanes.get(index);
    }

    /** 
     * @param players list of players to display tokens for 
     */
    public void drawTokens(List<Player> players) {
        boardView.drawTokens(players);
    }

    /**
     * Highlight the active player pane by index
     * @param index zero-based active pane index
     */
    public void setActivePlayerPane(int index) {
        for (PlayerPane pane : playerPanes) {
            pane.getStyleClass().remove("active");
        }
        playerPanes.get(index).getStyleClass().add("active");
    }

    /**
     * Store the currently offered property for purchase
     */
    public void setOfferedProperty(Tile property) {
        this.offeredProperty = property;
    }

    /**
     * @return the tile currently offered for purchase
     */
    public Tile getOfferedProperty() {
        return offeredProperty;
    }

    /**
     * Clear any outstanding purchase offer
     */
    public void clearPropertyOffer() {
        this.offeredProperty = null;
    }

    /**
     * Show a dialog announcing the winner
     * @param winnerName name of winning player
     */
    public void showWinnerDialog(String winnerName) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("The winner is " + winnerName + "!");
        alert.showAndWait();
    }

    /**
     * return number of player panes
     */
    public int getPlayerCount() {
        return playerPanes.size();
    }
}
