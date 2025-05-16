package edu.ntnu.stud.boardgame.common.view;

import edu.ntnu.stud.boardgame.common.controller.MainMenuController;
import edu.ntnu.stud.boardgame.core.navigation.ViewControllerFactory.ViewName;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.core.view.ui.Panel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainMenuView extends VBox {

  private final MainMenuController controller;

  public MainMenuView(MainMenuController controller) {
    this.controller = controller;
    setAlignment(Pos.TOP_LEFT);
    setSpacing(20);
  }

  public void setupComponents() {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10));

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(50);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);
    grid.getColumnConstraints().addAll(col1, col2);

    Panel SnakesAndLaddersPanel = createSnakesAndLaddersPanel();

    grid.add(SnakesAndLaddersPanel, 0, 0);

    getChildren().add(grid);
  }

  private Panel createSnakesAndLaddersPanel() {
    Label title = Label.builder().text("Snakes and Ladders").fontSize("5xl").build();
    Label description = Label.builder()
        .text("Play a game of snakes and ladders. Choose between presets and saved games")
        .fontSize("lg").wrapText(true).build();
    Button startButton = Button.builder().text("Play Snakes and Ladders").styleClass("primary")
        .onAction(event -> controller.navigateTo(ViewName.SL_GAME_SELECTION)).build();
    Panel card = Panel.builder().children(title, description, startButton).rounded(true)
        .bordered(true).build();

    return card;
  }
}
