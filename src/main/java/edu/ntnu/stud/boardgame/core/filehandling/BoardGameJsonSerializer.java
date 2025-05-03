package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.model.BoardGame;

/**
 * Base serializer for converting BoardGame objects to JSON format using GSON.
 * This class provides common functionality for all board game serializers.
 * Specific game types should extend this class to provide specialized
 * serialization.
 */
public abstract class BoardGameJsonSerializer implements Serializer<BoardGame> {

    /**
     * Serializes a BoardGame object to a JSON string.
     *
     * @param boardGame the board game to serialize
     * @return a JSON string representation of the board game
     */
    @Override
    public String serialize(BoardGame boardGame) {
        if (boardGame == null) {
            throw new IllegalArgumentException("Board game cannot be null");
        }

        JsonObject boardGameJson = new JsonObject();
        populateJsonObject(boardGame, boardGameJson);

        return boardGameJson.toString();
    }

    /**
     * Populates a JsonObject with data from a BoardGame.
     * Subclasses must implement this method to provide game-specific serialization.
     *
     * @param boardGame  the board game to serialize
     * @param jsonObject the JSON object to populate
     */
    protected abstract void populateJsonObject(BoardGame boardGame, JsonObject jsonObject);
}