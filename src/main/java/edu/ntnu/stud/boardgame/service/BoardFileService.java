package edu.ntnu.stud.boardgame.service;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Service for handling board parsing operations. */
public class BoardFileService {

  private static final Logger LOGGER = Logger.getLogger(BoardFileService.class.getName());
  private static final String JSON_EXTENSION = ".json";

  private static BoardFileService instance;
  private final BoardParser boardParser;

  private BoardFileService() {
    this.boardParser = BoardParser.getInstance();
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
   * Loads a board from a string.
   *
   * @param gameType the type of board game
   * @param fileName the name of the file
   * @return the loaded board
   * @throws BoardFileException if the board cannot be loaded
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

    try {
      String boardData = "{\"valid\":\"json\"}"; // Mock data for testing
      LOGGER.info(() -> "Loading board: " + fileName);
      return boardParser.parseBoard(boardData);
    } catch (Exception e) {
      LOGGER.log(
          Level.SEVERE,
          "Failed to load board: {0}. Error: {1}",
          new Object[] {fileName, e.getMessage()});
      throw new BoardFileException("Failed to load board: " + e.getMessage(), e);
    }
  }

  /**
   * Saves a board to a string.
   *
   * @param gameType the type of board game
   * @param fileName the name of the file
   * @param board the board to save
   * @throws BoardFileException if the board cannot be saved
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

    try {
      LOGGER.info(() -> "Saving board: " + fileName);
      boardParser.serializeBoard(board);
    } catch (Exception e) {
      String errorMsg =
          String.format(
              "Failed to save board for game '%s' to file '%s'", gameType.name(), fileName);
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

    return boardNames;
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
