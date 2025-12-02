/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.model;

/**
 *
 * @author josue
 */
public class Medicamento {
    private String nombre;
    private String tipo;
    private String nombreComercial;
    private String nombreGenerico;
    private String dosis;
    private String formaFarmaceutica;
    private String frecuenciaDuracion;
    private String indicaciones;
    private String advertencias;
    private boolean requiereOrden;
    private int cantidadDisponible;
    
    // Constructor completo
    public Medicamento(String nombre, String tipo, String nombreComercial, String nombreGenerico,
                       String dosis, String formaFarmaceutica, String frecuenciaDuracion,
                       String indicaciones, String advertencias, boolean requiereOrden, int cantidadDisponible) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
        this.dosis = dosis;
        this.formaFarmaceutica = formaFarmaceutica;
        this.frecuenciaDuracion = frecuenciaDuracion;
        this.indicaciones = indicaciones;
        this.advertencias = advertencias;
        this.requiereOrden = requiereOrden;
        this.cantidadDisponible = cantidadDisponible;
    }
    
    // ✅ NUEVO: Constructor simplificado para cargar reservas
    public Medicamento(String nombre, String nombreComercial, String tipo, 
                       int cantidadDisponible, boolean requiereOrden) {
        this.nombre = nombre;
        this.nombreComercial = nombreComercial;
        this.tipo = tipo;
        this.cantidadDisponible = cantidadDisponible;
        this.requiereOrden = requiereOrden;
        this.nombreGenerico = "";
        this.dosis = "";
        this.formaFarmaceutica = "";
        this.frecuenciaDuracion = "";
        this.indicaciones = "";
        this.advertencias = "";
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getNombreComercial() { return nombreComercial; }
    public String getNombreGenerico() { return nombreGenerico; }
    public String getDosis() { return dosis; }
    public String getFormaFarmaceutica() { return formaFarmaceutica; }
    public String getFrecuenciaDuracion() { return frecuenciaDuracion; }
    public String getIndicaciones() { return indicaciones; }
    public String getAdvertencias() { return advertencias; }
    public boolean isRequiereOrden() { return requiereOrden; }
    public int getCantidadDisponible() { return cantidadDisponible; }
    
    // Setters
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
    
    // Descontar stock
    public void descontar(int cant) { this.cantidadDisponible -= cant; }
    
    @Override
    public String toString() {
        return nombre + " (" + nombreComercial + ") - " + tipo +
               " | Stock: " + cantidadDisponible +
               " | Orden médica: " + (requiereOrden ? "Sí" : "No");
    }
}



