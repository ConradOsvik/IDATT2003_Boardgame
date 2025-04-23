package edu.ntnu.stud.boardgame.snakesandladders.model.action;

import edu.ntnu.stud.boardgame.core.model.action.TileAction;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;

/**
 * Action that occurs when a player lands on a snake's head.
 * The player slides down to the snake's tail.
 */
public class SnakeAction implements TileAction {

  private final Tile tailTile;

  /**
   * Constructs a new snake action.
   *
   * @param tailTile The tile at the snake's tail (destination)
   */
  public SnakeAction(Tile tailTile) {
    this.tailTile = tailTile;
  }

  /**
   * Performs the action of sliding down the snake.
   * The player is moved to the tail tile.
   *
   * @param player The player who landed on the snake's head
   */
  @Override
  public void perform(Player player) {
    player.placeOnTile(tailTile);
  }
}