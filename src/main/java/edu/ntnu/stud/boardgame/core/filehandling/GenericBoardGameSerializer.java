package edu.ntnu.stud.boardgame.core.filehandling;

import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.model.BoardGame;

/**
 * A generic serializer for board games that delegates to specific serializers
 * based on the game type.
 */
public class GenericBoardGameSerializer implements Serializer<BoardGame> {

    /**
     * Serializes a board game to a JSON string, including type information.
     *
     * @param boardGame the board game to serialize
     * @return a JSON string representation of the board game
     */
    @Override
    public String serialize(BoardGame boardGame) {
        if (boardGame == null) {
            throw new IllegalArgumentException("Board game cannot be null");
        }

        String gameType = boardGame.getClass().getSimpleName();

        Serializer<BoardGame> serializer = BoardGameSerializerRegistry.getSerializer(gameType);

        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("gameType", gameType);
        wrapper.addProperty("data", serializer.serialize(boardGame));

        return wrapper.toString();
    }
}