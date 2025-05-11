package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.navigation.ViewControllerFactory.ViewName;
import edu.ntnu.stud.boardgame.core.view.BaseView;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameSelectionController;
import edu.ntnu.stud.boardgame.snakesandladders.factory.SlBoardGameFactory;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.board.BoardPreview;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SlGameSelectionView extends GridPane implements BaseView {

  private final SlGameSelectionController controller;
  private final List<BoardGame> availableGames;

  public SlGameSelectionView(SlGameSelectionController controller) {
    this.controller = controller;
    this.availableGames = new ArrayList<>();

    availableGames.add(SlBoardGameFactory.createClassicGame());
    availableGames.add(SlBoardGameFactory.createDiceGame());

    configureLayout();
  }

  private void configureLayout() {
    setPadding(new Insets(20));
    setHgap(20);
    setVgap(20);
    setAlignment(Pos.CENTER);
  }

  @Override
  public void render() {
    getChildren().clear();

    Label headerLabel = Label.builder().text("Select a Game").build();
    headerLabel.getStyleClass().add("header-label");
    add(headerLabel, 0, 0, 2, 1);

    int row = 1;
    int col = 0;

    for (BoardGame game : availableGames) {
      add(createGameCell(game), col, row);

      col++;
      if (col >= 2) {
        col = 0;
        row++;
      }
    }

    if (col < 2) {
      add(createNewGameCell(), col, row);
    }
  }

  private VBox createGameCell(BoardGame game) {
    VBox cell = new VBox(10);
    cell.setPadding(new Insets(15));
    cell.setAlignment(Pos.CENTER);
    cell.getStyleClass().add("game-cell");

    Label titleLabel = Label.builder().text("classic").build();

    BoardPreview preview = new BoardPreview();
    if (game.getBoard() instanceof SlBoard slBoard) {
      preview.setBoard(slBoard);
    }

    Label descriptionLabel = Label.builder().text("Classic snake and ladders game").build();
    descriptionLabel.setWrapText(true);

    Button playButton = Button.builder()
        .text("Play")
        .onAction(event -> controller.navigateTo(ViewName.SL_GAME, game))
        .build();

    cell.getChildren().addAll(titleLabel, preview, descriptionLabel, playButton);
    VBox.setVgrow(preview, Priority.ALWAYS);

    return cell;
  }

  private VBox createNewGameCell() {
    VBox cell = new VBox(10);
    cell.setPadding(new Insets(15));
    cell.setAlignment(Pos.CENTER);
    cell.getStyleClass().add("new-game-cell");

    Label titleLabel = Label.builder().text("Create New Game").build();

    VBox placeholder = new VBox();
    placeholder.setMinHeight(200);
    placeholder.setStyle("-fx-border-style: dashed; -fx-border-color: gray;");

    Label descriptionLabel = Label.builder().text("Create a custom snake and ladders game").build();
    descriptionLabel.setWrapText(true);

    Button createButton = Button.builder()
        .text("Create")
        .onAction(event -> {
        })
        .build();

    cell.getChildren().addAll(titleLabel, placeholder, descriptionLabel, createButton);
    VBox.setVgrow(placeholder, Priority.ALWAYS);

    return cell;
  }
}