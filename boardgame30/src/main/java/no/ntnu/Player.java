package no.ntnu;

/**
 * Represents a player in the game, including their name,
 * chosen token identifier, and current tile.
 */
public class Player {
    private final String name;
    private final String token;
    private Tile currentTile;

    /**
     * Create a player with given name and no token.
     * (for backward compatibility)
     */
    public Player(String name) {
        this(name, "");
    }

    /**
     * Create a player with given name and token.
     */
    public Player(String name, String token) {
        this.name = name;
        this.token = token;
    }

    /**
     * Return the player’s name.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the player’s token identifier.
     */
    public String getToken() {
        return token;
    }

    /**
     * Return the tile the player is currently on, or null if not placed.
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Returns the ID of the tile the player is currently on, or -1 if none.
     */
    public int getCurrentTileId() {
        return currentTile != null ? currentTile.getId() : -1;
    }

    /**
     * Place the player on the given tile.
     */
    public void setCurrentTile(Tile tile) {
        this.currentTile = tile;
    }
}
