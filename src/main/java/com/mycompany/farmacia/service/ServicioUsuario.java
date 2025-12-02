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
import com.mycompany.farmacia.model.Empleado;
import com.mycompany.farmacia.model.Persona;
import com.mycompany.farmacia.model.Usuario;

public class ServicioUsuario {

    private BDUsuario bdUsuario;

    public ServicioUsuario() {
        bdUsuario = new BDUsuario();
    }

    // ----------------------------------------------------
    //   REGISTRO DE PERSONA
    // ----------------------------------------------------
    public boolean registrarPersona(Persona persona) {

        // evitar duplicados
        if (bdUsuario.buscarPorCedula(persona.getCedula()) != null) {
            System.out.println("La cédula ya existe. No se puede registrar.");
            return false;
        }

        bdUsuario.guardarPersona(persona);
        return true;
    }

    // ----------------------------------------------------
    //   REGISTRO DE EMPLEADO
    // ----------------------------------------------------
    public boolean registrarEmpleado(Empleado empleado) {

        // evitar duplicados
        if (bdUsuario.buscarPorCedula(empleado.getCedula()) != null) {
            System.out.println("La cédula ya existe. No se puede registrar.");
            return false;
        }

        bdUsuario.guardarEmpleado(empleado);
        return true;
    }

    // ----------------------------------------------------
    //   INICIO DE SESIÓN (cédula + contraseña)
    // ----------------------------------------------------
    public Usuario iniciarSesion(String cedula, String contrasena) {

        Usuario user = bdUsuario.buscarPorCedula(cedula);

        if (user == null) {
            System.out.println("Usuario no encontrado.");
            return null;
        }

        // validar contraseña
        if (!user.getPassword().equals(contrasena)) {
            System.out.println("Contraseña incorrecta.");
            return null;
        }

        return user; // éxito
    }

    // ----------------------------------------------------
    //   BUSCAR USUARIO POR CÉDULA
    // ----------------------------------------------------
    public Usuario buscarUsuario(String cedula) {
        return bdUsuario.buscarPorCedula(cedula);
    }
    public String validarLogin(String cedula, String password) {
    // 1. Leer todas las líneas del archivo de usuarios
    // 2. Iterar sobre cada línea.
    // 3. Si una línea coincide con la 'cedula' y la 'password' ingresadas:
    //    a. Extraer el rol (que debe estar guardado en la misma línea, por ejemplo, "CLIENTE|12345|Juan|EPS|pass" o "EMPLEADO|12345|...").
    //    b. Devolver el String del rol ("Cliente" o "Empleado").
    // 4. Si el bucle termina sin encontrar coincidencia, devolver null.
    
    // --- LÓGICA TEMPORAL DE EJEMPLO ---
    if ("123".equals(cedula) && "pass".equals(password)) {
        return "Cliente";
    }
    if ("987".equals(cedula) && "pass".equals(password)) {
        return "Empleado";
    }
    return null; // Credenciales inválidas
}
}

