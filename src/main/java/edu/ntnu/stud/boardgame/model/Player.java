package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.enums.PieceType;

public class Player {

  private final String name;
  private final PieceType piece;
  private Tile currentTile;
  private boolean skipNextTurn = false;

  public Player(String name, PieceType piece) {
    this.name = name;
    this.piece = piece;
  }

  public void placeOnTile(Tile tile) {
    this.currentTile = tile;
  }

  public void move(int steps) {
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
  }

  public void setSkipNextTurn(boolean skipNextTurn) {
    this.skipNextTurn = skipNextTurn;
  }

  public boolean shouldSkipNextTurn() {
    return skipNextTurn;
  }
}
