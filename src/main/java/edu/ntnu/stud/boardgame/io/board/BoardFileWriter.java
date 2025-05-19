package edu.ntnu.stud.boardgame.io.board;

import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.model.Board;
import java.nio.file.Path;

public interface BoardFileWriter {

  public void writeBoard(Path path, Board board) throws BoardWritingException;
}
