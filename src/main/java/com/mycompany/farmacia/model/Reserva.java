/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.model;

import java.time.LocalDateTime;

/**
 * Clase que representa una reserva de medicamento
 * @author josue
 */
public class Reserva {
    private String numeroTicket;
    private Persona persona;
    private Medicamento medicamento;
    private int cantidad;
    private boolean prioridad;
    private LocalDateTime fechaHora;
    private String estado; // "PENDIENTE", "ENTREGADO", "CANCELADO"
    
    public Reserva(String numeroTicket, Persona persona, Medicamento medicamento, 
                   int cantidad, boolean prioridad) {
        this.numeroTicket = numeroTicket;
        this.persona = persona;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        this.fechaHora = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
    
    // Constructor completo (para cargar desde archivo)
    public Reserva(String numeroTicket, Persona persona, Medicamento medicamento, 
                   int cantidad, boolean prioridad, LocalDateTime fechaHora, String estado) {
        this.numeroTicket = numeroTicket;
        this.persona = persona;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }
    
    // Getters
    public String getNumeroTicket() { return numeroTicket; }
    public Persona getPersona() { return persona; }
    public Medicamento getMedicamento() { return medicamento; }
    public int getCantidad() { return cantidad; }
    public boolean isPrioridad() { return prioridad; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getEstado() { return estado; }
    
    // Setters
    public void setEstado(String estado) { this.estado = estado; }
    
    /**
     * Retorna el nombre del medicamento
     */
    public String getNombreMedicamento() {
        return medicamento != null ? medicamento.getNombre() : "N/A";
    }
    
    /**
     * Retorna la fecha en formato legible
     */
    public String getFechaFormateada() {
        return fechaHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
