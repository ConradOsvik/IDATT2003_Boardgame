package edu.ntnu.stud.boardgame.observer;

public abstract class GameEvent {

  private final EventType type;

  protected GameEvent(EventType type) {
    this.type = type;
  }

  public EventType getType() {
    return type;
  }

  public enum EventType {
    GAME_STARTED,
    GAME_OVER,
    PLAYER_ADDED,
    PLAYER_MOVED,
    TURN_CHANGED,
    DICE_ROLLED
  }
}