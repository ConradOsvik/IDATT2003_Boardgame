package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.core.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Base deserializer for converting JSON strings to Player objects.
 * This class provides common functionality for all player deserializers.
 * Specific player types should extend this class to provide specialized
 * deserialization.
 */
public abstract class PlayerJsonDeserializer implements Deserializer<List<Player>> {

    /**
     * Deserializes a JSON string to a list of Player objects.
     *
     * @param serialized the JSON string to deserialize
     * @return the deserialized list of Player objects
     */
    @Override
    public List<Player> deserialize(String serialized) {
        if (serialized == null || serialized.trim().isEmpty() || "[]".equals(serialized.trim())) {
            return new ArrayList<>();
        }

        List<Player> players = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(serialized).getAsJsonArray();

        for (JsonElement element : jsonArray) {
            JsonObject playerJson = element.getAsJsonObject();
            Player player = createPlayerFromJson(playerJson);
            if (player != null) {
                players.add(player);
            }
        }

        return players;
    }

    /**
     * Creates a Player object from a JsonObject.
     * Subclasses must implement this method to provide player-specific
     * deserialization.
     *
     * @param jsonObject the JSON object to deserialize
     * @return the deserialized Player object
     */
    protected abstract Player createPlayerFromJson(JsonObject jsonObject);
}