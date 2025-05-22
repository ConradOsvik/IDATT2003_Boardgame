package edu.ntnu.stud.boardgame.observer;

public abstract class GameEvent {

  private final EventType type;

  protected GameEvent(EventType type) {
    if (type == null) {
      throw new IllegalArgumentException("EventType cannot be null for GameEvent.");
    }
    this.type = type;
  }

  public EventType getEventType() {
    return type;
  }

  public enum EventType {
    GAME_CREATED, GAME_STARTED, GAME_ENDED,

    TURN_CHANGED, DICE_ROLLED,

    PLAYER_ADDED, PLAYER_MOVED, PLAYER_WON,

    MONEY_TRANSFER, PROPERTY_PURCHASED, PLAYER_BANKRUPT,

    LADDER_CLIMBED, SNAKE_ENCOUNTERED, BOUNCE_BACK
  }
}