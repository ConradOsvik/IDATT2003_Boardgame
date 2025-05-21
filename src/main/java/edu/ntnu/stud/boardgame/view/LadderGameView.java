package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.BounceBackEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.GameStartedEvent;
import edu.ntnu.stud.boardgame.observer.event.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerWonEvent;
import edu.ntnu.stud.boardgame.observer.event.SnakeEncounteredEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import edu.ntnu.stud.boardgame.view.components.AbstractGameView;
import edu.ntnu.stud.boardgame.view.components.laddergame.ControlPanel;
import edu.ntnu.stud.boardgame.view.components.laddergame.LadderGameBoard;
import edu.ntnu.stud.boardgame.view.components.laddergame.PlayerScoreboard;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LadderGameView extends AbstractGameView {

  private final ControlPanel controlPanel;
  private final PlayerScoreboard scoreboard;
  private final LadderGameBoard gameBoard;

  public LadderGameView(MainController controller, GameController gameController) {
    super(controller, gameController);

    this.controlPanel = new ControlPanel(gameController);
    this.scoreboard = new PlayerScoreboard();
    this.gameBoard = new LadderGameBoard();

    initializeLayout();
  }

  private void initializeLayout() {
    VBox leftPanel = new VBox(20);
    leftPanel.setPadding(new Insets(15));
    leftPanel.setPrefWidth(300);
    leftPanel.getChildren().addAll(controlPanel, scoreboard);
    VBox.setVgrow(scoreboard, Priority.ALWAYS);

    gameArea.getChildren().addFirst(gameBoard);

    setLeft(leftPanel);
  }

  private void handleGameStarted(GameStartedEvent event) {
    gameBoard.clearPlayerPieces();

    gameBoard.setBoard(event.getBoard());
    scoreboard.updatePlayers(event.getPlayers());
    controlPanel.updateCurrentPlayer(event.getCurrentPlayer());
    victoryScreen.setVisible(false);

    for (Player player : event.getPlayers()) {
      if (player.getCurrentTile() != null) {
        gameBoard.updatePlayerPosition(player, player.getCurrentTile());
      }
    }
  }

  private void handleDiceRolled(DiceRolledEvent event) {
    soundManager.playSound("dice_roll");
    controlPanel.updateDiceValue(event.getDiceValue());
  }

  private void handleLadderClimbed(LadderClimbedEvent event) {
    soundManager.playSound("ladder");
    gameBoard.animatePlayerLadderClimb(
        event.getPlayer(),
        event.getFromTile(),
        event.getToTile());
  }

  private void handleSnakeEncountered(SnakeEncounteredEvent event) {
    soundManager.playSound("snake");
    gameBoard.animatePlayerSnakeSlide(
        event.getPlayer(),
        event.getFromTile(),
        event.getToTile());
  }

  private void handleBounceBack(BounceBackEvent event) {
    soundManager.playSound("bounce");
    gameBoard.animatePlayerBounceBack(
        event.getPlayer(),
        event.getFromTile(),
        event.getToTile());
  }

  private void handlePlayerMoved(PlayerMovedEvent event) {
    gameBoard.animatePlayerMove(
        event.getPlayer(),
        event.getFromTile(),
        event.getToTile());
  }

  private void handleTurnChanged(TurnChangedEvent event) {
    Player currentPlayer = event.getCurrentPlayer();
    controlPanel.updateCurrentPlayer(currentPlayer);
    scoreboard.highlightCurrentPlayer(currentPlayer);
  }

  private void handlePlayerWon(PlayerWonEvent event) {
    controlPanel.setDiceDisabled(true);
    Player winner = event.getWinner();
    if (winner != null) {
      showVictoryScreen(winner);
    }
  }

  @Override
  protected void handleGameEvent(GameEvent event) {
    if (event instanceof GameStartedEvent startedEvent) {
      handleGameStarted(startedEvent);
    } else if (event instanceof DiceRolledEvent diceEvent) {
      handleDiceRolled(diceEvent);
    } else if (event instanceof LadderClimbedEvent ladderEvent) {
      handleLadderClimbed(ladderEvent);
    } else if (event instanceof SnakeEncounteredEvent snakeEvent) {
      handleSnakeEncountered(snakeEvent);
    } else if (event instanceof BounceBackEvent bounceEvent) {
      handleBounceBack(bounceEvent);
    } else if (event instanceof PlayerMovedEvent moveEvent) {
      handlePlayerMoved(moveEvent);
    } else if (event instanceof TurnChangedEvent turnEvent) {
      handleTurnChanged(turnEvent);
    } else if (event instanceof PlayerWonEvent wonEvent) {
      handlePlayerWon(wonEvent);
    }
  }
}