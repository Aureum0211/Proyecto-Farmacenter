/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.service;

import com.mycompany.farmacia.model.Medicamento;
import com.mycompany.farmacia.model.Persona;
import com.mycompany.farmacia.data.BDMedicamento;
import java.util.List;

public class ServicioReserva {
    private BDMedicamento bdMedicamento;
    
    public ServicioReserva() {
        bdMedicamento = new BDMedicamento();
    }
    
    // ✅ SIMPLIFICADO: Solo actualiza el stock
    public boolean reservarMedicamento(Persona persona, String nombreMedicamento, int cantidad, boolean prioridad) {
        List<Medicamento> lista = bdMedicamento.cargarMedicamentos();
        for (Medicamento m : lista) {
            if (m.getNombre().equalsIgnoreCase(nombreMedicamento)) {
                if (m.getCantidadDisponible() >= cantidad) {
                    m.descontar(cantidad);
                    bdMedicamento.guardarMedicamentos(lista);
                    return true;
                } else {
                    System.out.println("Stock insuficiente.");
                    return false;
                }
            }
        }
        System.out.println("Medicamento no encontrado.");
        return false;
    }
}

