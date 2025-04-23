package edu.ntnu.stud.boardgame.core.model;

import java.util.List;

/**
 * Represents a player in the board game. Players have a name and token, can be placed on tiles, and can move
 * between connected tiles.
 */
public class Player extends BaseModel {

  /**
   * The name of this player.
   */
  private final String name;

  /**
   * The token/piece this player uses on the board.
   */
  private final String token;

  /**
   * The tile this player is currently on, or null if not placed on any tile.
   */
  private Tile currentTile;

  /**
   * Constructs a new player with the specified name and token.
   *
   * @param name  The name of the player
   * @param token The token/piece this player uses
   */
  public Player(String name, String token) {
    this.name = name;
    this.token = token;
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

    if (currentTile != null) {
      currentTile.leavePlayer(this);
    }

    currentTile = tile;
    tile.landPlayer(this);
  }

  // TODO: Use and javadoc this method for branching games
  public void move(List<Integer> steps) {
    if(steps == null || steps.isEmpty() || currentTile == null) {
      return;
    }

    Tile targetTile = currentTile;
    for (int step : steps) {
      List<Tile> nextTiles = targetTile.getConnectedTiles();

      if (nextTiles.isEmpty()) {
        break;
      }

      targetTile = nextTiles.get(step);

      if (targetTile.getTileId() == 90) {
        break;
      }
    }

    placeOnTile(targetTile);
  }

  /**
   * Moves the player by the specified number of steps. The player will
   * follow the connected tiles. If there are multiple connected tiles, the
   * player will take the first one. Movement stops if there are no more connected tiles,
   * if the player reaches tile 90, or when all steps have been taken.
   *
   * @param steps The number of steps to move
   */
  public void move(int steps) {
    if (steps <= 0 || currentTile == null) {
      return;
    }

    Tile targetTile = currentTile;
    for (int i = 0; i < steps; i++) {
      List<Tile> nextTiles = targetTile.getConnectedTiles();

      if (nextTiles.isEmpty()) {
        break;
      }

      targetTile = nextTiles.getFirst();

      if (targetTile.getTileId() == 100) {
        break;
      }
    }

    placeOnTile(targetTile);
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
   * Gets the name of this player.
   *
   * @return The player's name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the token/piece this player uses.
   *
   * @return The player's token
   */
  public String getToken() {
    return token;
  }
}