package edu.ntnu.stud.boardgame.snakesandladders.controller;

import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.snakesandladders.model.SLBoard;

import java.util.List;

/**
 * Controller for the game board. Handles interactions between the view and the model.
 */
public class GameBoardController {

  private BoardGame game;
  private int currentPlayerIndex;

  /**
   * Constructs a new game board controller with the specified game.
   *
   * @param game The board game
   */
  public GameBoardController(BoardGame game) {
    this.game = game;
    this.currentPlayerIndex = 0;
  }

  /**
   * Gets the current board game.
   *
   * @return The board game
   */
  public BoardGame getGame() {
    return game;
  }

  /**
   * Sets the current board game.
   *
   * @param game The board game to set
   */
  public void setGame(BoardGame game) {
    BoardGame oldGame = this.game;
    this.game = game;
    this.currentPlayerIndex = 0;

    if (oldGame != null && game != null) {
      game.transferObserversFrom(oldGame);
    }
  }

  /**
   * Gets the current player whose turn it is.
   *
   * @return The current player, or null if there are no players
   */
  public Player getCurrentPlayer() {
    List<Player> players = game.getPlayers();
    if (players.isEmpty()) {
      return null;
    }
    return players.get(currentPlayerIndex);
  }

  /**
   * Takes a turn for the current player.
   */
  public void takeTurn() {
    if (game.isFinished()) {
      return;
    }

    Player currentPlayer = getCurrentPlayer();
    if (currentPlayer != null) {
      game.playTurn(currentPlayer);
      nextPlayer();
    }
  }

  /**
   * Moves to the next player.
   */
  private void nextPlayer() {
    List<Player> players = game.getPlayers();
    if (!players.isEmpty()) {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
  }

  /**
   * Gets the Snakes and Ladders board.
   *
   * @return The SL board, or null if the game doesn't have an SL board
   */
  public SLBoard getSLBoard() {
    if (game.getBoard() instanceof SLBoard) {
      return (SLBoard) game.getBoard();
    }
    return null;
  }
}