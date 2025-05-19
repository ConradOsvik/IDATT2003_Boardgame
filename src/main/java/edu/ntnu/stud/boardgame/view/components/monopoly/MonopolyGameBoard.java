package edu.ntnu.stud.boardgame.view.components.monopoly;

import edu.ntnu.stud.boardgame.controller.MonopolyController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.AbstractGameBoard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonopolyGameBoard extends AbstractGameBoard {

  private final MonopolyController controller;

  public MonopolyGameBoard(MonopolyController controller) {
    super();
    this.controller = controller;
  }

  @Override
  protected void calculateCellSize() {
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

  @Override
  protected void drawBoard() {
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
}