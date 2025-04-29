package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.GameEvent.EventType;
import java.util.logging.Logger;

/**
 * Implementation of the BoardGame interface for the Snakes and Ladders game.
 */
public class SlBoardGame extends BoardGame {

  private static final Logger LOGGER = Logger.getLogger(SlBoardGame.class.getName());

  /**
   * Constructs a new Snakes and Ladders game.
   */
  public SlBoardGame() {
    super();
  }

  @Override
  public void createBoard() {
    this.board = new SlBoard();
    this.board.initializeBoard();
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
    if (player == null) {
      throw new InvalidPlayerException("Player cannot be null");
    }

    if (isFinished()) {
      throw new GameOverException("Game is already finished");
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

    //TODO: not using this atm
    if (previousTile != player.getCurrentTile()
        && previousTile.getTileId() + steps != player.getCurrentTile().getTileId()) {
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
  public SlBoard getBoard() {
    return (SlBoard) board;
  }
}