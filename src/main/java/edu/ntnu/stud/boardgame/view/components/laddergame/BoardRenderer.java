package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * A utility class responsible for rendering the Snakes and Ladders game board using JavaFX Canvas.
 * This class handles the visual representation of the game board, including tiles, snakes, and
 * ladders.
 *
 * @see Canvas
 * @see Board
 */
public class BoardRenderer {

  private static final Color SNAKE_COLOR = Color.RED;
  private static final Color LADDER_COLOR = Color.GREEN;
  private static final Color DEFAULT_TILE_COLOR = Color.YELLOW;
  private static final Color BOARD_BACKGROUND_COLOR = Color.WHITESMOKE;
  private static final Color SNAKE_TILE_COLOR = Color.LIGHTCORAL;
  private static final Color LADDER_TILE_COLOR = Color.LIGHTGREEN;
  private static final Color GENERAL_ACTION_TILE_COLOR = Color.PURPLE;
  private static final Color LAST_TILE_COLOR = Color.ORANGE;

  private static final int SNAKE_HEAD_RADIUS = 10;
  private static final int SNAKE_TAIL_RADIUS = 5;
  private static final double LADDER_SPACING = 8;

  private static final double MIN_FONT_SIZE = 9.0;
  private static final double MAX_FONT_SIZE = 16.0;

  /**
   * Renders the game board on the provided canvas, including tiles, snakes, and ladders.
   *
   * @param canvas the canvas on which the board will be rendered
   * @param board the game board to be rendered
   * @param cellSize the size of each cell in the board
   * @param padding the padding between the cells and the canvas edges
   */
  public void render(Canvas canvas, Board board, double cellSize, double padding) {
    if (board == null || canvas.getWidth() <= 0 || canvas.getHeight() <= 0) {
      return;
    }

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    gc.setFill(BOARD_BACKGROUND_COLOR);
    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    drawTiles(gc, board, cellSize, padding);
    drawSnakesAndLadders(gc, board, cellSize, padding);
  }

  private void drawTiles(GraphicsContext gc, Board board, double cellSize, double padding) {
    double fontSize = Math.min(MAX_FONT_SIZE, Math.max(MIN_FONT_SIZE, cellSize / 4));
    gc.setFont(Font.font("Arial", FontWeight.NORMAL, fontSize));
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setTextBaseline(VPos.CENTER);

    for (int i = 1; i <= board.getEndTileId(); i++) {
      Tile tile = board.getTile(i);
      if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
        continue;
      }

      int row = tile.getRow();
      int col = tile.getColumn();

      double x = col * cellSize + padding;
      double y = row * cellSize + padding;

      boolean isSnake = tile.getLandAction() instanceof SnakeAction;
      boolean isLadder = tile.getLandAction() instanceof LadderAction;
      boolean isEnd = i == board.getEndTileId();
      boolean hasGeneralAction = tile.getLandAction() != null && !isSnake && !isLadder;

      if (isEnd) {
        gc.setFill(LAST_TILE_COLOR);
      } else if (isSnake) {
        gc.setFill(SNAKE_TILE_COLOR);
      } else if (isLadder) {
        gc.setFill(LADDER_TILE_COLOR);
      } else if (hasGeneralAction) {
        gc.setFill(GENERAL_ACTION_TILE_COLOR);
      } else {
        gc.setFill(DEFAULT_TILE_COLOR);
      }

      gc.fillRect(x, y, cellSize, cellSize);
      gc.setStroke(Color.BLACK);
      gc.strokeRect(x, y, cellSize, cellSize);

      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(i), x + cellSize / 2, y + cellSize / 2);
    }
  }

  private void drawSnakesAndLadders(
      GraphicsContext gc, Board board, double cellSize, double padding) {
    for (Tile tile : board.getTiles().values()) {
      if (tile.getLandAction() instanceof SnakeAction action) {
        drawSnake(gc, tile, action.getDestinationTile(), cellSize, padding);
      } else if (tile.getLandAction() instanceof LadderAction action) {
        drawLadder(gc, tile, action.getDestinationTile(), cellSize, padding);
      }
    }
  }

  private void drawSnake(
      GraphicsContext gc, Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null
        || toTile == null
        || fromTile.getRow() == null
        || fromTile.getColumn() == null
        || toTile.getRow() == null
        || toTile.getColumn() == null) {
      return;
    }

    double headX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double headY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double tailX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double tailY = toTile.getRow() * cellSize + padding + cellSize / 2;

    double headRadius = Math.max(3, Math.min(SNAKE_HEAD_RADIUS, cellSize / 6));
    double tailRadius = Math.max(2, Math.min(SNAKE_TAIL_RADIUS, cellSize / 10));

    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(Math.max(3, cellSize / 15));

    double controlX1 = (headX + tailX) / 2 + cellSize;
    double controlY1 = (headY + tailY) / 2;
    double controlX2 = (headX + tailX) / 2 - cellSize;
    double controlY2 = (headY + tailY) / 2;

    gc.beginPath();
    gc.moveTo(headX, headY);
    gc.bezierCurveTo(controlX1, controlY1, controlX2, controlY2, tailX, tailY);
    gc.stroke();

    gc.setFill(SNAKE_COLOR);
    gc.fillOval(headX - headRadius, headY - headRadius, headRadius * 2, headRadius * 2);

    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(1);
    gc.strokeOval(tailX - tailRadius, tailY - tailRadius, tailRadius * 2, tailRadius * 2);
  }

  private void drawLadder(
      GraphicsContext gc, Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null
        || toTile == null
        || fromTile.getRow() == null
        || fromTile.getColumn() == null
        || toTile.getRow() == null
        || toTile.getColumn() == null) {
      return;
    }

    double bottomX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double bottomY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double topX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double topY = toTile.getRow() * cellSize + padding + cellSize / 2;

    double ladderSpacing = Math.max(2, Math.min(LADDER_SPACING, cellSize / 7));

    gc.setStroke(LADDER_COLOR);
    gc.setLineWidth(Math.max(1, cellSize / 20));

    double dx = topX - bottomX;
    double dy = topY - bottomY;
    double length = Math.sqrt(dx * dx + dy * dy);
    double xnorm = dx / length;
    double ynorm = dy / length;

    double leftBottomX = bottomX - ynorm * ladderSpacing;
    double leftBottomY = bottomY + xnorm * ladderSpacing;
    double leftTopX = topX - ynorm * ladderSpacing;
    double leftTopY = topY + xnorm * ladderSpacing;

    gc.beginPath();
    gc.moveTo(leftBottomX, leftBottomY);
    gc.lineTo(leftTopX, leftTopY);
    gc.stroke();

    double rightBottomX = bottomX + ynorm * ladderSpacing;
    double rightBottomY = bottomY - xnorm * ladderSpacing;
    double rightTopX = topX + ynorm * ladderSpacing;
    double rightTopY = topY - xnorm * ladderSpacing;

    gc.beginPath();
    gc.moveTo(rightBottomX, rightBottomY);
    gc.lineTo(rightTopX, rightTopY);
    gc.stroke();

    gc.setLineWidth(Math.max(1, cellSize / 30));
    int numRungs = Math.max(2, (int) (length / (cellSize / 2)));

    for (int i = 0; i < numRungs; i++) {
      double t = i / (double) (numRungs - 1);
      double rungBottomX = leftBottomX * (1 - t) + leftTopX * t;
      double rungBottomY = leftBottomY * (1 - t) + leftTopY * t;
      double rungTopX = rightBottomX * (1 - t) + rightTopX * t;
      double rungTopY = rightBottomY * (1 - t) + rightTopY * t;

      gc.beginPath();
      gc.moveTo(rungBottomX, rungBottomY);
      gc.lineTo(rungTopX, rungTopY);
      gc.stroke();
    }
  }
}
