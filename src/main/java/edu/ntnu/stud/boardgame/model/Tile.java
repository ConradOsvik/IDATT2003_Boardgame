package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.action.TileAction;

public class Tile {

  private final int tileId;
  private final int row;
  private final int column;
  private TileAction landAction;
  private Tile nextTile;
  private String name;

  public Tile(int tileId, int row, int column) {
    this.tileId = tileId;
    this.row = row;
    this.column = column;
  }

  public void landPlayer(Player player) {
    if (landAction != null) {
      landAction.perform(player);
    }
  }

  public TileAction getLandAction() {
    return landAction;
  }

  public void setLandAction(TileAction action) {
    this.landAction = action;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
