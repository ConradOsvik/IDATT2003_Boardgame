package edu.ntnu.stud.boardgame.core.observer;

public abstract class GameEvent {

  public enum EventType {
    GAME_CREATED,
    GAME_RESET,
    GAME_STARTED,
    GAME_RESTARTED,
    GAME_ENDED,

    TURN_CHANGED,
    DICE_ROLLED,

    PLAYER_ADDED,
    PLAYER_MOVED,
    PLAYER_WON,
  }

  private final EventType type;

  protected GameEvent(EventType type) {
    this.type = type;
  }

  public EventType getEventType() {
    return type;
  }
}
