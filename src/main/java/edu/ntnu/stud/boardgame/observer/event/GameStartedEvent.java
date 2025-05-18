package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class GameStartedEvent extends GameEvent {

  private final Player currentPlayer;

  public GameStartedEvent(Player currentPlayer) {
    super(EventType.GAME_STARTED);
    this.currentPlayer = currentPlayer;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}