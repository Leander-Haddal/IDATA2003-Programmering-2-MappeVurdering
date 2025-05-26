package observer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tile.Tile;
import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.PropertyColor;
import no.ntnu.observer.GameObserver;
import no.ntnu.Board;
import no.ntnu.BoardGame;
import no.ntnu.Dice;
import no.ntnu.Player;

/**
 * Tests that BoardGame properly notifies observers of game events.
 */
class BoardGameObserverTest {

    private static class TestObserver implements GameObserver {
        final List<String> events = new ArrayList<>();
        int diceRollCount = 0;
        int moveCount = 0;
        int actionCount = 0;
        int propertyAvailableCount = 0;
        int rentPaidCount = 0;
        int skipCount = 0;
        int gameEndCount = 0;
        Player lastWinner = null;

        @Override
        public void onDiceRoll(Player player, int die1, int die2) {
            events.add("DICE:" + player.getName() + ":" + die1 + "," + die2);
            diceRollCount++;
        }

        @Override
        public void onPlayerMoved(Player player, Tile from, Tile to, int steps) {
            events.add("MOVE:" + player.getName() + ":" + from.getId() + "->" + to.getId());
            moveCount++;
        }

        @Override
        public void onTileAction(Player player, String actionName, Tile tile) {
            events.add("ACTION:" + player.getName() + ":" + actionName + "@" + tile.getId());
            actionCount++;
        }

        @Override
        public void onPropertyAvailable(Player player, Tile property) {
            events.add("AVAILABLE:" + player.getName() + ":" + property.getId());
            propertyAvailableCount++;
        }

        @Override
        public void onRentPaid(Player payer, Player owner, Tile property, int amount) {
            events.add("RENT:" + payer.getName() + "->" + owner.getName() + ":" + amount);
            rentPaidCount++;
        }

        @Override
        public void onPlayerSkipped(Player player) {
            events.add("SKIP:" + player.getName());
            skipCount++;
        }

        @Override
        public void onGameEnd(Player winner) {
            events.add("END:" + winner.getName());
            gameEndCount++;
            lastWinner = winner;
        }
    }

    private Board board;
    private BoardGame game;
    private TestObserver observer;

    @BeforeEach
    void setUp() {
        board = new Board();
        Tile start = new Tile(1);
        board.setFirstTile(start);
        
        Dice dice = new Dice(2);
        game = new BoardGame(dice);
        game.setBoard(board);
        
        observer = new TestObserver();
        game.addObserver(observer);
    }

    @Test
    void testDiceRollNotification() {
        Player p1 = new Player("Alice");
        game.addPlayer(p1);
        
        game.playTurn();
        
        assertEquals(1, observer.diceRollCount);
        assertTrue(observer.events.stream().anyMatch(e -> e.startsWith("DICE:Alice:")));
    }

    @Test
    void testPlayerMovedNotification() {
        Tile t1 = board.getFirstTile();
        Tile t2 = new Tile(2);
        Tile t3 = new Tile(3);
        t1.setNextTile(t2);
        t2.setNextTile(t3);
        board.registerTile(t2);
        board.registerTile(t3);
        
        Player p1 = new Player("Bob");
        game.addPlayer(p1);
        
        game.playTurn();
        
        assertTrue(observer.moveCount >= 1);
        assertTrue(observer.events.stream().anyMatch(e -> e.contains("MOVE:Bob:")));
    }


    @Test
    void testRentPaidNotification() {
        PropertyTile prop = new PropertyTile(2, "Baltic Ave", PropertyColor.BROWN, 60, 4, 50);
        board.getFirstTile().setNextTile(prop);
        board.registerTile(prop);
        
        Player owner = new Player("Owner");
        Player renter = new Player("Renter");
        owner.adjustBalance(1000);
        renter.adjustBalance(1000);
        
        prop.setOwner(owner);
        owner.addOwned(prop);
        
        game.addPlayer(owner);
        game.addPlayer(renter);
        
        game.processRentPayment(renter, owner, prop, 50);
        
        assertEquals(1, observer.rentPaidCount);
        assertTrue(observer.events.contains("RENT:Renter->Owner:50"));
    }

    @Test
    void testPlayerSkippedNotification() {
        Player p1 = new Player("Skip");
        p1.setSkipNextTurn(true);
        game.addPlayer(p1);
        
        game.playTurn();
        
        assertEquals(1, observer.skipCount);
        assertTrue(observer.events.contains("SKIP:Skip"));
    }

    

    @Test
    void testMultipleObservers() {
        TestObserver observer2 = new TestObserver();
        game.addObserver(observer2);
        
        Player p1 = new Player("Multi");
        game.addPlayer(p1);
        
        game.playTurn();
        
        assertEquals(observer.diceRollCount, observer2.diceRollCount);
        assertEquals(observer.moveCount, observer2.moveCount);
    }
}