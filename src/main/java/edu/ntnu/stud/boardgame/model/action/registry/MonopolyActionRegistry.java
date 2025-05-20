package edu.ntnu.stud.boardgame.model.action.registry;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;

public class MonopolyActionRegistry {
  private static MonopolyActionRegistry instance;
  private MonopolyGame currentGame;

  private MonopolyActionRegistry() {
  }

  public static synchronized MonopolyActionRegistry getInstance() {
    if (instance == null) {
      instance = new MonopolyActionRegistry();
    }
    return instance;
  }

  public void registerGame(MonopolyGame game) {
    this.currentGame = game;
  }

  public void clearGame() {
    this.currentGame = null;
  }

  public void executePropertyAction(Player player, PropertyAction action) {
    if (currentGame == null) {
      return;
    }

    if (action.getOwner() != null && action.getOwner() != player) {
      int rent = action.getPrice() / 5;
      currentGame.payRent(player, action.getOwner(), rent);
    }
  }

  public void executeTaxAction(Player player, int amount) {
    if (currentGame == null) {
      return;
    }

    currentGame.payTax(player, amount);
  }

  public void executeStartAction(Player player, int amount) {
    if (currentGame == null) {
      return;
    }

    currentGame.receiveStartMoney(player, amount);
  }
}