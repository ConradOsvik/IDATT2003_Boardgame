package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class DiceRolledEvent extends GameEvent {

  private final int value;
  private final Player player;

  public DiceRolledEvent(int value, Player player) {
    super(EventType.DICE_ROLLED);
    this.value = value;
    this.player = player;
  }

  public int getValue() {
    return value;
  }

  public Player getPlayer() {
    return player;
  }
}