package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Dice;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
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

  public abstract Board createDefaultBoard();

  public void createDice(int numberOfDice) {
    this.dice = new Dice(numberOfDice);
  }

  public void addPlayer(Player player) {
    players.add(player);
    int startTileId = board.getStartTileId();
    Tile startTile = board.getTile(startTileId);
    player.placeOnTile(startTile);
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

    currentPlayerIndex = 0;
    currentPlayer = players.get(currentPlayerIndex);
    gameOver = false;
    winner = null;

    notifyObservers(new GameStartedEvent());
  }

  public void restartGame() {
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

    notifyObservers(new TurnChangedEvent());
  }

  public void registerObserver(BoardGameObserver observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  protected void notifyObservers(GameEvent event) {
    for (BoardGameObserver observer : observers) {
      observer.onGameEvent(event);
    }
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
