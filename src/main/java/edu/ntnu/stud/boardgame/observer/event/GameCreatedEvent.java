package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import java.util.List;

public class GameCreatedEvent extends GameEvent {

  private final Board board;
  private final List<Player> players;

  public GameCreatedEvent(Board board, List<Player> players) {
    super(EventType.GAME_CREATED);
    this.board = board;
    this.players = players;
  }

  public Board getBoard() {
    return board;
  }

  public List<Player> getPlayers() {
    return players;
  }
}