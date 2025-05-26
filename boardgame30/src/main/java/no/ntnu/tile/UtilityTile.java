package no.ntnu.tile;

import no.ntnu.Player;

/**
 * Represents a utility space; rent depends on dice roll.
 */
public class UtilityTile extends Tile {
    private final String name;
    private final int price;
    private Player owner;
    private int lastRoll;
    private int ownedCount;

    /**
     * Creates a utility tile.
     * 
     * @param id the tile ID
     * @param name the utility name
     * @param price the purchase price
     */
    public UtilityTile(int id, String name, int price) {
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
    public UtilityTile(int id, int price) {
        this(id, "Utility " + id, price);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    /**
     * Should be called by action to set context for rent calculation.
     * 
     * @param lastRoll the dice roll total
     * @param ownedCount number of utilities owned by the owner
     */
    public void setRentContext(int lastRoll, int ownedCount) {
        this.lastRoll = lastRoll;
        this.ownedCount = ownedCount;
    }

    @Override
    public int getRent() {
        int multiplier = (ownedCount == 1 ? 4 : 10);
        return lastRoll * multiplier;
    }

    /**
     * Gets the owner of this utility.
     * 
     * @return the owner player, or null if unowned
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Checks if this utility is owned.
     * 
     * @return true if owned
     */
    public boolean isOwned() {
        return owner != null;
    }

    /**
     * Sets the owner of this utility.
     * 
     * @param owner the new owner
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }
}