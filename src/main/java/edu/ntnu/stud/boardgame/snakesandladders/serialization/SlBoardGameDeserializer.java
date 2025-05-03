package edu.ntnu.stud.boardgame.snakesandladders.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.filehandling.BoardGameJsonDeserializer;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;

/**
 * Specialized deserializer for Snakes and Ladders board games.
 */
public class SlBoardGameDeserializer extends BoardGameJsonDeserializer {

    /**
     * Creates a Snakes and Ladders board game from a JsonObject.
     *
     * @param jsonObject the JSON object to deserialize
     * @return the deserialized Snakes and Ladders board game
     */
    @Override
    protected BoardGame createBoardGameFromJson(JsonObject jsonObject) {
        SlBoardGame boardGame = new SlBoardGame();
        boardGame.createBoard();
        boardGame.createDice(1);

        if (jsonObject.has("tiles")) {
            JsonArray tilesArray = jsonObject.getAsJsonArray("tiles");
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