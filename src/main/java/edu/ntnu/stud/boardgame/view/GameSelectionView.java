package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

public class GameSelectionView extends BorderPane {

  private static final Logger LOGGER = Logger.getLogger(GameSelectionView.class.getName());
  private final GameController controller;

  public GameSelectionView(GameController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("GameController cannot be null.");
    }
    this.controller = controller;

    getStyleClass().add("game-selection-view");

    initializeUI();
  }

  private void initializeUI() {
    Label titleLabel = new LabelBuilder()
        .text("Choose Your Adventure!")
        .styleClass("title")
        .build();

    HBox gameOptionsContainer = new HBox(40);
    gameOptionsContainer.setAlignment(Pos.CENTER);

    VBox ladderGameCard = createGameCard(
        "Snakes & Ladders",
        "/images/games/ladder_game.png",
        "Classic fun! Roll the dice, climb ladders, and dodge those sneaky snakes to reach the finish line first.",
        event -> controller.selectGameType(BoardGameType.LADDER));

    VBox monopolyGameCard = createGameCard(
        "Monopoly Lite",
        "/images/games/monopoly_game.png",
        "Become a property tycoon! Buy, sell, and trade your way to riches in this fast-paced version of Monopoly.",
        event -> controller.selectGameType(BoardGameType.MONOPOLY));

    gameOptionsContainer.getChildren().addAll(ladderGameCard, monopolyGameCard);

    VBox mainLayout = new VBox(30, titleLabel, gameOptionsContainer);
    mainLayout.setAlignment(Pos.TOP_CENTER);

    setCenter(mainLayout);
  }

  private VBox createGameCard(String title, String imagePath, String description,
      javafx.event.EventHandler<javafx.event.ActionEvent> onSelect) {

    String cardTitle = (title == null || title.trim().isEmpty()) ? "Untitled Game" : title;
    if (title == null || title.trim().isEmpty()) {
      LOGGER.warning("Game card title is null or empty. Using default.");
    }

    String cardDescription = (description == null || description.trim().isEmpty()) ? "No description available."
        : description;
    if (description == null || description.trim().isEmpty()) {
      LOGGER.warning("Game card description is null or empty. Using default for title: " + cardTitle);
    }

    if (onSelect == null) {
      LOGGER.severe("onSelect event handler is null for game card: " + cardTitle + ". Button will be unresponsive.");

    }

    VBox card = new VBox();
    card.getStyleClass().add("game-card");
    card.setAlignment(Pos.CENTER);

    Label gameTitle = new LabelBuilder()
        .text(cardTitle)
        .styleClass("game-title")
        .build();

    ImageView imageView = new ImageView();
    if (imagePath != null && !imagePath.trim().isEmpty()) {
      try {
        InputStream is = getClass().getResourceAsStream(imagePath);
        if (is != null) {
          Image image = new Image(is);
          imageView.setImage(image);
          imageView.getStyleClass().add("game-image");
          imageView.setPreserveRatio(true);
        } else {
          LOGGER.warning("Could not find image at " + imagePath);
          Label errorLabel = new Label("Image not found");
          card.getChildren().add(errorLabel);
        }
      } catch (Exception e) {
        LOGGER.severe("Failed to load image: " + imagePath);
        e.printStackTrace();
        Label errorLabel = new Label("Error loading image");
        card.getChildren().add(errorLabel);
      }
    } else {
      LOGGER.warning("Image path is null or empty for game card: " + cardTitle);
      Label errorLabel = new Label("Image not provided");
      card.getChildren().add(errorLabel);
    }

    Label descriptionLabel = new LabelBuilder()
        .text(cardDescription)
        .wrapText(true)
        .styleClass("game-description")
        .build();
    descriptionLabel.setMinHeight(Label.USE_PREF_SIZE);

    Button selectButton = new ButtonBuilder()
        .text("Play Now!")
        .styleClass("action-button")
        .onClick(onSelect)
        .build();

    if (imageView.getImage() != null) {
      card.getChildren().addAll(gameTitle, imageView, descriptionLabel, selectButton);
    } else {
      card.getChildren().addAll(gameTitle, descriptionLabel, selectButton);
    }
    VBox.setVgrow(descriptionLabel, Priority.ALWAYS);

    return card;
  }
}