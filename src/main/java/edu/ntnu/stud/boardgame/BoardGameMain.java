package edu.ntnu.stud.boardgame;

import edu.ntnu.stud.boardgame.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class BoardGameMain extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    new MainController(primaryStage);
  }
}
