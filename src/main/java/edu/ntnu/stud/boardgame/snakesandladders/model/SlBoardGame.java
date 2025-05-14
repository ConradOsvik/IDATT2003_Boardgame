package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameEndedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerWonEvent;
import edu.ntnu.stud.boardgame.core.observer.events.TurnChangedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.BounceBackEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.SnakeEncounteredEvent;
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
  public void playTurn(Player player) {
    validatePlayerAndGameState(player);

    int steps = rollDice();
    Tile previousTile = player.getCurrentTile();
    int previousTileId = previousTile != null ? previousTile.getTileId() : 0;
    int rawDestination = previousTileId + steps;

    if (isBounceBackMove(rawDestination)) {
      handleBounceBackMove(player, previousTile, steps, rawDestination);
    } else {
      handleRegularMove(player, previousTile, steps, rawDestination);
    }

    checkWinCondition(player);

    if (!isFinished()) {
      Player nextPlayer = getNextPlayer();
      if (nextPlayer != null) {
        GameEvent turnEvent = new TurnChangedEvent(nextPlayer);
        notifyObservers(turnEvent);
      }
    }
  }

  private void validatePlayerAndGameState(Player player) {
    if (player == null) {
      throw new InvalidPlayerException("Player cannot be null");
    }

    if (isFinished()) {
      throw new GameOverException("Game is already finished");
    }
  }

  private boolean isBounceBackMove(int rawDestination) {
    return rawDestination > getBoard().getNumTiles();
  }

  private void handleBounceBackMove(Player player, Tile previousTile, int steps,
      int rawDestination) {
    SlBoard slBoard = getBoard();
    int actualDestination = calculateBounceBackDestination(rawDestination);

    // Animate to final tile
    Tile finalTile = slBoard.getTile(slBoard.getNumTiles());
    notifyMoveToFinalTile(player, previousTile, finalTile, steps);

    // Handle bounce back
    Tile bounceDestination = slBoard.getTile(actualDestination);
    player.setCurrentTile(bounceDestination);
    notifyBounceBackMove(player, finalTile, bounceDestination);

    // Check if landed on special tile after bounce back
    checkForSpecialTileAfterBounce(player, bounceDestination, actualDestination);
  }

  private void handleRegularMove(Player player, Tile previousTile, int steps, int rawDestination) {
    player.move(steps);
    Tile currentTile = player.getCurrentTile();

    if (rawDestination != currentTile.getTileId()) {
      handleSpecialTileMove(player, previousTile, steps, rawDestination, currentTile);
    } else {
      notifyRegularMove(player, previousTile, currentTile, steps);
    }
  }

  private void checkWinCondition(Player player) {
    if (board.isLastTile(player.getCurrentTile())) {
      this.winner = player;
      this.finished = true;

      notifyWinAndGameEnd(player);
    }
  }

  @Override
  public SlBoard getBoard() {
    return (SlBoard) board;
  }

  private int calculateBounceBackDestination(int rawDestination) {
    return getBoard().getNumTiles() - (rawDestination - getBoard().getNumTiles());
  }

  private void notifyMoveToFinalTile(Player player, Tile previousTile, Tile finalTile, int steps) {
    GameEvent moveToFinalEvent = new PlayerMovedEvent(player, previousTile, finalTile, steps,
        getBoard());
    notifyObservers(moveToFinalEvent);
  }

  private void notifyBounceBackMove(Player player, Tile finalTile, Tile bounceDestination) {
    GameEvent bounceBackEvent = new BounceBackEvent(player, finalTile, bounceDestination, 0,
        getBoard());
    notifyObservers(bounceBackEvent);
  }

  private void checkForSpecialTileAfterBounce(Player player, Tile bounceDestination,
      int expectedTileId) {
    Tile currentTile = player.getCurrentTile();
    if (currentTile.getTileId() != expectedTileId) {
      boolean isSnake = currentTile.getTileId() < expectedTileId;
      boolean isLadder = currentTile.getTileId() > expectedTileId;

      if (isSnake) {
        GameEvent snakeEvent = new SnakeEncounteredEvent(player, bounceDestination, currentTile, 0,
            getBoard());
        notifyObservers(snakeEvent);
      }

      if (isLadder) {
        GameEvent ladderEvent = new LadderClimbedEvent(player, bounceDestination, currentTile, 0,
            getBoard());
        notifyObservers(ladderEvent);
      }
    }
  }

  private void handleSpecialTileMove(Player player, Tile previousTile, int steps,
      int rawDestination, Tile currentTile) {
    SlBoard slBoard = getBoard();
    Tile intermediateTile = slBoard.getTile(rawDestination);

    GameEvent intermediateMoveEvent = new PlayerMovedEvent(player, previousTile, intermediateTile,
        steps, slBoard);
    notifyObservers(intermediateMoveEvent);

    boolean isSnake = currentTile.getTileId() < rawDestination;
    boolean isLadder = currentTile.getTileId() > rawDestination;

    if (isSnake) {
      GameEvent snakeEvent = new SnakeEncounteredEvent(player, intermediateTile, currentTile, steps,
          slBoard);
      notifyObservers(snakeEvent);
    }

    if (isLadder) {
      GameEvent ladderEvent = new LadderClimbedEvent(player, intermediateTile, currentTile, steps,
          slBoard);
      notifyObservers(ladderEvent);
    }
  }

  private void notifyRegularMove(Player player, Tile previousTile, Tile currentTile, int steps) {
    GameEvent moveEvent = new PlayerMovedEvent(player, previousTile, currentTile, steps,
        getBoard());
    notifyObservers(moveEvent);
  }

  private void notifyWinAndGameEnd(Player player) {
    GameEvent winEvent = new PlayerWonEvent(player);
    notifyObservers(winEvent);

    GameEvent endEvent = new GameEndedEvent(player);
    notifyObservers(endEvent);
  }

  private void notifyTurnChanged(Player player) {
    GameEvent turnEvent = new TurnChangedEvent(player);
    notifyObservers(turnEvent);
  }
}