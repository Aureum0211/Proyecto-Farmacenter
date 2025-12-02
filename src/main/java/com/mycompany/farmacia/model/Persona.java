/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.model;

/**
 *
 * @author josue
 */
public class Persona extends Usuario {

    private String eps;

    public Persona(String cedula, String nombre, String eps, String password) {
        super(cedula, nombre, password);
        this.eps = eps;
    }

    public String getEps() {
        return eps;
    }
}
