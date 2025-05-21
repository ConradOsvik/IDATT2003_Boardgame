package edu.ntnu.stud.boardgame.model.action.registry;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import java.util.logging.Logger;

public class MonopolyActionRegistry {
  private static final Logger LOGGER = Logger.getLogger(MonopolyActionRegistry.class.getName());
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
    if (game == null) {
      throw new IllegalArgumentException("Cannot register a null game in MonopolyActionRegistry.");
    }
    this.currentGame = game;
  }

  public void clearGame() {
    this.currentGame = null;
  }

  public void executePropertyAction(Player player, PropertyAction action) {
    if (currentGame == null) {
      LOGGER.warning("Attempted to execute property action with no game registered.");
      return;
    }
    if (player == null) {
      LOGGER.warning("Attempted to execute property action with a null player.");
      return;
    }
    if (action == null) {
      LOGGER.warning("Attempted to execute property action with a null action object.");
      return;
    }

    if (action.getOwner() != null && action.getOwner() != player) {
      int rent = action.getPrice() / 5;
      currentGame.payRent(player, action.getOwner(), rent);
    }
  }

  public void executeTaxAction(Player player, int amount) {
    if (currentGame == null) {
      LOGGER.warning("Attempted to execute tax action with no game registered.");
      return;
    }
    if (player == null) {
      LOGGER.warning("Attempted to execute tax action with a null player.");
      return;
    }
    if (amount < 0) {
      LOGGER.warning("Attempted to execute tax action with a negative amount: " + amount);
      return;
    }

    currentGame.payTax(player, amount);
  }

  public void executeStartAction(Player player, int amount) {
    if (currentGame == null) {
      LOGGER.warning("Attempted to execute start action with no game registered.");
      return;
    }
    if (player == null) {
      LOGGER.warning("Attempted to execute start action with a null player.");
      return;
    }
    if (amount < 0) {
      LOGGER.warning("Attempted to execute start action with a negative amount: " + amount);
      return;
    }

    currentGame.receiveStartMoney(player, amount);
  }
}