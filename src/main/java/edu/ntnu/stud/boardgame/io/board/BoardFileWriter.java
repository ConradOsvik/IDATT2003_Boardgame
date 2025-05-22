package edu.ntnu.stud.boardgame.io.board;

import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.model.Board;
import java.nio.file.Path;

/**
 * An interface for writing {@link Board} objects to persistent storage.
 *
 * <p>Implementations of this interface are responsible for serializing board data to a specified
 * file
 * format. The default implementation uses JSON serialization via Gson.
 * </p>
 *
 * @see edu.ntnu.stud.boardgame.io.board.BoardFileWriterGson
 * @see edu.ntnu.stud.boardgame.io.board.BoardFileReader
 */
public interface BoardFileWriter {

  /**
   * Writes a board object to the specified file path.
   *
   * <p>This method serializes the provided {@link Board} object and saves it to the specified path.
   * If
   * the file already exists, it will be overwritten.
   * </p>
   *
   * @param path  the path where the board should be written
   * @param board the board object to serialize and save
   * @throws BoardWritingException if any errors occur during the writing process, such as I/O
   *                               errors or serialization failures
   */
  void writeBoard(Path path, Board board) throws BoardWritingException;
}
