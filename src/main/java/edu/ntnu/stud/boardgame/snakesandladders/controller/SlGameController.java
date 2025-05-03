package edu.ntnu.stud.boardgame.snakesandladders.controller;

import edu.ntnu.stud.boardgame.core.controller.BaseController;
import edu.ntnu.stud.boardgame.core.exception.GameNotInitializedException;
import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.IllegalGameStateException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.GameEvent.EventType;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.view.SlGameView;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class SlGameController extends BaseController {

  private static final Logger LOGGER = Logger.getLogger(SlGameController.class.getName());

  private final SlGameView view;

  private SlBoardGame game;

  private int currentPlayerIndex = 0;

  public SlGameController() {
    super();
    this.view = new SlGameView(this);
    view.render();
  }

  public void init(Object... args) {
    if (args.length == 0 || !(args[0] instanceof SlBoardGame newGame)) {
      throw new IllegalArgumentException("First argument must be a SlBoardGame instance");
    }

    SlBoardGame oldGame = this.game;
    this.game = newGame;
    this.currentPlayerIndex = 0;

    if (oldGame != null && oldGame != newGame) {
      newGame.transferObserversFrom(oldGame);
      newGame.init();
    }
    if (oldGame == null && newGame != null) {
      newGame.addObserver(view);
      newGame.init();
    }

    view.draw();
  }

  @Override
  public Node getView() {
    return view;
  }

  public void createNewGame() {
    validateGameInitialized();
    game.reset();
    currentPlayerIndex = 0;
  }

  public void addPlayer(String name, Color color) {
    validateGameInitialized();

    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }

    if (color == null) {
      throw new IllegalArgumentException("Player color cannot be null");
    }

    SlPlayer player = new SlPlayer(name, color);
    game.addPlayer(player);
  }

  public void startGame() {
    validateGameInitialized();
    game.startGame();
    currentPlayerIndex = 0;

    if (game.getPlayers().isEmpty()) {
      throw new InvalidPlayerException("No players have been added");
    }

    SlPlayer currentPlayer = getCurrentPlayer().orElseThrow(
        () -> new IllegalGameStateException("No current player available"));

    if (currentPlayer != null) {
      GameEvent turnEvent = new GameEvent(EventType.TURN_CHANGED);
      turnEvent.addData("player", currentPlayer);
      game.notifyObservers(turnEvent);
    }
  }

  public void rollDiceAndTakeTurn() {
    validateGameInitialized();

    if (game.isFinished()) {
      throw new GameOverException("Game is already finished");
    }

    SlPlayer currentPlayer = getCurrentPlayer()
        .orElseThrow(() -> new IllegalGameStateException("No current player available"));

    game.playTurn(currentPlayer);

    if (!game.isFinished()) {
      nextPlayer();
    }
  }

  private Optional<SlPlayer> getCurrentPlayer() {
    validateGameInitialized();

    List<Player> players = game.getPlayers();
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

    List<Player> players = game.getPlayers();
    if (!players.isEmpty()) {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
  }

  private void validateGameInitialized() {
    if (game == null) {
      LOGGER.log(Level.SEVERE, "Game is not initialized");
      throw new GameNotInitializedException("Game has not been initialized");
    }
  }
}
