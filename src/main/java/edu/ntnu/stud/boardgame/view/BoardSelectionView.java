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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BoardSelectionView extends BorderPane implements BoardGameObserver {

  private final MainController controller;
  private final GameController gameController;

  private final ObservableList<String> predefinedBoardItems = FXCollections.observableArrayList();
  private final ObservableList<String> savedBoardItems = FXCollections.observableArrayList();

  private ListView<String> predefinedBoardListView;
  private ListView<String> savedBoardListView;

  private Button loadBoardButton;
  private Button saveButton;
  private TextField saveNameField;
  private VBox saveActionsSubContainer;

  public BoardSelectionView(MainController controller, GameController gameController) {
    if (controller == null) {
      throw new IllegalArgumentException("MainController cannot be null.");
    }
    if (gameController == null) {
      throw new IllegalArgumentException("GameController cannot be null.");
    }
    this.controller = controller;
    this.gameController = gameController;

    getStyleClass().add("board-selection-view");

    initializeUI();
  }

  private void initializeUI() {
    Button backButton = new ButtonBuilder()
        .text("Back")
        .styleClass("secondary-button")
        .onClick(event -> controller.showGameSelectionView())
        .build();

    Label titleLabel = new LabelBuilder()
        .text("Select or Create a Board Configuration")
        .styleClass("title")
        .build();
    StackPane titleContainer = new StackPane(titleLabel);
    StackPane.setAlignment(titleLabel, Pos.CENTER);

    HBox headerBox = new HBox(15, backButton, titleContainer);
    headerBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(titleContainer, Priority.ALWAYS);
    headerBox.setPadding(new Insets(0, 0, 20, 0));

    VBox predefinedSection = new VBox(15);
    predefinedSection.setAlignment(Pos.TOP_LEFT);
    Label predefinedLabel = new LabelBuilder()
        .text("Available Predefined Boards")
        .styleClass("section-title")
        .build();

    predefinedBoardListView = new ListView<>(predefinedBoardItems);
    predefinedBoardListView.getStyleClass().add("board-list");
    predefinedBoardListView.setPrefHeight(250);
    VBox.setVgrow(predefinedBoardListView, Priority.ALWAYS);
    predefinedSection.getChildren().addAll(predefinedLabel, predefinedBoardListView);

    VBox savedSection = new VBox(15);
    savedSection.setAlignment(Pos.TOP_LEFT);
    Label savedLabel = new LabelBuilder()
        .text("Your Saved Board Configurations")
        .styleClass("section-title")
        .build();

    savedBoardListView = new ListView<>(savedBoardItems);
    savedBoardListView.getStyleClass().add("board-list");
    savedBoardListView.setPrefHeight(250);
    VBox.setVgrow(savedBoardListView, Priority.ALWAYS);
    savedSection.getChildren().addAll(savedLabel, savedBoardListView);

    HBox boardsContainer = new HBox(30);
    boardsContainer.setAlignment(Pos.TOP_CENTER);
    boardsContainer.getChildren().addAll(predefinedSection, savedSection);
    HBox.setHgrow(predefinedSection, Priority.ALWAYS);
    HBox.setHgrow(savedSection, Priority.ALWAYS);

    loadBoardButton = new ButtonBuilder()
        .text("Load Selected Board")
        .styleClass("action-button")
        .onClick(event -> loadSelectedBoard())
        .disabled(true)
        .build();

    Label saveSectionLabel = new LabelBuilder()
        .text("Save Current Selection As:")
        .styleClass("text-body-bold")
        .build();

    saveNameField = new TextField();
    saveNameField.setPromptText("Enter new configuration name");
    saveNameField.setPrefWidth(300);

    saveButton = new ButtonBuilder()
        .text("Save Configuration")
        .styleClass("create-button")
        .onClick(event -> saveBoardAs())
        .disabled(true)
        .build();

    HBox saveEntryBox = new HBox(10, saveNameField, saveButton);
    saveEntryBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(saveNameField, Priority.ALWAYS);

    saveActionsSubContainer = new VBox(8, saveSectionLabel, saveEntryBox);
    saveActionsSubContainer.setAlignment(Pos.CENTER_LEFT);
    saveActionsSubContainer.setPadding(new Insets(10, 0, 0, 0));

    VBox mainActionsContainer = new VBox(15);
    mainActionsContainer.setAlignment(Pos.CENTER);
    mainActionsContainer.getChildren().addAll(loadBoardButton, saveActionsSubContainer);
    mainActionsContainer.setPadding(new Insets(25, 0, 25, 0));

    VBox contentBox = new VBox(25);
    contentBox.setPadding(new Insets(0, 30, 30, 30));
    contentBox.getChildren().addAll(headerBox, boardsContainer, mainActionsContainer);
    contentBox.setAlignment(Pos.TOP_CENTER);

    setCenter(contentBox);

    predefinedBoardListView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal != null) {
            savedBoardListView.getSelectionModel().clearSelection();
            String displayName = getDisplayNameFromPredefined(newVal);
            saveNameField.setText(displayName);
            saveNameField.setPromptText("Confirm or change name: '" + displayName + "'");
            enableActions(true);
          } else if (savedBoardListView.getSelectionModel().getSelectedItem() == null) {
            enableActions(false);
          }
        });

    savedBoardListView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal != null) {
            predefinedBoardListView.getSelectionModel().clearSelection();
            saveNameField.setText(newVal);
            saveNameField.setPromptText("Confirm or change name: '" + newVal + "'");
            enableActions(true);
          } else if (predefinedBoardListView.getSelectionModel().getSelectedItem() == null) {
            enableActions(false);
          }
        });

    enableActions(false);
  }

  private void enableActions(boolean enable) {
    loadBoardButton.setDisable(!enable);
    saveNameField.setDisable(!enable);
    saveButton.setDisable(!enable);

    if (saveActionsSubContainer != null) {
      saveActionsSubContainer.setVisible(enable);
      saveActionsSubContainer.setManaged(enable);
    }

    if (!enable) {
      saveNameField.clear();
      saveNameField.setPromptText("Enter name for saving selected board");
    }
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

  private void loadSelectedBoard() {
    String selectedBoard = predefinedBoardListView.getSelectionModel().getSelectedItem();
    if (selectedBoard == null) {
      selectedBoard = savedBoardListView.getSelectionModel().getSelectedItem();
    }

    if (selectedBoard != null && !selectedBoard.isEmpty()) {
      String boardKeyToLoad = selectedBoard;
      if (selectedBoard.startsWith("Predefined:")) {

      }
      gameController.selectBoard(boardKeyToLoad);
    } else {
      controller.showErrorDialog("Selection Error", "Please select a board to load.");
    }
  }

  private String getDisplayNameFromPredefined(String predefinedId) {
    if (predefinedId != null && predefinedId.startsWith("Predefined:")) {
      return predefinedId.substring("Predefined:".length()).trim();
    }
    return predefinedId != null ? predefinedId.trim() : "";
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

  @Override
  public void onGameEvent(GameEvent event) {
  }
}