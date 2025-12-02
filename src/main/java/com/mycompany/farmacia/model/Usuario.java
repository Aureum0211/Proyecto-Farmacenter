/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.model;

/**
 *
 * @author josue
 */
public abstract class Usuario {

    protected String cedula;
    protected String nombre;
    protected String password;

    public Usuario(String cedula, String nombre, String password) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.password = password;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }
}
