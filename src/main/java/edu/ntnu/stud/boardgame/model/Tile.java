package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.action.TileAction;

public class Tile {

  private final int tileId;
  private final TileAction landAction;
  private final int row;
  private final int column;
  private Tile nextTile;

  public Tile(int tileId, TileAction landAction, int row, int column) {
    this.tileId = tileId;
    this.landAction = landAction;
    this.row = row;
    this.column = column;
  }

  public void landPlayer(Player player) {
    if (landAction != null) {
      landAction.perform(player);
    }
  }

  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public int getTileId() {
    return tileId;
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }
}
