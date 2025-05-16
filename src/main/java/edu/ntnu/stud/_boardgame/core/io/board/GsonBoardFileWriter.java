package edu.ntnu.stud.boardgame.core.io.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.stud.boardgame.core.model.Board;

public abstract class GsonBoardFileWriter<B extends Board> implements BoardFileWriter<B> {

  protected final Gson gson;

  public GsonBoardFileWriter() {
    this.gson = new GsonBuilder().setPrettyPrinting().create();
  }
}
