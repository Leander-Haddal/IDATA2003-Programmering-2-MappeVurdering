package no.ntnu;

/**
 * Represents any special action that can be triggered when a Player
 * lands on a Tile (e.g., ladder up, snake down, skip turn, etc.).
 */
public interface TileAction {
    void execute(Player player);
}