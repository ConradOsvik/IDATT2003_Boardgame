package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.BoardGameFacade;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import edu.ntnu.stud.boardgame.service.PlayerFileService;
import edu.ntnu.stud.boardgame.view.BoardSelection;
import edu.ntnu.stud.boardgame.view.GameSelection;
import edu.ntnu.stud.boardgame.view.PlayerSetup;
import java.net.URL;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

  private final Stage primaryStage;
  private final BorderPane mainContainer;
  private final BoardFileService boardFileService;
  private final PlayerFileService playerFileService;
  private final BoardGameFacade gameFacade;
  private final GameController gameController;

  private GameSelection gameSelectionView;
  private BoardSelection boardSelectionView;
  private PlayerSetup playerSetupView;
//  private LadderBoard ladderBoardView;
//  private MonopolyBoard monopolyBoardView;

  public MainController(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.mainContainer = new BorderPane();
    this.boardFileService = new BoardFileService();
    this.playerFileService = new PlayerFileService();
    this.gameFacade = new BoardGameFacade(boardFileService);

    Scene scene = new Scene(mainContainer, 900, 700);

    URL cssUrl = getClass().getResource("/styles/styles.css");
    if (cssUrl != null) {
      scene.getStylesheets().add(cssUrl.toExternalForm());
    } else {
      System.err.println("Could not find CSS file at /styles/styles.css");
    }

    primaryStage.setTitle("Board Game Application");
    primaryStage.setScene(scene);
    primaryStage.show();

    this.gameController = new GameController(this, gameFacade);

    initializeViews();
    showGameSelectionView();
  }

  private void initializeViews() {
    gameSelectionView = new GameSelection(this);
    boardSelectionView = new BoardSelection(this);
    playerSetupView = new PlayerSetup(this);
  }

  public void showGameSelectionView() {
    primaryStage.setTitle("Board Game - Select Game Type");
    mainContainer.setCenter(gameSelectionView);
  }

  public void showBoardSelectionView() {
    primaryStage.setTitle("Board Game - Select Board");
    boardSelectionView.refreshBoardList();
    mainContainer.setCenter(boardSelectionView);
  }

  public void showPlayerSetupView() {
    primaryStage.setTitle("Board Game - Player Setup");
    mainContainer.setCenter(playerSetupView);
  }

//  public void showGameView() {
//    try {
//      if (selectedGameType == BoardGameType.LADDER) {
//        if (ladderBoardView == null) {
//          ladderBoardView = new LadderBoard(this, gameController);
//        }
//        primaryStage.setTitle("Board Game - Ladder Game");
//        mainContainer.setCenter(ladderBoardView);
//      } else if (selectedGameType == BoardGameType.MONOPOLY) {
//        if (monopolyBoardView == null) {
//          monopolyBoardView = new MonopolyBoard(this);
//        }
//        primaryStage.setTitle("Board Game - Monopoly Game");
//        mainContainer.setCenter(monopolyBoardView);
//      }
//
//      // Start the game
//      gameFacade.startGame();
//    } catch (Exception e) {
//      showErrorDialog("Game Error", "Failed to start game: " + e.getMessage());
//    }
//  }

  public void selectGameType(BoardGameType gameType) {
    gameFacade.setCurrentGameType(gameType);
    try {
      showBoardSelectionView();
    } catch (Exception e) {
      showErrorDialog("Error", "Failed to create game: " + e.getMessage());
    }
  }

  public void selectBoard(String boardName) {
    try {
      System.out.println("Selected Board: " + boardName);
      gameFacade.createGame(boardName);
      System.out.println("Created game: " + boardName);
      showPlayerSetupView();
    } catch (Exception e) {
      showErrorDialog("Error", "Failed to load board: " + e.getMessage());
    }
  }

  public void startGame() {
    try {
      if (gameFacade.getCurrentGame().getPlayers().size() < 2) {
        showErrorDialog("Player Error", "You need at least 2 players to start the game.");
        return;
      }
//      showGameView();
    } catch (Exception e) {
      showErrorDialog("Error", "Failed to start game: " + e.getMessage());
    }
  }

  public void exitApplication() {
    Platform.exit();
  }

  public void showErrorDialog(String title, String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void showInfoDialog(String title, String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void registerObserver(BoardGameObserver observer) {
    gameFacade.registerObserver(observer);
  }

  public BoardGameFacade getGameFacade() {
    return gameFacade;
  }

  public PlayerFileService getPlayerFileService() {
    return playerFileService;
  }
}
