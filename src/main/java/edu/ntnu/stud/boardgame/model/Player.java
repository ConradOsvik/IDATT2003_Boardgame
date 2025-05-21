package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.enums.PieceType;

public class Player {

  private final String name;
  private final PieceType piece;
  private Tile currentTile;
  private boolean skipNextTurn = false;

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

  public void placeOnTile(Tile tile) {
    if (tile == null) {
      throw new IllegalArgumentException("Cannot place player on a null tile.");
    }
    this.currentTile = tile;
    tile.landPlayer(this);
  }

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

  public void move(int steps) {
    Tile targetTile = getDestinationTile(steps);
    placeOnTile(targetTile);
  }

  public void setSkipNextTurn(boolean skipNextTurn) {
    this.skipNextTurn = skipNextTurn;
  }

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

  public void setCurrentTile(Tile tile) {
    if (tile == null) {
      throw new IllegalArgumentException("Cannot set current tile to null.");
      // Or, if null is a valid state to mean "off board", then this check might be
      // different
      // For now, assume it's not valid.
    }
    this.currentTile = tile;
  }
}
