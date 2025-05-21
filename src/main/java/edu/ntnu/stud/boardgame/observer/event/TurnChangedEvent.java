package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class TurnChangedEvent extends GameEvent {

  private final Player currentPlayer;

  public TurnChangedEvent(Player currentPlayer) {
    super(EventType.TURN_CHANGED);
    if (currentPlayer == null) {
      throw new IllegalArgumentException("CurrentPlayer cannot be null for TurnChangedEvent.");
    }
    this.currentPlayer = currentPlayer;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
