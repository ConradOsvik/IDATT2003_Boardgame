package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when the turn changes to a new player.
 * 
 * <p>
 * Contains the player whose turn is starting.
 * </p>
 */
public class TurnChangedEvent extends GameEvent {

  private final Player currentPlayer;

  /**
   * Creates a new turn changed event.
   *
   * @param currentPlayer the player whose turn is starting
   * @throws IllegalArgumentException if currentPlayer is null
   */
  public TurnChangedEvent(Player currentPlayer) {
    super(EventType.TURN_CHANGED);
    if (currentPlayer == null) {
      throw new IllegalArgumentException("CurrentPlayer cannot be null for TurnChangedEvent.");
    }
    this.currentPlayer = currentPlayer;
  }

  /**
   * Gets the player whose turn is starting.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
