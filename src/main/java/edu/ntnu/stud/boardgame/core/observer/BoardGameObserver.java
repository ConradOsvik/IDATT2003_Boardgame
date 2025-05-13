package edu.ntnu.stud.boardgame.core.observer;

public interface BoardGameObserver {

  void init();

  void onGameEvent(GameEvent event);
}
