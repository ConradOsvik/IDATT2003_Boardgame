package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.core.model.BoardGame;

/**
 * A generic deserializer for board games that delegates to specific
 * deserializers based on the game type stored in the serialized data.
 */
public class GenericBoardGameDeserializer implements Deserializer<BoardGame> {

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

        JsonObject wrapper = JsonParser.parseString(serialized).getAsJsonObject();

        if (!wrapper.has("gameType") || !wrapper.has("data")) {
            throw new IllegalArgumentException("Invalid game data format: missing gameType or data");
        }

        String gameType = wrapper.get("gameType").getAsString();
        String gameData = wrapper.get("data").getAsString();

        Deserializer<BoardGame> deserializer = BoardGameSerializerRegistry.getDeserializer(gameType);
        return deserializer.deserialize(gameData);
    }
}