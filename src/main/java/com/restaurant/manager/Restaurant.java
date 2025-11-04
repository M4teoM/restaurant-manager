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

/**
* Constructor del restaurante
* @param name Nombre del restaurante
*/
private List<String> reservations;

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
/**
* Crea una nueva reserva
* @param customerName Nombre del cliente
* @param partySize Tamaño del grupo
* @param dateTime Fecha y hora de la reserva
* @throws IllegalArgumentException si los datos no son válidos
*/
public void makeReservation(String customerName, int partySize, String dateTime) {
if (customerName == null || customerName.trim().isEmpty()) {
throw new IllegalArgumentException("El nombre del cliente es requerido");
}
if (partySize <= 0) {
throw new IllegalArgumentException("El tamaño del grupo debe ser positivo");
}
if (dateTime == null || dateTime.trim().isEmpty()) {
throw new IllegalArgumentException("La fecha y hora son requeridas");
}
String reservation = String.format("%s - %d personas - %s",customerName, partySize, dateTime);reservations.add(reservation);
}
/**
* Obtiene todas las reservas
* @return lista de reservas
*/
public List<String> getReservations() {
return new ArrayList<>(reservations);
}
/**
* Obtiene el número de reservas activas
* @return cantidad de reservas
*/
public int getReservationCount() {
return reservations.size();
}
/**
* Cancela una reserva por nombre de cliente
* @param customerName Nombre del cliente
* @return true si se canceló, false si no existía
*/
public boolean cancelReservation(String customerName) {
if (customerName == null || customerName.trim().isEmpty()) {
return false;
}
return reservations.removeIf(res -> res.startsWith(customerName.trim()));
}
}
//