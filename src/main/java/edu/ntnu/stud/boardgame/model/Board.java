package edu.ntnu.stud.boardgame.model;

import java.util.HashMap;
import java.util.Map;

public class Board {

  private final Map<Integer, Tile> tiles;
  private final int rows;
  private final int columns;
  private final String name;
  private final String description;

  public Board(String name, String description, int rows, int columns) {
    this.tiles = new HashMap<>();
    this.rows = rows;
    this.columns = columns;
    this.name = name;
    this.description = description;
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

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
