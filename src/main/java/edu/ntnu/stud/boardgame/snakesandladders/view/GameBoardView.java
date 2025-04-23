package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.snakesandladders.controller.GameBoardController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SLBoard;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View for the Snakes and Ladders game board. Renders the board, snakes, ladders, and player tokens.
 */
public class GameBoardView extends BorderPane implements BoardGameObserver {

  private static final int CELL_SIZE = 60;
  private static final int CANVAS_PADDING = 20;

  private final GameBoardController controller;
  private final Canvas canvas;
  private final Map<String, Color> playerColors;

  /**
   * Constructs a new game board view with the specified controller.
   *
   * @param controller The game board controller
   */
  public GameBoardView(GameBoardController controller) {
    this.controller = controller;
    this.playerColors = new HashMap<>();

    playerColors.put("Red", Color.RED);
    playerColors.put("Blue", Color.BLUE);
    playerColors.put("Green", Color.GREEN);
    playerColors.put("Orange", Color.ORANGE);
    playerColors.put("Purple", Color.PURPLE);

    SLBoard board = controller.getSLBoard();
    int rows = board != null ? board.getRows() : 10;
    int cols = board != null ? board.getColumns() : 10;

    int canvasWidth = cols * CELL_SIZE + 2 * CANVAS_PADDING;
    int canvasHeight = rows * CELL_SIZE + 2 * CANVAS_PADDING;

    canvas = new Canvas(canvasWidth, canvasHeight);

    setPadding(new Insets(10));
    setCenter(canvas);

    controller.getGame().addObserver(this);

    drawBoard();
  }

  /**
   * Draws the game board, including the grid, snakes, ladders, and player tokens.
   */
  public void drawBoard() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    SLBoard board = controller.getSLBoard();

    System.out.println("Drawing board with " + controller.getGame().getPlayers().size() + " players");

    if (board == null) {
      return;
    }

    int rows = board.getRows();
    int cols = board.getColumns();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        int tileId = getTileIdFromCoordinates(row, col, rows, cols);

        double x = col * CELL_SIZE + CANVAS_PADDING;
        double y = row * CELL_SIZE + CANVAS_PADDING;

        if ((row + col) % 2 == 0) {
          gc.setFill(Color.LIGHTGRAY);
        } else {
          gc.setFill(Color.WHITE);
        }

        gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, CELL_SIZE, CELL_SIZE);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gc.fillText(String.valueOf(tileId), x + 5, y + 15);
      }
    }

    Map<Integer, Integer> snakes = board.getSnakes();
    for (Map.Entry<Integer, Integer> snake : snakes.entrySet()) {
      drawSnake(gc, snake.getKey(), snake.getValue(), rows, cols);
    }

    Map<Integer, Integer> ladders = board.getLadders();
    for (Map.Entry<Integer, Integer> ladder : ladders.entrySet()) {
      drawLadder(gc, ladder.getKey(), ladder.getValue(), rows, cols);
    }

    List<Player> players = controller.getGame().getPlayers();
    for (Player player : players) {
      Tile currentTile = player.getCurrentTile();
      if (currentTile != null) {
        drawPlayerToken(gc, player, currentTile.getTileId(), rows, cols);
      }
    }
  }

  /**
   * Draws a snake from the head tile to the tail tile.
   *
   * @param gc The graphics context
   * @param headTileId The ID of the tile where the snake's head is
   * @param tailTileId The ID of the tile where the snake's tail is
   * @param rows The number of rows on the board
   * @param cols The number of columns on the board
   */
  private void drawSnake(GraphicsContext gc, int headTileId, int tailTileId, int rows, int cols) {
    int[] headCoords = getTileCoordinatesFromId(headTileId, rows, cols);
    int[] tailCoords = getTileCoordinatesFromId(tailTileId, rows, cols);

    double headX = headCoords[1] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;
    double headY = headCoords[0] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;
    double tailX = tailCoords[1] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;
    double tailY = tailCoords[0] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;

    gc.setStroke(Color.RED);
    gc.setLineWidth(3);

    double controlX1 = (headX + tailX) / 2 + 30;
    double controlY1 = (headY + tailY) / 2 - 30;
    double controlX2 = (headX + tailX) / 2 - 30;
    double controlY2 = (headY + tailY) / 2 + 30;

    gc.beginPath();
    gc.moveTo(headX, headY);
    gc.bezierCurveTo(controlX1, controlY1, controlX2, controlY2, tailX, tailY);
    gc.stroke();

    gc.setFill(Color.RED);
    gc.fillOval(headX - 10, headY - 10, 20, 20);

    gc.setStroke(Color.RED);
    gc.setLineWidth(1);
    gc.strokeOval(tailX - 5, tailY - 5, 10, 10);
  }

  /**
   * Draws a ladder from the bottom tile to the top tile.
   *
   * @param gc The graphics context
   * @param bottomTileId The ID of the tile at the bottom of the ladder
   * @param topTileId The ID of the tile at the top of the ladder
   * @param rows The number of rows on the board
   * @param cols The number of columns on the board
   */
  private void drawLadder(GraphicsContext gc, int bottomTileId, int topTileId, int rows, int cols) {
    int[] bottomCoords = getTileCoordinatesFromId(bottomTileId, rows, cols);
    int[] topCoords = getTileCoordinatesFromId(topTileId, rows, cols);

    double bottomX = bottomCoords[1] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;
    double bottomY = bottomCoords[0] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;
    double topX = topCoords[1] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;
    double topY = topCoords[0] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;

    gc.setStroke(Color.GREEN);
    gc.setLineWidth(3);

    double spacing = 8;
    double dx = topX - bottomX;
    double dy = topY - bottomY;
    double length = Math.sqrt(dx * dx + dy * dy);
    double xNorm = dx / length;
    double yNorm = dy / length;

    double leftBottomX = bottomX - yNorm * spacing;
    double leftBottomY = bottomY + xNorm * spacing;
    double leftTopX = topX - yNorm * spacing;
    double leftTopY = topY + xNorm * spacing;

    gc.beginPath();
    gc.moveTo(leftBottomX, leftBottomY);
    gc.lineTo(leftTopX, leftTopY);
    gc.stroke();

    double rightBottomX = bottomX + yNorm * spacing;
    double rightBottomY = bottomY - xNorm * spacing;
    double rightTopX = topX + yNorm * spacing;
    double rightTopY = topY - xNorm * spacing;

    gc.beginPath();
    gc.moveTo(rightBottomX, rightBottomY);
    gc.lineTo(rightTopX, rightTopY);
    gc.stroke();

    gc.setLineWidth(2);
    int numRungs = (int) (length / (CELL_SIZE / 2)) + 1;

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

  /**
   * Draws a player token on the specified tile.
   *
   * @param gc The graphics context
   * @param player The player
   * @param tileId The ID of the tile where the player is
   * @param rows The number of rows on the board
   * @param cols The number of columns on the board
   */
  private void drawPlayerToken(GraphicsContext gc, Player player, int tileId, int rows, int cols) {
    int[] coords = getTileCoordinatesFromId(tileId, rows, cols);

    System.out.println("Drawing player: " + player.getName() +
        " with token: " + player.getToken() +
        " at tile: " + tileId);

    double x = coords[1] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;
    double y = coords[0] * CELL_SIZE + CANVAS_PADDING + CELL_SIZE / 2;

    Color playerColor = playerColors.getOrDefault(player.getToken(), Color.BLACK);

    gc.setFill(playerColor);
    gc.fillOval(x - 10, y - 10, 20, 20);

    gc.setFill(Color.WHITE);
    gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    String initial = player.getName().substring(0, 1).toUpperCase();
    gc.fillText(initial, x - 4, y + 4);
  }

  /**
   * Converts tile coordinates (row, col) to a tile ID.
   *
   * @param row The row (0-based, from top to bottom)
   * @param col The column (0-based, from left to right)
   * @param rows The total number of rows
   * @param cols The total number of columns
   * @return The tile ID
   */
  private int getTileIdFromCoordinates(int row, int col, int rows, int cols) {
    row = rows - 1 - row;

    if (row % 2 == 0) {
      return row * cols + col + 1;
    } else {
      return row * cols + (cols - col);
    }
  }

  /**
   * Converts a tile ID to tile coordinates (row, col).
   *
   * @param tileId The tile ID
   * @param rows The total number of rows
   * @param cols The total number of columns
   * @return An array where [0] is the row and [1] is the column
   */
  private int[] getTileCoordinatesFromId(int tileId, int rows, int cols) {
    int index = tileId - 1;

    int row = index / cols;

    int col;
    if (row % 2 == 0) {
      col = index % cols;
    } else {
      col = cols - 1 - (index % cols);
    }

    row = rows - 1 - row;

    return new int[] {row, col};
  }

  @Override
  public void onGameEvent(GameEvent event) {
    switch (event.getEventType()) {
      case GAME_STARTED:
      case PLAYER_ADDED:
      case PLAYER_MOVED:
      case SNAKE_ENCOUNTERED:
      case LADDER_CLIMBED:
      case PLAYER_WON:
        drawBoard();
        break;
      default:
        break;
    }
  }
}