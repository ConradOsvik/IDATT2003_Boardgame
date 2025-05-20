package edu.ntnu.stud.boardgame.view.components;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.util.SoundManager;
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

public abstract class AbstractGameView extends BorderPane implements BoardGameObserver {

  protected final MainController mainController;
  protected final GameController gameController;
  protected final VictoryScreen victoryScreen;
  protected final SoundManager soundManager;
  protected final StackPane gameArea;

  public AbstractGameView(MainController mainController, GameController gameController) {
    this.mainController = mainController;
    this.gameController = gameController;
    this.gameController.registerObserver(this);
    this.soundManager = SoundManager.getInstance();
    this.victoryScreen = new VictoryScreen(gameController);
    this.gameArea = new StackPane();

    initializeBasicLayout();
    createToolbar();
  }

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

  private void createToolbar() {
    ToolBar toolbar = new ToolBar();

    Button mainMenuButton = new ButtonBuilder()
        .text("Main Menu")
        .styleClass("btn-secondary")
        .onClick(e -> returnToMenu())
        .build();

    Button exitButton = new ButtonBuilder()
        .text("Exit")
        .styleClass("btn-danger")
        .onClick(e -> mainController.exitApplication())
        .build();

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    toolbar.getItems().addAll(mainMenuButton, spacer, exitButton);
    setTop(toolbar);
  }

  protected void returnToMenu() {
    mainController.showGameSelectionView();
  }

  protected void showVictoryScreen(Player winner) {
    soundManager.playSound("victory");
    victoryScreen.showVictory(winner);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    Platform.runLater(() -> {
      if (event instanceof GameEndedEvent endedEvent) {
        handleGameEnded(endedEvent);
      } else {
        handleGameEvent(event);
      }
    });
  }

  private void handleGameEnded(GameEndedEvent event) {
    Player winner = event.getWinner();
    if (winner != null) {
      showVictoryScreen(winner);
    }
  }

  // Template method to be implemented by subclasses
  protected abstract void handleGameEvent(GameEvent event);
}