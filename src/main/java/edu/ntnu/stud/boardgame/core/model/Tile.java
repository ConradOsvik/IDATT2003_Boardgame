package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.action.TileAction;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Tile extends BaseModel {

  private final int tileId;
  private TileAction landAction;
  private final Map<Direction, List<Tile>> connectedTiles;

  enum Direction {
    FORWARD,
    BACKWARD
  }

  Tile(int tileId) {
    this.tileId = tileId;
    this.connectedTiles = new EnumMap<>(Direction.class);

    this.connectedTiles.put(Direction.FORWARD, new ArrayList<>());
    this.connectedTiles.put(Direction.BACKWARD, new ArrayList<>());
  }

  public int getTileId() {
    return tileId;
  }

  void landPlayer(Player player) {
    requireNotNull(player, "Player cannot be null");

    if (landAction != null) {
      landAction.perform(player);
    }
  }

  void leavePlayer(Player player) {
    requireNotNull(player, "Player cannot be null");
    //TODO: Implement
  }

  void addNextTile(Tile nextTile) {
    requireNotNull(nextTile, "Next tile cannot be null");

    List<Tile> nextTiles = connectedTiles.get(Direction.FORWARD);
    if (!nextTiles.contains(nextTile)) {
      nextTiles.add(nextTile);
    }

    // We need to add this tile to the next tile's previous tiles
    List<Tile> prevTiles = nextTile.connectedTiles.get(Direction.BACKWARD);
    if (!prevTiles.contains(this)) {
      prevTiles.add(this);
    }
  }

  List<Tile> getConnectedTiles(Direction direction) {
    return connectedTiles.get(direction);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Tile tile = (Tile) obj;
    return tileId == tile.tileId;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tileId);
  }

  @Override
  public String toString() {
    return "Tile{" +
        "id=" + tileId +
        ", forwardConnectedTiles=" + connectedTiles.get(Direction.FORWARD).stream()
        .map(Tile::getTileId).toList() +
        ", backwardConnectedTiles=" + connectedTiles.get(Direction.BACKWARD).stream()
        .map(Tile::getTileId).toList() +
        '}';
  }
}
