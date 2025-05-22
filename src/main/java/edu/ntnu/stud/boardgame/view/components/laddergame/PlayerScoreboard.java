package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * A JavaFX component that displays the scoreboard for the Snakes and Ladders
 * game. Shows player
 * information, their current positions, and highlights the active player.
 * Extends {@link VBox} to
 * provide a vertical layout for player entries.
 *
 * @see Player
 * @see VBox
 */
public class PlayerScoreboard extends VBox {

  private final VBox playersContainer;
  private final Map<Player, HBox> playerRows = new HashMap<>();

  /**
   * Constructs a new PlayerScoreboard with default styling and layout.
   * Initializes the title and players container with appropriate CSS classes.
   */
  public PlayerScoreboard() {
    setPadding(new Insets(15));
    setSpacing(10);
    getStyleClass().add("card");

    Label titleLabel = new LabelBuilder().text("Players").styleClass("text-h3").build();

    playersContainer = new VBox(5);
    playersContainer.getStyleClass().add("list-container");

    getChildren().addAll(titleLabel, playersContainer);
  }

  /**
   * Updates the scoreboard with a new list of players.
   * Clears existing entries and creates new rows for each player.
   *
   * @param players the list of {@link Player}s to display
   */
  public void updatePlayers(List<Player> players) {
    playersContainer.getChildren().clear();
    playerRows.clear();

    for (Player player : players) {
      HBox playerRow = createPlayerRow(player);
      playerRows.put(player, playerRow);
      playersContainer.getChildren().add(playerRow);
    }
  }

  /**
   * Highlights the currently active player in the scoreboard.
   * Removes highlight from other players and applies it to the current player.
   *
   * @param currentPlayer the {@link Player} whose turn it is, or null if no
   *                      player is active
   */
  public void highlightCurrentPlayer(Player currentPlayer) {
    playerRows.values().forEach(row -> row.getStyleClass().remove("current-player"));

    if (currentPlayer != null) {
      HBox playerRow = playerRows.get(currentPlayer);
      if (playerRow != null) {
        playerRow.getStyleClass().add("current-player");
      }
    }
  }

  /**
   * Creates a row in the scoreboard for a single player.
   * Includes player indicator, name, and position information.
   *
   * @param player the {@link Player} to create a row for
   * @return an {@link HBox} containing the player's information
   */
  private HBox createPlayerRow(Player player) {
    HBox row = new HBox(10);
    row.setAlignment(Pos.CENTER_LEFT);
    row.setPadding(new Insets(5));
    row.getStyleClass().add("player-row");

    Circle playerIndicator = new Circle(8);
    playerIndicator.getStyleClass().add("player-indicator");
    playerIndicator.getStyleClass().add(player.getPiece().name().toLowerCase() + "-piece");

    Label nameLabel = new LabelBuilder().text(player.getName()).styleClass("text-body-bold")
        .build();

    Label positionLabel = new LabelBuilder().text("Position: Start").styleClass("text-body")
        .build();

    row.getChildren().addAll(playerIndicator, nameLabel, positionLabel);
    HBox.setHgrow(nameLabel, Priority.ALWAYS);

    return row;
  }
}
