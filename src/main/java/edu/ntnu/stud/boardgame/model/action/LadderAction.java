package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

public class LadderAction implements TileAction {

  private final Tile destinationTile;

  public LadderAction(Tile destinationTile) {
    if (destinationTile == null) {
      throw new IllegalArgumentException("Destination tile cannot be null");
    }

    this.destinationTile = destinationTile;
  }

  public void perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    player.placeOnTile(destinationTile);
  }

  public Tile getDestinationTile() {
    return destinationTile;
  }
}
