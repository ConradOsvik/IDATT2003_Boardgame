package edu.ntnu.stud.boardgame.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.event.GameCreatedEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.observer.event.GameStartedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerWonEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class BoardGameTest {

  private ConcreteBoardGame game;
  private Board mockBoard;
  private Player mockPlayer1;
  private Player mockPlayer2;
  private BoardGameObserver mockObserver;
  private Tile mockStartTile;

  @BeforeEach
  void setUp() {
    game = new ConcreteBoardGame();
    mockBoard = mock(Board.class);
    mockPlayer1 = mock(Player.class);
    mockPlayer2 = mock(Player.class);
    mockObserver = mock(BoardGameObserver.class);
    mockStartTile = mock(Tile.class);

    game.setBoard(mockBoard);
    game.createDice(1);

    when(mockBoard.getStartTileId()).thenReturn(0);
    when(mockBoard.getTile(0)).thenReturn(mockStartTile);
  }

  @Test
  void constructor_initializesCollectionsAndState() {
    assertTrue(game.getPlayers().isEmpty());
    assertNotNull(game.observers);
    assertTrue(game.observers.isEmpty());
    assertEquals(0, game.currentPlayerIndex);
    assertFalse(game.isGameOver());
    assertNull(game.getWinner());
    assertNull(game.getCurrentPlayer());
  }

  @Test
  void createDice_shouldInitializeDice() {
    game.createDice(2);
    assertNotNull(game.getDice());
  }

  @Test
  void addPlayer_withValidPlayer_shouldAddPlayerAndNotify() {
    game.registerObserver(mockObserver);
    game.addPlayer(mockPlayer1);

    assertEquals(1, game.getPlayers().size());
    assertTrue(game.getPlayers().contains(mockPlayer1));
    verify(mockObserver, times(1)).onGameEvent(any(PlayerAddedEvent.class));
  }

  @Test
  void addPlayer_withNullPlayer_shouldThrowIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> game.addPlayer(null));
  }

  @Test
  void setBoard_withValidBoard_shouldSetBoard() {
    Board newMockBoard = mock(Board.class);
    game.setBoard(newMockBoard);
    assertEquals(newMockBoard, game.getBoard());
  }

  @Test
  void setBoard_withNullBoard_shouldThrowIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> game.setBoard(null));
  }

  @Test
  void startGame_withNoPlayers_shouldThrowInvalidGameStateException() {
    assertThrows(InvalidGameStateException.class, () -> game.startGame());
  }

  @Test
  void startGame_withNoBoard_shouldThrowInvalidGameStateException() {
    ConcreteBoardGame newGame = new ConcreteBoardGame();
    newGame.addPlayer(mockPlayer1);
    newGame.createDice(1);
    assertThrows(InvalidGameStateException.class, () -> newGame.startGame());
  }

  @Test
  void startGame_withNoDice_shouldThrowInvalidGameStateException() {
    ConcreteBoardGame newGame = new ConcreteBoardGame();
    newGame.addPlayer(mockPlayer1);
    newGame.setBoard(mockBoard);
    assertThrows(InvalidGameStateException.class, () -> newGame.startGame());
  }

  @Test
  void startGame_withValidSetup_shouldInitializeGameAndNotify() {
    game.addPlayer(mockPlayer1);
    game.addPlayer(mockPlayer2);
    game.registerObserver(mockObserver);

    game.startGame();

    assertEquals(mockPlayer1, game.getCurrentPlayer());
    assertFalse(game.isGameOver());
    assertNull(game.getWinner());
    verify(mockPlayer1, times(1)).placeOnTile(mockStartTile);
    verify(mockPlayer2, times(1)).placeOnTile(mockStartTile);
    verify(mockObserver, times(1)).onGameEvent(any(GameStartedEvent.class));
  }

  @Test
  void startGame_withMissingStartTile_shouldThrowInvalidGameStateException() {
    when(mockBoard.getStartTileId()).thenReturn(99);
    when(mockBoard.getTile(99)).thenReturn(null);

    game.addPlayer(mockPlayer1);
    assertThrows(InvalidGameStateException.class, () -> game.startGame());
  }

  @Test
  void nextTurn_shouldAdvancePlayerAndNotify() {
    game.addPlayer(mockPlayer1);
    game.addPlayer(mockPlayer2);
    game.startGame();
    game.registerObserver(mockObserver);

    game.nextTurn();

    assertEquals(mockPlayer2, game.getCurrentPlayer());
    verify(mockObserver, times(1)).onGameEvent(any(TurnChangedEvent.class));
  }

  @Test
  void nextTurn_shouldWrapAroundPlayerList() {
    game.addPlayer(mockPlayer1);
    game.addPlayer(mockPlayer2);
    game.startGame();
    game.nextTurn();
    game.nextTurn();

    assertEquals(mockPlayer1, game.getCurrentPlayer());
  }

  @Test
  void nextTurn_shouldSkipPlayerIfSkipNextTurnIsTrue() {
    game.addPlayer(mockPlayer1);
    game.addPlayer(mockPlayer2);
    when(mockPlayer1.shouldSkipNextTurn()).thenReturn(false);
    when(mockPlayer2.shouldSkipNextTurn()).thenReturn(true);
    game.startGame();
    game.registerObserver(mockObserver);

    game.nextTurn();

    assertEquals(mockPlayer1, game.getCurrentPlayer());
    verify(mockPlayer2, times(1)).setSkipNextTurn(false);

    ArgumentCaptor<TurnChangedEvent> captor = ArgumentCaptor.forClass(TurnChangedEvent.class);
    verify(mockObserver, times(1)).onGameEvent(captor.capture());
    assertEquals(mockPlayer1, captor.getValue().getCurrentPlayer());
  }

  @Test
  void nextTurn_whenGameOver_shouldDoNothing() {
    game.addPlayer(mockPlayer1);
    game.startGame();
    game.endGame(mockPlayer1);
    game.registerObserver(mockObserver);

    Player originalCurrentPlayer = game.getCurrentPlayer();
    game.nextTurn();

    assertEquals(originalCurrentPlayer, game.getCurrentPlayer());
    verify(mockObserver, never()).onGameEvent(any(TurnChangedEvent.class));
  }

  @Test
  void endGame_shouldSetGameOverAndWinnerAndNotify() {
    game.registerObserver(mockObserver);
    game.addPlayer(mockPlayer1);
    game.startGame();

    game.endGame(mockPlayer1);

    assertTrue(game.isGameOver());
    assertEquals(mockPlayer1, game.getWinner());
    verify(mockObserver, times(1)).onGameEvent(any(PlayerWonEvent.class));
    verify(mockObserver, times(1)).onGameEvent(any(GameEndedEvent.class));
  }

  @Test
  void registerObserver_withNullObserver_shouldLogWarningAndNotAdd() {
    int initialObserverCount = game.observers.size();
    game.registerObserver(null);
    assertEquals(initialObserverCount, game.observers.size());
  }

  @Test
  void registerObserver_withValidObserver_shouldAddObserver() {
    game.registerObserver(mockObserver);
    assertTrue(game.observers.contains(mockObserver));
  }

  @Test
  void registerObserver_withDuplicateObserver_shouldNotAddAgain() {
    game.registerObserver(mockObserver);
    int countAfterFirstAdd = game.observers.size();
    game.registerObserver(mockObserver);
    assertEquals(countAfterFirstAdd, game.observers.size());
  }

  @Test
  void registerObservers_withNullList_shouldLogWarningAndNotFail() {
    int initialObserverCount = game.observers.size();
    game.registerObservers(null);
    assertEquals(initialObserverCount, game.observers.size());
  }

  @Test
  void registerObservers_withValidList_shouldAddAllObservers() {
    BoardGameObserver mockObserver2 = mock(BoardGameObserver.class);
    List<BoardGameObserver> observerList = new ArrayList<>();
    observerList.add(mockObserver);
    observerList.add(mockObserver2);

    game.registerObservers(observerList);
    assertEquals(2, game.observers.size());
    assertTrue(game.observers.contains(mockObserver));
    assertTrue(game.observers.contains(mockObserver2));
  }

  @Test
  void registerObservers_withListContainingNull_shouldAddNonNullObservers() {
    BoardGameObserver mockObserver2 = mock(BoardGameObserver.class);
    List<BoardGameObserver> observerList = new ArrayList<>();
    observerList.add(mockObserver);
    observerList.add(null);
    observerList.add(mockObserver2);

    game.registerObservers(observerList);
    assertEquals(2, game.observers.size());
    assertTrue(game.observers.contains(mockObserver));
    assertTrue(game.observers.contains(mockObserver2));
  }

  @Test
  void notifyObservers_withNullEvent_shouldLogWarningAndNotFail() {
    game.registerObserver(mockObserver);
    game.notifyObservers(null);
    verify(mockObserver, never()).onGameEvent(null);
  }

  @Test
  void notifyObservers_withValidEvent_shouldCallOnGameEventOnAllObservers() {
    BoardGameObserver mockObserver2 = mock(BoardGameObserver.class);
    game.registerObserver(mockObserver);
    game.registerObserver(mockObserver2);
    GameCreatedEvent event = new GameCreatedEvent(mockBoard, Collections.emptyList());

    game.notifyObservers(event);

    verify(mockObserver, times(1)).onGameEvent(event);
    verify(mockObserver2, times(1)).onGameEvent(event);
  }

  @Test
  void notifyGameCreated_shouldNotifyWithGameCreatedEvent() {
    game.registerObserver(mockObserver);
    game.addPlayer(mockPlayer1);

    game.notifyGameCreated();

    ArgumentCaptor<GameCreatedEvent> captor = ArgumentCaptor.forClass(GameCreatedEvent.class);
    verify(mockObserver).onGameEvent(captor.capture());
    GameCreatedEvent capturedEvent = captor.getValue();
    assertEquals(mockBoard, capturedEvent.getBoard());
    assertTrue(capturedEvent.getPlayers().contains(mockPlayer1));
  }

  @Test
  void getPlayers_shouldReturnCopyOfPlayersList() {
    game.addPlayer(mockPlayer1);
    List<Player> retrievedPlayers = game.getPlayers();
    assertNotNull(retrievedPlayers);
    assertEquals(1, retrievedPlayers.size());
    assertTrue(retrievedPlayers.contains(mockPlayer1));

    retrievedPlayers.clear();
    assertFalse(game.getPlayers().isEmpty());
  }

  @Test
  void getCurrentPlayer_whenNoPlayers_shouldReturnNull() {
    ConcreteBoardGame emptyGame = new ConcreteBoardGame();
    assertNull(emptyGame.getCurrentPlayer());
  }

  private static class ConcreteBoardGame extends BoardGame {
    public ConcreteBoardGame() {
      super();
    }

    @Override
    public void playTurn() {}
  }
}
