package edu.ntnu.stud.boardgame.model;

public abstract class BaseModel {

  protected void requireNotNull(Object obj, String message) {
    if (obj == null) {
      throw new IllegalArgumentException(message);
    }
  }

  protected void requireNotEmpty(String str, String message) {
    if (str == null || str.isEmpty()) {
      throw new IllegalArgumentException(message);
    }
  }
}
