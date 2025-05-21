module edu.ntnu.stud.boardgame {
  requires javafx.controls;
  requires javafx.media;
  requires com.google.gson;
  requires java.logging;

  exports edu.ntnu.stud.boardgame;
  exports edu.ntnu.stud.boardgame.model;
  exports edu.ntnu.stud.boardgame.model.action;
  exports edu.ntnu.stud.boardgame.model.action.registry;
  exports edu.ntnu.stud.boardgame.model.enums;
  exports edu.ntnu.stud.boardgame.model.game;
  exports edu.ntnu.stud.boardgame.controller;
  exports edu.ntnu.stud.boardgame.view;
  exports edu.ntnu.stud.boardgame.view.components;
  exports edu.ntnu.stud.boardgame.view.components.builder;
  exports edu.ntnu.stud.boardgame.view.components.laddergame;
  exports edu.ntnu.stud.boardgame.view.components.piece;
  exports edu.ntnu.stud.boardgame.io.board;
  exports edu.ntnu.stud.boardgame.io.player;
  exports edu.ntnu.stud.boardgame.exception;
  exports edu.ntnu.stud.boardgame.exception.files;
  exports edu.ntnu.stud.boardgame.observer;
  exports edu.ntnu.stud.boardgame.observer.event;
  exports edu.ntnu.stud.boardgame.service;
  exports edu.ntnu.stud.boardgame.util;
  exports edu.ntnu.stud.boardgame.factory;

  opens edu.ntnu.stud.boardgame.io.board;
  opens edu.ntnu.stud.boardgame.io.player;
  opens edu.ntnu.stud.boardgame.model;
  opens edu.ntnu.stud.boardgame.model.action;
  opens edu.ntnu.stud.boardgame.model.enums;
  opens edu.ntnu.stud.boardgame.model.game;
  opens edu.ntnu.stud.boardgame.exception;
  opens edu.ntnu.stud.boardgame.exception.files;
  opens edu.ntnu.stud.boardgame.observer;
  opens edu.ntnu.stud.boardgame.observer.event;
  opens edu.ntnu.stud.boardgame.service;
  opens edu.ntnu.stud.boardgame.util;
  opens edu.ntnu.stud.boardgame.factory;
}