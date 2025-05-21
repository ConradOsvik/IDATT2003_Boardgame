package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.GameCreatedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.TextFieldBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.ColumnConstraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerSetupView extends BorderPane implements BoardGameObserver {

  private final MainController controller;
  private final GameController gameController;
  private final ObservableList<String> playersList = FXCollections.observableArrayList();
  private TextField playerNameField;
  private ComboBox<PieceType> pieceTypeComboBox;
  private TextField newPlayerListNameField;
  private Button savePlayersButton;

  private ListView<String> savedPlayerListsView;
  private final ObservableList<String> savedPlayerListItems = FXCollections.observableArrayList();
  private Button loadSelectedPlayerListButton;

  private final List<PieceType> allPieceTypes = new ArrayList<>(Arrays.asList(PieceType.values()));
  private final ObservableList<PieceType> availablePieceTypesObservableList = FXCollections.observableArrayList();

  public PlayerSetupView(MainController controller, GameController gameController) {
    this.controller = controller;
    this.gameController = gameController;
    gameController.registerObserver(this);

    getStyleClass().add("player-setup-view");

    initializeUI();
    resetAvailablePieces();
    refreshSavedPlayerLists();
  }

  private void initializeUI() {
    Button backButton = new ButtonBuilder()
        .text("Back")
        .styleClass("secondary-button")
        .onClick(event -> controller.showBoardSelectionView())
        .build();

    Label titleLabel = new LabelBuilder().text("Set Up Your Players").styleClass("title").build();
    StackPane titleContainer = new StackPane(titleLabel);
    StackPane.setAlignment(titleLabel, Pos.CENTER);

    HBox headerBox = new HBox(15, backButton, titleContainer);
    headerBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(titleContainer, Priority.ALWAYS);
    headerBox.setPadding(new Insets(0, 0, 20, 0));

    HBox mainContent = new HBox(40);
    mainContent.setAlignment(Pos.TOP_CENTER);

    VBox playerCreationPanel = createPlayerCreationPanel();
    VBox playerFilePanel = createPlayerFilePanel();

    HBox.setHgrow(playerCreationPanel, Priority.ALWAYS);
    HBox.setHgrow(playerFilePanel, Priority.ALWAYS);

    mainContent.getChildren().addAll(playerCreationPanel, playerFilePanel);

    HBox bottomActionsBox = createBottomActionButtons();

    VBox contentBox = new VBox(30);
    contentBox.setPadding(new Insets(0, 30, 30, 30));
    contentBox.getChildren().addAll(headerBox, mainContent, bottomActionsBox);
    contentBox.setAlignment(Pos.TOP_CENTER);

    setCenter(contentBox);
    updateSavePlayersButtonState();
    updateLoadPlayerListButtonState();
  }

  private VBox createPlayerCreationPanel() {
    VBox panel = new VBox(20);
    panel.getStyleClass().add("setup-container");
    panel.setPrefWidth(380);
    panel.setMaxWidth(Double.MAX_VALUE);

    Label sectionTitle = new LabelBuilder().text("Add Players Manually").styleClass("section-title").build();

    GridPane inputGrid = new GridPane();
    inputGrid.setHgap(10);
    inputGrid.setVgap(15);
    inputGrid.setAlignment(Pos.CENTER_LEFT);

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setHgrow(Priority.NEVER);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setHgrow(Priority.ALWAYS);
    inputGrid.getColumnConstraints().addAll(col1, col2);

    Label nameLabel = new LabelBuilder().text("Player Name:").build();
    playerNameField = new TextFieldBuilder().promptText("E.g., Alex")
        .styleClass("input-field").build();
    playerNameField.setMaxWidth(Double.MAX_VALUE);

    Label pieceLabel = new LabelBuilder().text("Select Piece:").build();
    pieceTypeComboBox = new ComboBox<>(availablePieceTypesObservableList);
    pieceTypeComboBox.getStyleClass().add("combo-box");
    pieceTypeComboBox.setPromptText("Choose an available piece");
    pieceTypeComboBox.setMaxWidth(Double.MAX_VALUE);

    inputGrid.add(nameLabel, 0, 0);
    inputGrid.add(playerNameField, 1, 0);
    inputGrid.add(pieceLabel, 0, 1);
    inputGrid.add(pieceTypeComboBox, 1, 1);

    Button addPlayerButton = new ButtonBuilder().text("Add This Player")
        .styleClass("action-button")
        .onClick(event -> addPlayer()).build();
    HBox addPlayerButtonContainer = new HBox(addPlayerButton);
    addPlayerButtonContainer.setAlignment(Pos.CENTER_RIGHT);

    Label playersLabel = new LabelBuilder().text("Current Players for This Game:").styleClass("text-body-bold").build();

    ListView<String> playersListView = new ListView<>(playersList);
    playersListView.getStyleClass().add("player-list");
    playersListView.setPrefHeight(180);
    VBox.setVgrow(playersListView, Priority.ALWAYS);

    panel.getChildren()
        .addAll(sectionTitle, inputGrid, addPlayerButtonContainer, playersLabel, playersListView);

    return panel;
  }

  private VBox createPlayerFilePanel() {
    VBox panel = new VBox(20);
    panel.getStyleClass().add("setup-container");
    panel.setPrefWidth(380);
    panel.setMaxWidth(Double.MAX_VALUE);

    Label sectionTitle = new LabelBuilder().text("Manage Player Lists").styleClass("section-title").build();

    Label availableListsLabel = new LabelBuilder().text("Available Saved Lists:").styleClass("text-body-bold").build();
    savedPlayerListsView = new ListView<>(savedPlayerListItems);
    savedPlayerListsView.getStyleClass().add("player-list");
    savedPlayerListsView.setPrefHeight(150);
    VBox.setVgrow(savedPlayerListsView, Priority.SOMETIMES);

    loadSelectedPlayerListButton = new ButtonBuilder().text("Load Selected List")
        .styleClass("secondary-button")
        .onClick(event -> loadSelectedPlayerList())
        .build();

    VBox loadingSection = new VBox(10, availableListsLabel, savedPlayerListsView, loadSelectedPlayerListButton);
    loadingSection.setAlignment(Pos.CENTER_LEFT);

    Label saveAsLabel = new LabelBuilder().text("Save Current Player List As:").styleClass("text-body-bold").build();

    newPlayerListNameField = new TextFieldBuilder().promptText("Enter name for new list")
        .styleClass("input-field")
        .build();
    newPlayerListNameField.setMaxWidth(Double.MAX_VALUE);

    savePlayersButton = new ButtonBuilder().text("Save List")
        .styleClass("secondary-button")
        .onClick(event -> saveCurrentPlayersAs())
        .build();

    VBox savingSection = new VBox(10, saveAsLabel, newPlayerListNameField, savePlayersButton);
    savingSection.setAlignment(Pos.CENTER_LEFT);

    Label fileInfoLabel = new LabelBuilder()
        .text("Select a list to load, or save the current set of players with a new name.")
        .wrapText(true)
        .styleClass("text-body")
        .build();
    fileInfoLabel.setPadding(new Insets(10, 0, 0, 0));

    panel.getChildren().addAll(sectionTitle, loadingSection, savingSection, fileInfoLabel);

    savedPlayerListsView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      updateLoadPlayerListButtonState();
    });

    return panel;
  }

  private HBox createBottomActionButtons() {
    HBox buttonsBox = new HBox(20);
    buttonsBox.setAlignment(Pos.CENTER);

    Button startGameButton = new ButtonBuilder().text("Start Game!")
        .styleClass("action-button")
        .onClick(event -> startGame()).build();

    buttonsBox.getChildren().addAll(startGameButton);

    return buttonsBox;
  }

  private void addPlayer() {
    String playerName = playerNameField.getText().trim();
    PieceType selectedPiece = pieceTypeComboBox.getValue();

    if (playerName.isEmpty() || selectedPiece == null) {
      controller.showErrorDialog("Input Error", "Please enter a player name and select a piece.");
      return;
    }

    if (gameController.addPlayer(playerName, selectedPiece)) {
      playerNameField.clear();
    } else {
      Platform.runLater(this::refreshAvailablePiecesFromModelState);
    }
  }

  private void loadSelectedPlayerList() {
    String selectedList = savedPlayerListsView.getSelectionModel().getSelectedItem();
    if (selectedList != null && !selectedList.isEmpty()) {
      gameController.loadPlayers(selectedList);
    } else {
      controller.showErrorDialog("Selection Error", "Please select a player list to load.");
    }
  }

  private void saveCurrentPlayersAs() {
    String listName = newPlayerListNameField.getText().trim();
    if (listName.isEmpty()) {
      controller.showErrorDialog("Input Error", "Please enter a name for the player list.");
      return;
    }
    if (playersList.isEmpty()) {
      controller.showErrorDialog("No Players", "Add some players before saving a list.");
      return;
    }
    gameController.savePlayers(listName);
    refreshSavedPlayerLists();
    newPlayerListNameField.clear();
  }

  private void startGame() {
    if (playersList.isEmpty()) {
      controller.showErrorDialog("No Players Added",
          "Please add at least one player to start the game, or load a player list.");
      return;
    }
    if (playersList.size() < 2) {
      controller.showErrorDialog("Not Enough Players",
          "Most games require at least 2 players. Please add more players.");
      return;
    }

    controller.showGameView();
  }

  private void updateSavePlayersButtonState() {
    if (savePlayersButton != null && newPlayerListNameField != null) {
      boolean canSave = !playersList.isEmpty();
      savePlayersButton.setVisible(canSave);
      savePlayersButton.setManaged(canSave);
      newPlayerListNameField.setVisible(canSave);
      newPlayerListNameField.setManaged(canSave);
    }
  }

  private void updateLoadPlayerListButtonState() {
    if (loadSelectedPlayerListButton != null) {
      boolean selectionExists = savedPlayerListsView != null
          && savedPlayerListsView.getSelectionModel().getSelectedItem() != null;
      loadSelectedPlayerListButton.setDisable(!selectionExists);
    }
  }

  private void refreshSavedPlayerLists() {
    List<String> availableNames = new ArrayList<>();
    try {
      availableNames = gameController.getAvailablePlayerListNames();
    } catch (Exception e) {
      System.err.println("Error fetching saved player lists: " + e.getMessage());
      e.printStackTrace();
      controller.showErrorDialog("Load Error", "Could not retrieve the list of saved player configurations.");
      savedPlayerListItems.clear();
      updateLoadPlayerListButtonState();
      return;
    }

    if (availableNames != null) {
      savedPlayerListItems.setAll(availableNames);
      FXCollections.sort(savedPlayerListItems);
    } else {
      savedPlayerListItems.clear();
    }
    updateLoadPlayerListButtonState();
  }

  private void refreshAvailablePiecesFromModelState() {
    availablePieceTypesObservableList.setAll(allPieceTypes);
    if (gameController.getGame() != null && gameController.getGame().getPlayers() != null) {
      for (Player player : gameController.getGame().getPlayers()) {
        if (player.getPiece() != null) {
          availablePieceTypesObservableList.remove(player.getPiece());
        }
      }
    }
    if (pieceTypeComboBox != null) {
      pieceTypeComboBox.getSelectionModel().clearSelection();
    }
  }

  @Override
  public void onGameEvent(GameEvent event) {
    if (event instanceof PlayerAddedEvent playerAddedEvent) {
      Player player = playerAddedEvent.getPlayer();
      Platform.runLater(() -> {
        if (!playersList.contains(player.getName() + " (" + player.getPiece() + ")")) {
          playersList.add(player.getName() + " (" + player.getPiece() + ")");
        }
        availablePieceTypesObservableList.remove(player.getPiece());
        updateSavePlayersButtonState();
        if (pieceTypeComboBox.getItems().isEmpty()) {
          controller.showInfoDialog("All Pieces Taken", "All available player pieces have been selected.");
        }
      });
    } else if (event instanceof GameCreatedEvent) {
      Platform.runLater(() -> {
        playersList.clear();
        resetAvailablePieces();
        updateSavePlayersButtonState();
      });
    }
  }

  private void resetAvailablePieces() {
    availablePieceTypesObservableList.setAll(allPieceTypes);
    if (gameController.getGame() != null && gameController.getGame().getPlayers() != null) {
      for (Player p : gameController.getGame().getPlayers()) {
        if (p.getPiece() != null) {
          availablePieceTypesObservableList.remove(p.getPiece());
        }
      }
    }
    if (pieceTypeComboBox != null) {
      pieceTypeComboBox.getSelectionModel().clearSelection();
      pieceTypeComboBox.setPromptText("Choose an available piece");
    }
  }
}