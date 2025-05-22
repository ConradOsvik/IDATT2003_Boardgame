package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when a player is added to the game.
 * 
 * <p>
 * Contains the newly added player instance.
 * </p>
 */
public class PlayerAddedEvent extends GameEvent {

  private final Player player;

  /**
   * Creates a new player added event.
   *
   * @param player the player that was added
   * @throws IllegalArgumentException if player is null
   */
  public PlayerAddedEvent(Player player) {
    super(EventType.PLAYER_ADDED);
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for PlayerAddedEvent.");
    }
    this.player = player;
  }

  /**
   * Gets the added player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }
}
