package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.model.action.TileAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a tile on the game board. Tiles have a unique ID, can have connected tiles,
 * and can trigger actions when players land on them.
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
   * The tiles connected to this tile (can be accessed by a player moving from this tile).
   */
  private final Map<Integer, Tile> connectedTiles;

  /**
   * Constructs a new tile with the specified ID. The tile will initially have no connections or
   * landing action.
   *
   * @param tileId The unique identifier for this tile
   */
  public Tile(int tileId) {
    this.tileId = tileId;
    this.connectedTiles = new HashMap<>();
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
   * Sets the action that occurs when a player lands on this tile.
   *
   * @param landAction The action to set, or null to remove the action
   */
  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }

  /**
   * Gets the action that occurs when a player lands on this tile.
   *
   * @return The landing action, or null if no action is set
   */
  public TileAction getLandAction() {
    return landAction;
  }

  /**
   * Called when a player lands on this tile. Performs the landing action if one is set.
   *
   * @param player The player who landed on this tile
   * @throws IllegalArgumentException if the player is null
   */
  public void landPlayer(Player player) {
    requireNotNull(player, "Player cannot be null");

    if (landAction != null) {
      landAction.perform(player);
    }
  }

  /**
   * Called when a player leaves this tile.
   *
   * @param player The player who is leaving this tile
   * @throws IllegalArgumentException if the player is null
   */
  public void leavePlayer(Player player) {
    requireNotNull(player, "Player cannot be null");
    // Currently just a placeholder
  }

  /**
   * Adds a tile as a connection.
   *
   * @param tile The tile to connect to this one
   * @throws IllegalArgumentException if the tile is null
   */
  public void addConnectedTile(Tile tile) {
    requireNotNull(tile, "Tile cannot be null");
    connectedTiles.put(tile.getTileId(), tile);
  }

  /**
   * Gets the list of tiles connected to this tile.
   *
   * @return A list of connected tiles
   */
  public List<Tile> getConnectedTiles() {
    return new ArrayList<>(connectedTiles.values());
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
    return Objects.hash(tileId);
  }

  /**
   * Returns a string representation of this tile, including its ID and connected tiles.
   *
   * @return A string representation of this tile
   */
  @Override
  public String toString() {
    return "Tile{" +
        "id=" + tileId +
        ", connectedTiles=" + connectedTiles.keySet() +
        '}';
  }
}