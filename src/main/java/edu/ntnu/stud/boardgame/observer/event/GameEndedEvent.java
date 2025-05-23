package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when a game ends.
 *
 * <p>Contains the winning player, which may be null in case of a draw or if no player won.
 */
public class GameEndedEvent extends GameEvent {

  private final Player winner;

  /**
   * Creates a new game ended event.
   *
   * @param winner the winning player, or null if no winner
   */
  public GameEndedEvent(Player winner) {
    super(EventType.GAME_ENDED);
    this.winner = winner;
  }

  /**
   * Gets the winning player.
   *
   * @return the winner, or null if no winner
   */
  public Player getWinner() {
    return winner;
  }
}
