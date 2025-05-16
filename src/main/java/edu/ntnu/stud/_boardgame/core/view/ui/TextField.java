package edu.ntnu.stud.boardgame.core.view.ui;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * A customized JavaFX TextField with enhanced styling options and a builder API. This class extends
 * the standard JavaFX TextField to provide additional functionality and easier configuration
 * through a builder pattern.
 *
 * <p>TextFields are UI components that allow users to input text.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * TextField usernameField = TextField.builder()
 *     .promptText("Enter username")
 *     .maxLength(30)
 *     .width(200)
 *     .rounded(true)
 *     .build();
 * </pre>
 */
public class TextField extends javafx.scene.control.TextField {

  /**
   * Constructs a TextField with no initial text.
   */
  public TextField() {
    super();
    initialize();
  }

  /**
   * Constructs a TextField with the specified initial text.
   *
   * @param text the initial text to display
   */
  public TextField(String text) {
    super(text);
    initialize();
  }

  /**
   * Initializes the text field with default settings. Adds the standard CSS style class.
   */
  private void initialize() {
    getStyleClass().add("text-field");
  }

  /**
   * Creates a new text field builder instance to configure a text field.
   *
   * @return a new builder instance for creating a TextField
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for creating customized TextField instances. This pattern allows for more
   * readable and maintainable code when configuring text fields with multiple properties.
   */
  public static class Builder {

    private String text;
    private String id;
    private String styleClass;
    private String style;
    private double width;
    private double height;
    private String promptText;
    private boolean editable;
    private boolean disabled;
    private boolean focused;
    private boolean rounded;
    private int maxLength;
    private EventHandler<KeyEvent> onKeyPressed;
    private EventHandler<KeyEvent> onKeyReleased;
    private EventHandler<KeyEvent> onKeyTyped;

    /**
     * Creates a new builder instance with default settings.
     */
    private Builder() {
      this.text = "";
      this.width = -1;
      this.height = -1;
      this.editable = true;
      this.disabled = false;
      this.focused = false;
      this.rounded = false;
      this.maxLength = -1;
    }

    /**
     * Sets the initial text of the text field.
     *
     * @param text the initial text
     * @return this builder for method chaining
     */
    public Builder text(String text) {
      this.text = text;
      return this;
    }

    /**
     * Sets the CSS ID of the text field.
     *
     * @param id the CSS ID
     * @return this builder for method chaining
     */
    public Builder id(String id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a CSS style class to the text field.
     *
     * @param styleClass the CSS style class to add
     * @return this builder for method chaining
     */
    public Builder styleClass(String styleClass) {
      this.styleClass = styleClass;
      return this;
    }

    /**
     * Sets inline CSS style for the text field.
     *
     * @param style the CSS style string
     * @return this builder for method chaining
     */
    public Builder style(String style) {
      this.style = style;
      return this;
    }

    /**
     * Sets the preferred width of the text field.
     *
     * @param width the preferred width in pixels
     * @return this builder for method chaining
     */
    public Builder width(double width) {
      this.width = width;
      return this;
    }

    /**
     * Sets the preferred height of the text field.
     *
     * @param height the preferred height in pixels
     * @return this builder for method chaining
     */
    public Builder height(double height) {
      this.height = height;
      return this;
    }

    /**
     * Sets the prompt text to display when the text field is empty.
     *
     * @param promptText the prompt text to display
     * @return this builder for method chaining
     */
    public Builder promptText(String promptText) {
      this.promptText = promptText;
      return this;
    }

    /**
     * Sets whether the text field is editable.
     *
     * @param editable true if the text field should be editable, false otherwise
     * @return this builder for method chaining
     */
    public Builder editable(boolean editable) {
      this.editable = editable;
      return this;
    }

    /**
     * Sets whether the text field is disabled.
     *
     * @param disabled true to disable the text field, false to enable it
     * @return this builder for method chaining
     */
    public Builder disabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    /**
     * Sets whether the text field should automatically receive focus when added to a scene.
     *
     * @param focused true to request focus when added to a scene, false otherwise
     * @return this builder for method chaining
     */
    public Builder focused(boolean focused) {
      this.focused = focused;
      return this;
    }

    /**
     * Sets whether the text field should have rounded corners.
     *
     * @param rounded true for rounded corners, false for square corners
     * @return this builder for method chaining
     */
    public Builder rounded(boolean rounded) {
      this.rounded = rounded;
      return this;
    }

    /**
     * Sets the maximum number of characters that can be entered in the text field. A value less
     * than or equal to 0 means no limit.
     *
     * @param maxLength the maximum number of characters
     * @return this builder for method chaining
     */
    public Builder maxLength(int maxLength) {
      this.maxLength = maxLength;
      return this;
    }

    /**
     * Sets the event handler to be called when a key is pressed while the text field has focus.
     *
     * @param handler the event handler for key press events
     * @return this builder for method chaining
     */
    public Builder onKeyPressed(EventHandler<KeyEvent> handler) {
      this.onKeyPressed = handler;
      return this;
    }

    /**
     * Sets the event handler to be called when a key is released while the text field has focus.
     *
     * @param handler the event handler for key release events
     * @return this builder for method chaining
     */
    public Builder onKeyReleased(EventHandler<KeyEvent> handler) {
      this.onKeyReleased = handler;
      return this;
    }

    /**
     * Sets the event handler to be called when a key character is typed while the text field has
     * focus.
     *
     * @param handler the event handler for key typed events
     * @return this builder for method chaining
     */
    public Builder onKeyTyped(EventHandler<KeyEvent> handler) {
      this.onKeyTyped = handler;
      return this;
    }

    /**
     * Builds a new TextField instance with the configured properties.
     *
     * @return a new TextField instance
     */
    public TextField build() {
      TextField textField = new TextField(text);

      if (id != null) {
        textField.setId(id);
      }

      if (styleClass != null) {
        textField.getStyleClass().add(styleClass);
      }

      if (rounded) {
        textField.getStyleClass().add("rounded");
      }

      if (style != null) {
        textField.setStyle(style);
      }

      if (width > 0) {
        textField.setPrefWidth(width);
      }

      if (height > 0) {
        textField.setPrefHeight(height);
      }

      if (promptText != null) {
        textField.setPromptText(promptText);
      }

      textField.setEditable(editable);
      textField.setDisable(disabled);

      if (focused) {
        textField.sceneProperty().addListener((obs, oldScene, newScene) -> {
          if (newScene != null) {
            textField.requestFocus();
          }
        });
      }

      if (maxLength > 0) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null && newValue.length() > maxLength) {
            textField.setText(oldValue);
          }
        });
      }

      if (onKeyPressed != null) {
        textField.setOnKeyPressed(onKeyPressed);
      }

      if (onKeyReleased != null) {
        textField.setOnKeyReleased(onKeyReleased);
      }

      if (onKeyTyped != null) {
        textField.setOnKeyTyped(onKeyTyped);
      }

      return textField;
    }
  }
}