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

public class GameController {

  private final MainController mainController;
  private final BoardGameFacade gameFacade;
  private final PlayerFileService playerFileService;

  public GameController(MainController mainController, BoardGameFacade gameFacade,
      PlayerFileService playerFileService) {
    this.mainController = mainController;
    this.gameFacade = gameFacade;
    this.playerFileService = playerFileService;
  }

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

  public List<String> getAvailableBoards() {
    try {
      return gameFacade.getAvailableGameBoards();
    } catch (Exception e) {
      showError("Board List Error", "Failed to get available boards: " + e.getMessage());
      return List.of("Default");
    }
  }

  public boolean saveSelectedBoardAs(String selectedBoard, String newName) {
    try {
      if (selectedBoard == null || selectedBoard.trim().isEmpty()) {
        showError("Save Error", "Board name cannot be empty.");
        return false;
      }
      if (newName == null || newName.trim().isEmpty()) {
        showError("Save Error", "New board name cannot be empty.");
        return false;
      }
      if (gameFacade.getCurrentGameType() == null) {
        showError("Save Error", "No game type selected.");
        return false;
      }

      gameFacade.createGame(selectedBoard);

      gameFacade.saveCurrentBoard(newName);

      showInfo("Success", "Board saved successfully as: " + newName);
      return true;
    } catch (Exception e) {
      showError("Save Error", "Failed to save board: " + e.getMessage());
      return false;
    }
  }

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

  public boolean addPlayer(String name, PieceType pieceType) {
    try {
      if (name == null || name.trim().isEmpty()) {
        showError("Input Error", "Player name cannot be empty.");
        return false;
      }
      if (pieceType == null) {
        showError("Input Error", "Piece type cannot be null.");
        return false;
      }
      for (Player player : gameFacade.getCurrentGame().getPlayers()) {
        if (player.getPiece() == pieceType) {
          showError("Input Error",
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

  public boolean savePlayers(String fileName) {
    try {
      if (fileName == null || fileName.trim().isEmpty()) {
        showError("Save Error", "File name cannot be empty.");
        return false;
      }
      List<Player> players = gameFacade.getCurrentGame().getPlayers();
      if (players.isEmpty()) {
        showError("Input Error", "No players to save.");
        return false;
      }

      playerFileService.savePlayers(fileName, players);
      showInfo("Success", "Players saved successfully to " + fileName + ".csv");
      return true;
    } catch (Exception e) {
      showError("Save Error", "Failed to save players: " + e.getMessage());
      return false;
    }
  }

  public boolean loadPlayers(String fileName) {
    try {
      if (fileName == null || fileName.trim().isEmpty()) {
        showError("Load Error", "File name cannot be empty.");
        return false;
      }
      List<Player> loadedPlayers = playerFileService.loadPlayers(fileName);

      for (Player player : loadedPlayers) {
        addPlayer(player.getName(), player.getPiece());
      }

      return true;
    } catch (BoardGameException e) {
      showError("Load Error", "Failed to load players: " + e.getMessage());
      return false;
    } catch (Exception e) {
      showError("Load Error", "An unexpected error occurred while loading players: " + e.getMessage());
      return false;
    }
  }

  public boolean startGame() {
    try {
      if (gameFacade.getCurrentGame().getPlayers().size() < 2) {
        showError("Player Error", "You need at least 2 players to start the game.");
        return false;
      }

      gameFacade.startGame();
      return true;
    } catch (Exception e) {
      showError("Game Error", "Failed to start game: " + e.getMessage());
      return false;
    }
  }

  public boolean playTurn() {
    try {
      gameFacade.playTurn();
      return true;
    } catch (BoardGameException e) {
      showError("Game Error", "Failed to play turn: " + e.getMessage());
      return false;
    } catch (Exception e) {
      showError("Game Error", "An unexpected error occurred during the turn: " + e.getMessage());
      return false;
    }
  }

  public void registerObserver(BoardGameObserver observer) {
    if (observer == null) {
      System.err.println("Attempted to register a null observer.");
      return;
    }
    gameFacade.registerObserver(observer);
  }

  public BoardGame getGame() {
    return gameFacade.getCurrentGame();
  }

  public BoardGameType getCurrentGameType() {
    return gameFacade.getCurrentGameType();
  }

  public List<String> getAvailablePlayerListNames() {
    try {
      return playerFileService.getAvailablePlayerListFileNames();
    } catch (Exception e) {
      showError("Load Error", "Failed to retrieve saved player lists: " + e.getMessage());
      return new ArrayList<>();
    }
  }

  private void showError(String title, String message) {
    mainController.showErrorDialog(title, message);
  }

  public void showInfo(String title, String message) {
    mainController.showInfoDialog(title, message);
  }
}