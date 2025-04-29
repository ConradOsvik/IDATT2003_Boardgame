package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.view.BaseView;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.ComboBox;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.core.view.ui.Panel;
import edu.ntnu.stud.boardgame.core.view.ui.TextField;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SlGameView extends HBox implements BaseView, BoardGameObserver {

  private static final Logger LOGGER = Logger.getLogger(SlGameView.class.getName());

  private static final int BASE_CELL_SIZE = 60;

  private static final int BASE_PADDING = 20;

  private static final int CANVAS_PREF_WIDTH = SlBoard.BOARD_COLUMNS * BASE_CELL_SIZE;

  private static final int CANVAS_PREF_HEIGHT = SlBoard.BOARD_ROWS * BASE_CELL_SIZE;

  private final SlGameController controller;

  private final Pane canvasContainer;
  private final Canvas canvas;
  private final List<SlPlayer> players;
  private SlBoard board;
  private double scaleFactor = 1.0;
  private double currentCellSize;
  private double currentPadding;

  private Label titleLabel;
  private Label statusLabel;
  private Label currentPlayerLabel;
  private Label diceResultLabel;
  private TextField playerNameField;
  private ComboBox<ColorOption> tokenComboBox;
  private Button addPlayerButton;
  private Button newGameButton;
  private Button startGameButton;
  private Button rollDiceButton;

  public SlGameView(SlGameController controller) {
    this.controller = controller;
    this.setSpacing(10);
    this.canvasContainer = new Pane();
    this.canvas = new Canvas(CANVAS_PREF_WIDTH, CANVAS_PREF_HEIGHT);
    this.players = new ArrayList<>();

    HBox.setHgrow(canvasContainer, Priority.ALWAYS);

    canvasContainer.getChildren().add(canvas);

    canvas.widthProperty().bind(canvasContainer.widthProperty());
    canvas.heightProperty().bind(canvasContainer.heightProperty());

    canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
      calculateScaleFactors();
      draw();
    });

    canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
      calculateScaleFactors();
      draw();
    });
  }

  private static class ColorOption {

    private final Color color;
    private final String name;

    public ColorOption(Color color, String name) {
      this.color = color;
      this.name = name;
    }

    public Color getColor() {
      return color;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  @Override
  public void render() {
    canvasContainer.setPrefSize(CANVAS_PREF_WIDTH, CANVAS_PREF_HEIGHT);

    Panel controlPanel = setupComponents();

    getChildren().addAll(controlPanel, canvasContainer);

    calculateScaleFactors();
    draw();
  }

  private Panel setupComponents() {
    Panel controlPanel = new Panel();
    controlPanel.setPadding(new Insets(10));
    controlPanel.setPrefWidth(250);

    titleLabel = Label.builder().text("Snakes and Ladders").build();

    statusLabel = Label.builder().text("Status: Create a new game to begin").wrapText(true).build();
    currentPlayerLabel = Label.builder().text("Current Player: None").build();
    diceResultLabel = Label.builder().text("Dice: ").build();

    playerNameField = TextField.builder().promptText("Player Name").disabled(true).build();

    tokenComboBox = ComboBox.<ColorOption>builder()
        .items(
            new ColorOption(Color.RED, "Red"),
            new ColorOption(Color.BLUE, "Blue"),
            new ColorOption(Color.GREEN, "Green"),
            new ColorOption(Color.ORANGE, "Orange"),
            new ColorOption(Color.PURPLE, "Purple")
        )
        .promptText("Select token color").disabled(true).build();
    tokenComboBox.setPrefWidth(200);

    Label addPlayerLabel = Label.builder().text("Add new player").build();
    addPlayerButton = Button.builder().text("Add Player").styleClass("primary")
        .onAction(e -> addPlayerButtonHandler()).disabled(true).build();

    newGameButton = Button.builder().text("New Game").styleClass("primary").width(200)
        .onAction(e -> newGameButtonHandler()).build();

    startGameButton = Button.builder().text("Start Game").styleClass("success").width(200)
        .onAction(e -> startGameButtonHandler()).disabled(true).build();

    rollDiceButton = Button.builder().text("Roll Dice").styleClass("primary").width(200)
        .onAction(e -> rollDiceButtonHandler()).disabled(true).build();

    controlPanel.getChildren()
        .addAll(titleLabel, addPlayerLabel, playerNameField, tokenComboBox, addPlayerButton,
            new javafx.scene.control.Label(""), newGameButton, startGameButton, rollDiceButton,
            new javafx.scene.control.Label(""), currentPlayerLabel, diceResultLabel, statusLabel);

    return controlPanel;
  }

  private void addPlayerButtonHandler() {
    String name = playerNameField.getText().trim();
    ColorOption colorOption = tokenComboBox.getValue();

    if (name.isEmpty()) {
      showAlert("Please enter a player name", AlertType.ERROR);
      return;
    }

    if (colorOption == null) {
      showAlert("Please select a token color", AlertType.ERROR);
      return;
    }

    try {
      controller.addPlayer(name, colorOption.getColor());
      playerNameField.clear();
      tokenComboBox.setValue(null);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error adding player", e);
      showAlert("Error adding player: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void newGameButtonHandler() {
    try {
      controller.createNewGame();

      playerNameField.setDisable(false);
      tokenComboBox.setDisable(false);
      addPlayerButton.setDisable(false);
      startGameButton.setDisable(true);
      rollDiceButton.setDisable(true);

      playerNameField.clear();
      tokenComboBox.setValue(null);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error creating new game", e);
      showAlert("Error creating new game: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void startGameButtonHandler() {
    try {
      controller.startGame();

      playerNameField.setDisable(true);
      tokenComboBox.setDisable(true);
      addPlayerButton.setDisable(true);
      startGameButton.setDisable(true);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error starting game", e);
      showAlert("Error starting game: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void rollDiceButtonHandler() {
    try {
      controller.rollDiceAndTakeTurn();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error rolling dice", e);
      showAlert("Error rolling dice: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void showAlert(String message, AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(alertType.toString());
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void draw() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    if (board == null) {
      return;
    }

    int rows = board.getRows();
    int cols = board.getColumns();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    Map<Integer, Integer> snakes = board.getSnakes();
    Map<Integer, Integer> ladders = board.getLadders();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        double x = col * currentCellSize + currentPadding;
        double y = row * currentCellSize + currentPadding;

        boolean isSnake = snakes.containsKey(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols))
            || snakes.containsValue(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols));
        boolean isLadder = ladders.containsKey(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols))
            || ladders.containsValue(
            BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols));

        if (isSnake) {
          gc.setFill(Color.LIGHTCORAL);
        } else if (isLadder) {
          gc.setFill(Color.LIGHTGREEN);
        } else {
          gc.setFill(Color.YELLOW);
        }

        gc.fillRect(x, y, currentCellSize, currentCellSize);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, currentCellSize, currentCellSize);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12)); //TODO: use Inter font
        gc.fillText(String.valueOf(
                BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, rows, cols)), x + 5,
            y + 15);
      }
    }

    for (Map.Entry<Integer, Integer> snake : snakes.entrySet()) {
      drawSnake(gc, snake.getKey(), snake.getValue(), rows, cols);
    }

    for (Map.Entry<Integer, Integer> ladder : ladders.entrySet()) {
      drawLadder(gc, ladder.getKey(), ladder.getValue(), rows, cols);
    }

    for (SlPlayer player : players) {
      Tile currentTile = player.getCurrentTile();
      if (currentTile != null) {
        drawPlayerToken(gc, player, currentTile.getTileId(), rows, cols);
      }
    }
  }

  private void drawSnake(GraphicsContext gc, int headTileId, int tailTileId, int rows, int cols) {
    int[] headCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(headTileId, rows,
        cols);
    int[] tailCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(tailTileId, rows,
        cols);

    double headX = headCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double headY = headCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;
    double tailX = tailCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double tailY = tailCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;

    gc.setStroke(Color.RED);
    gc.setLineWidth(3);

    double controlX1 = (headX + tailX) / 2 + 30;
    double controlY1 = (headY + tailY) / 2 - 30;
    double controlX2 = (headX + tailX) / 2 - 30;
    double controlY2 = (headY + tailY) / 2 + 30;

    gc.beginPath();
    gc.moveTo(headX, headY);
    gc.bezierCurveTo(controlX1, controlY1, controlX2, controlY2, tailX, tailY);
    gc.stroke();

    gc.setFill(Color.RED);
    gc.fillOval(headX - 10, headY - 10, 20, 20);

    gc.setStroke(Color.RED);
    gc.setLineWidth(1);
    gc.strokeOval(tailX - 5, tailY - 5, 10, 10);
  }

  private void drawLadder(GraphicsContext gc, int bottomTileId, int topTileId, int rows, int cols) {
    int[] bottomCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(bottomTileId, rows,
        cols);
    int[] topCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(topTileId, rows,
        cols);

    double bottomX = bottomCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double bottomY = bottomCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;
    double topX = topCoords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double topY = topCoords[0] * currentCellSize + currentPadding + currentCellSize / 2;

    gc.setStroke(Color.GREEN);
    gc.setLineWidth(3);

    double spacing = 8;
    double dx = topX - bottomX;
    double dy = topY - bottomY;
    double length = Math.sqrt(dx * dx + dy * dy);
    double xNorm = dx / length;
    double yNorm = dy / length;

    double leftBottomX = bottomX - yNorm * spacing;
    double leftBottomY = bottomY + xNorm * spacing;
    double leftTopX = topX - yNorm * spacing;
    double leftTopY = topY + xNorm * spacing;

    gc.beginPath();
    gc.moveTo(leftBottomX, leftBottomY);
    gc.lineTo(leftTopX, leftTopY);
    gc.stroke();

    double rightBottomX = bottomX + yNorm * spacing;
    double rightBottomY = bottomY - xNorm * spacing;
    double rightTopX = topX + yNorm * spacing;
    double rightTopY = topY - xNorm * spacing;

    gc.beginPath();
    gc.moveTo(rightBottomX, rightBottomY);
    gc.lineTo(rightTopX, rightTopY);
    gc.stroke();

    gc.setLineWidth(2);
    int numRungs = (int) (length / (currentCellSize / 2)) + 1;

    for (int i = 0; i < numRungs; i++) {
      double t = i / (double) (numRungs - 1);
      double rungBottomX = leftBottomX * (1 - t) + leftTopX * t;
      double rungBottomY = leftBottomY * (1 - t) + leftTopY * t;
      double rungTopX = rightBottomX * (1 - t) + rightTopX * t;
      double rungTopY = rightBottomY * (1 - t) + rightTopY * t;

      gc.beginPath();
      gc.moveTo(rungBottomX, rungBottomY);
      gc.lineTo(rungTopX, rungTopY);
      gc.stroke();
    }
  }

  public void drawPlayerToken(GraphicsContext gc, SlPlayer player, int tileId, int rows, int cols) {
    int[] coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(tileId, rows, cols);

    double x = coords[1] * currentCellSize + currentPadding + currentCellSize / 2;
    double y = coords[0] * currentCellSize + currentPadding + currentCellSize / 2;

    gc.setFill(player.getColor());
    gc.fillOval(x - 10, y - 10, 20, 20);

    gc.setFill(Color.BLACK);
    gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12)); //TODO: use Inter font
    String initial = player.getName().substring(0, 1).toUpperCase();
    gc.fillText(initial, x - 10, y - 15);
  }

  private void calculateScaleFactors() {
    if (canvas.getWidth() <= 0 || canvas.getHeight() <= 0) {
      return;
    }

    if (board == null) {
      return;
    }

    int rows = board.getRows();
    int cols = board.getColumns();

    double baseWidth = cols * BASE_CELL_SIZE + 2 * BASE_PADDING;
    double baseHeight = rows * BASE_CELL_SIZE + 2 * BASE_PADDING;

    double scaleX = canvas.getWidth() / baseWidth;
    double scaleY = canvas.getHeight() / baseHeight;

    scaleFactor = Math.min(scaleX, scaleY);

    currentCellSize = BASE_CELL_SIZE * scaleFactor;
    currentPadding = BASE_PADDING * scaleFactor;
  }

  @Override
  public void onGameEvent(GameEvent event) {
    switch (event.getEventType()) {
      case PLAYER_ADDED -> {
        SlPlayer player = (SlPlayer) event.getData("player");
        if (player != null) {
          players.add(player);
          startGameButton.setDisable(false);
          statusLabel.setText("Status: Player " + player.getName() + " added");
          draw();
        }
      }
      case GAME_CREATED, GAME_RESET -> {
        players.clear();
        board = (SlBoard) event.getData("board");

        currentPlayerLabel.setText("Current Player: None");
        diceResultLabel.setText("Dice: ");
        statusLabel.setText("Status: Add players to start the game");

        draw();
      }
      case GAME_STARTED -> {
        statusLabel.setText("Status: Game started");
        rollDiceButton.setDisable(false);
      }
      case TURN_CHANGED -> {
        SlPlayer player = (SlPlayer) event.getData("player");
        if (player != null) {
          currentPlayerLabel.setText("Current Player: " + player.getName());
        }
      }
      case DICE_ROLLED -> {
        Integer result = (Integer) event.getData("result");
        if (result != null) {
          diceResultLabel.setText("Dice: " + result);
        }
      }
      case PLAYER_MOVED -> {
        SlPlayer player = (SlPlayer) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: " + player.getName() + " moved");
          draw();
        }
      }
      case SNAKE_ENCOUNTERED -> {
        SlPlayer player = (SlPlayer) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: " + player.getName() + " went down a snake!");
          draw();
        }
      }
      case LADDER_CLIMBED -> {
        SlPlayer player = (SlPlayer) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: " + player.getName() + " climbed a ladder!");
          draw();
        }
      }
      case PLAYER_WON -> {
        SlPlayer player = (SlPlayer) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: Game over! " + player.getName() + " wins!");
          rollDiceButton.setDisable(true);
          draw();
        }
      }
      default -> {
      }
    }
  }
}
