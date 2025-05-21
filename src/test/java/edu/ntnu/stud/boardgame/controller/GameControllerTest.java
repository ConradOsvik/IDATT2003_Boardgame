package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.BoardGameFacade;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.service.PlayerFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

  @Mock
  private MainController mainController;

  @Mock
  private BoardGameFacade gameFacade;

  // This mock might not be used if GameController creates its own instance via
  // PlayerFileService.getInstance()
  // This is a known issue addressed in the comments within tests for save/load
  // players.
  @Mock
  private PlayerFileService playerFileServiceMock;

  @Mock
  private BoardGame mockGame;

  private GameController gameController;

  // This field is to hold the actual PlayerFileService instance that
  // GameController creates.
  // We will use this to verify interactions if direct mocking via @Mock doesn't
  // work due to getInstance().
  // However, for proper unit testing, GameController should allow injection.
  // For now, tests that interact with playerFileService will try to verify on
  // playerFileServiceMock,
  // acknowledging this limitation.

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    // Because GameController internally news up BoardGameFacade and calls
    // PlayerFileService.getInstance(),
    // the @InjectMocks won't fully replace these unless the GameController is
    // designed for it (e.g. constructor injection).
    // For gameFacade, it's newed up in GameController constructor. We'd ideally
    // inject it.
    // For playerFileService, it's obtained via getInstance().

    // To properly mock gameFacade, we would need to change GameController's
    // constructor or add a setter.
    // For now, methods in GameController that use gameFacade will be tested
    // assuming we can control gameFacade's behavior via when/thenReturn.

    gameController = new GameController(mainController, gameFacade, playerFileServiceMock);
    when(gameFacade.getCurrentGame()).thenReturn(mockGame);
    // For methods directly calling `this.playerFileService.someMethod()` where
    // `this.playerFileService` was set by `PlayerFileService.getInstance()`,
    // `playerFileServiceMock` will not be used. This is a limitation of the current
    // GameController design for testability.
    // The tests for savePlayers/loadPlayers will reflect this challenge.
  }

  @Test
  void selectGameType_validType_setsTypeAndShowsBoardSelection() {
    BoardGameType gameType = BoardGameType.LADDER;
    gameController.selectGameType(gameType);
    verify(gameFacade).setCurrentGameType(gameType);
    verify(mainController).showBoardSelectionView();
  }

  @Test
  void selectGameType_nullType_showsError() {
    gameController.selectGameType(null);
    verify(mainController).showErrorDialog(eq("Game Type Error"), eq("Game type cannot be null."));
    verify(gameFacade, never()).setCurrentGameType(any());
    verify(mainController, never()).showBoardSelectionView();
  }

  @Test
  void getAvailableBoards_facadeReturnsBoards_returnsBoards() {
    List<String> expectedBoards = List.of("Board1", "Board2");
    when(gameFacade.getAvailableGameBoards()).thenReturn(expectedBoards);
    List<String> actualBoards = gameController.getAvailableBoards();
    assertEquals(expectedBoards, actualBoards);
  }

  @Test
  void getAvailableBoards_facadeThrowsException_showsErrorAndReturnsDefault() {
    when(gameFacade.getAvailableGameBoards()).thenThrow(new RuntimeException("Test Exception"));
    List<String> actualBoards = gameController.getAvailableBoards();
    verify(mainController).showErrorDialog(eq("Board List Error"),
        eq("Failed to get available boards: Test Exception"));
    assertEquals(List.of("Default"), actualBoards);
  }

  @Test
  void saveSelectedBoardAs_validInput_createsAndSavesBoard()
      throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    String selectedBoard = "OriginalBoard";
    String newName = "NewBoardName";
    when(gameFacade.getCurrentGameType()).thenReturn(BoardGameType.LADDER);
    assertTrue(gameController.saveSelectedBoardAs(selectedBoard, newName));
    verify(gameFacade).createGame(selectedBoard);
    verify(gameFacade).saveCurrentBoard(newName);
    verify(mainController).showInfoDialog(eq("Success"), eq("Board saved successfully as: " + newName));
  }

  @Test
  void saveSelectedBoardAs_nullBoardName_showsError() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    when(gameFacade.getCurrentGameType()).thenReturn(BoardGameType.LADDER); // Avoid NPE on getCurrentGameType
    assertFalse(gameController.saveSelectedBoardAs(null, "NewName"));
    verify(mainController).showErrorDialog(eq("Save Error"), eq("Board name cannot be empty."));
  }

  @Test
  void saveSelectedBoardAs_emptyNewName_showsError() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    when(gameFacade.getCurrentGameType()).thenReturn(BoardGameType.LADDER);
    assertFalse(gameController.saveSelectedBoardAs("Board", " "));
    verify(mainController).showErrorDialog(eq("Save Error"), eq("New board name cannot be empty."));
  }

  @Test
  void saveSelectedBoardAs_noGameTypeSelected_showsError() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    when(gameFacade.getCurrentGameType()).thenReturn(null);
    assertFalse(gameController.saveSelectedBoardAs("Board", "NewName"));
    verify(mainController).showErrorDialog(eq("Save Error"), eq("No game type selected."));
  }

  @Test
  void selectBoard_validName_createsGameAndShowsPlayerSetup()
      throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    String boardName = "TestBoard";
    assertTrue(gameController.selectBoard(boardName));
    verify(gameFacade).createGame(boardName);
    verify(mainController).showPlayerSetupView();
  }

  @Test
  void selectBoard_emptyName_showsError() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    assertFalse(gameController.selectBoard(""));
    verify(mainController).showErrorDialog(eq("Board Selection Error"), eq("Board name cannot be empty."));
  }

  @Test
  void addPlayer_validPlayer_addsPlayer() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    when(mockGame.getPlayers()).thenReturn(List.of());
    assertTrue(gameController.addPlayer("Player1", PieceType.RED));
    verify(gameFacade).addPlayer("Player1", PieceType.RED);
  }

  @Test
  void addPlayer_emptyName_showsError() {
    assertFalse(gameController.addPlayer("", PieceType.RED));
    verify(mainController).showErrorDialog(eq("Input Error"), eq("Player name cannot be empty."));
  }

  @Test
  void addPlayer_nullPieceType_showsError() {
    assertFalse(gameController.addPlayer("Player1", null));
    verify(mainController).showErrorDialog(eq("Input Error"), eq("Piece type cannot be null."));
  }

  @Test
  void addPlayer_pieceInUse_showsError() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    Player existingPlayer = mock(Player.class);
    when(existingPlayer.getPiece()).thenReturn(PieceType.RED);
    when(mockGame.getPlayers()).thenReturn(List.of(existingPlayer));
    assertFalse(gameController.addPlayer("Player2", PieceType.RED));
    verify(mainController).showErrorDialog(eq("Input Error"),
        eq("The piece " + PieceType.RED + " is already in use by another player."));
  }

  // Tests for savePlayers and loadPlayers are challenging due to
  // PlayerFileService.getInstance()
  // The following tests assume that playerFileServiceMock *could* be used.
  // If GameController is not refactored for PlayerFileService injection, these
  // tests might not truly mock file I/O.
  @Test
  void savePlayers_validFileNameAndPlayers_attemptsSave()
      throws edu.ntnu.stud.boardgame.exception.files.PlayerFileException {
    Player player1 = new Player("Alice", PieceType.RED);
    when(mockGame.getPlayers()).thenReturn(List.of(player1));
    String fileName = "testPlayers";
    gameController.savePlayers(fileName);
    verify(playerFileServiceMock).savePlayers(eq(fileName), anyList());
    verify(mainController).showInfoDialog(eq("Success"), eq("Players saved successfully to " + fileName + ".csv"));
  }

  @Test
  void savePlayers_emptyFileName_showsError() throws edu.ntnu.stud.boardgame.exception.files.PlayerFileException {
    when(mockGame.getPlayers()).thenReturn(List.of(mock(Player.class)));
    assertFalse(gameController.savePlayers(""));
    verify(mainController).showErrorDialog(eq("Save Error"), eq("File name cannot be empty."));
    verify(playerFileServiceMock, never()).savePlayers(anyString(), anyList());
  }

  @Test
  void savePlayers_noPlayers_showsError() throws edu.ntnu.stud.boardgame.exception.files.PlayerFileException {
    when(mockGame.getPlayers()).thenReturn(List.of());
    assertFalse(gameController.savePlayers("fileName"));
    verify(mainController).showErrorDialog(eq("Input Error"), eq("No players to save."));
    verify(playerFileServiceMock, never()).savePlayers(anyString(), anyList());
  }

  @Test
  void loadPlayers_validFileName_attemptsLoadAndAddsPlayers() throws Exception {
    String fileName = "testPlayers";
    Player player1 = new Player("Alice", PieceType.RED);
    List<Player> loadedPlayers = List.of(player1);
    when(playerFileServiceMock.loadPlayers(fileName)).thenReturn(loadedPlayers);
    when(mockGame.getPlayers()).thenReturn(new ArrayList<>());
    assertTrue(gameController.loadPlayers(fileName));
    verify(playerFileServiceMock).loadPlayers(fileName);
    verify(gameFacade).addPlayer("Alice", PieceType.RED);
  }

  @Test
  void loadPlayers_emptyFileName_showsError() throws Exception {
    assertFalse(gameController.loadPlayers(""));
    verify(mainController).showErrorDialog(eq("Load Error"), eq("File name cannot be empty."));
    verify(playerFileServiceMock, never()).loadPlayers(anyString());
  }

  @Test
  void startGame_enoughPlayers_startsGame() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    when(mockGame.getPlayers()).thenReturn(List.of(mock(Player.class), mock(Player.class)));
    assertTrue(gameController.startGame());
    verify(gameFacade).startGame();
  }

  @Test
  void startGame_notEnoughPlayers_showsError() throws edu.ntnu.stud.boardgame.exception.BoardGameException {
    when(mockGame.getPlayers()).thenReturn(List.of(mock(Player.class))); // Only one player
    assertFalse(gameController.startGame());
    verify(mainController).showErrorDialog(eq("Player Error"), eq("You need at least 2 players to start the game."));
  }

  @Test
  void playTurn_facadePlaysTurn_returnsTrue() throws Exception {
    doNothing().when(gameFacade).playTurn(); // gameFacade.playTurn() is void
    assertTrue(gameController.playTurn());
    verify(gameFacade).playTurn();
  }

  @Test
  void playTurn_facadeThrowsBoardGameException_returnsFalseAndShowsError() throws Exception {
    doThrow(new edu.ntnu.stud.boardgame.exception.BoardGameException("Facade Error")).when(gameFacade).playTurn();
    assertFalse(gameController.playTurn());
    verify(mainController).showErrorDialog(eq("Game Error"), eq("Failed to play turn: Facade Error"));
  }

  @Test
  void registerObserver_validObserver_registersObserver() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    gameController.registerObserver(observer);
    verify(gameFacade).registerObserver(observer);
  }

  @Test
  void registerObserver_nullObserver_doesNotThrow() {
    assertDoesNotThrow(() -> gameController.registerObserver(null));
    verify(gameFacade, never()).registerObserver(null);
  }

  @Test
  void getGame_returnsGameFromFacade() {
    assertEquals(mockGame, gameController.getGame());
    verify(gameFacade).getCurrentGame();
  }

  @Test
  void getCurrentGameType_returnsTypeFromFacade() {
    BoardGameType expectedType = BoardGameType.MONOPOLY;
    when(gameFacade.getCurrentGameType()).thenReturn(expectedType);
    assertEquals(expectedType, gameController.getCurrentGameType());
    verify(gameFacade).getCurrentGameType();
  }

  @Test
  void getAvailablePlayerListNames_serviceReturnsNames_returnsNames() throws Exception {
    List<String> expectedNames = List.of("players1", "players2");
    when(playerFileServiceMock.getAvailablePlayerListFileNames()).thenReturn(expectedNames);
    List<String> actualNames = gameController.getAvailablePlayerListNames();
    assertEquals(expectedNames, actualNames);
    verify(playerFileServiceMock).getAvailablePlayerListFileNames();
    verify(mainController, never()).showErrorDialog(anyString(), anyString());
  }

  @Test
  void getAvailablePlayerListNames_serviceThrowsException_showsErrorAndReturnsEmptyList() throws Exception {
    when(playerFileServiceMock.getAvailablePlayerListFileNames())
        .thenThrow(new edu.ntnu.stud.boardgame.exception.files.PlayerFileException("Service Exception"));
    List<String> names = gameController.getAvailablePlayerListNames();
    assertTrue(names.isEmpty());
    verify(mainController).showErrorDialog(eq("Load Error"),
        eq("Failed to retrieve saved player lists: Service Exception"));
    verify(playerFileServiceMock).getAvailablePlayerListFileNames();
  }
}