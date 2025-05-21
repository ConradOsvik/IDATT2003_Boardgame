package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.view.BoardSelectionView;
import edu.ntnu.stud.boardgame.view.GameSelectionView;
import edu.ntnu.stud.boardgame.view.LadderGameView;
import edu.ntnu.stud.boardgame.view.MonopolyGameView;
import edu.ntnu.stud.boardgame.view.PlayerSetupView;
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
  private final GameController gameController;

  private GameSelectionView gameSelectionView;
  private BoardSelectionView boardSelectionView;
  private PlayerSetupView playerSetupView;
  private LadderGameView ladderGameView;
  private MonopolyGameView monopolyGameView;

  public MainController(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.mainContainer = new BorderPane();

    this.gameController = new GameController(this);

    Scene scene = new Scene(mainContainer, 1200, 900);
    URL cssUrl = getClass().getResource("/styles/styles.css");
    if (cssUrl != null) {
      scene.getStylesheets().add(cssUrl.toExternalForm());
    } else {
      System.err.println("Could not find CSS file at /styles/styles.css");
    }

    primaryStage.setTitle("Board Game Application");
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(1000);
    primaryStage.setMinHeight(800);
    primaryStage.show();

    initializeViews();
    showGameSelectionView();
  }

  private void initializeViews() {
    gameSelectionView = new GameSelectionView(gameController);
    boardSelectionView = new BoardSelectionView(this, gameController);
    playerSetupView = new PlayerSetupView(this, gameController);
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

  public void showGameView() {
    if (gameController.getCurrentGameType() == BoardGameType.LADDER) {
      if (ladderGameView == null) {
        ladderGameView = new LadderGameView(this, gameController);
      }
      primaryStage.setTitle("Board Game - Snakes and Ladders");
      mainContainer.setCenter(ladderGameView);

    } else if (gameController.getCurrentGameType() == BoardGameType.MONOPOLY) {
      if (monopolyGameView == null) {
        monopolyGameView = new MonopolyGameView(this, gameController);
      }
      primaryStage.setTitle("Board Game - Monopoly");
      mainContainer.setCenter(monopolyGameView);

    } else {
      showErrorDialog("Not Implemented", "This game type is not implemented yet");
      return;
    }

    if (!gameController.startGame()) {
      showGameSelectionView();
    }
  }

  public void exitApplication() {
    Platform.exit();
  }

  public void showErrorDialog(String title, String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title != null ? title : "Error");
    alert.setHeaderText(null);
    alert.setContentText(message != null ? message : "An unexpected error occurred.");
    alert.showAndWait();
  }

  public void showInfoDialog(String title, String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title != null ? title : "Information");
    alert.setHeaderText(null);
    alert.setContentText(message != null ? message : "Information.");
    alert.showAndWait();
  }
}