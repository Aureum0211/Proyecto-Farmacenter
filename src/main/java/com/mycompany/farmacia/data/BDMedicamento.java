/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.data;

/**
 *
 * @author josue
 */

import com.mycompany.farmacia.model.Medicamento;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BDMedicamento {

    private static final String ARCHIVO = "medicamentos.txt";

    public BDMedicamento() {
        crearArchivoSiNoExiste();
    }

    private void crearArchivoSiNoExiste() {
        File archivo = new File(ARCHIVO);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                System.out.println("Archivo medicamentos.txt creado.");
            }
        } catch (IOException e) {
            System.out.println("Error creando archivo medicamentos.txt: " + e.getMessage());
        }
    }

    // ------------------- GUARDAR UN SOLO MEDICAMENTO -------------------
    public void guardarMedicamento(Medicamento m) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write(m.getNombre() + ";" +
                     m.getTipo() + ";" +
                     m.getNombreComercial() + ";" +
                     m.getNombreGenerico() + ";" +
                     m.getDosis() + ";" +
                     m.getFormaFarmaceutica() + ";" +
                     m.getFrecuenciaDuracion() + ";" +
                     m.getIndicaciones() + ";" +
                     m.getAdvertencias() + ";" +
                     m.isRequiereOrden() + ";" +
                     m.getCantidadDisponible());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error guardando medicamento: " + e.getMessage());
        }
    }

    // ------------------- SOBRESCRIBIR LISTA DE MEDICAMENTOS -------------------
    public void guardarMedicamentos(List<Medicamento> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, false))) {
            for (Medicamento m : lista) {
                bw.write(m.getNombre() + ";" +
                         m.getTipo() + ";" +
                         m.getNombreComercial() + ";" +
                         m.getNombreGenerico() + ";" +
                         m.getDosis() + ";" +
                         m.getFormaFarmaceutica() + ";" +
                         m.getFrecuenciaDuracion() + ";" +
                         m.getIndicaciones() + ";" +
                         m.getAdvertencias() + ";" +
                         m.isRequiereOrden() + ";" +
                         m.getCantidadDisponible());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando lista de medicamentos: " + e.getMessage());
        }
    }

    // ------------------- LEER MEDICAMENTOS -------------------
    public List<Medicamento> cargarMedicamentos() {
        List<Medicamento> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 11) {  // Validar número de campos
                    String nombre = datos[0];
                    String tipo = datos[1];
                    String nombreComercial = datos[2];
                    String nombreGenerico = datos[3];
                    String dosis = datos[4];
                    String forma = datos[5];
                    String frecuencia = datos[6];
                    String indicaciones = datos[7];
                    String advertencias = datos[8];
                    boolean requiereOrden = Boolean.parseBoolean(datos[9]);
                    int cantidad = Integer.parseInt(datos[10]);

                    Medicamento m = new Medicamento(nombre, tipo, nombreComercial, nombreGenerico,
                                                    dosis, forma, frecuencia, indicaciones, advertencias,
                                                    requiereOrden, cantidad);
                    lista.add(m);
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando medicamentos: " + e.getMessage());
        }
        return lista;
    }
}


