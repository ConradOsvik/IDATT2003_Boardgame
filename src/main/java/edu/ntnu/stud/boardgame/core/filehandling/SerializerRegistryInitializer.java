package edu.ntnu.stud.boardgame.core.filehandling;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.serialization.SlBoardGameDeserializer;
import edu.ntnu.stud.boardgame.snakesandladders.serialization.SlBoardGameSerializer;
import edu.ntnu.stud.boardgame.snakesandladders.serialization.SlPlayerDeserializer;
import edu.ntnu.stud.boardgame.snakesandladders.serialization.SlPlayerSerializer;

/**
 * Initializer for registering all game serializers.
 */
public class SerializerRegistryInitializer {

    /**
     * Private constructor to prevent instantiation.
     */
    private SerializerRegistryInitializer() {
    }

    /**
     * Initializes the registry with all known game serializers.
     */
    public static void initialize() {
        BoardGameSerializerRegistry.register(
                SlBoardGame.class.getSimpleName(),
                new SlBoardGameSerializer(),
                new SlBoardGameDeserializer());

        PlayerSerializerRegistry.register(
                SlPlayer.class.getSimpleName(),
                new SlPlayerSerializer(),
                new SlPlayerDeserializer());

    }
}