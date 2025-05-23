package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;

/** Interface for actions that can be performed when a player lands on a tile. */
public interface TileAction {

  /**
   * Performs the tile action for the given player.
   *
   * @param player the player performing the action
   * @throws IllegalArgumentException if player is null
   */
  void perform(Player player);
}
