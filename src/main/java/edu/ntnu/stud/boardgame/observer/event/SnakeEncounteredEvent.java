package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

/**
 * Event fired when a player lands on a snake in Snakes and Ladders.
 *
 * <p>Extends PlayerMovedEvent to track the movement from the snake's head to its tail.
 */
public class SnakeEncounteredEvent extends PlayerMovedEvent {

  /**
   * Creates a new snake encountered event.
   *
   * @param player the player encountering the snake
   * @param fromTile the snake's head tile
   * @param toTile the snake's tail tile
   * @param steps number of steps (usually 0 for snake movement)
   * @param board current board state
   */
  public SnakeEncounteredEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(EventType.SNAKE_ENCOUNTERED, player, fromTile, toTile, steps, board);
  }
}
