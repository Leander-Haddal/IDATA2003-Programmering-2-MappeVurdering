package no.ntnu.Archive;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import no.ntnu.Board;
import no.ntnu.action.SkipTurnAction;
import no.ntnu.tile.Tile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/**
 * Load a Board from a JSON file.
 */
public class BoardJsonDao {

    /**
     * Read the JSON at jsonPath and build a Board of Tiles
     * @param jsonPath path to JSON file
     * @return the constructed Board
     * @throws IOException if reading the file fails
     */
    public Board loadBoard(Path jsonPath) throws IOException {
        String text = Files.readString(jsonPath);
        JsonObject root = JsonParser.parseString(text).getAsJsonObject();
        JsonArray tilesArray = root.getAsJsonArray("tiles");

        Board board = new Board();
        for (JsonElement el : tilesArray) {
            int id = el.getAsJsonObject().get("id").getAsInt();
            board.registerTile(new Tile(id));
        }
        board.setFirstTile(board.getTileById(1));

        for (JsonElement el : tilesArray) {
            JsonObject o = el.getAsJsonObject();
            Tile t = board.getTileById(o.get("id").getAsInt());
            if (o.has("nextTile")) {
                int nextId = o.get("nextTile").getAsInt();
                t.setNextTile(board.getTileById(nextId));
            }
            if (o.has("action")) {
                JsonObject a = o.getAsJsonObject("action");
                String type = a.get("type").getAsString();
                if ("LadderAction".equals(type)) {
                    int dst = a.get("destinationTileId").getAsInt();
                    t.setAction(new LadderAction(board.getTileById(dst)));
                }
                else if ("SkipTurnAction".equals(type)) {
                    t.setAction(new SkipTurnAction());
                }
            }
        }
        return board;
    }
}
