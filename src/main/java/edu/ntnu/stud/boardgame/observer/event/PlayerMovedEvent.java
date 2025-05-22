package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when a player moves on the board.
 *
 * <p>Contains details about the movement including source and destination tiles, number of steps
 * moved, and the current board state.
 */
public class PlayerMovedEvent extends GameEvent {

  private final Player player;
  private final Tile fromTile;
  private final Tile toTile;
  private final int steps;
  private final Board board;

  /**
   * Creates a new player moved event with a specific event type.
   *
   * @param type event type (for subclass events)
   * @param player the player that moved
   * @param fromTile starting tile
   * @param toTile destination tile
   * @param steps number of steps moved
   * @param board current board state
   * @throws IllegalArgumentException if any parameter except steps is null
   */
  public PlayerMovedEvent(
      EventType type, Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(type);
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for PlayerMovedEvent.");
    }
    if (fromTile == null) {
      throw new IllegalArgumentException("FromTile cannot be null for PlayerMovedEvent.");
    }
    if (toTile == null) {
      throw new IllegalArgumentException("ToTile cannot be null for PlayerMovedEvent.");
    }
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null for PlayerMovedEvent.");
    }
    this.player = player;
    this.fromTile = fromTile;
    this.toTile = toTile;
    this.steps = steps;
    this.board = board;
  }

  /**
   * Creates a new standard player moved event.
   *
   * @param player the player that moved
   * @param fromTile starting tile
   * @param toTile destination tile
   * @param steps number of steps moved
   * @param board current board state
   */
  public PlayerMovedEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    this(EventType.PLAYER_MOVED, player, fromTile, toTile, steps, board);
  }

  /**
   * Gets the player that moved.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the starting tile.
   *
   * @return the source tile
   */
  public Tile getFromTile() {
    return fromTile;
  }

  /**
   * Gets the destination tile.
   *
   * @return the destination tile
   */
  public Tile getToTile() {
    return toTile;
  }

  /**
   * Gets the number of steps moved.
   *
   * @return number of steps
   */
  public int getSteps() {
    return steps;
  }

  /**
   * Gets the current board state.
   *
   * @return the board
   */
  public Board getBoard() {
    return board;
  }
}
