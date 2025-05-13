package edu.ntnu.stud.boardgame.core.controller;

import edu.ntnu.stud.boardgame.core.exception.GameNotInitializedException;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.navigation.Navigator;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GameController extends BaseController {

  protected final static Logger LOGGER = Logger.getLogger(GameController.class.getName());
  private final List<BoardGameObserver> observers;
  protected BoardGame boardGame;

  public GameController(Navigator navigator, ErrorDialog errorDialog) {
    super(navigator, errorDialog);
    this.observers = new ArrayList<>();
  }

  public void init(Object... args) {
    if (args.length == 0 || !(args[0] instanceof BoardGame newGame)) {
      throw new IllegalArgumentException("First argument must be a BoardGame instance");
    }

    BoardGame oldGame = this.boardGame;
    this.boardGame = newGame;

    if (oldGame == null || (newGame.getBoard() != oldGame.getBoard()
        || oldGame.getDice() != newGame.getDice())) {
      newGame.addObservers(observers);
      newGame.init();

      observers.forEach(BoardGameObserver::init);
    }
  }

  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
    if (boardGame != null) {
      boardGame.addObserver(observer);
    }
  }

  protected void validateGameInitialized() {
    if (boardGame == null) {
      LOGGER.log(Level.SEVERE, "Game is not initialized");
      throw new GameNotInitializedException("Game has not been initialized");
    }
  }
}
