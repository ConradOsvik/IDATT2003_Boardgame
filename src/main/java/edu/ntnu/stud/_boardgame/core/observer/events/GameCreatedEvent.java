package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class GameCreatedEvent extends GameEvent {

  protected final Board board;

  public GameCreatedEvent(Board board) {
    super(EventType.GAME_CREATED);
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }
}
