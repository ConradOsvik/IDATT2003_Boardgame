package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.event.BounceBackEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.SnakeEncounteredEvent;

public class LadderGame extends BoardGame {

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
        if (targetTile.getLandAction() instanceof edu.ntnu.stud.boardgame.model.action.LadderAction) {
          notifyObservers(
              new LadderClimbedEvent(currentPlayer, beforeActionTile, afterActionTile, 0, board));
        } else if (targetTile.getLandAction() instanceof edu.ntnu.stud.boardgame.model.action.SnakeAction) {
          notifyObservers(
              new SnakeEncounteredEvent(currentPlayer, beforeActionTile, afterActionTile, 0,
                  board));
        }
      }
    }
  }
}