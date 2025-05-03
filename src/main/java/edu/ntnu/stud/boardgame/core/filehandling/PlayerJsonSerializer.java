package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.model.Player;

import java.util.List;

/**
 * Base serializer for converting Player objects to JSON format.
 * This class provides common functionality for all player serializers.
 * Specific player types should extend this class to provide specialized
 * serialization.
 */
public abstract class PlayerJsonSerializer implements Serializer<List<Player>> {

    /**
     * Serializes a list of Player objects to a JSON string.
     *
     * @param players the list of players to serialize
     * @return a JSON string representation of the players
     */
    @Override
    public String serialize(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return "[]";
        }

        StringBuilder serialized = new StringBuilder("[");
        boolean first = true;

        for (Player player : players) {
            if (!first) {
                serialized.append(",");
            }
            first = false;

            JsonObject playerJson = new JsonObject();
            populatePlayerJson(player, playerJson);
            serialized.append(playerJson.toString());
        }

        serialized.append("]");
        return serialized.toString();
    }

    /**
     * Populates a JsonObject with data from a Player.
     * Subclasses must implement this method to provide player-specific
     * serialization.
     *
     * @param player     the player to serialize
     * @param jsonObject the JSON object to populate
     */
    protected abstract void populatePlayerJson(Player player, JsonObject jsonObject);
}