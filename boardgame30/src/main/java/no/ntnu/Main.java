package no.ntnu;

public class Main {
    public static void main(String[] args) {
        // 1. Create a new board (with 10 tiles for this example)
        Board board = createTestBoard();
        
        // 2. Create dice (2 dice, each with 6 faces)
        Dice dice = new Dice(2);

        // 3. Create the BoardGame object
        BoardGame game = new BoardGame(board, dice);

        // 4. Add some players
        game.addPlayer(new Player("Arne"));
        game.addPlayer(new Player("Ivar"));
        game.addPlayer(new Player("Majid"));
        game.addPlayer(new Player("Atle"));

        // 5. Play rounds until the game finishes
        System.out.println("=== Starting Board Game! ===");
        while (!game.isFinished()) {
            game.playRound();
            printRoundStatus(game);
        }

        // 6. Announce the winner
        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("And the winner is: " + winner.getName() + "!");
        } else {
            System.out.println("No winner determined. (Shouldn't happen with this logic.)");
        }
    }

    /**
     * Creates a linear board of 10 tiles, with a single ladder from tile #3 to tile #7.
     */
    private static Board createTestBoard() {
        Board board = new Board();
        
        // Create 10 tiles and link them
        Tile firstTile = new Tile(1);
        board.setFirstTile(firstTile); // also registers tile #1
        Tile current = firstTile;

        for (int i = 2; i <= 10; i++) {
            Tile next = new Tile(i);
            current.setNextTile(next);
            board.registerTile(next);
            current = next;
        }

        // Example: Put a LadderAction on tile #3 to jump to tile #7
        Tile tile3 = board.getTileById(3);
        Tile tile7 = board.getTileById(7);
        if (tile3 != null && tile7 != null) {
            tile3.setAction(new LadderAction(tile7));
        }

        return board;
    }

    /**
     * Prints each player's status: name and current tile ID.
     */
    private static void printRoundStatus(BoardGame game) {
        System.out.println("\nCurrent status after Round " + game.getRoundNumber() + ":");
        for (Player p : game.getPlayers()) {
            System.out.println("  - " + p.getName() + " on tile " + p.getCurrentTileId());
        }
    }
}
