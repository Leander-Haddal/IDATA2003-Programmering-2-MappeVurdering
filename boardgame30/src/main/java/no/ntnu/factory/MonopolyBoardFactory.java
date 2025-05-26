package no.ntnu.factory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import no.ntnu.Board;
import no.ntnu.BoardGame;
import no.ntnu.tile.Tile;
import no.ntnu.tile.PropertyColor;
import no.ntnu.exception.InvalidDataException;
import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.RailroadTile;
import no.ntnu.tile.UtilityTile;
import no.ntnu.tile.NamedTile;

/**
 * Factory for creating a standard Monopoly board.
 */
public final class MonopolyBoardFactory {
    private MonopolyBoardFactory() {}

    private enum SpaceType {
        GO, PROPERTY, RAILROAD, UTILITY,
        TAX, CHANCE, CHEST,
        JAIL, FREE_PARKING, GO_TO_JAIL
    }

    private static record SpaceSpec(
        int id,
        SpaceType type,
        String name,
        PropertyColor color,
        int price,
        int rent,
        int housePrice,
        String actionKey
    ) {}

    private static final List<SpaceSpec> SPECS = List.of(
        new SpaceSpec(1,  SpaceType.GO,           "GO",                        null,                    0,   0,   0, "GoAction"),
        new SpaceSpec(2,  SpaceType.PROPERTY,     "Mediterranean Avenue",      PropertyColor.BROWN,    60,   2,  50, "PropertyAction"),
        new SpaceSpec(3,  SpaceType.CHEST,        "Community Chest",           null,                    0,   0,   0, "ChestAction"),
        new SpaceSpec(4,  SpaceType.PROPERTY,     "Baltic Avenue",             PropertyColor.BROWN,    60,   4,  50, "PropertyAction"),
        new SpaceSpec(5,  SpaceType.TAX,          "Income Tax",                null,                    0,   0,   0, "TaxAction"),
        new SpaceSpec(6,  SpaceType.RAILROAD,     "Reading Railroad",          null,                  200,  25,   0, "RailroadAction"),
        new SpaceSpec(7,  SpaceType.PROPERTY,     "Oriental Avenue",           PropertyColor.LIGHT_BLUE, 100,   6,  50, "PropertyAction"),
        new SpaceSpec(8,  SpaceType.CHANCE,       "Chance",                    null,                    0,   0,   0, "ChanceAction"),
        new SpaceSpec(9,  SpaceType.PROPERTY,     "Vermont Avenue",            PropertyColor.LIGHT_BLUE, 100,   6,  50, "PropertyAction"),
        new SpaceSpec(10, SpaceType.PROPERTY,     "Connecticut Avenue",        PropertyColor.LIGHT_BLUE, 120,   8,  50, "PropertyAction"),
        new SpaceSpec(11, SpaceType.JAIL,         "Jail/Just Visiting",        null,                    0,   0,   0, null),
        new SpaceSpec(12, SpaceType.PROPERTY,     "St. Charles Place",         PropertyColor.PINK,     140,  10, 100, "PropertyAction"),
        new SpaceSpec(13, SpaceType.UTILITY,      "Electric Company",          null,                   150,   0,   0, "UtilityAction"),
        new SpaceSpec(14, SpaceType.PROPERTY,     "States Avenue",             PropertyColor.PINK,     140,  10, 100, "PropertyAction"),
        new SpaceSpec(15, SpaceType.PROPERTY,     "Virginia Avenue",           PropertyColor.PINK,     160,  12, 100, "PropertyAction"),
        new SpaceSpec(16, SpaceType.RAILROAD,     "Pennsylvania Railroad",     null,                   200,  25,   0, "RailroadAction"),
        new SpaceSpec(17, SpaceType.PROPERTY,     "St. James Place",           PropertyColor.ORANGE,   180,  14, 100, "PropertyAction"),
        new SpaceSpec(18, SpaceType.CHEST,        "Community Chest",           null,                    0,   0,   0, "ChestAction"),
        new SpaceSpec(19, SpaceType.PROPERTY,     "Tennessee Avenue",          PropertyColor.ORANGE,   180,  14, 100, "PropertyAction"),
        new SpaceSpec(20, SpaceType.PROPERTY,     "New York Avenue",           PropertyColor.ORANGE,   200,  16, 100, "PropertyAction"),
        new SpaceSpec(21, SpaceType.FREE_PARKING, "Free Parking",              null,                    0,   0,   0, null),
        new SpaceSpec(22, SpaceType.PROPERTY,     "Kentucky Avenue",           PropertyColor.RED,      220,  18, 150, "PropertyAction"),
        new SpaceSpec(23, SpaceType.CHANCE,       "Chance",                    null,                    0,   0,   0, "ChanceAction"),
        new SpaceSpec(24, SpaceType.PROPERTY,     "Indiana Avenue",            PropertyColor.RED,      220,  18, 150, "PropertyAction"),
        new SpaceSpec(25, SpaceType.PROPERTY,     "Illinois Avenue",           PropertyColor.RED,      240,  20, 150, "PropertyAction"),
        new SpaceSpec(26, SpaceType.RAILROAD,     "B&O Railroad",              null,                   200,  25,   0, "RailroadAction"),
        new SpaceSpec(27, SpaceType.PROPERTY,     "Atlantic Avenue",           PropertyColor.YELLOW,   260,  22, 150, "PropertyAction"),
        new SpaceSpec(28, SpaceType.PROPERTY,     "Ventnor Avenue",            PropertyColor.YELLOW,   260,  22, 150, "PropertyAction"),
        new SpaceSpec(29, SpaceType.UTILITY,      "Water Works",               null,                   150,   0,   0, "UtilityAction"),
        new SpaceSpec(30, SpaceType.PROPERTY,     "Marvin Gardens",            PropertyColor.YELLOW,   280,  24, 150, "PropertyAction"),
        new SpaceSpec(31, SpaceType.GO_TO_JAIL,   "Go To Jail",                null,                    0,   0,   0, "GoToJailAction"),
        new SpaceSpec(32, SpaceType.PROPERTY,     "Pacific Avenue",            PropertyColor.GREEN,    300,  26, 200, "PropertyAction"),
        new SpaceSpec(33, SpaceType.PROPERTY,     "North Carolina Avenue",     PropertyColor.GREEN,    300,  26, 200, "PropertyAction"),
        new SpaceSpec(34, SpaceType.CHEST,        "Community Chest",           null,                    0,   0,   0, "ChestAction"),
        new SpaceSpec(35, SpaceType.PROPERTY,     "Pennsylvania Avenue",       PropertyColor.GREEN,    320,  28, 200, "PropertyAction"),
        new SpaceSpec(36, SpaceType.RAILROAD,     "Short Line",                null,                   200,  25,   0, "RailroadAction"),
        new SpaceSpec(37, SpaceType.CHANCE,       "Chance",                    null,                    0,   0,   0, "ChanceAction"),
        new SpaceSpec(38, SpaceType.PROPERTY,     "Park Place",                PropertyColor.DARK_BLUE, 350,  35, 200, "PropertyAction"),
        new SpaceSpec(39, SpaceType.TAX,          "Luxury Tax",                null,                    0,   0,   0, "TaxAction"),
        new SpaceSpec(40, SpaceType.PROPERTY,     "Boardwalk",                 PropertyColor.DARK_BLUE, 400,  50, 200, "PropertyAction")
    );

    private static final Map<Integer,SpaceSpec> SPEC_BY_ID =
        SPECS.stream().collect(Collectors.toUnmodifiableMap(SpaceSpec::id, s->s));

    /**
     * Build & return a 40-tile board
     * 
     * @param game the game instance
     * @return a complete board
     * @throws InvalidDataException if board construction fails
     */
    public static Board createBoard(BoardGame game) throws InvalidDataException {
        Board board = new Board();

        SpaceSpec firstSpec = SPEC_BY_ID.get(1);
        Tile first = instantiateEmptyTile(firstSpec);
        board.setFirstTile(first);

        Tile prev = first;
        for (int id = 2; id <= 40; id++) {
            SpaceSpec spec = SPEC_BY_ID.get(id);
            if (spec == null) {
                throw new InvalidDataException("No board spec defined for space ID " + id);
            }
            Tile t = instantiateEmptyTile(spec);
            prev.setNextTile(t);
            board.registerTile(t);
            prev = t;
        }
        prev.setNextTile(first);

        for (SpaceSpec spec : SPECS) {
            if (spec.actionKey != null) {
                Tile t = board.getTileById(spec.id);
                t.setAction(TileActionFactory.create(
                    spec.actionKey, spec.id, board, game
                ));
            }
        }

        return board;
    }

    /**
     * Helper to pick the right subclass without attaching an action.
     * 
     * @param spec the space specification
     * @return a new tile instance
     */
    private static Tile instantiateEmptyTile(SpaceSpec spec) {
        return switch (spec.type) {
            case PROPERTY -> new PropertyTile(spec.id, spec.name, spec.color, spec.price, spec.rent, spec.housePrice);
            case RAILROAD -> new RailroadTile(spec.id, spec.name, spec.price);
            case UTILITY  -> new UtilityTile(spec.id, spec.name, spec.price);
            default      -> new NamedTile(spec.id, spec.name);
        };
    }
}