package edu.ntnu.stud.boardgame.service;

import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.io.board.BoardFileReader;
import edu.ntnu.stud.boardgame.io.board.BoardFileReaderGson;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriter;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriterGson;
import edu.ntnu.stud.boardgame.model.Board;

/** Service for parsing and serializing board data without file system operations. */
public class BoardParser {
  private static BoardParser instance;
  private final BoardFileReader boardReader;
  private final BoardFileWriter boardWriter;

  private BoardParser() {
    this.boardReader = new BoardFileReaderGson();
    this.boardWriter = new BoardFileWriterGson();
  }

  /**
   * Gets the singleton instance of BoardParser.
   *
   * @return the BoardParser instance
   */
  public static synchronized BoardParser getInstance() {
    if (instance == null) {
      instance = new BoardParser();
    }
    return instance;
  }

  /**
   * Parses board data from a string.
   *
   * @param boardData the JSON string containing board data
   * @return the parsed Board object
   * @throws BoardParsingException if the data cannot be parsed
   */
  public Board parseBoard(String boardData) throws BoardParsingException {
    if (boardData == null || boardData.trim().isEmpty()) {
      throw new IllegalArgumentException("Board data cannot be null or empty.");
    }
    return boardReader.readBoardFromString(boardData);
  }

  /**
   * Serializes a board to a JSON string.
   *
   * @param board the board to serialize
   * @return the JSON string representation of the board
   * @throws BoardWritingException if the board cannot be serialized
   */
  public String serializeBoard(Board board) throws BoardWritingException {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null.");
    }
    return boardWriter.writeBoardToString(board);
  }
}
