package edu.ntnu.stud.boardgame.snakesandladders.view.components.player;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class PlayerManager extends GameComponent<VBox> {

  private final PlayerList playerList;
  private final PlayerRegistration playerRegistration;

  public PlayerManager(SlGameController controller, ErrorDialog errorDialog) {
    super(controller, new VBox(10), errorDialog);

    this.playerList = new PlayerList(controller, errorDialog);
    this.playerRegistration = new PlayerRegistration(controller, errorDialog);

    setupLayout();
  }

  private void setupLayout() {
    getNode().setPadding(new Insets(5));

    // Create collapsible sections
    TitledPane playerListPane = new TitledPane("Current Players", playerList.getNode());
    playerListPane.setCollapsible(true);
    playerListPane.setExpanded(true);

    TitledPane addPlayerPane = new TitledPane("Add Player", playerRegistration.getNode());
    addPlayerPane.setCollapsible(true);
    addPlayerPane.setExpanded(true);

    getNode().getChildren().addAll(
        playerListPane,
        addPlayerPane
    );
  }

  @Override
  public void onGameEvent(GameEvent event) {
    // Child components handle their own events as they inherit from GameComponent
  }
}