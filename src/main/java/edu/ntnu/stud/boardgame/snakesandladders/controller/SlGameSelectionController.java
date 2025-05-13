package edu.ntnu.stud.boardgame.snakesandladders.controller;

import edu.ntnu.stud.boardgame.core.controller.BaseController;
import edu.ntnu.stud.boardgame.core.navigation.Navigator;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.view.SlGameSelectionView;
import javafx.scene.Node;

public class SlGameSelectionController extends BaseController {

  private final SlGameSelectionView view;

  public SlGameSelectionController(Navigator navigator, ErrorDialog errorDialog) {
    super(navigator, errorDialog);
    this.view = new SlGameSelectionView(this);
  }

  @Override
  public Node getView() {
    return view;
  }
}
