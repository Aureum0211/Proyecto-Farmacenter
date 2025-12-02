/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.model.Medicamento;
import com.mycompany.farmacia.model.Persona;
import com.mycompany.farmacia.service.ServicioMedicamento;
import com.mycompany.farmacia.service.ServicioReserva;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador para la vista de reservar medicamentos
 * @author josue
 */
public class ReservarController {
    
    // Componentes de búsqueda y tabla
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroOrden;
    @FXML private TableView<Medicamento> tablaMedicamentos;
    @FXML private TableColumn<Medicamento, String> colNombre;
    @FXML private TableColumn<Medicamento, String> colTipo;
    @FXML private TableColumn<Medicamento, Integer> colStock;
    @FXML private TableColumn<Medicamento, String> colOrden;
    
    // Componentes del formulario
    @FXML private Label lblUsuario;
    @FXML private Label lblMedicamentoSeleccionado;
    @FXML private Label lblStockDisponible;
    @FXML private Spinner<Integer> spinnerCantidad;
    @FXML private CheckBox chkPrioridad;
    @FXML private VBox panelOrdenMedica;
    @FXML private TextField txtCodigoMedico;
    @FXML private TextField txtEntidad;
    @FXML private Label lblMensaje;
    @FXML private Button btnReservar;
    
    // Servicios y datos
    private ServicioMedicamento servicioMedicamento;
    private ServicioReserva servicioReserva;
    private ObservableList<Medicamento> listaMedicamentos;
    private ObservableList<Medicamento> listaFiltrada;
    private Medicamento medicamentoSeleccionado;
    private static Persona personaActual;
    
    @FXML
    public void initialize() {
        System.out.println("ReservarController inicializado");
        
        servicioMedicamento = new ServicioMedicamento();
        servicioReserva = new ServicioReserva();
        
        // Configurar columnas de la tabla
        configurarTabla();
        
        // Cargar medicamentos
        cargarMedicamentos();
        
        // Configurar ComboBox de filtros
        configurarFiltros();
        
        // Configurar Spinner de cantidad
        configurarSpinner();
        
        // Listener para selección de medicamento
        tablaMedicamentos.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> seleccionarMedicamento(newValue)
        );
        
        // Configurar usuario si existe
        if (personaActual != null && lblUsuario != null) {
            lblUsuario.setText("Usuario: " + personaActual.getNombre());
        }
    }
    
    /**
     * Configura las columnas de la tabla
     */
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        colOrden.setCellValueFactory(cellData -> {
            boolean requiere = cellData.getValue().isRequiereOrden();
            return new SimpleStringProperty(requiere ? "Sí" : "No");
        });
    }
    
    /**
     * Carga todos los medicamentos disponibles
     */
    private void cargarMedicamentos() {
        List<Medicamento> medicamentos = servicioMedicamento.listarMedicamentos();
        
        // Filtrar solo medicamentos con stock disponible
        medicamentos = medicamentos.stream()
            .filter(m -> m.getCantidadDisponible() > 0)
            .collect(Collectors.toList());
        
        listaMedicamentos = FXCollections.observableArrayList(medicamentos);
        listaFiltrada = FXCollections.observableArrayList(medicamentos);
        tablaMedicamentos.setItems(listaFiltrada);
        
        System.out.println("Medicamentos cargados: " + medicamentos.size());
    }
    
    /**
     * Configura el ComboBox de filtros
     */
    private void configurarFiltros() {
        cmbFiltroOrden.getItems().addAll("Todos", "Con orden médica", "Sin orden médica");
        cmbFiltroOrden.getSelectionModel().selectFirst();
    }
    
    /**
     * Configura el Spinner de cantidad
     */
    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
        spinnerCantidad.setValueFactory(valueFactory);
        spinnerCantidad.setEditable(true);
    }
    
    /**
     * Busca medicamentos mientras el usuario escribe
     */
    @FXML
    private void buscarMedicamento() {
        aplicarFiltros();
    }
    
    /**
     * Filtra medicamentos por si requieren orden médica
     */
    @FXML
    private void filtrarPorOrden() {
        aplicarFiltros();
    }
    
    /**
     * Aplica todos los filtros combinados
     */
    private void aplicarFiltros() {
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        String filtroOrden = cmbFiltroOrden.getValue();
        
        listaFiltrada.clear();
        
        List<Medicamento> resultado = listaMedicamentos.stream()
            .filter(m -> {
                // Filtro por nombre
                boolean coincideNombre = busqueda.isEmpty() || 
                    m.getNombre().toLowerCase().contains(busqueda) ||
                    m.getNombreComercial().toLowerCase().contains(busqueda);
                
                // Filtro por orden médica
                boolean coincideOrden = true;
                if ("Con orden médica".equals(filtroOrden)) {
                    coincideOrden = m.isRequiereOrden();
                } else if ("Sin orden médica".equals(filtroOrden)) {
                    coincideOrden = !m.isRequiereOrden();
                }
                
                return coincideNombre && coincideOrden;
            })
            .collect(Collectors.toList());
        
        listaFiltrada.addAll(resultado);
        tablaMedicamentos.setItems(listaFiltrada);
    }
    
    /**
     * Maneja la selección de un medicamento en la tabla
     */
    private void seleccionarMedicamento(Medicamento medicamento) {
        medicamentoSeleccionado = medicamento;
        lblMensaje.setText("");
        
        if (medicamento != null) {
            // Mostrar información del medicamento
            lblMedicamentoSeleccionado.setText(medicamento.getNombre() + " - " + 
                                               medicamento.getNombreComercial());
            lblStockDisponible.setText(String.valueOf(medicamento.getCantidadDisponible()) + 
                                       " unidades");
            
            // Actualizar máximo del spinner
            int stockMax = medicamento.getCantidadDisponible();
            SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, stockMax, 1);
            spinnerCantidad.setValueFactory(valueFactory);
            
            // Mostrar u ocultar panel de orden médica
            boolean requiereOrden = medicamento.isRequiereOrden();
            panelOrdenMedica.setVisible(requiereOrden);
            panelOrdenMedica.setManaged(requiereOrden);
            
            // Limpiar campos de orden médica
            if (requiereOrden) {
                txtCodigoMedico.clear();
                txtEntidad.clear();
            }
            
            // Habilitar botón de reservar
            btnReservar.setDisable(false);
            
        } else {
            // Limpiar selección
            lblMedicamentoSeleccionado.setText("Ninguno");
            lblStockDisponible.setText("-");
            panelOrdenMedica.setVisible(false);
            panelOrdenMedica.setManaged(false);
            btnReservar.setDisable(true);
        }
    }
    
    /**
     * Realiza la reserva del medicamento
     */
   /**
 * Realiza la reserva del medicamento
 */
@FXML
private void realizarReserva() {
    if (medicamentoSeleccionado == null) {
        mostrarError("Por favor seleccione un medicamento");
        return;
    }
    
    if (personaActual == null) {
        mostrarError("No hay un usuario autenticado");
        return;
    }
    
    // Obtener cantidad
    int cantidad = spinnerCantidad.getValue();
    
    // Validar cantidad
    if (cantidad <= 0 || cantidad > medicamentoSeleccionado.getCantidadDisponible()) {
        mostrarError("Cantidad inválida. Stock disponible: " + 
                    medicamentoSeleccionado.getCantidadDisponible());
        return;
    }
    
    // Validar orden médica si es requerida
    if (medicamentoSeleccionado.isRequiereOrden()) {
        String codigoMedico = txtCodigoMedico.getText().trim();
        String entidad = txtEntidad.getText().trim();
        
        if (codigoMedico.isEmpty() || entidad.isEmpty()) {
            mostrarError("Este medicamento requiere orden médica.\n" +
                       "Complete el código del médico y la entidad.");
            return;
        }
    }
    
    // Obtener prioridad
    boolean prioridad = chkPrioridad.isSelected();
    
    // Realizar la reserva (actualizar el stock)
    List<Medicamento> lista = servicioMedicamento.listarMedicamentos();
    boolean reservaExitosa = false;
    
    for (Medicamento m : lista) {
        if (m.getNombre().equals(medicamentoSeleccionado.getNombre())) {
            if (m.getCantidadDisponible() >= cantidad) {
                m.descontar(cantidad);
                // Aquí deberías guardar los cambios en la base de datos
                reservaExitosa = true;
                break;
            }
        }
    }
    
    if (reservaExitosa) {
        // ✅ NUEVO: Mostrar la ventana del ticket
        mostrarVentanaTicket(personaActual, medicamentoSeleccionado, cantidad, prioridad);
        
        // Recargar medicamentos para actualizar stock
        cargarMedicamentos();
        
        // Limpiar selección
        tablaMedicamentos.getSelectionModel().clearSelection();
        limpiarFormulario();
        
    } else {
        mostrarError("No se pudo completar la reserva.\n" +
                    "Verifique el stock disponible.");
    }
}

/**
 * ✅ NUEVO: Muestra la ventana del ticket
 */
private void mostrarVentanaTicket(Persona persona, Medicamento medicamento, int cantidad, boolean prioridad) {
    try {
        // Cargar el FXML del ticket
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/farmacia/ticket.fxml"));
        Parent root = loader.load();
        
        // Obtener el controlador y configurar los datos
        TicketController ticketController = loader.getController();
        ticketController.configurarTicket(persona, medicamento, cantidad, prioridad);
        
        // Crear la nueva ventana (Stage)
        Stage ticketStage = new Stage();
        ticketStage.setTitle("Ticket de Reserva - Farma Center");
        ticketStage.setScene(new Scene(root));
        ticketStage.initModality(Modality.APPLICATION_MODAL); // Ventana modal
        ticketStage.setResizable(false);
        
        // Mostrar la ventana
        ticketStage.showAndWait();
        
    } catch (IOException e) {
        System.err.println("Error al mostrar ticket: " + e.getMessage());
        e.printStackTrace();
        mostrarError("No se pudo mostrar el ticket");
    }
}
    
    /**
     * Limpia el formulario de reserva
     */
    private void limpiarFormulario() {
        medicamentoSeleccionado = null;
        lblMedicamentoSeleccionado.setText("Ninguno");
        lblStockDisponible.setText("-");
        spinnerCantidad.getValueFactory().setValue(1);
        chkPrioridad.setSelected(false);
        txtCodigoMedico.clear();
        txtEntidad.clear();
        panelOrdenMedica.setVisible(false);
        panelOrdenMedica.setManaged(false);
        btnReservar.setDisable(true);
        lblMensaje.setText("");
    }
    
    /**
     * Vuelve al menú de cliente
     */
    @FXML
    private void volverMenu() {
        try {
            App.setRoot("menuCliente");
        } catch (IOException e) {
            System.err.println("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Configura la persona actual
     */
    public static void setPersonaActual(Persona persona) {
        personaActual = persona;
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #d9534f;");
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se pudo completar la reserva");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Muestra un mensaje de éxito
     */
    private void mostrarExito(String mensaje) {
        lblMensaje.setText("✅ Reserva exitosa");
        lblMensaje.setStyle("-fx-text-fill: #5cb85c;");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reserva Exitosa");
        alert.setHeaderText("¡Medicamento reservado!");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
