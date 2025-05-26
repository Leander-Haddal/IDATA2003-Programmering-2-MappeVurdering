package no.ntnu.tile;

/**
 * Represents the color groups for properties in Monopoly.
 */
public enum PropertyColor {
    BROWN(2),
    LIGHT_BLUE(3),
    PINK(3),
    ORANGE(3),
    RED(3),
    YELLOW(3),
    GREEN(3),
    DARK_BLUE(2);
    
    private final int groupSize;
    
    PropertyColor(int groupSize) {
        this.groupSize = groupSize;
    }
    
    /**
     * Gets the number of properties in this color group.
     * 
     * @return the group size
     */
    public int getGroupSize() {
        return groupSize;
    }
}