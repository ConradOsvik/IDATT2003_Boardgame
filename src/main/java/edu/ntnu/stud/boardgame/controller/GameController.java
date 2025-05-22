package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
import edu.ntnu.stud.boardgame.model.BoardGameFacade;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.service.PlayerFileService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller responsible for managing the board game lifecycle, handling communication between the
 * UI and the game model.
 *
 * <p>This class provides methods for:
 * <ul>
 *   <li>Game selection and initialization</li>
 *   <li>Board management (loading, saving)</li>
 *   <li>Player setup and management</li>
 *   <li>Game flow control (starting games, executing turns)</li>
 * </ul>
 *
 * <p>It acts as an intermediary between the {@link MainController} and the {@link BoardGameFacade},
 * encapsulating game logic and error handling.
 */
public class GameController {

  private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
  private static final String SAVE_ERROR = "Save Error";
  private static final String INPUT_ERROR = "Input Error";
  private static final String LOAD_ERROR = "Load Error";
  private static final String GAME_ERROR = "Game Error";


  private final MainController mainController;
  private final BoardGameFacade gameFacade;
  private final PlayerFileService playerFileService;

  /**
   * Creates a new game controller.
   *
   * @param mainController    the main controller for UI interactions
   * @param gameFacade        the facade for game logic operations
   * @param playerFileService the service for persisting player data
   */
  public GameController(MainController mainController, BoardGameFacade gameFacade,
      PlayerFileService playerFileService) {
    this.mainController = mainController;
    this.gameFacade = gameFacade;
    this.playerFileService = playerFileService;
  }

  /**
   * Sets the type of board game to be played.
   *
   * @param gameType the type of board game to select
   */
  public void selectGameType(BoardGameType gameType) {
    try {
      if (gameType == null) {
        showError("Game Type Error", "Game type cannot be null.");
        return;
      }
      gameFacade.setCurrentGameType(gameType);
      mainController.showBoardSelectionView();
    } catch (Exception e) {
      showError("Game Type Error", "Failed to select game type: " + e.getMessage());
    }
  }

  /**
   * Retrieves the list of available game boards for the current game type.
   *
   * @return a list of board names that can be selected
   */
  public List<String> getAvailableBoards() {
    try {
      return gameFacade.getAvailableGameBoards();
    } catch (Exception e) {
      showError("Board List Error", "Failed to get available boards: " + e.getMessage());
      return List.of("Default");
    }
  }

  /**
   * Saves a copy of the selected board with a new name.
   *
   * @param selectedBoard the name of the board to save
   * @param newName       the new name for the saved board
   * @return {@code true} if the board was saved successfully, {@code false} otherwise
   */
  public boolean saveSelectedBoardAs(String selectedBoard, String newName) {
    try {
      if (selectedBoard == null || selectedBoard.trim().isEmpty()) {
        showError(SAVE_ERROR, "Board name cannot be empty.");
        return false;
      }
      if (newName == null || newName.trim().isEmpty()) {
        showError(SAVE_ERROR, "New board name cannot be empty.");
        return false;
      }
      if (gameFacade.getCurrentGameType() == null) {
        showError(SAVE_ERROR, "No game type selected.");
        return false;
      }

      gameFacade.createGame(selectedBoard);

      gameFacade.saveCurrentBoard(newName);

      showInfo("Success", "Board saved successfully as: " + newName);
      return true;
    } catch (Exception e) {
      showError(SAVE_ERROR, "Failed to save board: " + e.getMessage());
      return false;
    }
  }

  /**
   * Selects a game board by name and prepares it for play.
   *
   * @param boardName the name of the board to select
   * @return {@code true} if the board was selected successfully, {@code false} otherwise
   */
  public boolean selectBoard(String boardName) {
    try {
      if (boardName == null || boardName.trim().isEmpty()) {
        showError("Board Selection Error", "Board name cannot be empty.");
        return false;
      }
      gameFacade.createGame(boardName);
      mainController.showPlayerSetupView();
      return true;
    } catch (Exception e) {
      showError("Board Selection Error", "Failed to load board: " + e.getMessage());
      return false;
    }
  }

  /**
   * Adds a new player to the current game with the specified piece type.
   *
   * @param name      the name of the player to add
   * @param pieceType the piece type chosen by the player
   * @return {@code true} if the player was added successfully, {@code false} otherwise
   */
  public boolean addPlayer(String name, PieceType pieceType) {
    try {
      if (name == null || name.trim().isEmpty()) {
        showError(INPUT_ERROR, "Player name cannot be empty.");
        return false;
      }
      if (pieceType == null) {
        showError(INPUT_ERROR, "Piece type cannot be null.");
        return false;
      }
      for (Player player : gameFacade.getCurrentGame().getPlayers()) {
        if (player.getPiece() == pieceType) {
          showError(INPUT_ERROR,
              "The piece " + pieceType + " is already in use by another player.");
          return false;
        }
      }

      gameFacade.addPlayer(name, pieceType);
      return true;
    } catch (Exception e) {
      showError("Player Error", "Failed to add player: " + e.getMessage());
      return false;
    }
  }

  /**
   * Saves the current list of players to a file.
   *
   * @param fileName the name of the file to save the players to (without extension)
   * @return {@code true} if the players were saved successfully, {@code false} otherwise
   */
  public boolean savePlayers(String fileName) {
    try {
      if (fileName == null || fileName.trim().isEmpty()) {
        showError(SAVE_ERROR, "File name cannot be empty.");
        return false;
      }
      List<Player> players = gameFacade.getCurrentGame().getPlayers();
      if (players.isEmpty()) {
        showError(INPUT_ERROR, "No players to save.");
        return false;
      }

      playerFileService.savePlayers(fileName, players);
      showInfo("Success", "Players saved successfully to " + fileName + ".csv");
      return true;
    } catch (Exception e) {
      showError(SAVE_ERROR, "Failed to save players: " + e.getMessage());
      return false;
    }
  }

  /**
   * Loads players from a file and adds them to the current game.
   *
   * @param fileName the name of the file to load players from
   * @return {@code true} if the players were loaded successfully, {@code false} otherwise
   */
  public boolean loadPlayers(String fileName) {
    try {
      if (fileName == null || fileName.trim().isEmpty()) {
        showError(LOAD_ERROR, "File name cannot be empty.");
        return false;
      }
      List<Player> loadedPlayers = playerFileService.loadPlayers(fileName);

      for (Player player : loadedPlayers) {
        addPlayer(player.getName(), player.getPiece());
      }

      return true;
    } catch (BoardGameException e) {
      showError(LOAD_ERROR, "Failed to load players: " + e.getMessage());
      return false;
    } catch (Exception e) {
      showError(LOAD_ERROR,
          "An unexpected error occurred while loading players: " + e.getMessage());
      return false;
    }
  }

  /**
   * Starts the game if all conditions are met (minimum number of players, etc.).
   *
   * @return {@code true} if the game started successfully, {@code false} otherwise
   */
  public boolean startGame() {
    try {
      if (gameFacade.getCurrentGame().getPlayers().size() < 2) {
        showError("Player Error", "You need at least 2 players to start the game.");
        return false;
      }

      gameFacade.startGame();
      return true;
    } catch (Exception e) {
      showError(GAME_ERROR, "Failed to start game: " + e.getMessage());
      return false;
    }
  }

  /**
   * Executes the current player's turn in the game.
   *
   * @return {@code true} if the turn was executed successfully, {@code false} otherwise
   */
  public boolean playTurn() {
    try {
      gameFacade.playTurn();
      return true;
    } catch (BoardGameException e) {
      showError(GAME_ERROR, "Failed to play turn: " + e.getMessage());
      return false;
    } catch (Exception e) {
      showError(GAME_ERROR, "An unexpected error occurred during the turn: " + e.getMessage());
      return false;
    }
  }

  /**
   * Registers an observer to receive notifications about game events.
   *
   * @param observer the observer to register
   */
  public void registerObserver(BoardGameObserver observer) {
    if (observer == null) {
      LOGGER.warning("Attempted to register a null observer.");
      return;
    }
    gameFacade.registerObserver(observer);
  }

  /**
   * Gets the current board game.
   *
   * @return the current board game instance
   */
  public BoardGame getGame() {
    return gameFacade.getCurrentGame();
  }

  /**
   * Gets the currently selected game type.
   *
   * @return the current game type
   */
  public BoardGameType getCurrentGameType() {
    return gameFacade.getCurrentGameType();
  }

  /**
   * Gets the list of available player list file names.
   *
   * @return a list of available player list file names
   */
  public List<String> getAvailablePlayerListNames() {
    try {
      return playerFileService.getAvailablePlayerListFileNames();
    } catch (Exception e) {
      showError(LOAD_ERROR, "Failed to retrieve saved player lists: " + e.getMessage());
      return new ArrayList<>();
    }
  }

  /**
   * Displays an error dialog with the given title and message.
   *
   * @param title   the title of the error dialog
   * @param message the error message to display
   */
  private void showError(String title, String message) {
    mainController.showErrorDialog(title, message);
  }

  /**
   * Displays an information dialog with the given title and message.
   *
   * @param title   the title of the information dialog
   * @param message the information message to display
   */
  public void showInfo(String title, String message) {
    mainController.showInfoDialog(title, message);
  }
}