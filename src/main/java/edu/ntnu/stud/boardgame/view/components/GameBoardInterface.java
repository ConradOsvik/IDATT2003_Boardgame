package edu.ntnu.stud.boardgame.view.components;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

/**
 * Interface defining the core functionality required for game board implementations. Provides
 * methods for managing the game board state and player movements.
 *
 * @see AbstractGameBoard
 * @see Board
 */
public interface GameBoardInterface {

  /**
   * Sets the game board model to be displayed.
   *
   * @param board The board model to use
   */
  void setBoard(Board board);

  /**
   * Updates a player's position to a specific tile.
   *
   * @param player The player to update
   * @param tile The destination tile
   */
  void updatePlayerPosition(Player player, Tile tile);

  /**
   * Animates a player's movement between tiles.
   *
   * @param player The player to move
   * @param fromTile The starting tile
   * @param toTile The destination tile
   */
  void animatePlayerMove(Player player, Tile fromTile, Tile toTile);

  /** Removes all player pieces from the board. */
  void clearPlayerPieces();

  /** Forces a redraw of the game board. */
  void refreshBoard();
}
