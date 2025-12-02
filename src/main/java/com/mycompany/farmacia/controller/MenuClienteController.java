/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.model.Persona;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Controlador para el menú principal de Cliente (Persona)
 * @author josue
 */
public class MenuClienteController {
    
    @FXML private Label lblUsuario;
    
    private static Persona personaActual;
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        System.out.println("MenuClienteController inicializado");
        
        // Configurar el label con el nombre si ya existe la persona
        if (personaActual != null && lblUsuario != null) {
            lblUsuario.setText("Usuario: " + personaActual.getNombre());
        }
    }
    
    /**
     * Configura la persona actual que inició sesión (método estático)
     */
    public static void setPersonaActual(Persona persona) {
        personaActual = persona;
    }
    
    /**
     * Configura la persona actual (método de instancia)
     */
    public void setPersona(Persona persona) {
        personaActual = persona;
        if (lblUsuario != null && persona != null) {
            lblUsuario.setText("Usuario: " + persona.getNombre());
        }
    }
    
    /**
     * Abre la vista para consultar medicamentos disponibles
     */
    @FXML
    private void consultarMedicamentos() {
        try {
            // Guardar que venimos del menú de cliente
            ConsultarController.setVistaOrigen("menuCliente");
            
            // Navegar a la vista de consultar
            App.setRoot("Consultar");
            
        } catch (IOException e) {
            System.err.println("Error al abrir consultar medicamentos: " + e.getMessage());
            e.printStackTrace();
            mostrarError("No se pudo abrir la vista de consulta");
        }
    }
    
    /**
     * Abre la vista para reservar un medicamento
     */
    @FXML
    private void reservarMedicamento() {
        try {
            // Pasar la persona actual al controlador de reservar
            ReservarController.setPersonaActual(personaActual);
            
            // Navegar a la vista de reservar
            App.setRoot("Reservar");
            
        } catch (IOException e) {
            System.err.println("Error al abrir reservar medicamento: " + e.getMessage());
            e.printStackTrace();
            mostrarError("No se pudo abrir la vista de reserva");
        }
    }
    
    /**
 * Abre la vista para ver las reservas del cliente
 */
@FXML
private void verMisReservas() {
    try {
        // ✅ CAMBIADO: Ahora sí abre la vista de mis reservas
        MisReservasController.setPersonaActual(personaActual);
        App.setRoot("misReservas");
    } catch (IOException e) {
        System.err.println("Error al abrir mis reservas: " + e.getMessage());
        e.printStackTrace();
        mostrarError("No se pudo abrir la vista de reservas");
    }
}
    
    /**
     * Abre la vista del perfil del usuario
     */
    @FXML
    private void verPerfil() {
        try {
            // Pasar la persona actual al controlador de perfil
            PerfilController.setPersonaActual(personaActual);
            
            // Navegar a la vista de perfil
            App.setRoot("Perfil");
            
        } catch (IOException e) {
            System.err.println("Error al abrir perfil: " + e.getMessage());
            e.printStackTrace();
            mostrarError("No se pudo abrir la vista de perfil");
        }
    }
    
    /**
     * Cierra sesión y vuelve al login
     */
    @FXML
    private void cerrarSesion() {
        try {
            personaActual = null;
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