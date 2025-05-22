package edu.ntnu.stud.boardgame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardFileServiceTest {

  @Mock private BoardParser mockBoardParser;
  @Mock private Board mockBoard;
  private BoardFileService boardFileService;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    boardFileService = BoardFileService.getInstance();

    // Fix the field names to match actual class
    java.lang.reflect.Field parserField = BoardFileService.class.getDeclaredField("boardParser");
    parserField.setAccessible(true);
    parserField.set(boardFileService, mockBoardParser);
  }

  @Test
  void getInstance_returnsSameInstance() {
    BoardFileService instance1 = BoardFileService.getInstance();
    BoardFileService instance2 = BoardFileService.getInstance();
    assertSame(instance1, instance2);
  }

  @Test
  void loadBoard_validFile_returnsBoard() throws BoardFileException {
    BoardGameType gameType = BoardGameType.LADDER;
    String fileName = "testBoard";
    String testBoardData = "{\"valid\":\"json\"}";

    when(mockBoardParser.parseBoard(testBoardData)).thenReturn(mockBoard);

    Board result = boardFileService.loadBoard(gameType, fileName);

    assertSame(mockBoard, result);
    verify(mockBoardParser).parseBoard(testBoardData);
  }

  @Test
  void loadBoard_nullGameType_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class, () -> boardFileService.loadBoard(null, "file"));
    assertEquals("Game type cannot be null.", e.getMessage());
  }

  @Test
  void loadBoard_nullFileName_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> boardFileService.loadBoard(BoardGameType.LADDER, null));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void loadBoard_emptyFileName_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> boardFileService.loadBoard(BoardGameType.LADDER, ""));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void loadBoard_parserThrowsException_throwsBoardFileException() throws BoardParsingException {
    BoardGameType gameType = BoardGameType.LADDER;
    String fileName = "parserIssue";
    String testBoardData = "{\"invalid\":\"json\"}";

    BoardParsingException parsingException = new BoardParsingException("Parse error");
    when(mockBoardParser.parseBoard(testBoardData)).thenThrow(parsingException);

    BoardFileException e =
        assertThrows(
            BoardFileException.class, () -> boardFileService.loadBoard(gameType, fileName));
    assertEquals("Failed to load board: Parse error", e.getMessage());
    assertSame(parsingException, e.getCause());
  }

  @Test
  void saveBoard_validInput_savesSuccessfully() throws BoardFileException {
    BoardGameType gameType = BoardGameType.MONOPOLY;
    String fileName = "saveTestBoard";
    String serializedBoard = "{\"serialized\":\"board\"}";

    when(mockBoardParser.serializeBoard(mockBoard)).thenReturn(serializedBoard);

    boardFileService.saveBoard(gameType, fileName, mockBoard);

    verify(mockBoardParser).serializeBoard(mockBoard);
  }

  @Test
  void saveBoard_nullGameType_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> boardFileService.saveBoard(null, "file", mockBoard));
    assertEquals("Game type cannot be null.", e.getMessage());
  }

  @Test
  void saveBoard_nullFileName_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> boardFileService.saveBoard(BoardGameType.LADDER, null, mockBoard));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void saveBoard_emptyFileName_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> boardFileService.saveBoard(BoardGameType.LADDER, "", mockBoard));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void saveBoard_nullBoard_throwsIllegalArgumentException() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> boardFileService.saveBoard(BoardGameType.MONOPOLY, "file", null));
    assertEquals("Board cannot be null.", e.getMessage());
  }

  @Test
  void saveBoard_parserThrowsException_throwsBoardFileException() throws BoardWritingException {
    BoardGameType gameType = BoardGameType.LADDER;
    String fileName = "parserIssue";

    BoardWritingException writingException = new BoardWritingException("Write error");
    when(mockBoardParser.serializeBoard(mockBoard)).thenThrow(writingException);

    BoardFileException e =
        assertThrows(
            BoardFileException.class,
            () -> boardFileService.saveBoard(gameType, fileName, mockBoard));
    assertTrue(e.getMessage().contains("Failed to save board"));
    assertSame(writingException, e.getCause());
  }

  @Test
  void listAvailableBoards_nullGameType_returnsEmptyList() {
    List<String> boards = boardFileService.listAvailableBoards(null);
    assertTrue(boards.isEmpty());
  }
}
