package edu.ntnu.stud.boardgame.core.observer;

import edu.ntnu.stud.boardgame.core.model.BoardGame;
import java.util.ArrayList;
import java.util.List;

public interface BoardGameObservable {

  List<BoardGameObserver> observers = new ArrayList<>();

  public void addObserver(BoardGameObserver observer);

  public void removeObserver(BoardGameObserver observer);

  public void notifyObservers(GameEvent event);

  void transferObserversFrom(BoardGame other);
}
