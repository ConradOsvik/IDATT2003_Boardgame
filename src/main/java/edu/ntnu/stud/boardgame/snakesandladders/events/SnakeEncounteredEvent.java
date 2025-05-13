package edu.ntnu.stud.boardgame.snakesandladders.events;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;

public class SnakeEncounteredEvent extends PlayerMovedEvent {

  public SnakeEncounteredEvent(Player player, Tile fromTile, Tile toTile, int steps, Board board) {
    super(player, fromTile, toTile, steps, board);
  }
}
