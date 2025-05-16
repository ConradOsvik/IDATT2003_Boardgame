package edu.ntnu.stud.boardgame.core.controller;

import edu.ntnu.stud.boardgame.core.navigation.Navigator;
import edu.ntnu.stud.boardgame.core.navigation.ViewController;
import edu.ntnu.stud.boardgame.core.navigation.ViewControllerFactory.ViewName;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;

public abstract class BaseController implements ViewController {

  protected final Navigator navigator;
  protected final ErrorDialog errorDialog;

  public BaseController(Navigator navigator, ErrorDialog errorDialog) {
    this.navigator = navigator;
    this.errorDialog = errorDialog;
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
