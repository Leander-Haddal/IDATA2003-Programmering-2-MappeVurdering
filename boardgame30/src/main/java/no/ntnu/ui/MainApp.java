package no.ntnu.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Player;
import no.ntnu.factory.MonopolyBoardFactory;
import no.ntnu.exception.InvalidDataException;

/**
 * New Main for launching the ui version of the game.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws InvalidDataException {
        PlayerSetupView setupView = new PlayerSetupView();
        Scene setupScene = new Scene(setupView);
        primaryStage.setTitle("Monopol setup");
        primaryStage.setScene(setupScene);
        primaryStage.show();

        setupView.getStartButton().setOnAction(e -> {
        var names = setupView.getPlayerNames();
        if (names.size() < 2 || names.size() > 4) return;
        
        Dice dice = new Dice(2);
        BoardGame gameModel = new BoardGame(dice);
        try {
            var board = MonopolyBoardFactory.createBoard(gameModel);
            gameModel.setBoard(board);
        } catch (InvalidDataException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not build the board");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return;
        }
        
        for (var name : names) {
            gameModel.addPlayer(new Player(name));
        }
        GameView gameView = new GameView(names.size());
        GameController controller = new GameController(gameModel, gameView);
        Scene gameScene = new Scene(gameView);
        primaryStage.setTitle("Monopol game");
        primaryStage.setScene(gameScene);
        primaryStage.sizeToScene();
    });
    }

    public static void main(String[] args) {
        launch(args);
    }
}