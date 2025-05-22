package edu.ntnu.stud.boardgame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.io.board.BoardFileReader;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriter;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardFileServiceTest {

  @TempDir
  Path tempDir;
  @Mock
  private BoardFileReader mockBoardReader;
  @Mock
  private BoardFileWriter mockBoardWriter;
  @Mock
  private Board mockBoard;
  private BoardFileService boardFileService;
  private Path testBoardsBaseDir;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
    boardFileService = BoardFileService.getInstance();

    // Fix the field names to match actual class
    java.lang.reflect.Field readerField = BoardFileService.class.getDeclaredField("boardReader");
    readerField.setAccessible(true);
    readerField.set(boardFileService, mockBoardReader);

    java.lang.reflect.Field writerField = BoardFileService.class.getDeclaredField("boardWriter");
    writerField.setAccessible(true);
    writerField.set(boardFileService, mockBoardWriter);

    // For the BOARDS_BASE_DIRECTORY field, use the correct name and handle the final modifier
    testBoardsBaseDir = tempDir.resolve("data").resolve("boards");
    Files.createDirectories(testBoardsBaseDir);

    java.lang.reflect.Field dirField = BoardFileService.class.getDeclaredField(
        "BOARDS_BASE_DIRECTORY");
    dirField.setAccessible(true);

    // Remove the final modifier
    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField(
        "modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(dirField, dirField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    dirField.set(null, testBoardsBaseDir); // null because it's a static field
  }

  private Path getExpectedBoardPath(BoardGameType gameType, String fileName) {
    return testBoardsBaseDir.resolve(gameType.name().toLowerCase()).resolve(fileName + ".json");
  }

  @Test
  void getInstance_returnsSameInstance() {
    BoardFileService instance1 = BoardFileService.getInstance();
    BoardFileService instance2 = BoardFileService.getInstance();
    assertSame(instance1, instance2);
  }

  @Test
  void loadBoard_validFile_returnsBoard()
      throws BoardFileException, BoardParsingException, IOException {
    BoardGameType gameType = BoardGameType.LADDER;
    String fileName = "testBoard";
    Path expectedPath = getExpectedBoardPath(gameType, fileName);
    Files.createFile(expectedPath);

    when(mockBoardReader.readBoard(expectedPath)).thenReturn(mockBoard);

    Board result = boardFileService.loadBoard(gameType, fileName);

    assertSame(mockBoard, result);
    verify(mockBoardReader).readBoard(expectedPath);
  }

  @Test
  void loadBoard_nullGameType_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class,
        () -> boardFileService.loadBoard(null, "file"));
    assertEquals("Game type cannot be null.", e.getMessage());
  }

  @Test
  void loadBoard_nullFileName_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class,
        () -> boardFileService.loadBoard(BoardGameType.LADDER, null));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void loadBoard_emptyFileName_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class,
        () -> boardFileService.loadBoard(BoardGameType.LADDER, ""));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void loadBoard_fileDoesNotExist_throwsBoardFileException() {
    BoardGameType gameType = BoardGameType.MONOPOLY;
    String fileName = "nonExistent";
    Path expectedPath = getExpectedBoardPath(gameType, fileName);

    BoardFileException e = assertThrows(BoardFileException.class,
        () -> boardFileService.loadBoard(gameType, fileName));
    assertTrue(e.getMessage().contains("Board file does not exist: " + expectedPath));
  }

  @Test
  void loadBoard_readerThrowsException_throwsBoardFileException()
      throws BoardParsingException, IOException {
    BoardGameType gameType = BoardGameType.LADDER;
    String fileName = "readerIssue";
    Path expectedPath = getExpectedBoardPath(gameType, fileName);
    Files.createFile(expectedPath);

    BoardParsingException parsingException = new BoardParsingException("Parse error");
    when(mockBoardReader.readBoard(expectedPath)).thenThrow(parsingException);

    BoardFileException e = assertThrows(BoardFileException.class,
        () -> boardFileService.loadBoard(gameType, fileName));
    assertEquals("Failed to load board: Parse error", e.getMessage());
    assertSame(parsingException, e.getCause());
  }

  @Test
  void saveBoard_validInput_savesSuccessfully() throws BoardFileException, BoardWritingException {
    BoardGameType gameType = BoardGameType.MONOPOLY;
    String fileName = "saveTestBoard";
    Path expectedPath = getExpectedBoardPath(gameType, fileName);

    boardFileService.saveBoard(gameType, fileName, mockBoard);

    verify(mockBoardWriter).writeBoard(expectedPath, mockBoard);
  }

  @Test
  void saveBoard_nullGameType_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class,
        () -> boardFileService.saveBoard(null, "file", mockBoard));
    assertEquals("Game type cannot be null.", e.getMessage());
  }

  @Test
  void saveBoard_nullFileName_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class,
        () -> boardFileService.saveBoard(BoardGameType.LADDER, null, mockBoard));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void saveBoard_emptyFileName_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class,
        () -> boardFileService.saveBoard(BoardGameType.LADDER, "", mockBoard));
    assertEquals("File name cannot be null or empty.", e.getMessage());
  }

  @Test
  void saveBoard_nullBoard_throwsIllegalArgumentException() {
    Exception e = assertThrows(IllegalArgumentException.class,
        () -> boardFileService.saveBoard(BoardGameType.MONOPOLY, "file", null));
    assertEquals("Board cannot be null.", e.getMessage());
  }

  @Test
  void saveBoard_writerThrowsException_throwsBoardFileException()
      throws BoardWritingException, IOException {
    BoardGameType gameType = BoardGameType.LADDER;
    String fileName = "writerIssue";
    Path expectedPath = getExpectedBoardPath(gameType, fileName);

    BoardWritingException writingException = new BoardWritingException("Write error");
    doThrow(writingException).when(mockBoardWriter).writeBoard(expectedPath, mockBoard);

    BoardFileException e = assertThrows(BoardFileException.class,
        () -> boardFileService.saveBoard(gameType, fileName, mockBoard));
    assertEquals("Failed to save board: Write error", e.getMessage());
    assertSame(writingException, e.getCause());
  }

  @Test
  void listAvailableBoards_nullGameType_returnsEmptyList() {
    List<String> boards = boardFileService.listAvailableBoards(null);
    assertTrue(boards.isEmpty());
  }

  @Test
  void listAvailableBoards_noJsonFiles_returnsEmptyList() throws IOException {
    BoardGameType gameType = BoardGameType.LADDER;
    Path gameTypeDir = testBoardsBaseDir.resolve(gameType.name().toLowerCase());
    Files.createFile(gameTypeDir.resolve("somefile.txt"));

    List<String> boards = boardFileService.listAvailableBoards(gameType);
    assertTrue(boards.isEmpty());
  }

  @Test
  void listAvailableBoards_withJsonFiles_returnsNamesWithoutExtension() throws IOException {
    BoardGameType gameType = BoardGameType.MONOPOLY;
    Path gameTypeDir = testBoardsBaseDir.resolve(gameType.name().toLowerCase());

    Files.createFile(gameTypeDir.resolve("boardA.json"));
    Files.createFile(gameTypeDir.resolve("boardB.JSON"));
    Files.createFile(gameTypeDir.resolve("boardC.txt"));

    List<String> expectedNames = new java.util.ArrayList<>(List.of("boardA", "boardB"));
    List<String> actualNames = boardFileService.listAvailableBoards(gameType);

    Collections.sort(expectedNames);
    Collections.sort(actualNames);
    assertEquals(expectedNames, actualNames);
  }

  @Test
  void listAvailableBoards_gameTypeDirectoryDoesNotExist_returnsEmptyList() throws IOException {
    Path ladderDir = testBoardsBaseDir.resolve(BoardGameType.LADDER.name().toLowerCase());
    try (var stream = Files.list(ladderDir)) {
      stream.forEach(p -> {
        try {
          Files.delete(p);
        } catch (IOException ignored) {
        }
      });
    }
    Files.delete(ladderDir);

    List<String> boards = boardFileService.listAvailableBoards(BoardGameType.LADDER);
    assertTrue(boards.isEmpty());
  }

  @Test
  void ensureFileExtension_addsJsonIfNotPresent() throws BoardFileException, BoardWritingException {
    BoardGameType gameType = BoardGameType.LADDER;
    String fileNameWithoutExt = "myBoard";
    String fileNameWithExt = "myBoard.json";
    Path expectedPathWithExt = getExpectedBoardPath(gameType, fileNameWithoutExt);

    BoardFileException loadException = assertThrows(BoardFileException.class, () -> {
      boardFileService.loadBoard(gameType, fileNameWithoutExt);
    });
    assertTrue(loadException.getMessage().contains(fileNameWithExt));

    boardFileService.saveBoard(gameType, fileNameWithoutExt, mockBoard);
    verify(mockBoardWriter).writeBoard(expectedPathWithExt, mockBoard);

    reset(mockBoardWriter);
    boardFileService.saveBoard(gameType, fileNameWithExt, mockBoard);
    verify(mockBoardWriter).writeBoard(expectedPathWithExt, mockBoard);

    reset(mockBoardWriter);
    String fileNameUpperJson = "myBoardUpper.JSON";
    Path expectedPathUpperJson = testBoardsBaseDir.resolve(gameType.name().toLowerCase())
        .resolve(fileNameUpperJson);
    boardFileService.saveBoard(gameType, fileNameUpperJson, mockBoard);
    verify(mockBoardWriter).writeBoard(expectedPathUpperJson, mockBoard);
  }

  @Test
  void constructorAndGetGameTypeDirectory_createsDirectories() {
    assertTrue(Files.exists(testBoardsBaseDir));
    assertTrue(Files.isDirectory(testBoardsBaseDir));

    Path ladderDir = testBoardsBaseDir.resolve(BoardGameType.LADDER.name().toLowerCase());
    assertTrue(Files.exists(ladderDir));
    assertTrue(Files.isDirectory(ladderDir));

    boardFileService.listAvailableBoards(BoardGameType.MONOPOLY);
    Path monopolyDir = testBoardsBaseDir.resolve(BoardGameType.MONOPOLY.name().toLowerCase());
    assertTrue(Files.exists(monopolyDir));
    assertTrue(Files.isDirectory(monopolyDir));
  }
}