/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.model.Persona;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador para la vista de perfil del cliente
 * @author josue
 */
public class PerfilController {
    
    @FXML private Label lblNombreCompleto;
    @FXML private Label lblCedula;
    @FXML private Label lblEps;
    @FXML private Label lblTotalReservas;
    @FXML private Label lblMedicamentosReservados;
    @FXML private Label lblUltimaReserva;
    @FXML private Label lblMensajeHistorial;
    
    @FXML private TableView<TicketReserva> tablaHistorial;
    @FXML private TableColumn<TicketReserva, String> colFecha;
    @FXML private TableColumn<TicketReserva, String> colMedicamento;
    @FXML private TableColumn<TicketReserva, String> colCantidad;
    @FXML private TableColumn<TicketReserva, String> colPrioridad;
    
    private static Persona personaActual;
    private ObservableList<TicketReserva> historialReservas;
    private static final String ARCHIVO_TICKETS = "tickets.txt";
    
    @FXML
    public void initialize() {

        configurarTabla();

        if (personaActual != null) {
            cargarDatosPersona();
            cargarHistorialReservas();
            calcularEstadisticas();
        } else {
            System.out.println("⚠️ No hay persona actual cargada");
        }
    }
    
    private void configurarTabla() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colMedicamento.setCellValueFactory(new PropertyValueFactory<>("medicamento"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
    }
    
    private void cargarDatosPersona() {
        lblNombreCompleto.setText(personaActual.getNombre());
        lblCedula.setText(personaActual.getCedula());
        lblEps.setText(personaActual.getEps());
    }
    
    private void cargarHistorialReservas() {
        historialReservas = FXCollections.observableArrayList();
        
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_TICKETS))) {
            String linea;
            TicketReserva reservaActual = null;
            
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                
                if (linea.contains("-------------------------------")) {
                    if (reservaActual != null && reservaActual.getCedula() != null &&
                        reservaActual.getCedula().equals(personaActual.getCedula())) {

                        historialReservas.add(reservaActual);
                    }
                    reservaActual = new TicketReserva();
                    continue;
                }
                
                if (reservaActual != null && !linea.isEmpty() && !linea.equals("Ticket:")) {

                    if (linea.startsWith("Usuario:")) {
                        String[] partes = linea.split("\\|");
                        if (partes.length >= 2) {
                            String cedulaParte = partes[1].trim();
                            if (cedulaParte.startsWith("Cédula:")) {
                                reservaActual.setCedula(cedulaParte.substring(8).trim());
                            }
                        }
                    } else if (linea.startsWith("Medicamento:")) {
                        String[] partes = linea.split("\\|");
                        if (partes.length >= 2) {
                            String medParte = partes[0].trim();
                            String cantParte = partes[1].trim();
                            
                            if (medParte.startsWith("Medicamento:")) {
                                reservaActual.setMedicamento(medParte.substring(13).trim());
                            }
                            if (cantParte.startsWith("Cantidad:")) {
                                reservaActual.setCantidad(cantParte.substring(9).trim());
                            }
                        }
                    } else if (linea.startsWith("Prioridad:")) {
                        reservaActual.setPrioridad(linea.substring(11).trim());
                    } else if (linea.startsWith("Fecha y hora:")) {
                        reservaActual.setFecha(linea.substring(14).trim());
                    }
                }
            }
            
            // Guardar última reserva si es del usuario
            if (reservaActual != null &&
                reservaActual.getCedula() != null &&
                reservaActual.getCedula().equals(personaActual.getCedula())) {

                historialReservas.add(reservaActual);
            }
            
        } catch (IOException e) {
            System.out.println("No se pudo cargar el historial: " + e.getMessage());
            lblMensajeHistorial.setText("No se encontró historial de reservas");
        }
        
        FXCollections.reverse(historialReservas);
        tablaHistorial.setItems(historialReservas);
        
        if (historialReservas.isEmpty()) {
            lblMensajeHistorial.setText("No hay reservas registradas aún");
        } else {
            lblMensajeHistorial.setText("Mostrando " + historialReservas.size() + " reservas");
        }
    }
    
    private void calcularEstadisticas() {
        int totalReservas = historialReservas.size();
        lblTotalReservas.setText(String.valueOf(totalReservas));
        
        Set<String> medicamentosUnicos = new HashSet<>();
        for (TicketReserva r : historialReservas) {
            if (r.getMedicamento() != null) {
                medicamentosUnicos.add(r.getMedicamento());
            }
        }
        lblMedicamentosReservados.setText(String.valueOf(medicamentosUnicos.size()));
        
        if (!historialReservas.isEmpty()) {
            String ultimaFecha = historialReservas.get(0).getFecha();
            if (ultimaFecha.length() > 16) {
                ultimaFecha = ultimaFecha.substring(0, 16);
            }
            lblUltimaReserva.setText(ultimaFecha);
        } else {
            lblUltimaReserva.setText("Sin reservas");
        }
    }
    
    @FXML
    private void actualizarHistorial() {
        cargarHistorialReservas();
        calcularEstadisticas();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Actualizado");
        alert.setHeaderText(null);
        alert.setContentText("El historial ha sido actualizado");
        alert.showAndWait();
    }
    
    @FXML
    private void editarPerfil() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Editar Perfil");
        alert.setHeaderText("¿Qué desea modificar?");
        alert.setContentText("Seleccione una opción:");
        
        ButtonType btnPassword = new ButtonType("Cambiar Contraseña");
        ButtonType btnCancelar = new ButtonType("Cancelar");
        
        alert.getButtonTypes().setAll(btnPassword, btnCancelar);
        
        Optional<ButtonType> resultado = alert.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == btnPassword) {
            cambiarContrasena();
        }
    }

    private void cambiarContrasena() {
        TextInputDialog dialogActual = new TextInputDialog();
        dialogActual.setTitle("Cambiar Contraseña");
        dialogActual.setHeaderText("Verificación de Seguridad");
        dialogActual.setContentText("Ingrese su contraseña actual:");
        
        Optional<String> passwordActual = dialogActual.showAndWait();
        
        if (!passwordActual.isPresent() || passwordActual.get().isEmpty()) {
            return;
        }
        
        if (!passwordActual.get().equals(personaActual.getPassword())) {
            mostrarError("La contraseña actual es incorrecta");
            return;
        }
        
        TextInputDialog dialogNueva = new TextInputDialog();
        dialogNueva.setTitle("Cambiar Contraseña");
        dialogNueva.setHeaderText("Nueva Contraseña");
        dialogNueva.setContentText("Ingrese su nueva contraseña:");
        
        Optional<String> passwordNueva = dialogNueva.showAndWait();
        
        if (!passwordNueva.isPresent() || passwordNueva.get().isEmpty()) {
            return;
        }
        
        if (passwordNueva.get().length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres");
            return;
        }
        
        TextInputDialog dialogConfirmar = new TextInputDialog();
        dialogConfirmar.setTitle("Cambiar Contraseña");
        dialogConfirmar.setHeaderText("Confirmar Contraseña");
        dialogConfirmar.setContentText("Confirme su nueva contraseña:");
        
        Optional<String> passwordConfirmar = dialogConfirmar.showAndWait();
        
        if (!passwordConfirmar.isPresent() || 
            !passwordConfirmar.get().equals(passwordNueva.get())) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }
        
        if (actualizarPasswordEnArchivo(personaActual.getCedula(), passwordNueva.get())) {
            mostrarExito("Contraseña actualizada correctamente");

            personaActual = new Persona(
                personaActual.getCedula(),
                personaActual.getNombre(),
                personaActual.getEps(),
                passwordNueva.get()
            );
            MenuClienteController.setPersonaActual(personaActual);
        } else {
            mostrarError("No se pudo actualizar la contraseña");
        }
    }
    
    private boolean actualizarPasswordEnArchivo(String cedula, String nuevaPassword) {
        List<String> lineas = new ArrayList<>();
        boolean actualizado = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 5 && partes[0].equals(cedula)) {
                    partes[3] = nuevaPassword;
                    linea = String.join(";", partes);
                    actualizado = true;
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            return false;
        }
        
        if (actualizado) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("usuarios.txt"))) {
                for (String linea : lineas) {
                    bw.write(linea);
                    bw.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        
        return false;
    }
    
    @FXML
    private void volverMenu() {
        try {
            App.setRoot("menuCliente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void setPersonaActual(Persona persona) {
        personaActual = persona;
    }
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Clase interna corregida
     */
    public static class TicketReserva {
        private String fecha;
        private String medicamento;
        private String cantidad;
        private String prioridad;
        private String cedula;
        
        public TicketReserva() {}
        
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        
        public String getMedicamento() { return medicamento; }
        public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
        
        public String getCantidad() { return cantidad; }
        public void setCantidad(String cantidad) { this.cantidad = cantidad; }
        
        public String getPrioridad() { return prioridad; }
        public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
        
        public String getCedula() { return cedula; }
        public void setCedula(String cedula) { this.cedula = cedula; }
    }
}

