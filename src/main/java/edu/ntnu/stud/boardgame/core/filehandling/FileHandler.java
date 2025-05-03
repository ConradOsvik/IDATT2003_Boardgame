package edu.ntnu.stud.boardgame.core.filehandling;

/**
 * Interface for defining file handling operations.
 * This interface provides general methods for reading from and writing to
 * files.
 */
public interface FileHandler {

    /**
     * Reads the content from a file at the specified path.
     *
     * @param filePath the path to the file to read from
     * @return the content of the file as a String
     * @throws java.io.IOException if there is an error reading the file
     */
    String readFromFile(String filePath) throws java.io.IOException;

    /**
     * Writes the content to a file at the specified path.
     *
     * @param filePath the path to the file to write to
     * @param content  the content to write to the file
     * @throws java.io.IOException if there is an error writing to the file
     */
    void writeToFile(String filePath, String content) throws java.io.IOException;
}