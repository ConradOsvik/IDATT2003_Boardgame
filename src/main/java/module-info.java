module edu.ntnu.stud.boardgame {
  requires javafx.controls;
  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.core;

  exports edu.ntnu.stud.boardgame;
  exports edu.ntnu.stud.boardgame.core.model;
  exports edu.ntnu.stud.boardgame.core.action;
  exports edu.ntnu.stud.boardgame.controller;

  opens edu.ntnu.stud.boardgame.core.model to com.fasterxml.jackson.databind;
}