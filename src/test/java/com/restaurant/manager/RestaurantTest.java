package com.restaurant.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests para la clase Restaurant
 */
@DisplayName("Tests de Restaurant")
public class RestaurantTest {
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        restaurant = new Restaurant("La Pizzeria");
    }

    @Test
    @DisplayName("Test crear restaurante con nombre válido")
    public void testCreateRestaurant() {
        assertEquals("La Pizzeria", restaurant.getName());
        assertTrue(restaurant.getMenu().isEmpty());
        assertEquals(0.0, restaurant.getTotalRevenue(), 0.001);
    }
    @Test
@DisplayName("Test agregar item válido al menú")
public void testAddMenuItem() {
restaurant.addMenuItem("Pizza Margherita", 12.99);
assertEquals(1, restaurant.getMenuSize());
List<String> menu = restaurant.getMenu();
assertTrue(menu.get(0).contains("Pizza Margherita"));
assertTrue(menu.get(0).contains("12.99"));
}
@Test
@DisplayName("Test agregar múltiples items al menú")
public void testAddMultipleMenuItems() {
restaurant.addMenuItem("Pizza Margherita", 12.99);
restaurant.addMenuItem("Pasta Carbonara", 10.50);
restaurant.addMenuItem("Tiramisu", 6.99);
assertEquals(3, restaurant.getMenuSize());
}
@Test
@DisplayName("Test agregar item con nombre vacío lanza excepción")
public void testAddMenuItemEmptyName() {
assertThrows(IllegalArgumentException.class,
() -> restaurant.addMenuItem(""
, 10.0));
assertThrows(IllegalArgumentException.class,
() -> restaurant.addMenuItem(null, 10.0));
}
@Test
@DisplayName("Test agregar item con precio negativo lanza excepción")
public void testAddMenuItemNegativePrice() {
assertThrows(IllegalArgumentException.class,
() -> restaurant.addMenuItem("Pizza",
-5.0));
}
@Test
@DisplayName("Test remover item existente del menú")
public void testRemoveMenuItem() {
restaurant.addMenuItem("Pizza Margherita", 12.99);
restaurant.addMenuItem("Pasta Carbonara", 10.50);
boolean removed = restaurant.removeMenuItem("Pizza Margherita");
assertTrue(removed);
assertEquals(1, restaurant.getMenuSize());
assertFalse(restaurant.getMenu().get(0).contains("Pizza"));
}
@Test
@DisplayName("Test remover item inexistente retorna false")
public void testRemoveNonExistentMenuItem() {
restaurant.addMenuItem("Pizza", 12.99);
boolean removed = restaurant.removeMenuItem("Pasta");
assertFalse(removed);
assertEquals(1, restaurant.getMenuSize());
}
}
