package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class GameRestartedEvent extends GameEvent {

  public GameRestartedEvent() {
    super(EventType.GAME_RESTARTED);
  }
}
