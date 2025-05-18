package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class TurnChangedEvent extends GameEvent {

  private Player currentPlayer;

  public TurnChangedEvent(Player currentPlayer) {
    super(EventType.TURN_CHANGED);
    this.currentPlayer = currentPlayer;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
