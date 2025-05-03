package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameEndedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerWonEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.SlPlayerMovedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.SlTurnChangedEvent;
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

    GameEvent turnEvent = new SlTurnChangedEvent((SlPlayer) player);
    notifyObservers(turnEvent);

    int steps = rollDice();

    Tile previousTile = player.getCurrentTile();
    player.move(steps);

    GameEvent moveEvent = new SlPlayerMovedEvent((SlPlayer) player, previousTile,
        player.getCurrentTile(), steps, (SlBoard) board);
    notifyObservers(moveEvent);

    if (previousTile != player.getCurrentTile()
        && previousTile.getTileId() + steps != player.getCurrentTile().getTileId()) {
      if (player.getCurrentTile().getTileId() > previousTile.getTileId() + steps) {
        GameEvent ladderEvent = new SlPlayerMovedEvent((SlPlayer) player, previousTile,
            player.getCurrentTile(), steps, (SlBoard) board, false, true);
        notifyObservers(ladderEvent);
      } else if (player.getCurrentTile().getTileId() < previousTile.getTileId() + steps) {
        GameEvent snakeEvent = new SlPlayerMovedEvent((SlPlayer) player, previousTile,
            player.getCurrentTile(), steps, (SlBoard) board, true, false);
        notifyObservers(snakeEvent);
      }
    }

    if (board.isLastTile(player.getCurrentTile())) {
      this.winner = player;
      this.finished = true;

      GameEvent winEvent = new PlayerWonEvent(player);
      notifyObservers(winEvent);

      GameEvent endEvent = new GameEndedEvent(player);
      notifyObservers(endEvent);
    }
  }

  @Override
  public SlBoard getBoard() {
    return (SlBoard) board;
  }
}