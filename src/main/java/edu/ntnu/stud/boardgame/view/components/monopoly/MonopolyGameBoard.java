package edu.ntnu.stud.boardgame.view.components.monopoly;

import edu.ntnu.stud.boardgame.controller.MonopolyController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.AbstractGameBoard;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonopolyGameBoard extends AbstractGameBoard {

  private final MonopolyController controller;
  private final Map<TileType, Color> tileColors;
  private final Map<Integer, Color> playerColors;

  public MonopolyGameBoard(MonopolyController controller) {
    super();
    this.controller = controller;
    this.tileColors = initializeTileColors();
    this.playerColors = initializePlayerColors();
  }

  private Map<TileType, Color> initializeTileColors() {
    Map<TileType, Color> colors = new HashMap<>();
    colors.put(TileType.START, Color.GREEN);
    colors.put(TileType.TAX, Color.ORANGE);
    colors.put(TileType.PROPERTY, Color.LIGHTBLUE);
    return colors;
  }

  private Map<Integer, Color> initializePlayerColors() {
    Map<Integer, Color> colors = new HashMap<>();
    colors.put(0, Color.RED);
    colors.put(1, Color.BLUE);
    colors.put(2, Color.GREEN);
    colors.put(3, Color.YELLOW);
    return colors;
  }

  @Override
  protected void calculateCellSize() {
    if (board == null || boardCanvas.getWidth() <= 0 || boardCanvas.getHeight() <= 0) {
      return;
    }

    int cols = board.getColumns();
    int rows = board.getRows();

    double availableWidth = boardCanvas.getWidth();
    double availableHeight = boardCanvas.getHeight();

    double paddingFactor = Math.max(0.05, 0.15 - (Math.min(cols, rows) * 0.01));
    padding = Math.min(availableWidth, availableHeight) * paddingFactor;

    double maxCellWidth = (availableWidth - 2 * padding) / cols;
    double maxCellHeight = (availableHeight - 2 * padding) / rows;

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

    drawBoardBackground(gc);
    drawAllTiles(gc);

    needsRedraw = false;
  }

  private void drawBoardBackground(GraphicsContext gc) {
    gc.setFill(Color.LIGHTGRAY);
    double boardWidth = cellSize * board.getColumns();
    double boardHeight = cellSize * board.getRows();
    gc.fillRect(padding, padding, boardWidth, boardHeight);
  }

  private void drawAllTiles(GraphicsContext gc) {
    int maxTileId = board.getEndTileId();

    for (int i = maxTileId; i > 0; i--) {
      drawTile(gc, i);
    }

    drawTile(gc, 0);
  }

  private void drawTile(GraphicsContext gc, int tileId) {
    Tile tile = board.getTile(tileId);
    if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
      return;
    }

    double x = tile.getColumn() * cellSize + padding;
    double y = tile.getRow() * cellSize + padding;

    TileType tileType = determineTileType(tileId);
    Color tileColor = determineTileColor(tile, tileType);

    drawTileBackground(gc, x, y, tileColor);
    drawTileBorder(gc, x, y);
    drawTileContent(gc, tile, tileId, x, y, tileType);
  }

  private TileType determineTileType(int tileId) {
    if (tileId == 0) {
      return TileType.START;
    } else if (tileId % 5 == 0) {
      return TileType.TAX;
    } else {
      return TileType.PROPERTY;
    }
  }

  private Color determineTileColor(Tile tile, TileType tileType) {
    if (tileType != TileType.PROPERTY) {
      return tileColors.get(tileType);
    }

    Player owner = controller.getPropertyOwner(tile);
    if (owner == null) {
      return tileColors.get(TileType.PROPERTY);
    }

    int playerIndex = controller.getGame().getPlayers().indexOf(owner);
    return playerColors.getOrDefault(playerIndex, Color.PURPLE);
  }

  private void drawTileBackground(GraphicsContext gc, double x, double y, Color color) {
    gc.setFill(color);
    gc.fillRect(x, y, cellSize, cellSize);
  }

  private void drawTileBorder(GraphicsContext gc, double x, double y) {
    gc.setStroke(Color.BLACK);
    gc.strokeRect(x, y, cellSize, cellSize);
  }

  private void drawTileContent(GraphicsContext gc, Tile tile, int tileId, double x, double y,
      TileType tileType) {
    gc.setFill(Color.BLACK);
    double fontSize = Math.max(8, cellSize / 5);
    gc.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
    gc.fillText(String.valueOf(tileId), x + 5, y + 15);

    if (tileType == TileType.START) {
      gc.fillText("GO", x + cellSize / 3, y + cellSize / 2);
    } else {
      String priceText = getPriceText(tile, tileType);
      gc.fillText(priceText, x + 5, y + cellSize - 5);
    }
  }

  private String getPriceText(Tile tile, TileType tileType) {
    if (tileType == TileType.TAX) {
      return "Tax";
    } else {
      int price = controller.getPropertyPrice(tile);
      return "$" + price;
    }
  }

  private enum TileType {
    START, PROPERTY, TAX
  }
}