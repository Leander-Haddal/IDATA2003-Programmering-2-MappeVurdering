package no.ntnu.io;

import no.ntnu.Player;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Load and save Players to a CSV file
 */
public class PlayerCsvDao {

    /**
     * Read all players from a CSV file
     */
    public List<Player> loadPlayers(Path csvPath) throws IOException {
        List<Player> players = new ArrayList<>();
        for (String line : Files.readAllLines(csvPath)) {
            String[] parts = line.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid CSV line: " + line);
            }
            String name = parts[0].trim();
            String token = parts[1].trim();
            players.add(new Player(name, token));
        }
        return players;
    }

    /**
     * Write the given players into a CSV file
     */
    public void savePlayers(List<Player> players, Path csvPath) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Player p : players) {
            lines.add(p.getName() + "," + p.getToken());
        }
        Files.write(csvPath, lines);
    }
}
