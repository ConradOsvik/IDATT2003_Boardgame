package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class PlayerWonEvent extends GameEvent {

  protected final Player winner;

  public PlayerWonEvent(Player winner) {
    super(EventType.PLAYER_WON);
    this.winner = winner;
  }

  public Player getWinner() {
    return winner;
  }
}
