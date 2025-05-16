package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class PlayerMovedEvent extends GameEvent {

  protected final Player player;
  protected final Tile fromTile;
  protected final Tile toTile;
  protected final int steps;
  protected final Board board;

  public PlayerMovedEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(EventType.PLAYER_MOVED);
    this.player = player;
    this.fromTile = fromTile;
    this.toTile = toTile;
    this.steps = steps;
    this.board = board;
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
