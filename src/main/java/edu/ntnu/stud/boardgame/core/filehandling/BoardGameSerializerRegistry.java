package edu.ntnu.stud.boardgame.core.filehandling;

import edu.ntnu.stud.boardgame.core.model.BoardGame;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for board game serializers and deserializers.
 * This allows different types of board games to be serialized and deserialized
 * using their own specialized implementations.
 */
public class BoardGameSerializerRegistry {

    private static final Map<String, Serializer<? extends BoardGame>> serializers = new HashMap<>();
    private static final Map<String, Deserializer<? extends BoardGame>> deserializers = new HashMap<>();

    /**
     * Registers a serializer and deserializer for a specific game type.
     *
     * @param gameType     the identifier for the game type
     * @param serializer   the serializer for the game type
     * @param deserializer the deserializer for the game type
     * @param <T>          the specific BoardGame subtype
     */
    public static <T extends BoardGame> void register(String gameType,
            Serializer<T> serializer,
            Deserializer<T> deserializer) {
        serializers.put(gameType, serializer);
        deserializers.put(gameType, deserializer);
    }

    /**
     * Gets a serializer for a game type.
     *
     * @param gameType the identifier for the game type
     * @return the serializer for the game type
     * @param <T> the specific BoardGame subtype
     * @throws IllegalArgumentException if no serializer is registered for the game
     *                                  type
     */
    @SuppressWarnings("unchecked")
    public static <T extends BoardGame> Serializer<T> getSerializer(String gameType) {
        Serializer<? extends BoardGame> serializer = serializers.get(gameType);
        if (serializer == null) {
            throw new IllegalArgumentException("No serializer registered for game type: " + gameType);
        }
        return (Serializer<T>) serializer;
    }

    /**
     * Gets a deserializer for a game type.
     *
     * @param gameType the identifier for the game type
     * @return the deserializer for the game type
     * @param <T> the specific BoardGame subtype
     * @throws IllegalArgumentException if no deserializer is registered for the
     *                                  game type
     */
    @SuppressWarnings("unchecked")
    public static <T extends BoardGame> Deserializer<T> getDeserializer(String gameType) {
        Deserializer<? extends BoardGame> deserializer = deserializers.get(gameType);
        if (deserializer == null) {
            throw new IllegalArgumentException("No deserializer registered for game type: " + gameType);
        }
        return (Deserializer<T>) deserializer;
    }

    /**
     * Checks if a serializer is registered for a game type.
     *
     * @param gameType the identifier for the game type
     * @return true if a serializer is registered for the game type
     */
    public static boolean hasSerializer(String gameType) {
        return serializers.containsKey(gameType);
    }

    /**
     * Checks if a deserializer is registered for a game type.
     *
     * @param gameType the identifier for the game type
     * @return true if a deserializer is registered for the game type
     */
    public static boolean hasDeserializer(String gameType) {
        return deserializers.containsKey(gameType);
    }
}