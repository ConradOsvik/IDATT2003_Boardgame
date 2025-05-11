package edu.ntnu.stud.boardgame.snakesandladders.view.components;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameStartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.ComboBox;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.core.view.ui.Panel;
import edu.ntnu.stud.boardgame.core.view.ui.TextField;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PlayerPanel extends GameComponent<Panel> {

  private static final Logger LOGGER = Logger.getLogger(PlayerPanel.class.getName());

  private final ObservableList<String> playerNames = FXCollections.observableArrayList();
  private final SlGameController controller;

  private final ObservableList<TokenOption> allTokens = FXCollections.observableArrayList();
  private final ObservableList<TokenOption> availableTokens = FXCollections.observableArrayList();
  private TextField playerNameField;
  private ComboBox<TokenOption> tokenComboBox;
  private Button addPlayerButton;

  public PlayerPanel(SlGameController controller) {
    super(controller, new Panel());
    this.controller = controller;

    Label titleLabel = Label.builder().text("Players").build();

    ListView<String> playerListView = new ListView<>(playerNames);
    playerListView.setPrefHeight(200);

    setupAddPlayerControls();

    getNode().getChildren().addAll(
        titleLabel,
        playerListView,
        Label.builder().text("Add new player").build(),
        playerNameField,
        tokenComboBox,
        addPlayerButton
    );
  }

  private static class TokenOption {

    private final String tokenPath;
    private final String tokenName;
    private final int tokenId;

    public TokenOption(int tokenId) {
      this.tokenId = tokenId;
      this.tokenPath = "/pieces/snakesandladders/" + tokenId + ".png";
      this.tokenName = "Token " + tokenId;
    }

    public String getTokenPath() {
      return tokenPath;
    }

    public int getTokenId() {
      return tokenId;
    }

    @Override
    public String toString() {
      return tokenName;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      TokenOption that = (TokenOption) o;
      return tokenId == that.tokenId;
    }

    @Override
    public int hashCode() {
      return Integer.hashCode(tokenId);
    }
  }

  private void setupAddPlayerControls() {
    playerNameField = TextField.builder().promptText("Player Name").disabled(true).build();

    for (int i = 1; i <= 5; i++) {
      TokenOption token = new TokenOption(i);
      allTokens.add(token);
      availableTokens.add(token);
    }

    tokenComboBox = ComboBox.<TokenOption>builder()
        .items(availableTokens)
        .promptText("Select token")
        .disabled(true)
        .build();
    tokenComboBox.setPrefWidth(200);

    tokenComboBox.setCellFactory(param -> new ListCell<>() {
      private final ImageView imageView = new ImageView();

      {
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);
      }

      @Override
      protected void updateItem(TokenOption item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          try {
            Image image = new Image(getClass().getResourceAsStream(item.getTokenPath()));
            imageView.setImage(image);

            HBox box = new HBox(5);
            box.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().addAll(imageView, new javafx.scene.control.Label(item.toString()));

            setGraphic(box);
            setText(null);
          } catch (Exception e) {
            setText(item.toString());
            setGraphic(null);
            LOGGER.log(Level.WARNING, "Could not load token image: " + item.getTokenPath(), e);
          }
        }
      }
    });

    tokenComboBox.setButtonCell(tokenComboBox.getCellFactory().call(null));

    addPlayerButton = Button.builder()
        .text("Add Player")
        .styleClass("primary")
        .onAction(e -> addPlayerButtonHandler())
        .disabled(true)
        .build();
  }

  private void addPlayerButtonHandler() {
    String name = playerNameField.getText().trim();
    TokenOption tokenOption = tokenComboBox.getValue();

    if (name.isEmpty()) {
      showAlert("Please enter a player name", AlertType.ERROR);
      return;
    }

    if (tokenOption == null) {
      showAlert("Please select a token", AlertType.ERROR);
      return;
    }

    if (playerNames.size() >= 5) {
      showAlert("Maximum number of players (5) reached", AlertType.ERROR);
      return;
    }

    try {
      controller.addPlayer(name, tokenOption.getTokenId());

      int tokenIdToRemove = tokenOption.getTokenId();
      availableTokens.removeIf(token -> token.getTokenId() == tokenIdToRemove);

      tokenComboBox.setItems(FXCollections.observableArrayList(availableTokens));

      playerNameField.clear();
      tokenComboBox.setValue(null);

      if (availableTokens.isEmpty() || playerNames.size() >= 5) {
        disableAddPlayerControls();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error adding player", e);
      showAlert("Error adding player: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void showAlert(String message, AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(alertType.toString());
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void disableAddPlayerControls() {
    playerNameField.setDisable(true);
    tokenComboBox.setDisable(true);
    addPlayerButton.setDisable(true);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    switch (event) {
      case PlayerAddedEvent playerAddedEvent -> {
        SlPlayer player = (SlPlayer) playerAddedEvent.getPlayer();
        String playerDisplay = player.getName() + " (Token " + player.getTokenId() + ")";
        playerNames.add(playerDisplay);

        if (playerNames.size() >= 5) {
          disableAddPlayerControls();
        }
      }
      case GameCreatedEvent ignored -> {
        playerNames.clear();

        availableTokens.clear();
        availableTokens.addAll(allTokens);

        playerNameField.setDisable(false);
        tokenComboBox.setDisable(false);
        addPlayerButton.setDisable(false);
        playerNameField.clear();
        tokenComboBox.setValue(null);
      }
      case GameResetEvent ignored -> {
        playerNames.clear();

        availableTokens.clear();
        availableTokens.addAll(allTokens);

        playerNameField.setDisable(false);
        tokenComboBox.setDisable(false);
        addPlayerButton.setDisable(false);
        playerNameField.clear();
        tokenComboBox.setValue(null);
      }
      case GameStartedEvent ignored -> {
        playerNameField.setDisable(true);
        tokenComboBox.setDisable(true);
        addPlayerButton.setDisable(true);
      }
      default -> {
      }
    }
  }
}