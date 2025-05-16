package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class DiceRolledEvent extends GameEvent {

  protected final int value;

  public DiceRolledEvent(int value) {
    super(EventType.DICE_ROLLED);
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
