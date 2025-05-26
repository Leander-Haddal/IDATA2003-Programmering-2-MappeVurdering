package no.ntnu.tile;

import no.ntnu.Player;

/**
 * Represents a railroad space. Owners collect rent from visitors.
 */
public class RailroadTile extends Tile {
    private final String name;
    private final int price;
    private Player owner;

    /**
     * Creates a railroad tile.
     * 
     * @param id the tile ID
     * @param name the railroad name
     * @param price the purchase price
     */
    public RailroadTile(int id, String name, int price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    /**
     * Old constructor for backward compatibility.
     * 
     * @param id the tile ID
     * @param price the purchase price
     */
    public RailroadTile(int id, int price) {
        this(id, "Railroad " + id, price);
    }

    @Override
    public String getName() {
        return name;
    }

    public int calculateRent(int ownedCount) {
        return switch (ownedCount) {
            case 1 -> 25;
            case 2 -> 50;
            case 3 -> 100;
            case 4 -> 200;
            default -> 0;
        };
    }

    @Override
    public int getRent() {
        return 0;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isOwned() {
        return owner != null;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
