package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.observer.BoardGameObservable;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import java.util.List;

/**
 * Interface representing a board game. Provides methods for game management,
 * player interactions, and game state control.
 */
public interface BoardGame extends BoardGameObservable {

  /**
   * Initializes a new board for this game.
   */
  void createBoard();

  /**
   * Creates dice for this game with the specified number of dice.
   *
   * @param numberOfDice The number of dice to create
   * @throws IllegalArgumentException if numberOfDice is less than 1
   */
  void createDice(int numberOfDice);

  /**
   * Adds a player to the game.
   *
   * @param player The player to add
   * @throws IllegalArgumentException if player is null
   */
  void addPlayer(Player player);

  /**
   * Gets the list of players in this game.
   *
   * @return A defensive copy of the list of players
   */
  List<Player> getPlayers();

  /**
   * Gets the board for this game.
   *
   * @return The game board
   */
  Board getBoard();

  /**
   * Plays one complete round of the game, where each player takes their turn.
   */
  void playOneRound();

  /**
   * Plays a turn for the specified player.
   *
   * @param player The player whose turn it is
   */
  void playTurn(Player player);

  /**
   * Rolls the dice and returns the total.
   *
   * @return The total from rolling all dice
   */
  int rollDice();

  /**
   * Checks if the game is finished (a player has won).
   *
   * @return true if the game is finished, false otherwise
   */
  boolean isFinished();

  /**
   * Gets the winner of the game.
   *
   * @return The player who won the game, or null if the game is not finished
   */
  Player getWinner();

  /**
   * Plays the game until completion (a player wins).
   */
  void play();
}