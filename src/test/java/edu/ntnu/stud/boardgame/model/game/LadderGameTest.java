package edu.ntnu.stud.boardgame.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Dice;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.event.BounceBackEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.observer.event.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerWonEvent;
import edu.ntnu.stud.boardgame.observer.event.SnakeEncounteredEvent;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class LadderGameTest {

  private LadderGame game;
  private Board mockBoard;
  private Dice mockDice;
  private Player mockPlayer;
  private BoardGameObserver mockObserver;
  private Tile mockStartTile,
      mockRegularTile,
      mockEndTile,
      mockLadderTile,
      mockSnakeTile,
      mockBounceTargetTile;
  private AtomicReference<Tile> playerTileRef;

  @BeforeEach
  void setUp() {
    game = new LadderGame();
    mockBoard = mock(Board.class);
    mockDice = mock(Dice.class);
    mockPlayer = mock(Player.class);
    mockObserver = mock(BoardGameObserver.class);
    playerTileRef = new AtomicReference<>();

    game.setBoard(mockBoard);
    game.dice = mockDice;
    game.addPlayer(mockPlayer);
    game.registerObserver(mockObserver);

    mockStartTile = mock(Tile.class);
    when(mockStartTile.getTileId()).thenReturn(0);
    mockRegularTile = mock(Tile.class);
    when(mockRegularTile.getTileId()).thenReturn(5);
    mockEndTile = mock(Tile.class);
    when(mockEndTile.getTileId()).thenReturn(10);
    mockLadderTile = mock(Tile.class);
    when(mockLadderTile.getTileId()).thenReturn(3);
    mockSnakeTile = mock(Tile.class);
    when(mockSnakeTile.getTileId()).thenReturn(8);
    mockBounceTargetTile = mock(Tile.class);
    when(mockBounceTargetTile.getTileId()).thenReturn(9);

    when(mockPlayer.getName()).thenReturn("TestPlayer");

    doAnswer(
            invocation -> {
              playerTileRef.set(invocation.getArgument(0));
              return null;
            })
        .when(mockPlayer)
        .setCurrentTile(any(Tile.class));
    when(mockPlayer.getCurrentTile()).thenAnswer(invocation -> playerTileRef.get());

    doAnswer(
            invocation -> {
              Tile tile = invocation.getArgument(0);
              playerTileRef.set(tile);
              return null;
            })
        .when(mockPlayer)
        .placeOnTile(any(Tile.class));

    when(mockBoard.getStartTileId()).thenReturn(0);
    when(mockBoard.getTile(0)).thenReturn(mockStartTile);
    when(mockBoard.getEndTileId()).thenReturn(10);
    when(mockBoard.getTile(10)).thenReturn(mockEndTile);

    game.startGame();
  }

  @Test
  void playTurn_basicMove_shouldMovePlayerAndNotifyEvents() {
    when(mockDice.roll()).thenReturn(3);
    when(mockPlayer.getDestinationTile(3)).thenReturn(mockLadderTile);
    when(mockLadderTile.getLandAction()).thenReturn(null);

    game.playTurn();

    verify(mockPlayer).setCurrentTile(mockLadderTile);
    ArgumentCaptor<DiceRolledEvent> diceEventCaptor =
        ArgumentCaptor.forClass(DiceRolledEvent.class);
    verify(mockObserver).onGameEvent(diceEventCaptor.capture());
    assertEquals(3, diceEventCaptor.getValue().getDiceValue());

    ArgumentCaptor<PlayerMovedEvent> moveEventCaptor =
        ArgumentCaptor.forClass(PlayerMovedEvent.class);
    verify(mockObserver, atLeastOnce()).onGameEvent(moveEventCaptor.capture());
    PlayerMovedEvent capturedMoveEvent =
        moveEventCaptor.getAllValues().stream()
            .filter(
                e -> e.getToTile().equals(mockLadderTile) && e.getFromTile().equals(mockStartTile))
            .findFirst()
            .orElse(null);
    assertNotNull(capturedMoveEvent, "PlayerMovedEvent for initial move not found");
    assertEquals(mockPlayer, capturedMoveEvent.getPlayer());
    assertEquals(mockStartTile, capturedMoveEvent.getFromTile());
    assertEquals(mockLadderTile, capturedMoveEvent.getToTile());
    assertEquals(3, capturedMoveEvent.getSteps());

    assertFalse(game.isGameOver());
  }

  @Test
  void playTurn_landOnEndTile_shouldEndGameAndNotify() {
    when(mockDice.roll()).thenReturn(10);
    when(mockPlayer.getDestinationTile(10)).thenReturn(mockEndTile);
    when(mockEndTile.getLandAction()).thenReturn(null);

    game.playTurn();

    verify(mockPlayer).setCurrentTile(mockEndTile);
    assertTrue(game.isGameOver());
    assertEquals(mockPlayer, game.getWinner());
    verify(mockObserver).onGameEvent(any(PlayerWonEvent.class));
    verify(mockObserver, atLeastOnce()).onGameEvent(any(GameEndedEvent.class));
  }

  @Test
  void playTurn_overshootEndTile_shouldBounceBackAndNotify() {
    when(mockDice.roll()).thenReturn(12);
    Tile mockBounceOrigin = mockEndTile;
    Tile mockBouncedToTile = mockSnakeTile;
    when(mockBoard.getTile(8)).thenReturn(mockBouncedToTile);
    when(mockBouncedToTile.getLandAction()).thenReturn(null);

    game.playTurn();

    verify(mockPlayer, times(1)).setCurrentTile(mockBounceOrigin);
    verify(mockPlayer, times(1)).setCurrentTile(mockBouncedToTile);

    ArgumentCaptor<PlayerMovedEvent> moveCaptor = ArgumentCaptor.forClass(PlayerMovedEvent.class);
    verify(mockObserver, atLeastOnce()).onGameEvent(moveCaptor.capture());
    assertTrue(
        moveCaptor.getAllValues().stream().anyMatch(e -> e.getToTile().equals(mockBounceOrigin)),
        "Move to end tile before bounce not found");

    ArgumentCaptor<BounceBackEvent> bounceCaptor = ArgumentCaptor.forClass(BounceBackEvent.class);
    verify(mockObserver).onGameEvent(bounceCaptor.capture());
    assertEquals(mockPlayer, bounceCaptor.getValue().getPlayer());
    assertEquals(mockBounceOrigin, bounceCaptor.getValue().getFromTile());
    assertEquals(mockBouncedToTile, bounceCaptor.getValue().getToTile());

    assertFalse(game.isGameOver());
  }

  @Test
  void playTurn_landOnLadder_shouldClimbAndNotify() {
    when(mockDice.roll()).thenReturn(3);
    when(mockPlayer.getDestinationTile(3)).thenReturn(mockLadderTile);
    LadderAction mockLadderAction = mock(LadderAction.class);
    when(mockLadderTile.getLandAction()).thenReturn(mockLadderAction);

    Tile mockLadderTopTile = mockRegularTile;

    doAnswer(
            invocation -> {
              Player p = invocation.getArgument(0);
              p.setCurrentTile(mockLadderTopTile);
              return null;
            })
        .when(mockLadderTile)
        .landPlayer(mockPlayer);

    game.playTurn();

    verify(mockLadderTile).landPlayer(mockPlayer);
    ArgumentCaptor<LadderClimbedEvent> ladderEventCaptor =
        ArgumentCaptor.forClass(LadderClimbedEvent.class);
    verify(mockObserver).onGameEvent(ladderEventCaptor.capture());
    assertEquals(mockPlayer, ladderEventCaptor.getValue().getPlayer());
    assertEquals(mockLadderTile, ladderEventCaptor.getValue().getFromTile());
    assertEquals(mockLadderTopTile, ladderEventCaptor.getValue().getToTile());
    assertFalse(game.isGameOver());
  }

  @Test
  void playTurn_landOnSnake_shouldSlideAndNotify() {
    when(mockDice.roll()).thenReturn(8);
    when(mockPlayer.getDestinationTile(8)).thenReturn(mockSnakeTile);

    SnakeAction mockSnakeAction = mock(SnakeAction.class);
    when(mockSnakeTile.getLandAction()).thenReturn(mockSnakeAction);

    doAnswer(
            invocation -> {
              Player p = invocation.getArgument(0);
              p.setCurrentTile(mockLadderTile);
              return null;
            })
        .when(mockSnakeTile)
        .landPlayer(mockPlayer);

    game.playTurn();

    verify(mockSnakeTile).landPlayer(mockPlayer);

    ArgumentCaptor<SnakeEncounteredEvent> snakeEventCaptor =
        ArgumentCaptor.forClass(SnakeEncounteredEvent.class);
    verify(mockObserver).onGameEvent(snakeEventCaptor.capture());
    assertEquals(mockPlayer, snakeEventCaptor.getValue().getPlayer());
    assertEquals(mockSnakeTile, snakeEventCaptor.getValue().getFromTile());
    assertEquals(mockLadderTile, snakeEventCaptor.getValue().getToTile());
    assertFalse(game.isGameOver());
  }

  @Test
  void playTurn_playerSkipsTurn_shouldNotMovePlayer() {
    when(mockPlayer.shouldSkipNextTurn()).thenReturn(true);

    game.playTurn();

    verify(mockPlayer).setSkipNextTurn(false);
    verify(mockDice, never()).roll();
    verify(mockPlayer, never()).getDestinationTile(anyInt());
    verify(mockObserver, never()).onGameEvent(any(DiceRolledEvent.class));
    verify(mockObserver, never()).onGameEvent(any(PlayerMovedEvent.class));
    assertFalse(game.isGameOver());
  }

  @Test
  void playTurn_destinationTileIsNull_shouldThrowException() {
    when(mockDice.roll()).thenReturn(5);
    when(mockPlayer.getDestinationTile(5)).thenReturn(null);

    Exception exception = assertThrows(InvalidGameStateException.class, () -> game.playTurn());
    assertTrue(exception.getMessage().contains("attempted to move to a null tile"));
    verify(mockObserver, never()).onGameEvent(any(PlayerMovedEvent.class));
  }

  @Test
  void playTurn_playerDoesNotBounceBackExactlyToStart_whenLandingOnEndTileWithExactRoll() {
    when(mockDice.roll()).thenReturn(10);
    when(mockPlayer.getDestinationTile(10)).thenReturn(mockEndTile);
    when(mockEndTile.getLandAction()).thenReturn(null);

    game.playTurn();

    verify(mockObserver, never()).onGameEvent(any(BounceBackEvent.class));
    assertTrue(game.isGameOver());
  }

  @Test
  void playTurn_overshootByOne_bouncesToOneBeforeEnd() {
    when(mockDice.roll()).thenReturn(11);
    when(mockBoard.getTile(9)).thenReturn(mockBounceTargetTile);
    when(mockBounceTargetTile.getLandAction()).thenReturn(null);

    game.playTurn();

    ArgumentCaptor<BounceBackEvent> bounceCaptor = ArgumentCaptor.forClass(BounceBackEvent.class);
    verify(mockObserver).onGameEvent(bounceCaptor.capture());
    assertEquals(mockBounceTargetTile, bounceCaptor.getValue().getToTile());
  }

  @Test
  void playTurn_withNullPlayerDestinationTile_shouldThrowAndNotNotifyMove() {
    when(mockDice.roll()).thenReturn(3);
    when(mockPlayer.getDestinationTile(3)).thenReturn(null);

    assertThrows(InvalidGameStateException.class, () -> game.playTurn());

    verify(mockObserver, never()).onGameEvent(any(PlayerMovedEvent.class));
    verify(mockObserver, never()).onGameEvent(any(LadderClimbedEvent.class));
    verify(mockObserver, never()).onGameEvent(any(SnakeEncounteredEvent.class));
    verify(mockObserver, never()).onGameEvent(any(PlayerWonEvent.class));
    verify(mockObserver, never()).onGameEvent(any(BounceBackEvent.class));
  }

  @Test
  void playTurn_endTileNotFound_throwsInvalidGameStateException() {
    when(mockBoard.getTile(mockBoard.getEndTileId())).thenReturn(null);
    when(mockDice.roll()).thenReturn(3);

    Exception exception = assertThrows(InvalidGameStateException.class, () -> game.playTurn());
    assertTrue(exception.getMessage().contains("End tile (ID: 10) not found on the board"));
  }

  @Test
  void playTurn_bouncedToNonExistentTile_throwsInvalidGameStateException() {
    when(mockDice.roll()).thenReturn(12);
    when(mockBoard.getTile(8)).thenReturn(null);

    Exception exception = assertThrows(InvalidGameStateException.class, () -> game.playTurn());
    assertTrue(exception.getMessage().contains("Bounced to a non-existent tile"));
  }

  @Test
  void playTurn_destinationTileIsNull_throwsInvalidGameStateException() {
    when(mockDice.roll()).thenReturn(5);
    when(mockPlayer.getDestinationTile(5)).thenReturn(null);

    Exception exception = assertThrows(InvalidGameStateException.class, () -> game.playTurn());
    assertTrue(exception.getMessage().contains("attempted to move to a null tile"));
  }
}
