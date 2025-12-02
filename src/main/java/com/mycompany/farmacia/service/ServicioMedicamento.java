/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.service;

/**
 *
 * @author josue
 */

import com.mycompany.farmacia.model.Medicamento;
import com.mycompany.farmacia.data.BDMedicamento;
import java.util.List;
import java.util.ArrayList;

public class ServicioMedicamento {

    private BDMedicamento bdMedicamento;

    public ServicioMedicamento() {
        bdMedicamento = new BDMedicamento();
    }

    // Listar todos los medicamentos
    public List<Medicamento> listarMedicamentos() {
        return bdMedicamento.cargarMedicamentos();
    }

    // Buscar medicamento por nombre
    public Medicamento buscarPorNombre(String nombre) {
        for (Medicamento m : bdMedicamento.cargarMedicamentos()) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                return m;
            }
        }
        return null;
    }

    // Reservar medicamento (descontar stock y actualizar archivo)
    public boolean reservarMedicamento(String nombre, int cantidad) {
        List<Medicamento> lista = bdMedicamento.cargarMedicamentos();
        for (Medicamento m : lista) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                if (m.getCantidadDisponible() >= cantidad) {
                    m.descontar(cantidad);
                    bdMedicamento.guardarMedicamentos(lista); // guarda cambios
                    return true;
                }
            }
        }
        return false;
    }

    // Filtrar medicamentos por si requieren orden médica
    public List<Medicamento> filtrarPorOrden(boolean requiereOrden) {
        List<Medicamento> resultado = new ArrayList<>();
        for (Medicamento m : bdMedicamento.cargarMedicamentos()) {
            if (m.isRequiereOrden() == requiereOrden) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}


