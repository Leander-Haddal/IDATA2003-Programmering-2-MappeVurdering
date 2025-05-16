package observer;

import static org.junit.jupiter.api.Assertions.*;
import no.ntnu.observer.GameObserver;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.Board;
import no.ntnu.factory.BoardFactory;
import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Player;
import no.ntnu.Tile;

/**
 * Tests that BoardGame notifies observers of moves and finish.
 */
class BoardGameObserverTest {

    private static class SpyObserver implements GameObserver {
        final List<String> events = new ArrayList<>();

        @Override
        public void onPlayerMoved(Player player, Tile toTile) {
            events.add(player.getName() + "->" + toTile.getId());
        }

        @Override
        public void onTurnChanged(Player nextPlayer) {
            
        }

        @Override
        public void onGameFinished(Player winner) {
            events.add("WIN:" + winner.getName());
        }
    }

    @Test
    void observerReceivesMovesAndFinish() {
        Board board = BoardFactory.createDefaultBoard();
        Dice dice = new Dice(1) {
            @Override public int rollAll() { return 9; }
        };
        BoardGame game = new BoardGame(board, dice);
        SpyObserver spy = new SpyObserver();
        game.addObserver(spy);
        Player p = new Player("X");
        game.addPlayer(p);
        game.playRound();

        assertTrue(spy.events.stream().anyMatch(e -> e.startsWith("X->")));
        assertTrue(spy.events.contains("WIN:X"));
    }
}
