package edu.ntnu.stud.boardgame.core.filehandling;

import edu.ntnu.stud.boardgame.core.model.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry for player serializers and deserializers.
 * This allows different types of players to be serialized and deserialized
 * using their own specialized implementations.
 */
public class PlayerSerializerRegistry {

    private static final Map<String, Serializer<List<Player>>> serializers = new HashMap<>();
    private static final Map<String, Deserializer<List<Player>>> deserializers = new HashMap<>();

    /**
     * Registers a serializer and deserializer for a specific player type.
     *
     * @param playerType   the identifier for the player type
     * @param serializer   the serializer for the player type
     * @param deserializer the deserializer for the player type
     */
    public static void register(String playerType,
            Serializer<List<Player>> serializer,
            Deserializer<List<Player>> deserializer) {
        serializers.put(playerType, serializer);
        deserializers.put(playerType, deserializer);
    }

    /**
     * Gets a serializer for a player type.
     *
     * @param playerType the identifier for the player type
     * @return the serializer for the player type
     * @throws IllegalArgumentException if no serializer is registered for the
     *                                  player type
     */
    public static Serializer<List<Player>> getSerializer(String playerType) {
        Serializer<List<Player>> serializer = serializers.get(playerType);
        if (serializer == null) {
            throw new IllegalArgumentException("No serializer registered for player type: " + playerType);
        }
        return serializer;
    }

    /**
     * Gets a deserializer for a player type.
     *
     * @param playerType the identifier for the player type
     * @return the deserializer for the player type
     * @throws IllegalArgumentException if no deserializer is registered for the
     *                                  player type
     */
    public static Deserializer<List<Player>> getDeserializer(String playerType) {
        Deserializer<List<Player>> deserializer = deserializers.get(playerType);
        if (deserializer == null) {
            throw new IllegalArgumentException("No deserializer registered for player type: " + playerType);
        }
        return deserializer;
    }

    /**
     * Checks if a serializer is registered for a player type.
     *
     * @param playerType the identifier for the player type
     * @return true if a serializer is registered for the player type
     */
    public static boolean hasSerializer(String playerType) {
        return serializers.containsKey(playerType);
    }

    /**
     * Checks if a deserializer is registered for a player type.
     *
     * @param playerType the identifier for the player type
     * @return true if a deserializer is registered for the player type
     */
    public static boolean hasDeserializer(String playerType) {
        return deserializers.containsKey(playerType);
    }
}