/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.farmacia.data;

import com.mycompany.farmacia.model.Medicamento;
import com.mycompany.farmacia.model.Persona;
import com.mycompany.farmacia.model.Reserva;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para gestionar el almacenamiento de reservas
 * @author josue
 */
public class BDReserva {
    private static final String ARCHIVO_RESERVAS = "reservas.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Guarda una nueva reserva
     */
    public void guardarReserva(Reserva reserva) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_RESERVAS, true))) {
            String linea = reserva.getNumeroTicket() + "|" +
                          reserva.getPersona().getCedula() + "|" +
                          reserva.getPersona().getNombre() + "|" +
                          reserva.getMedicamento().getNombre() + "|" +
                          reserva.getMedicamento().getNombreComercial() + "|" +
                          reserva.getCantidad() + "|" +
                          reserva.isPrioridad() + "|" +
                          reserva.getFechaHora().format(formatter) + "|" +
                          reserva.getEstado();
            
            writer.write(linea);
            writer.newLine();
            writer.flush(); // ✅ AGREGADO: Forzar escritura
            System.out.println("Reserva guardada: " + reserva.getNumeroTicket());
            System.out.println("Línea guardada: " + linea); // ✅ AGREGADO: Ver qué se guardó
        } catch (IOException e) {
            System.err.println("Error al guardar reserva: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carga todas las reservas
     */
    public List<Reserva> cargarReservas() {
        List<Reserva> reservas = new ArrayList<>();
        File archivo = new File(ARCHIVO_RESERVAS);
        
        // ✅ AGREGADO: Debug del archivo
        System.out.println("=== DEBUG CARGAR RESERVAS ===");
        System.out.println("Ruta del archivo: " + archivo.getAbsolutePath());
        System.out.println("Archivo existe: " + archivo.exists());
        
        if (!archivo.exists()) {
            System.out.println("El archivo no existe, retornando lista vacía");
            return reservas;
        }
        
        System.out.println("Tamaño del archivo: " + archivo.length() + " bytes");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;
            
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                System.out.println("Leyendo línea " + numeroLinea + ": " + linea); // ✅ AGREGADO
                
                if (linea.trim().isEmpty()) {
                    System.out.println("  -> Línea vacía, omitida");
                    continue;
                }
                
                String[] datos = linea.split("\\|");
                System.out.println("  -> Datos separados: " + datos.length + " elementos"); // ✅ AGREGADO
                
                if (datos.length >= 9) {
                    try {
                        // Crear objetos necesarios
                        Persona persona = new Persona(datos[1], datos[2], "", ""); // nombre, cedula
                        
                        // Crear medicamento
                        Medicamento medicamento = new Medicamento(
                            datos[3], // nombre
                            datos[4], // nombreComercial
                            "",       // tipo
                            0,        // cantidadDisponible
                            false     // requiereOrden
                        );
                        
                        // Parsear datos
                        String numeroTicket = datos[0];
                        int cantidad = Integer.parseInt(datos[5]);
                        boolean prioridad = Boolean.parseBoolean(datos[6]);
                        LocalDateTime fechaHora = LocalDateTime.parse(datos[7], formatter);
                        String estado = datos[8];
                        
                        // Crear reserva
                        Reserva reserva = new Reserva(numeroTicket, persona, medicamento, 
                                                      cantidad, prioridad, fechaHora, estado);
                        reservas.add(reserva);
                        System.out.println("  -> ✅ Reserva agregada: " + numeroTicket);
                        
                    } catch (Exception e) {
                        System.err.println("  -> ❌ Error procesando línea " + numeroLinea + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("  -> ❌ Línea con formato incorrecto (menos de 9 elementos)");
                }
            }
            
            System.out.println("Total de reservas cargadas: " + reservas.size());
            System.out.println("=== FIN DEBUG ===");
            
        } catch (IOException e) {
            System.err.println("Error al cargar reservas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reservas;
    }
    
    /**
     * Carga las reservas de una persona específica por cédula
     */
    public List<Reserva> cargarReservasPorCedula(String cedula) {
        System.out.println("Buscando reservas para cédula: " + cedula);
        List<Reserva> todasLasReservas = cargarReservas();
        List<Reserva> reservasPersona = new ArrayList<>();
        
        for (Reserva reserva : todasLasReservas) {
            System.out.println("  Comparando: " + reserva.getPersona().getCedula() + " con " + cedula);
            if (reserva.getPersona().getCedula().equals(cedula)) {
                reservasPersona.add(reserva);
                System.out.println("  -> ✅ Match encontrado!");
            }
        }
        
        System.out.println("Reservas encontradas para " + cedula + ": " + reservasPersona.size());
        return reservasPersona;
    }
    
    /**
     * Actualiza el estado de una reserva
     */
    public boolean actualizarEstadoReserva(String numeroTicket, String nuevoEstado) {
        List<Reserva> reservas = cargarReservas();
        boolean actualizado = false;
        
        for (Reserva reserva : reservas) {
            if (reserva.getNumeroTicket().equals(numeroTicket)) {
                reserva.setEstado(nuevoEstado);
                actualizado = true;
                break;
            }
        }
        
        if (actualizado) {
            guardarTodasLasReservas(reservas);
        }
        
        return actualizado;
    }
    
    /**
     * Guarda todas las reservas (reescribe el archivo)
     */
    private void guardarTodasLasReservas(List<Reserva> reservas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_RESERVAS))) {
            for (Reserva reserva : reservas) {
                String linea = reserva.getNumeroTicket() + "|" +
                              reserva.getPersona().getCedula() + "|" +
                              reserva.getPersona().getNombre() + "|" +
                              reserva.getMedicamento().getNombre() + "|" +
                              reserva.getMedicamento().getNombreComercial() + "|" +
                              reserva.getCantidad() + "|" +
                              reserva.isPrioridad() + "|" +
                              reserva.getFechaHora().format(formatter) + "|" +
                              reserva.getEstado();
                
                writer.write(linea);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error al guardar reservas: " + e.getMessage());
        }
    }
}
