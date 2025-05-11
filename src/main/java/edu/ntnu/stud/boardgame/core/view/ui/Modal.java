package edu.ntnu.stud.boardgame.core.view.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A customized modal dialog component with a builder API. This class creates a customizable modal
 * dialog that can be shown over the main application window.
 *
 * <p>Example usage:</p>
 * <pre>
 * Modal confirmDialog = Modal.builder()
 *     .title("Confirm Action")
 *     .description("Are you sure you want to proceed?")
 *     .content(contentNode)
 *     .addCancelButton("Cancel", e -> modal.hide())
 *     .addConfirmButton("Confirm", e -> handleConfirmation())
 *     .build();
 *
 * confirmDialog.show();
 * </pre>
 */
public class Modal {

  private final Stage dialogStage;
  private final BorderPane modalRoot;
  private final VBox contentArea;
  private final HBox buttonArea;

  /**
   * Constructs a Modal with the specified configuration.
   *
   * @param title       the modal title
   * @param description the modal description text
   * @param owner       the owner stage for this modal
   * @param modality    the modality type
   * @param width       the modal width
   * @param height      the modal height
   */
  private Modal(String title, String description, Stage owner, Modality modality, double width,
      double height) {
    dialogStage = new Stage();
    dialogStage.initOwner(owner);
    dialogStage.initModality(modality);
    dialogStage.initStyle(StageStyle.TRANSPARENT);

    // Create the modal backdrop that will fill the entire screen
    StackPane backdropPane = new StackPane();
    backdropPane.getStyleClass().add("modal-backdrop");

    // Make backdrop fill the entire screen
    backdropPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
    backdropPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

    // Create a container to center the modal content
    StackPane modalContainer = new StackPane();
    modalContainer.setAlignment(Pos.CENTER);

    // Create the modal content container with your existing structure
    modalRoot = new BorderPane();
    modalRoot.getStyleClass().add("modal");
    modalRoot.setMaxWidth(width);
    modalRoot.setMaxHeight(height);

    // Create header section with title and close button
    BorderPane headerPane = new BorderPane();
    headerPane.getStyleClass().add("modal-header");

    VBox titleBox = new VBox();
    titleBox.setSpacing(2);

    // Add title if provided
    if (title != null && !title.isEmpty()) {
      Label titleLabel = Label.builder()
          .text(title)
          .styleClass("modal-title")
          .fontSize("xl")
          .fontWeight(javafx.scene.text.FontWeight.BOLD)
          .build();
      titleBox.getChildren().add(titleLabel);
    }

    // Add description if provided
    if (description != null && !description.isEmpty()) {
      Label descLabel = Label.builder()
          .text(description)
          .styleClass("modal-description")
          .fontSize("sm")
          .build();
      titleBox.getChildren().add(descLabel);
    }

    headerPane.setLeft(titleBox);

    // Create close button
    edu.ntnu.stud.boardgame.core.view.ui.Button closeButton =
        edu.ntnu.stud.boardgame.core.view.ui.Button.builder()
            .text("✕")
            .styleClass("modal-close-button")
            .onAction(e -> dialogStage.close())
            .build();
    headerPane.setRight(closeButton);

    // Create content area
    contentArea = new VBox();
    contentArea.getStyleClass().add("modal-content");
    contentArea.setSpacing(16);

    // Create footer with buttons
    buttonArea = new HBox();
    buttonArea.getStyleClass().add("modal-footer");
    buttonArea.setSpacing(8);
    buttonArea.setAlignment(Pos.CENTER_RIGHT);

    // Add all sections to the modal
    modalRoot.setTop(headerPane);
    modalRoot.setCenter(contentArea);
    modalRoot.setBottom(buttonArea);

    // Add modal to the centering container
    modalContainer.getChildren().add(modalRoot);

    // Add both backdrop and centered modal container to the root
    backdropPane.getChildren().add(modalContainer);

    // Create scene with the full-screen backdrop
    Scene dialogScene = new Scene(backdropPane);
    dialogScene.setFill(Color.TRANSPARENT);

    // Load CSS
    dialogScene.getStylesheets().addAll(
        owner.getScene().getStylesheets()
    );

    dialogStage.setScene(dialogScene);

    // Set the stage to fill the screen
    dialogStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
    dialogStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

    // Position the dialog stage to cover the entire screen
    dialogStage.setOnShown(e -> {
      dialogStage.setX(Screen.getPrimary().getVisualBounds().getMinX());
      dialogStage.setY(Screen.getPrimary().getVisualBounds().getMinY());
    });
  }

  /**
   * Shows the modal dialog.
   */
  public void show() {
    dialogStage.show();
  }

  /**
   * Shows the modal dialog and waits for it to be closed.
   */
  public void showAndWait() {
    dialogStage.showAndWait();
  }

  /**
   * Hides the modal dialog.
   */
  public void hide() {
    dialogStage.hide();
  }

  /**
   * Adds a node to the content area of the modal.
   *
   * @param node the node to add
   */
  public void addContent(javafx.scene.Node node) {
    contentArea.getChildren().add(node);
  }

  /**
   * Adds a button to the footer area of the modal.
   *
   * @param button the button to add
   */
  public void addButton(edu.ntnu.stud.boardgame.core.view.ui.Button button) {
    buttonArea.getChildren().add(button);
  }

  /**
   * Creates a new modal builder instance to configure a modal.
   *
   * @return a new builder instance for creating a Modal
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for creating customized Modal instances. This pattern allows for more readable
   * and maintainable code when configuring modals with multiple properties.
   */
  public static class Builder {

    private String title;
    private String description;
    private Stage owner;
    private Modality modality = Modality.APPLICATION_MODAL;
    private double width = 500;
    private double height = 300;
    private javafx.scene.Node content;
    private final java.util.List<edu.ntnu.stud.boardgame.core.view.ui.Button> buttons =
        new java.util.ArrayList<>();

    /**
     * Creates a new builder instance with default settings.
     */
    private Builder() {
    }

    /**
     * Sets the title of the modal.
     *
     * @param title the modal title
     * @return this builder for method chaining
     */
    public Builder title(String title) {
      this.title = title;
      return this;
    }

    /**
     * Sets the description text of the modal.
     *
     * @param description the modal description
     * @return this builder for method chaining
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Sets the owner stage of the modal.
     *
     * @param owner the owner stage
     * @return this builder for method chaining
     */
    public Builder owner(Stage owner) {
      this.owner = owner;
      return this;
    }

    /**
     * Sets the modality of the modal.
     *
     * @param modality the modality type
     * @return this builder for method chaining
     */
    public Builder modality(Modality modality) {
      this.modality = modality;
      return this;
    }

    /**
     * Sets the width of the modal.
     *
     * @param width the modal width
     * @return this builder for method chaining
     */
    public Builder width(double width) {
      this.width = width;
      return this;
    }

    /**
     * Sets the height of the modal.
     *
     * @param height the modal height
     * @return this builder for method chaining
     */
    public Builder height(double height) {
      this.height = height;
      return this;
    }

    /**
     * Sets the content node of the modal.
     *
     * @param content the content node
     * @return this builder for method chaining
     */
    public Builder content(javafx.scene.Node content) {
      this.content = content;
      return this;
    }

    /**
     * Adds a button to the modal.
     *
     * @param button the button to add
     * @return this builder for method chaining
     */
    public Builder addButton(edu.ntnu.stud.boardgame.core.view.ui.Button button) {
      this.buttons.add(button);
      return this;
    }

    /**
     * Adds a primary action button to the modal.
     *
     * @param text    the button text
     * @param handler the event handler
     * @return this builder for method chaining
     */
    public Builder addPrimaryButton(String text, EventHandler<ActionEvent> handler) {
      edu.ntnu.stud.boardgame.core.view.ui.Button button =
          edu.ntnu.stud.boardgame.core.view.ui.Button.builder()
              .text(text)
              .styleClass("primary")
              .onAction(handler)
              .build();
      this.buttons.add(button);
      return this;
    }

    /**
     * Adds a secondary action button to the modal.
     *
     * @param text    the button text
     * @param handler the event handler
     * @return this builder for method chaining
     */
    public Builder addSecondaryButton(String text, EventHandler<ActionEvent> handler) {
      edu.ntnu.stud.boardgame.core.view.ui.Button button =
          edu.ntnu.stud.boardgame.core.view.ui.Button.builder()
              .text(text)
              .styleClass("secondary")
              .onAction(handler)
              .build();
      this.buttons.add(button);
      return this;
    }

    /**
     * Adds a destructive action button to the modal.
     *
     * @param text    the button text
     * @param handler the event handler
     * @return this builder for method chaining
     */
    public Builder addDestructiveButton(String text, EventHandler<ActionEvent> handler) {
      edu.ntnu.stud.boardgame.core.view.ui.Button button =
          edu.ntnu.stud.boardgame.core.view.ui.Button.builder()
              .text(text)
              .styleClass("destructive")
              .onAction(handler)
              .build();
      this.buttons.add(button);
      return this;
    }

    /**
     * Adds a cancel button to the modal.
     *
     * @param text    the button text
     * @param handler the event handler
     * @return this builder for method chaining
     */
    public Builder addCancelButton(String text, EventHandler<ActionEvent> handler) {
      edu.ntnu.stud.boardgame.core.view.ui.Button button =
          edu.ntnu.stud.boardgame.core.view.ui.Button.builder()
              .text(text)
              .styleClass("ghost")
              .onAction(e -> {
                if (handler != null) {
                  handler.handle(e);
                }
              })
              .build();
      this.buttons.add(button);
      return this;
    }

    /**
     * Builds a new Modal instance with the configured properties.
     *
     * @return a new Modal instance
     */
    public Modal build() {
      if (owner == null) {
        throw new IllegalStateException("Modal must have an owner stage");
      }

      Modal modal = new Modal(title, description, owner, modality, width, height);

      if (content != null) {
        modal.addContent(content);
      }

      for (edu.ntnu.stud.boardgame.core.view.ui.Button button : buttons) {
        modal.addButton(button);
      }

      return modal;
    }
  }
}