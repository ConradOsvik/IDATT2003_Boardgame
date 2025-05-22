package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when dice are rolled during a player's turn.
 * 
 * <p>
 * Contains the dice roll value and the player who rolled.
 * </p>
 */
public class DiceRolledEvent extends GameEvent {

  private final int diceValue;
  private final Player currentPlayer;

  /**
   * Creates a new dice rolled event.
   *
   * @param diceValue     the value rolled on the dice
   * @param currentPlayer the player who rolled
   * @throws IllegalArgumentException if currentPlayer is null
   */
  public DiceRolledEvent(int diceValue, Player currentPlayer) {
    super(EventType.DICE_ROLLED);
    if (currentPlayer == null) {
      throw new IllegalArgumentException("CurrentPlayer cannot be null for DiceRolledEvent.");
    }
    this.diceValue = diceValue;
    this.currentPlayer = currentPlayer;
  }

  /**
   * Gets the dice roll value.
   *
   * @return the value rolled
   */
  public int getDiceValue() {
    return diceValue;
  }

  /**
   * Gets the player who rolled.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}