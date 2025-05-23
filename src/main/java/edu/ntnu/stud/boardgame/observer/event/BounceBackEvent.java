package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

/**
 * Event fired when a player bounces back from the end tile in Snakes and Ladders.
 *
 * <p>Extends PlayerMovedEvent to track the movement from the end tile back to the bounced position.
 */
public class BounceBackEvent extends PlayerMovedEvent {

  /**
   * Creates a new bounce back event.
   *
   * @param player the player bouncing back
   * @param fromTile the end tile
   * @param toTile the bounced-to tile
   * @param steps number of steps (usually 0 for bounce movement)
   * @param board current board state
   */
  public BounceBackEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(EventType.BOUNCE_BACK, player, fromTile, toTile, steps, board);
  }
}
