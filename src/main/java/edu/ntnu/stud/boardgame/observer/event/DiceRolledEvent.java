package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class DiceRolledEvent extends GameEvent {

  private final int diceValue;
  private final Player currentPlayer;

  public DiceRolledEvent(int diceValue, Player currentPlayer) {
    super(EventType.DICE_ROLLED);
    if (currentPlayer == null) {
      throw new IllegalArgumentException("CurrentPlayer cannot be null for DiceRolledEvent.");
    }
    this.diceValue = diceValue;
    this.currentPlayer = currentPlayer;
  }

  public int getDiceValue() {
    return diceValue;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}