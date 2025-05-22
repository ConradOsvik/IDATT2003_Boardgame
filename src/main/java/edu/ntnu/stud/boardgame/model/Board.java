package edu.ntnu.stud.boardgame.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game board with tiles and their connections.
 * 
 * <p>
 * Manages the board's dimensions, tile layout, and start/end positions.
 * </p>
 */
public class Board {

  private final Map<Integer, Tile> tiles;
  private final int rows;
  private final int columns;
  private final String name;
  private final String description;
  private final int startTileId;
  private final int endTileId;

  /**
   * Creates a new board with the specified dimensions and properties.
   *
   * @param name        board name
   * @param description board description
   * @param rows        number of rows
   * @param columns     number of columns
   * @param startTileId ID of the starting tile
   * @param endTileId   ID of the ending tile
   * @throws IllegalArgumentException if any parameter is invalid
   */
  public Board(String name, String description, int rows, int columns, int startTileId,
      int endTileId) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Board name cannot be null or empty.");
    }
    if (description == null) {
      throw new IllegalArgumentException("Board description cannot be null.");
    }
    if (rows <= 0) {
      throw new IllegalArgumentException("Board rows must be positive.");
    }
    if (columns <= 0) {
      throw new IllegalArgumentException("Board columns must be positive.");
    }
    if (startTileId < 0) {
      throw new IllegalArgumentException("Start tile ID cannot be negative.");
    }
    if (endTileId < 0) {
      throw new IllegalArgumentException("End tile ID cannot be negative.");
    }

    this.tiles = new HashMap<>();
    this.rows = rows;
    this.columns = columns;
    this.name = name;
    this.description = description;
    this.startTileId = startTileId;
    this.endTileId = endTileId;
  }

  /**
   * Adds a tile to the board.
   *
   * @param tile the tile to add
   * @throws IllegalArgumentException if tile is null
   */
  public void addTile(Tile tile) {
    if (tile == null) {
      throw new IllegalArgumentException("Cannot add a null tile to the board.");
    }
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Gets a tile by its ID.
   *
   * @param tileId the ID of the tile
   * @return the tile, or null if not found
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  /**
   * Gets all tiles on the board.
   *
   * @return map of tile IDs to tiles
   */
  public Map<Integer, Tile> getTiles() {
    return tiles;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getStartTileId() {
    return startTileId;
  }

  public int getEndTileId() {
    return endTileId;
  }
}
