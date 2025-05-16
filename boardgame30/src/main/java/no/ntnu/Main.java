package no.ntnu;

import no.ntnu.factory.BoardFactory;
import no.ntnu.io.PlayerCsvDao;
import no.ntnu.observer.ConsoleGameLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Console (and JavaFX) launcher that wires DAO, Factory, Observer,
 * and provides a fallback if players.csv is missing.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // 1) Load players from CSV if present, otherwise use defaults
        Path playersFile = Path.of("players.csv");
        List<Player> players;
        if (Files.exists(playersFile)) {
            players = new PlayerCsvDao().loadPlayers(playersFile);
        } else {
            System.out.println("players.csv not found; using default players");
            players = List.of(
                new Player("Arne", "Arne"),
                new Player("Ivar", "Ivar"),
                new Player("Majid", "Majid"),
                new Player("Atle", "Atle")
            );
        }

        // 2) Load board from JSON (or fallback)
        Path boardFile = Path.of("board.json");
        Board board;
        if (Files.exists(boardFile)) {
            board = BoardFactory.fromJson(boardFile);
        } else {
            System.out.println("board.json not found; using default board");
            board = BoardFactory.createDefaultBoard();
        }

        // 3) Create game engine
        Dice dice = new Dice(2);
        BoardGame game = new BoardGame(board, dice);

        // 4) Register console-logging observer
        game.addObserver(new ConsoleGameLogger());

        // 5) Add players to game
        players.forEach(game::addPlayer);

        // 6) Run until finished
        System.out.println("=== Starting Board Game ===");
        while (!game.isFinished()) {
            game.playRound();
        }
    }
}
