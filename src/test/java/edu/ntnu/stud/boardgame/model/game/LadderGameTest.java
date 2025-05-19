//package edu.ntnu.stud.boardgame.model.game;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.atLeastOnce;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import edu.ntnu.stud.boardgame.model.Board;
//import edu.ntnu.stud.boardgame.model.Dice;
//import edu.ntnu.stud.boardgame.model.Player;
//import edu.ntnu.stud.boardgame.model.Tile;
//import edu.ntnu.stud.boardgame.model.action.LadderAction;
//import edu.ntnu.stud.boardgame.model.action.SnakeAction;
//import edu.ntnu.stud.boardgame.model.enums.PieceType;
//import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
//import edu.ntnu.stud.boardgame.observer.GameEvent;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//@DisplayName("LadderGame Tests")
//class LadderGameTest {
//
//  private LadderGame ladderGame;
//
//  @Mock
//  private Board mockBoard;
//
//  @Mock
//  private Dice mockDice;
//
//  @Mock
//  private Tile mockStartTile;
//
//  @Mock
//  private Tile mockEndTile;
//
//  @Mock
//  private Tile mockMiddleTile;
//
//  @Mock
//  private BoardGameObserver mockObserver;
//
//  private Player player1;
//  private Player player2;
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.openMocks(this);
//
//    ladderGame = new LadderGame();
//    ladderGame.setBoard(mockBoard);
//    ladderGame.createDice(2);
//
//    ladderGame.dice = mockDice;
//
//    when(mockBoard.getStartTileId()).thenReturn(0);
//    when(mockBoard.getEndTileId()).thenReturn(90);
//    when(mockBoard.getTile(0)).thenReturn(mockStartTile);
//    when(mockBoard.getTile(90)).thenReturn(mockEndTile);
//    when(mockBoard.getTile(45)).thenReturn(mockMiddleTile);
//
//    when(mockMiddleTile.getTileId()).thenReturn(45);
//    when(mockEndTile.getTileId()).thenReturn(90);
//
//    ladderGame.registerObserver(mockObserver);
//
//    player1 = new Player("Player 1", PieceType.RED);
//    player2 = new Player("Player 2", PieceType.BLUE);
//  }
//
//  @Test
//  @DisplayName("createDefaultBoard should create valid board with correct structure")
//  void createDefaultBoard_noParameters_createsValidBoard() {
//    Board board = ladderGame.createDefaultBoard();
//
//    assertNotNull(board);
//    assertEquals("Classic Snakes and Ladders", board.getName());
//    assertEquals(10, board.getRows());
//    assertEquals(9, board.getColumns());
//    assertEquals(0, board.getStartTileId());
//    assertEquals(90, board.getEndTileId());
//
//    for (int i = 0; i <= 90; i++) {
//      assertNotNull(board.getTile(i));
//    }
//
//    for (int i = 0; i < 90; i++) {
//      Tile tile = board.getTile(i);
//      Tile nextTile = board.getTile(i + 1);
//      assertEquals(nextTile, tile.getNextTile());
//    }
//  }
//
//  @Nested
//  @DisplayName("Game Initialization Tests")
//  class GameInitializationTests {
//
//    @Test
//    @DisplayName("startGame should initialize game state correctly")
//    void startGame_withPlayers_initializesGameStateCorrectly() {
//      ladderGame.addPlayer(player1);
//      ladderGame.addPlayer(player2);
//
//      ladderGame.startGame();
//
//      assertEquals(player1, ladderGame.getCurrentPlayer());
//      assertEquals(mockStartTile, player1.getCurrentTile());
//      assertEquals(mockStartTile, player2.getCurrentTile());
//      assertFalse(ladderGame.isGameOver());
//      assertNull(ladderGame.getWinner());
//    }
//  }
//
//  @Nested
//  @DisplayName("Game Play Tests")
//  class GamePlayTests {
//
//    @Test
//    @DisplayName("playTurn with normal move should move player correctly")
//    void playTurn_normalMove_movesPlayerCorrectly() {
//      ladderGame.addPlayer(player1);
//      ladderGame.startGame();
//
//      player1.setCurrentTile(mockStartTile);
//      when(mockDice.roll()).thenReturn(5);
//      when(mockStartTile.getTileId()).thenReturn(0);
//      when(mockStartTile.getNextTile()).thenReturn(mockMiddleTile);
//
//      ladderGame.playTurn();
//
//      verify(mockDice).roll();
//      assertEquals(mockMiddleTile, player1.getCurrentTile());
//    }
//
//    @Test
//    @DisplayName("playTurn with ladder should trigger ladder action")
//    void playTurn_landOnLadder_triggersLadderAction() {
//      ladderGame.addPlayer(player1);
//      ladderGame.startGame();
//
//      Tile ladderDestination = mock(Tile.class);
//      LadderAction ladderAction = mock(LadderAction.class);
//      when(mockMiddleTile.getLandAction()).thenReturn(ladderAction);
//      when(ladderAction.getDestinationTile()).thenReturn(ladderDestination);
//
//      player1.setCurrentTile(mockStartTile);
//      when(mockDice.roll()).thenReturn(5);
//      when(mockStartTile.getTileId()).thenReturn(0);
//      when(mockStartTile.getNextTile()).thenReturn(mockMiddleTile);
//
//      ladderGame.playTurn();
//
//      verify(mockDice).roll();
//      verify(mockMiddleTile).landPlayer(player1);
//    }
//
//    @Test
//    @DisplayName("playTurn with snake should trigger snake action")
//    void playTurn_landOnSnake_triggersSnakeAction() {
//      ladderGame.addPlayer(player1);
//      ladderGame.startGame();
//
//      Tile snakeDestination = mock(Tile.class);
//      SnakeAction snakeAction = mock(SnakeAction.class);
//      when(mockMiddleTile.getLandAction()).thenReturn(snakeAction);
//      when(snakeAction.getDestinationTile()).thenReturn(snakeDestination);
//
//      player1.setCurrentTile(mockStartTile);
//      when(mockDice.roll()).thenReturn(5);
//      when(mockStartTile.getTileId()).thenReturn(0);
//      when(mockStartTile.getNextTile()).thenReturn(mockMiddleTile);
//
//      ladderGame.playTurn();
//
//      verify(mockDice).roll();
//      verify(mockMiddleTile).landPlayer(player1);
//    }
//
//    @Test
//    @DisplayName("playTurn when player reaches end should end game")
//    void playTurn_playerReachesEnd_endsGame() {
//      ladderGame.addPlayer(player1);
//      ladderGame.startGame();
//
//      Tile nearEndTile = mock(Tile.class);
//      when(nearEndTile.getTileId()).thenReturn(89);
//      when(nearEndTile.getNextTile()).thenReturn(mockEndTile);
//      player1.setCurrentTile(nearEndTile);
//
//      when(mockDice.roll()).thenReturn(1);
//
//      ladderGame.playTurn();
//
//      verify(mockDice).roll();
//      assertEquals(mockEndTile, player1.getCurrentTile());
//      assertTrue(ladderGame.isGameOver());
//      assertEquals(player1, ladderGame.getWinner());
//    }
//  }
//
//  @Nested
//  @DisplayName("Turn Management Tests")
//  class TurnManagementTests {
//
//    @Test
//    @DisplayName("nextTurn should cycle through players")
//    void nextTurn_multiplePlayers_cyclesThroughPlayers() {
//      ladderGame.addPlayer(player1);
//      ladderGame.addPlayer(player2);
//      ladderGame.startGame();
//
//      assertEquals(player1, ladderGame.getCurrentPlayer());
//
//      ladderGame.nextTurn();
//
//      assertEquals(player2, ladderGame.getCurrentPlayer());
//
//      ladderGame.nextTurn();
//
//      assertEquals(player1, ladderGame.getCurrentPlayer());
//    }
//
//    @Test
//    @DisplayName("nextTurn should skip player with skipNextTurn flag")
//    void nextTurn_playerShouldSkip_skipsPlayer() {
//      ladderGame.addPlayer(player1);
//      ladderGame.addPlayer(player2);
//      Player player3 = new Player("Player 3", PieceType.GREEN);
//      ladderGame.addPlayer(player3);
//      ladderGame.startGame();
//
//      player2.setSkipNextTurn(true);
//
//      assertEquals(player1, ladderGame.getCurrentPlayer());
//
//      ladderGame.nextTurn();
//
//      assertEquals(player3, ladderGame.getCurrentPlayer());
//      assertFalse(player2.shouldSkipNextTurn());
//    }
//
//    @Test
//    @DisplayName("nextTurn should do nothing if game is over")
//    void nextTurn_gameIsOver_doesNothing() {
//      ladderGame.addPlayer(player1);
//      ladderGame.addPlayer(player2);
//      ladderGame.startGame();
//
//      ladderGame.endGame(player1);
//
//      Player initialCurrentPlayer = ladderGame.getCurrentPlayer();
//
//      ladderGame.nextTurn();
//
//      assertEquals(initialCurrentPlayer, ladderGame.getCurrentPlayer());
//    }
//  }
//
//  @Nested
//  @DisplayName("Observer Tests")
//  class ObserverTests {
//
//    @Test
//    @DisplayName("registerObserver should add observer")
//    void registerObserver_newObserver_addsObserver() {
//      BoardGameObserver newObserver = mock(BoardGameObserver.class);
//
//      ladderGame.registerObserver(newObserver);
//      ladderGame.endGame(player1);
//
//      verify(mockObserver, atLeastOnce()).onGameEvent(any(GameEvent.class));
//      verify(newObserver, atLeastOnce()).onGameEvent(any(GameEvent.class));
//    }
//
//    @Test
//    @DisplayName("registerObservers should add multiple observers")
//    void registerObservers_listOfObservers_addsAllObservers() {
//      BoardGameObserver observer1 = mock(BoardGameObserver.class);
//      BoardGameObserver observer2 = mock(BoardGameObserver.class);
//      List<BoardGameObserver> observers = new ArrayList<>();
//      observers.add(observer1);
//      observers.add(observer2);
//
//      ladderGame.registerObservers(observers);
//      ladderGame.endGame(player1);
//
//      verify(mockObserver, atLeastOnce()).onGameEvent(any(GameEvent.class));
//      verify(observer1, atLeastOnce()).onGameEvent(any(GameEvent.class));
//      verify(observer2, atLeastOnce()).onGameEvent(any(GameEvent.class));
//    }
//  }
//}