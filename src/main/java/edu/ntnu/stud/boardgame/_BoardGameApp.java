//package edu.ntnu.stud.boardgame;
//
//import edu.ntnu.stud.boardgame.controller.BoardFileController;
//import edu.ntnu.stud.boardgame.controller.PlayerFileController;
//import edu.ntnu.stud.boardgame.core.model._BoardGame;
//import edu.ntnu.stud.boardgame.core.model.Player;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class _BoardGameApp {
//
//  private _BoardGame boardGame = new _BoardGame();
//  private final PlayerFileController playerFileController = new PlayerFileController();
//  private final BoardFileController boardFileController = new BoardFileController();
//
//  public void start() {
//    createDataDirectory();
//    setupGameFromScratch();
//    saveGameDataToFiles();
//
//    // Reset the game
//    this.boardGame = new _BoardGame();
//
//    // 2. Load from files
//    loadGameDataFromFiles();
//
//    // Start playing the game
//    startGameplay();
//  }
//
//  private void createDataDirectory() {
//    Path dataDir = Paths.get("data");
//    if (!Files.exists(dataDir)) {
//      try {
//        Files.createDirectory(dataDir);
//      } catch (IOException e) {
//        System.err.println("Failed to create data directory: " + e.getMessage());
//      }
//    }
//  }
//
//  private void setupGameFromScratch() {
//    System.out.println("Setting up a new game from scratch...");
//
//    this.boardGame.createBoard();
//    this.boardGame.createDice(2);
//
//    this.boardGame.addPlayer(new Player("Arne", "TopHat", this.boardGame));
//    this.boardGame.addPlayer(new Player("Ivar", "RaceCar", this.boardGame));
//    this.boardGame.addPlayer(new Player("Majid", "Cat", this.boardGame));
//    this.boardGame.addPlayer(new Player("Atle", "Thimble", this.boardGame));
//
//    System.out.println("Game setup completed.");
//    listPlayers();
//  }
//
//  private void saveGameDataToFiles() {
//    System.out.println("\nSaving game data to files...");
//
//    try {
//      // Save players to CSV
//      playerFileController.savePlayers(boardGame, "data/players.csv");
//      System.out.println("Players saved to data/players.csv");
//
//      // Save board to JSON
//      boardFileController.saveBoard(boardGame, "data/board.json");
//      System.out.println("Board saved to data/board.json");
//    } catch (IOException e) {
//      System.err.println("Error saving game data: " + e.getMessage());
//    }
//  }
//
//  private void loadGameDataFromFiles() {
//    System.out.println("\nLoading game data from files...");
//
//    try {
//      // Check if files exist
//      File playersFile = new File("data/players.csv");
//      File boardFile = new File("data/board.json");
//
//      if (!playersFile.exists() || !boardFile.exists()) {
//        System.err.println("Game data files not found. Run the game once to create them.");
//        return;
//      }
//
//      // Load board from JSON
//      boardFileController.loadBoard(boardGame, "data/board.json");
//      System.out.println("Board loaded from data/board.json");
//
//      // Load players from CSV
//      this.boardGame.createDice(2);
//      playerFileController.loadPlayers(boardGame, "data/players.csv");
//      System.out.println("Players loaded from data/players.csv");
//
//      System.out.println("Game data loaded successfully.");
//      listPlayers();
//    } catch (IOException e) {
//      System.err.println("Error loading game data: " + e.getMessage());
//    }
//  }
//
//  private void startGameplay() {
//    System.out.println("\nStarting the game...");
//
//    int roundNumber = 1;
//    while (!this.boardGame.isFinished()) {
//      System.out.println("Round number " + roundNumber++);
//      this.boardGame.play();
//
//      if (!this.boardGame.isFinished()) {
//        showPlayerStatus();
//      }
//
//      System.out.println();
//    }
//
//    System.out.println("And the winner is: " + this.boardGame.getWinner().getName());
//  }
//
//  private void listPlayers() {
//    System.out.println("The following players are playing the game:");
//    for (Player player : boardGame.getPlayers()) {
//      System.out.println("Name: " + player.getName() + ", Token: " + player.getToken());
//    }
//    System.out.println();
//  }
//
//  private void showPlayerStatus() {
//    for (Player player : boardGame.getPlayers()) {
//      System.out.println("Player " + player.getName() +
//          " on tile " + player.getCurrentTile().getTileId());
//    }
//  }
//
//  public static void main(String[] args) {
//    _BoardGameApp app = new _BoardGameApp();
//    app.start();
//  }
//}