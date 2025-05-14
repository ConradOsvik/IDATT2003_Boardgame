package edu.ntnu.stud.boardgame.snakesandladders.view.components.player;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameStartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PlayerRegistration extends GameComponent<VBox> {

  private final TextField playerNameField;
  private final ComboBox<Integer> tokenSelector;
  private final Button addButton;

  // Keep track of available tokens (1-5)
  private final Set<Integer> availableTokens = new LinkedHashSet<>();
  private final int MAX_PLAYERS = 5;

  public PlayerRegistration(SlGameController controller, ErrorDialog errorDialog) {
    super(controller, new VBox(10), errorDialog);

    // Initialize available tokens
    for (int i = 1; i <= MAX_PLAYERS; i++) {
      availableTokens.add(i);
    }

    // Create player name field
    playerNameField = new TextField();
    playerNameField.setPromptText("Enter player name");

    // Create token selector
    tokenSelector = new ComboBox<>();
    tokenSelector.setPromptText("Select token");
    tokenSelector.setCellFactory(this::createTokenCell);
    tokenSelector.setButtonCell(createTokenCell(null));
    tokenSelector.setVisibleRowCount(Math.min(MAX_PLAYERS, 5));

    // Create add button
    addButton = new Button("Add Player");
    addButton.setMaxWidth(Double.MAX_VALUE);
    addButton.setOnAction(e -> addPlayer());

    updateTokenOptions();

    // Create layout
    GridPane inputGrid = new GridPane();
    inputGrid.setHgap(10);
    inputGrid.setVgap(10);
    inputGrid.setPadding(new Insets(5));

    // Add form elements to grid
    inputGrid.add(new Label("Name:"), 0, 0);
    inputGrid.add(playerNameField, 1, 0);
    inputGrid.add(new Label("Token:"), 0, 1);
    inputGrid.add(tokenSelector, 1, 1);

    GridPane.setHgrow(playerNameField, Priority.ALWAYS);
    GridPane.setHgrow(tokenSelector, Priority.ALWAYS);

    // Add elements to container
    getNode().getChildren().addAll(inputGrid, addButton);
    getNode().setPadding(new Insets(5));
  }

  /**
   * Creates a cell for the token selector with an image
   */
  private ListCell<Integer> createTokenCell(ListView<Integer> listView) {
    return new ListCell<>() {
      private final ImageView imageView = new ImageView();

      {
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);
        imageView.setPreserveRatio(true);
      }

      @Override
      protected void updateItem(Integer tokenId, boolean empty) {
        super.updateItem(tokenId, empty);

        if (empty || tokenId == null) {
          setText(null);
          setGraphic(null);
        } else {
          try {
            // Load token image
            String path = "/pieces/snakesandladders/" + tokenId + ".png";
            URL resource = getClass().getResource(path);
            if (resource != null) {
              Image image = new Image(resource.toExternalForm(), 24, 24, true, true);
              imageView.setImage(image);

              // Create a horizontal box with image and text
              HBox box = new HBox(5);
              box.setAlignment(Pos.CENTER_LEFT);
              box.getChildren().addAll(imageView, new Label("Token " + tokenId));

              setGraphic(box);
              setText(null);
            } else {
              setText("Token " + tokenId);
              setGraphic(null);
            }
          } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not load token image", e);
            setText("Token " + tokenId);
            setGraphic(null);
          }
        }
      }
    };
  }

  private void addPlayer() {
    String name = playerNameField.getText().trim();
    Integer tokenId = tokenSelector.getValue();

    if (name.isEmpty()) {
      ErrorDialog.getInstance().showError("Input Error", "Please enter a player name");
      return;
    }

    if (tokenId == null) {
      ErrorDialog.getInstance().showError("Input Error", "Please select a token");
      return;
    }

    // Request controller to add the player
    boolean success = ((SlGameController) controller).addPlayer(name, tokenId);

    if (success) {
      // Remove token from available options
      availableTokens.remove(tokenId);
      updateTokenOptions();

      // Clear inputs
      playerNameField.clear();
      tokenSelector.setValue(null);
    }
  }

  private void updateTokenOptions() {
    tokenSelector.getItems().clear();
    tokenSelector.getItems().addAll(availableTokens);

    tokenSelector.setVisibleRowCount(Math.min(availableTokens.size(), 5));

    if (availableTokens.isEmpty()) {
      tokenSelector.setDisable(true);
      addButton.setDisable(true);
    } else {
      tokenSelector.setDisable(false);
      addButton.setDisable(false);
    }
  }

  @Override
  public void onGameEvent(GameEvent event) {
    if (event instanceof PlayerAddedEvent playerAddedEvent) {
      // Remove token from available options when player is added
      SlPlayer player = (SlPlayer) playerAddedEvent.getPlayer();
      availableTokens.remove(player.getTokenId());
      updateTokenOptions();

    } else if (event instanceof GameStartedEvent) {
      // Disable controls when game starts
      playerNameField.setDisable(true);
      tokenSelector.setDisable(true);
      addButton.setDisable(true);

    } else if (event instanceof GameResetEvent || event instanceof GameCreatedEvent) {
      // Reset controls when game is reset
      init();
    }
  }

  @Override
  public void init() {
    // Reset token options
    availableTokens.clear();
    for (int i = 1; i <= MAX_PLAYERS; i++) {
      availableTokens.add(i);
    }
    updateTokenOptions();

    // Clear inputs
    playerNameField.clear();
    tokenSelector.setValue(null);

    // Enable controls
    addButton.setDisable(false);
    playerNameField.setDisable(false);
  }
}