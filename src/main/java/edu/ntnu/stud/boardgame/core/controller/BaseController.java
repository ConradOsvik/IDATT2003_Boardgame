package edu.ntnu.stud.boardgame.core.controller;

import edu.ntnu.stud.boardgame.core.navigation.Navigator;
import edu.ntnu.stud.boardgame.core.navigation.ViewController;
import edu.ntnu.stud.boardgame.core.navigation.ViewControllerFactory.ViewName;

public abstract class BaseController implements ViewController {

  protected final Navigator navigator;

  public BaseController() {
    this.navigator = Navigator.getInstance();
  }

  public void init(Object... args) {
  }

  public void navigateBack() {
    navigator.goBack();
  }

  public void navigateTo(ViewName viewName, Object... args) {
    navigator.navigateTo(viewName, args);
  }
}
