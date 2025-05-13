package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.ControlPanel;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.PlayerPanel;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.board.BoardGame;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SlGameView extends GameComponent<HBox> {

  public SlGameView(SlGameController controller, ErrorDialog errorDialog) {
    super(controller, new HBox(), errorDialog);
    PlayerPanel playerPanel = new PlayerPanel(controller, errorDialog);
    ControlPanel controlPanel = new ControlPanel(controller, errorDialog);
    BoardGame canvas = new BoardGame(controller, errorDialog);
    HBox.setHgrow(canvas.getNode(), Priority.ALWAYS);

    getNode().setPadding(new Insets(10));
    getNode().setSpacing(10);

    VBox infoContainer = new VBox();
    infoContainer.setSpacing(15);
    infoContainer.setPadding(new Insets(0, 0, 0, 0));
    infoContainer.getChildren().addAll(
        playerPanel.getNode(), controlPanel.getNode()
    );

    getNode().getChildren().addAll(infoContainer, canvas.getNode());
  }

  @Override
  public void onGameEvent(GameEvent event) {
  }
}