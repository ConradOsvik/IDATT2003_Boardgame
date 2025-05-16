package edu.ntnu.stud.boardgame.model;

import java.util.HashMap;
import java.util.Map;

public class Board {

  private final Map<Integer, Tile> tiles;
  private final int rows;
  private final int columns;

  public Board(int rows, int columns) {
    this.tiles = new HashMap<>();
    this.rows = rows;
    this.columns = columns;
  }

  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  public Map<Integer, Tile> getTiles() {
    return tiles;
  }
}
