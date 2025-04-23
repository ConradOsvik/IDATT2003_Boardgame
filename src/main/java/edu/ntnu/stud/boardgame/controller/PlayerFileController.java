//package edu.ntnu.stud.boardgame.controller;
//
//import edu.ntnu.stud.boardgame.core.model._BoardGame;
//import edu.ntnu.stud.boardgame.core.model.Player;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
///**
// * Controller for saving and loading player data to/from CSV files.
// */
//public class PlayerFileController {
//
//    private static final String DELIMITER = ",";
//
//    /**
//     * Saves the list of players from a board game to a CSV file.
//     *
//     * @param boardGame The board game containing the players to save
//     * @param filePath The path where to save the CSV file
//     * @throws IOException If an I/O error occurs during the save operation
//     */
//    public void savePlayers(_BoardGame boardGame, String filePath) throws IOException {
//        List<Player> players = boardGame.getPlayers();
//        Path path = Paths.get(filePath);
//
//        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//            // Write header
//            writer.write("name,token");
//            writer.newLine();
//
//            // Write player data
//            for (Player player : players) {
//                writer.write(player.getName() + DELIMITER + player.getToken());
//                writer.newLine();
//            }
//        }
//    }
//
//    /**
//     * Loads players from a CSV file and adds them to the board game.
//     *
//     * @param boardGame The board game to add the loaded players to
//     * @param filePath The path of the CSV file to load
//     * @throws IOException If an I/O error occurs during the load operation
//     */
//    public void loadPlayers(_BoardGame boardGame, String filePath) throws IOException {
//        Path path = Paths.get(filePath);
//
//        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//            // Skip header
//            String line = reader.readLine();
//
//            // Read player data
//            while ((line = reader.readLine()) != null) {
//                line = line.trim();
//                if (!line.isEmpty()) {
//                    String[] parts = line.split(DELIMITER);
//                    if (parts.length == 2) {
//                        String name = parts[0].trim();
//                        String token = parts[1].trim();
//                        Player player = new Player(name, token, boardGame);
//                        boardGame.addPlayer(player);
//                    }
//                }
//            }
//        }
//    }
//}