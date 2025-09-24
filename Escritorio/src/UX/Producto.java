/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UX;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import UI.Conexion;

// Clase para representar un producto
class Producto {
    private int id;
    private String nombre;
    private String imagen;
    private double precio;
    private int cantidad;
    private String categoria;
    private String talla;
    private String color;
    private String material;
    private String coleccion;
    private String descripcion;
    
    // Constructor
    public Producto(int id, String nombre, String imagen, double precio, int cantidad, 
                   String categoria, String talla, String color, String material, 
                   String coleccion, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.talla = talla;
        this.color = color;
        this.material = material;
        this.coleccion = coleccion;
        this.descripcion = descripcion;
    }
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getImagen() { return imagen; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public String getCategoria() { return categoria; }
    public String getTalla() { return talla; }
    public String getColor() { return color; }
    public String getMaterial() { return material; }
    public String getColeccion() { return coleccion; }
    public String getDescripcion() { return descripcion; }
    
    @Override
    public String toString() {
        return nombre + " - " + color + " - $" + precio + " (ID: " + id + ")";
    }
}