package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.action.TileAction;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a tile on the game board. Tiles have a unique ID, can have connected tiles in
 * different directions, and can trigger actions when players land on them.
 */
public class Tile extends BaseModel {

  /**
   * The unique identifier for this tile.
   */
  private final int tileId;

  /**
   * The action that occurs when a player lands on this tile, or null if no action.
   */
  private TileAction landAction;

  /**
   * Map of directions to lists of connected tiles.
   */
  private final Map<Direction, List<Tile>> connectedTiles;

  /**
   * Enum of possible directions for tile connections.
   */
  enum Direction {
    FORWARD,
    BACKWARD
  }

  /**
   * Constructs a new tile with the specified ID. The tile will initially have no connections or
   * landing action.
   *
   * @param tileId The unique identifier for this tile
   */
  Tile(int tileId) {
    this.tileId = tileId;
    this.connectedTiles = new EnumMap<>(Direction.class);

    this.connectedTiles.put(Direction.FORWARD, new ArrayList<>());
    this.connectedTiles.put(Direction.BACKWARD, new ArrayList<>());
  }

  /**
   * Gets the unique identifier for this tile.
   *
   * @return The tile ID
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * Called when a player lands on this tile. Performs the landing action if one is set.
   *
   * @param player The player who landed on this tile
   * @throws IllegalArgumentException if the player is null
   */
  void landPlayer(Player player) {
    requireNotNull(player, "Player cannot be null");

    if (landAction != null) {
      landAction.perform(player);
    }
  }

  /**
   * Called when a player leaves this tile. Currently a placeholder for future implementation.
   *
   * @param player The player who is leaving this tile
   * @throws IllegalArgumentException if the player is null
   */
  void leavePlayer(Player player) {
    requireNotNull(player, "Player cannot be null");
    //TODO: Implement
  }

  /**
   * Adds a connection to another tile in the forward direction. Also adds this tile as a connection
   * to the other tile in the backward direction. Does not add duplicate connections.
   *
   * @param nextTile The tile to connect to
   * @throws IllegalArgumentException if the next tile is null
   */
  void addNextTile(Tile nextTile) {
    requireNotNull(nextTile, "Next tile cannot be null");

    List<Tile> nextTiles = connectedTiles.get(Direction.FORWARD);
    if (!nextTiles.contains(nextTile)) {
      nextTiles.add(nextTile);
    }

    // We need to add this tile to the next tile's previous tiles
    List<Tile> prevTiles = nextTile.connectedTiles.get(Direction.BACKWARD);
    if (!prevTiles.contains(this)) {
      prevTiles.add(this);
    }
  }

  /**
   * Gets the list of tiles connected to this tile in the specified direction.
   *
   * @param direction The direction to get connections for
   * @return A list of connected tiles in the specified direction
   */
  List<Tile> getConnectedTiles(Direction direction) {
    return connectedTiles.get(direction);
  }

  /**
   * Compares this tile to another object for equality. Tiles are considered equal if they have the
   * same ID.
   *
   * @param obj The object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Tile tile = (Tile) obj;
    return tileId == tile.tileId;
  }

  /**
   * Returns a hash code for this tile based on its ID.
   *
   * @return The hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(tileId);
  }

  /**
   * Returns a string representation of this tile, including its ID and connected tiles in both
   * directions.
   *
   * @return A string representation of this tile
   */
  @Override
  public String toString() {
    return "Tile{" +
        "id=" + tileId +
        ", forwardConnectedTiles=" + connectedTiles.get(Direction.FORWARD).stream()
        .map(Tile::getTileId).toList() +
        ", backwardConnectedTiles=" + connectedTiles.get(Direction.BACKWARD).stream()
        .map(Tile::getTileId).toList() +
        '}';
  }
}
