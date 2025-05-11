package edu.ntnu.stud.boardgame.snakesandladders.controller;

import edu.ntnu.stud.boardgame.core.controller.GameController;
import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.IllegalGameStateException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.TurnChangedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.view.SlGameView;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.scene.Node;

public class SlGameController extends GameController {

  private static final Logger LOGGER = Logger.getLogger(SlGameController.class.getName());

  private final SlGameView view;

  private int currentPlayerIndex = 0;

  public SlGameController() {
    super();
    this.view = new SlGameView(this);
  }

  @Override
  public Node getView() {
    return view.getNode();
  }

  public void createNewGame() {
    validateGameInitialized();
    boardGame.reset();
    currentPlayerIndex = 0;
  }

  public void addPlayer(String name, int tokenId) {
    validateGameInitialized();

    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }

    SlPlayer player = new SlPlayer(name, tokenId);
    boardGame.addPlayer(player);
  }

  public void startGame() {
    validateGameInitialized();
    boardGame.start();
    currentPlayerIndex = 0;

    if (boardGame.getPlayers().isEmpty()) {
      throw new InvalidPlayerException("No players have been added");
    }

    SlPlayer currentPlayer = getCurrentPlayer().orElseThrow(
        () -> new IllegalGameStateException("No current player available"));

    if (currentPlayer != null) {
      GameEvent turnEvent = new TurnChangedEvent(currentPlayer);
      boardGame.notifyObservers(turnEvent);
    }
  }

  public void restartGame() {
    validateGameInitialized();
    boardGame.restart();
    currentPlayerIndex = 0;
  }

  public void rollDiceAndTakeTurn() {
    validateGameInitialized();

    if (boardGame.isFinished()) {
      throw new GameOverException("Game is already finished");
    }

    SlPlayer currentPlayer = getCurrentPlayer().orElseThrow(
        () -> new IllegalGameStateException("No current player available"));

    boardGame.playTurn(currentPlayer);

    if (!boardGame.isFinished()) {
      nextPlayer();
    }
  }

  private Optional<SlPlayer> getCurrentPlayer() {
    validateGameInitialized();

    List<Player> players = boardGame.getPlayers();
    if (players.isEmpty()) {
      return Optional.empty();
    }

    if (currentPlayerIndex >= players.size()) {
      currentPlayerIndex = 0;
    }

    return Optional.of((SlPlayer) players.get(currentPlayerIndex));
  }

  private void nextPlayer() {
    validateGameInitialized();

    List<Player> players = boardGame.getPlayers();
    if (!players.isEmpty()) {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
  }
}
