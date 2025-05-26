import static org.junit.jupiter.api.Assertions.*;
import no.ntnu.observer.GameObserver;
import no.ntnu.tile.Tile;

import org.junit.jupiter.api.Test;

import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Player;
import no.ntnu.Archive.BoardFactory;


class TurnBasedBoardGameTest {

    private static class Spy implements GameObserver {
        int movedCount = 0;
        int turnCount = 0;
        int skipCount = 0; 
        int finishCount = 0;

        @Override
        public void onPlayerMoved(Player player, Tile toTile) {
            movedCount++;
        }

        @Override
        public void onTurnChanged(Player nextPlayer) {
            turnCount++;
        }

        @Override
        public void onSkipTurn(Player skipped) {
            skipCount++;
        }

        @Override
        public void onGameFinished(Player winner) {
            finishCount++;
        }
    }

    @Test
    void playTurnCyclesPlayersAndNotifies() {
        BoardGame game = new BoardGame(
            BoardFactory.createDefaultBoard(),
            new Dice(1) { @Override public int rollAll() { return 1; } }
        );
        Player a = new Player("A");
        Player b = new Player("B");
        game.addPlayer(a);
        game.addPlayer(b);

        Spy spy = new Spy();
        game.addObserver(spy);

        // First turn: A moves, then turn change to B
        game.playTurn();
        assertEquals(1, spy.movedCount);
        assertEquals(1, spy.turnCount);

        // Second turn: B moves, then turn change to A
        game.playTurn();
        assertEquals(2, spy.movedCount);
        assertEquals(2, spy.turnCount);

        // Continue until someone finishes
        while (!game.isFinished()) {
            game.playTurn();
        }
        assertTrue(spy.finishCount >= 1);
    }
}
