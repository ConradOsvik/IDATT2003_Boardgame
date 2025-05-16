package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

public class LadderAction implements TileAction {

  private final Tile destinationTile;

  public LadderAction(Tile destinationTile) {
    this.destinationTile = destinationTile;
  }

  public void perform(Player player) {
    player.placeOnTile(destinationTile);
  }

  public Tile getDestinationTile() {
    return destinationTile;
  }
}
