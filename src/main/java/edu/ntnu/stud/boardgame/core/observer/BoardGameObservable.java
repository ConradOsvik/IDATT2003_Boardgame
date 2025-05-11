package edu.ntnu.stud.boardgame.core.observer;

import java.util.ArrayList;
import java.util.List;

public interface BoardGameObservable {

  List<BoardGameObserver> observers = new ArrayList<>();

  void addObserver(BoardGameObserver observer);

  void addObservers(List<BoardGameObserver> observers);

  void removeObserver(BoardGameObserver observer);

  void notifyObservers(GameEvent event);
}
