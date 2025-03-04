package edu.ntnu.stud.boardgame.core.model;

import java.util.List;

public class Player extends BaseModel {

  private final BoardGame boardGame;
  private final String name;
  private Tile currentTile;

  public Player(String name, BoardGame boardGame) {
    this.name = name;
    this.boardGame = boardGame;
  }

  void placeOnTile(Tile tile) {
    requireNotNull(tile, "Tile cannot be null");

    if (currentTile != null) {
      currentTile.leavePlayer(this);
    }

    currentTile = tile;
    tile.landPlayer(this);
  }

  void move(Tile.Direction direction, int steps) {
    if (steps <= 0 || currentTile == null) {
      return;
    }

    Tile targetTile = currentTile;
    for (int i = 0; i < steps && targetTile != null; i++) {
      List<Tile> nextTiles = targetTile.getConnectedTiles(direction);

      if (nextTiles.isEmpty()) {
        break;
      }

      if (nextTiles.size() > 1) {
        // TODO: Implement logic to handle multiple connected tiles
      }
      targetTile = nextTiles.getFirst();

      if (targetTile.getTileId() == 90) {
        break;
      }
    }

    if (targetTile != null) {
      placeOnTile(targetTile);
    }
  }

  public Tile getCurrentTile() {
    return currentTile;
  }

  public String getName() {
    return name;
  }
}
