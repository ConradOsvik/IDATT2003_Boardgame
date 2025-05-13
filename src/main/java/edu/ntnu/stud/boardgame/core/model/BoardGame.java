package edu.ntnu.stud.boardgame.core.model;

import edu.ntnu.stud.boardgame.core.exception.GameNotInitializedException;
import edu.ntnu.stud.boardgame.core.exception.IllegalGameStateException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObservable;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.DiceRolledEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameRestartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameStartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Interface representing a board game. Provides methods for game management, player interactions,
 * and game state control.
 */
public abstract class BoardGame extends BaseModel implements BoardGameObservable {

  private static final Logger LOGGER = Logger.getLogger(BoardGame.class.getName());
  protected final List<Player> players = new ArrayList<>();
  protected final List<BoardGameObserver> observers = new ArrayList<>();
  protected Board board;
  protected Dice dice;
  protected Player winner;
  protected boolean finished = false;
  protected int currentPlayerIndex = 0;

  public void init() {
    GameEvent event = new GameCreatedEvent(board);
    notifyObservers(event);
  }

  public void notifyObservers(GameEvent event) {
    LOGGER.info(
        "Notifying " + observers.size() + " observers about event: " + event.getEventType());
    for (BoardGameObserver observer : observers) {
      observer.onGameEvent(event);
    }
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

    GameEvent event = new PlayerAddedEvent(player);
    notifyObservers(event);
  }

  public Board getBoard() {
    return board;
  }

  public Dice getDice() {
    return dice;
  }

  public void playCurrentTurn() {
    Player currentPlayer = getCurrentPlayer();
    if (currentPlayer == null) {
      throw new IllegalGameStateException("No current player to take a turn");
    }

    playTurn(currentPlayer);
  }

  protected Player getCurrentPlayer() {
    if (players.isEmpty()) {
      throw new IllegalGameStateException("No players in the game");
    }

    if (currentPlayerIndex >= players.size()) {
      currentPlayerIndex = 0;
    }

    return players.get(currentPlayerIndex);
  }

  public abstract void playTurn(Player player);

  public int rollDice() {
    if (dice == null) {
      throw new GameNotInitializedException("Dice have not been initialized");
    }

    int result = dice.roll();

    // Notify observers about the dice roll
    GameEvent diceEvent = new DiceRolledEvent(result);
    notifyObservers(diceEvent);

    return result;
  }

  public boolean isFinished() {
    return finished;
  }

  public Optional<Player> getWinner() {
    return Optional.ofNullable(winner);
  }

  public void start() {
    if (board == null || dice == null) {
      throw new GameNotInitializedException("Game board or dice not initialized");
    }

    if (players.isEmpty()) {
      throw new IllegalGameStateException("Cannot start a game with no players");
    }

    GameEvent startEvent = new GameStartedEvent(players);
    notifyObservers(startEvent);
  }

  public void reset() {
    this.players.clear();
    this.winner = null;
    this.finished = false;
    this.currentPlayerIndex = 0;

    GameEvent event = new GameResetEvent(board);
    notifyObservers(event);
  }

  public void restart() {
    this.winner = null;
    this.finished = false;
    this.currentPlayerIndex = 0;

    for (Player player : players) {
      player.setStartingTile(board.getStartingTile());
    }

    GameEvent event = new GameRestartedEvent();
    notifyObservers(event);
  }

  public void addObservers(List<BoardGameObserver> observers) {
    for (BoardGameObserver observer : observers) {
      addObserver(observer);
    }
  }

  public void addObserver(BoardGameObserver observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
      LOGGER.info(
          "Observer " + observer.getClass().getSimpleName() + " added, total: " + observers.size());
    }
  }

  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    BoardGame other = (BoardGame) obj;

    if (finished != other.finished) {
      return false;
    }
    if (!Objects.equals(board, other.board)) {
      return false;
    }
    if (!Objects.equals(getPlayers(), other.getPlayers())) {
      return false;
    }
    return Objects.equals(winner, other.winner);
  }

  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  @Override
  public int hashCode() {
    int result = board != null ? board.hashCode() : 0;
    result = 31 * result + (finished ? 1 : 0);
    result = 31 * result + (winner != null ? winner.hashCode() : 0);
    result = 31 * result + getPlayers().hashCode();
    result = 31 * result + (dice != null ? dice.hashCode() : 0);
    return result;
  }
}