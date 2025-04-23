package edu.ntnu.stud.boardgame;

import edu.ntnu.stud.boardgame.core.view.styles.StyleManager;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BoardGameApp extends Application {
  @Override
  public void start(Stage primaryStage) {
    VBox root = new VBox();

    Button button = Button.builder().text("button").styleClass("primary").onAction(e -> System.out.println("clicked!")).build();

    root.getChildren().add(button);

    Scene scene = new Scene(root, 800, 600);
    StyleManager.initializeBaseStyles(scene);

    primaryStage.setTitle("Board Game Application");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
