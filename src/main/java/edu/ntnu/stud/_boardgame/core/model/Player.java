package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.exception.InvalidMoveException;
import java.util.Objects;

/**
 * Represents a player in the board game. Players have a name and token, can be placed on tiles, and
 * can move between connected tiles.
 */
public abstract class Player extends BaseModel {

  /**
   * The name of this player.
   */
  private final String name;

  private final int tokenId;

  /**
   * The tile this player is currently on, or null if not placed on any tile.
   */
  private Tile currentTile;

  /**
   * Constructs a new player with the specified name and token.
   *
   * @param name The name of the player
   */
  public Player(String name, int tokenId) {
    requireNotEmpty(name, "Name cannot be empty");
    this.name = name;
    this.tokenId = tokenId;
  }

  public void setStartingTile(Tile tile) {
    requireNotNull(tile, "Tile cannot be null");

    this.currentTile = tile;
  }

  /**
   * Moves the player by the specified number of steps. The player will follow the connected tiles.
   * If there are multiple connected tiles, the player will take the first one. Movement stops if
   * there are no more connected tiles, if the player reaches tile 90, or when all steps have been
   * taken.
   *
   * @param steps The number of steps to move
   */
  public void move(int steps) {
    if (steps < 0) {
      throw new InvalidMoveException("Cannot move a negative number of steps");
    }

    if (currentTile == null) {
      throw new InvalidMoveException("Player is not on any tile");
    }

    if (steps == 0) {
      return;
    }

    Tile targetTile = getTargetTile(steps);

    if (targetTile != null) {
      placeOnTile(targetTile);
    }

  }

  protected Tile getTargetTile(int steps) {
    Tile targetTile = currentTile;

    for (int i = 0; i < steps; i++) {
      if (targetTile.getNextTile() == null) {
        break;
      }

      targetTile = targetTile.getNextTile();
    }

    return targetTile;
  }

  /**
   * Places this player on the specified tile. If the player is already on a tile, they will leave
   * that tile before landing on the new one.
   *
   * @param tile The tile to place this player on
   * @throws IllegalArgumentException if the tile is null
   */
  public void placeOnTile(Tile tile) {
    requireNotNull(tile, "Tile cannot be null");

    Tile previousTile = currentTile;

    if (previousTile != null) {
      previousTile.leavePlayer(this);
    }

    this.currentTile = tile;
    tile.landPlayer(this);
  }

  /**
   * Gets the tile this player is currently on.
   *
   * @return The current tile, or null if the player is not on any tile
   */
  public Tile getCurrentTile() {
    return currentTile;
  }

  /**
   * Sets the current tile of this player. This method is used for testing purposes only.
   *
   * @param tile The tile to set as the current tile
   */
  public void setCurrentTile(Tile tile) {
    this.currentTile = tile;

    if (tile != null) {
      tile.landPlayer(this);
    }
  }

  /**
   * Gets the name of this player.
   *
   * @return The player's name
   */
  public String getName() {
    return name;
  }

  public int getTokenId() {
    return tokenId;
  }

  @Override
  public String toString() {
    return "Player{name='" + name + "', currentTile=" + (currentTile != null
        ? currentTile.getTileId() : "none") + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Player player = (Player) obj;
    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}