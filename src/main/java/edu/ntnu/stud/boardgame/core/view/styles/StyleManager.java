package edu.ntnu.stud.boardgame.core.view.styles;

import java.util.Objects;
import javafx.scene.Scene;

public class StyleManager {
  private static final String UI_CSS_PATH = "/styles/ui.css";

  public static void addStylesheet(Scene scene, String stylesheetPath) {
    if (scene == null || stylesheetPath == null || stylesheetPath.isEmpty()) {
      throw new IllegalArgumentException("Scene or stylesheet path cannot be null or empty");
    }

    String stylesheet = Objects.requireNonNull(StyleManager.class.getResource(stylesheetPath)).toExternalForm();
    scene.getStylesheets().add(stylesheet);
  }

  public static void initializeBaseStyles(Scene scene) {
    addStylesheet(scene, UI_CSS_PATH);
  }
}
