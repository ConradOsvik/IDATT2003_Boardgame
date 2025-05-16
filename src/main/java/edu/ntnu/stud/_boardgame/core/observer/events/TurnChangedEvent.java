package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class TurnChangedEvent extends GameEvent {

  protected final Player currentPlayer;

  public TurnChangedEvent(Player currentPlayer) {
    super(EventType.TURN_CHANGED);
    this.currentPlayer = currentPlayer;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
