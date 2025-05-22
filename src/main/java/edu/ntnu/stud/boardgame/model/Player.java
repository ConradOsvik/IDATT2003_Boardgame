package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.enums.PieceType;

/**
 * Represents a player in the game.
 *
 * <p>Tracks the player's position, piece type, and turn-skipping status.
 */
public class Player {

  private final String name;
  private final PieceType piece;
  private Tile currentTile;
  private boolean skipNextTurn = false;

  /**
   * Creates a new player.
   *
   * @param name player's name
   * @param piece player's piece type
   * @throws IllegalArgumentException if name is null/empty or piece is null
   */
  public Player(String name, PieceType piece) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Player name cannot be null or empty.");
    }
    if (piece == null) {
      throw new IllegalArgumentException("Player piece type cannot be null.");
    }
    this.name = name;
    this.piece = piece;
  }

  /**
   * Places the player on a tile and triggers any tile actions.
   *
   * @param tile tile to place the player on
   * @throws IllegalArgumentException if tile is null
   */
  public void placeOnTile(Tile tile) {
    if (tile == null) {
      throw new IllegalArgumentException("Cannot place player on a null tile.");
    }
    this.currentTile = tile;
    tile.landPlayer(this);
  }

  /**
   * Gets the destination tile after moving a number of steps.
   *
   * @param steps number of steps to move
   * @return the destination tile
   * @throws IllegalStateException if player is not on a tile
   * @throws IllegalArgumentException if steps is negative
   */
  public Tile getDestinationTile(int steps) {
    if (currentTile == null) {
      throw new IllegalStateException("Player is not currently on any tile.");
    }
    if (steps < 0) {
      throw new IllegalArgumentException("Steps cannot be negative");
    }

    Tile targetTile = currentTile;

    for (int i = 0; i < steps; i++) {
      Tile nextTile = targetTile.getNextTile();
      if (nextTile == null) {
        break;
      }
      targetTile = nextTile;
    }

    return targetTile;
  }

  /**
   * Moves the player a number of steps.
   *
   * @param steps number of steps to move
   */
  public void move(int steps) {
    Tile targetTile = getDestinationTile(steps);
    placeOnTile(targetTile);
  }

  /**
   * Sets whether the player should skip their next turn.
   *
   * @param skipNextTurn true to skip next turn
   */
  public void setSkipNextTurn(boolean skipNextTurn) {
    this.skipNextTurn = skipNextTurn;
  }

  /**
   * Checks if the player should skip their next turn.
   *
   * @return true if the player should skip next turn, false otherwise
   */
  public boolean shouldSkipNextTurn() {
    return skipNextTurn;
  }

  public String getName() {
    return name;
  }

  public PieceType getPiece() {
    return piece;
  }

  public Tile getCurrentTile() {
    return currentTile;
  }

  /**
   * Sets the player's current tile without triggering tile actions.
   *
   * @param tile tile to set
   * @throws IllegalArgumentException if tile is null
   */
  public void setCurrentTile(Tile tile) {
    if (tile == null) {
      throw new IllegalArgumentException("Cannot set current tile to null.");
    }
    this.currentTile = tile;
  }
}
