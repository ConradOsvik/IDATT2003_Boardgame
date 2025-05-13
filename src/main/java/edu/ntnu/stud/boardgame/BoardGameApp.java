package edu.ntnu.stud.boardgame;

import edu.ntnu.stud.boardgame.core.navigation.BoardGameViewControllerFactory;
import edu.ntnu.stud.boardgame.core.navigation.Navigator;
import edu.ntnu.stud.boardgame.core.navigation.ViewControllerFactory.ViewName;
import edu.ntnu.stud.boardgame.core.util.StyleManager;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BoardGameApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    BorderPane mainContainer = new BorderPane();

    Scene scene = new Scene(mainContainer, 900, 650);

    StyleManager.initializeBaseStyles(scene);

    ErrorDialog errorDialog = ErrorDialog.getInstance();
    errorDialog.initialize(primaryStage);

    Navigator navigator = Navigator.getInstance();
    navigator.setMainContainer(mainContainer);
    navigator.setViewControllerFactory(new BoardGameViewControllerFactory());
    navigator.setUseTransitions(true);

    navigator.navigateTo(ViewName.MAIN_MENU);

    primaryStage.setTitle("Board Game");
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(800);
    primaryStage.setMinHeight(600);
    primaryStage.setResizable(true);
    primaryStage.show();

    ToolBar toolBar = createToolBar(navigator, primaryStage);

    mainContainer.setTop(toolBar);
  }

  private ToolBar createToolBar(Navigator navigator, Stage primaryStage) {
    ToolBar toolBar = new ToolBar();

    Button goBack = Button.builder().text("Go Back").onAction(event -> {
      navigator.goBack();
    }).build();
    Button exit = Button.builder().text("Exit").styleClass("danger").onAction(event -> {
      System.exit(0);
    }).build();

    Button error = Button.builder().text("Error")
        .onAction(e -> ErrorDialog.getInstance().showError("test", "error")).build();

    toolBar.getItems().addAll(goBack, exit, error);

    return toolBar;
  }
}