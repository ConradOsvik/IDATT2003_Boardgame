package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.AbstractGameBoard;

/**
 * Represents the game board for the Snakes and Ladders game. Extends
 * {@link AbstractGameBoard} to
 * provide specific rendering and animation functionality for the Snakes and
 * Ladders game, including
 * ladder climbs and snake slides.
 *
 * @see AbstractGameBoard
 * @see BoardRenderer
 */
public class LadderGameBoard extends AbstractGameBoard {

  private final BoardRenderer boardRenderer;

  /**
   * Creates a new Snakes and Ladders game board with a {@link BoardRenderer}.
   */
  public LadderGameBoard() {
    super();
    this.boardRenderer = new BoardRenderer();
  }

  /**
   * Calculates the size of each board cell and padding based on canvas
   * dimensions.
   * Updates all piece positions after calculation.
   */
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

    padding = Math.min(
        (boardCanvas.getWidth() - (cellSize * cols)) / 2,
        (boardCanvas.getHeight() - (cellSize * rows)) / 2);

    updateAllPiecePositions();
  }

  /**
   * Renders the game board using the {@link BoardRenderer} if redraw is needed.
   */
  @Override
  protected void drawBoard() {
    if (board == null
        || boardCanvas.getWidth() <= 0
        || boardCanvas.getHeight() <= 0
        || !needsRedraw) {
      return;
    }

    boardRenderer.render(boardCanvas, board, cellSize, padding);
    needsRedraw = false;
  }

  /**
   * Animates a player climbing up a ladder.
   *
   * @param player   the {@link Player} to animate
   * @param fromTile the starting {@link Tile}
   * @param toTile   the destination {@link Tile} at the top of the ladder
   */
  public void animatePlayerLadderClimb(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      return;
    }

    pieceAnimation.animateLadderClimb(
        playerPieces.get(player), fromTile, toTile, cellSize, padding);
  }

  /**
   * Animates a player sliding down a snake.
   *
   * @param player   the {@link Player} to animate
   * @param fromTile the starting {@link Tile}
   * @param toTile   the destination {@link Tile} at the tail of the snake
   */
  public void animatePlayerSnakeSlide(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      return;
    }

    pieceAnimation.animateSnakeSlide(playerPieces.get(player), fromTile, toTile, cellSize, padding);
  }

  /**
   * Animates a player bouncing back when landing on an invalid tile.
   *
   * @param player   the {@link Player} to animate
   * @param fromTile the starting {@link Tile}
   * @param toTile   the {@link Tile} to bounce back to
   */
  public void animatePlayerBounceBack(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      return;
    }

    pieceAnimation.animateBounceBack(playerPieces.get(player), fromTile, toTile, cellSize, padding);
  }
}
