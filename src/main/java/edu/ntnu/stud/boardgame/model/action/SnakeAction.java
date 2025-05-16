package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

public class SnakeAction implements TileAction {

  private Tile destinationTile;

  public SnakeAction(Tile destinationTile) {
    this.destinationTile = destinationTile;
  }

  public void perform(Player player) {
    player.placeOnTile(destinationTile);
  }

  public Tile getDestinationTile() {
    return destinationTile;
  }
}
