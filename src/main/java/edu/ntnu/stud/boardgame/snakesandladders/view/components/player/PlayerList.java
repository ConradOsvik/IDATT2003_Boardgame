package edu.ntnu.stud.boardgame.snakesandladders.view.components.player;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PlayerList extends GameComponent<VBox> {

  private static final Logger LOGGER = Logger.getLogger(PlayerList.class.getName());
  private final ObservableList<SlPlayer> players = FXCollections.observableArrayList();
  private final ListView<SlPlayer> playerListView;

  public PlayerList(SlGameController controller, ErrorDialog errorDialog) {
    super(controller, new VBox(5), errorDialog);

    // Create a ListView with custom cells
    playerListView = new ListView<>(players);
    playerListView.setCellFactory(this::createPlayerCell);
    playerListView.setPrefHeight(200);
    playerListView.setMinHeight(200);
    playerListView.setMinWidth(200);

    VBox.setVgrow(playerListView, Priority.ALWAYS);

    // Add empty state message
    Label emptyLabel = new Label("No players added yet");
    emptyLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #888;");
    playerListView.setPlaceholder(emptyLabel);

    getNode().getChildren().add(playerListView);
    getNode().setPadding(new Insets(5));
    getNode().setMinWidth(220);
  }

  private ListCell<SlPlayer> createPlayerCell(ListView<SlPlayer> listView) {
    return new ListCell<>() {
      private final HBox container = new HBox(10);
      private final ImageView tokenImage = new ImageView();
      private final Label nameLabel = new Label();

      {
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(5));
        tokenImage.setFitWidth(24);
        tokenImage.setFitHeight(24);
        tokenImage.setPreserveRatio(true);
        container.getChildren().addAll(tokenImage, nameLabel);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
      }

      @Override
      protected void updateItem(SlPlayer player, boolean empty) {
        super.updateItem(player, empty);

        if (empty || player == null) {
          setGraphic(null);
        } else {
          nameLabel.setText(player.getName());

          // Load token image
          try {
            String path = "/pieces/snakesandladders/" + player.getTokenId() + ".png";
            URL resource = getClass().getResource(path);
            if (resource != null) {
              tokenImage.setImage(new Image(resource.toExternalForm(), 24, 24, true, true));
            }
          } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not load token image", e);
            // Continue without setting image - still display player name
          }

          setGraphic(container);
        }
      }
    };
  }

  @Override
  public void init() {
    players.clear();
  }

  @Override
  public void onGameEvent(GameEvent event) {
    if (event instanceof PlayerAddedEvent playerAddedEvent) {
      // Add new player to the list
      SlPlayer player = (SlPlayer) playerAddedEvent.getPlayer();
      if (!players.contains(player)) {
        players.add(player);
      }

    } else if (event instanceof GameResetEvent || event instanceof GameCreatedEvent) {
      // Reset player list
      players.clear();
    }
  }
}