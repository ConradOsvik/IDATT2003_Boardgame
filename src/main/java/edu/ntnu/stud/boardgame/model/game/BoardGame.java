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

public abstract class BoardGame {

  protected static final Logger LOGGER = Logger.getLogger(BoardGame.class.getName());

  protected final List<BoardGameObserver> observers;
  protected final List<Player> players;
  protected Board board;
  protected Dice dice;
  protected int currentPlayerIndex;
  protected Player currentPlayer;
  protected Player winner;
  protected boolean gameOver;

  public BoardGame() {
    this.observers = new ArrayList<>();
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.gameOver = false;
  }

  public void createDice(int numberOfDice) {
    this.dice = new Dice(numberOfDice);
  }

  public void addPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    players.add(player);

    notifyObservers(new PlayerAddedEvent(player));
  }

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
      player.placeOnTile(startTile);
    }

    currentPlayerIndex = 0;
    currentPlayer = players.get(currentPlayerIndex);
    gameOver = false;
    winner = null;

    notifyObservers(new GameStartedEvent(currentPlayer, players, board));
  }

  public abstract void playTurn();

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

  protected void endGame(Player winner) {
    this.winner = winner;
    this.gameOver = true;

    notifyObservers(new PlayerWonEvent(winner));
    notifyObservers(new GameEndedEvent(winner));
  }

  public void registerObserver(BoardGameObserver observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  public void registerObservers(List<BoardGameObserver> observers) {
    for (BoardGameObserver observer : observers) {
      registerObserver(observer);
    }
  }

  protected void notifyObservers(GameEvent event) {
    String eventType = event.getClass().getSimpleName();
    LOGGER.info(
        String.format("Notifying %d observers about event: %s", observers.size(), eventType));

    for (BoardGameObserver observer : observers) {
      observer.onGameEvent(event);
    }
  }

  public void notifyGameCreated() {
    notifyObservers(new GameCreatedEvent(board, players));
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public Player getWinner() {
    return winner;
  }

  public Dice getDice() {
    return dice;
  }
}