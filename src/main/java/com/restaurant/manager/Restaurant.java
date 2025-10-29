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
}
// TODO: Agregar métodos para gestionar menú y órdenes
//