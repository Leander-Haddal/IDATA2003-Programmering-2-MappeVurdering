package no.ntnu.tile;

import no.ntnu.action.TileAction;

/**
 * Represents a single Tile on the board. Each Tile has:
 * - A unique ID
 * - A reference to the next Tile (for a linear board)
 * - An optional TileAction
 */
public class Tile {
    private final int id;
    private Tile nextTile;
    private TileAction action; 

    public Tile(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    public TileAction getAction() {
        return action;
    }

    public void setAction(TileAction action) {
        this.action = action;
    }

    public String getName() {
        return "Tile " + id;
    }

    public int getRent() {
        return 0;
    }

    public int getPrice() {
        return 0;
    }
}