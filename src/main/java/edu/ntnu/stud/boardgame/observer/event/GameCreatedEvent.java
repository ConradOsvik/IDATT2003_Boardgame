package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class GameCreatedEvent extends GameEvent {

  private final Board board;

  public GameCreatedEvent(Board board) {
    super(EventType.GAME_CREATED);
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }
}