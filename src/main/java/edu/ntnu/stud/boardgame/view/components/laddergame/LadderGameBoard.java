package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.AbstractGameBoard;

/**
 * Represents the game board for the Snakes and Ladders game.
 * Extends {@link AbstractGameBoard} to provide specific rendering and animation
 * functionality for the Snakes and Ladders game, including ladder climbs and
 * snake slides.
 * 
 * @see AbstractGameBoard
 * @see BoardRenderer
 */
public class LadderGameBoard extends AbstractGameBoard {

  private final BoardRenderer boardRenderer;

  public LadderGameBoard() {
    super();
    this.boardRenderer = new BoardRenderer();
  }

  @Override
  protected void calculateCellSize() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0) {
      return;
    }

    int rows = board.getRows();
    int cols = board.getColumns();

    double maxCellWidth = (boardCanvas.getWidth() - 40) / cols;
    double maxCellHeight = (boardCanvas.getHeight() - 40) / rows;

    cellSize = Math.min(maxCellWidth, maxCellHeight);

    padding = Math.min((boardCanvas.getWidth() - (cellSize * cols)) / 2,
        (boardCanvas.getHeight() - (cellSize * rows)) / 2);

    updateAllPiecePositions();
  }

  @Override
  protected void drawBoard() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0
        || !needsRedraw) {
      return;
    }

    boardRenderer.render(boardCanvas, board, cellSize, padding);
    needsRedraw = false;
  }

  public void animatePlayerLadderClimb(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      return;
    }

    pieceAnimation.animateLadderClimb(playerPieces.get(player), fromTile, toTile, cellSize,
        padding);
  }

  public void animatePlayerSnakeSlide(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      return;
    }

    pieceAnimation.animateSnakeSlide(playerPieces.get(player), fromTile, toTile, cellSize, padding);
  }

  public void animatePlayerBounceBack(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      return;
    }

    pieceAnimation.animateBounceBack(playerPieces.get(player), fromTile, toTile, cellSize, padding);
  }
}