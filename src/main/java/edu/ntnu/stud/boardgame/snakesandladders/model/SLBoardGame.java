package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Dice;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObservable;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.GameEvent.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the BoardGame interface for the Snakes and Ladders game.
 */
public class SLBoardGame implements BoardGame {

  private SLBoard board;
  private Dice dice;
  private final List<Player> players;
  private Player winner;
  private boolean finished;
  private final List<BoardGameObserver> observers;

  /**
   * Constructs a new Snakes and Ladders game.
   */
  public SLBoardGame() {
    this.players = new ArrayList<>();
    this.observers = new ArrayList<>();
    this.finished = false;
  }

  @Override
  public void createBoard() {
    this.board = new SLBoard();
    this.board.initializeBoard();
  }

  @Override
  public void createDice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("Number of dice must be at least 1");
    }
    this.dice = new Dice(numberOfDice);
  }

  @Override
  public void addPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    players.add(player);

    if (board != null) {
      Tile startingTile = board.getStartingTile();
      if (startingTile != null) {
        player.placeOnTile(startingTile);
      }
    }

    GameEvent event = new GameEvent(EventType.PLAYER_ADDED);
    event.addData("player", player);
    notifyObservers(event);
  }

  @Override
  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public void playOneRound() {
    if (isFinished()) {
      return;
    }

    for (Player player : players) {
      if (isFinished()) {
        break;
      }
      playTurn(player);
    }
  }

  @Override
  public void playTurn(Player player) {
    if (isFinished()) {
      return;
    }

    GameEvent turnEvent = new GameEvent(EventType.TURN_CHANGED);
    turnEvent.addData("player", player);
    notifyObservers(turnEvent);

    int steps = rollDice();

    Tile previousTile = player.getCurrentTile();
    player.move(steps);

    GameEvent moveEvent = new GameEvent(EventType.PLAYER_MOVED);
    moveEvent.addData("player", player);
    moveEvent.addData("from", previousTile);
    moveEvent.addData("to", player.getCurrentTile());
    moveEvent.addData("steps", steps);
    notifyObservers(moveEvent);

    if (previousTile != player.getCurrentTile() && previousTile.getTileId() + steps != player.getCurrentTile().getTileId()) {
      if (player.getCurrentTile().getTileId() > previousTile.getTileId() + steps) {
        GameEvent ladderEvent = new GameEvent(EventType.LADDER_CLIMBED);
        ladderEvent.addData("player", player);
        ladderEvent.addData("from", previousTile);
        ladderEvent.addData("to", player.getCurrentTile());
        notifyObservers(ladderEvent);
      } else if (player.getCurrentTile().getTileId() < previousTile.getTileId() + steps) {
        GameEvent snakeEvent = new GameEvent(EventType.SNAKE_ENCOUNTERED);
        snakeEvent.addData("player", player);
        snakeEvent.addData("from", previousTile);
        snakeEvent.addData("to", player.getCurrentTile());
        notifyObservers(snakeEvent);
      }
    }

    if (board.isLastTile(player.getCurrentTile())) {
      this.winner = player;
      this.finished = true;

      GameEvent winEvent = new GameEvent(EventType.PLAYER_WON);
      winEvent.addData("player", player);
      notifyObservers(winEvent);

      GameEvent endEvent = new GameEvent(EventType.GAME_ENDED);
      endEvent.addData("winner", player);
      notifyObservers(endEvent);
    }
  }

  @Override
  public int rollDice() {
    int result = dice.roll();

    GameEvent diceEvent = new GameEvent(EventType.DICE_ROLLED);
    diceEvent.addData("result", result);
    notifyObservers(diceEvent);

    return result;
  }

  @Override
  public boolean isFinished() {
    return finished;
  }

  @Override
  public Player getWinner() {
    return winner;
  }

  @Override
  public void play() {
    if (players.isEmpty() || dice == null || board == null) {
      throw new IllegalStateException("Game not properly initialized");
    }

    GameEvent startEvent = new GameEvent(EventType.GAME_STARTED);
    startEvent.addData("players", players);
    notifyObservers(startEvent);

    while (!isFinished()) {
      playOneRound();
    }
  }

  @Override
  public void addObserver(BoardGameObserver observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
      System.out.println("Observer added, total: " + observers.size());
    }
  }

  @Override
  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers(GameEvent event) {
    System.out.println("Notifying " + observers.size() + " observers about event: " + event.getEventType());
    for (BoardGameObserver observer : observers) {
      observer.onGameEvent(event);
    }
  }

  @Override
  public void transferObserversFrom(BoardGameObservable other) {
    if (other instanceof SLBoardGame otherGame) {
      List<BoardGameObserver> observersToTransfer = new ArrayList<>(otherGame.observers);
      for (BoardGameObserver observer : observersToTransfer) {
        this.addObserver(observer);
      }
    }
  }
}