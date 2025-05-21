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

  public GameController(MainController mainController) {
    this.mainController = mainController;
    this.playerFileService = PlayerFileService.getInstance();
    this.gameFacade = new BoardGameFacade();
  }

  public void selectGameType(BoardGameType gameType) {
    try {
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
      List<Player> loadedPlayers = playerFileService.loadPlayers(fileName);

      for (Player player : loadedPlayers) {
        addPlayer(player.getName(), player.getPiece());
      }

      return true;
    } catch (Exception e) {
      showError("Load Error", "Failed to load players: " + e.getMessage());
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
    }
  }

  public void registerObserver(BoardGameObserver observer) {
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
      return new ArrayList<>(); // Return empty list on error
    }
  }

  private void showError(String title, String message) {
    mainController.showErrorDialog(title, message);
  }

  public void showInfo(String title, String message) {
    mainController.showInfoDialog(title, message);
  }
}