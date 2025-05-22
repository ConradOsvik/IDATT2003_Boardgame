package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import java.util.List;

/**
 * Event fired when a new game is created.
 * 
 * <p>
 * Contains the initial game state including the board and player list.
 * </p>
 */
public class GameCreatedEvent extends GameEvent {

  private final Board board;
  private final List<Player> players;

  /**
   * Creates a new game created event.
   *
   * @param board   the game board
   * @param players list of players
   * @throws IllegalArgumentException if board or players is null
   */
  public GameCreatedEvent(Board board, List<Player> players) {
    super(EventType.GAME_CREATED);
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null for GameCreatedEvent.");
    }
    if (players == null) {
      throw new IllegalArgumentException("Players list cannot be null for GameCreatedEvent.");
    }
    this.board = board;
    this.players = players;
  }

  /**
   * Gets the game board.
   *
   * @return the board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Gets the list of players.
   *
   * @return list of players
   */
  public List<Player> getPlayers() {
    return players;
  }
}