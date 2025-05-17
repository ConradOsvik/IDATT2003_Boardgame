package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class PlayerWonEvent extends GameEvent {

  private final Player winner;

  public PlayerWonEvent(Player winner) {
    super(EventType.PLAYER_WON);
    this.winner = winner;
  }

  public Player getWinner() {
    return winner;
  }
}