/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.data.BDReserva;
import com.mycompany.farmacia.model.Empleado;
import com.mycompany.farmacia.model.Reserva;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * Controlador para la vista de Ver Reservas de Clientes (Empleado)
 * @author josue
 */
public class VerReservasClientesController {
    
    // Componentes de la interfaz
    @FXML private Label lblEmpleado;
    @FXML private Label lblSubtitulo;
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroEstado;
    @FXML private ComboBox<String> cmbFiltroPrioridad;
    @FXML private Label lblTotalReservas;
    @FXML private Label lblPendientes;
    @FXML private Label lblEntregadas;
    @FXML private Label lblPrioritarias;
    
    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, String> colTicket;
    @FXML private TableColumn<Reserva, String> colCliente;
    @FXML private TableColumn<Reserva, String> colCedula;
    @FXML private TableColumn<Reserva, String> colMedicamento;
    @FXML private TableColumn<Reserva, Integer> colCantidad;
    @FXML private TableColumn<Reserva, String> colFecha;
    @FXML private TableColumn<Reserva, String> colPrioridad;
    @FXML private TableColumn<Reserva, String> colEstado;
    
    @FXML private VBox panelSinReservas;
    @FXML private Button btnVerDetalle;
    @FXML private Button btnMarcarEntregado;
    @FXML private Button btnCancelar;
    
    // Datos
    private static Empleado empleadoActual;
    private BDReserva bdReserva;
    private ObservableList<Reserva> listaReservas;
    private ObservableList<Reserva> listaFiltrada;
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        System.out.println("VerReservasClientesController inicializado");
        
        bdReserva = new BDReserva();
        
        // Configurar tabla
        configurarTabla();
        
        // Configurar filtros
        configurarFiltros();
        
        // Cargar reservas
        cargarReservas();
        
        // Listener para selección de reserva
        tablaReservas.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                boolean haySeleccion = newValue != null;
                btnVerDetalle.setDisable(!haySeleccion);
                
                // Solo se puede marcar como entregado si está PENDIENTE
                if (haySeleccion && "PENDIENTE".equals(newValue.getEstado())) {
                    btnMarcarEntregado.setDisable(false);
                    btnCancelar.setDisable(false);
                } else {
                    btnMarcarEntregado.setDisable(true);
                    btnCancelar.setDisable(true);
                }
            }
        );
        
        // Configurar empleado
        if (empleadoActual != null && lblEmpleado != null) {
            lblEmpleado.setText("Empleado: " + empleadoActual.getNombre());
        }
    }
    
    /**
     * Configura las columnas de la tabla
     */
    private void configurarTabla() {
        colTicket.setCellValueFactory(new PropertyValueFactory<>("numeroTicket"));
        
        // Columna cliente - mostrar nombre
        colCliente.setCellValueFactory(cellData -> {
            String nombre = cellData.getValue().getPersona().getNombre();
            return new SimpleStringProperty(nombre);
        });
        
        // Columna cédula
        colCedula.setCellValueFactory(cellData -> {
            String cedula = cellData.getValue().getPersona().getCedula();
            return new SimpleStringProperty(cedula);
        });
        
        // Columna medicamento - mostrar nombre y nombre comercial
        colMedicamento.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            String medicamento = reserva.getMedicamento().getNombre() + 
                               " (" + reserva.getMedicamento().getNombreComercial() + ")";
            return new SimpleStringProperty(medicamento);
        });
        
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
        
        // Columna prioridad - mostrar Sí/No con color
        colPrioridad.setCellValueFactory(cellData -> {
            boolean prioridad = cellData.getValue().isPrioridad();
            return new SimpleStringProperty(prioridad ? "⚠️ SÍ" : "No");
        });
        
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        // Estilo para las filas según el estado
        tablaReservas.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Reserva> row = new javafx.scene.control.TableRow<>();
            row.itemProperty().addListener((obs, oldReserva, newReserva) -> {
                if (newReserva == null) {
                    row.setStyle("");
                } else {
                    String estilo = "";
                    
                    // Color según estado
                    switch (newReserva.getEstado()) {
                        case "PENDIENTE":
                            estilo = "-fx-background-color: #fff3cd;";
                            break;
                        case "ENTREGADO":
                            estilo = "-fx-background-color: #d4edda;";
                            break;
                        case "CANCELADO":
                            estilo = "-fx-background-color: #f8d7da;";
                            break;
                    }
                    
                    // Resaltar si es prioritaria y está pendiente
                    if (newReserva.isPrioridad() && "PENDIENTE".equals(newReserva.getEstado())) {
                        estilo = "-fx-background-color: #ffe5e5; -fx-font-weight: bold;";
                    }
                    
                    row.setStyle(estilo);
                }
            });
            return row;
        });
    }
    
    /**
     * Configura los ComboBox de filtros
     */
    private void configurarFiltros() {
        cmbFiltroEstado.getItems().addAll("Todos", "PENDIENTE", "ENTREGADO", "CANCELADO");
        cmbFiltroEstado.getSelectionModel().selectFirst();
        
        cmbFiltroPrioridad.getItems().addAll("Todas", "Prioritarias", "Normales");
        cmbFiltroPrioridad.getSelectionModel().selectFirst();
    }
    
    /**
     * Carga todas las reservas de todos los clientes
     */
    private void cargarReservas() {
        // Cargar TODAS las reservas (no filtrar por cédula)
        List<Reserva> reservas = bdReserva.cargarReservas();
        
        listaReservas = FXCollections.observableArrayList(reservas);
        listaFiltrada = FXCollections.observableArrayList(reservas);
        
        if (reservas.isEmpty()) {
            mostrarPanelSinReservas(true);
        } else {
            mostrarPanelSinReservas(false);
            tablaReservas.setItems(listaFiltrada);
        }
        
        // Actualizar estadísticas
        actualizarEstadisticas();
        
        System.out.println("Reservas de clientes cargadas: " + reservas.size());
    }
    
    /**
     * Actualiza las estadísticas (contadores)
     */
    private void actualizarEstadisticas() {
        int total = listaReservas.size();
        long pendientes = listaReservas.stream()
            .filter(r -> "PENDIENTE".equals(r.getEstado()))
            .count();
        long entregadas = listaReservas.stream()
            .filter(r -> "ENTREGADO".equals(r.getEstado()))
            .count();
        long prioritarias = listaReservas.stream()
            .filter(r -> r.isPrioridad() && "PENDIENTE".equals(r.getEstado()))
            .count();
        
        lblTotalReservas.setText(String.valueOf(total));
        lblPendientes.setText(String.valueOf(pendientes));
        lblEntregadas.setText(String.valueOf(entregadas));
        lblPrioritarias.setText(String.valueOf(prioritarias));
    }
    
    /**
     * Muestra u oculta el panel "sin reservas"
     */
    private void mostrarPanelSinReservas(boolean mostrar) {
        panelSinReservas.setVisible(mostrar);
        panelSinReservas.setManaged(mostrar);
        tablaReservas.setVisible(!mostrar);
        tablaReservas.setManaged(!mostrar);
    }
    
    /**
     * Busca reservas mientras el usuario escribe
     */
    @FXML
    private void buscarReserva() {
        aplicarFiltros();
    }
    
    /**
     * Filtra reservas por estado
     */
    @FXML
    private void filtrarPorEstado() {
        aplicarFiltros();
    }
    
    /**
     * Filtra reservas por prioridad
     */
    @FXML
    private void filtrarPorPrioridad() {
        aplicarFiltros();
    }
    
    /**
     * Aplica todos los filtros combinados
     */
    private void aplicarFiltros() {
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        String filtroEstado = cmbFiltroEstado.getValue();
        String filtroPrioridad = cmbFiltroPrioridad.getValue();
        
        listaFiltrada.clear();
        
        List<Reserva> resultado = listaReservas.stream()
            .filter(r -> {
                // Filtro por búsqueda (cliente, medicamento o ticket)
                boolean coincideBusqueda = busqueda.isEmpty() || 
                    r.getPersona().getNombre().toLowerCase().contains(busqueda) ||
                    r.getPersona().getCedula().toLowerCase().contains(busqueda) ||
                    r.getNombreMedicamento().toLowerCase().contains(busqueda) ||
                    r.getNumeroTicket().toLowerCase().contains(busqueda);
                
                // Filtro por estado
                boolean coincideEstado = "Todos".equals(filtroEstado) || 
                    r.getEstado().equals(filtroEstado);
                
                // Filtro por prioridad
                boolean coincidePrioridad = "Todas".equals(filtroPrioridad) ||
                    ("Prioritarias".equals(filtroPrioridad) && r.isPrioridad()) ||
                    ("Normales".equals(filtroPrioridad) && !r.isPrioridad());
                
                return coincideBusqueda && coincideEstado && coincidePrioridad;
            })
            .collect(Collectors.toList());
        
        listaFiltrada.addAll(resultado);
        tablaReservas.setItems(listaFiltrada);
    }
    
    /**
     * Actualiza la tabla de reservas
     */
    @FXML
    private void actualizarTabla() {
        cargarReservas();
        txtBuscar.clear();
        cmbFiltroEstado.getSelectionModel().selectFirst();
        cmbFiltroPrioridad.getSelectionModel().selectFirst();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Actualizado");
        alert.setHeaderText(null);
        alert.setContentText("La lista de reservas se ha actualizado correctamente.");
        alert.showAndWait();
    }
    
    /**
     * Ver detalle de la reserva seleccionada
     */
    @FXML
    private void verDetalleReserva() {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        
        if (reservaSeleccionada == null) {
            mostrarError("Por favor seleccione una reserva");
            return;
        }
        
        // Crear mensaje con los detalles
        String detalles = "═══════════════════════════════════\n" +
                         "DETALLE DE LA RESERVA\n" +
                         "═══════════════════════════════════\n\n" +
                         "Número de Ticket: " + reservaSeleccionada.getNumeroTicket() + "\n" +
                         "Estado: " + reservaSeleccionada.getEstado() + "\n\n" +
                         "───────────────────────────────────\n" +
                         "DATOS DEL CLIENTE:\n" +
                         "───────────────────────────────────\n" +
                         "Nombre: " + reservaSeleccionada.getPersona().getNombre() + "\n" +
                         "Cédula: " + reservaSeleccionada.getPersona().getCedula() + "\n\n" +
                         "───────────────────────────────────\n" +
                         "MEDICAMENTO:\n" +
                         "───────────────────────────────────\n" +
                         "Nombre: " + reservaSeleccionada.getMedicamento().getNombre() + "\n" +
                         "Nombre Comercial: " + reservaSeleccionada.getMedicamento().getNombreComercial() + "\n" +
                         "Cantidad: " + reservaSeleccionada.getCantidad() + " unidad(es)\n\n" +
                         "───────────────────────────────────\n" +
                         "DATOS DE LA RESERVA:\n" +
                         "───────────────────────────────────\n" +
                         "Fecha: " + reservaSeleccionada.getFechaFormateada() + "\n" +
                         "Prioridad: " + (reservaSeleccionada.isPrioridad() ? "⚠️ SÍ - Atención Prioritaria" : "No") + "\n\n" +
                         "═══════════════════════════════════";
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle de Reserva");
        alert.setHeaderText("Información completa de la reserva");
        alert.setContentText(detalles);
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }
    
    /**
     * Marca una reserva como entregada
     */
    @FXML
    private void marcarComoEntregado() {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        
        if (reservaSeleccionada == null) {
            mostrarError("Por favor seleccione una reserva");
            return;
        }
        
        if (!"PENDIENTE".equals(reservaSeleccionada.getEstado())) {
            mostrarError("Solo se pueden marcar como entregadas las reservas PENDIENTES");
            return;
        }
        
        // Confirmar entrega
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Entrega");
        confirmacion.setHeaderText("¿Confirma que el medicamento ha sido entregado?");
        confirmacion.setContentText("Cliente: " + reservaSeleccionada.getPersona().getNombre() + "\n" +
                                   "Ticket: " + reservaSeleccionada.getNumeroTicket() + "\n" +
                                   "Medicamento: " + reservaSeleccionada.getNombreMedicamento() + "\n" +
                                   "Cantidad: " + reservaSeleccionada.getCantidad() + " unidad(es)");
        
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Actualizar estado en la base de datos
                boolean actualizado = bdReserva.actualizarEstadoReserva(
                    reservaSeleccionada.getNumeroTicket(), 
                    "ENTREGADO"
                );
                
                if (actualizado) {
                    mostrarExito("La reserva ha sido marcada como ENTREGADO exitosamente.\n" +
                               "El cliente ha recogido su medicamento.");
                    cargarReservas(); // Recargar la tabla
                } else {
                    mostrarError("No se pudo actualizar el estado de la reserva");
                }
            }
        });
    }
    
    /**
     * Cancelar una reserva
     */
    @FXML
    private void cancelarReserva() {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        
        if (reservaSeleccionada == null) {
            mostrarError("Por favor seleccione una reserva");
            return;
        }
        
        if (!"PENDIENTE".equals(reservaSeleccionada.getEstado())) {
            mostrarError("Solo se pueden cancelar reservas en estado PENDIENTE");
            return;
        }
        
        // Confirmar cancelación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cancelación");
        confirmacion.setHeaderText("¿Está seguro que desea cancelar esta reserva?");
        confirmacion.setContentText("Cliente: " + reservaSeleccionada.getPersona().getNombre() + "\n" +
                                   "Ticket: " + reservaSeleccionada.getNumeroTicket() + "\n" +
                                   "Medicamento: " + reservaSeleccionada.getNombreMedicamento() + "\n\n" +
                                   "Esta acción no se puede deshacer.");
        
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Actualizar estado en la base de datos
                boolean actualizado = bdReserva.actualizarEstadoReserva(
                    reservaSeleccionada.getNumeroTicket(), 
                    "CANCELADO"
                );
                
                if (actualizado) {
                    mostrarExito("La reserva ha sido cancelada exitosamente");
                    cargarReservas(); // Recargar la tabla
                } else {
                    mostrarError("No se pudo cancelar la reserva");
                }
            }
        });
    }
    
    /**
     * Vuelve al menú de empleado
     */
    @FXML
    private void volverMenu() {
        try {
            App.setRoot("menuEmpleado");
        } catch (IOException e) {
            System.err.println("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Configura el empleado actual
     */
    public static void setEmpleadoActual(Empleado empleado) {
        empleadoActual = empleado;
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
    
    /**
     * Muestra un mensaje de éxito
     */
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText("Operación exitosa");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
