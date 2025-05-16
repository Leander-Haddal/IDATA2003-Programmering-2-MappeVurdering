package io;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import no.ntnu.Player;
import no.ntnu.io.PlayerCsvDao;

class PlayerCsvDaoTest {
    @TempDir Path tempDir;

    @Test
    void loadAndSavePlayers(@TempDir Path dir) throws Exception {
        Path csv = dir.resolve("players.csv");
        List<String> lines = List.of("Alice,TokenA", "Bob,TokenB");
        Files.write(csv, lines);

        PlayerCsvDao dao = new PlayerCsvDao();
        List<Player> loaded = dao.loadPlayers(csv);
        assertEquals(2, loaded.size());
        assertEquals("Alice", loaded.get(0).getName());
        assertEquals("Bob", loaded.get(1).getName());

        // test savePlayers round-trip
        Path out = dir.resolve("out.csv");
        dao.savePlayers(loaded, out);
        List<String> outLines = Files.readAllLines(out);
        assertEquals(lines, outLines);
    }

    @Test
    void loadPlayersBadLine() throws Exception {
        Path csv = tempDir.resolve("bad.csv");
        Files.write(csv, List.of("BadLineWithoutComma"));
        PlayerCsvDao dao = new PlayerCsvDao();
        assertThrows(IllegalArgumentException.class, () -> dao.loadPlayers(csv));
    }
}
