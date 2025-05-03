package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class GameResetEvent extends GameEvent {

  protected final Board board;

  public GameResetEvent(Board board) {
    super(EventType.GAME_RESET);
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }
}
