/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.data;

/**
 *
 * @author josue
 */

import com.mycompany.farmacia.model.Empleado;
import com.mycompany.farmacia.model.Persona;
import com.mycompany.farmacia.model.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BDUsuario {

    private static final String ARCHIVO = "usuarios.txt";

    public BDUsuario() {
        crearArchivoSiNoExiste();
    }

    // ---------------------------------------------------------
    // CREAR ARCHIVO SI NO EXISTE
    // ---------------------------------------------------------
    private void crearArchivoSiNoExiste() {
        File archivo = new File(ARCHIVO);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                System.out.println("Archivo usuarios.txt creado.");
            }
        } catch (IOException e) {
            System.out.println("Error creando archivo usuarios.txt: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // VERIFICAR SI YA EXISTE UNA CÉDULA
    // ---------------------------------------------------------
    public boolean existeCedula(String cedulaBuscada) {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length >= 1 && p[0].equals(cedulaBuscada)) {
                    return true;
                }
            }

        } catch (IOException e) {
            System.out.println("Error verificando duplicados: " + e.getMessage());
        }

        return false;
    }

    // ---------------------------------------------------------
    // GUARDAR PERSONA
    // ---------------------------------------------------------
    public boolean guardarPersona(Persona persona) {

        if (existeCedula(persona.getCedula())) {
            System.out.println("ERROR: La cédula ya está registrada.");
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {

            // Formato:
            // cedula;nombre;eps;password;PERSONA
            bw.write(persona.getCedula() + ";" +
                    persona.getNombre() + ";" +
                    persona.getEps() + ";" +
                    persona.getPassword() + ";" +
                    "PERSONA");
            bw.newLine();
            return true;

        } catch (IOException e) {
            System.out.println("Error guardando persona: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // GUARDAR EMPLEADO
    // ---------------------------------------------------------
    public boolean guardarEmpleado(Empleado empleado) {

        if (existeCedula(empleado.getCedula())) {
            System.out.println("ERROR: La cédula ya está registrada.");
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {

            // Formato:
            // cedula;nombre;codigoEmpleado;password;EMPLEADO
            bw.write(empleado.getCedula() + ";" +
                    empleado.getNombre() + ";" +
                    empleado.getCodigoEmpleado() + ";" +
                    empleado.getPassword() + ";" +
                    "EMPLEADO");
            bw.newLine();
            return true;

        } catch (IOException e) {
            System.out.println("Error guardando empleado: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // CARGAR TODOS LOS USUARIOS
    // ---------------------------------------------------------
    public List<Usuario> cargarUsuarios() {

        List<Usuario> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {

            String linea;
            while ((linea = br.readLine()) != null) {

                String[] p = linea.split(";");

                if (p.length == 5) {

                    String cedula = p[0];
                    String nombre = p[1];
                    String datoExtra = p[2];   // eps o codigoEmpleado
                    String password = p[3];
                    String tipo = p[4];

                    if (tipo.equals("PERSONA")) {
                        lista.add(new Persona(cedula, nombre, datoExtra, password));
                    } else if (tipo.equals("EMPLEADO")) {
                        lista.add(new Empleado(cedula, nombre, datoExtra, password));
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error cargando usuarios: " + e.getMessage());
        }

        return lista;
    }

    // ---------------------------------------------------------
    // BUSCAR USUARIO POR CÉDULA (LOGIN)
    // ---------------------------------------------------------
    public Usuario buscarPorCedula(String cedulaBuscada) {

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] p = linea.split(";");

                if (p.length == 5) {

                    String cedula = p[0];

                    if (cedula.equals(cedulaBuscada)) {

                        String nombre = p[1];
                        String datoExtra = p[2];
                        String password = p[3];
                        String tipo = p[4];

                        if (tipo.equals("PERSONA")) {
                            return new Persona(cedula, nombre, datoExtra, password);
                        } else if (tipo.equals("EMPLEADO")) {
                            return new Empleado(cedula, nombre, datoExtra, password);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer usuarios.txt: " + e.getMessage());
        }

        return null; // si no lo encuentra
    }
}

