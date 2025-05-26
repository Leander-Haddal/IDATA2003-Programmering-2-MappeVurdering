package no.ntnu;

import java.util.HashMap;
import java.util.Map;
import no.ntnu.tile.Tile;

/**
 * Represents the game board containing all tiles in a circular linked structure.
 */
public class Board {
    private Tile firstTile;
    private final Map<Integer, Tile> tileMap = new HashMap<>();

    /**
     * Gets the first tile on the board (typically "GO").
     * 
     * @return the first tile
     */
    public Tile getFirstTile() {
        return firstTile;
    }

    /**
     * Sets the first tile on the board and registers it.
     * 
     * @param tile the tile to set as first
     */
    public void setFirstTile(Tile tile) {
        this.firstTile = tile;
        if (tile != null) {
            tileMap.put(tile.getId(), tile);
        }
    }

    /**
     * Registers a tile in the board's lookup map.
     * 
     * @param tile the tile to register
     * @throws IllegalArgumentException if tile is null or ID already exists
     */
    public void registerTile(Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Cannot register null tile");
        }
        if (tileMap.containsKey(tile.getId())) {
            return;
        }
        tileMap.put(tile.getId(), tile);
    }

    /**
     * Retrieves a tile by its ID.
     * 
     * @param id the tile ID
     * @return the tile with the given ID, or null if not found
     */
    public Tile getTileById(int id) {
        return tileMap.get(id);
    }

    /**
     * Gets the total number of tiles on the board.
     * 
     * @return the number of registered tiles
     */
    public int getTileCount() {
        return tileMap.size();
    }

    /**
     * Validates that the board is properly constructed.
     * 
     * @throws IllegalStateException if board is invalid
     */
    public void validate() {
        if (firstTile == null) {
            throw new IllegalStateException("Board has no first tile");
        }
        if (tileMap.size() != 40) {
            throw new IllegalStateException("Board should have exactly 40 tiles, but has " + tileMap.size());
        }
        
        Tile current = firstTile;
        int count = 0;
        do {
            count++;
            current = current.getNextTile();
            if (current == null) {
                throw new IllegalStateException("Board is not circular - break at tile " + (count));
            }
        } while (current != firstTile && count <= 40);
        
        if (count != 40) {
            throw new IllegalStateException("Board circularity issue - counted " + count + " tiles");
        }
    }
}