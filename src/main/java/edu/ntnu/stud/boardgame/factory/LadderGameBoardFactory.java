package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory for creating predefined Snakes and Ladders game boards.
 *
 * <p>This factory class provides several predefined board configurations for the Snakes and Ladders
 * game with varying difficulty levels. Each board includes different arrangements of ladders,
 * snakes, and special actions.
 *
 * <p>Available predefined boards include:
 *
 * <ul>
 *   <li><b>Classic</b> - Traditional game with balanced snakes and ladders
 *   <li><b>Easy</b> - Beginner-friendly game with more ladders than snakes
 *   <li><b>Hard</b> - Challenging game with more snakes than ladders
 *   <li><b>Extreme</b> - Extremely challenging game with many snakes and few ladders
 *   <li><b>Small</b> - A compact 30-tile game
 * </ul>
 *
 * <p>This factory is used by the {@link BoardGameFactory} to provide predefined board
 * configurations when creating new Ladder games.
 *
 * @see edu.ntnu.stud.boardgame.model.Board
 * @see edu.ntnu.stud.boardgame.model.action.LadderAction
 * @see edu.ntnu.stud.boardgame.model.action.SnakeAction
 * @see edu.ntnu.stud.boardgame.model.action.SkipTurnAction
 */
public class LadderGameBoardFactory {

  private static final Map<String, Board> PREDEFINED_BOARDS = new HashMap<>();

  static {
    PREDEFINED_BOARDS.put("Classic", createClassicBoard());
    PREDEFINED_BOARDS.put("Easy", createEasyBoard());
    PREDEFINED_BOARDS.put("Hard", createHardBoard());
    PREDEFINED_BOARDS.put("Extreme", createExtremeBoard());
    PREDEFINED_BOARDS.put("Small", createSmallBoard());
  }

  private LadderGameBoardFactory() {}

  /**
   * Returns a list of names for all available predefined board configurations.
   *
   * @return a list of predefined board names
   */
  public static List<String> getAvailableBoards() {
    return Arrays.asList("Classic", "Easy", "Hard", "Extreme", "Small");
  }

  /**
   * Creates a board instance for the specified predefined board configuration.
   *
   * @param boardName the name of the predefined board configuration to create
   * @return a fully configured {@link Board} instance
   * @throws IllegalArgumentException if boardName is null, empty, or not a recognized predefined
   *     board name
   */
  public static Board createBoard(String boardName) {
    if (boardName == null || boardName.trim().isEmpty()) {
      throw new IllegalArgumentException("Board name cannot be null or empty.");
    }
    if (!PREDEFINED_BOARDS.containsKey(boardName)) {
      throw new IllegalArgumentException("Unknown board name: " + boardName);
    }
    return PREDEFINED_BOARDS.get(boardName);
  }

  /**
   * Creates the classic Snakes and Ladders board configuration.
   *
   * <p>This is the traditional game with balanced numbers of snakes and ladders.
   *
   * @return a configured classic board
   */
  private static Board createClassicBoard() {
    Board board =
        new Board(
            "Classic Snakes and Ladders",
            "The traditional game with balanced snakes and ladders",
            10,
            9,
            0,
            90);

    initializeLadderBoard(board);

    addLadder(board, 1, 40);
    addLadder(board, 8, 10);
    addLadder(board, 36, 52);
    addLadder(board, 43, 62);
    addLadder(board, 49, 79);
    addLadder(board, 65, 82);
    addLadder(board, 68, 85);

    addSnake(board, 24, 5);
    addSnake(board, 33, 3);
    addSnake(board, 42, 30);
    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 74, 12);
    addSnake(board, 87, 70);

    return board;
  }

  /**
   * Creates an easy Snakes and Ladders board configuration.
   *
   * <p>This is a beginner-friendly game with more ladders than snakes.
   *
   * @return a configured easy board
   */
  private static Board createEasyBoard() {
    Board board =
        new Board(
            "Easy Snakes and Ladders",
            "A beginner-friendly game with more ladders than snakes",
            10,
            9,
            0,
            90);

    initializeLadderBoard(board);

    addLadder(board, 1, 40);
    addLadder(board, 8, 31);
    addLadder(board, 19, 42);
    addLadder(board, 28, 56);
    addLadder(board, 36, 52);
    addLadder(board, 43, 62);
    addLadder(board, 49, 79);
    addLadder(board, 65, 82);
    addLadder(board, 68, 85);

    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 87, 70);

    addSkipTurnAction(board, 21);

    return board;
  }

  /**
   * Creates a hard Snakes and Ladders board configuration.
   *
   * <p>This is a challenging game with more snakes than ladders.
   *
   * @return a configured hard board
   */
  private static Board createHardBoard() {
    Board board =
        new Board(
            "Hard Snakes and Ladders",
            "A challenging game with more snakes than ladders",
            10,
            9,
            0,
            90);

    initializeLadderBoard(board);

    addLadder(board, 7, 14);
    addLadder(board, 21, 42);
    addLadder(board, 36, 44);
    addLadder(board, 51, 67);
    addLadder(board, 71, 82);

    addSnake(board, 16, 4);
    addSnake(board, 24, 8);
    addSnake(board, 33, 3);
    addSnake(board, 38, 20);
    addSnake(board, 42, 30);
    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 74, 12);
    addSnake(board, 87, 70);
    addSnake(board, 89, 48);

    addSkipTurnAction(board, 18);
    addSkipTurnAction(board, 32);
    addSkipTurnAction(board, 59);

    return board;
  }

  /**
   * Creates an extreme Snakes and Ladders board configuration.
   *
   * <p>This is an extremely challenging game with many snakes, few ladders, and multiple skip turn
   * tiles.
   *
   * @return a configured extreme board
   */
  private static Board createExtremeBoard() {
    Board board =
        new Board(
            "Extreme Snakes and Ladders",
            "An extremely challenging game with many snakes and few ladders",
            10,
            9,
            0,
            90);

    initializeLadderBoard(board);

    addLadder(board, 7, 14);
    addLadder(board, 31, 40);
    addLadder(board, 71, 82);

    addSnake(board, 16, 4);
    addSnake(board, 24, 8);
    addSnake(board, 33, 3);
    addSnake(board, 38, 20);
    addSnake(board, 42, 30);
    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 74, 12);
    addSnake(board, 87, 70);
    addSnake(board, 89, 48);
    addSnake(board, 79, 41);
    addSnake(board, 61, 19);
    addSnake(board, 46, 25);

    addSkipTurnAction(board, 18);
    addSkipTurnAction(board, 32);
    addSkipTurnAction(board, 59);
    addSkipTurnAction(board, 76);
    addSkipTurnAction(board, 83);

    return board;
  }

  /**
   * Creates a small Snakes and Ladders board configuration.
   *
   * <p>This is a compact 30-tile game with fewer game elements.
   *
   * @return a configured small board
   */
  private static Board createSmallBoard() {
    Board board =
        new Board(
            "Small Snakes and Ladders", "A small 30-tile Snakes and Ladders game", 6, 5, 0, 30);

    initializeLadderBoard(board);

    addLadder(board, 1, 10);
    addLadder(board, 15, 25);
    addLadder(board, 20, 28);

    addSnake(board, 17, 7);
    addSnake(board, 22, 13);
    addSnake(board, 29, 16);

    addSkipTurnAction(board, 24);

    return board;
  }

  /**
   * Initializes the basic structure of a ladder board with the specified dimensions.
   *
   * <p>This method creates all tiles and arranges them in the serpentine pattern typical of Snakes
   * and Ladders boards, where each row alternates direction.
   *
   * @param board the board to initialize
   * @throws InvalidGameStateException if the board is improperly configured
   */
  private static void initializeLadderBoard(Board board) {
    int rows = board.getRows();
    int cols = board.getColumns();
    int totalTiles = rows * cols;

    Tile startingTile = new Tile(0);
    board.addTile(startingTile);

    for (int i = 1; i <= totalTiles; i++) {
      Tile tile = new Tile(i);

      int row = (i - 1) / cols;
      int col;

      if (row % 2 == 0) {
        col = (i - 1) % cols;
      } else {
        col = cols - 1 - ((i - 1) % cols);
      }

      tile.setRow(rows - 1 - row);
      tile.setColumn(col);

      board.addTile(tile);
    }

    for (int i = 0; i < totalTiles; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }
  }

  /**
   * Adds a ladder action from one tile to another.
   *
   * <p>When a player lands on the source tile, they immediately move to the destination tile
   * (moving up the board).
   *
   * @param board the board to add the ladder to
   * @param fromTileId the ID of the tile where the ladder starts
   * @param toTileId the ID of the tile where the ladder ends
   * @throws InvalidGameStateException if either the source or destination tile doesn't exist
   */
  private static void addLadder(Board board, int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    if (fromTile == null) {
      throw new InvalidGameStateException(
          "Ladder 'from' tile not found: " + fromTileId + ". Check factory configuration.");
    }
    if (toTile == null) {
      throw new InvalidGameStateException(
          "Ladder 'to' tile not found: " + toTileId + ". Check factory configuration.");
    }
    fromTile.setLandAction(new LadderAction(toTile));
  }

  /**
   * Adds a snake action from one tile to another.
   *
   * <p>When a player lands on the source tile, they immediately move to the destination tile
   * (moving down the board).
   *
   * @param board the board to add the snake to
   * @param fromTileId the ID of the tile where the snake head is
   * @param toTileId the ID of the tile where the snake tail is
   * @throws InvalidGameStateException if either the source or destination tile doesn't exist
   */
  private static void addSnake(Board board, int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    if (fromTile == null) {
      throw new InvalidGameStateException(
          "Snake 'from' tile not found: " + fromTileId + ". Check factory configuration.");
    }
    if (toTile == null) {
      throw new InvalidGameStateException(
          "Snake 'to' tile not found: " + toTileId + ". Check factory configuration.");
    }
    fromTile.setLandAction(new SnakeAction(toTile));
  }

  /**
   * Adds a skip turn action to a specific tile.
   *
   * <p>When a player lands on the specified tile, they will miss their next turn.
   *
   * @param board the board to add the skip turn action to
   * @param tileId the ID of the tile where the skip turn action should be placed
   * @throws InvalidGameStateException if the specified tile doesn't exist
   */
  private static void addSkipTurnAction(Board board, int tileId) {
    Tile tile = board.getTile(tileId);
    if (tile == null) {
      throw new InvalidGameStateException(
          "SkipTurnAction tile not found: " + tileId + ". Check factory configuration.");
    }
    tile.setLandAction(new SkipTurnAction());
  }
}
