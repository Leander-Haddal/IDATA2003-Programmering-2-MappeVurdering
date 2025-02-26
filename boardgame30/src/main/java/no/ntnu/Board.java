package no.ntnu;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the entire board: a collection of Tiles, linked linearly in this example.
 */
public class Board {
    private Tile firstTile;
    private final Map<Integer, Tile> tilesById = new HashMap<>();

    public Board() {
        // You might build the board externally and call 'setFirstTile' + 'registerTile'.
    }

    public void setFirstTile(Tile tile) {
        this.firstTile = tile;
        registerTile(tile);
    }

    public Tile getFirstTile() {
        return firstTile;
    }

    /**
     * Registers a tile in the internal map.
     * This is useful for quick lookups by ID without manually walking the chain.
     */
    public void registerTile(Tile tile) {
        tilesById.put(tile.getId(), tile);
    }

    public Tile getTileById(int id) {
        return tilesById.get(id);
    }

    /**
     * Returns the last Tile in the chain by following 'nextTile'.
     * Useful to know the "final" tile on the board.
     */
    public Tile getLastTile() {
        if (firstTile == null) return null;
        Tile current = firstTile;
        while (current.getNextTile() != null) {
            current = current.getNextTile();
        }
        return current;
    }
}