package edu.ntnu.stud.boardgame.core.filehandling;

import edu.ntnu.stud.boardgame.core.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic deserializer for players that delegates to specific CSV
 * deserializers.
 * Uses a simple format where the first player determines the type for the
 * entire list.
 */
public class GenericPlayerCsvDeserializer implements Deserializer<List<Player>> {

    /**
     * The default player type to use if no other type can be determined.
     */
    private static final String DEFAULT_PLAYER_TYPE = "SlPlayer";

    /**
     * Deserializes a CSV string to a list of Player objects.
     *
     * @param serialized the CSV string to deserialize
     * @return the deserialized list of Player objects
     */
    @Override
    public List<Player> deserialize(String serialized) {
        if (serialized == null || serialized.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // For CSV, we use a standard approach where we assume all players are of the
        // same type
        // We always use the deserializer for SlPlayer by default
        String playerType = DEFAULT_PLAYER_TYPE;

        Deserializer<List<Player>> deserializer = PlayerSerializerRegistry.getDeserializer(playerType);
        return deserializer.deserialize(serialized);
    }
}