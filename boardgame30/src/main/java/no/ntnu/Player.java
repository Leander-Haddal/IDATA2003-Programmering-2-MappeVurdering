package no.ntnu;

/**
 * Represents a player in the game, including their name and current position on the board.
 */
public class Player {
    private final String name;
    private Tile currentTile;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Returns the ID of the tile the player is currently on, or -1 if none.
     */
    public int getCurrentTileId() {
        return currentTile != null ? currentTile.getId() : -1;
    }

    public void setCurrentTile(Tile tile) {
        this.currentTile = tile;
    }
}