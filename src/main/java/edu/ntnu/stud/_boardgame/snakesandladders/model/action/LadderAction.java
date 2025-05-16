package edu.ntnu.stud.boardgame.snakesandladders.model.action;

import edu.ntnu.stud.boardgame.core.model.BaseModel;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.model.action.TileAction;

/**
 * Action that occurs when a player lands at the bottom of a ladder. The player climbs up to the
 * ladder's top.
 */
public class LadderAction extends BaseModel implements TileAction {

  private final Tile destinationTile;

  /**
   * Constructs a new ladder action.
   *
   * @param destinationTile The tile at the ladder's destination (top)
   */
  public LadderAction(Tile destinationTile) {
    requireNotNull(destinationTile, "Destination tile cannot be null");
    this.destinationTile = destinationTile;
  }

  /**
   * Performs the action of climbing up the ladder. The player is moved to the top tile.
   *
   * @param player The player who landed at the bottom of the ladder
   */
  public void perform(Player player) {
    requireNotNull(player, "Player cannot be null");
    player.placeOnTile(destinationTile);
  }

  public Tile getDestinationTile() {
    return destinationTile;
  }
}
