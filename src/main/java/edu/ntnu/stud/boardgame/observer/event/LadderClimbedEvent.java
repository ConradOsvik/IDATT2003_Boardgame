package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

public class LadderClimbedEvent extends PlayerMovedEvent {

  public LadderClimbedEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(EventType.LADDER_CLIMBED, player, fromTile, toTile, steps, board);
  }
}