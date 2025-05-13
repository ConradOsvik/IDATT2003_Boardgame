package edu.ntnu.stud.boardgame.snakesandladders.controller;

import edu.ntnu.stud.boardgame.core.controller.GameController;
import edu.ntnu.stud.boardgame.core.navigation.Navigator;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.view.SlGameView;
import java.util.logging.Logger;
import javafx.scene.Node;

public class SlGameController extends GameController {

  private static final Logger LOGGER = Logger.getLogger(SlGameController.class.getName());
  private final SlGameView view;

  public SlGameController(Navigator navigator, ErrorDialog errorDialog) {
    super(navigator, errorDialog);
    this.view = new SlGameView(this, errorDialog);
  }

  @Override
  public Node getView() {
    return view.getNode();
  }

  public boolean resetGame() {
    try {
      validateGameInitialized();
      boardGame.reset();
      return true;
    } catch (Exception e) {
      LOGGER.severe("Failed to reset game: " + e.getMessage());
      errorDialog.showError("An error occurred", "Failed to reset game: " + e.getMessage());
      return false;
    }
  }

  public boolean startGame() {
    try {
      validateGameInitialized();
      boardGame.start();
      return true;
    } catch (Exception e) {
      LOGGER.severe("Failed to start game: " + e.getMessage());
      errorDialog.showError("An error occurred", "Failed to start game: " + e.getMessage());
      return false;
    }
  }

  public boolean restartGame() {
    try {
      validateGameInitialized();
      boardGame.restart();
      return true;
    } catch (Exception e) {
      LOGGER.severe("Failed to restart game: " + e.getMessage());
      errorDialog.showError("An error occurred", "Failed to restart game: " + e.getMessage());
      return false;
    }
  }

  public boolean addPlayer(String name, int tokenId) {
    try {
      validateGameInitialized();
      if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Player name cannot be null or empty");
      }
      SlPlayer player = new SlPlayer(name, tokenId);
      boardGame.addPlayer(player);
      return true;
    } catch (Exception e) {
      LOGGER.severe("Failed to add player: " + e.getMessage());
      errorDialog.showError("An error occurred", "Failed to add player: " + e.getMessage());
      return false;
    }
  }

  public boolean playCurrentTurn() {
    try {
      validateGameInitialized();
      boardGame.playCurrentTurn();
      return true;
    } catch (Exception e) {
      LOGGER.severe("Failed to play current turn: " + e.getMessage());
      errorDialog.showError("An error occurred",
          "Failed to roll dice and take turn: " + e.getMessage());
      return false;
    }
  }
}
