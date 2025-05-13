package edu.ntnu.stud.boardgame.common.controller;

import edu.ntnu.stud.boardgame.common.view.MainMenuView;
import edu.ntnu.stud.boardgame.core.controller.BaseController;
import edu.ntnu.stud.boardgame.core.navigation.Navigator;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import javafx.scene.Node;

public class MainMenuController extends BaseController {

  private final MainMenuView view;

  public MainMenuController(Navigator navigator, ErrorDialog errorDialog) {
    super(navigator, errorDialog);
    this.view = new MainMenuView(this);
    view.setupComponents();
  }

  @Override
  public Node getView() {
    return view;
  }
}
