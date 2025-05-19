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
import javafx.scene.layout.VBox;

public class PlayerSetupView extends BorderPane implements BoardGameObserver {

  private final MainController controller;
  private final GameController gameController;
  private final ObservableList<String> playersList = FXCollections.observableArrayList();
  private TextField playerNameField;
  private ComboBox<PieceType> pieceTypeComboBox;
  private ListView<String> playersListView;
  private TextField fileNameField;

  public PlayerSetupView(MainController controller, GameController gameController) {
    this.controller = controller;
    this.gameController = gameController;
    gameController.registerObserver(this);

    getStyleClass().add("player-setup-view");

    initializeUI();
  }

  private void initializeUI() {
    Label titleLabel = new LabelBuilder().text("Player Setup").styleClass("title").build();

    VBox leftPanel = createPlayerCreationPanel();

    VBox rightPanel = createPlayerFilePanel();

    HBox buttonsBox = createActionButtons();

    HBox mainContent = new HBox(30);
    mainContent.setAlignment(Pos.CENTER);
    mainContent.getChildren().addAll(leftPanel, rightPanel);

    VBox contentBox = new VBox(20);
    contentBox.setPadding(new Insets(20));
    contentBox.getChildren().addAll(titleLabel, mainContent, buttonsBox);
    contentBox.setAlignment(Pos.TOP_CENTER);

    setCenter(contentBox);
  }

  private VBox createPlayerCreationPanel() {
    VBox panel = new VBox(15);
    panel.getStyleClass().add("setup-container");
    panel.setPadding(new Insets(20));
    panel.setMinWidth(350);

    Label sectionTitle = new LabelBuilder().text("Add Players").styleClass("section-title").build();

    Label nameLabel = new LabelBuilder().text("Player Name:").build();

    playerNameField = new TextFieldBuilder().promptText("Enter player name")
        .styleClass("input-field").build();

    Label pieceLabel = new LabelBuilder().text("Select Piece:").build();

    pieceTypeComboBox = new ComboBox<>();
    pieceTypeComboBox.getItems().addAll(PieceType.values());
    pieceTypeComboBox.getStyleClass().add("combo-box");
    pieceTypeComboBox.setPromptText("Select a piece");

    Button addPlayerButton = new ButtonBuilder().text("Add Player").styleClass("action-button")
        .onClick(event -> addPlayer()).build();

    Label playersLabel = new LabelBuilder().text("Current Players:").build();

    playersListView = new ListView<>(playersList);
    playersListView.getStyleClass().add("player-list");
    playersListView.setPrefHeight(200);

    GridPane inputGrid = new GridPane();
    inputGrid.setHgap(10);
    inputGrid.setVgap(10);
    inputGrid.add(nameLabel, 0, 0);
    inputGrid.add(playerNameField, 1, 0);
    inputGrid.add(pieceLabel, 0, 1);
    inputGrid.add(pieceTypeComboBox, 1, 1);

    panel.getChildren()
        .addAll(sectionTitle, inputGrid, addPlayerButton, playersLabel, playersListView);

    return panel;
  }

  private VBox createPlayerFilePanel() {
    VBox panel = new VBox(15);
    panel.getStyleClass().add("setup-container");
    panel.setPadding(new Insets(20));
    panel.setMinWidth(350);

    Label sectionTitle = new LabelBuilder().text("Load/Save Players").styleClass("section-title")
        .build();

    Label fileNameLabel = new LabelBuilder().text("File Name:").build();

    fileNameField = new TextFieldBuilder().promptText("Enter file name (without extension)")
        .styleClass("input-field").build();

    Button loadPlayersButton = new ButtonBuilder().text("Load Players").styleClass("action-button")
        .width(150).onClick(event -> loadPlayers()).build();

    Button savePlayersButton = new ButtonBuilder().text("Save Players").styleClass("action-button")
        .width(150).onClick(event -> savePlayers()).build();

    GridPane inputGrid = new GridPane();
    inputGrid.setHgap(10);
    inputGrid.setVgap(10);
    inputGrid.add(fileNameLabel, 0, 0);
    inputGrid.add(fileNameField, 1, 0);

    VBox buttonsBox = new VBox(10);
    buttonsBox.setAlignment(Pos.CENTER);
    buttonsBox.getChildren().addAll(loadPlayersButton, savePlayersButton);

    panel.getChildren().addAll(sectionTitle, inputGrid, buttonsBox);

    return panel;
  }

  private HBox createActionButtons() {
    HBox buttonsBox = new HBox(20);
    buttonsBox.setAlignment(Pos.CENTER);
    buttonsBox.setPadding(new Insets(20, 0, 0, 0));

    Button backButton = new ButtonBuilder().text("Back").styleClass("secondary-button")
        .onClick(event -> controller.showBoardSelectionView()).build();

    Button startGameButton = new ButtonBuilder().text("Start Game").styleClass("action-button")
        .onClick(event -> startGame()).build();

    buttonsBox.getChildren().addAll(backButton, startGameButton);

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
      pieceTypeComboBox.getSelectionModel().clearSelection();
    }
  }

  private void loadPlayers() {
    String fileName = fileNameField.getText().trim();
    if (fileName.isEmpty()) {
      controller.showErrorDialog("Input Error", "Please enter a file name.");
      return;
    }

    gameController.loadPlayers(fileName);
  }

  private void savePlayers() {
    String fileName = fileNameField.getText().trim();
    if (fileName.isEmpty()) {
      controller.showErrorDialog("Input Error", "Please enter a file name.");
      return;
    }

    gameController.savePlayers(fileName);
  }

  private void startGame() {
    if (playersList.isEmpty() || playersList.size() < 2) {
      controller.showErrorDialog("Not Enough Players",
          "Please add at least 2 players to start the game.");
      return;
    }

    controller.showGameView();
  }

  @Override
  public void onGameEvent(GameEvent event) {
    if (event instanceof PlayerAddedEvent playerAddedEvent) {
      Player player = playerAddedEvent.getPlayer();

      Platform.runLater(() -> {
        playersList.add(player.getName() + " (" + player.getPiece() + ")");
      });
    } else if (event instanceof GameCreatedEvent) {
      Platform.runLater(playersList::clear);
    }
  }
}