package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.observer.GameEvent;

public class TurnChangedEvent extends GameEvent {

  public TurnChangedEvent() {
    super(EventType.TURN_CHANGED);
  }
}
