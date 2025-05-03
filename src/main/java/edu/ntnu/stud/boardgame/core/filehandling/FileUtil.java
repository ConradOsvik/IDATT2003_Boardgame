package edu.ntnu.stud.boardgame.core.filehandling;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for file-related operations in the board game application.
 * This includes path definitions and common file operations.
 */
public class FileUtil {

    private static final String DATA_DIR = "user_data";
    private static final String GAMES_DIR = "games";
    private static final String PLAYERS_DIR = "players";
    private static final String CSV_EXTENSION = ".csv";
    private static final String JSON_EXTENSION = ".json";

    private FileUtil() {
    }

    /**
     * Returns the path to the data directory, creating it if it doesn't exist.
     *
     * @return the path to the data directory
     */
    public static Path getDataDirectory() {
        Path dataDir = Paths.get(DATA_DIR);
        ensureDirectoryExists(dataDir.toFile());
        return dataDir;
    }

    /**
     * Returns the path to the games directory, creating it if it doesn't exist.
     *
     * @return the path to the games directory
     */
    public static Path getGamesDirectory() {
        Path gamesDir = Paths.get(DATA_DIR, GAMES_DIR);
        ensureDirectoryExists(gamesDir.toFile());
        return gamesDir;
    }

    /**
     * Returns the path to the players directory, creating it if it doesn't exist.
     *
     * @return the path to the players directory
     */
    public static Path getPlayersDirectory() {
        Path playersDir = Paths.get(DATA_DIR, PLAYERS_DIR);
        ensureDirectoryExists(playersDir.toFile());
        return playersDir;
    }

    /**
     * Returns the path to a game file with the specified name.
     *
     * @param gameName the name of the game file (without extension)
     * @return the path to the game file
     */
    public static Path getGameFilePath(String gameName) {
        return getGamesDirectory().resolve(gameName + JSON_EXTENSION);
    }

    /**
     * Returns the path to a player file with the specified name.
     *
     * @param playerListName the name of the player list file (without extension)
     * @return the path to the player file
     */
    public static Path getPlayerFilePath(String playerListName) {
        return getPlayersDirectory().resolve(playerListName + CSV_EXTENSION);
    }

    /**
     * Lists all saved game files.
     *
     * @return a list of game file names (without extensions)
     */
    public static List<String> listGameFiles() {
        File gamesDir = getGamesDirectory().toFile();
        List<String> gameFiles = new ArrayList<>();

        if (gamesDir.exists() && gamesDir.isDirectory()) {
            File[] files = gamesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(JSON_EXTENSION));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    gameFiles.add(fileName.substring(0, fileName.lastIndexOf(".")));
                }
            }
        }

        return gameFiles;
    }

    /**
     * Lists all saved player files.
     *
     * @return a list of player file names (without extensions)
     */
    public static List<String> listPlayerFiles() {
        File playersDir = getPlayersDirectory().toFile();
        List<String> playerFiles = new ArrayList<>();

        if (playersDir.exists() && playersDir.isDirectory()) {
            File[] files = playersDir.listFiles((dir, name) -> name.toLowerCase().endsWith(CSV_EXTENSION));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    playerFiles.add(fileName.substring(0, fileName.lastIndexOf(".")));
                }
            }
        }

        return playerFiles;
    }

    /**
     * Ensures that the specified directory exists, creating it if necessary.
     *
     * @param directory the directory to ensure exists
     */
    private static void ensureDirectoryExists(File directory) {
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }
    }
}