package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.Controls;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.board.BoardGame;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.player.PlayerManager;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SlGameView extends GameComponent<BorderPane> {

  private final PlayerManager playerManager;
  private final Controls controls;
  private final BoardGame boardGame;

  public SlGameView(SlGameController controller, ErrorDialog errorDialog) {
    super(controller, new BorderPane(), errorDialog);

    // Create child components
    this.playerManager = new PlayerManager(controller, errorDialog);
    this.controls = new Controls(controller, errorDialog);
    this.boardGame = new BoardGame(controller, errorDialog);

    configureLayout();
  }

  private void configureLayout() {
    // Create left sidebar for player management and controls
    VBox sidebar = new VBox(15);
    sidebar.setPadding(new Insets(10));
    sidebar.setPrefWidth(300);
    sidebar.setMinWidth(300);
    sidebar.setMaxWidth(300);
    sidebar.getChildren().addAll(playerManager.getNode(), controls.getNode());

    // Set up the main layout
    getNode().setPadding(new Insets(10));
    getNode().setLeft(sidebar);
    getNode().setCenter(boardGame.getNode());

    // Make the board expand to fill available space
    BorderPane.setMargin(boardGame.getNode(), new Insets(0, 0, 0, 10));
    VBox.setVgrow(boardGame.getNode(), Priority.ALWAYS);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    // Individual components will handle their own event responses
  }
}