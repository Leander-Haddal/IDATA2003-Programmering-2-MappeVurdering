package no.ntnu.tile;

import no.ntnu.Player;

/**
 * A board tile representing a purchasable property.
 */
public class PropertyTile extends Tile {
    private final int price;
    private final int rent;
    private final PropertyColor color;
    private final String name;
    private final int housePrice;
    private Player owner;
    private int houseCount = 0;
    private boolean hasHotel = false;

    /**
     * Creates a property tile with all attributes.
     * 
     * @param id the tile ID
     * @param name the property name
     * @param color the property color group
     * @param price the purchase price
     * @param rent the base rent
     * @param housePrice the price to build a house
     */
    public PropertyTile(int id, String name, PropertyColor color, int price, int rent, int housePrice) {
        super(id);
        this.name = name;
        this.color = color;
        this.price = price;
        this.rent = rent;
        this.housePrice = housePrice;
    }

    /**
     * Old constructor for backward compatibility.
     * 
     * @param id the tile ID
     * @param price the purchase price
     * @param rent the base rent
     */
    public PropertyTile(int id, int price, int rent) {
        this(id, "Property " + id, null, price, rent, 50);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getRent() {
        if (owner == null) return 0;
        
        if (color != null && owner.hasMonopoly(color)) {
            if (hasHotel) {
                return rent * 50; 
            } else if (houseCount > 0) {
                return rent * (1 + houseCount * 5); 
            } else {
                return rent * 2; 
            }
        }
        return rent;
    }

    /**
     * Gets the property color group.
     * 
     * @return the color group
     */
    public PropertyColor getColor() {
        return color;
    }

    /**
     * Gets the price to build a house.
     * 
     * @return the house price
     */
    public int getHousePrice() {
        return housePrice;
    }

    /**
     * Gets the number of houses on this property.
     * 
     * @return the house count
     */
    public int getHouseCount() {
        return houseCount;
    }

    /**
     * Checks if this property has a hotel.
     * 
     * @return true if has hotel
     */
    public boolean hasHotel() {
        return hasHotel;
    }

    /**
     * Checks if a house can be built on this property.
     * 
     * @return true if building is allowed
     */
    public boolean canBuildHouse() {
        return owner != null 
            && color != null
            && owner.hasMonopoly(color) 
            && houseCount < 4 
            && !hasHotel
            && owner.getBalance() >= housePrice;
    }

    /**
     * Adds a house to this property.
     */
    public void addHouse() {
        if (houseCount < 4) {
            houseCount++;
        }
    }

    /**
     * Checks if a hotel can be built on this property.
     * 
     * @return true if hotel building is allowed
     */
    public boolean canBuildHotel() {
        return owner != null 
            && houseCount == 4 
            && !hasHotel
            && owner.getBalance() >= housePrice;
    }

    /**
     * Converts houses to a hotel.
     */
    public void buildHotel() {
        if (houseCount == 4) {
            houseCount = 0;
            hasHotel = true;
        }
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isOwned() {
        return owner != null;
    }

    public void setOwner(Player p) {
        this.owner = p;
    }
}