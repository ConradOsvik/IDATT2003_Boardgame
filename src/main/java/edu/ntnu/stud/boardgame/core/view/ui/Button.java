package edu.ntnu.stud.boardgame.core.view.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;

/**
 * A customized JavaFX Button with enhanced styling options and a builder API. This class extends
 * the standard JavaFX Button to provide additional functionality and easier configuration through a
 * builder pattern.
 *
 * <p>Example usage:</p>
 * <pre>
 * Button saveButton = Button.builder()
 *     .text("Save")
 *     .styleClass("primary")
 *     .width(120)
 *     .onAction(e -> saveData())
 *     .build();
 * </pre>
 */
public class Button extends javafx.scene.control.Button {

  /**
   * Constructs a Button with the specified text.
   *
   * @param text the text to display on the button
   */
  public Button(String text) {
    super(text);
    initialize();
  }

  /**
   * Constructs a Button with no text.
   */
  public Button() {
    super();
    initialize();
  }

  /**
   * Initializes the button with default settings. Sets the cursor to a hand pointer when hovering
   * over the button.
   */
  private void initialize() {
    setCursor(Cursor.HAND);
  }

  /**
   * Creates a new button builder instance to configure a button.
   *
   * @return a new builder instance for creating a Button
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for creating customized Button instances. This pattern allows for more readable
   * and maintainable code when configuring buttons with multiple properties.
   */
  public static class Builder {

    private String text;
    private String id;
    private String styleClass;
    private String style;
    private double width;
    private double height;
    private boolean disabled;
    private boolean rounded;
    private boolean outline;
    private String size;
    private EventHandler<ActionEvent> onAction;

    /**
     * Creates a new builder instance with default settings.
     */
    private Builder() {
      this.text = "";
      this.disabled = false;
      this.width = -1;
      this.height = -1;
      this.rounded = false;
      this.outline = false;
      this.size = "md";
    }

    /**
     * Sets the text to display on the button.
     *
     * @param text the text to display
     * @return this builder for method chaining
     */
    public Builder text(String text) {
      this.text = text;
      return this;
    }

    /**
     * Sets the CSS ID of the button.
     *
     * @param id the CSS ID
     * @return this builder for method chaining
     */
    public Builder id(String id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a CSS style class to the button.
     *
     * @param styleClass the CSS style class to add
     * @return this builder for method chaining
     */
    public Builder styleClass(String styleClass) {
      this.styleClass = styleClass;
      return this;
    }

    /**
     * Sets inline CSS style for the button.
     *
     * @param style the CSS style string
     * @return this builder for method chaining
     */
    public Builder style(String style) {
      this.style = style;
      return this;
    }

    /**
     * Sets the preferred width of the button.
     *
     * @param width the preferred width in pixels
     * @return this builder for method chaining
     */
    public Builder width(double width) {
      this.width = width;
      return this;
    }

    /**
     * Sets the preferred height of the button.
     *
     * @param height the preferred height in pixels
     * @return this builder for method chaining
     */
    public Builder height(double height) {
      this.height = height;
      return this;
    }

    /**
     * Sets whether the button is disabled.
     *
     * @param disabled true to disable the button, false to enable it
     * @return this builder for method chaining
     */
    public Builder disabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    /**
     * Sets whether the button should have rounded corners.
     *
     * @param rounded true for rounded corners, false for square corners
     * @return this builder for method chaining
     */
    public Builder rounded(boolean rounded) {
      this.rounded = rounded;
      return this;
    }

    /**
     * Sets whether the button should have an outline style.
     *
     * @param outline true for outline style, false for filled style
     * @return this builder for method chaining
     */
    public Builder outline(boolean outline) {
      this.outline = outline;
      return this;
    }

    /**
     * Sets the size of the button.
     *
     * @param size the size identifier (e.g., "sm", "md", "lg")
     * @return this builder for method chaining
     */
    public Builder size(String size) {
      this.size = size;
      return this;
    }

    /**
     * Sets the action handler to be called when the button is clicked.
     *
     * @param handler the event handler for button clicks
     * @return this builder for method chaining
     */
    public Builder onAction(EventHandler<ActionEvent> handler) {
      this.onAction = handler;
      return this;
    }

    /**
     * Builds a new Button instance with the configured properties.
     *
     * @return a new Button instance
     */
    public Button build() {
      Button button = new Button(text);

      if (id != null) {
        button.setId(id);
      }

      if (styleClass != null) {
        button.getStyleClass().add(styleClass);
      }

      button.getStyleClass().add("button-" + size);

      if (rounded) {
        button.getStyleClass().add("rounded");
      }

      if (outline) {
        button.getStyleClass().add("outline");
      }

      if (style != null) {
        button.setStyle(style);
      }

      if (width > 0) {
        button.setPrefWidth(width);
      }

      if (height > 0) {
        button.setPrefHeight(height);
      }

      if (onAction != null) {
        button.setOnAction(onAction);
      }

      button.setDisable(disabled);

      return button;
    }
  }
}