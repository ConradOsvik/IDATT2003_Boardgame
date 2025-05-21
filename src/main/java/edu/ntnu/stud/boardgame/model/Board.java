package edu.ntnu.stud.boardgame.model;

import java.util.HashMap;
import java.util.Map;

public class Board {

  private final Map<Integer, Tile> tiles;
  private final int rows;
  private final int columns;
  private final String name;
  private final String description;
  private final int startTileId;
  private final int endTileId;

  public Board(String name, String description, int rows, int columns, int startTileId,
      int endTileId) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Board name cannot be null or empty.");
    }
    if (description == null) { // Assuming empty description is okay, but not null
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
    // Further validation for startTileId and endTileId might be needed
    // e.g., ensuring they are within a valid range or correspond to actual tiles
    // once added.
    // For now, basic constructor parameter checks are implemented.

    this.tiles = new HashMap<>();
    this.rows = rows;
    this.columns = columns;
    this.name = name;
    this.description = description;
    this.startTileId = startTileId;
    this.endTileId = endTileId;
  }

  public void addTile(Tile tile) {
    if (tile == null) {
      throw new IllegalArgumentException("Cannot add a null tile to the board.");
    }
    tiles.put(tile.getTileId(), tile);
  }

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

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
