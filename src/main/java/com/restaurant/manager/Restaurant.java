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
public Restaurant(String name) {
this.name = name;
this.menu = new ArrayList<>();
this.totalRevenue = 0.0;
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
* Agrega un item al menú con su precio
* @param item Nombre del item
* @param price Precio del item
* @throws IllegalArgumentException si el item está vacío o el precio es nega
tivo
*/
public void addMenuItem(String item, double price) {
if (item == null || item.trim().isEmpty()) {
throw new IllegalArgumentException("El nombre del item no puede estar vacío");
}
if (price < 0) {
throw new IllegalArgumentException("El precio no puede ser negativo");
}
menu.add(item + " - $" + String.format("%.2f"
, price));
}
/**
* Remueve un item del menú por nombre
* @param item Nombre del item a remover
* @return true si el item fue removido, false si no existía
*/
public boolean removeMenuItem(String item) {
if (item == null || item.trim().isEmpty()) {
return false;
}
return menu.removeIf(menuItem -> menuItem.startsWith(item.trim()));
}
/**
* Obtiene el número de items en el menú
* @return cantidad de items
*/
public int getMenuSize() {
return menu.size();
}
}
// TODO: Agregar métodos para gestionar menú y órdenes
//