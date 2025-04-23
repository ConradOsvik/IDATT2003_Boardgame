package edu.ntnu.stud.boardgame;

import edu.ntnu.stud.boardgame.core.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.view.styles.StyleManager;
import edu.ntnu.stud.boardgame.snakesandladders.controller.ControlPanelController;
import edu.ntnu.stud.boardgame.snakesandladders.controller.GameBoardController;
import edu.ntnu.stud.boardgame.snakesandladders.view.ControlPanelView;
import edu.ntnu.stud.boardgame.snakesandladders.view.GameBoardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main application class for the Snakes and Ladders board game.
 */
public class BoardGameApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    BorderPane mainLayout = new BorderPane();

    BoardGame game = BoardGameFactory.createSnakesAndLaddersGame();

    GameBoardController gameBoardController = new GameBoardController(game);
    ControlPanelController controlPanelController = new ControlPanelController(gameBoardController);

    GameBoardView gameBoardView = new GameBoardView(gameBoardController);
    ControlPanelView controlPanelView = new ControlPanelView(controlPanelController);

    mainLayout.setCenter(gameBoardView);
    mainLayout.setRight(controlPanelView);

    Scene scene = new Scene(mainLayout, 900, 650);

    StyleManager.initializeBaseStyles(scene);

    primaryStage.setTitle("Snakes and Ladders");
    primaryStage.setScene(scene);
    primaryStage.setResizable(true);
    primaryStage.show();
  }

  /**
   * Main method to launch the application.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}