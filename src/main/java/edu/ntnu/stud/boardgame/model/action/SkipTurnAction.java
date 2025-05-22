package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;

/**
 * Represents a tile action that causes a player to skip their next turn.
 */
public class SkipTurnAction implements TileAction {

  @Override
  public void perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for SkipTurnAction.");
    }
    player.setSkipNextTurn(true);
  }
}
