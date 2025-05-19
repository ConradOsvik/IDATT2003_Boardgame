package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.StartAction;
import edu.ntnu.stud.boardgame.model.action.TaxAction;
import edu.ntnu.stud.boardgame.model.action.TileAction;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.observer.event.MoneyTransferEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerBankruptEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerWonEvent;
import edu.ntnu.stud.boardgame.observer.event.PropertyPurchasedEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MonopolyGame extends BoardGame {

  private static final Logger LOGGER = Logger.getLogger(MonopolyGame.class.getName());

  private static final int STARTING_MONEY = 1500;
  private static final int START_BONUS = 50;

  private final Map<Player, Integer> playerMoney = new HashMap<>();
  private final List<Player> bankruptPlayers = new ArrayList<>();

  @Override
  public Board createDefaultBoard() {
    Board board = new Board("Monopoly", "A simplified Monopoly game",
        11, 11, 0, 39);

    initializeBoard(board);

    return board;
  }

  @Override
  public void startGame() {
    super.startGame();

    playerMoney.clear();
    bankruptPlayers.clear();

    for (Player player : players) {
      playerMoney.put(player, STARTING_MONEY);
    }
  }

  @Override
  public void playTurn() {
    if (gameOver) {
      return;
    }

    if (bankruptPlayers.contains(currentPlayer)) {
      LOGGER.info(currentPlayer.getName() + " is bankrupt and skips their turn.");
      nextTurn();
      return;
    }

    int steps = dice.roll();
    notifyObservers(new DiceRolledEvent(steps, currentPlayer));

    Tile fromTile = currentPlayer.getCurrentTile();
    int startPosition = fromTile.getTileId();

    Tile toTile = currentPlayer.getDestinationTile(steps);
    int endPosition = toTile.getTileId();

    if (endPosition < startPosition && endPosition != 0) {
      receiveStartMoney(currentPlayer, START_BONUS);
    }

    currentPlayer.placeOnTile(toTile);
    notifyObservers(new PlayerMovedEvent(currentPlayer, fromTile, toTile, steps, board));

    checkGameEnd();
  }

  @Override
  public void nextTurn() {
    if (gameOver) {
      return;
    }

    int originalIndex = currentPlayerIndex;

    do {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
      currentPlayer = players.get(currentPlayerIndex);

      if (currentPlayerIndex == originalIndex && bankruptPlayers.contains(currentPlayer)) {
        LOGGER.warning("All players are bankrupt! Ending game.");
        endGame(null);
        return;
      }
    } while (bankruptPlayers.contains(currentPlayer) || currentPlayer.shouldSkipNextTurn());

    if (currentPlayer.shouldSkipNextTurn()) {
      currentPlayer.setSkipNextTurn(false);
      nextTurn();
      return;
    }

    notifyObservers(new TurnChangedEvent(currentPlayer));
  }

  public boolean buyProperty(Player player, Tile property) {
    if (player == null || property == null) {
      return false;
    }

    TileAction action = property.getLandAction();
    if (!(action instanceof PropertyAction propertyAction)) {
      return false;
    }

    if (propertyAction.getOwner() != null) {
      return false;
    }

    int price = propertyAction.getPrice();
    int playerMoney = getPlayerMoney(player);

    if (playerMoney < price) {
      return false;
    }

    this.playerMoney.put(player, playerMoney - price);

    propertyAction.setOwner(player);

    LOGGER.info(player.getName() + " bought property for $" + price);

    notifyObservers(new PropertyPurchasedEvent(player, property, price));

    return true;
  }

  public void payRent(Player tenant, Player owner, int amount) {
    if (tenant == null || owner == null || amount <= 0) {
      return;
    }

    int tenantMoney = playerMoney.getOrDefault(tenant, 0);
    int ownerMoney = playerMoney.getOrDefault(owner, 0);

    int actualPayment = Math.min(tenantMoney, amount);

    playerMoney.put(tenant, tenantMoney - actualPayment);
    playerMoney.put(owner, ownerMoney + actualPayment);

    LOGGER.info(tenant.getName() + " paid $" + actualPayment + " rent to " + owner.getName());

    notifyObservers(new MoneyTransferEvent(tenant, owner, actualPayment, "rent"));

    if (tenantMoney - actualPayment <= 0) {
      bankruptPlayers.add(tenant);
      LOGGER.info(tenant.getName() + " is bankrupt!");

      notifyObservers(new PlayerBankruptEvent(tenant));

      checkGameEnd();
    }
  }

  public void payTax(Player player, int amount) {
    if (player == null || amount <= 0) {
      return;
    }

    int money = playerMoney.getOrDefault(player, 0);

    int actualPayment = Math.min(money, amount);

    playerMoney.put(player, money - actualPayment);

    LOGGER.info(player.getName() + " paid $" + actualPayment + " in tax");

    notifyObservers(new MoneyTransferEvent(player, null, actualPayment, "tax"));

    if (money - actualPayment <= 0) {
      bankruptPlayers.add(player);
      LOGGER.info(player.getName() + " is bankrupt!");

      notifyObservers(new PlayerBankruptEvent(player));

      checkGameEnd();
    }
  }

  public void receiveStartMoney(Player player, int amount) {
    if (player == null || amount <= 0) {
      return;
    }

    int money = playerMoney.getOrDefault(player, 0);

    playerMoney.put(player, money + amount);

    LOGGER.info(player.getName() + " received $" + amount + " for passing GO");

    notifyObservers(new MoneyTransferEvent(null, player, amount, "passing GO"));
  }

  private void checkGameEnd() {
    int activePlayers = 0;
    Player lastActivePlayer = null;

    for (Player player : players) {
      if (!bankruptPlayers.contains(player)) {
        activePlayers++;
        lastActivePlayer = player;
      }
    }

    if (activePlayers == 1 && lastActivePlayer != null) {
      endGame(lastActivePlayer);
    }
  }

  private void initializeBoard(Board board) {
    Tile startTile = new Tile(0);
    startTile.setRow(10);
    startTile.setColumn(10);
    startTile.setName("GO");
    startTile.setLandAction(new StartAction(START_BONUS, this));
    board.addTile(startTile);

    for (int i = 1; i < 40; i++) {
      Tile tile = new Tile(i);

      int row, col;
      if (i <= 10) {
        row = 10;
        col = 10 - i;
      } else if (i <= 20) {
        row = 20 - i;
        col = 0;
      } else if (i <= 30) {
        row = 0;
        col = i - 20;
      } else {
        row = i - 30;
        col = 10;
      }

      tile.setRow(row);
      tile.setColumn(col);

      if (i % 5 == 0) {
        int taxAmount = 100;
        tile.setName("Tax $" + taxAmount);
        tile.setLandAction(new TaxAction(taxAmount, this));
      } else {
        int propertyPrice = 50 + i * 10;
        tile.setName("Property $" + propertyPrice);
        tile.setLandAction(new PropertyAction(propertyPrice, this));
      }

      board.addTile(tile);
    }

    for (int i = 0; i < 39; i++) {
      Tile currentTile = board.getTile(i);
      Tile nextTile = board.getTile(i + 1);
      currentTile.setNextTile(nextTile);
    }

    Tile lastTile = board.getTile(39);
    Tile firstTile = board.getTile(0);
    lastTile.setNextTile(firstTile);
  }

  public int getPlayerMoney(Player player) {
    return playerMoney.getOrDefault(player, 0);
  }

  public boolean isBankrupt(Player player) {
    return bankruptPlayers.contains(player);
  }

  public List<Player> getBankruptPlayers() {
    return new ArrayList<>(bankruptPlayers);
  }

  @Override
  protected void endGame(Player winner) {
    this.winner = winner;
    this.gameOver = true;

    if (winner != null) {
      notifyObservers(new PlayerWonEvent(winner));
    }
    notifyObservers(new GameEndedEvent(winner));
  }
}