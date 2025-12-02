/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.model;

import java.time.LocalDateTime;

/**
 *
 * @author josue
 */
public class Tiquete {
    private Persona persona;
    private Medicamento medicamento;
    private int cantidad;
    private LocalDateTime fechaHora;
    private OrdenMedica ordenMedica;

    public Tiquete(Persona persona, Medicamento medicamento, int cantidad, OrdenMedica ordenMedica) {
        this.persona = persona;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.ordenMedica = ordenMedica;
        this.fechaHora = LocalDateTime.now();
    }

    public Persona getPersona() { return persona; }
    public Medicamento getMedicamento() { return medicamento; }
    public int getCantidad() { return cantidad; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public OrdenMedica getOrdenMedica() { return ordenMedica; }
}
