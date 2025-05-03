package edu.ntnu.stud.boardgame.core.model;

import java.util.List;
import java.util.Map;

public abstract class Board extends BaseModel {
  protected Map<Integer, Tile> tiles;

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  public List<Tile> getTiles() {
    return List.copyOf(tiles.values());
  }

  public abstract void initializeBoard();
  public abstract Tile getStartingTile();
  public abstract boolean isLastTile(Tile tile);
}
