package edu.ntnu.stud.boardgame.io.board;

import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.model.Board;
import java.nio.file.Path;

public interface BoardFileReader {

  public Board readBoard(Path path) throws BoardParsingException;
}
