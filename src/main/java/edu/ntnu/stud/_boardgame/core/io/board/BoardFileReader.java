package edu.ntnu.stud.boardgame.core.io.board;

import edu.ntnu.stud.boardgame.core.model.Board;
import java.io.IOException;
import java.nio.file.Path;

public interface BoardFileReader<B extends Board> {

  B read(Path path) throws IOException;
}
