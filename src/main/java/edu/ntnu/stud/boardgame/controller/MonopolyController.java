package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;

public class MonopolyController {

  private final GameController gameController;
  private MonopolyGame monopolyGame;

  public MonopolyController(GameController gameController) {
    this.gameController = gameController;

    if (gameController.getGame() instanceof MonopolyGame) {
      this.monopolyGame = (MonopolyGame) gameController.getGame();
    }
  }

  public boolean buyProperty(Player player, Tile property) {
    if (monopolyGame == null) {
      return false;
    }
    return monopolyGame.buyProperty(player, property);
  }

  public boolean canBuyProperty(Player player, Tile property) {
    if (monopolyGame == null) {
      return false;
    }

    PropertyAction propertyAction = getPropertyAction(property);
    if (propertyAction == null) {
      return false;
    }

    return propertyAction.getOwner() == null &&
        monopolyGame.getPlayerMoney(player) >= propertyAction.getPrice();
  }

  public int getPropertyPrice(Tile property) {
    PropertyAction propertyAction = getPropertyAction(property);
    if (propertyAction == null) {
      return 0;
    }

    return propertyAction.getPrice();
  }

  public Player getPropertyOwner(Tile property) {
    PropertyAction propertyAction = getPropertyAction(property);
    if (propertyAction == null) {
      return null;
    }

    return propertyAction.getOwner();
  }

  private PropertyAction getPropertyAction(Tile property) {
    if (property == null || property.getLandAction() == null) {
      return null;
    }

    if (property.getLandAction() instanceof PropertyAction) {
      return (PropertyAction) property.getLandAction();
    }

    return null;
  }

  public int getPlayerMoney(Player player) {
    if (monopolyGame == null) {
      return 0;
    }
    return monopolyGame.getPlayerMoney(player);
  }

  public boolean isPlayerBankrupt(Player player) {
    if (monopolyGame == null) {
      return false;
    }
    return monopolyGame.isBankrupt(player);
  }

  public void rollDice() {
    gameController.playTurn();
  }

  public GameController getGameController() {
    return gameController;
  }

  public MonopolyGame getGame() {
    return monopolyGame;
  }

  public void updateGameReference() {
    if (gameController.getGame() instanceof MonopolyGame) {
      this.monopolyGame = (MonopolyGame) gameController.getGame();
    }
  }
}