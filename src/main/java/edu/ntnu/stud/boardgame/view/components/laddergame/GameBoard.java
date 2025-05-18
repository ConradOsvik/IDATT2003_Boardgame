package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameBoard extends StackPane {

  private static final Color SNAKE_COLOR = Color.rgb(220, 20, 60);
  private static final Color LADDER_COLOR = Color.rgb(50, 205, 50);
  private static final Color DEFAULT_TILE_COLOR = Color.rgb(135, 206, 250);
  private static final Color BOARD_BACKGROUND_COLOR = Color.WHITE;
  private static final Color SNAKE_TILE_COLOR = Color.rgb(255, 182, 193);
  private static final Color LADDER_TILE_COLOR = Color.rgb(144, 238, 144);
  private static final Color LAST_TILE_COLOR = Color.rgb(255, 215, 0);
  private static final Color START_TILE_COLOR = Color.rgb(211, 211, 211);

  private final Canvas boardCanvas;
  private final Pane piecesLayer;
  private final Map<Player, PlayerPiece> playerPieces = new HashMap<>();

  private Board board;
  private double cellSize;
  private double padding;
  private boolean needsRedraw = true;

  public GameBoard() {
    boardCanvas = new Canvas();
    piecesLayer = new Pane();

    boardCanvas.widthProperty().bind(widthProperty());
    boardCanvas.heightProperty().bind(heightProperty());

    ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> {
      if (newVal.doubleValue() > 0) {
        needsRedraw = true;
        calculateCellSize();
        Platform.runLater(this::drawBoard);
      }
    };

    boardCanvas.widthProperty().addListener(resizeListener);
    boardCanvas.heightProperty().addListener(resizeListener);

    getChildren().addAll(boardCanvas, piecesLayer);
  }

  public void setBoard(Board board) {
    this.board = board;
    needsRedraw = true;
    calculateCellSize();
    drawBoard();
  }

  public void updatePlayerPosition(Player player, Tile tile) {
    if (player == null) {
      return;
    }

    PlayerPiece piece = playerPieces.get(player);

    if (piece == null) {
      piece = new PlayerPiece(player);
      playerPieces.put(player, piece);
      piecesLayer.getChildren().add(piece);
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

    piece.animateMove(fromTile, toTile, cellSize, padding);
  }

  public void animatePlayerLadderClimb(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    piece.animateLadderClimb(fromTile, toTile, cellSize, padding);
  }

  public void animatePlayerSnakeSlide(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    piece.animateSnakeSlide(fromTile, toTile, cellSize, padding);
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

    padding = Math.min((boardCanvas.getWidth() - (cellSize * cols)) / 2,
        (boardCanvas.getHeight() - (cellSize * rows)) / 2);

    updateAllPiecePositions();
  }

  private void drawBoard() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0
        || !needsRedraw) {
      return;
    }

    GraphicsContext gc = boardCanvas.getGraphicsContext2D();

    gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

    gc.setFill(BOARD_BACKGROUND_COLOR);
    gc.fillRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

    drawTiles(gc);
    drawSnakesAndLadders(gc);

    needsRedraw = false;
  }

  private void drawTiles(GraphicsContext gc) {
    if (board == null) {
      return;
    }

    double fontSize = Math.min(16, Math.max(9, cellSize / 4));
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
      boolean isStart = i == board.getStartTileId() + 1;
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
      gc.fillText(String.valueOf(i), x + cellSize / 2, y + cellSize / 2);
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
    if (fromTile == null || toTile == null || fromTile.getRow() == null
        || fromTile.getColumn() == null || toTile.getRow() == null || toTile.getColumn() == null) {
      return;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    gc.setStroke(SNAKE_COLOR);
    gc.setLineWidth(Math.max(3, cellSize / 15));

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
    if (fromTile == null || toTile == null || fromTile.getRow() == null
        || fromTile.getColumn() == null || toTile.getRow() == null || toTile.getColumn() == null) {
      return;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    gc.setStroke(LADDER_COLOR);
    gc.setLineWidth(Math.max(3, cellSize / 15));

    double angle = Math.atan2(endY - startY, endX - startX);
    double railOffset = cellSize / 8;

    double rail1StartX = startX + railOffset * Math.cos(angle + Math.PI / 2);
    double rail1StartY = startY + railOffset * Math.sin(angle + Math.PI / 2);
    double rail1EndX = endX + railOffset * Math.cos(angle + Math.PI / 2);
    double rail1EndY = endY + railOffset * Math.sin(angle + Math.PI / 2);

    double rail2StartX = startX + railOffset * Math.cos(angle - Math.PI / 2);
    double rail2StartY = startY + railOffset * Math.sin(angle - Math.PI / 2);
    double rail2EndX = endX + railOffset * Math.cos(angle - Math.PI / 2);
    double rail2EndY = endY + railOffset * Math.sin(angle - Math.PI / 2);

    gc.strokeLine(rail1StartX, rail1StartY, rail1EndX, rail1EndY);
    gc.strokeLine(rail2StartX, rail2StartY, rail2EndX, rail2EndY);

    double dx = endX - startX;
    double dy = endY - startY;
    double length = Math.sqrt(dx * dx + dy * dy);

    int numRungs = Math.max(3, (int) (length / (cellSize / 2)));
    gc.setLineWidth(Math.max(2, cellSize / 20));

    for (int i = 0; i < numRungs; i++) {
      double t = i / (double) (numRungs - 1);
      double rungX = startX + t * (endX - startX);
      double rungY = startY + t * (endY - startY);

      double rung1X = rungX + railOffset * Math.cos(angle + Math.PI / 2);
      double rung1Y = rungY + railOffset * Math.sin(angle + Math.PI / 2);
      double rung2X = rungX + railOffset * Math.cos(angle - Math.PI / 2);
      double rung2Y = rungY + railOffset * Math.sin(angle - Math.PI / 2);

      gc.strokeLine(rung1X, rung1Y, rung2X, rung2Y);
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

  private void updateAllPiecePositions() {
    playerPieces.forEach((player, piece) -> {
      if (player.getCurrentTile() != null) {
        positionPieceAtTile(piece, player.getCurrentTile());
      }
    });
  }
}