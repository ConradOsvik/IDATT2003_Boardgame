package edu.ntnu.stud.boardgame.io.board;

import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.model.Board;
import java.nio.file.Path;

/**
 * Interface for reading board game configurations from files.
 *
 * <p>Implementations of this interface are responsible for parsing and loading board data from
 * various file formats into {@link Board} objects.
 *
 * @see Board
 * @see BoardParsingException
 */
public interface BoardFileReader {

  /**
   * Reads a board configuration from the specified file path.
   *
   * @param path the file path containing the board configuration data
   * @return a {@link Board} object populated with the configuration from the file
   * @throws BoardParsingException if the file cannot be read or contains invalid board data
   */
  Board readBoard(Path path) throws BoardParsingException;

  /**
   * Reads a board from a string.
   *
   * @param boardData the JSON string containing board data
   * @return the board parsed from the string
   * @throws BoardParsingException if the data cannot be parsed
   */
  Board readBoardFromString(String boardData) throws BoardParsingException;
}
