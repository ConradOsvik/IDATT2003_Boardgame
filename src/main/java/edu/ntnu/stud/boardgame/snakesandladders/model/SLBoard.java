package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.Tile;

public class SLBoard extends Board {

  @Override
  public void initializeBoard() {

  }

  @Override
  public Tile getStartingTile() {
    return null;
  }

  @Override
  public boolean isLastTile(Tile tile) {
    return false;
  }
}
