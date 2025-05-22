package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import java.util.List;

/**
 * Event fired when a game starts.
 *
 * <p>Contains the initial game state including the current player, all players, and the board
 * configuration.
 */
public class GameStartedEvent extends GameEvent {

  private final Player currentPlayer;
  private final List<Player> players;
  private final Board board;

  /**
   * Creates a new game started event.
   *
   * @param currentPlayer the starting player
   * @param players list of all players
   * @param board the game board
   * @throws IllegalArgumentException if any parameter is null
   */
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
    this.players = players;
    this.board = board;
  }

  /**
   * Gets the starting player.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Gets the list of all players.
   *
   * @return list of players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Gets the game board.
   *
   * @return the board
   */
  public Board getBoard() {
    return board;
  }
}
