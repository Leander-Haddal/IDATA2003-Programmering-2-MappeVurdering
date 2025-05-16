package no.ntnu.ui;

import javafx.event.ActionEvent;
import no.ntnu.BoardGame;

/**
 * Handles UI events and invokes model actions
 */
public class GameController {
    private final BoardGame model;
    private final GameView view;

    /**
     * Link the given model and view, register observer, and wire controls
     */
    public GameController(BoardGame model, GameView view) {
        this.model = model;
        this.view = view;
        this.model.addObserver(view);
        this.view.getRollButton().setOnAction(this::handleRoll);
    }

    /**
     * Handle the Roll Dice button press
     */
    private void handleRoll(ActionEvent event) {
        view.getRollButton().setDisable(true);
        model.playTurn();
    }
}
