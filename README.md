# IDATT2003 - Boardgame

## Students

| Name                       | Student ID |
|----------------------------|------------|
| Conrad Tinius Osvik        | 590789     |
| Embret Olav Rasmussen Roås | 111762     |

## Project Description

This project is a GUI-based board game application that supports multiple board game types. Built
with JavaFX, the application allows users to play various board games with customizable features. It
includes player management, game state tracking, and support for different game rules and actions.

The application currently supports:

- **Snakes and Ladders** - Classic game with ladders, snakes, and special actions
- **Simplified Monopoly** - Simplified Monopoly with property buying and money management

### Features

- 🎮 Multiple board game types with different rule sets
- 👥 Player management with customizable pieces and names
- 💾 Save/load player configurations and board layouts
- 🔊 Sound effects and audio controls
- ✨ Smooth animations for player movements
- 📁 File-based persistence for game data
- 🎨 Modern JavaFX user interface
- 🏗️ Extensible architecture using design patterns

## System Requirements

- Java JDK 21 or higher
- JavaFX 23.0.1 or higher
- Maven 3.6.0 or higher for building and running (alternatively, IDE with Maven support)

## Project Structure

```
.
├── data/                          # Game data storage
│   ├── boards/                    # Board configurations
│   │   ├── ladder/                # Snakes and Ladders boards
│   │   └── monopoly/              # Monopoly boards
│   └── players/                   # Player data
│       └── json_players.csv       # Saved player configurations
├── src/
│   ├── main/
│   │   ├── java/                  # Java source code
│   │   │   └── edu/ntnu/stud/boardgame/  # Main package
│   │   │       ├── controller/    # Game controllers
│   │   │       ├── exception/     # Custom exceptions
│   │   │       ├── factory/       # Game creation factories
│   │   │       ├── io/            # File I/O operations
│   │   │       ├── model/         # Game models and logic
│   │   │       ├── observer/      # Observer pattern implementation
│   │   │       ├── service/       # Game services
│   │   │       └── view/          # JavaFX UI components
│   │   └── resources/             # Application resources
│   │       ├── fonts/             # Custom fonts
│   │       ├── images/            # Game images and icons
│   │       ├── sounds/            # Sound effects
│   │       └── styles/            # CSS stylesheets
│   └── test/
│       └── java/                  # Test classes
│           └── edu/ntnu/stud/boardgame/  # Test package
└── pom.xml                        # Maven configuration
```

## Link to Repository

[Boardgame Project Repository](https://github.com/ConradOsvik/IDATT2003_Boardgame)

## How to Run the Project

Clone the repository and navigate to the project directory. You can run the project using Maven

### Compile

```bash
mvn clean compile
```

### Run tests

```bash
mvn clean test
```

### Package project

```bash
mvn clean package
```

Builds a Shaded JAR file with all dependencies included. The JAR file will be located in the `target/`

### Run JavaFX

```bash
mvn clean javafx:run
```

### Generate JavaDoc

```bash
mvn clean javadoc:javadoc
```

After running this command, JavaDocs will be available in the `target/site/apidocs/` directory.

### Generate JaCoCo report

```bash
mvn clean test jacoco:report
```

After running this command, the JaCoCo report will be available in the `target/site/jacoco/`
directory.