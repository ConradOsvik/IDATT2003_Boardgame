package edu.ntnu.stud.boardgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new VBox(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void launchTerminal() {
        System.out.println("Hello, World!");
    }

    public static void main(String[] args) {
        for(String arg : args) {
            if(arg.equals("--cli")) {
                launchTerminal();
                return;
            }
        }

        launch();
    }
}