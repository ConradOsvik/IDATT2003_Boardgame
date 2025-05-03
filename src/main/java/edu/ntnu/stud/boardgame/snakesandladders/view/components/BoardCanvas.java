package edu.ntnu.stud.boardgame.snakesandladders.view.components;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.events.SlPlayerMovedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
import java.util.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoardCanvas extends GameComponent<StackPane> {

  public static final int CELL_SIZE = 60;
  public static final int PADDING = 20;
  private static final int CANVAS_PREF_WIDTH = SlBoard.BOARD_COLUMNS * CELL_SIZE;
  private static final int CANVAS_PREF_HEIGHT = SlBoard.BOARD_ROWS * CELL_SIZE;

  private final Canvas canvas;
  private final TokenManager tokenManager;

  private SlBoard board;
  private double currentCellSize;
  private double currentPadding;

  public BoardCanvas(SlGameController controller) {
    super(controller, new StackPane());
    this.canvas = new Canvas(CANVAS_PREF_WIDTH, CANVAS_PREF_HEIGHT);
    this.tokenManager = new TokenManager();

    this.getNode().setMinWidth(0);
    this.getNode().setMinHeight(0);

    this.getNode().getChildren().addAll(canvas, tokenManager);

    bindCanvasSize();

    tokenManager.prefWidthProperty().bind(this.getNode().widthProperty());
    tokenManager.prefHeightProperty().bind(this.getNode().heightProperty());

    calculateScaleFactors();
    draw();
  }

  private void bindCanvasSize() {
    canvas.widthProperty().bind(this.getNode().widthProperty());
    canvas.heightProperty().bind(this.getNode().heightProperty());

    canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
      calculateScaleFactors();
      draw();
    });

    canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
      calculateScaleFactors();
      draw();
    });
  }

  private void calculateScaleFactors() {
    if (canvas.getWidth() <= 0 || canvas.getHeight() <= 0) {
      return;
    }

    double baseWidth = SlBoard.BOARD_COLUMNS * CELL_SIZE + 2 * PADDING;
    double baseHeight = SlBoard.BOARD_ROWS * CELL_SIZE + 2 * PADDING;

    double scaleX = canvas.getWidth() / baseWidth;
    double scaleY = canvas.getHeight() / baseHeight;

    double scaleFactor = Math.min(scaleX, scaleY);

    currentCellSize = CELL_SIZE * scaleFactor;
    currentPadding = PADDING * scaleFactor;
  }

  private void draw() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    if (board == null) {
      return;
    }

    int rows = board.getRows();
    int cols = board.getColumns();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    Map<Integer, Integer> snakes = board.getSnakes();
    Map<Integer, Integer> ladders = board.getLadders();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        double x = col * currentCellSize + currentPadding;
        double y = row * currentCellSize + currentPadding;

        boolean isSnake = snakes.containsKey(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols))
            || snakes.containsValue(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols));
        boolean isLadder = ladders.containsKey(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols))
            || ladders.containsValue(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols));

        if (isSnake) {
          gc.setFill(Color.LIGHTCORAL);
        } else if (isLadder) {
          gc.setFill(Color.LIGHTGREEN);
        } else {
          gc.setFill(Color.YELLOW);
        }

        gc.fillRect(x, y, currentCellSize, currentCellSize);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, currentCellSize, currentCellSize);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12)); //TODO: use Inter font
        gc.fillText(String.valueOf(
                BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols)),
            x + 5,
            y + 15);
      }
    }

    for (Map.Entry<Integer, Integer> snake : snakes.entrySet()) {
      drawSnake(gc, snake.getKey(), snake.getValue(), rows, cols);
    }

    for (Map.Entry<Integer, Integer> ladder : ladders.entrySet()) {
      drawLadder(gc, ladder.getKey(), ladder.getValue(), rows, cols);
    }
  }

  private void drawLadder(GraphicsContext gc, int bottomTileId, int topTileId, int rows, int cols) {
    int[] bottomCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(bottomTileId, rows,
        cols);
    int[] topCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(topTileId, rows,
        cols);

    double bottomX = bottomCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double bottomY = bottomCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;
    double topX = topCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double topY = topCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;

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
    int numRungs = (int) (length / (currentCellSize / 2)) + 1;

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

  private void drawSnake(GraphicsContext gc, int headTileId, int tailTileId, int rows, int cols) {
    int[] headCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(headTileId, rows,
        cols);
    int[] tailCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(tailTileId, rows,
        cols);

    double headX = headCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double headY = headCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;
    double tailX = tailCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double tailY = tailCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;

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


  @Override
  public void onGameEvent(GameEvent e) {
    switch (e) {
      case GameCreatedEvent event -> {
        board = (SlBoard) event.getBoard();
        tokenManager.clear();
        tokenManager.updateLayout(board, currentCellSize, currentPadding);
        draw();
      }
      case GameResetEvent event -> {
        board = (SlBoard) event.getBoard();
        tokenManager.clear();
        tokenManager.updateLayout(board, currentCellSize, currentPadding);
        draw();
      }
      case PlayerAddedEvent event -> {
        SlPlayer player = (SlPlayer) event.getPlayer();
        tokenManager.addPlayer(player);
      }
      case SlPlayerMovedEvent event -> {
        SlPlayer player = event.getPlayer();
        tokenManager.playerMoved(player, event.getFromTile(), event.getToTile());
        draw();
      }
      default -> {
      }
    }
  }
}