package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;

import java.util.Map;

/**
 * Serializer for converting BoardGame objects to JSON format using GSON.
 */
public class BoardGameJsonSerializer implements Serializer<BoardGame> {

    /**
     * Serializes a BoardGame object to a JSON string.
     *
     * @param boardGame the board game to serialize
     * @return a JSON string representation of the board game
     */
    @Override
    public String serialize(BoardGame boardGame) {
        if (boardGame == null) {
            throw new IllegalArgumentException("Board game cannot be null");
        }

        JsonObject boardGameJson = new JsonObject();

        if (boardGame instanceof SlBoardGame slBoardGame) {
            boardGameJson.addProperty("name", "Snakes and Ladders");
            boardGameJson.addProperty("description", "A classic Snakes and Ladders game.");

            SlBoard slBoard = slBoardGame.getBoard();
            if (slBoard != null) {
                boardGameJson.add("tiles", serializeTiles(slBoard));
            }
        }

        return boardGameJson.toString();
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