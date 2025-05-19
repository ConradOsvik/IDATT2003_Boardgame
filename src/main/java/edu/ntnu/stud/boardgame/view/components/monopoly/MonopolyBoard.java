package edu.ntnu.stud.boardgame.view.components.monopoly;

import edu.ntnu.stud.boardgame.controller.MonopolyController;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.laddergame.piece.PlayerPiece;
import java.util.HashMap;
import java.util.Map;
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
  private final MonopolyController controller;
  private final double padding = 20;
  private Board board;
  private double cellSize;

  public MonopolyBoard(MonopolyController controller) {
    this.controller = controller;

    boardCanvas = new Canvas(600, 600);
    piecesLayer = new Pane();

    boardCanvas.widthProperty().bind(widthProperty());
    boardCanvas.heightProperty().bind(heightProperty());

    widthProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal.doubleValue() > 0) {
        drawBoard();
      }
    });

    heightProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal.doubleValue() > 0) {
        drawBoard();
      }
    });

    getChildren().addAll(boardCanvas, piecesLayer);
  }

  public void setBoard(Board board) {
    this.board = board;
    drawBoard();
  }

  public void updatePlayerPositions(Player player, Tile tile) {
    PlayerPiece piece = playerPieces.get(player);

    if (piece == null) {
      piece = new PlayerPiece(player);
      playerPieces.put(player, piece);
      piecesLayer.getChildren().add(piece);
    }

    if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
      piece.setVisible(false);
      return;
    }

    piece.setVisible(true);

    double x = tile.getColumn() * cellSize + padding + cellSize / 2;
    double y = tile.getRow() * cellSize + padding + cellSize / 2;

    piece.setTranslateX(x - piece.getCurrentSize() / 2);
    piece.setTranslateY(y - piece.getCurrentSize() / 2);
  }

  public void refreshBoard() {
    drawBoard();
  }

  private void drawBoard() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0) {
      return;
    }

    GraphicsContext gc = boardCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

    double boardWidth = boardCanvas.getWidth() - 2 * padding;
    double boardHeight = boardCanvas.getHeight() - 2 * padding;
    cellSize = Math.min(boardWidth / 11, boardHeight / 11);

    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(padding, padding, cellSize * 11, cellSize * 11);

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
      gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
      gc.fillText(String.valueOf(i), x + 5, y + 15);

      if (i != 0) {
        String priceText;
        if (i % 5 == 0) {
          priceText = "Tax $100";
        } else {
          int price = controller.getPropertyPrice(tile);
          priceText = "$" + price;
        }
        gc.fillText(priceText, x + 5, y + cellSize - 5);
      } else {
        gc.fillText("GO", x + cellSize / 3, y + cellSize / 2);
      }
    }

    for (Player player : playerPieces.keySet()) {
      updatePlayerPositions(player, player.getCurrentTile());
    }
  }
}