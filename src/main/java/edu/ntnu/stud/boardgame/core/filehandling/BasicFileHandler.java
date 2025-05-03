package edu.ntnu.stud.boardgame.core.filehandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Basic implementation of the FileHandler interface.
 * This class provides methods for reading from and writing to files.
 */
public class BasicFileHandler implements FileHandler {

    /**
     * Reads the content from a file at the specified path.
     *
     * @param filePath the path to the file to read from
     * @return the content of the file as a String
     * @throws IOException if there is an error reading the file
     */
    @Override
    public String readFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File does not exist: " + filePath);
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    /**
     * Writes the content to a file at the specified path.
     *
     * @param filePath the path to the file to write to
     * @param content  the content to write to the file
     * @throws IOException if there is an error writing to the file
     */
    @Override
    public void writeToFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Path directory = path.getParent();

        if (directory != null && !Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}