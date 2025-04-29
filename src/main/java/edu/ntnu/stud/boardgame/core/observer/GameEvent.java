package edu.ntnu.stud.boardgame.core.observer;

import java.util.HashMap;
import java.util.Map;

public class GameEvent {

  public enum EventType {
    GAME_CREATED,
    GAME_RESET,
    GAME_STARTED,
    GAME_ENDED,
    PLAYER_ADDED,
    PLAYER_MOVED,
    DICE_ROLLED,
    SNAKE_ENCOUNTERED,
    LADDER_CLIMBED,
    TURN_CHANGED,
    PLAYER_WON,
  }

  private final EventType eventType;
  private final Map<String, Object> data;

  public GameEvent(EventType eventType) {
    this.eventType = eventType;
    this.data = new HashMap<>();
  }

  public EventType getEventType() {
    return eventType;
  }

  public void addData(String key, Object value) {
    data.put(key, value);
  }

  public Object getData(String key) {
    return data.get(key);
  }
}