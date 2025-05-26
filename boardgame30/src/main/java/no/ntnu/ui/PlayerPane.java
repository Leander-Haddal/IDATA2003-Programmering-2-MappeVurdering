package no.ntnu.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import no.ntnu.Player;

import java.util.List;

/**
 * Displays player info and action buttons
 */
public class PlayerPane extends VBox {
    private final Label nameLabel = new Label();
    private final Label balanceLabel = new Label();
    private final ListView<String> propertyListView = new ListView<>();
    private final Button rollButton = new Button("Roll");
    private final Button buyButton = new Button("Buy");
    private final Button nextButton = new Button("Next");
    private final Label statusLabel = new Label();

    /**
     * Constructs a player pane with buttons and status
     */
    public PlayerPane() {
        getStyleClass().add("player-pane");
        setSpacing(5);
        propertyListView.setPrefHeight(100);
        getChildren().addAll(
            nameLabel,
            balanceLabel,
            propertyListView,
            rollButton,
            buyButton,
            nextButton,
            statusLabel
        );
        rollButton.setDisable(true);
        buyButton.setDisable(true);
        nextButton.setDisable(true);
    }

    /**
     * Updates displayed name balance and properties
     *
     * @param player     the player to display
     * @param properties list of property names
     */
    public void update(Player player, List<String> properties) {
        nameLabel.setText(player.getName());
        balanceLabel.setText("$" + player.getBalance());
        propertyListView.getItems().setAll(properties);
    }

    /**
     * Sets the status text
     *
     * @param text status message
     */
    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    /**
     * Returns the roll button
     *
     * @return roll button
     */
    public Button getRollButton() {
        return rollButton;
    }

    /**
     * Returns the buy button
     *
     * @return buy button
     */
    public Button getBuyButton() {
        return buyButton;
    }

    /**
     * Returns the next button
     *
     * @return next button
     */
    public Button getNextButton() {
        return nextButton;
    }
}