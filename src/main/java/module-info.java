module edu.ntnu.stud.boardgame {
  requires javafx.controls;
  requires com.google.gson;
  requires java.logging;

  exports edu.ntnu.stud.boardgame;

  opens edu.ntnu.stud.boardgame.core.model to com.google.gson;
  opens edu.ntnu.stud.boardgame.snakesandladders.controller to org.testfx;
}