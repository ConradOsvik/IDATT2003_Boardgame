package edu.ntnu.stud.boardgame.snakesandladders.model.action;

import edu.ntnu.stud.boardgame.core.model.BaseModel;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.model.action.TileAction;

/**
 * Action that occurs when a player lands on a snake's head. The player slides down to the snake's
 * tail.
 */
public class SnakeAction extends BaseModel implements TileAction {

  private final Tile tailTile;

  /**
   * Constructs a new snake action.
   *
   * @param tailTile The tile at the snake's tail (destination)
   */
  public SnakeAction(Tile tailTile) {
    requireNotNull(tailTile, "Tail tile cannot be null");
    this.tailTile = tailTile;
  }

  /**
   * Performs the action of sliding down the snake. The player is moved to the tail tile.
   *
   * @param player The player who landed on the snake's head
   */
  @Override
  public void perform(Player player) {
    requireNotNull(player, "Player cannot be null");
    player.placeOnTile(tailTile);
  }
}