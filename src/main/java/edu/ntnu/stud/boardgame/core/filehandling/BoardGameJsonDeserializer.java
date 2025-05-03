package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;

/**
 * Base deserializer for converting JSON strings to BoardGame objects.
 * This class provides common functionality for all board game deserializers.
 * Specific game types should extend this class to provide specialized
 * deserialization.
 */
public abstract class BoardGameJsonDeserializer implements Deserializer<BoardGame> {

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

        JsonObject jsonObject = JsonParser.parseString(serialized).getAsJsonObject();
        return createBoardGameFromJson(jsonObject);
    }

    /**
     * Creates a BoardGame object from a JsonObject.
     * Subclasses must implement this method to provide game-specific
     * deserialization.
     *
     * @param jsonObject the JSON object to deserialize
     * @return the deserialized BoardGame object
     */
    protected abstract BoardGame createBoardGameFromJson(JsonObject jsonObject);
}