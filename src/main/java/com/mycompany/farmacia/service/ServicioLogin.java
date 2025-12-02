/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.service;

/**
 *
 * @author josue
 */


import com.mycompany.farmacia.data.BDUsuario;
import com.mycompany.farmacia.model.Usuario;

public class ServicioLogin {

    private BDUsuario bdUsuario;

    public ServicioLogin() {
        bdUsuario = new BDUsuario();
    }

    // ------------------- INICIAR SESIÓN -------------------
    public Usuario iniciarSesion(String cedula, String passwordIngresada) {

        Usuario usuario = bdUsuario.buscarPorCedula(cedula);

        if (usuario == null) {
            System.out.println(" La cédula no se encuentra registrada.");
            return null;
        }

        if (!usuario.getPassword().equals(passwordIngresada)) {
            System.out.println(" Contraseña incorrecta.");
            return null;
        }

        return usuario;
    }
}


