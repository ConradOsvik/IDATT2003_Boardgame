# Board Game Framework

A flexible, extensible framework for building board games in Java.

## Overview

This project provides a core framework for creating and playing various board games. It includes:

- Abstract base classes for common game concepts (BoardGame, Player, Tile, etc.)
- A modular serialization system for saving and loading games
- UI components for displaying and interacting with games
- A complete implementation of Snakes and Ladders as an example

## Serialization Architecture

The framework uses a flexible, extensible serialization system that makes it easy to add support for new game types:

### Core Components

- **Base Serializers**: Abstract classes provide common serialization logic:
  - `BoardGameJsonSerializer` for games (JSON format)
  - `PlayerCsvSerializer` for players (CSV format)
- **Game-specific Serializers**: Extensions of base serializers for specific game types (e.g., `SlBoardGameSerializer` for Snakes and Ladders)
- **Registry**: Central registries (`BoardGameSerializerRegistry`, `PlayerSerializerRegistry`) that manage game-specific serializers
- **Generic Serializers**: Top-level serializers that route to the appropriate game-specific serializer

### File Formats

- **Games**: Stored in JSON format
- **Players**: Stored in CSV format

### Adding a New Game Type

To add serialization support for a new game:

1. Create game-specific serializer and deserializer classes extending the base classes
2. Register these classes in `SerializerRegistryInitializer`

No changes to existing code are required - the system is designed to be open for extension but closed for modification.

Example for adding a Chess game:

```java
// 1. Create serializer extending the base class
public class ChessGameSerializer extends BoardGameJsonSerializer {
    @Override
    protected void populateJsonObject(BoardGame boardGame, JsonObject jsonObject) {
        // Chess-specific serialization logic
    }
}

// 2. Create deserializer extending the base class
public class ChessGameDeserializer extends BoardGameJsonDeserializer {
    @Override
    protected BoardGame createBoardGameFromJson(JsonObject jsonObject) {
        // Chess-specific deserialization logic
        return chessGame;
    }
}

// 3. Register in SerializerRegistryInitializer
BoardGameSerializerRegistry.register(
    ChessGame.class.getSimpleName(),
    new ChessGameSerializer(),
    new ChessGameDeserializer()
);
```

For player types, follow the same pattern but extend the CSV serializer classes:

```java
public class ChessPlayerSerializer extends PlayerCsvSerializer {
    @Override
    protected String serializePlayer(Player player) {
        // Convert player to CSV line
    }
}

public class ChessPlayerDeserializer extends PlayerCsvDeserializer {
    @Override
    protected Player deserializeLine(String line) {
        // Convert CSV line to player
    }
}
```

## Usage

[Add information about how to use the framework here]

## Dependencies

- JavaFX for UI components
- GSON for JSON serialization/deserialization

## License

[Add license information here]
