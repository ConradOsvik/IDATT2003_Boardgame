package edu.ntnu.stud.boardgame.snakesandladders.view.components;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameEndedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameRestartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerWonEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Scoreboard extends GameComponent<VBox> {

  private final VBox playerListContainer;
  private final Map<SlPlayer, HBox> playerRows = new HashMap<>();

  public Scoreboard(SlGameController controller, ErrorDialog errorDialog) {
    super(controller, new VBox(10), errorDialog);

    // Set up scoreboard header
    Label titleLabel = Label.builder().text("Scoreboard").fontSize("xl")
        .fontWeight(javafx.scene.text.FontWeight.BOLD).build();

    // Create scrollable container for player rows
    playerListContainer = new VBox(5);
    playerListContainer.setMinHeight(100);

    ScrollPane scrollPane = new ScrollPane(playerListContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(200);
    scrollPane.setMinHeight(100);
    scrollPane.setPannable(true);
    scrollPane.getStyleClass().add("scoreboard-scroll");

    // Add components to main container
    getNode().setPadding(new Insets(10));
    getNode().getChildren().addAll(titleLabel, scrollPane);

    // Make the scoreboard stretch vertically
    VBox.setVgrow(scrollPane, Priority.ALWAYS);
  }

  private void removePlayer(SlPlayer player) {
    HBox playerRow = playerRows.remove(player);
    if (playerRow != null) {
      playerListContainer.getChildren().remove(playerRow);
      updateScoreboard();
    }
  }

  private void updateScoreboard() {
    // Sort players by position (tile ID)
    List<SlPlayer> sortedPlayers = new ArrayList<>(playerRows.keySet());
    sortedPlayers.sort(Comparator.comparing(
        player -> player.getCurrentTile() != null ? player.getCurrentTile().getTileId() : 0,
        Comparator.reverseOrder()));

    // Re-order the player rows
    playerListContainer.getChildren().clear();

    int rank = 1;
    for (SlPlayer player : sortedPlayers) {
      HBox playerRow = playerRows.get(player);
      if (playerRow != null) {
        // Update rank label
        Label rankLabel = (Label) playerRow.getChildren().get(0);
        rankLabel.setText("#" + rank);

        // Update position label
        Label positionLabel = (Label) playerRow.getChildren().get(3);
        int position = player.getCurrentTile() != null ? player.getCurrentTile().getTileId() : 0;
        positionLabel.setText("Tile " + position);

        // Add row to container
        playerListContainer.getChildren().add(playerRow);
        rank++;
      }
    }
  }

  @Override
  public void init() {
    clearScoreboard();
  }

  private void clearScoreboard() {
    playerRows.clear();
    playerListContainer.getChildren().clear();
  }

  @Override
  public void onGameEvent(GameEvent event) {
    if (event instanceof PlayerAddedEvent playerAddedEvent) {
      // Add new player to scoreboard
      SlPlayer player = (SlPlayer) playerAddedEvent.getPlayer();
      addPlayer(player);
    } else if (event instanceof PlayerMovedEvent playerMovedEvent) {
      // Update player positions
      updateScoreboard();
    } else if (event instanceof PlayerWonEvent playerWonEvent) {
      // Highlight the winner
      SlPlayer winner = (SlPlayer) playerWonEvent.getWinner();
      HBox winnerRow = playerRows.get(winner);
      if (winnerRow != null) {
        winnerRow.getStyleClass().add("winner-row");
      }
    } else if (event instanceof GameResetEvent || event instanceof GameCreatedEvent) {
      // Reset the scoreboard
      clearScoreboard();
    } else if (event instanceof GameRestartedEvent) {
      // Keep players but reset positions
      updateScoreboard();
    } else if (event instanceof GameEndedEvent) {
      // Game over, update final positions
      updateScoreboard();
    }
  }

  private void addPlayer(SlPlayer player) {
    if (playerRows.containsKey(player)) {
      return;
    }

    HBox playerRow = new HBox(10);
    playerRow.setAlignment(Pos.CENTER_LEFT);
    playerRow.setPadding(new Insets(5));

    // Rank label (#1, #2, etc.)
    Label rankLabel = Label.builder().text("#" + (playerRows.size() + 1))
        .fontWeight(javafx.scene.text.FontWeight.BOLD).build();

    // Player token
    ImageView tokenImage = new ImageView();
    tokenImage.setFitWidth(24);
    tokenImage.setFitHeight(24);
    tokenImage.setPreserveRatio(true);

    try {
      String path = "/pieces/snakesandladders/" + player.getTokenId() + ".png";
      URL resource = getClass().getResource(path);
      if (resource != null) {
        tokenImage.setImage(new Image(resource.toExternalForm()));
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Could not load token image", e);
    }

    // Player name
    Label nameLabel = Label.builder().text(player.getName()).build();

    // Player position
    int position = player.getCurrentTile() != null ? player.getCurrentTile().getTileId() : 0;
    Label positionLabel = Label.builder().text("Tile " + position).build();

    playerRow.getChildren().addAll(rankLabel, tokenImage, nameLabel, positionLabel);
    HBox.setHgrow(nameLabel, Priority.ALWAYS);

    // Add to player rows map and container
    playerRows.put(player, playerRow);
    playerListContainer.getChildren().add(playerRow);

    // Update scoreboard ordering
    updateScoreboard();
  }
}