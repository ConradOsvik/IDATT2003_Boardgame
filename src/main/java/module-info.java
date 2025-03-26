module edu.ntnu.stud.boardgame {
  requires javafx.controls;
  requires com.google.gson;

  exports edu.ntnu.stud.boardgame;
  exports edu.ntnu.stud.boardgame.core.model;
  exports edu.ntnu.stud.boardgame.core.action;
  exports edu.ntnu.stud.boardgame.controller;

  opens edu.ntnu.stud.boardgame.core.model to com.google.gson;
}