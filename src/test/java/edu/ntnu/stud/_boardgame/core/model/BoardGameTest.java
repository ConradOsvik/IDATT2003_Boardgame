package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
import edu.ntnu.stud.boardgame.core.observer.events.DiceRolledEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameRestartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameStartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class BoardGameTest {

  private TestBoardGame game;

  @BeforeEach
  void setUp() {
    game = new TestBoardGame();
    Board mockBoard = mock(Board.class);
    Tile mockStartingTile = mock(Tile.class);
    when(mockBoard.getStartingTile()).thenReturn(mockStartingTile);
  }

  @Test
  void init_notifiesObservers() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    game.addObserver(observer);

    game.createBoard();
    game.init();

    ArgumentCaptor<GameEvent> eventCaptor = ArgumentCaptor.forClass(GameEvent.class);
    verify(observer, atLeastOnce()).onGameEvent(eventCaptor.capture());

    boolean gameCreatedEventFound = eventCaptor.getAllValues().stream()
        .anyMatch(event -> event instanceof GameCreatedEvent);

    assertTrue(gameCreatedEventFound);
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

    // Create a spy that will finish the game after the first player's turn
    TestBoardGame spyGame = spy(game);
    doAnswer(invocation -> {
      Player player = invocation.getArgument(0);
      // Set game as finished after first player's turn
      if (player.equals(player1)) {
        spyGame.setFinished(true);
        spyGame.setWinner(player1);
      }
      return null;
    }).when(spyGame).playTurn(any(Player.class));

    spyGame.playOneRound();

    // Verify first player had a turn
    verify(spyGame).playTurn(player1);
    // Verify second player didn't have a turn because game finished
    verify(spyGame, never()).playTurn(player2);

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
    assertInstanceOf(DiceRolledEvent.class, capturedEvent);
    assertEquals(result, ((DiceRolledEvent) capturedEvent).getValue());
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
  void start_notInitialized_throwsException() {
    Player player = new TestPlayer("Player");
    game.addPlayer(player);

    assertThrows(GameNotInitializedException.class, () -> game.start());
  }

  @Test
  void start_noPlayers_throwsException() {
    game.createBoard();
    game.createDice(1);

    assertThrows(IllegalGameStateException.class, () -> game.start());
  }

  @Test
  void start_validState_notifiesObservers() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    game.addObserver(observer);

    game.createBoard();
    game.createDice(1);
    game.addPlayer(new TestPlayer("Player"));

    game.start();

    ArgumentCaptor<GameEvent> eventCaptor = ArgumentCaptor.forClass(GameEvent.class);
    verify(observer, atLeastOnce()).onGameEvent(eventCaptor.capture());

    boolean gameStartedEventFound = eventCaptor.getAllValues().stream()
        .anyMatch(event -> event instanceof GameStartedEvent);

    assertTrue(gameStartedEventFound);
  }

  @Test
  void restart_resetsGameStateAndNotifiesObservers() {
    BoardGameObserver observer = mock(BoardGameObserver.class);
    game.addObserver(observer);

    game.createBoard();
    Player player = mock(Player.class);
    game.addPlayer(player);
    game.setWinner(player);
    game.setFinished(true);

    game.restart();

    assertFalse(game.isFinished());
    assertEquals(Optional.empty(), game.getWinner());

    ArgumentCaptor<GameEvent> eventCaptor = ArgumentCaptor.forClass(GameEvent.class);
    verify(observer, atLeastOnce()).onGameEvent(eventCaptor.capture());

    boolean restartEventFound = eventCaptor.getAllValues().stream()
        .anyMatch(event -> event instanceof GameRestartedEvent);

    assertTrue(restartEventFound);

    // Verify player's starting tile was reset
    verify(player, atLeastOnce()).setStartingTile(any(Tile.class));
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

    boolean resetEventFound = eventCaptor.getAllValues().stream()
        .anyMatch(event -> event instanceof GameResetEvent);

    assertTrue(resetEventFound);
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
  void addObservers_multipleObservers_addsAllObservers() {
    BoardGameObserver observer1 = mock(BoardGameObserver.class);
    BoardGameObserver observer2 = mock(BoardGameObserver.class);
    List<BoardGameObserver> observers = Arrays.asList(observer1, observer2);

    game.addObservers(observers);

    game.createBoard();

    verify(observer1).onGameEvent(any(GameEvent.class));
    verify(observer2).onGameEvent(any(GameEvent.class));
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
  void equals_sameInstance_returnsTrue() {
    assertEquals(game, game);
  }

  @Test
  void equals_null_returnsFalse() {
    assertNotEquals(null, game);
  }

  @Test
  void equals_differentClass_returnsFalse() {
    assertNotEquals("Not a BoardGame", game);
  }

  @Test
  void equals_equivalentGame_returnsTrue() {
    TestBoardGame game1 = new TestBoardGame();
    TestBoardGame game2 = new TestBoardGame();

    // Create a single board mock and share it
    Board mockBoard = mock(Board.class);

    // Use reflection to set the board on both games
    try {
      java.lang.reflect.Field boardField = BoardGame.class.getDeclaredField("board");
      boardField.setAccessible(true);
      boardField.set(game1, mockBoard);
      boardField.set(game2, mockBoard);
    } catch (Exception e) {
      fail("Failed to set board field: " + e.getMessage());
    }

    // Use the same dice instance
    Dice mockDice = new Dice(2);
    try {
      java.lang.reflect.Field diceField = BoardGame.class.getDeclaredField("dice");
      diceField.setAccessible(true);
      diceField.set(game1, mockDice);
      diceField.set(game2, mockDice);
    } catch (Exception e) {
      fail("Failed to set dice field: " + e.getMessage());
    }

    Player player = new TestPlayer("Player");
    game1.addPlayer(player);
    game2.addPlayer(player);

    assertEquals(game1, game2);
    assertEquals(game1.hashCode(), game2.hashCode());
  }

  @Test
  void equals_differentGames_returnsFalse() {
    TestBoardGame game1 = new TestBoardGame();
    TestBoardGame game2 = new TestBoardGame();

    game1.createBoard();
    game2.createBoard();

    Player player1 = new TestPlayer("Player1");
    Player player2 = new TestPlayer("Player2");

    game1.addPlayer(player1);
    game2.addPlayer(player2);

    assertNotEquals(game1, game2);
  }

  private static class TestBoardGame extends BoardGame {

    private boolean playTurnCalled = false;
    private Player lastPlayerToPlayTurn = null;

    @Override
    public void createBoard() {
      this.board = mock(Board.class);
      Tile mockStartingTile = mock(Tile.class);
      when(board.getStartingTile()).thenReturn(mockStartingTile);

      GameCreatedEvent event = new GameCreatedEvent(board);
      notifyObservers(event);
    }

    @Override
    public void reset() {
      super.reset();
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

      // Create mocks for the required parameters
      Tile fromTile = mock(Tile.class);
      Tile toTile = mock(Tile.class);
      int steps = 1;

      PlayerMovedEvent event = new PlayerMovedEvent(player, fromTile, toTile, steps, board);
      notifyObservers(event);
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
      super(name, 1);
    }
  }
}