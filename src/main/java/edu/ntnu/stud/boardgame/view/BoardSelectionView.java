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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class BoardSelectionView extends BorderPane implements BoardGameObserver {

  private final MainController controller;
  private final GameController gameController;

  private final ObservableList<String> predefinedBoardItems = FXCollections.observableArrayList();
  private final ObservableList<String> savedBoardItems = FXCollections.observableArrayList();

  private ListView<String> predefinedBoardListView;
  private ListView<String> savedBoardListView;

  private Button selectPredefinedButton;
  private Button selectSavedButton;
  private Button saveButton;
  private TextField saveNameField;

  public BoardSelectionView(MainController controller, GameController gameController) {
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

    Label predefinedLabel = new LabelBuilder()
        .text("Predefined Boards")
        .styleClass("section-title")
        .build();

    predefinedBoardListView = new ListView<>(predefinedBoardItems);
    predefinedBoardListView.getStyleClass().add("board-list");
    predefinedBoardListView.setPrefHeight(200);

    selectPredefinedButton = new ButtonBuilder()
        .text("Select Predefined Board")
        .styleClass("action-button")
        .onClick(event -> selectPredefinedBoard())
        .build();

    VBox predefinedSection = new VBox(10);
    predefinedSection.getChildren()
        .addAll(predefinedLabel, predefinedBoardListView, selectPredefinedButton);

    Label savedLabel = new LabelBuilder()
        .text("User Saved Boards")
        .styleClass("section-title")
        .build();

    savedBoardListView = new ListView<>(savedBoardItems);
    savedBoardListView.getStyleClass().add("board-list");
    savedBoardListView.setPrefHeight(200);

    selectSavedButton = new ButtonBuilder()
        .text("Select Saved Board")
        .styleClass("action-button")
        .onClick(event -> selectSavedBoard())
        .build();

    VBox savedSection = new VBox(10);
    savedSection.getChildren().addAll(savedLabel, savedBoardListView, selectSavedButton);

    HBox saveBox = new HBox(10);
    saveBox.setAlignment(Pos.CENTER);

    saveNameField = new TextField();
    saveNameField.setPromptText("Enter name for saving selected board");
    saveNameField.setPrefWidth(250);

    saveButton = new ButtonBuilder()
        .text("Save Selected Board")
        .styleClass("create-button")
        .onClick(event -> saveBoardAs())
        .build();

    saveBox.getChildren().addAll(saveNameField, saveButton);

    HBox boardsContainer = new HBox(20);
    boardsContainer.getChildren().addAll(predefinedSection, savedSection);
    HBox.setHgrow(predefinedSection, Priority.ALWAYS);
    HBox.setHgrow(savedSection, Priority.ALWAYS);

    Button backButton = new ButtonBuilder()
        .text("Back")
        .styleClass("secondary-button")
        .onClick(event -> controller.showGameSelectionView())
        .build();

    VBox contentBox = new VBox(20);
    contentBox.setPadding(new Insets(20));
    contentBox.getChildren().addAll(titleLabel, boardsContainer, saveBox, backButton);
    contentBox.setAlignment(Pos.TOP_CENTER);

    setCenter(contentBox);

    predefinedBoardListView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal != null) {
            savedBoardListView.getSelectionModel().clearSelection();
            saveNameField.setText(getDisplayNameFromPredefined(newVal));
          }
        });

    savedBoardListView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal != null) {
            predefinedBoardListView.getSelectionModel().clearSelection();
            saveNameField.setText(newVal);
          }
        });
  }

  public void refreshBoardList() {
    List<String> availableBoards = gameController.getAvailableBoards();

    predefinedBoardItems.clear();
    savedBoardItems.clear();

    if (availableBoards != null && !availableBoards.isEmpty()) {
      for (String board : availableBoards) {
        if (board.startsWith("Predefined:")) {
          predefinedBoardItems.add(board);
        } else {
          savedBoardItems.add(board);
        }
      }
    }

    FXCollections.sort(predefinedBoardItems);
    FXCollections.sort(savedBoardItems);
  }

  private void selectPredefinedBoard() {
    String selectedBoard = predefinedBoardListView.getSelectionModel().getSelectedItem();
    if (selectedBoard != null && !selectedBoard.isEmpty()) {
      gameController.selectBoard(selectedBoard);
    } else {
      controller.showErrorDialog("Selection Error", "Please select a predefined board.");
    }
  }

  private void selectSavedBoard() {
    String selectedBoard = savedBoardListView.getSelectionModel().getSelectedItem();
    if (selectedBoard != null && !selectedBoard.isEmpty()) {
      gameController.selectBoard(selectedBoard);
    } else {
      controller.showErrorDialog("Selection Error", "Please select a saved board.");
    }
  }

  private void saveBoardAs() {
    String boardName = saveNameField.getText().trim();
    if (boardName.isEmpty()) {
      controller.showErrorDialog("Input Error", "Please enter a name for the board.");
      return;
    }

    String selectedPredefined = predefinedBoardListView.getSelectionModel().getSelectedItem();
    String selectedSaved = savedBoardListView.getSelectionModel().getSelectedItem();

    if (selectedPredefined == null && selectedSaved == null) {
      controller.showErrorDialog("Selection Error", "Please select a board to save.");
      return;
    }

    String boardToSave = selectedPredefined != null ? selectedPredefined : selectedSaved;

    if (gameController.saveSelectedBoardAs(boardToSave, boardName)) {
      refreshBoardList();
    }
  }

  private String getDisplayNameFromPredefined(String predefinedId) {
    if (predefinedId != null && predefinedId.startsWith("Predefined:")) {
      return predefinedId.substring("Predefined:".length());
    }
    return predefinedId;
  }

  @Override
  public void onGameEvent(GameEvent event) {
  }
}