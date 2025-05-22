package edu.ntnu.stud.boardgame.view.components.monopoly;

import edu.ntnu.stud.boardgame.controller.MonopolyController;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A scoreboard component for the Monopoly game that displays player information. Shows each
 * player's current money, bankruptcy status, and highlights the active player. Extends {@link VBox}
 * to arrange player entries vertically with consistent styling.
 *
 * @see MonopolyController
 * @see VBox
 */
public class MonopolyPlayerScoreboard extends VBox {

  private final Label titleLabel;
  private final VBox playersContainer;
  private final Map<Player, Label> moneyLabels = new HashMap<>();
  private final Map<Player, HBox> playerRows = new HashMap<>();
  private final Map<Player, Circle> playerIndicators = new HashMap<>();
  private final MonopolyController controller;

  /**
   * Konstruktør for resultattavlen.
   *
   * @param controller {@link MonopolyController}-instansen som styrer spillogikken
   */
  public MonopolyPlayerScoreboard(MonopolyController controller) {
    this.controller = controller;

    setPadding(new Insets(15));
    setSpacing(10);
    getStyleClass().add("card");

    titleLabel = new LabelBuilder().text("Players").styleClass("text-h3").build();

    playersContainer = new VBox(5);
    playersContainer.getStyleClass().add("list-container");

    getChildren().addAll(titleLabel, playersContainer);
  }

  /**
   * Oppdaterer spillerlisten i resultattavlen.
   *
   * @param players listen over {@link Player}-objekter som skal vises
   * @see Player
   */
  public void updatePlayers(List<Player> players) {
    playersContainer.getChildren().clear();
    playerRows.clear();
    moneyLabels.clear();
    playerIndicators.clear();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      HBox playerRow = createPlayerRow(player, i);
      playerRows.put(player, playerRow);
      playersContainer.getChildren().add(playerRow);
    }
  }

  /**
   * Oppdaterer pengebeløpet og tilstanden for en spiller. Håndterer også visuelle endringer for
   * konkurs-status.
   *
   * @param player {@link Player}-objektet som skal oppdateres
   */
  public void updatePlayerMoney(Player player) {
    Label moneyLabel = moneyLabels.get(player);
    if (moneyLabel != null) {
      moneyLabel.setText("$" + controller.getPlayerMoney(player));

      HBox playerRow = playerRows.get(player);
      Circle indicator = playerIndicators.get(player);

      if (playerRow != null && playerRow.getChildren().size() >= 2 && indicator != null) {
        Label nameLabel = (Label) playerRow.getChildren().get(1);

        if (controller.isPlayerBankrupt(player)) {
          nameLabel.setText(player.getName() + " (BANKRUPT)");
          nameLabel.setTextFill(Color.RED);
          indicator.setOpacity(0.5);
          moneyLabel.setTextFill(Color.RED);
          playerRow.setOpacity(0.7);
          playerRow.getStyleClass().add("bankrupt-player");
        } else {
          nameLabel.setText(player.getName());
          nameLabel.setTextFill(Color.BLACK);
          indicator.setOpacity(1.0);
          moneyLabel.setTextFill(Color.BLACK);
          playerRow.setOpacity(1.0);
          playerRow.getStyleClass().remove("bankrupt-player");
        }
      }
    }
  }

  /**
   * Fremhever den aktive spilleren i resultattavlen.
   *
   * @param currentPlayer {@link Player}-objektet for aktiv spiller, eller <code>null</code>
   */
  public void highlightCurrentPlayer(Player currentPlayer) {
    playerRows.forEach(
        (player, row) -> {
          row.getStyleClass().remove("current-player");
        });

    if (currentPlayer != null) {
      HBox playerRow = playerRows.get(currentPlayer);
      if (playerRow != null) {
        if (!controller.isPlayerBankrupt(currentPlayer)) {
          playerRow.getStyleClass().add("current-player");
        }
      }
    }
  }

  private HBox createPlayerRow(Player player, int playerIndex) {
    HBox row = new HBox(10);
    row.setAlignment(Pos.CENTER_LEFT);
    row.setPadding(new Insets(5));
    row.getStyleClass().add("player-row");

    Circle playerIndicator = new Circle(8);
    playerIndicator.getStyleClass().add("player-indicator");
    playerIndicator.setFill(player.getPiece().getColor());

    Label nameLabel =
        new LabelBuilder().text(player.getName()).styleClass("text-body-bold").build();

    Label moneyLabel =
        new LabelBuilder()
            .text("$" + controller.getPlayerMoney(player))
            .styleClass("text-body")
            .build();

    moneyLabels.put(player, moneyLabel);
    playerIndicators.put(player, playerIndicator);

    if (controller.isPlayerBankrupt(player)) {
      nameLabel.setText(player.getName() + " (BANKRUPT)");
      nameLabel.setTextFill(Color.RED);
      playerIndicator.setOpacity(0.5);
      moneyLabel.setTextFill(Color.RED);
      row.setOpacity(0.7);
      row.getStyleClass().add("bankrupt-player");
    }

    row.getChildren().addAll(playerIndicator, nameLabel, moneyLabel);
    HBox.setHgrow(nameLabel, Priority.ALWAYS);

    return row;
  }
}
