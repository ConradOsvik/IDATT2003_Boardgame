package edu.ntnu.stud.boardgame.core.io.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.stud.boardgame.core.model.Board;

public abstract class GsonBoardFileReader<B extends Board> implements BoardFileReader<B> {

  protected final Gson gson;

  public GsonBoardFileReader() {
    this.gson = new GsonBuilder().create();
  }
}