package no.ntnu.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.ntnu.Board;
import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Player;
import no.ntnu.factory.BoardFactory;
import no.ntnu.io.PlayerCsvDao;
import no.ntnu.observer.ConsoleGameLogger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Launches the JavaFX board game application.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Load or default players
        Path playersFile = Path.of("players.csv");
        List<Player> players = Files.exists(playersFile)
            ? new PlayerCsvDao().loadPlayers(playersFile)
            : List.of(
                new Player("Arne", "Arne"),
                new Player("Ivar", "Ivar"),
                new Player("Majid", "Majid"),
                new Player("Atle", "Atle")
            );

        //Load or default board
        Path boardFile = Path.of("board.json");
        Board board = Files.exists(boardFile)
            ? BoardFactory.fromJson(boardFile)
            : BoardFactory.createDefaultBoard();

        //Create model and register players
        BoardGame model = new BoardGame(board, new Dice(2));
        for (Player p : players) {
            model.addPlayer(p);
        }

        //Create view + controller and show
        GameView view = new GameView(model);
        new GameController(model, view);

        Scene scene = new Scene(view);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Board Game");
        primaryStage.show();
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
