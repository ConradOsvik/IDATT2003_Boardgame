package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;

public class SkipTurnAction implements TileAction {

  @Override
  public void perform(Player player) {
    player.setSkipNextTurn(true);
  }
}
