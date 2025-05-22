package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.action.TileAction;

/**
 * Represents a tile on the game board.
 *
 * <p>Manages tile properties, actions, and connections to other tiles.
 */
public class Tile {

  private final int tileId;
  private Integer row;
  private Integer column;
  private TileAction landAction;
  private Tile nextTile;
  private String name;

  /**
   * Creates a new tile with the specified ID.
   *
   * @param tileId unique identifier for the tile
   */
  public Tile(int tileId) {
    this.tileId = tileId;
  }

  /**
   * Executes the tile's action when a player lands on it.
   *
   * @param player the player landing on the tile
   * @throws IllegalArgumentException if player is null
   */
  public void landPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null when landing on tile.");
    }
    if (landAction != null) {
      landAction.perform(player);
    }
  }

  /**
   * Gets the action associated with this tile.
   *
   * @return the tile action, or null if none
   */
  public TileAction getLandAction() {
    return landAction;
  }

  /**
   * Sets the action to be performed when landing on this tile.
   *
   * @param action the action to set
   */
  public void setLandAction(TileAction action) {
    this.landAction = action;
  }

  /**
   * Gets the next tile in sequence.
   *
   * @return the next tile, or null if this is the last tile
   */
  public Tile getNextTile() {
    return nextTile;
  }

  /**
   * Sets the next tile in sequence.
   *
   * @param nextTile the next tile
   */
  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public int getTileId() {
    return tileId;
  }

  public Integer getRow() {
    return row;
  }

  public void setRow(Integer row) {
    this.row = row;
  }

  public Integer getColumn() {
    return column;
  }

  public void setColumn(Integer column) {
    this.column = column;
  }

  public String getName() {
    return name;
  }

  /**
   * Sets the display name of the tile.
   *
   * @param name the name to set
   * @throws IllegalArgumentException if name is null
   */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Tile name cannot be null.");
    }
    this.name = name;
  }
}
