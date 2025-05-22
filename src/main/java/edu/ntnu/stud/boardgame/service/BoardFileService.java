package edu.ntnu.stud.boardgame.service;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.io.board.BoardFileReader;
import edu.ntnu.stud.boardgame.io.board.BoardFileReaderGson;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriter;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriterGson;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Singleton Service for handling board file operations such as loading, saving, and listing
 * available boards.
 */
public class BoardFileService {

  private static final Logger LOGGER = Logger.getLogger(BoardFileService.class.getName());
  private static final Path BOARDS_BASE_DIRECTORY = Paths.get("data/boards");
  private static final String JSON_EXTENSION = ".json";

  private static BoardFileService instance;

  private final BoardFileReader boardReader;
  private final BoardFileWriter boardWriter;

  private BoardFileService() {
    this.boardReader = new BoardFileReaderGson();
    this.boardWriter = new BoardFileWriterGson();
    createDirectoryIfNotExists(BOARDS_BASE_DIRECTORY);
  }

  /**
   * Gets the singleton instance of BoardFileService.
   *
   * @return the BoardFileService instance
   */
  public static synchronized BoardFileService getInstance() {
    if (instance == null) {
      instance = new BoardFileService();
    }
    return instance;
  }

  /**
   * Gets the directory for a specific game type.
   *
   * @param gameType the type of board game
   * @return the path to the game type directory
   * @throws IllegalArgumentException if gameType is null
   */
  private Path getGameTypeDirectory(final BoardGameType gameType) {
    if (gameType == null) {
      throw new IllegalArgumentException(
          "Game type cannot be null when getting game type directory.");
    }
    Path gameTypeDir = BOARDS_BASE_DIRECTORY.resolve(gameType.name().toLowerCase());
    createDirectoryIfNotExists(gameTypeDir);
    return gameTypeDir;
  }

  /**
   * Loads a board from a file.
   *
   * @param gameType the type of board game
   * @param fileName the name of the file
   * @return the loaded board
   * @throws BoardFileException       if the board cannot be loaded
   * @throws IllegalArgumentException if gameType is null or fileName is null or empty
   */
  public Board loadBoard(final BoardGameType gameType, final String fileName)
      throws BoardFileException {
    if (gameType == null) {
      throw new IllegalArgumentException("Game type cannot be null.");
    }
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    Path gameTypeDir = getGameTypeDirectory(gameType);
    Path boardPath = gameTypeDir.resolve(ensureFileExtension(fileName));

    if (!Files.exists(boardPath)) {
      throw new BoardFileException("Board file does not exist: " + boardPath);
    }

    try {
      LOGGER.info(() -> "Loading board from: " + boardPath);
      return boardReader.readBoard(boardPath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to load board from: {0}. Error: {1}",
          new Object[]{boardPath, e.getMessage()});
      throw new BoardFileException("Failed to load board: " + e.getMessage(), e);
    }
  }

  /**
   * Saves a board to a file.
   *
   * @param gameType the type of board game
   * @param fileName the name of the file
   * @param board    the board to save
   * @throws BoardFileException       if the board cannot be saved
   * @throws IllegalArgumentException if any parameter is null or fileName is empty
   */
  public void saveBoard(final BoardGameType gameType, final String fileName, final Board board)
      throws BoardFileException {
    if (gameType == null) {
      throw new IllegalArgumentException("Game type cannot be null.");
    }
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty.");
    }
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null.");
    }

    Path gameTypeDir = getGameTypeDirectory(gameType);
    Path boardPath = gameTypeDir.resolve(ensureFileExtension(fileName));

    try {
      LOGGER.info(() -> "Saving board to: " + boardPath);
      boardWriter.writeBoard(boardPath, board);
    } catch (Exception e) {
      String errorMsg = String.format("Failed to save board for game '%s' to file '%s'",
          gameType.name(), boardPath.getFileName());
      LOGGER.log(Level.SEVERE, errorMsg, e);
      throw new BoardFileException(errorMsg, e);
    }
  }

  /**
   * Lists all available boards for a game type.
   *
   * @param gameType the type of board game
   * @return a list of available board names (without extension)
   */
  public List<String> listAvailableBoards(final BoardGameType gameType) {
    List<String> boardNames = new ArrayList<>();

    if (gameType == null) {
      LOGGER.warning("Cannot list boards: game type is null.");
      return boardNames;
    }

    Path gameTypeDir = getGameTypeDirectory(gameType);

    try {
      if (Files.exists(gameTypeDir) && Files.isDirectory(gameTypeDir)) {
        try (Stream<Path> pathStream = Files.list(gameTypeDir)) {
          pathStream.filter(path -> path.toString().toLowerCase().endsWith(JSON_EXTENSION))
              .forEach(path -> {
                String fileName = path.getFileName().toString();
                boardNames.add(fileName.substring(0, fileName.length() - JSON_EXTENSION.length()));
              });
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Failed to list available boards. Error: {0}", e.getMessage());
    }

    return boardNames;
  }

  /**
   * Creates a directory if it does not exist.
   *
   * @param directory the directory to create
   */
  private void createDirectoryIfNotExists(final Path directory) {
    try {
      if (!Files.exists(directory)) {
        Files.createDirectories(directory);
        LOGGER.info(() -> "Created directory: " + directory);
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to create directory: {0}. Error: {1}",
          new Object[]{directory, e.getMessage()});
    }
  }

  /**
   * Ensures that the file name has a .json extension.
   *
   * @param fileName the file name to check
   * @return the file name with .json extension
   * @throws IllegalArgumentException if fileName is null or empty
   */
  private String ensureFileExtension(final String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "File name cannot be null or empty in ensureFileExtension.");
    }
    if (!fileName.toLowerCase().endsWith(JSON_EXTENSION)) {
      return fileName + JSON_EXTENSION;
    }
    return fileName;
  }
}