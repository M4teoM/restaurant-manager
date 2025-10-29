package com.restaurant.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
