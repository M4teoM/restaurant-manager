package com.restaurant.manager;
import java.util.ArrayList;
import java.util.List;
/**
*
* Clase principal para gestionar un restaurante
* @author equipo 4
*/
public class Restaurant {
private String name;
private List<String> menu;
private double totalRevenue;
private List<String> reservations;
public void processOrder(String orderName, double amount) {
        totalRevenue += amount;
    }
    public void addMenuItem(String name, double price) {
            menu.add(name);
        }
public void makeReservation(String customerName, int partySize, String dateTime) {
    reservations.add(customerName + " - " + partySize + " personas - " + dateTime);
}
/**
* Constructor del restaurante
* @param name Nombre del restaurante
*/
public Restaurant(String name) {
this.name = name;
this.menu = new ArrayList<>();
this.totalRevenue = 0.0;
this.reservations = new ArrayList<>();
}

/**
* Obtiene el nombre del restaurante
* @return nombre del restaurante
*/
public String getName() {
return name;
}
/**
* Obtiene una copia del menú
* @return lista de items del menú
*/
public List<String> getMenu() {
return new ArrayList<>(menu);
}
/**
* Obtiene los ingresos totales
* @return ingresos acumulados
*/
public double getTotalRevenue() {
return totalRevenue;
}

private int orderCount = 0;

/**
 * Obtiene el número total de órdenes procesadas
 * @return número de órdenes
 */
public int getOrderCount() {
	return orderCount;
}
/**
 * Calcula el valor promedio de las órdenes
 * @return promedio de ingresos por orden
 */
public double getAverageOrderValue() {
	int orderCount = getOrderCount();
	if (orderCount == 0) {
		return 0.0;
	}
	return totalRevenue / orderCount;
}
/**
* Verifica si el restaurante está generando buenos ingresos
* @param threshold Umbral mínimo de ingresos
* @return true si los ingresos superan el umbral
*/
public boolean isPerformingWell(double threshold) {
return totalRevenue >= threshold;
}
/**
 * Obtiene un resumen del estado del restaurante
 * @return String con estadísticas
 */
public String getStatisticsSummary() {
	return String.format(
		"Restaurant: %s\n" +
		"Items en menú: %d\n" +
		"Reservas activas: %d\n" +
		"Órdenes procesadas: %d\n" +
		"Ingresos totales: $%.2f\n" +
		"Valor promedio por orden: $%.2f",
		name,
		menu.size(),
		reservations.size(),
		getOrderCount(),
		totalRevenue,
		getAverageOrderValue()
	);
}
public static void main(String[] args) {
    System.out.println("=== Restaurant Manager ===");
    DatabaseConnection.testConnection();

    Restaurant restaurant = new Restaurant("La Pizzeria");
    restaurant.addMenuItem("Pizza Margherita", 12.99);
    restaurant.addMenuItem("Pasta Carbonara", 10.50);
    restaurant.makeReservation("Carlos López", 4, "2025-11-04 19:00");

    System.out.println();
    System.out.println(restaurant.getStatisticsSummary());
}
}

