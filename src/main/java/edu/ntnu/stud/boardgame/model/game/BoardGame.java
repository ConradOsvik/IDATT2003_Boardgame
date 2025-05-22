package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Dice;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.GameCreatedEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.observer.event.GameStartedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerWonEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract base class for board games.
 *
 * <p>Provides common functionality for managing players, turns, game state, and observer
 * notifications.
 */
public abstract class BoardGame {

  /** Logger for the BoardGame class. */
  protected static final Logger LOGGER = Logger.getLogger(BoardGame.class.getName());

  /** List of observers registered to receive game events. */
  protected final List<BoardGameObserver> observers;

  /** List of players participating in the game. */
  protected final List<Player> players;

  /** The game board containing tiles and game state. */
  protected Board board;

  /** The dice used for player moves. */
  protected Dice dice;

  /** Index of the current player in the players list. */
  protected int currentPlayerIndex;

  /** Reference to the current player whose turn it is. */
  protected Player currentPlayer;

  /** Reference to the player who won the game, if any. */
  protected Player winner;

  /** Flag indicating whether the game has ended. */
  protected boolean gameOver;

  /** Constructs a new BoardGame with default values. */
  public BoardGame() {
    this.observers = new ArrayList<>();
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.gameOver = false;
  }

  /**
   * Creates dice for the game.
   *
   * @param numberOfDice number of dice to create
   */
  public void createDice(int numberOfDice) {
    this.dice = new Dice(numberOfDice);
  }

  /**
   * Adds a player to the game.
   *
   * @param player the player to add
   * @throws IllegalArgumentException if player is null
   */
  public void addPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    players.add(player);

    notifyObservers(new PlayerAddedEvent(player));
  }

  /**
   * Starts the game by placing players on the start tile.
   *
   * @throws InvalidGameStateException if game setup is incomplete
   */
  public void startGame() {
    if (players.isEmpty()) {
      throw new InvalidGameStateException("No players have been added to the game");
    }

    if (board == null) {
      throw new InvalidGameStateException("Board has not been created");
    }

    if (dice == null) {
      throw new InvalidGameStateException("Dice has not been created");
    }

    for (Player player : players) {
      Tile startTile = board.getTile(board.getStartTileId());
      if (startTile == null) {
        throw new InvalidGameStateException(
            "Start tile (ID: " + board.getStartTileId() + ") not found on the board.");
      }
      player.placeOnTile(startTile);
    }

    currentPlayerIndex = 0;
    currentPlayer = players.get(currentPlayerIndex);
    gameOver = false;
    winner = null;

    notifyObservers(new GameStartedEvent(currentPlayer, players, board));
  }

  /** Executes a turn for the current player. */
  public abstract void playTurn();

  /** Advances to the next player's turn. */
  public void nextTurn() {
    if (gameOver) {
      return;
    }

    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    currentPlayer = players.get(currentPlayerIndex);

    if (currentPlayer.shouldSkipNextTurn()) {
      currentPlayer.setSkipNextTurn(false);
      nextTurn();
      return;
    }

    notifyObservers(new TurnChangedEvent(currentPlayer));
  }

  /**
   * Ends the game with a winner.
   *
   * @param winner the winning player
   */
  protected void endGame(Player winner) {
    this.winner = winner;
    this.gameOver = true;

    notifyObservers(new PlayerWonEvent(winner));
    notifyObservers(new GameEndedEvent(winner));
  }

  /**
   * Registers a single observer.
   *
   * @param observer the observer to register
   */
  public void registerObserver(BoardGameObserver observer) {
    if (observer == null) {
      LOGGER.warning("Attempted to register a null observer.");
      return;
    }
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  /**
   * Registers multiple observers.
   *
   * @param observers list of observers to register
   */
  public void registerObservers(List<BoardGameObserver> observers) {
    if (observers == null) {
      LOGGER.warning("Attempted to register a null list of observers.");
      return;
    }
    for (BoardGameObserver observer : observers) {
      registerObserver(observer);
    }
  }

  /**
   * Notifies all observers of a game event.
   *
   * @param event the event to notify about
   */
  protected void notifyObservers(GameEvent event) {
    if (event == null) {
      LOGGER.warning("Attempted to notify observers with a null event.");
      return;
    }
    String eventType = event.getClass().getSimpleName();
    LOGGER.info(
        String.format("Notifying %d observers about event: %s", observers.size(), eventType));

    for (BoardGameObserver observer : observers) {
      observer.onGameEvent(event);
    }
  }

  /** Notifies observers that a new game has been created. */
  public void notifyGameCreated() {
    notifyObservers(new GameCreatedEvent(board, players));
  }

  /**
   * Gets the game board.
   *
   * @return the current game board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Sets the game board.
   *
   * @param board the board to set
   * @throws IllegalArgumentException if board is null
   */
  public void setBoard(Board board) {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be set to null.");
    }
    this.board = board;
  }

  /**
   * Gets a copy of the player list.
   *
   * @return list of players
   */
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  /**
   * Gets the current player whose turn it is.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Checks if the game has ended.
   *
   * @return true if the game is over, false otherwise
   */
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * Gets the winning player.
   *
   * @return the player who won the game, or null if the game is not over
   */
  public Player getWinner() {
    return winner;
  }

  /**
   * Gets the dice used in the game.
   *
   * @return the game's dice
   */
  public Dice getDice() {
    return dice;
  }
}
