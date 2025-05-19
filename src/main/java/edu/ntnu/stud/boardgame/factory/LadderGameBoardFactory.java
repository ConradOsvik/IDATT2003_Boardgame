package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LadderGameBoardFactory {

  private static final Map<String, Board> PREDEFINED_BOARDS = new HashMap<>();

  static {
    PREDEFINED_BOARDS.put("Classic", createClassicBoard());
    PREDEFINED_BOARDS.put("Easy", createEasyBoard());
    PREDEFINED_BOARDS.put("Hard", createHardBoard());
    PREDEFINED_BOARDS.put("Extreme", createExtremeBoard());
  }

  public static List<String> getAvailableBoards() {
    return Arrays.asList("Classic", "Easy", "Hard", "Extreme");
  }

  public static Board createBoard(String boardName) {
    if (!PREDEFINED_BOARDS.containsKey(boardName)) {
      throw new IllegalArgumentException("Unknown board name: " + boardName);
    }
    return PREDEFINED_BOARDS.get(boardName);
  }

  private static Board createClassicBoard() {
    Board board = new Board("Classic Snakes and Ladders",
        "The traditional game with balanced snakes and ladders",
        10, 9, 0, 90);

    initializeLadderBoard(board);

    addLadder(board, 1, 40);
    addLadder(board, 8, 10);
    addLadder(board, 36, 52);
    addLadder(board, 43, 62);
    addLadder(board, 49, 79);
    addLadder(board, 65, 82);
    addLadder(board, 68, 85);

    addSnake(board, 24, 5);
    addSnake(board, 33, 3);
    addSnake(board, 42, 30);
    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 74, 12);
    addSnake(board, 87, 70);

    return board;
  }

  private static Board createEasyBoard() {
    Board board = new Board("Easy Snakes and Ladders",
        "A beginner-friendly game with more ladders than snakes",
        10, 9, 0, 90);

    initializeLadderBoard(board);

    addLadder(board, 1, 40);
    addLadder(board, 8, 31);
    addLadder(board, 19, 42);
    addLadder(board, 28, 56);
    addLadder(board, 36, 52);
    addLadder(board, 43, 62);
    addLadder(board, 49, 79);
    addLadder(board, 65, 82);
    addLadder(board, 68, 85);

    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 87, 70);

    addSkipTurnAction(board, 21);

    return board;
  }

  private static Board createHardBoard() {
    Board board = new Board("Hard Snakes and Ladders",
        "A challenging game with more snakes than ladders",
        10, 9, 0, 90);

    initializeLadderBoard(board);

    addLadder(board, 7, 14);
    addLadder(board, 21, 42);
    addLadder(board, 36, 44);
    addLadder(board, 51, 67);
    addLadder(board, 71, 82);

    addSnake(board, 16, 4);
    addSnake(board, 24, 8);
    addSnake(board, 33, 3);
    addSnake(board, 38, 20);
    addSnake(board, 42, 30);
    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 74, 12);
    addSnake(board, 87, 70);
    addSnake(board, 89, 48);

    addSkipTurnAction(board, 18);
    addSkipTurnAction(board, 32);
    addSkipTurnAction(board, 59);

    return board;
  }

  private static Board createExtremeBoard() {
    Board board = new Board("Extreme Snakes and Ladders",
        "An extremely challenging game with many snakes and few ladders",
        10, 9, 0, 90);

    initializeLadderBoard(board);

    addLadder(board, 7, 14);
    addLadder(board, 31, 40);
    addLadder(board, 71, 82);

    addSnake(board, 16, 4);
    addSnake(board, 24, 8);
    addSnake(board, 33, 3);
    addSnake(board, 38, 20);
    addSnake(board, 42, 30);
    addSnake(board, 56, 37);
    addSnake(board, 64, 27);
    addSnake(board, 74, 12);
    addSnake(board, 87, 70);
    addSnake(board, 89, 48);
    addSnake(board, 79, 41);
    addSnake(board, 61, 19);
    addSnake(board, 46, 25);

    addSkipTurnAction(board, 18);
    addSkipTurnAction(board, 32);
    addSkipTurnAction(board, 59);
    addSkipTurnAction(board, 76);
    addSkipTurnAction(board, 83);

    return board;
  }

  private static void initializeLadderBoard(Board board) {
    int rows = board.getRows();
    int cols = board.getColumns();
    int totalTiles = rows * cols;

    Tile startingTile = new Tile(0);
    board.addTile(startingTile);

    for (int i = 1; i <= totalTiles; i++) {
      Tile tile = new Tile(i);

      int row = (i - 1) / cols;
      int col;

      if (row % 2 == 0) {
        col = (i - 1) % cols;
      } else {
        col = cols - 1 - ((i - 1) % cols);
      }

      tile.setRow(rows - 1 - row);
      tile.setColumn(col);

      board.addTile(tile);
    }

    for (int i = 0; i < totalTiles; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }
  }

  private static void addLadder(Board board, int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    fromTile.setLandAction(new LadderAction(toTile));
  }

  private static void addSnake(Board board, int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    fromTile.setLandAction(new SnakeAction(toTile));
  }

  private static void addSkipTurnAction(Board board, int tileId) {
    Tile tile = board.getTile(tileId);
    tile.setLandAction(new SkipTurnAction());
  }
}