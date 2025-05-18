package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import java.util.List;

public class GameStartedEvent extends GameEvent {

  private final Player currentPlayer;
  private final List<Player> players;
  private final Board board;

  public GameStartedEvent(Player currentPlayer, List<Player> players, Board board) {
    super(EventType.GAME_STARTED);
    this.currentPlayer = currentPlayer;
    this.players = players;
    this.board = board;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Board getBoard() {
    return board;
  }
}