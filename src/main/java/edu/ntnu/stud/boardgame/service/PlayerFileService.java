package edu.ntnu.stud.boardgame.service;

import edu.ntnu.stud.boardgame.exception.files.PlayerFileException;
import edu.ntnu.stud.boardgame.io.player.PlayerFileReader;
import edu.ntnu.stud.boardgame.io.player.PlayerFileReaderCsv;
import edu.ntnu.stud.boardgame.io.player.PlayerFileWriter;
import edu.ntnu.stud.boardgame.io.player.PlayerFileWriterCsv;
import edu.ntnu.stud.boardgame.model.Player;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Singleton Service for handling player file operations such as loading, saving, and listing
 * available player lists.
 */
public class PlayerFileService {

  private static final Logger LOGGER = Logger.getLogger(PlayerFileService.class.getName());

  private static PlayerFileService instance;

  private final PlayerFileReader playerReader;
  private final PlayerFileWriter playerWriter;
  private final Path playersDirectory;

  private PlayerFileService() {
    this.playerReader = new PlayerFileReaderCsv();
    this.playerWriter = new PlayerFileWriterCsv();
    this.playersDirectory = Paths.get("data/players");

    createDirectoryIfNotExists(playersDirectory);
  }

  /**
   * Gets the singleton instance of PlayerFileService.
   *
   * @return the PlayerFileService instance
   */
  public static synchronized PlayerFileService getInstance() {
    if (instance == null) {
      instance = new PlayerFileService();
    }
    return instance;
  }

  /**
   * Loads a list of players from a file.
   *
   * @param fileName the name of the file
   * @return the loaded list of players
   * @throws PlayerFileException if the players cannot be loaded
   * @throws IllegalArgumentException if fileName is null or empty
   */
  public List<Player> loadPlayers(String fileName) throws PlayerFileException {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    Path filePath = playersDirectory.resolve(ensureFileExtension(fileName));

    if (!Files.exists(filePath)) {
      throw new PlayerFileException("Player file does not exist: " + filePath);
    }

    try {
      LOGGER.info("Loading players from: " + filePath);
      return playerReader.readPlayers(filePath);
    } catch (Exception e) {
      LOGGER.severe("Failed to load players from: " + filePath + ". Error: " + e.getMessage());
      throw new PlayerFileException("Failed to load players: " + e.getMessage(), e);
    }
  }

  /**
   * Saves a list of players to a file.
   *
   * @param fileName the name of the file
   * @param players the list of players to save
   * @throws PlayerFileException if the players cannot be saved
   * @throws IllegalArgumentException if fileName is null or empty, or if players is null
   */
  public void savePlayers(String fileName, List<Player> players) throws PlayerFileException {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    if (players == null) {
      throw new IllegalArgumentException("Players list cannot be null.");
    }

    Path filePath = playersDirectory.resolve(ensureFileExtension(fileName));

    try {
      LOGGER.info("Saving " + players.size() + " players to: " + filePath);
      playerWriter.writePlayers(filePath, players);
    } catch (Exception e) {
      LOGGER.severe("Failed to save players to: " + filePath + ". Error: " + e.getMessage());
      throw new PlayerFileException("Failed to save players: " + e.getMessage(), e);
    }
  }

  /**
   * Creates a directory if it does not exist.
   *
   * @param directory the directory to create
   */
  private void createDirectoryIfNotExists(Path directory) {
    try {
      if (!Files.exists(directory)) {
        Files.createDirectories(directory);
        LOGGER.info("Created directory: " + directory);
      }
    } catch (IOException e) {
      LOGGER.severe("Failed to create directory: " + directory + ". Error: " + e.getMessage());
    }
  }

  /**
   * Ensures that the file name has a .csv extension.
   *
   * @param fileName the file name to check
   * @return the file name with .csv extension
   * @throws IllegalArgumentException if fileName is null or empty
   */
  private String ensureFileExtension(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      LOGGER.warning("File name is null or empty in ensureFileExtension. Returning as is.");
      return fileName;
    }
    if (!fileName.toLowerCase().endsWith(".csv")) {
      return fileName + ".csv";
    }
    return fileName;
  }

  /**
   * Gets a list of available player list file names.
   *
   * @return a list of available player list names (without extension)
   * @throws PlayerFileException if the player directory cannot be accessed or listed
   */
  public List<String> getAvailablePlayerListFileNames() throws PlayerFileException {
    List<String> playerListNames = new ArrayList<>();

    File playersDirFile = playersDirectory.toFile();

    if (!playersDirFile.exists() || !playersDirFile.isDirectory()) {
      LOGGER.warning(
          "Player save directory does not exist or is not a directory: "
              + playersDirectory.toAbsolutePath());
      throw new PlayerFileException(
          "Player save directory not found: " + playersDirectory.toAbsolutePath());
    }

    File[] files = playersDirFile.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

    if (files == null) {
      throw new PlayerFileException(
          "Could not list files in player directory: " + playersDirectory.toAbsolutePath());
    }

    for (File file : files) {
      String fileName = file.getName();
      int lastDot = fileName.lastIndexOf('.');
      if (lastDot > 0 && fileName.substring(lastDot).equalsIgnoreCase(".csv")) {
        playerListNames.add(fileName.substring(0, lastDot));
      } else {
        playerListNames.add(fileName);
      }
    }
    return playerListNames;
  }
}
