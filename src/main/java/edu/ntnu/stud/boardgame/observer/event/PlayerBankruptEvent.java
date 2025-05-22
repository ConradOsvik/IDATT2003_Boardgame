package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when a player goes bankrupt in Monopoly.
 * 
 * <p>
 * Contains the player who has gone bankrupt.
 * </p>
 */
public class PlayerBankruptEvent extends GameEvent {

  private final Player player;

  /**
   * Creates a new player bankrupt event.
   *
   * @param player the player who went bankrupt
   * @throws IllegalArgumentException if player is null
   */
  public PlayerBankruptEvent(Player player) {
    super(EventType.PLAYER_BANKRUPT);
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for PlayerBankruptEvent.");
    }
    this.player = player;
  }

  /**
   * Gets the bankrupt player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }
}