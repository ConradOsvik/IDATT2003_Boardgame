package edu.ntnu.stud.boardgame.model.action.registry;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import java.util.logging.Logger;

/**
 * Singleton registry for managing Monopoly game actions.
 *
 * <p>Maintains the current game instance and handles execution of property, tax, and bonus actions.
 * Validates inputs and logs warnings for invalid operations.
 *
 * @see MonopolyGame
 * @see PropertyAction
 */
public class MonopolyActionRegistry {
  private static final Logger LOGGER = Logger.getLogger(MonopolyActionRegistry.class.getName());
  private static MonopolyActionRegistry instance;
  private MonopolyGame currentGame;

  private MonopolyActionRegistry() {}

  /**
   * Gets the singleton instance of the registry.
   *
   * @return the singleton instance
   */
  public static synchronized MonopolyActionRegistry getInstance() {
    if (instance == null) {
      instance = new MonopolyActionRegistry();
    }
    return instance;
  }

  /**
   * Registers a game instance for action execution.
   *
   * @param game the game to register
   * @throws IllegalArgumentException if game is null
   */
  public void registerGame(MonopolyGame game) {
    if (game == null) {
      throw new IllegalArgumentException("Cannot register a null game in MonopolyActionRegistry.");
    }
    this.currentGame = game;
  }

  /** Clears the current game instance. */
  public void clearGame() {
    this.currentGame = null;
  }

  /**
   * Executes a property action for a player.
   *
   * <p>Handles rent payments between players. Rent is calculated as 1/5 of the property price.
   *
   * @param player the player performing the action
   * @param action the property action to execute
   */
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

  /**
   * Executes a tax payment for a player.
   *
   * @param player the player paying tax
   * @param amount the tax amount (must be non-negative)
   */
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

  /**
   * Executes a start bonus payment for a player.
   *
   * @param player the player receiving the bonus
   * @param amount the bonus amount (must be non-negative)
   */
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
