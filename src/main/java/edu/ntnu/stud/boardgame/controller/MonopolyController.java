package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;

/**
 * Controller class that handles Monopoly-specific game logic and operations.</p>
 *
 * <p>This controller acts as an intermediary between the UI components and the Monopoly game
 * model, handling various game operations such as property purchases, dice rolls, and game state
 * management. It provides methods to validate and perform player actions while maintaining game
 * rules and state.</p>
 *
 * <p>The controller contains several inner result classes that encapsulate the outcome of
 * operations with success indicators and descriptive messages.</p>
 */
public class MonopolyController {

  private final GameController gameController;
  private MonopolyGame monopolyGame;

  /**
   * Constructs a MonopolyController with a reference to the parent GameController.
   *
   * @param gameController the parent game controller that manages the overall game flow
   */
  public MonopolyController(GameController gameController) {
    this.gameController = gameController;
    updateGameReference();
  }

  /**
   * Updates the reference to the MonopolyGame from the GameController. This method should be called
   * whenever the game instance in the GameController changes.
   */
  public void updateGameReference() {
    if (gameController.getGame() instanceof MonopolyGame) {
      this.monopolyGame = (MonopolyGame) gameController.getGame();
    } else {
      this.monopolyGame = null;
    }
  }

  /**
   * Attempts to purchase the property on which the current player is standing.
   *
   * @return a PropertyPurchaseResult object containing the result of the purchase attempt and a
   * descriptive message
   */
  public PropertyPurchaseResult attemptPropertyPurchase() {
    if (monopolyGame == null) {
      return new PropertyPurchaseResult(false, "No active Monopoly game found");
    }

    Player currentPlayer = monopolyGame.getCurrentPlayer();
    if (currentPlayer == null) {
      return new PropertyPurchaseResult(false, "No current player found");
    }

    Tile currentTile = currentPlayer.getCurrentTile();
    if (currentTile == null) {
      return new PropertyPurchaseResult(false, "Player is not on any tile");
    }

    if (isPlayerBankrupt(currentPlayer)) {
      return new PropertyPurchaseResult(false, "Player is bankrupt and cannot purchase properties");
    }

    if (!canBuyProperty(currentTile)) {
      return new PropertyPurchaseResult(false, "This property cannot be purchased");
    }

    boolean success = monopolyGame.buyProperty(currentPlayer, currentTile);
    if (success) {
      String propertyName = currentTile.getName() != null ? currentTile.getName() : "Property";
      int price = getPropertyPrice(currentTile);
      return new PropertyPurchaseResult(true,
          currentPlayer.getName() + " successfully purchased " + propertyName + " for $" + price);
    } else {
      return new PropertyPurchaseResult(false, "Purchase failed - insufficient funds");
    }
  }

  /**
   * Attempts to roll the dice and complete the current player's turn.
   *
   * @return a DiceRollResult object containing the result of the dice roll attempt and a
   * descriptive message
   */
  public DiceRollResult attemptDiceRoll() {
    if (monopolyGame == null) {
      return new DiceRollResult(false, "No active Monopoly game found");
    }

    Player currentPlayer = monopolyGame.getCurrentPlayer();
    if (currentPlayer == null) {
      return new DiceRollResult(false, "No current player found");
    }

    if (isPlayerBankrupt(currentPlayer)) {
      return new DiceRollResult(false, "Current player is bankrupt and cannot roll");
    }

    boolean success = gameController.playTurn();
    return new DiceRollResult(success, success ? "Turn completed successfully" : "Turn failed");
  }

  /**
   * Attempts to restart the game.
   *
   * @return a GameRestartResult object containing the result of the restart attempt and a
   * descriptive message
   */
  public GameRestartResult attemptGameRestart() {
    boolean success = gameController.startGame();
    return new GameRestartResult(success,
        success ? "Game restarted successfully" : "Failed to restart game");
  }

  /**
   * Gets the current player's action state, indicating what actions they can perform.
   *
   * @return a PlayerActionState object with flags for possible actions and a status message
   */
  public PlayerActionState getCurrentPlayerActionState() {
    if (monopolyGame == null) {
      return new PlayerActionState(false, false, "No active game");
    }

    Player currentPlayer = monopolyGame.getCurrentPlayer();
    if (currentPlayer == null) {
      return new PlayerActionState(false, false, "No current player");
    }

    if (isPlayerBankrupt(currentPlayer)) {
      return new PlayerActionState(false, false, "Player is bankrupt");
    }

    Tile currentTile = currentPlayer.getCurrentTile();
    boolean canBuy = canBuyProperty(currentTile);

    return new PlayerActionState(true, canBuy, "Ready to play");
  }

  /**
   * Checks if the current player can buy the specified property.
   *
   * @param property the tile to check if it can be purchased
   * @return {@code true} if the property can be purchased, {@code false} otherwise
   */
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

  /**
   * Gets the price of the specified property.
   *
   * @param property the property tile to get the price for
   * @return the price of the property, or 0 if the tile is not a property
   */
  public int getPropertyPrice(Tile property) {
    if (property == null || !(property.getLandAction() instanceof PropertyAction propertyAction)) {
      return 0;
    }
    return propertyAction.getPrice();
  }

  /**
   * Gets the owner of the specified property.
   *
   * @param property the property tile to get the owner for
   * @return the Player who owns the property, or {@code null} if the property is unowned or the
   * tile is not a property
   */
  public Player getPropertyOwner(Tile property) {
    if (property == null || !(property.getLandAction() instanceof PropertyAction propertyAction)) {
      return null;
    }
    return propertyAction.getOwner();
  }

  /**
   * Gets the amount of money the specified player has.
   *
   * @param player the player to get the money for
   * @return the amount of money the player has, or 0 if the player or game is invalid
   */
  public int getPlayerMoney(Player player) {
    if (monopolyGame == null || player == null) {
      return 0;
    }
    return monopolyGame.getPlayerMoney(player);
  }

  /**
   * Checks if the specified player is bankrupt.
   *
   * @param player the player to check
   * @return {@code true} if the player is bankrupt, {@code false} otherwise or if the player or
   * game is invalid
   */
  public boolean isPlayerBankrupt(Player player) {
    if (monopolyGame == null || player == null) {
      return false;
    }
    return monopolyGame.isBankrupt(player);
  }

  /**
   * Gets the current Monopoly game instance.
   *
   * @return the current MonopolyGame instance, or {@code null} if no Monopoly game is active
   */
  public MonopolyGame getGame() {
    return monopolyGame;
  }

  /**
   * Represents the result of a property purchase attempt.
   */
  public static class PropertyPurchaseResult {

    private final boolean success;
    private final String message;

    /**
     * Creates a new property purchase result.
     *
     * @param success whether the purchase was successful
     * @param message a descriptive message about the result
     */
    public PropertyPurchaseResult(boolean success, String message) {
      this.success = success;
      this.message = message;
    }

    /**
     * @return {@code true} if the purchase was successful, {@code false} otherwise
     */
    public boolean isSuccess() {
      return success;
    }

    /**
     * @return a descriptive message about the result
     */
    public String getMessage() {
      return message;
    }
  }

  /**
   * Represents the result of a dice roll attempt.
   */
  public static class DiceRollResult {

    private final boolean success;
    private final String message;

    /**
     * Creates a new dice roll result.
     *
     * @param success whether the dice roll was successful
     * @param message a descriptive message about the result
     */
    public DiceRollResult(boolean success, String message) {
      this.success = success;
      this.message = message;
    }

    /**
     * @return {@code true} if the dice roll was successful, {@code false} otherwise
     */
    public boolean isSuccess() {
      return success;
    }

    /**
     * @return a descriptive message about the result
     */
    public String getMessage() {
      return message;
    }
  }

  /**
   * Represents the result of a game restart attempt.
   */
  public static class GameRestartResult {

    private final boolean success;
    private final String message;

    /**
     * Creates a new game restart result.
     *
     * @param success whether the restart was successful
     * @param message a descriptive message about the result
     */
    public GameRestartResult(boolean success, String message) {
      this.success = success;
      this.message = message;
    }

    /**
     * @return {@code true} if the restart was successful, {@code false} otherwise
     */
    public boolean isSuccess() {
      return success;
    }

    /**
     * @return a descriptive message about the result
     */
    public String getMessage() {
      return message;
    }
  }

  /**
   * Represents the current state of player actions, indicating what actions the player can
   * perform.
   */
  public static class PlayerActionState {

    private final boolean canRoll;
    private final boolean canBuyProperty;
    private final String statusMessage;

    /**
     * Creates a new player action state.
     *
     * @param canRoll        whether the player can roll the dice
     * @param canBuyProperty whether the player can buy the current property
     * @param statusMessage  a descriptive message about the player's current status
     */
    public PlayerActionState(boolean canRoll, boolean canBuyProperty, String statusMessage) {
      this.canRoll = canRoll;
      this.canBuyProperty = canBuyProperty;
      this.statusMessage = statusMessage;
    }

    /**
     * @return {@code true} if the player can roll the dice, {@code false} otherwise
     */
    public boolean canRoll() {
      return canRoll;
    }

    /**
     * @return {@code true} if the player can buy the current property, {@code false} otherwise
     */
    public boolean canBuyProperty() {
      return canBuyProperty;
    }

    /**
     * @return a descriptive message about the player's current status
     */
    public String getStatusMessage() {
      return statusMessage;
    }
  }
}