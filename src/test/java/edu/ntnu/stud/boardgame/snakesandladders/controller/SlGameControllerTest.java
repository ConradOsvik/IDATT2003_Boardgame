//package edu.ntnu.stud.boardgame.snakesandladders.controller;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import edu.ntnu.stud.boardgame.core.exception.GameNotInitializedException;
//import edu.ntnu.stud.boardgame.core.exception.GameOverException;
//import edu.ntnu.stud.boardgame.core.exception.InvalidPlayerException;
//import edu.ntnu.stud.boardgame.core.model.Tile;
//import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;
//import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
//import javafx.scene.paint.Color;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.testfx.framework.junit5.ApplicationExtension;
//
//@ExtendWith(ApplicationExtension.class)
//class SlGameControllerTest {
//
//  private SlGameController controller;
//  private SlBoardGame mockGame;
//
//  @BeforeEach
//  void setUp() {
//    controller = new SlGameController();
//    mockGame = mock(SlBoardGame.class);
//  }
//
//  @Test
//  void init_validGame_setsGame() {
//    controller.init(mockGame);
//
//    verify(mockGame).init();
//  }
//
//  @Test
//  void init_noArguments_throwsException() {
//    assertThrows(IllegalArgumentException.class, () -> controller.init());
//  }
//
//  @Test
//  void init_wrongArgType_throwsException() {
//    assertThrows(IllegalArgumentException.class, () -> controller.init("Not a game"));
//  }
//
//  @Test
//  void createNewGame_gameNotInitialized_throwsException() {
//    assertThrows(GameNotInitializedException.class, () -> controller.createNewGame());
//  }
//
//  @Test
//  void createNewGame_validGame_resetsGame() {
//    controller.init(mockGame);
//    controller.createNewGame();
//
//    verify(mockGame).reset();
//  }
//
//  @Test
//  void addPlayer_gameNotInitialized_throwsException() {
//    assertThrows(GameNotInitializedException.class,
//        () -> controller.addPlayer("Player", Color.RED));
//  }
//
//  @Test
//  void addPlayer_nullName_throwsException() {
//    controller.init(mockGame);
//
//    assertThrows(IllegalArgumentException.class, () -> controller.addPlayer(null, Color.RED));
//  }
//
//  @Test
//  void addPlayer_emptyName_throwsException() {
//    controller.init(mockGame);
//
//    assertThrows(IllegalArgumentException.class, () -> controller.addPlayer("", Color.RED));
//  }
//
//  @Test
//  void addPlayer_nullColor_throwsException() {
//    controller.init(mockGame);
//
//    assertThrows(IllegalArgumentException.class, () -> controller.addPlayer("Player", null));
//  }
//
//  @Test
//  void addPlayer_validArgs_addsPlayerToGame() {
//    controller.init(mockGame);
//    controller.addPlayer("Player", Color.RED);
//
//    verify(mockGame).addPlayer(any(SlPlayer.class));
//  }
//
//  @Test
//  void startGame_gameNotInitialized_throwsException() {
//    assertThrows(GameNotInitializedException.class, () -> controller.startGame());
//  }
//
//  @Test
//  void startGame_validGame_startsGame() {
//    SlBoardGame game = new SlBoardGame();
//    game.createBoard();
//    game.createDice(1);
//    SlPlayer player = new SlPlayer("Test", Color.RED);
//    game.addPlayer(player);
//
//    controller.init(game);
//    controller.startGame();
//
//    assertNotNull(game.getBoard());
//    assertFalse(game.getPlayers().isEmpty());
//  }
//
//  @Test
//  void startGame_noPlayers_throwsException() {
//    SlBoardGame game = new SlBoardGame();
//    game.createBoard();
//    game.createDice(1);
//
//    controller.init(game);
//
//    assertThrows(InvalidPlayerException.class, () -> controller.startGame());
//  }
//
//  @Test
//  void rollDiceAndTakeTurn_gameNotInitialized_throwsException() {
//    assertThrows(GameNotInitializedException.class, () -> controller.rollDiceAndTakeTurn());
//  }
//
//  @Test
//  void rollDiceAndTakeTurn_gameFinished_throwsException() {
//    SlBoardGame game = mock(SlBoardGame.class);
//    when(game.isFinished()).thenReturn(true);
//
//    controller.init(game);
//
//    assertThrows(GameOverException.class, () -> controller.rollDiceAndTakeTurn());
//  }
//
//  @Test
//  void rollDiceAndTakeTurn_validGame_playsCurrentPlayerTurn() {
//    SlBoardGame game = spy(new SlBoardGame());
//    game.createBoard();
//    game.createDice(1);
//    SlPlayer player = new SlPlayer("Test", Color.RED);
//
//    Tile startingTile = game.getBoard().getStartingTile();
//    player.setStartingTile(startingTile);
//
//    game.addPlayer(player);
//
//    controller.init(game);
//    controller.startGame();
//    controller.rollDiceAndTakeTurn();
//
//    verify(game).playTurn(player);
//  }
//}