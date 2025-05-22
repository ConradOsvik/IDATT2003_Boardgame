package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.scene.control.TextField;

/**
 * Builder class for creating and configuring JavaFX {@link TextField} instances. Provides a fluent
 * interface for setting various text field properties.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * TextField textField = new TextFieldBuilder()
 *     .promptText("Enter your name")
 *     .text("John")
 *     .width(200)
 *     .editable(true)
 *     .build();
 * }</pre>
 *
 * @see TextField
 * @see javafx.scene.control.Control
 */
public class TextFieldBuilder {

  private final TextField textField;

  /**
   * Creates a new TextFieldBuilder instance with a default {@link TextField}.
   *
   * <p>The text field is initialized with default JavaFX properties and can be customized using the
   * builder methods.
   */
  public TextFieldBuilder() {
    this.textField = new TextField();
  }

  /**
   * Sets the prompt text (placeholder) displayed when the text field is empty.
   *
   * <p>The prompt text provides a hint to the user about what should be entered.
   *
   * @param text the prompt text to display
   * @return this builder instance for method chaining
   * @see TextField#setPromptText(String)
   */
  public TextFieldBuilder promptText(String text) {
    textField.setPromptText(text);
    return this;
  }

  /**
   * Sets the text content of the text field.
   *
   * <p>This sets the actual text value of the field.
   *
   * @param text the text to set
   * @return this builder instance for method chaining
   * @see TextField#setText(String)
   */
  public TextFieldBuilder text(String text) {
    textField.setText(text);
    return this;
  }

  /**
   * Adds style classes to the text field.
   *
   * <p>These classes should be defined in your CSS stylesheet.
   *
   * @param styleClasses one or more CSS style classes to apply
   * @return this builder instance for method chaining
   * @see javafx.scene.Node#getStyleClass()
   */
  public TextFieldBuilder styleClass(String... styleClasses) {
    textField.getStyleClass().addAll(styleClasses);
    return this;
  }

  /**
   * Sets the preferred width of the text field.
   *
   * <p>The actual width may vary based on the layout constraints.
   *
   * @param width the preferred width in pixels
   * @return this builder instance for method chaining
   * @see javafx.scene.layout.Region#setPrefWidth(double)
   */
  public TextFieldBuilder width(double width) {
    textField.setPrefWidth(width);
    return this;
  }

  /**
   * Sets whether the text field is editable.
   *
   * <p>When not editable, the text field becomes read-only.
   *
   * @param editable true to make the text field editable, false to make it read-only
   * @return this builder instance for method chaining
   * @see TextField#setEditable(boolean)
   */
  public TextFieldBuilder editable(boolean editable) {
    textField.setEditable(editable);
    return this;
  }

  /**
   * Builds and returns the configured {@link TextField} instance.
   *
   * <p>After this call, the text field is ready to be added to a scene graph.
   *
   * @return the configured JavaFX {@link TextField}
   */
  public TextField build() {
    return textField;
  }
}
