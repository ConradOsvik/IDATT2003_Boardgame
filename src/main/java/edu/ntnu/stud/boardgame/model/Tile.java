package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.action.TileAction;

public class Tile {

  private final int tileId;
  private final TileAction landAction;
  private Tile nextTile;

  public Tile(int tileId, TileAction landAction) {
    this.tileId = tileId;
    this.landAction = landAction;
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
}
