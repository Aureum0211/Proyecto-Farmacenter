/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.model.Medicamento;
import com.mycompany.farmacia.service.ServicioMedicamento;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

/**
 * Controlador para la vista de consulta de medicamentos
 * @author josue
 */
public class ConsultarController {
    
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TableView<Medicamento> tablaMedicamentos;
    @FXML private TableColumn<Medicamento, String> colNombre;
    @FXML private TableColumn<Medicamento, String> colTipo;
    @FXML private TableColumn<Medicamento, Integer> colStock;
    @FXML private TableColumn<Medicamento, String> colOrden;
    @FXML private TableColumn<Medicamento, String> colDosis;
    
    // Labels del panel de detalles
    @FXML private Label lblUsuario;
    @FXML private Label lblNombreComercial;
    @FXML private Label lblNombreGenerico;
    @FXML private Label lblForma;
    @FXML private Label lblFrecuencia;
    @FXML private TextArea txtIndicaciones;
    @FXML private TextArea txtAdvertencias;
    
    private ServicioMedicamento servicioMedicamento;
    private ObservableList<Medicamento> listaMedicamentos;
    private ObservableList<Medicamento> listaFiltrada;
    
    // Variable para saber desde dónde vino el usuario
    private static String vistaOrigen = "menuCliente"; // Por defecto cliente
    
    @FXML
    public void initialize() {
        System.out.println("\n========================================");
        System.out.println("🔍 INICIANDO ConsultarController");
        System.out.println("========================================");
        
        servicioMedicamento = new ServicioMedicamento();
        
        // DEBUG: Verificar que las columnas NO sean null
        System.out.println("\n📋 VERIFICACIÓN DE COLUMNAS:");
        System.out.println("colNombre: " + (colNombre != null ? "✅ OK" : "❌ NULL"));
        System.out.println("colTipo: " + (colTipo != null ? "✅ OK" : "❌ NULL"));
        System.out.println("colStock: " + (colStock != null ? "✅ OK" : "❌ NULL"));
        System.out.println("colOrden: " + (colOrden != null ? "✅ OK" : "❌ NULL"));
        System.out.println("colDosis: " + (colDosis != null ? "✅ OK" : "❌ NULL"));
        System.out.println("tablaMedicamentos: " + (tablaMedicamentos != null ? "✅ OK" : "❌ NULL"));
        
        // Configurar columnas
        if (colNombre != null) {
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            System.out.println("✅ colNombre configurada");
        } else {
            System.out.println("❌ ERROR: colNombre es NULL - NO SE PUEDE CONFIGURAR");
        }
        
        if (colTipo != null) {
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            System.out.println("✅ colTipo configurada");
        } else {
            System.out.println("❌ ERROR: colTipo es NULL - NO SE PUEDE CONFIGURAR");
        }
        
        if (colStock != null) {
            colStock.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
            System.out.println("✅ colStock configurada");
        } else {
            System.out.println("❌ ERROR: colStock es NULL - NO SE PUEDE CONFIGURAR");
        }
        
        if (colDosis != null) {
            colDosis.setCellValueFactory(new PropertyValueFactory<>("dosis"));
            System.out.println("✅ colDosis configurada");
        } else {
            System.out.println("❌ ERROR: colDosis es NULL - NO SE PUEDE CONFIGURAR");
        }
        
        if (colOrden != null) {
            colOrden.setCellValueFactory(cellData -> {
                boolean requiere = cellData.getValue().isRequiereOrden();
                return new SimpleStringProperty(requiere ? "Sí" : "No");
            });
            System.out.println("✅ colOrden configurada");
        } else {
            System.out.println("❌ ERROR: colOrden es NULL - NO SE PUEDE CONFIGURAR");
        }
        
        // Cargar medicamentos
        cargarMedicamentos();
        
        // Cargar tipos únicos en el ComboBox
        cargarTipos();
        
        // Listener para selección de medicamento en la tabla
        if (tablaMedicamentos != null) {
            tablaMedicamentos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetalles(newValue)
            );
        }
        
        System.out.println("\n========================================");
        System.out.println("✅ ConsultarController inicializado");
        System.out.println("========================================\n");
    }
    
    /**
     * Carga todos los medicamentos desde el servicio
     */
    private void cargarMedicamentos() {
        System.out.println("\n📦 CARGANDO MEDICAMENTOS...");
        List<Medicamento> medicamentos = servicioMedicamento.listarMedicamentos();
        
        System.out.println("Total de medicamentos cargados: " + medicamentos.size());
        
        if (medicamentos.isEmpty()) {
            System.out.println("⚠️ ADVERTENCIA: No hay medicamentos en el archivo medicamentos.txt");
            System.out.println("⚠️ Verifica que el archivo exista y tenga datos");
        } else {
            System.out.println("\n📋 PRIMEROS 3 MEDICAMENTOS:");
            for (int i = 0; i < Math.min(3, medicamentos.size()); i++) {
                Medicamento m = medicamentos.get(i);
                System.out.println((i+1) + ". " + m.getNombre() + " | " + m.getTipo() + 
                    " | Stock: " + m.getCantidadDisponible() + " | Dosis: " + m.getDosis());
            }
        }
        
        listaMedicamentos = FXCollections.observableArrayList(medicamentos);
        listaFiltrada = FXCollections.observableArrayList(medicamentos);
        
        if (tablaMedicamentos != null) {
            tablaMedicamentos.setItems(listaFiltrada);
            System.out.println("✅ Items asignados a la tabla");
            System.out.println("Items en tabla: " + tablaMedicamentos.getItems().size());
        } else {
            System.out.println("❌ ERROR: tablaMedicamentos es NULL - NO SE PUEDEN ASIGNAR ITEMS");
        }
    }
    
    /**
     * Carga los tipos únicos de medicamentos en el ComboBox
     */
    private void cargarTipos() {
        if (listaMedicamentos == null || listaMedicamentos.isEmpty()) {
            System.out.println("⚠️ No hay medicamentos para cargar tipos");
            return;
        }
        
        List<String> tipos = listaMedicamentos.stream()
            .map(Medicamento::getTipo)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        
        if (cmbTipo != null) {
            cmbTipo.getItems().add("Todos");
            cmbTipo.getItems().addAll(tipos);
            cmbTipo.getSelectionModel().selectFirst();
            System.out.println("✅ Tipos cargados en ComboBox: " + tipos);
        }
    }
    
    /**
     * Busca medicamentos por nombre mientras el usuario escribe
     */
    @FXML
    private void buscarMedicamento() {
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        String tipoSeleccionado = cmbTipo.getValue();
        
        filtrarMedicamentos(busqueda, tipoSeleccionado);
    }
    
    /**
     * Filtra medicamentos por tipo
     */
    @FXML
    private void filtrarPorTipo() {
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        String tipoSeleccionado = cmbTipo.getValue();
        
        filtrarMedicamentos(busqueda, tipoSeleccionado);
    }
    
    /**
     * Aplica filtros combinados de búsqueda y tipo
     */
    private void filtrarMedicamentos(String busqueda, String tipo) {
        listaFiltrada.clear();
        
        List<Medicamento> resultado = listaMedicamentos.stream()
            .filter(m -> {
                boolean coincideNombre = busqueda.isEmpty() || 
                    m.getNombre().toLowerCase().contains(busqueda) ||
                    m.getNombreComercial().toLowerCase().contains(busqueda);
                
                boolean coincideTipo = tipo == null || tipo.equals("Todos") || 
                    m.getTipo().equals(tipo);
                
                return coincideNombre && coincideTipo;
            })
            .collect(Collectors.toList());
        
        listaFiltrada.addAll(resultado);
        tablaMedicamentos.setItems(listaFiltrada);
        
        System.out.println("🔍 Filtro aplicado - Resultados: " + resultado.size());
    }
    
    /**
     * Limpia todos los filtros
     */
    @FXML
    private void limpiarFiltros() {
        txtBuscar.clear();
        cmbTipo.getSelectionModel().selectFirst();
        listaFiltrada.clear();
        listaFiltrada.addAll(listaMedicamentos);
        tablaMedicamentos.setItems(listaFiltrada);
        limpiarDetalles();
    }
    
    /**
     * Muestra los detalles del medicamento seleccionado
     */
    private void mostrarDetalles(Medicamento medicamento) {
        if (medicamento != null) {
            lblNombreComercial.setText(medicamento.getNombreComercial());
            lblNombreGenerico.setText(medicamento.getNombreGenerico());
            lblForma.setText(medicamento.getFormaFarmaceutica());
            lblFrecuencia.setText(medicamento.getFrecuenciaDuracion());
            txtIndicaciones.setText(medicamento.getIndicaciones());
            txtAdvertencias.setText(medicamento.getAdvertencias());
        } else {
            limpiarDetalles();
        }
    }
    
    /**
     * Limpia el panel de detalles
     */
    private void limpiarDetalles() {
        lblNombreComercial.setText("-");
        lblNombreGenerico.setText("-");
        lblForma.setText("-");
        lblFrecuencia.setText("-");
        txtIndicaciones.clear();
        txtAdvertencias.clear();
    }
    
    /**
     * Vuelve al menú principal (detecta automáticamente si es cliente o empleado)
     */
    @FXML
    private void volverMenu() {
        try {
            App.setRoot(vistaOrigen);
        } catch (IOException e) {
            System.err.println("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Configura el nombre del usuario en el label
     */
    public void setNombreUsuario(String nombre) {
        if (lblUsuario != null) {
            lblUsuario.setText("Usuario: " + nombre);
        }
    }
    
    /**
     * Configura desde qué vista se accedió a Consultar
     * @param origen "PersonaView" para cliente o "EmpleadoView" para empleado
     */
    public static void setVistaOrigen(String origen) {
        vistaOrigen = origen;
    }
}