package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.StartAction;
import edu.ntnu.stud.boardgame.model.action.TaxAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class for creating Monopoly board game configurations.
 *
 * <p>This class provides methods to create different pre-defined Monopoly board layouts:
 *
 * <ul>
 *   <li>Standard - Classic monopoly game with 40 spaces
 *   <li>Mini - Smaller version with 24 spaces
 *   <li>Economic - Higher property values and taxes version
 * </ul>
 *
 * <p>Each board is fully initialized with appropriate tiles, actions, and layout.
 */
public class MonopolyBoardFactory {

  private static final Map<String, Board> PREDEFINED_BOARDS = new HashMap<>();
  private static final int START_BONUS = 50;

  static {
    PREDEFINED_BOARDS.put("Standard", createStandardBoard());
    PREDEFINED_BOARDS.put("Mini", createMiniBoard());
    PREDEFINED_BOARDS.put("Economic", createEconomicBoard());
  }

  private MonopolyBoardFactory() {}

  /**
   * Returns a list of all available predefined board names.
   *
   * @return list of available board configuration names
   */
  public static List<String> getAvailableBoards() {
    return Arrays.asList("Standard", "Mini", "Economic");
  }

  /**
   * Creates a board based on the provided board name.
   *
   * @param boardName the name of the board configuration to create
   * @return the configured board instance
   * @throws IllegalArgumentException if boardName is null, empty, or not recognized
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
   * Creates the standard Monopoly board with 40 spaces.
   *
   * @return a configured standard Monopoly board
   */
  private static Board createStandardBoard() {
    Board board =
        new Board(
            "Standard Monopoly",
            "The classic monopoly game with 40 spaces around the board",
            11,
            11,
            0,
            39);

    initializeStandardBoard(board);

    return board;
  }

  /**
   * Creates a mini version of the Monopoly board with 24 spaces.
   *
   * @return a configured mini Monopoly board
   */
  private static Board createMiniBoard() {
    Board board =
        new Board(
            "Mini Monopoly", "A smaller version with 24 spaces around the board", 6, 6, 0, 23);

    initializeMiniBoard(board);

    return board;
  }

  /**
   * Creates an economic variant of the Monopoly board with higher values.
   *
   * @return a configured economic Monopoly board
   */
  private static Board createEconomicBoard() {
    Board board = new Board("Economic Monopoly", "Higher property values and taxes", 11, 11, 0, 39);

    initializeEconomicBoard(board);

    return board;
  }

  /**
   * Initializes the standard Monopoly board with appropriate tiles and actions.
   *
   * @param board the board to initialize
   */
  private static void initializeStandardBoard(Board board) {

    Tile startTile = new Tile(0);
    startTile.setRow(10);
    startTile.setColumn(10);
    startTile.setName("GO");
    startTile.setLandAction(new StartAction(START_BONUS));
    board.addTile(startTile);

    for (int i = 1; i <= 10; i++) {
      Tile tile = new Tile(i);
      tile.setRow(10);
      tile.setColumn(10 - i);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 11; i <= 20; i++) {
      Tile tile = new Tile(i);
      tile.setRow(20 - i);
      tile.setColumn(0);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 21; i <= 30; i++) {
      Tile tile = new Tile(i);
      tile.setRow(0);
      tile.setColumn(i - 20);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 31; i <= 39; i++) {
      Tile tile = new Tile(i);
      tile.setRow(i - 30);
      tile.setColumn(10);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 0; i < 39; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

    Tile lastTile = board.getTile(39);
    Tile firstTile = board.getTile(0);
    lastTile.setNextTile(firstTile);
  }

  /**
   * Initializes the mini Monopoly board with appropriate tiles and actions.
   *
   * @param board the board to initialize
   */
  private static void initializeMiniBoard(Board board) {
    Tile startTile = new Tile(0);
    startTile.setRow(5);
    startTile.setColumn(5);
    startTile.setName("GO");
    startTile.setLandAction(new StartAction(START_BONUS));
    board.addTile(startTile);

    for (int i = 1; i <= 5; i++) {
      Tile tile = new Tile(i);
      tile.setRow(5);
      tile.setColumn(5 - i);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 6; i <= 11; i++) {
      Tile tile = new Tile(i);
      tile.setRow(11 - i);
      tile.setColumn(0);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 12; i <= 17; i++) {
      Tile tile = new Tile(i);
      tile.setRow(0);
      tile.setColumn(i - 12);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 18; i <= 23; i++) {
      Tile tile = new Tile(i);
      tile.setRow(i - 18);
      tile.setColumn(5);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 0; i < 23; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

    Tile lastTile = board.getTile(23);
    Tile firstTile = board.getTile(0);
    lastTile.setNextTile(firstTile);
  }

  /**
   * Initializes the economic Monopoly board with appropriate tiles and actions.
   *
   * @param board the board to initialize
   */
  private static void initializeEconomicBoard(Board board) {
    Tile startTile = new Tile(0);
    startTile.setRow(10);
    startTile.setColumn(10);
    startTile.setName("GO");
    startTile.setLandAction(new StartAction(100));
    board.addTile(startTile);

    for (int i = 1; i < 40; i++) {
      Tile tile = new Tile(i);

      int row;
      int col;
      if (i <= 10) {
        row = 10;
        col = 10 - i;
      } else if (i <= 20) {
        row = 20 - i;
        col = 0;
      } else if (i <= 30) {
        row = 0;
        col = i - 20;
      } else {
        row = i - 30;
        col = 10;
      }

      tile.setRow(row);
      tile.setColumn(col);
      setupTileProperties(tile, i);
      board.addTile(tile);
    }

    for (int i = 0; i < 39; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

    Tile lastTile = board.getTile(39);
    Tile firstTile = board.getTile(0);
    lastTile.setNextTile(firstTile);
  }

  /**
   * Sets up the properties for a tile based on its index.
   *
   * <p>Every 5th tile is a tax tile, while others are property tiles. Property prices increase with
   * the index.
   *
   * @param tile the tile to configure
   * @param index the position index of the tile
   */
  private static void setupTileProperties(Tile tile, int index) {
    if (index % 5 == 0) {
      int taxAmount = 100;
      tile.setName("Tax $" + taxAmount);
      tile.setLandAction(new TaxAction(taxAmount));
    } else {
      int propertyPrice = 50 + index * 10;
      tile.setName("Property $" + propertyPrice);
      tile.setLandAction(new PropertyAction(propertyPrice));
    }
  }
}
