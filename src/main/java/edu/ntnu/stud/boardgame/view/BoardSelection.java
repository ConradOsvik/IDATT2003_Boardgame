package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BoardSelection extends BorderPane implements BoardGameObserver {

  private final MainController controller;
  private final GameController gameController;
  private final ObservableList<String> boardItems = FXCollections.observableArrayList();
  private ListView<String> boardListView;

  public BoardSelection(MainController controller, GameController gameController) {
    this.controller = controller;
    this.gameController = gameController;

    getStyleClass().add("board-selection-view");

    initializeUI();
  }

  private void initializeUI() {
    Label titleLabel = new LabelBuilder()
        .text("Select a Board")
        .styleClass("title")
        .build();

    boardListView = new ListView<>(boardItems);
    boardListView.getStyleClass().add("board-list");
    boardListView.setPrefHeight(400);

    Button selectButton = new ButtonBuilder()
        .text("Select Board")
        .styleClass("action-button")
        .onClick(event -> selectBoard())
        .build();

    Button createBoardButton = new ButtonBuilder()
        .text("Create New Board")
        .styleClass("create-button")
        .onClick(event -> createNewBoard())
        .build();

    Button backButton = new ButtonBuilder()
        .text("Back")
        .styleClass("secondary-button")
        .onClick(event -> controller.showGameSelectionView())
        .build();

    VBox buttonsBox = new VBox(10);
    buttonsBox.setAlignment(Pos.CENTER);
    buttonsBox.getChildren().addAll(selectButton, createBoardButton, backButton);
    buttonsBox.setPadding(new Insets(20, 0, 0, 0));

    VBox contentBox = new VBox(20);
    contentBox.setPadding(new Insets(20));
    contentBox.getChildren().addAll(titleLabel, boardListView, buttonsBox);
    contentBox.setAlignment(Pos.TOP_CENTER);

    setCenter(contentBox);
  }

  public void refreshBoardList() {
    List<String> availableBoards = gameController.getAvailableBoards();
    boardItems.clear();

    if (availableBoards != null && !availableBoards.isEmpty()) {
      boardItems.addAll(availableBoards);
    } else {
      boardItems.add("Default");
    }
  }

  private void selectBoard() {
    String selectedBoard = boardListView.getSelectionModel().getSelectedItem();
    if (selectedBoard != null && !selectedBoard.isEmpty()) {
      gameController.selectBoard(selectedBoard);
    } else {
      controller.showErrorDialog("Selection Error", "Please select a board.");
    }
  }

  private void createNewBoard() {
    controller.showErrorDialog("Not Implemented",
        "Creating new boards is not implemented yet. Select one of the existing boards.");
  }

  @Override
  public void onGameEvent(GameEvent event) {
  }
}