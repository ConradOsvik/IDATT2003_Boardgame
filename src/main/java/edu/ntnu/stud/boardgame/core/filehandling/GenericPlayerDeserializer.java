package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.core.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic deserializer for players that delegates to specific deserializers
 * based on the player type stored in the serialized data.
 */
public class GenericPlayerDeserializer implements Deserializer<List<Player>> {

    /**
     * Deserializes a JSON string to a list of Player objects.
     *
     * @param serialized the JSON string to deserialize
     * @return the deserialized list of Player objects
     */
    @Override
    public List<Player> deserialize(String serialized) {
        if (serialized == null || serialized.trim().isEmpty()) {
            return new ArrayList<>();
        }

        JsonObject wrapper = JsonParser.parseString(serialized).getAsJsonObject();

        if (!wrapper.has("playerType") || !wrapper.has("data")) {
            throw new IllegalArgumentException("Invalid player data format: missing playerType or data");
        }

        String playerType = wrapper.get("playerType").getAsString();
        String playerData = wrapper.get("data").getAsString();

        Deserializer<List<Player>> deserializer = PlayerSerializerRegistry.getDeserializer(playerType);
        return deserializer.deserialize(playerData);
    }
}