package edu.ntnu.stud.boardgame.service;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.io.board.BoardFileReader;
import edu.ntnu.stud.boardgame.io.board.BoardFileReaderGson;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriter;
import edu.ntnu.stud.boardgame.io.board.BoardFileWriterGson;
import edu.ntnu.stud.boardgame.model.Board;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class BoardFileService {

  private static final Logger LOGGER = Logger.getLogger(BoardFileService.class.getName());

  private final BoardFileReader boardReader;
  private final BoardFileWriter boardWriter;
  private final Path boardsDirectory;

  public BoardFileService() {
    this.boardReader = new BoardFileReaderGson();
    this.boardWriter = new BoardFileWriterGson();
    this.boardsDirectory = Paths.get("/data/boards");

    createDirectoryIfNotExists(boardsDirectory);
  }

  public Board loadBoard(String fileName) throws BoardFileException {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    Path boardPath = boardsDirectory.resolve(ensureFileExtension(fileName));

    if (!Files.exists(boardPath)) {
      throw new BoardFileException("Board file does not exist: " + boardPath);
    }

    try {
      LOGGER.info("Loading board from: " + boardPath);
      return boardReader.readBoard(boardPath);
    } catch (Exception e) {
      LOGGER.severe("Failed to load board from: " + boardPath + ". Error: " + e.getMessage());
      throw new BoardFileException("Failed to load board: " + e.getMessage(), e);
    }
  }

  public void saveBoard(String fileName, Board board) throws BoardFileException {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null.");
    }

    Path boardPath = boardsDirectory.resolve(ensureFileExtension(fileName));

    try {
      LOGGER.info("Saving board to: " + boardPath);
      boardWriter.writeBoard(boardPath, board);
    } catch (Exception e) {
      LOGGER.severe("Failed to save board to: " + boardPath + ". Error: " + e.getMessage());
      throw new BoardFileException("Failed to save board: " + e.getMessage(), e);
    }
  }

  public List<String> listAvailableBoards() {
    List<String> boardNames = new ArrayList<>();

    try {
      if (Files.exists(boardsDirectory) && Files.isDirectory(boardsDirectory)) {
        try (Stream<Path> pathStream = Files.list(boardsDirectory)) {
          pathStream
              .filter(path -> path.toString().toLowerCase().endsWith(".json"))
              .forEach(path -> {
                String fileName = path.getFileName().toString();
                boardNames.add(fileName.substring(0, fileName.length() - 5));
              });
        }
      }
    } catch (IOException e) {
      LOGGER.warning("Failed to list available boards. Error: " + e.getMessage());
    }

    return boardNames;
  }

  private void createDirectoryIfNotExists(Path directory) {
    try {
      if (!Files.exists(directory)) {
        Files.createDirectories(directory);
        LOGGER.info("Created directory: " + directory);
      }
    } catch (Exception e) {
      LOGGER.severe("Failed to create directory: " + directory + ". Error: " + e.getMessage());
    }
  }

  private String ensureFileExtension(String fileName) {
    if (!fileName.endsWith(".json")) {
      return fileName + ".json";
    }
    return fileName;
  }
}
