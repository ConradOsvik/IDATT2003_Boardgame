package edu.ntnu.stud.boardgame.core.view.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * A customized JavaFX VBox that serves as a container panel with enhanced styling options and a
 * builder API. This class extends VBox to provide additional functionality for creating panels with
 * consistent styling such as borders, backgrounds, and shadows.
 *
 * <p>Panels are useful for grouping related UI components with visual separation from other
 * elements in the interface.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * Panel loginPanel = Panel.builder()
 *     .children(usernameField, passwordField, loginButton)
 *     .styleClass("card")
 *     .rounded(true)
 *     .shadow(true)
 *     .build();
 * </pre>
 */
public class Panel extends VBox {

  /**
   * Constructs an empty Panel with default styling.
   */
  public Panel() {
    super();
    initialize();
  }

  /**
   * Constructs a Panel containing the specified children with default styling.
   *
   * @param children the initial children of the panel
   */
  public Panel(Node... children) {
    super(children);
    initialize();
  }

  /**
   * Initializes the panel with default settings. Adds standard CSS class, padding, and spacing.
   */
  private void initialize() {
    getStyleClass().add("panel");
    setPadding(new Insets(16));
    setSpacing(12);
  }

  /**
   * Creates a new panel builder instance to configure a panel.
   *
   * @return a new builder instance for creating a Panel
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for creating customized Panel instances. This pattern allows for more readable
   * and maintainable code when configuring panels with multiple properties.
   */
  public static class Builder {

    private Node[] children;
    private String id;
    private String styleClass;
    private String style;
    private double width;
    private double height;
    private double spacing;
    private Insets padding;
    private boolean rounded;
    private boolean shadow;
    private boolean bordered;
    private String bg;
    private Pos alignment;

    /**
     * Creates a new builder instance with default settings.
     */
    private Builder() {
      this.children = new Node[0];
      this.width = -1;
      this.height = -1;
      this.spacing = 12;
      this.padding = new Insets(16);
      this.rounded = false;
      this.shadow = false;
      this.bordered = true;
      this.bg = "white";
      this.alignment = null;
    }

    /**
     * Sets the children nodes to be contained in the panel.
     *
     * @param children the child nodes to add to the panel
     * @return this builder for method chaining
     */
    public Builder children(Node... children) {
      this.children = children;
      return this;
    }

    /**
     * Sets the CSS ID of the panel.
     *
     * @param id the CSS ID
     * @return this builder for method chaining
     */
    public Builder id(String id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a CSS style class to the panel.
     *
     * @param styleClass the CSS style class to add
     * @return this builder for method chaining
     */
    public Builder styleClass(String styleClass) {
      this.styleClass = styleClass;
      return this;
    }

    /**
     * Sets inline CSS style for the panel.
     *
     * @param style the CSS style string
     * @return this builder for method chaining
     */
    public Builder style(String style) {
      this.style = style;
      return this;
    }

    /**
     * Sets the preferred width of the panel.
     *
     * @param width the preferred width in pixels
     * @return this builder for method chaining
     */
    public Builder width(double width) {
      this.width = width;
      return this;
    }

    /**
     * Sets the preferred height of the panel.
     *
     * @param height the preferred height in pixels
     * @return this builder for method chaining
     */
    public Builder height(double height) {
      this.height = height;
      return this;
    }

    /**
     * Sets the spacing between child nodes in the panel.
     *
     * @param spacing the spacing in pixels
     * @return this builder for method chaining
     */
    public Builder spacing(double spacing) {
      this.spacing = spacing;
      return this;
    }

    /**
     * Sets the padding around the content of the panel using an Insets object.
     *
     * @param padding the padding as an Insets object
     * @return this builder for method chaining
     */
    public Builder padding(Insets padding) {
      this.padding = padding;
      return this;
    }

    /**
     * Sets the padding around the content of the panel with uniform padding on all sides.
     *
     * @param padding the padding in pixels
     * @return this builder for method chaining
     */
    public Builder padding(double padding) {
      this.padding = new Insets(padding);
      return this;
    }

    /**
     * Sets whether the panel should have rounded corners.
     *
     * @param rounded true for rounded corners, false for square corners
     * @return this builder for method chaining
     */
    public Builder rounded(boolean rounded) {
      this.rounded = rounded;
      return this;
    }

    /**
     * Sets whether the panel should have a drop shadow effect.
     *
     * @param shadow true to add a shadow, false for no shadow
     * @return this builder for method chaining
     */
    public Builder shadow(boolean shadow) {
      this.shadow = shadow;
      return this;
    }

    /**
     * Sets whether the panel should have a border.
     *
     * @param bordered true to add a border, false for no border
     * @return this builder for method chaining
     */
    public Builder bordered(boolean bordered) {
      this.bordered = bordered;
      return this;
    }

    /**
     * Sets the background color of the panel. Predefined colors include: "primary", "secondary",
     * "white", "light", "dark", etc.
     *
     * @param bg the background color identifier
     * @return this builder for method chaining
     */
    public Builder bg(String bg) {
      this.bg = bg;
      return this;
    }

    /**
     * Sets the alignment of children within the panel.
     *
     * @param alignment the alignment value
     * @return this builder for method chaining
     */
    public Builder alignment(Pos alignment) {
      this.alignment = alignment;
      return this;
    }

    /**
     * Builds a new Panel instance with the configured properties.
     *
     * @return a new Panel instance
     */
    public Panel build() {
      Panel panel = new Panel(children);

      if (id != null) {
        panel.setId(id);
      }

      panel.getStyleClass().add("panel");

      if (styleClass != null) {
        panel.getStyleClass().add(styleClass);
      }

      // Add background class
      if (bg != null) {
        panel.getStyleClass().add("bg-" + bg);
      }

      // Add rounded class
      if (rounded) {
        panel.getStyleClass().add("rounded");
      }

      // Add shadow class
      if (shadow) {
        panel.getStyleClass().add("shadow");
      }

      // Add border class
      if (bordered) {
        panel.getStyleClass().add("border");
      }

      // Apply explicit background and border if needed
      if (rounded) {
        Color bgColor = getBackgroundColor(bg);
        panel.setBackground(new Background(new BackgroundFill(
            bgColor, new CornerRadii(8), Insets.EMPTY)));

        if (bordered) {
          panel.setBorder(new Border(new BorderStroke(
              Color.LIGHTGRAY, BorderStrokeStyle.SOLID,
              new CornerRadii(8), BorderWidths.DEFAULT)));
        }
      } else if (bg != null) {
        // Apply background without rounded corners
        Color bgColor = getBackgroundColor(bg);
        panel.setBackground(new Background(new BackgroundFill(
            bgColor, CornerRadii.EMPTY, Insets.EMPTY)));

        if (bordered) {
          panel.setBorder(new Border(new BorderStroke(
              Color.LIGHTGRAY, BorderStrokeStyle.SOLID,
              CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
      }

      if (style != null) {
        panel.setStyle(style);
      }

      if (width > 0) {
        panel.setPrefWidth(width);
      }

      if (height > 0) {
        panel.setPrefHeight(height);
      }

      panel.setSpacing(spacing);
      panel.setPadding(padding);

      if (alignment != null) {
        panel.setAlignment(alignment);
      }

      return panel;
    }

    /**
     * Converts a background color identifier to a JavaFX Color object.
     *
     * @param bg the background color identifier
     * @return the corresponding Color object
     */
    private Color getBackgroundColor(String bg) {
      if (bg == null) {
        return Color.WHITE;
      }

      return switch (bg) {
        case "primary" -> Color.rgb(24, 24, 27);
        case "secondary" -> Color.rgb(244, 244, 245);
        case "destructive", "danger" -> Color.rgb(239, 68, 68);
        case "muted" -> Color.rgb(244, 244, 245);
        case "accent" -> Color.rgb(244, 244, 245);
        case "light" -> Color.rgb(248, 250, 252);
        case "dark" -> Color.rgb(30, 41, 59);
        case "black" -> Color.BLACK;
        case "gray" -> Color.LIGHTGRAY;
        default -> Color.WHITE;
      };
    }
  }
}