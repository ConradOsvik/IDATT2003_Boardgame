package edu.ntnu.stud.boardgame.core.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Board extends BaseModel {

  private final String name;
  private final String description;
  protected Map<Integer, Tile> tiles;

  public Board(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  public List<Tile> getTiles() {
    return List.copyOf(tiles.values());
  }

  public abstract void initializeBoard();

  public abstract Tile getStartingTile();

  public abstract boolean isLastTile(Tile tile);

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Board{");
    for (Tile tile : tiles.values()) {
      sb.append(tile).append(", ");
    }
    sb.append("}");
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Board board = (Board) obj;
    return Objects.equals(tiles, board.tiles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tiles);
  }
}
