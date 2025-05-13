package edu.ntnu.stud.boardgame.core.view.component;

import edu.ntnu.stud.boardgame.core.util.StyleManager;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ErrorDialog {

  private static ErrorDialog instance;

  private Stage mainStage;

  private ErrorDialog() {
  }

  public static ErrorDialog getInstance() {
    if (instance == null) {
      instance = new ErrorDialog();
    }
    return instance;
  }

  public void initialize(Stage mainStage) {
    this.mainStage = mainStage;
  }

  public void showError(String title, String message) {
    if (mainStage == null) {
      throw new IllegalStateException("ErrorDialog not initialized with main stage");
    }

    StackPane backdropPane = new StackPane();
    backdropPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

    mainStage.getScene().getRoot().setEffect(new BoxBlur(5, 5, 3));

    VBox dialogVBox = createDialogContent(title, message);
    dialogVBox.setPrefHeight(200);
    dialogVBox.setMaxHeight(250);

    backdropPane.getChildren().add(dialogVBox);
    StackPane.setAlignment(dialogVBox, Pos.CENTER);

    Scene backdropScene = new Scene(backdropPane);
    backdropScene.setFill(Color.TRANSPARENT);

    StyleManager.initializeBaseStyles(backdropScene);

    Stage dialogStage = new Stage(StageStyle.TRANSPARENT);
    dialogStage.initOwner(mainStage);
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setScene(backdropScene);

    backdropScene.widthProperty().addListener((obs, oldVal, newVal) -> centerDialog(dialogVBox));
    backdropScene.heightProperty().addListener((obs, oldVal, newVal) -> centerDialog(dialogVBox));

    dialogStage.setWidth(mainStage.getWidth());
    dialogStage.setHeight(mainStage.getHeight());
    mainStage.widthProperty()
        .addListener((obs, oldVal, newVal) -> dialogStage.setWidth(newVal.doubleValue()));
    mainStage.heightProperty()
        .addListener((obs, oldVal, newVal) -> dialogStage.setHeight(newVal.doubleValue()));

    dialogStage.setX(mainStage.getX());
    dialogStage.setY(mainStage.getY());
    mainStage.xProperty()
        .addListener((obs, oldVal, newVal) -> dialogStage.setX(newVal.doubleValue()));
    mainStage.yProperty()
        .addListener((obs, oldVal, newVal) -> dialogStage.setY(newVal.doubleValue()));

    Button okButton = (Button) dialogVBox.lookup("#okButton");
    okButton.setOnAction(e -> {
      mainStage.getScene().getRoot().setEffect(null);
      dialogStage.close();
    });

    dialogStage.showAndWait();
  }

  private VBox createDialogContent(String title, String message) {
    VBox dialogVBox = new VBox(15);
    dialogVBox.setPadding(new Insets(20));
    dialogVBox.setMaxWidth(400);
    dialogVBox.setMinHeight(200);
    dialogVBox.setAlignment(Pos.TOP_CENTER);
    dialogVBox.setStyle("-fx-background-color: white; " + "-fx-background-radius: 8px; "
        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

    Label titleLabel = Label.builder().text(title).fontSize("2xl").build();
    titleLabel.setMaxWidth(Double.MAX_VALUE);
    titleLabel.setAlignment(Pos.CENTER_LEFT);

    Separator separator = new Separator();

    VBox messageContainer = new VBox();
    messageContainer.setAlignment(Pos.TOP_LEFT);
    messageContainer.setMaxWidth(Double.MAX_VALUE);

    Label messageLabel = Label.builder().text(message).fontSize("lg").build();
    messageLabel.setMaxWidth(Double.MAX_VALUE);
    messageLabel.setWrapText(true);

    messageContainer.getChildren().add(messageLabel);
    VBox.setVgrow(messageContainer, Priority.ALWAYS);

    Button okButton = Button.builder().text("OK").id("okButton").styleClass("primary").build();
    okButton.setPrefWidth(80);

    HBox buttonContainer = new HBox(okButton);
    buttonContainer.setAlignment(Pos.CENTER_RIGHT);

    dialogVBox.getChildren().addAll(titleLabel, separator, messageContainer, buttonContainer);

    return dialogVBox;
  }

  private void centerDialog(VBox dialogVBox) {
    StackPane.setAlignment(dialogVBox, Pos.CENTER);
  }
}