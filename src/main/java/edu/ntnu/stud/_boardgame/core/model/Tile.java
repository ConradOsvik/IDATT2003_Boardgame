package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.model.action.TileAction;
import java.util.Objects;

/**
 * Represents a tile on the game board. Tiles have a unique ID, can have connected tiles, and can
 * trigger actions when players land on them.
 */
public class Tile extends BaseModel {

  private final int tileId;
  private Tile nextTile;
  private TileAction landAction;

  /**
   * Constructs a new tile with the specified ID. The tile will initially have no connections or
   * landing action.
   *
   * @param tileId The unique identifier for this tile
   */
  public Tile(int tileId) {
    this.tileId = tileId;
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
   * Sets the action that occurs when a player lands on this tile.
   *
   * @param landAction The action to set, or null to remove the action
   */
  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
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

  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Tile{")
        .append("id=").append(tileId);

    if (landAction != null) {
      builder.append(", action=").append(landAction.getClass().getSimpleName());
    }

    if (nextTile != null) {
      builder.append(", nextTile=").append(nextTile.getTileId());
    }

    builder.append("}");
    return builder.toString();
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
}