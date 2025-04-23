module edu.ntnu.stud.boardgame {
  requires javafx.controls;
  requires com.google.gson;

  exports edu.ntnu.stud.boardgame;
  exports edu.ntnu.stud.boardgame.core.model;
  exports edu.ntnu.stud.boardgame.core.model.action;
  exports edu.ntnu.stud.boardgame.core.observer;
  exports edu.ntnu.stud.boardgame.core.factory;
  exports edu.ntnu.stud.boardgame.core.view.ui;
  exports edu.ntnu.stud.boardgame.core.view.styles;

  exports edu.ntnu.stud.boardgame.snakesandladders.model;
  exports edu.ntnu.stud.boardgame.snakesandladders.model.action;
  exports edu.ntnu.stud.boardgame.snakesandladders.controller;
  exports edu.ntnu.stud.boardgame.snakesandladders.view;

  opens edu.ntnu.stud.boardgame.core.model to com.google.gson;
}