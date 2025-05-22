package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.BoardGameFacade;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.service.PlayerFileService;
import edu.ntnu.stud.boardgame.view.BoardSelectionView;
import edu.ntnu.stud.boardgame.view.GameSelectionView;
import edu.ntnu.stud.boardgame.view.LadderGameView;
import edu.ntnu.stud.boardgame.view.MonopolyGameView;
import edu.ntnu.stud.boardgame.view.PlayerSetupView;
import java.net.URL;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main controller for the board game application's user interface.
 *
 * <p>This class is responsible for:
 * <ul>
 *   <li>Managing the primary stage and UI components</li>
 *   <li>Coordinating navigation between different views</li>
 *   <li>Setting up the application window and styling</li>
 *   <li>Displaying dialog messages to the user</li>
 * </ul>
 *
 * <p>It serves as the entry point for the UI layer and delegates game logic operations
 * to the {@link GameController}.
 */
public class MainController {

  private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

  private final Stage primaryStage;
  private final BorderPane mainContainer;
  private final GameController gameController;

  private GameSelectionView gameSelectionView;
  private BoardSelectionView boardSelectionView;
  private PlayerSetupView playerSetupView;
  private LadderGameView ladderGameView;
  private MonopolyGameView monopolyGameView;

  /**
   * Creates a new main controller with the primary application stage.
   *
   * <p>Initializes the game controller and sets up the primary stage.
   *
   * @param primaryStage the primary stage for the JavaFX application
   */
  public MainController(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.mainContainer = new BorderPane();

    this.gameController = new GameController(this, new BoardGameFacade(),
        PlayerFileService.getInstance());

    setupStage();
  }

  /**
   * Sets up the main application window with the scene, CSS styling, and dimensions.
   *
   * <p>Initializes views and shows the game selection view.
   */
  private void setupStage() {
    Scene scene = new Scene(mainContainer, 1200, 900);
    URL cssUrl = getClass().getResource("/styles/styles.css");
    if (cssUrl != null) {
      scene.getStylesheets().add(cssUrl.toExternalForm());
    } else {
      LOGGER.warning("Could not find CSS file at /styles/styles.css");
    }

    primaryStage.setTitle("Board Game Application");
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(1000);
    primaryStage.setMinHeight(800);
    primaryStage.show();

    initializeViews();
    showGameSelectionView();
  }

  /**
   * Initializes all view components for the application.
   */
  private void initializeViews() {
    gameSelectionView = new GameSelectionView(gameController);
    boardSelectionView = new BoardSelectionView(this, gameController);
    playerSetupView = new PlayerSetupView(this, gameController);
  }

  /**
   * Displays the game selection view where users can choose a game type.
   */
  public void showGameSelectionView() {
    primaryStage.setTitle("Board Game - Select Game Type");
    mainContainer.setCenter(gameSelectionView);
  }

  /**
   * Displays the board selection view where users can choose a game board.
   *
   * <p>Refreshes the board list before showing the view.
   */
  public void showBoardSelectionView() {
    primaryStage.setTitle("Board Game - Select Board");
    boardSelectionView.refreshBoardList();
    mainContainer.setCenter(boardSelectionView);
  }

  /**
   * Displays the player setup view where users can configure players.
   */
  public void showPlayerSetupView() {
    primaryStage.setTitle("Board Game - Player Setup");
    mainContainer.setCenter(playerSetupView);
  }

  /**
   * Displays the appropriate game view based on the selected game type.
   *
   * <p>Creates the game view if it doesn't exist yet, then attempts to start the game.
   * If the game fails to start, returns to the game selection view.
   */
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

  /**
   * Exits the application by terminating the JavaFX platform.
   */
  public void exitApplication() {
    Platform.exit();
  }

  /**
   * Displays an error dialog with the specified title and message.
   *
   * @param title   the title of the error dialog
   * @param message the error message to display
   */
  public void showErrorDialog(String title, String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title != null ? title : "Error");
    alert.setHeaderText(null);
    alert.setContentText(message != null ? message : "An unexpected error occurred.");
    alert.showAndWait();
  }

  /**
   * Displays an information dialog with the specified title and message.
   *
   * @param title   the title of the information dialog
   * @param message the information message to display
   */
  public void showInfoDialog(String title, String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title != null ? title : "Information");
    alert.setHeaderText(null);
    alert.setContentText(message != null ? message : "Information.");
    alert.showAndWait();
  }
}