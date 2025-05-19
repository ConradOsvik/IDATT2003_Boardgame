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

public class MonopolyPlayerScoreboard extends VBox {

  private final Label titleLabel;
  private final VBox playersContainer;
  private final Map<Player, Label> moneyLabels = new HashMap<>();
  private final Map<Player, HBox> playerRows = new HashMap<>();
  private final MonopolyController controller;

  public MonopolyPlayerScoreboard(MonopolyController controller) {
    this.controller = controller;

    setPadding(new Insets(15));
    setSpacing(10);
    getStyleClass().add("card");

    titleLabel = new LabelBuilder()
        .text("Players")
        .styleClass("text-h3")
        .build();

    playersContainer = new VBox(5);
    playersContainer.getStyleClass().add("list-container");

    getChildren().addAll(titleLabel, playersContainer);
  }

  public void updatePlayers(List<Player> players) {
    playersContainer.getChildren().clear();
    playerRows.clear();
    moneyLabels.clear();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      HBox playerRow = createPlayerRow(player, i);
      playerRows.put(player, playerRow);
      playersContainer.getChildren().add(playerRow);
    }
  }

  public void updatePlayerMoney(Player player) {
    Label moneyLabel = moneyLabels.get(player);
    if (moneyLabel != null) {
      moneyLabel.setText("$" + controller.getPlayerMoney(player));

      HBox playerRow = playerRows.get(player);
      if (playerRow != null && playerRow.getChildren().size() >= 2) {
        Label nameLabel = (Label) playerRow.getChildren().get(1);
        Circle indicator = (Circle) playerRow.getChildren().get(0);

        if (controller.isPlayerBankrupt(player)) {
          nameLabel.setText(player.getName() + " (BANKRUPT)");
          nameLabel.setTextFill(Color.RED);
          indicator.setOpacity(0.5);
        }
      }
    }
  }

  public void highlightCurrentPlayer(Player currentPlayer) {
    playerRows.values().forEach(row -> row.getStyleClass().remove("list-item-highlighted"));

    if (currentPlayer != null) {
      HBox playerRow = playerRows.get(currentPlayer);
      if (playerRow != null) {
        playerRow.getStyleClass().add("list-item-highlighted");
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

    switch (playerIndex) {
      case 0:
        playerIndicator.setFill(Color.RED);
        break;
      case 1:
        playerIndicator.setFill(Color.BLUE);
        break;
      case 2:
        playerIndicator.setFill(Color.GREEN);
        break;
      case 3:
        playerIndicator.setFill(Color.YELLOW);
        break;
      default:
        playerIndicator.setFill(Color.PURPLE);
        break;
    }

    Label nameLabel = new LabelBuilder()
        .text(player.getName())
        .styleClass("text-body-bold")
        .build();

    Label moneyLabel = new LabelBuilder()
        .text("$" + controller.getPlayerMoney(player))
        .styleClass("text-body")
        .build();

    moneyLabels.put(player, moneyLabel);

    if (controller.isPlayerBankrupt(player)) {
      nameLabel.setText(player.getName() + " (BANKRUPT)");
      nameLabel.setTextFill(Color.RED);
      playerIndicator.setOpacity(0.5);
    }

    row.getChildren().addAll(playerIndicator, nameLabel, moneyLabel);
    HBox.setHgrow(nameLabel, Priority.ALWAYS);

    return row;
  }
}