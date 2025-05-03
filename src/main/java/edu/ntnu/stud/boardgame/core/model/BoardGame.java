package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.exception.GameNotInitializedException;
import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.IllegalGameStateException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObservable;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.GameEvent.EventType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Interface representing a board game. Provides methods for game management, player interactions,
 * and game state control.
 */
public abstract class BoardGame implements BoardGameObservable {

  private static final Logger LOGGER = Logger.getLogger(BoardGame.class.getName());

  protected Board board;
  protected Dice dice;
  protected final List<Player> players = new ArrayList<>();
  protected Player winner;
  protected boolean finished = false;
  protected final List<BoardGameObserver> observers = new ArrayList<>();

  public void init() {
    GameEvent event = new GameEvent(EventType.GAME_CREATED);
    event.addData("board", board);
    notifyObservers(event);
  }

  public abstract void createBoard();

  public void createDice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("Number of dice must be at least 1");
    }
    this.dice = new Dice(numberOfDice);
  }

  public void addPlayer(Player player) {
    if (player == null) {
      throw new InvalidPlayerException("Player cannot be null");
    }

    players.add(player);

    if (board != null) {
      Tile startingTile = board.getStartingTile();
      if (startingTile != null) {
        player.setStartingTile(startingTile);
      }
    }

    GameEvent event = new GameEvent(EventType.PLAYER_ADDED);
    event.addData("player", player);
    notifyObservers(event);
  }

  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  public Board getBoard() {
    return board;
  }

  public abstract void playTurn(Player player);

  public void playOneRound() {
    if (isFinished()) {
      throw new GameOverException("Game is already finished");
    }

    for (Player player : players) {
      if (isFinished()) {
        break;
      }
      playTurn(player);
    }
  }

  public void play() {
    if (players.isEmpty() || dice == null || board == null) {
      throw new GameNotInitializedException("Game not properly initialized");
    }

    startGame();

    while (!isFinished()) {
      playOneRound();
    }
  }

  public int rollDice() {
    if (dice == null) {
      throw new GameNotInitializedException("Dice have not been initialized");
    }

    int result = dice.roll();

    // Notify observers about the dice roll
    GameEvent diceEvent = new GameEvent(EventType.DICE_ROLLED);
    diceEvent.addData("result", result);
    notifyObservers(diceEvent);

    return result;
  }

  public boolean isFinished() {
    return finished;
  }

  public Optional<Player> getWinner() {
    return Optional.ofNullable(winner);
  }

  public void startGame() {
    if (board == null || dice == null) {
      throw new GameNotInitializedException("Game board or dice not initialized");
    }

    if (players.isEmpty()) {
      throw new IllegalGameStateException("Cannot start a game with no players");
    }

    GameEvent startEvent = new GameEvent(EventType.GAME_STARTED);
    startEvent.addData("players", players);
    notifyObservers(startEvent);
  }

  public void reset() {
    this.players.clear();
    this.winner = null;
    this.finished = false;

    GameEvent event = new GameEvent(EventType.GAME_RESET);
    event.addData("board", board);
    notifyObservers(event);
  }

  public void addObserver(BoardGameObserver observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
      LOGGER.info("Observer added, total: " + observers.size());
    }
  }

  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers(GameEvent event) {
    LOGGER.info(
        "Notifying " + observers.size() + " observers about event: " + event.getEventType());
    for (BoardGameObserver observer : observers) {
      observer.onGameEvent(event);
    }
  }

  public void transferObserversFrom(BoardGame other) {
    if (other != null) {
      List<BoardGameObserver> observersToTransfer = new ArrayList<>(other.observers);
      for (BoardGameObserver observer : observersToTransfer) {
        this.addObserver(observer);
      }
    }
  }
}