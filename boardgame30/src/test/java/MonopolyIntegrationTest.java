import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.action.*;
import no.ntnu.tile.*;
import no.ntnu.observer.GameObserver;
import no.ntnu.BoardGame;
import no.ntnu.Board;
import no.ntnu.Dice;
import no.ntnu.Player;

/**
 * Integration tests for complete Monopoly game scenarios.
 */
public class MonopolyIntegrationTest {
    
    private Board board;
    private BoardGame game;
    private Player player1;
    private Player player2;
    private Player player3;
    
    @BeforeEach
    void setUp() {
        board = createSimpleBoard();
        
        Dice dice = new Dice(2);
        game = new BoardGame(dice);
        game.setBoard(board);
        
        player1 = new Player("Alice", "car");
        player2 = new Player("Bob", "hat");
        player3 = new Player("Charlie", "boot");
    }
    
    /**
     * Creates a simplified board for testing with key tiles.
     * 
     * @return a test board
     */
    private Board createSimpleBoard() {
        Board b = new Board();
        
        Tile go = new NamedTile(1, "GO");
        b.setFirstTile(go);
        go.setAction(new GoAction(200));
        
        PropertyTile brown1 = new PropertyTile(2, "Mediterranean", PropertyColor.BROWN, 60, 2, 50);
        brown1.setAction(new PropertyAction(brown1));
        go.setNextTile(brown1);
        b.registerTile(brown1);
        
        Tile chest = new NamedTile(3, "Community Chest");
        chest.setAction(new ChestAction());
        brown1.setNextTile(chest);
        b.registerTile(chest);
        
        PropertyTile brown2 = new PropertyTile(4, "Baltic", PropertyColor.BROWN, 60, 4, 50);
        brown2.setAction(new PropertyAction(brown2));
        chest.setNextTile(brown2);
        b.registerTile(brown2);
        
        Tile tax = new NamedTile(5, "Income Tax");
        tax.setAction(new TaxAction(200));
        brown2.setNextTile(tax);
        b.registerTile(tax);
        
        RailroadTile railroad = new RailroadTile(6, "Reading Railroad", 200);
        railroad.setAction(new RailroadAction(railroad));
        tax.setNextTile(railroad);
        b.registerTile(railroad);
        
        PropertyTile lightBlue1 = new PropertyTile(7, "Oriental", PropertyColor.LIGHT_BLUE, 100, 6, 50);
        lightBlue1.setAction(new PropertyAction(lightBlue1));
        railroad.setNextTile(lightBlue1);
        b.registerTile(lightBlue1);
        
        Tile chance = new NamedTile(8, "Chance");
        chance.setAction(new ChanceAction());
        lightBlue1.setNextTile(chance);
        b.registerTile(chance);
        
        PropertyTile lightBlue2 = new PropertyTile(9, "Vermont", PropertyColor.LIGHT_BLUE, 100, 6, 50);
        lightBlue2.setAction(new PropertyAction(lightBlue2));
        chance.setNextTile(lightBlue2);
        b.registerTile(lightBlue2);
        
        PropertyTile lightBlue3 = new PropertyTile(10, "Connecticut", PropertyColor.LIGHT_BLUE, 120, 8, 50);
        lightBlue3.setAction(new PropertyAction(lightBlue3));
        lightBlue2.setNextTile(lightBlue3);
        b.registerTile(lightBlue3);
        
        Tile jail = new NamedTile(11, "Jail");
        lightBlue3.setNextTile(jail);
        b.registerTile(jail);
        
        UtilityTile utility = new UtilityTile(12, "Electric Company", 150);
        utility.setAction(new UtilityAction(utility));
        jail.setNextTile(utility);
        b.registerTile(utility);
        
        PropertyTile darkBlue1 = new PropertyTile(39, "Park Place", PropertyColor.DARK_BLUE, 350, 35, 200);
        darkBlue1.setAction(new PropertyAction(darkBlue1));
        utility.setNextTile(darkBlue1);
        b.registerTile(darkBlue1);
        
        PropertyTile darkBlue2 = new PropertyTile(40, "Boardwalk", PropertyColor.DARK_BLUE, 400, 50, 200);
        darkBlue2.setAction(new PropertyAction(darkBlue2));
        darkBlue1.setNextTile(darkBlue2);
        b.registerTile(darkBlue2);
        
        darkBlue2.setNextTile(go);
        
        return b;
    }
    
    @Test
    void testUtilityRentCalculation() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        UtilityTile utility = (UtilityTile) board.getTileById(12);
        
        game.buyProperty(player1, utility);
        
        Dice mockDice = new Dice(2) {
            @Override
            public int rollAll() { return 7; }
        };
        BoardGame mockGame = new BoardGame(mockDice);
        mockGame.setBoard(board);
        mockGame.addPlayer(player1);
        mockGame.addPlayer(player2);
        
        utility.setRentContext(7, 1);
        assertEquals(28, utility.getRent());
    }
    
    @Test
    void testTaxActionReducesBalance() {
        game.addPlayer(player1);
        
        Tile taxTile = board.getTileById(5);
        player1.setCurrentTile(taxTile);
        
        TaxAction taxAction = new TaxAction(200);
        taxAction.execute(player1, game);
        
        assertEquals(1300, player1.getBalance());
    }
    
    @Test
    void testPropertyBuildingProgression() {
        game.addPlayer(player1);
        
        PropertyTile lightBlue1 = (PropertyTile) board.getTileById(7);
        PropertyTile lightBlue2 = (PropertyTile) board.getTileById(9);
        PropertyTile lightBlue3 = (PropertyTile) board.getTileById(10);
        
        game.buyProperty(player1, lightBlue1);
        game.buyProperty(player1, lightBlue2);
        game.buyProperty(player1, lightBlue3);
        
        assertTrue(player1.hasMonopoly(PropertyColor.LIGHT_BLUE));
        
        assertTrue(lightBlue1.canBuildHouse());
        
        for (int i = 0; i < 4; i++) {
            lightBlue1.addHouse();
            player1.adjustBalance(-50);
        }
        
        assertEquals(4, lightBlue1.getHouseCount());
        assertTrue(lightBlue1.canBuildHotel());
        
        lightBlue1.buildHotel();
        player1.adjustBalance(-50);
        
        assertTrue(lightBlue1.hasHotel());
        assertEquals(0, lightBlue1.getHouseCount());
    }
    
    private static class TestGameEndObserver implements GameObserver {
        boolean gameEnded = false;
        Player winner = null;
        
        @Override
        public void onGameEnd(Player w) {
            gameEnded = true;
            winner = w;
        }
        
        @Override
        public void onDiceRoll(Player player, int die1, int die2) {}
        @Override
        public void onPlayerMoved(Player player, Tile from, Tile to, int steps) {}
        @Override
        public void onTileAction(Player player, String actionName, Tile tile) {}
        @Override
        public void onPropertyAvailable(Player player, Tile property) {}
        @Override
        public void onRentPaid(Player payer, Player owner, Tile property, int amount) {}
        @Override
        public void onPlayerSkipped(Player player) {}
    }
}