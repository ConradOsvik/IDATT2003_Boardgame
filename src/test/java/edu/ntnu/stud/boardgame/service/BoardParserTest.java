package edu.ntnu.stud.boardgame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.io.board.BoardFileReader;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriter;
import edu.ntnu.stud.boardgame.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardParserTest {

  @Mock private BoardFileReader mockBoardReader;
  @Mock private BoardFileWriter mockBoardWriter;
  @Mock private Board mockBoard;
  private BoardParser boardParser;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    boardParser = BoardParser.getInstance();

    // Fix the field names to match actual class
    java.lang.reflect.Field readerField = BoardParser.class.getDeclaredField("boardReader");
    readerField.setAccessible(true);
    readerField.set(boardParser, mockBoardReader);

    java.lang.reflect.Field writerField = BoardParser.class.getDeclaredField("boardWriter");
    writerField.setAccessible(true);
    writerField.set(boardParser, mockBoardWriter);
  }

  @Test
  void getInstance_returnsSameInstance() {
    BoardParser instance1 = BoardParser.getInstance();
    BoardParser instance2 = BoardParser.getInstance();
    assertSame(instance1, instance2);
  }

  @Test
  void parseBoard_validData_returnsBoard() throws BoardParsingException {
    String boardData = "{\"valid\":\"json\"}";
    when(mockBoardReader.readBoardFromString(boardData)).thenReturn(mockBoard);

    Board result = boardParser.parseBoard(boardData);

    assertSame(mockBoard, result);
    verify(mockBoardReader).readBoardFromString(boardData);
  }

  @Test
  void parseBoard_nullData_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> boardParser.parseBoard(null));
    assertEquals("Board data cannot be null or empty.", e.getMessage());
  }

  @Test
  void parseBoard_emptyData_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> boardParser.parseBoard(""));
    assertEquals("Board data cannot be null or empty.", e.getMessage());
  }

  @Test
  void parseBoard_readerThrowsException_throwsBoardParsingException() throws BoardParsingException {
    String boardData = "{\"invalid\":\"json\"}";
    BoardParsingException parsingException = new BoardParsingException("Parse error");
    when(mockBoardReader.readBoardFromString(boardData)).thenThrow(parsingException);

    assertThrows(BoardParsingException.class, () -> boardParser.parseBoard(boardData));
  }

  @Test
  void serializeBoard_validBoard_returnsJson() throws BoardWritingException {
    String expectedJson = "{\"serialized\":\"board\"}";
    when(mockBoardWriter.writeBoardToString(mockBoard)).thenReturn(expectedJson);

    String result = boardParser.serializeBoard(mockBoard);

    assertEquals(expectedJson, result);
    verify(mockBoardWriter).writeBoardToString(mockBoard);
  }

  @Test
  void serializeBoard_nullBoard_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(IllegalArgumentException.class, () -> boardParser.serializeBoard(null));
    assertEquals("Board cannot be null.", e.getMessage());
  }

  @Test
  void serializeBoard_writerThrowsException_throwsBoardWritingException()
      throws BoardWritingException {
    BoardWritingException writingException = new BoardWritingException("Write error");
    when(mockBoardWriter.writeBoardToString(mockBoard)).thenThrow(writingException);

    assertThrows(BoardWritingException.class, () -> boardParser.serializeBoard(mockBoard));
  }
}
