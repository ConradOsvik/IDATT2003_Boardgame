package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class PlayerMovedEvent extends GameEvent {

  private final Player player;
  private final Tile fromTile;
  private final Tile toTile;
  private final int steps;
  private final Board board;

  public PlayerMovedEvent(EventType type, Player player, Tile fromTile, Tile toTile, int steps,
      Board board) {
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

  public PlayerMovedEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    this(EventType.PLAYER_MOVED, player, fromTile, toTile, steps, board);
  }

  public Player getPlayer() {
    return player;
  }

  public Tile getFromTile() {
    return fromTile;
  }

  public Tile getToTile() {
    return toTile;
  }

  public int getSteps() {
    return steps;
  }

  public Board getBoard() {
    return board;
  }
}