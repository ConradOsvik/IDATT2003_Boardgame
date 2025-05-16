//package edu.ntnu.stud.boardgame.core.controller;
//
//import edu.ntnu.stud.boardgame.core.io.board.BoardFileReader;
//import edu.ntnu.stud.boardgame.core.io.board.BoardFileWriter;
//import edu.ntnu.stud.boardgame.core.io.player.PlayerFileReader;
//import edu.ntnu.stud.boardgame.core.io.player.PlayerFileWriter;
//import edu.ntnu.stud.boardgame.core.model.Board;
//import edu.ntnu.stud.boardgame.core.model.Player;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class FileController<B extends Board, P extends Player> {
//
//  private static final Logger LOGGER = Logger.getLogger(FileController.class.getName());
//
//  private final BoardFileReader<B> boardReader;
//  private final BoardFileWriter<B> boardWriter;
//  private final PlayerFileReader<P> playerReader;
//  private final PlayerFileWriter<P> playerWriter;
//
//  private final String boardsDirectory;
//  private final String playersFile;
//
//  public FileController(
//      BoardFileReader<B> boardReader,
//      BoardFileWriter<B> boardWriter,
//      PlayerFileReader<P> playerReader,
//      PlayerFileWriter<P> playerWriter,
//      String boardsDirectory,
//      String playersFile) {
//
//    this.boardReader = boardReader;
//    this.boardWriter = boardWriter;
//    this.playerReader = playerReader;
//    this.playerWriter = playerWriter;
//    this.boardsDirectory = boardsDirectory;
//    this.playersFile = playersFile;
//
//    File boardsDir = new File(boardsDirectory);
//    if (!boardsDir.exists()) {
//      boardsDir.mkdirs();
//    }
//  }
//
//  public List<P> loadPlayers() {
//    try {
//      return playerReader.read(playersFile);
//    } catch (IOException e) {
//      LOGGER.log(Level.WARNING, "Error loading players: {0}", e.getMessage());
//      return new ArrayList<>();
//    }
//  }
//
//  public boolean savePlayers(List<P> players) {
//    try {
//      playerWriter.write(playersFile, players);
//      return true;
//    } catch (IOException e) {
//      LOGGER.log(Level.WARNING, "Error saving players: {0}", e.getMessage());
//      return false;
//    }
//  }
//
//  public B loadBoard(String boardName) {
//    String filePath = boardsDirectory + boardName + ".json";
//    File file = new File(filePath);
//
//    if (!file.exists()) {
//      LOGGER.log(Level.WARNING, "Board file not found: {0}", filePath);
//      return null;
//    }
//
//    try {
//      return boardReader.read(filePath);
//    } catch (IOException e) {
//      LOGGER.log(Level.WARNING, "Error loading board: {0}", e.getMessage());
//      return null;
//    }
//  }
//
//  public boolean saveBoard(String boardName, B board) {
//    String filePath = boardsDirectory + boardName + ".json";
//
//    try {
//      boardWriter.write(filePath, board);
//      return true;
//    } catch (IOException e) {
//      LOGGER.log(Level.WARNING, "Error saving board: {0}", e.getMessage());
//      return false;
//    }
//  }
//
//  public List<String> listAvailableBoards() {
//    List<String> boardNames = new ArrayList<>();
//    File boardsDir = new File(boardsDirectory);
//
//    if (boardsDir.exists() && boardsDir.isDirectory()) {
//      File[] files = boardsDir.listFiles((dir, name) -> name.endsWith(".json"));
//
//      if (files != null) {
//        for (File file : files) {
//          String name = file.getName();
//          // Remove .json extension
//          boardNames.add(name.substring(0, name.length() - 5));
//        }
//      }
//    }
//
//    return boardNames;
//  }
//}