package edu.ntnu.stud.boardgame.snakesandladders.view.components.board;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
import java.util.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoardRenderer {

  private static final Color SNAKE_COLOR = Color.RED;
  private static final Color LADDER_COLOR = Color.GREEN;
  private static final Color DEFAULT_TILE_COLOR = Color.YELLOW;
  private static final Color SNAKE_TILE_COLOR = Color.LIGHTCORAL;
  private static final Color LADDER_TILE_COLOR = Color.LIGHTGREEN;
  private static final int SNAKE_HEAD_RADIUS = 10;
  private static final int SNAKE_TAIL_RADIUS = 5;
  private static final double LADDER_SPACING = 8;

  public BoardRenderer() {
  }

  public void render(Canvas canvas, SlBoard board, double cellSize, double padding) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    drawTiles(gc, board, cellSize, padding);
    drawSnakes(gc, board, cellSize, padding);
    drawLadders(gc, board, cellSize, padding);
  }

  private void drawTiles(GraphicsContext gc, SlBoard board, double cellSize, double padding) {
    int rows = board.getRows();
    int cols = board.getColumns();
    Map<Integer, Integer> snakes = board.getSnakes();
    Map<Integer, Integer> ladders = board.getLadders();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        double x = col * cellSize + padding;
        double y = row * cellSize + padding;
        int tileId = BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols);

        boolean isSnake = snakes.containsKey(tileId) || snakes.containsValue(tileId);
        boolean isLadder = ladders.containsKey(tileId) || ladders.containsValue(tileId);

        if (isSnake) {
          gc.setFill(SNAKE_TILE_COLOR);
        } else if (isLadder) {
          gc.setFill(LADDER_TILE_COLOR);
        } else {
          gc.setFill(DEFAULT_TILE_COLOR);
        }

        gc.fillRect(x, y, cellSize, cellSize);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, cellSize, cellSize);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gc.fillText(String.valueOf(tileId), x + 5, y + 15);
      }
    }
  }

  private void drawSnakes(GraphicsContext gc, SlBoard board, double cellSize, double padding) {
    for (Map.Entry<Integer, Integer> snake : board.getSnakes().entrySet()) {
      drawSnake(gc, snake.getKey(), snake.getValue(), board.getRows(), board.getColumns(),
          cellSize, padding);
    }
  }

  private void drawLadders(GraphicsContext gc, SlBoard board, double cellSize, double padding) {
    for (Map.Entry<Integer, Integer> ladder : board.getLadders().entrySet()) {
      drawLadder(gc, ladder.getKey(), ladder.getValue(), board.getRows(), board.getColumns(),
          cellSize, padding);
    }
  }

  private void drawSnake(GraphicsContext gc, int headTileId, int tailTileId, int rows, int cols,
      double cellSize, double padding) {
    int[] headCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(headTileId, rows,
        cols);
    int[] tailCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(tailTileId, rows,
        cols);

    double headX = headCoords[1] * cellSize + padding + cellSize / 2;
    double headY = headCoords[0] * cellSize + padding + cellSize / 2;
    double tailX = tailCoords[1] * cellSize + padding + cellSize / 2;
    double tailY = tailCoords[0] * cellSize + padding + cellSize / 2;

    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(3);

    double controlX1 = (headX + tailX) / 2 + 30;
    double controlY1 = (headY + tailY) / 2 - 30;
    double controlX2 = (headX + tailX) / 2 - 30;
    double controlY2 = (headY + tailY) / 2 + 30;

    gc.beginPath();
    gc.moveTo(headX, headY);
    gc.bezierCurveTo(controlX1, controlY1, controlX2, controlY2, tailX, tailY);
    gc.stroke();

    gc.setFill(SNAKE_COLOR);
    gc.fillOval(headX - SNAKE_HEAD_RADIUS, headY - SNAKE_HEAD_RADIUS,
        SNAKE_HEAD_RADIUS * 2, SNAKE_HEAD_RADIUS * 2);

    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(1);
    gc.strokeOval(tailX - SNAKE_TAIL_RADIUS, tailY - SNAKE_TAIL_RADIUS,
        SNAKE_TAIL_RADIUS * 2, SNAKE_TAIL_RADIUS * 2);
  }

  private void drawLadder(GraphicsContext gc, int bottomTileId, int topTileId, int rows, int cols,
      double cellSize, double padding) {
    int[] bottomCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(bottomTileId, rows,
        cols);
    int[] topCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(topTileId, rows,
        cols);

    double bottomX = bottomCoords[1] * cellSize + padding + cellSize / 2;
    double bottomY = bottomCoords[0] * cellSize + padding + cellSize / 2;
    double topX = topCoords[1] * cellSize + padding + cellSize / 2;
    double topY = topCoords[0] * cellSize + padding + cellSize / 2;

    gc.setStroke(LADDER_COLOR);
    gc.setLineWidth(3);

    double dx = topX - bottomX;
    double dy = topY - bottomY;
    double length = Math.sqrt(dx * dx + dy * dy);
    double xNorm = dx / length;
    double yNorm = dy / length;

    double leftBottomX = bottomX - yNorm * LADDER_SPACING;
    double leftBottomY = bottomY + xNorm * LADDER_SPACING;
    double leftTopX = topX - yNorm * LADDER_SPACING;
    double leftTopY = topY + xNorm * LADDER_SPACING;

    gc.beginPath();
    gc.moveTo(leftBottomX, leftBottomY);
    gc.lineTo(leftTopX, leftTopY);
    gc.stroke();

    double rightBottomX = bottomX + yNorm * LADDER_SPACING;
    double rightBottomY = bottomY - xNorm * LADDER_SPACING;
    double rightTopX = topX + yNorm * LADDER_SPACING;
    double rightTopY = topY - xNorm * LADDER_SPACING;

    gc.beginPath();
    gc.moveTo(rightBottomX, rightBottomY);
    gc.lineTo(rightTopX, rightTopY);
    gc.stroke();

    gc.setLineWidth(2);
    int numRungs = (int) (length / (cellSize / 2)) + 1;

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