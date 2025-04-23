package edu.ntnu.stud.boardgame.snakesandladders.controller;

import edu.ntnu.stud.boardgame.core.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the control panel. Handles game creation, player management, and game control.
 */
public class ControlPanelController {

  private final GameBoardController gameBoardController;
  private final List<String> availableTokens;
  private boolean gameInProgress;

  /**
   * Constructs a new control panel controller with the specified game board controller.
   *
   * @param gameBoardController The game board controller
   */
  public ControlPanelController(GameBoardController gameBoardController) {
    this.gameBoardController = gameBoardController;
    this.availableTokens = new ArrayList<>();
    this.gameInProgress = false;

    availableTokens.add("Red");
    availableTokens.add("Blue");
    availableTokens.add("Green");
    availableTokens.add("Orange");
    availableTokens.add("Purple");
  }

  /**
   * Creates a new Snakes and Ladders game.
   *
   * @return The new game
   */
  public BoardGame createNewGame() {
    BoardGame game = BoardGameFactory.createSnakesAndLaddersGame();
    gameBoardController.setGame(game);
    gameInProgress = false;
    return game;
  }

  /**
   * Creates a new Snakes and Ladders game with the specified number of dice.
   *
   * @param numberOfDice The number of dice to use
   * @return The new game
   */
  public BoardGame createNewGame(int numberOfDice) {
    BoardGame game = BoardGameFactory.createSnakesAndLaddersGame(numberOfDice);
    gameBoardController.setGame(game);
    gameInProgress = false;
    return game;
  }

  /**
   * Adds a player to the current game.
   *
   * @param name The player's name
   * @param token The player's token
   */
  public void addPlayer(String name, String token) {
    if (gameInProgress) {
      throw new IllegalStateException("Cannot add players after the game has started");
    }

    Player player = new Player(name, token);
    gameBoardController.getGame().addPlayer(player);
  }

  /**
   * Starts the game.
   */
  public void startGame() {
    BoardGame game = gameBoardController.getGame();
    if (game.getPlayers().isEmpty()) {
      throw new IllegalStateException("Cannot start a game with no players");
    }

    gameInProgress = true;
  }

  /**
   * Rolls the dice and takes a turn for the current player.
   *
   * @return The dice roll result
   */
  public int rollDiceAndTakeTurn() {
    if (!gameInProgress) {
      throw new IllegalStateException("Cannot take a turn before the game has started");
    }

    gameBoardController.takeTurn();
    return gameBoardController.getGame().rollDice();
  }

  /**
   * Checks if the game is in progress.
   *
   * @return true if the game is in progress, false otherwise
   */
  public boolean isGameInProgress() {
    return gameInProgress;
  }

  /**
   * Checks if the game is finished.
   *
   * @return true if the game is finished, false otherwise
   */
  public boolean isGameFinished() {
    return gameBoardController.getGame().isFinished();
  }

  /**
   * Gets the winner of the game.
   *
   * @return The winner, or null if the game is not finished
   */
  public Player getWinner() {
    return gameBoardController.getGame().getWinner();
  }

  /**
   * Gets the list of available tokens.
   *
   * @return The list of available tokens
   */
  public List<String> getAvailableTokens() {
    return new ArrayList<>(availableTokens);
  }

  /**
   * Gets the game board controller.
   *
   * @return The game board controller
   */
  public GameBoardController getGameBoardController() {
    return gameBoardController;
  }
}