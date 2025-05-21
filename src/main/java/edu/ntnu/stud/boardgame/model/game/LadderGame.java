package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.event.BounceBackEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.SnakeEncounteredEvent;
import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import java.util.logging.Logger;

public class LadderGame extends BoardGame {

  private static final Logger LOGGER = Logger.getLogger(LadderGame.class.getName());

  @Override
  public void playTurn() {
    if (gameOver) {
      return;
    }

    int steps = dice.roll();
    notifyObservers(new DiceRolledEvent(steps, currentPlayer));

    Tile currentTile = currentPlayer.getCurrentTile();
    if (currentTile == null) {
      throw new InvalidGameStateException(
          "Player " + currentPlayer.getName() + " is not on any tile. Cannot play turn.");
    }

    Tile endTile = board.getTile(board.getEndTileId());
    if (endTile == null) {
      throw new InvalidGameStateException(
          "End tile (ID: " + board.getEndTileId() + ") not found on the board. Cannot determine game end.");
    }

    int targetTileId = currentTile.getTileId() + steps;

    if (targetTileId > endTile.getTileId()) {
      Tile beforeTile = currentPlayer.getCurrentTile();
      currentPlayer.setCurrentTile(endTile);
      notifyObservers(new PlayerMovedEvent(currentPlayer, beforeTile, endTile, steps, board));

      int overshoot = targetTileId - endTile.getTileId();
      int bouncedTileId = endTile.getTileId() - overshoot;
      Tile bouncedTile = board.getTile(bouncedTileId);

      if (bouncedTile == null) {
        throw new InvalidGameStateException(
            "Bounced to a non-existent tile ID: " + bouncedTileId + ". Board may be invalid.");
      }

      currentPlayer.setCurrentTile(bouncedTile);
      notifyObservers(new BounceBackEvent(currentPlayer, endTile, bouncedTile, 0, board));

      triggerLandAction(bouncedTile);

    } else {
      Tile targetTile = currentPlayer.getDestinationTile(steps);
      if (targetTile == null) {
        throw new InvalidGameStateException(
            "Player " + currentPlayer.getName() + " attempted to move to a null tile from tile ID: "
                + currentTile.getTileId() + " with steps: " + steps);
      }
      Tile beforeTile = currentPlayer.getCurrentTile();

      currentPlayer.setCurrentTile(targetTile);
      notifyObservers(new PlayerMovedEvent(currentPlayer, beforeTile, targetTile, steps, board));

      triggerLandAction(targetTile);
    }

    Tile finalPlayerTile = currentPlayer.getCurrentTile();
    if (finalPlayerTile != null && finalPlayerTile.equals(endTile)) {
      endGame(currentPlayer);
    }
  }

  private void triggerLandAction(Tile targetTile) {
    if (targetTile == null) {
      LOGGER.warning("triggerLandAction called with a null targetTile.");
      return;
    }
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