package edu.ntnu.stud.boardgame.core.model;

import java.util.HashMap;
import java.util.Map;

class Board extends BaseModel {

  private final Map<Integer, Tile> tiles;

  Board() {
    this.tiles = new HashMap<>();
  }

  void addTile(Tile tile) {
    requireNotNull(tile, "Tile cannot be null");
    tiles.put(tile.getTileId(), tile);
  }

  Tile getTile(int tileId) {
    return tiles.get(tileId);
  }
}
