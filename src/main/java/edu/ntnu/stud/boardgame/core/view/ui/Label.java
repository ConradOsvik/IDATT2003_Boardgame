package edu.ntnu.stud.boardgame.core.view.ui;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * A customized JavaFX Label with enhanced styling options and builder API. This class extends the
 * standard JavaFX Label to provide additional functionality and easier configuration through a
 * builder pattern.
 *
 * <p>Labels are UI components that display non-editable text to the user.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * Label headerLabel = Label.builder()
 *     .text("Welcome to the Game")
 *     .fontWeight(FontWeight.BOLD)
 *     .fontSize("lg")
 *     .build();
 * </pre>
 */
public class Label extends javafx.scene.control.Label {

  /**
   * Constructs a Label with the specified text.
   *
   * @param text the text to display in the label
   */
  public Label(String text) {
    super(text);
  }

  /**
   * Constructs a Label with no text.
   */
  public Label() {
    super();
  }

  /**
   * Creates a new label builder instance to configure a label.
   *
   * @return a new builder instance for creating a Label
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for creating customized Label instances. This pattern allows for more readable
   * and maintainable code when configuring labels with multiple properties.
   */
  public static class Builder {

    private String text;
    private String id;
    private String styleClass;
    private String style;
    private double width;
    private double height;
    private Pos alignment;
    private TextAlignment textAlignment;
    private ContentDisplay contentDisplay;
    private String fontSize;
    private FontWeight fontWeight;
    private String fontFamily;
    private boolean truncate;
    private boolean italic;
    private boolean wrapText;

    /**
     * Creates a new builder instance with default settings.
     */
    private Builder() {
      this.text = "";
      this.width = -1;
      this.height = -1;
      this.fontSize = "md";
      this.fontWeight = FontWeight.NORMAL;
      this.fontFamily = "System";
      this.truncate = false;
      this.italic = false;
      this.contentDisplay = ContentDisplay.LEFT;
      this.textAlignment = TextAlignment.LEFT;
      this.wrapText = false;
    }

    /**
     * Sets the text to display in the label.
     *
     * @param text the text to display
     * @return this builder for method chaining
     */
    public Builder text(String text) {
      this.text = text;
      return this;
    }

    /**
     * Sets the CSS ID of the label.
     *
     * @param id the CSS ID
     * @return this builder for method chaining
     */
    public Builder id(String id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a CSS style class to the label.
     *
     * @param styleClass the CSS style class to add
     * @return this builder for method chaining
     */
    public Builder styleClass(String styleClass) {
      this.styleClass = styleClass;
      return this;
    }

    /**
     * Sets inline CSS style for the label.
     *
     * @param style the CSS style string
     * @return this builder for method chaining
     */
    public Builder style(String style) {
      this.style = style;
      return this;
    }

    /**
     * Sets the preferred width of the label.
     *
     * @param width the preferred width in pixels
     * @return this builder for method chaining
     */
    public Builder width(double width) {
      this.width = width;
      return this;
    }

    /**
     * Sets the preferred height of the label.
     *
     * @param height the preferred height in pixels
     * @return this builder for method chaining
     */
    public Builder height(double height) {
      this.height = height;
      return this;
    }

    /**
     * Sets the alignment of the label within its area.
     *
     * @param alignment the desired alignment
     * @return this builder for method chaining
     */
    public Builder alignment(Pos alignment) {
      this.alignment = alignment;
      return this;
    }

    /**
     * Sets the text alignment within the label.
     *
     * @param textAlignment the desired text alignment
     * @return this builder for method chaining
     */
    public Builder textAlignment(TextAlignment textAlignment) {
      this.textAlignment = textAlignment;
      return this;
    }

    /**
     * Sets how the graphic should be displayed relative to the text.
     *
     * @param contentDisplay the content display mode
     * @return this builder for method chaining
     */
    public Builder contentDisplay(ContentDisplay contentDisplay) {
      this.contentDisplay = contentDisplay;
      return this;
    }

    /**
     * Sets the font size of the label text. Values typically include "xs", "sm", "md", "lg", "xl",
     * etc.
     *
     * @param fontSize the font size identifier
     * @return this builder for method chaining
     */
    public Builder fontSize(String fontSize) {
      this.fontSize = fontSize;
      return this;
    }

    /**
     * Sets the font weight of the label text.
     *
     * @param fontWeight the font weight (NORMAL, BOLD, etc.)
     * @return this builder for method chaining
     */
    public Builder fontWeight(FontWeight fontWeight) {
      this.fontWeight = fontWeight;
      return this;
    }

    /**
     * Sets the font family of the label text.
     *
     * @param fontFamily the font family name
     * @return this builder for method chaining
     */
    public Builder fontFamily(String fontFamily) {
      this.fontFamily = fontFamily;
      return this;
    }

    /**
     * Sets whether the label text should be truncated if it doesn't fit.
     *
     * @param truncate true to truncate text, false to allow overflow
     * @return this builder for method chaining
     */
    public Builder truncate(boolean truncate) {
      this.truncate = truncate;
      return this;
    }

    /**
     * Sets whether the label text should be displayed in italic style.
     *
     * @param italic true for italic text, false for normal text
     * @return this builder for method chaining
     */
    public Builder italic(boolean italic) {
      this.italic = italic;
      return this;
    }

    /**
     * Sets whether the label text should wrap to multiple lines if it doesn't fit.
     *
     * @param wrapText true to wrap text, false to clip or truncate
     * @return this builder for method chaining
     */
    public Builder wrapText(boolean wrapText) {
      this.wrapText = wrapText;
      return this;
    }

    /**
     * Builds a new Label instance with the configured properties.
     *
     * @return a new Label instance
     */
    public Label build() {
      Label label = new Label(text);

      if (id != null) {
        label.setId(id);
      }

      if (styleClass != null) {
        label.getStyleClass().add(styleClass);
      }

      label.getStyleClass().add("text-" + fontSize);

      if (fontWeight == FontWeight.BOLD) {
        label.getStyleClass().add("font-bold");
      } else if (fontWeight == FontWeight.LIGHT) {
        label.getStyleClass().add("font-light");
      } else if (fontWeight == FontWeight.MEDIUM) {
        label.getStyleClass().add("font-medium");
      }

      if (italic) {
        label.getStyleClass().add("italic");
      }

      if (truncate) {
        label.getStyleClass().add("truncate");
      }

      if (style != null) {
        label.setStyle(style);
      }

      if (width > 0) {
        label.setPrefWidth(width);
      }

      if (height > 0) {
        label.setPrefHeight(height);
      }

      if (alignment != null) {
        label.setAlignment(alignment);
      }

      if (textAlignment != null) {
        label.setTextAlignment(textAlignment);
      }

      if (contentDisplay != null) {
        label.setContentDisplay(contentDisplay);
      }

      label.setWrapText(wrapText);

      return label;
    }
  }
}