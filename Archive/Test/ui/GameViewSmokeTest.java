package ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Archive.BoardFactory;
import no.ntnu.ui.GameView;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple smoke test for the GameView
 */
class GameViewSmokeTest {

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException e) {
            
        }
    }

    @Test
    void canInstantiateView() {
        BoardGame model = new BoardGame(
            BoardFactory.createDefaultBoard(),
            new Dice(2)
        );
        GameView view = new GameView(model);
        new Scene(view);

        Button roll = view.getRollButton();
        assertNotNull(roll, "Roll button should be present");
        assertFalse(roll.isDisabled(), "Roll button should be enabled initially");
    }
}