package edu.ntnu.stud.boardgame.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
import edu.ntnu.stud.boardgame.exception.files.PlayerFileException;
import edu.ntnu.stud.boardgame.model.BoardGameFacade;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.service.PlayerFileService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("GameController Tests")
class GameControllerTest {

  private GameController gameController;

  @Mock
  private MainController mockMainController;

  @Mock
  private BoardGameFacade mockGameFacade;

  @Mock
  private PlayerFileService mockPlayerFileService;

  @Mock
  private BoardGame mockGame;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    gameController = new GameController(mockMainController);

    java.lang.reflect.Field facadeField = GameController.class.getDeclaredField("gameFacade");
    facadeField.setAccessible(true);
    facadeField.set(gameController, mockGameFacade);

    java.lang.reflect.Field serviceField = GameController.class.getDeclaredField(
        "playerFileService");
    serviceField.setAccessible(true);
    serviceField.set(gameController, mockPlayerFileService);

    when(mockGameFacade.getCurrentGame()).thenReturn(mockGame);
  }

  @Test
  @DisplayName("selectGameType should set type and show board selection")
  void selectGameType_validType_setsTypeAndShowsBoardSelection() {
    gameController.selectGameType(BoardGameType.LADDER);

    verify(mockGameFacade).setCurrentGameType(BoardGameType.LADDER);
    verify(mockMainController).showBoardSelectionView();
  }

  @Nested
  @DisplayName("Board Selection Tests")
  class BoardSelectionTests {

    @Test
    @DisplayName("getAvailableBoards should return boards from facade")
    void getAvailableBoards_facadeReturnsBoards_returnsSameBoards() {
      List<String> expectedBoards = List.of("Default", "Custom");
      when(mockGameFacade.getAvailableGameBoards()).thenReturn(expectedBoards);

      List<String> result = gameController.getAvailableBoards();

      assertEquals(expectedBoards, result);
      verify(mockGameFacade).getAvailableGameBoards();
    }

    @Test
    @DisplayName("getAvailableBoards with error should return default list")
    void getAvailableBoards_facadeThrowsError_returnsDefaultList() {
      when(mockGameFacade.getAvailableGameBoards()).thenThrow(new RuntimeException("Test error"));

      List<String> result = gameController.getAvailableBoards();

      assertEquals(1, result.size());
      assertEquals("Default", result.get(0));
      verify(mockMainController).showErrorDialog(anyString(), anyString());
    }

    @Test
    @DisplayName("selectBoard should create game and show player setup view")
    void selectBoard_validBoard_createsGameAndShowsPlayerSetup() throws Exception {
      boolean result = gameController.selectBoard("TestBoard");

      assertTrue(result);
      verify(mockGameFacade).createGame("TestBoard");
      verify(mockMainController).showPlayerSetupView();
    }

    @Test
    @DisplayName("selectBoard with error should show error dialog")
    void selectBoard_createGameThrowsError_showsErrorDialog() throws Exception {
      doThrow(new BoardGameException("Test error")).when(mockGameFacade).createGame(anyString());

      boolean result = gameController.selectBoard("TestBoard");

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
    }
  }

  @Nested
  @DisplayName("Player Management Tests")
  class PlayerManagementTests {

    @Test
    @DisplayName("addPlayer should add player to game")
    void addPlayer_validPlayerData_addsPlayerToGame() throws Exception {
      when(mockGame.getPlayers()).thenReturn(new ArrayList<>());

      boolean result = gameController.addPlayer("Player", PieceType.RED);

      assertTrue(result);
      verify(mockGameFacade).addPlayer("Player", PieceType.RED);
    }

    @Test
    @DisplayName("addPlayer with duplicate piece should show error")
    void addPlayer_duplicatePiece_showsErrorDialog() throws Exception {
      Player existingPlayer = new Player("ExistingPlayer", PieceType.RED);
      when(mockGame.getPlayers()).thenReturn(List.of(existingPlayer));

      boolean result = gameController.addPlayer("NewPlayer", PieceType.RED);

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
      verify(mockGameFacade, never()).addPlayer(anyString(), any(PieceType.class));
    }

    @Test
    @DisplayName("addPlayer with facade error should show error dialog")
    void addPlayer_facadeThrowsError_showsErrorDialog() throws Exception {
      when(mockGame.getPlayers()).thenReturn(new ArrayList<>());
      doThrow(new BoardGameException("Test error")).when(mockGameFacade)
          .addPlayer(anyString(), any(PieceType.class));

      boolean result = gameController.addPlayer("Player", PieceType.RED);

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
    }

    @Test
    @DisplayName("savePlayers should save current player list")
    void savePlayers_withPlayers_savesPlayerList() throws Exception {
      List<Player> players = List.of(new Player("Player1", PieceType.RED));
      when(mockGame.getPlayers()).thenReturn(players);

      boolean result = gameController.savePlayers("testFile");

      assertTrue(result);
      verify(mockPlayerFileService).savePlayers("testFile", players);
      verify(mockMainController).showInfoDialog(anyString(), anyString());
    }

    @Test
    @DisplayName("savePlayers with empty list should show error")
    void savePlayers_emptyPlayerList_showsErrorDialog() throws Exception {
      when(mockGame.getPlayers()).thenReturn(new ArrayList<>());

      boolean result = gameController.savePlayers("testFile");

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
      verify(mockPlayerFileService, never()).savePlayers(anyString(), anyList());
    }

    @Test
    @DisplayName("savePlayers with service error should show error dialog")
    void savePlayers_serviceThrowsError_showsErrorDialog() throws Exception {
      List<Player> players = List.of(new Player("Player1", PieceType.RED));
      when(mockGame.getPlayers()).thenReturn(players);
      doThrow(new PlayerFileException("Test error")).when(mockPlayerFileService)
          .savePlayers(anyString(), anyList());

      boolean result = gameController.savePlayers("testFile");

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
    }

    @Test
    @DisplayName("loadPlayers should load and add players from file")
    void loadPlayers_validFile_loadsAndAddsPlayers() throws Exception {
      List<Player> loadedPlayers = List.of(
          new Player("Player1", PieceType.RED),
          new Player("Player2", PieceType.BLUE)
      );
      when(mockPlayerFileService.loadPlayers("testFile")).thenReturn(loadedPlayers);

      boolean result = gameController.loadPlayers("testFile");

      assertTrue(result);
      verify(mockPlayerFileService).loadPlayers("testFile");
      verify(mockGameFacade, times(2)).addPlayer(anyString(), any(PieceType.class));
    }

    @Test
    @DisplayName("loadPlayers with service error should show error dialog")
    void loadPlayers_serviceThrowsError_showsErrorDialog() throws Exception {
      when(mockPlayerFileService.loadPlayers(anyString())).thenThrow(
          new PlayerFileException("Test error"));

      boolean result = gameController.loadPlayers("testFile");

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
    }
  }

  @Nested
  @DisplayName("Game Flow Tests")
  class GameFlowTests {

    @Test
    @DisplayName("startGame with enough players should start game")
    void startGame_enoughPlayers_startsGame() throws Exception {
      List<Player> players = List.of(
          new Player("Player1", PieceType.RED),
          new Player("Player2", PieceType.BLUE)
      );
      when(mockGame.getPlayers()).thenReturn(players);

      boolean result = gameController.startGame();

      assertTrue(result);
      verify(mockGameFacade).startGame();
    }

    @Test
    @DisplayName("startGame with not enough players should show error")
    void startGame_notEnoughPlayers_showsErrorDialog() throws Exception {
      List<Player> players = List.of(new Player("Player1", PieceType.RED));
      when(mockGame.getPlayers()).thenReturn(players);

      boolean result = gameController.startGame();

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
      verify(mockGameFacade, never()).startGame();
    }

    @Test
    @DisplayName("startGame with facade error should show error dialog")
    void startGame_facadeThrowsError_showsErrorDialog() throws Exception {
      List<Player> players = List.of(
          new Player("Player1", PieceType.RED),
          new Player("Player2", PieceType.BLUE)
      );
      when(mockGame.getPlayers()).thenReturn(players);
      doThrow(new BoardGameException("Test error")).when(mockGameFacade).startGame();

      boolean result = gameController.startGame();

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
    }

    @Test
    @DisplayName("playTurn should play turn in facade")
    void playTurn_validGame_playsGameTurn() throws Exception {
      boolean result = gameController.playTurn();

      assertTrue(result);
      verify(mockGameFacade).playTurn();
    }

    @Test
    @DisplayName("playTurn with facade error should show error dialog")
    void playTurn_facadeThrowsError_showsErrorDialog() throws Exception {
      doThrow(new BoardGameException("Test error")).when(mockGameFacade).playTurn();

      boolean result = gameController.playTurn();

      assertFalse(result);
      verify(mockMainController).showErrorDialog(anyString(), anyString());
    }
  }
}