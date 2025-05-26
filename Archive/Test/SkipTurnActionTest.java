import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Player;
import no.ntnu.Archive.BoardFactory;
import no.ntnu.action.SkipTurnAction;
import no.ntnu.Board;

/**
 * Test SkipTurnAction causes exactly one skipped turn.
 */
class SkipTurnActionTest {
    @Test
    void playerSkipsExactlyOneTurn() {
        Board board = BoardFactory.createDefaultBoard();
        Dice dice = new Dice(1) { @Override public int rollAll() { return 0; } }; 
        BoardGame game = new BoardGame(board, dice);

        Player p = new Player("SkipMe");
        game.addPlayer(p);

        board.getFirstTile().setAction(new SkipTurnAction());

        game.playTurn();
        assertTrue(p.isSkipNextTurn(), "skip flag should be set");

        int before = p.getCurrentTileId();
        game.playTurn();
        assertEquals(before, p.getCurrentTileId(), "should not move");
        assertFalse(p.isSkipNextTurn(), "skip flag should clear");

        game.playTurn();
        assertEquals(before, p.getCurrentTileId(),
            "roll is zero, so player stays but no skip");
    }
}
