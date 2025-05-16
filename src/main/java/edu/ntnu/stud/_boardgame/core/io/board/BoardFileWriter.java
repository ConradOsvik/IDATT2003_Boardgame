package edu.ntnu.stud.boardgame.core.io.board;

import edu.ntnu.stud.boardgame.core.model.Board;
import java.io.IOException;
import java.nio.file.Path;

public interface BoardFileWriter<B extends Board> {

  void write(Path path, B board) throws IOException;
}
