package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Dice;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.GameStartedEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class BoardGame {

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

  public abstract void createBoard();

  public void createDice(int numberOfDice) {
    this.dice = new Dice(numberOfDice);
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void startGame() {
    if (players.isEmpty()) {
      throw new IllegalStateException("No players have been added to the game");
    }

    if (board == null) {
      throw new IllegalStateException("Board has not been created");
    }

    if (dice == null) {
      throw new IllegalStateException("Dice has not been created");
    }

    currentPlayerIndex = 0;
    currentPlayer = players.get(currentPlayerIndex);
    gameOver = false;
    winner = null;

    notifyObservers(new GameStartedEvent());
  }

  protected void notifyObservers(GameEvent event) {
    for (BoardGameObserver observer : observers) {
      observer.onGameEvent(event);
    }
  }

  public abstract void playTurn();

  protected void nextTurn() {
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

    notifyObservers(new TurnChangedEvent());
  }

  protected void registerObserver(BoardGameObserver observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
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

  public Board getBoard() {
    return board;
  }

  public Dice getDice() {
    return dice;
  }
}
