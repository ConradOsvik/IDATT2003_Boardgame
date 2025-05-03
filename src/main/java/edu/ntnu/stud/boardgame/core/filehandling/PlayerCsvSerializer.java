package edu.ntnu.stud.boardgame.core.filehandling;

import java.util.List;
import java.util.stream.Collectors;
import edu.ntnu.stud.boardgame.core.model.Player;

/**
 * Base serializer for converting Player objects to CSV format.
 * This class provides common functionality for all player serializers.
 * Specific player types should extend this class to provide specialized
 * serialization.
 */
public abstract class PlayerCsvSerializer implements Serializer<List<Player>> {

    /**
     * Serializes a list of Player objects to a CSV string.
     * Each player is represented as a line with columns separated by commas.
     *
     * @param players the list of players to serialize
     * @return a CSV-formatted string representing the players
     */
    @Override
    public String serialize(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return "";
        }

        return players.stream()
                .map(this::serializePlayer)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Serializes a single player to a CSV line.
     * Subclasses must implement this method to provide player-specific
     * serialization.
     *
     * @param player the player to serialize
     * @return a CSV line representing the player
     */
    protected abstract String serializePlayer(Player player);
}