package edu.ntnu.stud.boardgame.core.model.action;

import edu.ntnu.stud.boardgame.core.model.Player;

/**
 * Interface for actions that can be performed when a player lands / leaves on a tile.
 */
public interface TileAction {

  /**
   * Performs the action on the specified player. This method is called when a player lands / leaves
   * a tile with this action.
   *
   * @param player The player who landed on the tile with this action
   */
  void perform(Player player);
}
