package edu.ntnu.stud.boardgame.view.components;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.service.SoundManagerService;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Abstract base class for game view implementations. Provides common functionality for game views
 * including toolbar creation, victory screen management, and game event handling. Extends {@link
 * BorderPane} to organize game components in a border layout.
 *
 * @see MainController
 * @see GameController
 * @see BoardGameObserver
 */
public abstract class AbstractGameView extends BorderPane implements BoardGameObserver {

  protected final MainController mainController;
  protected final GameController gameController;
  protected final VictoryScreen victoryScreen;
  protected final SoundManagerService soundManager;
  protected final StackPane gameArea;

  /**
   * Creates a new game view with basic layout and controls.
   *
   * <p>Initializes the view with a toolbar, game area, and victory screen. Registers as an observer
   * for game events.
   *
   * @param mainController The main application controller
   * @param gameController The game-specific controller
   */
  protected AbstractGameView(MainController mainController, GameController gameController) {
    this.mainController = mainController;
    this.gameController = gameController;
    this.gameController.registerObserver(this);
    this.soundManager = SoundManagerService.getInstance();
    this.victoryScreen = new VictoryScreen(gameController);
    this.gameArea = new StackPane();

    initializeBasicLayout();
    createToolbar();
  }

  /**
   * Initializes the basic layout of the game view.
   *
   * <p>Sets up the right panel with audio controls and the central game area.
   */
  private void initializeBasicLayout() {
    VBox rightPanel = new VBox(20);
    rightPanel.setPadding(new Insets(15));
    rightPanel.setPrefWidth(250);
    rightPanel.getChildren().add(new AudioControlPanel());

    gameArea.setAlignment(javafx.geometry.Pos.CENTER);
    gameArea.getChildren().add(victoryScreen);

    setRight(rightPanel);
    setCenter(gameArea);

    victoryScreen.setVisible(false);
  }

  /**
   * Creates the toolbar with navigation and control buttons.
   *
   * <p>Adds Main Menu and Exit buttons with appropriate styling.
   */
  private void createToolbar() {
    ToolBar toolbar = new ToolBar();

    Button mainMenuButton =
        new ButtonBuilder()
            .text("Main Menu")
            .styleClass("btn-secondary")
            .onClick(e -> returnToMenu())
            .build();

    Button exitButton =
        new ButtonBuilder()
            .text("Exit")
            .styleClass("btn-danger")
            .onClick(e -> mainController.exitApplication())
            .build();

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    toolbar.getItems().addAll(mainMenuButton, spacer, exitButton);
    setTop(toolbar);
  }

  /** Returns to the game selection menu. */
  protected void returnToMenu() {
    mainController.showGameSelectionView();
  }

  /**
   * Displays the victory screen for the winning player.
   *
   * <p>Plays victory sound and shows the victory animation.
   *
   * @param winner The winning player
   */
  protected void showVictoryScreen(Player winner) {
    soundManager.playSound("victory");
    victoryScreen.showVictory(winner);
  }

  /**
   * Handles game events from the observed game controller.
   *
   * <p>Processes events on the JavaFX Application Thread.
   *
   * @param event The game event to handle
   */
  @Override
  public void onGameEvent(GameEvent event) {
    Platform.runLater(
        () -> {
          if (event instanceof GameEndedEvent endedEvent) {
            handleGameEnded(endedEvent);
          } else {
            handleGameEvent(event);
          }
        });
  }

  /**
   * Handles game ended events by showing the victory screen.
   *
   * @param event The game ended event
   */
  private void handleGameEnded(GameEndedEvent event) {
    Player winner = event.getWinner();
    if (winner != null) {
      showVictoryScreen(winner);
    }
  }

  /**
   * Abstract method to handle game-specific events.
   *
   * <p>Subclasses must implement this to handle their specific game events.
   *
   * @param event The game event to handle
   */
  protected abstract void handleGameEvent(GameEvent event);
}
