package edu.ntnu.stud.boardgame;

import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Player;

public class BoardGameApp {

  private BoardGame boardGame = new BoardGame();

  public void start() {
    this.boardGame.createBoard();
    this.boardGame.createDice(2);

    this.boardGame.addPlayer(new Player("Arne", this.boardGame));
    this.boardGame.addPlayer(new Player("Ivar", this.boardGame));
    this.boardGame.addPlayer(new Player("Majid", this.boardGame));
    this.boardGame.addPlayer(new Player("Atle", this.boardGame));

    listPlayers();

    int roundNumber = 1;
    while (!this.boardGame.isFinished()) {
      System.out.println("Round number " + roundNumber++);
      this.boardGame.play();

      if (!this.boardGame.isFinished()) {
        showPlayerStatus();
      }

      System.out.println();
    }

    System.out.println("And the winner is: " + this.boardGame.getWinner().getName());
  }

  private void listPlayers() {
    System.out.println("The following players are playing the game:");
    for (Player player : boardGame.getPlayers()) {
      System.out.println("Name: " + player.getName());
    }
    System.out.println();
  }

  private void showPlayerStatus() {
    for (Player player : boardGame.getPlayers()) {
      System.out.println("Player " + player.getName() +
          " on tile " + player.getCurrentTile().getTileId());
    }
  }

  public static void main(String[] args) {
    BoardGameApp app = new BoardGameApp();
    app.start();
  }
}