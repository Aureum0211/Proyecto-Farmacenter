/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.data.BDReserva;
import com.mycompany.farmacia.model.Medicamento;
import com.mycompany.farmacia.model.Persona;
import com.mycompany.farmacia.model.Reserva;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controlador para la vista del ticket de reserva
 * @author josue
 */
public class TicketController {
    
    @FXML private Label lblFechaHora;
    @FXML private Label lblNombreCliente;
    @FXML private Label lblCedulaCliente;
    @FXML private Label lblNombreMedicamento;
    @FXML private Label lblNombreComercial;
    @FXML private Label lblCantidad;
    @FXML private Label lblRequiereOrden;
    @FXML private Label lblPrioridad;
    @FXML private Label lblNumeroTicket;
    
    private static final String ARCHIVO_TICKETS = "tickets.txt";
    private static int contadorTickets = 1;
    
    private Persona persona;
    private Medicamento medicamento;
    private int cantidad;
    private boolean prioridad;
    private String ticketTexto;
    private String numeroTicket;
    
    private BDReserva bdReserva;
    
    /**
     * Inicialización del controlador
     */
    @FXML
    public void initialize() {
        System.out.println("TicketController inicializado");
        bdReserva = new BDReserva();
    }
    
    /**
     * Configura los datos del ticket
     */
    public void configurarTicket(Persona persona, Medicamento medicamento, int cantidad, boolean prioridad) {
        // ✅ AGREGADO: Inicializar bdReserva si es null
        if (bdReserva == null) {
            bdReserva = new BDReserva();
        }
        
        this.persona = persona;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        
        System.out.println("Configurando ticket para: " + persona.getNombre());
        
        cargarDatos();
        guardarReserva(); // ✅ CAMBIADO: Primero guardar la reserva
        generarTicketTexto(); // Luego generar el texto del ticket
    }
    
    /**
     * Carga los datos en los labels
     */
    private void cargarDatos() {
        // Fecha y hora
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String fechaHora = dtf.format(LocalDateTime.now());
        lblFechaHora.setText(fechaHora);
        
        // Datos del cliente
        lblNombreCliente.setText(persona.getNombre());
        lblCedulaCliente.setText(persona.getCedula());
        
        // Datos del medicamento
        lblNombreMedicamento.setText(medicamento.getNombre());
        lblNombreComercial.setText(medicamento.getNombreComercial());
        lblCantidad.setText(cantidad + " unidad(es)");
        lblRequiereOrden.setText(medicamento.isRequiereOrden() ? "Sí" : "No");
        
        // Detalles de la reserva
        if (prioridad) {
            lblPrioridad.setText("✓ Atención Prioritaria");
            lblPrioridad.setStyle("-fx-text-fill: #d9534f; -fx-font-weight: bold;");
        } else {
            lblPrioridad.setText("Normal");
            lblPrioridad.setStyle("-fx-text-fill: #555555;");
        }
        
        // Número de ticket
        numeroTicket = "#" + String.format("%03d", contadorTickets);
        lblNumeroTicket.setText(numeroTicket);
        contadorTickets++;
        
        System.out.println("Datos cargados. Ticket: " + numeroTicket);
    }
    
    /**
     * ✅ CORREGIDO: Guarda la reserva en la base de datos
     */
    private void guardarReserva() {
        try {
            System.out.println("Intentando guardar reserva...");
            System.out.println("Persona: " + persona.getNombre() + " - " + persona.getCedula());
            System.out.println("Medicamento: " + medicamento.getNombre());
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Número ticket: " + numeroTicket);
            
            Reserva reserva = new Reserva(numeroTicket, persona, medicamento, cantidad, prioridad);
            bdReserva.guardarReserva(reserva);
            
            System.out.println("✅ Reserva guardada exitosamente en BD");
            
        } catch (Exception e) {
            System.err.println("❌ Error al guardar reserva: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Genera el texto del ticket para guardar e imprimir
     */
    private void generarTicketTexto() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String fechaHora = dtf.format(LocalDateTime.now());
        
        ticketTexto = "=======================================\n" +
                      "           TICKET DE RESERVA           \n" +
                      "             FARMA CENTER              \n" +
                      "=======================================\n\n" +
                      "Número de Ticket: " + numeroTicket + "\n" +
                      "Fecha y Hora: " + fechaHora + "\n\n" +
                      "---------------------------------------\n" +
                      "DATOS DEL CLIENTE:\n" +
                      "---------------------------------------\n" +
                      "  Nombre: " + persona.getNombre() + "\n" +
                      "  Cédula: " + persona.getCedula() + "\n\n" +
                      "---------------------------------------\n" +
                      "DATOS DEL MEDICAMENTO:\n" +
                      "---------------------------------------\n" +
                      "  Medicamento: " + medicamento.getNombre() + "\n" +
                      "  Nombre Comercial: " + medicamento.getNombreComercial() + "\n" +
                      "  Cantidad Reservada: " + cantidad + " unidad(es)\n" +
                      "  Requiere Orden: " + (medicamento.isRequiereOrden() ? "Sí" : "No") + "\n\n" +
                      "---------------------------------------\n" +
                      "DETALLES DE LA RESERVA:\n" +
                      "---------------------------------------\n" +
                      "  Prioridad: " + (prioridad ? "SÍ - Atención Prioritaria" : "No") + "\n" +
                      "  Estado: PENDIENTE\n\n" +
                      "=======================================\n" +
                      "  Por favor conserve este ticket para\n" +
                      "  retirar su medicamento en farmacia\n" +
                      "=======================================\n\n";
        
        // Guardar en archivo
        guardarTicketEnArchivo();
    }
    
    /**
     * Guarda el ticket en el archivo de texto
     */
    private void guardarTicketEnArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_TICKETS, true))) {
            bw.write(ticketTexto);
            bw.newLine();
            System.out.println("Ticket guardado en: " + ARCHIVO_TICKETS);
        } catch (IOException e) {
            System.err.println("Error guardando ticket: " + e.getMessage());
        }
    }
    
    /**
     * Imprime el ticket (por ahora lo muestra en consola)
     */
    @FXML
    private void imprimirTicket() {
        System.out.println("\n=== IMPRIMIENDO TICKET ===");
        System.out.println(ticketTexto);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Imprimir Ticket");
        alert.setHeaderText("Ticket enviado a impresora");
        alert.setContentText("El ticket ha sido enviado a la impresora.\n" +
                           "También se ha guardado en el archivo: " + ARCHIVO_TICKETS);
        alert.showAndWait();
    }
    
    /**
     * Cierra la ventana del ticket
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) lblNumeroTicket.getScene().getWindow();
        stage.close();
    }
}
