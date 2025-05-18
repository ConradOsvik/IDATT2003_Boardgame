package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javafx.animation.PathTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameBoard extends StackPane {

  // Colors for board elements
  private static final Color SNAKE_COLOR = Color.RED;
  private static final Color LADDER_COLOR = Color.GREEN;
  private static final Color DEFAULT_TILE_COLOR = Color.LIGHTBLUE;
  private static final Color BOARD_BACKGROUND_COLOR = Color.WHITE;
  private static final Color SNAKE_TILE_COLOR = Color.PINK;
  private static final Color LADDER_TILE_COLOR = Color.LIGHTGREEN;
  private static final Color LAST_TILE_COLOR = Color.GOLD;
  private static final Color START_TILE_COLOR = Color.LIGHTGRAY;
  private final Canvas boardCanvas;
  private final Pane piecesLayer;
  private final Map<Player, PlayerPiece> playerPieces = new HashMap<>();
  private final Map<PlayerPiece, Queue<PathTransition>> animationQueues = new HashMap<>();
  private final Map<PlayerPiece, Boolean> isAnimating = new HashMap<>();
  private Board board;
  private double cellSize;
  private double padding;

  public GameBoard() {
    boardCanvas = new Canvas();
    piecesLayer = new Pane();

    boardCanvas.widthProperty().bind(widthProperty());
    boardCanvas.heightProperty().bind(heightProperty());

    boardCanvas.widthProperty().addListener((obs, oldVal, newVal) -> drawBoard());
    boardCanvas.heightProperty().addListener((obs, oldVal, newVal) -> drawBoard());

    getChildren().addAll(boardCanvas, piecesLayer);
  }

  public void setBoard(Board board) {
    this.board = board;

    calculateCellSize();

    drawBoard();
  }

  public void updatePlayerPosition(Player player, Tile tile) {
    PlayerPiece piece = playerPieces.get(player);

    if (piece == null) {
      piece = new PlayerPiece(player);
      playerPieces.put(player, piece);
      piecesLayer.getChildren().add(piece);

      animationQueues.put(piece, new LinkedList<>());
      isAnimating.put(piece, false);
    }

    if (tile != null) {
      positionPieceAtTile(piece, tile);
    }
  }

  public void animatePlayerMove(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    Path path = createDirectPath(fromTile, toTile);
    queueAnimation(piece, path, Duration.seconds(0.3));
  }

  public void animatePlayerLadderClimb(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    Path path = createLadderPath(fromTile, toTile);
    queueAnimation(piece, path, Duration.seconds(0.5));
  }

  public void animatePlayerSnakeSlide(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    Path path = createSnakePath(fromTile, toTile);
    queueAnimation(piece, path, Duration.seconds(0.5));
  }

  private void calculateCellSize() {
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
        (boardCanvas.getHeight() - (cellSize * rows)) / 2
    );
  }

  private void drawBoard() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0) {
      return;
    }

    calculateCellSize();

    GraphicsContext gc = boardCanvas.getGraphicsContext2D();

    gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

    gc.setFill(BOARD_BACKGROUND_COLOR);
    gc.fillRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

    drawTiles(gc);

    drawSnakesAndLadders(gc);

    updateAllPiecePositions();
  }

  private void drawTiles(GraphicsContext gc) {
    if (board == null) {
      return;
    }

    int rows = board.getRows();
    int cols = board.getColumns();

    double fontSize = Math.min(16, Math.max(9, cellSize / 4));
    gc.setFont(Font.font("Arial", FontWeight.NORMAL, fontSize));
    gc.setTextAlign(TextAlignment.CENTER);

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
      boolean isStart = i == 1;
      boolean isEnd = i == board.getEndTileId();

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

      gc.fillRect(x, y, cellSize, cellSize);
      gc.setStroke(Color.BLACK);
      gc.strokeRect(x, y, cellSize, cellSize);

      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(i), x + cellSize / 2, y + cellSize / 2 + fontSize / 3);
    }
  }

  private void drawSnakesAndLadders(GraphicsContext gc) {
    if (board == null) {
      return;
    }

    for (Tile tile : board.getTiles().values()) {
      if (tile.getLandAction() instanceof SnakeAction) {
        SnakeAction action = (SnakeAction) tile.getLandAction();
        drawSnake(gc, tile, action.getDestinationTile());
      } else if (tile.getLandAction() instanceof LadderAction) {
        LadderAction action = (LadderAction) tile.getLandAction();
        drawLadder(gc, tile, action.getDestinationTile());
      }
    }
  }

  private void drawSnake(GraphicsContext gc, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null ||
        fromTile.getRow() == null || fromTile.getColumn() == null ||
        toTile.getRow() == null || toTile.getColumn() == null) {
      return;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(3);

    double controlX1 = (startX + endX) / 2 + cellSize;
    double controlY1 = (startY + endY) / 2;
    double controlX2 = (startX + endX) / 2 - cellSize;
    double controlY2 = (startY + endY) / 2;

    gc.beginPath();
    gc.moveTo(startX, startY);
    gc.bezierCurveTo(controlX1, controlY1, controlX2, controlY2, endX, endY);
    gc.stroke();

    double headRadius = Math.max(3, cellSize / 8);
    gc.setFill(SNAKE_COLOR);
    gc.fillOval(startX - headRadius, startY - headRadius, headRadius * 2, headRadius * 2);
  }

  private void drawLadder(GraphicsContext gc, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null ||
        fromTile.getRow() == null || fromTile.getColumn() == null ||
        toTile.getRow() == null || toTile.getColumn() == null) {
      return;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    gc.setStroke(LADDER_COLOR);
    gc.setLineWidth(3);

    gc.strokeLine(startX, startY, endX, endY);

    double dx = endX - startX;
    double dy = endY - startY;
    double length = Math.sqrt(dx * dx + dy * dy);

    double nx = dx / length;
    double ny = dy / length;

    double px = -ny;
    double py = nx;

    int numRungs = (int) (length / (cellSize / 2)) + 1;
    double rungLength = cellSize / 3;

    for (int i = 0; i < numRungs; i++) {
      double t = i / (double) (numRungs - 1);
      double x = startX + t * dx;
      double y = startY + t * dy;

      gc.strokeLine(
          x - px * rungLength / 2, y - py * rungLength / 2,
          x + px * rungLength / 2, y + py * rungLength / 2
      );
    }
  }

  private void positionPieceAtTile(PlayerPiece piece, Tile tile) {
    if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
      return;
    }

    double x = tile.getColumn() * cellSize + padding + cellSize / 2;
    double y = tile.getRow() * cellSize + padding + cellSize / 2;

    piece.setTranslateX(x);
    piece.setTranslateY(y);
  }

  private Path createDirectPath(Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null ||
        fromTile.getRow() == null || fromTile.getColumn() == null ||
        toTile.getRow() == null || toTile.getColumn() == null) {
      return null;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));
    path.getElements().add(new LineTo(endX, endY));

    return path;
  }

  private Path createLadderPath(Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null ||
        fromTile.getRow() == null || fromTile.getColumn() == null ||
        toTile.getRow() == null || toTile.getColumn() == null) {
      return null;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));

    double midX = (startX + endX) / 2;
    double controlY = Math.min(startY, endY) - cellSize / 2;

    path.getElements().add(new QuadCurveTo(midX, controlY, endX, endY));

    return path;
  }

  private Path createSnakePath(Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null ||
        fromTile.getRow() == null || fromTile.getColumn() == null ||
        toTile.getRow() == null || toTile.getColumn() == null) {
      return null;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));

    double controlX1 = (startX + endX) / 2 + cellSize;
    double controlY1 = (startY + endY) / 2;
    double controlX2 = (startX + endX) / 2 - cellSize;
    double controlY2 = (startY + endY) / 2;

    path.getElements().add(new CubicCurveTo(
        controlX1, controlY1,
        controlX2, controlY2,
        endX, endY
    ));

    return path;
  }

  private void queueAnimation(PlayerPiece piece, Path path, Duration duration) {
    if (path == null || !animationQueues.containsKey(piece)) {
      return;
    }

    Queue<PathTransition> queue = animationQueues.get(piece);

    PathTransition transition = new PathTransition(duration, path, piece);
    queue.add(transition);

    if (!isAnimating.get(piece)) {
      playNextAnimation(piece);
    }
  }

  private void playNextAnimation(PlayerPiece piece) {
    Queue<PathTransition> queue = animationQueues.get(piece);

    if (queue.isEmpty()) {
      isAnimating.put(piece, false);
      return;
    }

    isAnimating.put(piece, true);
    PathTransition nextAnimation = queue.poll();

    nextAnimation.setOnFinished(e -> playNextAnimation(piece));
    nextAnimation.play();
  }

  private void updateAllPiecePositions() {
    playerPieces.forEach((player, piece) -> {
      if (player.getCurrentTile() != null) {
        positionPieceAtTile(piece, player.getCurrentTile());
      }
    });
  }

  private class PlayerPiece extends StackPane {

    private final Player player;

    public PlayerPiece(Player player) {
      this.player = player;

      Circle circle = new Circle(cellSize / 4);
      circle.getStyleClass().add("player-piece");
      circle.getStyleClass().add(player.getPiece().name().toLowerCase() + "-piece");

      getChildren().add(circle);

      setTranslateX(-cellSize / 4);
      setTranslateY(-cellSize / 4);
    }
  }
}