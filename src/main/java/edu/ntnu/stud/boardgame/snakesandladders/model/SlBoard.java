package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.snakesandladders.model.action.LadderAction;
import edu.ntnu.stud.boardgame.snakesandladders.model.action.SnakeAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Board class for the Snakes and Ladders game. It creates a 10x10 board with
 * 100 tiles and sets up snakes and ladders.
 */
public class SlBoard extends Board {

  public static final int BOARD_ROWS = 10;
  public static final int BOARD_COLUMNS = 9;
  public static final int NUM_TILES = BOARD_ROWS * BOARD_COLUMNS;

  private final Map<Integer, Integer> snakes;
  private final Map<Integer, Integer> ladders;

  /**
   * Constructs a new Snakes and Ladders board.
   */
  public SlBoard() {
    this.tiles = new HashMap<>();
    this.snakes = new HashMap<>();
    this.ladders = new HashMap<>();
  }

  /**
   * Initializes the board by creating tiles and setting up snakes and ladders.
   */
  @Override
  public void initializeBoard() {
    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLUMNS; col++) {
        int tileId = row * BOARD_COLUMNS + col + 1;
        Tile tile = new Tile(tileId);
        tiles.put(tileId, tile);
      }
    }

    for (int i = 1; i < NUM_TILES; i++) {
      Tile currentTile = tiles.get(i);
      Tile nextTile = tiles.get(i + 1);
      currentTile.addConnectedTile(nextTile);
    }
  }

  /**
   * Adds a ladder from the start tile to the end tile.
   *
   * @param start The tile ID where the ladder starts
   * @param end   The tile ID where the ladder ends
   */
  public void addLadder(int start, int end) {
    Tile startTile = tiles.get(start);
    Tile endTile = tiles.get(end);

    if (startTile != null && endTile != null) {
      startTile.setLandAction(new LadderAction(endTile));
      ladders.put(start, end);
    }
  }

  /**
   * Adds a snake from the start tile to the end tile.
   *
   * @param start The tile ID where the snake starts (head)
   * @param end   The tile ID where the snake ends (tail)
   */
  public void addSnake(int start, int end) {
    Tile startTile = tiles.get(start);
    Tile endTile = tiles.get(end);

    if (startTile != null && endTile != null) {
      startTile.setLandAction(new SnakeAction(endTile));
      snakes.put(start, end);
    }
  }

  /**
   * Gets the starting tile of the game.
   *
   * @return The starting tile (tile ID 1)
   */
  @Override
  public Tile getStartingTile() {
    return tiles.get(1);
  }

  /**
   * Checks if the given tile is the last tile on the board.
   *
   * @param tile The tile to check
   * @return True if the tile is the last tile, false otherwise
   */
  @Override
  public boolean isLastTile(Tile tile) {
    return tile != null && tile.getTileId() == NUM_TILES;
  }

  /**
   * Gets the map of snakes on the board.
   *
   * @return A map where keys are the head tile IDs and values are the tail tile IDs
   */
  public Map<Integer, Integer> getSnakes() {
    return new HashMap<>(snakes);
  }

  /**
   * Gets the map of ladders on the board.
   *
   * @return A map where keys are the bottom tile IDs and values are the top tile IDs
   */
  public Map<Integer, Integer> getLadders() {
    return new HashMap<>(ladders);
  }

  /**
   * Gets the number of rows on the board.
   *
   * @return The number of rows
   */
  public int getRows() {
    return BOARD_ROWS;
  }

  /**
   * Gets the number of columns on the board.
   *
   * @return The number of columns
   */
  public int getColumns() {
    return BOARD_COLUMNS;
  }

  /**
   * Converts a tile ID to x,y coordinates on the board. The board is laid out in a snake pattern
   * (zigzag).
   *
   * @param tileId The tile ID to convert
   * @return An array where [0] is the x coordinate and [1] is the y coordinate
   */
  public int[] getTileCoordinates(int tileId) {
    if (tileId < 1 || tileId > NUM_TILES) {
      throw new IllegalArgumentException("Invalid tile ID: " + tileId);
    }

    int index = tileId - 1;

    int row = BOARD_ROWS - 1 - (index / BOARD_COLUMNS);

    int col;
    if ((BOARD_ROWS - 1 - row) % 2 == 0) {
      col = index % BOARD_COLUMNS;
    } else {
      col = BOARD_COLUMNS - 1 - (index % BOARD_COLUMNS);
    }

    return new int[]{col, row};
  }
}