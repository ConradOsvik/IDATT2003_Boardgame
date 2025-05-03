package edu.ntnu.stud.boardgame.snakesandladders.events;

import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;

public class SlPlayerMovedEvent extends PlayerMovedEvent {

  private boolean isSnakeMove, isLadderMove;

  public SlPlayerMovedEvent(SlPlayer player, Tile fromTile, Tile toTile, int steps, SlBoard board) {
    super(player, fromTile, toTile, steps, board);

  }

  public SlPlayerMovedEvent(SlPlayer player, Tile fromTile, Tile toTile, int steps, SlBoard board,
      boolean isSnakeMove, boolean isLadderMove) {
    super(player, fromTile, toTile, steps, board);
    this.isSnakeMove = isSnakeMove;
    this.isLadderMove = isLadderMove;
  }

  @Override
  public SlPlayer getPlayer() {
    return (SlPlayer) super.getPlayer();
  }

  @Override
  public SlBoard getBoard() {
    return (SlBoard) super.getBoard();
  }

  public boolean isSnakeMove() {
    return isSnakeMove;
  }

  public boolean isLadderMove() {
    return isLadderMove;
  }
}
