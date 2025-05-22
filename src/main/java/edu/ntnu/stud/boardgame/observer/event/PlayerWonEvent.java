package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when a player wins the game.
 * 
 * <p>
 * Contains the winning player instance.
 * </p>
 */
public class PlayerWonEvent extends GameEvent {

  private final Player winner;

  /**
   * Creates a new player won event.
   *
   * @param winner the winning player
   */
  public PlayerWonEvent(Player winner) {
    super(EventType.PLAYER_WON);
    this.winner = winner;
  }

  /**
   * Gets the winning player.
   *
   * @return the winner
   */
  public Player getWinner() {
    return winner;
  }
}