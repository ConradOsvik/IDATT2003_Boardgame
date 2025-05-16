package edu.ntnu.stud.boardgame.service;

import edu.ntnu.stud.boardgame.exception.files.PlayerFileException;
import edu.ntnu.stud.boardgame.io.player.PlayerFileReader;
import edu.ntnu.stud.boardgame.io.player.PlayerFileReaderCsv;
import edu.ntnu.stud.boardgame.io.player.PlayerFileWriter;
import edu.ntnu.stud.boardgame.io.player.PlayerFileWriterCsv;
import edu.ntnu.stud.boardgame.model.Player;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class PlayerFileService {

  private static final Logger LOGGER = Logger.getLogger(PlayerFileService.class.getName());

  private final PlayerFileReader playerReader;
  private final PlayerFileWriter playerWriter;
  private final Path playersDirectory;

  public PlayerFileService() {
    this.playerReader = new PlayerFileReaderCsv();
    this.playerWriter = new PlayerFileWriterCsv();
    this.playersDirectory = Paths.get("data/players");

    createDirectoryIfNotExists(playersDirectory);
  }

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

  private String ensureFileExtension(String fileName) {
    if (!fileName.toLowerCase().endsWith(".csv")) {
      return fileName + ".csv";
    }
    return fileName;
  }
}
