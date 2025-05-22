package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Builder class for creating and configuring JavaFX {@link Button} instances. Provides a fluent
 * interface for setting various button properties.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * Button button = new ButtonBuilder()
 *     .text("Click Me")
 *     .width(100)
 *     .height(30)
 *     .onClick(event -> System.out.println("Button clicked!"))
 *     .build();
 * }</pre>
 *
 * @see Button
 * @see javafx.scene.control.Control
 */
public class ButtonBuilder {

  private final Button button;

  /**
   * Creates a new ButtonBuilder instance with a default {@link Button}.
   *
   * <p>The button is initialized with default JavaFX properties and can be customized using the
   * builder methods.
   */
  public ButtonBuilder() {
    this.button = new Button();
  }

  /**
   * Sets the text displayed on the button.
   *
   * <p>This text will be shown as the button's label.
   *
   * @param text the text to display
   * @return this builder instance for method chaining
   * @see Button#setText(String)
   */
  public ButtonBuilder text(String text) {
    button.setText(text);
    return this;
  }

  /**
   * Adds style classes to the button.
   *
   * <p>These classes should be defined in your CSS stylesheet.
   *
   * @param styleClasses one or more CSS style classes to apply
   * @return this builder instance for method chaining
   * @see javafx.scene.Node#getStyleClass()
   */
  public ButtonBuilder styleClass(String... styleClasses) {
    button.getStyleClass().addAll(styleClasses);
    return this;
  }

  /**
   * Sets the action handler for when the button is clicked.
   *
   * <p>The handler will be called whenever the button is clicked by the user.
   *
   * @param handler the event handler to execute on button click
   * @return this builder instance for method chaining
   * @see Button#setOnAction(EventHandler)
   */
  public ButtonBuilder onClick(EventHandler<ActionEvent> handler) {
    button.setOnAction(handler);
    return this;
  }

  /**
   * Sets the preferred width of the button.
   *
   * <p>The actual width may vary based on the layout constraints.
   *
   * @param width the preferred width in pixels
   * @return this builder instance for method chaining
   * @see javafx.scene.layout.Region#setPrefWidth(double)
   */
  public ButtonBuilder width(double width) {
    button.setPrefWidth(width);
    return this;
  }

  /**
   * Sets the preferred height of the button.
   *
   * <p>The actual height may vary based on the layout constraints.
   *
   * @param height the preferred height in pixels
   * @return this builder instance for method chaining
   * @see javafx.scene.layout.Region#setPrefHeight(double)
   */
  public ButtonBuilder height(double height) {
    button.setPrefHeight(height);
    return this;
  }

  /**
   * Sets whether the button is disabled.
   *
   * <p>A disabled button cannot be clicked and appears grayed out.
   *
   * @param disabled true to disable the button, false to enable it
   * @return this builder instance for method chaining
   * @see javafx.scene.Node#setDisable(boolean)
   */
  public ButtonBuilder disabled(boolean disabled) {
    button.setDisable(disabled);
    return this;
  }

  /**
   * Builds and returns the configured {@link Button} instance.
   *
   * <p>After this call, the button is ready to be added to a scene graph.
   *
   * @return the configured JavaFX {@link Button}
   */
  public Button build() {
    return button;
  }
}
