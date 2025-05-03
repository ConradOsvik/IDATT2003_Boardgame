package edu.ntnu.stud.boardgame.core.model;

/**
 * Abstract base class providing common functionality for model classes in the board game. Contains
 * utility methods for validation and error handling that can be used by subclasses.
 */
public abstract class BaseModel {

  /**
   * Utility method to check if an object is null and throw an exception if it is.
   *
   * @param obj     The object to check for null
   * @param message The error message to include in the exception if the object is null
   * @throws IllegalArgumentException if the provided object is null
   */
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
