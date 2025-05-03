package edu.ntnu.stud.boardgame.core.navigation;

import edu.ntnu.stud.boardgame.core.navigation.ViewControllerFactory.ViewName;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class Navigator {

  private static final Logger logger = Logger.getLogger(Navigator.class.getName());

  private static Navigator instance;

  private BorderPane mainContainer;

  private final Stack<ViewName> navigationHistory = new Stack<>();

  private final Map<ViewName, ViewController> viewControllerCache = new HashMap<>();

  private ViewControllerFactory viewControllerFactory;

  private boolean useTransitions = true;

  private Navigator() {

  }

  public static Navigator getInstance() {
    if (instance == null) {
      instance = new Navigator();
    }

    return instance;
  }

  public void setMainContainer(BorderPane mainContainer) {
    this.mainContainer = mainContainer;
  }

  public void setViewControllerFactory(ViewControllerFactory viewControllerFactory) {
    this.viewControllerFactory = viewControllerFactory;
  }

  public void setUseTransitions(boolean useTransitions) {
    this.useTransitions = useTransitions;
  }

  public void navigateTo(ViewName viewName, Object... args) {
    if (mainContainer == null) {
      throw new IllegalStateException("Main container not set. Call setMainContainer first.");
    }

    if (viewControllerFactory == null) {
      throw new IllegalStateException(
          "ViewControllerFactory not set. Call setViewControllerFactory first.");
    }

    ViewController controller = getViewController(viewName);
    if (controller == null) {
      logger.warning("ViewController for " + viewName + " not found.");
      return;
    }

    controller.init(args);

    Node view = controller.getView();

    if (useTransitions) {
      applyTransitions(view);
    }

    mainContainer.setCenter(view);

    navigationHistory.push(viewName);

    logger.info("Navigated to " + viewName);
  }

  public boolean goBack(Object... args) {
    if (navigationHistory.size() <= 1) {
      logger.info("Already at the root view. Cannot go back.");
      return false;
    }

    navigationHistory.pop();

    ViewName previousView = navigationHistory.peek();

    ViewController previousController = getViewController(previousView);
    if (previousController == null) {
      logger.warning("Previous view controller for " + previousView + " not found.");
      return false;
    }

    previousController.init(args);

    Node view = previousController.getView();

    if (useTransitions) {
      applyTransitions(view);
    }

    mainContainer.setCenter(view);

    logger.info("Navigated back to " + previousView);

    return true;
  }

  private ViewController getViewController(ViewName viewName) {
    if (viewControllerCache.containsKey(viewName)) {
      return viewControllerCache.get(viewName);
    }

    ViewController controller = viewControllerFactory.createViewController(viewName);
    if (controller != null) {
      viewControllerCache.put(viewName, controller);
    }

    return controller;
  }

  private void applyTransitions(Node newView) {
    newView.setOpacity(0);

    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newView);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);
    fadeIn.play();
  }

  public ViewName getCurrentView() {
    return navigationHistory.isEmpty() ? null : navigationHistory.peek();
  }

  public boolean canGoBack() {
    return navigationHistory.size() > 1;
  }
}
