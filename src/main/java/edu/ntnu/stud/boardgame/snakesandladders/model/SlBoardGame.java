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
  public void playTurn(Player player) {
    validatePlayerAndGameState(player);
    notifyTurnChanged(player);

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
  }

  private void validatePlayerAndGameState(Player player) {
    if (player == null) {
      throw new InvalidPlayerException("Player cannot be null");
    }

    if (isFinished()) {
      throw new GameOverException("Game is already finished");
    }
  }

  private void notifyTurnChanged(Player player) {
    GameEvent turnEvent = new SlTurnChangedEvent((SlPlayer) player);
    notifyObservers(turnEvent);
  }

  private boolean isBounceBackMove(int rawDestination) {
    return rawDestination > SlBoard.NUM_TILES;
  }

  private void handleRegularMove(Player player, Tile previousTile, int steps, int rawDestination) {
    player.move(steps);
    Tile currentTile = player.getCurrentTile();
    SlBoard slBoard = getBoard();

    if (rawDestination != currentTile.getTileId()) {
      handleSpecialTileMove(player, previousTile, steps, rawDestination, currentTile);
    } else {
      notifyRegularMove(player, previousTile, currentTile, steps);
    }
  }

  private void handleSpecialTileMove(Player player, Tile previousTile, int steps,
      int rawDestination,
      Tile currentTile) {
    SlBoard slBoard = getBoard();
    Tile intermediateTile = slBoard.getTile(rawDestination);

    GameEvent intermediateMoveEvent = new SlPlayerMovedEvent(
        (SlPlayer) player, previousTile, intermediateTile, steps, slBoard);
    notifyObservers(intermediateMoveEvent);

    boolean isSnake = currentTile.getTileId() < rawDestination;
    boolean isLadder = currentTile.getTileId() > rawDestination;

    GameEvent specialMoveEvent = new SlPlayerMovedEvent(
        (SlPlayer) player, intermediateTile, currentTile, steps, slBoard, isSnake, isLadder);
    notifyObservers(specialMoveEvent);
  }

  private void notifyRegularMove(Player player, Tile previousTile, Tile currentTile, int steps) {
    GameEvent moveEvent = new SlPlayerMovedEvent(
        (SlPlayer) player, previousTile, currentTile, steps, getBoard());
    notifyObservers(moveEvent);
  }

  private void handleBounceBackMove(Player player, Tile previousTile, int steps,
      int rawDestination) {
    SlBoard slBoard = getBoard();
    int actualDestination = calculateBounceBackDestination(rawDestination);

    // Animate to final tile
    Tile finalTile = slBoard.getTile(SlBoard.NUM_TILES);
    notifyMoveToFinalTile(player, previousTile, finalTile, steps);

    // Handle bounce back
    Tile bounceDestination = slBoard.getTile(actualDestination);
    player.setCurrentTile(bounceDestination);
    notifyBounceBackMove(player, finalTile, bounceDestination);

    // Check if landed on special tile after bounce back
    checkForSpecialTileAfterBounce(player, bounceDestination, actualDestination);
  }

  private int calculateBounceBackDestination(int rawDestination) {
    return SlBoard.NUM_TILES - (rawDestination - SlBoard.NUM_TILES);
  }

  private void notifyMoveToFinalTile(Player player, Tile previousTile, Tile finalTile, int steps) {
    GameEvent moveToFinalEvent = new SlPlayerMovedEvent(
        (SlPlayer) player, previousTile, finalTile, steps, getBoard());
    notifyObservers(moveToFinalEvent);
  }

  private void notifyBounceBackMove(Player player, Tile finalTile, Tile bounceDestination) {
    GameEvent bounceBackEvent = new SlPlayerMovedEvent(
        (SlPlayer) player, finalTile, bounceDestination, 0, getBoard(), false, false);
    notifyObservers(bounceBackEvent);
  }

  private void checkForSpecialTileAfterBounce(Player player, Tile bounceDestination,
      int expectedTileId) {
    Tile currentTile = player.getCurrentTile();
    if (currentTile.getTileId() != expectedTileId) {
      boolean isSnake = currentTile.getTileId() < expectedTileId;
      boolean isLadder = currentTile.getTileId() > expectedTileId;

      GameEvent specialMoveEvent = new SlPlayerMovedEvent(
          (SlPlayer) player, bounceDestination, currentTile, 0, getBoard(), isSnake, isLadder);
      notifyObservers(specialMoveEvent);
    }
  }

  private void checkWinCondition(Player player) {
    if (board.isLastTile(player.getCurrentTile())) {
      this.winner = player;
      this.finished = true;

      notifyWinAndGameEnd(player);
    }
  }

  private void notifyWinAndGameEnd(Player player) {
    GameEvent winEvent = new PlayerWonEvent(player);
    notifyObservers(winEvent);

    GameEvent endEvent = new GameEndedEvent(player);
    notifyObservers(endEvent);
  }

  @Override
  public SlBoard getBoard() {
    return (SlBoard) board;
  }
}