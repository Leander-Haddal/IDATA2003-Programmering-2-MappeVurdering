package no.ntnu.tile;

/**
 * A tile with just a name (for GO, Jail, etc).
 */
public class NamedTile extends Tile {
    private final String name;

    /**
     * Creates a named tile.
     * 
     * @param id the tile ID
     * @param name the tile name
     */
    public NamedTile(int id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}