package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

public class BounceBackEvent extends PlayerMovedEvent {

  public BounceBackEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(player, fromTile, toTile, steps, board);
  }
}