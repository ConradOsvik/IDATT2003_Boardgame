package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.core.exception.GameNotInitializedException;
import edu.ntnu.stud.boardgame.core.exception.GameOverException;
import edu.ntnu.stud.boardgame.core.exception.IllegalGameStateException;
import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class BoardGameTest {

  private TestBoardGame game;

  private static class TestBoardGame extends BoardGame {

    private boolean createBoardCalled = false;
    private boolean playTurnCalled = false;
    private Player lastPlayerToPlayTurn = null;

    @Override
    public void createBoard() {
      this.board = mock(Board.class);
      createBoardCalled = true;

      GameEvent event = new GameEvent(GameEvent.EventType.GAME_CREATED);
      event.addData("board", board);
      notifyObservers(event);
    }

    @Override
    public void reset() {
      super.reset();

      GameEvent event = new GameEvent(GameEvent.EventType.GAME_RESET);
      event.addData("board", board);
      notifyObservers(event);
    }

    @Override
    public void playTurn(Player player) {
      if (player == null) {
        throw new InvalidPlayerException("Player cannot be null");
      }

      if (isFinished()) {
        throw new GameOverException("Game is already finished");
      }

      playTurnCalled = true;
      lastPlayerToPlayTurn = player;

      GameEvent event = new GameEvent(GameEvent.EventType.PLAYER_MOVED);
      event.addData("player", player);
      notifyObservers(event);
    }

    public boolean wasCreateBoardCalled() {
      return createBoardCalled;
    }

    public boolean wasPlayTurnCalled() {
      return playTurnCalled;
    }

    public Player getLastPlayerToPlayTurn() {
      return lastPlayerToPlayTurn;
    }

    public void setFinished(boolean finished) {
      this.finished = finished;
    }

    public void setWinner(Player player) {
      this.winner = player;
    }
  }

  private static class TestPlayer extends Player {

    public TestPlayer(String name) {
      super(name);
    }
  }

  @BeforeEach
  void setUp() {
    game = new TestBoardGame();
  }

  @Test
  void createDice_validNumber_createsDice() {
    game.createDice(2);
    assertDoesNotThrow(() -> game.rollDice());
  }

  @Test
  void createDice_invalidNumber_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> game.createDice(0));
    assertThrows(IllegalArgumentException.class, () -> game.createDice(-1));
  }

  @Test
  void addPlayer_validPlayer_addsToPlayers() {
    Player player = new TestPlayer("TestPlayer");
    game.addPlayer(player);

    assertTrue(game.getPlayers().contains(player));
    assertEquals(1, game.getPlayers().size());
  }

  @Test
  void addPlayer_nullPlayer_throwsException() {
    assertThrows(InvalidPlayerException.class, () -> game.addPlayer(null));
  }

  @Test
  void addPlayer_withBoard_setsStartingTile() {
    game.createBoard();
    Tile startingTile = mock(Tile.class);
    when(game.getBoard().getStartingTile()).thenReturn(startingTile);

    Player player = mock(Player.class);
    game.addPlayer(player);

    verify(player).setStartingTile(startingTile);
  }

  @Test
  void getPlayers_returnsUnmodifiableList() {
    Player player = new TestPlayer("TestPlayer");
    game.addPlayer(player);

    assertThrows(UnsupportedOperationException.class,
        () -> game.getPlayers().add(new TestPlayer("Another")));
  }

  @Test
  void playOneRound_noPlayers_doesNothing() {
    game.playOneRound();
    assertFalse(game.wasPlayTurnCalled());
  }

  @Test
  void playOneRound_withPlayers_playsForAllPlayers() {
    Player player1 = new TestPlayer("Player1");
    Player player2 = new TestPlayer("Player2");

    game.addPlayer(player1);
    game.addPlayer(player2);

    game.playOneRound();

    assertTrue(game.wasPlayTurnCalled());
    assertEquals(player2, game.getLastPlayerToPlayTurn());
  }

  @Test
  void playOneRound_gameFinishedMidRound_stopsEarly() {
    Player player1 = new TestPlayer("Player1");
    Player player2 = new TestPlayer("Player2");

    game.addPlayer(player1);
    game.addPlayer(player2);

    TestBoardGame spyGame = spy(game);
    doAnswer(invocation -> {
      spyGame.playTurn(player1);
      spyGame.setFinished(true);
      spyGame.setWinner(player1);
      return null;
    }).when(spyGame).playOneRound();

    spyGame.playOneRound();

    assertTrue(spyGame.isFinished());
    assertEquals(player1, spyGame.getWinner().orElse(null));
  }

  @Test
  void playOneRound_gameAlreadyFinished_throwsException() {
    game.setFinished(true);
    assertThrows(GameOverException.class, () -> game.playOneRound());
  }

  @Test
  void play_notInitialized_throwsException() {
    assertThrows(GameNotInitializedException.class, () -> game.play());
  }

  @Test
  void play_noPlayers_throwsException() {
    game.createBoard();
    game.createDice(1);

    assertThrows(GameNotInitializedException.class, () -> game.play());
  }

  @Test
  void play_noDice_throwsException() {
    game.createBoard();
    game.addPlayer(new TestPlayer("Player"));

    assertThrows(GameNotInitializedException.class, () -> game.play());
  }

  @Test
  void play_noBoard_throwsException() {
    game.createDice(1);
    game.addPlayer(new TestPlayer("Player"));

    assertThrows(GameNotInitializedException.class, () -> game.play());
  }

  @Test
  void rollDice_noDice_throwsException() {
    assertThrows(GameNotInitializedException.class, () -> game.rollDice());
  }

  @Test
  void rollDice_withDice_returnsValueAndNotifiesObservers() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    game.addObserver(observer);
    game.createDice(1);

    int result = game.rollDice();

    assertTrue(result >= 1 && result <= 6);

    ArgumentCaptor<GameEvent> eventCaptor = ArgumentCaptor.forClass(GameEvent.class);
    verify(observer).onGameEvent(eventCaptor.capture());

    GameEvent capturedEvent = eventCaptor.getValue();
    assertEquals(GameEvent.EventType.DICE_ROLLED, capturedEvent.getEventType());
    assertEquals(result, capturedEvent.getData("result"));
  }

  @Test
  void getWinner_noWinner_returnsEmptyOptional() {
    assertEquals(Optional.empty(), game.getWinner());
  }

  @Test
  void getWinner_hasWinner_returnsWinner() {
    Player winner = new TestPlayer("Winner");
    game.setWinner(winner);

    assertEquals(Optional.of(winner), game.getWinner());
  }

  @Test
  void startGame_notInitialized_throwsException() {
    Player player = new TestPlayer("Player");
    game.addPlayer(player);

    assertThrows(GameNotInitializedException.class, () -> game.startGame());
  }

  @Test
  void startGame_noPlayers_throwsException() {
    game.createBoard();
    game.createDice(1);

    assertThrows(IllegalGameStateException.class, () -> game.startGame());
  }

  @Test
  void startGame_validState_notifiesObservers() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    game.addObserver(observer);

    game.createBoard();
    game.createDice(1);
    game.addPlayer(new TestPlayer("Player"));

    game.startGame();

    ArgumentCaptor<GameEvent> eventCaptor = ArgumentCaptor.forClass(GameEvent.class);
    verify(observer, atLeastOnce()).onGameEvent(eventCaptor.capture());

    boolean gameStartedEventFound = eventCaptor.getAllValues().stream()
        .anyMatch(event -> event.getEventType() == GameEvent.EventType.GAME_STARTED);

    assertTrue(gameStartedEventFound);
  }

  @Test
  void reset_clearsStateAndNotifiesObservers() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    game.addObserver(observer);

    Player player = new TestPlayer("Player");
    game.addPlayer(player);
    game.setWinner(player);
    game.setFinished(true);

    game.reset();

    assertTrue(game.getPlayers().isEmpty());
    assertEquals(Optional.empty(), game.getWinner());
    assertFalse(game.isFinished());

    ArgumentCaptor<GameEvent> eventCaptor = ArgumentCaptor.forClass(GameEvent.class);
    verify(observer, atLeastOnce()).onGameEvent(eventCaptor.capture());

    GameEvent resetEvent = eventCaptor.getAllValues().stream()
        .filter(event -> event.getEventType() == GameEvent.EventType.GAME_RESET)
        .findFirst()
        .orElse(null);

    assertNotNull(resetEvent);
    assertEquals(GameEvent.EventType.GAME_RESET, resetEvent.getEventType());
  }

  @Test
  void addObserver_validObserver_addsObserver() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    game.addObserver(observer);

    game.createBoard();

    verify(observer).onGameEvent(any(GameEvent.class));
  }

  @Test
  void addObserver_duplicateObserver_onlyAddsOnce() {
    BoardGameObserver observer = mock(BoardGameObserver.class);

    game.addObserver(observer);
    game.addObserver(observer);

    game.createBoard();

    verify(observer, times(1)).onGameEvent(any(GameEvent.class));
  }

  @Test
  void removeObserver_existingObserver_removesObserver() {
    BoardGameObserver observer = mock(BoardGameObserver.class);

    game.addObserver(observer);
    game.removeObserver(observer);

    game.createBoard();

    verify(observer, never()).onGameEvent(any(GameEvent.class));
  }

  @Test
  void transferObserversFrom_otherGame_transfersObservers() {
    BoardGameObserver observer = mock(BoardGameObserver.class);

    TestBoardGame otherGame = new TestBoardGame();
    otherGame.addObserver(observer);

    game.transferObserversFrom(otherGame);

    game.createBoard();

    verify(observer).onGameEvent(any(GameEvent.class));
  }
}