package edu.ntnu.stud.boardgame.view.components;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

public interface GameBoardInterface {

  void setBoard(Board board);

  void updatePlayerPosition(Player player, Tile tile);

  void animatePlayerMove(Player player, Tile fromTile, Tile toTile);

  void clearPlayerPieces();

  void refreshBoard();
}