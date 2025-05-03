package edu.ntnu.stud.boardgame.core.service;

import edu.ntnu.stud.boardgame.core.filehandling.BasicFileHandler;
import edu.ntnu.stud.boardgame.core.filehandling.FileHandler;
import edu.ntnu.stud.boardgame.core.filehandling.FileUtil;
import edu.ntnu.stud.boardgame.core.filehandling.GenericBoardGameDeserializer;
import edu.ntnu.stud.boardgame.core.filehandling.GenericBoardGameSerializer;
import edu.ntnu.stud.boardgame.core.filehandling.GenericPlayerCsvDeserializer;
import edu.ntnu.stud.boardgame.core.filehandling.GenericPlayerCsvSerializer;
import edu.ntnu.stud.boardgame.core.filehandling.Serializer;
import edu.ntnu.stud.boardgame.core.filehandling.Deserializer;
import edu.ntnu.stud.boardgame.core.filehandling.SerializerRegistryInitializer;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.snakesandladders.factory.SlBoardGameFactory;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for saving and loading board games and players.
 */
public class GameSaveService {

    private static final Logger LOGGER = Logger.getLogger(GameSaveService.class.getName());

    private final FileHandler fileHandler;
    private final Serializer<BoardGame> boardGameSerializer;
    private final Deserializer<BoardGame> boardGameDeserializer;
    private final Serializer<List<Player>> playerSerializer;
    private final Deserializer<List<Player>> playerDeserializer;

    /**
     * Constructs a new GameSaveService with default serializers and deserializers.
     */
    public GameSaveService() {
        SerializerRegistryInitializer.initialize();

        this.fileHandler = new BasicFileHandler();
        this.boardGameSerializer = new GenericBoardGameSerializer();
        this.boardGameDeserializer = new GenericBoardGameDeserializer();
        this.playerSerializer = new GenericPlayerCsvSerializer();
        this.playerDeserializer = new GenericPlayerCsvDeserializer();
    }

    /**
     * Saves a board game to a JSON file with the specified name.
     *
     * @param boardGame the board game to save
     * @param gameName  the name to use for the saved game file (without extension)
     * @return true if the game was saved successfully, false otherwise
     */
    public boolean saveGame(BoardGame boardGame, String gameName) {
        if (boardGame == null || gameName == null || gameName.trim().isEmpty()) {
            return false;
        }

        try {
            String serialized = boardGameSerializer.serialize(boardGame);
            Path filePath = FileUtil.getGameFilePath(gameName);
            fileHandler.writeToFile(filePath.toString(), serialized);
            LOGGER.info("Game saved successfully to " + filePath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save game: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Loads a board game from a JSON file with the specified name.
     * If the file doesn't exist, creates a new game.
     *
     * @param gameName the name of the game file to load (without extension)
     * @return the loaded board game, or a new game if the file doesn't exist
     */
    public BoardGame loadGame(String gameName) {
        if (gameName == null || gameName.trim().isEmpty()) {
            return null;
        }

        try {
            Path filePath = FileUtil.getGameFilePath(gameName);
            if (!Files.exists(filePath)) {
                LOGGER.info("Game file does not exist: " + filePath);
                SlBoardGame newGame = SlBoardGameFactory.createClassicGame();
                saveGame(newGame, gameName);
                return newGame;
            }

            String serialized = fileHandler.readFromFile(filePath.toString());
            BoardGame boardGame = boardGameDeserializer.deserialize(serialized);
            LOGGER.info("Game loaded successfully from " + filePath);
            return boardGame;
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Failed to load game: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Saves a list of players to a CSV file with the specified name.
     *
     * @param players        the list of players to save
     * @param playerListName the name to use for the saved player list file (without
     *                       extension)
     * @return true if the players were saved successfully, false otherwise
     */
    public boolean savePlayers(List<Player> players, String playerListName) {
        if (players == null || playerListName == null || playerListName.trim().isEmpty()) {
            return false;
        }

        try {
            String serialized = playerSerializer.serialize(players);
            Path filePath = FileUtil.getPlayerFilePath(playerListName);
            fileHandler.writeToFile(filePath.toString(), serialized);
            LOGGER.info("Players saved successfully to " + filePath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save players: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Loads a list of players from a CSV file with the specified name.
     * If the file doesn't exist, creates a new empty list and saves it.
     *
     * @param playerListName the name of the player list file to load (without
     *                       extension)
     * @return the loaded list of players, or an empty list if the file doesn't
     *         exist or loading failed
     */
    public List<Player> loadPlayers(String playerListName) {
        if (playerListName == null || playerListName.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            Path filePath = FileUtil.getPlayerFilePath(playerListName);
            if (!Files.exists(filePath)) {
                LOGGER.info("Player list file does not exist: " + filePath);
                List<Player> emptyList = new ArrayList<>();
                savePlayers(emptyList, playerListName);
                return emptyList;
            }

            String serialized = fileHandler.readFromFile(filePath.toString());
            List<Player> players = playerDeserializer.deserialize(serialized);
            LOGGER.info("Players loaded successfully from " + filePath);
            return players;
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Failed to load players: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Saves the classic Snakes and Ladders game to a JSON file.
     *
     * @return true if the game was saved successfully, false otherwise
     */
    public boolean saveClassicSnakesAndLadders() {
        SlBoardGame classicGame = SlBoardGameFactory.createClassicGame();
        return saveGame(classicGame, "classic_snakes_and_ladders");
    }
}