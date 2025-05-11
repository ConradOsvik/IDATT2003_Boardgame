package edu.ntnu.stud.boardgame.snakesandladders.view.components.board;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import javafx.scene.layout.BorderPane;

public class BoardPreview extends BorderPane {

  private final ResizableBoard resizableBoard;

  public BoardPreview() {
    this.resizableBoard = new ResizableBoard();
    setCenter(resizableBoard);

    setPrefSize(300, 300);
    setMinSize(300, 300);
  }

  public void setBoard(SlBoard board) {
    resizableBoard.setBoard(board);
  }
}