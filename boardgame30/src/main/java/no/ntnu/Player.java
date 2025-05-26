package no.ntnu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.ntnu.tile.PropertyColor;
import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.RailroadTile;
import no.ntnu.tile.Tile;
import no.ntnu.tile.UtilityTile;


/**
 * Represents a player in the game, including their name,
 * chosen token identifier, and current tile, balance, and owned assets.
 */
public class Player {
    private final String name;
    private final String token;
    private Tile currentTile;
    private boolean skipNextTurn = false;
    private int balance = 1500;
    private final List<Tile> owned = new ArrayList<>();
    private boolean inJail = false;
    private int jailTurns = 0;

    /** Backwards compatability constructor */
    public Player(String name) {
        this(name, "");
    }

    /** Full constructor. */
    public Player(String name, String token) {
        this.name  = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public int getCurrentTileId() {
        return currentTile != null ? currentTile.getId() : -1;
    }

    public void setCurrentTile(Tile tile) {
        this.currentTile = tile;
    }

    public boolean isSkipNextTurn() {
        return skipNextTurn;
    }

    public void setSkipNextTurn(boolean skip) {
        this.skipNextTurn = skip;
    }

    public int getBalance() {
        return balance;
    }

    public void adjustBalance(int amount) {
        this.balance += amount;
    }

    /**
     * Checks if the player is in jail.
     * 
     * @return true if in jail
     */
    public boolean isInJail() {
        return inJail;
    }

    /**
     * Sends the player to jail.
     */
    public void goToJail() {
        this.inJail = true;
        this.jailTurns = 0;
    }

    /**
     * Releases the player from jail.
     */
    public void releaseFromJail() {
        this.inJail = false;
        this.jailTurns = 0;
    }

    /**
     * Gets the number of turns the player has been in jail.
     * 
     * @return jail turn count
     */
    public int getJailTurns() {
        return jailTurns;
    }

    /**
     * Increments the jail turn counter.
     */
    public void incrementJailTurns() {
        this.jailTurns++;
    }

    /**
     * Record ownership of any asset (PropertyTile, RailroadTile, UtilityTile).
     */
    public void addOwned(Tile tile) {
        owned.add(tile);
    }

    /**
     * All owned ordinary properties.
     */
    public List<PropertyTile> getOwnedProperties() {
        return owned.stream()
            .filter(t -> t instanceof PropertyTile)
            .map(t -> (PropertyTile)t)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * All owned railroads.
     */
    public List<RailroadTile> getOwnedRailroads() {
        return owned.stream()
            .filter(t -> t instanceof RailroadTile)
            .map(t -> (RailroadTile)t)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * All owned utilities.
     */
    public List<UtilityTile> getOwnedUtilities() {
        return owned.stream()
            .filter(t -> t instanceof UtilityTile)
            .map(t -> (UtilityTile)t)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Clears all owned properties (used during bankruptcy).
     */
    public void clearOwnedProperties() {
        owned.clear();
    }

    /**
     * Checks if the player owns all properties of a color group.
     * 
     * @param color the color group to check
     * @return true if player has monopoly
     */
    public boolean hasMonopoly(PropertyColor color) {
        if (color == null) return false;
        
        List<PropertyTile> colorProperties = getOwnedProperties().stream()
            .filter(p -> p.getColor() == color)
            .collect(Collectors.toList());
        
        return colorProperties.size() == color.getGroupSize();
    }
}
