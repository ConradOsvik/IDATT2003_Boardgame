package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import java.util.logging.Logger;

public class MonopolyController {

  private static final Logger LOGGER = Logger.getLogger(MonopolyController.class.getName());

  private final GameController gameController;
  private MonopolyGame monopolyGame;

  public MonopolyController(GameController gameController) {
    this.gameController = gameController;
    updateGameReference();
  }

  public void updateGameReference() {
    if (gameController.getGame() instanceof MonopolyGame) {
      this.monopolyGame = (MonopolyGame) gameController.getGame();
    } else {
      this.monopolyGame = null;
    }
  }

  public boolean buyProperty(Tile property) {
    if (monopolyGame == null) {
      LOGGER.warning("Cannot buy property: monopoly game is null");
      return false;
    }

    if (property == null) {
      LOGGER.warning("Cannot buy property: tile property is null");
      return false;
    }

    Player currentPlayer = monopolyGame.getCurrentPlayer();
    if (currentPlayer == null) {
      LOGGER.warning("Cannot buy property: current player is null");
      return false;
    }

    return monopolyGame.buyProperty(currentPlayer, property);
  }

  public boolean canBuyProperty(Tile property) {
    if (monopolyGame == null || property == null) {
      return false;
    }

    Player currentPlayer = monopolyGame.getCurrentPlayer();
    if (currentPlayer == null) {
      return false;
    }

    if (!(property.getLandAction() instanceof PropertyAction propertyAction)) {
      return false;
    }

    return propertyAction.getOwner() == null &&
        monopolyGame.getPlayerMoney(currentPlayer) >= getPropertyPrice(property);
  }

  public int getPropertyPrice(Tile property) {
    if (property == null || !(property.getLandAction() instanceof PropertyAction propertyAction)) {
      return 0;
    }
    return propertyAction.getPrice();
  }

  public Player getPropertyOwner(Tile property) {
    if (property == null || !(property.getLandAction() instanceof PropertyAction propertyAction)) {
      return null;
    }
    return propertyAction.getOwner();
  }

  public int getPlayerMoney(Player player) {
    if (monopolyGame == null || player == null) {
      return 0;
    }
    return monopolyGame.getPlayerMoney(player);
  }

  public boolean isPlayerBankrupt(Player player) {
    if (monopolyGame == null || player == null) {
      return false;
    }
    return monopolyGame.isBankrupt(player);
  }

  public void rollDice() {
    gameController.playTurn();
  }

  public MonopolyGame getGame() {
    return monopolyGame;
  }
}