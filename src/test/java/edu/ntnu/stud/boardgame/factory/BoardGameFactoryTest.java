package edu.ntnu.stud.boardgame.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.model.game.LadderGame;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("BoardGameFactory Tests")
class BoardGameFactoryTest {

  private BoardGameFactory factory;

  @Mock
  private BoardFileService mockBoardFileService;

  @Mock
  private Board mockBoard;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new BoardGameFactory(mockBoardFileService);
  }

  @Test
  @DisplayName("getAvailableGameBoards should list available boards including Default")
  void getAvailableGameBoards_anyGameType_returnsListWithDefault() {
    List<String> mockBoardList = List.of("Board1", "Board2");
    when(mockBoardFileService.listAvailableBoards(BoardGameType.LADDER)).thenReturn(mockBoardList);

    List<String> result = factory.getAvailableGameBoards(BoardGameType.LADDER);

    assertTrue(result.contains("Default"));
    assertTrue(result.contains("Board1"));
    assertTrue(result.contains("Board2"));
    assertEquals(3, result.size());
  }

  @Test
  @DisplayName("loadGameFromFile should create different game types based on type parameter")
  void loadGameFromFile_differentGameTypes_createsCorrectGameType() throws BoardFileException {
    BoardGame ladderGame = factory.loadGameFromFile(BoardGameType.LADDER, "Default");
    BoardGame monopolyGame = factory.loadGameFromFile(BoardGameType.MONOPOLY, "Default");

    assertTrue(ladderGame instanceof LadderGame);
    assertTrue(monopolyGame instanceof MonopolyGame);
  }

  @Nested
  @DisplayName("Game Creation Tests")
  class GameCreationTests {

    @Test
    @DisplayName("loadGameFromFile with 'Default' should create default board")
    void loadGameFromFile_defaultBoard_createsDefaultBoard() throws BoardFileException {
      BoardGame game = factory.loadGameFromFile(BoardGameType.LADDER, "Default");

      assertNotNull(game);
      assertTrue(game instanceof LadderGame);
      assertNotNull(game.getBoard());
      assertNotNull(game.getDice());
    }

    @Test
    @DisplayName("loadGameFromFile with custom board name should load from service")
    void loadGameFromFile_customBoard_loadsFromService() throws BoardFileException {
      when(mockBoardFileService.loadBoard(BoardGameType.LADDER, "CustomBoard")).thenReturn(
          mockBoard);

      BoardGame game = factory.loadGameFromFile(BoardGameType.LADDER, "CustomBoard");

      assertNotNull(game);
      assertTrue(game instanceof LadderGame);
      assertEquals(mockBoard, game.getBoard());
      assertNotNull(game.getDice());
      verify(mockBoardFileService).loadBoard(BoardGameType.LADDER, "CustomBoard");
    }

    @Test
    @DisplayName("loadGameFromFile with board loading error should throw exception")
    void loadGameFromFile_boardLoadError_throwsException() throws BoardFileException {
      when(mockBoardFileService.loadBoard(BoardGameType.LADDER, "ErrorBoard"))
          .thenThrow(new BoardFileException("Test error"));

      assertThrows(BoardFileException.class, () -> {
        factory.loadGameFromFile(BoardGameType.LADDER, "ErrorBoard");
      });
    }
  }
}