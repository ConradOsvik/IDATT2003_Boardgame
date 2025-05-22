package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class PropertyPurchasedEventTest {

  private final int price = 200;

  @Test
  void constructor_withValidArguments_shouldCreateEvent() {
    Player mockPlayer = mock(Player.class);
    Tile mockProperty = mock(Tile.class);
    PropertyPurchasedEvent event = new PropertyPurchasedEvent(mockPlayer, mockProperty, price);

    assertNotNull(event);
    assertEquals(EventType.PROPERTY_PURCHASED, event.getEventType());
    assertEquals(mockPlayer, event.getPlayer());
    assertEquals(mockProperty, event.getProperty());
    assertEquals(price, event.getPrice());
  }

  @Test
  void constructor_withNullPlayer_shouldThrowIllegalArgumentException() {
    Tile mockProperty = mock(Tile.class);
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new PropertyPurchasedEvent(null, mockProperty, price);
            });
    assertEquals("Player cannot be null for PropertyPurchasedEvent.", exception.getMessage());
  }

  @Test
  void constructor_withNullProperty_shouldThrowIllegalArgumentException() {
    Player mockPlayer = mock(Player.class);
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new PropertyPurchasedEvent(mockPlayer, null, price);
            });
    assertEquals("Property cannot be null for PropertyPurchasedEvent.", exception.getMessage());
  }

  @Test
  void getPlayer_shouldReturnCorrectPlayer() {
    Player mockPlayer = mock(Player.class);
    Tile mockProperty = mock(Tile.class);
    PropertyPurchasedEvent event = new PropertyPurchasedEvent(mockPlayer, mockProperty, price);
    assertEquals(mockPlayer, event.getPlayer());
  }

  @Test
  void getProperty_shouldReturnCorrectTile() {
    Player mockPlayer = mock(Player.class);
    Tile mockProperty = mock(Tile.class);
    PropertyPurchasedEvent event = new PropertyPurchasedEvent(mockPlayer, mockProperty, price);
    assertEquals(mockProperty, event.getProperty());
  }

  @Test
  void getPrice_shouldReturnCorrectPrice() {
    Player mockPlayer = mock(Player.class);
    Tile mockProperty = mock(Tile.class);
    PropertyPurchasedEvent event = new PropertyPurchasedEvent(mockPlayer, mockProperty, price);
    assertEquals(price, event.getPrice());
  }
}
