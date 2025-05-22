package edu.ntnu.stud.boardgame;

import edu.ntnu.stud.boardgame.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

/** Main class for the application. */
public class BoardGameMain extends Application {

  /** Starts the application. */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Creates and shows the main application window.
   *
   * @param primaryStage the main application window
   */
  @Override
  public void start(Stage primaryStage) {
    new MainController(primaryStage);
  }
}
