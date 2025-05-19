package edu.ntnu.stud.boardgame.view.components.monopoly;

import edu.ntnu.stud.boardgame.controller.MonopolyController;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.piece.PieceAnimation;
import edu.ntnu.stud.boardgame.view.components.piece.PlayerPiece;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonopolyBoard extends StackPane {

  private final Canvas boardCanvas;
  private final Pane piecesLayer;
  private final Map<Player, PlayerPiece> playerPieces = new HashMap<>();
  private final PieceAnimation pieceAnimation;
  private final MonopolyController controller;
  private final double padding = 20;

  private Board board;
  private double cellSize;
  private boolean needsRedraw = true;

  public MonopolyBoard(MonopolyController controller) {
    this.controller = controller;
    this.pieceAnimation = new PieceAnimation();

    boardCanvas = new Canvas();
    piecesLayer = new Pane();

    setMinSize(400, 400);

    boardCanvas.widthProperty().bind(widthProperty());
    boardCanvas.heightProperty().bind(heightProperty());
    piecesLayer.setPickOnBounds(false);

    widthProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal.doubleValue() > 0) {
        needsRedraw = true;
        calculateCellSize();
        Platform.runLater(this::drawBoard);
      }
    });

    heightProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal.doubleValue() > 0) {
        needsRedraw = true;
        calculateCellSize();
        Platform.runLater(this::drawBoard);
      }
    });

    getChildren().addAll(boardCanvas, piecesLayer);
  }

  public void setBoard(Board board) {
    this.board = board;
    needsRedraw = true;
    calculateCellSize();
    drawBoard();
  }

  public void updatePlayerPositions(Player player, Tile tile) {
    PlayerPiece piece = playerPieces.get(player);

    if (piece == null) {
      piece = new PlayerPiece(player);
      playerPieces.put(player, piece);
      piecesLayer.getChildren().add(piece);
      piece.updateSize(Math.max(15, cellSize * 0.6));
    }

    if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
      piece.setVisible(false);
      return;
    }

    piece.setVisible(true);
    positionPieceAtTile(piece, tile);
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

  private void calculateCellSize() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0) {
      return;
    }

    int cols = board.getColumns();
    int rows = board.getRows();

    double maxCellWidth = (boardCanvas.getWidth() - 2 * padding) / cols;
    double maxCellHeight = (boardCanvas.getHeight() - 2 * padding) / rows;

    cellSize = Math.min(maxCellWidth, maxCellHeight);

    updateAllPiecePositions();
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

  private void drawBoard() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0
        || !needsRedraw) {
      return;
    }

    GraphicsContext gc = boardCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

    gc.setFill(Color.LIGHTGRAY);
    double boardWidth = cellSize * board.getColumns();
    double boardHeight = cellSize * board.getRows();
    gc.fillRect(padding, padding, boardWidth, boardHeight);

    for (int i = 0; i <= 39; i++) {
      Tile tile = board.getTile(i);
      if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
        continue;
      }

      double x = tile.getColumn() * cellSize + padding;
      double y = tile.getRow() * cellSize + padding;

      if (i == 0) {
        gc.setFill(Color.GREEN);
      } else if (i % 5 == 0) {
        gc.setFill(Color.ORANGE);
      } else {
        Player owner = controller.getPropertyOwner(tile);

        if (owner != null) {
          int playerIndex = controller.getGame().getPlayers().indexOf(owner);
          switch (playerIndex) {
            case 0:
              gc.setFill(Color.RED);
              break;
            case 1:
              gc.setFill(Color.BLUE);
              break;
            case 2:
              gc.setFill(Color.GREEN);
              break;
            case 3:
              gc.setFill(Color.YELLOW);
              break;
            default:
              gc.setFill(Color.PURPLE);
              break;
          }
        } else {
          gc.setFill(Color.LIGHTBLUE);
        }
      }

      gc.fillRect(x, y, cellSize, cellSize);
      gc.setStroke(Color.BLACK);
      gc.strokeRect(x, y, cellSize, cellSize);

      gc.setFill(Color.BLACK);
      double fontSize = Math.max(8, cellSize / 5);
      gc.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
      gc.fillText(String.valueOf(i), x + 5, y + 15);

      if (i != 0) {
        String priceText;
        if (i % 5 == 0) {
          priceText = "Tax";
        } else {
          int price = controller.getPropertyPrice(tile);
          priceText = "$" + price;
        }
        gc.fillText(priceText, x + 5, y + cellSize - 5);
      } else {
        gc.fillText("GO", x + cellSize / 3, y + cellSize / 2);
      }
    }

    needsRedraw = false;
  }

  public void refreshBoard() {
    needsRedraw = true;
    drawBoard();
  }

  public void clearPlayerPieces() {
    pieceAnimation.clearAllAnimations();
    piecesLayer.getChildren().clear();
    playerPieces.clear();
  }
}