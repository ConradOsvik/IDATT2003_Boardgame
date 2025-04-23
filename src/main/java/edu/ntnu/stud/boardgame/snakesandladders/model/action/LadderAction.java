package edu.ntnu.stud.boardgame.snakesandladders.model.action;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.model.action.TileAction;

public class LadderAction implements TileAction {
  private final Tile landingTile;

  public LadderAction(Tile landingTile) {
    this.landingTile = landingTile;
  }

  public void perform(Player player){
    player.placeOnTile(landingTile);
  }
}
