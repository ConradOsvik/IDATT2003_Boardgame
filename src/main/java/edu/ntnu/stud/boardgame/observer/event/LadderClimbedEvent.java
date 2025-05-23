package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

/**
 * Event fired when a player climbs a ladder in Snakes and Ladders.
 *
 * <p>Extends PlayerMovedEvent to track the movement from the ladder's bottom to its top.
 */
public class LadderClimbedEvent extends PlayerMovedEvent {

  /**
   * Creates a new ladder climbed event.
   *
   * @param player the player climbing the ladder
   * @param fromTile the ladder's bottom tile
   * @param toTile the ladder's top tile
   * @param steps number of steps (usually 0 for ladder movement)
   * @param board current board state
   */
  public LadderClimbedEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(EventType.LADDER_CLIMBED, player, fromTile, toTile, steps, board);
  }
}
