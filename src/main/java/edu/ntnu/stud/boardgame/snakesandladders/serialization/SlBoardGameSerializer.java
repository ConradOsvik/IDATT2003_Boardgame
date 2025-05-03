package edu.ntnu.stud.boardgame.snakesandladders.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.filehandling.BoardGameJsonSerializer;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;

import java.util.Map;

/**
 * Specialized serializer for Snakes and Ladders board games.
 */
public class SlBoardGameSerializer extends BoardGameJsonSerializer {

    /**
     * Populates a JsonObject with data from a Snakes and Ladders board game.
     *
     * @param boardGame     the board game to serialize
     * @param boardGameJson the JSON object to populate
     */
    @Override
    protected void populateJsonObject(BoardGame boardGame, JsonObject boardGameJson) {
        SlBoardGame slBoardGame = (SlBoardGame) boardGame;

        boardGameJson.addProperty("name", "Snakes and Ladders");
        boardGameJson.addProperty("description", "A classic Snakes and Ladders game.");

        SlBoard slBoard = slBoardGame.getBoard();
        if (slBoard != null) {
            boardGameJson.add("tiles", serializeTiles(slBoard));
        }
    }

    /**
     * Serializes the tiles of a board to a JSON array.
     *
     * @param board the board containing the tiles to serialize
     * @return a JSON array containing the serialized tiles
     */
    private JsonArray serializeTiles(SlBoard board) {
        JsonArray tilesArray = new JsonArray();
        Map<Integer, Integer> snakes = board.getSnakes();
        Map<Integer, Integer> ladders = board.getLadders();

        for (Tile tile : board.getTiles()) {
            JsonObject tileJson = new JsonObject();
            int tileId = tile.getTileId();

            tileJson.addProperty("id", tileId);

            if (!tile.getConnectedTiles().isEmpty()) {
                tileJson.addProperty("nextTile", tile.getConnectedTiles().getFirst().getTileId());
            }

            if (ladders.containsKey(tileId)) {
                JsonObject actionJson = new JsonObject();
                actionJson.addProperty("type", "LadderAction");
                actionJson.addProperty("destinationTileId", ladders.get(tileId));
                actionJson.addProperty("description", "Ladder from " + tileId + " to " + ladders.get(tileId));
                tileJson.add("action", actionJson);
            } else if (snakes.containsKey(tileId)) {
                JsonObject actionJson = new JsonObject();
                actionJson.addProperty("type", "SnakeAction");
                actionJson.addProperty("destinationTileId", snakes.get(tileId));
                actionJson.addProperty("description", "Snake from " + tileId + " to " + snakes.get(tileId));
                tileJson.add("action", actionJson);
            }

            tilesArray.add(tileJson);
        }

        return tilesArray;
    }
}