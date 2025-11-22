/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.observer;

// Observer concreto: Logger de eventos

import modelo.entidades.OrdenVeterinaria;

public class ObservadorLog implements IObservador {
    private String archivoLog;
    
    public ObservadorLog(String archivoLog) {
        this.archivoLog = archivoLog;
    }
    
    public ObservadorLog() {
        this("sistema.log");
    }
    
    @Override
    public void actualizar(OrdenVeterinaria orden, String evento) {
        String entrada = generarEntradaLog(orden, evento);
        escribirLog(entrada);
    }
    
    @Override
    public String getTipo() {
        return "Log";
    }
    
    private String generarEntradaLog(OrdenVeterinaria orden, String evento) {
        return String.format("[%s] Orden #%d - Evento: %s - Estado: %s",
            new java.util.Date(),
            orden.getIdOrden(),
            evento,
            orden.getEstado()
        );
    }
    
    private void escribirLog(String entrada) {
        System.out.println("[LOG -> " + archivoLog + "] " + entrada);
    }
}
