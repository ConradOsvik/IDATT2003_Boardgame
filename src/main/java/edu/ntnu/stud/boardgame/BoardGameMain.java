package edu.ntnu.stud.boardgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BoardGameMain extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene scene = new Scene(new VBox(), 800, 600);

    primaryStage.setTitle("Board Game");
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(800);
    primaryStage.setMinHeight(600);
    primaryStage.setResizable(true);
    primaryStage.show();
  }
}
