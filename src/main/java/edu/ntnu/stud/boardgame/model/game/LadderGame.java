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
  public void createBoard() {
    this.board = new Board("Classic Snakes and Ladders", "A classic game of Snakes and Ladders", 10,
        9, 0, 90);
    initializeStandardBoard();
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

    if (currentTile.equals(lastTile)) {
      this.winner = currentPlayer;
      this.gameOver = true;
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
        } else {
          notifyObservers(
              new SnakeEncounteredEvent(currentPlayer, beforeActionTile, afterActionTile, 0,
                  board));
        }
      }
    }
  }

  private void initializeStandardBoard() {
    Tile startingTile = new Tile(0);
    board.addTile(startingTile);

    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 9; col++) {
        int tileId = calculateTileId(row, col);
        Tile tile = new Tile(tileId);
        tile.setRow(row);
        tile.setColumn(col);
        board.addTile(tile);
      }
    }

    for (int i = 1; i < 90; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

    addLadder(4, 14);
    addLadder(9, 31);
    addLadder(28, 84);

    addSnake(17, 7);
    addSnake(54, 34);
    addSnake(87, 24);
  }

  private int calculateTileId(int row, int col) {
    if (row % 2 == 0) {
      return 90 - (row * 9) - 9 + col + 1;
    } else {
      return 90 - (row * 9) - col;
    }
  }

  private void addLadder(int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    fromTile.setLandAction(new LadderAction(toTile));
  }

  private void addSnake(int fromTileId, int toTileId) {
    Tile fromTile = board.getTile(fromTileId);
    Tile toTile = board.getTile(toTileId);
    fromTile.setLandAction(new SnakeAction(toTile));
  }
}
