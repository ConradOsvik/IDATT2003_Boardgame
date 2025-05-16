package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.observer.GameEvent;

public class GameStartedEvent extends GameEvent {

  public GameStartedEvent() {
    super(EventType.GAME_STARTED);
  }
}
