package edu.ntnu.stud.boardgame.observer;

/**
 * Observer interface for receiving board game events.
 * 
 * <p>
 * Classes implementing this interface can receive notifications about
 * game state changes, player actions, and other game events.
 * </p>
 */
public interface BoardGameObserver {

  /**
   * Called when a game event occurs.
   *
   * @param event the game event that occurred
   */
  void onGameEvent(GameEvent event);
}
