import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.Board;
import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Player;
import no.ntnu.Tile;

/**
 * Tests for the BoardGame class.
 */
public class BoardGameTest {

    private Board board;
    private Dice dice;
    private BoardGame game;

    @BeforeEach
    void setUp() {
        board = new Board();
        Tile tile1 = new Tile(1);
        board.setFirstTile(tile1);  
        Tile tile2 = new Tile(2);
        Tile tile3 = new Tile(3);
        Tile tile4 = new Tile(4);
        Tile tile5 = new Tile(5);

        tile1.setNextTile(tile2);
        tile2.setNextTile(tile3);
        tile3.setNextTile(tile4);
        tile4.setNextTile(tile5);

        board.registerTile(tile2);
        board.registerTile(tile3);
        board.registerTile(tile4);
        board.registerTile(tile5);

        dice = new Dice(2);

        game = new BoardGame(board, dice);
        game.addPlayer(new Player("Alice"));
        game.addPlayer(new Player("Bob"));
    }

    @Test
    void testAddPlayerAndInitialPosition() {
        List<Player> players = game.getPlayers();
        Assertions.assertEquals(2, players.size(),
            "There should be 2 players initially.");

        for (Player p : players) {
            Assertions.assertEquals(1, p.getCurrentTileId(),
                "Players should start on tile #1 (the first tile).");
        }
    }
    
    @Test
    void testGameFinishesAndHasWinner() {
        int maxRounds = 10;
        int rounds = 0;
    
        while (!game.isFinished() && rounds < maxRounds) {
            game.playRound();
            rounds++;
        }
    
        Assertions.assertTrue(game.isFinished(),
            "Game should eventually finish.");
        
        Player winner = game.getWinner();
        Assertions.assertNotNull(winner, "There should be a winner.");
        Assertions.assertEquals(board.getLastTile(), winner.getCurrentTile(),
            "Winner should be on the last tile.");
    }
}
