/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.model.Empleado;
import com.mycompany.farmacia.model.Persona;
import com.mycompany.farmacia.model.Usuario;
import com.mycompany.farmacia.service.ServicioLogin;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField; 
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controlador para la vista de Login
 * @author josue
 */
public class LoginController {
    
    @FXML
    private TextField txtCedula;
    
    @FXML
    private PasswordField txtContraseña;
    
    @FXML
    private Text bienvenido; 
    
    @FXML
    private ComboBox<String> comboTipo;
    
    private ServicioLogin servicioLogin = new ServicioLogin();
    
    @FXML
    public void initialize() {
        comboTipo.getItems().addAll("PERSONA", "EMPLEADO");
        comboTipo.getSelectionModel().selectFirst();
        bienvenido.setText("BIENVENIDO");
    }
    
    @FXML
    private void limpiarCampos() {
        txtCedula.clear();
        txtContraseña.clear();
        bienvenido.setText("Campos limpios");
    }
    
    @FXML
    private void iniciarSesion() {
        String cedula = txtCedula.getText().trim();
        String password = txtContraseña.getText().trim();
        String tipoSeleccionado = comboTipo.getValue();
        
        // Validación básica
        if (cedula.isEmpty() || password.isEmpty()) {
            bienvenido.setText("Complete todos los campos");
            return;
        }
        
        // Intentar login
        Usuario usuario = servicioLogin.iniciarSesion(cedula, password);
        
        if (usuario == null) {
            bienvenido.setText("Credenciales incorrectas");
            return;
        }
        
        // Validar que el tipo coincida
        if (tipoSeleccionado.equals("PERSONA") && !(usuario instanceof Persona)) {
            bienvenido.setText("El usuario no es una persona");
            return;
        }
        
        if (tipoSeleccionado.equals("EMPLEADO") && !(usuario instanceof Empleado)) {
            bienvenido.setText("El usuario no es un empleado");
            return;
        }
        
        // Login exitoso - Navegar según el tipo
        try {
            if (usuario instanceof Empleado) {
                System.out.println("Login exitoso como Empleado: " + usuario.getNombre());
                // ✅ AGREGADO: Pasar el empleado al menú de empleado
                MenuEmpleadoController.setPersonaActual((Empleado) usuario);
                App.setRoot("menuEmpleado");
            } else {
                System.out.println("Login exitoso como Persona: " + usuario.getNombre());
                Persona persona = (Persona) usuario;
                // ✅ AGREGADO: Pasar la persona al menú de cliente Y al controlador de reservar
                MenuClienteController.setPersonaActual(persona);
                ReservarController.setPersonaActual(persona);
                App.setRoot("menuCliente");
            }
        } catch (IOException e) {
            bienvenido.setText("Error al cargar menú");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void irARegistro() {
        try {
            App.setRoot("registro");
        } catch (IOException e) {
            bienvenido.setText("Error al cargar registro");
            e.printStackTrace();
        }
    }
}
