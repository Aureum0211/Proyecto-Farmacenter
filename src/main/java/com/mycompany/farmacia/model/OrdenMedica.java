/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.model;

/**
 *
 * @author josue
 */
public class OrdenMedica {
    private String codigoMedico;
    private String entidad;

    public OrdenMedica(String codigoMedico, String entidad) {
        this.codigoMedico = codigoMedico;
        this.entidad = entidad;
    }

    public String getCodigoMedico() { return codigoMedico; }
    public String getEntidad() { return entidad; }
}
