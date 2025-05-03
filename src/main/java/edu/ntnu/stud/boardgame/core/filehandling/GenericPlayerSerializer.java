package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.core.model.Player;

import java.util.List;

/**
 * A generic serializer for players that delegates to specific serializers
 * based on the player type.
 */
public class GenericPlayerSerializer implements Serializer<List<Player>> {

    /**
     * The default player type to use if no appropriate serializer is found.
     */
    private static final String DEFAULT_PLAYER_TYPE = "SlPlayer";

    /**
     * Serializes a list of players to a JSON string, including type information.
     *
     * @param players the list of players to serialize
     * @return a JSON string representation of the players
     */
    @Override
    public String serialize(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return "[]";
        }

        String playerType = determinePlayerType(players);

        Serializer<List<Player>> serializer = PlayerSerializerRegistry.getSerializer(playerType);

        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("playerType", playerType);
        wrapper.addProperty("data", serializer.serialize(players));

        return wrapper.toString();
    }

    /**
     * Determines the player type based on the class of the players.
     * Uses the first player in the list to determine the type.
     *
     * @param players the list of players to determine the type for
     * @return the player type identifier
     */
    private String determinePlayerType(List<Player> players) {
        if (players.isEmpty()) {
            return DEFAULT_PLAYER_TYPE;
        }

        Player firstPlayer = players.get(0);
        return firstPlayer.getClass().getSimpleName();
    }
}