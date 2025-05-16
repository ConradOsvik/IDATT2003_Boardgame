package edu.ntnu.stud.boardgame.snakesandladders.view.components.board;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
import java.util.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class BoardRenderer {

  // Colors for board elements
  private static final Color SNAKE_COLOR = Color.RED;
  private static final Color LADDER_COLOR = Color.GREEN;
  private static final Color DEFAULT_TILE_COLOR = Color.YELLOW;
  private static final Color BOARD_BACKGROUND_COLOR = Color.WHITESMOKE;
  private static final Color SNAKE_TILE_COLOR = Color.LIGHTCORAL;
  private static final Color LADDER_TILE_COLOR = Color.LIGHTGREEN;
  private static final Color LAST_TILE_COLOR = Color.LIGHTBLUE;
  private static final Color START_TILE_COLOR = Color.LIGHTGREEN;

  // Styles for snake and ladder
  private static final int SNAKE_HEAD_RADIUS = 10;
  private static final int SNAKE_TAIL_RADIUS = 5;
  private static final double LADDER_SPACING = 8;

  // Minimum font size for tile labels
  private static final double MIN_FONT_SIZE = 9.0;
  private static final double MAX_FONT_SIZE = 16.0;

  public void render(Canvas canvas, SlBoard board, double cellSize, double padding) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Clear the canvas
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Draw board background
    gc.setFill(BOARD_BACKGROUND_COLOR);
    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Draw tiles
    drawTiles(gc, board, cellSize, padding);

    // Draw snakes and ladders
    drawSnakes(gc, board, cellSize, padding);
    drawLadders(gc, board, cellSize, padding);
  }

  private void drawTiles(GraphicsContext gc, SlBoard board, double cellSize, double padding) {
    int rows = board.getRows();
    int cols = board.getColumns();
    Map<Integer, Integer> snakes = board.getSnakes();
    Map<Integer, Integer> ladders = board.getLadders();

    // Calculate a proportional font size based on cell size
    double fontSize = Math.min(MAX_FONT_SIZE, Math.max(MIN_FONT_SIZE, cellSize / 4));
    gc.setFont(Font.font("Arial", FontWeight.NORMAL, fontSize));
    gc.setTextAlign(TextAlignment.CENTER);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        double x = col * cellSize + padding;
        double y = row * cellSize + padding;
        int tileId = BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols);

        // Choose tile color based on its properties
        boolean isSnake = snakes.containsKey(tileId) || snakes.containsValue(tileId);
        boolean isLadder = ladders.containsKey(tileId) || ladders.containsValue(tileId);
        boolean isStart = tileId == 1;
        boolean isEnd = tileId == board.getNumTiles();

        if (isEnd) {
          gc.setFill(LAST_TILE_COLOR);
        } else if (isStart) {
          gc.setFill(START_TILE_COLOR);
        } else if (isSnake) {
          gc.setFill(SNAKE_TILE_COLOR);
        } else if (isLadder) {
          gc.setFill(LADDER_TILE_COLOR);
        } else {
          gc.setFill(DEFAULT_TILE_COLOR);
        }

        // Draw tile rectangle
        gc.fillRect(x, y, cellSize, cellSize);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, cellSize, cellSize);

        // Draw the tile number
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(tileId), x + cellSize / 2, y + cellSize / 2 + fontSize / 3);
      }
    }
  }

  private void drawSnakes(GraphicsContext gc, SlBoard board, double cellSize, double padding) {
    for (Map.Entry<Integer, Integer> snake : board.getSnakes().entrySet()) {
      drawSnake(gc, snake.getKey(), snake.getValue(), board.getRows(), board.getColumns(), cellSize,
          padding);
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

    // Scale snake radius based on cell size
    double headRadius = Math.max(3, Math.min(SNAKE_HEAD_RADIUS, cellSize / 6));
    double tailRadius = Math.max(2, Math.min(SNAKE_TAIL_RADIUS, cellSize / 10));

    // Draw snake body
    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(Math.max(1, cellSize / 20));

    // Create control points for a curved snake path
    double controlX1 = (headX + tailX) / 2 + cellSize / 3;
    double controlY1 = (headY + tailY) / 2 - cellSize / 3;
    double controlX2 = (headX + tailX) / 2 - cellSize / 3;
    double controlY2 = (headY + tailY) / 2 + cellSize / 3;

    gc.beginPath();
    gc.moveTo(headX, headY);
    gc.bezierCurveTo(controlX1, controlY1, controlX2, controlY2, tailX, tailY);
    gc.stroke();

    // Draw snake head
    gc.setFill(SNAKE_COLOR);
    gc.fillOval(headX - headRadius, headY - headRadius, headRadius * 2, headRadius * 2);

    // Draw snake tail
    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(1);
    gc.strokeOval(tailX - tailRadius, tailY - tailRadius, tailRadius * 2, tailRadius * 2);

    // Add arrow pointing to tail
    drawArrow(gc, headX, headY, tailX, tailY, SNAKE_COLOR);
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

    // Scale ladder spacing based on cell size
    double ladderSpacing = Math.max(2, Math.min(LADDER_SPACING, cellSize / 7));

    gc.setStroke(LADDER_COLOR);
    gc.setLineWidth(Math.max(1, cellSize / 20));

    // Calculate ladder direction vector
    double dx = topX - bottomX;
    double dy = topY - bottomY;
    double length = Math.sqrt(dx * dx + dy * dy);
    double xNorm = dx / length;
    double yNorm = dy / length;

    // Draw the ladder sides (perpendicular to the direction vector)
    double leftBottomX = bottomX - yNorm * ladderSpacing;
    double leftBottomY = bottomY + xNorm * ladderSpacing;
    double leftTopX = topX - yNorm * ladderSpacing;
    double leftTopY = topY + xNorm * ladderSpacing;

    gc.beginPath();
    gc.moveTo(leftBottomX, leftBottomY);
    gc.lineTo(leftTopX, leftTopY);
    gc.stroke();

    double rightBottomX = bottomX + yNorm * ladderSpacing;
    double rightBottomY = bottomY - xNorm * ladderSpacing;
    double rightTopX = topX + yNorm * ladderSpacing;
    double rightTopY = topY - xNorm * ladderSpacing;

    gc.beginPath();
    gc.moveTo(rightBottomX, rightBottomY);
    gc.lineTo(rightTopX, rightTopY);
    gc.stroke();

    // Draw the ladder rungs
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

    // Add arrow pointing to top
    drawArrow(gc, bottomX, bottomY, topX, topY, LADDER_COLOR);
  }

  /**
   * Draws an arrow to indicate direction.
   */
  private void drawArrow(GraphicsContext gc, double startX, double startY, double endX, double endY,
      Color color) {
    // Calculate direction vector
    double dx = endX - startX;
    double dy = endY - startY;
    double length = Math.sqrt(dx * dx + dy * dy);

    // Normalize
    double dirX = dx / length;
    double dirY = dy / length;

    // Calculate perpendicular vector
    double perpX = -dirY;
    double perpY = dirX;

    // Calculate arrow position (halfway between start and end)
    double arrowX = startX + dx * 0.5;
    double arrowY = startY + dy * 0.5;

    // Arrow head size based on distance
    double arrowSize = Math.min(length / 6, 10);

    // Draw arrow head
    gc.setFill(color);
    double[] xPoints = {arrowX + dirX * arrowSize,
        arrowX - dirX * arrowSize + perpX * arrowSize * 0.7,
        arrowX - dirX * arrowSize - perpX * arrowSize * 0.7};

    double[] yPoints = {arrowY + dirY * arrowSize,
        arrowY - dirY * arrowSize + perpY * arrowSize * 0.7,
        arrowY - dirY * arrowSize - perpY * arrowSize * 0.7};

    gc.fillPolygon(xPoints, yPoints, 3);
  }
}