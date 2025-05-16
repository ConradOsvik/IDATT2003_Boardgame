package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.snakesandladders.model.action.LadderAction;
import edu.ntnu.stud.boardgame.snakesandladders.model.action.SnakeAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the Board class for the Snakes and Ladders game. It creates a 10x10 board with
 * 100 tiles and sets up snakes and ladders.
 */
public class SlBoard extends Board {

  private final int rows;
  private final int columns;
  private final int numTiles;

  private final Map<Integer, Integer> snakes;
  private final Map<Integer, Integer> ladders;

  public SlBoard(String name, String description) {
    this(name, description, 10, 9);
  }

  public SlBoard(String name, String description, int rows, int columns) {
    super(name, description);
    this.rows = rows;
    this.columns = columns;
    this.numTiles = rows * columns;
    this.tiles = new HashMap<>();
    this.snakes = new HashMap<>();
    this.ladders = new HashMap<>();
  }

  /**
   * Initializes the board by creating tiles and setting up snakes and ladders.
   */
  @Override
  public void initializeBoard() {
    Tile startingTile = new Tile(0);
    tiles.put(0, startingTile);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        int tileId = row * columns + col + 1;
        Tile tile = new Tile(tileId);
        tiles.put(tileId, tile);
      }
    }

    for (int i = 0; i < numTiles; i++) {
      Tile currentTile = tiles.get(i);
      Tile nextTile = tiles.get(i + 1);
      currentTile.setNextTile(nextTile);
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
    return tiles.get(0);
  }

  /**
   * Checks if the given tile is the last tile on the board.
   *
   * @param tile The tile to check
   * @return True if the tile is the last tile, false otherwise
   */
  @Override
  public boolean isLastTile(Tile tile) {
    return tile != null && tile.getTileId() == numTiles;
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
    return rows;
  }

  /**
   * Gets the number of columns on the board.
   *
   * @return The number of columns
   */
  public int getColumns() {
    return columns;
  }

  public int getNumTiles() {
    return numTiles;
  }

  /**
   * Converts a tile ID to x,y coordinates on the board. The board is laid out in a snake pattern
   * (zigzag).
   *
   * @param tileId The tile ID to convert
   * @return An array where [0] is the x coordinate and [1] is the y coordinate
   */
  public int[] getTileCoordinates(int tileId) {
    if (tileId == 0) {
      throw new IllegalArgumentException("Tile 0 doesn't have coordinates");
    }

    if (tileId < 1 || tileId > numTiles) {
      throw new IllegalArgumentException("Invalid tile ID: " + tileId);
    }

    int index = tileId - 1;

    int row = rows - 1 - (index / columns);

    int col;
    if ((rows - 1 - row) % 2 == 0) {
      col = index % columns;
    } else {
      col = columns - 1 - (index % columns);
    }

    return new int[]{row, col};
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }

    SlBoard slBoard = (SlBoard) obj;
    return Objects.equals(snakes, slBoard.snakes) && Objects.equals(ladders, slBoard.ladders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), snakes, ladders);
  }
}