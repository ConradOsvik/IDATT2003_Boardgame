package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class GameEndedEvent extends GameEvent {

  private final Player winner;

  public GameEndedEvent(Player winner) {
    super(EventType.GAME_ENDED);
    this.winner = winner;
  }

  public Player getWinner() {
    return winner;
  }
}