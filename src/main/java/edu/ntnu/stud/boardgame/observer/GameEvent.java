package edu.ntnu.stud.boardgame.observer;

public abstract class GameEvent {

  private final EventType type;

  protected GameEvent(EventType type) {
    this.type = type;
  }

  public EventType getEventType() {
    return type;
  }

  public enum EventType {
    GAME_CREATED, GAME_STARTED, GAME_ENDED, GAME_RESTARTED,

    TURN_CHANGED, DICE_ROLLED,

    PLAYER_ADDED, PLAYER_MOVED, PLAYER_WON
  }
}