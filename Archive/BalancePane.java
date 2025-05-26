package no.ntnu.Archive;

import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import no.ntnu.Player;
import no.ntnu.tile.Tile;
import no.ntnu.tile.PropertyTile;
import no.ntnu.Board;
import java.util.ArrayList;

/**
 * A simple panel showing a player's balance and owned properties.
 */
public class BalancePane extends VBox {
    private final Board board;
    private final Label balanceLabel = new Label();
    private final ListView<String> propsList = new ListView<>();

    /**
     * Create pane for the given board.
     */
    public BalancePane(Board board) {
        this.board = board;
        getChildren().addAll(
            new Label("Balance:"), balanceLabel,
            new Label("Properties:"), propsList
        );
        setSpacing(5);
        setPadding(new Insets(10));
    }

    /**
     * Refresh to display the given player's data.
     */
    public void update(Player player) {
        balanceLabel.setText("$" + player.getBalance());
        propsList.getItems().setAll(playerOwnedNames(player));
    }

    private ArrayList<String> playerOwnedNames(Player player) {
        ArrayList<String> names = new ArrayList<>();
        for (Tile t : board.getAllTiles()) {
            if (t instanceof PropertyTile pt && pt.getOwner() == player) {
                names.add("Prop " + pt.getId());
            }
        }
        return names;
    }
}
