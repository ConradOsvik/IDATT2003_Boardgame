package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.action.TileAction;

public class Tile {

  private final int tileId;
  private Integer row;
  private Integer column;
  private TileAction landAction;
  private Tile nextTile;
  private String name;

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public void landPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null when landing on tile.");
    }
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

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Tile name cannot be null.");
    }
    this.name = name;
  }
}
