package edu.ntnu.stud.boardgame.factory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.model.game.LadderGame;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardGameFactoryTest {

  @Mock private BoardFileService mockBoardFileService;

  private BoardGameFactory boardGameFactory;

  @BeforeEach
  void setUp() {
    boardGameFactory = new BoardGameFactory(mockBoardFileService);
  }

  @Test
  void constructor_nullService_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new BoardGameFactory(null));
  }

  @Test
  void createGame_ladderGame_predefined_returnsLadderGame() throws BoardFileException {
    Board mockBoard = mock(Board.class);
    try (MockedStatic<LadderGameBoardFactory> mockedFactory =
        Mockito.mockStatic(LadderGameBoardFactory.class)) {
      mockedFactory.when(() -> LadderGameBoardFactory.createBoard("Default")).thenReturn(mockBoard);

      BoardGame game = boardGameFactory.createGame(BoardGameType.LADDER, "Predefined:Default");

      assertNotNull(game);
      assertInstanceOf(LadderGame.class, game);
      assertEquals(mockBoard, game.getBoard());
      assertNotNull(game.getDice());
      mockedFactory.verify(() -> LadderGameBoardFactory.createBoard("Default"));
    }
  }

  @Test
  void createGame_monopolyGame_fromFile_returnsMonopolyGame() throws BoardFileException {
    Board mockBoard = mock(Board.class);
    when(mockBoardFileService.loadBoard(BoardGameType.MONOPOLY, "MyMonopolyBoard"))
        .thenReturn(mockBoard);

    BoardGame game = boardGameFactory.createGame(BoardGameType.MONOPOLY, "MyMonopolyBoard");

    assertNotNull(game);
    assertInstanceOf(MonopolyGame.class, game);
    assertEquals(mockBoard, game.getBoard());
    assertNotNull(game.getDice());
    verify(mockBoardFileService).loadBoard(BoardGameType.MONOPOLY, "MyMonopolyBoard");
  }

  @Test
  void createGame_nullType_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> boardGameFactory.createGame(null, "boardName"));
  }

  @Test
  void createGame_nullBoardName_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> boardGameFactory.createGame(BoardGameType.LADDER, null));
  }

  @Test
  void createGame_emptyBoardName_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> boardGameFactory.createGame(BoardGameType.LADDER, "  "));
  }

  @Test
  void createGame_fileServiceThrowsException_propagatesException() throws BoardFileException {
    when(mockBoardFileService.loadBoard(BoardGameType.LADDER, "FaultyBoard"))
        .thenThrow(new BoardFileException("Failed to load"));
    assertThrows(
        BoardFileException.class,
        () -> boardGameFactory.createGame(BoardGameType.LADDER, "FaultyBoard"));
  }

  @Test
  void getAvailableGameBoards_ladder_combinesPredefinedAndFileBoards() {
    List<String> predefined = List.of("LadderPredefined1");
    List<String> fromFiles = List.of("LadderFile1", "LadderFile2");

    try (MockedStatic<LadderGameBoardFactory> mockedFactory =
        Mockito.mockStatic(LadderGameBoardFactory.class)) {
      mockedFactory.when(LadderGameBoardFactory::getAvailableBoards).thenReturn(predefined);
      when(mockBoardFileService.listAvailableBoards(BoardGameType.LADDER)).thenReturn(fromFiles);

      List<String> result = boardGameFactory.getAvailableGameBoards(BoardGameType.LADDER);

      assertEquals(3, result.size());
      assertTrue(result.contains("Predefined:LadderPredefined1"));
      assertTrue(result.contains("LadderFile1"));
      assertTrue(result.contains("LadderFile2"));
    }
  }

  @Test
  void getAvailableGameBoards_monopoly_combinesPredefinedAndFileBoards() {
    List<String> predefined = List.of("MonopolyPredefined1");
    List<String> fromFiles = List.of("MonopolyFile1");

    try (MockedStatic<MonopolyBoardFactory> mockedFactory =
        Mockito.mockStatic(MonopolyBoardFactory.class)) {
      mockedFactory.when(MonopolyBoardFactory::getAvailableBoards).thenReturn(predefined);
      when(mockBoardFileService.listAvailableBoards(BoardGameType.MONOPOLY)).thenReturn(fromFiles);

      List<String> result = boardGameFactory.getAvailableGameBoards(BoardGameType.MONOPOLY);

      assertEquals(2, result.size());
      assertTrue(result.contains("Predefined:MonopolyPredefined1"));
      assertTrue(result.contains("MonopolyFile1"));
    }
  }

  @Test
  void getAvailableGameBoards_nullGameType_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> boardGameFactory.getAvailableGameBoards(null));
  }
}
