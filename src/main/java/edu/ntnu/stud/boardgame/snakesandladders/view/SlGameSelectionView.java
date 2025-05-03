package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.navigation.ViewControllerFactory.ViewName;
import edu.ntnu.stud.boardgame.core.view.BaseView;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameSelectionController;
import edu.ntnu.stud.boardgame.snakesandladders.factory.SlBoardGameFactory;
import javafx.scene.layout.VBox;

public class SlGameSelectionView extends VBox implements BaseView {

  private final SlGameSelectionController controller;

  public SlGameSelectionView(SlGameSelectionController controller) {
    this.controller = controller;
  }

  @Override
  public void render() {
    BoardGame game = SlBoardGameFactory.createClassicGame();

    getChildren().addAll(Label.builder().text("Sl Game Selection View").build(),
        Button.builder().text("Go to game")
            .onAction(event -> controller.navigateTo(ViewName.SL_GAME, game)).build());
  }
}
