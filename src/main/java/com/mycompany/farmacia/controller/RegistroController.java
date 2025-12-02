/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.model.Persona;
// IMPORTANTE: Debes asegurarte de que esta clase exista en tu paquete model
import com.mycompany.farmacia.model.Empleado; 
import com.mycompany.farmacia.service.ServicioUsuario;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controlador para la vista de registro de nuevos usuarios (Personas o Empleados).
 * Implementa la lógica de validación y registro de usuarios.
 * @author josue
 */
public class RegistroController {

    // Campos del FXML (Registro.fxml)
    @FXML private ComboBox<String> cmbTipoUsuario;
    @FXML private Label lblDatoAdicional; // Etiqueta que cambia (EPS o Cargo)
    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtDatoAdicional; // Reemplaza txtEps. Dato extra requerido
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Text lblMensaje; 
    
    // Servicio de lógica de negocio
    private ServicioUsuario servicioUsuario = new ServicioUsuario();

    @FXML
    public void initialize() {
        // Inicializar el ComboBox con las opciones de registro
        cmbTipoUsuario.setItems(FXCollections.observableArrayList("Cliente", "Empleado"));
        // Establecer un valor por defecto al iniciar
        cmbTipoUsuario.getSelectionModel().select("Cliente");
        // Inicializar el mensaje de error y el texto de la etiqueta adicional
        lblMensaje.setText(""); 
        lblDatoAdicional.setText("Nombre de EPS:");
    }
    
    /**
     * Maneja la acción del ComboBox: cambia la etiqueta del campo adicional 
     * según si es Cliente (EPS) o Empleado (Cargo/ID).
     */
    @FXML
    private void cambiarTipoRegistro() {
        String tipo = cmbTipoUsuario.getSelectionModel().getSelectedItem();
        if ("Cliente".equals(tipo)) {
            lblDatoAdicional.setText("Nombre de EPS:");
            txtDatoAdicional.setPromptText("Ingrese el nombre de su EPS");
        } else if ("Empleado".equals(tipo)) {
            lblDatoAdicional.setText("Cargo/Rol:"); // Asumimos que el empleado requiere un Cargo
            txtDatoAdicional.setPromptText("Ej: 'Farmacéutico', 'Gerente'");
        }
        lblMensaje.setText(""); // Limpiar mensajes al cambiar de tipo
        txtDatoAdicional.clear(); // Limpiar el campo para evitar errores de datos
    }

    /**
     * Maneja la acción del botón REGISTRARSE.
     * Recoge los datos, valida y llama al servicio de registro, creando 
     * el objeto Persona o Empleado según la selección.
     */
    @FXML
    private void registrarUsuario() {
        
        String tipoRegistro = cmbTipoUsuario.getSelectionModel().getSelectedItem();
        
        // 1. Obtener valores
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String datoAdicional = txtDatoAdicional.getText().trim(); 
        
        // 2. Validación de campos obligatorios básicos
        if (tipoRegistro == null || cedula.isEmpty() || nombre.isEmpty() || password.isEmpty() || datoAdicional.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos los campos y seleccione el Tipo de Registro.");
            return;
        }

        // 3. Validación de contraseñas
        if (!password.equals(confirmPassword)) {
            lblMensaje.setText("Las contraseñas no coinciden.");
            return;
        }
        
        boolean registroExitoso = false;
        
        // 4. (Lógica de negocio) Intentar registrar
        try {
            if ("Cliente".equals(tipoRegistro)) {
                // Registro de Cliente (Persona)
                // Se usa el constructor: Persona(cedula, nombre, eps, password)
                Persona nuevaPersona = new Persona(cedula, nombre, datoAdicional, password);
                registroExitoso = servicioUsuario.registrarPersona(nuevaPersona);
                
            } else if ("Empleado".equals(tipoRegistro)) {
                // Registro de Empleado
                // Se usa el constructor: Empleado(cedula, nombre, cargo, password)
                Empleado nuevoEmpleado = new Empleado(cedula, nombre, datoAdicional, password); 
                registroExitoso = servicioUsuario.registrarEmpleado(nuevoEmpleado);
            } else {
                lblMensaje.setText("Tipo de registro no válido.");
                return;
            }

            if (registroExitoso) {
                lblMensaje.setText("¡Registro de " + tipoRegistro + " exitoso! Redirigiendo a Login...");
                volverLogin(); 
            } else {
                lblMensaje.setText("Error: La cédula ya se encuentra registrada o hubo un fallo en el servicio.");
            }
            
        } catch (IOException e) {
            lblMensaje.setText("Error interno del sistema al navegar.");
            System.err.println("Error de IO al volver al login: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            lblMensaje.setText("Error desconocido al registrar: " + e.getMessage());
            System.err.println("Error de registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega de vuelta a la pantalla de Login.
     */
    @FXML
    private void volverLogin() throws IOException {
        App.setRoot("login");
    }
}