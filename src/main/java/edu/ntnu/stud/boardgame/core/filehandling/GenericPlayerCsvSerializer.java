package edu.ntnu.stud.boardgame.core.filehandling;

import edu.ntnu.stud.boardgame.core.model.Player;

import java.util.List;

/**
 * A generic serializer for players that delegates to specific CSV serializers
 * based on the player type.
 */
public class GenericPlayerCsvSerializer implements Serializer<List<Player>> {

    /**
     * The default player type to use if no appropriate serializer is found.
     */
    private static final String DEFAULT_PLAYER_TYPE = "SlPlayer";

    /**
     * Serializes a list of players to a CSV string.
     *
     * @param players the list of players to serialize
     * @return a CSV string representation of the players
     */
    @Override
    public String serialize(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return "";
        }

        // Determine the player type from the first player
        String playerType = determinePlayerType(players);

        // Get specific serializer
        Serializer<List<Player>> serializer = PlayerSerializerRegistry.getSerializer(playerType);

        // Use the appropriate serializer directly
        return serializer.serialize(players);
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