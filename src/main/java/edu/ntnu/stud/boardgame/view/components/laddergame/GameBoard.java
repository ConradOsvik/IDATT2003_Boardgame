package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.laddergame.board.BoardRenderer;
import edu.ntnu.stud.boardgame.view.components.laddergame.piece.PieceAnimation;
import edu.ntnu.stud.boardgame.view.components.laddergame.piece.PlayerPiece;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GameBoard extends StackPane {

  private final Canvas boardCanvas;
  private final Pane piecesLayer;
  private final Map<Player, PlayerPiece> playerPieces = new HashMap<>();
  private final BoardRenderer boardRenderer;
  private final PieceAnimation pieceAnimation;

  private Board board;
  private double cellSize;
  private double padding;
  private boolean needsRedraw = true;

  public GameBoard() {
    boardCanvas = new Canvas();
    piecesLayer = new Pane();
    boardRenderer = new BoardRenderer();
    pieceAnimation = new PieceAnimation();

    boardCanvas.widthProperty().bind(widthProperty());
    boardCanvas.heightProperty().bind(heightProperty());
    piecesLayer.setPickOnBounds(false);

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
      piece.updateSize(Math.max(15, cellSize * 0.6));
    }

    if (tile == null || tile.getTileId() == 0) {
      piece.setVisible(false);
    } else {
      piece.setVisible(true);
      positionPieceAtTile(piece, tile);
    }
  }

  public void animatePlayerMove(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null) {
      return;
    }

    if (fromTile == null || fromTile.getTileId() == 0) {
      piece.setVisible(toTile != null && toTile.getTileId() != 0);
      if (piece.isVisible()) {
        positionPieceAtTile(piece, toTile);
      }
      return;
    }

    if (toTile == null || toTile.getTileId() == 0) {
      piece.setVisible(false);
      return;
    }

    int steps = Math.abs(toTile.getTileId() - fromTile.getTileId());

    piece.setVisible(true);
    pieceAnimation.animateRegularMove(piece, fromTile, toTile, cellSize, padding, steps);
  }

  public void animatePlayerLadderClimb(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    piece.setVisible(true);
    pieceAnimation.animateLadderClimb(piece, fromTile, toTile, cellSize, padding);
  }

  public void animatePlayerSnakeSlide(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    piece.setVisible(true);
    pieceAnimation.animateSnakeSlide(piece, fromTile, toTile, cellSize, padding);
  }

  public void animatePlayerBounceBack(Player player, Tile fromTile, Tile toTile) {
    PlayerPiece piece = playerPieces.get(player);
    if (piece == null || fromTile == null || toTile == null) {
      return;
    }

    piece.setVisible(true);
    pieceAnimation.animateBounceBack(piece, fromTile, toTile, cellSize, padding);
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

    boardRenderer.render(boardCanvas, board, cellSize, padding);
    needsRedraw = false;
  }

  private void positionPieceAtTile(PlayerPiece piece, Tile tile) {
    if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
      return;
    }

    double x = tile.getColumn() * cellSize + padding + cellSize / 2;
    double y = tile.getRow() * cellSize + padding + cellSize / 2;

    double pieceSize = piece.getCurrentSize();
    piece.setTranslateX(x - pieceSize / 2);
    piece.setTranslateY(y - pieceSize / 2);
  }

  private void updateAllPiecePositions() {
    double pieceSize = Math.max(15, cellSize * 0.6);
    playerPieces.values().forEach(piece -> piece.updateSize(pieceSize));

    playerPieces.forEach((player, piece) -> {
      if (player.getCurrentTile() != null) {
        if (player.getCurrentTile().getTileId() == 0) {
          piece.setVisible(false);
        } else {
          piece.setVisible(true);
          positionPieceAtTile(piece, player.getCurrentTile());
        }
      } else {
        piece.setVisible(false);
      }
    });
  }
}