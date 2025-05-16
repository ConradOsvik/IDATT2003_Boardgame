package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class GameEndedEvent extends GameEvent {

  protected final Player winner;

  public GameEndedEvent(Player winner) {
    super(EventType.GAME_ENDED);
    this.winner = winner;
  }

  public Player getWinner() {
    return winner;
  }
}
