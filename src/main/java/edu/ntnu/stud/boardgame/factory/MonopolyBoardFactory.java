package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.StartAction;
import edu.ntnu.stud.boardgame.model.action.TaxAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonopolyBoardFactory {

  private static final Map<String, Board> PREDEFINED_BOARDS = new HashMap<>();

  private static final int START_BONUS = 50;

  static {
    PREDEFINED_BOARDS.put("Standard", createStandardBoard());
    PREDEFINED_BOARDS.put("Mini", createMiniBoard());
    PREDEFINED_BOARDS.put("Economic", createEconomicBoard());
  }

  public static List<String> getAvailableBoards() {
    return Arrays.asList("Standard", "Mini", "Economic");
  }

  public static Board createBoard(String boardName) {
    if (!PREDEFINED_BOARDS.containsKey(boardName)) {
      throw new IllegalArgumentException("Unknown board name: " + boardName);
    }
    return PREDEFINED_BOARDS.get(boardName);
  }

  private static Board createStandardBoard() {
    Board board = new Board("Standard Monopoly", "The classic monopoly game with 40 spaces around the board",
        11, 11, 0, 39);

    initializeMonopolyBoard(board, 40);

    return board;
  }

  private static Board createMiniBoard() {
    Board board = new Board("Mini Monopoly", "A smaller version with 24 spaces around the board",
        6, 6, 0, 23);

    initializeMonopolyBoard(board, 24);

    return board;
  }

  private static Board createEconomicBoard() {
    Board board = new Board("Economic Monopoly", "Higher property values and taxes",
        11, 11, 0, 39);

    initializeEconomicBoard(board);

    return board;
  }

  private static void initializeMonopolyBoard(Board board, int size) {
    Tile startTile = new Tile(0);
    startTile.setRow(size / 4);
    startTile.setColumn(size / 4);
    startTile.setName("GO");
    startTile.setLandAction(new StartAction(START_BONUS));
    board.addTile(startTile);

    int maxIndex = size - 1;

    for (int i = 1; i < size; i++) {
      Tile tile = new Tile(i);

      int row, col;
      if (i <= maxIndex / 4) {
        row = size / 4;
        col = size / 4 - i;
      } else if (i <= maxIndex / 2) {
        row = size / 4 - (i - maxIndex / 4);
        col = 0;
      } else if (i <= 3 * maxIndex / 4) {
        row = 0;
        col = i - maxIndex / 2;
      } else {
        row = i - 3 * maxIndex / 4;
        col = size / 4;
      }

      tile.setRow(row);
      tile.setColumn(col);

      if (i % 5 == 0) {
        int taxAmount = 100;
        tile.setName("Tax $" + taxAmount);
        tile.setLandAction(new TaxAction(taxAmount));
      } else {
        int propertyPrice = 50 + i * 10;
        tile.setName("Property $" + propertyPrice);
        tile.setLandAction(new PropertyAction(propertyPrice));
      }

      board.addTile(tile);
    }

    for (int i = 0; i < maxIndex; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

    Tile lastTile = board.getTile(maxIndex);
    Tile firstTile = board.getTile(0);
    lastTile.setNextTile(firstTile);
  }

  private static void initializeEconomicBoard(Board board) {
    Tile startTile = new Tile(0);
    startTile.setRow(10);
    startTile.setColumn(10);
    startTile.setName("GO");
    startTile.setLandAction(new StartAction(100));
    board.addTile(startTile);

    for (int i = 1; i < 40; i++) {
      Tile tile = new Tile(i);

      int row, col;
      if (i <= 10) {
        row = 10;
        col = 10 - i;
      } else if (i <= 20) {
        row = 20 - i;
        col = 0;
      } else if (i <= 30) {
        row = 0;
        col = i - 20;
      } else {
        row = i - 30;
        col = 10;
      }

      tile.setRow(row);
      tile.setColumn(col);

      if (i % 5 == 0) {
        int taxAmount = 200;
        tile.setName("Tax $" + taxAmount);
        tile.setLandAction(new TaxAction(taxAmount));
      } else {
        int propertyPrice = 100 + i * 20;
        tile.setName("Property $" + propertyPrice);
        tile.setLandAction(new PropertyAction(propertyPrice));
      }

      board.addTile(tile);
    }

    for (int i = 0; i < 39; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

    Tile lastTile = board.getTile(39);
    Tile firstTile = board.getTile(0);
    lastTile.setNextTile(firstTile);
  }
}