package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("BoardGameFacade Tests")
class BoardGameFacadeTest {

  private BoardGameFacade facade;

  @Mock private BoardGameFactory mockFactory;

  @Mock private BoardGame mockGame;

  @Mock private BoardGameObserver mockObserver;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    facade = new BoardGameFacade();
    java.lang.reflect.Field factoryField = BoardGameFacade.class.getDeclaredField("factory");
    factoryField.setAccessible(true);
    factoryField.set(facade, mockFactory);

    when(mockFactory.createGame(any(), any())).thenReturn(mockGame);
    when(mockFactory.getAvailableGameBoards(any())).thenReturn(List.of("Default", "Custom"));
  }

  @Test
  @DisplayName("registerObserver should register observer with game")
  void registerObserver_anyObserver_registersWithGame() throws Exception {
    facade.setCurrentGameType(BoardGameType.LADDER);
    facade.createGame("TestBoard");

    facade.registerObserver(mockObserver);

    verify(mockGame).registerObserver(mockObserver);
  }

  @Nested
  @DisplayName("Game Creation Tests")
  class GameCreationTests {

    @Test
    @DisplayName("createGame without game type should throw exception")
    void createGame_noGameType_throwsException() {
      assertThrows(
          InvalidGameStateException.class,
          () -> {
            facade.createGame("TestBoard");
          });
    }

    @Test
    @DisplayName("createGame with valid parameters should create game")
    void createGame_validParameters_createsGame() throws Exception {
      facade.setCurrentGameType(BoardGameType.LADDER);

      facade.createGame("TestBoard");

      verify(mockFactory).createGame(BoardGameType.LADDER, "TestBoard");
      assertEquals(mockGame, facade.getCurrentGame());
    }
  }

  @Nested
  @DisplayName("Game Operation Tests")
  class GameOperationTests {

    @Test
    @DisplayName("startGame without created game should throw exception")
    void startGame_noCreatedGame_throwsException() {
      assertThrows(
          BoardGameException.class,
          () -> {
            facade.startGame();
          });
    }

    @Test
    @DisplayName("startGame with created game should start game")
    void startGame_withCreatedGame_startsGame() throws Exception {
      facade.setCurrentGameType(BoardGameType.LADDER);
      facade.createGame("TestBoard");

      facade.startGame();

      verify(mockGame).startGame();
    }

    @Test
    @DisplayName("addPlayer without created game should throw exception")
    void addPlayer_noCreatedGame_throwsException() {
      assertThrows(
          BoardGameException.class,
          () -> {
            facade.addPlayer("Player", PieceType.RED);
          });
    }

    @Test
    @DisplayName("addPlayer with created game should add player")
    void addPlayer_withCreatedGame_addsPlayer() throws Exception {
      facade.setCurrentGameType(BoardGameType.LADDER);
      facade.createGame("TestBoard");

      facade.addPlayer("Player", PieceType.RED);

      verify(mockGame).addPlayer(any(Player.class));
    }
  }

  @Nested
  @DisplayName("Turn Management Tests")
  class TurnManagementTests {

    @Test
    @DisplayName("playTurn without created game should throw exception")
    void playTurn_noCreatedGame_throwsException() {
      assertThrows(
          BoardGameException.class,
          () -> {
            facade.playTurn();
          });
    }

    @Test
    @DisplayName("playTurn with game over should throw exception")
    void playTurn_gameOver_throwsException() throws Exception {
      facade.setCurrentGameType(BoardGameType.LADDER);
      facade.createGame("TestBoard");
      when(mockGame.isGameOver()).thenReturn(true);

      assertThrows(
          BoardGameException.class,
          () -> {
            facade.playTurn();
          });
    }

    @Test
    @DisplayName("playTurn with active game should play turn and advance")
    void playTurn_activeGame_playsTurnAndAdvances() throws Exception {
      facade.setCurrentGameType(BoardGameType.LADDER);
      facade.createGame("TestBoard");
      when(mockGame.isGameOver()).thenReturn(false);

      facade.playTurn();

      verify(mockGame).playTurn();
      verify(mockGame).nextTurn();
    }
  }

  @Nested
  @DisplayName("Board Management Tests")
  class BoardManagementTests {

    @Test
    @DisplayName("getAvailableGameBoards without game type should throw exception")
    void getAvailableGameBoards_noGameType_throwsException() {
      assertThrows(
          InvalidGameStateException.class,
          () -> {
            facade.getAvailableGameBoards();
          });
    }

    @Test
    @DisplayName("getAvailableGameBoards with game type should return boards list")
    void getAvailableGameBoards_withGameType_returnsBoardsList() {
      facade.setCurrentGameType(BoardGameType.LADDER);

      List<String> boards = facade.getAvailableGameBoards();

      assertEquals(2, boards.size());
      assertTrue(boards.contains("Default"));
      assertTrue(boards.contains("Custom"));
      verify(mockFactory).getAvailableGameBoards(BoardGameType.LADDER);
    }
  }
}
