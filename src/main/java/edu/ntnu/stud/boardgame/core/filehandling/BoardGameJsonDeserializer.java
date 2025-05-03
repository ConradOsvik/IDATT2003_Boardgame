package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;

/**
 * Deserializer for converting JSON format to BoardGame objects using GSON.
 */
public class BoardGameJsonDeserializer implements Deserializer<BoardGame> {

    /**
     * Deserializes a JSON string to a BoardGame object.
     *
     * @param serialized the JSON string to deserialize
     * @return the deserialized BoardGame object
     */
    @Override
    public BoardGame deserialize(String serialized) {
        if (serialized == null || serialized.trim().isEmpty()) {
            throw new IllegalArgumentException("Serialized JSON string cannot be null or empty");
        }

        JsonObject boardGameJson = JsonParser.parseString(serialized).getAsJsonObject();

        SlBoardGame boardGame = new SlBoardGame();

        boardGame.createBoard();
        boardGame.createDice(1);

        if (boardGameJson.has("tiles")) {
            JsonArray tilesArray = boardGameJson.getAsJsonArray("tiles");
            SlBoard board = boardGame.getBoard();

            for (JsonElement tileElement : tilesArray) {
                JsonObject tileJson = tileElement.getAsJsonObject();

                if (tileJson.has("action")) {
                    JsonObject actionJson = tileJson.getAsJsonObject("action");
                    String actionType = actionJson.get("type").getAsString();
                    int tileId = tileJson.get("id").getAsInt();
                    int destinationId = actionJson.get("destinationTileId").getAsInt();

                    if ("LadderAction".equals(actionType)) {
                        board.addLadder(tileId, destinationId);
                    } else if ("SnakeAction".equals(actionType)) {
                        board.addSnake(tileId, destinationId);
                    }
                }
            }
        }

        return boardGame;
    }
}