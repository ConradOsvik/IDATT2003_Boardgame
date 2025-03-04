package edu.ntnu.stud.boardgame.core.model;

abstract class BaseModel {

  protected void requireNotNull(Object obj, String message) {
    if (obj == null) {
      throw new IllegalArgumentException(message);
    }
  }
}
