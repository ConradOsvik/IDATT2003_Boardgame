package edu.ntnu.stud.boardgame.snakesandladders.controller;

import edu.ntnu.stud.boardgame.core.controller.BaseController;
import edu.ntnu.stud.boardgame.core.exception.GameNotInitializedException;
import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.IllegalGameStateException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.GameEvent.EventType;
import edu.ntnu.stud.boardgame.core.service.GameSaveService;
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

  private final GameSaveService gameSaveService;

  public SlGameController() {
    super();
    this.view = new SlGameView(this);
    this.gameSaveService = new GameSaveService();
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

  /**
   * Saves the current game to a file with the given name.
   * 
   * @param gameName the name to save the game as (without extension)
   * @return true if the game was saved successfully, false otherwise
   */
  public boolean saveGame(String gameName) {
    validateGameInitialized();
    return gameSaveService.saveGame(game, gameName);
  }

  /**
   * Loads a game from a file with the given name.
   * 
   * @param gameName the name of the game to load (without extension)
   * @return true if the game was loaded successfully, false otherwise
   */
  public boolean loadGame(String gameName) {
    BoardGame loadedGame = gameSaveService.loadGame(gameName);
    if (loadedGame instanceof SlBoardGame slBoardGame) {
      init(slBoardGame);
      return true;
    }
    return false;
  }

  /**
   * Saves the current list of players to a file with the given name.
   * 
   * @param playerListName the name to save the player list as (without extension)
   * @return true if the players were saved successfully, false otherwise
   */
  public boolean savePlayers(String playerListName) {
    validateGameInitialized();
    return gameSaveService.savePlayers(game.getPlayers(), playerListName);
  }

  /**
   * Loads players from a file with the given name and adds them to the current
   * game.
   * 
   * @param playerListName the name of the player list to load (without extension)
   * @return true if the players were loaded and added successfully, false
   *         otherwise
   */
  public boolean loadPlayers(String playerListName) {
    validateGameInitialized();
    List<Player> players = gameSaveService.loadPlayers(playerListName);

    if (players.isEmpty()) {
      return false;
    }

    for (Player player : players) {
      if (player instanceof SlPlayer slPlayer) {
        // We need to check if this player already exists in the game
        if (!game.getPlayers().contains(slPlayer)) {
          game.addPlayer(slPlayer);
        }
      }
    }

    return true;
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
