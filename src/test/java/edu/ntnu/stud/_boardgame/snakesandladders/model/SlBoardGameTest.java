//package edu.ntnu.stud.boardgame.snakesandladders.model;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.Mockito.atLeast;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.verify;
//
//import edu.ntnu.stud.boardgame.core.exception.GameOverException;
//import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
//import edu.ntnu.stud.boardgame.core.model.Tile;
//import edu.ntnu.stud.boardgame.core.observer._BoardGameObserver;
//import edu.ntnu.stud.boardgame.core.observer._GameEvent;
//import javafx.scene.paint.Color;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//class SlBoardGameTest {
//
//  private SlBoardGame game;
//  private _BoardGameObserver observer;
//
//  @BeforeEach
//  void setUp() {
//    game = new SlBoardGame();
//    observer = mock(_BoardGameObserver.class);
//    game.addObserver(observer);
//  }
//
//  @Test
//  void createBoard_createsSlBoard() {
//    game.createBoard();
//
//    assertNotNull(game.getBoard());
//    assertTrue(game.getBoard() instanceof SlBoard);
//  }
//
//  @Test
//  void playTurn_nullPlayer_throwsException() {
//    assertThrows(InvalidPlayerException.class, () -> game.playTurn(null));
//  }
//
//  @Test
//  void playTurn_gameFinished_throwsException() {
//    SlPlayer player = new SlPlayer("Test", Color.RED);
//    game.addPlayer(player);
//
//    game.createBoard();
//    game.createDice(1);
//
//    SlPlayer winner = new SlPlayer("Winner", Color.BLUE);
//    game.addPlayer(winner);
//
//    try {
//      java.lang.reflect.Field finishedField = game.getClass().getSuperclass()
//          .getDeclaredField("finished");
//      finishedField.setAccessible(true);
//      finishedField.set(game, true);
//
//      java.lang.reflect.Field winnerField = game.getClass().getSuperclass()
//          .getDeclaredField("winner");
//      winnerField.setAccessible(true);
//      winnerField.set(game, winner);
//    } catch (Exception e) {
//      fail("Failed to set up test: " + e.getMessage());
//    }
//
//    assertThrows(GameOverException.class, () -> game.playTurn(player));
//  }
//
//  @Test
//  void playTurn_normalTurn_movePlayerAndNotifyObservers() {
//    SlBoardGame mockedGame = spy(game);
//    doReturn(3).when(mockedGame).rollDice();
//
//    SlPlayer player = new SlPlayer("Test", Color.RED);
//    mockedGame.createBoard();
//
//    Tile startingTile = mockedGame.getBoard().getTile(0);
//    player.setStartingTile(startingTile);
//
//    mockedGame.addPlayer(player);
//    mockedGame.playTurn(player);
//
//    assertEquals(3, player.getCurrentTile().getTileId());
//
//    ArgumentCaptor<_GameEvent> eventCaptor = ArgumentCaptor.forClass(_GameEvent.class);
//    verify(observer, atLeast(3)).onGameEvent(eventCaptor.capture());
//
//    boolean moveEventFound = eventCaptor.getAllValues().stream()
//        .anyMatch(event -> event.getEventType() == _GameEvent.EventType.PLAYER_MOVED);
//
//    assertTrue(moveEventFound);
//  }
//
//  @Test
//  void playTurn_landOnSnake_notifiesSnakeEvent() {
//    SlBoardGame mockedGame = spy(game);
//    doReturn(4).when(mockedGame).rollDice();
//
//    SlPlayer player = new SlPlayer("Test", Color.RED);
//    mockedGame.createBoard();
//
//    ((SlBoard) mockedGame.getBoard()).addSnake(4, 2);
//
//    Tile startingTile = mockedGame.getBoard().getTile(0);
//    player.setStartingTile(startingTile);
//
//    mockedGame.addPlayer(player);
//    mockedGame.playTurn(player);
//
//    assertEquals(2, player.getCurrentTile().getTileId());
//
//    ArgumentCaptor<_GameEvent> eventCaptor = ArgumentCaptor.forClass(_GameEvent.class);
//    verify(observer, atLeast(3)).onGameEvent(eventCaptor.capture());
//
//    boolean snakeEventFound = eventCaptor.getAllValues().stream()
//        .anyMatch(event -> event.getEventType() == _GameEvent.EventType.SNAKE_ENCOUNTERED);
//
//    assertTrue(snakeEventFound);
//  }
//
//  @Test
//  void playTurn_landOnLadder_notifiesLadderEvent() {
//    SlBoardGame mockedGame = spy(game);
//    doReturn(4).when(mockedGame).rollDice();
//
//    SlPlayer player = new SlPlayer("Test", Color.RED);
//    mockedGame.createBoard();
//
//    ((SlBoard) mockedGame.getBoard()).addLadder(4, 10);
//
//    Tile startingTile = mockedGame.getBoard().getTile(0);
//    player.setStartingTile(startingTile);
//
//    mockedGame.addPlayer(player);
//    mockedGame.playTurn(player);
//
//    assertEquals(10, player.getCurrentTile().getTileId());
//
//    ArgumentCaptor<_GameEvent> eventCaptor = ArgumentCaptor.forClass(_GameEvent.class);
//    verify(observer, atLeast(3)).onGameEvent(eventCaptor.capture());
//
//    boolean ladderEventFound = eventCaptor.getAllValues().stream()
//        .anyMatch(event -> event.getEventType() == _GameEvent.EventType.LADDER_CLIMBED);
//
//    assertTrue(ladderEventFound);
//  }
//
//  @Test
//  void playTurn_reachLastTile_finishesGameAndNotifiesWin() {
//    SlBoardGame mockedGame = spy(game);
//
//    mockedGame.createBoard();
//    SlBoard board = (SlBoard) mockedGame.getBoard();
//
//    SlPlayer player = new SlPlayer("Test", Color.RED);
//
//    // Add player to the game
//    mockedGame.addPlayer(player);
//    mockedGame.addObserver(observer);
//
//    // Set the player's starting tile to the second last tile after having been added to the game
//    // Otherwise, the player would be placed on the starting tile
//    Tile almostLastTile = board.getTile(SlBoard.NUM_TILES - 1);
//    player.placeOnTile(almostLastTile);
//
//    doReturn(1).when(mockedGame).rollDice();
//
//    mockedGame.playTurn(player);
//
//    assertTrue(mockedGame.isFinished());
//    assertEquals(player, mockedGame.getWinner().orElse(null));
//
//    ArgumentCaptor<_GameEvent> eventCaptor = ArgumentCaptor.forClass(_GameEvent.class);
//    verify(observer, atLeast(3)).onGameEvent(eventCaptor.capture());
//
//    boolean winEventFound = eventCaptor.getAllValues().stream()
//        .anyMatch(event -> event.getEventType() == _GameEvent.EventType.PLAYER_WON);
//
//    boolean gameEndedEventFound = eventCaptor.getAllValues().stream()
//        .anyMatch(event -> event.getEventType() == _GameEvent.EventType.GAME_ENDED);
//
//    assertTrue(winEventFound);
//    assertTrue(gameEndedEventFound);
//  }
//}