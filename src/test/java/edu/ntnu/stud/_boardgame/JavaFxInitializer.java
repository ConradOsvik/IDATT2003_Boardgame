package edu.ntnu.stud.boardgame;

import javafx.application.Platform;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JavaFxInitializer implements BeforeAllCallback {

  @Override
  public void beforeAll(ExtensionContext context) {
    Platform.startup(() -> {
    });
//    new JFXPanel();
  }
}