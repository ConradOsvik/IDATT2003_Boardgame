package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import edu.ntnu.stud.boardgame.observer.event.BounceBackEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.SnakeEncounteredEvent;

public class LadderGame extends BoardGame {

  @Override
  public Board createDefaultBoard() {
    Board board = new Board("Classic Snakes and Ladders", "A classic game of Snakes and Ladders",
        10, 9, 0, 90);

    initializeStandardBoard(board);

    return board;
  }

  @Override
  public void playTurn() {
    if (gameOver) {
      return;
    }

    int steps = dice.roll();
    notifyObservers(new DiceRolledEvent(steps, currentPlayer));

    Tile currentTile = currentPlayer.getCurrentTile();
    Tile lastTile = board.getTile(board.getEndTileId());

    int targetTileId = currentTile.getTileId() + steps;

    if (targetTileId > lastTile.getTileId()) {
      Tile beforeTile = currentPlayer.getCurrentTile();
      currentPlayer.setCurrentTile(lastTile);
      notifyObservers(new PlayerMovedEvent(currentPlayer, beforeTile, lastTile, steps, board));

      int overshoot = targetTileId - lastTile.getTileId();
      int bouncedTileId = lastTile.getTileId() - overshoot;
      Tile bouncedTile = board.getTile(bouncedTileId);

      currentPlayer.setCurrentTile(bouncedTile);
      notifyObservers(new BounceBackEvent(currentPlayer, lastTile, bouncedTile, 0, board));

      triggerLandAction(bouncedTile);

    } else {
      Tile targetTile = currentPlayer.getDestinationTile(steps);
      Tile beforeTile = currentPlayer.getCurrentTile();

      currentPlayer.setCurrentTile(targetTile);
      notifyObservers(new PlayerMovedEvent(currentPlayer, beforeTile, targetTile, steps, board));

      triggerLandAction(targetTile);
    }

    if (currentPlayer.getCurrentTile().equals(lastTile)) {
      endGame(currentPlayer);
    }
  }

  private void triggerLandAction(Tile targetTile) {
    if (targetTile.getLandAction() != null) {
      Tile beforeActionTile = currentPlayer.getCurrentTile();
      targetTile.landPlayer(currentPlayer);
      Tile afterActionTile = currentPlayer.getCurrentTile();

      if (!afterActionTile.equals(beforeActionTile)) {
        if (targetTile.getLandAction() instanceof LadderAction) {
          notifyObservers(
              new LadderClimbedEvent(currentPlayer, beforeActionTile, afterActionTile, 0, board));
        } else if (targetTile.getLandAction() instanceof SnakeAction) {
          notifyObservers(
              new SnakeEncounteredEvent(currentPlayer, beforeActionTile, afterActionTile, 0,
                  board));
        }
      }
    }
  }

  private void initializeStandardBoard(Board board) {
    Tile startingTile = new Tile(0);
    board.addTile(startingTile);

    for (int i = 1; i <= 90; i++) {
      Tile tile = new Tile(i);

      int row = (i - 1) / 9;
      int col;

      if (row % 2 == 0) {
        col = (i - 1) % 9;
      } else {
        col = 8 - ((i - 1) % 9);
      }

      tile.setRow(9 - row);
      tile.setColumn(col);

      board.addTile(tile);
    }

    for (int i = 0; i < 90; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

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
  }

  private void addLadder(Board board, int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    fromTile.setLandAction(new LadderAction(toTile));
  }

  private void addSnake(Board board, int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    fromTile.setLandAction(new SnakeAction(toTile));
  }
}
