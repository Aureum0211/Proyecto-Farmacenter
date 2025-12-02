/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.model;

/**
 *
 * @author josue
 */
public class Empleado extends Usuario {

    private String codigoEmpleado;

    public Empleado(String cedula, String nombre, String codigoEmpleado, String password) {
        super(cedula, nombre, password);
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }
}