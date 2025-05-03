package edu.ntnu.stud.boardgame.snakesandladders.events;

import edu.ntnu.stud.boardgame.core.observer.events.TurnChangedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;

public class SlTurnChangedEvent extends TurnChangedEvent {

  public SlTurnChangedEvent(SlPlayer currentPlayer) {
    super(currentPlayer);
  }

  @Override
  public SlPlayer getCurrentPlayer() {
    return (SlPlayer) super.getCurrentPlayer();
  }
}
