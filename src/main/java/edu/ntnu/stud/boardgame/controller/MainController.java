package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.BoardGameFacade;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import edu.ntnu.stud.boardgame.service.PlayerFileService;
import edu.ntnu.stud.boardgame.view.BoardSelection;
import edu.ntnu.stud.boardgame.view.GameSelection;
import edu.ntnu.stud.boardgame.view.PlayerSetup;
import javafx.scene.Scene;
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

  public MainController(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.mainContainer = new BorderPane();
    this.boardFileService = new BoardFileService();
    this.playerFileService = new PlayerFileService();
    this.gameFacade = new BoardGameFacade(boardFileService);

    Scene scene = new Scene(mainContainer);
    primaryStage.setScene(scene);

    this.gameController = new GameController(this, gameFacade);

    initializeViews();
  }

  private void initializeViews() {
    gameSelectionView = new GameSelection(this);
    boardSelectionView = new BoardSelection(this);
    playerSetupView = new PlayerSetup(this);
  }

  public void showGameSelectionView() {
    mainContainer.setCenter(gameSelectionView);
  }

  public void showBoardSelectionView() {
    mainContainer.setCenter(boardSelectionView);
  }

  public void showPlayerSetupView() {
    mainContainer.setCenter(playerSetupView);
  }

  public void exitApplication() {
    primaryStage.close();
  }

  public void showErrorDialog(String title, String message) {
  }

  public void registerObserver(BoardGameObserver observer) {
    gameFacade.registerObserver(observer);
  }
}
