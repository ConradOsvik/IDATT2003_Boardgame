package edu.ntnu.stud.boardgame.core.filehandling;

import java.util.ArrayList;
import java.util.List;
import edu.ntnu.stud.boardgame.core.model.Player;

/**
 * Base deserializer for converting CSV format to Player objects.
 * This class provides common functionality for all player deserializers.
 * Specific player types should extend this class to provide specialized
 * deserialization.
 */
public abstract class PlayerCsvDeserializer implements Deserializer<List<Player>> {

    /**
     * Deserializes a CSV string to a list of Player objects.
     * Each line in the CSV should have player data separated by commas.
     *
     * @param serialized the CSV string to deserialize
     * @return a list of deserialized Player objects
     */
    @Override
    public List<Player> deserialize(String serialized) {
        if (serialized == null || serialized.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Player> players = new ArrayList<>();
        String[] lines = serialized.split("\n");

        for (String line : lines) {
            Player player = deserializeLine(line);
            if (player != null) {
                players.add(player);
            }
        }

        return players;
    }

    /**
     * Deserializes a single CSV line to a Player object.
     * Subclasses must implement this method to provide player-specific
     * deserialization.
     *
     * @param line the CSV line to deserialize
     * @return the deserialized Player object, or null if deserialization failed
     */
    protected abstract Player deserializeLine(String line);
}