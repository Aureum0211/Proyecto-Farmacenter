/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.farmacia.controller;

import com.mycompany.farmacia.App;
import com.mycompany.farmacia.data.BDMedicamento;
import com.mycompany.farmacia.model.Medicamento;
import com.mycompany.farmacia.service.ServicioMedicamento;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador para gestionar medicamentos (CRUD completo)
 * @author josue
 */
public class GestionarMedicamentosController {
    
    // Componentes de la tabla
    @FXML private TextField txtBuscar;
    @FXML private TableView<Medicamento> tablaMedicamentos;
    @FXML private TableColumn<Medicamento, String> colNombre;
    @FXML private TableColumn<Medicamento, String> colTipo;
    @FXML private TableColumn<Medicamento, String> colComercial;
    @FXML private TableColumn<Medicamento, Integer> colStock;
    @FXML private TableColumn<Medicamento, Boolean> colOrden;
    
    // Botones
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnGuardar;
    
    // Labels
    @FXML private Label lblEmpleado;
    @FXML private Label lblTituloForm;
    @FXML private Label lblMensaje;
    
    // Campos del formulario
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtNombreComercial;
    @FXML private TextField txtNombreGenerico;
    @FXML private TextField txtDosis;
    @FXML private TextField txtForma;
    @FXML private TextField txtFrecuencia;
    @FXML private TextArea txtIndicaciones;
    @FXML private TextArea txtAdvertencias;
    @FXML private CheckBox chkRequiereOrden;
    @FXML private Spinner<Integer> spnCantidad;
    
    // Servicios y datos
    private ServicioMedicamento servicioMedicamento;
    private BDMedicamento bdMedicamento;
    private ObservableList<Medicamento> listaMedicamentos;
    private ObservableList<Medicamento> listaFiltrada;
    private Medicamento medicamentoSeleccionado;
    private boolean modoEdicion = false;
    
    @FXML
    public void initialize() {
        servicioMedicamento = new ServicioMedicamento();
        bdMedicamento = new BDMedicamento();
        
        // Configurar columnas de la tabla
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colComercial.setCellValueFactory(new PropertyValueFactory<>("nombreComercial"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        colOrden.setCellValueFactory(new PropertyValueFactory<>("requiereOrden"));
        
        // Configurar spinner
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0);
        spnCantidad.setValueFactory(valueFactory);
        
        // Cargar medicamentos
        cargarMedicamentos();
        
        // Cargar tipos en el ComboBox
        cargarTipos();
        
        // Listener para la selección en la tabla
        tablaMedicamentos.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                medicamentoSeleccionado = newValue;
            }
        );
    }
    
    /**
     * Carga todos los medicamentos en la tabla
     */
    private void cargarMedicamentos() {
        List<Medicamento> medicamentos = servicioMedicamento.listarMedicamentos();
        listaMedicamentos = FXCollections.observableArrayList(medicamentos);
        listaFiltrada = FXCollections.observableArrayList(medicamentos);
        tablaMedicamentos.setItems(listaFiltrada);
    }
    
    /**
     * Carga tipos únicos en el ComboBox
     */
    private void cargarTipos() {
        List<String> tipos = List.of(
            "Analgésico", "Antibiótico", "Antiinflamatorio", "Antihipertensivo",
            "Antidiabético", "Antihistamínico", "Vitamina", "Suplemento"
        );
        cmbTipo.setItems(FXCollections.observableArrayList(tipos));
    }
    
    /**
     * Busca medicamentos en tiempo real
     */
    @FXML
    private void buscarMedicamento() {
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        
        if (busqueda.isEmpty()) {
            listaFiltrada.clear();
            listaFiltrada.addAll(listaMedicamentos);
        } else {
            List<Medicamento> resultado = listaMedicamentos.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(busqueda) ||
                           m.getNombreComercial().toLowerCase().contains(busqueda))
                .collect(Collectors.toList());
            
            listaFiltrada.clear();
            listaFiltrada.addAll(resultado);
        }
        
        tablaMedicamentos.setItems(listaFiltrada);
    }
    
    /**
     * Limpia la búsqueda
     */
    @FXML
    private void limpiarBusqueda() {
        txtBuscar.clear();
        listaFiltrada.clear();
        listaFiltrada.addAll(listaMedicamentos);
        tablaMedicamentos.setItems(listaFiltrada);
    }
    
    /**
     * Prepara el formulario para un nuevo medicamento
     */
    @FXML
    private void nuevoMedicamento() {
        modoEdicion = false;
        limpiarFormulario();
        lblTituloForm.setText("Agregar Nuevo Medicamento");
        btnGuardar.setText("💾 Guardar");
        mostrarMensaje("", "");
    }
    
    /**
     * Carga el medicamento seleccionado para editar
     */
    @FXML
    private void editarMedicamento() {
        if (medicamentoSeleccionado == null) {
            mostrarError("Seleccione un medicamento de la tabla para editar.");
            return;
        }
        
        modoEdicion = true;
        lblTituloForm.setText("Editar Medicamento");
        btnGuardar.setText("💾 Actualizar");
        
        // Cargar datos al formulario
        txtNombre.setText(medicamentoSeleccionado.getNombre());
        cmbTipo.setValue(medicamentoSeleccionado.getTipo());
        txtNombreComercial.setText(medicamentoSeleccionado.getNombreComercial());
        txtNombreGenerico.setText(medicamentoSeleccionado.getNombreGenerico());
        txtDosis.setText(medicamentoSeleccionado.getDosis());
        txtForma.setText(medicamentoSeleccionado.getFormaFarmaceutica());
        txtFrecuencia.setText(medicamentoSeleccionado.getFrecuenciaDuracion());
        txtIndicaciones.setText(medicamentoSeleccionado.getIndicaciones());
        txtAdvertencias.setText(medicamentoSeleccionado.getAdvertencias());
        chkRequiereOrden.setSelected(medicamentoSeleccionado.isRequiereOrden());
        spnCantidad.getValueFactory().setValue(medicamentoSeleccionado.getCantidadDisponible());
        
        mostrarMensaje("", "");
    }
    
    /**
     * Elimina el medicamento seleccionado
     */
    @FXML
    private void eliminarMedicamento() {
        if (medicamentoSeleccionado == null) {
            mostrarError("Seleccione un medicamento de la tabla para eliminar.");
            return;
        }
        
        // Confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este medicamento?");
        alert.setContentText("Medicamento: " + medicamentoSeleccionado.getNombre() + 
                           "\nEsta acción no se puede deshacer.");
        
        Optional<ButtonType> resultado = alert.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar de la lista
            listaMedicamentos.remove(medicamentoSeleccionado);
            listaFiltrada.remove(medicamentoSeleccionado);
            
            // Guardar cambios
            bdMedicamento.guardarMedicamentos(listaMedicamentos);
            
            mostrarMensaje("✓ Medicamento eliminado correctamente", "#5cb85c");
            limpiarFormulario();
            medicamentoSeleccionado = null;
        }
    }
    
    /**
     * Guarda o actualiza un medicamento
     */
    @FXML
    private void guardarMedicamento() {
        // Validar campos obligatorios
        if (!validarCampos()) {
            return;
        }
        
        // Crear objeto Medicamento
        Medicamento medicamento = new Medicamento(
            txtNombre.getText().trim(),
            cmbTipo.getValue(),
            txtNombreComercial.getText().trim(),
            txtNombreGenerico.getText().trim(),
            txtDosis.getText().trim(),
            txtForma.getText().trim(),
            txtFrecuencia.getText().trim(),
            txtIndicaciones.getText().trim(),
            txtAdvertencias.getText().trim(),
            chkRequiereOrden.isSelected(),
            spnCantidad.getValue()
        );
        
        if (modoEdicion) {
            // Actualizar existente
            int index = listaMedicamentos.indexOf(medicamentoSeleccionado);
            if (index >= 0) {
                listaMedicamentos.set(index, medicamento);
                mostrarMensaje("✓ Medicamento actualizado correctamente", "#5cb85c");
            }
        } else {
            // Agregar nuevo
            listaMedicamentos.add(medicamento);
            mostrarMensaje("✓ Medicamento agregado correctamente", "#5cb85c");
        }
        
        // Guardar en archivo
        bdMedicamento.guardarMedicamentos(listaMedicamentos);
        
        // Actualizar tabla
        listaFiltrada.clear();
        listaFiltrada.addAll(listaMedicamentos);
        tablaMedicamentos.refresh();
        
        // Limpiar formulario
        limpiarFormulario();
        modoEdicion = false;
        lblTituloForm.setText("Agregar Nuevo Medicamento");
        btnGuardar.setText("💾 Guardar");
    }
    
    /**
     * Valida que los campos obligatorios estén llenos
     */
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre del medicamento es obligatorio.");
            return false;
        }
        if (cmbTipo.getValue() == null || cmbTipo.getValue().isEmpty()) {
            mostrarError("El tipo de medicamento es obligatorio.");
            return false;
        }
        if (txtNombreComercial.getText().trim().isEmpty()) {
            mostrarError("El nombre comercial es obligatorio.");
            return false;
        }
        if (txtDosis.getText().trim().isEmpty()) {
            mostrarError("La dosis es obligatoria.");
            return false;
        }
        return true;
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        cmbTipo.setValue(null);
        txtNombreComercial.clear();
        txtNombreGenerico.clear();
        txtDosis.clear();
        txtForma.clear();
        txtFrecuencia.clear();
        txtIndicaciones.clear();
        txtAdvertencias.clear();
        chkRequiereOrden.setSelected(false);
        spnCantidad.getValueFactory().setValue(0);
        medicamentoSeleccionado = null;
        mostrarMensaje("", "");
    }
    
    /**
     * Muestra un mensaje temporal
     */
    private void mostrarMensaje(String mensaje, String color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: " + color + ";");
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validación");
        alert.setHeaderText("Campos incompletos");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Vuelve al menú empleado
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
}
