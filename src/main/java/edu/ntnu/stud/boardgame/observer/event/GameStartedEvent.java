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
    if (currentPlayer == null) {
      throw new IllegalArgumentException("CurrentPlayer cannot be null for GameStartedEvent.");
    }
    if (players == null) {
      throw new IllegalArgumentException("Players list cannot be null for GameStartedEvent.");
    }
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null for GameStartedEvent.");
    }
    this.currentPlayer = currentPlayer;
    this.players = players; // Consider defensive copy
    this.board = board;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public List<Player> getPlayers() {
    return players; // Consider defensive copy
  }

  public Board getBoard() {
    return board;
  }
}