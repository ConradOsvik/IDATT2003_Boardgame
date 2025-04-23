package edu.ntnu.stud.boardgame.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the game board containing the collection of tiles. The board manages a map of tile IDs
 * to tile objects, allowing for tile lookup by ID.
 */
public class _Board extends BaseModel {

  /**
   * Map of tile IDs to tile objects for efficient tile lookup.
   */
  private final Map<Integer, Tile> tiles;

  /**
   * Constructs a new board with an empty tile collection.
   */
  public _Board() {
    this.tiles = new HashMap<>();
  }

  /**
   * Adds a tile to the board. If a tile with the same ID already exists, it will be replaced with
   * the new tile.
   *
   * @param tile The tile to add to the board
   * @throws IllegalArgumentException if the tile is null
   */
  public void addTile(Tile tile) {
    requireNotNull(tile, "Tile cannot be null");
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Retrieves a tile from the board by its ID.
   *
   * @param tileId The ID of the tile to retrieve
   * @return The tile with the specified ID, or null if no such tile exists
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }
}
