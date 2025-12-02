/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.model.Empleado;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Controlador para el menú principal de Empleado
 * @author josue
 */
public class MenuEmpleadoController {
    
    @FXML private Label lblEmpleado;
    
    // ✅ CAMBIADO: Ahora es static para poder recibirlo desde el login
    private static Empleado empleadoActual;
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        System.out.println("MenuEmpleadoController inicializado");
        
        // ✅ AGREGADO: Mostrar el nombre del empleado si ya está configurado
        if (empleadoActual != null && lblEmpleado != null) {
            lblEmpleado.setText("Empleado: " + empleadoActual.getNombre());
        }
    }
    
    // ✅ AGREGADO: Método estático para recibir empleado desde el login
    public static void setPersonaActual(Empleado empleado) {
        empleadoActual = empleado;
    }
    
    /**
     * Configura el empleado actual que inició sesión
     */
    public void setEmpleado(Empleado empleado) {
        empleadoActual = empleado;
        if (lblEmpleado != null && empleado != null) {
            lblEmpleado.setText("Empleado: " + empleado.getNombre());
        }
    }
    
    /**
     * Abre la vista para gestionar medicamentos
     */
    @FXML
   private void gestionarMedicamentos() {
       try {
           App.setRoot("gestionarMedicamentos");
       } catch (IOException e) {
           System.err.println("Error: " + e.getMessage());
           mostrarError("No se pudo abrir gestionar medicamentos");
       }
   }
    
    /**
     * Abre la vista para ver reservas de clientes
     */
    @FXML
    private void verReservasClientes() {
        try {
            // Navegar a la vista de ver reservas de clientes
            App.setRoot("verReservasClientes");
            
        } catch (IOException e) {
            System.err.println("Error al abrir ver reservas: " + e.getMessage());
            e.printStackTrace();
            mostrarError("No se pudo abrir la vista de reservas.\n" +
                        "Verifica que el archivo verReservasClientes.fxml existe.");
        }
    }
    
    /**
     * Abre la vista para consultar stock de medicamentos
     * IMPORTANTE: Los empleados también pueden consultar medicamentos
     */
    @FXML
    private void consultarStock() {
        try {
            // ✅ AGREGADO: Guardar que venimos del menú empleado
            ConsultarController.setVistaOrigen("menuEmpleado");
            
            // Navegar a la vista de consultar
            App.setRoot("Consultar");
            
        } catch (IOException e) {
            System.err.println("Error al abrir consultar stock: " + e.getMessage());
            e.printStackTrace();
            mostrarError("No se pudo abrir la vista de consulta");
        }
    }
    
    /**
     * Abre la vista para registrar nuevos medicamentos
     */
    
    /**
     * Abre la vista para generar reportes
     */
    @FXML
    private void generarReportes() {
        mostrarMensajeNoImplementado("Generar Reportes");
        // TODO: Implementar cuando tengas la vista
        // App.setRoot("reportes");
    }
    
    /**
     * Cierra sesión y vuelve al login
     */
    @FXML
    private void cerrarSesion() {
        try {
            empleadoActual = null;
            App.setRoot("login");
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra un mensaje de funcionalidad no implementada
     */
    private void mostrarMensajeNoImplementado(String funcionalidad) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText(funcionalidad);
        alert.setContentText("Esta funcionalidad aún no ha sido implementada.\n" +
                           "Por ahora puedes consultar medicamentos.");
        alert.showAndWait();
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
