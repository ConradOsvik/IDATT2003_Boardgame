package edu.ntnu.stud.boardgame.observer;

/**
 * Base class for all game events in the observer system.
 * 
 * <p>
 * Provides common functionality for event type identification and
 * validation. All specific game events should extend this class.
 * </p>
 */
public abstract class GameEvent {

  private final EventType type;

  /**
   * Creates a new game event with the specified type.
   *
   * @param type the type of event
   * @throws IllegalArgumentException if type is null
   */
  protected GameEvent(EventType type) {
    if (type == null) {
      throw new IllegalArgumentException("EventType cannot be null for GameEvent.");
    }
    this.type = type;
  }

  /**
   * Gets the type of this event.
   *
   * @return the event type
   */
  public EventType getEventType() {
    return type;
  }

  /**
   * Enumeration of all possible game event types.
   */
  public enum EventType {
    /** Event when a new game is created */
    GAME_CREATED,
    /** Event when a game starts */
    GAME_STARTED,
    /** Event when a game ends */
    GAME_ENDED,

    /** Event when the turn changes to a new player */
    TURN_CHANGED,
    /** Event when dice are rolled */
    DICE_ROLLED,

    /** Event when a player is added to the game */
    PLAYER_ADDED,
    /** Event when a player moves to a new tile */
    PLAYER_MOVED,
    /** Event when a player wins the game */
    PLAYER_WON,

    /** Event when money is transferred between players or bank */
    MONEY_TRANSFER,
    /** Event when a property is purchased */
    PROPERTY_PURCHASED,
    /** Event when a player goes bankrupt */
    PLAYER_BANKRUPT,

    /** Event when a player climbs a ladder */
    LADDER_CLIMBED,
    /** Event when a player encounters a snake */
    SNAKE_ENCOUNTERED,
    /** Event when a player bounces back from the end tile */
    BOUNCE_BACK
  }
}