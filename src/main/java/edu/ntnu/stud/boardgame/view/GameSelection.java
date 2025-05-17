package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class GameSelection extends BorderPane implements BoardGameObserver {

  private final MainController controller;

  public GameSelection(MainController controller) {
    this.controller = controller;

    getStyleClass().add("game-selection-view");

    initializeUI();
  }

  private void initializeUI() {
    Label titleLabel = new LabelBuilder()
        .text("Select a Game")
        .styleClass("title")
        .build();

    HBox gameOptionsContainer = new HBox(30);
    gameOptionsContainer.setAlignment(Pos.CENTER);
    gameOptionsContainer.setPadding(new Insets(30));

    VBox ladderGameCard = createGameCard(
        "Snakes and Ladders",
        "/images/games/ladder_game.png",
        "Play the classic game of Snakes and Ladders! Roll the dice, climb the ladders, and avoid"
            + " the snakes.",
        event -> controller.selectGameType(BoardGameType.LADDER)
    );

    VBox monopolyGameCard = createGameCard(
        "Monopoly",
        "/images/games/monopoly_game.png",
        "Play a simplified version of Monopoly! Buy properties, collect rent, and become the "
            + "richest player.",
        event -> controller.selectGameType(BoardGameType.MONOPOLY)
    );

    gameOptionsContainer.getChildren().addAll(ladderGameCard, monopolyGameCard);

    setTop(createCenteredNode(titleLabel));
    setCenter(gameOptionsContainer);
  }

  private VBox createGameCard(String title, String imagePath, String description,
      javafx.event.EventHandler<javafx.event.ActionEvent> onSelect) {
    VBox card = new VBox(15);
    card.getStyleClass().add("game-card");
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(20));

    Label gameTitle = new LabelBuilder()
        .text(title)
        .styleClass("game-title")
        .build();

    ImageView imageView = new ImageView();
    try {
      InputStream is = getClass().getResourceAsStream(imagePath);
      if (is != null) {
        Image image = new Image(is);
        imageView.setImage(image);
        imageView.getStyleClass().add("game-image");
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
      } else {
        System.err.println("Could not find image at " + imagePath);
      }
    } catch (Exception e) {
      System.err.println("Failed to load image: " + imagePath);
      e.printStackTrace();
    }

    Label descriptionLabel = new LabelBuilder()
        .text(description)
        .wrapText(true)
        .textAlignment(TextAlignment.CENTER)
        .styleClass("game-description")
        .build();

    Button selectButton = new ButtonBuilder()
        .text("Select")
        .styleClass("action-button")
        .onClick(onSelect)
        .build();

    card.getChildren().addAll(gameTitle, imageView, descriptionLabel, selectButton);
    return card;
  }

  private BorderPane createCenteredNode(javafx.scene.Node node) {
    BorderPane pane = new BorderPane();
    pane.setCenter(node);
    pane.setPadding(new Insets(20, 0, 0, 0));
    return pane;
  }

  @Override
  public void onGameEvent(GameEvent event) {
  }
}